package co.edu.uniquidio.poo.proyecto_final.ViewController;

import co.edu.uniquidio.poo.proyecto_final.App;
import co.edu.uniquidio.poo.proyecto_final.Controller.*;
import co.edu.uniquidio.poo.proyecto_final.model.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import java.io.File;
import java.util.List;

/**
 * ViewController para el panel del usuario (RF-003 a RF-011).
 *
 * <p>Funcionalidades cubiertas:</p>
 * <ul>
 *   <li>RF-003: Explorar eventos con filtros</li>
 *   <li>RF-004: Ver detalle del evento</li>
 *   <li>RF-005: Navegar a selección de asientos</li>
 *   <li>RF-006: Crear/cancelar compra</li>
 *   <li>RF-007: Pagar compra (Strategy de pago)</li>
 *   <li>RF-008: Ver estado de compra</li>
 *   <li>RF-009: Agregar servicios adicionales (Decorator)</li>
 *   <li>RF-010: Historial de compras</li>
 *   <li>RF-011: Descargar reporte de compras (CSV)</li>
 * </ul>
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public class UserDashboardViewController {

    // === Eventos ===
    @FXML private TableView<Evento>               tablaEventos;
    @FXML private TableColumn<Evento, String>     colEvNombre, colEvCiudad, colEvFecha,
                                                  colEvEstado, colEvCategoria;
    @FXML private TextField                       txtFiltroNombre, txtFiltroCiudad, txtFiltroCategoria;
    @FXML private TextArea                        txtDetalleEvento;

    // === Compras ===
    @FXML private TableView<Compra>               tablaCompras;
    @FXML private TableColumn<Compra, String>     colCId, colCEvento, colCEstado, colCFecha, colCTotal;
    @FXML private ComboBox<String>                cmbServicio;
    @FXML private Label                           lblTotalConServicio;

    // === Info general ===
    @FXML private Label                           lblBienvenida;
    @FXML private Button                          btnIrAdmin;

    private GestionEventosController              eventosCtrl;
    private GestionComprasController              comprasCtrl;
    private ReporteController                     reporteCtrl;
    private SistemaGestionEventosSingleton        sistema;

    @FXML
    public void initialize() {
        sistema     = SistemaGestionEventosSingleton.getInstance();
        eventosCtrl = new GestionEventosController(sistema);
        comprasCtrl = new GestionComprasController(sistema);
        reporteCtrl = new ReporteController(sistema);

        Usuario logueado = sistema.getUsuarioLogueado();
        if (logueado != null) {
            lblBienvenida.setText("👋 Bienvenido/a, " + logueado.getNombreCompleto());
            if (btnIrAdmin != null) {
                btnIrAdmin.setVisible(logueado.esAdmin());
                btnIrAdmin.setManaged(logueado.esAdmin());
            }
        }

        // RF-009: Opciones del Decorator
        if (cmbServicio != null)
            cmbServicio.setItems(FXCollections.observableArrayList(
                "vip", "seguro", "merchandising", "parqueadero"));

        configurarTablaEventos();
        configurarTablaCompras();
        cargarDatos();
    }

    // ==================== CONFIGURACIÓN DE TABLAS ====================

    private void configurarTablaEventos() {
        colEvNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEvCiudad.setCellValueFactory(new PropertyValueFactory<>("ciudad"));
        colEvFecha.setCellValueFactory(new PropertyValueFactory<>("fechaHora"));
        colEvEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colEvCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        // RF-004: Al seleccionar evento, mostrar detalle completo
        tablaEventos.getSelectionModel().selectedItemProperty().addListener((obs, o, sel) -> {
            if (sel != null && txtDetalleEvento != null) {
                String zonas = "Sin recinto asignado";
                if (sel.getRecinto() != null) {
                    zonas = "Recinto: " + sel.getRecinto().getNombre() + "\n"
                          + "Zonas: " + sel.getRecinto().getZonas().stream()
                                .map(z -> z.getNombre()
                                    + " ($" + String.format("%,.0f", z.getPrecioBase()) + ")"
                                    + " [" + (z.getCapacidad() - z.getOcupacion()) + " disponibles]")
                                .reduce((a, b) -> a + ", " + b).orElse("N/A");
                }
                txtDetalleEvento.setText(
                    "📌 " + sel.getNombre() + "\n"
                    + "Categoría:  " + sel.getCategoria() + "\n"
                    + "Ciudad:     " + sel.getCiudad() + "\n"
                    + "Fecha:      " + sel.getFechaHora() + "\n"
                    + "Estado:     " + sel.getEstado() + "\n"
                    + "Descripción: " + sel.getDescripcion() + "\n"
                    + "Política:   " + sel.getPoliticasCancelacion() + "\n"
                    + zonas);
            }
        });
    }

    private void configurarTablaCompras() {
        colCId.setCellValueFactory(new PropertyValueFactory<>("idCompra"));
        colCEvento.setCellValueFactory(cd -> new SimpleStringProperty(
            cd.getValue().getEvento() != null ? cd.getValue().getEvento().getNombre() : "—"));
        colCEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colCFecha.setCellValueFactory(new PropertyValueFactory<>("fechaCreacion"));
        // Lambda para que el Decorator se refleje automáticamente en cada refresh
        colCTotal.setCellValueFactory(cd -> new SimpleStringProperty(
            "$" + String.format("%,.0f", cd.getValue().getTotal())));

        // RF-009: Mostrar total y descripción con servicio al seleccionar compra
        tablaCompras.getSelectionModel().selectedItemProperty().addListener((obs, o, sel) -> {
            if (sel != null) actualizarLabelTotal(sel);
        });
    }

    // ==================== CARGA DE DATOS ====================

    /**
     * Recarga la tabla de eventos (publicados) y el historial de compras del usuario.
     */
    private void cargarDatos() {
        tablaEventos.setItems(
            FXCollections.observableArrayList(eventosCtrl.listarEventosDisponibles()));
        recargarTablaCompras();
    }

    /**
     * Recarga únicamente la tabla de compras del usuario logueado.
     * Mantiene la selección si el id de la compra seleccionada sigue existiendo,
     * y actualiza inmediatamente el label de total con los datos frescos.
     *
     * <p>Se garantiza que tanto {@link TableView#setItems(ObservableList)} como
     * {@link TableView#refresh()} se ejecutan en el hilo JavaFX (EDT) para que
     * los cambios se reflejen al instante en pantalla.</p>
     */
    private void recargarTablaCompras() {
        String idSel = tablaCompras.getSelectionModel().getSelectedItem() != null
            ? tablaCompras.getSelectionModel().getSelectedItem().getIdCompra() : null;

        List<Compra> compras = comprasCtrl.historialUsuarioLogueado();
        ObservableList<Compra> items = FXCollections.observableArrayList(compras);

        // Ejecutar en el hilo de JavaFX para actualización inmediata
        Platform.runLater(() -> {
            tablaCompras.setItems(items);
            // refresh() fuerza la re-evaluación de todas las CellValueFactory,
            // incluyendo las lambdas de colCTotal y colCEstado que leen getTotal()
            tablaCompras.refresh();

            // Restaurar selección y actualizar el label de total inmediatamente
            if (idSel != null) {
                items.stream()
                    .filter(c -> c.getIdCompra().equals(idSel))
                    .findFirst()
                    .ifPresent(c -> {
                        tablaCompras.getSelectionModel().select(c);
                        actualizarLabelTotal(c);
                    });
            }
        });
    }

    /**
     * Actualiza el label {@code lblTotalConServicio} con los datos actuales de la compra.
     * Método auxiliar para garantizar consistencia visual entre tabla y label.
     *
     * @param compra la compra cuyo total y estado se deben mostrar
     */
    private void actualizarLabelTotal(Compra compra) {
        if (lblTotalConServicio == null || compra == null) return;
        lblTotalConServicio.setText(
            "Total: $" + String.format("%,.0f", compra.getTotal())
            + "  |  " + compra.getDescripcion()
            + "  |  Estado: " + compra.getEstado());
    }

    // ==================== ACCIONES EVENTOS ====================

    /** RF-003: Filtrar eventos por nombre, ciudad y/o categoría. */
    @FXML
    private void filtrarEventos() {
        String nombre    = txtFiltroNombre    != null ? txtFiltroNombre.getText().trim()    : "";
        String ciudad    = txtFiltroCiudad    != null ? txtFiltroCiudad.getText().trim()    : "";
        String categoria = txtFiltroCategoria != null ? txtFiltroCategoria.getText().trim() : "";
        tablaEventos.setItems(FXCollections.observableArrayList(
            eventosCtrl.filtrar(
                nombre.isEmpty()    ? null : nombre,
                ciudad.isEmpty()    ? null : ciudad,
                categoria.isEmpty() ? null : categoria)));
    }

    /** Limpia filtros y recarga todos los eventos disponibles. */
    @FXML
    private void limpiarFiltros() {
        if (txtFiltroNombre    != null) txtFiltroNombre.clear();
        if (txtFiltroCiudad    != null) txtFiltroCiudad.clear();
        if (txtFiltroCategoria != null) txtFiltroCategoria.clear();
        cargarDatos();
    }

    /** RF-006: Crear compra para el evento seleccionado. */
    @FXML
    private void comprarEntrada() {
        Evento evSel = tablaEventos.getSelectionModel().getSelectedItem();
        if (evSel == null) {
            alerta(Alert.AlertType.WARNING, "Selecciona un evento de la lista primero."); return;
        }
        Usuario u = sistema.getUsuarioLogueado();
        if (u == null) {
            alerta(Alert.AlertType.WARNING, "No hay usuario logueado."); return;
        }
        // Precio desde la primera zona del recinto, o precio base por defecto
        double precio = 100_000;
        if (evSel.getRecinto() != null && !evSel.getRecinto().getZonas().isEmpty()) {
            precio = evSel.getRecinto().getZonas().get(0).getPrecioBase();
        }
        try {
            comprasCtrl.crearCompra(u, evSel, precio);
            recargarTablaCompras();
            alerta(Alert.AlertType.INFORMATION,
                "✅ Compra creada para: " + evSel.getNombre()
                + "\n💰 Total: $" + String.format("%,.0f", precio)
                + "\n\nSelecciónala en 'Mis Compras' y haz clic en '💳 Pagar Compra'.");
        } catch (Exception ex) {
            alerta(Alert.AlertType.ERROR, "❌ Error al crear compra: " + ex.getMessage());
        }
    }

    /** RF-005: Navegar a la pantalla de selección visual de asientos. */
    @FXML
    private void irASeleccionAsientos() { App.mostrarSeleccionAsientos(); }

    // ==================== ACCIONES COMPRAS ====================

    /**
     * RF-007: Pagar la compra seleccionada usando el patrón Strategy (tarjeta por defecto).
     * Solo se puede pagar si la compra está en estado "Creada".
     * Recarga y refresca la tabla inmediatamente tras el cambio de estado.
     */
    @FXML
    private void pagarCompra() {
        Compra sel = tablaCompras.getSelectionModel().getSelectedItem();
        if (sel == null) {
            alerta(Alert.AlertType.WARNING, "Selecciona una compra de la tabla."); return;
        }
        String estado = sel.getEstado();
        if (!"Creada".equalsIgnoreCase(estado)) {
            alerta(Alert.AlertType.WARNING,
                "Solo puedes pagar compras en estado 'Creada'.\n"
                + "Estado actual: " + estado);
            return;
        }
        // Usar Strategy: tarjeta como método por defecto (RF-047)
        comprasCtrl.pagarCompra(sel, "tarjeta", "VISA-xxxx");

        // Recargar tabla para reflejar el nuevo estado "Pagada" de forma inmediata
        final String idSelFinal = sel.getIdCompra();
        List<Compra> actualizadas = comprasCtrl.historialUsuarioLogueado();
        ObservableList<Compra> items = FXCollections.observableArrayList(actualizadas);

        Platform.runLater(() -> {
            tablaCompras.setItems(items);
            tablaCompras.refresh();

            // Seleccionar la misma compra y actualizar el label inmediatamente
            items.stream()
                .filter(c -> c.getIdCompra().equals(idSelFinal))
                .findFirst()
                .ifPresent(c -> {
                    tablaCompras.getSelectionModel().select(c);
                    actualizarLabelTotal(c);
                });

            alerta(Alert.AlertType.INFORMATION,
                "✅ Compra pagada correctamente.\n🎟️ Entradas generadas.\n"
                + "Estado: Pagada");
        });
    }

    /** RF-006: Cancelar la compra seleccionada y reflejar el cambio inmediatamente en la UI. */
    @FXML
    private void cancelarCompra() {
        Compra sel = tablaCompras.getSelectionModel().getSelectedItem();
        if (sel == null) {
            alerta(Alert.AlertType.WARNING, "Selecciona una compra."); return;
        }
        if ("Cancelada".equalsIgnoreCase(sel.getEstado())) {
            alerta(Alert.AlertType.WARNING, "La compra ya está cancelada."); return;
        }
        comprasCtrl.cancelarCompra(sel);

        // Recargar y refrescar tabla inmediatamente en el hilo JavaFX
        final String idSelFinal = sel.getIdCompra();
        List<Compra> actualizadas = comprasCtrl.historialUsuarioLogueado();
        ObservableList<Compra> items = FXCollections.observableArrayList(actualizadas);

        Platform.runLater(() -> {
            tablaCompras.setItems(items);
            tablaCompras.refresh();

            // Reseleccionar y actualizar label con estado "Cancelada"
            items.stream()
                .filter(c -> c.getIdCompra().equals(idSelFinal))
                .findFirst()
                .ifPresent(c -> {
                    tablaCompras.getSelectionModel().select(c);
                    actualizarLabelTotal(c);
                });

            alerta(Alert.AlertType.INFORMATION, "❌ Compra cancelada correctamente.");
        });
    }

    /**
     * RF-009: Agregar servicio adicional a la compra seleccionada (patrón Decorator).
     * Aplica el decorador al objeto Compra y actualiza la tabla y el label de total
     * de forma <b>inmediata</b> usando {@link Platform#runLater(Runnable)}.
     *
     * <p><b>IMPORTANTE:</b> agregar un servicio NO cambia el estado de la compra.
     * El estado solo cambia al pulsar "Pagar Compra" (RF-007).</p>
     */
    @FXML
    private void agregarServicio() {
        Compra sel = tablaCompras.getSelectionModel().getSelectedItem();
        if (sel == null) {
            alerta(Alert.AlertType.WARNING, "Selecciona una compra de la tabla primero."); return;
        }
        // Solo se puede agregar servicios a compras en estado "Creada"
        if (!"Creada".equalsIgnoreCase(sel.getEstado())) {
            alerta(Alert.AlertType.WARNING,
                "Solo puedes agregar servicios a compras en estado 'Creada'.\n"
                + "Estado actual: " + sel.getEstado()); return;
        }
        String servicio = cmbServicio != null ? cmbServicio.getValue() : null;
        if (servicio == null || servicio.isBlank()) {
            alerta(Alert.AlertType.WARNING, "Selecciona un tipo de servicio del combo (VIP, Seguro, etc.).");
            return;
        }
        try {
            double totalAntes = sel.getTotalBase();

            // Aplicar el Decorator (RF-009 / RF-050): solo muta el campo servicioDecorator.
            // NO llama a pagar(), NO cambia el estado de la compra.
            comprasCtrl.agregarServicioAdicional(sel, servicio);

            // Verificar explícitamente que el estado sigue siendo "Creada"
            if (!"Creada".equalsIgnoreCase(sel.getEstado())) {
                sel.setEstado("Creada");
                sel.setEstadoActual(new co.edu.uniquidio.poo.proyecto_final.model.EstadoCreadaState());
            }

            // Calcular precio extra para el mensaje (antes de runLater para capturar valor)
            double totalDespues = sel.getTotal();
            double extra        = totalDespues - totalAntes;
            final String idSelFinal = sel.getIdCompra();
            final String servicioFinal = servicio;

            // Recargar datos y actualizar UI inmediatamente en el hilo JavaFX
            List<Compra> actualizadas = comprasCtrl.historialUsuarioLogueado();
            ObservableList<Compra> items = FXCollections.observableArrayList(actualizadas);

            Platform.runLater(() -> {
                tablaCompras.setItems(items);
                // refresh() re-evalúa todas las CellValueFactory, reflejando el nuevo getTotal()
                tablaCompras.refresh();

                // Reseleccionar la misma compra para que el listener actualice el label
                Compra actualizada = items.stream()
                    .filter(c -> c.getIdCompra().equals(idSelFinal))
                    .findFirst().orElse(sel);
                tablaCompras.getSelectionModel().select(actualizada);

                // Actualizar label de total inmediatamente con los valores nuevos
                if (lblTotalConServicio != null) {
                    lblTotalConServicio.setText(
                        "✅ Total con servicio '" + servicioFinal.toUpperCase()
                        + "': $" + String.format("%,.0f", actualizada.getTotal())
                        + "  |  Estado: " + actualizada.getEstado());
                }

                // Mensaje de confirmación claro (RF-009)
                alerta(Alert.AlertType.INFORMATION,
                    "✅ Servicio '" + servicioFinal.toUpperCase() + "' agregado correctamente.\n\n"
                    + "💰 Total base:        $" + String.format("%,.0f", totalAntes) + "\n"
                    + "➕ Costo del servicio: $" + String.format("%,.0f", extra) + "\n"
                    + "💳 Nuevo total:       $" + String.format("%,.0f", actualizada.getTotal()) + "\n\n"
                    + "📋 " + actualizada.getDescripcion() + "\n"
                    + "🔖 Estado:            " + actualizada.getEstado()
                    + "  ← (sin cambios, paga cuando quieras)");
            });

        } catch (IllegalArgumentException ex) {
            alerta(Alert.AlertType.ERROR, "❌ " + ex.getMessage());
        }
    }

    /**
     * RF-011 / RF-046: Exportar historial de compras a CSV.
     * Abre un {@link FileChooser} para que el usuario elija la ubicación de guardado.
     */
    @FXML
    private void descargarReporteCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Reporte CSV");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Archivos CSV (*.csv)", "*.csv"));
        fileChooser.setInitialFileName("reporte_compras_"
            + java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"))
            + ".csv");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        javafx.stage.Stage stage =
            (javafx.stage.Stage) tablaCompras.getScene().getWindow();
        File destino = fileChooser.showSaveDialog(stage);

        if (destino != null) {
            boolean exito = reporteCtrl.exportarCSVAArchivo(destino.getAbsolutePath());
            if (exito) {
                alerta(Alert.AlertType.INFORMATION,
                    "✅ Reporte CSV exportado exitosamente.\n📁 Guardado en:\n"
                    + destino.getAbsolutePath());
            } else {
                alerta(Alert.AlertType.ERROR,
                    "❌ Error al generar el CSV. Verifica permisos de escritura.");
            }
        }
    }

    // ==================== NAVEGACIÓN ====================

    /** Navega al panel de administrador (solo para usuarios admin). */
    @FXML private void irAlPanelAdmin()  { App.mostrarDashboardAdmin(); }

    /** Navega a la pantalla de perfil del usuario. */
    @FXML private void irAPerfil()       { App.mostrarPerfil(); }

    /** Cierra la sesión y vuelve a la pantalla de login. */
    @FXML
    private void cerrarSesion() {
        sistema.setUsuarioLogueado(null);
        App.mostrarLogin();
    }

    // ==================== HELPERS ====================

    private void alerta(Alert.AlertType tipo, String msg) {
        Alert a = new Alert(tipo);
        a.setTitle("PGII - Plataforma de Eventos");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
