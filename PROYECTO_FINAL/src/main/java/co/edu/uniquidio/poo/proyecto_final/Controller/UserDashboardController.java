package co.edu.uniquidio.poo.proyecto_final.Controller;

import co.edu.uniquidio.poo.proyecto_final.model.Compra;
import co.edu.uniquidio.poo.proyecto_final.model.Evento;
import co.edu.uniquidio.poo.proyecto_final.model.SistemaGestionEventos;

import java.util.Collection;

public class UserDashboardController {

    private SistemaGestionEventos sistemaGestionEventos;

    public UserDashboardController(SistemaGestionEventos sistemaGestionEventos) {
        this.sistemaGestionEventos = sistemaGestionEventos;
    }

    /**
     * Obtiene la lista de eventos disponibles desde el modelo.
     */
    public Collection<Evento> obtenerEventosDisponibles() {
        return sistemaGestionEventos.getEventosDisponibles();
    }

    /**
     * Obtiene el historial de compras del usuario.
     */
    public Collection<Compra> obtenerHistorialCompras() {
        return sistemaGestionEventos.listarCompras();
    }
}