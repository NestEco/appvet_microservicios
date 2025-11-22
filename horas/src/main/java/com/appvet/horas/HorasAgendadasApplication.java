package com.appvet.horas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class HorasAgendadasApplication {

    public static void main(String[] args) {
        SpringApplication.run(HorasAgendadasApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("📅 Microservicio Horas Agendadas INICIADO");
        System.out.println("📍 Puerto: 8082");
        System.out.println("🌐 URL: http://localhost:8082/api/horas-agendadas");
        System.out.println("❤️  Health: http://localhost:8082/api/horas-agendadas/health");
        System.out.println("========================================\n");
    }

    @Bean
    WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }
}