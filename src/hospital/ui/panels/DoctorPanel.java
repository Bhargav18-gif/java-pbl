package hospital.ui.panels;

import hospital.exceptions.InvalidInputException;
import hospital.model.Doctor;
import hospital.service.HospitalManager;
import hospital.ui.Theme;
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
 * Doctor Management Screen.
 * Enables adding, updating, searching, and managing hospital doctors and their specializations.
 */
public class DoctorPanel extends JPanel {
    private final HospitalManager manager;

    // Form fields
    private JTextField txtId;
    private JTextField txtName;
    private JComboBox<String> cbSpecialization;
    private JTextField txtRoomNumber;
    private JTextField txtPhone;
    private JTextField txtEmail;
    private JTextField txtQualification;
    private JSpinner spinExperience;
    private JTextField txtAvailableDays;
    private JTextField txtShiftTimings;

    // Table & Search
    private JTextField txtSearch;
    private DefaultTableModel tableModel;
    private ModernTable doctorTable;
    private JLabel lblTotalDoctors;

    public DoctorPanel(HospitalManager manager) {
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

        JLabel title = new JLabel("Doctor & Specialist Directory");
        title.setFont(Theme.FONT_HEADER);
        title.setForeground(Theme.NAVY_DARK);

        JLabel subtitle = new JLabel("Manage medical specialists, consultation rooms, qualifications, and duty shifts.");
        subtitle.setFont(Theme.FONT_REGULAR);
        subtitle.setForeground(Theme.TEXT_SECONDARY);

        JPanel titleWrapper = new JPanel(new GridLayout(2, 1, 0, 4));
        titleWrapper.setOpaque(false);
        titleWrapper.add(title);
        titleWrapper.add(subtitle);
        topHeader.add(titleWrapper, BorderLayout.WEST);

        add(topHeader, BorderLayout.NORTH);

        // Center split
        JPanel mainContent = new JPanel(new BorderLayout(16, 0));
        mainContent.setOpaque(false);

        // LEFT: Form Card
        JPanel formCard = Theme.createCard();
        formCard.setPreferredSize(new Dimension(380, 0));
        formCard.setLayout(new BorderLayout(0, 12));

        JLabel formTitle = new JLabel("Doctor Profile Details");
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
        txtId.setText(manager.generateNextDoctorId());

        txtName = Theme.createTextField(15);

        String[] specializations = {
                "Cardiology", "Neurology", "Pediatrics", "Orthopedics",
                "General Medicine", "Dermatology", "Oncology", "ENT",
                "Ophthalmology", "Psychiatry", "Radiology", "Pulmonology"
        };
        cbSpecialization = Theme.createComboBox(specializations);

        txtRoomNumber = Theme.createTextField(15);
        txtPhone = Theme.createTextField(15);
        txtEmail = Theme.createTextField(15);
        txtQualification = Theme.createTextField(15);

        spinExperience = new JSpinner(new SpinnerNumberModel(10, 0, 60, 1));
        spinExperience.setFont(Theme.FONT_REGULAR);
        spinExperience.setPreferredSize(new Dimension(spinExperience.getPreferredSize().width, 32));

        txtAvailableDays = Theme.createTextField(15);
        txtAvailableDays.setText("Mon - Fri");

        txtShiftTimings = Theme.createTextField(15);
        txtShiftTimings.setText("09:00 AM - 03:00 PM");

        int row = 0;
        addField(formFields, gbc, "Doctor ID (Auto):", txtId, row++);
        addField(formFields, gbc, "Doctor Full Name *:", txtName, row++);
        addField(formFields, gbc, "Medical Specialization *:", cbSpecialization, row++);
        addField(formFields, gbc, "Consultation Room / Chamber *:", txtRoomNumber, row++);
        addField(formFields, gbc, "Phone Contact:", txtPhone, row++);
        addField(formFields, gbc, "Hospital Email:", txtEmail, row++);
        addField(formFields, gbc, "Qualifications & Degrees:", txtQualification, row++);
        addField(formFields, gbc, "Experience (Years):", spinExperience, row++);
        addField(formFields, gbc, "Available Days:", txtAvailableDays, row++);
        addField(formFields, gbc, "Shift Timings:", txtShiftTimings, row++);

        JScrollPane formScroll = new JScrollPane(formFields);
        formScroll.setBorder(null);
        formScroll.setOpaque(false);
        formScroll.getViewport().setOpaque(false);
        formCard.add(formScroll, BorderLayout.CENTER);

        // Action Buttons
        JPanel formButtons = new JPanel(new GridLayout(2, 2, 8, 8));
        formButtons.setOpaque(false);

        ModernButton btnAdd = ModernButton.createPrimary("Add Doctor");
        btnAdd.addActionListener(e -> handleAddDoctor());

        ModernButton btnUpdate = ModernButton.createNavy("Update Doctor");
        btnUpdate.addActionListener(e -> handleUpdateDoctor());

        ModernButton btnClear = ModernButton.createSecondary("Clear Form");
        btnClear.addActionListener(e -> clearForm());

        ModernButton btnDelete = ModernButton.createDanger("Delete Doctor");
        btnDelete.addActionListener(e -> handleDeleteDoctor());

        formButtons.add(btnAdd);
        formButtons.add(btnUpdate);
        formButtons.add(btnClear);
        formButtons.add(btnDelete);

        formCard.add(formButtons, BorderLayout.SOUTH);
        mainContent.add(formCard, BorderLayout.WEST);

        // RIGHT: Directory Table Card
        JPanel tableCard = Theme.createCard();
        tableCard.setLayout(new BorderLayout(0, 12));

        // Search Bar
        JPanel searchBar = new JPanel(new BorderLayout(10, 0));
        searchBar.setOpaque(false);

        JLabel lblSearch = new JLabel("Search Doctors:");
        lblSearch.setFont(Theme.FONT_TITLE_BOLD);
        lblSearch.setForeground(Theme.NAVY_DARK);

        txtSearch = Theme.createTextField(20);
        txtSearch.putClientProperty("JTextField.placeholderText", "Search by ID, Name, Specialization, or Room...");
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterDoctors(); }
            public void removeUpdate(DocumentEvent e) { filterDoctors(); }
            public void changedUpdate(DocumentEvent e) { filterDoctors(); }
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
        String[] cols = {"Doctor ID", "Name", "Specialization", "Room", "Qualifications", "Experience", "Shift Timings"};
        tableModel = new DefaultTableModel(cols, 0);
        doctorTable = new ModernTable(tableModel);

        doctorTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && doctorTable.getSelectedRow() != -1) {
                int selectedRow = doctorTable.getSelectedRow();
                String id = (String) tableModel.getValueAt(selectedRow, 0);
                loadDoctorToForm(id);
            }
        });

        tableCard.add(ModernTable.wrap(doctorTable), BorderLayout.CENTER);

        // Footer
        JPanel tableFooter = new JPanel(new BorderLayout());
        tableFooter.setOpaque(false);

        lblTotalDoctors = new JLabel("Active Specialists: 0");
        lblTotalDoctors.setFont(Theme.FONT_TITLE_BOLD);
        lblTotalDoctors.setForeground(Theme.TEXT_SECONDARY);

        tableFooter.add(lblTotalDoctors, BorderLayout.WEST);
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

    private void handleAddDoctor() {
        try {
            Doctor d = new Doctor();
            d.setId(txtId.getText().trim());
            d.setName(txtName.getText().trim());
            d.setSpecialization((String) cbSpecialization.getSelectedItem());
            d.setRoomNumber(txtRoomNumber.getText().trim());
            d.setPhone(txtPhone.getText().trim());
            d.setEmail(txtEmail.getText().trim());
            d.setQualification(txtQualification.getText().trim());
            d.setExperienceYears((Integer) spinExperience.getValue());
            d.setAvailableDays(txtAvailableDays.getText().trim());
            d.setShiftTimings(txtShiftTimings.getText().trim());

            manager.addDoctor(d);
            JOptionPane.showMessageDialog(this,
                    "Doctor added successfully!\nID: " + d.getId() + "\nSpecialization: " + d.getSpecialization(),
                    "Doctor Added", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            refreshTable();
        } catch (InvalidInputException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdateDoctor() {
        try {
            String id = txtId.getText().trim();
            Doctor d = manager.getDoctorById(id);
            if (d == null) {
                JOptionPane.showMessageDialog(this, "Please select an existing doctor from the table to update.",
                        "No Doctor Selected", JOptionPane.WARNING_MESSAGE);
                return;
            }

            d.setName(txtName.getText().trim());
            d.setSpecialization((String) cbSpecialization.getSelectedItem());
            d.setRoomNumber(txtRoomNumber.getText().trim());
            d.setPhone(txtPhone.getText().trim());
            d.setEmail(txtEmail.getText().trim());
            d.setQualification(txtQualification.getText().trim());
            d.setExperienceYears((Integer) spinExperience.getValue());
            d.setAvailableDays(txtAvailableDays.getText().trim());
            d.setShiftTimings(txtShiftTimings.getText().trim());

            manager.updateDoctor(d);
            JOptionPane.showMessageDialog(this, "Doctor details updated successfully!", "Updated", JOptionPane.INFORMATION_MESSAGE);
            refreshTable();
        } catch (InvalidInputException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleDeleteDoctor() {
        int selected = doctorTable.getSelectedRow();
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Please select a doctor to remove.", "Select Doctor", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = (String) tableModel.getValueAt(selected, 0);
        String name = (String) tableModel.getValueAt(selected, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove Dr. " + name + " (" + id + ")?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            manager.deleteDoctor(id);
            clearForm();
            refreshTable();
            JOptionPane.showMessageDialog(this, "Doctor profile removed.", "Removed", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void loadDoctorToForm(String id) {
        Doctor d = manager.getDoctorById(id);
        if (d != null) {
            txtId.setText(d.getId());
            txtName.setText(d.getName());
            cbSpecialization.setSelectedItem(d.getSpecialization());
            txtRoomNumber.setText(d.getRoomNumber());
            txtPhone.setText(d.getPhone());
            txtEmail.setText(d.getEmail());
            txtQualification.setText(d.getQualification());
            spinExperience.setValue(d.getExperienceYears());
            txtAvailableDays.setText(d.getAvailableDays());
            txtShiftTimings.setText(d.getShiftTimings());
        }
    }

    private void clearForm() {
        txtId.setText(manager.generateNextDoctorId());
        txtName.setText("");
        cbSpecialization.setSelectedIndex(0);
        txtRoomNumber.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtQualification.setText("");
        spinExperience.setValue(10);
        txtAvailableDays.setText("Mon - Fri");
        txtShiftTimings.setText("09:00 AM - 03:00 PM");
        doctorTable.clearSelection();
    }

    private void filterDoctors() {
        String q = txtSearch.getText().trim();
        List<Doctor> list = manager.searchDoctors(q);
        populateTable(list);
    }

    public void refreshTable() {
        List<Doctor> list = manager.getAllDoctors();
        populateTable(list);
    }

    private void populateTable(List<Doctor> list) {
        tableModel.setRowCount(0);
        for (Doctor d : list) {
            tableModel.addRow(new Object[]{
                    d.getId(),
                    "Dr. " + d.getName(),
                    d.getSpecialization(),
                    d.getRoomNumber(),
                    d.getQualification(),
                    d.getExperienceYears() + " yrs",
                    d.getShiftTimings()
            });
        }
        lblTotalDoctors.setText("Active Specialists: " + list.size());
    }
}
