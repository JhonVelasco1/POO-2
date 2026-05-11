package co.edu.uniquidio.poo.proyecto_final.Controller;

import co.edu.uniquidio.poo.proyecto_final.model.*;

import java.util.List;

/**
 * Controller para gestión de eventos (RF-013, RF-023, RF-024, RF-025).
 *
 * <p><b>SOLID — SRP:</b> única responsabilidad: operaciones CRUD y de estado sobre {@link Evento}.</p>
 * <p><b>SOLID — OCP:</b> usa {@link EventoFactoryMethod} para crear eventos;
 * agregar un nuevo tipo solo requiere modificar la factory.</p>
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public class GestionEventosController {

    private final SistemaGestionEventosSingleton sistema;

    public GestionEventosController(SistemaGestionEventosSingleton sistema) {
        this.sistema = sistema;
    }

    /**
     * Crea un evento usando el Factory Method (RF-023, RF-049).
     *
     * @param tipo      concierto | teatro | conferencia
     * @param nombre    nombre del evento
     * @param categoria categoría
     * @param ciudad    ciudad
     * @param fechaHora fecha y hora
     * @return el evento creado (en estado Borrador)
     */
    public Evento crearEvento(String tipo, String nombre, String categoria,
                              String ciudad, String fechaHora) {
        Evento e = EventoFactoryMethod.crearEvento(tipo, nombre, categoria, ciudad, fechaHora);
        sistema.crearEvento(e);
        return e;
    }

    /** RF-013: Lista todos los eventos. */
    public List<Evento> listarEventos() { return sistema.listarEventos(); }

    /** RF-003: Lista solo eventos publicados. */
    public List<Evento> listarEventosDisponibles() { return sistema.getEventosDisponibles(); }

    /** RF-003: Filtra eventos por nombre, ciudad y categoría. */
    public List<Evento> filtrar(String nombre, String ciudad, String categoria) {
        return sistema.filtrarEventos(nombre, ciudad, categoria);
    }

    /** RF-024: Publica un evento (Borrador → Publicado). */
    public void publicarEvento(String idEvento) {
        Evento e = sistema.buscarEventoPorId(idEvento);
        if (e != null) e.publicar();
    }

    /** RF-024: Pausa un evento. */
    public void pausarEvento(String idEvento) {
        Evento e = sistema.buscarEventoPorId(idEvento);
        if (e != null) e.pausar();
    }

    /** RF-024: Cancela un evento y notifica a usuarios. */
    public void cancelarEvento(String idEvento) {
        Evento e = sistema.buscarEventoPorId(idEvento);
        if (e != null) e.cancelar();
    }

    /** RF-013: Elimina un evento por ID. */
    public void eliminarEvento(String idEvento) {
        sistema.getEventos().removeIf(e -> e.getIdEvento().equals(idEvento));
    }

    /**
     * RF-023: Actualiza los datos básicos de un evento existente.
     *
     * @param idEvento  identificador del evento
     * @param nombre    nuevo nombre (null = sin cambio)
     * @param ciudad    nueva ciudad (null = sin cambio)
     * @param fechaHora nueva fecha/hora (null = sin cambio)
     */
    public void actualizarEvento(String idEvento, String nombre, String ciudad, String fechaHora) {
        Evento e = sistema.buscarEventoPorId(idEvento);
        if (e == null) return;
        if (nombre    != null && !nombre.isBlank())    e.setNombre(nombre.trim());
        if (ciudad    != null && !ciudad.isBlank())    e.setCiudad(ciudad.trim());
        if (fechaHora != null && !fechaHora.isBlank()) e.setFechaHora(fechaHora.trim());
    }

    /** RF-025: Consulta disponibilidad de un evento por zonas. */
    public String consultarDisponibilidad(String idEvento) {
        Evento e = sistema.buscarEventoPorId(idEvento);
        if (e == null || e.getRecinto() == null) return "Sin información de disponibilidad.";
        StringBuilder sb = new StringBuilder("Disponibilidad de " + e.getNombre() + ":\n");
        for (Zona z : e.getRecinto().getZonas()) {
            sb.append("  ").append(z.getNombre())
              .append(": ").append(z.getCapacidadDisponible())
              .append("/").append(z.getCapacidad()).append(" libres\n");
        }
        return sb.toString();
    }
}
