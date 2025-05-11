package co.edu.unipiloto.mascotas;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RegistrarCuidadorActivity extends Activity {
    private EditText etNombre, etAddress, etEmail, etPhone, etAvailability;
    private Button btnRegistrarCuidador, btnEliminarCuidador;
    private TextView tvUploadPhoto;
    private ImageView ivProfilePhoto;
    private FrameLayout flPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_cuidador); // Ajusta el layout de la actividad

        // Inicializando los campos
        etNombre = findViewById(R.id.etNombre);
        etAddress = findViewById(R.id.etAddress);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAvailability = findViewById(R.id.etAvailability);
        btnRegistrarCuidador = findViewById(R.id.btnRegistrarCuidador);
        btnEliminarCuidador = findViewById(R.id.btnEliminarCuidador);
        tvUploadPhoto = findViewById(R.id.tvUploadPhoto);
        ivProfilePhoto = findViewById(R.id.ivProfilePhoto);
        flPhoto = findViewById(R.id.flPhoto);

        // Cargar automáticamente los datos del usuario autenticado
        int userId = obtenerUserId();
        if (userId != -1) {
            SQLiteHelper db = new SQLiteHelper(this);
            Cursor cursor = db.getUserData(userId);

            if (cursor != null && cursor.moveToFirst()) {
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String direccion = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));

                etNombre.setText(nombre);
                etAddress.setText(direccion);
                etEmail.setText(email);

                // Opcional: evitar que el usuario los edite
                etNombre.setEnabled(false);
                etAddress.setEnabled(false);
                etEmail.setEnabled(false);
            }

            if (cursor != null) {
                cursor.close();
            }
        }

        // Configuración del botón para registrar al cuidador
        btnRegistrarCuidador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarCuidador();
            }
        });

        // Configuración del botón para eliminar al cuidador
        btnEliminarCuidador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarCuidador();
            }
        });

        // Acción para subir foto de perfil
        tvUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementa la lógica para abrir la galería y seleccionar una foto
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1); // La constante 1 es un código arbitrario
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1 && data != null) {
            ivProfilePhoto.setVisibility(View.VISIBLE);
            ivProfilePhoto.setImageURI(data.getData()); // Establecer la foto seleccionada
        }
    }

    private void registrarCuidador() {
        String nombre = etNombre.getText().toString().trim();
        String direccion = etAddress.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String telefono = etPhone.getText().toString().trim();
        String disponibilidad = etAvailability.getText().toString().trim();

        int userId = obtenerUserId();

        if (nombre.isEmpty() || direccion.isEmpty() || email.isEmpty() || telefono.isEmpty() || disponibilidad.isEmpty()) {
            Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userId == -1) {
            Toast.makeText(this, "No se ha encontrado el usuario autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteHelper db = new SQLiteHelper(this);

        if (db.isCaretakerRegistered(userId)) {
            Toast.makeText(this, "Ya estás registrado como cuidador", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = db.insertCaretaker(userId, email, direccion, telefono, telefono, disponibilidad);

        if (success) {
            Toast.makeText(this, "Cuidador registrado con éxito", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al registrar el cuidador", Toast.LENGTH_SHORT).show();
        }
    }

    private void eliminarCuidador() {
        int userId = obtenerUserId();

        if (userId == -1) {
            Toast.makeText(this, "No se ha encontrado el usuario autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteHelper db = new SQLiteHelper(this);
        if (!db.isCaretakerRegistered(userId)) {
            Toast.makeText(this, "No estás registrado como cuidador", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean eliminado = db.deleteCaretaker(userId);
        if (eliminado) {
            Toast.makeText(this, "Registro como cuidador eliminado", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al eliminar el cuidador", Toast.LENGTH_SHORT).show();
        }
    }

    private int obtenerUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("usuario_pref", MODE_PRIVATE);
        return sharedPreferences.getInt("user_id", -1); // Devuelve -1 si no se encuentra
    }
}

