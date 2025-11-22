package com.appvet.horas.controller;

import com.appvet.horas.model.HoraAgendada;
import com.appvet.horas.service.HoraAgendadaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/horas-agendadas")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class HoraAgendadaController {
    
    private final HoraAgendadaService horaAgendadaService;
    
    @GetMapping
    public ResponseEntity<List<HoraAgendada>> obtenerTodas() {
        log.info("GET /api/horas-agendadas - Obteniendo todas las horas");
        return ResponseEntity.ok(horaAgendadaService.obtenerTodas());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<HoraAgendada> obtenerPorId(@PathVariable String id) {
        log.info("GET /api/horas-agendadas/{}", id);
        return horaAgendadaService.obtenerPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<HoraAgendada>> obtenerPorUsuario(@PathVariable String usuarioId) {
        log.info("GET /api/horas-agendadas/usuario/{}", usuarioId);
        return ResponseEntity.ok(horaAgendadaService.obtenerPorUsuario(usuarioId));
    }
    
    @GetMapping("/mascota/{mascotaId}")
    public ResponseEntity<List<HoraAgendada>> obtenerPorMascota(@PathVariable String mascotaId) {
        log.info("GET /api/horas-agendadas/mascota/{}", mascotaId);
        return ResponseEntity.ok(horaAgendadaService.obtenerPorMascota(mascotaId));
    }
    
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<HoraAgendada>> obtenerPorEstado(@PathVariable String estado) {
        log.info("GET /api/horas-agendadas/estado/{}", estado);
        return ResponseEntity.ok(horaAgendadaService.obtenerPorEstado(estado));
    }
    
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<HoraAgendada>> obtenerPorTipo(@PathVariable String tipo) {
        log.info("GET /api/horas-agendadas/tipo/{}", tipo);
        return ResponseEntity.ok(horaAgendadaService.obtenerPorTipo(tipo));
    }
    
    @PostMapping
    public ResponseEntity<HoraAgendada> crear(@Valid @RequestBody HoraAgendada horaAgendada) {
        log.info("POST /api/horas-agendadas - Creando: {}", horaAgendada.getTipo());
        HoraAgendada nueva = horaAgendadaService.guardar(horaAgendada);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<HoraAgendada> actualizar(
            @PathVariable String id,
            @Valid @RequestBody HoraAgendada horaAgendada) {
        log.info("PUT /api/horas-agendadas/{}", id);
        try {
            return ResponseEntity.ok(horaAgendadaService.actualizar(id, horaAgendada));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PatchMapping("/{id}/estado")
    public ResponseEntity<HoraAgendada> cambiarEstado(
            @PathVariable String id,
            @RequestBody Map<String, String> body) {
        log.info("PATCH /api/horas-agendadas/{}/estado", id);
        try {
            String nuevoEstado = body.get("estado");
            return ResponseEntity.ok(horaAgendadaService.cambiarEstado(id, nuevoEstado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        log.info("DELETE /api/horas-agendadas/{}", id);
        try {
            horaAgendadaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "microservicio-horas-agendadas",
            "port", "8082"
        ));
    }
}