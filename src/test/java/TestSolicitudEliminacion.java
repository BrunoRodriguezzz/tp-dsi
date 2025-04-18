import entities.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TestSolicitudEliminacion {
    Solicitud solicitudEliminacion;
    ImportadorHechos importador = new ImportadorHechos();
    Hecho hecho = new Hecho(
        "Brote de enfermedad contagiosa causa estragos en San Lorenzo, Santa Fe",
        "Grave brote de enfermedad contagiosa ocurrió en las inmediaciones de San Lorenzo, Santa Fe. El incidente dejó varios heridos y daños materiales. Se ha declarado estado de emergencia en la región para facilitar la asistencia.",
        new Categoria("Desastre tecnológico"),
        new Ubicacion("-32.786098", "-60.741543"),
        LocalDate.parse("2005-07-05"),
        Origen.MANUAL
        );
        
    Coleccion coleccion;

    @BeforeEach
    public void setUpColeccion() {
        coleccion = new Coleccion("Coleccion1", "Una conexion");
    }

    @Test
    @DisplayName("Solicitud Rechazada 1 día después de su creación.")
    public void rechazarSolicitud(){
        solicitudEliminacion = new Solicitud(hecho, "a".repeat(500) + " Esto esta bien fundado");
        // Al crear una solicitud queda en estado pendiente
        Assertions.assertEquals(EstadoSolicitud.PENDIENTE,solicitudEliminacion.getEstadoSolicitud());
        // Rechazar esta solicitud un día después de su creación.
        solicitudEliminacion.serRechazada();
        solicitudEliminacion.setFechaResolucion(LocalDateTime.now().plusDays(1));

        // Dado que fue rechazada, el hecho puede ser agregado a cualquier colección. 
        coleccion.agregarHecho(hecho);
        Assertions.assertFalse(coleccion.consultarHechos().isEmpty()); // Se agrega a una coleccion vacía, luego no debe estarlo
        // Verificar que la solicitud haya quedado en estado rechazada.
        Assertions.assertEquals(EstadoSolicitud.RECHAZADA,solicitudEliminacion.getEstadoSolicitud()); 
        
    }
    @Test
    @DisplayName("Solicitud Aceptada a las 2 horas.")
    public void aceptarSolicitud(){
        // Generar otra solicitud para el mismo hecho.
        solicitudEliminacion = new Solicitud(hecho, "a".repeat(500) + " Esto esta bien fundado");
        
        // Es aceptada a las 2 horas
        solicitudEliminacion.serAceptada();
        solicitudEliminacion.setFechaResolucion(LocalDateTime.now().plusHours(2));
        
        // Esta vez el hecho no debería poder agregarse a una colección, puesto que este fue eliminado.
        coleccion.agregarHecho(hecho);
        Assertions.assertTrue(coleccion.consultarHechos().isEmpty());

        // Verificar que la solicitud haya quedado en estado aceptada.
        Assertions.assertEquals(EstadoSolicitud.ACEPTADA, solicitudEliminacion.getEstadoSolicitud());
    }

}
