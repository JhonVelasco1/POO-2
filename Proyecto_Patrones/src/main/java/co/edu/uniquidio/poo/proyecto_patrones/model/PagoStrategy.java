package co.edu.uniquidio.poo.proyecto_patrones.model;

public interface PagoStrategy {
    boolean procesarPago(double monto, String metodo);
}