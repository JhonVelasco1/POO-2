package co.edu.uniquidio.poo.proyecto_patrones.Controller;


import co.edu.uniquidio.poo.proyecto_patrones.model.SistemaGestionEventos;
import co.edu.uniquidio.poo.proyecto_patrones.model.Usuario;
import co.edu.uniquidio.poo.proyecto_patrones.model.Evento; // Asumiendo que existe la clase Evento
import java.util.Collection;

public class AdminDashboardController {

    private SistemaGestionEventos sistemaGestionEventos;

    public AdminDashboardController(SistemaGestionEventos sistemaGestionEventos) {
        this.sistemaGestionEventos = sistemaGestionEventos;
    }

    /**
     * Obtiene la lista de usuarios desde el modelo.
     */
    public Collection<Usuario> obtenerUsuarios() {
        return sistemaGestionEventos.listarUsuarios();
    }

    /**
     * Obtiene la lista de eventos desde el modelo.
     */
    public Collection<Evento> obtenerEventos() {
        return sistemaGestionEventos.listarEventos();
    }

    // Aquí podrías agregar métodos para obtener datos reales de ventas u ocupación
    // Ejemplo: public Map<String, Double> obtenerEstadisticasVentas() { ... }
}