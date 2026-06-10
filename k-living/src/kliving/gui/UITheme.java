package kliving.gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.Border;

/** Shared colour palette and factory helpers for the K-Living UI. */
public final class UITheme {

    private UITheme() {}

    // ── Palette ───────────────────────────────────────────────────────────
    public static final Color PRIMARY    = new Color(44,  62,  80);   // dark header
    public static final Color ACCENT     = new Color(52, 152, 219);   // blue buttons
    public static final Color SUCCESS    = new Color(39, 174,  96);   // green
    public static final Color DANGER     = new Color(231, 76,  60);   // red
    public static final Color WARNING    = new Color(243,156,  18);   // orange
    public static final Color SECONDARY  = new Color(127,140, 141);   // grey
    public static final Color BG         = new Color(245,246, 250);   // page bg
    public static final Color CARD_BG    = Color.WHITE;
    public static final Color BORDER_CLR = new Color(220,220, 228);
    public static final Color TEXT_DARK  = new Color(44, 62,  80);
    public static final Color TEXT_MUTED = new Color(127,140, 141);

    // ── Fonts ─────────────────────────────────────────────────────────────
    public static final Font FONT_TITLE  = new Font("맑은 고딕", Font.BOLD,  22);
    public static final Font FONT_H2     = new Font("맑은 고딕", Font.BOLD,  16);
    public static final Font FONT_H3     = new Font("맑은 고딕", Font.BOLD,  13);
    public static final Font FONT_BODY   = new Font("맑은 고딕", Font.PLAIN, 13);
    public static final Font FONT_SMALL  = new Font("맑은 고딕", Font.PLAIN, 11);
    public static final Font FONT_MONO   = resolveMonoFont();

    /** 한글을 지원하는 모노스페이스 폰트를 우선순위대로 선택한다. */
    private static Font resolveMonoFont() {
        String[] candidates = { "D2Coding", "나눔고딕코딩", "굴림체", "맑은 고딕" };
        java.util.Set<String> available = new java.util.HashSet<>(
            java.util.Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getAvailableFontFamilyNames()));
        for (String name : candidates) {
            if (available.contains(name)) return new Font(name, Font.PLAIN, 13);
        }
        return new Font("맑은 고딕", Font.PLAIN, 13);
    }

    // ── Border ────────────────────────────────────────────────────────────
    public static Border cardBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_CLR, 1, true),
            BorderFactory.createEmptyBorder(10, 14, 10, 14));
    }

    public static Border paddingBorder(int v, int h) {
        return BorderFactory.createEmptyBorder(v, h, v, h);
    }

    // ── Button factory ────────────────────────────────────────────────────
    public static JButton btn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFont(FONT_H3);
        b.setForeground(Color.WHITE);
        b.setBackground(bg);
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));

        Color hover = bg.darker();
        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setBackground(hover); }
            @Override public void mouseExited (MouseEvent e) { b.setBackground(bg); }
        });
        return b;
    }

    public static JButton btnPrimary  (String t) { return btn(t, ACCENT);   }
    public static JButton btnSuccess  (String t) { return btn(t, SUCCESS);  }
    public static JButton btnDanger   (String t) { return btn(t, DANGER);   }
    public static JButton btnSecondary(String t) { return btn(t, SECONDARY);}
    public static JButton btnWarning  (String t) { return btn(t, WARNING);  }

    // ── Header bar ────────────────────────────────────────────────────────
    public static JPanel header(String title) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(PRIMARY);
        p.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        JLabel lbl = new JLabel(title);
        lbl.setFont(FONT_TITLE);
        lbl.setForeground(Color.WHITE);
        p.add(lbl, BorderLayout.WEST);
        return p;
    }

    /** Header with a right-aligned extra label (e.g. user info). */
    public static JPanel header(String title, String sub) {
        JPanel p = header(title);
        JLabel s = new JLabel(sub);
        s.setFont(FONT_SMALL);
        s.setForeground(new Color(189, 195, 199));
        p.add(s, BorderLayout.EAST);
        return p;
    }

    // ── Labelled input row ────────────────────────────────────────────────
    public static JPanel labeledField(String labelText, JComponent field) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(FONT_H3);
        lbl.setForeground(TEXT_DARK);
        lbl.setPreferredSize(new Dimension(120, 30));
        row.add(lbl,   BorderLayout.WEST);
        row.add(field, BorderLayout.CENTER);
        return row;
    }

    // ── Styled text field ─────────────────────────────────────────────────
    public static JTextField textField() {
        JTextField tf = new JTextField();
        tf.setFont(FONT_BODY);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_CLR),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        return tf;
    }

    public static JPasswordField passwordField() {
        JPasswordField pf = new JPasswordField();
        pf.setFont(FONT_BODY);
        pf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_CLR),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        return pf;
    }

    public static JTextArea textArea(int rows, int cols) {
        JTextArea ta = new JTextArea(rows, cols);
        ta.setFont(FONT_BODY);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        return ta;
    }

    // ── Info badge ────────────────────────────────────────────────────────
    public static JLabel badge(String text, Color bg) {
        JLabel lbl = new JLabel(" " + text + " ");
        lbl.setFont(FONT_SMALL);
        lbl.setForeground(Color.WHITE);
        lbl.setBackground(bg);
        lbl.setOpaque(true);
        lbl.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
        return lbl;
    }

    // ── Separator ─────────────────────────────────────────────────────────
    public static JSeparator sep() {
        JSeparator s = new JSeparator();
        s.setForeground(BORDER_CLR);
        return s;
    }

    /** Shows an error dialog. */
    public static void showError(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "오류", JOptionPane.ERROR_MESSAGE);
    }

    /** Shows an info dialog. */
    public static void showInfo(Component parent, String msg, String title) {
        JOptionPane.showMessageDialog(parent, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
