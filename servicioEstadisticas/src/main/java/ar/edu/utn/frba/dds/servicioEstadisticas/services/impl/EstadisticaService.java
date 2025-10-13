package ar.edu.utn.frba.dds.servicioAutenticacion.services.impl;

import ar.edu.utn.frba.dds.servicioAutenticacion.domain.dtos.ColeccionInputDTO;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.dtos.HechoInputDTO;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.dtos.SolicitudEliminacionInputDTO;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.*;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.dimensiones.Categoria;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.dimensiones.Coleccion;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.dimensiones.HoraDelDia;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.dimensiones.Provincia;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.trazabilidad.EstadisticaCategoria;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.trazabilidad.EstadisticaHoraXCategoria;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.trazabilidad.EstadisticaProvinciaXCategoria;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.trazabilidad.EstadisticaProvinciaXColeccion;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.trazabilidad.EstadisticaSolicitudes;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.utils.ClaveEstadistica;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.utils.EstadisticaCombinacion;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.repositories.*;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.repositories.estadisticasTrazabilidad.IEstadisticaCategoriaRepository;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.repositories.estadisticasTrazabilidad.IEstadisticaHoraXCategoriaRepository;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.repositories.estadisticasTrazabilidad.IEstadisticaProvinciaXCategoriaRepository;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.repositories.estadisticasTrazabilidad.IEstadisticaProvinciaXColeccionRepository;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.repositories.estadisticasTrazabilidad.IEstadisticaSolicitudesRepository;
import ar.edu.utn.frba.dds.servicioAutenticacion.services.IEstadisticaService;
import ar.edu.utn.frba.dds.servicioAutenticacion.services.IEstadisticasCsvService;
import ar.edu.utn.frba.dds.servicioAutenticacion.services.IImportadorService;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EstadisticaService implements IEstadisticaService {
    private IImportadorService importadorService;
    private IEstadisticasCsvService estadisticasCsvService;

    // Caché para evitar duplicar hechos durante una misma ejecución
    private Map<Long, Hecho> cacheHechos = new HashMap<>();

    public EstadisticaService(IImportadorService importadorService, IEstadisticasCsvService estadisticasCsvService) {
        this.importadorService = importadorService;
        this.estadisticasCsvService = estadisticasCsvService;
    }

    @Autowired
    private IEstadisticaCombinacionRepository estadisticaRepository;

    @Autowired
    private IHechoRepository hechoRepository;

    @Autowired
    private IColeccionRepository coleccionRepository;

    @Autowired
    private ICategoriaRepository categoriaRepository;

    @Autowired
    private IEstadisticaProvinciaXColeccionRepository estadisticaProvinciaXColeccionRepository;

    @Autowired
    private IEstadisticaCategoriaRepository estadisticaCategoriaRepository;

    @Autowired
    private IEstadisticaProvinciaXCategoriaRepository estadisticaProvinciaXCategoriaRepository;

    @Autowired
    private IEstadisticaHoraXCategoriaRepository estadisticaHoraXCategoriaRepository;

    @Autowired
    private IEstadisticaSolicitudesRepository estadisticaSolicitudesRepository;

    @Autowired
    private ISolicitudRepository solicitudRepository;

    public EstadisticaProvinciaXColeccion provinciaConMasHechosDeUnaColeccion(Long idColeccion) {
        Coleccion coleccion = coleccionRepository.findById(idColeccion)
            .orElseThrow(() -> new RuntimeException("Colección no encontrada"));

        // Obtener los resultados del repository como Object[]
        List<Object[]> resultados = estadisticaRepository.findProvinciaConHechosPorColeccion(idColeccion);

        // Convertir los resultados a un mapa
        Map<Provincia, Long> provinciasConHechos = new LinkedHashMap<>();
        for (Object[] resultado : resultados) {
            Provincia provincia = (Provincia) resultado[0];
            Long cantidad = ((Number) resultado[1]).longValue();
            provinciasConHechos.put(provincia, cantidad);
        }

        EstadisticaProvinciaXColeccion estadistica = new EstadisticaProvinciaXColeccion(
            provinciasConHechos,
            LocalDateTime.now(),
            coleccion
        );

        return this.estadisticaProvinciaXColeccionRepository.save(estadistica);
    }

    public EstadisticaCategoria categoriaConMasHechos() {
        List<Object[]> resultados = estadisticaRepository.findCategoriasConHechos();

        // Convertir los resultados a un mapa (limitado a 30)
        Map<Categoria, Long> categoriasConHechos = new LinkedHashMap<>();
        for (Object[] resultado : resultados) {
            if (categoriasConHechos.size() >= 30) {
                break; // Limitar a máximo 30 categorías
            }
            Categoria categoria = (Categoria) resultado[0];
            Long cantidad = ((Number) resultado[1]).longValue();
            categoriasConHechos.put(categoria, cantidad);
        }

        EstadisticaCategoria estadisticaCategoria = new EstadisticaCategoria(categoriasConHechos, LocalDateTime.now());
        return this.estadisticaCategoriaRepository.save(estadisticaCategoria);
    }

    public EstadisticaProvinciaXCategoria provinciaConMasHechosSegunCategoria(Long idCategoria) {
        Categoria categoria = categoriaRepository.findById(idCategoria)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        List<Object[]> resultados = estadisticaRepository.findProvinciasConHechosPorCategoria(idCategoria);

        // Convertir los resultados a un mapa
        Map<Provincia, Long> provinciasConHechos = new LinkedHashMap<>();
        for (Object[] resultado : resultados) {
            Provincia provincia = (Provincia) resultado[0];
            Long cantidad = ((Number) resultado[1]).longValue();
            provinciasConHechos.put(provincia, cantidad);
        }

        // Crear y guardar la estadística
        EstadisticaProvinciaXCategoria estadisticaProvinciaXCategoria = new EstadisticaProvinciaXCategoria(
            categoria,
            LocalDateTime.now(),
            provinciasConHechos
        );

        return this.estadisticaProvinciaXCategoriaRepository.save(estadisticaProvinciaXCategoria);
    }

    public EstadisticaHoraXCategoria horaConMasHechosSegunCategoria(Long idCategoria) {
        Categoria categoria = categoriaRepository.findById(idCategoria)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        List<Object[]> resultados = estadisticaRepository.findHorasConHechosPorCategoria(idCategoria);

        // Convertir los resultados a un mapa
        Map<HoraDelDia, Long> horasConHechos = new LinkedHashMap<>();
        for (Object[] resultado : resultados) {
            HoraDelDia hora = (HoraDelDia) resultado[0];
            Long cantidad = ((Number) resultado[1]).longValue();
            horasConHechos.put(hora, cantidad);
        }

        // Crear y guardar la estadística
        EstadisticaHoraXCategoria estadisticaHoraXCategoria = new EstadisticaHoraXCategoria(
            categoria,
            LocalDateTime.now(),
            horasConHechos
        );

        return this.estadisticaHoraXCategoriaRepository.save(estadisticaHoraXCategoria);
    }

    public EstadisticaSolicitudes cantSolicitudesSpam() {
        Long cantSpam = solicitudRepository.countByEstado(EstadoSolicitudEliminacion.SPAM);
        Long cantAceptada = solicitudRepository.countByEstado(EstadoSolicitudEliminacion.ACEPTADA);
        Long cantRechazada = solicitudRepository.countByEstado(EstadoSolicitudEliminacion.RECHAZADA);
        Long cantPendiente = solicitudRepository.countByEstado(EstadoSolicitudEliminacion.PENDIENTE);

        Long cantNoSpam = cantAceptada + cantRechazada + cantPendiente;

        EstadisticaSolicitudes estadisticaSolicitudes = new EstadisticaSolicitudes(
            LocalDateTime.now(),
            cantSpam,
            cantNoSpam
        );

        return this.estadisticaSolicitudesRepository.save(estadisticaSolicitudes);
    }

    @Override
    @Transactional
    public List<EstadisticaCombinacion> calcularEstadisticas() {
        // Limpiar caché al inicio de cada cálculo
        this.limpiarDatosAnteriores();
        this.cacheHechos.clear();

        List<EstadisticaCombinacion> estadisticas = new ArrayList<>();

        List<ColeccionInputDTO> colecciones = this.importadorService.importarColecciones();
        List<HechoInputDTO> hechos = this.importadorService.importarHechos();
        List<SolicitudEliminacionInputDTO> solicitudes = this.importadorService.importarSolicitudes();

        HashMap<ClaveEstadistica, List<Hecho>> mapa = this.generarMapa(colecciones, hechos);

        mapa.forEach((claveEstadistica, hechosXEstadistica) -> {
            EstadisticaCombinacion estadistica = new EstadisticaCombinacion();
            estadistica.setColeccion(claveEstadistica.getColeccion());
            estadistica.setProvincia(claveEstadistica.getProvincia());
            estadistica.setCategoria(claveEstadistica.getCategoria());
            estadistica.setHora(claveEstadistica.getHoraDelDia());
            estadistica.setHechos(hechosXEstadistica);

            EstadisticaCombinacion estadisticaGuardada = this.crearEstadistica(estadistica);
            estadisticas.add(estadisticaGuardada);
        });

        this.guardarSolicitudes(solicitudes);

        // Limpiar caché al final
        this.cacheHechos.clear();
        this.persistirTrazabilidad();
        return estadisticas;
    }

    @Override
    public void persistirEnCSV(){
        this.exportarCSV();
    }

    public HashMap<ClaveEstadistica, List<Hecho>> generarMapa(List<ColeccionInputDTO> colecciones, List<HechoInputDTO> hechos) {
        HashMap<ClaveEstadistica, List<Hecho>> mapaEstadisticas = new HashMap<>();

        if (colecciones != null) {
            for (ColeccionInputDTO coleccionDTO : colecciones) {
                if (coleccionDTO != null && coleccionDTO.getHechos() != null) {
                    Coleccion coleccion = coleccionDTO.convertirAColeccion();

                    for (HechoInputDTO hechoDTO : coleccionDTO.getHechos()) {
                        Provincia provincia = hechoDTO.getUbicacion().getProvincia();
                        Categoria categoria = hechoDTO.convertirACategoria();
                        HoraDelDia horaDelDia = HoraDelDia.de(hechoDTO.getFechaAcontecimiento());
                        Hecho hecho = this.findOrCreateHechoFromDTO(hechoDTO);

                        ClaveEstadistica clave = new ClaveEstadistica(coleccion, provincia, categoria, horaDelDia);

                        // Crear lista si no existe, luego agregar el hecho
                        mapaEstadisticas.computeIfAbsent(clave, k -> new ArrayList<>()).add(hecho);
                    }
                }
            }
        }

        if (hechos != null) {
            for (HechoInputDTO hechoInputDTO : hechos) {
                Coleccion coleccion = null;
                Provincia provincia = hechoInputDTO.getUbicacion().getProvincia();
                Categoria categoria = hechoInputDTO.convertirACategoria();
                HoraDelDia horaDelDia = HoraDelDia.de(hechoInputDTO.getFechaAcontecimiento());
                Hecho hecho = this.findOrCreateHechoFromDTO(hechoInputDTO);

                ClaveEstadistica clave = new ClaveEstadistica(coleccion, provincia, categoria, horaDelDia);

                // Crear lista si no existe, luego agregar el hecho
                mapaEstadisticas.computeIfAbsent(clave, k -> new ArrayList<>()).add(hecho);
            }
        }

        return mapaEstadisticas;
    }

    private void limpiarDatosAnteriores() {
        this.estadisticaRepository.deleteAll();
    }

    @Override
    @Transactional
    public EstadisticaCombinacion crearEstadistica(EstadisticaCombinacion estadistica) {
        Categoria categoriaGuardada = this.findOrCreate(estadistica.getCategoria());
        Coleccion coleccionGuardada = this.findOrCreate(estadistica.getColeccion());

        // Los hechos ya están procesados y guardados desde generarMapa()
        // No es necesario volver a procesarlos aquí
        estadistica.setCategoria(categoriaGuardada);
        estadistica.setColeccion(coleccionGuardada);

        EstadisticaCombinacion estadisticaGuardada = this.estadisticaRepository
                .findByAllFieldsExceptId(estadistica)
                .orElse(estadistica);

        // Si la estadística ya existe, agregar los hechos nuevos a la lista existente
        if (estadisticaGuardada.getId() != null && estadistica.getHechos() != null) {
            // Evitar duplicados - solo agregar hechos que no estén ya en la lista
            List<Long> hechosExistentesIds = estadisticaGuardada.getHechos().stream()
                    .map(Hecho::getId)
                    .collect(Collectors.toList());

            List<Hecho> hechosNuevos = estadistica.getHechos().stream()
                    .filter(hecho -> !hechosExistentesIds.contains(hecho.getId()))
                    .collect(Collectors.toList());

            estadisticaGuardada.getHechos().addAll(hechosNuevos);
        }

        return this.estadisticaRepository.save(estadisticaGuardada);
    }

    /**
     * Método específico para buscar o crear hechos desde DTOs
     * Utiliza el ID del DTO como referencia principal y caché para evitar duplicados
     */
    private Hecho findOrCreateHechoFromDTO(HechoInputDTO hechoDTO) {
        if (hechoDTO == null || hechoDTO.getId() == null) {
            return null;
        }

        // Verificar caché primero
        if (this.cacheHechos.containsKey(hechoDTO.getId())) {
            return this.cacheHechos.get(hechoDTO.getId());
        }

        // Buscar en base de datos por ID del DTO
        Optional<Hecho> hechoExistente = hechoRepository.findById(hechoDTO.getId());

        Hecho hecho;
        if (hechoExistente.isPresent()) {
            hecho = hechoExistente.get();
        } else {
            // Si no existe, convertir DTO y guardar
            hecho = hechoDTO.convertirAHecho();
            hecho = hechoRepository.save(hecho);
        }

        // Agregar al caché
        this.cacheHechos.put(hechoDTO.getId(), hecho);

        return hecho;
    }

    private Coleccion findOrCreate(Coleccion coleccion) {
        if (coleccion == null) {
            return null; // Para hechos sin colección
        }

        return coleccionRepository.findByDetalle(coleccion.getDetalle())
                .orElseGet(() -> coleccionRepository.save(coleccion));
    }

    private Categoria findOrCreate(Categoria categoria) {
        if (categoria == null) {
            return null;
        }

        return categoriaRepository.findByDetalle(categoria.getDetalle())
                .orElseGet(() -> categoriaRepository.save(categoria));
    }

    /**
     * Método para buscar o crear hechos que ya están convertidos desde entidades
     * Mantenido para compatibilidad con otros métodos
     */
    private Hecho findOrCreate(Hecho hecho) {
        if (hecho == null) {
            return null;
        }

        // Si ya tiene ID y está en el caché, devolverlo
        if (hecho.getId() != null && this.cacheHechos.containsKey(hecho.getId())) {
            return this.cacheHechos.get(hecho.getId());
        }

        if (hecho.getId() != null) {
            Optional<Hecho> hechoExistente = hechoRepository.findById(hecho.getId());
            if (hechoExistente.isPresent()) {
                Hecho hechoEncontrado = hechoExistente.get();
                this.cacheHechos.put(hecho.getId(), hechoEncontrado);
                return hechoEncontrado;
            }
        }

        // Si no existe, guardarlo
        Hecho hechoGuardado = hechoRepository.save(hecho);
        if (hechoGuardado.getId() != null) {
            this.cacheHechos.put(hechoGuardado.getId(), hechoGuardado);
        }

        return hechoGuardado;
    }

    private void guardarSolicitudes(List<SolicitudEliminacionInputDTO> solicitudes) {
        solicitudes.forEach(solicitudEliminacion -> {
            // Usar el método específico para DTOs y eliminar la doble llamada
            Hecho hechoGuardado = this.findOrCreateHechoFromDTO(solicitudEliminacion.getHecho());
            this.solicitudRepository.save(solicitudEliminacion.convertirASolicitud(hechoGuardado));
        });
    }

    private SolicitudEliminacion findOrCreate(SolicitudEliminacion solicitud) {
        return solicitudRepository.save(solicitud);
    }

    private void persistirTrazabilidad() {
        this.coleccionRepository.findAll().forEach(coleccion -> {
            this.provinciaConMasHechosDeUnaColeccion(coleccion.getId());
        });
        this.categoriaConMasHechos();
        this.categoriaRepository.findAll().forEach(categoria -> {
            this.provinciaConMasHechosSegunCategoria(categoria.getId());
            this.horaConMasHechosSegunCategoria(categoria.getId());
        });
        this.cantSolicitudesSpam();
    }

    private void exportarCSV() {
        List<EstadisticaCategoria> estadisticasCategorias = this.estadisticaCategoriaRepository.findAll();
        List<EstadisticaHoraXCategoria> estadisticasHoraXCategoria = this.estadisticaHoraXCategoriaRepository.findAll();
        List<EstadisticaProvinciaXCategoria> estadisticasProvinciaXCategoria = this.estadisticaProvinciaXCategoriaRepository.findAll();
        List<EstadisticaProvinciaXColeccion> estadisticasProvinciaXColeccion = this.estadisticaProvinciaXColeccionRepository.findAll();
        List<EstadisticaSolicitudes> estadisticasSolicitudes = this.estadisticaSolicitudesRepository.findAll();

        Path directorioActual = Paths.get("").toAbsolutePath();
        Path directorioPublic = directorioActual.resolve("public").resolve("estadisticas");

        try {
            if (!Files.exists(directorioPublic)) {
                Files.createDirectories(directorioPublic);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.estadisticasCsvService.exportarEstadisticas(
            estadisticasCategorias,
            estadisticasHoraXCategoria,
            estadisticasProvinciaXCategoria,
            estadisticasProvinciaXColeccion,
            estadisticasSolicitudes,
            directorioPublic.toString()
        );
    }
}