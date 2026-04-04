package co.edu.uniquidio.poo.proyecto_final.model;

import java.util.concurrent.atomic.AtomicLong;

public class EventoFactory {

    // Contador atómico para garantizar IDs únicos aunque se creen muy rápido
    private static final AtomicLong contador = new AtomicLong(System.currentTimeMillis());

    public static Evento crearEvento(String tipo, String nombre, String categoria, String ciudad, String fechaHora) {
        // CORRECCIÓN: ya NO se crea un recinto nuevo aquí.
        // El recinto se asigna luego desde el sistema si es necesario.
        // Se usa null para que no pise el recinto real del sistema.
        String id = "E" + contador.incrementAndGet();

        return switch (tipo.toLowerCase()) {
            case "concierto"   -> new Concierto(id, nombre, categoria,
                                    "Concierto de " + nombre, ciudad, fechaHora, null);
            case "teatro"      -> new Teatro(id, nombre, categoria,
                                    "Obra de teatro " + nombre, ciudad, fechaHora, null);
            case "conferencia" -> new Conferencia(id, nombre, categoria,
                                    "Conferencia " + nombre, ciudad, fechaHora, null);
            default -> throw new IllegalArgumentException("Tipo de evento desconocido: " + tipo);
        };
    }
}
