package ar.edu.utn.frba.dds.fuenteDinamica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FuenteDinamicaApplication {

	public static void main(String[] args) {
		SpringApplication.run(FuenteDinamicaApplication.class, args);
	}

}