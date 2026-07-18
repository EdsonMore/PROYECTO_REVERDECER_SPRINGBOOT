package com.example.Proyecto_Reverdecer.service;

import com.example.Proyecto_Reverdecer.model.Especie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SupervivenciaService {

    @Autowired
    private ClimaService climaService;

    public Map<String, Object> calcularSupervivencia(Long especieId, Especie especie, double lat, double lng) {
        Map<String, Object> climaActual = climaService.obtenerClima(lat, lng);
        Map<String, Object> climaHistorico = climaService.obtenerClimaHistorico(lat, lng);

        if (!Boolean.TRUE.equals(climaActual.get("success"))) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", "No se pudo obtener datos climáticos para la ubicación");
            return error;
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> clima = (Map<String, Object>) climaActual.get("clima");

        double temperatura = toDouble(clima.get("temperatura"));
        double humedad = toDouble(clima.get("humedad"));
        double precipitacion = toDouble(clima.get("precipitacion"));
        double viento = toDouble(clima.get("viento_velocidad"));
        double indiceUV = toDouble(clima.get("indice_uv"));
        double sensacionTermica = toDouble(clima.get("sensacion_termica"));

        double tempMaxHistorico = toDouble(climaHistorico.get("temp_max_promedio"));
        double tempMinHistorico = toDouble(climaHistorico.get("temp_min_promedio"));
        double humedadHistorica = toDouble(climaHistorico.get("humedad_promedio"));

        double tempMinEspecie = especie != null && especie.getTempMin() != null ? especie.getTempMin() : 8.0;
        double tempMaxEspecie = especie != null && especie.getTempMax() != null ? especie.getTempMax() : 40.0;
        String toleranciaSequia = especie != null ? especie.getToleranciaSequia() : "Media";
        String requiereAgua = especie != null ? especie.getRequiereAgua() : "Media";
        String climaEspecie = especie != null ? especie.getClima() : "Tropical";
        String origen = especie != null ? especie.getOrigen() : "Piura";
        Integer probBase = especie != null ? especie.getProbabilidadSupervivencia() : null;

        List<String> factores = new ArrayList<>();
        int puntaje = 50;

        puntaje += evaluarTemperatura(temperatura, tempMinEspecie, tempMaxEspecie, factores, tempMinHistorico, tempMaxHistorico);
        puntaje += evaluarHumedad(humedad, humedadHistorica, factores);
        puntaje += evaluarPrecipitacion(precipitacion, toleranciaSequia, factores);
        puntaje += evaluarViento(viento, factores);
        puntaje += evaluarUV(indiceUV, factores);
        puntaje += evaluarOrigen(origen, factores);
        puntaje += evaluarClima(climaEspecie, temperatura, humedad, factores);
        puntaje += evaluarAgua(requiereAgua, precipitacion, factores);
        puntaje += evaluarSequia(toleranciaSequia, precipitacion, factores);

        puntaje = Math.max(0, Math.min(100, puntaje));

        // Si hay una probabilidad base de la especie, promediar
        if (probBase != null && probBase > 0) {
            puntaje = (puntaje + probBase) / 2;
        }

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("success", true);
        resultado.put("timestamp", climaActual.get("timestamp"));
        resultado.put("coordinates", Map.of("latitude", lat, "longitude", lng));
        resultado.put("clima_actual", clima);
        resultado.put("clima_historico", climaHistorico);

        Map<String, Object> supervivencia = new HashMap<>();
        supervivencia.put("puntaje", puntaje);
        supervivencia.put("nivel", getNivel(puntaje));
        supervivencia.put("color", getColor(puntaje));
        supervivencia.put("factores", factores);
        supervivencia.put("recomendacion", getRecomendacion(puntaje, factores));
        resultado.put("supervivencia", supervivencia);

        return resultado;
    }

    private int evaluarTemperatura(double tempActual, double tempMinEsp, double tempMaxEsp,
                                    List<String> factores, double tempMinHist, double tempMaxHist) {
        double tempOptimaMin = tempMinEsp + (tempMaxEsp - tempMinEsp) * 0.3;
        double tempOptimaMax = tempMinEsp + (tempMaxEsp - tempMinEsp) * 0.7;

        if (tempActual >= tempOptimaMin && tempActual <= tempOptimaMax) {
            factores.add(String.format("Temperatura actual (%.1f°C) dentro del rango óptimo (%.1f-%.1f°C)", tempActual, tempOptimaMin, tempOptimaMax));
            return 30;
        } else if (tempActual >= tempMinEsp && tempActual <= tempMaxEsp) {
            factores.add(String.format("Temperatura actual (%.1f°C) aceptable dentro del rango de tolerancia (%.1f-%.1f°C)", tempActual, tempMinEsp, tempMaxEsp));
            return 15;
        } else if (tempActual < tempMinEsp) {
            factores.add(String.format("Temperatura actual (%.1f°C) por debajo del mínimo recomendado (%.1f°C)", tempActual, tempMinEsp));
            return 0;
        } else {
            factores.add(String.format("Temperatura actual (%.1f°C) por encima del máximo recomendado (%.1f°C)", tempActual, tempMaxEsp));
            return 5;
        }
    }

    private int evaluarHumedad(double humedad, double humedadHistorica, List<String> factores) {
        if (humedad >= 40 && humedad <= 80) {
            factores.add(String.format("Humedad relativa (%.0f%%) dentro del rango adecuado (40-80%%)", humedad));
            return 15;
        } else if (humedad > 80) {
            factores.add(String.format("Humedad relativa alta (%.0f%%) - puede favorecer hongos", humedad));
            return 5;
        } else {
            factores.add(String.format("Humedad relativa baja (%.0f%%) - propia del clima de Piura", humedad));
            return 10;
        }
    }

    private int evaluarPrecipitacion(double precipitacion, String toleranciaSequia, List<String> factores) {
        if (precipitacion > 0) {
            if ("Muy Alta".equals(toleranciaSequia) || "Alta".equals(toleranciaSequia)) {
                factores.add(String.format("Precipitación (%.1f mm) - lluvia beneficiosa para especie tolerante a sequía", precipitacion));
                return 10;
            } else {
                factores.add(String.format("Precipitación actual (%.1f mm) - favorable", precipitacion));
                return 10;
            }
        } else {
            if ("Muy Alta".equals(toleranciaSequia)) {
                factores.add("Sin precipitación actual - especie muy tolerante a sequía, ideal para clima seco");
                return 10;
            } else if ("Alta".equals(toleranciaSequia)) {
                factores.add("Sin precipitación actual - especie tolerante a periodos secos");
                return 5;
            } else if ("Media".equals(toleranciaSequia)) {
                factores.add("Sin precipitación - especie con tolerancia media, requiere riego complementario");
                return 0;
            } else {
                factores.add("Sin precipitación - especie sensible a sequía, requiere riego frecuente");
                return -5;
            }
        }
    }

    private int evaluarViento(double viento, List<String> factores) {
        if (viento > 30) {
            factores.add(String.format("Viento fuerte (%.0f km/h) - riesgo de daño estructural", viento));
            return -10;
        } else if (viento > 20) {
            factores.add(String.format("Viento moderado (%.0f km/h) - monitorear estabilidad", viento));
            return 0;
        } else {
            factores.add(String.format("Viento favorable (%.0f km/h)", viento));
            return 5;
        }
    }

    private int evaluarUV(double indiceUV, List<String> factores) {
        if (indiceUV > 8) {
            factores.add(String.format("Radiación UV muy alta (%.1f) - proteger especies sensibles", indiceUV));
            return 0;
        } else if (indiceUV > 5) {
            factores.add(String.format("Radiación UV moderada (%.1f) - aceptable para especies nativas", indiceUV));
            return 3;
        } else {
            factores.add(String.format("Radiación UV baja (%.1f) - condiciones favorables", indiceUV));
            return 5;
        }
    }

    private int evaluarOrigen(String origen, List<String> factores) {
        if (origen == null) {
            factores.add("Origen no especificado");
            return 0;
        }
        String o = origen.toLowerCase();
        if (o.contains("piura") || o.contains("perú") || o.contains("peru") || o.contains("nativo")) {
            factores.add("Especie nativa de la región - perfectamente adaptada al ecosistema local");
            return 15;
        } else if (o.contains("sudamer") || o.contains("tropical") || o.contains("américa")) {
            factores.add("Especie de origen tropical americano - buena adaptabilidad potencial");
            return 8;
        } else {
            factores.add("Especie introducida - puede requerir cuidados adicionales");
            return 3;
        }
    }

    private int evaluarClima(String climaEsp, double temperatura, double humedad, List<String> factores) {
        if (climaEsp == null) return 0;
        String c = climaEsp.toLowerCase();
        boolean esArido = c.contains("árido") || c.contains("arido") || c.contains("seco");
        boolean esTropical = c.contains("tropical") || c.contains("cálido") || c.contains("calido");
        boolean esTemplado = c.contains("templado") || c.contains("subtropical");

        if (esArido && temperatura > 25 && humedad < 60) {
            factores.add("Clima árido coincide con las condiciones actuales");
            return 10;
        } else if (esTropical && temperatura > 20 && humedad > 40) {
            factores.add("Clima tropical coincide con las condiciones actuales");
            return 10;
        } else if (esTemplado && temperatura > 10 && temperatura < 30) {
            factores.add("Clima templado compatible con la ubicación");
            return 5;
        }
        return 0;
    }

    private int evaluarAgua(String requiereAgua, double precipitacion, List<String> factores) {
        if (requiereAgua == null) return 0;
        switch (requiereAgua) {
            case "Baja":
                factores.add("Bajo requerimiento de agua - ideal para la región");
                return 10;
            case "Media":
                factores.add("Requerimiento hídrico moderado");
                return 5;
            case "Alta":
                if (precipitacion < 1) {
                    factores.add("Alto requerimiento de agua - necesitará riego constante");
                    return 0;
                }
                factores.add("Alto requerimiento de agua - lluvia actual ayuda");
                return 3;
            default:
                return 0;
        }
    }

    private int evaluarSequia(String toleranciaSequia, double precipitacion, List<String> factores) {
        if (toleranciaSequia == null) return 0;
        switch (toleranciaSequia) {
            case "Muy Alta":
                factores.add("Tolerancia a sequía muy alta - ideal para clima piurano");
                return 10;
            case "Alta":
                factores.add("Buena tolerancia a periodos secos");
                return 8;
            case "Media":
                if (precipitacion > 0) {
                    factores.add("Tolerancia media a sequía - la lluvia actual ayuda");
                    return 3;
                }
                return 0;
            case "Baja":
                if (precipitacion < 1) {
                    factores.add("Sensible a sequía - requiere riego frecuente");
                    return -5;
                }
                return 0;
            default:
                return 0;
        }
    }

    private String getNivel(int puntaje) {
        if (puntaje >= 80) return "Muy Alta";
        if (puntaje >= 60) return "Alta";
        if (puntaje >= 40) return "Moderada";
        if (puntaje >= 20) return "Baja";
        return "Muy Baja";
    }

    private String getColor(int puntaje) {
        if (puntaje >= 80) return "#28a745";
        if (puntaje >= 60) return "#5cb85c";
        if (puntaje >= 40) return "#f0ad4e";
        if (puntaje >= 20) return "#d9534f";
        return "#c9302c";
    }

    private String getRecomendacion(int puntaje, List<String> factores) {
        if (puntaje >= 80) {
            return "Esta especie tiene una probabilidad muy alta de sobrevivir en esta ubicación. Las condiciones climáticas actuales son favorables y la especie está bien adaptada al ecosistema local.";
        } else if (puntaje >= 60) {
            return "La especie tiene buenas probabilidades de sobrevivir. Se recomienda riego complementario durante los meses más secos y monitoreo periódico.";
        } else if (puntaje >= 40) {
            return "La supervivencia es posible pero requerirá cuidados adicionales como riego frecuente, protección solar temporal y monitoreo constante de plagas.";
        } else if (puntaje >= 20) {
            return "Las condiciones no son las ideales para esta especie. Se recomienda considerar especies nativas mejor adaptadas al clima local de Piura.";
        } else {
            return "Esta especie tiene pocas probabilidades de sobrevivir en esta ubicación. Se sugiere elegir una especie diferente del catálogo de especies nativas de Piura.";
        }
    }

    private double toDouble(Object value) {
        if (value == null) return 0.0;
        if (value instanceof Number) return ((Number) value).doubleValue();
        try {
            return Double.parseDouble(value.toString());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}