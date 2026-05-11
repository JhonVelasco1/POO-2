package co.edu.uniquidio.poo.proyecto_final.Controller;

import co.edu.uniquidio.poo.proyecto_final.model.*;

import java.util.List;

/**
 * Controller para la selección interactiva de asientos (RF-005, RF-015, RF-033).
 *
 * <p><b>SOLID — SRP:</b> única responsabilidad: gestionar la reserva de asientos
 * y crear la compra correspondiente.</p>
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public class SeleccionAsientosController {

    private final SistemaGestionEventosSingleton sistema;

    public SeleccionAsientosController(SistemaGestionEventosSingleton sistema) {
        this.sistema = sistema;
    }

    /**
     * Intenta reservar un asiento específico en un recinto (RF-005, RF-032).
     *
     * @param idRecinto  identificador del recinto
     * @param nombreZona nombre de la zona
     * @param fila       fila del asiento
     * @param numero     número del asiento
     * @return {@code true} si la reserva fue exitosa
     */
    public boolean reservarAsiento(String idRecinto, String nombreZona, String fila, int numero) {
        try {
            Recinto r = sistema.buscarRecintoPorId(idRecinto);
            if (r == null) return false;
            return r.getZonas().stream()
                    .filter(z -> z.getNombre().equalsIgnoreCase(nombreZona))
                    .findFirst()
                    .map(z -> z.reservarAsiento(fila, numero))
                    .orElse(false);
        } catch (Exception e) {
            sistema.registrarIncidencia(new Incidencia("DOBLE_COMPRA_ASIENTO",
                "Error al reservar " + fila + numero + " en zona " + nombreZona));
            return false;
        }
    }

    /**
     * Crea una compra vinculada a los asientos seleccionados (RF-034).
     *
     * @param usuario        usuario comprador
     * @param evento         evento seleccionado
     * @param zona           zona elegida
     * @param asientosCount  cantidad de asientos reservados
     * @return compra creada
     */
    public Compra confirmarCompraAsientos(Usuario usuario, Evento evento,
                                          Zona zona, int asientosCount) {
        double total = zona.getPrecioBase() * asientosCount;
        return sistema.crearCompra(new CompraBuilder()
                .setUsuario(usuario).setEvento(evento).setTotal(total));
    }

    /** @return lista de recintos disponibles para el selector */
    public List<Recinto> listarRecintos() { return sistema.listarRecintos(); }

    /** @return lista de eventos disponibles (publicados) */
    public List<Evento> listarEventosDisponibles() { return sistema.getEventosDisponibles(); }
}
