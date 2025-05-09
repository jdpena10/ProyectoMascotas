package co.edu.unipiloto.mascotas;

import android.Manifest;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.google.android.gms.maps.CameraUpdateFactory;

public class AdopcionMascotasActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private Button btnBuscarAdopcion;
    private RecyclerView recyclerViewAdopcion;
    private LugarAdapter lugarAdapter;
    private List<Lugar> listaLugares = new ArrayList<>();
    private String googlePlacesApiKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adopcion_mascotas);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtener la clave de la API de Google Places desde los metadatos
        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            googlePlacesApiKey = appInfo.metaData.getString("com.google.android.geo.API_KEY");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al obtener la clave de la API", Toast.LENGTH_SHORT).show();
        }

        // Configurar el botón y RecyclerView
        btnBuscarAdopcion = findViewById(R.id.btnBuscarAdopcion);
        recyclerViewAdopcion = findViewById(R.id.recyclerViewAdopcion);
        recyclerViewAdopcion.setLayoutManager(new LinearLayoutManager(this));
        lugarAdapter = new LugarAdapter(listaLugares);
        recyclerViewAdopcion.setAdapter(lugarAdapter);

        // Evento de clic para buscar lugares de adopción
        btnBuscarAdopcion.setOnClickListener(v -> obtenerUbicacion());

        // Configurar el mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapAdopcion);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Verificar permisos de ubicación y mostrar la ubicación en el mapa
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
    }

    private void obtenerUbicacion() {
        // Verificar permisos antes de obtener la ubicación
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                // Obtener ubicación actual y buscar lugares de adopción
                buscarLugaresAdopcion(location.getLatitude(), location.getLongitude());

                // Establecer la ubicación actual como el centro del mapa
                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));  // Zoom a la ubicación actual
            } else {
                Toast.makeText(AdopcionMascotasActivity.this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void buscarLugaresAdopcion(double lat, double lng) {
        // Construir la URL para la API de Overpass con un rango de 100 km (100000 metros)
        String url = "https://overpass-api.de/api/interpreter?data=[out:json];"
                + "("
                + "node[\"animal_shelter\"](around:100000," + lat + "," + lng + ");"
                + "node[\"shop\"=\"pet\"][\"adoption\"=\"yes\"](around:100000," + lat + "," + lng + ");"
                + ");out center;";

        new Thread(() -> {
            try {
                // Realizar la solicitud HTTP
                HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                InputStream inputStream = new BufferedInputStream(conn.getInputStream());
                String response = new BufferedReader(new InputStreamReader(inputStream))
                        .lines().collect(Collectors.joining("\n"));

                // Parsear la respuesta JSON
                JSONObject json = new JSONObject(response);
                JSONArray elements = json.getJSONArray("elements");

                listaLugares.clear();

                for (int i = 0; i < elements.length(); i++) {
                    JSONObject element = elements.getJSONObject(i);

                    // Obtener el nombre
                    String nombre = "Lugar de adopción";
                    if (element.has("tags") && element.getJSONObject("tags").has("name")) {
                        nombre = element.getJSONObject("tags").getString("name");
                    } else if (element.has("tags")) {
                        JSONObject tags = element.getJSONObject("tags");
                        if (tags.has("animal_shelter")) {
                            nombre = "Refugio de animales";
                        } else if (tags.has("shop")) {
                            nombre = "Tienda de adopción de mascotas";
                        }
                    }

                    // Obtener la dirección
                    String direccion = "Dirección no disponible";
                    if (element.has("tags") && element.getJSONObject("tags").has("addr:street")) {
                        direccion = element.getJSONObject("tags").getString("addr:street");
                    }

                    double latLugar = element.getDouble("lat");
                    double lngLugar = element.getDouble("lon");

                    // Usar ID de OSM como identificador
                    String placeId = "osm:" + element.getLong("id");

                    listaLugares.add(new Lugar(nombre, direccion, placeId, latLugar, lngLugar));
                }

                runOnUiThread(() -> {
                    lugarAdapter.notifyDataSetChanged();

                    for (Lugar lugar : listaLugares) {
                        LatLng ubicacion = new LatLng(lugar.getLatitud(), lugar.getLongitud());
                        mMap.addMarker(new MarkerOptions().position(ubicacion).title(lugar.getNombre()));
                    }

                    if (listaLugares.isEmpty()) {
                        Toast.makeText(AdopcionMascotasActivity.this, "No se encontraron lugares de adopción cercanos", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(AdopcionMascotasActivity.this, "Error al obtener los datos", Toast.LENGTH_SHORT).show());
                e.printStackTrace();
            }
        }).start();
    }
}


