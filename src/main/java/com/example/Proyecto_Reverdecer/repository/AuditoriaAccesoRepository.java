package com.example.Proyecto_Reverdecer.repository;

import com.example.Proyecto_Reverdecer.model.AuditoriaAcceso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditoriaAccesoRepository extends JpaRepository<AuditoriaAcceso, Long> {
    List<AuditoriaAcceso> findByUsuarioIdOrderByFechaDesc(Long usuarioId);
    List<AuditoriaAcceso> findByAccion(String accion);
    List<AuditoriaAcceso> findByFechaAfterOrderByFechaDesc(LocalDateTime fecha);
    List<AuditoriaAcceso> findAllByOrderByFechaDesc();
    long countByAccion(String accion);
}
