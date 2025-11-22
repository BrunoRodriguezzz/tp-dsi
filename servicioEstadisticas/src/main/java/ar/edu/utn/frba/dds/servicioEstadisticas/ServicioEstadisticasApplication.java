package ar.edu.utn.frba.dds.servicioEstadisticas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableAsync
public class ServicioEstadisticasApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicioEstadisticasApplication.class, args);
	}

}
