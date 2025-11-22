package com.appvet.mascotas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class MascotasApplication {

    public static void main(String[] args) {
        SpringApplication.run(MascotasApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("🐾 Microservicio Mascotas INICIADO");
        System.out.println("📍 Puerto: 8081");
        System.out.println("🌐 URL: http://localhost:8081/api/mascotas");
        System.out.println("❤️  Health: http://localhost:8081/api/mascotas/health");
        System.out.println("========================================\n");
    }

    // Configuración CORS para permitir peticiones desde Android
    @Bean
    WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }
}