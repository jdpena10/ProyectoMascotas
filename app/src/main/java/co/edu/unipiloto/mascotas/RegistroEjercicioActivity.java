package co.edu.unipiloto.mascotas;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Spinner;
import java.util.List;
import java.util.ArrayList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class RegistroEjercicioActivity extends AppCompatActivity {

    private TextView txtDistancia, txtDuracion, txtCalorias;
    private Button btnIniciar, btnPausar, btnFinalizar;

    private boolean enEjecucion = false;
    private double distancia = 0.0;
    private int duracionSegundos = 0;
    private int calorias = 0;

    private Handler handler = new Handler();
    private Runnable simulador;

    private RecyclerView recyclerViewMascotas;
    private MascotaAdapter mascotaAdapter;

    private SQLiteHelper dbHelper;
    private List<Mascota> listaMascotas = new ArrayList<>();

    private Mascota mascotaSeleccionada; // Nueva variable para la mascota seleccionada

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_ejercicio);

        txtDistancia = findViewById(R.id.txt_distancia);
        txtDuracion = findViewById(R.id.txt_duracion);
        txtCalorias = findViewById(R.id.txt_calorias);

        btnIniciar = findViewById(R.id.btn_iniciar);
        btnPausar = findViewById(R.id.btn_pausar);
        btnFinalizar = findViewById(R.id.btn_finalizar);

        recyclerViewMascotas = findViewById(R.id.recycler_mascotas);
        recyclerViewMascotas.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new SQLiteHelper(this);

        // Inicializar adaptador con listener
        mascotaAdapter = new MascotaAdapter(listaMascotas, dbHelper, new MascotaAdapter.OnMascotaClickListener() {
            @Override
            public void onMascotaClick(Mascota mascota) {
                mascotaSeleccionada = mascota;
                Toast.makeText(RegistroEjercicioActivity.this, "Seleccionaste a " + mascota.getNombre(), Toast.LENGTH_SHORT).show();
            }
        });
        recyclerViewMascotas.setAdapter(mascotaAdapter);

        cargarMascotas();

        simulador = new Runnable() {
            @Override
            public void run() {
                if (enEjecucion) {
                    duracionSegundos++;
                    distancia += 0.01; // cada segundo +0.01 km
                    calorias = (int) (distancia * 1000 * 0.05);

                    txtDuracion.setText((duracionSegundos / 60) + "m");
                    txtDistancia.setText(String.format("%.2fkm", distancia));
                    txtCalorias.setText(calorias + "cal");

                    handler.postDelayed(this, 1000); // cada 1 segundo
                }
            }
        };
    }

    public void iniciarSimulacion(View view) {
        if (!enEjecucion) {
            enEjecucion = true;
            handler.post(simulador);
        }
    }

    public void pausarSimulacion(View view) {
        enEjecucion = false;
    }

    public void finalizarSimulacion(View view) {
        enEjecucion = false;
        handler.removeCallbacks(simulador);

        // Guardar en base de datos
        if (mascotaSeleccionada != null) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

            dbHelper.insertarSesionEjercicio(
                    mascotaSeleccionada.getId(),
                    distancia,
                    duracionSegundos,
                    calorias,
                    timestamp
            );
        } else {
            Toast.makeText(this, "Debes seleccionar una mascota para guardar el ejercicio.", Toast.LENGTH_SHORT).show();
            return; // No continuar si no hay mascota seleccionada
        }

        // Pasar a ResumenActivity
        Intent resumenIntent = new Intent(this, ResumenActivity.class);
        resumenIntent.putExtra("distancia", distancia);
        resumenIntent.putExtra("duracion", duracionSegundos);
        resumenIntent.putExtra("calorias", calorias);
        resumenIntent.putExtra("mascota_id", mascotaSeleccionada.getId());

        startActivity(resumenIntent);
        finish();
    }


    private void cargarMascotas() {
        int userId = obtenerUserId();
        List<Mascota> listaMascotas = dbHelper.obtenerMascotas(userId);
        Log.d("Mascotas", "Número de mascotas obtenidas: " + listaMascotas.size());

        if (listaMascotas.isEmpty()) {
            Log.d("Mascotas", "No se encontraron mascotas en la base de datos.");
        }

        mascotaAdapter.actualizarLista(listaMascotas);
    }

    private int obtenerUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("usuario_pref", MODE_PRIVATE);
        return sharedPreferences.getInt("user_id", -1);
    }
}



