package co.edu.uniquidio.poo.proyecto_final.Controller;

import co.edu.uniquidio.poo.proyecto_final.model.*;

import java.util.List;

/**
 * Controller para gestión de compras (RF-006, RF-007, RF-008, RF-010,
 * RF-016, RF-034, RF-035, RF-036, RF-037, RF-038, RF-039, RF-040).
 *
 * <p><b>SOLID — SRP:</b> única responsabilidad: ciclo de vida de {@link Compra}.</p>
 * <p><b>SOLID — ISP:</b> métodos específicos para usuario y para administrador.</p>
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public class GestionComprasController {

    private final SistemaGestionEventosSingleton sistema;

    public GestionComprasController(SistemaGestionEventosSingleton sistema) {
        this.sistema = sistema;
    }

    /**
     * Crea una nueva compra usando el Builder (RF-034).
     *
     * @param usuario usuario comprador
     * @param evento  evento seleccionado
     * @param total   monto base
     * @return la compra creada en estado "Creada"
     */
    public Compra crearCompra(Usuario usuario, Evento evento, double total) {
        if (usuario == null) throw new IllegalStateException("No hay usuario logueado.");
        if (evento  == null) throw new IllegalArgumentException("Debe seleccionar un evento.");
        if (total   <= 0)    throw new IllegalArgumentException("El total debe ser mayor a cero.");
        return sistema.crearCompra(new CompraBuilder()
                .setUsuario(usuario).setEvento(evento).setTotal(total));
    }

    /**
     * Procesa el pago de una compra (RF-007).
     * Usa el patrón Strategy internamente para el método de pago.
     *
     * @param compra        la compra a pagar
     * @param metodo        método de pago: "tarjeta" | "pse" | "efectivo"
     * @param infoMetodo    número de tarjeta, banco, o referencia
     */
    public void pagarCompra(Compra compra, String metodo, String infoMetodo) {
        if (compra == null) throw new IllegalArgumentException("Compra no puede ser null.");
        // Strategy de pago (RF-051)
        PagoStrategyInterface estrategia = switch (metodo.toLowerCase()) {
            case "pse"      -> new PagoPSEStrategy();
            case "efectivo" -> new PagoEfectivoStrategy();
            default         -> new PagoTarjetaStrategy();
        };
        ProcesadorPago procesador = new ProcesadorPago(estrategia);
        boolean exitoso = procesador.ejecutarPago(compra.getTotal(), infoMetodo);
        if (exitoso) {
            compra.pagar();
            // Generar entradas automáticamente (RF-038)
            generarEntradas(compra);
        } else {
            sistema.registrarIncidencia(new Incidencia("PAGO_FALLIDO",
                    "Fallo al pagar compra " + compra.getIdCompra(), "Compra"));
        }
    }

    /**
     * Cancela una compra según políticas (RF-036).
     *
     * @param compra la compra a cancelar
     */
    public void cancelarCompra(Compra compra) {
        if (compra == null) return;
        compra.cancelar();
        // Anular entradas asociadas (RF-040)
        compra.getEntradas().forEach(Entrada::anular);
    }

    /**
     * Confirma una compra pagada (RF-016 — acción admin).
     *
     * @param compra la compra a confirmar
     */
    public void confirmarCompra(Compra compra) {
        if (compra != null) compra.confirmar();
    }

    /**
     * Solicita reembolso de una compra (RF-016, RF-036).
     *
     * @param compra la compra a reembolsar
     */
    public void reembolsarCompra(Compra compra) {
        if (compra != null) compra.reembolsar();
    }

    /**
     * Agrega un servicio adicional a una compra (RF-009).
     * Usa el patrón Decorator (RF-050).
     *
     * @param compra  la compra a decorar
     * @param servicio "vip" | "seguro" | "merchandising" | "parqueadero"
     */
    public void agregarServicioAdicional(Compra compra, String servicio) {
        if (compra == null) return;
        ServicioAdicionalDecorator dec = switch (servicio.toLowerCase()) {
            case "vip"          -> new ServicioVIPDecorator(compra);
            case "seguro"       -> new ServicioSeguroCancelacionDecorator(compra);
            case "merchandising"-> new ServicioMerchandisingDecorator(compra);
            case "parqueadero"  -> new ServicioParqueaderoDecorator(compra);
            default -> throw new IllegalArgumentException("Servicio no reconocido: " + servicio);
        };
        compra.setServicioDecorator(dec);
    }

    /**
     * Historial de compras del usuario logueado (RF-010).
     *
     * @return lista de compras del usuario
     */
    public List<Compra> historialUsuarioLogueado() {
        Usuario u = sistema.getUsuarioLogueado();
        if (u == null) return List.of();
        return sistema.listarComprasDeUsuario(u.getIdUsuario());
    }

    /** RF-016: Lista todas las compras (vista admin). */
    public List<Compra> listarTodasLasCompras() { return sistema.listarCompras(); }

    /** RF-037: Consulta una compra por ID. */
    public Compra buscarCompraPorId(String id) { return sistema.buscarCompraPorId(id); }

    /**
     * RF-038: Genera entradas asociadas a una compra pagada.
     *
     * @param compra la compra pagada
     */
    private void generarEntradas(Compra compra) {
        if (compra.getEvento() == null || compra.getEvento().getRecinto() == null) return;
        List<Zona> zonas = compra.getEvento().getRecinto().getZonas();
        if (zonas.isEmpty()) return;
        Zona zona = zonas.get(0); // Zona principal
        Entrada entrada = Entrada.generarEntrada(zona, null, zona.getPrecioBase());
        compra.agregarEntrada(entrada);
        System.out.println("🎟️ Entrada generada: " + entrada.getIdEntrada());
    }

    /** RF-039: Consulta entradas por compra. */
    public List<Entrada> obtenerEntradasDeCompra(Compra compra) {
        return compra != null ? compra.getEntradas() : List.of();
    }
}
