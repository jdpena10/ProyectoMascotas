package co.edu.unipiloto.mascotas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color
import android.os.Handler // Importa la clase correcta
import android.os.Looper
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

class RutaMapaActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var ruta: List<LatLng>
    private lateinit var nombreMascota: String
    private var marcador: Marker? = null
    private var index = 0
    private val handler = Handler(Looper.getMainLooper())
    private var isRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ruta_mapa)

        // Obtiene datos del intent
        ruta = intent.getParcelableArrayListExtra("ruta") ?: emptyList()
        nombreMascota = intent.getStringExtra("nombre_mascota") ?: "Mascota"

        // Muestra el nombre en el TextView
        val textNombreMascota = findViewById<TextView>(R.id.textNombreMascota)
        textNombreMascota.text = "Ruta de la mascota: $nombreMascota"

        // Inicializa el mapa
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_ruta) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL)

        // Desactiva edificios en 3D
        mMap.isBuildingsEnabled = false

        if (ruta.isNotEmpty()) {
            // Forzar vista completamente desde arriba
            val cameraPosition = CameraPosition.Builder()
                .target(ruta.first())
                .zoom(17f)
                .tilt(0f) // Sin inclinación
                .bearing(0f) // Sin rotación
                .build()

            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

            mMap.addPolyline(
                PolylineOptions()
                    .addAll(ruta)
                    .color(Color.BLUE)
                    .width(8f)
            )

            marcador = mMap.addMarker(
                MarkerOptions()
                    .position(ruta.first())
                    .title("Inicio - $nombreMascota")
            )

            mMap.addMarker(
                MarkerOptions()
                    .position(ruta.last())
                    .title("Fin - $nombreMascota")
            )

            moverMarcador()
        }
    }


    private fun moverMarcador() {
        if (!isRunning) {
            isRunning = true
            val runnable = object : Runnable {
                override fun run() {
                    if (index < ruta.size) {
                        marcador?.position = ruta[index]
                        index++
                        handler.postDelayed(this, 700)
                    } else {
                        index = 0
                        handler.postDelayed(this, 10)
                    }
                }
            }
            handler.post(runnable)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        isRunning = false
    }
}





