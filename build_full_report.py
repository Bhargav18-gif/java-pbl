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
        margin: 15mm 15mm 15mm 15mm;
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
        font-size: 9.3pt;
        background: #FFFFFF;
        margin: 0;
        padding: 0;
    }}

    .page-break {{
        page-break-after: always;
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
        letter-spacing: 2.5px;
        color: #0B2545;
        margin: 0 0 6px 0;
        text-transform: uppercase;
        font-weight: 700;
    }}

    .cover-header h4 {{
        font-family: 'Segoe UI', sans-serif;
        font-size: 11.5pt;
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
       CERTIFICATE & DECLARATION
       ============================================================ */
    .cert-box {{
        border: 1.5px solid #CBD5E1;
        padding: 20px;
        border-radius: 6px;
        background: #FAFCFE;
        margin-top: 12px;
    }}

    .cert-title {{
        text-align: center;
        font-family: 'Georgia', serif;
        font-size: 14pt;
        color: #0B2545;
        font-weight: 700;
        margin-bottom: 12px;
        text-transform: uppercase;
        letter-spacing: 1.5px;
    }}

    /* ============================================================
       HEADINGS & SECTIONS
       ============================================================ */
    h1.chapter-title {{
        font-family: 'Georgia', 'Times New Roman', serif;
        font-size: 14pt;
        color: #0B2545;
        border-bottom: 2px solid #1E40AF;
        padding-bottom: 4px;
        margin-top: 0;
        margin-bottom: 10px;
        page-break-after: avoid;
        display: flex;
        align-items: center;
    }}

    h1.chapter-title::before {{
        content: "";
        display: inline-block;
        width: 5px;
        height: 16px;
        background: #2563EB;
        margin-right: 8px;
        border-radius: 2px;
    }}

    h2.sub-title {{
        font-size: 11pt;
        color: #1E3A8A;
        margin-top: 10px;
        margin-bottom: 5px;
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
        margin-bottom: 2px;
    }}

    /* ============================================================
       FULL-PAGE SCREENSHOT PRESENTATION
       ============================================================ */
    .screenshot-hero {{
        border: 1px solid #CBD5E1;
        border-radius: 6px;
        background: #FFFFFF;
        box-shadow: 0 2px 8px rgba(0,0,0,0.05);
        overflow: hidden;
        margin: 8px 0 10px 0;
        text-align: center;
    }}

    .screenshot-hero img {{
        width: 100%;
        max-height: 360px;
        object-fit: contain;
        display: block;
        background: #F8FAFC;
    }}

    .screenshot-caption {{
        background: #F1F5F9;
        border-top: 1px solid #E2E8F0;
        padding: 6px 12px;
        font-size: 8.8pt;
        font-weight: 700;
        color: #0B2545;
    }}

    .feature-grid {{
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 10px;
        margin-top: 8px;
    }}

    .feature-item {{
        background: #F8FAFC;
        border: 1px solid #E2E8F0;
        border-radius: 5px;
        padding: 8px 12px;
    }}

    .feature-item h4 {{
        margin: 0 0 3px 0;
        font-size: 9pt;
        color: #1E40AF;
        font-weight: 700;
    }}

    .feature-item p {{
        margin: 0;
        font-size: 8.3pt;
        color: #475569;
        line-height: 1.35;
    }}

    /* ============================================================
       CODE SNIPPETS
       ============================================================ */
    .code-box {{
        margin-bottom: 10px;
    }}

    .code-header {{
        background: #0B2545;
        color: #93C5FD;
        font-family: 'Consolas', monospace;
        font-size: 8pt;
        padding: 5px 10px;
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
        font-size: 7.6pt;
        line-height: 1.32;
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
        font-size: 8pt;
        padding: 10px 12px;
        border-radius: 5px;
        line-height: 1.4;
        white-space: pre;
        margin: 8px 0 12px 0;
        border: 1px solid #1E293B;
    }}

    table.content-table {{
        width: 100%;
        border-collapse: collapse;
        margin: 8px 0 12px 0;
        font-size: 8.6pt;
    }}

    table.content-table th {{
        background: #0B2545;
        color: #FFFFFF;
        font-weight: 600;
        text-align: left;
        padding: 6px 9px;
        border: 1px solid #0B2545;
    }}

    table.content-table td {{
        padding: 5px 9px;
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
        font-size: 8.6pt;
    }}

    .arch-diagram {{
        background: #F8FAFC;
        border: 1px solid #CBD5E1;
        border-left: 3.5px solid #1E40AF;
        padding: 10px 12px;
        border-radius: 0 5px 5px 0;
        margin: 8px 0 12px 0;
        font-family: 'Consolas', monospace;
        font-size: 8.2pt;
        white-space: pre;
        line-height: 1.3;
        color: #0F172A;
    }}
</style>
</head>
<body>

<!-- ============================================================
     PAGE 1: COVER PAGE
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
                <td class="val-col">Multi-Threaded Priority Queue with Monitor Synchronization</td>
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
     PAGE 2: CERTIFICATE & DECLARATION
     ============================================================ -->
<div class="page-break">
    <h1 class="chapter-title">Academic Certificate & Declaration</h1>

    <div class="cert-box">
        <div class="cert-title">Department of Data Engineering</div>
        <p style="text-align: center; font-weight: 600; color: #1E40AF; margin-bottom: 16px;">
            CERTIFICATE OF COMPLETION
        </p>
        <p>
            This is to certify that the project entitled <strong>"Hospital Appointment and Patient Queue Management System"</strong> is a bonafide work carried out by the student in partial fulfillment of the requirements for the course <strong>Advance java programing</strong> in the <strong>Department of Data Engineering</strong> during the academic year 2026.
        </p>
        <p>
            The project has been developed utilizing native Java Swing desktop GUI components, Object-Oriented Programming (OOP) paradigms, the Java Collections Framework, custom checked exception handling, and multi-threaded Producer–Consumer synchronization. The application has been demonstrated and verified for correct execution.
        </p>

        <div style="margin-top: 50px; display: flex; justify-content: space-between; padding: 0 16px;">
            <div style="text-align: center; width: 42%; border-top: 1.5px solid #64748B; padding-top: 6px; font-size: 8.5pt; font-weight: bold;">
                Internal Faculty Supervisor<br>Department of Data Engineering
            </div>
            <div style="text-align: center; width: 42%; border-top: 1.5px solid #64748B; padding-top: 6px; font-size: 8.5pt; font-weight: bold;">
                Head of Department<br>Department of Data Engineering
            </div>
        </div>
    </div>

    <div class="cert-box" style="margin-top: 20px;">
        <div class="cert-title" style="font-size: 12.5pt;">Student Declaration</div>
        <p>
            I hereby declare that this project report entitled <strong>"Hospital Appointment and Patient Queue Management System"</strong> submitted to the <strong>Department of Data Engineering</strong> for the course <strong>Advance java programing</strong> represents my original work.
        </p>
        <p>
            The implementation details, class designs, multi-threaded algorithms, and GUI layouts were engineered in accordance with academic integrity and project-based learning guidelines.
        </p>

        <div style="margin-top: 40px; text-align: right; padding-right: 20px;">
            <div style="display: inline-block; text-align: center; border-top: 1.5px solid #64748B; padding-top: 6px; font-size: 8.5pt; font-weight: bold; width: 220px;">
                Signature of Student Candidate<br>Department of Data Engineering
            </div>
        </div>
    </div>
</div>

<!-- ============================================================
     PAGE 3: ABSTRACT, PROBLEM STATEMENT & OBJECTIVES
     ============================================================ -->
<div class="page-break">
    <h1 class="chapter-title">1. Executive Summary & Problem Formulation</h1>
    
    <h2 class="sub-title">1.1 Abstract</h2>
    <p>
        In modern healthcare centers, coordinating patient arrival queues with specialist appointment schedules represents an operational bottleneck. Traditional systems suffer from clerical double-booking, sluggish software response times during background data processing, and lack of dynamic triage mechanisms to prioritize critical walk-in emergencies over routine checkups.
    </p>
    <p>
        The <strong>Hospital Appointment and Patient Queue Management System</strong> is a standalone clinical software application engineered in pure <strong>Java Swing</strong> by the <strong>Department of Data Engineering</strong> for the course <strong>Advance java programing</strong>. Designed around a modern medical palette (Deep Navy, Pure White, and Medical Blue), the application integrates core Advanced Java disciplines:
    </p>
    <ul>
        <li><strong>Multi-Threaded Producer–Consumer Queue:</strong> Uses Java intrinsic monitors (<code>synchronized</code>, <code>wait()</code>, and <code>notifyAll()</code>) to guarantee thread safety between appointment dispatchers (producers) and consultation examination desks (consumers).</li>
        <li><strong>Dual-Queue Priority Triage:</strong> Maintains independent Emergency and Regular patient queues where critical patients receive non-preemptive priority over standard appointments.</li>
        <li><strong>Object-Oriented Architecture:</strong> Features an abstract base <code>Person</code> class with polymorphic subclasses (<code>Patient</code> and <code>Doctor</code>) and decoupled interfaces (<code>Queueable</code>, <code>Searchable</code>, <code>QueueObserver</code>).</li>
        <li><strong>Conflict Prevention:</strong> Implements checked exceptions including <code>DuplicateBookingException</code> to prevent double-booking specialists for identical time slots.</li>
        <li><strong>Responsive GUI & Dynamic Observer:</strong> Offloads heavy tasks to background worker threads while routing UI mutations to the Swing Event Dispatch Thread (EDT) via <code>SwingUtilities.invokeLater()</code>.</li>
    </ul>

    <h2 class="sub-title">1.2 Problem Statement & Motivation</h2>
    <p>
        Outpatient facilities require simultaneous management of two distinct patient flows: patients with pre-scheduled appointments and spontaneous walk-in arrivals presenting acute trauma. When both flows compete for the same attending specialist, an automated system must ensure that emergency cases are expedited without corrupting existing schedules or freezing clerical workstations.
    </p>

    <div class="callout">
        <strong>Key Project Objectives:</strong>
        <ol style="margin: 4px 0 0 0; padding-left: 16px;">
            <li>Construct an intuitive desktop interface with six functional modules: Dashboard, Patient Management, Doctor Directory, Appointment Dispatch, Priority Queues, and Consultation Desk.</li>
            <li>Implement thread-safe Producer–Consumer queuing utilizing Java monitor synchronization.</li>
            <li>Enforce strict priority queue dispatch ensuring emergency patients are attended before standard appointments.</li>
            <li>Eliminate scheduling conflicts through custom validation and duplicate-booking exception handling.</li>
            <li>Ensure 100% responsiveness on the Swing Event Dispatch Thread (EDT).</li>
        </ol>
    </div>
</div>

<!-- ============================================================
     PAGE 4: SYSTEM ARCHITECTURE & OOP MAPPING
     ============================================================ -->
<div class="page-break">
    <h1 class="chapter-title">2. System Architecture & Object-Oriented Design</h1>
    <p>
        The system employs a layered architectural model separating presentation, controller logic, concurrent queue processing, and domain data models.
    </p>

    <div class="arch-diagram">
+-----------------------------------------------------------------------------------+
|                        PRESENTATION TIER (JAVA SWING GUI)                         |
|   MainFrame (CardLayout Coordinator)  |  DashboardPanel  |  PatientPanel          |
|   DoctorPanel  |  AppointmentPanel    |  QueuePanel      |  ConsultationPanel     |
+-----------------------------------------------------------------------------------+
                                          |
+-----------------------------------------------------------------------------------+
|                         APPLICATION SERVICE CONTROLLER                            |
|                  HospitalManager (Thread-Safe Singleton Service)                  |
|    - Maps: LinkedHashMap&lt;String, Patient&gt;, Doctor, Appointment                   |
|    - History: ArrayList&lt;ConsultationRecord&gt;  |  Observers: List&lt;QueueObserver&gt;    |
+-----------------------------------------------------------------------------------+
                   |                                                 |
+---------------------------------------+         +---------------------------------+
|         CONCURRENCY TIER              |         |          DOMAIN MODELS          |
|      HospitalQueueEngine              |         |  - Person (Abstract Superclass) |
|  - Producer: produceToken()           |         |  - Patient, Doctor (Subclasses) |
|  - Consumer: consumeToken()           |         |  - Appointment, QueueToken      |
|  - Dual LinkedList Queues             |         |  - ConsultationRecord           |
|  - Monitor: synchronized/wait/notify  |         |  - Custom Checked Exceptions    |
+---------------------------------------+         +---------------------------------+
    </div>

    <h2 class="sub-title">2.1 Object-Oriented Programming (OOP) Paradigm Mapping</h2>
    <table class="content-table">
        <thead>
            <tr>
                <th style="width: 22%;">OOP Principle</th>
                <th style="width: 45%;">Implementation Details in Application</th>
                <th style="width: 33%;">Key Classes</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td><strong>Abstraction</strong></td>
                <td>Abstract base class encapsulating universal demographic fields (ID, name, age, phone) with abstract role method <code>getRoleTitle()</code>.</td>
                <td><code>Person.java</code> (Abstract Superclass)</td>
            </tr>
            <tr>
                <td><strong>Inheritance</strong></td>
                <td>Specialized subclasses extending <code>Person</code>, adding clinical history, blood group, emergency contacts, qualifications, and specialties.</td>
                <td><code>Patient.java</code>, <code>Doctor.java</code></td>
            </tr>
            <tr>
                <td><strong>Polymorphism</strong></td>
                <td>Overridden <code>matches()</code> query searching; priority comparator in tokens; interface-driven observer notifications.</td>
                <td><code>Queueable.java</code>, <code>Searchable.java</code></td>
            </tr>
            <tr>
                <td><strong>Encapsulation</strong></td>
                <td>All state variables marked <code>private</code>/<code>protected</code> with validated getter and setter access routines.</td>
                <td>All Domain Models</td>
            </tr>
            <tr>
                <td><strong>Collections Framework</strong></td>
                <td><code>LinkedHashMap</code> for $O(1)$ fast lookups; <code>LinkedList</code> for dual FIFO priority queues; <code>ArrayList</code> for records.</td>
                <td><code>HospitalManager.java</code></td>
            </tr>
        </tbody>
    </table>
</div>

<!-- ============================================================
     PAGE 5: MODULE 1 — DASHBOARD
     ============================================================ -->
<div class="page-break">
    <h1 class="chapter-title">3. Module 1: Hospital Overview & Analytics Dashboard</h1>
    <p>
        The Dashboard provides hospital administrators and clinical staff with a high-level operational overview of hospital activity, live queue progression, and recent appointment bookings.
    </p>

    <div class="screenshot-hero">
        <img src="{img_dashboard}" alt="Hospital Dashboard Screen">
        <div class="screenshot-caption">Figure 3.1: Hospital Overview & Real-Time Analytics Dashboard Screen</div>
    </div>

    <div class="feature-grid">
        <div class="feature-item">
            <h4>Live KPI Summary Cards</h4>
            <p>Six real-time metric cards displaying Total Patients, Total Bookings, Waiting in Queue, Emergency Queue, Completed Consultations, and Active Doctors.</p>
        </div>
        <div class="feature-item">
            <h4>Real-Time Queue Banner</h4>
            <p>Live alert banner displaying current queue breakdown (Emergency count vs Regular count) and the patient currently inside the consultation chamber.</p>
        </div>
        <div class="feature-item">
            <h4>Recent Appointments Table</h4>
            <p>Tabular view of the latest outpatient bookings with date, slot, patient name, assigned specialist, and rounded color-coded status badges.</p>
        </div>
        <div class="feature-item">
            <h4>Quick Action Shortcuts</h4>
            <p>One-click dispatch buttons to initiate patient registration, book appointments, admit emergency walk-ins, and jump directly to the consultation desk.</p>
        </div>
    </div>
</div>

<!-- ============================================================
     PAGE 6: MODULE 2 — PATIENT MANAGEMENT
     ============================================================ -->
<div class="page-break">
    <h1 class="chapter-title">4. Module 2: Patient Registration & Search Directory</h1>
    <p>
        The Patient Management module implements full electronic medical record (EMR) intake capabilities with instant multi-field searching and direct queue dispatching.
    </p>

    <div class="screenshot-hero">
        <img src="{img_patients}" alt="Patient Management Screen">
        <div class="screenshot-caption">Figure 4.1: Patient Profile Management & Multi-Field Search Directory Screen</div>
    </div>

    <div class="feature-grid">
        <div class="feature-item">
            <h4>Demographic Intake Form</h4>
            <p>Captures Patient ID (auto-generated <code>PAT-XXXX</code>), Full Name, Age, Gender, Phone, Email, Blood Group, Residential Address, Allergies, and Emergency Contact.</p>
        </div>
        <div class="feature-item">
            <h4>Real-Time Search Engine</h4>
            <p>Filters directory records instantaneously as the user types across Patient ID, Name, Phone Number, and Blood Group using polymorphic <code>matches()</code> queries.</p>
        </div>
        <div class="feature-item">
            <h4>Record CRUD Operations</h4>
            <p>Supports registering new patients, updating existing demographics, clearing inputs, and safely deleting records with confirmation dialogs.</p>
        </div>
        <div class="feature-item">
            <h4>Direct Live Queue Dispatch</h4>
            <p>A dedicated button allowing staff to dispatch any selected patient directly into the live triage queue with assigned doctor and priority level.</p>
        </div>
    </div>
</div>

<!-- ============================================================
     PAGE 7: MODULE 3 — DOCTOR DIRECTORY
     ============================================================ -->
<div class="page-break">
    <h1 class="chapter-title">5. Module 3: Doctor & Medical Specialist Directory</h1>
    <p>
        The Doctor Directory module organizes physicians by medical specialization, consultation room assignments, clinical qualifications, experience years, and duty shift schedules.
    </p>

    <div class="screenshot-hero">
        <img src="{img_doctors}" alt="Doctor Management Screen">
        <div class="screenshot-caption">Figure 5.1: Medical Specialist Directory and Consultation Room Management Screen</div>
    </div>

    <div class="feature-grid">
        <div class="feature-item">
            <h4>Specialist Profiling</h4>
            <p>Maintains comprehensive records for specialists across Cardiology, Neurology, Pediatrics, Orthopedics, General Medicine, Oncology, and Dermatology.</p>
        </div>
        <div class="feature-item">
            <h4>Chamber & Contact Allocation</h4>
            <p>Assigns consultation room/chamber numbers, direct hospital phone extensions, and institutional email addresses to every doctor profile.</p>
        </div>
        <div class="feature-item">
            <h4>Experience & Qualifications</h4>
            <p>Tracks post-graduate medical degrees (MD, MS, MBBS, FACC), total clinical experience years, and assigned duty shift timings (e.g. 09:00 AM - 03:00 PM).</p>
        </div>
        <div class="feature-item">
            <h4>Specialist Search & Filtering</h4>
            <p>Provides fast multi-field search allowing receptionists to look up available doctors by name, medical specialization, or room number.</p>
        </div>
    </div>
</div>

<!-- ============================================================
     PAGE 8: MODULE 4 — APPOINTMENTS
     ============================================================ -->
<div class="page-break">
    <h1 class="chapter-title">6. Module 4: Appointment Scheduling & Conflict Resolution</h1>
    <p>
        The Appointment Scheduling module handles outpatient bookings, verifies slot availability, eliminates double-booking conflicts, and dispatches patients to the live queue.
    </p>

    <div class="screenshot-hero">
        <img src="{img_appointments}" alt="Appointment Management Screen">
        <div class="screenshot-caption">Figure 6.1: Outpatient Appointment Booking and Duplicate Conflict Resolution Screen</div>
    </div>

    <div class="feature-grid">
        <div class="feature-item">
            <h4>Unique Appointment ID Generation</h4>
            <p>Automatically generates sequential, collision-free identifiers (<code>APT-5001</code>, <code>APT-5002</code>, etc.) for every scheduled booking.</p>
        </div>
        <div class="feature-item">
            <h4>Duplicate Conflict Prevention</h4>
            <p>Validates bookings against existing records, throwing custom <code>DuplicateBookingException</code> if a doctor is double-booked for the same date and time slot.</p>
        </div>
        <div class="feature-item">
            <h4>Reschedule & Cancellation Actions</h4>
            <p>Interactive rescheduling dialog enables shifting appointments to new dates and time slots while enforcing the duplicate conflict validation.</p>
        </div>
        <div class="feature-item">
            <h4>Queue Dispatch Workflow</h4>
            <p>One-click action to transition a confirmed appointment into an active queue token, automatically updating the appointment status to <code>IN_QUEUE</code>.</p>
        </div>
    </div>
</div>

<!-- ============================================================
     PAGE 9: MODULE 5 — PRIORITY QUEUE
     ============================================================ -->
<div class="page-break">
    <h1 class="chapter-title">7. Module 5: Dual Priority Queue Management</h1>
    <p>
        The Queue Management module enforces strict emergency triage by operating two parallel FIFO queues and guaranteeing that acute trauma cases receive non-preemptive priority.
    </p>

    <div class="screenshot-hero">
        <img src="{img_queue}" alt="Queue Management Screen">
        <div class="screenshot-caption">Figure 7.1: Dual Priority Patient Queue Screen with Emergency Precedence Enforcement</div>
    </div>

    <div class="feature-grid">
        <div class="feature-item">
            <h4>Dual Priority Queues</h4>
            <p>Displays separate visual tables for the <strong>Emergency Queue (Priority 1)</strong> in crimson and the <strong>Regular Queue (Priority 2)</strong> in navy.</p>
        </div>
        <div class="feature-item">
            <h4>Strict Emergency Priority Dispatch</h4>
            <p>The queue engine strictly checks the emergency queue first; regular patients are only called when zero emergency patients are waiting.</p>
        </div>
        <div class="feature-item">
            <h4>Dynamic "Next-in-Line" Banner</h4>
            <p>Automatically computes and highlights the next patient to be attended, their assigned specialist, chief complaint, and arrival timestamp.</p>
        </div>
        <div class="feature-item">
            <h4>Direct Walk-In Token Generator</h4>
            <p>Enables triage nurses to issue emergency (<code>TKN-EMG-XXX</code>) or regular (<code>TKN-REG-XXX</code>) tokens directly for walk-in arrivals without prior booking.</p>
        </div>
    </div>
</div>

<!-- ============================================================
     PAGE 10: MODULE 6 — CONSULTATION DESK
     ============================================================ -->
<div class="page-break">
    <h1 class="chapter-title">8. Module 6: Doctor Consultation Desk & Medical Examination</h1>
    <p>
        The Consultation Desk serves as the doctor's workstation, where queued patients are examined, vitals recorded, diagnoses formulated, and electronic prescriptions generated.
    </p>

    <div class="screenshot-hero">
        <img src="{img_consultation}" alt="Consultation Desk Screen">
        <div class="screenshot-caption">Figure 8.1: Doctor Consultation Desk with Electronic Prescription Entry & History Archive</div>
    </div>

    <div class="feature-grid">
        <div class="feature-item">
            <h4>Active Examination Station</h4>
            <p>Displays attending specialist name, patient identity, age, gender, chief complaint, and triage priority badge of the patient currently in the chamber.</p>
        </div>
        <div class="feature-item">
            <h4>Multi-Threaded Consumer Simulator</h4>
            <p>Toggle button to launch an autonomous background worker thread simulating consultation processing with real-time progress bar feedback.</p>
        </div>
        <div class="feature-item">
            <h4>Electronic Clinical Intake Form</h4>
            <p>Fields for Clinical Vitals (BP, Heart Rate, SpO2, Temperature), Symptoms, Formal Diagnosis, Prescription & Dosage Schedule, and Follow-up Advice.</p>
        </div>
        <div class="feature-item">
            <h4>Permanent Clinical History Archive</h4>
            <p>Completed consultations are logged in the history table with a "View Full Medical Record" dialog displaying printable consultation summaries.</p>
        </div>
    </div>
</div>

<!-- ============================================================
     PAGE 11: SOURCE CODE — PRODUCER-CONSUMER QUEUE ENGINE
     ============================================================ -->
<div class="page-break">
    <h1 class="chapter-title">9. Core Source Code: Producer–Consumer Concurrency</h1>
    <p>
        The <code>HospitalQueueEngine</code> class represents the concurrent bounded buffer coordinating reception producers and doctor consumers using Java intrinsic monitors.
    </p>

    <div class="code-box">
        <div class="code-header">
            <span>HospitalQueueEngine.java — Monitor Synchronization Core</span>
            <span>Package: hospital.queue</span>
        </div>
        <pre><code>package hospital.queue;

import hospital.exceptions.QueueEmptyException;
import hospital.model.PatientType;
import hospital.model.QueueToken;
import java.util.LinkedList;

public class HospitalQueueEngine {{
    private final LinkedList&lt;QueueToken&gt; emergencyQueue = new LinkedList&lt;&gt;();
    private final LinkedList&lt;QueueToken&gt; regularQueue = new LinkedList&lt;&gt;();
    private final int capacity = 100;
    private QueueToken currentlyServing = null;

    // PRODUCER METHOD: Adds token to queue; waits if full, notifies waiting consumers
    public synchronized void produceToken(QueueToken token) throws InterruptedException {{
        while ((emergencyQueue.size() + regularQueue.size()) >= capacity) {{
            wait(); // Bounded buffer full: Producer waits
        }}
        if (token.isEmergency()) {{
            emergencyQueue.addLast(token);
        }} else {{
            regularQueue.addLast(token);
        }}
        notifyAll(); // Wake up consumers waiting for patients
        notifyObserversQueueUpdated();
    }}

    // CONSUMER METHOD: Strict Emergency Priority Queue Retrieval
    public synchronized QueueToken consumeToken() throws InterruptedException {{
        while (emergencyQueue.isEmpty() && regularQueue.isEmpty()) {{
            wait(); // Queue empty: Consumer thread waits
        }}
        // Strict Priority: Emergency queue polled first
        QueueToken token = !emergencyQueue.isEmpty() 
                ? emergencyQueue.removeFirst() 
                : regularQueue.removeFirst();
        this.currentlyServing = token;
        token.setStatus("IN_CONSULTATION");
        notifyAll(); // Wake up producers waiting for space
        notifyObserversQueueUpdated();
        notifyObserversPatientCalled(token);
        return token;
    }}

    // Non-blocking immediate consumption for manual UI click
    public synchronized QueueToken pollNextImmediate() throws QueueEmptyException {{
        if (emergencyQueue.isEmpty() && regularQueue.isEmpty()) {{
            throw new QueueEmptyException("No patients currently waiting in the queue.");
        }}
        QueueToken token = !emergencyQueue.isEmpty() ? emergencyQueue.removeFirst() : regularQueue.removeFirst();
        this.currentlyServing = token;
        token.setStatus("IN_CONSULTATION");
        notifyAll();
        notifyObserversQueueUpdated();
        notifyObserversPatientCalled(token);
        return token;
    }}
}}</code></pre>
    </div>
</div>

<!-- ============================================================
     PAGE 12: SOURCE CODE — PERSON & PATIENT MODELS
     ============================================================ -->
<div class="page-break">
    <h1 class="chapter-title">10. Core Source Code: Abstract Person & Patient Model</h1>
    <p>
        Demonstrates Object-Oriented Abstraction and Inheritance: <code>Person</code> defines universal demographic fields, while <code>Patient</code> extends it with clinical attributes.
    </p>

    <div class="code-box">
        <div class="code-header">
            <span>Person.java — Abstract Base Class</span>
            <span>Package: hospital.model</span>
        </div>
        <pre><code>package hospital.model;

import java.io.Serializable;

public abstract class Person implements Searchable, Serializable {{
    protected String id;
    protected String name;
    protected int age;
    protected String gender;
    protected String phone;
    protected String email;
    protected String address;

    public Person(String id, String name, int age, String gender, String phone, String email, String address) {{
        this.id = id; this.name = name; this.age = age;
        this.gender = gender; this.phone = phone; this.email = email; this.address = address;
    }}

    public abstract String getRoleTitle(); // Polymorphic method

    @Override
    public boolean matches(String query) {{
        if (query == null || query.trim().isEmpty()) return true;
        String q = query.toLowerCase().trim();
        return (id != null && id.toLowerCase().contains(q)) ||
               (name != null && name.toLowerCase().contains(q)) ||
               (phone != null && phone.toLowerCase().contains(q));
    }}
}}</code></pre>
    </div>

    <div class="code-box">
        <div class="code-header">
            <span>Patient.java — Subclass Extending Person</span>
            <span>Package: hospital.model</span>
        </div>
        <pre><code>package hospital.model;

public class Patient extends Person {{
    private String bloodGroup;
    private String medicalHistory;
    private String emergencyContact;

    public Patient(String id, String name, int age, String gender, String phone,
                   String email, String address, String bloodGroup,
                   String medicalHistory, String emergencyContact) {{
        super(id, name, age, gender, phone, email, address);
        this.bloodGroup = bloodGroup;
        this.medicalHistory = medicalHistory;
        this.emergencyContact = emergencyContact;
    }}

    @Override
    public String getRoleTitle() {{ return "Patient"; }}

    @Override
    public boolean matches(String query) {{
        if (super.matches(query)) return true;
        String q = query.toLowerCase().trim();
        return (bloodGroup != null && bloodGroup.toLowerCase().contains(q)) ||
               (medicalHistory != null && medicalHistory.toLowerCase().contains(q));
    }}
}}</code></pre>
    </div>
</div>

<!-- ============================================================
     PAGE 13: SOURCE CODE — DOCTOR, TOKEN & EXCEPTION
     ============================================================ -->
<div class="page-break">
    <h1 class="chapter-title">11. Core Source Code: Doctor, Priority Token & Exception</h1>
    <p>
        Showcases physician profiling, priority queue comparison logic via <code>Comparable&lt;QueueToken&gt;</code>, and checked exception handling.
    </p>

    <div class="code-box">
        <div class="code-header">
            <span>QueueToken.java — Priority Comparator Implementation</span>
            <span>Package: hospital.model</span>
        </div>
        <pre><code>package hospital.model;

public class QueueToken implements Queueable, Comparable&lt;QueueToken&gt; {{
    private String tokenNumber;
    private String patientName;
    private PatientType type; // REGULAR or EMERGENCY
    private long timestamp;

    @Override
    public int compareTo(QueueToken other) {{
        // Strict Priority: Emergency cases precede Regular cases
        if (this.isEmergency() && !other.isEmergency()) return -1;
        if (!this.isEmergency() && other.isEmergency()) return 1;
        // Equal priority resolved via FIFO arrival timestamp
        return Long.compare(this.timestamp, other.timestamp);
    }}
    public boolean isEmergency() {{ return type == PatientType.EMERGENCY; }}
}}</code></pre>
    </div>

    <div class="code-box">
        <div class="code-header">
            <span>DuplicateBookingException.java — Custom Checked Exception</span>
            <span>Package: hospital.exceptions</span>
        </div>
        <pre><code>package hospital.exceptions;

public class DuplicateBookingException extends Exception {{
    private static final long serialVersionUID = 1L;

    public DuplicateBookingException(String message) {{
        super(message);
    }}
}}</code></pre>
    </div>

    <div class="code-box">
        <div class="code-header">
            <span>Doctor.java — Subclass with Specialist Attributes</span>
            <span>Package: hospital.model</span>
        </div>
        <pre><code>package hospital.model;

public class Doctor extends Person {{
    private String specialization;
    private String roomNumber;
    private String qualification;
    private int experienceYears;

    @Override
    public String getRoleTitle() {{ return "Dr. (" + specialization + ")"; }}
}}</code></pre>
    </div>
</div>

<!-- ============================================================
     PAGE 14: SOURCE CODE — HOSPITAL MANAGER (COLLECTIONS)
     ============================================================ -->
<div class="page-break">
    <h1 class="chapter-title">12. Core Source Code: HospitalManager Controller</h1>
    <p>
        The central singleton controller managing in-memory collections (<code>LinkedHashMap</code>, <code>LinkedList</code>, <code>ArrayList</code>) and conflict prevention.
    </p>

    <div class="code-box">
        <div class="code-header">
            <span>HospitalManager.java — Business Logic & Collections Management</span>
            <span>Package: hospital.service</span>
        </div>
        <pre><code>package hospital.service;

import hospital.exceptions.DuplicateBookingException;
import hospital.exceptions.InvalidInputException;
import hospital.model.*;
import java.util.*;

public class HospitalManager {{
    private static HospitalManager instance;
    private final Map&lt;String, Patient&gt; patientMap = new LinkedHashMap&lt;&gt;();
    private final Map&lt;String, Doctor&gt; doctorMap = new LinkedHashMap&lt;&gt;();
    private final Map&lt;String, Appointment&gt; appointmentMap = new LinkedHashMap&lt;&gt;();
    private final List&lt;ConsultationRecord&gt; consultationHistory = new ArrayList&lt;&gt;();

    public static synchronized HospitalManager getInstance() {{
        if (instance == null) instance = new HospitalManager();
        return instance;
    }}

    // Conflict-Free Booking with DuplicateBookingException Enforcement
    public synchronized void bookAppointment(Appointment appt) 
            throws DuplicateBookingException, InvalidInputException {{
        for (Appointment existing : appointmentMap.values()) {{
            if (existing.getStatus() != AppointmentStatus.CANCELLED &&
                existing.getDoctorId().equalsIgnoreCase(appt.getDoctorId()) &&
                existing.getAppointmentDate().equals(appt.getAppointmentDate()) &&
                existing.getTimeSlot().equalsIgnoreCase(appt.getTimeSlot())) {{
                throw new DuplicateBookingException("Dr. " + existing.getDoctorName() +
                        " already has an appointment booked on " + appt.getAppointmentDate() +
                        " at " + appt.getTimeSlot() + ". Please choose another time slot.");
            }}
        }}
        if (appt.getAppointmentId() == null) appt.setAppointmentId(generateNextAppointmentId());
        appointmentMap.put(appt.getAppointmentId(), appt);
        notifyChangeListeners();
    }}
}}</code></pre>
    </div>
</div>

<!-- ============================================================
     PAGE 15: SYSTEM OUTPUTS & EXECUTION TRACES
     ============================================================ -->
<div class="page-break">
    <h1 class="chapter-title">13. System Outputs & Terminal Execution Traces</h1>
    <p>
        Below are actual runtime console execution traces illustrating startup initialization, concurrent Producer–Consumer event coordination, and a sample clinical consultation record generated by the system.
    </p>

    <h2 class="sub-title">13.1 Application Initialization & Startup Trace</h2>
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

    <h2 class="sub-title">13.2 Multi-Threaded Producer–Consumer Runtime Execution Trace</h2>
    <div class="terminal-box">[Queue Consumer] Queue is empty. Waiting for patients...
[Queue Producer] Added REGULAR Token: TKN-REG-102 - Arthur Pendelton (Buffer size: 1)
[Queue Consumer] Consumed REGULAR Token: TKN-REG-102
[Queue Producer] Added EMERGENCY Token: TKN-EMG-502 - David Kim (Buffer size: 1)
[Queue Consumer] Consumed EMERGENCY Token: TKN-EMG-502
[Queue Producer] Added EMERGENCY Token: TKN-EMG-503 - Clara Oswald
[Queue Producer] Added REGULAR Token: TKN-REG-103 - Samantha Hayes
[Queue Consumer] Consumed EMERGENCY Token: TKN-EMG-503 (Emergency Priority Served First!)
[Queue Consumer] Consumed REGULAR Token: TKN-REG-103 (Regular Patient Served)</div>

    <h2 class="sub-title">13.3 System-Generated Clinical Medical Record & Prescription Output</h2>
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
</div>

<!-- ============================================================
     PAGE 16: CONCLUSION, OUTCOMES & REFERENCES
     ============================================================ -->
<div class="page-break">
    <h1 class="chapter-title">14. Conclusion & Project-Based Learning Outcomes</h1>
    <p>
        The <strong>Hospital Appointment and Patient Queue Management System</strong> successfully satisfies all requirements of the <strong>Advance java programing</strong> curriculum in the <strong>Department of Data Engineering</strong>.
    </p>

    <h2 class="sub-title">14.1 Technical Learning Outcomes</h2>
    <ul>
        <li><strong>Multi-Threaded Concurrency:</strong> Mastered thread coordination using intrinsic monitor synchronization (<code>wait()</code> and <code>notifyAll()</code>) under the Producer–Consumer architectural model.</li>
        <li><strong>Dual-Queue Priority Dispatch:</strong> Implemented algorithms to guarantee emergency triage precedence over standard appointments.</li>
        <li><strong>Defensive Programming:</strong> Enforced clinical scheduling integrity using custom checked exceptions (<code>DuplicateBookingException</code>).</li>
        <li><strong>Responsive Desktop UI:</strong> Designed modular Java Swing interfaces with smooth user interactions by offloading heavy execution to background worker threads.</li>
        <li><strong>Efficient Data Management:</strong> Utilized Java Collections (<code>LinkedHashMap</code>, <code>LinkedList</code>, <code>ArrayList</code>) for predictable, high-performance in-memory state manipulation.</li>
    </ul>

    <h2 class="sub-title">14.2 Future Scope & Enhancements</h2>
    <ul>
        <li><strong>Database Integration:</strong> Connecting persistence layers via JDBC to relational databases (PostgreSQL/MySQL).</li>
        <li><strong>Automated Notifications:</strong> Sending SMS/Email token alerts to patients when their turn approaches.</li>
        <li><strong>Distributed Networking:</strong> Transitioning to a client-server architecture using Java RMI or RESTful sockets across hospital intranet terminals.</li>
    </ul>

    <h2 class="sub-title">14.3 References & Bibliography</h2>
    <ol>
        <li>Bloch, Joshua. <em>Effective Java (3rd Edition)</em>. Addison-Wesley, 2018.</li>
        <li>Goetz, Brian, et al. <em>Java Concurrency in Practice</em>. Addison-Wesley Professional, 2006.</li>
        <li>Oracle Corporation. <em>Java Platform, Standard Edition Documentation (Java SE 25)</em>.</li>
        <li>Eckstein, Robert, Marc Loy, and Dave Wood. <em>Java Swing (2nd Edition)</em>. O'Reilly Media.</li>
    </ol>

    <hr style="border: 0; border-top: 1px solid #CBD5E1; margin: 25px 0 8px 0;">
    <p style="text-align: center; font-size: 8pt; color: #64748B;">
        &copy; 2026 Department of Data Engineering &bull; Course: Advance java programing &bull; PBL Capstone Report
    </p>
</div>

</body>
</html>
"""

with open("PROJECT_REPORT.html", "w", encoding="utf-8") as f:
    f.write(html_content)

print("PROJECT_REPORT.html successfully built for 16-page target!")
