package co.edu.uniquidio.poo.proyecto_final.model;

/**
 * ============================================================
 *  PATRÓN: BUILDER (Creacional) — RF-049
 * ============================================================
 *  PROBLEMA: La entidad {@link Compra} tiene múltiples atributos
 *  opcionales (servicios adicionales, lista de entradas, etc.). Un
 *  constructor telescópico con todos los parámetros sería ilegible
 *  y propenso a errores.
 *
 *  PROPÓSITO: Separar la construcción de un objeto complejo de su
 *  representación, permitiendo que el mismo proceso de construcción
 *  pueda crear diferentes representaciones.
 *
 *  APLICACIÓN: {@code CompraBuilder} acumula los atributos de
 *  {@link Compra} mediante métodos encadenables ({@code set...()})
 *  y los materializa en {@code build()}. Usado en todos los lugares
 *  donde se crea una compra: sistema, controllers y tests.
 *
 *  ASCII:
 *  ┌────────────────────────────┐       ┌─────────────┐
 *  │ CompraBuilder              │──────►│ Compra      │
 *  │ + setUsuario(u)            │build()│             │
 *  │ + setEvento(e)             │       └─────────────┘
 *  │ + setTotal(t)              │
 *  │ + agregarServicio(s)       │
 *  │ + build(): Compra          │
 *  └────────────────────────────┘
 * ============================================================
 *
 * Builder para la entidad {@link Compra} (RF-034, RF-035, RF-049).
 * Permite construir una compra de manera fluida especificando solo
 * los atributos necesarios.
 *
 * <p>Uso típico:</p>
 * <pre>{@code
 * Compra c = new CompraBuilder()
 *     .setUsuario(usuario)
 *     .setEvento(evento)
 *     .setTotal(150000)
 *     .build();
 * }</pre>
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public class CompraBuilder {

    // Atributos del Builder (mismos de Compra)
    String idCompra           = "C" + System.currentTimeMillis();
    Usuario usuario;
    Evento evento;
    double total;
    ServicioAdicionalDecorator servicioDecorator;

    /**
     * Establece el usuario comprador.
     *
     * @param u el usuario
     * @return this (para encadenamiento)
     */
    public CompraBuilder setUsuario(Usuario u) {
        this.usuario = u;
        return this;
    }

    /**
     * Establece el evento al que corresponde la compra.
     *
     * @param e el evento
     * @return this (para encadenamiento)
     */
    public CompraBuilder setEvento(Evento e) {
        this.evento = e;
        return this;
    }

    /**
     * Establece el monto total de la compra antes de servicios adicionales.
     *
     * @param t el total en pesos
     * @return this (para encadenamiento)
     */
    public CompraBuilder setTotal(double t) {
        this.total = t;
        return this;
    }

    /**
     * Agrega un servicio adicional (Decorator) a la compra (RF-009).
     *
     * @param s el servicio adicional decorador
     * @return this (para encadenamiento)
     */
    public CompraBuilder agregarServicio(ServicioAdicionalDecorator s) {
        this.servicioDecorator = s;
        return this;
    }

    /**
     * Construye y devuelve la instancia de {@link Compra}.
     *
     * @return nueva instancia de {@link Compra} con los atributos configurados
     */
    public Compra build() {
        return new Compra(this);
    }
}
