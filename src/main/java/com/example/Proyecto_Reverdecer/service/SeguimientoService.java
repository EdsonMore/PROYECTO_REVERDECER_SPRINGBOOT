package com.example.Proyecto_Reverdecer.service;

import com.example.Proyecto_Reverdecer.model.Arbol;
import com.example.Proyecto_Reverdecer.model.Seguimiento;
import com.example.Proyecto_Reverdecer.model.Usuario;
import com.example.Proyecto_Reverdecer.repository.ArbolRepository;
import com.example.Proyecto_Reverdecer.repository.SeguimientoRepository;
import com.example.Proyecto_Reverdecer.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SeguimientoService {

    @Autowired
    private SeguimientoRepository seguimientoRepository;

    @Autowired
    private ArbolRepository arbolRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Seguimiento> listarPorUsuario(Long usuarioId) {
        return seguimientoRepository.findByUsuarioIdOrderByFechaSeguimientoDesc(usuarioId);
    }

    public List<Seguimiento> listarPorArbol(Long arbolId) {
        return seguimientoRepository.findByArbolIdOrderByFechaSeguimientoDesc(arbolId);
    }

    public List<Seguimiento> listarPorUsuarioYArbol(Long usuarioId, Long arbolId) {
        return seguimientoRepository.findByUsuarioIdAndArbolId(usuarioId, arbolId);
    }

    public Seguimiento obtenerPorId(Long id) {
        return seguimientoRepository.findById(id).orElse(null);
    }

    public Seguimiento crear(Seguimiento seguimiento, Long usuarioId) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }

        Optional<Arbol> arbolOpt = arbolRepository.findById(seguimiento.getArbol().getId());
        if (arbolOpt.isEmpty()) {
            throw new RuntimeException("Árbol no encontrado");
        }

        seguimiento.setUsuario(usuarioOpt.get());
        seguimiento.setArbol(arbolOpt.get());
        seguimiento.setCreadoEn(LocalDateTime.now());
        seguimiento.setActualizadoEn(LocalDateTime.now());
        if (seguimiento.getFechaSeguimiento() == null) {
            seguimiento.setFechaSeguimiento(LocalDate.now());
        }

        return seguimientoRepository.save(seguimiento);
    }

    public Seguimiento actualizar(Long id, Seguimiento seguimiento, Long usuarioId) {
        Seguimiento existente = seguimientoRepository.findById(id).orElse(null);
        if (existente == null) {
            return null;
        }

        if (!existente.getUsuario().getId().equals(usuarioId)) {
            return null;
        }

        if (seguimiento.getTitulo() != null) existente.setTitulo(seguimiento.getTitulo());
        if (seguimiento.getDescripcion() != null) existente.setDescripcion(seguimiento.getDescripcion());
        if (seguimiento.getFotoUrl() != null) existente.setFotoUrl(seguimiento.getFotoUrl());
        if (seguimiento.getAlturaCm() != null) existente.setAlturaCm(seguimiento.getAlturaCm());
        if (seguimiento.getSalud() != null) existente.setSalud(seguimiento.getSalud());
        if (seguimiento.getTipoSeguimiento() != null) existente.setTipoSeguimiento(seguimiento.getTipoSeguimiento());
        if (seguimiento.getFechaSeguimiento() != null) existente.setFechaSeguimiento(seguimiento.getFechaSeguimiento());
        if (seguimiento.getTemperaturaAmbiente() != null) existente.setTemperaturaAmbiente(seguimiento.getTemperaturaAmbiente());
        if (seguimiento.getHumedadSuelo() != null) existente.setHumedadSuelo(seguimiento.getHumedadSuelo());
        if (seguimiento.getNotasTecnicas() != null) existente.setNotasTecnicas(seguimiento.getNotasTecnicas());

        existente.setActualizadoEn(LocalDateTime.now());
        return seguimientoRepository.save(existente);
    }

    public boolean eliminar(Long id, Long usuarioId) {
        Seguimiento existente = seguimientoRepository.findById(id).orElse(null);
        if (existente == null) {
            return false;
        }

        if (!existente.getUsuario().getId().equals(usuarioId)) {
            return false;
        }

        seguimientoRepository.deleteById(id);
        return true;
    }

    public long contarPorArbol(Long arbolId) {
        return seguimientoRepository.countByArbolId(arbolId);
    }

    public long contarPorUsuario(Long usuarioId) {
        return seguimientoRepository.countByUsuarioId(usuarioId);
    }
}
