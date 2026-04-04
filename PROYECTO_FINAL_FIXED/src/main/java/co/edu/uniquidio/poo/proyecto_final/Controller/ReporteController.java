package co.edu.uniquidio.poo.proyecto_final.Controller;


import co.edu.uniquidio.poo.proyecto_final.model.SistemaGestionEventos;
import co.edu.uniquidio.poo.proyecto_final.model.ReporteAdapter;
// Asegúrate de importar tu ReporteAdapter aquí

public class ReporteController {

    private SistemaGestionEventos sistemaGestionEventos;
    private ReporteAdapter reporteAdapter;

    public ReporteController(SistemaGestionEventos sistemaGestionEventos) {
        this.sistemaGestionEventos = sistemaGestionEventos;
        this.reporteAdapter = new ReporteAdapter();
    }

    /**
     * Delega la generación del reporte al Adapter.
     */
    public void generarReporte(String tipo, String rangoFechas) {
        // Nota: En un caso real, aquí podrías pasarle datos al adapter.
        // Ejemplo: reporteAdapter.generarReporte(tipo, rangoFechas, sistemaGestionEventos.listarCompras());

        reporteAdapter.generarReporte(tipo, rangoFechas);
    }
}