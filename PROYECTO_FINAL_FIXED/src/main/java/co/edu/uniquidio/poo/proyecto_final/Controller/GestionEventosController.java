package co.edu.uniquidio.poo.proyecto_final.Controller;

import co.edu.uniquidio.poo.proyecto_final.model.Evento;
import co.edu.uniquidio.poo.proyecto_final.model.EventoFactory;
import co.edu.uniquidio.poo.proyecto_final.model.SistemaGestionEventos;

import java.util.List;

/**
 * Controller para gestión de eventos (RF-013, RF-023, RF-024).
 * Principio SRP: solo gestiona operaciones sobre Evento.
 */
public class GestionEventosController {

    private final SistemaGestionEventos sistema;

    public GestionEventosController(SistemaGestionEventos sistema) {
        this.sistema = sistema;
    }

    /** RF-023: Crear evento usando Factory Method (RF-049) */
    public Evento crearEvento(String tipo, String nombre, String categoria, String ciudad, String fechaHora) {
        Evento e = EventoFactory.crearEvento(tipo, nombre, categoria, ciudad, fechaHora);
        sistema.crearEvento(e);
        return e;
    }

    /** RF-013: Listar todos los eventos */
    public List<Evento> listarEventos() {
        return sistema.listarEventos();
    }

    /** RF-024: Publicar evento */
    public void publicarEvento(String idEvento) {
        Evento e = sistema.buscarEventoPorId(idEvento);
        if (e != null) e.publicar();
    }

    /** RF-024: Pausar evento */
    public void pausarEvento(String idEvento) {
        Evento e = sistema.buscarEventoPorId(idEvento);
        if (e != null) e.setEstado("Pausado");
    }

    /** RF-024: Cancelar evento */
    public void cancelarEvento(String idEvento) {
        Evento e = sistema.buscarEventoPorId(idEvento);
        if (e != null) e.cancelar();
    }

    /** RF-013: Eliminar evento */
    public void eliminarEvento(String idEvento) {
        sistema.getEventos().removeIf(e -> e.getIdEvento().equals(idEvento));
    }

    /** RF-023: Actualizar datos básicos de un evento */
    public void actualizarEvento(String idEvento, String nombre, String ciudad, String fechaHora) {
        Evento e = sistema.buscarEventoPorId(idEvento);
        if (e != null) {
            if (nombre != null && !nombre.isEmpty()) e.setNombre(nombre);
            if (ciudad != null && !ciudad.isEmpty()) e.setCiudad(ciudad);
            if (fechaHora != null && !fechaHora.isEmpty()) e.setFechaHora(fechaHora);
        }
    }
}
