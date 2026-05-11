package co.edu.uniquidio.poo.proyecto_final;

import co.edu.uniquidio.poo.proyecto_final.Controller.GestionComprasController;
import co.edu.uniquidio.poo.proyecto_final.model.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/**
 * ============================================================
 *  PRUEBAS UNITARIAS — PATRONES DE COMPORTAMIENTO (RF-051)
 * ============================================================
 *  Cubre:
 *  - Strategy : PagoStrategyInterface y estrategias concretas
 *  - State    : EstadoCompraState y transiciones del ciclo de vida
 *  - Observer : ObserverNotificacion, Usuario como Observer
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
@DisplayName("RF-051 | Patrones de Comportamiento")
class PatronesComportamientoTest {

    private SistemaGestionEventosSingleton sistema;
    private Usuario usuario;
    private Evento  evento;

    @BeforeEach
    void setup() {
        SistemaGestionEventosSingleton.resetInstancia();
        sistema  = SistemaGestionEventosSingleton.getInstance();
        usuario  = sistema.listarUsuarios().get(0);
        evento   = sistema.listarEventos().get(0);
    }

    // ==================== STRATEGY ====================

    @Nested
    @DisplayName("Strategy — PagoStrategyInterface y ProcesadorPago")
    class StrategyTest {

        @Test
        @DisplayName("PagoTarjetaStrategy.procesarPago() devuelve true")
        void tarjetaAprueba() {
            PagoStrategyInterface estrategia = new PagoTarjetaStrategy();
            assertTrue(estrategia.procesarPago(150_000, "VISA-1234"),
                "Pago con tarjeta debe retornar true");
        }

        @Test
        @DisplayName("PagoPSEStrategy.procesarPago() devuelve true")
        void pseAprueba() {
            PagoStrategyInterface estrategia = new PagoPSEStrategy();
            assertTrue(estrategia.procesarPago(200_000, "Bancolombia"),
                "Pago con PSE debe retornar true");
        }

        @Test
        @DisplayName("PagoEfectivoStrategy.procesarPago() devuelve true")
        void efectivoAprueba() {
            PagoStrategyInterface estrategia = new PagoEfectivoStrategy();
            assertTrue(estrategia.procesarPago(80_000, "Efectivo"),
                "Pago en efectivo debe retornar true");
        }

        @Test
        @DisplayName("ProcesadorPago con Tarjeta retorna true y ejecuta la estrategia")
        void procesadorConTarjeta() {
            ProcesadorPago procesador = new ProcesadorPago(new PagoTarjetaStrategy());
            assertTrue(procesador.ejecutarPago(100_000, "VISA-4321"),
                "ProcesadorPago con tarjeta debe retornar true");
        }

        @Test
        @DisplayName("GestionComprasController selecciona PagoTarjetaStrategy por defecto")
        void controllerUsaTarjetaPorDefecto() {
            GestionComprasController ctrl = new GestionComprasController(sistema);
            Compra c = ctrl.crearCompra(usuario, evento, 100_000);
            assertDoesNotThrow(() -> ctrl.pagarCompra(c, "tarjeta", "VISA-9999"),
                "Pagar con 'tarjeta' no debe lanzar excepción");
            assertEquals("Pagada", c.getEstado(),
                "Después de pagarCompra() el estado debe ser 'Pagada'");
        }

        @Test
        @DisplayName("Estrategias tienen getNombreMetodo() no vacío")
        void nombreMetodoNoVacio() {
            assertFalse(new PagoTarjetaStrategy().getNombreMetodo().isBlank());
            assertFalse(new PagoPSEStrategy().getNombreMetodo().isBlank());
            assertFalse(new PagoEfectivoStrategy().getNombreMetodo().isBlank());
        }
    }

    // ==================== STATE ====================

    @Nested
    @DisplayName("State — EstadoCompraState y transiciones del ciclo de vida")
    class StateTest {

        private Compra compra;

        @BeforeEach
        void crearCompra() {
            compra = sistema.crearCompra(
                new CompraBuilder().setUsuario(usuario).setEvento(evento).setTotal(120_000));
        }

        @Test
        @DisplayName("Estado inicial es 'Creada' con EstadoCreadaState")
        void estadoInicial() {
            assertEquals("Creada", compra.getEstado());
            assertInstanceOf(EstadoCreadaState.class, compra.getEstadoActual());
        }

        @Test
        @DisplayName("Creada → pagar() → Pagada con EstadoPagadaState")
        void transicionCreadaAPagada() {
            compra.pagar();
            assertEquals("Pagada", compra.getEstado(),
                "Tras pagar(), el estado debe ser 'Pagada'");
            assertInstanceOf(EstadoPagadaState.class, compra.getEstadoActual(),
                "El estado actual debe cambiar a EstadoPagadaState");
        }

        @Test
        @DisplayName("Creada → cancelar() → Cancelada con EstadoCanceladaState")
        void transicionCreadaACancelada() {
            compra.cancelar();
            assertEquals("Cancelada", compra.getEstado(),
                "Tras cancelar() en estado Creada, el estado debe ser 'Cancelada'");
            assertInstanceOf(EstadoCanceladaState.class, compra.getEstadoActual());
        }

        @Test
        @DisplayName("Pagada → confirmar() → Confirmada con EstadoConfirmadaState")
        void transicionPagadaAConfirmada() {
            compra.pagar();
            compra.confirmar();
            assertEquals("Confirmada", compra.getEstado(),
                "Tras confirmar() en estado Pagada, el estado debe ser 'Confirmada'");
            assertInstanceOf(EstadoConfirmadaState.class, compra.getEstadoActual());
        }

        @Test
        @DisplayName("Pagada → cancelar() → Cancelada (con incidencia)")
        void transicionPagadaACancelada() {
            compra.pagar();
            int incidenciasAntes = sistema.getIncidencias().size();
            compra.cancelar();
            assertEquals("Cancelada", compra.getEstado());
            assertTrue(sistema.getIncidencias().size() > incidenciasAntes,
                "Cancelar una compra pagada debe registrar una incidencia (RF-041)");
        }

        @Test
        @DisplayName("pagar() en estado 'Pagada' no cambia el estado (idempotente)")
        void pagadaSegundoPayNoAltera() {
            compra.pagar(); // → Pagada
            compra.pagar(); // llamada redundante
            assertEquals("Pagada", compra.getEstado(),
                "Pagar una compra ya pagada no debe cambiar su estado");
        }

        @Test
        @DisplayName("Cancelada → no permite más transiciones activas")
        void canceladaEsFinal() {
            compra.cancelar();
            compra.pagar();     // intento ilegal
            compra.confirmar(); // intento ilegal
            assertEquals("Cancelada", compra.getEstado(),
                "Una compra cancelada no debe transicionar con pagar() o confirmar()");
        }

        @Test
        @DisplayName("GestionComprasController.cancelarCompra() anula entradas (RF-040)")
        void cancelarCompraAnulaEntradas() {
            GestionComprasController ctrl = new GestionComprasController(sistema);
            ctrl.pagarCompra(compra, "tarjeta", "TEST");
            // Después de pagar existen entradas
            ctrl.cancelarCompra(compra);
            assertEquals("Cancelada", compra.getEstado());
            // Todas las entradas deben estar anuladas
            List<Entrada> entradas = compra.getEntradas();
            assertTrue(entradas.stream().allMatch(e -> "Anulada".equals(e.getEstado())),
                "Cancelar la compra debe anular todas las entradas (RF-040)");
        }
    }

    // ==================== OBSERVER ====================

    @Nested
    @DisplayName("Observer — ObserverNotificacion, Usuario y SistemaGestion (Sujeto)")
    class ObserverTest {

        @Test
        @DisplayName("Usuario implementa ObserverNotificacion")
        void usuarioImplementaObserver() {
            assertInstanceOf(ObserverNotificacion.class, usuario,
                "Usuario debe implementar ObserverNotificacion");
        }

        @Test
        @DisplayName("Sistema registra automáticamente cada usuario como observer al crearlo")
        void usuarioRegistradoComoObserver() {
            int observersAntes = sistema.getObservers().size();
            // Crear un nuevo usuario
            Usuario nuevoU = new Usuario("U-OBS", "Test Obs", "obs@test.com", "pass", "3001111111");
            sistema.crearUsuario(nuevoU);
            assertEquals(observersAntes + 1, sistema.getObservers().size(),
                "Crear un usuario debe registrarlo como observer automáticamente");
        }

        @Test
        @DisplayName("notificarTodos() llena el historial de notificaciones del usuario")
        void notificacionLlegaAUsuario() {
            sistema.notificarTodos("Prueba de notificación PGII");
            // El usuario está en la lista de observers; su historial debe contener el mensaje
            List<String> notifs = usuario.getNotificaciones();
            assertTrue(notifs.stream().anyMatch(n -> n.contains("Prueba de notificación PGII")),
                "El usuario debe haber recibido la notificación");
        }

        @Test
        @DisplayName("Publicar evento dispara notificación a todos los observers")
        void publicarEventoNotifica() {
            List<String> notifsBefore = List.copyOf(usuario.getNotificaciones());
            evento.publicar();
            List<String> notifsAfter = usuario.getNotificaciones();
            assertTrue(notifsAfter.size() > notifsBefore.size(),
                "Publicar un evento debe enviar notificación a los observers (RF-017)");
        }

        @Test
        @DisplayName("Cancelar evento dispara notificación y registra incidencia (RF-017)")
        void cancelarEventoNotificaYRegistraIncidencia() {
            int incidenciasAntes = sistema.getIncidencias().size();
            List<String> notifsBefore = List.copyOf(usuario.getNotificaciones());

            evento.cancelar();

            assertTrue(usuario.getNotificaciones().size() > notifsBefore.size(),
                "Cancelar evento debe notificar a los observers");
            assertTrue(sistema.getIncidencias().size() > incidenciasAntes,
                "Cancelar evento debe registrar una incidencia (RF-041)");
        }

        @Test
        @DisplayName("Crear compra en el sistema dispara notificación (RF-008)")
        void crearCompraNotifica() {
            List<String> notifsBefore = List.copyOf(usuario.getNotificaciones());
            sistema.crearCompra(
                new CompraBuilder().setUsuario(usuario).setEvento(evento).setTotal(50_000));
            assertTrue(usuario.getNotificaciones().size() > notifsBefore.size(),
                "Crear una compra debe notificar a los observers (RF-008)");
        }

        @Test
        @DisplayName("Observer no se registra dos veces (no duplicados)")
        void noDuplicados() {
            int countAntes = sistema.getObservers().size();
            sistema.agregarObserver(usuario); // intentar agregar el mismo observer de nuevo
            assertEquals(countAntes, sistema.getObservers().size(),
                "Agregar el mismo observer dos veces no debe duplicar la lista");
        }
    }
}
