package co.edu.unipiloto.mascotas

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import android.content.Context;

class MainActivity : AppCompatActivity() {

    private lateinit var usuario: EditText
    private lateinit var contraseña: EditText
    private lateinit var registerTv: TextView
    private lateinit var btnLogin: Button
    private lateinit var loginRequest: LoginRequest  //  Instancia para autenticación
    private lateinit var dbHelper: SQLiteHelper  //  Instancia para la base de datos
    private lateinit var registroReceiver: RegistroReceiver
    private lateinit var fusedLocationClient: FusedLocationProviderClient  // Cliente de ubicación
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicialización de vistas
        usuario = findViewById(R.id.username_input)
        contraseña = findViewById(R.id.password_input)
        registerTv = findViewById(R.id.register_btn)
        btnLogin = findViewById(R.id.logib_btn)

        // Inicialización de Base de Datos y Login
        loginRequest = LoginRequest(this)  // Para autenticación
        dbHelper = SQLiteHelper(this)  // Para manejo de SQLite
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Manejo de eventos
        registerTv.setOnClickListener {
            val intentReg = Intent(this, Registro::class.java)
            startActivity(intentReg)
        }

        btnLogin.setOnClickListener {
            val username = usuario.text.toString().trim()
            val password = contraseña.text.toString().trim()

            if (loginRequest.authenticateUser(username, password)) {  // Usando loginRequest
                // Suponiendo que loginRequest devuelve el userId y nombre
                val userId = loginRequest.getUserId(username)  // Obtener el userId del usuario autenticado
                val name = loginRequest.getUserName(username) ?: "Usuario"

                // Guardar el userId en SharedPreferences
                val sharedPreferences = getSharedPreferences("usuario_pref", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putInt("user_id", userId)  // Guardamos el userId
                editor.putString("username", username)  // También puedes guardar el username si lo deseas
                editor.apply()

                // Enviar los datos al Intent para la siguiente actividad
                val intent = Intent(this, Inicio::class.java)
                intent.putExtra("name", name)
                intent.putExtra("username", username)
                intent.putExtra("password", password)
                startActivity(intent)
            } else {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Correo o contraseña incorrectos")
                    .setNegativeButton("Intenta de nuevo", null)
                    .create().show()
            }
        }

        // **Registrar dinámicamente el BroadcastReceiver**
        registroReceiver = RegistroReceiver()
        val filter = IntentFilter("co.edu.unipiloto.REGISTRO_COMPLETADO")
        ContextCompat.registerReceiver(
            this,
            registroReceiver,
            filter,
            ContextCompat.RECEIVER_NOT_EXPORTED // Solo para uso interno en la app
        )

        // Configurar ubicación en tiempo real
        configurarUbicacion()
    }


    // Configura la solicitud de ubicación en tiempo real
    private fun configurarUbicacion() {
        Log.d("MainActivity", "Configurando ubicación...")

        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
            .setMinUpdateIntervalMillis(2000)  // Mínimo 2 segundos entre actualizaciones
            .setWaitForAccurateLocation(false)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                for (location in locationResult.locations) {
                    val lat = location.latitude
                    val lon = location.longitude
                    Log.d("MainActivity", "Ubicación actualizada: Latitud = $lat, Longitud = $lon")
                    dbHelper.actualizarUbicacionMascota(1, lat, lon)  // Guardar en SQLite
                }
            }
        }

        // Verificar permisos
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("MainActivity", "Solicitando permiso de ubicación...")
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            Log.d("MainActivity", "Permiso de ubicación concedido, iniciando actualizaciones...")
            iniciarActualizacionesUbicacion()
        }
    }

    // Iniciar actualizaciones en tiempo real
    private fun iniciarActualizacionesUbicacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d("MainActivity", "Iniciando actualizaciones de ubicación...")
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        } else {
            Log.e("MainActivity", "Permiso de ubicación no concedido. No se pueden iniciar actualizaciones.")
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    }


    // Manejar la respuesta de permisos
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d("MainActivity", "Permiso de ubicación otorgado, iniciando actualizaciones...")
            iniciarActualizacionesUbicacion()
        } else {
            Log.d("MainActivity", "Permiso de ubicación denegado")
            Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(registroReceiver) // Evita fugas de memoria
        fusedLocationClient.removeLocationUpdates(locationCallback) // Detener actualizaciones al cerrar
        Log.d("MainActivity", "Deteniendo actualizaciones de ubicación y liberando recursos")
    }
}


