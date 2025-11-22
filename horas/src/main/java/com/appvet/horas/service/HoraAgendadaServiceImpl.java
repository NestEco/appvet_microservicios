package com.appvet.horas.service;

import com.appvet.horas.model.HoraAgendada;
import com.appvet.horas.repository.HoraAgendadaRepository;
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
public class HoraAgendadaServiceImpl implements HoraAgendadaService {
    
    private final HoraAgendadaRepository horaAgendadaRepository;
    
    @Override
    @Transactional(readOnly = true)
    public List<HoraAgendada> obtenerTodas() {
        log.info("Obteniendo todas las horas agendadas");
        return horaAgendadaRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<HoraAgendada> obtenerPorId(String id) {
        log.info("Buscando hora agendada con ID: {}", id);
        return horaAgendadaRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<HoraAgendada> obtenerPorUsuario(String usuarioId) {
        log.info("Obteniendo horas agendadas del usuario: {}", usuarioId);
        return horaAgendadaRepository.findByUsuarioId(usuarioId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<HoraAgendada> obtenerPorMascota(String mascotaId) {
        log.info("Obteniendo horas agendadas de la mascota: {}", mascotaId);
        return horaAgendadaRepository.findByMascotaId(mascotaId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<HoraAgendada> obtenerPorEstado(String estado) {
        log.info("Obteniendo horas agendadas con estado: {}", estado);
        return horaAgendadaRepository.findByEstado(estado);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<HoraAgendada> obtenerPorTipo(String tipo) {
        log.info("Obteniendo horas agendadas de tipo: {}", tipo);
        return horaAgendadaRepository.findByTipo(tipo);
    }
    
    @Override
    public HoraAgendada guardar(HoraAgendada horaAgendada) {
        log.info("Guardando hora agendada: {} - {}", horaAgendada.getTipo(), horaAgendada.getFecha());
        return horaAgendadaRepository.save(horaAgendada);
    }
    
    @Override
    public HoraAgendada actualizar(String id, HoraAgendada horaActualizada) {
        log.info("Actualizando hora agendada con ID: {}", id);
        return horaAgendadaRepository.findById(id)
            .map(horaExistente -> {
                horaExistente.setFecha(horaActualizada.getFecha());
                horaExistente.setHora(horaActualizada.getHora());
                horaExistente.setMinuto(horaActualizada.getMinuto());
                horaExistente.setTipo(horaActualizada.getTipo());
                horaExistente.setEstado(horaActualizada.getEstado());
                horaExistente.setMascotaId(horaActualizada.getMascotaId());
                horaExistente.setNotas(horaActualizada.getNotas());
                return horaAgendadaRepository.save(horaExistente);
            })
            .orElseThrow(() -> new RuntimeException("Hora agendada no encontrada con ID: " + id));
    }
    
    @Override
    public HoraAgendada cambiarEstado(String id, String nuevoEstado) {
        log.info("Cambiando estado de hora agendada {} a {}", id, nuevoEstado);
        return horaAgendadaRepository.findById(id)
            .map(hora -> {
                hora.setEstado(nuevoEstado);
                return horaAgendadaRepository.save(hora);
            })
            .orElseThrow(() -> new RuntimeException("Hora agendada no encontrada con ID: " + id));
    }
    
    @Override
    public void eliminar(String id) {
        log.info("Eliminando hora agendada con ID: {}", id);
        if (!horaAgendadaRepository.existsById(id)) {
            throw new RuntimeException("Hora agendada no encontrada con ID: " + id);
        }
        horaAgendadaRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long contarPorUsuario(String usuarioId) {
        log.info("Contando horas agendadas del usuario: {}", usuarioId);
        return horaAgendadaRepository.countByUsuarioId(usuarioId);
    }
}