import entities.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TestGonza {
    Categoria categoria = new Categoria("Caída de aeronave");

    Hecho hecho1 = new Hecho("Caída de aeronave impacta en Olavarría",
            "Grave caída de aeronave ocurrió en las inmediaciones de Olavarría, Buenos Aires. El incidente provocó pánico entre los residentes locales. Voluntarios de diversas organizaciones se han sumado a las tareas de auxilio.",
            );






    Solicitud solicitudEliminacion;
    Coleccion coleccion = new Coleccion();





    @BeforeEach
    public void init(){
        hecho.setTitulo("Brote de enfermedad contagiosa causa estragos en San Lorenzo, Santa Fe");
        hecho.setDescripcion("Grave brote de enfermedad contagiosa ocurrió en las inmediaciones de San Lorenzo, Santa Fe. El incidente dejó varios heridos y daños materiales. Se ha declarado estado de emergencia en la región para facilitar la asistencia.");
        Categoria desastreTecnologico = new Categoria();
        desastreTecnologico.setTitulo("Evento Sanitario");
        hecho.setCategoria(desastreTecnologico);
        hecho.setUbicacion(new Ubicacion("-32.786098", "-60.741543"));
        hecho.setFechaAcontecimiento(LocalDate.parse("2005-07-05")); // YYYY-MM-DD
        hecho.setEliminado(false);
    }
    @Test
    @DisplayName("Solicitud Rechazada 1 día después de su creación.")
    public void rechazarSolicitud(){

        solicitudEliminacion = new Solicitud(hecho, "a x 500 = Bien Fundado");
        coleccion.agregarHecho(hecho);
        solicitudEliminacion.setFechaResolucion(LocalDateTime.now().plusDays(1));
        solicitudEliminacion.serRechazada();

        Assertions.assertFalse(coleccion.consultarHechos().isEmpty());
        Assertions.assertEquals(EstadoSolicitud.RECHAZADA,solicitudEliminacion.getEstadoSolicitud());

    }
    @Test
    @DisplayName("Solicitud Aceptada a las 2 horas.")
    public void aceptarSolicitud(){
        solicitudEliminacion = new Solicitud(hecho, "b x 500 = Bien Fundando");

        solicitudEliminacion.setFechaResolucion(LocalDateTime.now().plusHours(2));
        solicitudEliminacion.serAceptada();
        //TODO: Por mantener consistente el DATO
        hecho.setEliminado(true);
        coleccion.agregarHecho(hecho);

        Assertions.assertTrue(coleccion.consultarHechos().isEmpty());
        Assertions.assertEquals(EstadoSolicitud.ACEPTADA, solicitudEliminacion.getEstadoSolicitud());
    }

}
