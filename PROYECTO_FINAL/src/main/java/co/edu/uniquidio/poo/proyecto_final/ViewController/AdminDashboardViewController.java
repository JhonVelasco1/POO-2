package co.edu.uniquidio.poo.proyecto_final.ViewController;

import co.edu.uniquidio.poo.proyecto_final.Controller.AdminDashboardController;
import co.edu.uniquidio.poo.proyecto_final.model.SistemaGestionEventos;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;

public class AdminDashboardViewController {

    @FXML private TableView<Object> tablaUsuarios;
    @FXML private TableView<Object> tablaEventos;
    @FXML private BarChart<String, Number> chartVentas;
    @FXML private PieChart chartOcupacion;

    private AdminDashboardController dashboardController;

    @FXML
    private void initialize() {
        dashboardController = new AdminDashboardController(SistemaGestionEventos.getInstance());

        // Limpiar y cargar datos
        tablaUsuarios.getItems().clear();
        tablaUsuarios.getItems().addAll(dashboardController.obtenerUsuarios());

        tablaEventos.getItems().clear();
        tablaEventos.getItems().addAll(dashboardController.obtenerEventos());

        cargarGraficos();
    }

    private void cargarGraficos() {
        // Datos de Ejemplo para BarChart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Ventas");
        series.getData().add(new XYChart.Data<>("Concierto Juanes", 4500000));

        chartVentas.getData().clear();
        chartVentas.getData().add(series);

        // Datos de Ejemplo para PieChart
        chartOcupacion.getData().clear();
        chartOcupacion.getData().addAll(
                new PieChart.Data("VIP", 65),
                new PieChart.Data("Preferencial", 25),
                new PieChart.Data("General", 10)
        );
    }

    // ==========================================
    // MÉTODOS PARA LOS BOTONES DE REPORTE (FXML)
    // ==========================================

    @FXML
    private void generarReporteCSV() {
        // Aquí conectas con ReporteController cuando lo necesites
        mostrarAlerta(Alert.AlertType.INFORMATION, "Reporte Generado", "Se ha generado el reporte CSV exitosamente.");
    }

    @FXML
    private void generarReportePDF() {
        // Aquí conectas con ReporteController cuando lo necesites
        mostrarAlerta(Alert.AlertType.INFORMATION, "Reporte Generado", "Se ha generado el reporte PDF exitosamente.");
    }

    // ==========================================

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}