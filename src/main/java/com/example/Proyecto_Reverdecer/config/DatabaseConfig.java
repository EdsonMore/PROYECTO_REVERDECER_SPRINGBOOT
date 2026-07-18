package com.example.Proyecto_Reverdecer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean
    public DataSource dataSource() {
        // 1. Try MYSQL_PUBLIC_URL
        String publicUrl = System.getenv("MYSQL_PUBLIC_URL");
        System.out.println("[DB] MYSQL_PUBLIC_URL raw: '" + publicUrl + "'");
        if (publicUrl != null && !publicUrl.isEmpty() && !publicUrl.contains("{{")) {
            String jdbcUrl = "jdbc:" + publicUrl
                    + "?useSSL=true&requireSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
            System.out.println("[DB] Trying public URL: " + jdbcUrl);
            DriverManagerDataSource ds = new DriverManagerDataSource();
            ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
            ds.setUrl(jdbcUrl);
            return ds;
        }

        // 2. Try MYSQL_URL
        String mysqlUrl = System.getenv("MYSQL_URL");
        System.out.println("[DB] MYSQL_URL raw: '" + mysqlUrl + "'");
        if (mysqlUrl != null && !mysqlUrl.isEmpty() && !mysqlUrl.contains("{{")) {
            String jdbcUrl = "jdbc:" + mysqlUrl
                    + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
            System.out.println("[DB] Trying private URL: " + jdbcUrl);
            DriverManagerDataSource ds = new DriverManagerDataSource();
            ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
            ds.setUrl(jdbcUrl);
            return ds;
        }

        // 3. Fallback: hardcoded connection string
        System.out.println("[DB] Using fallback: jdbc:mysql://localhost:3306/reverdecer_bd (WILL FAIL!)");
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/reverdecer_bd?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
        ds.setUsername("root");
        ds.setPassword("0805");
        return ds;
    }
}
