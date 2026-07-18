package com.example.Proyecto_Reverdecer.service;

import com.example.Proyecto_Reverdecer.model.DispositivoIoT;
import com.example.Proyecto_Reverdecer.repository.DispositivoIoTRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DispositivoIoTService {

    @Autowired
    private DispositivoIoTRepository dispositivoIoTRepository;

    public List<DispositivoIoT> listarTodos() {
        return dispositivoIoTRepository.findAll();
    }

    public List<DispositivoIoT> listarActivos() {
        return dispositivoIoTRepository.findByActivoTrue();
    }

    public Optional<DispositivoIoT> obtenerPorId(Long id) {
        return dispositivoIoTRepository.findById(id);
    }

    public DispositivoIoT guardar(DispositivoIoT dispositivo) {
        if (dispositivo.getFechaRegistro() == null) {
            dispositivo.setFechaRegistro(LocalDate.now());
        }
        if (dispositivo.getActivo() == null) {
            dispositivo.setActivo(true);
        }
        return dispositivoIoTRepository.save(dispositivo);
    }

    public DispositivoIoT actualizar(Long id, DispositivoIoT dispositivo) {
        return dispositivoIoTRepository.findById(id).map(d -> {
            d.setNombre(dispositivo.getNombre());
            d.setTipo(dispositivo.getTipo());
            d.setUbicacion(dispositivo.getUbicacion());
            d.setLatitud(dispositivo.getLatitud());
            d.setLongitud(dispositivo.getLongitud());
            d.setActivo(dispositivo.getActivo());
            if (dispositivo.getUsuario() != null) {
                d.setUsuario(dispositivo.getUsuario());
            }
            return dispositivoIoTRepository.save(d);
        }).orElseThrow(() -> new RuntimeException("Dispositivo no encontrado"));
    }

    public void eliminar(Long id) {
        dispositivoIoTRepository.deleteById(id);
    }

    public long contarActivos() {
        return dispositivoIoTRepository.countByActivoTrue();
    }
}
