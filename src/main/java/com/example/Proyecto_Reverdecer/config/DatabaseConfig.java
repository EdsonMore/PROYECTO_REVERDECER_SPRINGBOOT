package com.example.Proyecto_Reverdecer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean
    public DataSource dataSource() {
        String mysqlUrl = env("MYSQL_URL");

        if (mysqlUrl != null) {
            String jdbcUrl = "jdbc:" + mysqlUrl
                    + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            System.out.println("[DB] Conectando por red privada: " + jdbcUrl);
            DriverManagerDataSource ds = new DriverManagerDataSource();
            ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
            ds.setUrl(jdbcUrl);
            return ds;
        }

        System.out.println("[DB] Sin variables de BD disponibles");
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/railway?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
        ds.setUsername("root");
        ds.setPassword("");
        return ds;
    }

    private String env(String nombre) {
        String valor = System.getenv(nombre);
        if (valor == null || valor.isEmpty() || valor.contains("${{")) {
            return null;
        }
        return valor;
    }

    private String envDefault(String nombre, String defecto) {
        String valor = env(nombre);
        return valor != null ? valor : defecto;
    }
}
