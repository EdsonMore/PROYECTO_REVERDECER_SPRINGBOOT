package com.example.Proyecto_Reverdecer.service;

import com.example.Proyecto_Reverdecer.model.Arbol;
import com.example.Proyecto_Reverdecer.repository.ArbolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GestorAmbientalService {

    //repositorio para consultar arboles
    @Autowired
    private ArbolRepository arbolRepository;

    //cuenta el total de árboles registrados
    public long totalArboles() {
        return arbolRepository.count();
    }


    //agrupa los arboles por especie, zona y estado
    public Map<String, Long> arbolesPorEspecie() {
        return arbolRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        a -> a.getEspecie() != null ? a.getEspecie() : "Sin especie",
                        Collectors.counting()));
    }

    public Map<String, Long> arbolesPorZona() {
        return arbolRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        a -> extraerZona(a.getUbicacion()),
                        Collectors.counting()));
    }

    public Map<String, Long> arbolesPorEstado() {
        return arbolRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        a -> a.getEstado() != null ? a.getEstado() : "Desconocido",
                        Collectors.counting()));
    }

    //porcentaje de árboles en riesgo
    public double porcentajeEnRiesgo() {
        long total = arbolRepository.count();
        if (total == 0) return 0.0;

        long enRiesgo = arbolRepository.findAll().stream()
                .filter(a -> esEstadoRiesgo(a.getEstado()))
                .count();

        return Math.round((enRiesgo * 100.0 / total) * 10.0) / 10.0;
    }


    //obtiene las zonas más criticas
    public Map<String, Long> zonasCriticas() {
        return arbolRepository.findAll().stream()
                .filter(a -> esEstadoRiesgo(a.getEstado()))
                .collect(Collectors.groupingBy(
                        a -> extraerZona(a.getUbicacion()),
                        Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));
    }


    //porcentaje de árboles en riesgo por zona
    public Map<String, Double> porcentajeRiesgoPorZona() {
        Map<String, Long> totalPorZona = arbolesPorZona();
        Map<String, Long> riesgoPorZona = zonasCriticas();

        Map<String, Double> resultado = new LinkedHashMap<>();
        for (Map.Entry<String, Long> entry : totalPorZona.entrySet()) {
            String zona = entry.getKey();
            long total = entry.getValue();
            long riesgo = riesgoPorZona.getOrDefault(zona, 0L);
            double pct = total > 0 ? Math.round((riesgo * 100.0 / total) * 10.0) / 10.0 : 0.0;
            resultado.put(zona, pct);
        }

        return resultado.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));
    }

    //especies con mejor supervivencia
    public Map<String, Double> especiesConMejorSupervivencia() {
        Map<String, List<Arbol>> porEspecie = arbolRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        a -> a.getEspecie() != null ? a.getEspecie() : "Sin especie"));

        Map<String, Double> resultado = new LinkedHashMap<>();
        for (Map.Entry<String, List<Arbol>> entry : porEspecie.entrySet()) {
            String especie = entry.getKey();
            List<Arbol> arboles = entry.getValue();
            long saludables = arboles.stream()
                    .filter(a -> esEstadoSaludable(a.getEstado()))
                    .count();
            double pct = Math.round((saludables * 100.0 / arboles.size()) * 10.0) / 10.0;
            resultado.put(especie, pct);
        }

        return resultado.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));
    }

    //cuenta especies activas por conteo 
    public Map<String, Long> especiesActivasPorConteo() {
        return arbolRepository.findAll().stream()
                .filter(a -> !"MUERTO".equalsIgnoreCase(a.getEstado()))
                .collect(Collectors.groupingBy(
                        a -> a.getEspecie() != null ? a.getEspecie() : "Sin especie",
                        Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));
    }

    //  Alertas 
    public List<Arbol> arbolesConAlertas() {
        return arbolRepository.findAll().stream()
                .filter(a -> esEstadoRiesgo(a.getEstado()))
                .sorted(Comparator.comparing(
                        a -> severidadEstado(a.getEstado()),
                        Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    public Map<String, Long> alertasPorZona() {
        return arbolRepository.findAll().stream()
                .filter(a -> esEstadoRiesgo(a.getEstado()))
                .collect(Collectors.groupingBy(
                        a -> extraerZona(a.getUbicacion()),
                        Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));
    }

    //  Resumen general del dashboard 

    public Map<String, Object> resumenDashboard() {
        Map<String, Object> resumen = new LinkedHashMap<>();

        long total = arbolRepository.count();
        long saludables = arbolRepository.findAll().stream()
                .filter(a -> esEstadoSaludable(a.getEstado()))
                .count();
        long enRiesgo = arbolRepository.findAll().stream()
                .filter(a -> esEstadoRiesgo(a.getEstado()))
                .count();
        long muertos = arbolRepository.countByEstado("MUERTO");

        resumen.put("totalArboles", total);
        resumen.put("arbolesActivos", saludables);
        resumen.put("arbolesEnRiesgo", enRiesgo);
        resumen.put("arbolesMuertos", muertos);
        resumen.put("porcentajeRiesgo", porcentajeEnRiesgo());
        resumen.put("totalEspecies", arbolesPorEspecie().size());
        resumen.put("totalZonas", arbolesPorZona().size());
        resumen.put("totalAlertas", arbolesConAlertas().size());

        return resumen;
    }


    private String extraerZona(String ubicacion) {
        if (ubicacion == null || ubicacion.isBlank()) return "Sin zona";
        String[] partes = ubicacion.split("[,\\-/]");
        return partes[0].trim().isEmpty() ? "Sin zona" : partes[0].trim();
    }

    private boolean esEstadoRiesgo(String estado) {
        if (estado == null) return false;
        String e = estado.toUpperCase();
        return e.equals("ENFERMO") || e.equals("CRITICO") || e.equals("MUERTO")
                || e.equals("RIESGO") || e.equals("EN_RIESGO");
    }

    private boolean esEstadoSaludable(String estado) {
        if (estado == null) return false;
        String e = estado.toUpperCase();
        return e.equals("BUENO") || e.equals("SALUDABLE") || e.equals("EXCELENTE")
                || e.equals("NORMAL") || e.equals("ACTIVO");
    }

    private int severidadEstado(String estado) {
        if (estado == null) return 0;
        return switch (estado.toUpperCase()) {
            case "MUERTO" -> 4;
            case "CRITICO" -> 3;
            case "ENFERMO" -> 2;
            case "RIESGO", "EN_RIESGO" -> 1;
            default -> 0;
        };
    }
}
