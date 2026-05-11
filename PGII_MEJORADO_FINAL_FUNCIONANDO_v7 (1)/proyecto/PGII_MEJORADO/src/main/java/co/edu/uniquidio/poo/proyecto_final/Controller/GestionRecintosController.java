package co.edu.uniquidio.poo.proyecto_final.Controller;

import co.edu.uniquidio.poo.proyecto_final.model.*;

import java.util.List;

/**
 * Controller para gestión de recintos, zonas y asientos
 * (RF-014, RF-015, RF-026, RF-027, RF-028, RF-029, RF-030, RF-031, RF-032, RF-033).
 *
 * <p><b>SOLID — SRP:</b> única responsabilidad: CRUD de recintos/zonas/asientos.</p>
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public class GestionRecintosController {

    private final SistemaGestionEventosSingleton sistema;

    public GestionRecintosController(SistemaGestionEventosSingleton sistema) {
        this.sistema = sistema;
    }

    /** RF-026: Lista todos los recintos. */
    public List<Recinto> listarRecintos() { return sistema.listarRecintos(); }

    /**
     * RF-026: Crea un nuevo recinto.
     *
     * @param id        identificador único
     * @param nombre    nombre del recinto
     * @param direccion dirección física
     * @param ciudad    ciudad
     * @return el recinto creado
     */
    public Recinto crearRecinto(String id, String nombre, String direccion, String ciudad) {
        Recinto r = new Recinto(id, nombre, direccion, ciudad);
        sistema.agregarRecinto(r);
        return r;
    }

    /**
     * RF-027: Agrega una zona a un recinto existente (RF-028, RF-029).
     *
     * @param idRecinto  ID del recinto
     * @param idZona     ID de la zona
     * @param nombre     nombre de la zona
     * @param capacidad  capacidad total
     * @param precioBase precio base en pesos
     */
    public void agregarZonaARecinto(String idRecinto, String idZona, String nombre,
                                    int capacidad, double precioBase) {
        Recinto r = sistema.buscarRecintoPorId(idRecinto);
        if (r == null) throw new IllegalArgumentException("Recinto no encontrado: " + idRecinto);
        Zona z = new Zona(idZona, nombre, capacidad, precioBase);
        r.agregarZona(z);
    }

    /** RF-030: Consulta la ocupación de una zona. */
    public String consultarOcupacionZona(String idRecinto, String nombreZona) {
        Recinto r = sistema.buscarRecintoPorId(idRecinto);
        if (r == null) return "Recinto no encontrado.";
        return r.getZonas().stream()
                .filter(z -> z.getNombre().equalsIgnoreCase(nombreZona))
                .findFirst()
                .map(z -> nombreZona + ": " + z.getOcupacion() + "/" + z.getCapacidad()
                         + " (" + String.format("%.1f%%", sistema.calcularOcupacionZona(nombreZona)) + ")")
                .orElse("Zona no encontrada.");
    }

    /**
     * RF-032: Cambia el estado de un asiento.
     *
     * @param idRecinto  ID del recinto
     * @param nombreZona nombre de la zona
     * @param fila       fila del asiento
     * @param numero     número del asiento
     * @param estado     nuevo estado: Disponible | Reservado | Vendido | Bloqueado
     * @return {@code true} si se cambió exitosamente
     */
    public boolean cambiarEstadoAsiento(String idRecinto, String nombreZona,
                                        String fila, int numero, String estado) {
        Recinto r = sistema.buscarRecintoPorId(idRecinto);
        if (r == null) return false;
        return r.getZonas().stream()
                .filter(z -> z.getNombre().equalsIgnoreCase(nombreZona))
                .findFirst()
                .map(z -> {
                    z.getAsientos().stream()
                     .filter(a -> a.getFila().equals(fila) && a.getNumero() == numero)
                     .findFirst()
                     .ifPresent(a -> a.cambiarEstado(estado));
                    return true;
                }).orElse(false);
    }

    /** RF-033: Muestra el mapa de asientos de una zona en consola. */
    public void mostrarMapaZona(String idRecinto, String nombreZona) {
        Recinto r = sistema.buscarRecintoPorId(idRecinto);
        if (r == null) { System.out.println("Recinto no encontrado."); return; }
        r.getZonas().stream()
         .filter(z -> z.getNombre().equalsIgnoreCase(nombreZona))
         .findFirst()
         .ifPresent(ComponenteSeatingComposite::mostrarMapa);
    }
}
