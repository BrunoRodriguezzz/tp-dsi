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
        if (!sonUbicacionesValidas(ubicacion1, ubicacion2)) {
            return false;
        }

        try {
            Double lat1 = parsearCoordenadaSeguro(ubicacion1.getLatitud());
            Double lon1 = parsearCoordenadaSeguro(ubicacion1.getLongitud());
            Double lat2 = parsearCoordenadaSeguro(ubicacion2.getLatitud());
            Double lon2 = parsearCoordenadaSeguro(ubicacion2.getLongitud());

            if (lat1 == null || lon1 == null || lat2 == null || lon2 == null) {
                return false;
            }

            double distancia = calcularDistanciaHaversine(lat1, lon1, lat2, lon2);
            return distancia <= radioMetros;

        } catch (Exception e) {
            return false;
        }
    }

    private boolean sonUbicacionesValidas(Ubicacion u1, Ubicacion u2) {
        return u1 != null && u2 != null &&
                u1.getLatitud() != null && u1.getLongitud() != null &&
                u2.getLatitud() != null && u2.getLongitud() != null;
    }

    private Double parsearCoordenadaSeguro(String coordenada) {
        if (coordenada == null) {
            return null;
        }

        try {
            String normalizada = coordenada.trim()
                    .replace(',', '.')
                    .replaceAll("[^0-9.\\-]", ""); // Solo números, punto y signo negativo

            return Double.parseDouble(normalizada);
        } catch (NumberFormatException e) {
            return null;
        }
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
