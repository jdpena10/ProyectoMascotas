package co.edu.unipiloto.mascotas;

public class Cita {
    public String nombreVeterinario;
    public String direccion;
    public String fecha;
    public String hora;
    public String motivo;
    public String nombreMascota;

    public Cita(String nombreVeterinario, String direccion, String fecha, String hora, String motivo, String nombreMascota) {
        this.nombreVeterinario = nombreVeterinario;
        this.direccion = direccion;
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
        this.nombreMascota = nombreMascota;
    }

    // Constructor sin parámetros
    public Cita() {
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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getNombreMascota() {
        return nombreMascota;
    }

    public void setNombreMascota(String nombreMascota) {
        this.nombreMascota = nombreMascota;
    }
}

