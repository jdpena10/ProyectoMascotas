package co.edu.unipiloto.mascotas;

import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://172.23.74.143:5000/"; // URL de tu API
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            // Log para verificar la URL de la instancia de Retrofit
            Log.d("Retrofit URL", retrofit.baseUrl().toString());
        }
        return retrofit;
    }
}


