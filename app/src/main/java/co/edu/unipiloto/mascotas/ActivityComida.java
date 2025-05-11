package co.edu.unipiloto.mascotas;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ActivityComida extends AppCompatActivity {

    Spinner spinnerMascotas;
    EditText editTipo, editCantidad;
    TextView textHorarios, textAlerta;
    Button btnGuardar;
    SQLiteHelper dbHelper;
    List<Mascota> mascotas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comida);

        spinnerMascotas = findViewById(R.id.spinnerMascotas);
        editTipo = findViewById(R.id.editTipoComida);
        editCantidad = findViewById(R.id.editCantidadComida);
        textHorarios = findViewById(R.id.textHorarios);
        textAlerta = findViewById(R.id.textAlerta);
        btnGuardar = findViewById(R.id.btnGuardarComida);

        dbHelper = new SQLiteHelper(this);
        Spinner spinnerMascotas = findViewById(R.id.spinnerMascotas); // asegúrate de que el ID es correcto

        final List<Mascota> mascotas;

        int userId = obtenerUserId();
        if (userId != -1) {
            mascotas = dbHelper.obtenerMascotas(userId);
        } else {
            Toast.makeText(this, "No se encontró el ID del usuario", Toast.LENGTH_SHORT).show();
            mascotas = new ArrayList<>();
        }

        if (!mascotas.isEmpty()) {
            ArrayAdapter<Mascota> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mascotas);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerMascotas.setAdapter(adapter);
        } else {
            Toast.makeText(this, "No hay mascotas registradas", Toast.LENGTH_SHORT).show();
        }

        spinnerMascotas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Mascota m = mascotas.get(position);
                List<String> horarios = sugerirHorarios(m.getRaza(), m.getEdad());
                textHorarios.setText("Horarios sugeridos: " + horarios.toString());

                if (dbHelper.detectarCambiosEnHabitos(m.getId())) {
                    textAlerta.setVisibility(View.VISIBLE);
                    textAlerta.setText("¡Cambio en los hábitos detectado!");
                } else {
                    textAlerta.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        btnGuardar.setOnClickListener(v -> {
            Mascota mascota = (Mascota) spinnerMascotas.getSelectedItem();
            String tipo = editTipo.getText().toString();
            int cantidad = Integer.parseInt(editCantidad.getText().toString());

            // Obtener la fecha actual en formato "yyyy-MM-dd"
            String fechaActual = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            dbHelper.agregarRegistroAlimentacion(mascota.getId(), fechaActual, tipo, cantidad);
            Toast.makeText(this, "Comida registrada", Toast.LENGTH_SHORT).show();
        });

    }

    private List<String> sugerirHorarios(String raza, int edad) {
        List<String> horarios = new ArrayList<>();

        if (edad < 1) {
            horarios.add("08:00 AM");
            horarios.add("01:00 PM");
            horarios.add("06:00 PM");
        } else if (edad < 7) {
            horarios.add("09:00 AM");
            horarios.add("06:00 PM");
        } else {
            horarios.add("10:00 AM");
            horarios.add("05:00 PM");
        }

        if (raza.equalsIgnoreCase("chihuahua")) {
            horarios.add("08:00 PM");
        }

        return horarios;
    }


    private int obtenerUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("usuario_pref", MODE_PRIVATE);
        return sharedPreferences.getInt("user_id", -1); // Devuelve -1 si no se encuentra el user_id
    }
}
