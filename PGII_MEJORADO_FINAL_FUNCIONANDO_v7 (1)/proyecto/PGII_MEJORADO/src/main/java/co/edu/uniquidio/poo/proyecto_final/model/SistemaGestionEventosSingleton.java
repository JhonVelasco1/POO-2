package co.edu.uniquidio.poo.proyecto_final.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ============================================================
 *  PATRÓN: SINGLETON (Creacional) — RF-049
 * ============================================================
 *  PROBLEMA: La aplicación necesita UNA SOLA instancia del sistema
 *  central que coordine usuarios, eventos, compras e incidencias.
 *  Si hubiera múltiples instancias, los datos estarían desincronizados.
 *
 *  PROPÓSITO: Garantizar que exista exactamente una instancia de
 *  {@code SistemaGestionEventosSingleton} y proporcionar un punto
 *  global de acceso a ella.
 *
 *  APLICACIÓN: El constructor es privado; {@code getInstance()} devuelve
 *  siempre la misma instancia. Todos los ViewControllers y Controllers
 *  llaman a {@code SistemaGestionEventosSingleton.getInstance()}.
 *
 *  ASCII:
 *  ┌──────────────────────────────────────┐
 *  │  SistemaGestionEventosSingleton      │
 *  │  - instancia: static (private)       │
 *  │  + getInstance(): static             │◄── único acceso
 *  │  - SistemaGestionEventosSingleton()  │    (privado)
 *  └──────────────────────────────────────┘
 * ============================================================
 *
 * Clase central del sistema. Gestiona todas las entidades del dominio:
 * {@link Usuario}, {@link Evento}, {@link Recinto}, {@link Compra} e
 * {@link Incidencia}. Implementa el patrón Observer para notificaciones
 * a los usuarios registrados (RF-008, RF-017).
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public class SistemaGestionEventosSingleton {

    // ==================== SINGLETON ====================
    /** Instancia única (lazy initialization). */
    private static SistemaGestionEventosSingleton instancia;

    // ==================== LISTAS DE ENTIDADES ====================
    /** RF-012: Repositorio en memoria de usuarios. */
    private final List<Usuario> usuarios    = new ArrayList<>();
    /** RF-013: Repositorio en memoria de eventos. */
    private final List<Evento>  eventos     = new ArrayList<>();
    /** RF-014: Repositorio en memoria de recintos. */
    private final List<Recinto> recintos    = new ArrayList<>();
    /** RF-016: Repositorio en memoria de compras. */
    private final List<Compra>  compras     = new ArrayList<>();
    /** RF-041: Repositorio en memoria de incidencias. */
    private final List<Incidencia> incidencias = new ArrayList<>();

    // ==================== OBSERVER ====================
    /** Lista de observadores para notificaciones (RF-008). */
    private final List<ObserverNotificacion> observers = new ArrayList<>();

    /** Usuario actualmente autenticado. */
    private Usuario usuarioLogueado;

    // ==================== CONSTRUCTOR PRIVADO ====================
    /**
     * Constructor privado — parte del patrón Singleton.
     * Inicializa los datos de prueba (RF-045).
     */
    private SistemaGestionEventosSingleton() {
        // inicialización se hace en getInstance() después de asignar instancia
    }

    /**
     * Punto de acceso global a la instancia única.
     * Implementación lazy (se crea al primer uso).
     *
     * @return la única instancia de {@code SistemaGestionEventosSingleton}
     */
    public static SistemaGestionEventosSingleton getInstance() {
        if (instancia == null) {
            instancia = new SistemaGestionEventosSingleton(); // constructor vacío
            instancia.inicializarDatosPrueba();               // init segura: instancia ya asignada
        }
        return instancia;
    }

    /**
     * Devuelve la instancia si ya fue creada, o {@code null} si aún está en construcción.
     * Usado por {@link Evento} para evitar recursión infinita durante la inicialización.
     */
    public static SistemaGestionEventosSingleton getInstanciaONull() {
        return instancia;
    }

    // ==================== OBSERVER (RF-008, RF-017) ====================

    /**
     * Registra un observador para recibir notificaciones del sistema.
     *
     * @param o el observador a registrar
     */
    public void agregarObserver(ObserverNotificacion o) {
        if (o != null && !observers.contains(o)) {
            observers.add(o);
        }
    }

    /**
     * Notifica a todos los observadores registrados con un mensaje.
     *
     * @param mensaje el mensaje de notificación
     */
    public void notificarTodos(String mensaje) {
        observers.forEach(o -> o.actualizar(mensaje));
        System.out.println("📢 Notificación: " + mensaje);
    }

    // ==================== INCIDENCIAS (RF-041, RF-042) ====================

    /**
     * Registra una incidencia operativa en el sistema (RF-041).
     *
     * @param i la incidencia a registrar
     */
    public void registrarIncidencia(Incidencia i) {
        incidencias.add(i);
        System.out.println("⚠️ Incidencia: " + i.getTipo() + " - " + i.getDescripcion());
        notificarTodos("Incidencia detectada: " + i.getTipo());
    }

    /**
     * Devuelve todas las incidencias registradas (RF-042).
     *
     * @return lista de incidencias (copia defensiva)
     */
    public List<Incidencia> getIncidencias() {
        return new ArrayList<>(incidencias);
    }

    /**
     * Filtra incidencias por tipo (RF-042).
     *
     * @param tipo tipo de incidencia a filtrar
     * @return lista filtrada de incidencias
     */
    public List<Incidencia> filtrarIncidenciasPorTipo(String tipo) {
        if (tipo == null || tipo.isBlank()) return getIncidencias();
        return incidencias.stream()
                .filter(i -> i.getTipo().equalsIgnoreCase(tipo))
                .collect(Collectors.toList());
    }

    // ==================== AUTENTICACIÓN (RF-001, RF-020) ====================

    /**
     * Autentica a un usuario con correo y contraseña (RF-001).
     *
     * @param email    correo del usuario
     * @param password contraseña del usuario
     * @return el {@link Usuario} autenticado, o {@code null} si las credenciales son incorrectas
     */
    public Usuario autenticar(String email, String password) {
        Usuario u = usuarios.stream()
                .filter(us -> us.getCorreo().equalsIgnoreCase(email)
                           && us.getPassword().equals(password))
                .findFirst().orElse(null);
        if (u != null) this.usuarioLogueado = u;
        return u;
    }

    /** @return el usuario actualmente logueado, o {@code null} */
    public Usuario getUsuarioLogueado() { return usuarioLogueado; }

    /**
     * Establece (o limpia) el usuario logueado.
     *
     * @param u el usuario a establecer como logueado, o {@code null} para cerrar sesión
     */
    public void setUsuarioLogueado(Usuario u) { this.usuarioLogueado = u; }

    // ==================== GESTIÓN DE USUARIOS (RF-012, RF-020) ====================

    /**
     * Crea y registra un nuevo usuario en el sistema (RF-012, RF-020).
     * También lo registra como observador de notificaciones.
     *
     * @param u el usuario a registrar
     */
    public void crearUsuario(Usuario u) {
        usuarios.add(u);
        agregarObserver(u);
    }

    /**
     * Devuelve todos los usuarios registrados (RF-012).
     *
     * @return lista de usuarios (copia defensiva)
     */
    public List<Usuario> listarUsuarios() { return new ArrayList<>(usuarios); }

    /**
     * Elimina un usuario por su identificador único (RF-012).
     *
     * @param id el identificador del usuario a eliminar
     */
    public void eliminarUsuario(String id) {
        usuarios.removeIf(u -> u.getIdUsuario().equals(id));
    }

    // ==================== GESTIÓN DE EVENTOS (RF-013, RF-023, RF-024) ====================

    /**
     * Agrega un nuevo evento al sistema (RF-013, RF-023).
     *
     * @param e el evento a agregar
     */
    public void crearEvento(Evento e) { eventos.add(e); }

    /**
     * Devuelve todos los eventos del sistema (RF-013).
     *
     * @return lista de todos los eventos
     */
    public List<Evento> listarEventos() { return new ArrayList<>(eventos); }

    /**
     * Devuelve solo los eventos en estado "Publicado" (RF-003).
     *
     * @return lista de eventos disponibles para el usuario
     */
    public List<Evento> getEventosDisponibles() {
        return eventos.stream()
                .filter(e -> "Publicado".equals(e.getEstado()))
                .collect(Collectors.toList());
    }

    /**
     * Filtra eventos por nombre, ciudad y/o categoría (RF-003).
     *
     * @param nombre    fragmento del nombre (puede ser {@code null})
     * @param ciudad    ciudad del evento (puede ser {@code null})
     * @param categoria categoría del evento (puede ser {@code null})
     * @return lista de eventos que cumplen los criterios
     */
    public List<Evento> filtrarEventos(String nombre, String ciudad, String categoria) {
        return eventos.stream()
                .filter(e -> "Publicado".equals(e.getEstado()))
                .filter(e -> nombre    == null || e.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .filter(e -> ciudad    == null || e.getCiudad().toLowerCase().contains(ciudad.toLowerCase()))
                .filter(e -> categoria == null || e.getCategoria().toLowerCase().contains(categoria.toLowerCase()))
                .collect(Collectors.toList());
    }

    // ==================== GESTIÓN DE RECINTOS Y ZONAS (RF-014, RF-026, RF-027) ====================

    /**
     * Agrega un recinto al sistema (RF-014, RF-026).
     *
     * @param r el recinto a agregar
     */
    public void agregarRecinto(Recinto r) { recintos.add(r); }

    /**
     * Devuelve todos los recintos registrados (RF-014, RF-026).
     *
     * @return lista de recintos (copia defensiva)
     */
    public List<Recinto> listarRecintos() { return new ArrayList<>(recintos); }

    // ==================== GESTIÓN DE COMPRAS (RF-034 a RF-037) ====================

    /**
     * Crea una nueva compra usando el Builder y la registra en el sistema (RF-034).
     * También asocia la compra al usuario correspondiente.
     *
     * @param builder el builder con los datos de la compra
     * @return la compra creada
     */
    public Compra crearCompra(CompraBuilder builder) {
        Compra c = builder.build();
        compras.add(c);
        if (c.getUsuario() != null) {
            c.getUsuario().agregarCompra(c);
        }
        notificarTodos("Nueva compra creada: " + c.getIdCompra());
        return c;
    }

    /**
     * Devuelve todas las compras del sistema (RF-016).
     *
     * @return lista de compras
     */
    public List<Compra> listarCompras() { return new ArrayList<>(compras); }

    /**
     * Devuelve el historial de compras de un usuario específico (RF-010).
     *
     * @param idUsuario el identificador del usuario
     * @return lista de compras asociadas al usuario
     */
    public List<Compra> listarComprasDeUsuario(String idUsuario) {
        return compras.stream()
                .filter(c -> c.getUsuario() != null
                          && c.getUsuario().getIdUsuario().equals(idUsuario))
                .collect(Collectors.toList());
    }

    // ==================== MÉTRICAS (RF-018, RF-019) ====================

    /**
     * Genera un resumen de métricas del sistema (RF-018).
     *
     * @return cadena con métricas: ventas, ingresos, tasa de cancelación y top evento
     */
    public String obtenerMetricas() {
        long ventasTotales  = compras.size();
        double ingresos     = compras.stream().mapToDouble(Compra::getTotal).sum();
        long canceladas     = compras.stream()
                                     .filter(c -> "Cancelada".equals(c.getEstado()))
                                     .count();
        double tasaCancel   = ventasTotales > 0 ? (canceladas * 100.0 / ventasTotales) : 0;
        String topEvento    = eventos.isEmpty() ? "N/A" : eventos.get(0).getNombre();

        return String.format(
            "Ventas: %d | Ingresos: $%,.0f | Cancelaciones: %.1f%% | Top evento: %s",
            ventasTotales, ingresos, tasaCancel, topEvento
        );
    }

    /**
     * Calcula los ingresos totales para un evento específico (RF-018).
     *
     * @param idEvento identificador del evento
     * @return total de ingresos del evento (excluyendo compras canceladas)
     */
    public double calcularIngresosPorEvento(String idEvento) {
        return compras.stream()
                .filter(c -> c.getEvento() != null
                          && c.getEvento().getIdEvento().equals(idEvento))
                .filter(c -> !"Cancelada".equals(c.getEstado()))
                .mapToDouble(Compra::getTotal)
                .sum();
    }

    /**
     * Calcula la tasa de ocupación (porcentaje) de una zona en todos los eventos (RF-030).
     *
     * @param nombreZona nombre de la zona
     * @return porcentaje de ocupación entre 0.0 y 100.0
     */
    public double calcularOcupacionZona(String nombreZona) {
        for (Recinto r : recintos) {
            for (Zona z : r.getZonas()) {
                if (z.getNombre().equalsIgnoreCase(nombreZona)) {
                    int cap = z.getCapacidad();
                    if (cap == 0) return 0;
                    return (z.getOcupacion() * 100.0) / cap;
                }
            }
        }
        return 0;
    }

    // ==================== BÚSQUEDAS ====================

    /**
     * Busca un recinto por su identificador único.
     *
     * @param id el identificador del recinto
     * @return el recinto encontrado, o {@code null}
     */
    public Recinto buscarRecintoPorId(String id) {
        return recintos.stream()
                .filter(r -> r.getIdRecinto().equals(id))
                .findFirst().orElse(null);
    }

    /**
     * Busca un evento por su identificador único.
     *
     * @param id el identificador del evento
     * @return el evento encontrado, o {@code null}
     */
    public Evento buscarEventoPorId(String id) {
        return eventos.stream()
                .filter(e -> e.getIdEvento().equals(id))
                .findFirst().orElse(null);
    }

    /**
     * Busca una compra por su identificador único (RF-037).
     *
     * @param id el identificador de la compra
     * @return la compra encontrada, o {@code null}
     */
    public Compra buscarCompraPorId(String id) {
        return compras.stream()
                .filter(c -> c.getIdCompra().equals(id))
                .findFirst().orElse(null);
    }

    // ==================== INICIALIZACIÓN DATOS DE PRUEBA (RF-045) ====================

    /**
     * Inicializa datos de prueba: usuarios, recintos, zonas, asientos, eventos y compras.
     * Requerido por RF-045 (datos de prueba inicializados).
     */
    private void inicializarDatosPrueba() {
        // --- Usuarios (RF-020) ---
        Usuario userNormal = new Usuario("U001", "Jhon Deivid García",      "usuario@ejemplo.com", "1234",  "3001234567");
        Usuario admin      = new Usuario("A001", "Administrador Sistema",    "admin@ejemplo.com",   "admin", "3000000000");
        Usuario user2      = new Usuario("U002", "María López",              "maria@ejemplo.com",   "1234",  "3109876543");
        crearUsuario(userNormal);
        crearUsuario(admin);
        crearUsuario(user2);

        // --- Recintos y Zonas (RF-026, RF-028, RF-029, RF-031) ---
        Recinto estadio = new Recinto("R001", "Estadio Centenario", "Calle 19 # 14-60", "Armenia");
        Zona vip          = new Zona("Z001", "VIP",          36, 250000);
        Zona preferencial = new Zona("Z002", "Preferencial", 48, 150000);
        Zona general      = new Zona("Z003", "General",      72,  80000);
        estadio.agregarZona(vip);
        estadio.agregarZona(preferencial);
        estadio.agregarZona(general);
        agregarRecinto(estadio);

        Recinto teatro = new Recinto("R002", "Teatro Quimbaya", "Cra 14 # 18-41", "Armenia");
        Zona platino  = new Zona("Z004", "Platino",  30, 180000);
        Zona standard = new Zona("Z005", "Standard", 60,  90000);
        teatro.agregarZona(platino);
        teatro.agregarZona(standard);
        agregarRecinto(teatro);

        Recinto convCenter = new Recinto("R003", "Centro de Convenciones", "Av. Bolívar # 1-10", "Bogotá");
        Zona sala1 = new Zona("Z006", "Principal", 200, 120000);
        Zona sala2 = new Zona("Z007", "VIP-Conf",   30, 300000);
        convCenter.agregarZona(sala1);
        convCenter.agregarZona(sala2);
        agregarRecinto(convCenter);

        // --- Eventos usando EventoFactoryMethod (RF-049) ---
        Evento concierto = EventoFactoryMethod.crearEvento("concierto",   "Concierto Juanes",
                "Música",     "Armenia", "2026-05-15 20:00");
        Evento obra      = EventoFactoryMethod.crearEvento("teatro",      "La Dama de las Camelias",
                "Teatro",     "Armenia", "2026-06-01 19:30");
        Evento conf      = EventoFactoryMethod.crearEvento("conferencia", "TechConf 2026",
                "Tecnología", "Bogotá",  "2026-06-20 09:00");

        concierto.setPoliticasCancelacion("Cancelación gratuita hasta 48h antes del evento.");
        obra.setPoliticasCancelacion("Sin reembolso después de la compra.");
        conf.setPoliticasCancelacion("Reembolso del 80% hasta 7 días antes.");

        crearEvento(concierto);
        crearEvento(obra);
        crearEvento(conf);

        concierto.publicar();
        obra.publicar();
        conf.publicar();

        // --- Compras de prueba (RF-034, RF-038) ---
        Compra compra1 = crearCompra(new CompraBuilder()
                .setUsuario(userNormal)
                .setEvento(concierto)
                .setTotal(250000));
        compra1.pagar();

        Compra compra2 = crearCompra(new CompraBuilder()
                .setUsuario(user2)
                .setEvento(obra)
                .setTotal(180000));

        // Demostrar el Decorator (RF-050): agregar servicio VIP a compra2
        ServicioAdicionalDecorator vipDec = new ServicioVIPDecorator(compra2);
        compra2.setServicioDecorator(vipDec);

        // Reservar asientos de prueba (RF-015, RF-031)
        vip.reservarAsiento("A", 1);
        vip.reservarAsiento("A", 2);
        preferencial.reservarAsiento("A", 1);

        // Registrar incidencia de prueba (RF-041)
        registrarIncidencia(new Incidencia("PAGO_FALLIDO",
                "Intento de pago rechazado para compra " + compra1.getIdCompra()));

        System.out.println("✅ Datos de prueba cargados: "
            + usuarios.size() + " usuarios, " + eventos.size() + " eventos, "
            + recintos.size() + " recintos, " + compras.size() + " compras.");
    }

    // ==================== GETTERS ====================

    /** @return referencia directa a la lista interna de usuarios (solo para Controllers) */
    public List<Usuario> getUsuarios()    { return usuarios;    }
    /** @return referencia directa a la lista interna de eventos */
    public List<Evento>  getEventos()     { return eventos;     }
    /** @return referencia directa a la lista interna de recintos */
    public List<Recinto> getRecintos()    { return recintos;    }
    /** @return referencia directa a la lista interna de compras */
    public List<Compra>  getCompras()     { return compras;     }
    /** @return lista de observadores registrados */
    public List<ObserverNotificacion> getObservers() { return observers; }

    /** Solo para pruebas unitarias — resetea la instancia. */
    public static void resetInstancia() { instancia = null; }
}
