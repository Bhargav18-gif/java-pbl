package hospital.ui.components;

import hospital.ui.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Metric summary card for the hospital dashboard.
 * Displays value, title, icon/symbol, and color accent.
 */
public class StatCard extends JPanel {
    private final JLabel titleLabel;
    private final JLabel valueLabel;
    private final JLabel subtitleLabel;
    private final JLabel iconLabel;
    private final Color accentColor;
    private final Color badgeBgColor;

    public StatCard(String title, String initialValue, String subtitle, String iconSymbol, Color accentColor, Color badgeBgColor) {
        this.accentColor = accentColor;
        this.badgeBgColor = badgeBgColor;

        setLayout(new BorderLayout(12, 4));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(16, 18, 16, 18));
        setPreferredSize(new Dimension(200, 105));

        // Left / Center details
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        titleLabel = new JLabel(title);
        titleLabel.setFont(Theme.FONT_SMALL_BOLD);
        titleLabel.setForeground(Theme.TEXT_SECONDARY);

        valueLabel = new JLabel(initialValue);
        valueLabel.setFont(Theme.FONT_STAT_VAL);
        valueLabel.setForeground(Theme.TEXT_PRIMARY);

        subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(Theme.FONT_SMALL);
        subtitleLabel.setForeground(Theme.TEXT_MUTED);

        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(4));
        contentPanel.add(valueLabel);
        contentPanel.add(Box.createVerticalStrut(2));
        contentPanel.add(subtitleLabel);

        // Right icon badge
        iconLabel = new JLabel(iconSymbol, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(badgeBgColor);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
        iconLabel.setForeground(accentColor);
        iconLabel.setPreferredSize(new Dimension(44, 44));
        iconLabel.setOpaque(false);

        JPanel iconWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        iconWrapper.setOpaque(false);
        iconWrapper.add(iconLabel);

        add(contentPanel, BorderLayout.CENTER);
        add(iconWrapper, BorderLayout.EAST);
    }

    public void setValue(String value) {
        valueLabel.setText(value);
    }

    public void setSubtitle(String subtitle) {
        subtitleLabel.setText(subtitle);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));

        // Border
        g2.setColor(Theme.BORDER_COLOR);
        g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 12, 12));

        // Top accent bar
        g2.setColor(accentColor);
        g2.fillRoundRect(0, 0, getWidth(), 4, 4, 4);

        g2.dispose();
        super.paintComponent(g);
    }
}
