package co.edu.uniquidio.poo.proyecto_final.ViewController;

import co.edu.uniquidio.poo.proyecto_final.App;
import co.edu.uniquidio.poo.proyecto_final.Controller.SeleccionAsientosController;
import co.edu.uniquidio.poo.proyecto_final.model.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewController para la selección visual de asientos (RF-005, RF-033).
 * Muestra un mapa interactivo de asientos usando botones de colores.
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public class SeleccionAsientosViewController {

    @FXML private GridPane      gridAsientos;
    @FXML private ComboBox<String> cmbZona;
    @FXML private ComboBox<String> cmbEvento;
    @FXML private Label         lblInfoZona;
    @FXML private Label         lblSeleccionados;
    @FXML private Label         lblTotal;
    @FXML private Button        btnConfirmar;

    private SeleccionAsientosController controller;
    private SistemaGestionEventosSingleton sistema;
    private Zona zonaActual;
    private final List<Asiento> asientosSeleccionados = new ArrayList<>();
    private List<Evento> eventosDisponibles = new ArrayList<>();

    private static final String ESTILO_DISP = "-fx-background-color:#4caf50;-fx-text-fill:white;-fx-background-radius:5;-fx-min-width:40;-fx-min-height:34;-fx-cursor:hand;-fx-font-size:10;";
    private static final String ESTILO_SEL  = "-fx-background-color:#1e88e5;-fx-text-fill:white;-fx-background-radius:5;-fx-min-width:40;-fx-min-height:34;-fx-cursor:hand;-fx-font-size:10;";
    private static final String ESTILO_RSV  = "-fx-background-color:#e53935;-fx-text-fill:white;-fx-background-radius:5;-fx-min-width:40;-fx-min-height:34;-fx-opacity:0.7;-fx-font-size:10;";
    private static final String ESTILO_BLQ  = "-fx-background-color:#9e9e9e;-fx-text-fill:white;-fx-background-radius:5;-fx-min-width:40;-fx-min-height:34;-fx-opacity:0.7;-fx-font-size:10;";

    @FXML
    public void initialize() {
        sistema    = SistemaGestionEventosSingleton.getInstance();
        controller = new SeleccionAsientosController(sistema);
        cargarEventos();
        cargarZonas();
        actualizarResumen();
    }

    private void cargarEventos() {
        eventosDisponibles = controller.listarEventosDisponibles();
        List<String> nombres = eventosDisponibles.stream().map(Evento::getNombre).toList();
        if (cmbEvento != null) {
            cmbEvento.setItems(FXCollections.observableArrayList(nombres));
            if (!nombres.isEmpty()) cmbEvento.getSelectionModel().selectFirst();
        }
    }

    private void cargarZonas() {
        List<Recinto> recintos = controller.listarRecintos();
        if (recintos.isEmpty()) {
            if (lblInfoZona != null) lblInfoZona.setText("Sin recintos disponibles.");
            return;
        }
        // Recopilar zonas de todos los recintos disponibles
        List<String> nombresZona = recintos.stream()
                .flatMap(r -> r.getZonas().stream())
                .map(Zona::getNombre)
                .distinct()
                .toList();
        cmbZona.setItems(FXCollections.observableArrayList(nombresZona));
        // Registrar listener solo si no lo tiene ya (evita duplicados en refrescarMapa)
        cmbZona.setOnAction(e -> {
            asientosSeleccionados.clear();
            actualizarResumen();
            cargarMapaZona();
        });
        if (cmbZona.getValue() == null || !nombresZona.contains(cmbZona.getValue())) {
            cmbZona.getSelectionModel().selectFirst();
        }
        cargarMapaZona();
    }

    private void cargarMapaZona() {
        if (gridAsientos == null) return;
        gridAsientos.getChildren().clear();
        String nombreZona = cmbZona.getValue();
        if (nombreZona == null) return;
        zonaActual = sistema.listarRecintos().stream()
                .flatMap(r -> r.getZonas().stream())
                .filter(z -> z.getNombre().equals(nombreZona))
                .findFirst().orElse(null);
        if (zonaActual == null) return;

        if (lblInfoZona != null)
            lblInfoZona.setText(String.format("Zona %s | $%,.0f | %d/%d libres",
                zonaActual.getNombre(), zonaActual.getPrecioBase(),
                zonaActual.getCapacidadDisponible(), zonaActual.getCapacidad()));

        List<Asiento> asientos = zonaActual.getAsientos();
        int cols = 12;
        for (int i = 0; i < asientos.size(); i++) {
            Asiento a = asientos.get(i);
            Button btn = crearBotonAsiento(a);
            gridAsientos.add(btn, i % cols, i / cols);
            GridPane.setMargin(btn, new Insets(2));
        }
    }

    private Button crearBotonAsiento(Asiento a) {
        Button btn = new Button(a.getFila() + a.getNumero());
        switch (a.getEstado()) {
            case "Disponible" -> { btn.setStyle(ESTILO_DISP); btn.setOnAction(e -> toggle(btn, a)); }
            case "Reservado", "Vendido" -> { btn.setStyle(ESTILO_RSV); btn.setDisable(true); }
            default -> { btn.setStyle(ESTILO_BLQ); btn.setDisable(true); }
        }
        Tooltip.install(btn, new Tooltip(a.getIdAsiento() + " | " + a.getEstado()));
        return btn;
    }

    private void toggle(Button btn, Asiento a) {
        if (asientosSeleccionados.contains(a)) {
            asientosSeleccionados.remove(a); btn.setStyle(ESTILO_DISP);
        } else {
            asientosSeleccionados.add(a); btn.setStyle(ESTILO_SEL);
        }
        actualizarResumen();
    }

    private void actualizarResumen() {
        int n = asientosSeleccionados.size();
        double precio = zonaActual != null ? zonaActual.getPrecioBase() : 0;
        if (lblSeleccionados != null) lblSeleccionados.setText("Seleccionados: " + n);
        if (lblTotal         != null) lblTotal.setText(String.format("Total: $%,.0f", n * precio));
        if (btnConfirmar     != null) btnConfirmar.setDisable(n == 0);
    }

    @FXML
    private void confirmarSeleccion() {
        if (asientosSeleccionados.isEmpty() || zonaActual == null) return;
        Usuario usuario = sistema.getUsuarioLogueado();
        if (usuario == null) { alerta(Alert.AlertType.WARNING, "Debes iniciar sesión."); return; }
        int idx = cmbEvento != null ? cmbEvento.getSelectionModel().getSelectedIndex() : -1;
        Evento evento = (idx >= 0 && idx < eventosDisponibles.size()) ? eventosDisponibles.get(idx) : null;
        if (evento == null) { alerta(Alert.AlertType.WARNING, "Selecciona un evento."); return; }

        int reservados = 0;
        for (Asiento a : new ArrayList<>(asientosSeleccionados)) {
            if (controller.reservarAsiento(sistema.listarRecintos().get(0).getIdRecinto(),
                    zonaActual.getNombre(), a.getFila(), a.getNumero())) reservados++;
        }
        controller.confirmarCompraAsientos(usuario, evento, zonaActual, reservados);
        asientosSeleccionados.clear();
        cargarMapaZona(); actualizarResumen();
        alerta(Alert.AlertType.INFORMATION,
            String.format("✅ %d asiento(s) reservados en %s para '%s'.\nTotal: $%,.0f",
                reservados, zonaActual.getNombre(), evento.getNombre(),
                reservados * zonaActual.getPrecioBase()));
    }

    /**
     * RF-005: Refresca completamente el mapa de asientos.
     * Recarga los eventos disponibles, las zonas y el mapa visual,
     * reflejando asientos pagados o liberados desde la última carga.
     */
    @FXML
    private void refrescarMapa() {
        // Guardar zona seleccionada para restaurarla si sigue existiendo
        String zonaSeleccionadaAntes = cmbZona != null ? cmbZona.getValue() : null;

        // Limpiar selección local
        asientosSeleccionados.clear();

        // Recargar eventos y zonas desde el Singleton (fuente de verdad)
        cargarEventos();
        cargarZonas();

        // Restaurar la misma zona si todavía existe en el combo
        if (zonaSeleccionadaAntes != null && cmbZona != null
                && cmbZona.getItems().contains(zonaSeleccionadaAntes)) {
            cmbZona.setValue(zonaSeleccionadaAntes);
        }

        // Recargar mapa visual con estado actualizado de asientos
        cargarMapaZona();
        actualizarResumen();

        // Tooltip informativo
        if (lblInfoZona != null && zonaActual != null) {
            lblInfoZona.setText(String.format("🔄 Actualizado | Zona %s | $%,.0f | %d/%d libres",
                zonaActual.getNombre(), zonaActual.getPrecioBase(),
                zonaActual.getCapacidadDisponible(), zonaActual.getCapacidad()));
        }
    }

    @FXML
    private void volver() { App.mostrarDashboardUsuario(); }

    private void alerta(Alert.AlertType tipo, String msg) {
        Alert a = new Alert(tipo); a.setTitle("Asientos"); a.setHeaderText(null); a.setContentText(msg); a.showAndWait();
    }
}
