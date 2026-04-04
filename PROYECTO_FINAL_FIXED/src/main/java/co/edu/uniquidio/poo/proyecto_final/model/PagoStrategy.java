package co.edu.uniquidio.poo.proyecto_final.model;

public interface PagoStrategy {
    boolean procesarPago(double monto, String metodo);
}