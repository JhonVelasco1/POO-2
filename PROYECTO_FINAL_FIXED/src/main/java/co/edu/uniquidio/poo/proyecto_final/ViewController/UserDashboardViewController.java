package co.edu.uniquidio.poo.proyecto_final.ViewController;

import co.edu.uniquidio.poo.proyecto_final.App;
import co.edu.uniquidio.poo.proyecto_final.Controller.UserDashboardController;
import co.edu.uniquidio.poo.proyecto_final.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class UserDashboardViewController {

    @FXML private TableView<Evento> tablaEventos;
    @FXML private TableColumn<Evento, String> colEvNombre, colEvCiudad, colEvFecha, colEvEstado;
    @FXML private TextField txtFiltroNombre, txtFiltroCiudad;

    @FXML private TableView<Compra> tablaCompras;
    @FXML private TableColumn<Compra, String> colCId, colCEvento, colCEstado;
    @FXML private TableColumn<Compra, Double> colCTotal;

    @FXML private Label lblBienvenida;
    @FXML private Button btnIrAdmin;

    private UserDashboardController dashboardController;

    @FXML
    private void initialize() {
        dashboardController = new UserDashboardController(SistemaGestionEventos.getInstance());

        Usuario logueado = SistemaGestionEventos.getInstance().getUsuarioLogueado();
        if (logueado != null) {
            lblBienvenida.setText("👋 Bienvenido, " + logueado.getNombreCompleto());
            if (logueado.esAdmin()) {
                btnIrAdmin.setVisible(true);
                btnIrAdmin.setManaged(true);
            }
        }

        configurarTablaEventos();
        configurarTablaCompras();
        cargarDatos();
    }

    private void configurarTablaEventos() {
        colEvNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEvCiudad.setCellValueFactory(new PropertyValueFactory<>("ciudad"));
        colEvFecha.setCellValueFactory(new PropertyValueFactory<>("fechaHora"));
        colEvEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
    }

    private void configurarTablaCompras() {
        colCId.setCellValueFactory(new PropertyValueFactory<>("idCompra"));
        colCEvento.setCellValueFactory(cellData -> {
            Compra compra = cellData.getValue();
            String nombreEvento = (compra.getEvento() != null)
                    ? compra.getEvento().getNombre()
                    : "Sin evento";
            return new SimpleStringProperty(nombreEvento);
        });
        colCEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colCTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
    }

    private void cargarDatos() {
        tablaEventos.setItems(FXCollections.observableArrayList(dashboardController.obtenerEventosDisponibles()));
        tablaCompras.setItems(FXCollections.observableArrayList(dashboardController.obtenerHistorialCompras()));
    }

    @FXML
    private void filtrarEventos() {
        String nombre = txtFiltroNombre.getText().trim().toLowerCase();
        String ciudad = txtFiltroCiudad.getText().trim().toLowerCase();
        var filtrados = dashboardController.obtenerEventosDisponibles().stream()
                .filter(e -> e.getNombre().toLowerCase().contains(nombre))
                .filter(e -> e.getCiudad().toLowerCase().contains(ciudad))
                .toList();
        tablaEventos.setItems(FXCollections.observableArrayList(filtrados));
    }

    @FXML
    private void limpiarFiltros() {
        txtFiltroNombre.clear();
        txtFiltroCiudad.clear();
        cargarDatos();
    }

    @FXML
    private void comprarEntrada() {
        Evento sel = tablaEventos.getSelectionModel().getSelectedItem();
        if (sel == null) { alerta(Alert.AlertType.WARNING, "Aviso", "Selecciona un evento primero."); return; }
        Usuario u = SistemaGestionEventos.getInstance().getUsuarioLogueado();
        if (u == null) return;

        double precio = 100000;
        if (sel.getRecinto() != null && !sel.getRecinto().getZonas().isEmpty()) {
            precio = sel.getRecinto().getZonas().get(0).getPrecioBase();
        }

        SistemaGestionEventos.getInstance().crearCompra(
            new Compra.CompraBuilder().setUsuario(u).setEvento(sel).setTotal(precio)
        );
        cargarDatos();
        alerta(Alert.AlertType.INFORMATION, "Compra creada",
               "✅ Compra creada para: " + sel.getNombre() + "\n💰 Total: $" + (long)precio +
               "\n\nVe a '🛒 Mis Compras' para confirmar el pago.");
    }

    @FXML
    private void pagarCompra() {
        Compra sel = tablaCompras.getSelectionModel().getSelectedItem();
        if (sel == null) { alerta(Alert.AlertType.WARNING, "Aviso", "Selecciona una compra de la tabla."); return; }
        if (!"Creada".equals(sel.getEstado())) {
            alerta(Alert.AlertType.WARNING, "Aviso", "Solo puedes pagar compras en estado 'Creada'.\nEsta está: " + sel.getEstado());
            return;
        }
        sel.pagar();
        tablaCompras.refresh();
        alerta(Alert.AlertType.INFORMATION, "Pago exitoso", "✅ Compra pagada correctamente.\n🎟️ Tus entradas están listas.");
    }

    @FXML
    private void cancelarCompra() {
        Compra sel = tablaCompras.getSelectionModel().getSelectedItem();
        if (sel == null) { alerta(Alert.AlertType.WARNING, "Aviso", "Selecciona una compra de la tabla."); return; }
        if ("Cancelada".equals(sel.getEstado())) {
            alerta(Alert.AlertType.WARNING, "Aviso", "Esta compra ya está cancelada."); return;
        }
        sel.cancelar();
        tablaCompras.refresh();
        alerta(Alert.AlertType.INFORMATION, "Cancelada", "❌ Compra cancelada correctamente.");
    }

    @FXML
    private void irAlPanelAdmin() {
        App.mostrarDashboardAdmin();
    }

    // CORRECCIÓN: cerrar sesión limpia el usuario y regresa al login
    @FXML
    private void cerrarSesion() {
        SistemaGestionEventos.getInstance().setUsuarioLogueado(null);
        App.mostrarLogin();
    }

    private void alerta(Alert.AlertType tipo, String titulo, String msg) {
        Alert a = new Alert(tipo);
        a.setTitle(titulo); a.setHeaderText(null); a.setContentText(msg);
        a.showAndWait();
    }
}
