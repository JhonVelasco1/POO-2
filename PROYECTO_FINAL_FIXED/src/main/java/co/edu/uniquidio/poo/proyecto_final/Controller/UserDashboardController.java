package co.edu.uniquidio.poo.proyecto_final.Controller;

import co.edu.uniquidio.poo.proyecto_final.model.*;

import java.util.Collection;
import java.util.List;

/**
 * Controller del dashboard de usuario (RF-003, RF-006, RF-007, RF-008, RF-010).
 */
public class UserDashboardController {

    private final SistemaGestionEventos sistema;

    public UserDashboardController(SistemaGestionEventos sistema) {
        this.sistema = sistema;
    }

    /** RF-003: Eventos disponibles (publicados) */
    public List<Evento> obtenerEventosDisponibles() {
        return sistema.getEventosDisponibles();
    }

    /** RF-010: Historial de compras del usuario logueado */
    public List<Compra> obtenerHistorialCompras() {
        Usuario u = sistema.getUsuarioLogueado();
        if (u == null) return List.of();
        return sistema.listarComprasDeUsuario(u.getIdUsuario());
    }

    /** RF-006: Crear compra para un evento */
    public Compra crearCompra(Evento evento, double total) {
        Usuario u = sistema.getUsuarioLogueado();
        if (u == null) throw new IllegalStateException("No hay usuario logueado.");
        return sistema.crearCompra(new Compra.CompraBuilder()
                .setUsuario(u).setEvento(evento).setTotal(total));
    }

    /** RF-007: Pagar una compra */
    public void pagarCompra(Compra compra) {
        if (compra != null) compra.pagar();
    }

    /** RF-006: Cancelar una compra */
    public void cancelarCompra(Compra compra) {
        if (compra != null) compra.cancelar();
    }
}
