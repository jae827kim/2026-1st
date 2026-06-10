package kliving.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import kliving.manager.SystemManager;

/** UC-02 : Login screen. */
public class LoginPanel extends JPanel {

    private final MainFrame    frame;
    private final JTextField   tfId;
    private final JPasswordField tfPw;

    public LoginPanel(MainFrame frame) {
        this.frame = frame;
        setBackground(UITheme.BG);
        setLayout(new GridBagLayout());

        // ── Card container ────────────────────────────────────────────────
        JPanel card = new JPanel();
        card.setBackground(UITheme.CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER_CLR, 1, true),
            BorderFactory.createEmptyBorder(36, 48, 36, 48)));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(400, 420));

        // Logo / Title
        JLabel logo = new JLabel("K-Living Solution");
        logo.setFont(new Font("맑은 고딕", Font.BOLD, 26));
        logo.setForeground(UITheme.PRIMARY);
        logo.setAlignmentX(CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Reward-based Knowledge Sharing");
        sub.setFont(UITheme.FONT_SMALL);
        sub.setForeground(UITheme.TEXT_MUTED);
        sub.setAlignmentX(CENTER_ALIGNMENT);

        // Fields
        tfId = UITheme.textField();
        JPasswordField pw = UITheme.passwordField();
        tfPw = pw;

        JPanel fieldPanel = new JPanel();
        fieldPanel.setOpaque(false);
        fieldPanel.setLayout(new GridLayout(4, 1, 0, 10));
        fieldPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        fieldPanel.add(UITheme.labeledField("아이디", tfId));
        fieldPanel.add(new JPanel());   // spacer row already consumed by GridLayout gap
        fieldPanel.add(UITheme.labeledField("비밀번호", tfPw));
        fieldPanel.add(new JPanel());

        // Rebuild layout (simpler vertical box approach)
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.removeAll();
        addRow(fieldPanel, "아이디     ", tfId);
        fieldPanel.add(Box.createVerticalStrut(10));
        addRow(fieldPanel, "비밀번호", tfPw);

        // Buttons
        JButton btnLogin    = UITheme.btnPrimary("로그인");
        JButton btnRegister = UITheme.btnSecondary("회원가입");
        btnLogin   .setAlignmentX(CENTER_ALIGNMENT);
        btnRegister.setAlignmentX(CENTER_ALIGNMENT);
        btnLogin   .setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnRegister.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        btnLogin.addActionListener(this::doLogin);
        btnRegister.addActionListener(e -> frame.showRegister());

        // Allow Enter key to submit
        tfId.addActionListener(this::doLogin);
        tfPw.addActionListener(this::doLogin);

        card.add(logo);
        card.add(Box.createVerticalStrut(4));
        card.add(sub);
        card.add(Box.createVerticalStrut(28));
        card.add(fieldPanel);
        card.add(Box.createVerticalStrut(20));
        card.add(btnLogin);
        card.add(Box.createVerticalStrut(10));
        card.add(btnRegister);

        add(card);
    }

    private void addRow(JPanel parent, String label, JComponent field) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(UITheme.FONT_H3);
        lbl.setForeground(UITheme.TEXT_DARK);
        lbl.setPreferredSize(new Dimension(80, 30));
        row.add(lbl,   BorderLayout.WEST);
        row.add(field, BorderLayout.CENTER);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        parent.add(row);
    }

    private void doLogin(ActionEvent e) {
        String userId = tfId.getText().trim();
        String pw     = new String(tfPw.getPassword());

        String result = SystemManager.getInstance().login(userId, pw);
        if (result.startsWith("ERROR:")) {
            UITheme.showError(frame, result.substring(6));
        } else {
            tfId.setText("");
            tfPw.setText("");
            frame.showHome();
        }
    }
}
