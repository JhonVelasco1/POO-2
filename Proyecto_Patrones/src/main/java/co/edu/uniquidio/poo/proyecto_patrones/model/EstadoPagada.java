package co.edu.uniquidio.poo.proyecto_patrones.model;

public class EstadoPagada implements EstadoCompra {
    @Override public void pagar(Compra compra) { System.out.println("Ya está pagada"); }
    @Override public void cancelar(Compra compra) {
        compra.setEstado("Cancelada");
        compra.setEstadoActual(new EstadoCancelada());
    }
    @Override public void confirmar(Compra compra) {
        compra.setEstado("Confirmada");
        System.out.println("Compra confirmada");
    }

}