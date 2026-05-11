package co.edu.uniquidio.poo.proyecto_final.model;

/**
 * ============================================================
 *  PATRÓN: ADAPTER (Estructural) — RF-050
 * ============================================================
 *  PROPÓSITO: Convertir la interfaz de una clase en otra interfaz que
 *  los clientes esperan.
 *
 *  ASCII:
 *  Controller → ReporteTargetInterface ← ReporteAdapterImpl → SistemaGestionEventosSingleton
 * ============================================================
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public interface ReporteTargetInterface {
    void generarReporte(String tipo, String rangoFechas);
    String generarContenidoReporte(String tipo, String rangoFechas);
}
