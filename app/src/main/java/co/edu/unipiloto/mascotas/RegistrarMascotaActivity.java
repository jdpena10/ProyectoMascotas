package co.edu.unipiloto.mascotas;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.*;
import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import android.content.SharedPreferences;
import android.content.Context;


public class RegistrarMascotaActivity extends AppCompatActivity {

    private EditText etNombre, etTipo, etRaza, etEdad;
    private Button btnRegistrar, btnVolver;
    private SQLiteHelper dbHelper;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private double latitud = 0.0, longitud = 0.0;
    private LocationRequest locationRequest;

    private RecyclerView rvMascotas;
    private MascotaAdapter mascotaAdapter;
    private List<Mascota> listaMascotas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_mascota);

        // Inicializar base de datos primero
        dbHelper = new SQLiteHelper(this);

        // Inicializar vistas
        etNombre = findViewById(R.id.et_pet_name);
        etTipo = findViewById(R.id.et_pet_type);
        etRaza = findViewById(R.id.et_pet_breed);
        etEdad = findViewById(R.id.et_pet_age);
        btnRegistrar = findViewById(R.id.btn_guardar);
        btnVolver = findViewById(R.id.btnVolver);

        // Inicializar RecyclerView
        rvMascotas = findViewById(R.id.rvMascotas);
        rvMascotas.setLayoutManager(new LinearLayoutManager(this));

        // Aquí pasas también dbHelper al adaptador
        mascotaAdapter = new MascotaAdapter(listaMascotas, dbHelper, new MascotaAdapter.OnMascotaClickListener() {
            @Override
            public void onMascotaClick(Mascota mascota) {
                Toast.makeText(RegistrarMascotaActivity.this, "Seleccionaste a " + mascota.getNombre(), Toast.LENGTH_SHORT).show();
            }
        });


        // Cargar las mascotas existentes (después de inicializar dbHelper)
        cargarMascotas();

        // Inicializar ubicación
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Configurar solicitud de ubicación
        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setMinUpdateIntervalMillis(2000)
                .build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    for (Location location : locationResult.getLocations()) {
                        latitud = location.getLatitude();
                        longitud = location.getLongitude();
                    }
                }
            }
        };

        btnRegistrar.setOnClickListener(v -> registrarMascota());
        btnVolver.setOnClickListener(v -> finish());

        obtenerUbicacionEnTiempoReal();
    }



    private void obtenerUbicacionEnTiempoReal() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    private void registrarMascota() {
        // Recuperar el userId de SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("usuario_pref", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", -1);  // -1 es el valor por defecto si no existe el userId

        // Agregar un log para verificar el userId recuperado
        Log.d("RegistrarMascota", "userId recuperado de SharedPreferences: " + userId);

        // Verificar si el userId es válido
        if (userId == -1) {
            Toast.makeText(this, "No se ha encontrado el usuario autenticado", Toast.LENGTH_SHORT).show();
            return;  // Salir de la función si no se encuentra el userId
        }

        // Continuar con el registro de la mascota
        String nombre = etNombre.getText().toString().trim();
        String tipo = etTipo.getText().toString().trim();
        String raza = etRaza.getText().toString().trim();

        if (nombre.isEmpty() || tipo.isEmpty() || raza.isEmpty() || etEdad.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        int edad = Integer.parseInt(etEdad.getText().toString().trim());

        if (latitud == 0.0 && longitud == 0.0) {
            Toast.makeText(this, "Esperando ubicación... Intenta de nuevo en unos segundos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Guardar la mascota con la ubicación actual en SQLite
        long petId = dbHelper.insertPet(nombre, tipo, raza, edad, userId, latitud, longitud);

        if (petId != -1) {
            Toast.makeText(this, "Mascota registrada con éxito", Toast.LENGTH_SHORT).show();

            // Recargar la lista de mascotas
            cargarMascotas();

            // Abrir el mapa con la ubicación de la mascota
            Intent intent = new Intent(RegistrarMascotaActivity.this, MapsActivity.class);
            intent.putExtra("PET_ID", (int) petId);
            startActivity(intent);

            // Detener la actualización de ubicación para no consumir batería innecesariamente
            fusedLocationClient.removeLocationUpdates(locationCallback);
            finish();
        } else {
            Toast.makeText(this, "Error al registrar mascota", Toast.LENGTH_SHORT).show();
        }
    }


    private void cargarMascotas() {
        // Obtener el userId desde SharedPreferences o el contexto de autenticación
        int userId = obtenerUserId(); // Suponiendo que tienes un método que obtiene el userId

        // Pasar el userId a la función obtenerMascotas()
        List<Mascota> listaMascotas = dbHelper.obtenerMascotas(userId);
        Log.d("Mascotas", "Número de mascotas obtenidas: " + listaMascotas.size());

        // Verifica si la lista está vacía
        if (listaMascotas.isEmpty()) {
            Log.d("Mascotas", "No se encontraron mascotas en la base de datos.");
        }

        // Actualiza la lista en el adaptador y notifica los cambios
        mascotaAdapter.actualizarLista(listaMascotas);
        mascotaAdapter.notifyDataSetChanged();  // Asegura que el RecyclerView se actualice
    }

    // Ejemplo de un método que obtiene el userId desde SharedPreferences
    private int obtenerUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("usuario_pref", MODE_PRIVATE);
        return sharedPreferences.getInt("user_id", -1); // Si no se encuentra, devuelve -1 (o el valor que elijas)
    }


    @Override
    protected void onStop() {
        super.onStop();
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }
}

