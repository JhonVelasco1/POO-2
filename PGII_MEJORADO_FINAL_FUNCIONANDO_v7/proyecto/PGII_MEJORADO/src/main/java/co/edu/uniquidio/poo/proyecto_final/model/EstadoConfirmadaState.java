package co.edu.uniquidio.poo.proyecto_final.model;

/**
 * Estado CONFIRMADA de la compra (State pattern — RF-051).
 * La compra está completamente procesada.
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public class EstadoConfirmadaState implements EstadoCompraState {

    @Override
    public void pagar(Compra c) { System.out.println("⚠️ La compra ya fue confirmada."); }

    @Override
    public void cancelar(Compra c) {
        c.setEstado("Cancelada");
        c.setEstadoActual(new EstadoCanceladaState());
        System.out.println("❌ Compra confirmada cancelada. Aplica política de reembolso.");
    }

    @Override
    public void confirmar(Compra c) { System.out.println("⚠️ La compra ya está confirmada."); }

    @Override
    public void reembolsar(Compra c) {
        c.setEstado("Reembolsada");
        System.out.println("💰 Reembolso parcial procesado (compra confirmada).");
    }
}
