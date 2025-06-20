//package ar.edu.utn.frba.dds.fuenteEstatica.config;
//
//import ar.edu.utn.frba.dds.domain.models.entities.fuentes.FuenteEstatica;
//import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.Archivo;
//import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.TipoArchivo;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.List;
//
//@Configuration
//public class ArchivoConfig {
//    @Bean
//    public List<Archivo> archivosPrueba(@Value("${path.csv}") String rutaArchivo, @Value("${nombre.csv}") String nombre){
//        Archivo archivo = new Archivo();
//        archivo.setTipoArchivo(new FuenteEstatica(rutaArchivo));
//        archivo.setNombre(nombre);
//        return List.of(archivo);
//    }
//}
