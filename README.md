# Hospital Appointment & Patient Queue Management System
**Advanced Java Programming Project-Based Learning (PBL)**

A complete desktop application built entirely with **Java Swing**, designed with a modern medical aesthetic (Navy Blue, Medical White, Light Blue) and architectural rigor.

---

## Key Highlights & PBL Requirements Mapping

| Requirement | Implementation Details |
|---|---|
| **Java Swing GUI** | 100% pure Java Swing with custom theme, rounded cards, responsive layouts (`CardLayout`, `BorderLayout`, `GridBagLayout`), status pill badges, and table styling. |
| **OOP Concepts** | **Abstraction & Inheritance:** Base abstract class `Person` extended by `Patient` and `Doctor`.<br>**Interfaces:** `Queueable`, `Searchable`, `QueueObserver`.<br>**Polymorphism & Encapsulation:** Private fields with getters/setters, overridden search and display methods. |
| **Java Collections** | - `LinkedHashMap`: Fast $O(1)$ indexed retrieval and ordering for Patients, Doctors, and Appointments.<br>- `LinkedList`: Separate queues for Emergency and Regular patients.<br>- `ArrayList`: Consultation logs, search results, observer subscriber lists.<br>- `Comparable<QueueToken>`: Priority ordering logic. |
| **Exception Handling** | Custom checked exceptions:<br>- `DuplicateBookingException` (same doctor & time slot conflict)<br>- `InvalidInputException` (validation failures)<br>- `DoctorUnavailableException`<br>- `QueueEmptyException` |
| **Producer–Consumer** | Implemented in `HospitalQueueEngine`.<br>- **Producers:** Reception Walk-in Token Generator & Appointment Dispatcher.<br>- **Consumers:** Doctor Consultation Desk & Background Auto-Processing Thread.<br>- Thread synchronization with `synchronized`, `wait()`, and `notifyAll()`. |
| **Priority Queue Logic** | Strict priority: Emergency patients (`EMERGENCY`) are dequeued before Regular patients (`REGULAR`), with FIFO tie-breaking on creation timestamp. |
| **Responsive GUI** | Multi-threaded operations run off the Swing Event Dispatch Thread (EDT) and push updates via `SwingUtilities.invokeLater` and `QueueObserver` callbacks. |
| **Dynamic Dashboard** | Dynamic KPI cards and live tables updating automatically whenever records are added, modified, or processed. |

---

## System Screens

1. **Dashboard**
   - Live KPI cards: Total Patients, Total Appointments, Waiting in Queue, Emergency Queue, Completed Consultations, Active Doctors.
   - Real-time queue status alert bar.
   - Recent appointments summary table with visual status badges.
   - Quick action shortcuts.

2. **Patient Management**
   - Register new patients with full demographics (Name, Age, Gender, Phone, Email, Blood Group, Address, Allergies/Medical History, Emergency Contact).
   - Real-time multi-field search (by Name, ID, Phone, or Blood Group).
   - Edit, update, and delete patient records.
   - Direct walk-in dispatch to live queue.

3. **Doctor Management**
   - Doctor profiles with specializations (Cardiology, Neurology, Pediatrics, Orthopedics, General Medicine, etc.), consultation rooms, qualifications, experience, and shift timings.
   - Search by name, specialization, or room number.
   - Add, update, and delete doctor profiles.

4. **Appointment Management**
   - Automatic unique appointment ID generation (`APT-XXXX`).
   - Time slot selection and scheduling.
   - Duplicate booking conflict prevention with friendly error alert.
   - Reschedule and cancel actions.
   - One-click dispatch of scheduled appointments into the live consultation queue.

5. **Queue Management**
   - Dual-queue display: Dedicated **Emergency Priority Queue** (Red accent) alongside **Regular Queue** (Navy accent).
   - Live "Next Patient In-Line" banner highlighting emergency cases.
   - Direct Walk-in Token Generator (Producer).
   - Manual token call (Consumer) and token removal actions.

6. **Consultation Management (Doctor's Desk)**
   - Active patient card with chief complaint and clinical vitals.
   - Multi-threaded consultation simulator (toggle auto background consumer thread).
   - Clinical examination form: Vitals, Diagnosis, Prescription, and Doctor's Advice.
   - Consultation completion and permanent history archive.
   - "View Full Medical Record" dialog for prescription review.

---

## Project Structure

```
java(pbl)/
├── run.bat                          # One-click Windows launcher
├── build.bat                        # Recompiles all source code
├── test.bat                         # Runs the automated PBL test suite
├── src/
│   └── hospital/
│       ├── Main.java                # GUI Application Entry Point
│       ├── SystemTestSuite.java     # Headless Automated Test Suite
│       ├── model/
│       │   ├── Person.java          # Abstract base class
│       │   ├── Patient.java         # Extends Person
│       │   ├── Doctor.java          # Extends Person
│       │   ├── Appointment.java     # Appointment entity
│       │   ├── AppointmentStatus.java
│       │   ├── PatientType.java     # REGULAR, EMERGENCY
│       │   ├── QueueToken.java      # Priority token (Comparable)
│       │   ├── ConsultationRecord.java
│       │   ├── Queueable.java       # Interface
│       │   └── Searchable.java      # Interface
│       ├── exceptions/
│       │   ├── DuplicateBookingException.java
│       │   ├── InvalidInputException.java
│       │   ├── DoctorUnavailableException.java
│       │   └── QueueEmptyException.java
│       ├── queue/
│       │   ├── HospitalQueueEngine.java # Synchronized Producer-Consumer
│       │   └── QueueObserver.java       # Observer interface for Swing updates
│       ├── service/
│       │   └── HospitalManager.java     # Singleton Business Logic Controller
│       └── ui/
│           ├── Theme.java               # Color palette & styling constants
│           ├── MainFrame.java           # Sidebar & CardLayout Coordinator
│           ├── components/
│           │   ├── ModernButton.java    # Custom rounded button with hover states
│           │   ├── ModernTable.java     # Styled JTable with Navy header
│           │   ├── StatCard.java        # Dashboard KPI widget
│           │   └── BadgeRenderer.java   # Rounded status pill renderer
│           └── panels/
│               ├── DashboardPanel.java
│               ├── PatientPanel.java
│               ├── DoctorPanel.java
│               ├── AppointmentPanel.java
│               ├── QueuePanel.java
│               └── ConsultationPanel.java
└── bin/                             # Compiled Java class binaries
```

---

## How to Run & Demonstrate

### Option 1: Using Batch Scripts (Windows)
- Double-click `run.bat` to launch the GUI application.
- Double-click `test.bat` to run the automated verification suite.
- Double-click `build.bat` to recompile the project.

### Option 2: Using the Command Line
```powershell
# Compile
javac -d bin -sourcepath src src/hospital/Main.java src/hospital/SystemTestSuite.java

# Run Application
java -cp bin hospital.Main

# Run Verification Suite
java -cp bin hospital.SystemTestSuite
```
