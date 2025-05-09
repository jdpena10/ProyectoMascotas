package co.edu.unipiloto.mascotas;

public class Veterinario {
    private String nombre;
    private String direccion;
    private double rating;
    private String placeId;
    private double latitude;
    private double longitude;
    private String website;

    public Veterinario(String nombre, String direccion, double rating, String placeId, double latitude, double longitude, String website) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.rating = rating;
        this.placeId = placeId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.website = website;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public double getRating() {
        return rating;
    }

    public String getPlaceId() {
        return placeId;
    }

    public double getLatitud() {
        return latitude;
    }

    public double getLongitud() {
        return longitude;
    }

    public String getWebsite() { // Add the getter method for website
        return website;
    }
}
