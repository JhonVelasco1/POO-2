package co.edu.uniquidio.poo.proyecto_final.Controller;


import co.edu.uniquidio.poo.proyecto_final.model.SistemaGestionEventos;

public class SeleccionAsientosController {

    private SistemaGestionEventos sistemaGestionEventos;

    public SeleccionAsientosController(SistemaGestionEventos sistemaGestionEventos) {
        this.sistemaGestionEventos = sistemaGestionEventos;
    }

    /**
     * Intenta reservar un asiento delegando la búsqueda al modelo.
     * @param recintoId ID del recinto (ej: "R001")
     * @param zonaIndex Índice de la zona en la lista
     * @param fila Letra de la fila
     * @param numero Número del asiento
     * @return true si la reserva fue exitosa
     */
    public boolean realizarReserva(String recintoId, int zonaIndex, String fila, int numero) {
        try {
            return sistemaGestionEventos
                    .buscarRecintoPorId(recintoId)
                    .getZonas()
                    .get(zonaIndex)
                    .reservarAsiento(fila, numero);
        } catch (Exception e) {
            // Manejo de error si el recinto o zona no existen
            return false;
        }
    }
}