package co.edu.unipiloto.mascotas

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class RegistroReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")
        val address = intent.getStringExtra("address") // Nuevo campo

        if (!email.isNullOrEmpty() && !password.isNullOrEmpty() && !address.isNullOrEmpty()) {
            registrarUsuario(context, email, password, address)
        }
    }

    private fun registrarUsuario(context: Context, email: String, password: String, address: String) {
        val sqliteHelper = SQLiteHelper(context)
        val name = email.substringBefore("@") // Usar el correo como nombre predeterminado
        val username = email // Usar el email como username

        val isInserted = sqliteHelper.addUser(name, username, password, email, address)

        if (isInserted) {
            Log.d("RegistroReceiver", "Usuario registrado en SQLite: $email")
        } else {
            Log.e("RegistroReceiver", "Error al registrar usuario en SQLite")
        }
    }
}
