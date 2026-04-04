package co.edu.uniquidio.poo.proyecto_final.model;


import java.util.ArrayList;
import java.util.List;

public class Recinto {
    private String idRecinto;
    private String nombre;
    private String direccion;
    private String ciudad;
    private List<Zona> zonas = new ArrayList<>();  // Composición (patrón Composite)

    public Recinto(String idRecinto, String nombre, String direccion, String ciudad) {
        this.idRecinto = idRecinto;
        this.nombre = nombre;
        this.direccion = direccion;
        this.ciudad = ciudad;
    }
    public String getIdRecinto() { return idRecinto; }

    // RF-026, RF-027
    public void agregarZona(Zona zona) { zonas.add(zona); }
    public List<Zona> getZonas() { return zonas; }
    public String getNombre() { return nombre; }

    public void setIdRecinto(String idRecinto) {
        this.idRecinto = idRecinto;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setZonas(List<Zona> zonas) {
        this.zonas = zonas;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}