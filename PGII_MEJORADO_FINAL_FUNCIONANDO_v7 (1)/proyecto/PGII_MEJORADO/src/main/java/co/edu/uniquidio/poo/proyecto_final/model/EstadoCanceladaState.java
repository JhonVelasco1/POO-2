package co.edu.uniquidio.poo.proyecto_final.model;

/**
 * Estado CANCELADA de la compra (State pattern — RF-051).
 * Estado terminal — ninguna acción está permitida.
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public class EstadoCanceladaState implements EstadoCompraState {

    @Override
    public void pagar(Compra c)     { System.out.println("⚠️ No se puede pagar una compra cancelada."); }

    @Override
    public void cancelar(Compra c)  { System.out.println("⚠️ La compra ya está cancelada."); }

    @Override
    public void confirmar(Compra c) { System.out.println("⚠️ No se puede confirmar una compra cancelada."); }

    @Override
    public void reembolsar(Compra c){ System.out.println("⚠️ La compra ya está en estado terminal."); }
}
