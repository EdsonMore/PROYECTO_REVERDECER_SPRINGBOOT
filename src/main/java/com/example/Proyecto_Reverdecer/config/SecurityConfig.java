package com.example.Proyecto_Reverdecer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.Proyecto_Reverdecer.service.CustomUserDetailsService;
import com.example.Proyecto_Reverdecer.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Configurar autorización de solicitudes
            .authorizeHttpRequests(authz -> authz
                // Permitir acceso libre a recursos estáticos
                .requestMatchers("/static/**", "/css/**", "/js/**", "/img/**", "/data/**").permitAll()
                .requestMatchers("/resources/**").permitAll()
                
                // Permitir acceso libre a rutas públicas
                .requestMatchers("/", "/sobre-nosotros", "/contacto").permitAll()
                .requestMatchers("/auth/registro", "/auth/login", "/auth/logout").permitAll()
                .requestMatchers("/acceso-denegado").permitAll()
                .requestMatchers("/mapa", "/mapa/**").permitAll()
                .requestMatchers("/arboles/api").permitAll()
                
                // Endpoint REST para login con JWT (público)
                .requestMatchers("/api/auth/login").permitAll()
                
                // Rutas protegidas por interceptores personalizados (permitAll aquí, validación en interceptor)
                .requestMatchers("/admin/**").permitAll()
                .requestMatchers("/gestor/**").permitAll()
                .requestMatchers("/supervisor/**").permitAll()
                
                // Rutas que requieren sesión autenticada pero sin rol específico
                .requestMatchers("/arboles/**").permitAll()
                .requestMatchers("/perfil/**").permitAll()
                
                // API protegida requiere JWT
                .requestMatchers("/api/**").authenticated()
                
                // Todas las demás solicitudes permitidas
                .anyRequest().permitAll()   
            )
            .csrf(csrf -> csrf.disable())
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .permitAll())
            .exceptionHandling(exceptions -> exceptions
                .accessDeniedPage("/acceso-denegado"))
            // agreamos el filtro JWT antes del filtro de autenticación de Spring Security
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public org.springframework.security.web.session.HttpSessionEventPublisher httpSessionEventPublisher() {
        return new org.springframework.security.web.session.HttpSessionEventPublisher();
    }
}