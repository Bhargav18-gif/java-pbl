package hospital.ui.panels;

import hospital.exceptions.DuplicateBookingException;
import hospital.exceptions.InvalidInputException;
import hospital.model.Appointment;
import hospital.model.AppointmentStatus;
import hospital.model.Doctor;
import hospital.model.Patient;
import hospital.model.PatientType;
import hospital.model.QueueToken;
import hospital.service.HospitalManager;
import hospital.ui.Theme;
import hospital.ui.components.BadgeRenderer;
import hospital.ui.components.ModernButton;
import hospital.ui.components.ModernTable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Appointment Management Screen.
 * Supports booking, rescheduling, cancelling, and duplicate detection.
 */
public class AppointmentPanel extends JPanel {
    private final HospitalManager manager;

    // Booking form controls
    private JTextField txtApptId;
    private JComboBox<String> cbPatients;
    private JComboBox<String> cbDoctors;
    private JTextField txtDate;
    private JComboBox<String> cbTimeSlots;
    private JComboBox<PatientType> cbType;
    private JTextArea txtNotes;

    // Table & Search
    private JTextField txtSearch;
    private DefaultTableModel tableModel;
    private ModernTable apptTable;
    private JLabel lblTotalAppointments;

    private static final String[] TIME_SLOTS = {
            "09:00 AM", "09:30 AM", "10:00 AM", "10:30 AM",
            "11:00 AM", "11:30 AM", "01:30 PM", "02:00 PM",
            "02:30 PM", "03:00 PM", "03:30 PM", "04:00 PM"
    };

    public AppointmentPanel(HospitalManager manager) {
        this.manager = manager;

        setLayout(new BorderLayout(16, 16));
        setBackground(Theme.BG_MAIN);
        setBorder(new EmptyBorder(20, 24, 20, 24));

        initUI();
        refreshDropdowns();
        refreshTable();
    }

    private void initUI() {
        // Header
        JPanel topHeader = new JPanel(new BorderLayout());
        topHeader.setOpaque(false);

        JLabel title = new JLabel("Appointment Scheduling & Dispatch");
        title.setFont(Theme.FONT_HEADER);
        title.setForeground(Theme.NAVY_DARK);

        JLabel subtitle = new JLabel("Book, reschedule, cancel appointments, and dispatch patients into live queues.");
        subtitle.setFont(Theme.FONT_REGULAR);
        subtitle.setForeground(Theme.TEXT_SECONDARY);

        JPanel titleWrapper = new JPanel(new GridLayout(2, 1, 0, 4));
        titleWrapper.setOpaque(false);
        titleWrapper.add(title);
        titleWrapper.add(subtitle);
        topHeader.add(titleWrapper, BorderLayout.WEST);

        add(topHeader, BorderLayout.NORTH);

        // Main content
        JPanel mainContent = new JPanel(new BorderLayout(16, 0));
        mainContent.setOpaque(false);

        // LEFT: Booking Form Card
        JPanel formCard = Theme.createCard();
        formCard.setPreferredSize(new Dimension(390, 0));
        formCard.setLayout(new BorderLayout(0, 12));

        JLabel formTitle = new JLabel("New Appointment Booking");
        formTitle.setFont(Theme.FONT_SUBHEADER);
        formTitle.setForeground(Theme.NAVY_DARK);
        formCard.add(formTitle, BorderLayout.NORTH);

        JPanel formFields = new JPanel(new GridBagLayout());
        formFields.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.weightx = 1.0;

        txtApptId = Theme.createTextField(15);
        txtApptId.setEditable(false);
        txtApptId.setBackground(new Color(245, 247, 250));
        txtApptId.setText(manager.generateNextAppointmentId());

        cbPatients = new JComboBox<>();
        cbPatients.setFont(Theme.FONT_REGULAR);
        cbPatients.setBackground(Color.WHITE);
        cbPatients.setPreferredSize(new Dimension(cbPatients.getPreferredSize().width, 34));

        cbDoctors = new JComboBox<>();
        cbDoctors.setFont(Theme.FONT_REGULAR);
        cbDoctors.setBackground(Color.WHITE);
        cbDoctors.setPreferredSize(new Dimension(cbDoctors.getPreferredSize().width, 34));

        txtDate = Theme.createTextField(15);
        txtDate.setText(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));

        cbTimeSlots = Theme.createComboBox(TIME_SLOTS);
        cbType = new JComboBox<>(PatientType.values());
        cbType.setFont(Theme.FONT_REGULAR);
        cbType.setBackground(Color.WHITE);
        cbType.setPreferredSize(new Dimension(cbType.getPreferredSize().width, 34));

        txtNotes = new JTextArea(3, 15);
        txtNotes.setFont(Theme.FONT_REGULAR);
        txtNotes.setLineWrap(true);
        txtNotes.setWrapStyleWord(true);
        JScrollPane scrollNotes = new JScrollPane(txtNotes);
        scrollNotes.setBorder(Theme.createInputPaddingBorder());

        int row = 0;
        addField(formFields, gbc, "Appointment ID (Unique):", txtApptId, row++);
        addField(formFields, gbc, "Select Patient *:", cbPatients, row++);
        addField(formFields, gbc, "Select Doctor & Specialty *:", cbDoctors, row++);
        addField(formFields, gbc, "Date (YYYY-MM-DD) *:", txtDate, row++);
        addField(formFields, gbc, "Consultation Time Slot *:", cbTimeSlots, row++);
        addField(formFields, gbc, "Appointment Category:", cbType, row++);
        addField(formFields, gbc, "Clinical Notes / Reason:", scrollNotes, row++);

        JScrollPane formScroll = new JScrollPane(formFields);
        formScroll.setBorder(null);
        formScroll.setOpaque(false);
        formScroll.getViewport().setOpaque(false);
        formCard.add(formScroll, BorderLayout.CENTER);

        // Buttons
        JPanel formButtons = new JPanel(new GridLayout(2, 1, 8, 8));
        formButtons.setOpaque(false);

        ModernButton btnBook = ModernButton.createPrimary("Confirm & Book Appointment");
        btnBook.addActionListener(e -> handleBookAppointment());

        ModernButton btnClear = ModernButton.createSecondary("Reset Form Fields");
        btnClear.addActionListener(e -> clearForm());

        formButtons.add(btnBook);
        formButtons.add(btnClear);

        formCard.add(formButtons, BorderLayout.SOUTH);
        mainContent.add(formCard, BorderLayout.WEST);

        // RIGHT: Appointments Table Card
        JPanel tableCard = Theme.createCard();
        tableCard.setLayout(new BorderLayout(0, 12));

        // Search Bar
        JPanel searchBar = new JPanel(new BorderLayout(10, 0));
        searchBar.setOpaque(false);

        JLabel lblSearch = new JLabel("Filter Appointments:");
        lblSearch.setFont(Theme.FONT_TITLE_BOLD);
        lblSearch.setForeground(Theme.NAVY_DARK);

        txtSearch = Theme.createTextField(20);
        txtSearch.putClientProperty("JTextField.placeholderText", "Search by ID, Patient, Doctor, Date, or Status...");
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterAppointments(); }
            public void removeUpdate(DocumentEvent e) { filterAppointments(); }
            public void changedUpdate(DocumentEvent e) { filterAppointments(); }
        });

        ModernButton btnReset = ModernButton.createSecondary("Reset");
        btnReset.addActionListener(e -> {
            txtSearch.setText("");
            refreshTable();
        });

        searchBar.add(lblSearch, BorderLayout.WEST);
        searchBar.add(txtSearch, BorderLayout.CENTER);
        searchBar.add(btnReset, BorderLayout.EAST);

        tableCard.add(searchBar, BorderLayout.NORTH);

        // Table
        String[] cols = {"Appt ID", "Date", "Slot", "Patient Name", "Doctor", "Specialization", "Type", "Status"};
        tableModel = new DefaultTableModel(cols, 0);
        apptTable = new ModernTable(tableModel);
        apptTable.getColumnModel().getColumn(6).setCellRenderer(new BadgeRenderer());
        apptTable.getColumnModel().getColumn(7).setCellRenderer(new BadgeRenderer());

        tableCard.add(ModernTable.wrap(apptTable), BorderLayout.CENTER);

        // Bottom action toolbar
        JPanel tableFooter = new JPanel(new BorderLayout(12, 0));
        tableFooter.setOpaque(false);

        lblTotalAppointments = new JLabel("Total Bookings: 0");
        lblTotalAppointments.setFont(Theme.FONT_TITLE_BOLD);
        lblTotalAppointments.setForeground(Theme.TEXT_SECONDARY);

        JPanel actionGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actionGroup.setOpaque(false);

        ModernButton btnReschedule = ModernButton.createNavy("Reschedule Selected");
        btnReschedule.addActionListener(e -> handleReschedule());

        ModernButton btnCancel = ModernButton.createDanger("Cancel Selected");
        btnCancel.addActionListener(e -> handleCancel());

        ModernButton btnSendToQueue = ModernButton.createSuccess("+ Dispatch to Live Queue");
        btnSendToQueue.addActionListener(e -> handleSendToQueue());

        actionGroup.add(btnReschedule);
        actionGroup.add(btnCancel);
        actionGroup.add(btnSendToQueue);

        tableFooter.add(lblTotalAppointments, BorderLayout.WEST);
        tableFooter.add(actionGroup, BorderLayout.EAST);

        tableCard.add(tableFooter, BorderLayout.SOUTH);
        mainContent.add(tableCard, BorderLayout.CENTER);

        add(mainContent, BorderLayout.CENTER);
    }

    private void addField(JPanel p, GridBagConstraints gbc, String label, Component comp, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        p.add(Theme.createFieldLabel(label), gbc);

        gbc.gridy = ++row;
        p.add(comp, gbc);
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

    private void handleBookAppointment() {
        if (cbPatients.getSelectedIndex() == -1 || cbDoctors.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Please ensure patients and doctors are available and selected.",
                    "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String patSelected = (String) cbPatients.getSelectedItem();
            String patientId = patSelected.split(" - ")[0];
            Patient patient = manager.getPatientById(patientId);

            String docSelected = (String) cbDoctors.getSelectedItem();
            String docId = docSelected.split(" - ")[0];
            Doctor doctor = manager.getDoctorById(docId);

            LocalDate date;
            try {
                date = LocalDate.parse(txtDate.getText().trim(), DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException dtEx) {
                throw new InvalidInputException("Invalid Date format. Please use YYYY-MM-DD (e.g. 2026-11-15).");
            }

            String slot = (String) cbTimeSlots.getSelectedItem();
            PatientType type = (PatientType) cbType.getSelectedItem();
            String notes = txtNotes.getText().trim();

            Appointment appt = new Appointment(
                    txtApptId.getText().trim(),
                    patient.getId(),
                    patient.getName(),
                    doctor.getId(),
                    doctor.getName(),
                    doctor.getSpecialization(),
                    date,
                    slot,
                    type,
                    AppointmentStatus.SCHEDULED,
                    notes
            );

            manager.bookAppointment(appt);

            JOptionPane.showMessageDialog(this,
                    "Appointment booked successfully!\n" +
                    "Appointment ID: " + appt.getAppointmentId() + "\n" +
                    "Doctor: Dr. " + doctor.getName() + " (" + doctor.getSpecialization() + ")\n" +
                    "Date & Time: " + appt.getFormattedDate() + " at " + slot,
                    "Booking Confirmed", JOptionPane.INFORMATION_MESSAGE);

            clearForm();
            refreshTable();

        } catch (DuplicateBookingException dupEx) {
            JOptionPane.showMessageDialog(this,
                    "DUPLICATE BOOKING DETECTED!\n\n" + dupEx.getMessage(),
                    "Scheduling Conflict", JOptionPane.ERROR_MESSAGE);
        } catch (InvalidInputException invEx) {
            JOptionPane.showMessageDialog(this, invEx.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unexpected error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleReschedule() {
        int selected = apptTable.getSelectedRow();
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Please select an appointment to reschedule.", "Select Appointment", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String apptId = (String) tableModel.getValueAt(selected, 0);
        String patient = (String) tableModel.getValueAt(selected, 3);
        String doctor = (String) tableModel.getValueAt(selected, 4);

        JTextField newDateField = new JTextField(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        JComboBox<String> newSlotBox = new JComboBox<>(TIME_SLOTS);

        JPanel panel = new JPanel(new GridLayout(3, 2, 8, 8));
        panel.add(new JLabel("Patient:"));
        panel.add(new JLabel(patient));
        panel.add(new JLabel("New Date (YYYY-MM-DD):"));
        panel.add(newDateField);
        panel.add(new JLabel("New Time Slot:"));
        panel.add(newSlotBox);

        int res = JOptionPane.showConfirmDialog(this, panel, "Reschedule Appointment " + apptId,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (res == JOptionPane.OK_OPTION) {
            try {
                LocalDate newDate = LocalDate.parse(newDateField.getText().trim(), DateTimeFormatter.ISO_LOCAL_DATE);
                String newSlot = (String) newSlotBox.getSelectedItem();

                manager.rescheduleAppointment(apptId, newDate, newSlot);
                JOptionPane.showMessageDialog(this,
                        "Appointment " + apptId + " rescheduled successfully to " + newDate + " at " + newSlot,
                        "Rescheduled", JOptionPane.INFORMATION_MESSAGE);
                refreshTable();
            } catch (DuplicateBookingException ex) {
                JOptionPane.showMessageDialog(this, "Conflict: " + ex.getMessage(), "Conflict", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleCancel() {
        int selected = apptTable.getSelectedRow();
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Please select an appointment to cancel.", "Select Appointment", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String apptId = (String) tableModel.getValueAt(selected, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel appointment " + apptId + "?",
                "Confirm Cancellation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                manager.cancelAppointment(apptId);
                JOptionPane.showMessageDialog(this, "Appointment cancelled.", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleSendToQueue() {
        int selected = apptTable.getSelectedRow();
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Please select an appointment to send to the queue.", "Select Appointment", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String apptId = (String) tableModel.getValueAt(selected, 0);
        Appointment appt = null;
        for (Appointment a : manager.getAllAppointments()) {
            if (a.getAppointmentId().equalsIgnoreCase(apptId)) {
                appt = a;
                break;
            }
        }

        if (appt == null) return;

        if (appt.getStatus() == AppointmentStatus.CANCELLED) {
            JOptionPane.showMessageDialog(this, "Cannot dispatch a cancelled appointment.", "Invalid Status", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (appt.getStatus() == AppointmentStatus.IN_QUEUE) {
            JOptionPane.showMessageDialog(this, "This appointment is already dispatched into the active queue.", "Already in Queue", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            QueueToken token = manager.issueToken(
                    appt.getPatientId(),
                    appt.getDoctorId(),
                    appt.getType(),
                    appt.getNotes() != null && !appt.getNotes().isEmpty() ? appt.getNotes() : "Scheduled Consultation",
                    appt.getAppointmentId()
            );

            JOptionPane.showMessageDialog(this,
                    "Patient Dispatched to Live Queue!\n" +
                    "Assigned Token: " + token.getTokenNumber() + "\n" +
                    "Priority: " + token.getType() + "\n" +
                    "Status: Waiting for Dr. " + token.getDoctorName(),
                    "Token Generated", JOptionPane.INFORMATION_MESSAGE);

            refreshTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to dispatch: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        txtApptId.setText(manager.generateNextAppointmentId());
        txtDate.setText(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        cbTimeSlots.setSelectedIndex(0);
        cbType.setSelectedIndex(0);
        txtNotes.setText("");
        apptTable.clearSelection();
    }

    private void filterAppointments() {
        String q = txtSearch.getText().trim();
        List<Appointment> list = manager.searchAppointments(q);
        populateTable(list);
    }

    public void refreshTable() {
        List<Appointment> list = manager.getAllAppointments();
        populateTable(list);
    }

    private void populateTable(List<Appointment> list) {
        tableModel.setRowCount(0);
        for (Appointment a : list) {
            tableModel.addRow(new Object[]{
                    a.getAppointmentId(),
                    a.getFormattedDate(),
                    a.getTimeSlot(),
                    a.getPatientName(),
                    "Dr. " + a.getDoctorName(),
                    a.getDoctorSpecialization(),
                    a.getType().name(),
                    a.getStatus().name()
            });
        }
        lblTotalAppointments.setText("Total Bookings: " + list.size());
    }
}
