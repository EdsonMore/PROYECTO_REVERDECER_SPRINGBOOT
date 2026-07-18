package com.example.Proyecto_Reverdecer.service;

import com.example.Proyecto_Reverdecer.model.Especie;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SpeciesEnricherService {

    private static final Map<String, SpeciesProfile> CONOCIDAS = new HashMap<>();
    private static final List<SpeciesRule> REGLAS = new ArrayList<>();

    static {
        inicializarConocidas();
        inicializarReglas();
    }

    public void enriquecer(Especie especie) {
        String nombre = especie.getNombreComun() != null ? especie.getNombreComun().toLowerCase() : "";
        String cientifico = especie.getNombreCientifico() != null ? especie.getNombreCientifico().toLowerCase() : "";

        SpeciesProfile perfil = CONOCIDAS.get(nombre);
        if (perfil == null) {
            perfil = CONOCIDAS.get(cientifico);
        }

        if (perfil != null) {
            aplicarPerfil(especie, perfil);
            return;
        }

        for (SpeciesRule regla : REGLAS) {
            if (regla.coincide(nombre, cientifico)) {
                aplicarRegla(especie, regla);
                return;
            }
        }

        aplicarPerfilGenerico(especie);
    }

    private void aplicarPerfil(Especie especie, SpeciesProfile perfil) {
        if (especie.getNombreCientifico() == null || especie.getNombreCientifico().equals(especie.getNombreComun())) {
            especie.setNombreCientifico(perfil.cientifico);
        }
        if (especie.getFamilia() == null) especie.setFamilia(perfil.familia);
        if (especie.getOrigen() == null) especie.setOrigen(perfil.origen);
        if (especie.getClima() == null) especie.setClima(perfil.clima);
        if (especie.getTempMin() == null) especie.setTempMin(perfil.tempMin);
        if (especie.getTempMax() == null) especie.setTempMax(perfil.tempMax);
        if (especie.getAlturaMaximaCm() == null) especie.setAlturaMaximaCm(perfil.alturaMax);
        if (especie.getCrecimiento() == null) especie.setCrecimiento(perfil.crecimiento);
        if (especie.getRequiereAgua() == null) especie.setRequiereAgua(perfil.requiereAgua);
        if (especie.getToleranciaSequia() == null) especie.setToleranciaSequia(perfil.toleranciaSequia);
        if (especie.getUsoPrincipal() == null) especie.setUsoPrincipal(perfil.usoPrincipal);
        if (especie.getProbabilidadSupervivencia() == null) especie.setProbabilidadSupervivencia(perfil.probabilidad);
        if (especie.getDescripcion() == null) especie.setDescripcion(perfil.descripcion);
    }

    private void aplicarRegla(Especie especie, SpeciesRule regla) {
        if (especie.getNombreCientifico() == null || especie.getNombreCientifico().equals(especie.getNombreComun())) {
            especie.setNombreCientifico(regla.cientificoBase + " " + especie.getNombreComun());
        }
        if (especie.getFamilia() == null) especie.setFamilia(regla.familia);
        if (especie.getOrigen() == null) especie.setOrigen(regla.origen);
        if (especie.getClima() == null) especie.setClima(regla.clima);
        if (especie.getTempMin() == null) especie.setTempMin(regla.tempMin);
        if (especie.getTempMax() == null) especie.setTempMax(regla.tempMax);
        if (especie.getAlturaMaximaCm() == null) especie.setAlturaMaximaCm(regla.alturaMax);
        if (especie.getCrecimiento() == null) especie.setCrecimiento(regla.crecimiento);
        if (especie.getRequiereAgua() == null) especie.setRequiereAgua(regla.requiereAgua);
        if (especie.getToleranciaSequia() == null) especie.setToleranciaSequia(regla.toleranciaSequia);
        if (especie.getUsoPrincipal() == null) especie.setUsoPrincipal(regla.usoPrincipal);
        if (especie.getProbabilidadSupervivencia() == null) especie.setProbabilidadSupervivencia(regla.probabilidad);
        if (especie.getDescripcion() == null) {
            especie.setDescripcion(String.format(regla.descripcionTemplate, especie.getNombreComun()));
        }
    }

    private void aplicarPerfilGenerico(Especie especie) {
        String nombre = especie.getNombreComun() != null ? especie.getNombreComun().toLowerCase() : "";
        if (especie.getNombreCientifico() == null) {
            especie.setNombreCientifico(nombre.replace(' ', '_') + "_sp");
        }
        if (especie.getFamilia() == null) especie.setFamilia("Por determinar");
        if (especie.getOrigen() == null) especie.setOrigen("Piura, Perú");
        if (especie.getClima() == null) especie.setClima("Tropical-Árido");
        if (especie.getTempMin() == null) especie.setTempMin(15.0);
        if (especie.getTempMax() == null) especie.setTempMax(38.0);
        if (especie.getAlturaMaximaCm() == null) especie.setAlturaMaximaCm(800);
        if (especie.getCrecimiento() == null) especie.setCrecimiento("Medio");
        if (especie.getRequiereAgua() == null) especie.setRequiereAgua("Media");
        if (especie.getToleranciaSequia() == null) especie.setToleranciaSequia("Media");
        if (especie.getUsoPrincipal() == null) especie.setUsoPrincipal("Ornamental / Ambiental");
        if (especie.getProbabilidadSupervivencia() == null) especie.setProbabilidadSupervivencia(65);
        if (especie.getDescripcion() == null) {
            especie.setDescripcion(String.format("%s - especie registrada automáticamente para el catálogo de Piura.",
                    especie.getNombreComun()));
        }
    }

    private static void inicializarConocidas() {
        SpeciesProfile p;

        p = new SpeciesProfile();
        p.cientifico = "Mangifera indica";
        p.familia = "Anacardiaceae";
        p.origen = "India (adaptada a Piura)";
        p.clima = "Tropical";
        p.tempMin = 18.0; p.tempMax = 38.0;
        p.alturaMax = 3000; p.crecimiento = "Lento";
        p.requiereAgua = "Media"; p.toleranciaSequia = "Alta";
        p.usoPrincipal = "Frutal"; p.probabilidad = 80;
        p.descripcion = "Árbol frutal tropical de gran tamaño, muy cultivado en Piura por su adaptabilidad al clima seco costero.";
        CONOCIDAS.put("mango", p);
        CONOCIDAS.put("mangifera indica", p);
        CONOCIDAS.put("mangifera", p);

        p = new SpeciesProfile();
        p.cientifico = "Prosopis pallida";
        p.familia = "Fabaceae";
        p.origen = "Piura, Perú (Nativo)";
        p.clima = "Árido";
        p.tempMin = 10.0; p.tempMax = 45.0;
        p.alturaMax = 1500; p.crecimiento = "Lento";
        p.requiereAgua = "Baja"; p.toleranciaSequia = "Muy Alta";
        p.usoPrincipal = "Forestal / Forraje"; p.probabilidad = 95;
        p.descripcion = "Árbol nativo del bosque seco ecuatorial, emblemático de Piura. Altamente resistente a la sequía.";
        CONOCIDAS.put("algarrobo", p);
        CONOCIDAS.put("algarrobus", p);
        CONOCIDAS.put("prosopis pallida", p);

        p = new SpeciesProfile();
        p.cientifico = "Carica papaya";
        p.familia = "Caricaceae";
        p.origen = "América Central (adaptada a Piura)";
        p.clima = "Tropical";
        p.tempMin = 20.0; p.tempMax = 35.0;
        p.alturaMax = 1000; p.crecimiento = "Rápido";
        p.requiereAgua = "Alta"; p.toleranciaSequia = "Baja";
        p.usoPrincipal = "Frutal"; p.probabilidad = 70;
        p.descripcion = "Planta frutal de crecimiento rápido, requiere riego constante en clima seco.";
        CONOCIDAS.put("papaya", p);
        CONOCIDAS.put("carica papaya", p);

        p = new SpeciesProfile();
        p.cientifico = "Citrus aurantiifolia";
        p.familia = "Rutaceae";
        p.origen = "Sudeste Asiático (adaptado a Piura)";
        p.clima = "Tropical";
        p.tempMin = 15.0; p.tempMax = 38.0;
        p.alturaMax = 600; p.crecimiento = "Medio";
        p.requiereAgua = "Media"; p.toleranciaSequia = "Media";
        p.usoPrincipal = "Frutal / Cítrico"; p.probabilidad = 75;
        p.descripcion = "Árbol cítrico ampliamente cultivado en Piura. Resistente al clima seco con riego complementario.";
        CONOCIDAS.put("limón", p);
        CONOCIDAS.put("limon", p);
        CONOCIDAS.put("citrus aurantiifolia", p);

        p = new SpeciesProfile();
        p.cientifico = "Citrus sinensis";
        p.familia = "Rutaceae";
        p.origen = "Asia (adaptado a Piura)";
        p.clima = "Tropical";
        p.tempMin = 15.0; p.tempMax = 36.0;
        p.alturaMax = 1000; p.crecimiento = "Medio";
        p.requiereAgua = "Media"; p.toleranciaSequia = "Media";
        p.usoPrincipal = "Frutal / Cítrico"; p.probabilidad = 75;
        p.descripcion = "Naranjo dulce, cultivado en valles de Piura con riego tecnificado.";
        CONOCIDAS.put("naranja", p);
        CONOCIDAS.put("naranjo", p);
        CONOCIDAS.put("citrus sinensis", p);

        p = new SpeciesProfile();
        p.cientifico = "Persea americana";
        p.familia = "Lauraceae";
        p.origen = "México (adaptado a Piura)";
        p.clima = "Tropical";
        p.tempMin = 18.0; p.tempMax = 32.0;
        p.alturaMax = 2000; p.crecimiento = "Lento";
        p.requiereAgua = "Alta"; p.toleranciaSequia = "Baja";
        p.usoPrincipal = "Frutal"; p.probabilidad = 65;
        p.descripcion = "Aguacate o palta, requiere riego constante y protección del sol intenso en Piura.";
        CONOCIDAS.put("aguacate", p);
        CONOCIDAS.put("palta", p);
        CONOCIDAS.put("persea americana", p);

        p = new SpeciesProfile();
        p.cientifico = "Neltuma alba";
        p.familia = "Fabaceae";
        p.origen = "Piura, Perú (Nativo)";
        p.clima = "Árido";
        p.tempMin = 8.0; p.tempMax = 45.0;
        p.alturaMax = 1500; p.crecimiento = "Lento";
        p.requiereAgua = "Baja"; p.toleranciaSequia = "Muy Alta";
        p.usoPrincipal = "Forestal / Sombra"; p.probabilidad = 95;
        p.descripcion = "Árbol nativo del bosque seco, madera noble y resistente. Ideal para reforestación en Piura.";
        CONOCIDAS.put("algarrobo blanco", p);
        CONOCIDAS.put("neltuma alba", p);

        p = new SpeciesProfile();
        p.cientifico = "Schinus molle";
        p.familia = "Anacardiaceae";
        p.origen = "Perú (Nativo)";
        p.clima = "Árido";
        p.tempMin = 10.0; p.tempMax = 40.0;
        p.alturaMax = 1500; p.crecimiento = "Medio";
        p.requiereAgua = "Baja"; p.toleranciaSequia = "Muy Alta";
        p.usoPrincipal = "Ornamental / Medicinal"; p.probabilidad = 90;
        p.descripcion = "Árbol nativo peruano de porte mediano, muy resistente a la sequía y ideal para sombra.";
        CONOCIDAS.put("molle", p);
        CONOCIDAS.put("schinus molle", p);

        p = new SpeciesProfile();
        p.cientifico = "Erythrina crista-galli";
        p.familia = "Fabaceae";
        p.origen = "América del Sur";
        p.clima = "Tropical";
        p.tempMin = 15.0; p.tempMax = 38.0;
        p.alturaMax = 800; p.crecimiento = "Rápido";
        p.requiereAgua = "Media"; p.toleranciaSequia = "Alta";
        p.usoPrincipal = "Ornamental"; p.probabilidad = 80;
        p.descripcion = "Ceibo, árbol ornamental de flor roja, resistente al clima seco costero.";
        CONOCIDAS.put("ceibo", p);
        CONOCIDAS.put("ceiba", p);
        CONOCIDAS.put("erythrina", p);

        p = new SpeciesProfile();
        p.cientifico = "Musa paradisiaca";
        p.familia = "Musaceae";
        p.origen = "Sudeste Asiático (adaptado a Piura)";
        p.clima = "Tropical";
        p.tempMin = 20.0; p.tempMax = 38.0;
        p.alturaMax = 1500; p.crecimiento = "Rápido";
        p.requiereAgua = "Alta"; p.toleranciaSequia = "Baja";
        p.usoPrincipal = "Frutal"; p.probabilidad = 65;
        p.descripcion = "Plátano, requiere riego constante y suelos fértiles. Se cultiva en valles piuranos.";
        CONOCIDAS.put("plátano", p);
        CONOCIDAS.put("platano", p);
        CONOCIDAS.put("banano", p);
        CONOCIDAS.put("musa", p);

        p = new SpeciesProfile();
        p.cientifico = "Cocos nucifera";
        p.familia = "Arecaceae";
        p.origen = "Asia Tropical (adaptado a Piura)";
        p.clima = "Tropical";
        p.tempMin = 20.0; p.tempMax = 38.0;
        p.alturaMax = 3000; p.crecimiento = "Lento";
        p.requiereAgua = "Media"; p.toleranciaSequia = "Alta";
        p.usoPrincipal = "Frutal / Multiusos"; p.probabilidad = 75;
        p.descripcion = "Palmera de coco, cultivada en zonas costeras de Piura. Resistente a suelos arenosos.";
        CONOCIDAS.put("coco", p);
        CONOCIDAS.put("cocotero", p);

        p = new SpeciesProfile();
        p.cientifico = "Tamarindus indica";
        p.familia = "Fabaceae";
        p.origen = "África (adaptado a Piura)";
        p.clima = "Tropical";
        p.tempMin = 15.0; p.tempMax = 42.0;
        p.alturaMax = 3000; p.crecimiento = "Lento";
        p.requiereAgua = "Baja"; p.toleranciaSequia = "Muy Alta";
        p.usoPrincipal = "Frutal / Sombra"; p.probabilidad = 85;
        p.descripcion = "Tamarindo, árbol muy resistente a la sequía, ideal para clima árido. Produce vainas comestibles.";
        CONOCIDAS.put("tamarindo", p);

        p = new SpeciesProfile();
        p.cientifico = "Annona cherimola";
        p.familia = "Annonaceae";
        p.origen = "Perú (Nativo)";
        p.clima = "Subtropical";
        p.tempMin = 12.0; p.tempMax = 30.0;
        p.alturaMax = 900; p.crecimiento = "Medio";
        p.requiereAgua = "Media"; p.toleranciaSequia = "Media";
        p.usoPrincipal = "Frutal"; p.probabilidad = 70;
        p.descripcion = "Chirimoya, fruta considerada la mejor del mundo. Originaria de Perú, cultivada en valles interandinos.";
        CONOCIDAS.put("chirimoya", p);
        CONOCIDAS.put("chirimoyo", p);

        p = new SpeciesProfile();
        p.cientifico = "Pouteria lucuma";
        p.familia = "Sapotaceae";
        p.origen = "Perú (Nativo)";
        p.clima = "Subtropical";
        p.tempMin = 12.0; p.tempMax = 32.0;
        p.alturaMax = 1500; p.crecimiento = "Lento";
        p.requiereAgua = "Media"; p.toleranciaSequia = "Alta";
        p.usoPrincipal = "Frutal"; p.probabilidad = 75;
        p.descripcion = "Lúcuma, fruta emblemática del Perú, utilizada en postres y helados. Árbol resistente.";
        CONOCIDAS.put("lúcuma", p);
        CONOCIDAS.put("lucuma", p);

        p = new SpeciesProfile();
        p.cientifico = "Caesalpinia spinosa";
        p.familia = "Fabaceae";
        p.origen = "Perú (Nativo)";
        p.clima = "Árido";
        p.tempMin = 10.0; p.tempMax = 38.0;
        p.alturaMax = 500; p.crecimiento = "Medio";
        p.requiereAgua = "Baja"; p.toleranciaSequia = "Muy Alta";
        p.usoPrincipal = "Forestal / Tannino"; p.probabilidad = 90;
        p.descripcion = "Tara, árbol nativo peruano de gran valor económico por sus vainas ricas en taninos.";
        CONOCIDAS.put("tara", p);
        CONOCIDAS.put("caesalpinia spinosa", p);

        p = new SpeciesProfile();
        p.cientifico = "Bixa orellana";
        p.familia = "Bixaceae";
        p.origen = "América Tropical (Nativo)";
        p.clima = "Tropical";
        p.tempMin = 18.0; p.tempMax = 36.0;
        p.alturaMax = 800; p.crecimiento = "Rápido";
        p.requiereAgua = "Media"; p.toleranciaSequia = "Alta";
        p.usoPrincipal = "Industrial / Medicinal"; p.probabilidad = 80;
        p.descripcion = "Achiote, arbusto nativo usado como colorante natural. Resistente al clima seco.";
        CONOCIDAS.put("achiote", p);
        CONOCIDAS.put("bixa orellana", p);

        p = new SpeciesProfile();
        p.cientifico = "Psidium guajava";
        p.familia = "Myrtaceae";
        p.origen = "América Tropical (Nativo)";
        p.clima = "Tropical";
        p.tempMin = 15.0; p.tempMax = 38.0;
        p.alturaMax = 1000; p.crecimiento = "Rápido";
        p.requiereAgua = "Media"; p.toleranciaSequia = "Alta";
        p.usoPrincipal = "Frutal"; p.probabilidad = 80;
        p.descripcion = "Guayaba, frutal resistente y productivo, bien adaptado al clima de Piura.";
        CONOCIDAS.put("guayaba", p);
        CONOCIDAS.put("guayabo", p);

        p = new SpeciesProfile();
        p.cientifico = "Moringa oleifera";
        p.familia = "Moringaceae";
        p.origen = "India (adaptada a Piura)";
        p.clima = "Tropical-Árido";
        p.tempMin = 18.0; p.tempMax = 45.0;
        p.alturaMax = 1200; p.crecimiento = "Rápido";
        p.requiereAgua = "Baja"; p.toleranciaSequia = "Muy Alta";
        p.usoPrincipal = "Multiusos / Medicinal"; p.probabilidad = 90;
        p.descripcion = "Moringa, árbol de rápido crecimiento con alto valor nutricional. Ideal para clima árido.";
        CONOCIDAS.put("moringa", p);

        p = new SpeciesProfile();
        p.cientifico = "Inga feuilleei";
        p.familia = "Fabaceae";
        p.origen = "Perú (Nativo)";
        p.clima = "Tropical";
        p.tempMin = 15.0; p.tempMax = 35.0;
        p.alturaMax = 1500; p.crecimiento = "Rápido";
        p.requiereAgua = "Media"; p.toleranciaSequia = "Media";
        p.usoPrincipal = "Frutal / Sombra"; p.probabilidad = 75;
        p.descripcion = "Guaba, árbol de sombra con vainas comestibles. Común en valles de Piura.";
        CONOCIDAS.put("guaba", p);
        CONOCIDAS.put("inga", p);

        p = new SpeciesProfile();
        p.cientifico = "Eucalyptus globulus";
        p.familia = "Myrtaceae";
        p.origen = "Australia (adaptado a Perú)";
        p.clima = "Mediterráneo";
        p.tempMin = 8.0; p.tempMax = 40.0;
        p.alturaMax = 6000; p.crecimiento = "Rápido";
        p.requiereAgua = "Media"; p.toleranciaSequia = "Alta";
        p.usoPrincipal = "Forestal / Medicinal"; p.probabilidad = 85;
        p.descripcion = "Eucalipto, árbol de rápido crecimiento, usado en reforestación y producción de aceites esenciales.";
        CONOCIDAS.put("eucalipto", p);
        CONOCIDAS.put("eucalyptus", p);

        p = new SpeciesProfile();
        p.cientifico = "Olea europaea";
        p.familia = "Oleaceae";
        p.origen = "Mediterráneo (adaptado a Piura)";
        p.clima = "Mediterráneo-Árido";
        p.tempMin = 8.0; p.tempMax = 38.0;
        p.alturaMax = 1500; p.crecimiento = "Lento";
        p.requiereAgua = "Baja"; p.toleranciaSequia = "Muy Alta";
        p.usoPrincipal = "Frutal / Aceite"; p.probabilidad = 85;
        p.descripcion = "Olivo, árbol milenario adaptado al clima mediterráneo y árido. Produce aceitunas y aceite de oliva.";
        CONOCIDAS.put("olivo", p);
        CONOCIDAS.put("aceituna", p);
        CONOCIDAS.put("olea europaea", p);

        p = new SpeciesProfile();
        p.cientifico = "Ficus carica";
        p.familia = "Moraceae";
        p.origen = "Mediterráneo (adaptado a Piura)";
        p.clima = "Mediterráneo";
        p.tempMin = 10.0; p.tempMax = 38.0;
        p.alturaMax = 1000; p.crecimiento = "Medio";
        p.requiereAgua = "Media"; p.toleranciaSequia = "Alta";
        p.usoPrincipal = "Frutal"; p.probabilidad = 80;
        p.descripcion = "Higo, frutal mediterráneo bien adaptado al clima seco de Piura.";
        CONOCIDAS.put("higo", p);

        p = new SpeciesProfile();
        p.cientifico = "Tecoma stans";
        p.familia = "Bignoniaceae";
        p.origen = "América Tropical (Nativo)";
        p.clima = "Tropical-Árido";
        p.tempMin = 12.0; p.tempMax = 40.0;
        p.alturaMax = 800; p.crecimiento = "Rápido";
        p.requiereAgua = "Baja"; p.toleranciaSequia = "Muy Alta";
        p.usoPrincipal = "Ornamental"; p.probabilidad = 90;
        p.descripcion = "Huaranhuay, árbol nativo de flores amarillas, muy resistente a la sequía. Ideal para Piura.";
        CONOCIDAS.put("huaranhuay", p);
        CONOCIDAS.put("tecoma stans", p);

        p = new SpeciesProfile();
        p.cientifico = "Cordia lutea";
        p.familia = "Boraginaceae";
        p.origen = "Piura, Perú (Nativo)";
        p.clima = "Árido";
        p.tempMin = 10.0; p.tempMax = 42.0;
        p.alturaMax = 600; p.crecimiento = "Rápido";
        p.requiereAgua = "Baja"; p.toleranciaSequia = "Muy Alta";
        p.usoPrincipal = "Ornamental / Medicinal"; p.probabilidad = 95;
        p.descripcion = "Overo, árbol nativo del bosque seco piurano. Flores amarillas, muy resistente a la sequía.";
        CONOCIDAS.put("overo", p);
        CONOCIDAS.put("cordia lutea", p);

        p = new SpeciesProfile();
        p.cientifico = "Bursera graveolens";
        p.familia = "Burseraceae";
        p.origen = "Piura, Perú (Nativo)";
        p.clima = "Árido";
        p.tempMin = 10.0; p.tempMax = 42.0;
        p.alturaMax = 1200; p.crecimiento = "Lento";
        p.requiereAgua = "Baja"; p.toleranciaSequia = "Muy Alta";
        p.usoPrincipal = "Medicinal / Aromático"; p.probabilidad = 95;
        p.descripcion = "Palo Santo, árbol nativo del bosque seco, conocido por su aroma y propiedades medicinales.";
        CONOCIDAS.put("palo santo", p);
        CONOCIDAS.put("bursera graveolens", p);

        p = new SpeciesProfile();
        p.cientifico = "Loxopterygium huasango";
        p.familia = "Anacardiaceae";
        p.origen = "Piura, Perú (Nativo)";
        p.clima = "Árido";
        p.tempMin = 8.0; p.tempMax = 42.0;
        p.alturaMax = 3500; p.crecimiento = "Lento";
        p.requiereAgua = "Baja"; p.toleranciaSequia = "Muy Alta";
        p.usoPrincipal = "Forestal / Madera"; p.probabilidad = 95;
        p.descripcion = "Hualtaco, árbol emblemático del bosque seco piurano. Madera muy dura y resistente.";
        CONOCIDAS.put("hualtaco", p);

        p = new SpeciesProfile();
        p.cientifico = "Handroanthus billbergii";
        p.familia = "Bignoniaceae";
        p.origen = "Piura, Perú (Nativo)";
        p.clima = "Árido";
        p.tempMin = 10.0; p.tempMax = 40.0;
        p.alturaMax = 2500; p.crecimiento = "Lento";
        p.requiereAgua = "Baja"; p.toleranciaSequia = "Muy Alta";
        p.usoPrincipal = "Forestal / Ornamental"; p.probabilidad = 90;
        p.descripcion = "Guayacán, árbol nativo de madera noble, produce flores amarillas en temporada seca.";
        CONOCIDAS.put("guayacán", p);
        CONOCIDAS.put("guayacan", p);

        p = new SpeciesProfile();
        p.cientifico = "Capparis scabrida";
        p.familia = "Capparaceae";
        p.origen = "Piura, Perú (Nativo)";
        p.clima = "Árido";
        p.tempMin = 10.0; p.tempMax = 42.0;
        p.alturaMax = 500; p.crecimiento = "Lento";
        p.requiereAgua = "Baja"; p.toleranciaSequia = "Muy Alta";
        p.usoPrincipal = "Forestal / Forraje"; p.probabilidad = 95;
        p.descripcion = "Algarrobo de la costa, arbusto nativo del bosque seco, esencial para la fauna local.";
        CONOCIDAS.put("algarrobo costero", p);
    }

    private static void inicializarReglas() {
        REGLAS.add(new SpeciesRule(
            n -> n.contains("fruta") || n.contains("frutal"),
            n -> true,
            "Rosaceae", "América Tropical", "Tropical",
            18.0, 35.0, 1200, "Medio", "Media", "Media",
            "Frutal", 70,
            "%s - árbol frutal tropical adaptado al clima de Piura."
        ));

        REGLAS.add(new SpeciesRule(
            n -> n.contains("palma") || n.contains("palmera"),
            n -> true,
            "Arecaceae", "Tropical", "Tropical",
            20.0, 38.0, 2500, "Lento", "Media", "Alta",
            "Ornamental / Frutal", 75,
            "%s - palmera tropical, resistente al clima costero de Piura."
        ));

        REGLAS.add(new SpeciesRule(
            n -> n.contains("cactus") || n.contains("cacto"),
            n -> true,
            "Cactaceae", "América", "Árido",
            10.0, 45.0, 500, "Lento", "Baja", "Muy Alta",
            "Ornamental", 90,
            "%s - cactus resistente a la sequía extrema, ideal para clima árido."
        ));

        REGLAS.add(new SpeciesRule(
            n -> n.contains("pino") || n.contains("conífera") || n.contains("conifera"),
            n -> true,
            "Pinaceae", "América del Norte", "Templado",
            5.0, 35.0, 3000, "Lento", "Baja", "Alta",
            "Forestal / Madera", 70,
            "%s - conífera de crecimiento lento, adaptada a climas templados."
        ));

        REGLAS.add(new SpeciesRule(
            n -> n.contains("ornamental") || n.contains("flor") || n.contains("jardín"),
            n -> true,
            "Por determinar", "América Tropical", "Tropical",
            15.0, 36.0, 600, "Rápido", "Media", "Media",
            "Ornamental", 70,
            "%s - planta ornamental, requiere cuidados básicos de jardinería."
        ));
    }

    private static class SpeciesProfile {
        String cientifico, familia, origen, clima, crecimiento, requiereAgua, toleranciaSequia, usoPrincipal, descripcion;
        double tempMin, tempMax;
        int alturaMax, probabilidad;
    }

    private static class SpeciesRule {
        interface Condicion {
            boolean evaluar(String nombre);
        }

        Condicion condNombre;
        Condicion condCientifico;
        String cientificoBase, familia, origen, clima, crecimiento, requiereAgua, toleranciaSequia, usoPrincipal, descripcionTemplate;
        double tempMin, tempMax;
        int alturaMax, probabilidad;

        SpeciesRule(Condicion condNombre, Condicion condCientifico,
                    String familia, String origen, String clima,
                    double tempMin, double tempMax, int alturaMax,
                    String crecimiento, String requiereAgua, String toleranciaSequia,
                    String usoPrincipal, int probabilidad,
                    String descripcionTemplate) {
            this.condNombre = condNombre;
            this.condCientifico = condCientifico;
            this.cientificoBase = familia.toLowerCase().replaceAll("[^a-z]", "");
            this.familia = familia;
            this.origen = origen;
            this.clima = clima;
            this.tempMin = tempMin;
            this.tempMax = tempMax;
            this.alturaMax = alturaMax;
            this.crecimiento = crecimiento;
            this.requiereAgua = requiereAgua;
            this.toleranciaSequia = toleranciaSequia;
            this.usoPrincipal = usoPrincipal;
            this.probabilidad = probabilidad;
            this.descripcionTemplate = descripcionTemplate;
        }

        boolean coincide(String nombre, String cientifico) {
            return condNombre.evaluar(nombre) || condCientifico.evaluar(cientifico);
        }
    }
}