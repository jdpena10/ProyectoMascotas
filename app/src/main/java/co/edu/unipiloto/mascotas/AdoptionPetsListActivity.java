package co.edu.unipiloto.mascotas;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AdoptionPetsListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdoptionPetsAdapter adapter;
    private SQLiteHelper dbHelper;

    private EditText editTextAge, editTextType, editTextBreed;
    private Button buttonFilter, buttonClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adoption_pets_list);

        recyclerView = findViewById(R.id.recyclerViewPets);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dbHelper = new SQLiteHelper(this);

        editTextAge = findViewById(R.id.editTextAge);
        editTextType = findViewById(R.id.editTextType);
        editTextBreed = findViewById(R.id.editTextBreed);
        buttonFilter = findViewById(R.id.buttonFilter);
        buttonClear = findViewById(R.id.buttonClear);

        // Mostrar todas las mascotas al inicio
        loadPets(null, null, null);

        // Botón para aplicar filtros
        buttonFilter.setOnClickListener(v -> {
            Integer age = null;
            String type = null;
            String breed = null;

            String ageText = editTextAge.getText().toString().trim();
            if (!ageText.isEmpty()) {
                try {
                    age = Integer.parseInt(ageText);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Edad inválida", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            type = editTextType.getText().toString().trim();
            breed = editTextBreed.getText().toString().trim();

            if (type.isEmpty()) type = null;
            if (breed.isEmpty()) breed = null;

            loadPets(age, type, breed);
        });

        // Botón para limpiar filtros
        buttonClear.setOnClickListener(v -> {
            editTextAge.setText("");
            editTextType.setText("");
            editTextBreed.setText("");
            loadPets(null, null, null); // Cargar todas las mascotas otra vez
        });
    }

    private void loadPets(Integer age, String type, String breed) {
        List<AdoptionPet> petsList = dbHelper.filterAdoptionPets(age, type, breed);

        if (petsList != null && !petsList.isEmpty()) {
            adapter = new AdoptionPetsAdapter(petsList);
            recyclerView.setAdapter(adapter);
        } else {
            recyclerView.setAdapter(null);
            Toast.makeText(this, "No se encontraron mascotas con esos filtros", Toast.LENGTH_SHORT).show();
        }
    }
}

