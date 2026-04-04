package co.edu.uniquidio.poo.proyecto_final.model;

public class ReporteAdapter implements ReporteTarget {
    private final SistemaGestionEventos sistema = SistemaGestionEventos.getInstance();

    @Override
    public void generarReporte(String tipo, String rangoFechas) {
        if ("CSV".equalsIgnoreCase(tipo)) {
            // Lógica simple manual (RF-046)
            System.out.println("=== REPORTE CSV (" + rangoFechas + ") ===");
            System.out.println("Ventas totales: " + sistema.obtenerMetricas());
            // Aquí podrías escribir a archivo real con lógica básica
        } else if ("PDF".equalsIgnoreCase(tipo)) {
            System.out.println("=== REPORTE PDF (" + rangoFechas + ") ===");
            System.out.println("Métricas generadas desde SistemaGestionEventos");
        } else {
            throw new IllegalArgumentException("Tipo de reporte no soportado");
        }
    }

    public SistemaGestionEventos getSistema() {
        return sistema;
    }
}