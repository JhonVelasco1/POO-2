package co.edu.uniquidio.poo.proyecto_final.model;

/**
 * ============================================================
 *  PATRÓN: STRATEGY (Comportamiento) — RF-051
 * ============================================================
 *  PROBLEMA: El sistema debe soportar múltiples métodos de pago
 *  (tarjeta, PSE, efectivo). Sin Strategy, se usarían if/else o
 *  switch para cada método, violando el principio OCP.
 *
 *  PROPÓSITO: Definir una familia de algoritmos, encapsular cada uno
 *  y hacerlos intercambiables.
 *
 *  ASCII:
 *  ┌──────────────────────────────┐
 *  │ PagoStrategyInterface        │◄── ProcesadorPago.estrategia
 *  │ + procesarPago(monto, info)  │
 *  └──────────────────────────────┘
 *       ▲           ▲           ▲
 *  PagoTarjeta   PagoPSE  PagoEfectivo
 * ============================================================
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public interface PagoStrategyInterface {
    boolean procesarPago(double monto, String informacion);
    String getNombreMetodo();
}
