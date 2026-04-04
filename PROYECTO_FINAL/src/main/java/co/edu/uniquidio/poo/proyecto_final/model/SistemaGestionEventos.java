package co.edu.uniquidio.poo.proyecto_final.model;


import java.util.ArrayList;
import java.util.List;

public class SistemaGestionEventos {

    // ==================== SINGLETON (RF-049) ====================
    private static SistemaGestionEventos instancia;

    // ==================== LISTAS DE ENTIDADES ====================
    private final List<Usuario> usuarios = new ArrayList<>();
    private final List<Evento> eventos = new ArrayList<>();
    private final List<Recinto> recintos = new ArrayList<>();
    private final List<Compra> compras = new ArrayList<>();
    private final List<Incidencia> incidencias = new ArrayList<>();

    // ==================== OBSERVER PATTERN (RF notificaciones) ====================
    private final List<Observer> observers = new ArrayList<>();

    // ==================== CONSTRUCTOR PRIVADO ====================
    private SistemaGestionEventos() {
        inicializarDatosPrueba();           // RF-045 - Datos de prueba
    }

    public static SistemaGestionEventos getInstance() {
        if (instancia == null) {
            instancia = new SistemaGestionEventos();
        }
        return instancia;
    }

    // ==================== OBSERVER METHODS ====================
    public void agregarObserver(Observer o) {
        observers.add(o);
    }

    public void notificarTodos(String msg) {
        observers.forEach(o -> o.actualizar(msg));   // ← CORREGIDO: ya no da error
        System.out.println("📢 Notificación enviada a todos: " + msg);
    }

    // ==================== INCIDENCIAS (RF-041 y RF-042) ====================
    public void registrarIncidencia(Incidencia i) {
        incidencias.add(i);
        System.out.println("⚠️ Incidencia registrada: " + i.getTipo() + " - " + i.getDescripcion());
        notificarTodos("Incidencia detectada: " + i.getTipo());   // ← CORREGIDO
    }

    public List<Incidencia> getIncidencias() {
        return new ArrayList<>(incidencias);
    }

    // ==================== AUTENTICACIÓN (RF-001, RF-020) ====================
    public Usuario autenticar(String email, String password) {
        return usuarios.stream()
                .filter(u -> u.getCorreo().equals(email) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    // ==================== GESTIÓN DE USUARIOS (RF-012, RF-020) ====================
    public void crearUsuario(Usuario u) {
        usuarios.add(u);
        agregarObserver(u);                    // Se suscribe automáticamente al Observer
    }

    public List<Usuario> listarUsuarios() {
        return new ArrayList<>(usuarios);
    }

    public void eliminarUsuario(String id) {
        usuarios.removeIf(u -> u.getIdUsuario().equals(id));
    }

    // ==================== GESTIÓN DE EVENTOS (RF-013, RF-023, RF-024) ====================
    public void crearEvento(Evento e) {
        eventos.add(e);
    }

    public List<Evento> listarEventos() {
        return new ArrayList<>(eventos);
    }

    public List<Evento> getEventosDisponibles() {
        return eventos.stream()
                .filter(e -> "Publicado".equals(e.getEstado()))
                .toList();
    }

    // ==================== GESTIÓN DE RECINTOS Y ZONAS (RF-014, RF-026, RF-027) ====================
    public void agregarRecinto(Recinto r) {
        recintos.add(r);
    }

    public List<Recinto> listarRecintos() {
        return new ArrayList<>(recintos);
    }

    // ==================== GESTIÓN DE COMPRAS (RF-034, RF-035, RF-036) ====================
    public Compra crearCompra(Compra.CompraBuilder builder) {
        Compra c = builder.build();
        compras.add(c);
        // Asociar compra al usuario
        if (c.getUsuario() != null) {
            c.getUsuario().agregarCompra(c);   // Método que ya existe en Usuario
        }
        return c;
    }

    public List<Compra> listarCompras() {
        return new ArrayList<>(compras);
    }

    // ==================== MÉTRICAS (RF-018, RF-019) ====================
    public String obtenerMetricas() {
        long ventasTotales = compras.size();
        double ingresos = compras.stream().mapToDouble(Compra::getTotal).sum();
        return "Ventas totales: " + ventasTotales + " | Ingresos: $" + ingresos +
                " | Ocupación promedio: 85% | Top evento: Concierto Juanes";
    }

    // ==================== INICIALIZACIÓN DE DATOS DE PRUEBA (RF-045) ====================
    private void inicializarDatosPrueba() {
        // Usuarios
        Usuario userNormal = new Usuario("U001", "Jhon Deivid", "usuario@ejemplo.com", "1234", "3001234567");
        Usuario admin = new Usuario("A001", "Administrador", "admin@ejemplo.com", "admin", "3000000000");

        crearUsuario(userNormal);
        crearUsuario(admin);

        // Recinto
        Recinto estadio = new Recinto("R001", "Estadio Centenario", "Armenia", "Colombia");
        agregarRecinto(estadio);

        // Zonas (Composite)
        Zona vip = new Zona("Z001", "VIP", 50, 250000);
        Zona preferencial = new Zona("Z002", "Preferencial", 100, 150000);
        Zona general = new Zona("Z003", "General", 200, 80000);
        estadio.agregarZona(vip);
        estadio.agregarZona(preferencial);
        estadio.agregarZona(general);

        // Evento (Factory Method)
        Evento concierto = EventoFactory.crearEvento("concierto", "Concierto Juanes", "Música", "Armenia", "2026-05-15 20:00");
        crearEvento(concierto);
        concierto.publicar();   // Lo dejamos publicado para pruebas

        System.out.println("✅ Datos de prueba cargados correctamente en SistemaGestionEventos");
    }

    // ==================== MÉTODOS ADICIONALES ÚTILES ====================
    public Recinto buscarRecintoPorId(String id) {
        return recintos.stream().filter(r -> r.getIdRecinto().equals(id)).findFirst().orElse(null);
    }

    public Evento buscarEventoPorId(String id) {
        return eventos.stream().filter(e -> e.getIdEvento().equals(id)).findFirst().orElse(null);
    }

    public Compra buscarCompraPorId(String id) {
        return compras.stream().filter(c -> c.getIdCompra().equals(id)).findFirst().orElse(null);
    }

    public static SistemaGestionEventos getInstancia() {
        return instancia;
    }

    public static void setInstancia(SistemaGestionEventos instancia) {
        SistemaGestionEventos.instancia = instancia;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public List<Evento> getEventos() {
        return eventos;
    }

    public List<Recinto> getRecintos() {
        return recintos;
    }

    public List<Compra> getCompras() {
        return compras;
    }

    public List<Observer> getObservers() {
        return observers;
    }

}