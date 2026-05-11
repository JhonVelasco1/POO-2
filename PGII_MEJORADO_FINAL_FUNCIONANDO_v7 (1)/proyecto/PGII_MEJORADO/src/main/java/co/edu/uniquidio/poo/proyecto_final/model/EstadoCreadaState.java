package co.edu.uniquidio.poo.proyecto_final.model;

/**
 * Estado CREADA de la compra (State pattern — RF-051).
 * Permite: pagar, cancelar. No permite: confirmar, reembolsar.
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public class EstadoCreadaState implements EstadoCompraState {

    @Override
    public void pagar(Compra compra) {
        compra.setEstado("Pagada");
        compra.setEstadoActual(new EstadoPagadaState());
        System.out.println("✅ Compra " + compra.getIdCompra() + " pagada correctamente.");
        SistemaGestionEventosSingleton.getInstance()
            .notificarTodos("Pago confirmado para compra " + compra.getIdCompra());
    }

    @Override
    public void cancelar(Compra compra) {
        compra.setEstado("Cancelada");
        compra.setEstadoActual(new EstadoCanceladaState());
        System.out.println("❌ Compra " + compra.getIdCompra() + " cancelada antes del pago.");
    }

    @Override
    public void confirmar(Compra compra) {
        System.out.println("⚠️ No se puede confirmar una compra en estado 'Creada'. Primero debe pagarse.");
    }

    @Override
    public void reembolsar(Compra compra) {
        System.out.println("⚠️ No se puede reembolsar una compra que aún no ha sido pagada.");
    }
}
