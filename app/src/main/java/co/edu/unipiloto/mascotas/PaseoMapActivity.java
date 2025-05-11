package co.edu.unipiloto.mascotas;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.Manifest;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class PaseoMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker markerMascota;
    private List<LatLng> rutaDefinida = new ArrayList<>();
    private int currentRutaIndex = 0;
    private String nombreMascota;
    private FusedLocationProviderClient fusedLocationClient;
    private LatLng ubicacionActual;
    private Button btnUbicacionActual, btnAgregarPunto, btnIniciarRecorrido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paseo_mapa);

        nombreMascota = getIntent().getStringExtra("nombreMascota");

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        btnUbicacionActual = findViewById(R.id.btnUbicacionActual); // Botón para mostrar ubicación actual
        btnAgregarPunto = findViewById(R.id.btnAgregarPunto);       // Botón para agregar punto manual
        btnIniciarRecorrido = findViewById(R.id.btnIniciarRecorrido);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Botón para obtener y mostrar ubicación actual
        btnUbicacionActual.setOnClickListener(v -> {
            obtenerUbicacionActual();
            if (ubicacionActual != null) {
                Log.d("Ubicacion", "Ubicación actual: " + ubicacionActual);
                mMap.addMarker(new MarkerOptions().position(ubicacionActual).title("Ubicación actual"));
            }
        });

        // Botón para agregar punto manual (posición central del mapa)
        btnAgregarPunto.setOnClickListener(v -> {
            if (mMap != null) {
                LatLng punto = mMap.getCameraPosition().target;
                rutaDefinida.add(punto);
                mMap.addMarker(new MarkerOptions().position(punto).title("Punto manual"));

                Log.d("Ruta", "Punto manual agregado: " + punto);
                Log.d("Ruta", "Tamaño de la ruta: " + rutaDefinida.size());
            }
        });

        btnIniciarRecorrido.setOnClickListener(v -> iniciarSimulacion());
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        obtenerUbicacionActual();
    }

    private void obtenerUbicacionActual() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                ubicacionActual = new LatLng(location.getLatitude(), location.getLongitude());
                Log.d("Ubicacion", "Ubicación actual: " + ubicacionActual);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionActual, 17f));
            }
        });
    }

    private void iniciarSimulacion() {
        if (rutaDefinida.isEmpty()) {
            Toast.makeText(this, "No hay puntos en la ruta", Toast.LENGTH_SHORT).show();
            return;
        }

        if (markerMascota != null) {
            markerMascota.remove();
        }

        markerMascota = mMap.addMarker(new MarkerOptions()
                .position(rutaDefinida.get(0))
                .title(nombreMascota)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(rutaDefinida.get(0), 17f));

        Handler handler = new Handler();
        Runnable mover = new Runnable() {
            @Override
            public void run() {
                if (currentRutaIndex < rutaDefinida.size()) {
                    LatLng nuevaPos = rutaDefinida.get(currentRutaIndex);
                    markerMascota.setPosition(nuevaPos);
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(nuevaPos));

                    currentRutaIndex++;
                    handler.postDelayed(this, 2000);
                } else {
                    Toast.makeText(PaseoMapActivity.this, "Recorrido finalizado", Toast.LENGTH_SHORT).show();
                }
            }
        };

        handler.post(mover);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            obtenerUbicacionActual();
        }
    }
}



