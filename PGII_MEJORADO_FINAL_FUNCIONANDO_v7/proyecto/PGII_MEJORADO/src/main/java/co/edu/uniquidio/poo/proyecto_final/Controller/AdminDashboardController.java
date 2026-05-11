package co.edu.uniquidio.poo.proyecto_final.Controller;

import co.edu.uniquidio.poo.proyecto_final.model.*;

import java.util.List;

/**
 * Controller del panel de administrador (RF-012, RF-013, RF-014, RF-016, RF-017, RF-018).
 *
 * <p><b>SOLID — ISP:</b> expone solo las operaciones necesarias para el panel admin,
 * separadas de las del usuario normal.</p>
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public class AdminDashboardController {

    private final SistemaGestionEventosSingleton sistema;

    public AdminDashboardController(SistemaGestionEventosSingleton sistema) {
        this.sistema = sistema;
    }

    /** RF-012: Lista todos los usuarios. */
    public List<Usuario> obtenerUsuarios() { return sistema.listarUsuarios(); }

    /** RF-013: Lista todos los eventos. */
    public List<Evento> obtenerEventos()   { return sistema.listarEventos(); }

    /** RF-016: Lista todas las compras. */
    public List<Compra> obtenerCompras()   { return sistema.listarCompras(); }

    /** RF-017: Lista todas las incidencias. */
    public List<Incidencia> obtenerIncidencias() { return sistema.getIncidencias(); }

    /** RF-017: Registra una incidencia manual. */
    public void registrarIncidencia(String tipo, String descripcion) {
        sistema.registrarIncidencia(new Incidencia(tipo, descripcion));
    }

    /** RF-018: Resumen de métricas del sistema. */
    public String obtenerMetricas() { return sistema.obtenerMetricas(); }

    /** RF-018: Ingresos calculados para un evento. */
    public double calcularIngresosPorEvento(String idEvento) {
        return sistema.calcularIngresosPorEvento(idEvento);
    }

    /** RF-016: Cancela una compra desde el panel admin (con reembolso si aplica). */
    public void cancelarCompraAdmin(Compra compra) {
        if (compra != null) {
            compra.cancelar();
            compra.getEntradas().forEach(Entrada::anular);
            sistema.registrarIncidencia(new Incidencia("CANCELACION_ADMIN",
                "Compra " + compra.getIdCompra() + " cancelada por administrador."));
        }
    }
}
