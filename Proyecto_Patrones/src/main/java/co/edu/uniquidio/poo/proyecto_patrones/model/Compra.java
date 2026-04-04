package co.edu.uniquidio.poo.proyecto_patrones.model;


import java.util.ArrayList;
import java.util.List;

public class Compra {

    private String idCompra;
    private Usuario usuario;           // ← Necesario para getUsuario()
    private Evento evento;             // ← Necesario para getEvento()
    private double total;
    private String estado = "Creada";
    private List<Entrada> entradas = new ArrayList<>();
    private ServicioAdicional servicioDecorator; // Decorator (RF-009)

    private EstadoCompra estadoActual; // State pattern (RF-051)

    // Constructor privado (usado por Builder - Patrón Creacional)
    private Compra(CompraBuilder builder) {
        this.idCompra = builder.idCompra;
        this.usuario = builder.usuario;
        this.evento = builder.evento;
        this.total = builder.total;
        this.servicioDecorator = builder.servicioDecorator;
        this.estadoActual = new EstadoCreada(); // State inicial
    }

    // ==================== GETTERS CORREGIDOS (estos eran los que faltaban) ====================
    public Usuario getUsuario() {          // ← SOLUCIONA EL ERROR "get usuario"
        return usuario;
    }

    public Evento getEvento() {            // ← SOLUCIONA EL ERROR "get evento"
        return evento;
    }

    public String getIdCompra() {
        return idCompra;
    }

    public double getTotal() {             // Usado por VIPDecorator
        return (servicioDecorator != null) ? servicioDecorator.getPrecio() : total;
    }

    public String getDescripcion() {       // Usado por VIPDecorator
        return (servicioDecorator != null)
                ? servicioDecorator.getDescripcion()
                : "Compra de entradas para " + (evento != null ? evento.getNombre() : "Evento");
    }

    public String getEstado() {
        return estado;
    }

    public List<Entrada> getEntradas() {
        return new ArrayList<>(entradas);
    }

    // ==================== SETTERS Y MÉTODOS DE STATE ====================
    public void setEstado(String nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public void setEstadoActual(EstadoCompra nuevoEstado) {
        this.estadoActual = nuevoEstado;
    }

    public void pagar() { estadoActual.pagar(this); }
    public void cancelar() { estadoActual.cancelar(this); }
    public void confirmar() { estadoActual.confirmar(this); }

    // ==================== BUILDER (Patrón Creacional RF-049) ====================
    public static class CompraBuilder {
        private String idCompra = "C" + System.currentTimeMillis();
        private Usuario usuario;
        private Evento evento;
        private double total;
        private ServicioAdicional servicioDecorator;

        public CompraBuilder setUsuario(Usuario u) { this.usuario = u; return this; }
        public CompraBuilder setEvento(Evento e) { this.evento = e; return this; }
        public CompraBuilder setTotal(double t) { this.total = t; return this; }
        public CompraBuilder agregarServicio(ServicioAdicional s) { this.servicioDecorator = s; return this; }
        public Compra build() { return new Compra(this); }
    }

    public void setIdCompra(String idCompra) {
        this.idCompra = idCompra;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setEntradas(List<Entrada> entradas) {
        this.entradas = entradas;
    }

    public ServicioAdicional getServicioDecorator() {
        return servicioDecorator;
    }

    public void setServicioDecorator(ServicioAdicional servicioDecorator) {
        this.servicioDecorator = servicioDecorator;
    }

    public EstadoCompra getEstadoActual() {
        return estadoActual;
    }
}