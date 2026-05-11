package co.edu.uniquidio.poo.proyecto_final.model;

/**
 * ============================================================
 *  PATRÓN: STATE (Comportamiento) — RF-051
 * ============================================================
 *  PROBLEMA: Una compra puede estar en varios estados (Creada, Pagada,
 *  Confirmada, Cancelada, Reembolsada). El comportamiento de acciones
 *  como {@code pagar()}, {@code cancelar()} y {@code confirmar()} varía
 *  según el estado actual. Sin State, esto se implementaría con un gran
 *  bloque de if/else que viola SRP y OCP.
 *
 *  PROPÓSITO: Permitir que un objeto altere su comportamiento cuando
 *  su estado interno cambia. El objeto parecerá cambiar de clase.
 *
 *  APLICACIÓN: Cada estado concreto (EstadoCreadaState, EstadoPagadaState,
 *  etc.) implementa esta interfaz. El objeto {@link Compra} delega las
 *  acciones al estado actual y este las ejecuta según sus reglas.
 *
 *  ASCII:
 *  ┌────────────────────────┐
 *  │ EstadoCompraState      │◄── Compra.estadoActual
 *  │ + pagar(Compra)        │
 *  │ + cancelar(Compra)     │
 *  │ + confirmar(Compra)    │
 *  │ + reembolsar(Compra)   │
 *  └────────────────────────┘
 *       ▲           ▲           ▲           ▲
 *  EstadoCreada EstadoPagada EstadoCancelada EstadoConfirmada
 * ============================================================
 *
 * Interfaz State para el ciclo de vida de una {@link Compra} (RF-008, RF-051).
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public interface EstadoCompraState {

    /**
     * Procesa el pago de la compra según el estado actual.
     * @param compra la compra sobre la que se aplica la acción
     */
    void pagar(Compra compra);

    /**
     * Cancela la compra según el estado actual y las políticas (RF-036).
     * @param compra la compra a cancelar
     */
    void cancelar(Compra compra);

    /**
     * Confirma la compra (generalmente por el administrador, RF-016).
     * @param compra la compra a confirmar
     */
    void confirmar(Compra compra);

    /**
     * Solicita reembolso según el estado actual (RF-036).
     * @param compra la compra a reembolsar
     */
    void reembolsar(Compra compra);
}
