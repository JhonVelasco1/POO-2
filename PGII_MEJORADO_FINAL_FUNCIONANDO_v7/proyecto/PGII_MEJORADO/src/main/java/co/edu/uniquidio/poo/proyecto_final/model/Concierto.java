package co.edu.uniquidio.poo.proyecto_final.model;

/**
 * Subclase de {@link Evento} que representa un concierto musical (RF-023).
 * Creado a través de {@link EventoFactoryMethod}.
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public class Concierto extends Evento {

    /**
     * Construye un evento de tipo Concierto.
     *
     * @param idEvento    identificador único
     * @param nombre      nombre del concierto
     * @param categoria   categoría musical
     * @param descripcion descripción del concierto
     * @param ciudad      ciudad del evento
     * @param fechaHora   fecha y hora
     * @param recinto     recinto donde se realiza
     */
    public Concierto(String idEvento, String nombre, String categoria,
                     String descripcion, String ciudad, String fechaHora, Recinto recinto) {
        super(idEvento, nombre, categoria, descripcion, ciudad, fechaHora, recinto);
    }

    /**
     * Devuelve el tipo de evento.
     *
     * @return "Concierto"
     */
    public String getTipoEvento() { return "Concierto"; }
}
