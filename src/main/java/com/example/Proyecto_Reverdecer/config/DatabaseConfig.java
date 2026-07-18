package com.example.Proyecto_Reverdecer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean
    public DataSource dataSource() {
        String host = System.getenv().getOrDefault("MYSQLHOST", "localhost");
        String port = System.getenv().getOrDefault("MYSQLPORT", "3306");
        String db = System.getenv().getOrDefault("MYSQL_DATABASE", "reverdecer_bd");
        String user = System.getenv().getOrDefault("MYSQLUSER", "root");
        String password = System.getenv().getOrDefault("MYSQLPASSWORD", "0805");

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://" + host + ":" + port + "/" + db
                + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
        dataSource.setUsername(user);
        dataSource.setPassword(password);

        return dataSource;
    }
}
