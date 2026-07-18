package com.example.Proyecto_Reverdecer.repository;

import com.example.Proyecto_Reverdecer.model.LecturaSensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LecturaSensorRepository extends JpaRepository<LecturaSensor, Long> {
    List<LecturaSensor> findByDispositivoIdOrderByFechaLecturaDesc(Long dispositivoId);
    List<LecturaSensor> findByDispositivoIdAndFechaLecturaBetweenOrderByFechaLecturaAsc(
        Long dispositivoId, LocalDateTime inicio, LocalDateTime fin);
    List<LecturaSensor> findByFechaLecturaAfter(LocalDateTime fecha);
    long countByDispositivoId(Long dispositivoId);
}
