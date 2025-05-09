package co.edu.unipiloto.mascotas;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    // Obtener todas las citas
    /*@GET("/veterinarias")
    Call<List<Cita>> getVeterinarias();*/

    @GET("veterinarias")
    Call<List<Veterinaria>> getVeterinarias();

    // Agendar una cita
    @POST("/agendar")
    Call<Cita> agendarCita(@Body Cita cita);


}

