package hospital.ui.panels;

import hospital.exceptions.InvalidInputException;
import hospital.model.Patient;
import hospital.model.PatientType;
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
import java.util.List;

/**
 * Patient Management Screen.
 * Allows hospital staff to register, update, search, delete, and view patient profiles.
 */
public class PatientPanel extends JPanel {
    private final HospitalManager manager;

    // Form fields
    private JTextField txtId;
    private JTextField txtName;
    private JSpinner spinAge;
    private JComboBox<String> cbGender;
    private JTextField txtPhone;
    private JTextField txtEmail;
    private JComboBox<String> cbBloodGroup;
    private JTextField txtEmergencyContact;
    private JTextField txtAddress;
    private JTextArea txtMedicalHistory;

    // Table & Search
    private JTextField txtSearch;
    private DefaultTableModel tableModel;
    private ModernTable patientTable;
    private JLabel lblTotalCount;

    public PatientPanel(HospitalManager manager) {
        this.manager = manager;

        setLayout(new BorderLayout(16, 16));
        setBackground(Theme.BG_MAIN);
        setBorder(new EmptyBorder(20, 24, 20, 24));

        initUI();
        refreshTable();
    }

    private void initUI() {
        // Top Header
        JPanel topHeader = new JPanel(new BorderLayout());
        topHeader.setOpaque(false);

        JLabel title = new JLabel("Patient Management");
        title.setFont(Theme.FONT_HEADER);
        title.setForeground(Theme.NAVY_DARK);

        JLabel subtitle = new JLabel("Register new patients, edit existing records, and search clinical history.");
        subtitle.setFont(Theme.FONT_REGULAR);
        subtitle.setForeground(Theme.TEXT_SECONDARY);

        JPanel titleWrapper = new JPanel(new GridLayout(2, 1, 0, 4));
        titleWrapper.setOpaque(false);
        titleWrapper.add(title);
        titleWrapper.add(subtitle);
        topHeader.add(titleWrapper, BorderLayout.WEST);

        add(topHeader, BorderLayout.NORTH);

        // Center split: Form on Left, Table on Right
        JPanel mainContent = new JPanel(new BorderLayout(16, 0));
        mainContent.setOpaque(false);

        // LEFT: Form Card
        JPanel formCard = Theme.createCard();
        formCard.setPreferredSize(new Dimension(380, 0));
        formCard.setLayout(new BorderLayout(0, 12));

        JLabel formTitle = new JLabel("Patient Details Form");
        formTitle.setFont(Theme.FONT_SUBHEADER);
        formTitle.setForeground(Theme.NAVY_DARK);
        formCard.add(formTitle, BorderLayout.NORTH);

        JPanel formFields = new JPanel(new GridBagLayout());
        formFields.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.weightx = 1.0;

        txtId = Theme.createTextField(15);
        txtId.setEditable(false);
        txtId.setBackground(new Color(245, 247, 250));
        txtId.setText(manager.generateNextPatientId());

        txtName = Theme.createTextField(15);
        spinAge = new JSpinner(new SpinnerNumberModel(30, 1, 125, 1));
        spinAge.setFont(Theme.FONT_REGULAR);
        spinAge.setPreferredSize(new Dimension(spinAge.getPreferredSize().width, 32));

        cbGender = Theme.createComboBox(new String[]{"Male", "Female", "Other"});
        txtPhone = Theme.createTextField(15);
        txtEmail = Theme.createTextField(15);
        cbBloodGroup = Theme.createComboBox(new String[]{"O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-"});
        txtEmergencyContact = Theme.createTextField(15);
        txtAddress = Theme.createTextField(15);

        txtMedicalHistory = new JTextArea(3, 15);
        txtMedicalHistory.setFont(Theme.FONT_REGULAR);
        txtMedicalHistory.setLineWrap(true);
        txtMedicalHistory.setWrapStyleWord(true);
        JScrollPane scrollHistory = new JScrollPane(txtMedicalHistory);
        scrollHistory.setBorder(Theme.createInputPaddingBorder());

        int row = 0;
        addField(formFields, gbc, "Patient ID (Auto):", txtId, row++);
        addField(formFields, gbc, "Full Name *:", txtName, row++);
        addField(formFields, gbc, "Age *:", spinAge, row++);
        addField(formFields, gbc, "Gender:", cbGender, row++);
        addField(formFields, gbc, "Phone Number *:", txtPhone, row++);
        addField(formFields, gbc, "Email Address:", txtEmail, row++);
        addField(formFields, gbc, "Blood Group:", cbBloodGroup, row++);
        addField(formFields, gbc, "Emergency Contact:", txtEmergencyContact, row++);
        addField(formFields, gbc, "Residential Address:", txtAddress, row++);
        addField(formFields, gbc, "Medical Notes / Allergies:", scrollHistory, row++);

        JScrollPane formScroll = new JScrollPane(formFields);
        formScroll.setBorder(null);
        formScroll.setOpaque(false);
        formScroll.getViewport().setOpaque(false);
        formCard.add(formScroll, BorderLayout.CENTER);

        // Form action buttons
        JPanel formButtons = new JPanel(new GridLayout(2, 2, 8, 8));
        formButtons.setOpaque(false);

        ModernButton btnSave = ModernButton.createPrimary("Save Patient");
        btnSave.addActionListener(e -> handleSavePatient());

        ModernButton btnUpdate = ModernButton.createNavy("Update Details");
        btnUpdate.addActionListener(e -> handleUpdatePatient());

        ModernButton btnClear = ModernButton.createSecondary("Clear Form");
        btnClear.addActionListener(e -> clearForm());

        ModernButton btnDelete = ModernButton.createDanger("Delete");
        btnDelete.addActionListener(e -> handleDeletePatient());

        formButtons.add(btnSave);
        formButtons.add(btnUpdate);
        formButtons.add(btnClear);
        formButtons.add(btnDelete);

        formCard.add(formButtons, BorderLayout.SOUTH);
        mainContent.add(formCard, BorderLayout.WEST);

        // RIGHT: Directory Table Card
        JPanel tableCard = Theme.createCard();
        tableCard.setLayout(new BorderLayout(0, 12));

        // Search Bar & Filter
        JPanel searchBar = new JPanel(new BorderLayout(10, 0));
        searchBar.setOpaque(false);

        JLabel lblSearch = new JLabel("Search Patients:");
        lblSearch.setFont(Theme.FONT_TITLE_BOLD);
        lblSearch.setForeground(Theme.NAVY_DARK);

        txtSearch = Theme.createTextField(20);
        txtSearch.putClientProperty("JTextField.placeholderText", "Search by ID, Name, Phone, or Blood Group...");
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterPatients(); }
            public void removeUpdate(DocumentEvent e) { filterPatients(); }
            public void changedUpdate(DocumentEvent e) { filterPatients(); }
        });

        ModernButton btnResetSearch = ModernButton.createSecondary("Reset");
        btnResetSearch.addActionListener(e -> {
            txtSearch.setText("");
            refreshTable();
        });

        searchBar.add(lblSearch, BorderLayout.WEST);
        searchBar.add(txtSearch, BorderLayout.CENTER);
        searchBar.add(btnResetSearch, BorderLayout.EAST);

        tableCard.add(searchBar, BorderLayout.NORTH);

        // Table
        String[] cols = {"Patient ID", "Name", "Age", "Gender", "Phone", "Blood Grp", "Emergency Contact"};
        tableModel = new DefaultTableModel(cols, 0);
        patientTable = new ModernTable(tableModel);

        patientTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && patientTable.getSelectedRow() != -1) {
                int selectedRow = patientTable.getSelectedRow();
                String id = (String) tableModel.getValueAt(selectedRow, 0);
                loadPatientToForm(id);
            }
        });

        tableCard.add(ModernTable.wrap(patientTable), BorderLayout.CENTER);

        // Footer with counter and Direct Queue action
        JPanel tableFooter = new JPanel(new BorderLayout());
        tableFooter.setOpaque(false);

        lblTotalCount = new JLabel("Total Patients: 0");
        lblTotalCount.setFont(Theme.FONT_TITLE_BOLD);
        lblTotalCount.setForeground(Theme.TEXT_SECONDARY);

        ModernButton btnQueueWalkin = ModernButton.createNavy("+ Send Selected Patient to Live Queue");
        btnQueueWalkin.addActionListener(e -> handleSendToQueue());

        tableFooter.add(lblTotalCount, BorderLayout.WEST);
        tableFooter.add(btnQueueWalkin, BorderLayout.EAST);

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

    private void handleSavePatient() {
        try {
            Patient p = new Patient();
            p.setId(txtId.getText().trim());
            p.setName(txtName.getText().trim());
            p.setAge((Integer) spinAge.getValue());
            p.setGender((String) cbGender.getSelectedItem());
            p.setPhone(txtPhone.getText().trim());
            p.setEmail(txtEmail.getText().trim());
            p.setBloodGroup((String) cbBloodGroup.getSelectedItem());
            p.setEmergencyContact(txtEmergencyContact.getText().trim());
            p.setAddress(txtAddress.getText().trim());
            p.setMedicalHistory(txtMedicalHistory.getText().trim());

            manager.addPatient(p);
            JOptionPane.showMessageDialog(this,
                    "Patient registered successfully!\nGenerated ID: " + p.getId(),
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            refreshTable();
        } catch (InvalidInputException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to save: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdatePatient() {
        try {
            String id = txtId.getText().trim();
            Patient p = manager.getPatientById(id);
            if (p == null) {
                JOptionPane.showMessageDialog(this, "Please select an existing patient from the table to update.",
                        "No Patient Selected", JOptionPane.WARNING_MESSAGE);
                return;
            }

            p.setName(txtName.getText().trim());
            p.setAge((Integer) spinAge.getValue());
            p.setGender((String) cbGender.getSelectedItem());
            p.setPhone(txtPhone.getText().trim());
            p.setEmail(txtEmail.getText().trim());
            p.setBloodGroup((String) cbBloodGroup.getSelectedItem());
            p.setEmergencyContact(txtEmergencyContact.getText().trim());
            p.setAddress(txtAddress.getText().trim());
            p.setMedicalHistory(txtMedicalHistory.getText().trim());

            manager.updatePatient(p);
            JOptionPane.showMessageDialog(this, "Patient details updated successfully!", "Updated", JOptionPane.INFORMATION_MESSAGE);
            refreshTable();
        } catch (InvalidInputException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleDeletePatient() {
        int selected = patientTable.getSelectedRow();
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient to delete.", "Select Patient", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = (String) tableModel.getValueAt(selected, 0);
        String name = (String) tableModel.getValueAt(selected, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete patient " + name + " (" + id + ")?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            manager.deletePatient(id);
            clearForm();
            refreshTable();
            JOptionPane.showMessageDialog(this, "Patient record removed.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void handleSendToQueue() {
        int selected = patientTable.getSelectedRow();
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient from the table first.", "Select Patient", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String patientId = (String) tableModel.getValueAt(selected, 0);
        Patient p = manager.getPatientById(patientId);
        if (p == null) return;

        // Dialog to select Doctor & Priority
        JComboBox<String> docBox = new JComboBox<>();
        for (var doc : manager.getAllDoctors()) {
            docBox.addItem(doc.getId() + " - Dr. " + doc.getName() + " (" + doc.getSpecialization() + ")");
        }
        JComboBox<PatientType> typeBox = new JComboBox<>(PatientType.values());
        JTextField txtReason = new JTextField("Walk-in Consultation");

        JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));
        form.add(new JLabel("Select Doctor:"));
        form.add(docBox);
        form.add(new JLabel("Queue Priority:"));
        form.add(typeBox);
        form.add(new JLabel("Reason / Symptoms:"));
        form.add(txtReason);

        int res = JOptionPane.showConfirmDialog(this, form, "Send Patient to Queue: " + p.getName(),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (res == JOptionPane.OK_OPTION) {
            try {
                String docSelected = (String) docBox.getSelectedItem();
                String docId = docSelected.split(" - ")[0];
                PatientType type = (PatientType) typeBox.getSelectedItem();

                var token = manager.issueToken(p.getId(), docId, type, txtReason.getText().trim(), null);
                JOptionPane.showMessageDialog(this,
                        "Token Generated Successfully!\n" +
                        "Token No: " + token.getTokenNumber() + "\n" +
                        "Queue Type: " + token.getType() + "\n" +
                        "Doctor: " + token.getDoctorName(),
                        "Token Issued", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error queueing patient: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadPatientToForm(String id) {
        Patient p = manager.getPatientById(id);
        if (p != null) {
            txtId.setText(p.getId());
            txtName.setText(p.getName());
            spinAge.setValue(p.getAge());
            cbGender.setSelectedItem(p.getGender());
            txtPhone.setText(p.getPhone());
            txtEmail.setText(p.getEmail());
            cbBloodGroup.setSelectedItem(p.getBloodGroup());
            txtEmergencyContact.setText(p.getEmergencyContact());
            txtAddress.setText(p.getAddress());
            txtMedicalHistory.setText(p.getMedicalHistory());
        }
    }

    private void clearForm() {
        txtId.setText(manager.generateNextPatientId());
        txtName.setText("");
        spinAge.setValue(30);
        cbGender.setSelectedIndex(0);
        txtPhone.setText("");
        txtEmail.setText("");
        cbBloodGroup.setSelectedIndex(0);
        txtEmergencyContact.setText("");
        txtAddress.setText("");
        txtMedicalHistory.setText("");
        patientTable.clearSelection();
    }

    private void filterPatients() {
        String q = txtSearch.getText().trim();
        List<Patient> list = manager.searchPatients(q);
        populateTable(list);
    }

    public void refreshTable() {
        List<Patient> list = manager.getAllPatients();
        populateTable(list);
    }

    private void populateTable(List<Patient> list) {
        tableModel.setRowCount(0);
        for (Patient p : list) {
            tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getName(),
                    p.getAge(),
                    p.getGender(),
                    p.getPhone(),
                    p.getBloodGroup(),
                    p.getEmergencyContact()
            });
        }
        lblTotalCount.setText("Total Patients: " + list.size());
    }
}
