package co.edu.uniquidio.poo.proyecto_final.Controller;

import co.edu.uniquidio.poo.proyecto_final.model.*;

/**
 * Controller para generación de reportes operativos (RF-011, RF-018, RF-046).
 *
 * <p>Usa el patrón <b>Adapter</b> ({@link ReporteTargetInterface}) para desacoplar
 * la interfaz esperada por la vista de la lógica real de generación (RF-050).</p>
 *
 * <p><b>SOLID — SRP:</b> única responsabilidad: orquestar la generación de reportes.</p>
 * <p><b>SOLID — DIP:</b> depende de la interfaz {@link ReporteTargetInterface}, no de la implementación.</p>
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public class ReporteController {

    /** Adaptador que convierte la interfaz del sistema a lo que necesita la vista. */
    private final ReporteTargetInterface reporteAdapter;
    private final SistemaGestionEventosSingleton sistema;

    public ReporteController(SistemaGestionEventosSingleton sistema) {
        this.sistema        = sistema;
        this.reporteAdapter = new ReporteAdapterImpl();
    }

    /**
     * Genera un reporte del tipo especificado y lo exporta/imprime (RF-046).
     *
     * @param tipo        "CSV" | "PDF" | "VENTAS" | "OCUPACION" | "CANCELACION" | "TOP_EVENTOS"
     * @param rangoFechas rango de fechas descriptivo
     */
    public void generarReporte(String tipo, String rangoFechas) {
        reporteAdapter.generarReporte(tipo, rangoFechas);
    }

    /**
     * Genera el contenido del reporte como cadena de texto (para mostrar en UI).
     *
     * @param tipo        tipo de reporte
     * @param rangoFechas rango de fechas
     * @return contenido del reporte
     */
    public String obtenerContenidoReporte(String tipo, String rangoFechas) {
        return reporteAdapter.generarContenidoReporte(tipo, rangoFechas);
    }

    /**
     * Exporta el reporte de compras a un archivo CSV en la ruta especificada (RF-046).
     *
     * @param rutaAbsoluta ruta completa del archivo de destino
     * @return {@code true} si se exportó con éxito, {@code false} en caso de error
     */
    public boolean exportarCSVAArchivo(String rutaAbsoluta) {
        return ((ReporteAdapterImpl) reporteAdapter).exportarCSVEnRuta(rutaAbsoluta);
    }

    /**
     * Devuelve el resumen de métricas del sistema (RF-018).
     *
     * @return cadena con métricas resumidas
     */
    public String obtenerMetricas() {
        return sistema.obtenerMetricas();
    }
}
