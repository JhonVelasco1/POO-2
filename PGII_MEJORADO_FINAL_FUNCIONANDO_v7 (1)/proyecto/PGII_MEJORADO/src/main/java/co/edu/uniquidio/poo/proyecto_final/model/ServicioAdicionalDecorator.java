package co.edu.uniquidio.poo.proyecto_final.model;

/**
 * ============================================================
 *  PATRÓN: DECORATOR (Estructural) — RF-050
 * ============================================================
 *  PROBLEMA: Los usuarios pueden agregar servicios adicionales a una
 *  compra (VIP, seguro de cancelación, merchandising, parqueadero — RF-009).
 *  Usar herencia para cada combinación posible generaría una explosión de subclases.
 *
 *  PROPÓSITO: Adjuntar responsabilidades adicionales a un objeto de
 *  forma dinámica.
 *
 *  ASCII:
 *  ┌──────────────────────────────┐
 *  │ ServicioAdicionalDecorator   │◄── compraBase: Compra
 *  │ # compraBase                 │
 *  │ + getPrecio(): double        │
 *  │ + getDescripcion(): String   │
 *  └──────────────────────────────┘
 *       ▲           ▲           ▲           ▲
 *  ServicioVIP ServicioSeguro ServicioMerch ServicioParqueadero
 * ============================================================
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public abstract class ServicioAdicionalDecorator {

    /** La compra base que se está decorando. */
    protected Compra compraBase;

    /**
     * Construye el decorador envolviendo una compra existente.
     * @param compraBase la compra a la que se añade el servicio
     */
    protected ServicioAdicionalDecorator(Compra compraBase) {
        this.compraBase = compraBase;
    }

    /** @return precio total en pesos incluyendo el servicio adicional */
    public abstract double getPrecio();

    /** @return descripción enriquecida con el servicio adicional */
    public abstract String getDescripcion();
}
