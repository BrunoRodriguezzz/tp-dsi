package ar.edu.utn.frba.dds.servicioEstadisticas.services.impl;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.ColeccionInputDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.HechoInputDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.SolicitudEliminacionInputDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.*;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.repositories.*;
import ar.edu.utn.frba.dds.servicioEstadisticas.services.IEstadisticaService;
import ar.edu.utn.frba.dds.servicioEstadisticas.services.IImportadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EstadisticaService implements IEstadisticaService {
    private IImportadorService importadorService;

    // Caché para evitar duplicar hechos durante una misma ejecución
    private Map<Long, Hecho> cacheHechos = new HashMap<>();

    public EstadisticaService(IImportadorService importadorService) {
        this.importadorService = importadorService;
    }

    @Autowired
    private IEstadisticaHechosRepository estadisticaRepository;

    @Autowired
    private ICategoriaRepository categoriaRepository;

    @Autowired
    private IColeccionRepository coleccionRepository;

    @Autowired
    private ISolicitudRepository solicitudRepository;

    @Autowired
    private IHechoRepository hechoRepository;

    @Override
    public Provincia provinciaConMasHechosDeUnaColeccion(Long idColeccion) {
        return estadisticaRepository.findProvinciaConMasHechosPorColeccion(idColeccion);
    }

    @Override
    public Categoria categoriaConMasHechos() {
        return estadisticaRepository.findCategoriaConMasHechos();
    }

    @Override
    public Provincia provinciaConMasHechosSegunCategoria(Long idCategoria) {
        return estadisticaRepository.findProvinciaConMasHechosPorCategoria(idCategoria);
    }

    @Override
    public LocalTime horaConMasHechosSegunCategoria(Long idCategoria) {
        HoraDelDia horaDelDia = estadisticaRepository.findHoraConMasHechosPorCategoria(idCategoria);
        return horaDelDia != null ? horaDelDia.getHora() : null;
    }

    @Override
    public Long cantSolicitudesSpam() {
        return this.solicitudRepository.countByEstado(EstadoSolicitudEliminacion.SPAM);
    }

    @Override
    @Transactional
    public List<EstadisticaHechos> calcularEstadisticas() {
        // Limpiar caché al inicio de cada cálculo
        this.cacheHechos.clear();

        List<EstadisticaHechos> estadisticas = new ArrayList<>();

        List<ColeccionInputDTO> colecciones = this.importadorService.importarColecciones();
        List<HechoInputDTO> hechos = this.importadorService.importarHechos();
        List<SolicitudEliminacionInputDTO> solicitudes = this.importadorService.importarSolicitudes();

        HashMap<ClaveEstadistica, List<Hecho>> mapa = this.generarMapa(colecciones, hechos);

        mapa.forEach((claveEstadistica, hechosXEstadistica) -> {
            EstadisticaHechos estadistica = new EstadisticaHechos();

            estadistica.setColeccion(claveEstadistica.getColeccion());
            estadistica.setProvincia(claveEstadistica.getProvincia());
            estadistica.setCategoria(claveEstadistica.getCategoria());
            estadistica.setHora(claveEstadistica.getHoraDelDia());
            estadistica.setHechos(hechosXEstadistica);

            EstadisticaHechos estadisticaGuardada = this.crearEstadistica(estadistica);
            estadisticas.add(estadisticaGuardada);
        });

        this.guardarSolicitudes(solicitudes);

        // Limpiar caché al final
        this.cacheHechos.clear();

        return estadisticas;
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

    @Override
    @Transactional
    public EstadisticaHechos crearEstadistica(EstadisticaHechos estadistica) {
        Categoria categoriaGuardada = this.findOrCreate(estadistica.getCategoria());
        Coleccion coleccionGuardada = this.findOrCreate(estadistica.getColeccion());

        // Los hechos ya están procesados y guardados desde generarMapa()
        // No es necesario volver a procesarlos aquí
        estadistica.setCategoria(categoriaGuardada);
        estadistica.setColeccion(coleccionGuardada);

        EstadisticaHechos estadisticaGuardada = this.estadisticaRepository
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
}