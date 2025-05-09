package co.edu.unipiloto.mascotas

import android.os.Bundle
import android.content.Intent
import android.app.Activity
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast

class EditarPerfilActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var etEmail: EditText
    private lateinit var etAddress: EditText
    private lateinit var btnGuardar: Button
    private lateinit var btnCancelar: Button
    private lateinit var dbHelper: SQLiteHelper

    private var oldUsername: String? = null  // Almacenamos el usuario actual

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil)

        // Inicializar los campos de la interfaz
        etNombre = findViewById(R.id.et_nombre)
        etUsername = findViewById(R.id.et_username)
        etPassword = findViewById(R.id.et_password)
        etEmail = findViewById(R.id.et_email)
        etAddress = findViewById(R.id.et_address)
        btnGuardar = findViewById(R.id.btn_guardar)
        btnCancelar = findViewById(R.id.btn_cancelar)
        dbHelper = SQLiteHelper(this) // Inicializamos la base de datos

        // Obtener datos actuales del intent
        val nombreActual = intent.getStringExtra("name") ?: ""
        oldUsername = intent.getStringExtra("username")  // Almacenamos el usuario actual
        val passwordActual = intent.getStringExtra("password") ?: ""
        val emailActual = intent.getStringExtra("email") ?: ""
        val addressActual = intent.getStringExtra("address") ?: ""

        // Cargar los valores actuales en los campos
        etNombre.setText(nombreActual)
        etUsername.setText(oldUsername)
        etPassword.setText(passwordActual)
        etEmail.setText(emailActual)
        etAddress.setText(addressActual)

        btnGuardar.setOnClickListener {
            val nuevoNombre = etNombre.text.toString().trim()
            val nuevoUsername = etUsername.text.toString().trim()
            val nuevaPassword = etPassword.text.toString().trim()
            val nuevoEmail = etEmail.text.toString().trim()
            val nuevaAddress = etAddress.text.toString().trim()

            if (oldUsername != null && nuevoNombre.isNotEmpty() && nuevoUsername.isNotEmpty() &&
                nuevaPassword.isNotEmpty() && nuevoEmail.isNotEmpty() && nuevaAddress.isNotEmpty()) {

                val userId = dbHelper.getUserIdByUsername(oldUsername!!) // Obtener ID

                if (userId == -1) {
                    Toast.makeText(this, "Error: Usuario no encontrado", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                dbHelper.updateUser(userId, nuevoNombre, nuevoUsername, nuevaPassword, nuevoEmail, nuevaAddress)

                Toast.makeText(this, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()

                val resultIntent = Intent().apply {
                    putExtra("nuevoNombre", nuevoNombre)
                    putExtra("nuevoUsername", nuevoUsername)
                    putExtra("nuevaPassword", nuevaPassword)
                    putExtra("nuevoEmail", nuevoEmail)
                    putExtra("nuevaAddress", nuevaAddress)
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Cancelar edición
        btnCancelar.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }
}
