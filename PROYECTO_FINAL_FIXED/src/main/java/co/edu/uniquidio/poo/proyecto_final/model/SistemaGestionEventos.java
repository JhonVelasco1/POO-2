package co.edu.uniquidio.poo.proyecto_final.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SistemaGestionEventos {

    // ==================== SINGLETON (RF-049) ====================
    private static SistemaGestionEventos instancia;

    // ==================== LISTAS DE ENTIDADES ====================
    private final List<Usuario>   usuarios   = new ArrayList<>();
    private final List<Evento>    eventos    = new ArrayList<>();
    private final List<Recinto>   recintos   = new ArrayList<>();
    private final List<Compra>    compras    = new ArrayList<>();
    private final List<Incidencia> incidencias = new ArrayList<>();

    // ==================== USUARIO LOGUEADO ====================
    private Usuario usuarioLogueado;

    // ==================== OBSERVER PATTERN ====================
    private final List<Observer> observers = new ArrayList<>();

    // ==================== CONSTRUCTOR PRIVADO ====================
    private SistemaGestionEventos() {
        inicializarDatosPrueba();
    }

    public static SistemaGestionEventos getInstance() {
        if (instancia == null) {
            instancia = new SistemaGestionEventos();
        }
        return instancia;
    }

    // ==================== OBSERVER METHODS ====================
    public void agregarObserver(Observer o) { observers.add(o); }

    public void notificarTodos(String msg) {
        observers.forEach(o -> o.actualizar(msg));
        System.out.println("📢 Notificación: " + msg);
    }

    // ==================== INCIDENCIAS (RF-041, RF-042) ====================
    public void registrarIncidencia(Incidencia i) {
        incidencias.add(i);
        System.out.println("⚠️ Incidencia: " + i.getTipo() + " - " + i.getDescripcion());
        notificarTodos("Incidencia detectada: " + i.getTipo());
    }

    public List<Incidencia> getIncidencias() { return new ArrayList<>(incidencias); }

    // ==================== AUTENTICACIÓN (RF-001, RF-020) ====================
    public Usuario autenticar(String email, String password) {
        Usuario u = usuarios.stream()
                .filter(us -> us.getCorreo().equalsIgnoreCase(email)
                           && us.getPassword().equals(password))
                .findFirst().orElse(null);
        if (u != null) this.usuarioLogueado = u;
        return u;
    }

    public Usuario getUsuarioLogueado() { return usuarioLogueado; }
    public void setUsuarioLogueado(Usuario u) { this.usuarioLogueado = u; }

    // ==================== GESTIÓN DE USUARIOS (RF-012, RF-020) ====================
    public void crearUsuario(Usuario u) {
        usuarios.add(u);
        agregarObserver(u);
    }

    public List<Usuario> listarUsuarios() { return new ArrayList<>(usuarios); }

    public void eliminarUsuario(String id) {
        usuarios.removeIf(u -> u.getIdUsuario().equals(id));
    }

    // ==================== GESTIÓN DE EVENTOS (RF-013, RF-023, RF-024) ====================
    public void crearEvento(Evento e) { eventos.add(e); }

    public List<Evento> listarEventos() { return new ArrayList<>(eventos); }

    public List<Evento> getEventosDisponibles() {
        return eventos.stream()
                .filter(e -> "Publicado".equals(e.getEstado()))
                .collect(Collectors.toList());
    }

    // RF-003: Filtrar eventos por criterios
    public List<Evento> filtrarEventos(String nombre, String ciudad, String categoria) {
        return eventos.stream()
                .filter(e -> "Publicado".equals(e.getEstado()))
                .filter(e -> nombre == null || e.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .filter(e -> ciudad == null || e.getCiudad().toLowerCase().contains(ciudad.toLowerCase()))
                .filter(e -> categoria == null || e.getCategoria().toLowerCase().contains(categoria.toLowerCase()))
                .collect(Collectors.toList());
    }

    // ==================== GESTIÓN DE RECINTOS Y ZONAS (RF-014, RF-026, RF-027) ====================
    public void agregarRecinto(Recinto r) { recintos.add(r); }
    public List<Recinto> listarRecintos() { return new ArrayList<>(recintos); }

    // ==================== GESTIÓN DE COMPRAS (RF-034 a RF-037) ====================
    public Compra crearCompra(Compra.CompraBuilder builder) {
        Compra c = builder.build();
        compras.add(c);
        if (c.getUsuario() != null) {
            c.getUsuario().agregarCompra(c);
        }
        notificarTodos("Nueva compra creada: " + c.getIdCompra());
        return c;
    }

    public List<Compra> listarCompras() { return new ArrayList<>(compras); }

    // RF-010: Historial de compras del usuario logueado
    public List<Compra> listarComprasDeUsuario(String idUsuario) {
        return compras.stream()
                .filter(c -> c.getUsuario() != null
                          && c.getUsuario().getIdUsuario().equals(idUsuario))
                .collect(Collectors.toList());
    }

    // ==================== MÉTRICAS (RF-018, RF-019) ====================
    public String obtenerMetricas() {
        long ventasTotales = compras.size();
        double ingresos = compras.stream().mapToDouble(Compra::getTotal).sum();
        long canceladas = compras.stream().filter(c -> "Cancelada".equals(c.getEstado())).count();
        double tasaCancelacion = ventasTotales > 0 ? (canceladas * 100.0 / ventasTotales) : 0;
        return String.format(
            "Ventas: %d | Ingresos: $%,.0f | Cancelaciones: %.1f%% | Top evento: %s",
            ventasTotales, ingresos, tasaCancelacion,
            eventos.isEmpty() ? "N/A" : eventos.get(0).getNombre()
        );
    }

    // RF-018: Ingresos por periodo (simulado)
    public double calcularIngresosPorEvento(String idEvento) {
        return compras.stream()
                .filter(c -> c.getEvento() != null && c.getEvento().getIdEvento().equals(idEvento))
                .filter(c -> !"Cancelada".equals(c.getEstado()))
                .mapToDouble(Compra::getTotal)
                .sum();
    }

    // ==================== INICIALIZACIÓN DE DATOS DE PRUEBA (RF-045) ====================
    private void inicializarDatosPrueba() {
        // --- Usuarios ---
        Usuario userNormal = new Usuario("U001", "Jhon Deivid García", "usuario@ejemplo.com", "1234", "3001234567");
        Usuario admin      = new Usuario("A001", "Administrador Sistema", "admin@ejemplo.com", "admin", "3000000000");
        Usuario user2      = new Usuario("U002", "María López", "maria@ejemplo.com", "1234", "3109876543");
        crearUsuario(userNormal);
        crearUsuario(admin);
        crearUsuario(user2);

        // --- Recintos y Zonas ---
        Recinto estadio = new Recinto("R001", "Estadio Centenario", "Calle 19 # 14-60", "Armenia");
        Zona vip          = new Zona("Z001", "VIP",         36, 250000);
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

        // --- Eventos (usando Factory) ---
        Evento concierto = EventoFactory.crearEvento("concierto",   "Concierto Juanes",    "Música",     "Armenia", "2026-05-15 20:00");
        Evento obra      = EventoFactory.crearEvento("teatro",      "La Dama de las Camelias", "Teatro", "Armenia", "2026-06-01 19:30");
        Evento conf      = EventoFactory.crearEvento("conferencia", "TechConf 2026",        "Tecnología", "Bogotá",  "2026-06-20 09:00");

        crearEvento(concierto);
        crearEvento(obra);
        crearEvento(conf);

        concierto.publicar();
        obra.publicar();
        conf.publicar();

        // --- Compra de prueba para userNormal ---
        Compra compra1 = crearCompra(new Compra.CompraBuilder()
                .setUsuario(userNormal)
                .setEvento(concierto)
                .setTotal(250000));
        compra1.pagar();  // Estado → Pagada

        // Reservar un asiento de prueba en zona VIP
        vip.reservarAsiento("A1", 1);
        vip.reservarAsiento("A1", 2);
        vip.reservarAsiento("A2", 1);

        System.out.println("✅ Datos de prueba cargados: " +
            usuarios.size() + " usuarios, " + eventos.size() + " eventos, " +
            recintos.size() + " recintos, " + compras.size() + " compras.");
    }

    // ==================== BÚSQUEDAS ====================
    public Recinto buscarRecintoPorId(String id) {
        return recintos.stream().filter(r -> r.getIdRecinto().equals(id)).findFirst().orElse(null);
    }

    public Evento buscarEventoPorId(String id) {
        return eventos.stream().filter(e -> e.getIdEvento().equals(id)).findFirst().orElse(null);
    }

    public Compra buscarCompraPorId(String id) {
        return compras.stream().filter(c -> c.getIdCompra().equals(id)).findFirst().orElse(null);
    }

    // ==================== GETTERS ====================
    public List<Usuario>   getUsuarios()  { return usuarios; }
    public List<Evento>    getEventos()   { return eventos; }
    public List<Recinto>   getRecintos()  { return recintos; }
    public List<Compra>    getCompras()   { return compras; }
    public List<Observer>  getObservers() { return observers; }

    public static SistemaGestionEventos getInstancia() { return instancia; }
    public static void setInstancia(SistemaGestionEventos i) { instancia = i; }
}
