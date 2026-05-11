package co.edu.uniquidio.poo.proyecto_final;

import co.edu.uniquidio.poo.proyecto_final.model.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ============================================================
 *  PRUEBAS UNITARIAS — PATRONES CREACIONALES (RF-049)
 * ============================================================
 *  Cubre:
 *  - Singleton : SistemaGestionEventosSingleton
 *  - Factory Method : EventoFactoryMethod
 *  - Builder : CompraBuilder
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
@DisplayName("RF-049 | Patrones Creacionales")
class PatronesCreacionalesTest {

    @BeforeEach
    void resetSingleton() {
        // Reinicia la instancia para aislar cada test (RF-045)
        SistemaGestionEventosSingleton.resetInstancia();
    }

    // ==================== SINGLETON ====================

    @Nested
    @DisplayName("Singleton — SistemaGestionEventosSingleton")
    class SingletonTest {

        @Test
        @DisplayName("getInstance() devuelve siempre la misma instancia")
        void mismaInstancia() {
            SistemaGestionEventosSingleton a = SistemaGestionEventosSingleton.getInstance();
            SistemaGestionEventosSingleton b = SistemaGestionEventosSingleton.getInstance();
            assertSame(a, b, "getInstance() debe retornar la misma referencia siempre");
        }

        @Test
        @DisplayName("Datos de prueba se inicializan al primer getInstance()")
        void datosPruebaInicializados() {
            SistemaGestionEventosSingleton s = SistemaGestionEventosSingleton.getInstance();
            assertFalse(s.listarUsuarios().isEmpty(),   "Deben existir usuarios de prueba (RF-045)");
            assertFalse(s.listarEventos().isEmpty(),    "Deben existir eventos de prueba (RF-045)");
            assertFalse(s.listarRecintos().isEmpty(),   "Deben existir recintos de prueba (RF-045)");
            assertFalse(s.listarCompras().isEmpty(),    "Deben existir compras de prueba (RF-045)");
        }

        @Test
        @DisplayName("autenticar() con credenciales correctas devuelve usuario (RF-001)")
        void autenticacionExitosa() {
            SistemaGestionEventosSingleton s = SistemaGestionEventosSingleton.getInstance();
            Usuario u = s.autenticar("usuario@ejemplo.com", "1234");
            assertNotNull(u, "Debe autenticar con credenciales correctas");
            assertEquals("usuario@ejemplo.com", u.getCorreo());
        }

        @Test
        @DisplayName("autenticar() con credenciales incorrectas devuelve null (RF-001)")
        void autenticacionFallida() {
            SistemaGestionEventosSingleton s = SistemaGestionEventosSingleton.getInstance();
            assertNull(s.autenticar("inexistente@test.com", "wrong"),
                "Debe retornar null con credenciales incorrectas");
        }

        @Test
        @DisplayName("getInstanciaONull() devuelve null antes de la primera llamada")
        void instanciaONullAntesDeInit() {
            // resetInstancia() ya fue llamado en @BeforeEach
            assertNull(SistemaGestionEventosSingleton.getInstanciaONull(),
                "Antes de getInstance(), getInstanciaONull() debe ser null");
        }
    }

    // ==================== FACTORY METHOD ====================

    @Nested
    @DisplayName("Factory Method — EventoFactoryMethod")
    class FactoryMethodTest {

        @Test
        @DisplayName("crearEvento('concierto') devuelve instancia de Concierto")
        void creaConcierto() {
            Evento e = EventoFactoryMethod.crearEvento(
                "concierto", "Rock en Quindío", "Música", "Armenia", "2026-09-01 20:00");
            assertNotNull(e);
            assertInstanceOf(Concierto.class, e, "Debe ser instancia de Concierto");
            assertEquals("Rock en Quindío", e.getNombre());
            assertEquals("Borrador", e.getEstado(), "Estado inicial debe ser Borrador");
        }

        @Test
        @DisplayName("crearEvento('teatro') devuelve instancia de Teatro")
        void creaTeatro() {
            Evento e = EventoFactoryMethod.crearEvento(
                "teatro", "El Rey Lear", "Teatro", "Bogotá", "2026-10-15 19:00");
            assertInstanceOf(Teatro.class, e, "Debe ser instancia de Teatro");
        }

        @Test
        @DisplayName("crearEvento('conferencia') devuelve instancia de Conferencia")
        void creaConferencia() {
            Evento e = EventoFactoryMethod.crearEvento(
                "conferencia", "Java Summit", "Tecnología", "Medellín", "2026-11-20 09:00");
            assertInstanceOf(Conferencia.class, e, "Debe ser instancia de Conferencia");
        }

        @Test
        @DisplayName("IDs generados son únicos entre llamadas consecutivas")
        void idsUnicos() {
            Evento e1 = EventoFactoryMethod.crearEvento("concierto","A","M","C","2026-01-01");
            Evento e2 = EventoFactoryMethod.crearEvento("teatro",   "B","M","C","2026-01-02");
            assertNotEquals(e1.getIdEvento(), e2.getIdEvento(),
                "Cada evento debe tener un ID único");
        }

        @Test
        @DisplayName("tipo no reconocido lanza IllegalArgumentException")
        void tipoDesconocidoLanzaExcepcion() {
            assertThrows(IllegalArgumentException.class, () ->
                EventoFactoryMethod.crearEvento("circo", "Circo Magno", "Arte", "Cali", "2026-01-01"),
                "Tipo desconocido debe lanzar IllegalArgumentException");
        }

        @Test
        @DisplayName("tipo en MAYÚSCULAS es reconocido (insensible a mayúsculas)")
        void tipoMayusculasAceptado() {
            assertDoesNotThrow(() ->
                EventoFactoryMethod.crearEvento("CONCIERTO", "Test", "Música", "X", "2026-01-01"));
        }
    }

    // ==================== BUILDER ====================

    @Nested
    @DisplayName("Builder — CompraBuilder")
    class BuilderTest {

        private SistemaGestionEventosSingleton sistema;
        private Usuario usuario;
        private Evento  evento;

        @BeforeEach
        void setup() {
            sistema  = SistemaGestionEventosSingleton.getInstance();
            usuario  = sistema.listarUsuarios().get(0);
            evento   = sistema.listarEventos().get(0);
        }

        @Test
        @DisplayName("build() crea Compra con atributos correctos")
        void builderCreaCompra() {
            Compra c = new CompraBuilder()
                .setUsuario(usuario)
                .setEvento(evento)
                .setTotal(200_000)
                .build();
            assertNotNull(c,                         "build() no debe devolver null");
            assertEquals(usuario,    c.getUsuario(), "Usuario debe coincidir");
            assertEquals(evento,     c.getEvento(),  "Evento debe coincidir");
            assertEquals(200_000,    c.getTotal(),   0.01, "Total debe coincidir");
            assertEquals("Creada",   c.getEstado(),  "Estado inicial debe ser 'Creada'");
            assertNotNull(c.getIdCompra(),            "ID no debe ser null");
            assertNotNull(c.getFechaCreacion(),        "Fecha de creación no debe ser null");
        }

        @Test
        @DisplayName("API fluida: los métodos retornan el mismo builder (encadenamiento)")
        void apiFluida() {
            CompraBuilder b = new CompraBuilder();
            assertSame(b, b.setUsuario(usuario),   "setUsuario() debe retornar this");
            assertSame(b, b.setEvento(evento),     "setEvento() debe retornar this");
            assertSame(b, b.setTotal(100_000),     "setTotal() debe retornar this");
        }

        @Test
        @DisplayName("IDs generados son únicos entre dos builds consecutivos")
        void buildIdsUnicos() {
            Compra c1 = new CompraBuilder().setUsuario(usuario).setEvento(evento).setTotal(100).build();
            Compra c2 = new CompraBuilder().setUsuario(usuario).setEvento(evento).setTotal(100).build();
            assertNotEquals(c1.getIdCompra(), c2.getIdCompra(),
                "Dos builds deben producir IDs distintos");
        }

        @Test
        @DisplayName("Estado inicial de la compra es siempre 'Creada'")
        void estadoInicialCreada() {
            Compra c = new CompraBuilder().setUsuario(usuario).setEvento(evento).setTotal(50_000).build();
            assertEquals("Creada", c.getEstado());
            assertNotNull(c.getEstadoActual(), "estadoActual no debe ser null");
            assertInstanceOf(EstadoCreadaState.class, c.getEstadoActual());
        }
    }
}
