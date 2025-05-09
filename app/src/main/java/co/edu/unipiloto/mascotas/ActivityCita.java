package co.edu.unipiloto.mascotas;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityCita extends AppCompatActivity {

    private EditText nombreVeterinario, direccion, fecha, hora, motivo, nombreMascota;
    private Button btnAgendar;
    private Spinner spinnerVeterinarias;
    private List<Veterinaria> listaVeterinarias = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cita);

        nombreVeterinario = findViewById(R.id.nombreVeterinario);
        direccion = findViewById(R.id.direccion);
        fecha = findViewById(R.id.fecha);
        hora = findViewById(R.id.hora);
        motivo = findViewById(R.id.motivo);
        nombreMascota = findViewById(R.id.nombreMascota);
        btnAgendar = findViewById(R.id.btnAgendar);
        spinnerVeterinarias = findViewById(R.id.spinnerVeterinarias);

        cargarVeterinarias();

        btnAgendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agendarCita();
            }
        });
    }

    private void agendarCita() {
        String nombreMascotaIngresado = nombreMascota.getText().toString();
        int userId = obtenerUserId();

        if (userId == -1) {
            Toast.makeText(ActivityCita.this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Mascota> mascotas = obtenerMascotas(userId);

        boolean mascotaExistente = false;
        for (Mascota mascota : mascotas) {
            if (mascota.getNombre().equalsIgnoreCase(nombreMascotaIngresado)) {
                mascotaExistente = true;
                break;
            }
        }

        if (!mascotaExistente) {
            Toast.makeText(ActivityCita.this, "No se encontró la mascota", Toast.LENGTH_SHORT).show();
            return;
        }

        Cita cita = new Cita();
        cita.setNombreVeterinario(nombreVeterinario.getText().toString());
        cita.setDireccion(direccion.getText().toString());
        cita.setFecha(fecha.getText().toString());
        cita.setHora(hora.getText().toString());
        cita.setMotivo(motivo.getText().toString());
        cita.setNombreMascota(nombreMascotaIngresado);

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<Cita> call = apiService.agendarCita(cita);

        call.enqueue(new Callback<Cita>() {
            @Override
            public void onResponse(Call<Cita> call, Response<Cita> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ActivityCita.this, "Cita agendada con éxito", Toast.LENGTH_SHORT).show();

                    // Enviar correo de confirmación
                    SQLiteHelper dbHelper = new SQLiteHelper(ActivityCita.this);
                    int userId = obtenerUserId();
                    if (userId != -1) {
                        String userEmail = dbHelper.getUserEmailByUserId(userId);
                        if (userEmail != null && !userEmail.isEmpty()) {
                            new Thread(() -> {
                                String asunto = "Confirmación de Cita Veterinaria";
                                String mensaje = "¡Hola!\n\nTu cita para la mascota '" + nombreMascotaIngresado +
                                        "' ha sido agendada exitosamente.\n\n" +
                                        "Detalles:\n" +
                                        "Veterinario: " + cita.getNombreVeterinario() + "\n" +
                                        "Dirección: " + cita.getDireccion() + "\n" +
                                        "Fecha: " + cita.getFecha() + "\n" +
                                        "Hora: " + cita.getHora() + "\n" +
                                        "Motivo: " + cita.getMotivo() + "\n\n" +
                                        "¡Gracias por confiar en nosotros!";
                                EmailSender.sendEmail(userEmail, asunto, mensaje);
                            }).start();
                        } else {
                            Log.e("ActivityCita", "No se encontró el correo para userId: " + userId);
                        }
                    } else {
                        Log.e("ActivityCita", "No se encontró el user_id en SharedPreferences");
                    }

                } else {
                    Log.e("API_ERROR", "Error Code: " + response.code());
                    try {
                        Log.e("API_ERROR", "Error Body: " + response.errorBody().string());
                    } catch (IOException e) {
                        Log.e("API_ERROR", "IOException while reading error body: " + e.getMessage());
                    }
                    Toast.makeText(ActivityCita.this, "Error al agendar la cita", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Cita> call, Throwable t) {
                Log.e("API_ERROR", "Connection failed: " + t.getMessage(), t);
                Toast.makeText(ActivityCita.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int obtenerUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("usuario_pref", MODE_PRIVATE);
        return sharedPreferences.getInt("user_id", -1);
    }

    private List<Mascota> obtenerMascotas(int userId) {
        SQLiteHelper dbHelper = new SQLiteHelper(this);
        return dbHelper.obtenerMascotas(userId);
    }

    private void cargarVeterinarias() {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Veterinaria>> call = apiService.getVeterinarias();

        call.enqueue(new Callback<List<Veterinaria>>() {
            @Override
            public void onResponse(Call<List<Veterinaria>> call, Response<List<Veterinaria>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaVeterinarias = response.body();

                    List<String> nombresVeterinarias = new ArrayList<>();
                    for (Veterinaria vet : listaVeterinarias) {
                        nombresVeterinarias.add(vet.getNombreVeterinario() + " - " + vet.getDireccion());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(ActivityCita.this, android.R.layout.simple_spinner_item, nombresVeterinarias);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerVeterinarias.setAdapter(adapter);

                    spinnerVeterinarias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Veterinaria veterinariaSeleccionada = listaVeterinarias.get(position);
                            nombreVeterinario.setText(veterinariaSeleccionada.getNombreVeterinario());
                            direccion.setText(veterinariaSeleccionada.getDireccion());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // No hacer nada
                        }
                    });
                } else {
                    Toast.makeText(ActivityCita.this, "Error al cargar veterinarias", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Veterinaria>> call, Throwable t) {
                Toast.makeText(ActivityCita.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}




