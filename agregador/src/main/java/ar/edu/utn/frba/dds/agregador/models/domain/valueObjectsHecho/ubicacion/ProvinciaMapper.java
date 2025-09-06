package ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.ubicacion;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ProvinciaMapper {
    private static final Map<String, Provincia> equivalencias = new HashMap<>();

    static {
        try (InputStream input = ProvinciaMapper.class.getClassLoader().getResourceAsStream("provincias.json")) {
            if (input == null) {
                throw new IllegalStateException("No se encontró provincias.json en resources");
            }
            ObjectMapper mapper = new ObjectMapper();

            // Leemos el JSON como un mapa <String, String>
            Map<String, String> rawMap = mapper.readValue(input, Map.class);

            // Convertimos a <String, Provincia>
            rawMap.forEach((clave, valor) -> equivalencias.put(clave.toUpperCase(), Provincia.valueOf(valor)));
        } catch (IOException e) {
            throw new RuntimeException("Error cargando equivalencias de provincias", e);
        }
    }

    public static Provincia map(String nombre) {
        if (nombre == null) return null;
        return equivalencias.get(nombre.trim().toUpperCase());
    }
}
