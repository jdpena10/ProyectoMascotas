package co.edu.unipiloto.mascotas;

import android.content.Context;
import java.util.HashMap;
import java.util.Map;

public class RegistroRequest {

    private SQLiteHelper dbHelper;
    private Map<String, String> params;

    // Constructor que toma el contexto para inicializar SQLiteHelper con los nuevos campos
    public RegistroRequest(Context context, String name, String username, String password, String email, String address) {
        dbHelper = new SQLiteHelper(context);
        params = new HashMap<>();
        params.put("name", name);
        params.put("username", username);
        params.put("password", password);
        params.put("email", email);
        params.put("address", address);
    }

    // Método para registrar un usuario en la base de datos
    public boolean registerUser() {
        // Verificamos si el usuario ya existe
        if (dbHelper.checkUserExists(params.get("username"))) {
            return false;  // El usuario ya existe
        }

        // Intentamos agregar al usuario en la base de datos con los nuevos campos
        return dbHelper.addUser(
                params.get("name"),
                params.get("username"),
                params.get("password"),
                params.get("email"),
                params.get("address")
        );
    }

    // Método para obtener los parámetros (similar a getParams de StringRequest)
    public Map<String, String> getParams() {
        return params;
    }
}

