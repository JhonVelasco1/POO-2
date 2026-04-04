package co.edu.uniquidio.poo.proyecto_patrones.ViewController;




import co.edu.uniquidio.poo.proyecto_patrones.Controller.AdminDashboardController;
import co.edu.uniquidio.poo.proyecto_patrones.model.SistemaGestionEventos;
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
        // 1. Inicializar el controlador
        dashboardController = new AdminDashboardController(SistemaGestionEventos.getInstance());

        // 2. Cargar datos en las tablas a través del controlador
        tablaUsuarios.getItems().clear();
        tablaUsuarios.getItems().addAll(dashboardController.obtenerUsuarios());

        tablaEventos.getItems().clear();
        tablaEventos.getItems().addAll(dashboardController.obtenerEventos());

        // 3. Cargar gráficos
        cargarGraficos();
    }

    private void cargarGraficos() {
        // Ejemplo de serie de datos para el BarChart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Ventas");
        series.getData().add(new XYChart.Data<>("Concierto Juanes", 4500000));

        chartVentas.getData().clear();
        chartVentas.getData().add(series);

        // Datos para el PieChart
        chartOcupacion.getData().clear();
        chartOcupacion.getData().addAll(
                new PieChart.Data("VIP", 65),
                new PieChart.Data("Preferencial", 25),
                new PieChart.Data("General", 10)
        );
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}