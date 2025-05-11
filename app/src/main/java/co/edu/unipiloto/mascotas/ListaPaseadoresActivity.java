package co.edu.unipiloto.mascotas;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class ListaPaseadoresActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PaseadorAdapter adapter;
    private List<Paseador> paseadores;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST = 1;

    // Lista simulada de paseadores (sin API)
    private List<Paseador> paseadoresSimulados = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_paseadores);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        paseadores = new ArrayList<>();
        adapter = new PaseadorAdapter(this, paseadores);
        recyclerView.setAdapter(adapter);

        // Agregar datos simulados
        paseadoresSimulados.add(new Paseador("María", 3.7f, "https://i.pravatar.cc/100?img=5", 4.645, -74.080, "Calle 123", "3001234567"));
        paseadoresSimulados.add(new Paseador("Juan", 4.9f, "https://i.pravatar.cc/100?img=7", 4.651, -74.070, "Calle 456", "3007654321"));

        paseadoresSimulados.add(new Paseador("Laura", 4.2f, "https://i.pravatar.cc/100?img=23", 4.642, -74.065, "Carrera 10", "3012345678"));
        paseadoresSimulados.add(new Paseador("Carlos", 4.5f, "https://i.pravatar.cc/100?img=13", 4.648, -74.075, "Calle 50", "3023456789"));
        paseadoresSimulados.add(new Paseador("Ana", 2.0f, "https://i.pravatar.cc/100?img=16", 4.640, -74.072, "Transversal 8", "3034567890"));
        paseadoresSimulados.add(new Paseador("Pedro", 3.0f, "https://i.pravatar.cc/100?img=33", 4.643, -74.067, "Calle 77", "3045678901"));
        paseadoresSimulados.add(new Paseador("Sofía", 3.7f, "https://i.pravatar.cc/100?img=10", 4.649, -74.069, "Carrera 15", "3056789012"));
        paseadoresSimulados.add(new Paseador("Andrés", 4.3f, "https://i.pravatar.cc/100?img=8", 4.644, -74.073, "Calle 98", "3067890123"));
        paseadoresSimulados.add(new Paseador("Camila", 4.6f, "https://i.pravatar.cc/100?img=9", 4.647, -74.068, "Avenida 1", "3078901234"));
        paseadoresSimulados.add(new Paseador("Mateo", 2.5f, "https://i.pravatar.cc/100?img=11", 4.646, -74.066, "Diagonal 22", "3089012345"));
        paseadoresSimulados.add(new Paseador("Isabela", 4.4f, "https://i.pravatar.cc/100?img=19", 4.641, -74.071, "Calle 65", "3090123456"));
        paseadoresSimulados.add(new Paseador("Diego", 3.1f, "https://i.pravatar.cc/100?img=12", 4.650, -74.078, "Carrera 25", "3101234567"));


        // Agrega más paseadores simulados...

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Verificar si el permiso está concedido
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Si el permiso no está concedido, pedirlo
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
        } else {
            // Si ya se tiene el permiso, obtener la ubicación
            obtenerUbicacionYFiltrar();
        }
    }

    private void obtenerUbicacionYFiltrar() {
        try {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    filtrarPaseadoresCercanos(location);
                } else {
                    Toast.makeText(this, "No se pudo obtener ubicación", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (SecurityException e) {
            // Manejo de la excepción en caso de que no tengamos permisos
            Toast.makeText(this, "Permiso de ubicación no disponible", Toast.LENGTH_SHORT).show();
        }
    }

    private void filtrarPaseadoresCercanos(Location location) {
        paseadores.clear();

        for (Paseador paseador : paseadoresSimulados) {
            Location paseadorLocation = new Location("");
            paseadorLocation.setLatitude(paseador.getLatitud());
            paseadorLocation.setLongitude(paseador.getLongitud());

            // Filtrar paseadores dentro de un radio de 5 km, por ejemplo
            float distance = location.distanceTo(paseadorLocation);
            if (distance <= 5000) { // 5 km
                paseadores.add(paseador);
            }
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); // Llamada al método padre

        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obtenerUbicacionYFiltrar();
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
