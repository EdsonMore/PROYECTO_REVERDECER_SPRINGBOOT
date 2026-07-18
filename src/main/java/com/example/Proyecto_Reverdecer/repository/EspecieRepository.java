package com.example.Proyecto_Reverdecer.repository;

import com.example.Proyecto_Reverdecer.model.Especie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EspecieRepository extends JpaRepository<Especie, Long> {

    List<Especie> findByActivoTrueOrderByNombreComunAsc();

    List<Especie> findByActivoTrueAndAutoRegistradaFalseOrderByNombreComunAsc();

    Optional<Especie> findByNombreCientificoIgnoreCase(String nombreCientifico);

    Optional<Especie> findByNombreComunIgnoreCase(String nombreComun);

    @Query("SELECT e FROM Especie e WHERE e.activo = true AND (" +
           "LOWER(e.nombreComun) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(e.nombreCientifico) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(e.familia) LIKE LOWER(CONCAT('%', :q, '%'))) " +
           "ORDER BY e.nombreComun ASC")
    List<Especie> buscar(@Param("q") String q);

    long countByActivoTrue();

    boolean existsByNombreCientificoIgnoreCase(String nombreCientifico);
}
