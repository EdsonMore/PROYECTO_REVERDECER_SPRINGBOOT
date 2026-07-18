package com.example.Proyecto_Reverdecer.service;

import com.example.Proyecto_Reverdecer.model.AuditoriaAcceso;
import com.example.Proyecto_Reverdecer.repository.AuditoriaAccesoRepository;
import com.example.Proyecto_Reverdecer.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditoriaAccesoService {

    @Autowired
    private AuditoriaAccesoRepository auditoriaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<AuditoriaAcceso> listarTodas() {
        return auditoriaRepository.findAllByOrderByFechaDesc();
    }

    public List<AuditoriaAcceso> listarPorUsuario(Long usuarioId) {
        return auditoriaRepository.findByUsuarioIdOrderByFechaDesc(usuarioId);
    }

    public List<AuditoriaAcceso> listarPorAccion(String accion) {
        return auditoriaRepository.findByAccion(accion);
    }

    public List<AuditoriaAcceso> listarRecientes() {
        return auditoriaRepository.findByFechaAfterOrderByFechaDesc(LocalDateTime.now().minusDays(7));
    }

    public AuditoriaAcceso registrar(String accion, String detalle, String ip, String endpoint, Long usuarioId) {
        AuditoriaAcceso log = new AuditoriaAcceso();
        log.setAccion(accion);
        log.setDetalle(detalle);
        log.setIp(ip);
        log.setEndpoint(endpoint);
        log.setFecha(LocalDateTime.now());
        if (usuarioId != null) {
            usuarioRepository.findById(usuarioId).ifPresent(log::setUsuario);
        }
        return auditoriaRepository.save(log);
    }

    public AuditoriaAcceso registrarSinUsuario(String accion, String detalle, String ip, String endpoint) {
        AuditoriaAcceso log = new AuditoriaAcceso();
        log.setAccion(accion);
        log.setDetalle(detalle);
        log.setIp(ip);
        log.setEndpoint(endpoint);
        log.setFecha(LocalDateTime.now());
        return auditoriaRepository.save(log);
    }

    public long contarPorAccion(String accion) {
        return auditoriaRepository.countByAccion(accion);
    }

    public void eliminar(Long id) {
        auditoriaRepository.deleteById(id);
    }
}
