package co.edu.unipiloto.mascotas;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import androidx.appcompat.app.AlertDialog;

public class ReporteActivity extends AppCompatActivity {

    EditText editFechaInicio, editFechaFin, editNombreMascota;
    Spinner spinnerTipoReporte;
    Button btnExportarExcel, btnExportarPdf, btnVolver;
    SQLiteHelper dbHelper;

    private static final int CREATE_FILE = 1001;
    private List<SesionEjercicio> sesiones;
    private String nombreMascota, tipoReporte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte);

        dbHelper = new SQLiteHelper(this);

        editFechaInicio = findViewById(R.id.editFechaInicio);
        editFechaFin = findViewById(R.id.editFechaFin);
        editNombreMascota = findViewById(R.id.editNombreMascota);
        spinnerTipoReporte = findViewById(R.id.spinnerTipoReporte);
        btnExportarExcel = findViewById(R.id.btnExportarExcel);
        btnExportarPdf = findViewById(R.id.btnExportarPdf);
        btnVolver = findViewById(R.id.btnVolver);

        //btnExportarExcel.setOnClickListener(v -> exportar("excel"));
        btnExportarPdf.setOnClickListener(v -> prepararExportacionPDF());
        btnVolver.setOnClickListener(v -> finish());

        configurarFecha(editFechaInicio);
        configurarFecha(editFechaFin);
    }

    private void configurarFecha(EditText editText) {
        editText.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year, month, dayOfMonth) -> {
                        String fecha = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);
                        editText.setText(fecha);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });
    }

    private void prepararExportacionPDF() {
        nombreMascota = editNombreMascota.getText().toString();
        String fechaInicio = editFechaInicio.getText().toString();
        String fechaFin = editFechaFin.getText().toString();
        tipoReporte = spinnerTipoReporte.getSelectedItem().toString();

        if (nombreMascota.isEmpty() || fechaInicio.isEmpty() || fechaFin.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        sesiones = dbHelper.obtenerSesionesFiltradas(nombreMascota, fechaInicio, fechaFin, tipoReporte);
        if (sesiones == null || sesiones.isEmpty()) {
            Toast.makeText(this, "No hay sesiones para exportar", Toast.LENGTH_SHORT).show();
            return;
        }
        // Crea un intent para que el usuario elija dónde guardar el archivo
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE, "Reporte_" + nombreMascota + "_" + tipoReporte + ".pdf");
        startActivityForResult(intent, CREATE_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_FILE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                ReporteExporter.exportarPDF(this, sesiones, nombreMascota, tipoReporte, uri);
            }
        }
    }
}
