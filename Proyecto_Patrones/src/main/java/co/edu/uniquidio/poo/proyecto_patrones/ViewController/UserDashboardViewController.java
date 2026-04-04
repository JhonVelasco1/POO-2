package co.edu.uniquidio.poo.proyecto_patrones.ViewController;




import co.edu.uniquidio.poo.proyecto_patrones.Controller.UserDashboardController;
import co.edu.uniquidio.poo.proyecto_patrones.model.Compra;
import co.edu.uniquidio.poo.proyecto_patrones.model.Evento;
import co.edu.uniquidio.poo.proyecto_patrones.model.SistemaGestionEventos;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class UserDashboardViewController {

    @FXML private TableView<Evento> tablaEventos;
    @FXML private TableView<Compra> tablaCompras;

    private UserDashboardController dashboardController;

    @FXML
    private void initialize() {
        // 1. Inicializar el controlador con el modelo
        dashboardController = new UserDashboardController(SistemaGestionEventos.getInstance());

        // 2. Configurar y cargar datos
        configurarTablas();
        cargarDatos();
    }

    private void configurarTablas() {
        // Configuramos la columna solo si no ha sido configurada previamente (evita duplicados)
        if (tablaEventos.getColumns().isEmpty()) {
            TableColumn<Evento, String> col = new TableColumn<>("Evento");
            col.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            tablaEventos.getColumns().add(col);
        }
    }

    private void cargarDatos() {
        // 3. Delegamos la obtención de datos al controller
        tablaEventos.getItems().clear();
        tablaEventos.getItems().addAll(dashboardController.obtenerEventosDisponibles());

        tablaCompras.getItems().clear();
        tablaCompras.getItems().addAll(dashboardController.obtenerHistorialCompras());
    }

    @FXML
    private void abrirSeleccionAsientos() {
        // Aquí podrías llamar a un método en App para cambiar de ventana
        mostrarAlerta(Alert.AlertType.INFORMATION, "Seleccionar Asientos", "Abriendo mapa de asientos...");
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}