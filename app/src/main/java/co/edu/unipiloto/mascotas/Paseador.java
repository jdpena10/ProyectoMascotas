package co.edu.unipiloto.mascotas;

public class Paseador {
    private String nombre;
    private float calificacion;
    private String imagenUrl;
    private double latitud;
    private double longitud;
    private String direccion;  // Nuevo campo para dirección
    private String celular;    // Nuevo campo para celular

    // Constructor actualizado
    public Paseador(String nombre, float calificacion, String imagenUrl, double latitud, double longitud, String direccion, String celular) {
        this.nombre = nombre;
        this.calificacion = calificacion;
        this.imagenUrl = imagenUrl;
        this.latitud = latitud;
        this.longitud = longitud;
        this.direccion = direccion;
        this.celular = celular;
    }

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(float calificacion) {
        this.calificacion = calificacion;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }
}


