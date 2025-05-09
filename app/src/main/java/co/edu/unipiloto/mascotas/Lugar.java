package co.edu.unipiloto.mascotas;

public class Lugar {
    private String nombre;
    private String direccion;
    private String placeId;
    private double latitud;
    private double longitud;

    // Constructor
    public Lugar(String nombre, String direccion, String placeId, double latitud, double longitud) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.placeId = placeId;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
}


