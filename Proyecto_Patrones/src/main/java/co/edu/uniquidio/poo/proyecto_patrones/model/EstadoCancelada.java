package co.edu.uniquidio.poo.proyecto_patrones.model;
public class EstadoCancelada implements EstadoCompra {
    @Override public void pagar(Compra compra) { System.out.println("No se puede pagar una compra cancelada"); }
    @Override public void cancelar(Compra compra) { System.out.println("Ya está cancelada"); }
    @Override public void confirmar(Compra compra) { System.out.println("No se puede confirmar una compra cancelada"); }
}