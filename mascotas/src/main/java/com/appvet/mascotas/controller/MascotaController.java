package com.appvet.mascotas.controller;

import com.appvet.mascotas.model.Mascota;
import com.appvet.mascotas.service.MascotaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mascotas")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*") // Permite peticiones desde cualquier origen (Android)
public class MascotaController {
    
    private final MascotaService mascotaService;
    
    // GET - Obtener todas las mascotas
    @GetMapping
    public ResponseEntity<List<Mascota>> obtenerTodas() {
        log.info("GET /api/mascotas - Obteniendo todas las mascotas");
        List<Mascota> mascotas = mascotaService.obtenerTodas();
        return ResponseEntity.ok(mascotas);
    }
    
    // GET - Obtener mascota por ID
    @GetMapping("/{id}")
    public ResponseEntity<Mascota> obtenerPorId(@PathVariable String id) {
        log.info("GET /api/mascotas/{} - Buscando mascota", id);
        return mascotaService.obtenerPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    // GET - Obtener mascotas por usuario
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Mascota>> obtenerPorUsuario(@PathVariable String usuarioId) {
        log.info("GET /api/mascotas/usuario/{} - Obteniendo mascotas del usuario", usuarioId);
        List<Mascota> mascotas = mascotaService.obtenerPorUsuario(usuarioId);
        return ResponseEntity.ok(mascotas);
    }
    
    // GET - Obtener mascotas por especie
    @GetMapping("/especie/{especie}")
    public ResponseEntity<List<Mascota>> obtenerPorEspecie(@PathVariable String especie) {
        log.info("GET /api/mascotas/especie/{} - Obteniendo mascotas de especie", especie);
        List<Mascota> mascotas = mascotaService.obtenerPorEspecie(especie);
        return ResponseEntity.ok(mascotas);
    }
    
    // GET - Contar mascotas por usuario
    @GetMapping("/usuario/{usuarioId}/count")
    public ResponseEntity<Map<String, Long>> contarPorUsuario(@PathVariable String usuarioId) {
        log.info("GET /api/mascotas/usuario/{}/count - Contando mascotas", usuarioId);
        long count = mascotaService.contarPorUsuario(usuarioId);
        return ResponseEntity.ok(Map.of("count", count));
    }
    
    // POST - Crear nueva mascota
    @PostMapping
    public ResponseEntity<Mascota> crear(@Valid @RequestBody Mascota mascota) {
        log.info("POST /api/mascotas - Creando nueva mascota: {}", mascota.getNombre());
        Mascota nuevaMascota = mascotaService.guardar(mascota);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaMascota);
    }
    
    // PUT - Actualizar mascota existente
    @PutMapping("/{id}")
    public ResponseEntity<Mascota> actualizar(
            @PathVariable String id,
            @Valid @RequestBody Mascota mascota) {
        log.info("PUT /api/mascotas/{} - Actualizando mascota", id);
        try {
            Mascota mascotaActualizada = mascotaService.actualizar(id, mascota);
            return ResponseEntity.ok(mascotaActualizada);
        } catch (RuntimeException e) {
            log.error("Error al actualizar mascota: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    // DELETE - Eliminar mascota
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        log.info("DELETE /api/mascotas/{} - Eliminando mascota", id);
        try {
            mascotaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error al eliminar mascota: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    // GET - Health check
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "microservicio-mascotas",
            "port", "8081"
        ));
    }
}