package com.example.Proyecto_Reverdecer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean
    public DataSource dataSource() {
        String host = obtenerVariable("DB_HOST", "localhost");
        String port = obtenerVariable("DB_PORT", "3306");
        String db = obtenerVariable("DB_DATABASE", "railway");
        String user = obtenerVariable("DB_USERNAME", "root");
        String password = obtenerVariable("DB_PASSWORD", "");

        System.out.println("[DB] Conectando a: " + host + ":" + port + "/" + db);

        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://" + host + ":" + port + "/" + db
                + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
        ds.setUsername(user);
        ds.setPassword(password);
        return ds;
    }

    private String obtenerVariable(String nombre, String defecto) {
        String valor = System.getenv(nombre);
        if (valor == null || valor.isEmpty() || valor.contains("${{")) {
            return defecto;
        }
        return valor;
    }
}
