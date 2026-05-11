package co.edu.uniquidio.poo.proyecto_final.model;

/**
 * Contexto del patrón Strategy: procesa pagos usando la estrategia inyectada.
 * Desacopla al cliente de la implementación concreta del pago (RF-047 DIP).
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public class ProcesadorPago {

    private PagoStrategyInterface estrategia;

    public ProcesadorPago(PagoStrategyInterface estrategia) {
        this.estrategia = estrategia;
    }

    public void setEstrategia(PagoStrategyInterface estrategia) {
        this.estrategia = estrategia;
    }

    public boolean ejecutarPago(double monto, String informacion) {
        System.out.println("🔄 Usando método: " + estrategia.getNombreMetodo());
        return estrategia.procesarPago(monto, informacion);
    }
}
