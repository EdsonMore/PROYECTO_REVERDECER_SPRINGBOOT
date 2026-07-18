package com.example.Proyecto_Reverdecer.repository;

import com.example.Proyecto_Reverdecer.model.Seguimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeguimientoRepository extends JpaRepository<Seguimiento, Long> {

    List<Seguimiento> findByUsuarioIdOrderByFechaSeguimientoDesc(Long usuarioId);

    List<Seguimiento> findByArbolIdOrderByFechaSeguimientoDesc(Long arbolId);

    @Query("SELECT s FROM Seguimiento s WHERE s.usuario.id = :usuarioId AND s.arbol.id = :arbolId ORDER BY s.fechaSeguimiento DESC")
    List<Seguimiento> findByUsuarioIdAndArbolId(@Param("usuarioId") Long usuarioId, @Param("arbolId") Long arbolId);

    @Query("SELECT s FROM Seguimiento s WHERE s.arbol.id = :arbolId ORDER BY s.fechaSeguimiento DESC")
    List<Seguimiento> findUltimosPorArbol(@Param("arbolId") Long arbolId);

    long countByArbolId(Long arbolId);

    long countByUsuarioId(Long usuarioId);
}
