package co.edu.unipiloto.mascotas;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResumenActivity extends AppCompatActivity {

    private TextView txtResumenDistancia, txtResumenDuracion, txtResumenCalorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen);

        txtResumenDistancia = findViewById(R.id.txt_resumen_distancia);
        txtResumenDuracion = findViewById(R.id.txt_resumen_duracion);
        txtResumenCalorias = findViewById(R.id.txt_resumen_calorias);

        double distancia = getIntent().getDoubleExtra("distancia", 0.0);
        int duracion = getIntent().getIntExtra("duracion", 0);
        int calorias = getIntent().getIntExtra("calorias", 0);

        txtResumenDistancia.setText(String.format("%.2fkm", distancia));
        txtResumenDuracion.setText((duracion / 60) + "m");
        txtResumenCalorias.setText(calorias + "cal");
    }

    public void volverAlInicio(View view) {
        finish(); // o ir a otra pantalla de inicio si tienes una
    }
}

