package hospital.ui;

import hospital.model.QueueToken;
import hospital.queue.QueueObserver;
import hospital.service.HospitalManager;
import hospital.ui.components.ModernButton;
import hospital.ui.panels.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Main Application Window.
 * Coordinates sidebar navigation, modern hospital branding, and dynamic panel updates.
 */
public class MainFrame extends JFrame implements QueueObserver {
    private final HospitalManager manager;

    private CardLayout cardLayout;
    private JPanel contentContainer;

    // Panels
    private DashboardPanel dashboardPanel;
    private PatientPanel patientPanel;
    private DoctorPanel doctorPanel;
    private AppointmentPanel appointmentPanel;
    private QueuePanel queuePanel;
    private ConsultationPanel consultationPanel;

    // Sidebar navigation buttons map
    private final Map<String, JButton> navButtons = new HashMap<>();
    private String currentScreen = "DASHBOARD";

    public MainFrame() {
        this.manager = HospitalManager.getInstance();

        setTitle("St. Jude Hospital - Appointment & Patient Queue Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1320, 840);
        setMinimumSize(new Dimension(1120, 720));
        setLocationRelativeTo(null);

        initUI();

        // Register observers for real-time dynamic updates across all tabs
        manager.addChangeListener(this::onGlobalDataChanged);
        manager.getQueueEngine().addObserver(this);
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Theme.BG_MAIN);

        // LEFT: Sidebar Navigation
        JPanel sidebar = createSidebar();
        root.add(sidebar, BorderLayout.WEST);

        // CENTER: Content Panels with CardLayout
        cardLayout = new CardLayout();
        contentContainer = new JPanel(cardLayout);
        contentContainer.setBackground(Theme.BG_MAIN);

        dashboardPanel = new DashboardPanel(manager, this::showScreen);
        patientPanel = new PatientPanel(manager);
        doctorPanel = new DoctorPanel(manager);
        appointmentPanel = new AppointmentPanel(manager);
        queuePanel = new QueuePanel(manager, this::showScreen);
        consultationPanel = new ConsultationPanel(manager);

        contentContainer.add(dashboardPanel, "DASHBOARD");
        contentContainer.add(patientPanel, "PATIENTS");
        contentContainer.add(doctorPanel, "DOCTORS");
        contentContainer.add(appointmentPanel, "APPOINTMENTS");
        contentContainer.add(queuePanel, "QUEUE");
        contentContainer.add(consultationPanel, "CONSULTATION");

        root.add(contentContainer, BorderLayout.CENTER);
        setContentPane(root);

        // Highlight initial button
        highlightNavButton("DASHBOARD");
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setBackground(Theme.NAVY_DARK);
        sidebar.setBorder(new EmptyBorder(24, 16, 24, 16));

        // Brand / Logo Top
        JPanel brandPanel = new JPanel();
        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));
        brandPanel.setOpaque(false);

        JLabel logoBadge = new JLabel("⚕ ST. JUDE MEDICAL", SwingConstants.LEFT);
        logoBadge.setFont(Theme.FONT_APP_TITLE);
        logoBadge.setForeground(Color.WHITE);

        JLabel logoSub = new JLabel("CLINICAL QUEUE & APPOINTMENTS");
        logoSub.setFont(Theme.FONT_SMALL_BOLD);
        logoSub.setForeground(Theme.BLUE_ACCENT);

        JLabel appVersion = new JLabel("PBL Demonstration Edition v2.5");
        appVersion.setFont(Theme.FONT_SMALL);
        appVersion.setForeground(Theme.TEXT_MUTED);

        brandPanel.add(logoBadge);
        brandPanel.add(Box.createVerticalStrut(4));
        brandPanel.add(logoSub);
        brandPanel.add(Box.createVerticalStrut(2));
        brandPanel.add(appVersion);
        brandPanel.add(Box.createVerticalStrut(24));

        JSeparator sep = new JSeparator();
        sep.setForeground(Theme.NAVY_LIGHT);
        brandPanel.add(sep);
        brandPanel.add(Box.createVerticalStrut(18));

        sidebar.add(brandPanel, BorderLayout.NORTH);

        // Nav buttons center
        JPanel navPanel = new JPanel(new GridLayout(6, 1, 0, 8));
        navPanel.setOpaque(false);

        addNavButton(navPanel, "DASHBOARD", "Dashboard", "Overview & statistics");
        addNavButton(navPanel, "PATIENTS", "Patients", "Register & directory");
        addNavButton(navPanel, "DOCTORS", "Doctors", "Specialists & schedules");
        addNavButton(navPanel, "APPOINTMENTS", "Appointments", "Booking & dispatch");
        addNavButton(navPanel, "QUEUE", "Live Queues", "Emergency & Regular");
        addNavButton(navPanel, "CONSULTATION", "Consultation Desk", "Patient examination");

        sidebar.add(navPanel, BorderLayout.CENTER);

        // Footer panel
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.Y_AXIS));
        footerPanel.setOpaque(false);

        JSeparator sepBottom = new JSeparator();
        sepBottom.setForeground(Theme.NAVY_LIGHT);
        footerPanel.add(sepBottom);
        footerPanel.add(Box.createVerticalStrut(12));

        JLabel lblSystemStatus = new JLabel("System Status: Online");
        lblSystemStatus.setFont(Theme.FONT_SMALL_BOLD);
        lblSystemStatus.setForeground(Theme.SUCCESS_GREEN);

        JLabel lblThreading = new JLabel("Producer-Consumer: Active");
        lblThreading.setFont(Theme.FONT_SMALL);
        lblThreading.setForeground(new Color(203, 213, 225));

        footerPanel.add(lblSystemStatus);
        footerPanel.add(Box.createVerticalStrut(4));
        footerPanel.add(lblThreading);

        sidebar.add(footerPanel, BorderLayout.SOUTH);

        return sidebar;
    }

    private void addNavButton(JPanel container, String screenId, String title, String subtitle) {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                boolean isSelected = screenId.equals(currentScreen);
                if (isSelected) {
                    g2.setColor(Theme.BLUE_PRIMARY);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                } else if (getModel().isRollover()) {
                    g2.setColor(Theme.NAVY_LIGHT);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                }

                g2.dispose();
                super.paintComponent(g);
            }
        };

        btn.setLayout(new GridLayout(2, 1, 0, 1));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 14, 8, 14));

        JLabel lTitle = new JLabel(title);
        lTitle.setFont(Theme.FONT_TITLE_BOLD);
        lTitle.setForeground(Color.WHITE);

        JLabel lSub = new JLabel(subtitle);
        lSub.setFont(Theme.FONT_SMALL);
        lSub.setForeground(new Color(186, 215, 248));

        btn.add(lTitle);
        btn.add(lSub);

        btn.addActionListener(e -> showScreen(screenId));

        navButtons.put(screenId, btn);
        container.add(btn);
    }

    public void showScreen(String screenId) {
        this.currentScreen = screenId;
        cardLayout.show(contentContainer, screenId);
        highlightNavButton(screenId);

        // Refresh panel specific data if needed
        if ("APPOINTMENTS".equals(screenId)) {
            appointmentPanel.refreshDropdowns();
            appointmentPanel.refreshTable();
        } else if ("QUEUE".equals(screenId)) {
            queuePanel.refreshDropdowns();
            queuePanel.refreshQueues();
        } else if ("CONSULTATION".equals(screenId)) {
            consultationPanel.syncWithQueueState();
            consultationPanel.refreshHistoryTable();
        } else if ("DASHBOARD".equals(screenId)) {
            dashboardPanel.refreshData();
        }
    }

    private void highlightNavButton(String activeScreenId) {
        for (Map.Entry<String, JButton> entry : navButtons.entrySet()) {
            entry.getValue().repaint();
        }
    }

    private void onGlobalDataChanged() {
        SwingUtilities.invokeLater(() -> {
            dashboardPanel.refreshData();
            appointmentPanel.refreshTable();
            patientPanel.refreshTable();
            doctorPanel.refreshTable();
            queuePanel.refreshQueues();
            consultationPanel.refreshHistoryTable();
        });
    }

    // --- QueueObserver Callbacks ---
    @Override
    public void onQueueUpdated() {
        SwingUtilities.invokeLater(() -> {
            dashboardPanel.refreshData();
            queuePanel.refreshQueues();
            consultationPanel.syncWithQueueState();
        });
    }

    @Override
    public void onPatientCalled(QueueToken token) {
        SwingUtilities.invokeLater(() -> {
            dashboardPanel.refreshData();
            queuePanel.refreshQueues();
            consultationPanel.setActivePatient(token);
        });
    }

    @Override
    public void onConsultationCompleted(QueueToken token) {
        SwingUtilities.invokeLater(() -> {
            dashboardPanel.refreshData();
            queuePanel.refreshQueues();
            consultationPanel.refreshHistoryTable();
        });
    }
}
