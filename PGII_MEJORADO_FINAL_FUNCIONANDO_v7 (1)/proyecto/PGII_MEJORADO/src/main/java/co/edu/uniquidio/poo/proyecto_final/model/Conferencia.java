package co.edu.uniquidio.poo.proyecto_final.model;

/**
 * Subclase de {@link Evento} que representa una conferencia o evento académico (RF-023).
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public class Conferencia extends Evento {

    public Conferencia(String idEvento, String nombre, String categoria,
                       String descripcion, String ciudad, String fechaHora, Recinto recinto) {
        super(idEvento, nombre, categoria, descripcion, ciudad, fechaHora, recinto);
    }

    /** @return "Conferencia" */
    public String getTipoEvento() { return "Conferencia"; }
}
