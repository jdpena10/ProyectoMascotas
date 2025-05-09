package co.edu.unipiloto.mascotas;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HistorialCitasActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CitasAdapter adapter;
    private SQLiteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        dbHelper = new SQLiteHelper(this);
        recyclerView = findViewById(R.id.recyclerCitas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Cita> citas = dbHelper.obtenerCitas();
        adapter = new CitasAdapter(citas);
        recyclerView.setAdapter(adapter);
    }
}

