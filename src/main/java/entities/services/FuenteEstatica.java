package entities.services;

import entities.Coleccion;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;

public class FuenteEstatica {
    private String rutaArchivo;
    private BufferedReader lector; //lee el archivo
    private String linea; //recibe la linea de cada fila
    private String partes[] = null; //almacena cada linea leída

    public FuenteEstatica(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public Integer cargarColeccion(Coleccion coleccion) {
        try {
            lector = new BufferedReader(new FileReader(rutaArchivo));
            while ((linea = lector.readLine()) != null) {
                partes = linea.split(",");
            }
            lector.close();
            linea = null;
            partes = null;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return 0;
    }
}

//TODO Mostrar hechos e instanciarlos.