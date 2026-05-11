package co.edu.uniquidio.poo.proyecto_final.model;

/**
 * ============================================================
 *  PATRÓN: OBSERVER (Comportamiento) — RF-051
 * ============================================================
 *  PROBLEMA: Cuando cambia el estado de un evento o una compra,
 *  múltiples usuarios deben ser notificados automáticamente sin
 *  que el sistema conozca los detalles de cada usuario.
 *
 *  PROPÓSITO: Definir una dependencia uno-a-muchos entre objetos.
 *  Cuando el sujeto ({@link SistemaGestionEventosSingleton}) cambia
 *  de estado, todos sus dependientes (usuarios) son notificados.
 *
 *  APLICACIÓN: {@link Usuario} implementa esta interfaz. El sistema
 *  llama a {@code notificarTodos(msg)} al crear compras, cancelar
 *  eventos o registrar incidencias (RF-008, RF-017).
 *
 *  ASCII:
 *  ┌─────────────────────────┐        ┌─────────────────────────────────────┐
 *  │ ObserverNotificacion    │◄───────│ SistemaGestionEventosSingleton      │
 *  │ + actualizar(msg)       │        │ + agregarObserver(o)                 │
 *  └─────────────────────────┘        │ + notificarTodos(msg)                │
 *           ▲                         └─────────────────────────────────────┘
 *           │
 *  ┌─────────────────────────┐
 *  │ Usuario                 │
 *  │ + actualizar(msg)       │
 *  └─────────────────────────┘
 * ============================================================
 *
 * Interfaz Observer para el sistema de notificaciones (RF-008, RF-017).
 * Toda entidad que desee recibir notificaciones del sistema debe
 * implementar esta interfaz.
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public interface ObserverNotificacion {

    /**
     * Método invocado por el sistema cuando ocurre un evento relevante.
     *
     * @param mensaje el mensaje de notificación
     */
    void actualizar(String mensaje);
}
