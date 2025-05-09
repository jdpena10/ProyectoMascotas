package co.edu.unipiloto.mascotas;

public class ExerciseSession {
    private int id;
    private int petId;
    private double distance;
    private int duration;
    private int calories;
    private String timestamp;

    private SQLiteHelper dbHelper; // Instancia de la clase dbHelper para obtener el nombre de la mascota

    public ExerciseSession(int id, int petId, double distance, int duration, int calories, String timestamp, SQLiteHelper dbHelper) {
        this.id = id;
        this.petId = petId;
        this.distance = distance;
        this.duration = duration;
        this.calories = calories;
        this.timestamp = timestamp;
        this.dbHelper = dbHelper; // Asignar el dbHelper en el constructor
    }

    // Getters
    public int getId() { return id; }
    public int getPetId() { return petId; }
    public double getDistance() { return distance; }
    public int getDuration() { return duration; }
    public int getCalories() { return calories; }
    public String getTimestamp() { return timestamp; }

    // Método para obtener el nombre de la mascota utilizando el petId
    public String getPetName() {
        return dbHelper.obtenerIdMascotaPorNombre(petId);  // Asumimos que dbHelper tiene este método
    }
}

