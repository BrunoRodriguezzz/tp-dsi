package ar.edu.utn.frba.dds.fuenteProxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableAsync
public class FuenteProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(FuenteProxyApplication.class, args);
	}

}

