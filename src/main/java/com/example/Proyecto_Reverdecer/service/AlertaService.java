package com.example.Proyecto_Reverdecer.service;

import com.example.Proyecto_Reverdecer.model.Alerta;
import com.example.Proyecto_Reverdecer.repository.AlertaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AlertaService {

    @Autowired
    private AlertaRepository alertaRepository;

    public List<Alerta> listarTodas() {
        return alertaRepository.findAllByOrderByFechaCreacionDesc();
    }

    public List<Alerta> listarActivas() {
        return alertaRepository.findByResueltaFalseOrderByFechaCreacionDesc();
    }

    public List<Alerta> listarPorTipo(String tipo) {
        return alertaRepository.findByTipo(tipo);
    }

    public Optional<Alerta> obtenerPorId(Long id) {
        return alertaRepository.findById(id);
    }

    public Alerta crear(Alerta alerta) {
        if (alerta.getFechaCreacion() == null) {
            alerta.setFechaCreacion(LocalDateTime.now());
        }
        alerta.setLeida(false);
        alerta.setResuelta(false);
        return alertaRepository.save(alerta);
    }

    public Alerta marcarLeida(Long id) {
        return alertaRepository.findById(id).map(a -> {
            a.setLeida(true);
            return alertaRepository.save(a);
        }).orElseThrow(() -> new RuntimeException("Alerta no encontrada"));
    }

    public Alerta resolver(Long id) {
        return alertaRepository.findById(id).map(a -> {
            a.setResuelta(true);
            a.setFechaResolucion(LocalDateTime.now());
            return alertaRepository.save(a);
        }).orElseThrow(() -> new RuntimeException("Alerta no encontrada"));
    }

    public long contarActivas() {
        return alertaRepository.countByResueltaFalse();
    }

    public long contarPorNivel(String nivel) {
        return alertaRepository.countByNivelAndResueltaFalse(nivel);
    }

    public void eliminar(Long id) {
        alertaRepository.deleteById(id);
    }
}
