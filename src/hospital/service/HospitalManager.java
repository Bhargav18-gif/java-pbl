package hospital.service;

import hospital.exceptions.DuplicateBookingException;
import hospital.exceptions.InvalidInputException;
import hospital.exceptions.QueueEmptyException;
import hospital.model.*;
import hospital.queue.HospitalQueueEngine;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Singleton Service orchestrating patients, doctors, appointments,
 * queues, and consultation operations.
 * Uses HashMap, ArrayList, and synchronized queue structures.
 */
public class HospitalManager {
    private static HospitalManager instance;

    // Collections
    private final Map<String, Patient> patientMap = new LinkedHashMap<>();
    private final Map<String, Doctor> doctorMap = new LinkedHashMap<>();
    private final Map<String, Appointment> appointmentMap = new LinkedHashMap<>();
    private final List<ConsultationRecord> consultationHistory = new ArrayList<>();
    private final HospitalQueueEngine queueEngine = new HospitalQueueEngine(100);

    // ID Generators
    private final AtomicInteger patientSeq = new AtomicInteger(1000);
    private final AtomicInteger doctorSeq = new AtomicInteger(200);
    private final AtomicInteger appointmentSeq = new AtomicInteger(5000);
    private final AtomicInteger regularTokenSeq = new AtomicInteger(100);
    private final AtomicInteger emergencyTokenSeq = new AtomicInteger(500);
    private final AtomicInteger recordSeq = new AtomicInteger(3000);

    // Listeners for global updates
    private final List<Runnable> changeListeners = new ArrayList<>();

    private HospitalManager() {
        seedSampleData();
    }

    public static synchronized HospitalManager getInstance() {
        if (instance == null) {
            instance = new HospitalManager();
        }
        return instance;
    }

    public HospitalQueueEngine getQueueEngine() {
        return queueEngine;
    }

    public void addChangeListener(Runnable listener) {
        if (listener != null && !changeListeners.contains(listener)) {
            changeListeners.add(listener);
        }
    }

    public void notifyChangeListeners() {
        for (Runnable r : changeListeners) {
            try {
                r.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // ================= PATIENT MANAGEMENT =================

    public String generateNextPatientId() {
        return "PAT-" + patientSeq.incrementAndGet();
    }

    public synchronized void addPatient(Patient patient) throws InvalidInputException {
        validatePatient(patient);
        if (patient.getId() == null || patient.getId().trim().isEmpty()) {
            patient.setId(generateNextPatientId());
        }
        patientMap.put(patient.getId(), patient);
        notifyChangeListeners();
    }

    public synchronized void updatePatient(Patient patient) throws InvalidInputException {
        validatePatient(patient);
        if (!patientMap.containsKey(patient.getId())) {
            throw new InvalidInputException("Patient with ID " + patient.getId() + " not found.");
        }
        patientMap.put(patient.getId(), patient);
        notifyChangeListeners();
    }

    public synchronized boolean deletePatient(String patientId) {
        if (patientMap.remove(patientId) != null) {
            notifyChangeListeners();
            return true;
        }
        return false;
    }

    public synchronized Patient getPatientById(String id) {
        return patientMap.get(id);
    }

    public synchronized List<Patient> getAllPatients() {
        return new ArrayList<>(patientMap.values());
    }

    public synchronized List<Patient> searchPatients(String query) {
        List<Patient> result = new ArrayList<>();
        for (Patient p : patientMap.values()) {
            if (p.matches(query)) {
                result.add(p);
            }
        }
        return result;
    }

    private void validatePatient(Patient p) throws InvalidInputException {
        if (p == null) {
            throw new InvalidInputException("Patient details cannot be null.");
        }
        if (p.getName() == null || p.getName().trim().isEmpty()) {
            throw new InvalidInputException("Patient name is required.");
        }
        if (p.getAge() <= 0 || p.getAge() > 125) {
            throw new InvalidInputException("Please enter a valid age between 1 and 125.");
        }
        if (p.getPhone() == null || p.getPhone().trim().length() < 7) {
            throw new InvalidInputException("Please enter a valid contact phone number (min 7 digits).");
        }
    }

    // ================= DOCTOR MANAGEMENT =================

    public String generateNextDoctorId() {
        return "DOC-" + doctorSeq.incrementAndGet();
    }

    public synchronized void addDoctor(Doctor doctor) throws InvalidInputException {
        validateDoctor(doctor);
        if (doctor.getId() == null || doctor.getId().trim().isEmpty()) {
            doctor.setId(generateNextDoctorId());
        }
        doctorMap.put(doctor.getId(), doctor);
        notifyChangeListeners();
    }

    public synchronized void updateDoctor(Doctor doctor) throws InvalidInputException {
        validateDoctor(doctor);
        if (!doctorMap.containsKey(doctor.getId())) {
            throw new InvalidInputException("Doctor with ID " + doctor.getId() + " not found.");
        }
        doctorMap.put(doctor.getId(), doctor);
        notifyChangeListeners();
    }

    public synchronized boolean deleteDoctor(String doctorId) {
        if (doctorMap.remove(doctorId) != null) {
            notifyChangeListeners();
            return true;
        }
        return false;
    }

    public synchronized Doctor getDoctorById(String id) {
        return doctorMap.get(id);
    }

    public synchronized List<Doctor> getAllDoctors() {
        return new ArrayList<>(doctorMap.values());
    }

    public synchronized List<Doctor> searchDoctors(String query) {
        List<Doctor> result = new ArrayList<>();
        for (Doctor d : doctorMap.values()) {
            if (d.matches(query)) {
                result.add(d);
            }
        }
        return result;
    }

    private void validateDoctor(Doctor d) throws InvalidInputException {
        if (d == null) {
            throw new InvalidInputException("Doctor details cannot be null.");
        }
        if (d.getName() == null || d.getName().trim().isEmpty()) {
            throw new InvalidInputException("Doctor name is required.");
        }
        if (d.getSpecialization() == null || d.getSpecialization().trim().isEmpty()) {
            throw new InvalidInputException("Specialization is required.");
        }
        if (d.getRoomNumber() == null || d.getRoomNumber().trim().isEmpty()) {
            throw new InvalidInputException("Room or Chamber number is required.");
        }
    }

    // ================= APPOINTMENT MANAGEMENT =================

    public String generateNextAppointmentId() {
        return "APT-" + appointmentSeq.incrementAndGet();
    }

    public synchronized void bookAppointment(Appointment appt) throws DuplicateBookingException, InvalidInputException {
        if (appt == null) {
            throw new InvalidInputException("Appointment data cannot be null.");
        }
        if (appt.getPatientId() == null || !patientMap.containsKey(appt.getPatientId())) {
            throw new InvalidInputException("Selected patient does not exist.");
        }
        if (appt.getDoctorId() == null || !doctorMap.containsKey(appt.getDoctorId())) {
            throw new InvalidInputException("Selected doctor does not exist.");
        }
        if (appt.getAppointmentDate() == null) {
            throw new InvalidInputException("Appointment date is required.");
        }
        if (appt.getTimeSlot() == null || appt.getTimeSlot().trim().isEmpty()) {
            throw new InvalidInputException("Time slot is required.");
        }

        // Duplicate check: Same doctor, same date, same time slot for another scheduled appointment
        for (Appointment existing : appointmentMap.values()) {
            if (existing.getStatus() != AppointmentStatus.CANCELLED &&
                existing.getDoctorId().equalsIgnoreCase(appt.getDoctorId()) &&
                existing.getAppointmentDate().equals(appt.getAppointmentDate()) &&
                existing.getTimeSlot().equalsIgnoreCase(appt.getTimeSlot())) {
                throw new DuplicateBookingException("Dr. " + existing.getDoctorName() +
                        " already has an appointment booked on " + appt.getAppointmentDate() +
                        " at " + appt.getTimeSlot() + ". Please choose another time slot.");
            }
        }

        if (appt.getAppointmentId() == null || appt.getAppointmentId().trim().isEmpty()) {
            appt.setAppointmentId(generateNextAppointmentId());
        }

        appointmentMap.put(appt.getAppointmentId(), appt);
        notifyChangeListeners();
    }

    public synchronized void rescheduleAppointment(String appointmentId, LocalDate newDate, String newTimeSlot)
            throws DuplicateBookingException, InvalidInputException {
        Appointment appt = appointmentMap.get(appointmentId);
        if (appt == null) {
            throw new InvalidInputException("Appointment " + appointmentId + " not found.");
        }
        if (newDate == null) {
            throw new InvalidInputException("New appointment date is required.");
        }
        if (newTimeSlot == null || newTimeSlot.trim().isEmpty()) {
            throw new InvalidInputException("New time slot is required.");
        }

        // Duplicate check
        for (Appointment existing : appointmentMap.values()) {
            if (!existing.getAppointmentId().equalsIgnoreCase(appointmentId) &&
                existing.getStatus() != AppointmentStatus.CANCELLED &&
                existing.getDoctorId().equalsIgnoreCase(appt.getDoctorId()) &&
                existing.getAppointmentDate().equals(newDate) &&
                existing.getTimeSlot().equalsIgnoreCase(newTimeSlot)) {
                throw new DuplicateBookingException("Time slot " + newTimeSlot + " on " + newDate +
                        " is already booked for Dr. " + appt.getDoctorName());
            }
        }

        appt.setAppointmentDate(newDate);
        appt.setTimeSlot(newTimeSlot);
        appt.setStatus(AppointmentStatus.RESCHEDULED);
        notifyChangeListeners();
    }

    public synchronized void cancelAppointment(String appointmentId) throws InvalidInputException {
        Appointment appt = appointmentMap.get(appointmentId);
        if (appt == null) {
            throw new InvalidInputException("Appointment " + appointmentId + " not found.");
        }
        appt.setStatus(AppointmentStatus.CANCELLED);
        notifyChangeListeners();
    }

    public synchronized void updateAppointmentStatus(String appointmentId, AppointmentStatus status) {
        Appointment appt = appointmentMap.get(appointmentId);
        if (appt != null) {
            appt.setStatus(status);
            notifyChangeListeners();
        }
    }

    public synchronized List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointmentMap.values());
    }

    public synchronized List<Appointment> getRecentAppointments(int limit) {
        List<Appointment> list = new ArrayList<>(appointmentMap.values());
        list.sort((a, b) -> b.getBookedAt().compareTo(a.getBookedAt()));
        if (list.size() > limit) {
            return new ArrayList<>(list.subList(0, limit));
        }
        return list;
    }

    public synchronized List<Appointment> searchAppointments(String query) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment a : appointmentMap.values()) {
            if (a.matches(query)) {
                result.add(a);
            }
        }
        return result;
    }

    // ================= QUEUE & TOKEN MANAGEMENT =================

    public String generateNextTokenNumber(PatientType type) {
        if (type == PatientType.EMERGENCY) {
            return "TKN-EMG-" + emergencyTokenSeq.incrementAndGet();
        } else {
            return "TKN-REG-" + regularTokenSeq.incrementAndGet();
        }
    }

    public synchronized QueueToken issueToken(String patientId, String doctorId,
                                             PatientType type, String issueDescription,
                                             String appointmentId)
            throws InvalidInputException, InterruptedException {
        Patient p = patientMap.get(patientId);
        if (p == null) {
            throw new InvalidInputException("Patient ID " + patientId + " not found.");
        }
        Doctor d = doctorMap.get(doctorId);
        if (d == null) {
            throw new InvalidInputException("Doctor ID " + doctorId + " not found.");
        }

        String tokenNum = generateNextTokenNumber(type);
        QueueToken token = new QueueToken(
                tokenNum,
                p.getId(),
                p.getName(),
                p.getAge(),
                p.getGender(),
                d.getId(),
                d.getName(),
                appointmentId,
                type,
                issueDescription != null ? issueDescription : "General Consultation"
        );

        queueEngine.produceToken(token);

        if (appointmentId != null && !appointmentId.isEmpty()) {
            updateAppointmentStatus(appointmentId, AppointmentStatus.IN_QUEUE);
        }

        notifyChangeListeners();
        return token;
    }

    // ================= CONSULTATION MANAGEMENT =================

    public String generateNextRecordId() {
        return "CNS-" + recordSeq.incrementAndGet();
    }

    public synchronized QueueToken callNextPatient() throws QueueEmptyException {
        QueueToken token = queueEngine.pollNextImmediate();
        notifyChangeListeners();
        return token;
    }

    public synchronized void completeConsultation(ConsultationRecord record) throws InvalidInputException {
        if (record == null) {
            throw new InvalidInputException("Consultation record cannot be null.");
        }
        if (record.getRecordId() == null || record.getRecordId().trim().isEmpty()) {
            record.setRecordId(generateNextRecordId());
        }

        consultationHistory.add(0, record);
        queueEngine.markCurrentConsultationComplete();

        // If there was an appointment linked to this token, mark completed
        String tokenNum = record.getTokenNumber();
        for (Appointment a : appointmentMap.values()) {
            if (a.getPatientId().equalsIgnoreCase(record.getPatientId()) &&
                a.getStatus() == AppointmentStatus.IN_QUEUE) {
                a.setStatus(AppointmentStatus.COMPLETED);
                break;
            }
        }

        notifyChangeListeners();
    }

    public synchronized List<ConsultationRecord> getConsultationHistory() {
        return new ArrayList<>(consultationHistory);
    }

    // ================= DASHBOARD STATISTICS =================

    public synchronized int getTotalPatients() {
        return patientMap.size();
    }

    public synchronized int getTotalAppointments() {
        return appointmentMap.size();
    }

    public synchronized int getWaitingPatients() {
        return queueEngine.getTotalWaitingCount();
    }

    public synchronized int getEmergencyPatients() {
        return queueEngine.getEmergencyCount();
    }

    public synchronized int getCompletedConsultations() {
        return consultationHistory.size();
    }

    public synchronized int getTotalDoctors() {
        return doctorMap.size();
    }

    // ================= SAMPLE DATA SEEDING =================

    private void seedSampleData() {
        // Sample Doctors
        Doctor d1 = new Doctor("DOC-201", "Sarah Jenkins", 42, "Female", "555-0101",
                "dr.jenkins@stmary.org", "Suite 4B, West Wing", "Cardiology", "Room 102",
                "MD, FACC - Harvard Medical", 15, "Mon, Wed, Fri", "09:00 AM - 02:00 PM");
        Doctor d2 = new Doctor("DOC-202", "Robert Chen", 48, "Male", "555-0102",
                "dr.chen@stmary.org", "Suite 2A, North Wing", "Neurology", "Room 205",
                "MD, PhD - Johns Hopkins", 20, "Tue, Thu, Sat", "10:00 AM - 04:00 PM");
        Doctor d3 = new Doctor("DOC-203", "Elena Rostova", 36, "Female", "555-0103",
                "dr.rostova@stmary.org", "Pediatric Wing Room 10", "Pediatrics", "Room 110",
                "MD, FAAP - Stanford", 10, "Mon - Fri", "08:30 AM - 01:30 PM");
        Doctor d4 = new Doctor("DOC-204", "Marcus Vance", 51, "Male", "555-0104",
                "dr.vance@stmary.org", "Trauma & Ortho Unit", "Orthopedics", "Room 304",
                "MS, FRCS Ortho - Oxford", 22, "Mon, Wed, Thu", "09:00 AM - 03:00 PM");
        Doctor d5 = new Doctor("DOC-205", "Priya Nair", 39, "Female", "555-0105",
                "dr.nair@stmary.org", "OPD Block A", "General Medicine", "Room 101",
                "MD, Internal Medicine - AIIMS", 12, "Mon - Sat", "08:00 AM - 02:00 PM");

        doctorMap.put(d1.getId(), d1);
        doctorMap.put(d2.getId(), d2);
        doctorMap.put(d3.getId(), d3);
        doctorMap.put(d4.getId(), d4);
        doctorMap.put(d5.getId(), d5);

        // Sample Patients
        Patient p1 = new Patient("PAT-1001", "Arthur Pendelton", 58, "Male", "555-4321",
                "arthur.p@mail.com", "742 Evergreen Terrace", "O+", "Hypertension, Mild Asthma", "Evelyn (Wife): 555-9011");
        Patient p2 = new Patient("PAT-1002", "Samantha Hayes", 29, "Female", "555-8765",
                "sam.hayes@mail.com", "124 Conch St, Boston", "A+", "None reported. Penicillin allergy.", "Mark (Brother): 555-9022");
        Patient p3 = new Patient("PAT-1003", "David Kim", 64, "Male", "555-7890",
                "dkim64@mail.com", "89 Beacon Blvd", "B-", "Type-2 Diabetes mellitus", "Grace (Daughter): 555-9033");
        Patient p4 = new Patient("PAT-1004", "Olivia Martinez", 8, "Female", "555-3210",
                "parents.martinez@mail.com", "304 Oak Ridge Way", "AB+", "Seasonal rhinitis", "Maria (Mother): 555-9044");
        Patient p5 = new Patient("PAT-1005", "James Wilson", 45, "Male", "555-6543",
                "jwilson@mail.com", "55 Elm Street", "O-", "Recent ankle fracture surgery", "Linda (Spouse): 555-9055");
        Patient p6 = new Patient("PAT-1006", "Clara Oswald", 34, "Female", "555-2198",
                "clara.o@mail.com", "42 Baker Ave", "A-", "Frequent migraine headaches", "John (Father): 555-9066");

        patientMap.put(p1.getId(), p1);
        patientMap.put(p2.getId(), p2);
        patientMap.put(p3.getId(), p3);
        patientMap.put(p4.getId(), p4);
        patientMap.put(p5.getId(), p5);
        patientMap.put(p6.getId(), p6);

        // Sample Appointments
        LocalDate today = LocalDate.now();
        Appointment a1 = new Appointment("APT-5001", p1.getId(), p1.getName(), d1.getId(), d1.getName(),
                d1.getSpecialization(), today, "09:30 AM", PatientType.REGULAR, AppointmentStatus.COMPLETED,
                "Routine quarterly blood pressure review.");
        Appointment a2 = new Appointment("APT-5002", p2.getId(), p2.getName(), d5.getId(), d5.getName(),
                d5.getSpecialization(), today, "10:00 AM", PatientType.REGULAR, AppointmentStatus.IN_QUEUE,
                "Persistent dry cough for two weeks.");
        Appointment a3 = new Appointment("APT-5003", p3.getId(), p3.getName(), d2.getId(), d2.getName(),
                d2.getSpecialization(), today, "11:15 AM", PatientType.REGULAR, AppointmentStatus.SCHEDULED,
                "Neurological follow-up for numbness in fingertips.");
        Appointment a4 = new Appointment("APT-5004", p4.getId(), p4.getName(), d3.getId(), d3.getName(),
                d3.getSpecialization(), today, "01:00 PM", PatientType.REGULAR, AppointmentStatus.SCHEDULED,
                "Annual growth monitoring & vaccination.");
        Appointment a5 = new Appointment("APT-5005", p5.getId(), p5.getName(), d4.getId(), d4.getName(),
                d4.getSpecialization(), today.plusDays(1), "10:30 AM", PatientType.REGULAR, AppointmentStatus.SCHEDULED,
                "Post-op cast removal checkup.");

        appointmentMap.put(a1.getAppointmentId(), a1);
        appointmentMap.put(a2.getAppointmentId(), a2);
        appointmentMap.put(a3.getAppointmentId(), a3);
        appointmentMap.put(a4.getAppointmentId(), a4);
        appointmentMap.put(a5.getAppointmentId(), a5);

        // Sample Completed Consultation
        ConsultationRecord cr1 = new ConsultationRecord(
                "CNS-3001", "TKN-REG-099", p1.getId(), p1.getName(), d1.getId(), d1.getName(),
                "Occasional morning headaches, elevated BP",
                "Stage 1 Essential Hypertension (Controlled)",
                "Amlodipine 5mg once daily OD; Low sodium diet; 30min daily walk.",
                "BP: 138/88 mmHg | Pulse: 74 bpm | SpO2: 98% | Temp: 98.4°F",
                "Patient responded well to current dosage. Follow up in 3 months."
        );
        consultationHistory.add(cr1);

        // Seed initial tokens into Queue
        try {
            // Emergency patient (high priority)
            QueueToken emg1 = new QueueToken(
                    "TKN-EMG-501", p6.getId(), p6.getName(), p6.getAge(), p6.getGender(),
                    d5.getId(), d5.getName(), null, PatientType.EMERGENCY,
                    "Acute chest tightness and shortness of breath"
            );
            queueEngine.produceToken(emg1);

            // Regular patient from appointment
            QueueToken reg1 = new QueueToken(
                    "TKN-REG-101", p2.getId(), p2.getName(), p2.getAge(), p2.getGender(),
                    d5.getId(), d5.getName(), a2.getAppointmentId(), PatientType.REGULAR,
                    "Persistent dry cough for two weeks"
            );
            queueEngine.produceToken(reg1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
