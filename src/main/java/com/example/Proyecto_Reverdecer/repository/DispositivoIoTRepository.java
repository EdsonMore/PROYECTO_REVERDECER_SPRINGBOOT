package com.example.Proyecto_Reverdecer.repository;

import com.example.Proyecto_Reverdecer.model.DispositivoIoT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DispositivoIoTRepository extends JpaRepository<DispositivoIoT, Long> {
    List<DispositivoIoT> findByActivoTrue();
    List<DispositivoIoT> findByTipo(String tipo);
    List<DispositivoIoT> findByUsuarioId(Long usuarioId);
    long countByActivoTrue();
}
