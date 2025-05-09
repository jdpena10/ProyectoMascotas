package co.edu.unipiloto.mascotas;

public class SesionEjercicio {
    private int id;
    private int petId;
    private double distancia;
    private int duracion;
    private int calorias;
    private String timestamp;

    // Constructor opcional si lo necesitas
    public SesionEjercicio() {
        // Constructor vacío necesario para usar new SesionEjercicio() sin parámetros
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getPetId() {
        return petId;
    }

    public double getDistancia() {
        return distancia;
    }

    public int getDuracion() {
        return duracion;
    }

    public int getCalorias() {
        return calorias;
    }

    public String getTimestamp() {
        return timestamp;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public void setCalorias(int calorias) {
        this.calorias = calorias;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}


