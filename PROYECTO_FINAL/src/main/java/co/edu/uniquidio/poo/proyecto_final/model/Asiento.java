package co.edu.uniquidio.poo.proyecto_final.model;


public class Asiento implements ComponenteSeating {  // Hoja del Composite
    private String idAsiento;
    private String fila;
    private int numero;
    private String estado;  // Disponible, Reservado, Vendido, Bloqueado (RF-032)
    private Zona zonaPadre;

    public Asiento(String fila, int numero, String estadoInicial, Zona zonaPadre) {
        this.idAsiento = zonaPadre.getNombre() + "-" + fila + numero;
        this.fila = fila;
        this.numero = numero;
        this.estado = estadoInicial;
        this.zonaPadre = zonaPadre;
    }

    // RF-032
    public boolean reservar() {
        if (estado.equals("Disponible")) {
            this.estado = "Reservado";
            return true;
        }
        return false;
    }

    public void cambiarEstado(String nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public String getEstado() { return estado; }
    public String getFila() { return fila; }
    public int getNumero() { return numero; }

    // Composite
    @Override
    public void mostrarMapa() {
        System.out.println("  Asiento " + fila + "-" + numero + " → " + estado);
    }

    @Override
    public int getCapacidadDisponible() {
        return estado.equals("Disponible") ? 1 : 0;
    }

    @Override
    public boolean reservarAsiento(String fila, int numero) {
        return this.fila.equals(fila) && this.numero == numero && reservar();
    }

    public String getIdAsiento() {
        return idAsiento;
    }

    public void setIdAsiento(String idAsiento) {
        this.idAsiento = idAsiento;
    }

    public void setFila(String fila) {
        this.fila = fila;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Zona getZonaPadre() {
        return zonaPadre;
    }

    public void setZonaPadre(Zona zonaPadre) {
        this.zonaPadre = zonaPadre;
    }
}