package ar.edu.utn.frba.dds.agregador.models.domain.test;

import ar.edu.utn.frba.dds.fuenteProxy.Services.impl.ColeccionService;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.Fuente;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.TipoFuente;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.impl.InstanciaMetaMapa;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.input.InputColeccionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import reactor.core.publisher.Flux;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InstanciaMetaMapaIntegrationTest {
    @Mock
    private TipoFuente instanciaMetaMapa;
    private Fuente fuente;

    @InjectMocks
    private ColeccionService coleccionService;

    @BeforeEach
    public void setUp() {
        fuente = new Fuente(instanciaMetaMapa, "GIGAMAPA", "http://dummy");
        fuente.setId(42L);
    }

    @Test
    @DisplayName("Obtener ")
    void getAllColecciones_deberiaRetornarLosDTOsMockeados() {
        // 1) Preparamos el Flux que queremos “recibir” de la API externa
        InputColeccionDTO dto1 = new InputColeccionDTO(1L, "T1", "D1");
        InputColeccionDTO dto2 = new InputColeccionDTO(2L, "T2", "D2");
        Mockito.when(responseSpec.bodyToFlux(InputColeccionDTO.class))
                .thenReturn(Flux.just(dto1, dto2));

        // 2) Llamamos al método
        Flux<InputColeccionDTO> resultado = instanciaMetaMapa.getAllColecciones();

        // 3) Verificamos con StepVerifier
        StepVerifier.create(resultado)
                .expectNext(dto1, dto2)
                .verifyComplete();

        // 4) También podemos verificar que se llamó exactamente UNA vez a retrieve()
        Mockito.verify(headersSpec, Mockito.times(1)).retrieve();
    }
}
