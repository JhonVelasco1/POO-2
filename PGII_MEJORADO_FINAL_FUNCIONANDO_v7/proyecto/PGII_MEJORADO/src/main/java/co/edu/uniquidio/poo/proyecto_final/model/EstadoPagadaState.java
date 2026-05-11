package co.edu.uniquidio.poo.proyecto_final.model;

/**
 * Estado PAGADA de la compra (State pattern — RF-051).
 * Permite: confirmar, cancelar (con posible reembolso), reembolsar.
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public class EstadoPagadaState implements EstadoCompraState {

    @Override
    public void pagar(Compra compra) {
        System.out.println("⚠️ La compra " + compra.getIdCompra() + " ya está pagada.");
    }

    @Override
    public void cancelar(Compra compra) {
        compra.setEstado("Cancelada");
        compra.setEstadoActual(new EstadoCanceladaState());
        System.out.println("❌ Compra " + compra.getIdCompra() + " cancelada. Se procesará reembolso.");
        SistemaGestionEventosSingleton.getInstance()
            .registrarIncidencia(new Incidencia("CANCELACION_POST_PAGO",
                "Compra " + compra.getIdCompra() + " cancelada después del pago."));
    }

    @Override
    public void confirmar(Compra compra) {
        compra.setEstado("Confirmada");
        compra.setEstadoActual(new EstadoConfirmadaState());
        System.out.println("✅ Compra " + compra.getIdCompra() + " confirmada.");
    }

    @Override
    public void reembolsar(Compra compra) {
        compra.setEstado("Reembolsada");
        compra.setEstadoActual(new EstadoCanceladaState());
        System.out.println("💰 Reembolso procesado para compra " + compra.getIdCompra());
    }
}
