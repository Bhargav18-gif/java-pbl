package hospital;

import hospital.ui.MainFrame;

import javax.swing.*;
import java.awt.*;

/**
 * Entry point for the Hospital Appointment and Patient Queue Management System.
 * Advanced Java Programming Project-Based Learning (PBL).
 */
public class Main {
    public static void main(String[] args) {
        // Enable anti-aliasing for text rendering
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        // Set modern Look and Feel
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    // Nimbus or System look and feel
                    break;
                }
            }
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set system look and feel: " + e.getMessage());
        }

        // Customize UI defaults for clean styling
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 12));
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 13));

        // Start GUI on Swing Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
                System.out.println("==================================================");
                System.out.println("  Hospital Appointment & Queue Management System  ");
                System.out.println("  Status: Application launched successfully!     ");
                System.out.println("==================================================");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Failed to initialize application: " + ex.getMessage(),
                        "Fatal Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
