package co.edu.uniquidio.poo.proyecto_patrones.Controller;

import co.edu.uniquidio.poo.proyecto_patrones.model.ReporteAdapter;

/**
 * CONTROLADOR DE REPORTES - RF-011 y RF-046
 * Usa Adapter Pattern del Model
 */
public class ReporteController {

    private final ReporteAdapter reporteAdapter = new ReporteAdapter();

    public void generarReporte(String tipo, String rangoFechas) {
        reporteAdapter.generarReporte(tipo, rangoFechas);
    }
}