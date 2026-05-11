package co.edu.uniquidio.poo.proyecto_final.model;

/**
 * Subclase de {@link Evento} que representa una obra de teatro (RF-023).
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public class Teatro extends Evento {

    public Teatro(String idEvento, String nombre, String categoria,
                  String descripcion, String ciudad, String fechaHora, Recinto recinto) {
        super(idEvento, nombre, categoria, descripcion, ciudad, fechaHora, recinto);
    }

    /** @return "Teatro" */
    public String getTipoEvento() { return "Teatro"; }
}
