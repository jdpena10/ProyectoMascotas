package co.edu.unipiloto.mascotas;

public class Mascota {
    private int id;
    private String nombre;
    private String tipo;
    private String raza;
    private int edad;
    private int userId;
    private double latitud;
    private double longitud;

    // Constructor
    public Mascota(int id, String nombre, String tipo, String raza, int edad, int userId, double latitud, double longitud) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.raza = raza;
        this.edad = edad;
        this.userId = userId;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

