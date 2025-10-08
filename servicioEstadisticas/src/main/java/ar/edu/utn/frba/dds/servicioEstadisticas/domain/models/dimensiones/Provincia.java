package ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.dimensiones;

public enum Provincia {
    BUENOS_AIRES {public String getValue() {return "Buenos Aires";}},
    CATAMARCA {public String getValue() {return "Catamarca";}},
    CHACO {public String getValue() {return "Chaco";}},
    CHUBUT {public String getValue() {return "Chubut";}},
    CORDOBA {public String getValue() {return "Cordoba";}},
    CORRIENTES {public String getValue() {return "Corrientes";}},
    ENTRE_RIOS {public String getValue() {return "Entre Ríos";}},
    FORMOSA {public String getValue() {return "Formosa";}},
    JUJUY {public String getValue() {return "Jujuy";}},
    LA_PAMPA {public String getValue() {return "La Pampa";}},
    LA_RIOJA {public String getValue() {return "La Rioja";}},
    MENDOZA {public String getValue() {return "Mendoza";}},
    MISIONES {public String getValue() {return "Misiones";}},
    NEUQUEN {public String getValue() {return "Neuquen";}},
    RIO_NEGRO {public String getValue() {return "Rio Negro";}},
    SALTA {public String getValue() {return "Salta";}},
    SAN_JUAN {public String getValue() {return "San Juan";}},
    SAN_LUIS {public String getValue() {return "San Luis";}},
    SANTA_CRUZ {public String getValue() {return "Santa Cruz";}},
    SANTA_FE {public String getValue() {return "Santa Fe";}},
    SANTIAGO_DEL_ESTERO {public String getValue() {return "Santiago Del Estero";}},
    TIERRA_DEL_FUEGO {public String getValue() {return "Tierra Del Fuego";}},
    TUCUMAN {public String getValue() {return "Tucuman";}},
    CIUDAD_AUTONOMA_DE_BUENOS_AIRES {public String getValue() {return "CABA";}};

    public abstract String getValue();
}
