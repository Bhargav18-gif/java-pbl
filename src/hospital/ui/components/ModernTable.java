package hospital.ui.components;

import hospital.ui.Theme;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Modern JTable tailored for hospital dashboards and records.
 * Provides custom Navy headers, alternating row colors, and padded cells.
 */
public class ModernTable extends JTable {

    public ModernTable(DefaultTableModel model) {
        super(model);
        setupStyle();
    }

    public ModernTable() {
        super();
        setupStyle();
    }

    private void setupStyle() {
        setFont(Theme.FONT_REGULAR);
        setForeground(Theme.TEXT_PRIMARY);
        setRowHeight(36);
        setGridColor(new Color(238, 242, 246));
        setShowVerticalLines(false);
        setShowHorizontalLines(true);
        setSelectionBackground(new Color(224, 237, 255));
        setSelectionForeground(Theme.NAVY_DARK);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setFillsViewportHeight(true);

        // Header Style
        JTableHeader header = getTableHeader();
        header.setFont(Theme.FONT_TITLE_BOLD);
        header.setBackground(Theme.NAVY_MEDIUM);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 38));
        header.setReorderingAllowed(false);
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                l.setBackground(Theme.NAVY_MEDIUM);
                l.setForeground(Color.WHITE);
                l.setFont(Theme.FONT_TITLE_BOLD);
                l.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                l.setHorizontalAlignment(SwingConstants.LEFT);
                return l;
            }
        });

        // Default Cell Padding
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : Theme.BLUE_PALE);
                }
                return c;
            }
        });
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false; // read-only by default in UI tables
    }

    public static JScrollPane wrap(JTable table) {
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(Theme.BORDER_COLOR, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        return scrollPane;
    }
}
