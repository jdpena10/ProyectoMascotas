package co.edu.unipiloto.mascotas;

public class Veterinaria {
    private int id;
    private String nombreVeterinario;
    private String direccion;

    // Getters y Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNombreVeterinario() {
        return nombreVeterinario;
    }
    public void setNombreVeterinario(String nombreVeterinario) {
        this.nombreVeterinario = nombreVeterinario;
    }

    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}

