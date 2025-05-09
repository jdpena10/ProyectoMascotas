package co.edu.unipiloto.mascotas;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import android.app.AlertDialog;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import android.content.ContentValues;

public class VaccinesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private VaccinesAdapter vaccinesAdapter;
    private List<String> listaVacunas;
    private SQLiteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccines);

        // Inicializar DBHelper y RecyclerView
        dbHelper = new SQLiteHelper(this);
        recyclerView = findViewById(R.id.recyclerViewVacunas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Obtener las vacunas de la base de datos usando el método de SQLiteHelper
        listaVacunas = dbHelper.obtenerVacunasConMascotas(dbHelper.getReadableDatabase());

        // Configurar el RecyclerView con el Adapter
        vaccinesAdapter = new VaccinesAdapter(listaVacunas, this);
        recyclerView.setAdapter(vaccinesAdapter);

        // Botón para agregar nueva vacuna
        Button btnAgregarVacuna = findViewById(R.id.btnAgregarVacuna);
        btnAgregarVacuna.setOnClickListener(v -> {
            // Llamar a un método para agregar una nueva vacuna
            agregarVacuna();
        });
    }

    // Método para agregar una nueva vacuna
    private void agregarVacuna() {
        // Crear los campos del formulario
        EditText etNombreVacuna = new EditText(this);
        etNombreVacuna.setHint("Nombre de la vacuna");

        EditText etFechaVacuna = new EditText(this);
        etFechaVacuna.setHint("Fecha de la vacuna (yyyy-mm-dd)");

        EditText etProximaDosis = new EditText(this);
        etProximaDosis.setHint("Fecha de próxima dosis (yyyy-mm-dd)");

        EditText etNombreMascota = new EditText(this);
        etNombreMascota.setHint("Nombre de la mascota");

        // Crear un LinearLayout para contener los EditText
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(etNombreVacuna);
        layout.addView(etFechaVacuna);
        layout.addView(etProximaDosis);
        layout.addView(etNombreMascota);

        // Crear el Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Agregar Nueva Vacuna")
                .setView(layout)
                .setPositiveButton("Agregar", (dialog, which) -> {
                    // Obtener los datos ingresados por el usuario
                    String nombreVacuna = etNombreVacuna.getText().toString();
                    String fechaVacuna = etFechaVacuna.getText().toString();
                    String proximaDosis = etProximaDosis.getText().toString();
                    String nombreMascota = etNombreMascota.getText().toString();

                    // Validar que todos los campos estén llenos
                    if (!nombreVacuna.isEmpty() && !fechaVacuna.isEmpty() && !proximaDosis.isEmpty() && !nombreMascota.isEmpty()) {
                        // Validar el formato de las fechas
                        if (!esFechaValida(fechaVacuna) || !esFechaValida(proximaDosis)) {
                            Toast.makeText(this, "Por favor, ingresa las fechas en formato correcto (yyyy-mm-dd).", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Insertar en la base de datos
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        dbHelper.insertarVacuna(nombreVacuna, fechaVacuna, proximaDosis, nombreMascota);

                        // Crear el string para la nueva vacuna
                        String nuevaVacuna = "Pet: " + nombreMascota + " - Vaccine: " + nombreVacuna +
                                " (Date: " + fechaVacuna + ", Next Dose: " + proximaDosis + ")";

                        // Actualizar la lista y notificar al adapter
                        listaVacunas.add(nuevaVacuna);
                        vaccinesAdapter.notifyItemInserted(listaVacunas.size() - 1);

                        // Mostrar un mensaje de confirmación
                        Toast.makeText(this, "Vacuna agregada con éxito", Toast.LENGTH_SHORT).show();
                    } else {
                        // Mostrar un mensaje si faltan campos
                        Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .create()
                .show();
    }


    // Método para validar el formato de la fecha (yyyy-mm-dd)
    private boolean esFechaValida(String fecha) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);  // No permite fechas inválidas como "2023-02-30"
            sdf.parse(fecha);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

}

