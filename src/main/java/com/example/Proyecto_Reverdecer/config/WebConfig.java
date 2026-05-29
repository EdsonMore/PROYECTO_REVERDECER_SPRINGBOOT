package com.example.Proyecto_Reverdecer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

   //interceptor para validar acceso de los roles     
    @Autowired
    private AdminInterceptor adminInterceptor;


    @Autowired
    private GestorAmbientalInterceptor gestorAmbientalInterceptor;


    //registra los interceptores segun la ruta
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admin/**");

        registry.addInterceptor(gestorAmbientalInterceptor)
                .addPathPatterns("/gestor/**");
    }

    //configura las rutas para los recursos estaticos
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/", "classpath:/public/")
                .setCachePeriod(0);

        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/")
                .setCachePeriod(0);

        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/")
                .setCachePeriod(0);

        registry.addResourceHandler("/img/**")
                .addResourceLocations("classpath:/static/img/")
                .setCachePeriod(0);
    }
}