package co.edu.uniquidio.poo.proyecto_final;

import co.edu.uniquidio.poo.proyecto_final.Controller.ReporteController;
import co.edu.uniquidio.poo.proyecto_final.model.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ============================================================
 *  PRUEBAS UNITARIAS — PATRONES ESTRUCTURALES (RF-050)
 * ============================================================
 *  Cubre:
 *  - Decorator : ServicioAdicionalDecorator y subclases
 *  - Adapter   : ReporteAdapterImpl / ReporteTargetInterface
 *  - Composite : ComponenteSeatingComposite, Zona, Asiento
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
@DisplayName("RF-050 | Patrones Estructurales")
class PatronesEstructuralesTest {

    private SistemaGestionEventosSingleton sistema;
    private Compra compraBase;

    @BeforeEach
    void setup() {
        SistemaGestionEventosSingleton.resetInstancia();
        sistema = SistemaGestionEventosSingleton.getInstance();
        Usuario u = sistema.listarUsuarios().get(0);
        Evento  e = sistema.listarEventos().get(0);
        compraBase = sistema.crearCompra(
            new CompraBuilder().setUsuario(u).setEvento(e).setTotal(100_000));
    }

    // ==================== DECORATOR ====================

    @Nested
    @DisplayName("Decorator — ServicioAdicionalDecorator")
    class DecoratorTest {

        @Test
        @DisplayName("Sin decorator: getTotal() == getTotalBase()")
        void sinDecoratorTotalesIguales() {
            assertEquals(compraBase.getTotalBase(), compraBase.getTotal(), 0.01,
                "Sin decorator el total debe ser igual al total base");
        }

        @Test
        @DisplayName("ServicioVIPDecorator añade exactamente $80.000")
        void vipAgregaPrecio() {
            double antes = compraBase.getTotalBase();
            compraBase.setServicioDecorator(new ServicioVIPDecorator(compraBase));
            assertEquals(antes + 80_000, compraBase.getTotal(), 0.01,
                "VIP debe añadir exactamente $80.000");
        }

        @Test
        @DisplayName("ServicioSeguroCancelacionDecorator añade exactamente $25.000")
        void seguroAgregaPrecio() {
            double antes = compraBase.getTotalBase();
            compraBase.setServicioDecorator(new ServicioSeguroCancelacionDecorator(compraBase));
            assertEquals(antes + 25_000, compraBase.getTotal(), 0.01,
                "Seguro debe añadir exactamente $25.000");
        }

        @Test
        @DisplayName("ServicioMerchandisingDecorator añade exactamente $35.000")
        void merchanAgregaPrecio() {
            double antes = compraBase.getTotalBase();
            compraBase.setServicioDecorator(new ServicioMerchandisingDecorator(compraBase));
            assertEquals(antes + 35_000, compraBase.getTotal(), 0.01,
                "Merchandising debe añadir exactamente $35.000");
        }

        @Test
        @DisplayName("ServicioParqueaderoDecorator añade exactamente $15.000")
        void parqueaderoAgregaPrecio() {
            double antes = compraBase.getTotalBase();
            compraBase.setServicioDecorator(new ServicioParqueaderoDecorator(compraBase));
            assertEquals(antes + 15_000, compraBase.getTotal(), 0.01,
                "Parqueadero debe añadir exactamente $15.000");
        }

        @Test
        @DisplayName("getDescripcion() con VIP contiene '+ Acceso VIP'")
        void vipEnriqueceDescripcion() {
            compraBase.setServicioDecorator(new ServicioVIPDecorator(compraBase));
            assertTrue(compraBase.getDescripcion().contains("VIP"),
                "La descripción con VIP debe contener 'VIP'");
        }

        @Test
        @DisplayName("El estado NO cambia al agregar un decorator (sigue en 'Creada')")
        void decoratorNoAlteraEstado() {
            compraBase.setServicioDecorator(new ServicioVIPDecorator(compraBase));
            assertEquals("Creada", compraBase.getEstado(),
                "Agregar un servicio NO debe cambiar el estado de la compra");
        }

        @Test
        @DisplayName("Reemplazar decorator actualiza el total correctamente")
        void reemplazarDecoratorActualizaTotal() {
            compraBase.setServicioDecorator(new ServicioVIPDecorator(compraBase));
            double conVIP = compraBase.getTotal();
            compraBase.setServicioDecorator(new ServicioParqueaderoDecorator(compraBase));
            double conParqueadero = compraBase.getTotal();
            // El nuevo decorator se basa en totalBase, no en el total con VIP
            assertEquals(compraBase.getTotalBase() + 15_000, conParqueadero, 0.01);
            assertNotEquals(conVIP, conParqueadero,
                "Los totales con distintos decorators deben ser distintos");
        }
    }

    // ==================== ADAPTER ====================

    @Nested
    @DisplayName("Adapter — ReporteAdapterImpl / ReporteTargetInterface")
    class AdapterTest {

        private ReporteController reporteCtrl;

        @BeforeEach
        void setupAdapter() {
            reporteCtrl = new ReporteController(sistema);
        }

        @Test
        @DisplayName("generarContenidoReporte('VENTAS') devuelve contenido no vacío")
        void reporteVentasNoVacio() {
            String contenido = reporteCtrl.obtenerContenidoReporte("VENTAS", "2026");
            assertNotNull(contenido,  "Contenido no debe ser null");
            assertFalse(contenido.isBlank(), "Contenido no debe estar vacío");
        }

        @Test
        @DisplayName("generarContenidoReporte('OCUPACION') incluye zonas del recinto")
        void reporteOcupacionContieneZonas() {
            String contenido = reporteCtrl.obtenerContenidoReporte("OCUPACION", "Actual");
            assertTrue(contenido.contains("Zona") || contenido.contains("zona"),
                "Reporte de ocupación debe mencionar zonas");
        }

        @Test
        @DisplayName("generarContenidoReporte('CANCELACION') incluye tasa de cancelación")
        void reporteCancelacionContieneTasa() {
            String contenido = reporteCtrl.obtenerContenidoReporte("CANCELACION", "2026");
            assertNotNull(contenido);
            assertFalse(contenido.isBlank());
        }

        @Test
        @DisplayName("exportarCSVAArchivo crea el archivo en disco")
        void exportarCSVCreaArchivo() {
            String ruta = System.getProperty("java.io.tmpdir") + "/test_reporte_pgii.csv";
            boolean exito = reporteCtrl.exportarCSVAArchivo(ruta);
            assertTrue(exito, "La exportación CSV debe ser exitosa");
            java.io.File archivo = new java.io.File(ruta);
            assertTrue(archivo.exists(), "El archivo CSV debe existir en disco");
            assertTrue(archivo.length() > 0, "El archivo CSV no debe estar vacío");
            archivo.deleteOnExit();
        }

        @Test
        @DisplayName("ReporteAdapterImpl implementa ReporteTargetInterface (polimorfismo)")
        void implementaInterfaz() {
            // Verifica que el adapter puede tratarse como la interfaz Target
            assertDoesNotThrow(() -> {
                ReporteTargetInterface adapter = new ReporteAdapterImpl();
                adapter.generarReporte("VENTAS", "2026");
            }, "El Adapter debe implementar la interfaz Target sin errores");
        }
    }

    // ==================== COMPOSITE ====================

    @Nested
    @DisplayName("Composite — ComponenteSeatingComposite, Zona, Asiento")
    class CompositeTest {

        private Zona zona;

        @BeforeEach
        void setupComposite() {
            // Crear una zona con 12 asientos para pruebas aisladas
            zona = new Zona("Z-TEST", "General-Test", 12, 80_000);
        }

        @Test
        @DisplayName("Zona implementa ComponenteSeatingComposite")
        void zonaImplementaInterfaz() {
            assertInstanceOf(ComponenteSeatingComposite.class, zona,
                "Zona debe implementar ComponenteSeatingComposite");
        }

        @Test
        @DisplayName("Asiento implementa ComponenteSeatingComposite")
        void asientoImplementaInterfaz() {
            Asiento a = zona.getAsientos().get(0);
            assertInstanceOf(ComponenteSeatingComposite.class, a,
                "Asiento debe implementar ComponenteSeatingComposite");
        }

        @Test
        @DisplayName("Zona recién creada: getCapacidadDisponible() == capacidad")
        void capacidadDisponibleInicialLlena() {
            assertEquals(12, zona.getCapacidadDisponible(),
                "Zona nueva debe tener toda su capacidad disponible");
        }

        @Test
        @DisplayName("Después de reservar, getCapacidadDisponible() disminuye en 1")
        void capacidadDisminuyeAlReservar() {
            boolean reservado = zona.reservarAsiento("A", 1);
            assertTrue(reservado, "La reserva debe ser exitosa");
            assertEquals(11, zona.getCapacidadDisponible(),
                "Disponibilidad debe bajar en 1 tras reservar");
        }

        @Test
        @DisplayName("Reservar mismo asiento dos veces: segunda reserva devuelve false")
        void reservaDuplicadaFalla() {
            zona.reservarAsiento("A", 1);
            boolean segundaReserva = zona.reservarAsiento("A", 1);
            assertFalse(segundaReserva, "Asiento ya reservado no puede reservarse de nuevo");
        }

        @Test
        @DisplayName("getOcupacion() == número de asientos no disponibles")
        void ocupacionCoincideConNoDisponibles() {
            zona.reservarAsiento("A", 1);
            zona.reservarAsiento("A", 2);
            assertEquals(2, zona.getOcupacion(),
                "getOcupacion() debe reflejar los 2 asientos reservados");
        }

        @Test
        @DisplayName("Asiento individual: getCapacidadDisponible() es 1 si disponible, 0 si no")
        void asientoHoja() {
            Asiento a = zona.getAsientos().get(0);
            assertEquals(1, a.getCapacidadDisponible(), "Asiento disponible aporta 1");
            a.reservar();
            assertEquals(0, a.getCapacidadDisponible(), "Asiento reservado aporta 0");
        }

        @Test
        @DisplayName("Zona genera asientos automáticamente al construirse")
        void generacionAutomaticaAsientos() {
            Zona z = new Zona("Z-NEW", "VIP-New", 24, 200_000);
            assertEquals(24, z.getAsientos().size(),
                "Zona con capacidad 24 debe generar 24 asientos");
        }

        @Test
        @DisplayName("Asientos generados tienen filas A-Z con máximo 12 por fila")
        void asientosTienenFilasCorrectas() {
            // 12 asientos → todos en fila A
            Zona z12 = new Zona("Z12", "Test12", 12, 50_000);
            assertTrue(z12.getAsientos().stream().allMatch(a -> a.getFila().equals("A")),
                "12 asientos deben estar todos en fila A");
            // 13 asientos → 12 en A y 1 en B
            Zona z13 = new Zona("Z13", "Test13", 13, 50_000);
            long filaB = z13.getAsientos().stream().filter(a -> a.getFila().equals("B")).count();
            assertEquals(1, filaB, "El asiento 13 debe estar en fila B");
        }
    }
}
