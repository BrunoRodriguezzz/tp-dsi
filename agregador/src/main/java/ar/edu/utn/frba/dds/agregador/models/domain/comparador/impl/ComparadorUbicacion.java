package ar.edu.utn.frba.dds.agregador.models.domain.comparador.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.comparador.IComparacionUbicacion;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.ubicacion.Ubicacion;

public class ComparadorUbicacion implements IComparacionUbicacion {
    private final double radioMetros;

    public ComparadorUbicacion(double radioMetros) {
        this.radioMetros = radioMetros;
    }

    // Factory methods
    public static ComparadorUbicacion conRadioMetros(double metros) {
        return new ComparadorUbicacion(metros);
    }

    public static ComparadorUbicacion conRadioKilometros(double kilometros) {
        return new ComparadorUbicacion(kilometros * 1000);
    }

    public static ComparadorUbicacion conRadioMillas(double millas) {
        return new ComparadorUbicacion(millas * 1609.34);
    }

    @Override
    public boolean comparar(Ubicacion ubicacion1, Ubicacion ubicacion2) {
        if (ubicacion1 == null || ubicacion2 == null) {
            return false;
        }

        double lat1 = ubicacion1.getLatitud();
        double lon1 = ubicacion1.getLongitud();
        double lat2 = ubicacion2.getLatitud();
        double lon2 = ubicacion2.getLongitud();

        double distancia = calcularDistanciaHaversine(lat1, lon1, lat2, lon2);
        return distancia <= radioMetros;
    }

    private double calcularDistanciaHaversine(double lat1, double lon1, double lat2, double lon2) {
        final int RADIO_TIERRA = 6371000;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return RADIO_TIERRA * c;
    }
}
