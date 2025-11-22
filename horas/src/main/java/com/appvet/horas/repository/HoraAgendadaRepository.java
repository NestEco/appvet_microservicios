package com.appvet.horas.repository;

import com.appvet.horas.model.HoraAgendada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HoraAgendadaRepository extends JpaRepository<HoraAgendada, String> {
    
    List<HoraAgendada> findByUsuarioId(String usuarioId);
    List<HoraAgendada> findByMascotaId(String mascotaId);
    List<HoraAgendada> findByEstado(String estado);
    List<HoraAgendada> findByTipo(String tipo);
    List<HoraAgendada> findByUsuarioIdAndEstado(String usuarioId, String estado);
    
    @Query("SELECT h FROM HoraAgendada h WHERE h.fecha >= :fechaDesde AND h.fecha <= :fechaHasta")
    List<HoraAgendada> findByFechaBetween(@Param("fechaDesde") Long fechaDesde, @Param("fechaHasta") Long fechaHasta);
    
    long countByUsuarioId(String usuarioId);
    long countByEstado(String estado);
}