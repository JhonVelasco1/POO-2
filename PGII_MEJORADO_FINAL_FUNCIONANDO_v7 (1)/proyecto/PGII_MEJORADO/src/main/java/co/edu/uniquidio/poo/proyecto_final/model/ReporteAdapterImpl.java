package co.edu.uniquidio.poo.proyecto_final.model;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Implementación concreta del Adapter de reportes (RF-046, RF-050).
 * Adapta la interfaz {@link ReporteTargetInterface} al sistema de
 * métricas de {@link SistemaGestionEventosSingleton}.
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public class ReporteAdapterImpl implements ReporteTargetInterface {

    private final SistemaGestionEventosSingleton sistema;

    public ReporteAdapterImpl() {
        this.sistema = SistemaGestionEventosSingleton.getInstance();
    }

    @Override
    public void generarReporte(String tipo, String rangoFechas) {
        String contenido = generarContenidoReporte(tipo, rangoFechas);
        System.out.println(contenido);
        if ("CSV".equalsIgnoreCase(tipo)) {
            exportarCSV(rangoFechas);
        }
    }

    @Override
    public String generarContenidoReporte(String tipo, String rangoFechas) {
        StringBuilder sb = new StringBuilder();
        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        sb.append("╔══════════════════════════════════════════════════╗\n");
        sb.append("║  REPORTE: ").append(tipo.toUpperCase())
          .append("   Generado: ").append(ts).append("\n");
        sb.append("║  Periodo: ").append(rangoFechas).append("\n");
        sb.append("╚══════════════════════════════════════════════════╝\n\n");

        List<Compra> compras = sistema.listarCompras();

        switch (tipo.toUpperCase()) {
            case "CSV", "VENTAS" -> {
                sb.append("📊 VENTAS POR EVENTO\n");
                sb.append("----------------------------------------------\n");
                sb.append("Evento,Compras,Ingresos,Estado\n");
                for (Evento e : sistema.listarEventos()) {
                    long numCompras = compras.stream()
                            .filter(c -> c.getEvento() != null
                                      && c.getEvento().getIdEvento().equals(e.getIdEvento()))
                            .count();
                    double ingresos = sistema.calcularIngresosPorEvento(e.getIdEvento());
                    sb.append(e.getNombre()).append(",")
                      .append(numCompras).append(",")
                      .append(String.format("%.0f", ingresos)).append(",")
                      .append(e.getEstado()).append("\n");
                }
                sb.append("\n📈 TOTAL SISTEMA\n");
                sb.append(sistema.obtenerMetricas()).append("\n");
            }
            case "PDF", "OCUPACION" -> {
                sb.append("🏟️ OCUPACIÓN POR ZONA\n");
                sb.append("----------------------------------------------\n");
                for (Recinto r : sistema.listarRecintos()) {
                    sb.append("Recinto: ").append(r.getNombre()).append("\n");
                    for (Zona z : r.getZonas()) {
                        double pct = z.getCapacidad() > 0
                                ? (z.getOcupacion() * 100.0 / z.getCapacidad()) : 0;
                        sb.append("  Zona ").append(z.getNombre())
                          .append(": ").append(z.getOcupacion())
                          .append("/").append(z.getCapacidad())
                          .append(" (").append(String.format("%.1f", pct)).append("%)\n");
                    }
                }
            }
            case "CANCELACION" -> {
                sb.append("❌ TASA DE CANCELACIÓN\n");
                sb.append("----------------------------------------------\n");
                long total = compras.size();
                long canceladas = compras.stream()
                        .filter(c -> "Cancelada".equals(c.getEstado())).count();
                double tasa = total > 0 ? (canceladas * 100.0 / total) : 0;
                sb.append("Total compras: ").append(total).append("\n");
                sb.append("Canceladas:    ").append(canceladas).append("\n");
                sb.append(String.format("Tasa:          %.1f%%\n", tasa));
            }
            case "TOP_EVENTOS" -> {
                sb.append("🏆 TOP EVENTOS POR VENTAS\n");
                sb.append("----------------------------------------------\n");
                sistema.listarEventos().stream()
                    .sorted((a, b) -> Double.compare(
                        sistema.calcularIngresosPorEvento(b.getIdEvento()),
                        sistema.calcularIngresosPorEvento(a.getIdEvento())))
                    .limit(5)
                    .forEach(e -> sb.append(String.format("  %-30s $%,.0f%n",
                        e.getNombre(),
                        sistema.calcularIngresosPorEvento(e.getIdEvento()))));
            }
            default -> sb.append("⚠️ Tipo de reporte no reconocido: ").append(tipo);
        }

        return sb.toString();
    }

    private void exportarCSV(String rangoFechas) {
        String nombreArchivo = "reporte_ventas_" +
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm")) + ".csv";
        exportarCSVEnRuta(nombreArchivo);
    }

    /**
     * Exporta el reporte de compras al archivo indicado por la ruta absoluta (RF-046).
     *
     * @param rutaAbsoluta ruta completa donde se guardará el CSV
     * @return {@code true} si se generó con éxito
     */
    public boolean exportarCSVEnRuta(String rutaAbsoluta) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(rutaAbsoluta))) {
            pw.println("idCompra,usuario,evento,total,estado,fecha,descripcion");
            for (Compra c : sistema.listarCompras()) {
                pw.println(
                    escapeCsv(c.getIdCompra()) + "," +
                    escapeCsv(c.getUsuario() != null ? c.getUsuario().getNombreCompleto() : "N/A") + "," +
                    escapeCsv(c.getEvento()   != null ? c.getEvento().getNombre()         : "N/A") + "," +
                    String.format("%.0f", c.getTotal()) + "," +
                    escapeCsv(c.getEstado()) + "," +
                    escapeCsv(c.getFechaCreacion()) + "," +
                    escapeCsv(c.getDescripcion())
                );
            }
            System.out.println("✅ CSV exportado: " + rutaAbsoluta);
            return true;
        } catch (IOException e) {
            System.err.println("❌ Error exportando CSV: " + e.getMessage());
            return false;
        }
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
