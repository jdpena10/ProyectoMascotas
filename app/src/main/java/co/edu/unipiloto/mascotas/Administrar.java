package co.edu.unipiloto.mascotas;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;

public class Administrar extends AppCompatActivity {

    private EditText etUsername, etName, etPassword, etEmail, etAddress;
    private Button btnAgregarUsuario, btnEliminarUsuario, btnVolverInicio;
    private ListView listViewUsuarios;
    private SQLiteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrar); // Asegúrate de que el XML tenga los IDs correctos

        // Inicializar componentes de la interfaz
        etName = findViewById(R.id.et_name);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etEmail = findViewById(R.id.et_email);
        etAddress = findViewById(R.id.et_address);
        btnAgregarUsuario = findViewById(R.id.btn_agregar_usuario);
        btnEliminarUsuario = findViewById(R.id.btn_eliminar_usuario);
        btnVolverInicio = findViewById(R.id.btn_volver_inicio);
        listViewUsuarios = findViewById(R.id.listViewUsuarios);
        dbHelper = new SQLiteHelper(this);

        // Llamar al método para mostrar usuarios al iniciar la actividad
        mostrarUsuarios();

        // Acción cuando se presiona "Agregar Usuario"
        btnAgregarUsuario.setOnClickListener(v -> agregarUsuario());

        // Acción cuando se presiona "Eliminar Usuario"
        btnEliminarUsuario.setOnClickListener(v -> eliminarUsuario());

        // Acción cuando se presiona "Volver a Inicio"
        btnVolverInicio.setOnClickListener(v -> volverInicio());
    }

    // Método para agregar un usuario
    private void agregarUsuario() {
        String name = etName.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        if (name.isEmpty() || username.isEmpty() || password.isEmpty() || email.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dbHelper.checkUserExists(username)) {
            Toast.makeText(this, "El usuario ya existe", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = dbHelper.addUser(name, username, password, email, address);
        if (success) {
            Toast.makeText(this, "Usuario agregado correctamente", Toast.LENGTH_SHORT).show();
            limpiarCampos();
            mostrarUsuarios(); // Actualizar la lista de usuarios
        } else {
            Toast.makeText(this, "Error al agregar usuario", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para eliminar un usuario
    private void eliminarUsuario() {
        String username = etUsername.getText().toString().trim();

        if (username.isEmpty()) {
            Toast.makeText(this, "Ingrese un nombre de usuario", Toast.LENGTH_SHORT).show();
            return;
        }

        int userId = dbHelper.getUserIdByUsername(username); // Obtener el ID del usuario
        if (userId == -1) {
            Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = dbHelper.deleteUser(userId); // Eliminar por ID
        if (success) {
            Toast.makeText(this, "Usuario eliminado correctamente", Toast.LENGTH_SHORT).show();
            limpiarCampos();
            mostrarUsuarios(); // Actualizar la lista de usuarios
        } else {
            Toast.makeText(this, "Error al eliminar usuario", Toast.LENGTH_SHORT).show();
        }
    }


    // Método para mostrar la lista de usuarios en el ListView
    private void mostrarUsuarios() {
        Cursor cursor = dbHelper.getAllUsers();

        if (cursor != null && cursor.getCount() > 0) {
            String[] columns = new String[]{"name", "username", "email", "address"};
            int[] views = new int[]{android.R.id.text1, android.R.id.text2};

            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    this,
                    android.R.layout.simple_list_item_2, // Cambiar a un layout personalizado si es necesario
                    cursor,
                    columns,
                    views,
                    0
            );

            listViewUsuarios.setAdapter(adapter);
            listViewUsuarios.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, "No hay usuarios en la base de datos", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para volver a la pantalla de inicio
    private void volverInicio() {
        Intent intent = new Intent(Administrar.this, Inicio.class);

        String username = getIntent().getStringExtra("username");
        String name = getIntent().getStringExtra("name");
        String password = getIntent().getStringExtra("password");
        String email = getIntent().getStringExtra("email");
        String address = getIntent().getStringExtra("address");

        intent.putExtra("username", username);
        intent.putExtra("name", name);
        intent.putExtra("password", password);
        intent.putExtra("email", email);
        intent.putExtra("address", address);

        startActivity(intent);
        finish();
    }

    // Método para limpiar los campos del formulario
    private void limpiarCampos() {
        etName.setText("");
        etUsername.setText("");
        etPassword.setText("");
        etEmail.setText("");
        etAddress.setText("");
    }
}

