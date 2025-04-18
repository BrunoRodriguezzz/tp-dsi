import entities.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TestGonza {
    Ubicacion ubicacion1 = new Ubicacion("-36.868375", "-60.343297");
    Categoria categoria1 = new Categoria("Caída de aeronave");
    Origen origen1 = Origen.MANUAL;

    Hecho hecho1 = new Hecho("Caída de aeronave impacta en Olavarría",
            "Grave caída de aeronave ocurrió en las inmediaciones de Olavarría, Buenos Aires. El incidente provocó pánico entre los residentes locales. Voluntarios de diversas organizaciones se han sumado a las tareas de auxilio.",
            categoria1, ubicacion1, LocalDate.of(2001, 11, 21), origen1);

    Ubicacion ubicacion2 = new Ubicacion("-37.345571", "-70.241485");
    Categoria categoria2 = new Categoria("Accidente con maquinaria industrial");
    Origen origen2 = Origen.MANUAL;

    Hecho hecho2 = new Hecho("Serio incidente: Accidente con maquinaria industrial en Chos Malal, Neuquén",
            "Un grave accidente con maquinaria industrial se registró en Chos Malal, Neuquén. El incidente dejó a varios sectores sin comunicación. Voluntarios de diversas organizaciones se han sumado a las tareas de auxilio.",
            categoria2, ubicacion2, LocalDate.of(2001, 8, 16), origen2);

    Ubicacion ubicacion3 = new Ubicacion("-33.768051", "-61.921032");
    Categoria categoria3 = new Categoria("Caída de aeronave");
    Origen origen3 = Origen.MANUAL;

    Hecho hecho3 = new Hecho("Serio incidente: Accidente con maquinaria industrial en Chos Malal, Neuquén",
            "Un grave accidente con maquinaria industrial se registró en Chos Malal, Neuquén. El incidente dejó a varios sectores sin comunicación. Voluntarios de diversas organizaciones se han sumado a las tareas de auxilio.",
            categoria3, ubicacion3, LocalDate.of(2008, 8, 8), origen3);

    Ubicacion ubicacion4 = new Ubicacion("-35.855811", "-61.940589");
    Categoria categoria4 = new Categoria("Accidente en paso a nivel");
    Origen origen4 = Origen.MANUAL;

    Hecho hecho4 = new Hecho("Accidente en paso a nivel deja múltiples daños en Pehuajó, Buenos Aires",
            "Grave accidente en paso a nivel ocurrió en las inmediaciones de Pehuajó, Buenos Aires. El incidente generó preocupación entre las autoridades provinciales. El Ministerio de Desarrollo Social está brindando apoyo a los damnificados.",
            categoria4, ubicacion4, LocalDate.of(2020, 1, 27), origen4);

    Ubicacion ubicacion5 = new Ubicacion("-26.780008", "-60.458782");
    Categoria categoria5 = new Categoria("Derrumbe en obra en construcción");
    Origen origen5 = Origen.MANUAL;

    Hecho hecho5 = new Hecho("Devastador Derrumbe en obra en construcción afecta a Presidencia Roque Sáenz Peña",
            "Un grave derrumbe en obra en construcción se registró en Presidencia Roque Sáenz Peña, Chaco. El incidente generó preocupación entre las autoridades provinciales. El intendente local se ha trasladado al lugar para supervisar las operaciones.",
            categoria5, ubicacion5, LocalDate.of(2016, 6, 4), origen5);

    Coleccion coleccion = new Coleccion("Colección prueba", "Esto es una prueba");

    ImportadorHechos importador = new ImportadorHechos();
    //importador.cargarColeccion(coleccion, [hecho1, hecho2, hecho3, hecho4, hecho5]);

    //Assertions.assertEquals(coleccion.size(), 5);

//    coleccion.agregarHecho(hecho1);
//    coleccion.agregarHecho(hecho2);
//    coleccion.agregarHecho(hecho3);
//    coleccion.agregarHecho(hecho4);
//    coleccion.agregarHecho(hecho5);




//    @BeforeEach
//    public void init(){
//        hecho.setTitulo("Brote de enfermedad contagiosa causa estragos en San Lorenzo, Santa Fe");
//        hecho.setDescripcion("Grave brote de enfermedad contagiosa ocurrió en las inmediaciones de San Lorenzo, Santa Fe. El incidente dejó varios heridos y daños materiales. Se ha declarado estado de emergencia en la región para facilitar la asistencia.");
//        Categoria desastreTecnologico = new Categoria();
//        desastreTecnologico.setTitulo("Evento Sanitario");
//        hecho.setCategoria(desastreTecnologico);
//        hecho.setUbicacion(new Ubicacion("-32.786098", "-60.741543"));
//        hecho.setFechaAcontecimiento(LocalDate.parse("2005-07-05")); // YYYY-MM-DD
//        hecho.setEliminado(false);
//    }
//    @Test
//    @DisplayName("Solicitud Rechazada 1 día después de su creación.")
//    public void rechazarSolicitud(){
//
//        solicitudEliminacion = new Solicitud(hecho, "a x 500 = Bien Fundado");
//        coleccion.agregarHecho(hecho);
//        solicitudEliminacion.setFechaResolucion(LocalDateTime.now().plusDays(1));
//        solicitudEliminacion.serRechazada();
//
//        Assertions.assertFalse(coleccion.consultarHechos().isEmpty());
//        Assertions.assertEquals(EstadoSolicitud.RECHAZADA,solicitudEliminacion.getEstadoSolicitud());
//
//    }
//    @Test
//    @DisplayName("Solicitud Aceptada a las 2 horas.")
//    public void aceptarSolicitud(){
//        solicitudEliminacion = new Solicitud(hecho, "b x 500 = Bien Fundando");
//
//        solicitudEliminacion.setFechaResolucion(LocalDateTime.now().plusHours(2));
//        solicitudEliminacion.serAceptada();
//        //TODO: Por mantener consistente el DATO
//        hecho.setEliminado(true);
//        coleccion.agregarHecho(hecho);
//
//        Assertions.assertTrue(coleccion.consultarHechos().isEmpty());
//        Assertions.assertEquals(EstadoSolicitud.ACEPTADA, solicitudEliminacion.getEstadoSolicitud());
//    }

}
