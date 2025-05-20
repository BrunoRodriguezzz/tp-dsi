package ar.edu.utn.frba.dds.domain;

import ar.edu.utn.frba.dds.domain.models.entities.fuentes.Fuente;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Coleccion;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import ar.edu.utn.frba.dds.domain.models.entities.solicitudEliminacion.EstadoSolicitudEliminacion;
import ar.edu.utn.frba.dds.domain.models.entities.solicitudEliminacion.ResolucionSolicitudEliminacion;
import ar.edu.utn.frba.dds.domain.models.entities.solicitudEliminacion.SolicitudEliminacion;
import ar.edu.utn.frba.dds.domain.models.entities.usuarios.Administrador;
import ar.edu.utn.frba.dds.domain.models.entities.usuarios.Contribuyente;
import ar.edu.utn.frba.dds.domain.models.entities.utils.Errores.ER_ValueObjects.DescripcionInvalidaException;
import ar.edu.utn.frba.dds.domain.models.entities.utils.Errores.ER_ValueObjects.FechaInvalidaException;
import ar.edu.utn.frba.dds.domain.models.entities.utils.Errores.ER_ValueObjects.TituloInvalidoException;
import ar.edu.utn.frba.dds.domain.models.entities.utils.Errores.ER_ValueObjects.UbicacionInvalidaException;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Categoria;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Origen;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Ubicacion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestSolicitudEliminacion {
    // ------------------------------------------------ Instanciacion de Ubicaciones ------------------------------------------------
    Ubicacion ubicacion;

    // ------------------------------------------------ Instanciacion de Categorias ------------------------------------------------
    Categoria categoria;

    // ------------------------------------------------ Instanciacion de Hechos ------------------------------------------------
    Hecho hecho = new Hecho(
        "Brote de enfermedad contagiosa causa estragos en San Lorenzo, Santa Fe",
        "Grave brote de enfermedad contagiosa ocurrió en las inmediaciones de San Lorenzo, Santa Fe. El incidente dejó varios heridos y daños materiales. Se ha declarado estado de emergencia en la región para facilitar la asistencia.",
        categoria,
        ubicacion,
        LocalDate.parse("2005-07-05"),
        Origen.MANUAL
    );

    // ------------------------------------------------ Clases Auxiliares ------------------------------------------------
    Coleccion coleccion;
    Fuente fuenteMockeada = mock(Fuente.class);
    SolicitudEliminacion solicitudEliminacion;
    Contribuyente contribuyente;
    Administrador administrador;

    public TestSolicitudEliminacion() throws DescripcionInvalidaException, TituloInvalidoException, FechaInvalidaException {
    }

    @BeforeEach
    public void setUp() {
        try {
            ubicacion = new Ubicacion("-32.786098", "-60.741543");
            categoria = new Categoria("Desastre tecnológico");
        } catch (UbicacionInvalidaException e) {
            fail("No debería fallar para coordenadas válidas");
        } catch (Exception e){
            fail("No debería fallar por: " + e.getMessage());
        }

        coleccion = new Coleccion("Coleccion1", "Una coleccion");
        when(fuenteMockeada.importarHechos()).thenReturn(Arrays.asList(hecho));

        try{
            contribuyente = new Contribuyente("Anonimo","Anonimo", LocalDate.now());
        }
        catch (FechaInvalidaException e){
            fail("No debería fallar por: " + e.getMessage());
        }
        administrador = new Administrador("Anonimo","Anonimo");
    }


    @Test
    @DisplayName("Solicitud Rechazada 1 día después de su creación.")
    public void rechazarSolicitud(){
        String fundamento = "a".repeat(500) + " Esto esta bien fundado";

        try {
            solicitudEliminacion = new SolicitudEliminacion(hecho, fundamento, contribuyente, LocalDateTime.now());
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        // Al crear una solicitud queda en estado pendiente
        Assertions.assertEquals(EstadoSolicitudEliminacion.PENDIENTE,solicitudEliminacion.getEstadoSolicitudEliminacion());

        // Rechazar esta solicitud un día después de su creación.
        try {
            solicitudEliminacion.serRechazada(administrador);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        ResolucionSolicitudEliminacion resolucionSolicitudEliminacion = new ResolucionSolicitudEliminacion(administrador, LocalDateTime.now().plusDays(1));
        solicitudEliminacion.setResolucionSolicitudEliminacion(resolucionSolicitudEliminacion);

        // Dado que fue rechazada, el hecho puede ser agregado a cualquier colección.
        coleccion = new Coleccion("Colección sin criterio", "Esto es una prueba");

        coleccion.setFuente(fuenteMockeada);

        Assertions.assertFalse(coleccion.consultarHechos().isEmpty()); // Se agrega a una coleccion vacía, luego no debe estarlo
        // Verificar que la solicitud haya quedado en estado rechazada.
        Assertions.assertEquals(EstadoSolicitudEliminacion.RECHAZADA,solicitudEliminacion.getEstadoSolicitudEliminacion());

    }
    @Test
    @DisplayName("Solicitud Aceptada a las 2 horas.")
    public void aceptarSolicitud(){
        // Generar otra solicitud para el mismo hecho.
        String fundamento = "a".repeat(500) + " Esto esta bien fundado";
        coleccion = new Coleccion("Colección sin criterio", "Esto es una prueba");

        try {
            solicitudEliminacion = new SolicitudEliminacion(hecho, fundamento, contribuyente, LocalDateTime.now());
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        // Es aceptada a las 2 horas
        try {
            solicitudEliminacion.serAceptada(administrador);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        ResolucionSolicitudEliminacion resolucionSolicitudEliminacion = new ResolucionSolicitudEliminacion(administrador, LocalDateTime.now().plusHours(2));
        solicitudEliminacion.setResolucionSolicitudEliminacion(resolucionSolicitudEliminacion);
        // Esta vez el hecho no debería poder agregarse a una colección, puesto que este fue eliminado.
        coleccion.setFuente(fuenteMockeada);

        Assertions.assertTrue(coleccion.consultarHechos().isEmpty()); // Se agrega a una coleccion vacía, luego no debe estarlo
        // Verificar que la solicitud haya quedado en estado aceptada.
        Assertions.assertEquals(EstadoSolicitudEliminacion.ACEPTADA, solicitudEliminacion.getEstadoSolicitudEliminacion());
    }

}
