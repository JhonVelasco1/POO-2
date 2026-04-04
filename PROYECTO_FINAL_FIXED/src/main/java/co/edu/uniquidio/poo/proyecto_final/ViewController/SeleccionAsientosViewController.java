package co.edu.uniquidio.poo.proyecto_final.ViewController;

import co.edu.uniquidio.poo.proyecto_final.Controller.SeleccionAsientosController;
import co.edu.uniquidio.poo.proyecto_final.model.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;

public class SeleccionAsientosViewController {

    @FXML private GridPane gridAsientos;
    @FXML private ComboBox<String> cmbZona;
    // CORRECCIÓN: ComboBox para seleccionar el evento al que se reservan los asientos
    @FXML private ComboBox<String> cmbEvento;
    @FXML private Label lblInfoZona;
    @FXML private Label lblSeleccionados;
    @FXML private Label lblTotal;
    @FXML private Button btnConfirmar;

    private SeleccionAsientosController controller;
    private SistemaGestionEventos sistema;
    private Zona zonaActual;
    private final List<Asiento> asientosSeleccionados = new ArrayList<>();
    // Lista de eventos disponibles para vincular la compra
    private List<Evento> eventosDisponibles = new ArrayList<>();

    private static final String ESTILO_DISPONIBLE  =
        "-fx-background-color: #4caf50; -fx-text-fill: white; " +
        "-fx-background-radius: 6; -fx-font-size: 10px; " +
        "-fx-min-width: 44; -fx-min-height: 38; -fx-cursor: hand;";

    private static final String ESTILO_SELECCIONADO =
        "-fx-background-color: #1e88e5; -fx-text-fill: white; " +
        "-fx-background-radius: 6; -fx-font-size: 10px; " +
        "-fx-min-width: 44; -fx-min-height: 38; -fx-cursor: hand;";

    private static final String ESTILO_RESERVADO =
        "-fx-background-color: #e53935; -fx-text-fill: white; " +
        "-fx-background-radius: 6; -fx-font-size: 10px; " +
        "-fx-min-width: 44; -fx-min-height: 38; -fx-opacity: 0.7;";

    private static final String ESTILO_BLOQUEADO =
        "-fx-background-color: #9e9e9e; -fx-text-fill: white; " +
        "-fx-background-radius: 6; -fx-font-size: 10px; " +
        "-fx-min-width: 44; -fx-min-height: 38; -fx-opacity: 0.7;";

    @FXML
    public void initialize() {
        sistema    = SistemaGestionEventos.getInstance();
        controller = new SeleccionAsientosController(sistema);
        cargarEventos();   // CORRECCIÓN: cargar eventos primero
        cargarZonas();
        actualizarResumen();
    }

    // CORRECCIÓN: carga el ComboBox de eventos disponibles
    private void cargarEventos() {
        eventosDisponibles = sistema.getEventosDisponibles();
        List<String> nombres = new ArrayList<>();
        for (Evento e : eventosDisponibles) {
            nombres.add(e.getNombre());
        }
        cmbEvento.setItems(FXCollections.observableArrayList(nombres));
        if (!nombres.isEmpty()) cmbEvento.getSelectionModel().selectFirst();
    }

    private void cargarZonas() {
        List<Recinto> recintos = sistema.listarRecintos();
        if (recintos.isEmpty()) {
            lblInfoZona.setText("No hay recintos configurados.");
            return;
        }
        Recinto recinto = recintos.get(0);
        List<String> nombres = new ArrayList<>();
        for (Zona z : recinto.getZonas()) nombres.add(z.getNombre());

        cmbZona.setItems(FXCollections.observableArrayList(nombres));
        cmbZona.setOnAction(e -> {
            asientosSeleccionados.clear();
            actualizarResumen();
            cargarMapaZona();
        });
        cmbZona.getSelectionModel().selectFirst();
        cargarMapaZona();
    }

    private void cargarMapaZona() {
        gridAsientos.getChildren().clear();
        String nombreZona = cmbZona.getValue();
        if (nombreZona == null) return;

        List<Recinto> recintos = sistema.listarRecintos();
        if (recintos.isEmpty()) return;

        zonaActual = recintos.get(0).getZonas().stream()
                .filter(z -> z.getNombre().equals(nombreZona))
                .findFirst().orElse(null);

        if (zonaActual == null) return;

        lblInfoZona.setText(String.format(
            "Zona %s  |  Precio: $%,.0f  |  Disponibles: %d / %d",
            zonaActual.getNombre(), zonaActual.getPrecioBase(),
            zonaActual.getCapacidadDisponible(), zonaActual.getCapacidad()));

        List<Asiento> asientos = zonaActual.getAsientos();
        int cols = 8;
        for (int i = 0; i < asientos.size(); i++) {
            Asiento a = asientos.get(i);
            Button btn = crearBotonAsiento(a);
            gridAsientos.add(btn, i % cols, i / cols);
            GridPane.setMargin(btn, new Insets(3));
        }
    }

    private Button crearBotonAsiento(Asiento a) {
        Button btn = new Button(String.valueOf(a.getNumero()));
        switch (a.getEstado()) {
            case "Disponible" -> {
                btn.setStyle(ESTILO_DISPONIBLE);
                btn.setOnAction(e -> toggleSeleccion(btn, a));
            }
            case "Reservado", "Vendido" -> {
                btn.setStyle(ESTILO_RESERVADO);
                btn.setDisable(true);
            }
            default -> {
                btn.setStyle(ESTILO_BLOQUEADO);
                btn.setDisable(true);
            }
        }
        Tooltip tip = new Tooltip("ID: " + a.getIdAsiento() + "\nEstado: " + a.getEstado());
        Tooltip.install(btn, tip);
        return btn;
    }

    private void toggleSeleccion(Button btn, Asiento a) {
        if (asientosSeleccionados.contains(a)) {
            asientosSeleccionados.remove(a);
            btn.setStyle(ESTILO_DISPONIBLE);
        } else {
            asientosSeleccionados.add(a);
            btn.setStyle(ESTILO_SELECCIONADO);
        }
        actualizarResumen();
    }

    private void actualizarResumen() {
        int n = asientosSeleccionados.size();
        double precio = (zonaActual != null) ? zonaActual.getPrecioBase() : 0;
        lblSeleccionados.setText("Seleccionados: " + n + " asiento(s)");
        lblTotal.setText(String.format("Total estimado: $%,.0f", n * precio));
        btnConfirmar.setDisable(n == 0);
    }

    @FXML
    private void confirmarSeleccion() {
        if (asientosSeleccionados.isEmpty() || zonaActual == null) return;

        Usuario usuario = sistema.getUsuarioLogueado();
        if (usuario == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Sesión requerida",
                "Debes iniciar sesión para comprar entradas.");
            return;
        }

        // CORRECCIÓN: obtener el evento seleccionado en el ComboBox
        int idxEvento = cmbEvento.getSelectionModel().getSelectedIndex();
        Evento eventoSeleccionado = (idxEvento >= 0 && idxEvento < eventosDisponibles.size())
                ? eventosDisponibles.get(idxEvento)
                : null;

        if (eventoSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Sin evento",
                "Selecciona un evento antes de reservar asientos.");
            return;
        }

        int reservados = 0;
        for (Asiento a : new ArrayList<>(asientosSeleccionados)) {
            if (zonaActual.reservarAsiento(a.getFila(), a.getNumero())) {
                reservados++;
            }
        }

        double total = reservados * zonaActual.getPrecioBase();

        // CORRECCIÓN: la compra queda vinculada al evento seleccionado y al usuario logueado
        // → aparecerá en "Mis Compras" correctamente
        sistema.crearCompra(new Compra.CompraBuilder()
            .setUsuario(usuario)
            .setEvento(eventoSeleccionado)
            .setTotal(total));

        asientosSeleccionados.clear();
        cargarMapaZona();
        actualizarResumen();

        mostrarAlerta(Alert.AlertType.INFORMATION, "¡Reserva exitosa!",
            String.format("✅ %d asiento(s) reservados en zona %s para '%s'.\n💰 Total: $%,.0f\n\n" +
                "Ve a '🛒 Mis Compras' para completar el pago.",
                reservados, zonaActual.getNombre(), eventoSeleccionado.getNombre(), total));
    }

    @FXML
    private void refrescarMapa() {
        asientosSeleccionados.clear();
        // CORRECCIÓN: recargar eventos también al refrescar (por si se publicaron nuevos)
        cargarEventos();
        cargarMapaZona();
        actualizarResumen();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String msg) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
