package com.example.Proyecto_Reverdecer.repository;

import com.example.Proyecto_Reverdecer.model.Riego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RiegoRepository extends JpaRepository<Riego, Long> {
    List<Riego> findByDispositivoIdOrderByFechaProgramadoDesc(Long dispositivoId);
    List<Riego> findByEstado(String estado);
    List<Riego> findByFechaEjecutadoAfter(LocalDateTime fecha);
    long countByEstado(String estado);
}
