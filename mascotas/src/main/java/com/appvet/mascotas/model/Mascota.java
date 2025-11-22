package com.appvet.mascotas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "mascotas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mascota {
    
    @Id
    @Column(name = "id", length = 36)
    private String id;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @NotNull(message = "La edad es obligatoria")
    @Min(value = 0, message = "La edad no puede ser negativa")
    @Max(value = 50, message = "La edad no puede ser mayor a 50 años")
    @Column(name = "edad", nullable = false)
    private Integer edad;
    
    @NotBlank(message = "La especie es obligatoria")
    @Size(max = 50, message = "La especie no puede exceder 50 caracteres")
    @Column(name = "especie", nullable = false, length = 50)
    private String especie;
    
    @Column(name = "foto_icono")
    private Integer fotoIcono;
    
    @Column(name = "foto_uri", columnDefinition = "TEXT")
    private String fotoUri;
    
    @NotBlank(message = "El ID de usuario es obligatorio")
    @Column(name = "usuario_id", nullable = false, length = 36)
    private String usuarioId;
    
    @CreationTimestamp
    @Column(name = "fecha_registro", updatable = false)
    private LocalDateTime fechaRegistro;
    
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    // Constructor sin timestamps (para crear desde Android)
    public Mascota(String id, String nombre, Integer edad, String especie, String fotoUri, String usuarioId) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.especie = especie;
        this.fotoUri = fotoUri;
        this.usuarioId = usuarioId;
    }
}