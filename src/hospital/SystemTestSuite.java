package hospital;

import hospital.exceptions.DuplicateBookingException;
import hospital.exceptions.InvalidInputException;
import hospital.exceptions.QueueEmptyException;
import hospital.model.*;
import hospital.queue.HospitalQueueEngine;
import hospital.service.HospitalManager;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Automated Verification Suite for Advanced Java PBL Requirements.
 */
public class SystemTestSuite {
    public static void main(String[] args) {
        System.out.println("===============================================================");
        System.out.println("  RUNNING COMPREHENSIVE HOSPITAL SYSTEM TEST SUITE (PBL)       ");
        System.out.println("===============================================================");

        int passed = 0;
        int failed = 0;

        HospitalManager mgr = HospitalManager.getInstance();

        // TEST 1: Patient Registration and Search
        try {
            System.out.print("[Test 1] Patient Registration & Search: ");
            Patient testPatient = new Patient(
                    mgr.generateNextPatientId(), "Test Patient Alpha", 35, "Male",
                    "555-9988", "alpha@test.com", "Test City", "B+", "No allergies", "Contact: 555-1111"
            );
            mgr.addPatient(testPatient);
            Patient retrieved = mgr.getPatientById(testPatient.getId());
            assert retrieved != null && retrieved.getName().equals("Test Patient Alpha");

            List<Patient> searchResults = mgr.searchPatients("Alpha");
            assert searchResults.size() >= 1;
            System.out.println("PASSED");
            passed++;
        } catch (Throwable t) {
            System.out.println("FAILED: " + t.getMessage());
            failed++;
        }

        // TEST 2: Doctor Registration & Specialization lookup
        try {
            System.out.print("[Test 2] Doctor Directory & Specialization: ");
            Doctor testDoctor = new Doctor(
                    mgr.generateNextDoctorId(), "Gregory House", 52, "Male", "555-7788",
                    "house@princeton.org", "Diagnostic Wing", "Diagnostic Medicine", "Room 404",
                    "MD, Nephrology & Pathology", 25, "Mon - Fri", "10:00 AM - 05:00 PM"
            );
            mgr.addDoctor(testDoctor);
            Doctor docRetrieved = mgr.getDoctorById(testDoctor.getId());
            assert docRetrieved != null && docRetrieved.getSpecialization().equals("Diagnostic Medicine");
            System.out.println("PASSED");
            passed++;
        } catch (Throwable t) {
            System.out.println("FAILED: " + t.getMessage());
            failed++;
        }

        // TEST 3: Unique Appointment ID and Booking
        try {
            System.out.print("[Test 3] Appointment Booking & ID Generation: ");
            Patient p = mgr.getAllPatients().get(0);
            Doctor d = mgr.getAllDoctors().get(0);
            LocalDate tomorrow = LocalDate.now().plusDays(2);
            String slot = "02:00 PM";

            Appointment apt = new Appointment(
                    mgr.generateNextAppointmentId(), p.getId(), p.getName(),
                    d.getId(), d.getName(), d.getSpecialization(),
                    tomorrow, slot, PatientType.REGULAR, AppointmentStatus.SCHEDULED, "Routine evaluation"
            );
            mgr.bookAppointment(apt);
            assert apt.getAppointmentId().startsWith("APT-");
            System.out.println("PASSED (" + apt.getAppointmentId() + ")");
            passed++;
        } catch (Throwable t) {
            System.out.println("FAILED: " + t.getMessage());
            failed++;
        }

        // TEST 4: Duplicate Booking Exception Validation
        try {
            System.out.print("[Test 4] Duplicate Booking Prevention: ");
            Patient p2 = mgr.getAllPatients().get(1);
            Doctor d = mgr.getAllDoctors().get(0);
            LocalDate tomorrow = LocalDate.now().plusDays(2);
            String slot = "02:00 PM"; // Same slot and doctor as Test 3!

            Appointment duplicateApt = new Appointment(
                    mgr.generateNextAppointmentId(), p2.getId(), p2.getName(),
                    d.getId(), d.getName(), d.getSpecialization(),
                    tomorrow, slot, PatientType.REGULAR, AppointmentStatus.SCHEDULED, "Conflict test"
            );

            boolean exceptionThrown = false;
            try {
                mgr.bookAppointment(duplicateApt);
            } catch (DuplicateBookingException e) {
                exceptionThrown = true;
            }

            if (exceptionThrown) {
                System.out.println("PASSED (DuplicateBookingException caught cleanly)");
                passed++;
            } else {
                System.out.println("FAILED: DuplicateBookingException was NOT thrown!");
                failed++;
            }
        } catch (Throwable t) {
            System.out.println("FAILED: " + t.getMessage());
            failed++;
        }

        // TEST 5: Queue Emergency Priority Verification
        try {
            System.out.print("[Test 5] Strict Emergency Priority in Queue: ");
            HospitalQueueEngine queue = new HospitalQueueEngine();

            QueueToken regularToken = new QueueToken(
                    "TKN-REG-991", "P-1", "Regular Patient", 30, "M", "D-1", "Dr. A", null,
                    PatientType.REGULAR, "Mild cold"
            );
            QueueToken emergencyToken = new QueueToken(
                    "TKN-EMG-992", "P-2", "Emergency Patient", 45, "F", "D-1", "Dr. A", null,
                    PatientType.EMERGENCY, "Trauma / Severe"
            );

            // Add regular first, then emergency
            queue.produceToken(regularToken);
            queue.produceToken(emergencyToken);

            // Next polled token MUST be emergency token despite regular being added first
            QueueToken firstServed = queue.pollNextImmediate();
            assert firstServed.isEmergency() : "Emergency token was not prioritized!";
            assert firstServed.getTokenNumber().equals("TKN-EMG-992");

            QueueToken secondServed = queue.pollNextImmediate();
            assert !secondServed.isEmergency();
            assert secondServed.getTokenNumber().equals("TKN-REG-991");

            System.out.println("PASSED (Emergency served before Regular)");
            passed++;
        } catch (Throwable t) {
            System.out.println("FAILED: " + t.getMessage());
            failed++;
        }

        // TEST 6: Multi-threaded Producer-Consumer with wait() & notifyAll()
        try {
            System.out.print("[Test 6] Multi-threaded Producer-Consumer (wait/notify): ");
            HospitalQueueEngine boundedQueue = new HospitalQueueEngine(2); // Small capacity to force wait/notify
            CountDownLatch latch = new CountDownLatch(4);
            AtomicBoolean threadSuccess = new AtomicBoolean(true);

            // Consumer Thread
            Thread consumer = new Thread(() -> {
                for (int i = 0; i < 4; i++) {
                    try {
                        QueueToken token = boundedQueue.consumeToken(); // blocks on wait() if empty
                        if (token == null) threadSuccess.set(false);
                        latch.countDown();
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        threadSuccess.set(false);
                    }
                }
            });

            // Producer Thread
            Thread producer = new Thread(() -> {
                for (int i = 0; i < 4; i++) {
                    try {
                        QueueToken token = new QueueToken(
                                "TKN-" + i, "P-" + i, "Name " + i, 20 + i, "M",
                                "D-1", "Dr. X", null,
                                (i % 2 == 0) ? PatientType.REGULAR : PatientType.EMERGENCY,
                                "Symptom " + i
                        );
                        boundedQueue.produceToken(token); // blocks on wait() if full, notifies consumer
                        Thread.sleep(40);
                    } catch (InterruptedException e) {
                        threadSuccess.set(false);
                    }
                }
            });

            consumer.start();
            producer.start();

            boolean done = latch.await(4, TimeUnit.SECONDS);
            if (done && threadSuccess.get()) {
                System.out.println("PASSED (Safe concurrency across threads)");
                passed++;
            } else {
                System.out.println("FAILED: Thread coordination timed out");
                failed++;
            }
        } catch (Throwable t) {
            System.out.println("FAILED: " + t.getMessage());
            failed++;
        }

        // TEST 7: Consultation Completion & Record Generation
        try {
            System.out.print("[Test 7] Consultation Record Generation: ");
            ConsultationRecord rec = new ConsultationRecord(
                    mgr.generateNextRecordId(), "TKN-REG-999", "PAT-1001", "Arthur Pendelton",
                    "DOC-201", "Sarah Jenkins", "Fever and sore throat",
                    "Acute Pharyngitis", "Amoxicillin 500mg TDS x 5 days",
                    "BP: 120/80 | Temp: 100.2 F", "Advised warm saline gargle"
            );
            mgr.completeConsultation(rec);
            List<ConsultationRecord> history = mgr.getConsultationHistory();
            assert history.size() >= 1;
            assert history.get(0).getDiagnosis().equals("Acute Pharyngitis");
            System.out.println("PASSED (" + rec.getRecordId() + ")");
            passed++;
        } catch (Throwable t) {
            System.out.println("FAILED: " + t.getMessage());
            failed++;
        }

        System.out.println("===============================================================");
        System.out.println("  SUMMARY: " + passed + " PASSED, " + failed + " FAILED");
        System.out.println("===============================================================");

        if (failed > 0) {
            System.exit(1);
        }
    }
}
