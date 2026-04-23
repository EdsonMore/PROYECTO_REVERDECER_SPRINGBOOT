package com.example.Proyecto_Reverdecer.service;

import com.example.Proyecto_Reverdecer.model.Arbol;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ArbolService {

    private List<Arbol> arboles = new ArrayList<>();
    private AtomicLong contadorId = new AtomicLong(1);

    public ArbolService() {
        arboles.add(new Arbol(contadorId.getAndIncrement(), "Roble", "Parque Central", LocalDate.now().minusDays(5), "Bueno"));
        arboles.add(new Arbol(contadorId.getAndIncrement(), "Pino", "Av. Principal", LocalDate.now().minusDays(10), "Muy Bueno"));
        arboles.add(new Arbol(contadorId.getAndIncrement(), "Cedro", "Jardín Botánico", LocalDate.now().minusDays(2), "Regular"));
    }

    public List<Arbol> listarTodos() {
        List<Arbol> arbolesActivos = new ArrayList<>();
        for (Arbol a : arboles) {
            if (a != null) {
                arbolesActivos.add(a);
            }
        }
        return arbolesActivos;
    }

    public List<Arbol> listarPorEstado(String estado) {
        List<Arbol> resultado = new ArrayList<>();
        for (Arbol a : arboles) {
            if (a.getEstado().equalsIgnoreCase(estado)) {
                resultado.add(a);
            }
        }
        return resultado;
    }

    public Arbol guardar(Arbol arbol) {
        if (arbol.getId() == null) {
            arbol.setId(contadorId.getAndIncrement());
            arbol.setFecha(LocalDate.now());
            arboles.add(arbol);
        } else {
            for (int i = 0; i < arboles.size(); i++) {
                if (arboles.get(i).getId().equals(arbol.getId())) {
                    arboles.set(i, arbol);
                    break;
                }
            }
        }
        return arbol;
    }

    public Optional<Arbol> buscarPorId(Long id) {
        for (Arbol a : arboles) {
            if (a.getId().equals(id)) {
                return Optional.of(a);
            }
        }
        return Optional.empty();
    }

    public boolean eliminar(Long id) {
        return arboles.removeIf(a -> a.getId().equals(id));
    }

    public int contarPorEstado(String estado) {
        int contador = 0;
        for (Arbol a : arboles) {
            if (a.getEstado().equalsIgnoreCase(estado)) {
                contador++;
            }
        }
        return contador;
    }
}