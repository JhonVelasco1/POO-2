package co.edu.uniquidio.poo.proyecto_patrones.model;

public class TarjetaStrategy implements PagoStrategy {
    @Override public boolean procesarPago(double monto, String metodo) {
        System.out.println("Pago con tarjeta procesado: $" + monto);
        return true;
    }
}
// Más estrategias: PayPalStrategy, EfectivoStrategy