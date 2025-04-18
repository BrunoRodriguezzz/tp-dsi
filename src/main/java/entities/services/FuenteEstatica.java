package entities.services;

import entities.*;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FuenteEstatica {
    private String rutaArchivo;
    private BufferedReader lector; //lee el archivo
    private String linea; //recibe la linea de cada fila
    private String atributosHecho[] = null; //almacena cada linea leída

    public FuenteEstatica(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public Integer cargarColeccion(Coleccion coleccion) {
        try {
            lector = new BufferedReader(new FileReader(rutaArchivo));
            while ((linea = lector.readLine()) != null) {
                atributosHecho = linea.split(",");
                Hecho hecho = new Hecho (this.atributosHecho[0], this.atributosHecho[1], new Categoria(this.atributosHecho[2])
                        , new Ubicacion(this.atributosHecho[3], this.atributosHecho[4]), convertirFecha(atributosHecho[5]),
                        Origen.DATASET);
                coleccion.agregarHecho(hecho);
            }
            lector.close();
            linea = null;
            atributosHecho = null;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return 0;
    }

    private static LocalDate convertirFecha(String fechaStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(fechaStr, formatter);
    }

}

//Hecho hecho = new Hecho(
//        "Brote de enfermedad contagiosa causa estragos en San Lorenzo, Santa Fe",
//        "Grave brote de enfermedad contagiosa ocurrió en las inmediaciones de San Lorenzo, Santa Fe. El incidente dejó varios heridos y daños materiales. Se ha declarado estado de emergencia en la región para facilitar la asistencia.",
//        new Categoria("Desastre tecnológico"),
//        new Ubicacion("-32.786098", "-60.741543"),
//        LocalDate.parse("2005-07-05"),
//        Origen.MANUAL
//);

//TODO Mostrar hechos e instanciarlos.