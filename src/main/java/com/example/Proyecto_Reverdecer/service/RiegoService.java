package com.example.Proyecto_Reverdecer.service;

import com.example.Proyecto_Reverdecer.model.Riego;
import com.example.Proyecto_Reverdecer.repository.RiegoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RiegoService {

    @Autowired
    private RiegoRepository riegoRepository;

    public List<Riego> listarTodos() {
        return riegoRepository.findAll();
    }

    public Optional<Riego> obtenerPorId(Long id) {
        return riegoRepository.findById(id);
    }

    public List<Riego> listarPorDispositivo(Long dispositivoId) {
        return riegoRepository.findByDispositivoIdOrderByFechaProgramadoDesc(dispositivoId);
    }

    public Riego programar(Riego riego) {
        if (riego.getEstado() == null) {
            riego.setEstado("PROGRAMADO");
        }
        return riegoRepository.save(riego);
    }

    public Riego ejecutar(Long id) {
        return riegoRepository.findById(id).map(r -> {
            r.setActivado(true);
            r.setEstado("EJECUTADO");
            r.setFechaEjecutado(LocalDateTime.now());
            return riegoRepository.save(r);
        }).orElseThrow(() -> new RuntimeException("Riego no encontrado"));
    }

    public Riego cancelar(Long id) {
        return riegoRepository.findById(id).map(r -> {
            r.setActivado(false);
            r.setEstado("CANCELADO");
            return riegoRepository.save(r);
        }).orElseThrow(() -> new RuntimeException("Riego no encontrado"));
    }

    public long contarPorEstado(String estado) {
        return riegoRepository.countByEstado(estado);
    }

    public void eliminar(Long id) {
        riegoRepository.deleteById(id);
    }
}
