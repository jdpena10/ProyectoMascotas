package co.edu.unipiloto.mascotas;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.widget.Button;
import android.content.Intent;

public class MapsActivity extends AppCompatActivity {
    private GoogleMap mMap;
    private SQLiteHelper dbHelper;
    private Button btnVolver, btnRegistrarZonaSegura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        dbHelper = new SQLiteHelper(this);

        int petId = getIntent().getIntExtra("PET_ID", -1);

        // Inicializar el botón de volver
        btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> finish()); // Cierra la actividad actual y vuelve a la anterior

        btnRegistrarZonaSegura = findViewById(R.id.btnRegistrarZonaSegura);
        // Acción del botón de registrar zona segura
        btnRegistrarZonaSegura.setOnClickListener(v -> {
            Intent intent = new Intent(MapsActivity.this, SafeZoneManager.class);
            startActivity(intent);
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(googleMap -> {
                mMap = googleMap;

                if (petId != -1) {
                    LatLng ubicacionMascota = dbHelper.obtenerUbicacionMascota(petId);
                    if (ubicacionMascota != null) {
                        Log.d("MapsActivity", "Ubicación de la mascota: " + ubicacionMascota.latitude + ", " + ubicacionMascota.longitude);
                        mMap.addMarker(new MarkerOptions()
                                .position(ubicacionMascota)
                                .title("Ubicación de la mascota"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionMascota, 15f));
                    } else {
                        Log.e("MapsActivity", "No se encontró la ubicación de la mascota");
                        Toast.makeText(this, "No se encontró la ubicación de la mascota", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("MapsActivity", "ID de mascota no válido");
                    Toast.makeText(this, "ID de mascota no válido", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}



