package co.edu.uniquidio.poo.proyecto_final.model;

import java.util.concurrent.atomic.AtomicLong;

/**
 * ============================================================
 *  PATRÓN: FACTORY METHOD (Creacional) — RF-049
 * ============================================================
 *  PROBLEMA: El sistema necesita crear distintos tipos de eventos
 *  (Concierto, Teatro, Conferencia). Si el código de creación estuviera
 *  disperso por los controllers, agregar un nuevo tipo requeriría
 *  modificar múltiples clases (viola OCP de SOLID).
 *
 *  PROPÓSITO: Definir una interfaz para crear objetos, pero dejar que
 *  las subclases (o un método de fábrica estático) decidan qué clase
 *  instanciar. Encapsula la lógica de construcción en un solo lugar.
 *
 *  APLICACIÓN: {@code crearEvento(tipo, ...)} recibe un string con el
 *  tipo y devuelve la subclase concreta correcta. Para añadir un nuevo
 *  tipo solo se modifica esta clase.
 *
 *  ASCII:
 *       «Factory»
 *  ┌──────────────────────────┐
 *  │ EventoFactoryMethod      │
 *  │ + crearEvento(tipo,..)   │──► Concierto
 *  └──────────────────────────┘──► Teatro
 *                                ──► Conferencia
 * ============================================================
 *
 * Fábrica estática para la creación de instancias de {@link Evento}.
 * Implementa el patrón Factory Method (RF-049). Centraliza la lógica
 * de instanciación de los distintos subtipos de evento.
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 * @see Concierto
 * @see Teatro
 * @see Conferencia
 */
public class EventoFactoryMethod {

    /** Contador atómico para garantizar IDs únicos aunque se creen muy rápido. */
    private static final AtomicLong contador = new AtomicLong(System.currentTimeMillis() % 100000);

    /**
     * Construtor privado — esta clase solo tiene métodos estáticos.
     */
    private EventoFactoryMethod() {}

    /**
     * Método de fábrica: crea y devuelve el tipo de evento correcto
     * según el parámetro {@code tipo}.
     *
     * <p>Tipos soportados (insensible a mayúsculas/minúsculas):</p>
     * <ul>
     *   <li>{@code "concierto"} → {@link Concierto}</li>
     *   <li>{@code "teatro"}   → {@link Teatro}</li>
     *   <li>{@code "conferencia"} → {@link Conferencia}</li>
     * </ul>
     *
     * @param tipo      tipo del evento (concierto / teatro / conferencia)
     * @param nombre    nombre del evento
     * @param categoria categoría (ej: Música, Arte, Tecnología)
     * @param ciudad    ciudad donde se realiza
     * @param fechaHora fecha y hora en formato libre
     * @return una nueva instancia del subtipo {@link Evento} correspondiente
     * @throws IllegalArgumentException si {@code tipo} no es reconocido
     */
    public static Evento crearEvento(String tipo, String nombre, String categoria,
                                     String ciudad, String fechaHora) {
        String id = "EVT-" + contador.incrementAndGet();
        String descripcion = generarDescripcion(tipo, nombre);

        return switch (tipo.toLowerCase()) {
            case "concierto"   -> new Concierto(id, nombre, categoria, descripcion, ciudad, fechaHora, null);
            case "teatro"      -> new Teatro(id, nombre, categoria, descripcion, ciudad, fechaHora, null);
            case "conferencia" -> new Conferencia(id, nombre, categoria, descripcion, ciudad, fechaHora, null);
            default -> throw new IllegalArgumentException(
                "Tipo de evento no reconocido: '" + tipo +
                "'. Usa: concierto, teatro, conferencia.");
        };
    }

    /**
     * Genera una descripción por defecto según el tipo de evento.
     *
     * @param tipo   tipo del evento
     * @param nombre nombre del evento
     * @return descripción generada automáticamente
     */
    private static String generarDescripcion(String tipo, String nombre) {
        return switch (tipo.toLowerCase()) {
            case "concierto"   -> "Concierto en vivo: " + nombre;
            case "teatro"      -> "Presentación teatral: " + nombre;
            case "conferencia" -> "Evento académico/tecnológico: " + nombre;
            default            -> "Evento: " + nombre;
        };
    }
}
