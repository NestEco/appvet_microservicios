package com.appvet.mascotas.service;

import com.appvet.mascotas.model.Mascota;
import com.appvet.mascotas.repository.MascotaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MascotaServiceImpl implements MascotaService {
    
    private final MascotaRepository mascotaRepository;
    
    @Override
    @Transactional(readOnly = true)
    public List<Mascota> obtenerTodas() {
        log.info("Obteniendo todas las mascotas");
        return mascotaRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Mascota> obtenerPorId(String id) {
        log.info("Buscando mascota con ID: {}", id);
        return mascotaRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Mascota> obtenerPorUsuario(String usuarioId) {
        log.info("Obteniendo mascotas del usuario: {}", usuarioId);
        return mascotaRepository.findByUsuarioId(usuarioId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Mascota> obtenerPorEspecie(String especie) {
        log.info("Obteniendo mascotas de especie: {}", especie);
        return mascotaRepository.findByEspecie(especie);
    }
    
    @Override
    public Mascota guardar(Mascota mascota) {
        log.info("Guardando mascota: {}", mascota.getNombre());
        return mascotaRepository.save(mascota);
    }
    
    @Override
    public Mascota actualizar(String id, Mascota mascotaActualizada) {
        log.info("Actualizando mascota con ID: {}", id);
        return mascotaRepository.findById(id)
            .map(mascotaExistente -> {
                mascotaExistente.setNombre(mascotaActualizada.getNombre());
                mascotaExistente.setEdad(mascotaActualizada.getEdad());
                mascotaExistente.setEspecie(mascotaActualizada.getEspecie());
                mascotaExistente.setFotoUri(mascotaActualizada.getFotoUri());
                mascotaExistente.setFotoIcono(mascotaActualizada.getFotoIcono());
                return mascotaRepository.save(mascotaExistente);
            })
            .orElseThrow(() -> new RuntimeException("Mascota no encontrada con ID: " + id));
    }
    
    @Override
    public void eliminar(String id) {
        log.info("Eliminando mascota con ID: {}", id);
        if (!mascotaRepository.existsById(id)) {
            throw new RuntimeException("Mascota no encontrada con ID: " + id);
        }
        mascotaRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long contarPorUsuario(String usuarioId) {
        log.info("Contando mascotas del usuario: {}", usuarioId);
        return mascotaRepository.countByUsuarioId(usuarioId);
    }
}