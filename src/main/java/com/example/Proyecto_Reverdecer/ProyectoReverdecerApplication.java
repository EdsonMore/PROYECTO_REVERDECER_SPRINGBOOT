package com.example.Proyecto_Reverdecer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProyectoReverdecerApplication {

	public static void main(String[] args) {
		String port = System.getenv("PORT");
		System.out.println("[PORT_DEBUG] PORT='" + port + "'");
		if (port != null && !port.isEmpty()) {
			System.setProperty("server.port", port);
			System.out.println("[PORT_DEBUG] server.port -> " + port);
		}
		SpringApplication.run(ProyectoReverdecerApplication.class, args);
	}
}