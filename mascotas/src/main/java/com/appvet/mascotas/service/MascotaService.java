package com.appvet.mascotas.service;

import com.appvet.mascotas.model.Mascota;
import java.util.List;
import java.util.Optional;

public interface MascotaService {
    List<Mascota> obtenerTodas();
    Optional<Mascota> obtenerPorId(String id);
    List<Mascota> obtenerPorUsuario(String usuarioId);
    List<Mascota> obtenerPorEspecie(String especie);
    Mascota guardar(Mascota mascota);
    Mascota actualizar(String id, Mascota mascota);
    void eliminar(String id);
    long contarPorUsuario(String usuarioId);
}