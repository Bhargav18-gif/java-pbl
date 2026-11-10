package hospital.ui.panels;

import hospital.model.Appointment;
import hospital.model.PatientType;
import hospital.service.HospitalManager;
import hospital.ui.Theme;
import hospital.ui.components.BadgeRenderer;
import hospital.ui.components.ModernButton;
import hospital.ui.components.ModernTable;
import hospital.ui.components.StatCard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

/**
 * Hospital Dashboard Screen.
 * Displays KPI summary cards, live queue status, recent appointments, and quick action shortcuts.
 */
public class DashboardPanel extends JPanel {
    private final HospitalManager manager;
    private final Consumer<String> navigationHandler;

    private StatCard cardPatients;
    private StatCard cardAppointments;
    private StatCard cardWaiting;
    private StatCard cardEmergency;
    private StatCard cardCompleted;
    private StatCard cardDoctors;

    private JLabel queueBannerText;
    private DefaultTableModel recentApptModel;
    private ModernTable recentApptTable;

    public DashboardPanel(HospitalManager manager, Consumer<String> navigationHandler) {
        this.manager = manager;
        this.navigationHandler = navigationHandler;

        setLayout(new BorderLayout(0, 18));
        setBackground(Theme.BG_MAIN);
        setBorder(new EmptyBorder(20, 24, 20, 24));

        initUI();
        refreshData();
    }

    private void initUI() {
        // Top Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel title = new JLabel("Hospital Overview & Dashboard");
        title.setFont(Theme.FONT_HEADER);
        title.setForeground(Theme.NAVY_DARK);

        JLabel subtitle = new JLabel("Live hospital statistics, real-time queues, and clinical appointment activity.");
        subtitle.setFont(Theme.FONT_REGULAR);
        subtitle.setForeground(Theme.TEXT_SECONDARY);

        JPanel titleWrapper = new JPanel(new GridLayout(2, 1, 0, 4));
        titleWrapper.setOpaque(false);
        titleWrapper.add(title);
        titleWrapper.add(subtitle);
        headerPanel.add(titleWrapper, BorderLayout.WEST);

        // Live Queue Status Banner
        JPanel bannerPanel = new JPanel(new BorderLayout(12, 0));
        bannerPanel.setBackground(Theme.BLUE_LIGHT_BG);
        bannerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(186, 215, 248), 1, true),
                new EmptyBorder(10, 16, 10, 16)
        ));

        queueBannerText = new JLabel("Queue Status: Loading...");
        queueBannerText.setFont(Theme.FONT_TITLE_BOLD);
        queueBannerText.setForeground(Theme.NAVY_DARK);

        ModernButton btnQueueJump = ModernButton.createPrimary("View Live Queue");
        btnQueueJump.setFont(Theme.FONT_SMALL_BOLD);
        btnQueueJump.addActionListener(e -> navigationHandler.accept("QUEUE"));

        bannerPanel.add(queueBannerText, BorderLayout.CENTER);
        bannerPanel.add(btnQueueJump, BorderLayout.EAST);

        JPanel topContainer = new JPanel(new BorderLayout(0, 14));
        topContainer.setOpaque(false);
        topContainer.add(headerPanel, BorderLayout.NORTH);
        topContainer.add(bannerPanel, BorderLayout.SOUTH);
        add(topContainer, BorderLayout.NORTH);

        // Center Content: Cards + Tables & Actions
        JPanel centerPanel = new JPanel(new BorderLayout(0, 18));
        centerPanel.setOpaque(false);

        // 6 KPI Summary Cards
        JPanel statsGrid = new JPanel(new GridLayout(2, 3, 14, 14));
        statsGrid.setOpaque(false);

        cardPatients = new StatCard("TOTAL PATIENTS", "0", "Registered in system", "P",
                Theme.BLUE_PRIMARY, Theme.BLUE_LIGHT_BG);
        cardAppointments = new StatCard("TOTAL APPOINTMENTS", "0", "Scheduled bookings", "A",
                Theme.NAVY_LIGHT, new Color(241, 245, 249));
        cardWaiting = new StatCard("WAITING IN QUEUE", "0", "Currently waiting", "Q",
                Theme.WARNING_AMBER, Theme.WARNING_BG);
        cardEmergency = new StatCard("EMERGENCY QUEUE", "0", "Critical priority patients", "E",
                Theme.EMERGENCY_RED, Theme.EMERGENCY_BG);
        cardCompleted = new StatCard("CONSULTATIONS DONE", "0", "Completed today", "C",
                Theme.SUCCESS_GREEN, Theme.SUCCESS_BG);
        cardDoctors = new StatCard("ACTIVE DOCTORS", "0", "Specialists available", "D",
                Theme.INFO_BLUE, Theme.INFO_BG);

        statsGrid.add(cardPatients);
        statsGrid.add(cardAppointments);
        statsGrid.add(cardWaiting);
        statsGrid.add(cardEmergency);
        statsGrid.add(cardCompleted);
        statsGrid.add(cardDoctors);

        centerPanel.add(statsGrid, BorderLayout.NORTH);

        // Bottom section: Recent Appointments Table & Quick Action shortcuts
        JPanel bottomSplit = new JPanel(new BorderLayout(16, 0));
        bottomSplit.setOpaque(false);

        // Recent Appointments Card
        JPanel tableCard = Theme.createCard();
        tableCard.setLayout(new BorderLayout(0, 12));

        JLabel tableTitle = new JLabel("Recent Appointments");
        tableTitle.setFont(Theme.FONT_SUBHEADER);
        tableTitle.setForeground(Theme.NAVY_DARK);

        String[] cols = {"Appt ID", "Patient Name", "Doctor", "Specialization", "Date", "Slot", "Type", "Status"};
        recentApptModel = new DefaultTableModel(cols, 0);
        recentApptTable = new ModernTable(recentApptModel);
        recentApptTable.getColumnModel().getColumn(6).setCellRenderer(new BadgeRenderer());
        recentApptTable.getColumnModel().getColumn(7).setCellRenderer(new BadgeRenderer());

        tableCard.add(tableTitle, BorderLayout.NORTH);
        tableCard.add(ModernTable.wrap(recentApptTable), BorderLayout.CENTER);
        bottomSplit.add(tableCard, BorderLayout.CENTER);

        // Quick Actions Sidebar
        JPanel actionsCard = Theme.createCard();
        actionsCard.setPreferredSize(new Dimension(240, 200));
        actionsCard.setLayout(new BorderLayout(0, 12));

        JLabel actionsTitle = new JLabel("Quick Actions");
        actionsTitle.setFont(Theme.FONT_SUBHEADER);
        actionsTitle.setForeground(Theme.NAVY_DARK);

        JPanel buttonsPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        buttonsPanel.setOpaque(false);

        ModernButton btnQuickReg = ModernButton.createPrimary("+ Register Patient");
        btnQuickReg.addActionListener(e -> navigationHandler.accept("PATIENTS"));

        ModernButton btnQuickAppt = ModernButton.createNavy("+ Book Appointment");
        btnQuickAppt.addActionListener(e -> navigationHandler.accept("APPOINTMENTS"));

        ModernButton btnQuickEmergency = ModernButton.createDanger("! Emergency Walk-in");
        btnQuickEmergency.addActionListener(e -> navigationHandler.accept("QUEUE"));

        ModernButton btnConsultationDesk = ModernButton.createSuccess("Open Consultation Desk");
        btnConsultationDesk.addActionListener(e -> navigationHandler.accept("CONSULTATION"));

        buttonsPanel.add(btnQuickReg);
        buttonsPanel.add(btnQuickAppt);
        buttonsPanel.add(btnQuickEmergency);
        buttonsPanel.add(btnConsultationDesk);

        actionsCard.add(actionsTitle, BorderLayout.NORTH);
        actionsCard.add(buttonsPanel, BorderLayout.CENTER);

        bottomSplit.add(actionsCard, BorderLayout.EAST);
        centerPanel.add(bottomSplit, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }

    public void refreshData() {
        cardPatients.setValue(String.valueOf(manager.getTotalPatients()));
        cardAppointments.setValue(String.valueOf(manager.getTotalAppointments()));
        cardWaiting.setValue(String.valueOf(manager.getWaitingPatients()));
        cardEmergency.setValue(String.valueOf(manager.getEmergencyPatients()));
        cardCompleted.setValue(String.valueOf(manager.getCompletedConsultations()));
        cardDoctors.setValue(String.valueOf(manager.getTotalDoctors()));

        int emg = manager.getEmergencyPatients();
        int reg = manager.getQueueEngine().getRegularCount();
        String servingText = manager.getQueueEngine().getCurrentlyServing() != null
                ? "Serving: [" + manager.getQueueEngine().getCurrentlyServing().getTokenNumber() + "] "
                + manager.getQueueEngine().getCurrentlyServing().getPatientName()
                : "No patient currently in consultation";

        queueBannerText.setText(String.format("Live Queue: %d Emergency | %d Regular Waiting   ●   %s", emg, reg, servingText));

        // Refresh Recent Appointments
        recentApptModel.setRowCount(0);
        List<Appointment> recents = manager.getRecentAppointments(10);
        for (Appointment a : recents) {
            recentApptModel.addRow(new Object[]{
                    a.getAppointmentId(),
                    a.getPatientName(),
                    "Dr. " + a.getDoctorName(),
                    a.getDoctorSpecialization(),
                    a.getFormattedDate(),
                    a.getTimeSlot(),
                    a.getType().name(),
                    a.getStatus().name()
            });
        }
    }
}
