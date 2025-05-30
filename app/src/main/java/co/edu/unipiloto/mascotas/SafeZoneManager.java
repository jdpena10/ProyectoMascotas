package co.edu.unipiloto.mascotas;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.Manifest;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;


public class SafeZoneManager extends AppCompatActivity {

    private static final int REQUEST_LOCATION_PERMISSION = 100;

    private SQLiteHelper dbHelper;
    private EditText etLatitude, etLongitude, etRadius;
    private Button btnGuardar, btnVerMapa;

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_zone);

        // Inicializar base de datos
        dbHelper = new SQLiteHelper(this);

        // Referencias UI
        etLatitude = findViewById(R.id.etLatitude);
        etLongitude = findViewById(R.id.etLongitude);
        etRadius = findViewById(R.id.etRadius);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnVerMapa = findViewById(R.id.btnVerMapa);

        // Inicializar cliente de ubicación
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Pedir permisos o cargar ubicación
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            obtenerUbicacion();
        }

        // Configurar botones
        btnGuardar.setOnClickListener(v -> guardarZonaSegura());
        btnVerMapa.setOnClickListener(v -> abrirMapa());
    }

    @SuppressLint("MissingPermission") // Porque validamos permisos antes de llamar
    private void obtenerUbicacion() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        etLatitude.setText(String.valueOf(location.getLatitude()));
                        etLongitude.setText(String.valueOf(location.getLongitude()));
                    } else {
                        Toast.makeText(this, "No se pudo obtener la ubicación actual", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obtenerUbicacion();
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void guardarZonaSegura() {
        if (validarCampos()) {
            int petId = 1; // Cambia esto según la mascota seleccionada
            double lat = Double.parseDouble(etLatitude.getText().toString());
            double lon = Double.parseDouble(etLongitude.getText().toString());
            float radio = Float.parseFloat(etRadius.getText().toString());

            // Guardar en la base de datos
            dbHelper.insertSafeZone(petId, lat, lon, radio);
            Log.d("SafeZoneManager", "Zona segura guardada en la BD");

            Toast.makeText(this, "Zona segura guardada", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void abrirMapa() {
        int petId = 1; // Cambia según mascota

        Intent intent = new Intent(this, SafeZoneMapActivity.class);
        intent.putExtra("PET_ID", petId);
        startActivity(intent);
    }

    private boolean validarCampos() {
        if (etLatitude.getText().toString().isEmpty() ||
                etLongitude.getText().toString().isEmpty() ||
                etRadius.getText().toString().isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}



