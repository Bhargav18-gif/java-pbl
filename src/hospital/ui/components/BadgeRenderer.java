package hospital.ui.components;

import hospital.ui.Theme;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Custom table cell renderer that formats status strings and priorities as
 * modern rounded pill badges.
 */
public class BadgeRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(Theme.FONT_SMALL_BOLD);

        String val = value != null ? value.toString() : "";
        label.setText(val);

        Color bg = new Color(241, 245, 249);
        Color fg = Theme.TEXT_PRIMARY;

        if (val.equalsIgnoreCase("EMERGENCY")) {
            bg = Theme.EMERGENCY_BG;
            fg = Theme.EMERGENCY_RED;
        } else if (val.equalsIgnoreCase("REGULAR")) {
            bg = Theme.INFO_BG;
            fg = Theme.INFO_BLUE;
        } else if (val.equalsIgnoreCase("COMPLETED")) {
            bg = Theme.SUCCESS_BG;
            fg = Theme.SUCCESS_GREEN;
        } else if (val.equalsIgnoreCase("SCHEDULED") || val.equalsIgnoreCase("CONFIRMED")) {
            bg = Theme.BLUE_LIGHT_BG;
            fg = Theme.BLUE_PRIMARY;
        } else if (val.equalsIgnoreCase("IN_QUEUE") || val.equalsIgnoreCase("WAITING") || val.equalsIgnoreCase("CALLING")) {
            bg = Theme.WARNING_BG;
            fg = Theme.WARNING_AMBER;
        } else if (val.equalsIgnoreCase("CANCELLED")) {
            bg = new Color(241, 245, 249);
            fg = Theme.TEXT_MUTED;
        } else if (val.equalsIgnoreCase("RESCHEDULED")) {
            bg = new Color(237, 233, 254);
            fg = new Color(109, 40, 217);
        }

        final Color badgeBg = bg;
        final Color badgeFg = fg;

        // Custom renderer wrapper
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(badgeBg);
                int h = 24;
                int w = Math.min(getWidth() - 16, Math.max(70, label.getPreferredSize().width + 16));
                int x = (getWidth() - w) / 2;
                int y = (getHeight() - h) / 2;
                g2.fillRoundRect(x, y, w, h, 14, 14);
                g2.dispose();
            }
        };

        panel.setOpaque(true);
        if (isSelected) {
            panel.setBackground(table.getSelectionBackground());
        } else {
            panel.setBackground(row % 2 == 0 ? Color.WHITE : Theme.BLUE_PALE);
        }

        label.setForeground(badgeFg);
        label.setOpaque(false);
        panel.add(label);

        return panel;
    }
}
