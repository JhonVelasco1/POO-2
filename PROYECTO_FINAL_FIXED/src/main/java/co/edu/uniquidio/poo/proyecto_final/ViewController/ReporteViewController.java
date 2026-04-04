package co.edu.uniquidio.poo.proyecto_final.ViewController;


import co.edu.uniquidio.poo.proyecto_final.Controller.ReporteController;
import co.edu.uniquidio.poo.proyecto_final.model.SistemaGestionEventos;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class ReporteViewController {

    @FXML private ComboBox<String> cmbTipoReporte;
    @FXML private TextField txtRangoFechas;

    private ReporteController reporteController;

    @FXML
    private void initialize() {
        // 1. Inicializamos el controlador pasando el modelo (Singleton)
        reporteController = new ReporteController(SistemaGestionEventos.getInstance());

        // 2. Llenamos las opciones del menú desplegable
        cmbTipoReporte.getItems().addAll("Ventas", "Ocupación de Recintos", "Usuarios Registrados");
    }

    @FXML
    private void handleGenerarReporte() {
        // Obtenemos los valores de la interfaz
        String tipoSeleccionado = cmbTipoReporte.getValue();
        String fechas = txtRangoFechas.getText();

        // Validamos que el usuario haya ingresado la información
        if (tipoSeleccionado == null || fechas == null || fechas.trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Datos incompletos", "Por favor selecciona un tipo de reporte y escribe las fechas.");
            return;
        }

        try {
            // 3. Delegamos la acción al controlador
            reporteController.generarReporte(tipoSeleccionado, fechas);
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "El reporte de " + tipoSeleccionado + " se ha generado correctamente.");

        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Ocurrió un problema al generar el reporte.");
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