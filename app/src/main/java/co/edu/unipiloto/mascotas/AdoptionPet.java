package co.edu.unipiloto.mascotas;

// AdoptionPet.java
public class AdoptionPet {

    private String name;
    private int age;
    private String type;  // <-- Agregado
    private String breed;
    private String adoptionStatus;
    private String healthStatus;
    private String location;
    private String userName;
    private String userPhone;

    // Constructor
    public AdoptionPet(String name, int age, String type, String breed, String adoptionStatus, String healthStatus, String location, String userName, String userPhone) {
        this.name = name;
        this.age = age;
        this.type = type;
        this.breed = breed;
        this.adoptionStatus = adoptionStatus;
        this.healthStatus = healthStatus;
        this.location = location;
        this.userName = userName;
        this.userPhone = userPhone;
    }

    public AdoptionPet() {
        // Constructor vacío, inicializa los valores predeterminados si lo deseas
    }


    // Getters
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getType() { return type; }  // <-- Getter del nuevo campo
    public String getBreed() { return breed; }
    public String getAdoptionStatus() { return adoptionStatus; }
    public String getHealthStatus() { return healthStatus; }
    public String getLocation() { return location; }
    public String getUserName() { return userName; }
    public String getUserPhone() { return userPhone; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setType(String type) { this.type = type; }  // <-- Setter del nuevo campo
    public void setBreed(String breed) { this.breed = breed; }
    public void setAdoptionStatus(String adoptionStatus) { this.adoptionStatus = adoptionStatus; }
    public void setHealthStatus(String healthStatus) { this.healthStatus = healthStatus; }
    public void setLocation(String location) { this.location = location; }
    public void setUserName(String userName) { this.userName = userName; }
    public void setUserPhone(String userPhone) { this.userPhone = userPhone; }
}


