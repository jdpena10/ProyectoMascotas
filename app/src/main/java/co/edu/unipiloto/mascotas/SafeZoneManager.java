package co.edu.unipiloto.mascotas;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;


public class SafeZoneManager extends AppCompatActivity {
    private SQLiteHelper dbHelper;
    private EditText etLatitude, etLongitude, etRadius;
    private Button btnGuardar, btnVerMapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_zone); // Asegúrate de tener este XML

        // Inicializar la base de datos
        dbHelper = new SQLiteHelper(this);

        // Referencias a los elementos del layout
        etLatitude = findViewById(R.id.etLatitude);
        etLongitude = findViewById(R.id.etLongitude);
        etRadius = findViewById(R.id.etRadius);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnVerMapa = findViewById(R.id.btnVerMapa);

        // Configurar evento del botón
        btnGuardar.setOnClickListener(v -> guardarZonaSegura());
        btnVerMapa.setOnClickListener(v -> abrirMapa());
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
            finish(); // Cierra la actividad después de guardar
        }
    }

    private void abrirMapa() {
        int petId = 1; // Cambia esto según la mascota seleccionada

        // Crear Intent para abrir SafeZoneMapActivity
        Intent intent = new Intent(this, SafeZoneMapActivity.class);
        intent.putExtra("PET_ID", petId); // Pasar ID de la mascota si se necesita
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



