package co.edu.unipiloto.mascotas;

import android.content.Context;
import android.os.SystemClock;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExerciseSessionManager {

    private long startTimeMillis;
    private double distanceKm;
    private int petId;
    private Context context;

    public ExerciseSessionManager(Context context, int petId) {
        this.context = context;
        this.petId = petId;
    }

    public void iniciarSesion() {
        startTimeMillis = SystemClock.elapsedRealtime();
        distanceKm = 0.0;
    }

    public void registrarDistancia(double km) {
        distanceKm += km;
    }

    public void finalizarSesion() {
        long endTimeMillis = SystemClock.elapsedRealtime();
        int durationMinutes = (int) ((endTimeMillis - startTimeMillis) / 1000 / 60);
        int calories = (int) (distanceKm * 1000 * 0.05);

        String timestamp = obtenerTimestampActual();

        SQLiteHelper dbHelper = new SQLiteHelper(context);
        dbHelper.insertarSesionEjercicio(petId, distanceKm, durationMinutes, calories, timestamp);
    }

    private String obtenerTimestampActual() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}

