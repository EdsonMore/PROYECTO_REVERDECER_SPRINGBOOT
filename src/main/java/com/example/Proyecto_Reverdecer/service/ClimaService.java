package com.example.Proyecto_Reverdecer.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.HashMap;

@Service
public class ClimaService {

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> obtenerClima(double lat, double lng) {
        try {
            String url = String.format(
                "https://api.open-meteo.com/v1/forecast?latitude=%f&longitude=%f&current=temperature_2m,relative_humidity_2m,precipitation,weather_code,wind_speed_10m,wind_gusts_10m,apparent_temperature,uv_index&temperature_unit=celsius&wind_speed_unit=kmh",
                lat, lng
            );
            String json = restTemplate.getForObject(url, String.class);

            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            Map<String, Object> data = mapper.readValue(json, new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {});

            @SuppressWarnings("unchecked")
            Map<String, Object> current = (Map<String, Object>) data.get("current");
            Map<String, Object> clima = new HashMap<>();

            if (current != null) {
                clima.put("temperatura", current.get("temperature_2m"));
                clima.put("sensacion_termica", current.get("apparent_temperature"));
                clima.put("humedad", current.get("relative_humidity_2m"));
                clima.put("precipitacion", current.get("precipitation"));
                clima.put("viento_velocidad", current.get("wind_speed_10m"));
                clima.put("viento_rafagas", current.get("wind_gusts_10m"));
                clima.put("indice_uv", current.get("uv_index"));
                clima.put("descripcion", descripcionClima(
                    current.get("weather_code") != null ? ((Number) current.get("weather_code")).intValue() : null
                ));
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("timestamp", new java.util.Date().toInstant().toString());
            response.put("coordinates", Map.of("latitude", lat, "longitude", lng));
            response.put("clima", clima);
            return response;
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", "Error al obtener datos climáticos: " + e.getMessage());
            return error;
        }
    }

    public Map<String, Object> obtenerClimaHistorico(double lat, double lng) {
        try {
            String url = String.format(
                "https://api.open-meteo.com/v1/forecast?latitude=%f&longitude=%f&daily=temperature_2m_max,temperature_2m_min,precipitation_sum,precipitation_hours,wind_speed_10m_max,relative_humidity_2m_mean&daily=1&timezone=America/Lima&temperature_unit=celsius&wind_speed_unit=kmh&forecast_days=7",
                lat, lng
            );
            String json = restTemplate.getForObject(url, String.class);

            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            Map<String, Object> data = mapper.readValue(json, new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {});

            @SuppressWarnings("unchecked")
            Map<String, Object> daily = (Map<String, Object>) data.get("daily");
            Map<String, Object> historico = new HashMap<>();

            if (daily != null) {
                historico.put("temp_max_promedio", promedioLista((java.util.List<Number>) daily.get("temperature_2m_max")));
                historico.put("temp_min_promedio", promedioLista((java.util.List<Number>) daily.get("temperature_2m_min")));
                historico.put("precipitacion_total", sumarLista((java.util.List<Number>) daily.get("precipitation_sum")));
                historico.put("humedad_promedio", promedioLista((java.util.List<Number>) daily.get("relative_humidity_2m_mean")));
                historico.put("viento_max_promedio", promedioLista((java.util.List<Number>) daily.get("wind_speed_10m_max")));
                historico.put("dias_pronostico", ((java.util.List<?>) daily.get("time")).size());
            }

            return historico;
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Error al obtener clima histórico: " + e.getMessage());
            return error;
        }
    }

    private Double promedioLista(java.util.List<Number> valores) {
        if (valores == null || valores.isEmpty()) return null;
        return valores.stream()
            .filter(v -> v != null)
            .mapToDouble(Number::doubleValue)
            .average()
            .orElse(0.0);
    }

    private Double sumarLista(java.util.List<Number> valores) {
        if (valores == null || valores.isEmpty()) return null;
        return valores.stream()
            .filter(v -> v != null)
            .mapToDouble(Number::doubleValue)
            .sum();
    }

    private String descripcionClima(Integer code) {
        if (code == null) return "Desconocido";
        Map<Integer, String> codigos = new HashMap<>();
        codigos.put(0, "Cielo despejado");
        codigos.put(1, "Principalmente despejado");
        codigos.put(2, "Parcialmente nublado");
        codigos.put(3, "Nublado");
        codigos.put(45, "Niebla");
        codigos.put(48, "Niebla con escarcha");
        codigos.put(51, "Llovizna ligera");
        codigos.put(53, "Llovizna moderada");
        codigos.put(55, "Llovizna densa");
        codigos.put(61, "Lluvia ligera");
        codigos.put(63, "Lluvia moderada");
        codigos.put(65, "Lluvia densa");
        codigos.put(71, "Nieve ligera");
        codigos.put(73, "Nieve moderada");
        codigos.put(75, "Nieve densa");
        codigos.put(77, "Granos de nieve");
        codigos.put(80, "Lluvia ligera e intermitente");
        codigos.put(81, "Lluvia moderada e intermitente");
        codigos.put(82, "Lluvia densa e intermitente");
        codigos.put(85, "Nieve ligera intermitente");
        codigos.put(86, "Nieve moderada intermitente");
        codigos.put(95, "Tormenta");
        codigos.put(96, "Tormenta con granizo ligero");
        codigos.put(99, "Tormenta con granizo densa");
        return codigos.getOrDefault(code, "Desconocido");
    }
}
