package co.edu.uniquidio.poo.proyecto_final.model;

/**
 * ============================================================
 *  PATRÓN: COMPOSITE (Estructural) — RF-050
 * ============================================================
 *  PROBLEMA: Un recinto tiene zonas, y cada zona tiene asientos.
 *  Las operaciones como "consultar disponibilidad" o "mostrar mapa"
 *  deben funcionar de forma uniforme tanto para una zona entera como
 *  para un asiento individual. Sin Composite, el código cliente
 *  tendría que distinguir siempre entre nodo y hoja.
 *
 *  PROPÓSITO: Componer objetos en estructuras de árbol para representar
 *  jerarquías parte-todo. Permite que los clientes traten objetos
 *  individuales y composiciones de objetos de manera uniforme.
 *
 *  APLICACIÓN:
 *  - {@code Recinto}  → Raíz del árbol (contiene zonas)
 *  - {@link Zona}     → Nodo intermedio del Composite (contiene asientos)
 *  - {@link Asiento}  → Hoja del Composite (unidad básica)
 *
 *  ASCII:
 *  ComponenteSeatingComposite
 *       │
 *  ┌────┴────┐
 *  Zona    Asiento   (ambos implementan la interfaz)
 *  │
 *  Asiento*  (hijos de la zona)
 * ============================================================
 *
 * Interfaz Composite para la jerarquía de asientos del sistema (RF-033, RF-050).
 * Implementada por {@link Zona} (nodo) y {@link Asiento} (hoja).
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public interface ComponenteSeatingComposite {

    /**
     * Muestra el mapa de asientos en consola (RF-033).
     */
    void mostrarMapa();

    /**
     * Calcula la capacidad disponible (asientos libres).
     *
     * @return número de asientos disponibles en este componente
     */
    int getCapacidadDisponible();

    /**
     * Intenta reservar un asiento específico por fila y número (RF-005, RF-032).
     *
     * @param fila   identificador de fila (ej: "A", "B")
     * @param numero número de asiento
     * @return {@code true} si la reserva fue exitosa
     */
    boolean reservarAsiento(String fila, int numero);
}
