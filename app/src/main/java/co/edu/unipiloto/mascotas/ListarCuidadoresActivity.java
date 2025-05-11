package co.edu.unipiloto.mascotas;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ListarCuidadoresActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> listaCuidadores, listaOriginal;
    private SQLiteHelper dbHelper;
    private Button btnRegistrarse;
    private EditText etBuscarDireccion;
    private Map<String, Address> mapaCuidadores = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_cuidadores);

        listView = findViewById(R.id.listViewCuidadores);
        btnRegistrarse = findViewById(R.id.btnRegistrarseCuidador);
        etBuscarDireccion = findViewById(R.id.etBuscarDireccion);
        dbHelper = new SQLiteHelper(this);

        // Obtener lista original de cuidadores
        listaOriginal = dbHelper.obtenerTodosLosCuidadores();
        listaCuidadores = new ArrayList<>(listaOriginal); // copia editable

        // Crear adaptador
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaCuidadores);
        listView.setAdapter(adapter);

        // Acción al hacer clic en botón de registro
        btnRegistrarse.setOnClickListener(v -> {
            Intent intent = new Intent(ListarCuidadoresActivity.this, RegistrarCuidadorActivity.class);
            startActivity(intent);
        });

        // Filtrar por dirección en tiempo real
        etBuscarDireccion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String texto = s.toString();
                filtrarPorDireccion(texto);
                obtenerCoordenadasDesdeDireccion(texto); // Se mantiene si deseas ver coordenadas del texto ingresado
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Convertir todas las direcciones de los cuidadores a coordenadas y guardarlas en el mapa
        obtenerCoordenadasDeCuidadores();
    }

    private void filtrarPorDireccion(String texto) {
        listaCuidadores.clear();
        for (String cuidador : listaOriginal) {
            Address address = mapaCuidadores.get(cuidador);
            if (address != null) {
                String localidad = address.getLocality() != null ? address.getLocality() : "";
                String subLocalidad = address.getSubLocality() != null ? address.getSubLocality() : "";

                if (localidad.toLowerCase().contains(texto.toLowerCase()) ||
                        subLocalidad.toLowerCase().contains(texto.toLowerCase())) {
                    listaCuidadores.add(cuidador);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void obtenerCoordenadasDesdeDireccion(String direccion) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> direcciones = geocoder.getFromLocationName(direccion, 1);
            if (direcciones != null && !direcciones.isEmpty()) {
                Address ubicacion = direcciones.get(0);
                double latitud = ubicacion.getLatitude();
                double longitud = ubicacion.getLongitude();

                Log.d("CoordenadasBusqueda", "Lat: " + latitud + ", Lon: " + longitud);
            } else {
                Log.d("CoordenadasBusqueda", "No se encontró la dirección: " + direccion);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("CoordenadasBusqueda", "Error al obtener coordenadas para: " + direccion);
        }
    }

    private void obtenerCoordenadasDeCuidadores() {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        for (String cuidador : listaOriginal) {
            String direccionCuidador = cuidador; // Ajustar si se tiene formato distinto

            try {
                List<Address> direcciones = geocoder.getFromLocationName(direccionCuidador, 1);
                if (direcciones != null && !direcciones.isEmpty()) {
                    Address ubicacion = direcciones.get(0);
                    mapaCuidadores.put(cuidador, ubicacion);

                    Log.d("CoordenadasCuidador", "Cuidador: " + cuidador +
                            " - Localidad: " + ubicacion.getLocality() +
                            " - SubLocalidad: " + ubicacion.getSubLocality());
                } else {
                    Log.d("CoordenadasCuidador", "No se encontró dirección para: " + cuidador);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("CoordenadasCuidador", "Error al obtener coordenadas para: " + cuidador);
            }
        }
    }
}

