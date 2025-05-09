package co.edu.unipiloto.mascotas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.ActivityNotFoundException;
public class Registro extends AppCompatActivity implements View.OnClickListener {
    EditText etNombre, etUsuario, etPass, etEmail, etAddress;
    Button btnRegistrar, btnLoginEmail;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        etNombre = findViewById(R.id.nombreReg);
        etUsuario = findViewById(R.id.usuarioReg);
        etPass = findViewById(R.id.passReg);
        etEmail = findViewById(R.id.emailReg); // Nuevo campo
        etAddress = findViewById(R.id.addressReg); // Nuevo campo
        btnRegistrar = findViewById(R.id.registrar_btn);
        btnLoginEmail = findViewById(R.id.login_email_btn);

        btnRegistrar.setOnClickListener(this);
        btnLoginEmail.setOnClickListener(this);

        // Recibir datos del Intent
        Intent intent = getIntent();
        if (intent != null) {
            String email = intent.getStringExtra("correo");
            String password = intent.getStringExtra("password");
            String address = intent.getStringExtra("direccion"); // Nuevo dato

            if (email != null) {
                etUsuario.setText(email);
                etEmail.setText(email); // Establecer también en el nuevo campo
            }
            if (password != null) {
                etPass.setText(password);
            }
            if (address != null) {
                etAddress.setText(address);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.registrar_btn) {
            String name = etNombre.getText().toString().trim();
            String username = etUsuario.getText().toString().trim();
            String password = etPass.getText().toString().trim();
            String email = etEmail.getText().toString().trim(); // Nuevo campo
            String address = etAddress.getText().toString().trim(); // Nuevo campo

            // Verificamos si los campos están vacíos
            if (name.isEmpty() || username.isEmpty() || password.isEmpty() || email.isEmpty() || address.isEmpty()) {
                Toast.makeText(Registro.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Crear una instancia de RegistroRequest pasando todos los datos
            RegistroRequest registerRequest = new RegistroRequest(Registro.this, name, username, password, email, address);

            // Intentar registrar un nuevo usuario
            boolean success = registerRequest.registerUser();

            if (success) {
                Intent intent = new Intent(Registro.this, MainActivity.class);
                Registro.this.startActivity(intent);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(Registro.this);
                builder.setMessage("El usuario ya existe.")
                        .setNegativeButton("Inténtalo de nuevo", null)
                        .create().show();
            }
        } else if (v.getId() == R.id.login_email_btn) {
            Intent intent = new Intent(Registro.this, MainActivity.class);
            startActivity(intent);

            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(Registro.this, "La aplicación de inicio de sesión no está instalada", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

