package co.edu.unipiloto.mascotas;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.view.View;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class SafeZoneMapActivity extends AppCompatActivity {
    private GoogleMap mMap;
    private SQLiteHelper dbHelper;
    private Button btnVolver, btnEliminarZonaSegura, btnSimularMovimiento;
    private LatLng petLocation;
    private CircleOptions safeZoneCircle;
    private Marker petMarker;
    private double movementOffset = 0.0005;
    private int petId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_zone_map);

        dbHelper = new SQLiteHelper(this);
        petId = getIntent().getIntExtra("PET_ID", -1);

        btnVolver = findViewById(R.id.btnVolver);
        btnEliminarZonaSegura = findViewById(R.id.btnEliminarZonaSegura);
        btnSimularMovimiento = findViewById(R.id.btnSimularMovimiento);

        btnVolver.setOnClickListener(v -> finish());

        btnEliminarZonaSegura.setOnClickListener(v -> {
            if (petId != -1) {
                dbHelper.deleteSafeZone(petId);
                Toast.makeText(this, "Zona segura eliminada", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "ID de mascota no válido", Toast.LENGTH_SHORT).show();
            }
        });

        btnSimularMovimiento.setOnClickListener(v -> {
            Log.d("SafeZoneMapActivity", "Botón de simulación presionado");
            simularMovimiento();
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(googleMap -> {
                mMap = googleMap;
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                Log.d("SafeZoneMapActivity", "Mapa inicializado correctamente.");

                if (petId != -1) {
                    SafeZone safeZone = dbHelper.getSafeZone(petId);

                    if (safeZone != null) {
                        LatLng centroZonaSegura = new LatLng(safeZone.getLatitude(), safeZone.getLongitude());
                        safeZoneCircle = new CircleOptions()
                                .center(centroZonaSegura)
                                .radius(safeZone.getRadius())
                                .strokeColor(Color.RED)
                                .fillColor(0x30FF0000)
                                .strokeWidth(2f);
                        mMap.addCircle(safeZoneCircle);

                        petLocation = centroZonaSegura;
                        petMarker = mMap.addMarker(new MarkerOptions().position(petLocation).title("Mascota"));

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centroZonaSegura, 15f));
                    } else {
                        Log.e("SafeZoneMapActivity", "No se encontró la zona segura");
                        Toast.makeText(this, "No se encontró la zona segura", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("SafeZoneMapActivity", "ID de mascota no válido");
                    Toast.makeText(this, "ID de mascota no válido", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void simularMovimiento() {
        if (mMap != null && petMarker != null && safeZoneCircle != null) {
            // Simula el nuevo movimiento
            petLocation = new LatLng(petLocation.latitude + movementOffset, petLocation.longitude + movementOffset);

            runOnUiThread(() -> {
                petMarker.setPosition(petLocation);
                mMap.animateCamera(CameraUpdateFactory.newLatLng(petLocation));

                float[] distance = new float[1];
                android.location.Location.distanceBetween(
                        safeZoneCircle.getCenter().latitude, safeZoneCircle.getCenter().longitude,
                        petLocation.latitude, petLocation.longitude, distance
                );

                if (distance[0] > safeZoneCircle.getRadius()) {
                    Toast.makeText(this, "¡Alerta! La mascota ha salido de la zona segura.", Toast.LENGTH_LONG).show();
                    mostrarNotificacion("¡Alerta!", "Tu mascota ha salido de la zona segura.");
                    Log.d("SafeZoneMapActivity", "Mascota fuera de la zona segura.");

                    int userId = obtenerUserId();
                    if (userId != -1) {
                        String userEmail = dbHelper.getUserEmailByUserId(userId);
                        if (userEmail != null && !userEmail.isEmpty()) {
                            new Thread(() -> {
                                EmailSender.sendEmail(userEmail, "¡Alerta de Zona Segura!",
                                        "Tu mascota ha salido de la zona segura.");
                            }).start();
                        } else {
                            Log.e("SafeZoneMapActivity", "No se encontró el correo para userId: " + userId);
                        }
                    } else {
                        Log.e("SafeZoneMapActivity", "No se encontró el user_id en SharedPreferences");
                    }
                }
            });
        } else {
            Log.e("SafeZoneMapActivity", "Error: petMarker o safeZoneCircle es nulo");
        }
    }

    private int obtenerUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("usuario_pref", MODE_PRIVATE);
        return sharedPreferences.getInt("user_id", -1);
    }

    private void mostrarNotificacion(String titulo, String mensaje) {
        String channelId = "ALERTA_ZONA_SEGURA";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && notificationManager.getNotificationChannel(channelId) == null) {
            NotificationChannel channel = new NotificationChannel(channelId, "Alertas de zona segura",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle(titulo)
                .setContentText(mensaje)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        notificationManager.notify(1, builder.build());
    }
}





