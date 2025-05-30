package co.edu.unipiloto.mascotas;

import android.Manifest;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import android.os.Looper;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import android.util.Log;
import android.location.Location;
import com.google.android.gms.location.Priority;
import androidx.annotation.NonNull;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;

public class VeterinariosActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private SQLiteHelper dbHelper;
    private Button btnVolver, btnBuscarVeterinarios;
    private RecyclerView recyclerViewVeterinarios;
    private VeterinarioAdapter veterinarioAdapter;
    private List<Veterinario> listaVeterinarios = new ArrayList<>();
    private String googlePlacesApiKey;
    private Button btnAgendarCita;
    private Veterinario veterinarioSeleccionado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veterinarios);

        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            googlePlacesApiKey = appInfo.metaData.getString("com.google.android.geo.API_KEY");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al obtener la clave de la API", Toast.LENGTH_SHORT).show();
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        btnVolver = findViewById(R.id.btnVolver);
        btnBuscarVeterinarios = findViewById(R.id.btnBuscarVeterinarios);
        //recyclerViewVeterinarios = findViewById(R.id.recyclerViewVeterinarios);

        recyclerViewVeterinarios.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar adaptador con listener
        veterinarioAdapter = new VeterinarioAdapter(listaVeterinarios, veterinario -> {
            double lat = veterinario.getLatitud();
            double lng = veterinario.getLongitud();

            LatLng ubicacion = new LatLng(lat, lng);
            mMap.addMarker(new MarkerOptions().position(ubicacion).title(veterinario.getNombre()));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 15));

            // Guardamos el veterinario seleccionado
            veterinarioSeleccionado = veterinario;

            // Mostramos el botón para agendar cita
            btnAgendarCita.setVisibility(View.VISIBLE);
        });

        recyclerViewVeterinarios.setAdapter(veterinarioAdapter);

        btnVolver.setOnClickListener(v -> finish());
        btnBuscarVeterinarios.setOnClickListener(v -> {
            Toast.makeText(this, "Buscando veterinarios...", Toast.LENGTH_SHORT).show();
            obtenerUbicacionYBuscarVeterinarios();
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        //btnAgendarCita = findViewById(R.id.btnAgendarCita);
        btnAgendarCita.setOnClickListener(v -> {
            if (veterinarioSeleccionado != null) {
                // Mostrar mensaje
                Toast.makeText(this, "Buscando en Google: " + veterinarioSeleccionado.getNombre(), Toast.LENGTH_SHORT).show();

                // Construir URL de búsqueda en Google
                String query = veterinarioSeleccionado.getNombre();
                try {
                    query = URLEncoder.encode(query, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String googleSearchUrl = "https://www.google.com/search?q=" + query;

                // Abrir el navegador con la búsqueda
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(googleSearchUrl));
                startActivity(intent);
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, 1);
        }
    }


    private void obtenerUbicacionYBuscarVeterinarios() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                Log.d("GPS", "Lat: " + location.getLatitude() + ", Lng: " + location.getLongitude());
                buscarVeterinarios(location.getLatitude(), location.getLongitude());
            } else {
                Log.d("GPS", "Ubicación es NULL, solicitando actualización...");
                Toast.makeText(this, "Intentando obtener ubicación en tiempo real...", Toast.LENGTH_SHORT).show();
                solicitarUbicacion();
            }
        }).addOnFailureListener(e -> {
            Log.e("GPS", "Error al obtener la ubicación: " + e.getMessage());
            Toast.makeText(this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show();
        });
    }

    private void solicitarUbicacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationRequest locationRequest = new LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY, 5000
        ).build();

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Toast.makeText(VeterinariosActivity.this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    Log.d("GPS", "Lat: " + location.getLatitude() + ", Lng: " + location.getLongitude());
                    buscarVeterinarios(location.getLatitude(), location.getLongitude());
                }
            }
        };

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obtenerUbicacionYBuscarVeterinarios();
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void buscarVeterinarios(double lat, double lng) {
        String url = "https://overpass-api.de/api/interpreter?data=[out:json];node[amenity=veterinary](around:5000," + lat + "," + lng + ");out;";
        Log.d("API_REQUEST", "URL de solicitud: " + url);

        new Thread(() -> {
            try {
                URL apiUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
                connection.setRequestMethod("GET");
                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                String response = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));

                Log.d("API_RESPONSE", "Respuesta de la API: " + response);

                JSONObject jsonResponse = new JSONObject(response);
                JSONArray elements = jsonResponse.getJSONArray("elements");

                if (elements.length() == 0) {
                    Log.d("API_RESPONSE", "No se encontraron veterinarios en la ubicación.");
                    runOnUiThread(() -> Toast.makeText(this, "No hay veterinarios cerca", Toast.LENGTH_SHORT).show());
                    return;
                }

                listaVeterinarios.clear();

                for (int i = 0; i < elements.length(); i++) {
                    JSONObject element = elements.getJSONObject(i);
                    JSONObject tags = element.getJSONObject("tags");

                    String nombre = tags.has("name") ? tags.getString("name") : "Nombre no disponible";
                    String direccion = tags.has("addr:full") ? tags.getString("addr:full") :
                            tags.has("addr:street") ? tags.getString("addr:street") : "Dirección no disponible";

                    double latitudVet = element.getDouble("lat");
                    double longitudVet = element.getDouble("lon");

                    double rating = 0.0;
                    String placeId = element.getString("id");

                    // Obtener el sitio web, si está disponible
                    String website = tags.has("website") ? tags.getString("website") : null;

                    listaVeterinarios.add(new Veterinario(nombre, direccion, rating, placeId, latitudVet, longitudVet, website));
                }

                runOnUiThread(() -> veterinarioAdapter.notifyDataSetChanged());

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Error al obtener datos", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}



