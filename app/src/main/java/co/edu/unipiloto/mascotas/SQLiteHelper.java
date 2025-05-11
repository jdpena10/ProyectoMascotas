package co.edu.unipiloto.mascotas;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import com.google.android.gms.maps.model.LatLng;
import android.util.Log;
import android.util.Pair;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SQLiteHelper extends SQLiteOpenHelper {

    // Nombre y versión de la base de datos
    private static final String DATABASE_NAME = "Usuarios.db";
    private static final int DATABASE_VERSION = 21;

    // Tabla de usuarios
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_ADDRESS = "address";

    // Tabla de mascotas
    private static final String TABLE_PETS = "pets";
    private static final String COLUMN_PET_ID = "pet_id";
    private static final String COLUMN_PET_NAME = "pet_name";
    private static final String COLUMN_PET_TYPE = "pet_type";
    private static final String COLUMN_PET_BREED = "pet_breed";
    private static final String COLUMN_PET_AGE = "pet_age";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_PET_LAT = "pet_latitude";
    private static final String COLUMN_PET_LON = "pet_longitude";


    // Tabla de zonas seguras
    private static final String TABLE_SAFE_ZONES = "safe_zones";
    private static final String COLUMN_SAFE_ZONE_ID = "safe_zone_id";
    private static final String COLUMN_SAFE_ZONE_PET_ID = "pet_id";
    private static final String COLUMN_SAFE_ZONE_LAT = "safe_zone_lat";
    private static final String COLUMN_SAFE_ZONE_LON = "safe_zone_lon";
    private static final String COLUMN_SAFE_ZONE_RADIUS = "safe_zone_radius";

    // Tabla de sesiones de ejercicio
    private static final String TABLE_EXERCISE = "exercise_sessions";
    private static final String COLUMN_EXERCISE_ID = "exercise_id";
    private static final String COLUMN_EXERCISE_PET_ID = "pet_id";  // <- ahora usamos el ID de la mascota
    private static final String COLUMN_EXERCISE_DISTANCE = "distance"; // en km
    private static final String COLUMN_EXERCISE_DURATION = "duration"; // en minutos
    private static final String COLUMN_EXERCISE_CALORIES = "calories"; // en calorías
    private static final String COLUMN_EXERCISE_TIMESTAMP = "timestamp";



    // Tabla de vacunas
    private static final String TABLE_VACCINES = "vaccines";
    private static final String COLUMN_VACCINE_ID = "vaccine_id";
    private static final String COLUMN_VACCINE_NAME = "vaccine_name";
    private static final String COLUMN_VACCINE_DATE = "vaccine_date";
    private static final String COLUMN_NEXT_DOSE_DATE = "next_dose_date";
    private static final String COLUMN_PET_NAME2 = "pet_name";




    // Tabla de citas
    private static final String TABLE_CITAS = "citas";
    private static final String COLUMN_CITA_ID = "id";
    private static final String COLUMN_VETERINARIO = "nombre_veterinario";
    private static final String COLUMN_DIRECCION = "direccion";
    private static final String COLUMN_FECHA = "fecha";
    private static final String COLUMN_HORA = "hora";
    private static final String COLUMN_MOTIVO = "motivo";
    private static final String COLUMN_CITA_PET_ID = "pet_id";




    // Tabla de adopción de mascotas
    private static final String TABLE_ADOPTION_PETS = "adoption_pets";
    private static final String PET_ID = "id";
    private static final String PET_NAME = "name";
    private static final String PET_AGE = "age";
    private static final String PET_BREED = "breed";
    private static final String PET_HEALTH_STATUS = "health_status";
    private static final String PET_LOCATION = "location";
    private static final String PET_ADOPTION_STATUS = "adoption_status"; // Disponible / Adoptado
    private static final String USER_NAME = "user_name"; // Nombre del usuario
    private static final String USER_PHONE = "user_phone"; // Número de celular del usuario
    private static final String PET_TYPE = "pet_type";




    // Tabla de registros de alimentación
    private static final String TABLE_FOOD_LOG = "food_log";
    private static final String COLUMN_FOOD_ID = "food_id";
    private static final String COLUMN_FOOD_DATE = "food_date";
    private static final String COLUMN_FOOD_TYPE = "food_type";
    private static final String COLUMN_FOOD_AMOUNT = "food_amount";






    // Tabla de cuidadores
    private static final String TABLE_CARETAKERS = "caretakers";
    private static final String COLUMN_CARETAKER_ID = "id";
    private static final String COLUMN_CARETAKER_USER_ID = "user_id"; // FK que hace referencia a users
    private static final String COLUMN_CARETAKER_EMAIL = "email";  // Trae el email del usuario
    private static final String COLUMN_CARETAKER_ADDRESS = "address";  // Trae la dirección del usuario
    private static final String COLUMN_CARETAKER_PROFILE_PICTURE = "profile_picture";
    private static final String COLUMN_CARETAKER_AVAILABILITY = "availability";
    private static final String COLUMN_CARETAKER_PHONE_NUMBER = "phone_number";




    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_USERNAME + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_ADDRESS + " TEXT)";
        db.execSQL(CREATE_USERS_TABLE);

        String CREATE_PETS_TABLE = "CREATE TABLE " + TABLE_PETS + " (" +
                COLUMN_PET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PET_NAME + " TEXT, " +
                COLUMN_PET_TYPE + " TEXT, " +
                COLUMN_PET_BREED + " TEXT, " +
                COLUMN_PET_AGE + " INTEGER, " +
                COLUMN_USER_ID + " INTEGER, " +
                COLUMN_PET_LAT + " REAL, " +
                COLUMN_PET_LON + " REAL, " +
                "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + ") ON DELETE CASCADE)";
        db.execSQL(CREATE_PETS_TABLE);

        String CREATE_SAFE_ZONES_TABLE = "CREATE TABLE " + TABLE_SAFE_ZONES + " (" +
                COLUMN_SAFE_ZONE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SAFE_ZONE_PET_ID + " INTEGER, " +
                COLUMN_SAFE_ZONE_LAT + " REAL, " +
                COLUMN_SAFE_ZONE_LON + " REAL, " +
                COLUMN_SAFE_ZONE_RADIUS + " REAL, " +
                "FOREIGN KEY(" + COLUMN_SAFE_ZONE_PET_ID + ") REFERENCES " + TABLE_PETS + "(" + COLUMN_PET_ID + ") ON DELETE CASCADE)";
        db.execSQL(CREATE_SAFE_ZONES_TABLE);


        String CREATE_EXERCISE_TABLE = "CREATE TABLE " + TABLE_EXERCISE + " (" +
                COLUMN_EXERCISE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EXERCISE_PET_ID + " INTEGER, " +
                COLUMN_EXERCISE_DISTANCE + " REAL, " +
                COLUMN_EXERCISE_DURATION + " INTEGER, " +
                COLUMN_EXERCISE_CALORIES + " INTEGER, " +
                COLUMN_EXERCISE_TIMESTAMP + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_EXERCISE_PET_ID + ") REFERENCES " + TABLE_PETS + "(" + COLUMN_PET_ID + ") ON DELETE CASCADE)";
        db.execSQL(CREATE_EXERCISE_TABLE);

        String CREATE_VACCINES_TABLE = "CREATE TABLE " + TABLE_VACCINES + " (" +
                COLUMN_VACCINE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PET_NAME + " TEXT, " +
                COLUMN_VACCINE_NAME + " TEXT, " +
                COLUMN_VACCINE_DATE + " TEXT, " +
                COLUMN_NEXT_DOSE_DATE + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_PET_NAME2 + ") REFERENCES " + TABLE_PETS + "(" + COLUMN_PET_NAME + ") ON DELETE CASCADE)";
        db.execSQL(CREATE_VACCINES_TABLE);


        // Crear tabla de citas (con referencia a una mascota)
        String createCitasTable = "CREATE TABLE " + TABLE_CITAS + " (" +
                COLUMN_CITA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_VETERINARIO + " TEXT, " +
                COLUMN_DIRECCION + " TEXT, " +
                COLUMN_FECHA + " TEXT, " +
                COLUMN_HORA + " TEXT, " +
                COLUMN_MOTIVO + " TEXT, " +
                COLUMN_CITA_PET_ID + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_CITA_PET_ID + ") REFERENCES " + TABLE_PETS + "(" + COLUMN_PET_ID + "))";
        db.execSQL(createCitasTable);



        String CREATE_ADOPTION_PETS_TABLE = "CREATE TABLE " + TABLE_ADOPTION_PETS + " (" +
                PET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PET_NAME + " TEXT, " +
                PET_AGE + " INTEGER, " +
                PET_TYPE + " TEXT, " +
                PET_BREED + " TEXT, " +
                PET_HEALTH_STATUS + " TEXT, " +
                PET_LOCATION + " TEXT, " +
                PET_ADOPTION_STATUS + " TEXT, " +
                USER_NAME + " TEXT, " +
                USER_PHONE + " TEXT)";
        db.execSQL(CREATE_ADOPTION_PETS_TABLE);


        String CREATE_FOOD_LOG_TABLE = "CREATE TABLE " + TABLE_FOOD_LOG + " (" +
                COLUMN_FOOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PET_ID + " INTEGER, " +
                COLUMN_FOOD_DATE + " TEXT, " + // formato: YYYY-MM-DD
                COLUMN_FOOD_TYPE + " TEXT, " +
                COLUMN_FOOD_AMOUNT + " REAL, " +
                "FOREIGN KEY(" + COLUMN_PET_ID + ") REFERENCES " + TABLE_PETS + "(" + COLUMN_PET_ID + ") ON DELETE CASCADE)";
        db.execSQL(CREATE_FOOD_LOG_TABLE);



        // Crear tabla de cuidadores
        String CREATE_CARETAKERS_TABLE = "CREATE TABLE " + TABLE_CARETAKERS + "("
                + COLUMN_CARETAKER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_CARETAKER_USER_ID + " INTEGER, "
                + COLUMN_CARETAKER_EMAIL + " TEXT, "
                + COLUMN_CARETAKER_ADDRESS + " TEXT, "
                + COLUMN_CARETAKER_PROFILE_PICTURE + " TEXT, "
                + COLUMN_CARETAKER_AVAILABILITY + " TEXT, "
                + COLUMN_CARETAKER_PHONE_NUMBER + " TEXT, "  // Nuevo campo de número de celular
                + "FOREIGN KEY (" + COLUMN_CARETAKER_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + ")"
                + ")";
        db.execSQL(CREATE_CARETAKERS_TABLE);
    }

    // Si la base de datos cambia de versión, eliminar las tablas y crearlas de nuevo
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PETS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAFE_ZONES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VACCINES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADOPTION_PETS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD_LOG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARETAKERS);
        onCreate(db);
    }

    // ========== MÉTODOS CRUD PARA USUARIOS ==========

    // Método para agregar un usuario a la base de datos
    public boolean addUser(String name, String username, String password, String email, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, name);
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_ADDRESS, address);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1; // Retorna true si la inserción fue exitosa, false si falló
    }


    public Cursor getUserById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS, null);
    }

    public void updateUser(int id, String name, String username, String password, String email, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_ADDRESS, address);
        db.update(TABLE_USERS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public int getUserIdByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM usuarios WHERE username = ?", new String[]{username});

        if (cursor.moveToFirst()) {
            int userId = cursor.getInt(0);
            cursor.close();
            return userId;
        } else {
            cursor.close();
            return -1; // No encontrado
        }
    }


    public boolean deleteUser(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_USERS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsDeleted > 0; // Retorna true si se eliminó al menos un usuario
    }


    // Método para verificar si un usuario ya existe en la base de datos
    public boolean checkUserExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});

        boolean exists = false;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                exists = cursor.getInt(0) > 0; // Si el conteo es mayor a 0, el usuario existe
            }
            cursor.close();
        }
        db.close();
        return exists;
    }





    public String getUserEmail(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT email FROM " + TABLE_USERS + " WHERE id = ?", new String[]{String.valueOf(userId)});
        String email = null;
        if (cursor.moveToFirst()) {
            email = cursor.getString(0); // 0 porque es la primera columna del SELECT
        }
        cursor.close();
        db.close();
        return email;
    }



    //obtener el nombre, direccion y email a partir del userid
    public Cursor getUserData(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT name, address, email FROM users WHERE id = ?",
                new String[]{String.valueOf(userId)}
        );
    }


    // ========== MÉTODOS CRUD PARA MASCOTAS ==========

    public long insertPet(String petName, String petType, String petBreed, int petAge, int userId, double lat, double lon) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PET_NAME, petName);
        values.put(COLUMN_PET_TYPE, petType);
        values.put(COLUMN_PET_BREED, petBreed);
        values.put(COLUMN_PET_AGE, petAge);
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_PET_LAT, lat);
        values.put(COLUMN_PET_LON, lon);

        long petId = db.insert(TABLE_PETS, null, values);
        db.close();

        return petId; // Retorna el ID de la mascota insertada
    }




    public Cursor getPetById(int petId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PETS + " WHERE " + COLUMN_PET_ID + " = ?", new String[]{String.valueOf(petId)});
    }

    public Cursor getAllPetsByUserId(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PETS + " WHERE " + COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)});
    }

    public void updatePet(int petId, String petName, String petType, String petBreed, int petAge,
                          int userId, double petLatitude, double petLongitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PET_NAME, petName);
        values.put(COLUMN_PET_TYPE, petType);
        values.put(COLUMN_PET_BREED, petBreed);
        values.put(COLUMN_PET_AGE, petAge);
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_PET_LAT, petLatitude);
        values.put(COLUMN_PET_LON, petLongitude);

        db.update(TABLE_PETS, values, COLUMN_PET_ID + " = ?", new String[]{String.valueOf(petId)});
        db.close();
    }


    public void deletePet(int petId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PETS, COLUMN_PET_ID + " = ?", new String[]{String.valueOf(petId)});
        db.close();
    }


    public void actualizarUbicacionMascota(int petId, double latitud, double longitud) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("pet_latitude", latitud);
        values.put("pet_longitude", longitud);

        int filasActualizadas = db.update("pets", values, "pet_id = ?", new String[]{String.valueOf(petId)});

        if (filasActualizadas > 0) {
            Log.d("SQLiteHelper", "Ubicación actualizada: " + latitud + ", " + longitud);
        } else {
            Log.e("SQLiteHelper", "No se pudo actualizar la ubicación.");
        }

        db.close();
    }

    // Método para obtener la ubicación de una mascota
    public LatLng obtenerUbicacionMascota(int petId) {
        SQLiteDatabase db = this.getReadableDatabase();
        LatLng ubicacion = null;

        // Consulta corregida usando los nombres correctos de la tabla y columnas
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_PET_LAT + ", " + COLUMN_PET_LON +
                        " FROM " + TABLE_PETS + " WHERE " + COLUMN_PET_ID + " = ?",
                new String[]{String.valueOf(petId)});

        if (cursor != null && cursor.moveToFirst()) {
            double latitud = cursor.getDouble(0);
            double longitud = cursor.getDouble(1);
            ubicacion = new LatLng(latitud, longitud);
            Log.d("SQLiteHelper", "Ubicación obtenida: " + latitud + ", " + longitud);
        } else {
            Log.e("SQLiteHelper", "No se encontró la ubicación en la base de datos.");
        }

        if (cursor != null) cursor.close();
        db.close();
        return ubicacion;
    }


        public List<Mascota> obtenerMascotas(int userId) {
            List<Mascota> mascotas = new ArrayList<>();
            SQLiteDatabase db = this.getReadableDatabase();

            // Consulta SQL con el filtro para obtener solo las mascotas del usuario autenticado
            String query = "SELECT " + COLUMN_PET_ID + ", " + COLUMN_PET_NAME + ", " + COLUMN_PET_TYPE + ", " +
                    COLUMN_PET_BREED + ", " + COLUMN_PET_AGE + ", " + COLUMN_USER_ID + ", " +
                    COLUMN_PET_LAT + ", " + COLUMN_PET_LON + " FROM " + TABLE_PETS +
                    " WHERE " + COLUMN_USER_ID + " = ?"; // Filtro para el userId

            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

            if (cursor.moveToFirst()) {
                do {
                    Mascota mascota = new Mascota(
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PET_ID)),   // ID
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PET_NAME)), // Nombre
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PET_TYPE)), // Tipo
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PET_BREED)), // Raza
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PET_AGE)),   // Edad
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)),   // userId
                            cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PET_LAT)), // Latitud
                            cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PET_LON))  // Longitud
                    );
                    mascotas.add(mascota);

                    // Log para cada mascota obtenida
                    Log.d("Mascota", "ID: " + mascota.getId() +
                            ", Nombre: " + mascota.getNombre() +
                            ", Tipo: " + mascota.getTipo() +
                            ", Raza: " + mascota.getRaza() +
                            ", Edad: " + mascota.getEdad() +
                            ", Lat: " + mascota.getLatitud() +
                            ", Lon: " + mascota.getLongitud());
                } while (cursor.moveToNext());
            } else {
                Log.d("Mascota", "No se encontraron mascotas para el usuario con ID: " + userId);
            }

            cursor.close();
            db.close(); // Cerrar la base de datos después de la consulta
            return mascotas;
        }

    public void eliminarMascota(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PETS, COLUMN_PET_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }




    //Zonas seguras
    // Método para insertar una zona segura
    public void insertSafeZone(int petId, double lat, double lon, double radius) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SAFE_ZONE_PET_ID, petId);
        values.put(COLUMN_SAFE_ZONE_LAT, lat);
        values.put(COLUMN_SAFE_ZONE_LON, lon);
        values.put(COLUMN_SAFE_ZONE_RADIUS, radius);
        db.insert(TABLE_SAFE_ZONES, null, values);
        db.close();
    }

    // Método para obtener la zona segura de una mascota
    public SafeZone getSafeZone(int petId) {
        SQLiteDatabase db = this.getReadableDatabase();
        SafeZone safeZone = null;

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SAFE_ZONES + " WHERE " + COLUMN_SAFE_ZONE_PET_ID + " = ?", new String[]{String.valueOf(petId)});

        if (cursor != null && cursor.moveToFirst()) {
            try {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SAFE_ZONE_ID));
                double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SAFE_ZONE_LAT));
                double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SAFE_ZONE_LON));
                float radius = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_SAFE_ZONE_RADIUS)); // Se ajusta a float

                safeZone = new SafeZone(id, latitude, longitude, radius);
            } catch (IllegalArgumentException e) {
                Log.e("SQLiteError", "Columna no encontrada: " + e.getMessage());
            } finally {
                cursor.close();
            }
        }

        return safeZone;
    }



    // Método para actualizar la zona segura de una mascota
    public void updateSafeZone(int petId, double lat, double lon, double radius) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SAFE_ZONE_LAT, lat);
        values.put(COLUMN_SAFE_ZONE_LON, lon);
        values.put(COLUMN_SAFE_ZONE_RADIUS, radius);
        db.update(TABLE_SAFE_ZONES, values, COLUMN_SAFE_ZONE_PET_ID + " = ?", new String[]{String.valueOf(petId)});
        db.close();
    }


    public void deleteSafeZone(int petId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete(TABLE_SAFE_ZONES, COLUMN_SAFE_ZONE_PET_ID + " = ?", new String[]{String.valueOf(petId)});
        db.close();

        if (deletedRows > 0) {
            Log.d("SQLiteHelper", "Zona segura eliminada para petId: " + petId);
        } else {
            Log.w("SQLiteHelper", "No se encontró una zona segura para eliminar con petId: " + petId);
        }
    }

    public String getUserEmailByUserId(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String email = null;

        Cursor cursor = db.rawQuery("SELECT email FROM users WHERE id = ?", new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            email = cursor.getString(0);
        }

        cursor.close();
        db.close();
        return email;
    }



    //Metodo para insertar un ejercicio
    public void insertarSesionEjercicio(int mascotaId, double distancia, int duracion, int calorias, String timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("pet_id", mascotaId);
        values.put("distance", distancia);
        values.put("duration", duracion);
        values.put("calories", calorias);
        values.put("timestamp", timestamp);

        long resultado = db.insert("exercise_sessions", null, values);

        if (resultado != -1) {
            Log.d("DB_INSERT", "Sesión guardada correctamente: mascotaId=" + mascotaId + ", distancia=" + distancia +
                    ", duración=" + duracion + ", calorías=" + calorias + ", timestamp=" + timestamp);
        } else {
            Log.e("DB_INSERT", "Error al guardar la sesión");
        }

        db.close();
    }

    public List<SesionEjercicio> obtenerSesionesFiltradas(String nombreMascota, String fechaInicio, String fechaFin, String tipoReporte) {
        List<SesionEjercicio> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Buscar el ID de la mascota a partir de su nombre
        int mascotaId = obtenerIdMascotaPorNombre(nombreMascota);
        if (mascotaId == -1) {
            Log.e("DB_QUERY", "No se encontró mascota con nombre: " + nombreMascota);
            return lista; // Devuelve lista vacía si no se encuentra
        }

        // Si las fechas están en formato incorrecto, puede que queramos filtrar en Java después
        // Cambia el formato de las fechas que se usen en el BETWEEN para evitar errores de comparación
        String query = "SELECT * FROM " + TABLE_EXERCISE + " WHERE " + COLUMN_EXERCISE_PET_ID + " = ? AND " +
                COLUMN_EXERCISE_TIMESTAMP + " BETWEEN ? AND ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(mascotaId), fechaInicio, fechaFin});

        if (cursor.moveToFirst()) {
            do {
                SesionEjercicio sesion = new SesionEjercicio();

                // Validar que el índice de la columna no sea -1 antes de usarlo
                int indexId = cursor.getColumnIndex("id");
                if (indexId != -1) {
                    sesion.setId(cursor.getInt(indexId));
                }

                int indexPetId = cursor.getColumnIndex(COLUMN_EXERCISE_PET_ID);
                if (indexPetId != -1) {
                    sesion.setPetId(cursor.getInt(indexPetId));
                }

                int indexDistance = cursor.getColumnIndex(COLUMN_EXERCISE_DISTANCE);
                if (indexDistance != -1) {
                    sesion.setDistancia(cursor.getDouble(indexDistance));
                }

                int indexDuration = cursor.getColumnIndex(COLUMN_EXERCISE_DURATION);
                if (indexDuration != -1) {
                    sesion.setDuracion(cursor.getInt(indexDuration));
                }

                int indexCalories = cursor.getColumnIndex(COLUMN_EXERCISE_CALORIES);
                if (indexCalories != -1) {
                    sesion.setCalorias(cursor.getInt(indexCalories));
                }

                int indexTimestamp = cursor.getColumnIndex(COLUMN_EXERCISE_TIMESTAMP);
                if (indexTimestamp != -1) {
                    sesion.setTimestamp(cursor.getString(indexTimestamp));
                }

                lista.add(sesion);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        // Filtrar las sesiones por las fechas en Java (si la fecha de la base de datos no es en formato adecuado)
        return obtenerSesionesFiltradasEnJava(lista, fechaInicio, fechaFin);
    }

    public List<SesionEjercicio> obtenerSesionesFiltradasEnJava(List<SesionEjercicio> todas, String fechaInicio, String fechaFin) {
        List<SesionEjercicio> filtradas = new ArrayList<>();

        // Log para imprimir la consulta y los parámetros
        Log.d("DB_QUERY", "Consulta: SELECT * FROM " + TABLE_EXERCISE + " WHERE " + COLUMN_EXERCISE_PET_ID + " = ? AND " +
                COLUMN_EXERCISE_TIMESTAMP + " BETWEEN ? AND ?");
        Log.d("DB_QUERY", "Parametros: "  + ", " + fechaInicio + ", " + fechaFin);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        try {
            Date inicio = sdf.parse(fechaInicio);
            Date fin = sdf.parse(fechaFin);

            // Depuración para imprimir las fechas de filtro
            Log.d("Fecha Filtro", "Fecha Inicio: " + fechaInicio + ", Fecha Fin: " + fechaFin);

            for (SesionEjercicio sesion : todas) {
                // Depuración para imprimir el timestamp de cada sesión
                Log.d("Fecha Sesion", "Sesion Timestamp: " + sesion.getTimestamp());

                Date fechaSesion = sdf.parse(sesion.getTimestamp());
                if (fechaSesion != null && !fechaSesion.before(inicio) && !fechaSesion.after(fin)) {
                    filtradas.add(sesion);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return filtradas;
    }




    public int obtenerIdMascotaPorNombre(String nombreMascota) {
        SQLiteDatabase db = this.getReadableDatabase();
        int id = -1;

        // Usar los nombres reales: pet_id y pet_name
        Cursor cursor = db.rawQuery("SELECT pet_id FROM pets WHERE pet_name = ?", new String[]{nombreMascota});

        if (cursor.moveToFirst()) {
            int indexId = cursor.getColumnIndex("pet_id");
            if (indexId != -1) {
                id = cursor.getInt(indexId);
            }
        }

        cursor.close();

        return id;
    }



    public String obtenerIdMascotaPorNombre(int petId) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Realizamos la consulta a la tabla 'pets' para obtener el nombre de la mascota con el ID dado
        Cursor cursor = db.query(
                TABLE_PETS,  // Nombre de la tabla de mascotas
                new String[]{COLUMN_PET_NAME},  // La columna que deseamos obtener (el nombre de la mascota)
                COLUMN_PET_ID + " = ?",  // Condición de búsqueda, usando el petId
                new String[]{String.valueOf(petId)},  // Argumento para la búsqueda (convertimos petId a String)
                null, null, null);

        // Verificar si el cursor no es nulo y si se ha movido al primer resultado
        if (cursor != null && cursor.moveToFirst()) {
            // Obtener el índice de la columna 'pet_name'
            int columnIndex = cursor.getColumnIndex(COLUMN_PET_NAME);
            if (columnIndex != -1) {
                // Si la columna existe, devolvemos el nombre de la mascota
                return cursor.getString(columnIndex);
            } else {
                // Si la columna no se encuentra, logueamos un error
                Log.e("DB_ERROR", "Columna 'pet_name' no encontrada en la tabla 'pets'.");
                return null;
            }
        }

        // Si el cursor es nulo o no hay resultados, se devuelve null
        return null;
    }




    public List<ExerciseSession> getAllExerciseSessions() {
        List<ExerciseSession> sessions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Log.d("DB_DEBUG", "Abriendo base de datos para lectura de sesiones de ejercicio.");

        Cursor cursor = db.query(
                "exercise_sessions",
                null, // Todas las columnas
                null,
                null,
                null,
                null,
                "timestamp DESC" // Ordenar por fecha más reciente
        );

        if (cursor != null) {
            Log.d("DB_DEBUG", "Cursor no es nulo. Total de filas: " + cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("exercise_id"));
                    int petId = cursor.getInt(cursor.getColumnIndexOrThrow("pet_id"));
                    double distance = cursor.getDouble(cursor.getColumnIndexOrThrow("distance"));
                    int duration = cursor.getInt(cursor.getColumnIndexOrThrow("duration"));
                    int calories = cursor.getInt(cursor.getColumnIndexOrThrow("calories"));
                    String timestamp = cursor.getString(cursor.getColumnIndexOrThrow("timestamp"));

                    Log.d("DB_DEBUG", "Fila leída - ID: " + id + ", Pet ID: " + petId + ", Distancia: " + distance +
                            ", Duración: " + duration + ", Calorías: " + calories + ", Timestamp: " + timestamp);

                    // Asegúrate de pasar el dbHelper correctamente
                    ExerciseSession session = new ExerciseSession(id, petId, distance, duration, calories, timestamp, this);
                    sessions.add(session);
                } while (cursor.moveToNext());
            }

            cursor.close();
        } else {
            Log.d("DB_DEBUG", "Cursor es nulo. No se pudo consultar la tabla.");
        }

        Log.d("DB_DEBUG", "Total de sesiones cargadas: " + sessions.size());

        return sessions;
    }




    /*-------------------------------------------------------------------------------------------------------------------*/
    // Método para insertar una vacuna
    public void insertarVacuna(SQLiteDatabase db, int petId, String vaccineName, String vaccineDate, String nextDoseDate) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PET_ID, petId);
        values.put(COLUMN_VACCINE_NAME, vaccineName);
        values.put(COLUMN_VACCINE_DATE, vaccineDate);
        values.put(COLUMN_NEXT_DOSE_DATE, nextDoseDate);

        db.insert(TABLE_VACCINES, null, values);
    }

    // Método para obtener vacunas de una mascota
    public List<String> obtenerVacunasDeMascota(SQLiteDatabase db, int petId) {
        List<String> vacunas = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_VACCINES + " WHERE " + COLUMN_PET_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(petId)});

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VACCINE_NAME));
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VACCINE_DATE));
                String proxima = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NEXT_DOSE_DATE));
                vacunas.add("Vacuna: " + nombre + ", Fecha: " + fecha + ", Próxima: " + proxima);
            }
            cursor.close();
        }

        return vacunas;
    }

    // Método para eliminar una vacuna por ID
    public void eliminarVacuna(SQLiteDatabase db, int vaccineId) {
        db.delete(TABLE_VACCINES, COLUMN_VACCINE_ID + " = ?", new String[]{String.valueOf(vaccineId)});
    }





    public List<String> obtenerVacunasConMascotas(SQLiteDatabase db) {
        List<String> listaVacunas = new ArrayList<>();

        // Consulta SQL para obtener las vacunas y el nombre de la mascota
        String query = "SELECT vaccines.vaccine_name, vaccines.vaccine_date, vaccines.next_dose_date, pets.pet_name " +
                "FROM " + TABLE_VACCINES + " vaccines " +
                "JOIN " + TABLE_PETS + " pets ON vaccines." + COLUMN_PET_NAME + " = pets." + COLUMN_PET_NAME;

        // Ejecutar la consulta
        Cursor cursor = db.rawQuery(query, null);

        // Verificar si se obtuvieron resultados
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Obtener los valores de cada columna
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VACCINE_NAME));
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VACCINE_DATE));
                String proxima = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NEXT_DOSE_DATE));
                String petName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PET_NAME));

                // Crear un string con los datos de la vacuna y la mascota
                String item = "Pet: " + petName + " - Vaccine: " + nombre +
                        " (Date: " + fecha + ", Next Dose: " + proxima + ")";

                // Añadir el item a la lista
                listaVacunas.add(item);
            } while (cursor.moveToNext());
            cursor.close(); // Cerrar el cursor después de usarlo
        }

        return listaVacunas;
    }

    public void insertarVacuna(String nombreVacuna, String fechaVacuna, String proximaDosis, String nombreMascota) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_VACCINE_NAME, nombreVacuna);
        values.put(COLUMN_VACCINE_DATE, fechaVacuna);
        values.put(COLUMN_NEXT_DOSE_DATE, proximaDosis);
        values.put(COLUMN_PET_NAME2, nombreMascota);

        db.insert(TABLE_VACCINES, null, values);
        db.close();
    }





    // Insertar una cita asociada a una mascota
    public void guardarCita(String nombreVet, String direccion, String fecha, String hora, String motivo, int petId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_VETERINARIO, nombreVet);
        values.put(COLUMN_DIRECCION, direccion);
        values.put(COLUMN_FECHA, fecha);
        values.put(COLUMN_HORA, hora);
        values.put(COLUMN_MOTIVO, motivo);
        values.put(COLUMN_CITA_PET_ID, petId);
        db.insert(TABLE_CITAS, null, values);
        db.close();
    }



    // Obtener citas con información de la mascota
    public List<Cita> obtenerCitas() {
        List<Cita> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT c.*, p." + COLUMN_PET_NAME + " FROM " + TABLE_CITAS + " c " +
                "JOIN " + TABLE_PETS + " p ON c." + COLUMN_CITA_PET_ID + " = p." + COLUMN_PET_ID +
                " ORDER BY c." + COLUMN_FECHA + ", c." + COLUMN_HORA;

        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            String nombreVet = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VETERINARIO));
            String direccion = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIRECCION));
            String fecha = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA));
            String hora = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HORA));
            String motivo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MOTIVO));
            String nombreMascota = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PET_NAME));

            lista.add(new Cita(nombreVet, direccion, fecha, hora, motivo, nombreMascota));
        }

        cursor.close();
        db.close();
        return lista;
    }









    /*---------------------------------------------------------------------------------------------*/
    //Adocpcion de mascotas

    // REGISTRAR UNA NUEVA MASCOTA CON LA INFORMACIÓN DEL USUARIO
    public boolean addAdoptionPet(String petName, int petAge, String petType, String petBreed, String petHealthStatus,
                                  String petLocation, String petAdoptionStatus, String userName, String userPhone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PET_NAME, petName);
        values.put(PET_AGE, petAge);
        values.put(PET_TYPE, petType);
        values.put(PET_BREED, petBreed);
        values.put(PET_HEALTH_STATUS, petHealthStatus);
        values.put(PET_LOCATION, petLocation);
        values.put(PET_ADOPTION_STATUS, petAdoptionStatus);
        values.put(USER_NAME, userName); // Guardar el nombre del usuario
        values.put(USER_PHONE, userPhone); // Guardar el número de celular del usuario

        long result = db.insert(TABLE_ADOPTION_PETS, null, values);
        db.close();
        return result != -1; // true si se insertó correctamente
    }


    public List<AdoptionPet> filterAdoptionPets(Integer age, String type, String breed) {
        List<AdoptionPet> petList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Armar la consulta dinámicamente
        String selection = "";
        List<String> selectionArgsList = new ArrayList<>();

        if (age != null) {
            selection += PET_AGE + " = ?";
            selectionArgsList.add(String.valueOf(age));
        }

        if (type != null && !type.isEmpty()) {
            if (!selection.isEmpty()) selection += " AND ";
            selection += PET_TYPE + " LIKE ?";
            selectionArgsList.add("%" + type + "%");
        }

        if (breed != null && !breed.isEmpty()) {
            if (!selection.isEmpty()) selection += " AND ";
            selection += "LOWER(" + PET_BREED + ") LIKE ?";
            selectionArgsList.add("%" + breed.toLowerCase() + "%");
        }

        String[] selectionArgs = selectionArgsList.toArray(new String[0]);

        Cursor cursor = db.query(
                TABLE_ADOPTION_PETS,
                null, // columnas (null = todas)
                selection.isEmpty() ? null : selection, // condición WHERE
                selectionArgs, // valores del WHERE
                null, // groupBy
                null, // having
                null  // orderBy
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                AdoptionPet pet = new AdoptionPet(
                        cursor.getString(cursor.getColumnIndexOrThrow(PET_NAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(PET_AGE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(PET_TYPE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(PET_BREED)),
                        cursor.getString(cursor.getColumnIndexOrThrow(PET_ADOPTION_STATUS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(PET_HEALTH_STATUS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(PET_LOCATION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(USER_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(USER_PHONE))
                );

                petList.add(pet);
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return petList;
    }


    // FILTRAR MASCOTAS POR CRITERIOS (raza, ubicación, estado de adopción)
    public Cursor searchAdoptionPets(String breed, String location, String adoptionStatus) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_ADOPTION_PETS + " WHERE " +
                PET_BREED + " LIKE ? AND " +
                PET_LOCATION + " LIKE ? AND " +
                PET_ADOPTION_STATUS + " = ?";
        return db.rawQuery(query, new String[]{"%" + breed + "%", "%" + location + "%", adoptionStatus});
    }

    // ACTUALIZAR EL ESTADO DE UNA MASCOTA A "ADOPTADO"
    public boolean updatePetStatus(int petId, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PET_ADOPTION_STATUS, newStatus);

        int rowsAffected = db.update(TABLE_ADOPTION_PETS, values, PET_ID + " = ?", new String[]{String.valueOf(petId)});
        db.close();
        return rowsAffected > 0; // true si se actualizó correctamente
    }

    public List<AdoptionPet> getAllAdoptionPets() {
        List<AdoptionPet> petsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM adoption_pets", null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                int age = cursor.getInt(cursor.getColumnIndexOrThrow("age"));
                String breed = cursor.getString(cursor.getColumnIndexOrThrow("breed"));
                String type = cursor.getString(cursor.getColumnIndexOrThrow("pet_type"));
                String adoptionStatus = cursor.getString(cursor.getColumnIndexOrThrow("adoption_status"));
                String healthStatus = cursor.getString(cursor.getColumnIndexOrThrow("health_status"));
                String location = cursor.getString(cursor.getColumnIndexOrThrow("location"));
                String userName = cursor.getString(cursor.getColumnIndexOrThrow("user_name"));
                String userPhone = cursor.getString(cursor.getColumnIndexOrThrow("user_phone"));

                AdoptionPet pet = new AdoptionPet(name, age, breed, type, adoptionStatus, healthStatus, location, userName, userPhone);
                petsList.add(pet);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return petsList;
    }





    //------------------------------------------------------------------------------------------------------------------
    //Metodos de la tabla de alimentacion
    public void agregarRegistroAlimentacion(int petId, String fecha, String tipo, double cantidad) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PET_ID, petId);
        values.put(COLUMN_FOOD_DATE, fecha);
        values.put(COLUMN_FOOD_TYPE, tipo);
        values.put(COLUMN_FOOD_AMOUNT, cantidad);
        db.insert(TABLE_FOOD_LOG, null, values);
        db.close();
    }
    public List<String> sugerirHorarios(String raza, int edad) {
        List<String> horarios = new ArrayList<>();

        if (edad < 1) { // cachorro
            horarios.add("08:00 AM");
            horarios.add("01:00 PM");
            horarios.add("06:00 PM");
        } else if (edad < 7) { // adulto
            horarios.add("09:00 AM");
            horarios.add("06:00 PM");
        } else { // senior
            horarios.add("10:00 AM");
            horarios.add("05:00 PM");
        }

        // Podrías ajustar por raza también
        if (raza.equalsIgnoreCase("chihuahua")) {
            horarios.add("08:00 PM"); // ejemplo para razas pequeñas
        }

        return horarios;
    }
    public boolean detectarCambiosEnHabitos(int petId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT AVG(" + COLUMN_FOOD_AMOUNT + ") as promedio FROM " + TABLE_FOOD_LOG +
                " WHERE " + COLUMN_PET_ID + " = ? AND " + COLUMN_FOOD_DATE + " >= date('now','-7 days')";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(petId)});

        if (cursor.moveToFirst()) {
            double promedio = cursor.getDouble(cursor.getColumnIndexOrThrow("promedio"));

            // Suponiendo que el promedio normal es entre 150-250 gramos
            if (promedio < 100 || promedio > 300) {
                cursor.close();
                return true;
            }
        }
        cursor.close();
        return false;
    }




    //--------------------------------------------------------------------------------------------------------------------
    //tabla de cuidadores
    public boolean insertCaretaker(int userId, String email, String address, String profilePicture, String phoneNumber, String availability) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CARETAKER_USER_ID, userId); // Asociar el cuidador con el usuario
        values.put(COLUMN_CARETAKER_EMAIL, email); // Traer email del usuario
        values.put(COLUMN_CARETAKER_ADDRESS, address); // Traer dirección del usuario
        values.put(COLUMN_CARETAKER_PROFILE_PICTURE, profilePicture);
        values.put(COLUMN_CARETAKER_PHONE_NUMBER, phoneNumber);
        values.put(COLUMN_CARETAKER_AVAILABILITY, availability);

        long result = db.insert(TABLE_CARETAKERS, null, values);
        db.close();
        return result != -1; // true si fue exitoso, false si falló
    }




    // Verificar si ya hay un cuidador registrado con ese user_id
    public boolean isCaretakerRegistered(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM caretakers WHERE user_id = ?", new String[]{String.valueOf(userId)});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }


    public ArrayList<String> obtenerTodosLosCuidadores() {
        ArrayList<String> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CARETAKERS, null);

        if (cursor.moveToFirst()) {
            do {
                //int userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                String availability = cursor.getString(cursor.getColumnIndexOrThrow("availability"));
                String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("phone_number")); // Recuperar el número de celular

                // Agregar los detalles del cuidador incluyendo el número de celular
                String cuidador = "\nEmail: " + email + "\nDirección: " + address +
                        "\nDisponibilidad: " + availability + "\nTeléfono: " + phoneNumber;
                lista.add(cuidador);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return lista;
    }


    public boolean deleteCaretaker(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_CARETAKERS, COLUMN_CARETAKER_USER_ID + " = ?", new String[]{String.valueOf(userId)});
        db.close();
        return rowsDeleted > 0; // true si se eliminó al menos una fila
    }
}




