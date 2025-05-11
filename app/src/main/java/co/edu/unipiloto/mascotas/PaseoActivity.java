package co.edu.unipiloto.mascotas;



import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;
import java.util.List;

public class PaseoActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private TextView tvLatitud;
    private TextView tvLongitud;

    private final List<LatLng> rutaMascota = new ArrayList<>();
    private LatLng ultimaUbicacionReal = null;
    private SQLiteHelper dbHelper;
    private List<Mascota> listaMascotas = new ArrayList<>();
    private RecyclerView recyclerViewMascotas;
    private MascotaAdapter mascotaAdapter;
    private Mascota mascotaSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paseo);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        tvLatitud = findViewById(R.id.tv_latitud);
        tvLongitud = findViewById(R.id.tv_longitud);
        Button btnUbicacion = findViewById(R.id.btn_location);
        Button btnMostrarRuta = findViewById(R.id.btn_ver_ruta);
        Button btnSimularMovimiento = findViewById(R.id.btn_simular_movimiento);

        btnUbicacion.setOnClickListener(v -> obtenerUbicacion());
        btnMostrarRuta.setOnClickListener(v -> abrirRutaEnGoogleMaps());
        btnSimularMovimiento.setOnClickListener(v -> simularMovimiento());

        dbHelper = new SQLiteHelper(this);
        recyclerViewMascotas = findViewById(R.id.recycler_mascotas);
        recyclerViewMascotas.setLayoutManager(new LinearLayoutManager(this));

        mascotaAdapter = new MascotaAdapter(new ArrayList<>(), dbHelper, new MascotaAdapter.OnMascotaClickListener() {
            @Override
            public void onMascotaClick(Mascota mascota) {
                mascotaSeleccionada = mascota;
                Toast.makeText(PaseoActivity.this, "Mascota seleccionada: " + mascota.getNombre(), Toast.LENGTH_SHORT).show();
            }
        });
        recyclerViewMascotas.setAdapter(mascotaAdapter);
        cargarMascotas();



        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (android.location.Location location : locationResult.getLocations()) {
                    LatLng nuevaUbicacion = new LatLng(location.getLatitude(), location.getLongitude());

                    if (!nuevaUbicacion.equals(ultimaUbicacionReal)) {
                        rutaMascota.add(nuevaUbicacion);
                        ultimaUbicacionReal = nuevaUbicacion;

                        tvLatitud.setText("Latitud: " + nuevaUbicacion.latitude);
                        tvLongitud.setText("Longitud: " + nuevaUbicacion.longitude);

                        Toast.makeText(PaseoActivity.this, "Movimiento detectado", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
    }

    private void obtenerUbicacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                tvLatitud.setText("Latitud: " + location.getLatitude());
                tvLongitud.setText("Longitud: " + location.getLongitude());
            } else {
                tvLatitud.setText("Latitud: desconocida");
                tvLongitud.setText("Longitud: desconocida");
            }
        });
    }

    private void iniciarPaseo() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
            return;
        }

        fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
        );

        Toast.makeText(this, "Iniciando paseo...", Toast.LENGTH_SHORT).show();
    }

    private void abrirRutaEnGoogleMaps() {
        if (mascotaSeleccionada == null) {
            Toast.makeText(this, "Debes seleccionar una mascota primero", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, RutaMapaActivity.class);
        intent.putParcelableArrayListExtra("ruta", new ArrayList<>(rutaMascota));
        intent.putExtra("nombre_mascota", mascotaSeleccionada.getNombre());
        startActivity(intent);
    }


    private void simularMovimiento() {
        LatLng ultimaUbicacion = rutaMascota.isEmpty() ?
                new LatLng(4.7110, -74.0721) : rutaMascota.get(rutaMascota.size() - 1);

        double nuevaLatitud = ultimaUbicacion.latitude + 0.0001;
        double nuevaLongitud = ultimaUbicacion.longitude + 0.0001;
        LatLng nuevoPunto = new LatLng(nuevaLatitud, nuevaLongitud);

        rutaMascota.add(nuevoPunto);

        tvLatitud.setText("Latitud: " + nuevaLatitud);
        tvLongitud.setText("Longitud: " + nuevaLongitud);

        Toast.makeText(this, "Movimiento simulado agregado", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == 1) {
                obtenerUbicacion();
            } else if (requestCode == 2) {
                iniciarPaseo();
            }
        } else {
            Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
        }
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
