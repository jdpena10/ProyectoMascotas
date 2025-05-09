package co.edu.unipiloto.mascotas

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class Inicio : AppCompatActivity() {

    private lateinit var tvNombre: TextView
    private lateinit var tvUsuario: TextView
    private lateinit var btnCerrarSesion: Button
    private lateinit var btnAdministracion: Button
    private lateinit var btnEditarPerfil: Button
    private lateinit var btn_registrarMascota: Button
    private lateinit var btn_buscarVeterinario: Button
    private lateinit var monitoreo_ejercicio: Button
    private lateinit var aplicar_vacuna: Button
    private lateinit var agendar_cita: Button
    private lateinit var registro_de_adopcion: Button
    private lateinit var lita_adopcion: Button
    //private lateinit var editTextNombreMascota: EditText  // EditText para el nombre de la mascota
    //private lateinit var adopcion_mascotas: Button

    private var username: String? = null  // Guardamos el usuario actualizado

    // Nuevo manejador de resultados
    private val editarPerfilLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val nuevoNombre = data?.getStringExtra("nuevoNombre")
            val nuevoUsername = data?.getStringExtra("nuevoUsername")

            if (!nuevoNombre.isNullOrEmpty()) {
                tvNombre.text = nuevoNombre
            }
            if (!nuevoUsername.isNullOrEmpty()) {
                tvUsuario.text = nuevoUsername
                username = nuevoUsername  // Actualizar el username en la actividad
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Inicializar vistas
        tvNombre = findViewById(R.id.textv_nombre)
        tvUsuario = findViewById(R.id.textv_usuario)
        btnCerrarSesion = findViewById(R.id.btn_cerrar_sesion)
        btnAdministracion = findViewById(R.id.btn_administracion)
        btnEditarPerfil = findViewById(R.id.btn_editar_perfil)
        btn_registrarMascota = findViewById(R.id.btn_registrarMascota)
        btn_buscarVeterinario = findViewById(R.id.btn_buscarVeterinario)
        monitoreo_ejercicio = findViewById(R.id.monitoreo_ejercicio)
        //aplicar_vacuna = findViewById(R.id.aplicar_vacuna)
        agendar_cita = findViewById(R.id.agendar_cita)
        registro_de_adopcion = findViewById(R.id.registro_de_adopcion)

        registro_de_adopcion.setOnClickListener {
            val intent = Intent(this, AddAdoptionPetActivity::class.java)
            startActivity(intent)
        }


        lita_adopcion = findViewById(R.id.lita_adopcion)
        lita_adopcion.setOnClickListener {
            val intent = Intent(this, AdoptionPetsListActivity::class.java)
            startActivity(intent)
        }
        //editTextNombreMascota = findViewById(R.id.editTextNombreMascota)  // Asociar el EditText
        //btnConfirmarCita = findViewById(R.id.btnConfirmarCita) // El botón para confirmar la cita
        //adopcion_mascotas = findViewById(R.id.adopcion_mascotas)

        // Inicialmente ocultamos el EditText y el botón de confirmar cita
        //editTextNombreMascota.visibility = View.GONE
        //btnConfirmarCita.visibility = View.GONE

        // Instancia de DBHelper
        val dbHelper = SQLiteHelper(this)

        btn_registrarMascota.setOnClickListener {
            val intent = Intent(this, RegistrarMascotaActivity::class.java)
            startActivity(intent)
        }

        btn_buscarVeterinario.setOnClickListener {
            val intent = Intent(this, VeterinariosActivity::class.java)
            startActivity(intent)
        }

        monitoreo_ejercicio.setOnClickListener {
            val intent = Intent(this, RegistroEjercicioActivity::class.java)
            startActivity(intent)
        }

        /*aplicar_vacuna.setOnClickListener {
            val intent = Intent(this, VaccinesActivity::class.java)
            startActivity(intent)
        }*/

        // Aquí hacemos que el EditText y el botón de confirmar cita se muestren cuando el usuario haga clic en "Agendar cita"
        agendar_cita.setOnClickListener {
            val intent = Intent(this, ActivityCita::class.java)
            startActivity(intent)
        }


        /*adopcion_mascotas.setOnClickListener {
            val intent = Intent(this, AdopcionMascotasActivity ::class.java)
            startActivity(intent)
        }*/


        val intent = intent
        val name = intent.getStringExtra("name")
        val username = intent.getStringExtra("username")
        val password = intent.getStringExtra("password")

        tvNombre.text = name
        tvUsuario.text = username

        if (username != "juan123" || password != "1234") {
            btnAdministracion.visibility = View.GONE
        }

        btnCerrarSesion.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        btnAdministracion.setOnClickListener {
            val intent = Intent(this, Administrar::class.java)
            intent.putExtra("username", username)
            intent.putExtra("name", name)
            intent.putExtra("password", password)
            startActivity(intent)
        }

        // Usar el nuevo ActivityResultLauncher en lugar de startActivityForResult()
        btnEditarPerfil.setOnClickListener {
            val intent = Intent(this, EditarPerfilActivity::class.java)
            intent.putExtra("name", name)
            intent.putExtra("username", username)
            intent.putExtra("password", password)
            editarPerfilLauncher.launch(intent)
        }
    }
}


