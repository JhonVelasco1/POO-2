package co.edu.uniquidio.poo.proyecto_final;

import co.edu.uniquidio.poo.proyecto_final.Controller.*;
import co.edu.uniquidio.poo.proyecto_final.model.*;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ============================================================
 *  PRUEBAS DE INTEGRACIÓN — FLUJOS FUNCIONALES COMPLETOS
 * ============================================================
 *  Verifica que los Requisitos Funcionales se cumplen end-to-end,
 *  coordinando Model + Controller tal como lo hace la Vista (ViewController).
 *
 *  Flujos cubiertos:
 *  - RF-001: Login usuario
 *  - RF-003: Filtrar eventos
 *  - RF-006, RF-007: Crear y pagar compra
 *  - RF-009: Agregar servicio (Decorator)
 *  - RF-010: Historial de compras
 *  - RF-012: CRUD de usuarios
 *  - RF-013: CRUD de eventos
 *  - RF-018: Métricas del sistema
 *  - RF-041: Registro de incidencias
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
@DisplayName("Integración | Flujos Funcionales Completos")
class IntegracionFlujosTest {

    private SistemaGestionEventosSingleton sistema;
    private GestionComprasController  comprasCtrl;
    private GestionEventosController  eventosCtrl;
    private GestionUsuariosController usuariosCtrl;
    private LoginController           loginCtrl;
    private ReporteController         reporteCtrl;

    @BeforeEach
    void setup() {
        SistemaGestionEventosSingleton.resetInstancia();
        sistema      = SistemaGestionEventosSingleton.getInstance();
        comprasCtrl  = new GestionComprasController(sistema);
        eventosCtrl  = new GestionEventosController(sistema);
        usuariosCtrl = new GestionUsuariosController(sistema);
        loginCtrl    = new LoginController(sistema);
        reporteCtrl  = new ReporteController(sistema);
    }

    // ==================== AUTENTICACIÓN ====================

    @Test
    @DisplayName("RF-001 | Login exitoso con usuario de prueba")
    void loginExitoso() {
        Usuario u = loginCtrl.autenticar("usuario@ejemplo.com", "1234");
        assertNotNull(u, "Login con credenciales correctas debe retornar usuario");
        assertEquals("usuario@ejemplo.com", u.getCorreo());
        assertFalse(u.esAdmin(), "Este usuario no es admin");
    }

    @Test
    @DisplayName("RF-001 | Login del administrador retorna usuario con esAdmin()==true")
    void loginAdmin() {
        Usuario admin = loginCtrl.autenticar("admin@ejemplo.com", "admin");
        assertNotNull(admin);
        assertTrue(admin.esAdmin(), "El usuario 'admin@ejemplo.com' debe ser admin");
    }

    @Test
    @DisplayName("RF-001 | Login con contraseña incorrecta devuelve null")
    void loginPasswordIncorrecto() {
        assertNull(loginCtrl.autenticar("usuario@ejemplo.com", "WRONG"));
    }

    // ==================== EXPLORACIÓN ====================

    @Test
    @DisplayName("RF-003 | listarEventosDisponibles() devuelve solo eventos Publicados")
    void soloEventosPublicados() {
        List<Evento> disponibles = eventosCtrl.listarEventosDisponibles();
        assertTrue(disponibles.stream().allMatch(e -> "Publicado".equals(e.getEstado())),
            "Todos los eventos disponibles deben estar en estado 'Publicado'");
    }

    @Test
    @DisplayName("RF-003 | filtrar() por ciudad funciona correctamente")
    void filtrarEventosPorCiudad() {
        List<Evento> armenia = eventosCtrl.filtrar(null, "Armenia", null);
        assertFalse(armenia.isEmpty(), "Debe haber eventos en Armenia");
        assertTrue(armenia.stream().allMatch(e -> e.getCiudad().equalsIgnoreCase("Armenia")),
            "Todos los eventos filtrados deben ser de Armenia");
    }

    @Test
    @DisplayName("RF-003 | filtrar() por nombre (fragmento) funciona")
    void filtrarEventosPorNombre() {
        List<Evento> resultado = eventosCtrl.filtrar("Juanes", null, null);
        assertEquals(1, resultado.size(), "Debe encontrar exactamente el concierto de Juanes");
    }

    @Test
    @DisplayName("RF-003 | filtrar() sin parámetros devuelve todos los publicados")
    void filtrarSinParametros() {
        List<Evento> todos  = eventosCtrl.listarEventosDisponibles();
        List<Evento> filtro = eventosCtrl.filtrar(null, null, null);
        assertEquals(todos.size(), filtro.size(),
            "Filtrar sin parámetros debe devolver el mismo número que listarDisponibles");
    }

    // ==================== FLUJO COMPLETO DE COMPRA ====================

    @Test
    @DisplayName("RF-006 + RF-007 | Flujo completo: crear compra → pagar → verificar estado")
    void flujoCompletoCreaYPaga() {
        // RF-001: Autenticar usuario
        Usuario u = loginCtrl.autenticar("usuario@ejemplo.com", "1234");
        sistema.setUsuarioLogueado(u);

        // RF-006: Crear compra
        Evento evento = eventosCtrl.listarEventosDisponibles().get(0);
        Compra compra = comprasCtrl.crearCompra(u, evento, 180_000);
        assertNotNull(compra, "La compra debe ser creada");
        assertEquals("Creada", compra.getEstado(), "Estado inicial debe ser 'Creada'");

        // RF-007: Pagar compra
        comprasCtrl.pagarCompra(compra, "tarjeta", "VISA-5432");
        assertEquals("Pagada", compra.getEstado(), "Tras pagar, el estado debe ser 'Pagada'");

        // RF-038: Entradas generadas
        assertFalse(compra.getEntradas().isEmpty(), "Pagar debe generar entradas (RF-038)");
    }

    @Test
    @DisplayName("RF-006 | Cancelar compra en estado 'Creada'")
    void cancelarCompraCreada() {
        Usuario u = sistema.listarUsuarios().get(0);
        Evento  e = eventosCtrl.listarEventosDisponibles().get(0);
        Compra compra = comprasCtrl.crearCompra(u, e, 100_000);
        comprasCtrl.cancelarCompra(compra);
        assertEquals("Cancelada", compra.getEstado());
    }

    @Test
    @DisplayName("RF-009 | Agregar servicio VIP actualiza el total correctamente")
    void agregarServicioVIPActualizaTotal() {
        Usuario u = sistema.listarUsuarios().get(0);
        Evento  e = eventosCtrl.listarEventosDisponibles().get(0);
        Compra compra = comprasCtrl.crearCompra(u, e, 100_000);

        double totalAntes = compra.getTotal();
        comprasCtrl.agregarServicioAdicional(compra, "vip");

        assertEquals(totalAntes + 80_000, compra.getTotal(), 0.01,
            "Agregar VIP debe añadir $80.000 al total");
        assertEquals("Creada", compra.getEstado(),
            "Agregar servicio NO debe cambiar el estado de la compra (RF-009)");
    }

    @Test
    @DisplayName("RF-009 | agregarServicioAdicional con tipo inválido lanza excepción")
    void servicioInvalidoLanzaExcepcion() {
        Compra compra = comprasCtrl.crearCompra(
            sistema.listarUsuarios().get(0), eventosCtrl.listarEventos().get(0), 50_000);
        assertThrows(IllegalArgumentException.class,
            () -> comprasCtrl.agregarServicioAdicional(compra, "masaje"),
            "Servicio desconocido debe lanzar IllegalArgumentException");
    }

    @Test
    @DisplayName("RF-010 | historialUsuarioLogueado() devuelve solo compras del usuario logueado")
    void historialSoloDelUsuario() {
        Usuario u = sistema.listarUsuarios().get(0);
        sistema.setUsuarioLogueado(u);
        List<Compra> historial = comprasCtrl.historialUsuarioLogueado();
        assertTrue(historial.stream().allMatch(c -> c.getUsuario().getIdUsuario().equals(u.getIdUsuario())),
            "El historial debe contener solo las compras del usuario logueado");
    }

    // ==================== CRUD USUARIOS (RF-012) ====================

    @Test
    @DisplayName("RF-012 | Crear usuario con correo nuevo lo registra en el sistema")
    void crearUsuarioNuevo() {
        int antes = usuariosCtrl.listarUsuarios().size();
        Usuario nuevo = usuariosCtrl.crearUsuario(
            "Ana Prueba", "ana@test.com", "abc123", "3009876543");
        assertEquals(antes + 1, usuariosCtrl.listarUsuarios().size(),
            "La lista de usuarios debe crecer en 1");
        assertNotNull(nuevo, "El nuevo usuario no debe ser null");
    }

    @Test
    @DisplayName("RF-012 | Crear usuario con correo duplicado lanza excepción")
    void correoDuplicadoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
            () -> usuariosCtrl.crearUsuario("Clon", "usuario@ejemplo.com", "1234", "0"),
            "Correo duplicado debe lanzar IllegalArgumentException");
    }

    @Test
    @DisplayName("RF-012 | Eliminar usuario lo quita del sistema")
    void eliminarUsuario() {
        Usuario u = usuariosCtrl.crearUsuario("Borrar Me", "borrar@test.com", "1234", "0");
        int antes = usuariosCtrl.listarUsuarios().size();
        usuariosCtrl.eliminarUsuario(u.getIdUsuario());
        assertEquals(antes - 1, usuariosCtrl.listarUsuarios().size());
        assertNull(sistema.listarUsuarios().stream()
            .filter(x -> x.getIdUsuario().equals(u.getIdUsuario())).findFirst().orElse(null));
    }

    // ==================== CRUD EVENTOS (RF-013) ====================

    @Test
    @DisplayName("RF-013 | Crear evento lo agrega al sistema")
    void crearEventoLoAgrega() {
        int antes = eventosCtrl.listarEventos().size();
        Evento nuevo = eventosCtrl.crearEvento(
            "concierto", "Feria de Flores", "Música", "Medellín", "2026-08-01 18:00");
        assertEquals(antes + 1, eventosCtrl.listarEventos().size());
        assertEquals("Borrador", nuevo.getEstado(),
            "Evento recién creado debe estar en estado 'Borrador'");
    }

    @Test
    @DisplayName("RF-024 | Publicar evento cambia su estado a 'Publicado'")
    void publicarEventoCambiaEstado() {
        Evento e = eventosCtrl.crearEvento(
            "teatro", "Nuevo Show", "Arte", "Cali", "2026-07-20 20:00");
        assertEquals("Borrador", e.getEstado());
        eventosCtrl.publicarEvento(e.getIdEvento());
        assertEquals("Publicado", e.getEstado());
    }

    // ==================== MÉTRICAS E INCIDENCIAS ====================

    @Test
    @DisplayName("RF-018 | obtenerMetricas() devuelve cadena con datos del sistema")
    void metricasNoVacias() {
        String metricas = reporteCtrl.obtenerMetricas();
        assertNotNull(metricas);
        assertFalse(metricas.isBlank(), "Las métricas del sistema no deben estar vacías");
        assertTrue(metricas.contains("Ventas") || metricas.contains("Ingresos"),
            "Las métricas deben incluir información de ventas o ingresos");
    }

    @Test
    @DisplayName("RF-041 | registrarIncidencia() aumenta el conteo de incidencias")
    void registrarIncidencia() {
        int antes = sistema.getIncidencias().size();
        sistema.registrarIncidencia(
            new Incidencia("TEST_UNITARIO", "Prueba de registro de incidencia"));
        assertEquals(antes + 1, sistema.getIncidencias().size(),
            "Registrar incidencia debe aumentar el contador en 1");
    }

    @Test
    @DisplayName("RF-042 | filtrarIncidenciasPorTipo() devuelve solo las del tipo indicado")
    void filtrarIncidenciasPorTipo() {
        sistema.registrarIncidencia(new Incidencia("TIPO_X", "Primera de tipo X"));
        sistema.registrarIncidencia(new Incidencia("TIPO_X", "Segunda de tipo X"));
        sistema.registrarIncidencia(new Incidencia("TIPO_Y", "Una de tipo Y"));

        List<Incidencia> resultado = sistema.filtrarIncidenciasPorTipo("TIPO_X");
        assertTrue(resultado.size() >= 2, "Debe haber al menos 2 incidencias de TIPO_X");
        assertTrue(resultado.stream().allMatch(i -> "TIPO_X".equals(i.getTipo())),
            "Todas las incidencias filtradas deben ser de TIPO_X");
    }
}
