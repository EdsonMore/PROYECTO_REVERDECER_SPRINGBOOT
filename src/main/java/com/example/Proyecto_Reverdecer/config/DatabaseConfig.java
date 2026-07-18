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
            System.out.println("[DB] Usando MYSQL_URL: " + jdbcUrl);
            DriverManagerDataSource ds = new DriverManagerDataSource();
            ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
            ds.setUrl(jdbcUrl);
            return ds;
        }

        String host = envDefault("DB_HOST", "localhost");
        String port = envDefault("DB_PORT", "3306");
        String db = envDefault("DB_DATABASE", "railway");

        System.out.println("[DB] Conectando a: " + host + ":" + port + "/" + db);
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://" + host + ":" + port + "/" + db
                + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
        ds.setUsername(envDefault("DB_USERNAME", "root"));
        ds.setPassword(envDefault("DB_PASSWORD", ""));
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
