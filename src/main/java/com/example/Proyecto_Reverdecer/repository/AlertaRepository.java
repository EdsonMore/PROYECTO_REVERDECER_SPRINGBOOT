package com.example.Proyecto_Reverdecer.repository;

import com.example.Proyecto_Reverdecer.model.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertaRepository extends JpaRepository<Alerta, Long> {
    List<Alerta> findAllByOrderByFechaCreacionDesc();
    List<Alerta> findByResueltaFalseOrderByFechaCreacionDesc();
    List<Alerta> findByTipo(String tipo);
    List<Alerta> findByNivel(String nivel);
    List<Alerta> findByArbolId(Long arbolId);
    List<Alerta> findByUsuarioId(Long usuarioId);
    long countByResueltaFalse();
    long countByNivelAndResueltaFalse(String nivel);
}
