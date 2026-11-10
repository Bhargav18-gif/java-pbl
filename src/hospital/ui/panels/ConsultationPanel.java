package hospital.ui.panels;

import hospital.exceptions.InvalidInputException;
import hospital.exceptions.QueueEmptyException;
import hospital.model.ConsultationRecord;
import hospital.model.QueueToken;
import hospital.queue.HospitalQueueEngine;
import hospital.service.HospitalManager;
import hospital.ui.Theme;
import hospital.ui.components.BadgeRenderer;
import hospital.ui.components.ModernButton;
import hospital.ui.components.ModernTable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Consultation Management Screen.
 * Demonstrates Consumer processing in Producer-Consumer architecture.
 * Supports manual consultation entry and background simulated thread consumer.
 */
public class ConsultationPanel extends JPanel {
    private final HospitalManager manager;

    // Active Patient Card Elements
    private JLabel lblCurrentToken;
    private JLabel lblCurrentPatient;
    private JLabel lblCurrentDoctor;
    private JLabel lblCurrentVitals;
    private JLabel lblCurrentBadge;
    private JProgressBar progressBar;
    private ModernButton btnToggleSim;
    private ModernButton btnCallNext;

    // Consultation Form Inputs
    private JTextField txtVitals;
    private JTextField txtSymptoms;
    private JTextField txtDiagnosis;
    private JTextArea txtPrescription;
    private JTextArea txtDoctorNotes;
    private ModernButton btnComplete;

    // Consultation History Table
    private DefaultTableModel historyModel;
    private ModernTable historyTable;
    private JLabel lblHistoryCount;

    // Currently active token being examined
    private QueueToken activeToken;

    public ConsultationPanel(HospitalManager manager) {
        this.manager = manager;

        setLayout(new BorderLayout(16, 16));
        setBackground(Theme.BG_MAIN);
        setBorder(new EmptyBorder(20, 24, 20, 24));

        initUI();
        syncWithQueueState();
        refreshHistoryTable();
    }

    private void initUI() {
        // Header
        JPanel topHeader = new JPanel(new BorderLayout());
        topHeader.setOpaque(false);

        JLabel title = new JLabel("Doctor Consultation Desk & Processing");
        title.setFont(Theme.FONT_HEADER);
        title.setForeground(Theme.NAVY_DARK);

        JLabel subtitle = new JLabel("Examine queued patients, record clinical vitals, formulate diagnoses, and prescribe treatments.");
        subtitle.setFont(Theme.FONT_REGULAR);
        subtitle.setForeground(Theme.TEXT_SECONDARY);

        JPanel titleWrapper = new JPanel(new GridLayout(2, 1, 0, 4));
        titleWrapper.setOpaque(false);
        titleWrapper.add(title);
        titleWrapper.add(subtitle);
        topHeader.add(titleWrapper, BorderLayout.WEST);

        add(topHeader, BorderLayout.NORTH);

        // Center: Split Top (Active Patient + Actions) and Bottom (History Table)
        JPanel mainCenter = new JPanel(new BorderLayout(0, 14));
        mainCenter.setOpaque(false);

        // TOP SECTION: Currently Serving Patient + Consultation Form
        JPanel topSection = new JPanel(new BorderLayout(16, 0));
        topSection.setOpaque(false);

        // Current Patient Info Card (Left)
        JPanel patientCard = Theme.createCard();
        patientCard.setPreferredSize(new Dimension(380, 0));
        patientCard.setLayout(new BorderLayout(0, 12));

        JPanel cardHeader = new JPanel(new BorderLayout());
        cardHeader.setOpaque(false);
        JLabel cardTitle = new JLabel("Currently In Consultation");
        cardTitle.setFont(Theme.FONT_SUBHEADER);
        cardTitle.setForeground(Theme.NAVY_DARK);

        lblCurrentBadge = new JLabel("IDLE", SwingConstants.CENTER);
        lblCurrentBadge.setFont(Theme.FONT_SMALL_BOLD);
        lblCurrentBadge.setForeground(Theme.TEXT_MUTED);
        lblCurrentBadge.setOpaque(true);
        lblCurrentBadge.setBackground(new Color(241, 245, 249));
        lblCurrentBadge.setBorder(new EmptyBorder(4, 10, 4, 10));

        cardHeader.add(cardTitle, BorderLayout.WEST);
        cardHeader.add(lblCurrentBadge, BorderLayout.EAST);
        patientCard.add(cardHeader, BorderLayout.NORTH);

        // Patient details in card
        JPanel detailsPanel = new JPanel(new GridLayout(4, 1, 0, 6));
        detailsPanel.setOpaque(false);

        lblCurrentToken = new JLabel("Token: None (Desk Ready)");
        lblCurrentToken.setFont(Theme.FONT_TITLE_BOLD);
        lblCurrentToken.setForeground(Theme.BLUE_PRIMARY);

        lblCurrentPatient = new JLabel("Patient: Waiting for next call");
        lblCurrentPatient.setFont(Theme.FONT_REGULAR_BOLD);
        lblCurrentPatient.setForeground(Theme.TEXT_PRIMARY);

        lblCurrentDoctor = new JLabel("Attending Doctor: -");
        lblCurrentDoctor.setFont(Theme.FONT_REGULAR);
        lblCurrentDoctor.setForeground(Theme.TEXT_SECONDARY);

        lblCurrentVitals = new JLabel("Chief Complaint: -");
        lblCurrentVitals.setFont(Theme.FONT_REGULAR);
        lblCurrentVitals.setForeground(Theme.TEXT_SECONDARY);

        detailsPanel.add(lblCurrentToken);
        detailsPanel.add(lblCurrentPatient);
        detailsPanel.add(lblCurrentDoctor);
        detailsPanel.add(lblCurrentVitals);
        patientCard.add(detailsPanel, BorderLayout.CENTER);

        // Bottom of Patient card: Action buttons + Thread Sim Toggle
        JPanel patientCardFooter = new JPanel(new BorderLayout(0, 8));
        patientCardFooter.setOpaque(false);

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setString("Consultation Desk Ready");
        progressBar.setFont(Theme.FONT_SMALL_BOLD);
        progressBar.setForeground(Theme.BLUE_PRIMARY);
        progressBar.setPreferredSize(new Dimension(progressBar.getPreferredSize().width, 22));

        JPanel buttonsRow = new JPanel(new GridLayout(2, 1, 0, 8));
        buttonsRow.setOpaque(false);

        btnCallNext = ModernButton.createPrimary("Call Next Queue Patient (Consume)");
        btnCallNext.addActionListener(e -> handleCallNext());

        btnToggleSim = ModernButton.createNavy("Start Auto Background Consumer (Thread)");
        btnToggleSim.addActionListener(e -> handleToggleSimulation());

        buttonsRow.add(btnCallNext);
        buttonsRow.add(btnToggleSim);

        patientCardFooter.add(progressBar, BorderLayout.NORTH);
        patientCardFooter.add(buttonsRow, BorderLayout.SOUTH);
        patientCard.add(patientCardFooter, BorderLayout.SOUTH);

        topSection.add(patientCard, BorderLayout.WEST);

        // Consultation Clinical Form (Right)
        JPanel formCard = Theme.createCard();
        formCard.setLayout(new BorderLayout(0, 10));

        JLabel formHeader = new JLabel("Clinical Examination & Diagnosis Form");
        formHeader.setFont(Theme.FONT_SUBHEADER);
        formHeader.setForeground(Theme.NAVY_DARK);
        formCard.add(formHeader, BorderLayout.NORTH);

        JPanel formFields = new JPanel(new GridBagLayout());
        formFields.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.weightx = 1.0;

        txtVitals = Theme.createTextField(15);
        txtVitals.setText("BP: 120/80 mmHg | Pulse: 72 bpm | SpO2: 99% | Temp: 98.6°F");

        txtSymptoms = Theme.createTextField(15);
        txtDiagnosis = Theme.createTextField(15);

        txtPrescription = new JTextArea(2, 15);
        txtPrescription.setFont(Theme.FONT_REGULAR);
        txtPrescription.setLineWrap(true);
        txtPrescription.setWrapStyleWord(true);
        JScrollPane scrollPresc = new JScrollPane(txtPrescription);
        scrollPresc.setBorder(Theme.createInputPaddingBorder());

        txtDoctorNotes = new JTextArea(2, 15);
        txtDoctorNotes.setFont(Theme.FONT_REGULAR);
        txtDoctorNotes.setLineWrap(true);
        txtDoctorNotes.setWrapStyleWord(true);
        JScrollPane scrollNotes = new JScrollPane(txtDoctorNotes);
        scrollNotes.setBorder(Theme.createInputPaddingBorder());

        int row = 0;
        addField(formFields, gbc, "Vital Signs (BP, Heart Rate, SpO2, Temp):", txtVitals, row++);
        addField(formFields, gbc, "Observed Symptoms & Findings:", txtSymptoms, row++);
        addField(formFields, gbc, "Clinical Diagnosis *:", txtDiagnosis, row++);
        addField(formFields, gbc, "Prescription & Dosage Schedule *:", scrollPresc, row++);
        addField(formFields, gbc, "Doctor's Advice & Follow-up Instructions:", scrollNotes, row++);

        JScrollPane scrollForm = new JScrollPane(formFields);
        scrollForm.setBorder(null);
        scrollForm.setOpaque(false);
        scrollForm.getViewport().setOpaque(false);
        formCard.add(scrollForm, BorderLayout.CENTER);

        // Complete Button
        btnComplete = ModernButton.createSuccess("Complete Consultation & Save Clinical Record");
        btnComplete.setFont(Theme.FONT_TITLE_BOLD);
        btnComplete.setPreferredSize(new Dimension(btnComplete.getPreferredSize().width, 38));
        btnComplete.addActionListener(e -> handleCompleteConsultation());
        formCard.add(btnComplete, BorderLayout.SOUTH);

        topSection.add(formCard, BorderLayout.CENTER);
        mainCenter.add(topSection, BorderLayout.NORTH);

        // BOTTOM SECTION: Completed Consultation History Table
        JPanel historyCard = Theme.createCard();
        historyCard.setLayout(new BorderLayout(0, 10));

        JPanel historyHeader = new JPanel(new BorderLayout());
        historyHeader.setOpaque(false);

        JLabel histTitle = new JLabel("Completed Consultation Records History");
        histTitle.setFont(Theme.FONT_SUBHEADER);
        histTitle.setForeground(Theme.NAVY_DARK);

        lblHistoryCount = new JLabel("Total Completed: 0");
        lblHistoryCount.setFont(Theme.FONT_TITLE_BOLD);
        lblHistoryCount.setForeground(Theme.TEXT_SECONDARY);

        historyHeader.add(histTitle, BorderLayout.WEST);
        historyHeader.add(lblHistoryCount, BorderLayout.EAST);
        historyCard.add(historyHeader, BorderLayout.NORTH);

        String[] cols = {"Record ID", "Token No", "Date & Time", "Patient Name", "Doctor", "Diagnosis", "Status"};
        historyModel = new DefaultTableModel(cols, 0);
        historyTable = new ModernTable(historyModel);
        historyTable.getColumnModel().getColumn(6).setCellRenderer(new BadgeRenderer());

        historyCard.add(ModernTable.wrap(historyTable), BorderLayout.CENTER);

        // View record details button
        JPanel historyFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        historyFooter.setOpaque(false);

        ModernButton btnViewRecord = ModernButton.createNavy("View Full Medical Record");
        btnViewRecord.addActionListener(e -> handleViewRecord());
        historyFooter.add(btnViewRecord);

        historyCard.add(historyFooter, BorderLayout.SOUTH);
        mainCenter.add(historyCard, BorderLayout.CENTER);

        add(mainCenter, BorderLayout.CENTER);
    }

    private void addField(JPanel p, GridBagConstraints gbc, String label, Component comp, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        p.add(Theme.createFieldLabel(label), gbc);

        gbc.gridy = ++row;
        p.add(comp, gbc);
    }

    private void handleCallNext() {
        try {
            QueueToken token = manager.callNextPatient();
            setActivePatient(token);
        } catch (QueueEmptyException ex) {
            JOptionPane.showMessageDialog(this,
                    "No patients waiting in queue.\nAdd patients via Live Queue or Appointment Dispatch.",
                    "Queue Empty", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error calling patient: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setActivePatient(QueueToken token) {
        this.activeToken = token;
        if (token != null) {
            lblCurrentToken.setText("Token: " + token.getTokenNumber() + " (" + token.getType() + ")");
            lblCurrentPatient.setText("Patient: " + token.getPatientName() + " (" + token.getPatientAge() + " / " + token.getPatientGender() + ")");
            lblCurrentDoctor.setText("Attending: Dr. " + token.getDoctorName());
            lblCurrentVitals.setText("Complaint: " + token.getIssueDescription());

            if (token.isEmergency()) {
                lblCurrentBadge.setText("EMERGENCY");
                lblCurrentBadge.setForeground(Theme.EMERGENCY_RED);
                lblCurrentBadge.setBackground(Theme.EMERGENCY_BG);
            } else {
                lblCurrentBadge.setText("IN CONSULTATION");
                lblCurrentBadge.setForeground(Theme.BLUE_PRIMARY);
                lblCurrentBadge.setBackground(Theme.BLUE_LIGHT_BG);
            }

            txtSymptoms.setText(token.getIssueDescription());
            txtDiagnosis.setText("");
            txtPrescription.setText("");
            txtDoctorNotes.setText("");
            progressBar.setValue(50);
            progressBar.setString("Consultation In Progress...");
        } else {
            lblCurrentToken.setText("Token: None (Desk Ready)");
            lblCurrentPatient.setText("Patient: Waiting for next call");
            lblCurrentDoctor.setText("Attending Doctor: -");
            lblCurrentVitals.setText("Chief Complaint: -");
            lblCurrentBadge.setText("IDLE");
            lblCurrentBadge.setForeground(Theme.TEXT_MUTED);
            lblCurrentBadge.setBackground(new Color(241, 245, 249));
            progressBar.setValue(0);
            progressBar.setString("Consultation Desk Ready");
        }
    }

    private void handleCompleteConsultation() {
        if (activeToken == null) {
            JOptionPane.showMessageDialog(this, "No patient is currently in consultation.\nPlease call a patient first.",
                    "No Active Consultation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String diagnosis = txtDiagnosis.getText().trim();
        String prescription = txtPrescription.getText().trim();

        if (diagnosis.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Clinical Diagnosis.", "Diagnosis Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (prescription.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Prescription / Medication instructions.", "Prescription Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            ConsultationRecord record = new ConsultationRecord(
                    manager.generateNextRecordId(),
                    activeToken.getTokenNumber(),
                    activeToken.getPatientId(),
                    activeToken.getPatientName(),
                    activeToken.getDoctorId(),
                    activeToken.getDoctorName(),
                    txtSymptoms.getText().trim(),
                    diagnosis,
                    prescription,
                    txtVitals.getText().trim(),
                    txtDoctorNotes.getText().trim()
            );

            manager.completeConsultation(record);

            JOptionPane.showMessageDialog(this,
                    "Consultation completed successfully!\n" +
                    "Record ID: " + record.getRecordId() + "\n" +
                    "Patient: " + record.getPatientName() + "\n" +
                    "Diagnosis: " + record.getDiagnosis(),
                    "Consultation Saved", JOptionPane.INFORMATION_MESSAGE);

            setActivePatient(null);
            refreshHistoryTable();

        } catch (InvalidInputException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to complete: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleToggleSimulation() {
        HospitalQueueEngine engine = manager.getQueueEngine();
        if (engine.isSimulationRunning()) {
            engine.stopSimulation();
            btnToggleSim.setText("Start Auto Background Consumer (Thread)");
            btnToggleSim.setBackground(Theme.NAVY_MEDIUM);
            progressBar.setValue(0);
            progressBar.setString("Simulation Stopped");
        } else {
            btnToggleSim.setText("Stop Background Consumer (Running...)");
            btnToggleSim.setBackground(Theme.EMERGENCY_RED);
            progressBar.setString("Auto-Processing Enabled");

            engine.startSimulation(new HospitalQueueEngine.ConsultationProcessorCallback() {
                @Override
                public void onConsultationStarted(QueueToken token) {
                    SwingUtilities.invokeLater(() -> {
                        setActivePatient(token);
                        progressBar.setValue(30);
                        progressBar.setString("Auto Doctor Examining: " + token.getPatientName());
                    });
                }

                @Override
                public void onConsultationFinished(QueueToken token) {
                    SwingUtilities.invokeLater(() -> {
                        // Automatically generate completed clinical record
                        try {
                            ConsultationRecord cr = new ConsultationRecord(
                                    manager.generateNextRecordId(),
                                    token.getTokenNumber(),
                                    token.getPatientId(),
                                    token.getPatientName(),
                                    token.getDoctorId(),
                                    token.getDoctorName(),
                                    token.getIssueDescription(),
                                    token.isEmergency() ? "Acute Medical Episode (Stabilized)" : "General Outpatient Condition (Reviewed)",
                                    "Prescribed Standard Outpatient Therapy & Medications.",
                                    "BP: 122/82 mmHg | Pulse: 75 bpm | SpO2: 98%",
                                    "Processed automatically via Multi-threaded Background Queue Consumer."
                            );
                            manager.completeConsultation(cr);
                            setActivePatient(null);
                            progressBar.setValue(100);
                            progressBar.setString("Consultation Finished: " + token.getTokenNumber());
                            refreshHistoryTable();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            });
        }
    }

    private void handleViewRecord() {
        int selected = historyTable.getSelectedRow();
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Please select a consultation record to view.", "Select Record", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String recordId = (String) historyModel.getValueAt(selected, 0);
        ConsultationRecord rec = null;
        for (ConsultationRecord r : manager.getConsultationHistory()) {
            if (r.getRecordId().equalsIgnoreCase(recordId)) {
                rec = r;
                break;
            }
        }

        if (rec == null) return;

        JTextArea summary = new JTextArea();
        summary.setFont(Theme.FONT_REGULAR);
        summary.setEditable(false);
        summary.setText(
                "==========================================================\n" +
                "       HOSPITAL CLINICAL CONSULTATION REPORT\n" +
                "==========================================================\n\n" +
                "Record ID:      " + rec.getRecordId() + "\n" +
                "Token Number:   " + rec.getTokenNumber() + "\n" +
                "Date & Time:    " + rec.getTimestamp() + "\n\n" +
                "Patient ID:     " + rec.getPatientId() + "\n" +
                "Patient Name:   " + rec.getPatientName() + "\n\n" +
                "Doctor:         Dr. " + rec.getDoctorName() + " (" + rec.getDoctorId() + ")\n\n" +
                "Vital Signs:    " + rec.getVitalSigns() + "\n" +
                "Symptoms:       " + rec.getSymptoms() + "\n\n" +
                "Diagnosis:      " + rec.getDiagnosis() + "\n\n" +
                "Prescription:   \n" + rec.getPrescription() + "\n\n" +
                "Doctor Notes:   \n" + rec.getDoctorNotes() + "\n\n" +
                "Status:         " + rec.getStatus() + "\n" +
                "==========================================================\n"
        );
        summary.setCaretPosition(0);

        JScrollPane sp = new JScrollPane(summary);
        sp.setPreferredSize(new Dimension(520, 420));

        JOptionPane.showMessageDialog(this, sp, "Clinical Record: " + rec.getRecordId(), JOptionPane.PLAIN_MESSAGE);
    }

    public void syncWithQueueState() {
        QueueToken serving = manager.getQueueEngine().getCurrentlyServing();
        if (serving != null) {
            setActivePatient(serving);
        }
    }

    public void refreshHistoryTable() {
        historyModel.setRowCount(0);
        List<ConsultationRecord> history = manager.getConsultationHistory();
        for (ConsultationRecord r : history) {
            historyModel.addRow(new Object[]{
                    r.getRecordId(),
                    r.getTokenNumber(),
                    r.getTimestamp(),
                    r.getPatientName(),
                    "Dr. " + r.getDoctorName(),
                    r.getDiagnosis(),
                    r.getStatus()
            });
        }
        lblHistoryCount.setText("Total Completed: " + history.size());
    }
}
