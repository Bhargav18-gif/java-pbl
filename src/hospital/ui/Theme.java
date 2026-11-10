package hospital.ui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Visual design system constants and factory helpers for the Hospital application.
 * Professional Hospital Theme: White, Light Blue, Dark Navy.
 */
public final class Theme {
    private Theme() {}

    // Color Palette
    public static final Color NAVY_DARK = new Color(15, 34, 64);       // #0F2240 Deep Hospital Navy
    public static final Color NAVY_MEDIUM = new Color(24, 49, 83);     // #183153
    public static final Color NAVY_LIGHT = new Color(38, 71, 114);     // #264772
    public static final Color BLUE_PRIMARY = new Color(24, 119, 242);  // #1877F2 Vibrant Medical Blue
    public static final Color BLUE_HOVER = new Color(13, 99, 212);     // #0D63D4
    public static final Color BLUE_ACCENT = new Color(0, 168, 204);    // #00A8CC Hospital Cyan
    public static final Color BLUE_LIGHT_BG = new Color(235, 244, 255);// #EBF4FF Soft light blue
    public static final Color BLUE_PALE = new Color(243, 248, 255);    // #F3F8FF Very soft blue tint

    public static final Color BG_MAIN = new Color(248, 250, 252);      // #F8FAFC Crisp hospital off-white
    public static final Color CARD_BG = Color.WHITE;
    public static final Color BORDER_COLOR = new Color(222, 230, 240); // Soft grey-blue border
    public static final Color BORDER_FOCUSED = new Color(24, 119, 242);

    public static final Color TEXT_PRIMARY = new Color(15, 23, 42);    // #0F172A Near-black navy
    public static final Color TEXT_SECONDARY = new Color(100, 116, 139);// #64748B Slate grey
    public static final Color TEXT_MUTED = new Color(148, 163, 184);

    // Alert & Status Colors
    public static final Color EMERGENCY_RED = new Color(220, 38, 38);
    public static final Color EMERGENCY_BG = new Color(254, 226, 226);
    public static final Color SUCCESS_GREEN = new Color(16, 149, 79);
    public static final Color SUCCESS_BG = new Color(220, 252, 231);
    public static final Color WARNING_AMBER = new Color(217, 119, 6);
    public static final Color WARNING_BG = new Color(254, 243, 199);
    public static final Color INFO_BLUE = new Color(2, 132, 199);
    public static final Color INFO_BG = new Color(224, 242, 254);

    // Typography
    public static final Font FONT_APP_TITLE = new Font("Segoe UI", Font.BOLD, 19);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_SUBHEADER = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_TITLE_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_REGULAR_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_SMALL_BOLD = new Font("Segoe UI", Font.BOLD, 11);
    public static final Font FONT_STAT_VAL = new Font("Segoe UI", Font.BOLD, 26);

    // Helpers
    public static JPanel createCard() {
        JPanel card = new JPanel();
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(16, 16, 16, 16)
        ));
        return card;
    }

    public static JTextField createTextField(int columns) {
        JTextField tf = new JTextField(columns);
        tf.setFont(FONT_REGULAR);
        tf.setForeground(TEXT_PRIMARY);
        tf.setBackground(Color.WHITE);
        tf.setBorder(createInputPaddingBorder());
        tf.setPreferredSize(new Dimension(tf.getPreferredSize().width, 34));
        return tf;
    }

    public static <E> JComboBox<E> createComboBox(E[] items) {
        JComboBox<E> cb = new JComboBox<>(items);
        cb.setFont(FONT_REGULAR);
        cb.setBackground(Color.WHITE);
        cb.setForeground(TEXT_PRIMARY);
        cb.setPreferredSize(new Dimension(cb.getPreferredSize().width, 34));
        return cb;
    }

    public static JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_REGULAR_BOLD);
        label.setForeground(TEXT_PRIMARY);
        return label;
    }

    public static Border createInputPaddingBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(6, 10, 6, 10)
        );
    }
}
