package com.example.Proyecto_Reverdecer.config;

import com.example.Proyecto_Reverdecer.model.Especie;
import com.example.Proyecto_Reverdecer.model.Usuario;
import com.example.Proyecto_Reverdecer.model.TipoDoc;
import com.example.Proyecto_Reverdecer.repository.EspecieRepository;
import com.example.Proyecto_Reverdecer.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EspecieRepository especieRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        crearAdminPorDefecto();
        crearGestorPorDefecto();
        inicializarCatalogoEspecies();
    }

    private void crearAdminPorDefecto() {
        boolean adminExists = usuarioRepository.findAll().stream()
                .anyMatch(u -> u.getIsAdmin() != null && u.getIsAdmin());
        if (adminExists) {
            System.out.println("Admin ya existe en la base de datos");
            return;
        }
        System.out.println("No hay admin en la BD. Creando admin por defecto...");
        Usuario admin = crearUsuario("admin", "admin123", "admin@reverdecerpiura.com",
                "Administrador", "Sistema", "ReVerdecer", "00000000");
        admin.setIsAdmin(true);
        guardarUsuario(admin, "Admin");
    }

    private void crearGestorPorDefecto() {
        boolean gestorExists = usuarioRepository.findAll().stream()
                .anyMatch(u -> u.getRol() != null && u.getRol().equals("ROLE_GESTOR_AMBIENTAL"));
        if (gestorExists) {
            System.out.println("Gestor Ambiental ya existe en la base de datos");
            return;
        }
        System.out.println("No hay Gestor Ambiental. Creando gestor por defecto...");
        Usuario gestor = crearUsuario("gestor", "gestor123", "gestor@reverdecerpiura.com",
                "Gestor", "Ambiental", "ReVerdecer", "11111111");
        gestor.setRol("ROLE_GESTOR_AMBIENTAL");
        guardarUsuario(gestor, "Gestor Ambiental");
    }

    private void inicializarCatalogoEspecies() {
        if (especieRepository.count() > 0) {
            System.out.println("Catálogo de especies ya existe (" + especieRepository.count() + " registros)");
            return;
        }
        System.out.println("Inicializando catálogo de especies de Piura...");

        Especie[] catalogo = {
            crearEspecie("Algarrobo", "Prosopis pallida", "Fabaceae", "Nativo de Piura", "Seco tropical",
                "Árbol emblemático de Piura, resistente a la sequía. Su fruto es la algarrobina.", 1600, "Lento", "Baja", "Muy Alta", "Sombra, forraje, madera", 18.0, 38.0, 95),
            crearEspecie("Mango", "Mangifera indica", "Anacardiaceae", "Introducido (Asia)", "Tropical seco",
                "Árbol frutal muy cultivado en Piura, produce mangos de excelente calidad.", 1500, "Medio", "Media", "Alta", "Frutal", 15.0, 35.0, 85),
            crearEspecie("Limón", "Citrus × limon", "Rutaceae", "Introducido (Asia)", "Tropical seco",
                "Árbol frutal muy común en huertos piuranos. Produce limones todo el año.", 600, "Medio", "Media", "Media", "Frutal", 10.0, 35.0, 75),
            crearEspecie("Guayaba", "Psidium guajava", "Myrtaceae", "Nativo de América", "Tropical",
                "Árbol frutal muy adaptable, crece bien en la costa norte del Perú.", 600, "Rápido", "Media", "Alta", "Frutal", 12.0, 36.0, 80),
            crearEspecie("Papaya", "Carica papaya", "Caricaceae", "Nativo de América", "Tropical",
                "Árbol frutal de crecimiento rápido, muy cultivado en la región Piura.", 800, "Rápido", "Alta", "Baja", "Frutal", 15.0, 33.0, 65),
            crearEspecie("Ponciana Real", "Delonix regia", "Fabaceae", "Introducido (Madagascar)", "Tropical seco",
                "Árbol ornamental con flores rojas intensas, usado en parques y avenidas.", 1200, "Rápido", "Media", "Alta", "Ornamental", 12.0, 38.0, 90),
            crearEspecie("Neem", "Azadirachta indica", "Meliaceae", "Introducido (India)", "Seco tropical",
                "Árbol muy resistente a la sequía. Usado como sombra y propiedades medicinales.", 1500, "Rápido", "Baja", "Muy Alta", "Sombra, medicinal", 10.0, 40.0, 95),
            crearEspecie("Ficus", "Ficus benjamina", "Moraceae", "Introducido (Asia)", "Tropical",
                "Árbol ornamental de gran porte, usado en parques y plazas de Piura.", 2000, "Medio", "Media", "Media", "Ornamental", 13.0, 35.0, 70),
            crearEspecie("Palmera Datilera", "Phoenix dactylifera", "Arecaceae", "Introducido (Medio Oriente)", "Desértico cálido",
                "Palmera que produce dátiles, muy adaptada al clima seco de la costa piurana.", 2000, "Lento", "Media", "Muy Alta", "Frutal, ornamental", 8.0, 42.0, 90),
            crearEspecie("Sauce Llorón", "Salix babylonica", "Salicaceae", "Introducido (China)", "Templado",
                "Árbol de aspecto llorón, necesita humedad constante. Cerca de ríos y canales.", 1200, "Rápido", "Alta", "Baja", "Ornamental", 5.0, 30.0, 45),
            crearEspecie("Coco", "Cocos nucifera", "Arecaceae", "Introducido", "Tropical húmedo",
                "Palmera tropical que produce cocos, común en zonas costeras cálidas.", 2500, "Lento", "Alta", "Media", "Frutal", 15.0, 35.0, 60),
            crearEspecie("Huarango", "Acacia macracantha", "Fabaceae", "Nativo de Piura", "Seco tropical",
                "Árbol nativo de la costa norte del Perú, muy resistente a sequía y suelos pobres.", 1000, "Medio", "Baja", "Muy Alta", "Sombra, recuperación de suelos", 15.0, 40.0, 98),
            crearEspecie("Pacae", "Inga feuilleei", "Fabaceae", "Nativo de Perú", "Tropical seco",
                "Árbol frutal nativo de los valles interandinos y costa norte, produce vainas comestibles.", 1200, "Medio", "Media", "Media", "Frutal, sombra", 12.0, 34.0, 70),
            crearEspecie("Lúcuma", "Pouteria lúcuma", "Sapotaceae", "Nativo de Perú", "Tropical seco",
                "Árbol frutal nativo del Perú, produce el lúcumo, fruto muy apreciado.", 1000, "Lento", "Media", "Media", "Frutal", 10.0, 30.0, 60),
            crearEspecie("Guaba", "Inga edulis", "Fabaceae", "Nativo de América del Sur", "Tropical",
                "Árbol frutal que produce vainas largas con pulpa dulce. Común cerca de ríos.", 1500, "Rápido", "Alta", "Baja", "Frutal, sombra", 14.0, 33.0, 55),
            crearEspecie("Bambú", "Bambusa vulgaris", "Poaceae", "Introducido (Asia)", "Tropical",
                "Planta de tallo leñoso de rápido crecimiento, usada para cercos vivos.", 2000, "Muy Rápido", "Alta", "Media", "Cercos, construcción", 10.0, 35.0, 65),
            crearEspecie("Molle Serrano", "Schinus molle", "Anacardiaceae", "Nativo del Perú", "Templado seco",
                "Árbol perennifolio de copa redondeada, resistente y de bajo requerimiento hídrico.", 1200, "Medio", "Baja", "Muy Alta", "Ornamental, medicinal", 8.0, 36.0, 85),
            crearEspecie("Tara", "Tara spinosa", "Fabaceae", "Nativo del Perú", "Seco tropical",
                "Árbol nativo de la costa norte, sus vainas se usan para gomas y taninos.", 600, "Medio", "Baja", "Muy Alta", "Industrial, forestal", 12.0, 38.0, 90),
            crearEspecie("Pimiento", "Schinus terebinthifolia", "Anacardiaceae", "Nativo de América del Sur", "Tropical seco",
                "Árbol de tamaño mediano con frutos rosados, usado como especia y ornamental.", 800, "Rápido", "Baja", "Alta", "Ornamental, especia", 10.0, 37.0, 85),
            crearEspecie("Chirimoya", "Annona cherimola", "Annonaceae", "Nativo de Perú", "Templado subtropical",
                "Árbol frutal de fruto dulce y cremoso, cultivado en valles del norte peruano.", 800, "Medio", "Media", "Media", "Frutal", 8.0, 28.0, 50),
            crearEspecie("Capulí", "Prunus salicifolia", "Rosaceae", "Nativo de América", "Templado",
                "Árbol frutal de fruto pequeño y dulce, cultivado en la sierra piurana.", 1000, "Medio", "Media", "Media", "Frutal", 5.0, 28.0, 45),
            crearEspecie("Eucalipto", "Eucalyptus globulus", "Myrtaceae", "Introducido (Australia)", "Templado",
                "Árbol de rápido crecimiento, usado en reforestación y producción de madera.", 3000, "Rápido", "Alta", "Media", "Forestal, medicinal", 3.0, 32.0, 55),
            crearEspecie("Guarango", "Caesalpinia spinosa", "Fabaceae", "Nativo de Perú", "Seco tropical",
                "Árbol espinoso nativo de la costa norte, muy resistente a condiciones áridas.", 500, "Lento", "Baja", "Muy Alta", "Industrial", 12.0, 40.0, 95),
            crearEspecie("Higuera", "Ficus carica", "Moraceae", "Introducido (Mediterráneo)", "Templado cálido",
                "Árbol frutal que produce higos dulces. Se adapta bien al clima seco de la costa.", 700, "Medio", "Media", "Alta", "Frutal", 5.0, 36.0, 70),
            crearEspecie("Ciruelo", "Spondias purpurea", "Anacardiaceae", "Nativo de América", "Tropical seco",
                "Árbol frutal tropical que produce ciruelas rojas o amarillas. Muy común en Piura.", 800, "Rápido", "Media", "Alta", "Frutal", 12.0, 36.0, 80),
            crearEspecie("Tamarindo", "Tamarindus indica", "Fabaceae", "Introducido (África)", "Tropical seco",
                "Árbol frutal de gran porte, produce vainas con pulpa agridulce.", 2000, "Lento", "Baja", "Muy Alta", "Frutal, sombra", 12.0, 40.0, 90),
            crearEspecie("Naranjo", "Citrus × sinensis", "Rutaceae", "Introducido (Asia)", "Tropical seco",
                "Árbol frutal de cítricos, ampliamente cultivado en la región Piura.", 800, "Medio", "Media", "Media", "Frutal", 8.0, 33.0, 65),
            crearEspecie("Palta", "Persea americana", "Lauraceae", "Nativo de América", "Templado subtropical",
                "Árbol frutal de fruto cremoso muy apreciado. Se cultiva en valles de Piura.", 1500, "Medio", "Alta", "Media", "Frutal", 8.0, 30.0, 55),
            crearEspecie("Uva de Monte", "Coccoloba ruiziana", "Polygonaceae", "Nativo del Perú", "Tropical",
                "Árbol nativo de los bosques secos del norte peruano, produce frutos comestibles.", 1000, "Medio", "Baja", "Alta", "Forestal", 14.0, 35.0, 80),
            crearEspecie("Guachapelí", "Pseudosamanea guachapele", "Fabaceae", "Nativo de América", "Tropical seco",
                "Árbol de gran porte, madera fina y copa amplia, de bosques secos ecuatoriales.", 2500, "Medio", "Media", "Alta", "Madera, sombra", 14.0, 36.0, 75),
            crearEspecie("Higuerón", "Ficus luschnathiana", "Moraceae", "Nativo de América del Sur", "Tropical",
                "Árbol de gran tamaño, forma frutos que alimentan a la fauna local.", 2000, "Medio", "Media", "Media", "Sombra, ecológico", 14.0, 34.0, 70),
            crearEspecie("Palo Santo", "Bursera graveolens", "Burseraceae", "Nativo de Piura", "Seco tropical",
                "Árbol emblemático del bosque seco piurano, famoso por su aroma.", 800, "Lento", "Baja", "Muy Alta", "Forestal, aromático", 14.0, 40.0, 95),
            crearEspecie("Ceibo", "Erythrina smithiana", "Fabaceae", "Nativo de América del Sur", "Tropical",
                "Árbol de flores rojas intensas, usado como ornamental y sombra.", 1500, "Rápido", "Media", "Alta", "Ornamental, sombra", 12.0, 36.0, 80),
            crearEspecie("Cerezo de Cayena", "Eugenia uniflora", "Myrtaceae", "Nativo de América del Sur", "Tropical",
                "Árbol frutal pequeño, produce frutos rojos comestibles. Ideal para jardines.", 400, "Medio", "Media", "Media", "Frutal, ornamental", 10.0, 34.0, 70),
            crearEspecie("Maracuyá", "Passiflora edulis", "Passifloraceae", "Nativo de América del Sur", "Tropical",
                "Planta trepadora leñosa, cultivada por su fruto. Se adapta a valles cálidos.", 1200, "Rápido", "Media", "Media", "Frutal", 12.0, 34.0, 65),
            crearEspecie("Pitajaya", "Selenicereus undatus", "Cactaceae", "Introducido (México)", "Tropical seco",
                "Cactus trepador que produce frutos exóticos de pulpa dulce.", 300, "Rápido", "Baja", "Muy Alta", "Frutal", 10.0, 40.0, 90),
            crearEspecie("Roble de la Costa", "Tabebuia rosea", "Bignoniaceae", "Nativo de América", "Tropical",
                "Árbol ornamental de hermosas flores rosadas, usado en parques y avenidas.", 2000, "Medio", "Media", "Alta", "Ornamental", 12.0, 36.0, 80),
            crearEspecie("Pomelo", "Citrus maxima", "Rutaceae", "Introducido (Asia)", "Tropical",
                "Árbol de cítricos de fruto grande, cultivado en zonas cálidas de Piura.", 1000, "Medio", "Media", "Media", "Frutal", 10.0, 34.0, 65),
            crearEspecie("Mandarina", "Citrus reticulata", "Rutaceae", "Introducido (Asia)", "Tropical seco",
                "Árbol de cítricos de fruto pequeño y dulce, cultivado en la costa norte.", 600, "Medio", "Media", "Media", "Frutal", 8.0, 33.0, 65),
            crearEspecie("Granadilla", "Passiflora ligularis", "Passifloraceae", "Nativo de América", "Templado subtropical",
                "Planta trepadora de fruto dulce, cultivada en valles interandinos.", 600, "Rápido", "Media", "Media", "Frutal", 10.0, 28.0, 50),
        };

        for (Especie e : catalogo) {
            especieRepository.save(e);
        }
        System.out.println("Catálogo de especies inicializado con " + catalogo.length + " especies");
    }

    private Especie crearEspecie(String nombreComun, String nombreCientifico, String familia,
            String origen, String clima, String descripcion, Integer alturaMaximaCm,
            String crecimiento, String requiereAgua, String toleranciaSequia,
            String usoPrincipal, Double tempMin, Double tempMax, Integer probabilidadSupervivencia) {
        Especie e = new Especie();
        e.setNombreComun(nombreComun);
        e.setNombreCientifico(nombreCientifico);
        e.setFamilia(familia);
        e.setOrigen(origen);
        e.setClima(clima);
        e.setDescripcion(descripcion);
        e.setAlturaMaximaCm(alturaMaximaCm);
        e.setCrecimiento(crecimiento);
        e.setRequiereAgua(requiereAgua);
        e.setToleranciaSequia(toleranciaSequia);
        e.setUsoPrincipal(usoPrincipal);
        e.setTempMin(tempMin);
        e.setTempMax(tempMax);
        e.setProbabilidadSupervivencia(probabilidadSupervivencia);
        e.setActivo(true);
        e.setAutoRegistrada(false);
        e.setCreadoEn(java.time.LocalDateTime.now());
        return e;
    }

    private Usuario crearUsuario(String user, String password, String correo,
            String nombres, String apellidoPaterno, String apellidoMaterno, String dni) {
        Usuario usuario = new Usuario();
        usuario.setUser(user);
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setCorreo(correo);
        usuario.setNombres(nombres);
        usuario.setApellidoPaterno(apellidoPaterno);
        usuario.setApellidoMaterno(apellidoMaterno);
        usuario.setDni(dni);
        usuario.setTipoDoc(TipoDoc.DNI);
        usuario.setGenero("Otro");
        usuario.setDireccion1("Sistema");
        usuario.setActivo(true);
        usuario.setFechaRegistro(LocalDate.now());
        return usuario;
    }

    private void guardarUsuario(Usuario usuario, String tipo) {
        try {
            usuarioRepository.save(usuario);
            System.out.println("Creado exitosamente!");
            System.out.println("Usuario: " + usuario.getUser());
            System.out.println("Correo: " + usuario.getCorreo());
            if (usuario.getUser().equals("admin")) {
                System.out.println("Contraseña: admin123");
            } else if (usuario.getUser().equals("gestor")) {
                System.out.println("Contraseña: gestor123");
            }
        } catch (Exception e) {
            System.err.println("Error al crear " + tipo + ": " + e.getMessage());
        }
    }
}
