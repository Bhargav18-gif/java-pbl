package hospital.ui.components;

import hospital.ui.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Modern custom styled Swing button with rounded corners, clean hover effect,
 * and hospital color accents.
 */
public class ModernButton extends JButton {
    private Color normalColor;
    private Color hoverColor;
    private Color pressedColor;
    private boolean isHovered = false;
    private boolean isPressed = false;
    private int cornerRadius = 8;

    public ModernButton(String text, Color normalColor, Color hoverColor, Color textColor) {
        super(text);
        this.normalColor = normalColor;
        this.hoverColor = hoverColor;
        this.pressedColor = hoverColor.darker();
        setForeground(textColor);
        setFont(Theme.FONT_REGULAR_BOLD);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setMargin(new Insets(8, 16, 8, 16));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isEnabled()) {
                    isHovered = true;
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (isEnabled()) {
                    isPressed = true;
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
    }

    public static ModernButton createPrimary(String text) {
        return new ModernButton(text, Theme.BLUE_PRIMARY, Theme.BLUE_HOVER, Color.WHITE);
    }

    public static ModernButton createNavy(String text) {
        return new ModernButton(text, Theme.NAVY_MEDIUM, Theme.NAVY_LIGHT, Color.WHITE);
    }

    public static ModernButton createDanger(String text) {
        return new ModernButton(text, Theme.EMERGENCY_RED, new Color(185, 28, 28), Color.WHITE);
    }

    public static ModernButton createSuccess(String text) {
        return new ModernButton(text, Theme.SUCCESS_GREEN, new Color(21, 128, 61), Color.WHITE);
    }

    public static ModernButton createSecondary(String text) {
        ModernButton btn = new ModernButton(text, new Color(241, 245, 249), new Color(226, 232, 240), Theme.TEXT_PRIMARY);
        btn.setBorder(BorderFactory.createLineBorder(Theme.BORDER_COLOR, 1));
        return btn;
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (!isEnabled()) {
            g2.setColor(new Color(226, 232, 240));
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));
            g2.dispose();
            super.paintComponent(g);
            return;
        }

        if (isPressed) {
            g2.setColor(pressedColor);
        } else if (isHovered) {
            g2.setColor(hoverColor);
        } else {
            g2.setColor(normalColor);
        }

        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));
        g2.dispose();

        super.paintComponent(g);
    }
}
