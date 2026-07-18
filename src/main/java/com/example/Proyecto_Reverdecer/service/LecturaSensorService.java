package com.example.Proyecto_Reverdecer.service;

import com.example.Proyecto_Reverdecer.model.LecturaSensor;
import com.example.Proyecto_Reverdecer.repository.LecturaSensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LecturaSensorService {

    @Autowired
    private LecturaSensorRepository lecturaSensorRepository;

    public List<LecturaSensor> listarPorDispositivo(Long dispositivoId) {
        return lecturaSensorRepository.findByDispositivoIdOrderByFechaLecturaDesc(dispositivoId);
    }

    public List<LecturaSensor> listarPorDispositivoYPeriodo(Long dispositivoId, LocalDateTime inicio, LocalDateTime fin) {
        return lecturaSensorRepository.findByDispositivoIdAndFechaLecturaBetweenOrderByFechaLecturaAsc(
            dispositivoId, inicio, fin);
    }

    public Optional<LecturaSensor> obtenerPorId(Long id) {
        return lecturaSensorRepository.findById(id);
    }

    public LecturaSensor guardar(LecturaSensor lectura) {
        if (lectura.getFechaLectura() == null) {
            lectura.setFechaLectura(LocalDateTime.now());
        }
        return lecturaSensorRepository.save(lectura);
    }

    public List<LecturaSensor> obtenerLecturasRecientes() {
        return lecturaSensorRepository.findByFechaLecturaAfter(LocalDateTime.now().minusHours(24));
    }

    public void eliminar(Long id) {
        lecturaSensorRepository.deleteById(id);
    }
}
