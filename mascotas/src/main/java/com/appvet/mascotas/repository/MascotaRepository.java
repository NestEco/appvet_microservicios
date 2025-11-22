package com.appvet.mascotas.repository;

import com.appvet.mascotas.model.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MascotaRepository extends JpaRepository<Mascota, String> {
    
    // Buscar mascotas por usuario
    List<Mascota> findByUsuarioId(String usuarioId);
    
    // Buscar mascotas por especie
    List<Mascota> findByEspecie(String especie);
    
    // Buscar mascotas por nombre (case insensitive)
    List<Mascota> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar mascotas por usuario y especie
    List<Mascota> findByUsuarioIdAndEspecie(String usuarioId, String especie);
    
    // Contar mascotas por usuario
    long countByUsuarioId(String usuarioId);
    
    // Buscar mascotas por rango de edad
    @Query("SELECT m FROM Mascota m WHERE m.edad BETWEEN :edadMin AND :edadMax")
    List<Mascota> findByEdadBetween(@Param("edadMin") Integer edadMin, @Param("edadMax") Integer edadMax);
    
    // Verificar si existe una mascota con ese ID y usuario
    boolean existsByIdAndUsuarioId(String id, String usuarioId);
}