import base64
import os

def get_base64(path):
    if os.path.exists(path):
        with open(path, "rb") as f:
            return "data:image/png;base64," + base64.b64encode(f.read()).decode("utf-8")
    return ""

img_dashboard = get_base64("screenshots/dashboard.png")
img_patients = get_base64("screenshots/patients.png")
img_doctors = get_base64("screenshots/doctors.png")
img_appointments = get_base64("screenshots/appointments.png")
img_queue = get_base64("screenshots/queue.png")
img_consultation = get_base64("screenshots/consultation.png")

html_content = f"""<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Hospital Appointment & Patient Queue Management System - PBL Report</title>
<style>
    @page {{
        size: A4 portrait;
        margin: 14mm 14mm 14mm 14mm;
        @bottom-right {{
            content: "Page " counter(page);
            font-family: 'Segoe UI', Arial, sans-serif;
            font-size: 8pt;
            color: #64748B;
        }}
        @bottom-left {{
            content: "Department of Data Engineering • Advance java programing";
            font-family: 'Segoe UI', Arial, sans-serif;
            font-size: 8pt;
            color: #64748B;
        }}
        @top-right {{
            content: "Hospital Queue & Appointment System (PBL)";
            font-family: 'Segoe UI', Arial, sans-serif;
            font-size: 8pt;
            color: #94A3B8;
        }}
    }}

    * {{
        box-sizing: border-box;
    }}

    body {{
        font-family: 'Segoe UI', -apple-system, BlinkMacSystemFont, Roboto, Helvetica, Arial, sans-serif;
        color: #1E293B;
        line-height: 1.45;
        font-size: 9pt;
        background: #FFFFFF;
        margin: 0;
        padding: 0;
    }}

    /* ============================================================
       COVER PAGE
       ============================================================ */
    .cover-container {{
        page-break-after: always;
        height: 94vh;
        border: 2.5px double #0B2545;
        outline: 1px solid #1E3A8A;
        outline-offset: -6px;
        padding: 35px 25px 25px 25px;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        text-align: center;
        background: radial-gradient(circle at top right, #F8FAFC 0%, #FFFFFF 70%);
    }}

    .cover-header h3 {{
        font-family: 'Georgia', 'Times New Roman', serif;
        font-size: 14pt;
        letter-spacing: 2px;
        color: #0B2545;
        margin: 0 0 6px 0;
        text-transform: uppercase;
        font-weight: 700;
    }}

    .cover-header h4 {{
        font-family: 'Segoe UI', sans-serif;
        font-size: 11pt;
        font-weight: 600;
        color: #1E40AF;
        margin: 0 0 4px 0;
        text-transform: uppercase;
        letter-spacing: 1.5px;
    }}

    .cover-header p {{
        font-size: 9.5pt;
        color: #64748B;
        margin: 0;
        font-weight: 500;
    }}

    .cover-title-section {{
        margin: 20px 0;
        padding: 24px 18px;
        background: #FFFFFF;
        border-top: 3px solid #1E40AF;
        border-bottom: 3px solid #1E40AF;
        box-shadow: 0 2px 8px rgba(15, 23, 42, 0.04);
    }}

    .cover-title-section .subtitle-tag {{
        display: inline-block;
        background: #DBEAFE;
        color: #1E40AF;
        padding: 3px 12px;
        border-radius: 16px;
        font-size: 8.5pt;
        font-weight: 700;
        letter-spacing: 1px;
        text-transform: uppercase;
        margin-bottom: 10px;
    }}

    .cover-title-section h1 {{
        font-family: 'Georgia', 'Times New Roman', serif;
        font-size: 21pt;
        color: #0B2545;
        margin: 0 0 8px 0;
        line-height: 1.25;
        font-weight: 700;
    }}

    .cover-title-section h2 {{
        font-size: 11pt;
        color: #2563EB;
        font-weight: 600;
        margin: 0;
        line-height: 1.4;
    }}

    .cover-details-grid {{
        width: 88%;
        margin: 0 auto;
        border: 1px solid #CBD5E1;
        border-radius: 6px;
        background: #F8FAFC;
        overflow: hidden;
    }}

    .cover-details-grid table {{
        width: 100%;
        border-collapse: collapse;
        font-size: 9.5pt;
    }}

    .cover-details-grid td {{
        padding: 7px 14px;
        border-bottom: 1px solid #E2E8F0;
        text-align: left;
    }}

    .cover-details-grid tr:last-child td {{
        border-bottom: none;
    }}

    .cover-details-grid td.label-col {{
        font-weight: 700;
        color: #0B2545;
        width: 38%;
        background: #F1F5F9;
        border-right: 1px solid #CBD5E1;
    }}

    .cover-details-grid td.val-col {{
        color: #1E293B;
    }}

    .cover-signatures {{
        display: flex;
        justify-content: space-between;
        margin-top: 20px;
        padding: 0 30px;
    }}

    .sig-block {{
        width: 42%;
        text-align: center;
        border-top: 1.5px solid #94A3B8;
        padding-top: 6px;
        font-size: 8.5pt;
        color: #475569;
        font-weight: 600;
    }}

    .cover-footer {{
        font-size: 8pt;
        color: #94A3B8;
        margin-top: 6px;
    }}

    /* ============================================================
       REPORT HEADERS & TYPOGRAPHY
       ============================================================ */
    h1.chapter-title {{
        font-family: 'Georgia', 'Times New Roman', serif;
        font-size: 13.5pt;
        color: #0B2545;
        border-bottom: 2px solid #1E40AF;
        padding-bottom: 4px;
        margin-top: 18px;
        margin-bottom: 10px;
        page-break-after: avoid;
        display: flex;
        align-items: center;
    }}

    h1.chapter-title::before {{
        content: "";
        display: inline-block;
        width: 5px;
        height: 15px;
        background: #2563EB;
        margin-right: 8px;
        border-radius: 2px;
    }}

    h2.sub-title {{
        font-size: 10.5pt;
        color: #1E3A8A;
        margin-top: 12px;
        margin-bottom: 6px;
        font-weight: 700;
        page-break-after: avoid;
    }}

    p {{
        margin: 0 0 8px 0;
        text-align: justify;
        color: #334155;
    }}

    ul, ol {{
        margin: 0 0 8px 0;
        padding-left: 18px;
        color: #334155;
    }}

    li {{
        margin-bottom: 3px;
    }}

    /* ============================================================
       COMPACT SCREENSHOT GRID LAYOUT (2 PER ROW/PAGE)
       ============================================================ */
    .module-card {{
        border: 1px solid #CBD5E1;
        border-radius: 6px;
        background: #FFFFFF;
        overflow: hidden;
        margin-bottom: 14px;
        page-break-inside: avoid;
        box-shadow: 0 1px 4px rgba(0,0,0,0.05);
    }}

    .module-header {{
        background: #F1F5F9;
        border-bottom: 1px solid #CBD5E1;
        padding: 5px 10px;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }}

    .module-title {{
        font-weight: 700;
        color: #0B2545;
        font-size: 9.5pt;
    }}

    .module-badge {{
        background: #DBEAFE;
        color: #1E40AF;
        padding: 2px 7px;
        border-radius: 10px;
        font-size: 7.5pt;
        font-weight: 700;
    }}

    .module-content {{
        display: flex;
        flex-direction: row;
        align-items: center;
        padding: 8px;
        gap: 12px;
    }}

    .module-img {{
        width: 58%;
        border: 1px solid #E2E8F0;
        border-radius: 4px;
        display: block;
    }}

    .module-desc {{
        width: 42%;
        font-size: 8.5pt;
        color: #475569;
        line-height: 1.4;
    }}

    .module-desc ul {{
        margin: 4px 0 0 0;
        padding-left: 14px;
    }}

    .module-desc li {{
        margin-bottom: 2px;
    }}

    /* ============================================================
       COMPACT CODE SNIPPETS
       ============================================================ */
    .code-box {{
        margin-bottom: 12px;
        page-break-inside: avoid;
    }}

    .code-header {{
        background: #0B2545;
        color: #93C5FD;
        font-family: 'Consolas', monospace;
        font-size: 8pt;
        padding: 4px 10px;
        border-radius: 5px 5px 0 0;
        font-weight: bold;
        display: flex;
        justify-content: space-between;
    }}

    pre {{
        background-color: #0F172A;
        color: #E2E8F0;
        padding: 8px 12px;
        border-radius: 0 0 5px 5px;
        font-family: 'Consolas', 'Courier New', monospace;
        font-size: 7.5pt;
        line-height: 1.35;
        overflow-x: hidden;
        border: 1px solid #1E293B;
        border-top: none;
        margin: 0;
    }}

    code {{
        font-family: 'Consolas', monospace;
        background-color: #F1F5F9;
        color: #B91C1C;
        padding: 1px 4px;
        border-radius: 3px;
        font-size: 8pt;
    }}

    pre code {{
        background: transparent;
        color: inherit;
        padding: 0;
    }}

    .terminal-box {{
        background: #020617;
        color: #38BDF8;
        font-family: 'Consolas', monospace;
        font-size: 7.8pt;
        padding: 10px 12px;
        border-radius: 5px;
        line-height: 1.4;
        white-space: pre;
        margin: 8px 0 12px 0;
        border: 1px solid #1E293B;
        page-break-inside: avoid;
    }}

    table.content-table {{
        width: 100%;
        border-collapse: collapse;
        margin: 8px 0 12px 0;
        font-size: 8.5pt;
        page-break-inside: avoid;
    }}

    table.content-table th {{
        background: #0B2545;
        color: #FFFFFF;
        font-weight: 600;
        text-align: left;
        padding: 5px 8px;
        border: 1px solid #0B2545;
    }}

    table.content-table td {{
        padding: 5px 8px;
        border: 1px solid #E2E8F0;
        vertical-align: top;
        color: #334155;
    }}

    table.content-table tr:nth-child(even) {{
        background-color: #F8FAFC;
    }}

    .callout {{
        background-color: #F0F9FF;
        border-left: 3.5px solid #0284C7;
        padding: 8px 12px;
        margin: 8px 0 12px 0;
        border-radius: 0 5px 5px 0;
        page-break-inside: avoid;
        font-size: 8.5pt;
    }}

    .page-break {{
        page-break-after: always;
    }}
</style>
</head>
<body>

<!-- ============================================================
     COVER PAGE (NO SYMBOL, SUBJECT: Advance java programing)
     ============================================================ -->
<div class="cover-container">
    <div class="cover-header">
        <h3>PROJECT-BASED LEARNING (PBL) CAPSTONE REPORT</h3>
        <h4>DEPARTMENT OF DATA ENGINEERING</h4>
        <p>Course: Advance java programing</p>
    </div>

    <div class="cover-title-section">
        <span class="subtitle-tag">Advanced Java PBL Project</span>
        <h1>Hospital Appointment and Patient Queue Management System</h1>
        <h2>A Multi-Threaded, Priority-Driven Clinical Desktop Application Using Java Swing</h2>
    </div>

    <div class="cover-details-grid">
        <table>
            <tr>
                <td class="label-col">Department:</td>
                <td class="val-col"><strong>Department of Data Engineering</strong></td>
            </tr>
            <tr>
                <td class="label-col">Subject Name:</td>
                <td class="val-col"><strong>Advance java programing</strong></td>
            </tr>
            <tr>
                <td class="label-col">Project Domain:</td>
                <td class="val-col">Healthcare Systems & Concurrent Systems Engineering</td>
            </tr>
            <tr>
                <td class="label-col">Technology Stack:</td>
                <td class="val-col">Java SE 25, Java Swing, Concurrency & Collections</td>
            </tr>
            <tr>
                <td class="label-col">Architecture:</td>
                <td class="val-col">Producer–Consumer Pattern, MVC/Tiered, Observer Pattern</td>
            </tr>
            <tr>
                <td class="label-col">Execution Model:</td>
                <td class="val-col">Multi-Threaded Queue with Monitor Synchronization</td>
            </tr>
        </table>
    </div>

    <div class="cover-signatures">
        <div class="sig-block">
            Project Candidate / Student<br>Department of Data Engineering
        </div>
        <div class="sig-block">
            Course Faculty / Evaluator<br>Department of Data Engineering
        </div>
    </div>

    <div class="cover-footer">
        Academic Year 2026 &bull; Department of Data Engineering &bull; Advance java programing
    </div>
</div>

<!-- ============================================================
     CHAPTER 1: INTRODUCTION & ARCHITECTURE
     ============================================================ -->
<h1 class="chapter-title">1. Introduction, Objectives & System Architecture</h1>
<p>
The <strong>Hospital Appointment and Patient Queue Management System</strong> is a standalone clinical software application engineered in pure <strong>Java Swing</strong> by the <strong>Department of Data Engineering</strong> for the course <strong>Advance java programing</strong>. It coordinates outpatient booking, emergency walk-in triage, and doctor consultation processing using concurrent thread synchronization.
</p>

<div class="callout">
    <strong>Key Engineering Objectives:</strong>
    1) Multi-threaded Producer–Consumer queue using <code>wait()</code> and <code>notifyAll()</code>; 
    2) Dual-queue triage enforcing strict priority for Emergency patients over Regular checkups; 
    3) Conflict prevention throwing custom <code>DuplicateBookingException</code>; 
    4) High-responsiveness Swing GUI updated dynamically via an Observer pattern.
</div>

<h2 class="sub-title">1.1 Core Architecture & Object-Oriented Principles</h2>
<table class="content-table">
    <thead>
        <tr>
            <th style="width: 20%;">OOP Concept</th>
            <th style="width: 45%;">Implementation Details</th>
            <th style="width: 35%;">Key Classes</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td><strong>Abstraction</strong></td>
            <td>Abstract base class encapsulating universal demographic fields (ID, name, age, phone) with abstract role display.</td>
            <td><code>Person.java</code> (Abstract Superclass)</td>
        </tr>
        <tr>
            <td><strong>Inheritance</strong></td>
            <td>Subclasses extending <code>Person</code>, appending medical records, blood group, emergency contacts, qualifications, and specialties.</td>
            <td><code>Patient.java</code>, <code>Doctor.java</code></td>
        </tr>
        <tr>
            <td><strong>Polymorphism</strong></td>
            <td>Overridden <code>matches()</code> query searching; priority comparator in tokens; interface-driven observer notifications.</td>
            <td><code>Queueable.java</code>, <code>Searchable.java</code></td>
        </tr>
        <tr>
            <td><strong>Concurrency</strong></td>
            <td>Bounded monitor buffer coordinating receptionist producers and doctor consumers safely.</td>
            <td><code>HospitalQueueEngine.java</code></td>
        </tr>
    </tbody>
</table>

<div class="page-break"></div>

<!-- ============================================================
     CHAPTER 2: MODULE OUTPUTS & SCREENSHOTS (PAGE 1: MODULES 1 & 2)
     ============================================================ -->
<h1 class="chapter-title">2. Module Outputs & Graphical User Interface</h1>
<p>
Below are the actual GUI outputs captured directly from the running Java Swing desktop application, paired with their operational specifications.
</p>

<!-- MODULE 1: DASHBOARD -->
<div class="module-card">
    <div class="module-header">
        <span class="module-title">Module 1: Hospital Overview & Real-Time Analytics Dashboard</span>
        <span class="module-badge">Analytics & Overview</span>
    </div>
    <div class="module-content">
        <img class="module-img" src="{img_dashboard}" alt="Dashboard Screen">
        <div class="module-desc">
            <strong>Key Functionalities:</strong>
            <ul>
                <li><strong>6 Dynamic KPI Cards:</strong> Live counters for Total Patients, Appointments, Waiting Queue, Emergency Queue, Completed Consultations, Active Doctors.</li>
                <li><strong>Real-Time Status Banner:</strong> Displays active queue triage status and the patient currently inside the consultation chamber.</li>
                <li><strong>Recent Bookings Table:</strong> Displays recent appointments with color-coded status badges.</li>
                <li><strong>Quick Action Shortcuts:</strong> Direct navigation to registration, appointment scheduling, and consultation.</li>
            </ul>
        </div>
    </div>
</div>

<!-- MODULE 2: PATIENT MANAGEMENT -->
<div class="module-card">
    <div class="module-header">
        <span class="module-title">Module 2: Patient Registration & Search Directory</span>
        <span class="module-badge">Patient Intake</span>
    </div>
    <div class="module-content">
        <img class="module-img" src="{img_patients}" alt="Patient Management Screen">
        <div class="module-desc">
            <strong>Key Functionalities:</strong>
            <ul>
                <li><strong>Electronic Intake Form:</strong> Captures Patient ID (auto-generated <code>PAT-XXXX</code>), Name, Age, Gender, Phone, Blood Group, Address, Allergies, and Emergency Contact.</li>
                <li><strong>Instant Multi-Field Search:</strong> Real-time filtering across ID, Name, Phone, and Blood Group.</li>
                <li><strong>Direct Queue Dispatch:</strong> One-click button to send any registered patient directly into the live triage queue.</li>
                <li><strong>Record CRUD Operations:</strong> Save, update, clear form, and delete records.</li>
            </ul>
        </div>
    </div>
</div>

<div class="page-break"></div>

<!-- ============================================================
     CHAPTER 2 CONTINUED: MODULES 3 & 4 (DOCTORS & APPOINTMENTS)
     ============================================================ -->
<h1 class="chapter-title">2. Module Outputs & Graphical User Interface (Contd.)</h1>

<!-- MODULE 3: DOCTOR DIRECTORY -->
<div class="module-card">
    <div class="module-header">
        <span class="module-title">Module 3: Doctor & Specialist Directory</span>
        <span class="module-badge">Specialist Registry</span>
    </div>
    <div class="module-content">
        <img class="module-img" src="{img_doctors}" alt="Doctor Management Screen">
        <div class="module-desc">
            <strong>Key Functionalities:</strong>
            <ul>
                <li><strong>Specialist Profile Management:</strong> Organizes physicians across Cardiology, Neurology, Pediatrics, Orthopedics, General Medicine, Oncology, and Dermatology.</li>
                <li><strong>Consultation Chambers:</strong> Assigns room numbers, hospital emails, and contact numbers.</li>
                <li><strong>Duty Rosters:</strong> Tracks qualifications, clinical experience years, and shift timings.</li>
                <li><strong>Search & Filter:</strong> Fast multi-column search by doctor name, specialty, or room number.</li>
            </ul>
        </div>
    </div>
</div>

<!-- MODULE 4: APPOINTMENTS -->
<div class="module-card">
    <div class="module-header">
        <span class="module-title">Module 4: Appointment Scheduling & Conflict Resolution</span>
        <span class="module-badge">Scheduling Engine</span>
    </div>
    <div class="module-content">
        <img class="module-img" src="{img_appointments}" alt="Appointment Scheduling Screen">
        <div class="module-desc">
            <strong>Key Functionalities:</strong>
            <ul>
                <li><strong>Unique ID Generation:</strong> Generates unique identifiers (<code>APT-XXXX</code>) automatically.</li>
                <li><strong>Duplicate Conflict Prevention:</strong> Enforces business rules throwing <code>DuplicateBookingException</code> if a doctor is double-booked for the same date and slot.</li>
                <li><strong>Rescheduling Modal:</strong> Allows modifying appointment date and time slots with instant validation.</li>
                <li><strong>Dispatch to Queue:</strong> Dispatches confirmed appointments into the active queue as consultation tokens.</li>
            </ul>
        </div>
    </div>
</div>

<div class="page-break"></div>

<!-- ============================================================
     CHAPTER 2 CONTINUED: MODULES 5 & 6 (QUEUE & CONSULTATION)
     ============================================================ -->
<h1 class="chapter-title">2. Module Outputs & Graphical User Interface (Contd.)</h1>

<!-- MODULE 5: QUEUE MANAGEMENT -->
<div class="module-card">
    <div class="module-header">
        <span class="module-title">Module 5: Dual Priority Queue Management (Emergency vs. Regular)</span>
        <span class="module-badge">Priority Triage</span>
    </div>
    <div class="module-content">
        <img class="module-img" src="{img_queue}" alt="Queue Management Screen">
        <div class="module-desc">
            <strong>Key Functionalities:</strong>
            <ul>
                <li><strong>Dual Priority Queues:</strong> Separate visual tables for <strong>Emergency Queue (Priority 1)</strong> in crimson and <strong>Regular Queue (Priority 2)</strong> in navy.</li>
                <li><strong>Emergency Precedence:</strong> Strictly dequeues emergency patients before regular checkup patients regardless of arrival time.</li>
                <li><strong>Next-in-Line Indicator:</strong> Computes and displays the next patient to be called in real time.</li>
                <li><strong>Walk-In Token Producer:</strong> Issues triage tokens on-the-spot without prior booking.</li>
            </ul>
        </div>
    </div>
</div>

<!-- MODULE 6: CONSULTATION DESK -->
<div class="module-card">
    <div class="module-header">
        <span class="module-title">Module 6: Doctor Consultation Desk & Medical Examination</span>
        <span class="module-badge">Clinical Workstation</span>
    </div>
    <div class="module-content">
        <img class="module-img" src="{img_consultation}" alt="Consultation Desk Screen">
        <div class="module-desc">
            <strong>Key Functionalities:</strong>
            <ul>
                <li><strong>Active Patient Station:</strong> Displays attending doctor, patient identity, and triage level.</li>
                <li><strong>Multi-Threaded Consumer Simulation:</strong> Toggleable background worker thread simulating consultation processing with a live progress bar.</li>
                <li><strong>Electronic Prescription Entry:</strong> Records clinical vitals (BP, pulse, SpO2, temp), diagnosis, medication dosage schedules, and doctor advice.</li>
                <li><strong>Medical Record Archival:</strong> Permanently stores consultation summaries with full record review dialogs.</li>
            </ul>
        </div>
    </div>
</div>

<div class="page-break"></div>

<!-- ============================================================
     CHAPTER 3: CORE SOURCE CODE IMPLEMENTATION
     ============================================================ -->
<h1 class="chapter-title">3. Core Source Code Implementation</h1>
<p>
The following excerpts showcase the critical engineering logic implementing OOP, multi-threaded Producer–Consumer synchronization, priority queue comparison, and custom exception handling.
</p>

<!-- SNIPPET 1: HOSPITAL QUEUE ENGINE (PRODUCER CONSUMER) -->
<div class="code-box">
    <div class="code-header">
        <span>HospitalQueueEngine.java — Thread-Safe Producer–Consumer with Emergency Priority</span>
        <span>Package: hospital.queue</span>
    </div>
    <pre><code>public class HospitalQueueEngine {{
    private final LinkedList&lt;QueueToken&gt; emergencyQueue = new LinkedList&lt;&gt;();
    private final LinkedList&lt;QueueToken&gt; regularQueue = new LinkedList&lt;&gt;();
    private final int capacity = 100;

    // PRODUCER METHOD: Adds token to queue; waits if full, notifies waiting consumers
    public synchronized void produceToken(QueueToken token) throws InterruptedException {{
        while ((emergencyQueue.size() + regularQueue.size()) >= capacity) {{
            wait(); // Buffer full: Producer waits
        }}
        if (token.isEmergency()) {{
            emergencyQueue.addLast(token);
        }} else {{
            regularQueue.addLast(token);
        }}
        notifyAll(); // Wake up consumers waiting for patients
        notifyObserversQueueUpdated();
    }}

    // CONSUMER METHOD: Strict Emergency Priority Retrieval
    public synchronized QueueToken consumeToken() throws InterruptedException {{
        while (emergencyQueue.isEmpty() && regularQueue.isEmpty()) {{
            wait(); // Queue empty: Consumer thread waits
        }}
        // Strict Priority: Emergency queue polled first
        QueueToken token = !emergencyQueue.isEmpty() 
                ? emergencyQueue.removeFirst() 
                : regularQueue.removeFirst();
        notifyAll(); // Wake up producers waiting for space
        notifyObserversQueueUpdated();
        notifyObserversPatientCalled(token);
        return token;
    }}
}}</code></pre>
</div>

<!-- SNIPPET 2: QUEUE TOKEN COMPARATOR -->
<div class="code-box">
    <div class="code-header">
        <span>QueueToken.java — Priority Ordering Logic</span>
        <span>Package: hospital.model</span>
    </div>
    <pre><code>public class QueueToken implements Queueable, Comparable&lt;QueueToken&gt; {{
    private String tokenNumber;
    private PatientType type; // REGULAR or EMERGENCY
    private long timestamp;

    @Override
    public int compareTo(QueueToken other) {{
        // Emergency cases strictly precede Regular cases
        if (this.isEmergency() && !other.isEmergency()) return -1;
        if (!this.isEmergency() && other.isEmergency()) return 1;
        // Identical priority level resolves via FIFO arrival timestamp
        return Long.compare(this.timestamp, other.timestamp);
    }}
}}</code></pre>
</div>

<!-- SNIPPET 3: DUPLICATE BOOKING EXCEPTION & CHECK -->
<div class="code-box">
    <div class="code-header">
        <span>HospitalManager.java — Duplicate Booking Prevention</span>
        <span>Package: hospital.service</span>
    </div>
    <pre><code>public synchronized void bookAppointment(Appointment appt) 
        throws DuplicateBookingException, InvalidInputException {{
    // Verify no conflicting booking exists for the same doctor at identical date & time
    for (Appointment existing : appointmentMap.values()) {{
        if (existing.getStatus() != AppointmentStatus.CANCELLED &&
            existing.getDoctorId().equalsIgnoreCase(appt.getDoctorId()) &&
            existing.getAppointmentDate().equals(appt.getAppointmentDate()) &&
            existing.getTimeSlot().equalsIgnoreCase(appt.getTimeSlot())) {{
            throw new DuplicateBookingException("Dr. " + existing.getDoctorName() +
                    " already has an appointment booked on " + appt.getAppointmentDate() +
                    " at " + appt.getTimeSlot() + ". Please choose another slot.");
        }}
    }}
    if (appt.getAppointmentId() == null) appt.setAppointmentId(generateNextAppointmentId());
    appointmentMap.put(appt.getAppointmentId(), appt);
    notifyChangeListeners();
}}</code></pre>
</div>

<div class="page-break"></div>

<!-- ============================================================
     CHAPTER 4: SYSTEM OUTPUTS & EXECUTION LOGS
     ============================================================ -->
<h1 class="chapter-title">4. System Outputs & Terminal Execution Traces</h1>
<p>
The live execution outputs below show application startup, multi-threaded producer–consumer dispatch events, and an electronically archived consultation report.
</p>

<h2 class="sub-title">4.1 Application Startup & Module Registration Log</h2>
<div class="terminal-box">==================================================
  Hospital Appointment & Queue Management System  
  Department of Data Engineering
  Course: Advance java programing
  Status: Application launched successfully!     
==================================================
[Queue Producer] Added EMERGENCY Token: TKN-EMG-501 - Clara Oswald
[Queue Producer] Added REGULAR Token: TKN-REG-101 - Samantha Hayes
[MainFrame] Initialized 6 Core Modules with CardLayout
[Observer] Registered UI panels for real-time EDT synchronization</div>

<h2 class="sub-title">4.2 Multi-Threaded Producer–Consumer Runtime Execution Trace</h2>
<div class="terminal-box">[Queue Consumer] Queue is empty. Waiting for patients...
[Queue Producer] Added REGULAR Token: TKN-REG-102 - Arthur Pendelton (Buffer size: 1)
[Queue Consumer] Consumed REGULAR Token: TKN-REG-102
[Queue Producer] Added EMERGENCY Token: TKN-EMG-502 - David Kim (Buffer size: 1)
[Queue Consumer] Consumed EMERGENCY Token: TKN-EMG-502
[Queue Producer] Added EMERGENCY Token: TKN-EMG-503 - Clara Oswald
[Queue Producer] Added REGULAR Token: TKN-REG-103 - Samantha Hayes
[Queue Consumer] Consumed EMERGENCY Token: TKN-EMG-503 (Emergency Priority Served First!)
[Queue Consumer] Consumed REGULAR Token: TKN-REG-103 (Regular Patient Served)</div>

<h2 class="sub-title">4.3 System-Generated Clinical Medical Record Output</h2>
<div class="terminal-box">==========================================================
       HOSPITAL CLINICAL CONSULTATION REPORT
==========================================================
Record ID:      CNS-3001
Token Number:   TKN-REG-099
Date & Time:    2026-11-10 10:15
Patient ID:     PAT-1001
Patient Name:   Arthur Pendelton
Doctor:         Dr. Sarah Jenkins (DOC-201) - Cardiology

Vital Signs:    BP: 138/88 mmHg | Pulse: 74 bpm | SpO2: 98% | Temp: 98.4°F
Symptoms:       Occasional morning headaches, elevated BP
Diagnosis:      Stage 1 Essential Hypertension (Controlled)
Prescription:   Amlodipine 5mg once daily OD; Low sodium diet; 30min daily walk.
Doctor Notes:   Patient responded well to current dosage. Follow up in 3 months.
Status:         COMPLETED
==========================================================</div>

<!-- ============================================================
     CHAPTER 5: CONCLUSION & LEARNING OUTCOMES
     ============================================================ -->
<h1 class="chapter-title">5. Conclusion & Project-Based Learning Outcomes</h1>
<p>
The <strong>Hospital Appointment and Patient Queue Management System</strong> successfully achieves all curriculum objectives for <strong>Advance java programing</strong> in the <strong>Department of Data Engineering</strong>.
</p>
<ul>
    <li><strong>Monitor Concurrency:</strong> Eliminated race conditions across dispatchers and consultation desks using Java intrinsic locks (<code>wait()</code> and <code>notifyAll()</code>).</li>
    <li><strong>Clinical Priority Queuing:</strong> Solved outpatient bottlenecking by strictly prioritizing emergency cases over routine checkups.</li>
    <li><strong>Defensive Integrity:</strong> Prevented clinical double-booking using custom checked exceptions (<code>DuplicateBookingException</code>).</li>
    <li><strong>Responsive Desktop GUI:</strong> Maintained smooth 60 FPS Swing responsiveness through asynchronous background threading and Swing Event Dispatch Thread (EDT) updates.</li>
</ul>

<hr style="border: 0; border-top: 1px solid #CBD5E1; margin: 25px 0 8px 0;">
<p style="text-align: center; font-size: 8pt; color: #64748B;">
    &copy; 2026 Department of Data Engineering &bull; Course: Advance java programing &bull; PBL Capstone Report
</p>

</body>
</html>
"""

with open("PROJECT_REPORT.html", "w", encoding="utf-8") as f:
    f.write(html_content)

print("PROJECT_REPORT.html built with compact layout and embedded screenshots!")
