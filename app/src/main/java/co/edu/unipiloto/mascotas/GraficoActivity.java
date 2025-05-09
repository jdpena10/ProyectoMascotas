package co.edu.unipiloto.mascotas;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import android.graphics.Color;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;


public class GraficoActivity extends AppCompatActivity {

    private BarChart barChart;
    private SQLiteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico);

        barChart = findViewById(R.id.barChart);
        dbHelper = new SQLiteHelper(this);

        mostrarGrafico();
    }

    private void mostrarGrafico() {
        List<ExerciseSession> sesiones = dbHelper.getAllExerciseSessions();
        Collections.reverse(sesiones);

        List<BarEntry> entries = new ArrayList<>();
        List<String> etiquetas = new ArrayList<>();

        for (int i = 0; i < sesiones.size(); i++) {
            ExerciseSession sesion = sesiones.get(i);
            entries.add(new BarEntry(i, (float) sesion.getDistance()));

            String nombreMascota = sesion.getPetName();
            String etiqueta = nombreMascota + " (" + sesion.getTimestamp().split(" ")[0] + ")";
            etiquetas.add(etiqueta);
        }

        BarDataSet dataSet = new BarDataSet(entries, "Distancia por sesión (km)");
        dataSet.setColor(getResources().getColor(R.color.purple_500));
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.BLACK);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f);

        barChart.setData(barData);
        barChart.setFitBars(true);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(etiquetas));
        xAxis.setLabelRotationAngle(45f);

        YAxis leftAxis = barChart.getAxisLeft();
        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);


        barChart.animateY(1000);
        barChart.invalidate();
    }
}


