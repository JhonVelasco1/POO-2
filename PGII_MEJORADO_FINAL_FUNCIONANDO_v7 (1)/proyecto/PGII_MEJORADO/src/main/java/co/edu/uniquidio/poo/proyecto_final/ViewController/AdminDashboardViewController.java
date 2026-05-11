package co.edu.uniquidio.poo.proyecto_final.ViewController;

import co.edu.uniquidio.poo.proyecto_final.App;
import co.edu.uniquidio.poo.proyecto_final.Controller.*;
import co.edu.uniquidio.poo.proyecto_final.model.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;
import java.util.Optional;

/**
 * ViewController para el panel de administrador (RF-012 a RF-019).
 *
 * <p>Funcionalidades:</p>
 * <ul>
 *   <li>RF-012: CRUD de usuarios</li>
 *   <li>RF-013: CRUD y estados de eventos</li>
 *   <li>RF-014: Gestión de recintos y zonas</li>
 *   <li>RF-016: Gestión de compras (cancelar, confirmar)</li>
 *   <li>RF-017: Registro de incidencias</li>
 *   <li>RF-018: Panel de métricas</li>
 *   <li>RF-019: Gráficos JavaFX (BarChart + PieChart)</li>
 * </ul>
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public class AdminDashboardViewController {

    // === Tab Usuarios ===
    @FXML private TableView<Usuario>               tablaUsuarios;
    @FXML private TableColumn<Usuario, String>     colUId, colUNombre, colUCorreo, colUTelefono;
    @FXML private TextField                        txtUNombre, txtUCorreo, txtUTelefono;
    @FXML private PasswordField                    txtUPassword;

    // === Tab Eventos ===
    @FXML private TableView<Evento>                tablaEventos;
    @FXML private TableColumn<Evento, String>      colEId, colENombre, colECiudad, colEFecha, colEEstado;
    @FXML private TextField                        txtENombre, txtECiudad, txtEFecha;
    @FXML private ComboBox<String>                 cmbETipo;

    // === Tab Compras ===
    @FXML private TableView<Compra>                tablaCompras;
    @FXML private TableColumn<Compra, String>      colCompraId, colCompraUsuario,
                                                   colCompraEvento, colCompraTotal, colCompraEstado;

    // === Tab Incidencias ===
    @FXML private TableView<Incidencia>            tablaIncidencias;
    @FXML private TableColumn<Incidencia, String>  colIncId, colIncTipo, colIncDesc, colIncFecha;
    @FXML private TextField                        txtIncTipo, txtIncDesc;

    // === Tab Métricas (RF-018, RF-019) ===
    @FXML private BarChart<String, Number>         chartVentas;
    @FXML private PieChart                         chartOcupacion;
    @FXML private Label                            lblMetricas;

    private AdminDashboardController          dashCtrl;
    private GestionEventosController          eventosCtrl;
    private GestionUsuariosController         usuariosCtrl;
    private GestionComprasController          comprasCtrl;
    private ReporteController                 reporteCtrl;
    private SistemaGestionEventosSingleton    sistema;

    /**
     * Inicializa controladores, configura tablas y carga datos.
     * Usa {@link Platform#runLater} para garantizar que la inyección @FXML
     * esté completamente finalizada antes de poblar los nodos (RF-012 a RF-019).
     */
    @FXML
    public void initialize() {
        sistema      = SistemaGestionEventosSingleton.getInstance();
        dashCtrl     = new AdminDashboardController(sistema);
        eventosCtrl  = new GestionEventosController(sistema);
        usuariosCtrl = new GestionUsuariosController(sistema);
        comprasCtrl  = new GestionComprasController(sistema);
        reporteCtrl  = new ReporteController(sistema);

        configurarTablaUsuarios();
        configurarTablaEventos();
        configurarTablaCompras();
        configurarTablaIncidencias();
        cargarTiposEvento();

        // Platform.runLater garantiza que el scene graph esté completamente
        // construido antes de poblar datos y gráficos (evita NPE de timing FXML).
        Platform.runLater(() -> {
            cargarDatos();
            cargarGraficos();
        });
    }

    // ==================== CONFIGURACIÓN DE TABLAS ====================

    /**
     * Configura columnas y listener de selección para la tabla de usuarios (RF-012).
     */
    private void configurarTablaUsuarios() {
        colUId.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        colUNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        colUCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colUTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        tablaUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, o, sel) -> {
            if (sel != null) {
                txtUNombre.setText(sel.getNombreCompleto());
                txtUCorreo.setText(sel.getCorreo());
                txtUTelefono.setText(sel.getTelefono());
            }
        });
    }

    /**
     * Configura columnas y listener de selección para la tabla de eventos (RF-013).
     */
    private void configurarTablaEventos() {
        colEId.setCellValueFactory(new PropertyValueFactory<>("idEvento"));
        colENombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colECiudad.setCellValueFactory(new PropertyValueFactory<>("ciudad"));
        colEFecha.setCellValueFactory(new PropertyValueFactory<>("fechaHora"));
        colEEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        tablaEventos.getSelectionModel().selectedItemProperty().addListener((obs, o, sel) -> {
            if (sel != null) {
                txtENombre.setText(sel.getNombre());
                txtECiudad.setText(sel.getCiudad());
                txtEFecha.setText(sel.getFechaHora());
            }
        });
    }

    /**
     * Configura las columnas de la tabla de compras con lambdas para campos calculados (RF-016).
     * Se usan {@link SimpleStringProperty} para usuario, evento y total (campos no mapeables
     * directamente con {@link PropertyValueFactory}).
     */
    private void configurarTablaCompras() {
        colCompraId.setCellValueFactory(new PropertyValueFactory<>("idCompra"));
        colCompraUsuario.setCellValueFactory(cd -> new SimpleStringProperty(
            cd.getValue().getUsuario() != null
                ? cd.getValue().getUsuario().getNombreCompleto() : "—"));
        colCompraEvento.setCellValueFactory(cd -> new SimpleStringProperty(
            cd.getValue().getEvento() != null
                ? cd.getValue().getEvento().getNombre() : "—"));
        colCompraTotal.setCellValueFactory(cd -> new SimpleStringProperty(
            "$" + String.format("%,.0f", cd.getValue().getTotal())));
        colCompraEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
    }

    /**
     * Configura las columnas de la tabla de incidencias (RF-017).
     */
    private void configurarTablaIncidencias() {
        colIncId.setCellValueFactory(new PropertyValueFactory<>("idIncidencia"));
        colIncTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colIncDesc.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colIncFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
    }

    /**
     * Carga los tipos de evento disponibles en el ComboBox (RF-013).
     */
    private void cargarTiposEvento() {
        if (cmbETipo != null)
            cmbETipo.setItems(FXCollections.observableArrayList("concierto", "teatro", "conferencia"));
    }

    /**
     * Carga todas las entidades en las tablas y actualiza métricas (RF-012 a RF-018).
     * Siempre debe invocarse dentro de {@link Platform#runLater}.
     */
    private void cargarDatos() {
        // RF-012: Usuarios
        List<Usuario> usuarios = usuariosCtrl.listarUsuarios();
        tablaUsuarios.setItems(FXCollections.observableArrayList(usuarios));

        // RF-013: Eventos (admin ve TODOS, no solo publicados)
        List<Evento> eventos = eventosCtrl.listarEventos();
        tablaEventos.setItems(FXCollections.observableArrayList(eventos));

        // RF-016: Compras (todas las del sistema)
        List<Compra> compras = comprasCtrl.listarTodasLasCompras();
        tablaCompras.setItems(FXCollections.observableArrayList(compras));

        // RF-017: Incidencias
        List<Incidencia> incidencias = dashCtrl.obtenerIncidencias();
        tablaIncidencias.setItems(FXCollections.observableArrayList(incidencias));

        // RF-018: Métricas
        if (lblMetricas != null)
            lblMetricas.setText(dashCtrl.obtenerMetricas());
    }

    // ==================== RF-019: GRÁFICOS JavaFX ====================

    /**
     * Carga el BarChart de ingresos por evento y el PieChart de ocupación por zona — RF-019.
     * Siempre debe invocarse dentro de {@link Platform#runLater}.
     */
    private void cargarGraficos() {
        // --- BarChart: ingresos por evento (RF-019) ---
        if (chartVentas != null) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Ingresos ($)");
            for (Evento e : eventosCtrl.listarEventos()) {
                double ingresos = dashCtrl.calcularIngresosPorEvento(e.getIdEvento());
                String label = e.getNombre().length() > 18
                    ? e.getNombre().substring(0, 15) + "…" : e.getNombre();
                series.getData().add(new XYChart.Data<>(label, ingresos));
            }
            chartVentas.getData().clear();
            chartVentas.getData().add(series);
        }

        // --- PieChart: ocupación por zona (RF-019) ---
        if (chartOcupacion != null) {
            chartOcupacion.getData().clear();
            for (Recinto r : sistema.listarRecintos()) {
                for (Zona z : r.getZonas()) {
                    // Mostrar todas las zonas con su capacidad total para visualización
                    int valor = z.getOcupacion() > 0 ? z.getOcupacion() : z.getCapacidad();
                    if (valor > 0) {
                        String label = z.getNombre() + " (" + r.getNombre() + ")";
                        chartOcupacion.getData().add(new PieChart.Data(label, valor));
                    }
                }
            }
            if (chartOcupacion.getData().isEmpty()) {
                chartOcupacion.getData().add(new PieChart.Data("Sin datos de zonas", 1));
            }
        }
    }

    // ==================== CRUD USUARIOS (RF-012) ====================

    /** Crea un nuevo usuario con los datos del formulario (RF-012). */
    @FXML
    private void crearUsuario() {
        String nombre = txtUNombre.getText().trim();
        String correo = txtUCorreo.getText().trim();
        String tel    = txtUTelefono.getText().trim();
        String pass   = txtUPassword != null ? txtUPassword.getText().trim() : "1234";
        if (nombre.isEmpty() || correo.isEmpty()) {
            alerta("⚠️ Nombre y correo son obligatorios."); return;
        }
        try {
            usuariosCtrl.crearUsuario(nombre, correo, pass.isEmpty() ? "1234" : pass, tel);
            cargarDatos();
            limpiarCamposUsuario();
            alerta(Alert.AlertType.INFORMATION, "Éxito", "✅ Usuario creado.");
        } catch (IllegalArgumentException ex) { alerta("❌ " + ex.getMessage()); }
    }

    /** Actualiza el usuario seleccionado en la tabla (RF-012). */
    @FXML
    private void actualizarUsuario() {
        Usuario sel = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (sel == null) { alerta("⚠️ Selecciona un usuario."); return; }
        try {
            usuariosCtrl.actualizarUsuario(sel,
                txtUNombre.getText(), txtUCorreo.getText(), txtUTelefono.getText());
            tablaUsuarios.refresh();
            alerta(Alert.AlertType.INFORMATION, "Éxito", "✅ Usuario actualizado.");
        } catch (IllegalArgumentException ex) { alerta("❌ " + ex.getMessage()); }
    }

    /** Elimina el usuario seleccionado de la tabla (RF-012). */
    @FXML
    private void eliminarUsuario() {
        Usuario sel = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (sel == null) { alerta("⚠️ Selecciona un usuario."); return; }
        if (confirmar("¿Eliminar al usuario \"" + sel.getNombreCompleto() + "\"?")) {
            usuariosCtrl.eliminarUsuario(sel.getIdUsuario());
            cargarDatos();
            limpiarCamposUsuario();
        }
    }

    // ==================== CRUD EVENTOS (RF-013) ====================

    /** Crea un nuevo evento con los datos del formulario (RF-013). */
    @FXML
    private void crearEvento() {
        String nombre = txtENombre.getText().trim();
        String ciudad = txtECiudad.getText().trim();
        String fecha  = txtEFecha.getText().trim();
        String tipo   = cmbETipo != null && cmbETipo.getValue() != null
                        ? cmbETipo.getValue() : "concierto";
        if (nombre.isEmpty() || ciudad.isEmpty() || fecha.isEmpty()) {
            alerta("⚠️ Todos los campos del evento son obligatorios."); return;
        }
        String cat = tipo.substring(0, 1).toUpperCase() + tipo.substring(1);
        eventosCtrl.crearEvento(tipo, nombre, cat, ciudad, fecha);
        cargarDatos();
        limpiarCamposEvento();
        alerta(Alert.AlertType.INFORMATION, "Éxito", "✅ Evento creado.");
    }

    /** Actualiza el evento seleccionado (RF-013). */
    @FXML
    private void actualizarEvento() {
        Evento sel = tablaEventos.getSelectionModel().getSelectedItem();
        if (sel == null) { alerta("⚠️ Selecciona un evento."); return; }
        eventosCtrl.actualizarEvento(sel.getIdEvento(),
            txtENombre.getText(), txtECiudad.getText(), txtEFecha.getText());
        tablaEventos.refresh();
        cargarGraficos();
        alerta(Alert.AlertType.INFORMATION, "Éxito", "✅ Evento actualizado.");
    }

    /** Publica el evento seleccionado (RF-013). */
    @FXML
    private void publicarEvento() {
        Evento sel = tablaEventos.getSelectionModel().getSelectedItem();
        if (sel == null) { alerta("⚠️ Selecciona un evento."); return; }
        eventosCtrl.publicarEvento(sel.getIdEvento());
        tablaEventos.refresh();
        alerta(Alert.AlertType.INFORMATION, "Publicado", "✅ Evento publicado.");
    }

    /** Pausa el evento seleccionado (RF-013). */
    @FXML
    private void pausarEvento() {
        Evento sel = tablaEventos.getSelectionModel().getSelectedItem();
        if (sel == null) { alerta("⚠️ Selecciona un evento."); return; }
        eventosCtrl.pausarEvento(sel.getIdEvento());
        tablaEventos.refresh();
        alerta(Alert.AlertType.INFORMATION, "Pausado", "⏸️ Evento pausado.");
    }

    /** Cancela el evento seleccionado (RF-013). */
    @FXML
    private void cancelarEvento() {
        Evento sel = tablaEventos.getSelectionModel().getSelectedItem();
        if (sel == null) { alerta("⚠️ Selecciona un evento."); return; }
        if (confirmar("¿Cancelar el evento \"" + sel.getNombre() + "\"?")) {
            eventosCtrl.cancelarEvento(sel.getIdEvento());
            tablaEventos.refresh();
            cargarDatos();
        }
    }

    /** Elimina el evento seleccionado (RF-013). */
    @FXML
    private void eliminarEvento() {
        Evento sel = tablaEventos.getSelectionModel().getSelectedItem();
        if (sel == null) { alerta("⚠️ Selecciona un evento."); return; }
        if (confirmar("¿Eliminar el evento \"" + sel.getNombre() + "\"?")) {
            eventosCtrl.eliminarEvento(sel.getIdEvento());
            cargarDatos();
            limpiarCamposEvento();
        }
    }

    // ==================== GESTIÓN COMPRAS (RF-016) ====================

    /** Confirma la compra seleccionada (RF-016). */
    @FXML
    private void confirmarCompra() {
        Compra sel = tablaCompras.getSelectionModel().getSelectedItem();
        if (sel == null) { alerta("⚠️ Selecciona una compra."); return; }
        comprasCtrl.confirmarCompra(sel);
        // Recargar tabla para reflejar el nuevo estado
        tablaCompras.setItems(
            FXCollections.observableArrayList(comprasCtrl.listarTodasLasCompras()));
        alerta(Alert.AlertType.INFORMATION, "Confirmada", "✅ Compra confirmada.");
    }

    /** Cancela la compra seleccionada como administrador (RF-016). */
    @FXML
    private void cancelarCompraAdmin() {
        Compra sel = tablaCompras.getSelectionModel().getSelectedItem();
        if (sel == null) { alerta("⚠️ Selecciona una compra."); return; }
        if (confirmar("¿Cancelar la compra " + sel.getIdCompra() + "?")) {
            dashCtrl.cancelarCompraAdmin(sel);
            tablaCompras.setItems(
                FXCollections.observableArrayList(comprasCtrl.listarTodasLasCompras()));
            cargarDatos();
        }
    }

    // ==================== INCIDENCIAS (RF-017) ====================

    /** Registra una incidencia manual desde el formulario (RF-017). */
    @FXML
    private void registrarIncidencia() {
        String tipo = txtIncTipo != null ? txtIncTipo.getText().trim() : "";
        String desc = txtIncDesc != null ? txtIncDesc.getText().trim() : "";
        if (tipo.isEmpty() || desc.isEmpty()) {
            alerta("⚠️ Tipo y descripción son obligatorios."); return;
        }
        dashCtrl.registrarIncidencia(tipo, desc);
        if (txtIncTipo != null) txtIncTipo.clear();
        if (txtIncDesc != null) txtIncDesc.clear();
        // Recargar tabla incidencias
        tablaIncidencias.setItems(
            FXCollections.observableArrayList(dashCtrl.obtenerIncidencias()));
        alerta(Alert.AlertType.INFORMATION, "Incidencia", "⚠️ Incidencia registrada.");
    }

    // ==================== REPORTES Y GRÁFICOS (RF-018, RF-019, RF-046) ====================

    /**
     * Muestra reporte de ventas y permite exportar a CSV (RF-046).
     */
    @FXML
    private void generarReporteVentas() {
        String contenido = reporteCtrl.obtenerContenidoReporte("VENTAS", "Todas las fechas");
        mostrarReporteEnDialog("📊 Reporte de Ventas", contenido);
    }

    /** Muestra reporte de ocupación por zona (RF-018). */
    @FXML
    private void generarReporteOcupacion() {
        String contenido = reporteCtrl.obtenerContenidoReporte("OCUPACION", "Actual");
        mostrarReporteEnDialog("🏟️ Reporte de Ocupación", contenido);
    }

    /** Muestra reporte de tasa de cancelación (RF-018). */
    @FXML
    private void generarReporteCancelacion() {
        String contenido = reporteCtrl.obtenerContenidoReporte("CANCELACION", "Todas");
        mostrarReporteEnDialog("❌ Tasa de Cancelación", contenido);
    }

    /** Muestra el top 5 de eventos por ingresos (RF-018). */
    @FXML
    private void generarTopEventos() {
        String contenido = reporteCtrl.obtenerContenidoReporte("TOP_EVENTOS", "Histórico");
        mostrarReporteEnDialog("🏆 Top Eventos por Ingresos", contenido);
    }

    /** Recarga los gráficos y métricas manualmente (RF-019). */
    @FXML
    private void actualizarGraficos() {
        cargarGraficos();
        if (lblMetricas != null) lblMetricas.setText(dashCtrl.obtenerMetricas());
    }

    // ==================== NAVEGACIÓN ====================

    /** Cierra la sesión y navega al login. */
    @FXML
    private void cerrarSesion() {
        sistema.setUsuarioLogueado(null);
        App.mostrarLogin();
    }

    // ==================== HELPERS ====================

    /**
     * Muestra el contenido de un reporte en un diálogo no editable con opción de exportar CSV.
     *
     * @param titulo   título del diálogo
     * @param contenido texto del reporte
     */
    private void mostrarReporteEnDialog(String titulo, String contenido) {
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle(titulo);
        dlg.setHeaderText(null);
        TextArea ta = new TextArea(contenido);
        ta.setEditable(false);
        ta.setPrefSize(640, 420);
        ta.setStyle("-fx-font-family: monospace; -fx-font-size: 12px;");

        ButtonType exportarBtn = new ButtonType("📥 Exportar CSV", ButtonBar.ButtonData.LEFT);
        dlg.getDialogPane().setContent(ta);
        dlg.getDialogPane().getButtonTypes().addAll(exportarBtn, ButtonType.CLOSE);

        dlg.showAndWait().ifPresent(bt -> {
            if (bt == exportarBtn) {
                javafx.stage.FileChooser fc = new javafx.stage.FileChooser();
                fc.setTitle("Guardar CSV");
                fc.getExtensionFilters().add(
                    new javafx.stage.FileChooser.ExtensionFilter("CSV", "*.csv"));
                fc.setInitialFileName("reporte_" +
                    java.time.LocalDateTime.now()
                        .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"))
                    + ".csv");
                fc.setInitialDirectory(new java.io.File(System.getProperty("user.home")));
                java.io.File dest = fc.showSaveDialog(tablaEventos.getScene().getWindow());
                if (dest != null) {
                    boolean ok = reporteCtrl.exportarCSVAArchivo(dest.getAbsolutePath());
                    alerta(ok ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR,
                        ok ? "Exportación exitosa" : "Error",
                        ok ? "✅ CSV guardado en:\n" + dest.getAbsolutePath()
                           : "❌ Error al guardar el CSV.");
                }
            }
        });
    }

    private void limpiarCamposUsuario() {
        if (txtUNombre   != null) txtUNombre.clear();
        if (txtUCorreo   != null) txtUCorreo.clear();
        if (txtUTelefono != null) txtUTelefono.clear();
        if (txtUPassword != null) txtUPassword.clear();
    }

    private void limpiarCamposEvento() {
        if (txtENombre != null) txtENombre.clear();
        if (txtECiudad != null) txtECiudad.clear();
        if (txtEFecha  != null) txtEFecha.clear();
    }

    private void alerta(String msg) {
        alerta(Alert.AlertType.WARNING, "Aviso", msg);
    }

    private void alerta(Alert.AlertType tipo, String titulo, String msg) {
        Alert a = new Alert(tipo);
        a.setTitle(titulo); a.setHeaderText(null); a.setContentText(msg);
        a.showAndWait();
    }

    private boolean confirmar(String msg) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Confirmar"); a.setHeaderText(null); a.setContentText(msg);
        Optional<ButtonType> r = a.showAndWait();
        return r.isPresent() && r.get() == ButtonType.OK;
    }
}
