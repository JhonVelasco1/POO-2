package co.edu.uniquidio.poo.proyecto_patrones.model;

// model/EventoFactory.java (FACTORY METHOD - Creacional)


public class EventoFactory {
    // Factory Method - Crea el tipo correcto según RF-013
    public static Evento crearEvento(String tipo, String nombre, String categoria, String ciudad, String fechaHora) {
        Recinto recintoPorDefecto = new Recinto("R001", "Estadio Centenario", "Armenia", "Colombia");

        return switch (tipo.toLowerCase()) {
            case "concierto" -> new Concierto("E" + System.currentTimeMillis(), nombre, categoria, "Concierto de " + nombre, ciudad, fechaHora, recintoPorDefecto);
            case "teatro" -> new Teatro("E" + System.currentTimeMillis(), nombre, categoria, "Obra de teatro " + nombre, ciudad, fechaHora, recintoPorDefecto);
            case "conferencia" -> new Conferencia("E" + System.currentTimeMillis(), nombre, categoria, "Conferencia " + nombre, ciudad, fechaHora, recintoPorDefecto);
            default -> throw new IllegalArgumentException("Tipo de evento desconocido: " + tipo);
        };
    }
}