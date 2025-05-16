import java.util.*;
import java.util.regex.Pattern;

class Doctor {
    String name, city, email, phone, speciality;

    Doctor(String name, String city, String email, String phone, String speciality) {
        if (name.length() < 3 || city.length() > 20 || !isValidEmail(email) || phone.length() < 10) {
            throw new IllegalArgumentException("Invalid Doctor Data");
        }

        if (!Arrays.asList("Delhi", "Noida", "Faridabad").contains(city)) {
            throw new IllegalArgumentException("Invalid city for doctor");
        }

        if (!Arrays.asList("Orthopaedic", "Gynecology", "Dermatology", "ENT").contains(speciality)) {
            throw new IllegalArgumentException("Invalid speciality for doctor");
        }

        this.name = name;
        this.city = city;
        this.email = email;
        this.phone = phone;
        this.speciality = speciality;
    }

    public static boolean isValidEmail(String email) {
        return Pattern.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$", email);
    }
}

class Patient {
    static int idCounter = 1;
    int id;
    String name, city, email, phone, symptom;

    Patient(String name, String city, String email, String phone, String symptom) {
        if (name.length() < 3 || city.length() > 20 || !Doctor.isValidEmail(email) || phone.length() < 10) {
            throw new IllegalArgumentException("Invalid Patient Data");
        }

        if (!getSymptomToSpeciality().containsKey(symptom)) {
            throw new IllegalArgumentException("Invalid symptom");
        }

        this.id = idCounter++;
        this.name = name;
        this.city = city;
        this.email = email;
        this.phone = phone;
        this.symptom = symptom;
    }

    public static Map<String, String> getSymptomToSpeciality() {
        Map<String, String> map = new HashMap<>();
        map.put("Arthritis", "Orthopaedic");
        map.put("Back Pain", "Orthopaedic");
        map.put("Tissue injuries", "Orthopaedic");
        map.put("Dysmenorrhea", "Gynecology");
        map.put("Skin infection", "Dermatology");
        map.put("skin burn", "Dermatology");
        map.put("Ear pain", "ENT");
        return map;
    }
}

public class HealthPlatform {

    static List<Doctor> doctors = new ArrayList<>();
    static List<Patient> patients = new ArrayList<>();

    // Add doctor
    public static void addDoctor(String name, String city, String email, String phone, String speciality) {
        try {
            doctors.add(new Doctor(name, city, email, phone, speciality));
            System.out.println("Doctor added: " + name);
        } catch (Exception e) {
            System.out.println("Failed to add doctor: " + e.getMessage());
        }
    }

    // Remove doctor by name
    public static void removeDoctor(String name) {
        boolean removed = doctors.removeIf(doc -> doc.name.equalsIgnoreCase(name));
        if (removed) {
            System.out.println("Doctor removed: " + name);
        } else {
            System.out.println("Doctor not found: " + name);
        }
    }

    // Add patient
    public static void addPatient(String name, String city, String email, String phone, String symptom) {
        try {
            Patient p = new Patient(name, city, email, phone, symptom);
            patients.add(p);
            System.out.println("Patient added with ID: " + p.id);
        } catch (Exception e) {
            System.out.println("Failed to add patient: " + e.getMessage());
        }
    }

    // Get patient by ID
    public static Patient getPatientById(int id) {
        for (Patient p : patients) {
            if (p.id == id) return p;
        }
        return null;
    }

    // Suggest doctor based on patient ID
    public static void suggestDoctor(int patientId) {
        Patient p = getPatientById(patientId);
        if (p == null) {
            System.out.println("Patient not found");
            return;
        }

        String symptom = p.symptom;
        String city = p.city;
        String speciality = Patient.getSymptomToSpeciality().get(symptom);

        if (!Arrays.asList("Delhi", "Noida", "Faridabad").contains(city)) {
            System.out.println("We are still waiting to expand to your location");
            return;
        }

        List<Doctor> matched = new ArrayList<>();
        for (Doctor d : doctors) {
            if (d.city.equalsIgnoreCase(city) && d.speciality.equalsIgnoreCase(speciality)) {
                matched.add(d);
            }
        }

        if (matched.isEmpty()) {
            System.out.println("There isn’t any doctor present at your location for your symptom");
        } else {
            System.out.println("Suggested doctors for " + p.name + " (Symptom: " + symptom + "):");
            for (Doctor d : matched) {
                System.out.println("- Dr. " + d.name + " (" + d.speciality + ", " + d.city + ")");
            }
        }
    }

    public static void main(String[] args) {
        // Sample doctors
        addDoctor("Anil Mehta", "Delhi", "anil@hospital.com", "9999911111", "Orthopaedic");
        addDoctor("Pooja Sharma", "Noida", "pooja@hospital.com", "8888822222", "Gynecology");
        addDoctor("Raj Malhotra", "Faridabad", "raj@hospital.com", "7777733333", "Dermatology");
        addDoctor("Neha Kapoor", "Delhi", "neha@hospital.com", "6666644444", "ENT");

        // Sample patients
        addPatient("Ravi", "Delhi", "ravi@email.com", "1234567890", "Arthritis");
        addPatient("Sita", "Agra", "sita@email.com", "9876543210", "Ear pain"); // city not in doctor list
        addPatient("Amit", "Noida", "amit@email.com", "1234567890", "Dysmenorrhea");

        // Suggest doctors for patients
        suggestDoctor(1); // Ravi
        suggestDoctor(2); // Sita
        suggestDoctor(3); // Amit

        // Example remove doctor
        removeDoctor("Raj Malhotra");

        // Try suggesting again after removal
        suggestDoctor(2);
    }
}
