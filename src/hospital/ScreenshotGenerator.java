package hospital;

import hospital.ui.MainFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ScreenshotGenerator {
    public static void main(String[] args) throws Exception {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeAndWait(() -> {
            try {
                MainFrame frame = new MainFrame();
                frame.setSize(1240, 780);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                String[] screens = {"DASHBOARD", "PATIENTS", "DOCTORS", "APPOINTMENTS", "QUEUE", "CONSULTATION"};
                String[] filenames = {
                    "screenshots/dashboard.png",
                    "screenshots/patients.png",
                    "screenshots/doctors.png",
                    "screenshots/appointments.png",
                    "screenshots/queue.png",
                    "screenshots/consultation.png"
                };

                File dir = new File("screenshots");
                if (!dir.exists()) dir.mkdirs();

                for (int i = 0; i < screens.length; i++) {
                    frame.showScreen(screens[i]);
                    frame.getContentPane().validate();
                    frame.getContentPane().repaint();
                    
                    Thread.sleep(400);

                    Container cp = frame.getContentPane();
                    BufferedImage image = new BufferedImage(cp.getWidth(), cp.getHeight(), BufferedImage.TYPE_INT_RGB);
                    Graphics2D g2 = image.createGraphics();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    cp.printAll(g2);
                    g2.dispose();

                    ImageIO.write(image, "png", new File(filenames[i]));
                    System.out.println("Saved screenshot: " + filenames[i] + " (" + new File(filenames[i]).length() + " bytes)");
                }

                frame.dispose();
                System.out.println("All screenshots captured successfully.");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        System.exit(0);
    }
}
