package ar.edu.utn.frba.dds.servicioEstadisticas.services.impl;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.ColeccionInputDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.HechoInputDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.*;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.repositories.ICategoriaRepository;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.repositories.IColeccionRepository;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.repositories.IEstadisticaHechosRepository;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.repositories.ISolicitudRepository;
import ar.edu.utn.frba.dds.servicioEstadisticas.services.IEstadisticaService;
import ar.edu.utn.frba.dds.servicioEstadisticas.services.IImportadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class EstadisticaService implements IEstadisticaService {
    private IImportadorService importadorService;

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
    public Integer cantSolicitudesSpam() {
        // TODO: Implementación pendiente - necesitaríamos más info sobre cómo detectar spam
        return 0;
    }

    @Override
    public List<EstadisticaHechos> calcularEstadisticas() {
        List<EstadisticaHechos> estadisticas = new ArrayList<>();

        List<ColeccionInputDTO> colecciones = this.importadorService.importarColecciones();
        List<HechoInputDTO> hechos = this.importadorService.importarHechos();

        HashMap<ClaveEstadistica, Integer> mapa = this.generarMapa(colecciones, hechos);

        mapa.forEach((claveEstadistica, cantidadHechos) -> {
            EstadisticaHechos estadistica = new EstadisticaHechos();

            estadistica.setColeccion(claveEstadistica.getColeccion());
            estadistica.setProvincia(claveEstadistica.getProvincia());
            estadistica.setCategoria(claveEstadistica.getCategoria());
            estadistica.setHora(claveEstadistica.getHoraDelDia());
            estadistica.setCantidad_hechos(cantidadHechos);

            EstadisticaHechos estadisticaGuardada = this.crearEstadistica(estadistica);
            estadisticas.add(estadisticaGuardada);
        });

        return estadisticas;
    }

    public HashMap<ClaveEstadistica, Integer> generarMapa(List<ColeccionInputDTO> colecciones, List<HechoInputDTO> hechos) {
        HashMap<ClaveEstadistica, Integer> mapaEstadisticas = new HashMap<>();

        if (colecciones != null) {
            for (ColeccionInputDTO coleccionDTO : colecciones) {
                if (coleccionDTO != null && coleccionDTO.getHechos() != null) {
                    Coleccion coleccion = coleccionDTO.convertirAColeccion();

                    for (HechoInputDTO hecho : coleccionDTO.getHechos()) {
                        Provincia provincia = hecho.getUbicacion().getProvincia();
                        Categoria categoria = hecho.convertirACategoria();
                        HoraDelDia horaDelDia = HoraDelDia.de(hecho.getFechaAcontecimiento());

                        ClaveEstadistica clave = new ClaveEstadistica(coleccion, provincia, categoria, horaDelDia);
                        System.out.println("🔍 Debug - Hecho: " + hecho.getTitulo());
                        System.out.println("   Clave: " + clave);
                        System.out.println("   Contador anterior: " + mapaEstadisticas.get(clave));
                        System.out.println("¿Claves estadísticas iguales?" + mapaEstadisticas.keySet());
                        mapaEstadisticas.merge(clave, 1, Integer::sum);
                        System.out.println("   Contador nuevo: " + mapaEstadisticas.get(clave));
                    }
                }
            }
        }

        if (hechos != null) {
            for (HechoInputDTO hecho : hechos) {
                Coleccion coleccion = null;
                Provincia provincia = hecho.getUbicacion().getProvincia();
                Categoria categoria = hecho.convertirACategoria();
                HoraDelDia horaDelDia = HoraDelDia.de(hecho.getFechaAcontecimiento());
                ClaveEstadistica clave = new ClaveEstadistica(coleccion, provincia, categoria, horaDelDia);
                mapaEstadisticas.merge(clave, 1, Integer::sum);
            }
        }

        return mapaEstadisticas;
    }

    @Override
    public EstadisticaHechos crearEstadistica(EstadisticaHechos estadistica) {
        Categoria categoriaGuardada = this.findOrCreate(estadistica.getCategoria());
        Coleccion coleccionGuardada = this.findOrCreate(estadistica.getColeccion());

        estadistica.setCategoria(categoriaGuardada);
        estadistica.setColeccion(coleccionGuardada);

        EstadisticaHechos estadisticaGuardada = this.estadisticaRepository
                .findByAllFieldsExceptId(estadistica)
                .orElse(estadistica);

        return this.estadisticaRepository.save(estadisticaGuardada);
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

    private Solicitud findOrCreate(Solicitud solicitud) {
        return solicitudRepository.save(solicitud);
    }
}