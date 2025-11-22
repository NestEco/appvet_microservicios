package com.appvet.horas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "horas_agendadas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HoraAgendada {
    
    @Id
    @Column(name = "id", length = 36)
    private String id;
    
    @Column(name = "fecha")
    private Long fecha; // Timestamp en milisegundos (como en Kotlin)
    
    @NotNull(message = "La hora es obligatoria")
    @Min(value = 0, message = "La hora debe estar entre 0 y 23")
    @Max(value = 23, message = "La hora debe estar entre 0 y 23")
    @Column(name = "hora", nullable = false)
    private Integer hora;
    
    @NotNull(message = "El minuto es obligatorio")
    @Min(value = 0, message = "El minuto debe estar entre 0 y 59")
    @Max(value = 59, message = "El minuto debe estar entre 0 y 59")
    @Column(name = "minuto", nullable = false)
    private Integer minuto;
    
    @NotBlank(message = "El tipo es obligatorio")
    @Size(max = 100, message = "El tipo no puede exceder 100 caracteres")
    @Column(name = "tipo", nullable = false, length = 100)
    private String tipo;
    
    @NotBlank(message = "El ID de usuario es obligatorio")
    @Column(name = "usuario_id", nullable = false, length = 36)
    private String usuarioId;
    
    @Column(name = "mascota_id", length = 36)
    private String mascotaId;
    
    @Column(name = "estado", length = 50)
    private String estado = "Pendiente";
    
    @Column(name = "notas", columnDefinition = "TEXT")
    private String notas;
    
    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    // Constructor sin timestamps (para crear desde Android)
    public HoraAgendada(String id, Long fecha, Integer hora, Integer minuto, String tipo, String usuarioId) {
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.minuto = minuto;
        this.tipo = tipo;
        this.usuarioId = usuarioId;
        this.estado = "Pendiente";
    }
}