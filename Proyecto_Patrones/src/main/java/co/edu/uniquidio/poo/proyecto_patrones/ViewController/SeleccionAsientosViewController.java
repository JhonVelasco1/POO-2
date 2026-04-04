package co.edu.uniquidio.poo.proyecto_patrones.ViewController;




import co.edu.uniquidio.poo.proyecto_patrones.Controller.SeleccionAsientosController;
import co.edu.uniquidio.poo.proyecto_patrones.model.SistemaGestionEventos;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class SeleccionAsientosViewController {

    @FXML private GridPane gridAsientos;

    private SeleccionAsientosController controller;

    @FXML
    private void initialize() {
        // 1. Inicializar el controller con el modelo
        controller = new SeleccionAsientosController(SistemaGestionEventos.getInstance());

        // 2. Construir la interfaz
        crearMapa();
    }

    private void crearMapa() {
        for (int i = 1; i <= 20; i++) {
            Button btn = new Button("A" + i);
            btn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

            int numAsiento = i;
            btn.setOnAction(e -> gestionarReserva(btn, numAsiento));

            // Ubicación en el grid (5 columnas)
            gridAsientos.add(btn, (i - 1) % 5, (i - 1) / 5);
        }
    }

    private void gestionarReserva(Button btn, int num) {
        // 3. Delegar la lógica de negocio al controller
        // Usamos parámetros fijos según tu código original ("R001" y zona 0)
        boolean ok = controller.realizarReserva("R001", 0, "A", num);

        if (ok) {
            btn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
            btn.setDisable(true); // Evita que se vuelva a clickear
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Asiento A" + num + " reservado correctamente.");
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo realizar la reserva.");
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}