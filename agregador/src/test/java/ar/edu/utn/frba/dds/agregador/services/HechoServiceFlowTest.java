package ar.edu.utn.frba.dds.agregador.services;

import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Categoria;
import ar.edu.utn.frba.dds.agregador.models.repositories.ICategoriaRepository;
import ar.edu.utn.frba.dds.agregador.models.repositories.IFuenteRepository;
import ar.edu.utn.frba.dds.agregador.models.repositories.IHechoRepository;
import ar.edu.utn.frba.dds.agregador.services.impl.HechoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

public class HechoServiceFlowTest {

    @Mock
    private IFuenteRepository fuenteRepository;

    @Mock
    private IHechoRepository hechoRepository;

    @Mock
    private ICategoriaRepository categoriaRepository;

    @Mock
    private INormalizadorService normalizadorService;

    @Mock
    private IUbicacionService ubicacionService;

    @InjectMocks
    private HechoService hechoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGuardarHechosFlow() {
        // Arrange
        int count = 10;
        List<Hecho> hechos = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Hecho h = new Hecho();
            h.setId((long) i);
            h.setFechaAcontecimiento(LocalDateTime.now());
            Categoria c = new Categoria();
            c.setTitulo("Test");
            h.setCategoria(c);
            h.setEsNuevoOModificado(true);
            hechos.add(h);
        }

        Flux<Hecho> inputFlux = Flux.fromIterable(hechos);

        // Mocks
        when(normalizadorService.normalizarHechosReactivo(any(Flux.class)))
                .thenAnswer(invocation -> invocation.getArgument(0)); // Pass through

        when(ubicacionService.obtenerUbicacionesReactivo(any(Flux.class)))
                .thenAnswer(invocation -> invocation.getArgument(0)); // Pass through

        when(hechoRepository.saveAll(anyList())).thenAnswer(invocation -> {
            List<Hecho> list = invocation.getArgument(0);
            return list;
        });

        // Act
        Flux<Hecho> resultFlux = hechoService.guardarHechos(inputFlux);

        // Assert
        StepVerifier.create(resultFlux)
                .expectNextCount(count)
                .verifyComplete();

        // Verify interactions
        verify(normalizadorService).normalizarHechosReactivo(any(Flux.class));
        verify(ubicacionService).obtenerUbicacionesReactivo(any(Flux.class));
        verify(hechoRepository, atLeastOnce()).saveAll(anyList());
    }

    @Test
    public void testGuardarHechosHighVolume() {
        // Arrange
        int count = 10000;
        List<Hecho> hechos = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Hecho h = new Hecho();
            h.setId((long) i);
            h.setFechaAcontecimiento(LocalDateTime.now());
            Categoria c = new Categoria();
            c.setTitulo("Test " + i);
            h.setCategoria(c);
            h.setEsNuevoOModificado(true);
            hechos.add(h);
        }

        Flux<Hecho> inputFlux = Flux.fromIterable(hechos);

        // Mocks
        when(normalizadorService.normalizarHechosReactivo(any(Flux.class)))
                .thenAnswer(invocation -> invocation.getArgument(0)); // Pass through

        when(ubicacionService.obtenerUbicacionesReactivo(any(Flux.class)))
                .thenAnswer(invocation -> invocation.getArgument(0)); // Pass through

        when(hechoRepository.saveAll(anyList())).thenAnswer(invocation -> {
            List<Hecho> list = invocation.getArgument(0);
            return list;
        });

        // Act
        long start = System.currentTimeMillis();
        Flux<Hecho> resultFlux = hechoService.guardarHechos(inputFlux);

        // Assert
        StepVerifier.create(resultFlux)
                .expectNextCount(count)
                .verifyComplete();
        
        long end = System.currentTimeMillis();
        System.out.println("High volume test took: " + (end - start) + "ms");

        // Verify interactions
        verify(normalizadorService).normalizarHechosReactivo(any(Flux.class));
        verify(ubicacionService).obtenerUbicacionesReactivo(any(Flux.class));
        // Should be called roughly count / 100 times
        verify(hechoRepository, atLeast(count / 100)).saveAll(anyList());
    }
}
