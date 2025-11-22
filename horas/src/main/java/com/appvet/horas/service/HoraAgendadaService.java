package com.appvet.horas.service;

import com.appvet.horas.model.HoraAgendada;
import java.util.List;
import java.util.Optional;

public interface HoraAgendadaService {
    List<HoraAgendada> obtenerTodas();
    Optional<HoraAgendada> obtenerPorId(String id);
    List<HoraAgendada> obtenerPorUsuario(String usuarioId);
    List<HoraAgendada> obtenerPorMascota(String mascotaId);
    List<HoraAgendada> obtenerPorEstado(String estado);
    List<HoraAgendada> obtenerPorTipo(String tipo);
    HoraAgendada guardar(HoraAgendada horaAgendada);
    HoraAgendada actualizar(String id, HoraAgendada horaAgendada);
    HoraAgendada cambiarEstado(String id, String nuevoEstado);
    void eliminar(String id);
    long contarPorUsuario(String usuarioId);
}