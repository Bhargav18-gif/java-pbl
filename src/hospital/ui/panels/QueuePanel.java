package hospital.ui.panels;

import hospital.exceptions.QueueEmptyException;
import hospital.model.Doctor;
import hospital.model.Patient;
import hospital.model.PatientType;
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
import java.util.function.Consumer;

/**
 * Queue Management Screen.
 * Maintains separate Normal and Emergency queues with strict Emergency Priority.
 * Demonstrates thread-safe Producer-Consumer queue operations.
 */
public class QueuePanel extends JPanel {
    private final HospitalManager manager;
    private final Consumer<String> navigationHandler;

    // Tables
    private DefaultTableModel emergencyModel;
    private ModernTable emergencyTable;
    private DefaultTableModel regularModel;
    private ModernTable regularTable;

    // Header & Next In Line Display
    private JLabel lblNextInLine;
    private JLabel lblNextDetails;
    private JPanel nextInLineBanner;

    // Direct Walk-In Token Generator Form
    private JComboBox<String> cbPatients;
    private JComboBox<String> cbDoctors;
    private JComboBox<PatientType> cbPriority;
    private JTextField txtReason;

    public QueuePanel(HospitalManager manager, Consumer<String> navigationHandler) {
        this.manager = manager;
        this.navigationHandler = navigationHandler;

        setLayout(new BorderLayout(16, 16));
        setBackground(Theme.BG_MAIN);
        setBorder(new EmptyBorder(20, 24, 20, 24));

        initUI();
        refreshDropdowns();
        refreshQueues();
    }

    private void initUI() {
        // Top Header + Next In Line Banner
        JPanel topContainer = new JPanel(new BorderLayout(0, 12));
        topContainer.setOpaque(false);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel title = new JLabel("Live Patient Queue Management");
        title.setFont(Theme.FONT_HEADER);
        title.setForeground(Theme.NAVY_DARK);

        JLabel subtitle = new JLabel("Separate Emergency and Regular queues. Emergency patients receive automatic priority dispatch.");
        subtitle.setFont(Theme.FONT_REGULAR);
        subtitle.setForeground(Theme.TEXT_SECONDARY);

        JPanel titleWrapper = new JPanel(new GridLayout(2, 1, 0, 4));
        titleWrapper.setOpaque(false);
        titleWrapper.add(title);
        titleWrapper.add(subtitle);
        headerPanel.add(titleWrapper, BorderLayout.WEST);

        // Next In Line Display Banner
        nextInLineBanner = new JPanel(new BorderLayout(16, 4));
        nextInLineBanner.setBackground(Theme.BLUE_LIGHT_BG);
        nextInLineBanner.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER_COLOR, 1, true),
                new EmptyBorder(12, 16, 12, 16)
        ));

        JPanel nextTextPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        nextTextPanel.setOpaque(false);

        lblNextInLine = new JLabel("NEXT IN LINE: None");
        lblNextInLine.setFont(Theme.FONT_SUBHEADER);
        lblNextInLine.setForeground(Theme.NAVY_DARK);

        lblNextDetails = new JLabel("Queue is currently clear.");
        lblNextDetails.setFont(Theme.FONT_REGULAR);
        lblNextDetails.setForeground(Theme.TEXT_SECONDARY);

        nextTextPanel.add(lblNextInLine);
        nextTextPanel.add(lblNextDetails);

        JPanel bannerActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        bannerActions.setOpaque(false);

        ModernButton btnCallNow = ModernButton.createSuccess("Call Next Patient");
        btnCallNow.setFont(Theme.FONT_TITLE_BOLD);
        btnCallNow.addActionListener(e -> handleCallNextPatient());

        bannerActions.add(btnCallNow);

        nextInLineBanner.add(nextTextPanel, BorderLayout.CENTER);
        nextInLineBanner.add(bannerActions, BorderLayout.EAST);

        topContainer.add(headerPanel, BorderLayout.NORTH);
        topContainer.add(nextInLineBanner, BorderLayout.SOUTH);
        add(topContainer, BorderLayout.NORTH);

        // Center: Dual Queue Display (Emergency on Left, Regular on Right)
        JPanel dualQueuePanel = new JPanel(new GridLayout(1, 2, 16, 0));
        dualQueuePanel.setOpaque(false);

        // --- Emergency Queue Card ---
        JPanel emergencyCard = Theme.createCard();
        emergencyCard.setLayout(new BorderLayout(0, 10));

        JPanel emgHeader = new JPanel(new BorderLayout());
        emgHeader.setOpaque(false);
        JLabel emgTitle = new JLabel("EMERGENCY QUEUE (Priority 1)");
        emgTitle.setFont(Theme.FONT_SUBHEADER);
        emgTitle.setForeground(Theme.EMERGENCY_RED);

        JLabel emgBadge = new JLabel("HIGH PRIORITY", SwingConstants.CENTER);
        emgBadge.setFont(Theme.FONT_SMALL_BOLD);
        emgBadge.setForeground(Theme.EMERGENCY_RED);
        emgBadge.setOpaque(true);
        emgBadge.setBackground(Theme.EMERGENCY_BG);
        emgBadge.setBorder(new EmptyBorder(4, 8, 4, 8));

        emgHeader.add(emgTitle, BorderLayout.WEST);
        emgHeader.add(emgBadge, BorderLayout.EAST);
        emergencyCard.add(emgHeader, BorderLayout.NORTH);

        String[] emgCols = {"Token No", "Patient Name", "Age/Sex", "Doctor", "Issue", "Time"};
        emergencyModel = new DefaultTableModel(emgCols, 0);
        emergencyTable = new ModernTable(emergencyModel);
        emergencyCard.add(ModernTable.wrap(emergencyTable), BorderLayout.CENTER);

        dualQueuePanel.add(emergencyCard);

        // --- Regular Queue Card ---
        JPanel regularCard = Theme.createCard();
        regularCard.setLayout(new BorderLayout(0, 10));

        JPanel regHeader = new JPanel(new BorderLayout());
        regHeader.setOpaque(false);
        JLabel regTitle = new JLabel("REGULAR QUEUE (Priority 2)");
        regTitle.setFont(Theme.FONT_SUBHEADER);
        regTitle.setForeground(Theme.NAVY_DARK);

        JLabel regBadge = new JLabel("STANDARD", SwingConstants.CENTER);
        regBadge.setFont(Theme.FONT_SMALL_BOLD);
        regBadge.setForeground(Theme.INFO_BLUE);
        regBadge.setOpaque(true);
        regBadge.setBackground(Theme.INFO_BG);
        regBadge.setBorder(new EmptyBorder(4, 8, 4, 8));

        regHeader.add(regTitle, BorderLayout.WEST);
        regHeader.add(regBadge, BorderLayout.EAST);
        regularCard.add(regHeader, BorderLayout.NORTH);

        String[] regCols = {"Token No", "Patient Name", "Age/Sex", "Doctor", "Issue", "Time"};
        regularModel = new DefaultTableModel(regCols, 0);
        regularTable = new ModernTable(regularModel);
        regularCard.add(ModernTable.wrap(regularTable), BorderLayout.CENTER);

        dualQueuePanel.add(regularCard);

        // Bottom Section: Token Generator Form & Queue Ops
        JPanel bottomPanel = new JPanel(new BorderLayout(16, 0));
        bottomPanel.setOpaque(false);
        bottomPanel.setPreferredSize(new Dimension(0, 180));

        // Form Card (Token Producer)
        JPanel producerCard = Theme.createCard();
        producerCard.setLayout(new BorderLayout(0, 8));

        JLabel producerTitle = new JLabel("Issue New Queue Token (Producer)");
        producerTitle.setFont(Theme.FONT_TITLE_BOLD);
        producerTitle.setForeground(Theme.NAVY_DARK);
        producerCard.add(producerTitle, BorderLayout.NORTH);

        JPanel prodForm = new JPanel(new GridLayout(2, 4, 10, 8));
        prodForm.setOpaque(false);

        cbPatients = new JComboBox<>();
        cbPatients.setFont(Theme.FONT_REGULAR);
        cbPatients.setBackground(Color.WHITE);

        cbDoctors = new JComboBox<>();
        cbDoctors.setFont(Theme.FONT_REGULAR);
        cbDoctors.setBackground(Color.WHITE);

        cbPriority = new JComboBox<>(PatientType.values());
        cbPriority.setFont(Theme.FONT_REGULAR);
        cbPriority.setBackground(Color.WHITE);

        txtReason = Theme.createTextField(15);
        txtReason.setText("Walk-in Consultation");

        prodForm.add(Theme.createFieldLabel("Select Patient:"));
        prodForm.add(Theme.createFieldLabel("Assign Doctor:"));
        prodForm.add(Theme.createFieldLabel("Queue Priority:"));
        prodForm.add(Theme.createFieldLabel("Chief Complaint:"));

        prodForm.add(cbPatients);
        prodForm.add(cbDoctors);
        prodForm.add(cbPriority);
        prodForm.add(txtReason);

        producerCard.add(prodForm, BorderLayout.CENTER);

        JPanel prodActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        prodActions.setOpaque(false);

        ModernButton btnEnqueue = ModernButton.createPrimary("+ Enqueue Token (Produce)");
        btnEnqueue.addActionListener(e -> handleEnqueueToken());

        prodActions.add(btnEnqueue);
        producerCard.add(prodActions, BorderLayout.SOUTH);

        bottomPanel.add(producerCard, BorderLayout.CENTER);

        // Queue Controls Card
        JPanel queueOpsCard = Theme.createCard();
        queueOpsCard.setPreferredSize(new Dimension(250, 0));
        queueOpsCard.setLayout(new BorderLayout(0, 8));

        JLabel opsTitle = new JLabel("Queue Operations");
        opsTitle.setFont(Theme.FONT_TITLE_BOLD);
        opsTitle.setForeground(Theme.NAVY_DARK);
        queueOpsCard.add(opsTitle, BorderLayout.NORTH);

        JPanel opsButtons = new JPanel(new GridLayout(2, 1, 0, 8));
        opsButtons.setOpaque(false);

        ModernButton btnRemoveSelected = ModernButton.createSecondary("Remove Selected Token");
        btnRemoveSelected.addActionListener(e -> handleRemoveSelected());

        ModernButton btnClearAll = ModernButton.createDanger("Clear All Queues");
        btnClearAll.addActionListener(e -> handleClearAll());

        opsButtons.add(btnRemoveSelected);
        opsButtons.add(btnClearAll);
        queueOpsCard.add(opsButtons, BorderLayout.CENTER);

        bottomPanel.add(queueOpsCard, BorderLayout.EAST);

        // Assemble center and bottom
        JPanel centerContainer = new JPanel(new BorderLayout(0, 14));
        centerContainer.setOpaque(false);
        centerContainer.add(dualQueuePanel, BorderLayout.CENTER);
        centerContainer.add(bottomPanel, BorderLayout.SOUTH);

        add(centerContainer, BorderLayout.CENTER);
    }

    public void refreshDropdowns() {
        cbPatients.removeAllItems();
        for (Patient p : manager.getAllPatients()) {
            cbPatients.addItem(p.getId() + " - " + p.getName());
        }

        cbDoctors.removeAllItems();
        for (Doctor d : manager.getAllDoctors()) {
            cbDoctors.addItem(d.getId() + " - Dr. " + d.getName() + " (" + d.getSpecialization() + ")");
        }
    }

    private void handleEnqueueToken() {
        if (cbPatients.getSelectedIndex() == -1 || cbDoctors.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Please select both a patient and a doctor.", "Missing Data", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String pat = (String) cbPatients.getSelectedItem();
            String patientId = pat.split(" - ")[0];

            String doc = (String) cbDoctors.getSelectedItem();
            String docId = doc.split(" - ")[0];

            PatientType type = (PatientType) cbPriority.getSelectedItem();
            String issue = txtReason.getText().trim();

            QueueToken token = manager.issueToken(patientId, docId, type, issue, null);

            JOptionPane.showMessageDialog(this,
                    "Token Generated & Added to Queue!\n" +
                    "Token: " + token.getTokenNumber() + "\n" +
                    "Category: " + token.getType() + "\n" +
                    "Patient: " + token.getPatientName() + "\n" +
                    "Doctor: Dr. " + token.getDoctorName(),
                    "Token Enqueued", JOptionPane.INFORMATION_MESSAGE);

            refreshQueues();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error issuing token: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleCallNextPatient() {
        try {
            QueueToken token = manager.callNextPatient();
            if (token != null) {
                int res = JOptionPane.showConfirmDialog(this,
                        "Calling Patient: " + token.getPatientName() + "\n" +
                        "Token: " + token.getTokenNumber() + " [" + token.getType() + "]\n" +
                        "Assigned Doctor: Dr. " + token.getDoctorName() + "\n\n" +
                        "Open Consultation Desk for this patient now?",
                        "Patient Called", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (res == JOptionPane.YES_OPTION) {
                    navigationHandler.accept("CONSULTATION");
                }
            }
        } catch (QueueEmptyException ex) {
            JOptionPane.showMessageDialog(this, "The queue is currently empty. No patients waiting.",
                    "Queue Empty", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        refreshQueues();
    }

    private void handleRemoveSelected() {
        int emgRow = emergencyTable.getSelectedRow();
        int regRow = regularTable.getSelectedRow();

        if (emgRow == -1 && regRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a token from either the Emergency or Regular table to remove.",
                    "Select Token", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String tokenNum;
        if (emgRow != -1) {
            tokenNum = (String) emergencyModel.getValueAt(emgRow, 0);
        } else {
            tokenNum = (String) regularModel.getValueAt(regRow, 0);
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Remove token " + tokenNum + " from queue?",
                "Confirm Removal", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            manager.getQueueEngine().removeToken(tokenNum);
            refreshQueues();
        }
    }

    private void handleClearAll() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to clear all waiting tokens from both queues?",
                "Confirm Clear Queues", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            manager.getQueueEngine().clearAll();
            refreshQueues();
        }
    }

    public void refreshQueues() {
        HospitalQueueEngine engine = manager.getQueueEngine();

        // Emergency Queue Table
        emergencyModel.setRowCount(0);
        List<QueueToken> emgList = engine.getEmergencyQueueSnapshot();
        for (QueueToken t : emgList) {
            emergencyModel.addRow(new Object[]{
                    t.getTokenNumber(),
                    t.getPatientName(),
                    t.getPatientAge() + " / " + t.getPatientGender(),
                    "Dr. " + t.getDoctorName(),
                    t.getIssueDescription(),
                    t.getCreatedAtFormatted()
            });
        }

        // Regular Queue Table
        regularModel.setRowCount(0);
        List<QueueToken> regList = engine.getRegularQueueSnapshot();
        for (QueueToken t : regList) {
            regularModel.addRow(new Object[]{
                    t.getTokenNumber(),
                    t.getPatientName(),
                    t.getPatientAge() + " / " + t.getPatientGender(),
                    "Dr. " + t.getDoctorName(),
                    t.getIssueDescription(),
                    t.getCreatedAtFormatted()
            });
        }

        // Next In Line Display
        QueueToken next = engine.peekNext();
        if (next != null) {
            if (next.isEmergency()) {
                lblNextInLine.setText("NEXT IN LINE: [" + next.getTokenNumber() + "] " + next.getPatientName() + " (EMERGENCY PRIORITY)");
                lblNextInLine.setForeground(Theme.EMERGENCY_RED);
                nextInLineBanner.setBackground(Theme.EMERGENCY_BG);
            } else {
                lblNextInLine.setText("NEXT IN LINE: [" + next.getTokenNumber() + "] " + next.getPatientName() + " (REGULAR)");
                lblNextInLine.setForeground(Theme.NAVY_DARK);
                nextInLineBanner.setBackground(Theme.BLUE_LIGHT_BG);
            }
            lblNextDetails.setText("Doctor: Dr. " + next.getDoctorName() + "  |  Issue: " + next.getIssueDescription() + "  |  Time: " + next.getCreatedAtFormatted());
        } else {
            lblNextInLine.setText("NEXT IN LINE: None (Queue is currently empty)");
            lblNextInLine.setForeground(Theme.TEXT_SECONDARY);
            lblNextDetails.setText("Use the form below or Appointment Dispatch to add patients to the queue.");
            nextInLineBanner.setBackground(Theme.BLUE_PALE);
        }
    }
}
