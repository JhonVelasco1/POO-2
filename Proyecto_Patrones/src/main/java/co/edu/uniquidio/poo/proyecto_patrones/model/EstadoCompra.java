package co.edu.uniquidio.poo.proyecto_patrones.model;


public interface EstadoCompra {
    void pagar(Compra compra);
    void cancelar(Compra compra);
    void confirmar(Compra compra);
}