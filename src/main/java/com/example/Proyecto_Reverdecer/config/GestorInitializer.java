package com.example.Proyecto_Reverdecer.config;

import com.example.Proyecto_Reverdecer.model.Usuario;
import com.example.Proyecto_Reverdecer.model.TipoDoc;
import com.example.Proyecto_Reverdecer.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class GestorInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n=== INICIALIZADOR DE GESTOR AMBIENTAL ===");
        
        // Verificar si ya existe un gestor
        boolean gestorExists = usuarioRepository.findAll().stream()
            .anyMatch(u -> u.getRol() != null && u.getRol().equals("ROLE_GESTOR_AMBIENTAL"));
        
        if (gestorExists) {
            System.out.println("✓ Gestor Ambiental ya existe en la base de datos");
            return;
        }
        
        System.out.println("⚠ No hay Gestor Ambiental. Creando gestor por defecto...");
        
        Usuario gestor = new Usuario();
        gestor.setUser("gestor");
        gestor.setPassword(passwordEncoder.encode("gestor123"));
        gestor.setCorreo("gestor@reverdecerpiura.com");
        gestor.setNombres("Gestor");
        gestor.setApellidoPaterno("Ambiental");
        gestor.setApellidoMaterno("ReVerdecer");
        gestor.setDni("11111111");
        gestor.setTipoDoc(TipoDoc.DNI);
        gestor.setGenero("Otro");
        gestor.setDireccion1("Sistema");
        gestor.setActivo(true);
        gestor.setRol("ROLE_GESTOR_AMBIENTAL");
        gestor.setFechaRegistro(LocalDate.now());
        
        try {
            usuarioRepository.save(gestor);
            System.out.println("✓ Gestor Ambiental creado exitosamente!");
            System.out.println("  - Usuario: gestor");
            System.out.println("  - Correo: gestor@reverdecerpiura.com");
            System.out.println("  - Contraseña: gestor123 (CAMBIAR EN PRODUCCIÓN)");
        } catch (Exception e) {
            System.err.println("❌ Error al crear gestor: " + e.getMessage());
        }
    }
}