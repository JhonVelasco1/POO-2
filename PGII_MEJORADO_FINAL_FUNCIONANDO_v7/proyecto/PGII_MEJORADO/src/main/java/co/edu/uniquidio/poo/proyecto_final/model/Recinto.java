package co.edu.uniquidio.poo.proyecto_final.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Recinto — lugar físico donde ocurre el evento (RF-026, RF-027).
 * Actúa como raíz de la jerarquía Composite: Recinto → Zona → Asiento.
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public class Recinto {

    private String idRecinto;
    private String nombre;
    private String direccion;
    private String ciudad;
    /** RF-027: Zonas del recinto (Composición). */
    private List<Zona> zonas = new ArrayList<>();

    /**
     * Construye un recinto con sus datos básicos.
     *
     * @param idRecinto identificador único
     * @param nombre    nombre del recinto
     * @param direccion dirección física
     * @param ciudad    ciudad
     */
    public Recinto(String idRecinto, String nombre, String direccion, String ciudad) {
        this.idRecinto = idRecinto;
        this.nombre    = nombre;
        this.direccion = direccion;
        this.ciudad    = ciudad;
    }

    /** RF-027: Agrega una zona al recinto. */
    public void agregarZona(Zona zona)      { zonas.add(zona); }

    /** RF-027: Elimina una zona por ID. */
    public void eliminarZona(String idZona) { zonas.removeIf(z -> z.getIdZona().equals(idZona)); }

    /** @return lista de zonas del recinto */
    public List<Zona> getZonas()            { return zonas; }
    /** @return identificador único */
    public String getIdRecinto()            { return idRecinto; }
    /** @return nombre del recinto */
    public String getNombre()               { return nombre; }
    /** @return dirección física */
    public String getDireccion()            { return direccion; }
    /** @return ciudad */
    public String getCiudad()               { return ciudad; }

    public void setIdRecinto(String id)     { this.idRecinto = id; }
    public void setNombre(String n)         { this.nombre = n; }
    public void setDireccion(String d)      { this.direccion = d; }
    public void setCiudad(String c)         { this.ciudad = c; }
    public void setZonas(List<Zona> z)      { this.zonas = z; }

    @Override
    public String toString() { return nombre + " - " + ciudad; }
}
