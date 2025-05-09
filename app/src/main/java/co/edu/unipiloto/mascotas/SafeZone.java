package co.edu.unipiloto.mascotas;

public class SafeZone {
    private int petId;
    private double latitude;
    private double longitude;
    private float radius;

    public SafeZone(int petId, double latitude, double longitude, float radius) {
        this.petId = petId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }

    // Getters y Setters
    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}
