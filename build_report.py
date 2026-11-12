import base64
import os
import html

def get_base64(path):
    if os.path.exists(path):
        with open(path, "rb") as f:
            return "data:image/png;base64," + base64.b64encode(f.read()).decode("utf-8")
    return ""

def read_code(path):
    if os.path.exists(path):
        with open(path, "r", encoding="utf-8") as f:
            return html.escape(f.read())
    return ""

img_dashboard = get_base64("screenshots/dashboard.png")
img_patients = get_base64("screenshots/patients.png")
img_doctors = get_base64("screenshots/doctors.png")
img_appointments = get_base64("screenshots/appointments.png")
img_queue = get_base64("screenshots/queue.png")
img_consultation = get_base64("screenshots/consultation.png")

code_person = read_code("src/hospital/model/Person.java")
code_patient = read_code("src/hospital/model/Patient.java")
code_doctor = read_code("src/hospital/model/Doctor.java")
code_token = read_code("src/hospital/model/QueueToken.java")
code_dup_ex = read_code("src/hospital/exceptions/DuplicateBookingException.java")
code_queue_engine = read_code("src/hospital/queue/HospitalQueueEngine.java")
code_manager = read_code("src/hospital/service/HospitalManager.java")
code_mainframe = read_code("src/hospital/ui/MainFrame.java")

html_content = f"""<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Hospital Appointment & Patient Queue Management System - PBL Project Report</title>
<style>
    @page {{
        size: A4 portrait;
        margin: 20mm 18mm 20mm 18mm;
        @bottom-right {{
            content: "Page " counter(page);
            font-family: 'Segoe UI', Arial, sans-serif;
            font-size: 8.5pt;
            color: #64748B;
        }}
        @bottom-left {{
            content: "Department of Data Engineering • advance java programing";
            font-family: 'Segoe UI', Arial, sans-serif;
            font-size: 8.5pt;
            color: #64748B;
        }}
        @top-right {{
            content: "Hospital Queue & Appointment System";
            font-family: 'Segoe UI', Arial, sans-serif;
            font-size: 8.5pt;
            color: #94A3B8;
        }}
    }}

    * {{
        box-sizing: border-box;
    }}

    body {{
        font-family: 'Segoe UI', -apple-system, BlinkMacSystemFont, Roboto, Helvetica, Arial, sans-serif;
        color: #1E293B;
        line-height: 1.6;
        font-size: 10pt;
        background: #FFFFFF;
        margin: 0;
        padding: 0;
    }}

    /* ============================================================
       COVER PAGE STYLING (NO SYMBOL, FORMAL ACADEMIC FORMAT)
       ============================================================ */
    .cover-container {{
        page-break-after: always;
        height: 96vh;
        border: 3px double #0B2545;
        outline: 1px solid #1E3A8A;
        outline-offset: -7px;
        padding: 45px 32px 35px 32px;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        text-align: center;
        background: radial-gradient(circle at top right, #F8FAFC 0%, #FFFFFF 70%);
    }}

    .cover-header h3 {{
        font-family: 'Georgia', 'Times New Roman', serif;
        font-size: 15pt;
        letter-spacing: 3px;
        color: #0B2545;
        margin: 0 0 8px 0;
        text-transform: uppercase;
        font-weight: 700;
    }}

    .cover-header h4 {{
        font-family: 'Segoe UI', sans-serif;
        font-size: 12pt;
        font-weight: 600;
        color: #1E40AF;
        margin: 0 0 6px 0;
        text-transform: uppercase;
        letter-spacing: 1.5px;
    }}

    .cover-header p {{
        font-size: 10pt;
        color: #64748B;
        margin: 0;
        font-weight: 500;
    }}

    .cover-title-section {{
        margin: 25px 0;
        padding: 28px 22px;
        background: #FFFFFF;
        border-top: 3.5px solid #1E40AF;
        border-bottom: 3.5px solid #1E40AF;
        box-shadow: 0 3px 12px rgba(15, 23, 42, 0.05);
    }}

    .cover-title-section .subtitle-tag {{
        display: inline-block;
        background: #DBEAFE;
        color: #1E40AF;
        padding: 4px 14px;
        border-radius: 20px;
        font-size: 9pt;
        font-weight: 700;
        letter-spacing: 1px;
        text-transform: uppercase;
        margin-bottom: 14px;
    }}

    .cover-title-section h1 {{
        font-family: 'Georgia', 'Times New Roman', serif;
        font-size: 23pt;
        color: #0B2545;
        margin: 0 0 10px 0;
        line-height: 1.28;
        font-weight: 700;
    }}

    .cover-title-section h2 {{
        font-size: 12pt;
        color: #2563EB;
        font-weight: 600;
        margin: 0;
        line-height: 1.45;
    }}

    .cover-details-grid {{
        width: 90%;
        margin: 0 auto;
        border: 1px solid #CBD5E1;
        border-radius: 6px;
        background: #F8FAFC;
        overflow: hidden;
    }}

    .cover-details-grid table {{
        width: 100%;
        border-collapse: collapse;
        font-size: 10.5pt;
    }}

    .cover-details-grid td {{
        padding: 9px 16px;
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
        margin-top: 30px;
        padding: 0 35px;
    }}

    .sig-block {{
        width: 42%;
        text-align: center;
        border-top: 1.5px solid #94A3B8;
        padding-top: 8px;
        font-size: 9.5pt;
        color: #475569;
        font-weight: 600;
    }}

    .cover-footer {{
        font-size: 9pt;
        color: #94A3B8;
        margin-top: 10px;
    }}

    /* ============================================================
       REPORT BODY STYLING
       ============================================================ */
    h1.chapter-title {{
        font-family: 'Georgia', 'Times New Roman', serif;
        font-size: 15.5pt;
        color: #0B2545;
        border-bottom: 2.5px solid #1E40AF;
        padding-bottom: 5px;
        margin-top: 28px;
        margin-bottom: 14px;
        page-break-after: avoid;
        display: flex;
        align-items: center;
    }}

    h1.chapter-title::before {{
        content: "";
        display: inline-block;
        width: 6px;
        height: 18px;
        background: #2563EB;
        margin-right: 10px;
        border-radius: 2px;
    }}

    h2.sub-title {{
        font-size: 12pt;
        color: #1E3A8A;
        margin-top: 18px;
        margin-bottom: 8px;
        font-weight: 700;
        page-break-after: avoid;
    }}

    h3 {{
        font-size: 10.5pt;
        color: #0284C7;
        margin-top: 14px;
        margin-bottom: 6px;
        font-weight: 700;
        page-break-after: avoid;
    }}

    p {{
        margin: 0 0 10px 0;
        text-align: justify;
        color: #334155;
    }}

    ul, ol {{
        margin: 0 0 12px 0;
        padding-left: 22px;
        color: #334155;
    }}

    li {{
        margin-bottom: 4px;
    }}

    /* Image & Screenshot Containers */
    .screenshot-card {{
        margin: 16px 0 20px 0;
        border: 1px solid #CBD5E1;
        border-radius: 8px;
        overflow: hidden;
        background: #FFFFFF;
        box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05);
        page-break-inside: avoid;
    }}

    .screenshot-card img {{
        width: 100%;
        height: auto;
        display: block;
        border-bottom: 1px solid #E2E8F0;
    }}

    .screenshot-caption {{
        padding: 8px 14px;
        background: #F8FAFC;
        font-size: 9pt;
        color: #475569;
        font-weight: 600;
        text-align: center;
    }}

    /* Code Blocks */
    pre {{
        background-color: #0F172A;
        color: #E2E8F0;
        padding: 12px 14px;
        border-radius: 6px;
        font-family: 'Consolas', 'Courier New', monospace;
        font-size: 8pt;
        line-height: 1.4;
        overflow-x: auto;
        page-break-inside: avoid;
        border: 1px solid #1E293B;
        margin: 10px 0 16px 0;
    }}

    code {{
        font-family: 'Consolas', monospace;
        background-color: #F1F5F9;
        color: #B91C1C;
        padding: 2px 5px;
        border-radius: 3px;
        font-size: 8.5pt;
    }}

    pre code {{
        background: transparent;
        color: inherit;
        padding: 0;
    }}

    .code-header {{
        background: #1E293B;
        color: #93C5FD;
        font-family: 'Consolas', monospace;
        font-size: 8.5pt;
        padding: 5px 12px;
        border-radius: 6px 6px 0 0;
        border-bottom: 1px solid #334155;
        font-weight: bold;
        margin-top: 12px;
        margin-bottom: -10px;
    }}

    /* Callout & Architecture */
    .callout {{
        background-color: #F0F9FF;
        border-left: 4px solid #0284C7;
        padding: 12px 16px;
        margin: 14px 0;
        border-radius: 0 6px 6px 0;
        page-break-inside: avoid;
    }}

    .callout-title {{
        font-weight: 700;
        color: #0369A1;
        margin-bottom: 4px;
        font-size: 10pt;
    }}

    .terminal-output {{
        background: #020617;
        color: #38BDF8;
        font-family: 'Consolas', monospace;
        font-size: 8.5pt;
        padding: 14px;
        border-radius: 6px;
        line-height: 1.45;
        white-space: pre;
        margin: 12px 0 16px 0;
        border: 1px solid #1E293B;
        page-break-inside: avoid;
    }}

    .page-break {{
        page-break-after: always;
    }}
</style>
</head>
<body>

<!-- ============================================================
     COVER PAGE (NO SYMBOL, SUBJECT: advance java programing)
     ============================================================ -->
<div class="cover-container">
    <div class="cover-header">
        <h3>PROJECT-BASED LEARNING (PBL) CAPSTONE REPORT</h3>
        <h4>DEPARTMENT OF DATA ENGINEERING</h4>
        <p>Subject: advance java programing</p>
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
                <td class="val-col"><strong>advance java programing</strong></td>
            </tr>
            <tr>
                <td class="label-col">Project Domain:</td>
                <td class="val-col">Healthcare Systems & Concurrent Data Engineering</td>
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
                <td class="label-col">Implementation Status:</td>
                <td class="val-col"><strong>Complete & Functional Desktop GUI Application</strong></td>
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
        Academic Year 2026 &bull; Department of Data Engineering &bull; advance java programing
    </div>
</div>

<!-- ============================================================
     CHAPTER 1: ABSTRACT & OBJECTIVES
     ============================================================ -->
<h1 class="chapter-title">1. Executive Summary & Abstract</h1>
<p>
Modern outpatient healthcare departments manage hundreds of patient visits daily across medical specialties. Manual queuing and disjointed scheduling lead to significant patient waiting times, clerical appointment conflicts, and hazardous delays when handling emergency trauma arrivals.
</p>
<p>
The <strong>Hospital Appointment and Patient Queue Management System</strong> is a standalone clinical software application engineered in pure <strong>Java Swing</strong> by the <strong>Department of Data Engineering</strong> for the course <strong>advance java programing</strong>. The application features a medical design theme (Deep Navy, Pure White, and Sky Blue) and incorporates essential Advanced Java engineering paradigms:
</p>
<ul>
    <li><strong>Multi-Threaded Producer–Consumer Queue:</strong> Uses Java intrinsic monitors (<code>synchronized</code>, <code>wait()</code>, and <code>notifyAll()</code>) to guarantee thread safety between appointment dispatchers (producers) and consultation examination desks (consumers).</li>
    <li><strong>Strict Emergency Priority Triage:</strong> Maintains independent Emergency and Regular patient queues where critical patients receive non-preemptive priority over standard appointments.</li>
    <li><strong>Object-Oriented Architecture:</strong> Features an abstract base <code>Person</code> class with polymorphic subclasses (<code>Patient</code> and <code>Doctor</code>) and decoupled interfaces (<code>Queueable</code>, <code>Searchable</code>, <code>QueueObserver</code>).</li>
    <li><strong>Data Integrity & Exception Handling:</strong> Implements custom checked exceptions including <code>DuplicateBookingException</code> to prevent double-booking specialists for identical time slots.</li>
    <li><strong>Responsive GUI & Dynamic Observer:</strong> Offloads heavy tasks to background worker threads while routing UI mutations to the Swing Event Dispatch Thread (EDT) via <code>SwingUtilities.invokeLater()</code>.</li>
</ul>

<div class="callout">
    <div class="callout-title">Core Engineering Objectives</div>
    <ol>
        <li>Construct an intuitive desktop interface with six functional modules: Dashboard, Patient Management, Doctor Directory, Appointment Dispatch, Priority Queues, and Consultation Desk.</li>
        <li>Implement thread-safe Producer–Consumer queuing utilizing Java monitor synchronization.</li>
        <li>Enforce strict priority queue dispatch ensuring emergency patients are attended before standard appointments.</li>
        <li>Eliminate scheduling conflicts through custom validation and duplicate-booking exception handling.</li>
        <li>Ensure 100% responsiveness on the Swing Event Dispatch Thread (EDT).</li>
    </ol>
</div>

<div class="page-break"></div>

<!-- ============================================================
     CHAPTER 2: APPLICATION OUTPUTS & MODULE SCREENSHOTS
     ============================================================ -->
<h1 class="chapter-title">2. Application Outputs & Module Screenshots</h1>
<p>
Below are the actual GUI outputs captured from the live Java Swing desktop application, showcasing each clinical workstation module in operation.
</p>

<h2 class="sub-title">2.1 Module 1: Hospital Overview & Analytics Dashboard</h2>
<p>
The Dashboard provides real-time clinical visibility through 6 KPI summary cards (Total Patients, Total Appointments, Waiting in Queue, Emergency Queue, Completed Consultations, Active Doctors). It displays an interactive Live Queue Status Banner that monitors active consultations and a Recent Appointments table with status badges.
</p>
<div class="screenshot-card">
    <img src="{img_dashboard}" alt="Dashboard Screen">
    <div class="screenshot-caption">Figure 2.1: Real-Time Hospital Analytics Dashboard with Dynamic KPI Cards and Recent Bookings</div>
</div>

<h2 class="sub-title">2.2 Module 2: Patient Registration & Search Directory</h2>
<p>
The Patient Management module features a split-screen design with an electronic medical intake form on the left and a live-filtering directory table on the right. Staff can register new patients, update clinical records, and directly dispatch any patient into the live triage queue.
</p>
<div class="screenshot-card">
    <img src="{img_patients}" alt="Patient Management Screen">
    <div class="screenshot-caption">Figure 2.2: Patient Profile Management & Multi-Field Real-Time Search Directory</div>
</div>

<div class="page-break"></div>

<h2 class="sub-title">2.3 Module 3: Doctor & Specialist Directory</h2>
<p>
The Doctor Directory screen organizes hospital physicians across specialties (Cardiology, Neurology, Pediatrics, Orthopedics, General Medicine, etc.), consultation rooms, qualifications, experience years, and shift timings.
</p>
<div class="screenshot-card">
    <img src="{img_doctors}" alt="Doctor Management Screen">
    <div class="screenshot-caption">Figure 2.3: Medical Specialist Directory and Consultation Room Management</div>
</div>

<h2 class="sub-title">2.4 Module 4: Appointment Scheduling & Conflict Resolution</h2>
<p>
The Appointment module generates unique booking IDs (<code>APT-XXXX</code>), enforces slot availability, detects scheduling conflicts via <code>DuplicateBookingException</code>, and enables one-click patient dispatch to the live consultation queue.
</p>
<div class="screenshot-card">
    <img src="{img_appointments}" alt="Appointment Scheduling Screen">
    <div class="screenshot-caption">Figure 2.4: Outpatient Appointment Booking and Duplicate Conflict Resolution Screen</div>
</div>

<div class="page-break"></div>

<h2 class="sub-title">2.5 Module 5: Dual Priority Queue Management</h2>
<p>
Maintains separate queues for <strong>Emergency Patients (Priority 1)</strong> in crimson and <strong>Regular Patients (Priority 2)</strong> in navy. The Next-in-Line banner computes priority in real time, and the walk-in token producer enables on-the-spot token generation.
</p>
<div class="screenshot-card">
    <img src="{img_queue}" alt="Queue Management Screen">
    <div class="screenshot-caption">Figure 2.5: Dual Priority Patient Queue Screen with Emergency Precedence Enforcement</div>
</div>

<h2 class="sub-title">2.6 Module 6: Doctor Consultation Desk & Medical Examination</h2>
<p>
The active doctor's examination workstation displays the current patient, allows manual or simulated background consumer processing, records clinical vitals (BP, Heart Rate, SpO2, Temperature), and saves prescriptions to the clinical history archive.
</p>
<div class="screenshot-card">
    <img src="{img_consultation}" alt="Consultation Desk Screen">
    <div class="screenshot-caption">Figure 2.6: Doctor Consultation Desk with Producer–Consumer Processing and Electronic Prescription Entry</div>
</div>

<div class="page-break"></div>

<!-- ============================================================
     CHAPTER 3: SYSTEM EXECUTION LOGS & TEXT OUTPUTS
     ============================================================ -->
<h1 class="chapter-title">3. System Execution Logs & Terminal Outputs</h1>
<p>
The following terminal trace logs show the application launching, the multi-threaded Producer–Consumer engine dispatching tokens concurrently, and a sample clinical medical consultation record generated by the system.
</p>

<h2 class="sub-title">3.1 Application Startup & Initial Seed Log</h2>
<div class="terminal-output">==================================================
  Hospital Appointment & Queue Management System  
  Department of Data Engineering
  Course: advance java programing
  Status: Application launched successfully!     
==================================================
[Queue Producer] Added EMERGENCY Token: TKN-EMG-501 - Clara Oswald
[Queue Producer] Added REGULAR Token: TKN-REG-101 - Samantha Hayes
[MainFrame] Initialized 6 Core Modules with CardLayout
[Observer] Registered 6 UI panels for real-time synchronization</div>

<h2 class="sub-title">3.2 Multi-Threaded Producer–Consumer Execution Trace</h2>
<div class="terminal-output">[Queue Consumer] Queue is empty. Waiting for patients...
[Queue Producer] Added REGULAR Token: TKN-REG-102 - Arthur Pendelton
[Queue Consumer] Consumed REGULAR Token: TKN-REG-102
[Queue Producer] Added EMERGENCY Token: TKN-EMG-502 - David Kim
[Queue Consumer] Consumed EMERGENCY Token: TKN-EMG-502
[Queue Producer] Added EMERGENCY Token: TKN-EMG-503 - Clara Oswald
[Queue Producer] Added REGULAR Token: TKN-REG-103 - Samantha Hayes
[Queue Consumer] Consumed EMERGENCY Token: TKN-EMG-503 (Emergency Priority Served First)
[Queue Consumer] Consumed REGULAR Token: TKN-REG-103 (Regular Patient Served)</div>

<h2 class="sub-title">3.3 Clinical Consultation Record Output (Generated by System)</h2>
<div class="terminal-output">==========================================================
       HOSPITAL CLINICAL CONSULTATION REPORT
==========================================================
Record ID:      CNS-3001
Token Number:   TKN-REG-099
Date & Time:    2026-11-10 10:15
Patient ID:     PAT-1001
Patient Name:   Arthur Pendelton
Doctor:         Dr. Sarah Jenkins (DOC-201)

Vital Signs:    BP: 138/88 mmHg | Pulse: 74 bpm | SpO2: 98% | Temp: 98.4°F
Symptoms:       Occasional morning headaches, elevated BP
Diagnosis:      Stage 1 Essential Hypertension (Controlled)
Prescription:   Amlodipine 5mg once daily OD; Low sodium diet; 30min daily walk.
Doctor Notes:   Patient responded well to current dosage. Follow up in 3 months.
Status:         COMPLETED
==========================================================</div>

<div class="page-break"></div>

<!-- ============================================================
     CHAPTER 4: SOURCE CODE IMPLEMENTATION
     ============================================================ -->
<h1 class="chapter-title">4. Core Source Code Implementation</h1>
<p>
Below is the complete source code of the foundational classes demonstrating Object-Oriented Programming (OOP), Custom Exception Handling, Multi-threaded Producer–Consumer Concurrency, and UI Coordination.
</p>

<div class="code-header">1. Abstract Base Class: Person.java (Abstraction & Inheritance)</div>
<pre><code>{code_person}</code></pre>

<div class="code-header">2. Subclass: Patient.java (Inheritance & Polymorphism)</div>
<pre><code>{code_patient}</code></pre>

<div class="page-break"></div>

<div class="code-header">3. Priority Queue Token: QueueToken.java (Priority Comparable)</div>
<pre><code>{code_token}</code></pre>

<div class="code-header">4. Custom Checked Exception: DuplicateBookingException.java</div>
<pre><code>{code_dup_ex}</code></pre>

<div class="page-break"></div>

<div class="code-header">5. Multi-Threaded Queue Engine: HospitalQueueEngine.java (Producer–Consumer)</div>
<pre><code>{code_queue_engine}</code></pre>

<div class="page-break"></div>

<div class="code-header">6. UI MainFrame Coordinator: MainFrame.java (Swing CardLayout & Observer)</div>
<pre><code>{code_mainframe}</code></pre>

<div class="page-break"></div>

<!-- ============================================================
     CHAPTER 5: CONCLUSION
     ============================================================ -->
<h1 class="chapter-title">5. Conclusion & Learning Outcomes</h1>
<p>
The <strong>Hospital Appointment and Patient Queue Management System</strong> successfully fulfills the objectives of the <strong>advance java programing</strong> curriculum in the <strong>Department of Data Engineering</strong>.
</p>
<p>
Through this project-based learning endeavor, the following practical engineering principles were realized:
</p>
<ol>
    <li><strong>Concurrent Programming:</strong> Mastering thread synchronization using intrinsic monitor locks, bounded queues, and <code>wait()</code> / <code>notifyAll()</code> to achieve race-condition-free scheduling.</li>
    <li><strong>Clinical Priority Triage:</strong> Designing dual-tiered priority queue mechanisms ensuring life-critical emergency arrivals bypass non-critical outpatient bookings safely.</li>
    <li><strong>Defensive Design & Exception Handling:</strong> Preventing clinical scheduling conflicts through custom checked exceptions like <code>DuplicateBookingException</code>.</li>
    <li><strong>Responsive GUI Architecture:</strong> Building scalable Swing desktop interfaces by isolating computation threads from the Event Dispatch Thread (EDT).</li>
    <li><strong>Data Organization:</strong> Implementing efficient in-memory lookups ($O(1)$) using <code>LinkedHashMap</code>, <code>LinkedList</code>, and <code>ArrayList</code> structures.</li>
</ol>

<hr style="border: 0; border-top: 1px solid #CBD5E1; margin: 35px 0 10px 0;">
<p style="text-align: center; font-size: 9pt; color: #64748B;">
    &copy; 2026 Department of Data Engineering &bull; Subject: advance java programing &bull; Project-Based Learning Capstone Report
</p>

</body>
</html>
"""

with open("PROJECT_REPORT.html", "w", encoding="utf-8") as f:
    f.write(html_content)

print("PROJECT_REPORT.html successfully built with embedded base64 screenshots and source code!")
