package com.example.Proyecto_Reverdecer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean
    public DataSource dataSource() {
        // 1. Try MYSQL_PUBLIC_URL (TCP proxy - works cross-service)
        String publicUrl = System.getenv("MYSQL_PUBLIC_URL");
        if (publicUrl != null && !publicUrl.isEmpty()) {
            String jdbcUrl = "jdbc:" + publicUrl
                    + "?useSSL=true&requireSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
            DriverManagerDataSource ds = new DriverManagerDataSource();
            ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
            ds.setUrl(jdbcUrl);
            return ds;
        }

        // 2. Try MYSQL_URL (private network - if available)
        String mysqlUrl = System.getenv("MYSQL_URL");
        if (mysqlUrl != null && !mysqlUrl.isEmpty()) {
            String jdbcUrl = "jdbc:" + mysqlUrl
                    + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
            DriverManagerDataSource ds = new DriverManagerDataSource();
            ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
            ds.setUrl(jdbcUrl);
            return ds;
        }

        // 3. Fallback: individual variables
        String host = System.getenv().getOrDefault("MYSQLHOST", "localhost");
        String port = System.getenv().getOrDefault("MYSQLPORT", "3306");
        String db = System.getenv().getOrDefault("MYSQL_DATABASE", "reverdecer_bd");
        String user = System.getenv().getOrDefault("MYSQLUSER", "root");
        String password = System.getenv().getOrDefault("MYSQLPASSWORD", "0805");

        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://" + host + ":" + port + "/" + db
                + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
        ds.setUsername(user);
        ds.setPassword(password);

        return ds;
    }
}
