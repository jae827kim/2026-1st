package kliving.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import kliving.manager.SystemManager;

/** UC-01 : Registration screen. */
public class RegisterPanel extends JPanel {

    private final MainFrame      frame;
    private final JTextField     tfId;
    private final JPasswordField tfPw;
    private final JTextField     tfNickname;
    private final JTextField     tfExpYear;

    public RegisterPanel(MainFrame frame) {
        this.frame = frame;
        setBackground(UITheme.BG);
        setLayout(new GridBagLayout());

        JPanel card = new JPanel();
        card.setBackground(UITheme.CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER_CLR, 1, true),
            BorderFactory.createEmptyBorder(36, 48, 36, 48)));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(440, 500));

        JLabel title = new JLabel("회원가입");
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.PRIMARY);
        title.setAlignmentX(CENTER_ALIGNMENT);

        tfId       = UITheme.textField();
        tfPw       = UITheme.passwordField();
        tfNickname = UITheme.textField();
        tfExpYear  = UITheme.textField();

        JPanel fields = new JPanel();
        fields.setOpaque(false);
        fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));
        fields.setBorder(BorderFactory.createEmptyBorder(24, 0, 24, 0));

        addRow(fields, "아이디",       tfId,       "영문자, 숫자 조합");
        fields.add(Box.createVerticalStrut(12));
        addRow(fields, "비밀번호",     tfPw,       "6자 이상 필수");
        fields.add(Box.createVerticalStrut(12));
        addRow(fields, "닉네임",       tfNickname, "커뮤니티에서 표시될 이름");
        fields.add(Box.createVerticalStrut(12));
        addRow(fields, "자취 경력(년)", tfExpYear, "0 이상의 정수 입력");

        JLabel note = new JLabel("신규 가입 시 100 포인트가 지급됩니다.");
        note.setFont(UITheme.FONT_SMALL);
        note.setForeground(UITheme.ACCENT);
        note.setAlignmentX(CENTER_ALIGNMENT);

        JButton btnReg  = UITheme.btnSuccess("회원가입");
        JButton btnBack = UITheme.btnSecondary("로그인으로 돌아가기");
        btnReg .setAlignmentX(CENTER_ALIGNMENT);
        btnBack.setAlignmentX(CENTER_ALIGNMENT);
        btnReg .setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnBack.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        btnReg .addActionListener(this::doRegister);
        btnBack.addActionListener(e -> frame.showLogin());

        card.add(title);
        card.add(Box.createVerticalStrut(4));
        card.add(note);
        card.add(fields);
        card.add(btnReg);
        card.add(Box.createVerticalStrut(10));
        card.add(btnBack);

        add(card);
    }

    private void addRow(JPanel parent, String label, JComponent field, String hint) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);

        JLabel lbl = new JLabel(label);
        lbl.setFont(UITheme.FONT_H3);
        lbl.setForeground(UITheme.TEXT_DARK);
        lbl.setPreferredSize(new Dimension(100, 30));

        JPanel right = new JPanel(new BorderLayout(0, 2));
        right.setOpaque(false);
        right.add(field, BorderLayout.CENTER);
        JLabel hintLbl = new JLabel("  " + hint);
        hintLbl.setFont(UITheme.FONT_SMALL);
        hintLbl.setForeground(UITheme.TEXT_MUTED);
        right.add(hintLbl, BorderLayout.SOUTH);

        row.add(lbl,   BorderLayout.WEST);
        row.add(right, BorderLayout.CENTER);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        parent.add(row);
    }

    private void doRegister(ActionEvent e) {
        String userId  = tfId.getText().trim();
        String pw      = new String(tfPw.getPassword());
        String nick    = tfNickname.getText().trim();
        String expYear = tfExpYear.getText().trim();

        String result = SystemManager.getInstance().register(userId, pw, nick, expYear);
        if (result.startsWith("ERROR:")) {
            UITheme.showError(frame, result.substring(6));
        } else {
            UITheme.showInfo(frame,
                "회원가입이 완료되었습니다!\n아이디: " + userId + "\n100 포인트가 지급되었습니다.",
                "회원가입 완료");
            clearFields();
            frame.showLogin();
        }
    }

    private void clearFields() {
        tfId.setText("");
        tfPw.setText("");
        tfNickname.setText("");
        tfExpYear.setText("");
    }
}
