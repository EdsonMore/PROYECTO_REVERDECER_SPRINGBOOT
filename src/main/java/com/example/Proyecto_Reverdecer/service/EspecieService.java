package com.example.Proyecto_Reverdecer.service;

import com.example.Proyecto_Reverdecer.model.Especie;
import com.example.Proyecto_Reverdecer.repository.EspecieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EspecieService {

    @Autowired
    private EspecieRepository especieRepository;

    @Autowired
    private SpeciesEnricherService speciesEnricherService;

    public List<Especie> listarActivas() {
        return especieRepository.findByActivoTrueOrderByNombreComunAsc();
    }

    public List<Especie> listarCatalogo() {
        return especieRepository.findByActivoTrueAndAutoRegistradaFalseOrderByNombreComunAsc();
    }

    public List<Especie> buscar(String q) {
        if (q == null || q.trim().isEmpty()) {
            return listarCatalogo();
        }
        return especieRepository.buscar(q.trim());
    }

    public Optional<Especie> obtenerPorId(Long id) {
        return especieRepository.findById(id);
    }

    public Optional<Especie> buscarPorNombreComun(String nombre) {
        return especieRepository.findByNombreComunIgnoreCase(nombre);
    }

    public Especie guardar(Especie especie) {
        especie.setActivo(true);
        if (especie.getCreadoEn() == null) {
            especie.setCreadoEn(java.time.LocalDateTime.now());
        }
        return especieRepository.save(especie);
    }

    public Especie actualizar(Long id, Especie especie) {
        Especie existente = especieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especie no encontrada"));
        if (especie.getNombreComun() != null) existente.setNombreComun(especie.getNombreComun());
        if (especie.getNombreCientifico() != null) existente.setNombreCientifico(especie.getNombreCientifico());
        if (especie.getFamilia() != null) existente.setFamilia(especie.getFamilia());
        if (especie.getOrigen() != null) existente.setOrigen(especie.getOrigen());
        if (especie.getClima() != null) existente.setClima(especie.getClima());
        if (especie.getDescripcion() != null) existente.setDescripcion(especie.getDescripcion());
        if (especie.getAlturaMaximaCm() != null) existente.setAlturaMaximaCm(especie.getAlturaMaximaCm());
        if (especie.getCrecimiento() != null) existente.setCrecimiento(especie.getCrecimiento());
        if (especie.getRequiereAgua() != null) existente.setRequiereAgua(especie.getRequiereAgua());
        if (especie.getToleranciaSequia() != null) existente.setToleranciaSequia(especie.getToleranciaSequia());
        if (especie.getUsoPrincipal() != null) existente.setUsoPrincipal(especie.getUsoPrincipal());
        if (especie.getTempMin() != null) existente.setTempMin(especie.getTempMin());
        if (especie.getTempMax() != null) existente.setTempMax(especie.getTempMax());
        if (especie.getProbabilidadSupervivencia() != null) existente.setProbabilidadSupervivencia(especie.getProbabilidadSupervivencia());
        if (especie.getActivo() != null) existente.setActivo(especie.getActivo());
        return especieRepository.save(existente);
    }

    public void desactivar(Long id) {
        Especie existente = especieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especie no encontrada"));
        existente.setActivo(false);
        especieRepository.save(existente);
    }

    public Optional<Especie> buscarPorNombreCientifico(String nombre) {
        return especieRepository.findByNombreCientificoIgnoreCase(nombre);
    }

    public Especie autoRegistrar(String nombreComun, String nombreCientifico) {
        String cientifico = nombreCientifico != null ? nombreCientifico : nombreComun;
        Optional<Especie> existente = especieRepository.findByNombreCientificoIgnoreCase(cientifico);
        if (existente.isPresent()) {
            return existente.get();
        }
        Especie especie = new Especie();
        especie.setNombreComun(nombreComun);
        especie.setNombreCientifico(cientifico);
        especie.setAutoRegistrada(true);
        especie.setActivo(true);
        speciesEnricherService.enriquecer(especie);
        return especieRepository.save(especie);
    }

    public long contarActivas() {
        return especieRepository.countByActivoTrue();
    }
}
