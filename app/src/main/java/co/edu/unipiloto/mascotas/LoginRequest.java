package co.edu.unipiloto.mascotas;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class LoginRequest {
    // Instancia de SQLiteHelper para manejar la base de datos
    private SQLiteHelper dbHelper;

    // Constructor que recibe el contexto y lo usa para inicializar SQLiteHelper
    public LoginRequest(Context context) {
        this.dbHelper = new SQLiteHelper(context);
    }

    /**
     * Metodo para autenticar un usuario verificando su nombre de usuario y contrasena en la base de datos.
     * @param username Nombre de usuario ingresado.
     * @param password Contrasena ingresada.
     * @return true si las credenciales son validas, false en caso contrario.
     */
    public boolean authenticateUser(String username, String password) {
        // Se obtiene la base de datos en modo lectura
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Consulta SQL para verificar si existe un usuario con el nombre de usuario y contrasena proporcionados
        Cursor cursor = db.rawQuery("SELECT name FROM users WHERE username = ? AND password = ?",
                new String[]{username, password});

        // Se verifica si la consulta devuelve algún resultado
        boolean isAuthenticated = cursor.moveToFirst();
        String name = isAuthenticated ? cursor.getString(0) : null;

        // Se cierran el cursor y la conexión a la base de datos
        cursor.close();
        db.close();
        return isAuthenticated;
    }

    /**
     * Metodo para obtener el nombre de un usuario a partir de su nombre de usuario.
     * @param username Nombre de usuario a buscar.
     * @return El nombre del usuario si existe, o null si no se encuentra.
     */
    public String getUserName(String username) {
        // Se obtiene la base de datos en modo lectura
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Consulta SQL para obtener el nombre del usuario
        Cursor cursor = db.rawQuery("SELECT name FROM users WHERE username = ?",
                new String[]{username});

        // Se extrae el nombre si el usuario existe
        String name = null;
        if (cursor.moveToFirst()) {
            name = cursor.getString(0);
        }

        // Se cierran el cursor y la conexión a la base de datos
        cursor.close();
        db.close();
        return name;
    }

    /**
     * Metodo para obtener el ID de usuario a partir de su nombre de usuario.
     * @param username Nombre de usuario a buscar.
     * @return El ID del usuario si existe, o -1 si no se encuentra.
     */
    public int getUserId(String username) {
        // Se obtiene la base de datos en modo lectura
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Consulta SQL para obtener el user_id del usuario
        Cursor cursor = db.rawQuery("SELECT id FROM users WHERE username = ?",
                new String[]{username});

        // Se extrae el user_id si el usuario existe
        int userId = -1; // Valor por defecto si no se encuentra el usuario
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }

        // Se cierran el cursor y la conexión a la base de datos
        cursor.close();
        db.close();
        return userId;
    }
}


