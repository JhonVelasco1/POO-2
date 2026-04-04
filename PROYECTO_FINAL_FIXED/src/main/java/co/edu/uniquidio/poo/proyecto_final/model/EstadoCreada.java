package co.edu.uniquidio.poo.proyecto_final.model;

public class EstadoCreada implements EstadoCompra {
    @Override
    public void pagar(Compra compra) {
        compra.setEstado("Pagada");
        compra.setEstadoActual(new EstadoPagada());
        System.out.println("Compra " + compra.getIdCompra() + " pagada correctamente");
    }

    @Override
    public void cancelar(Compra compra) {
        compra.setEstado("Cancelada");
        compra.setEstadoActual(new EstadoCancelada());
        System.out.println("Compra cancelada antes de pagar");
    }

    @Override
    public void confirmar(Compra compra) {
        System.out.println("No se puede confirmar una compra en estado Creada");
    }
}