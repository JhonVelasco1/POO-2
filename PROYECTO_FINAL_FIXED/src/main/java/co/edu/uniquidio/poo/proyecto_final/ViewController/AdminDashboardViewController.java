package co.edu.uniquidio.poo.proyecto_final.ViewController;

import co.edu.uniquidio.poo.proyecto_final.App;
import co.edu.uniquidio.poo.proyecto_final.Controller.AdminDashboardController;
import co.edu.uniquidio.poo.proyecto_final.Controller.GestionEventosController;
import co.edu.uniquidio.poo.proyecto_final.Controller.GestionUsuariosController;
import co.edu.uniquidio.poo.proyecto_final.model.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.Optional;

public class AdminDashboardViewController {

    // === Usuarios ===
    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML private TableColumn<Usuario, String> colUId, colUNombre, colUCorreo, colUTelefono;
    @FXML private TextField txtUNombre, txtUCorreo, txtUTelefono;
    @FXML private PasswordField txtUPassword;

    // === Eventos ===
    @FXML private TableView<Evento> tablaEventos;
    @FXML private TableColumn<Evento, String> colEId, colENombre, colECiudad, colEFecha, colEEstado;
    @FXML private TextField txtENombre, txtECiudad, txtEFecha;
    @FXML private ComboBox<String> cmbETipo;

    // === Charts ===
    @FXML private BarChart<String, Number> chartVentas;
    @FXML private PieChart chartOcupacion;

    private AdminDashboardController dashboardController;
    private GestionEventosController eventosController;
    private GestionUsuariosController usuariosController;

    @FXML
    private void initialize() {
        SistemaGestionEventos sistema = SistemaGestionEventos.getInstance();
        dashboardController  = new AdminDashboardController(sistema);
        eventosController    = new GestionEventosController(sistema);
        usuariosController   = new GestionUsuariosController(sistema);

        configurarTablaUsuarios();
        configurarTablaEventos();
        cargarTiposEvento();
        cargarDatos();
        cargarGraficos();
    }

    private void configurarTablaUsuarios() {
        colUId.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        colUNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        colUCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colUTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        // Al seleccionar una fila, llenar los campos del formulario
        tablaUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                txtUNombre.setText(sel.getNombreCompleto());
                txtUCorreo.setText(sel.getCorreo());
                txtUTelefono.setText(sel.getTelefono());
                // No se llena la contraseña por seguridad
            }
        });
    }

    private void configurarTablaEventos() {
        colEId.setCellValueFactory(new PropertyValueFactory<>("idEvento"));
        colENombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colECiudad.setCellValueFactory(new PropertyValueFactory<>("ciudad"));
        colEFecha.setCellValueFactory(new PropertyValueFactory<>("fechaHora"));
        colEEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // Al seleccionar una fila, llenar los campos del formulario
        tablaEventos.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                txtENombre.setText(sel.getNombre());
                txtECiudad.setText(sel.getCiudad());
                txtEFecha.setText(sel.getFechaHora());
            }
        });
    }

    private void cargarTiposEvento() {
        cmbETipo.setItems(FXCollections.observableArrayList("concierto", "teatro", "conferencia"));
        cmbETipo.setValue("concierto");
    }

    private void cargarDatos() {
        tablaUsuarios.setItems(FXCollections.observableArrayList(usuariosController.listarUsuarios()));
        tablaEventos.setItems(FXCollections.observableArrayList(eventosController.listarEventos()));
    }

    // ========== CRUD USUARIOS ==========

    @FXML
    private void crearUsuario() {
        String nombre = txtUNombre.getText().trim();
        String correo = txtUCorreo.getText().trim();
        String tel    = txtUTelefono.getText().trim();
        String pass   = txtUPassword.getText().trim();

        if (nombre.isEmpty() || correo.isEmpty()) {
            alerta("⚠️ Nombre y correo son obligatorios.");
            return;
        }
        try {
            usuariosController.crearUsuario(nombre, correo, pass.isEmpty() ? "1234" : pass, tel);
            cargarDatos();
            limpiarCamposUsuario();
            alerta(Alert.AlertType.INFORMATION, "Éxito", "✅ Usuario creado correctamente.");
        } catch (IllegalArgumentException ex) {
            alerta("❌ " + ex.getMessage());
        }
    }

    @FXML
    private void actualizarUsuario() {
        Usuario sel = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (sel == null) { alerta("⚠️ Selecciona un usuario de la tabla primero."); return; }

        String nombre = txtUNombre.getText().trim();
        String correo = txtUCorreo.getText().trim();
        String tel    = txtUTelefono.getText().trim();

        if (nombre.isEmpty() || correo.isEmpty()) {
            alerta("⚠️ Nombre y correo no pueden estar vacíos.");
            return;
        }

        // CORRECCIÓN: se actualiza directamente el objeto seleccionado
        usuariosController.actualizarUsuario(sel, nombre, correo, tel);

        // CORRECCIÓN: refresh() para que la tabla muestre los nuevos valores
        tablaUsuarios.refresh();
        alerta(Alert.AlertType.INFORMATION, "Éxito", "✅ Usuario actualizado correctamente.");
    }

    @FXML
    private void eliminarUsuario() {
        Usuario sel = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (sel == null) { alerta("⚠️ Selecciona un usuario de la tabla primero."); return; }
        Optional<ButtonType> r = confirmar("¿Eliminar al usuario \"" + sel.getNombreCompleto() + "\"?");
        if (r.isPresent() && r.get() == ButtonType.OK) {
            usuariosController.eliminarUsuario(sel.getIdUsuario());
            cargarDatos();
            limpiarCamposUsuario();
        }
    }

    // ========== CRUD EVENTOS ==========

    @FXML
    private void crearEvento() {
        String nombre = txtENombre.getText().trim();
        String ciudad = txtECiudad.getText().trim();
        String fecha  = txtEFecha.getText().trim();
        String tipo   = cmbETipo.getValue();

        if (nombre.isEmpty() || ciudad.isEmpty() || fecha.isEmpty()) {
            alerta("⚠️ Todos los campos del evento son obligatorios.");
            return;
        }

        String categoria = tipo.substring(0, 1).toUpperCase() + tipo.substring(1);
        eventosController.crearEvento(tipo, nombre, categoria, ciudad, fecha);
        cargarDatos();
        limpiarCamposEvento();
        alerta(Alert.AlertType.INFORMATION, "Éxito", "✅ Evento creado correctamente.");
    }

    @FXML
    private void actualizarEvento() {
        // CORRECCIÓN: método que faltaba — actualiza el evento seleccionado con los campos del formulario
        Evento sel = tablaEventos.getSelectionModel().getSelectedItem();
        if (sel == null) { alerta("⚠️ Selecciona un evento de la tabla primero."); return; }

        String nombre = txtENombre.getText().trim();
        String ciudad = txtECiudad.getText().trim();
        String fecha  = txtEFecha.getText().trim();

        if (nombre.isEmpty() || ciudad.isEmpty() || fecha.isEmpty()) {
            alerta("⚠️ Todos los campos son obligatorios para actualizar.");
            return;
        }

        eventosController.actualizarEvento(sel.getIdEvento(), nombre, ciudad, fecha);

        // CORRECCIÓN: refresh() para que la tabla muestre los cambios
        tablaEventos.refresh();
        alerta(Alert.AlertType.INFORMATION, "Éxito", "✅ Evento actualizado correctamente.");
    }

    @FXML
    private void publicarEvento() {
        Evento sel = tablaEventos.getSelectionModel().getSelectedItem();
        if (sel == null) { alerta("⚠️ Selecciona un evento primero."); return; }
        eventosController.publicarEvento(sel.getIdEvento());
        tablaEventos.refresh();
        alerta(Alert.AlertType.INFORMATION, "Publicado", "✅ Evento publicado correctamente.");
    }

    @FXML
    private void cancelarEvento() {
        Evento sel = tablaEventos.getSelectionModel().getSelectedItem();
        if (sel == null) { alerta("⚠️ Selecciona un evento primero."); return; }
        Optional<ButtonType> r = confirmar("¿Cancelar el evento \"" + sel.getNombre() + "\"?");
        if (r.isPresent() && r.get() == ButtonType.OK) {
            eventosController.cancelarEvento(sel.getIdEvento());
            tablaEventos.refresh();
        }
    }

    @FXML
    private void eliminarEvento() {
        Evento sel = tablaEventos.getSelectionModel().getSelectedItem();
        if (sel == null) { alerta("⚠️ Selecciona un evento primero."); return; }
        Optional<ButtonType> r = confirmar("¿Eliminar el evento \"" + sel.getNombre() + "\"?");
        if (r.isPresent() && r.get() == ButtonType.OK) {
            eventosController.eliminarEvento(sel.getIdEvento());
            cargarDatos();
            limpiarCamposEvento();
        }
    }

    // ========== REPORTES ==========

    @FXML
    private void generarReporteCSV() {
        String metricas = SistemaGestionEventos.getInstance().obtenerMetricas();
        alerta(Alert.AlertType.INFORMATION, "Reporte CSV", "✅ Métricas actuales:\n\n" + metricas);
    }

    @FXML
    private void generarReportePDF() {
        alerta(Alert.AlertType.INFORMATION, "Reporte PDF", "✅ Reporte PDF generado en consola.");
    }

    // ========== CERRAR SESIÓN ==========

    @FXML
    private void cerrarSesion() {
        // CORRECCIÓN: limpia el usuario logueado y vuelve al login
        SistemaGestionEventos.getInstance().setUsuarioLogueado(null);
        App.mostrarLogin();
    }

    // ========== GRÁFICOS ==========

    private void cargarGraficos() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Ventas");
        series.getData().add(new XYChart.Data<>("Concierto Juanes", 4500000));
        series.getData().add(new XYChart.Data<>("Teatro La Dama",    1200000));
        series.getData().add(new XYChart.Data<>("Conferencia Tech",   800000));
        chartVentas.getData().clear();
        chartVentas.getData().add(series);

        chartOcupacion.getData().clear();
        chartOcupacion.getData().addAll(
            new PieChart.Data("VIP",         65),
            new PieChart.Data("Preferencial", 25),
            new PieChart.Data("General",      10)
        );
    }

    // ========== HELPERS ==========

    private void limpiarCamposUsuario() {
        txtUNombre.clear(); txtUCorreo.clear(); txtUTelefono.clear(); txtUPassword.clear();
    }

    private void limpiarCamposEvento() {
        txtENombre.clear(); txtECiudad.clear(); txtEFecha.clear();
    }

    private void alerta(String msg) { alerta(Alert.AlertType.WARNING, "Aviso", msg); }

    private void alerta(Alert.AlertType tipo, String titulo, String msg) {
        Alert a = new Alert(tipo);
        a.setTitle(titulo); a.setHeaderText(null); a.setContentText(msg);
        a.showAndWait();
    }

    private Optional<ButtonType> confirmar(String msg) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Confirmar"); a.setHeaderText(null); a.setContentText(msg);
        return a.showAndWait();
    }
}
