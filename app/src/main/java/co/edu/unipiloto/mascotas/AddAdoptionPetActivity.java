package co.edu.unipiloto.mascotas;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddAdoptionPetActivity extends AppCompatActivity {

    private EditText etPetName, etPetAge, etPetBreed, etPetHealthStatus, etPetLocation, correoUser, etUserPhone, etPetType;
    private Button btnRegisterPet;
    private SQLiteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_adoption_pet);

        // Inicializar las vistas
        etPetName = findViewById(R.id.etPetName);
        etPetAge = findViewById(R.id.etPetAge);
        etPetBreed = findViewById(R.id.etPetBreed);
        etPetHealthStatus = findViewById(R.id.etPetHealthStatus);
        etPetLocation = findViewById(R.id.etPetLocation);
        correoUser = findViewById(R.id.correoUser);
        etUserPhone = findViewById(R.id.etUserPhone);
        btnRegisterPet = findViewById(R.id.btnRegisterPet);
        etPetType = findViewById(R.id.etPetType);

        dbHelper = new SQLiteHelper(this);

        // Obtener y mostrar el correo automáticamente
        int userId = obtenerUserId();
        if (userId != -1) {
            String email = dbHelper.getUserEmail(userId);
            if (email != null) {
                correoUser.setText(email);
                correoUser.setEnabled(false); // Evitar que se edite
            } else {
                Toast.makeText(this, "No se encontró el correo del usuario", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }

        btnRegisterPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String petName = etPetName.getText().toString().trim();
                String petAgeStr = etPetAge.getText().toString().trim();
                String petType = etPetType.getText().toString().trim();
                String petBreed = etPetBreed.getText().toString().trim();
                String petHealthStatus = etPetHealthStatus.getText().toString().trim();
                String petLocation = etPetLocation.getText().toString().trim();
                String userPhone = etUserPhone.getText().toString().trim();
                String userName = correoUser.getText().toString().trim();

                if (petName.isEmpty() || petAgeStr.isEmpty() || petBreed.isEmpty() || petHealthStatus.isEmpty()
                        || petLocation.isEmpty() || userName.isEmpty() || userPhone.isEmpty()) {
                    Toast.makeText(AddAdoptionPetActivity.this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    int petAge = Integer.parseInt(petAgeStr);
                    String petAdoptionStatus = "Disponible"; // Siempre inicia como Disponible

                    boolean result = dbHelper.addAdoptionPet(petName, petAge, petType, petBreed, petHealthStatus, petLocation, petAdoptionStatus, userName, userPhone);
                    if (result) {
                        Toast.makeText(AddAdoptionPetActivity.this, "Mascota registrada con éxito", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddAdoptionPetActivity.this, "Error al registrar la mascota", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private int obtenerUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("usuario_pref", MODE_PRIVATE);
        return sharedPreferences.getInt("user_id", -1);
    }
}



