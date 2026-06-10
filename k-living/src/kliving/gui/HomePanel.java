package kliving.gui;

import java.awt.*;
import javax.swing.*;
import kliving.manager.SystemManager;
import kliving.model.User;

/** Home / main-menu screen shown after a successful login. */
public class HomePanel extends JPanel {

    private final MainFrame frame;
    private final JLabel    lblWelcome;
    private final JLabel    lblInfo;
    private final JButton   btnProfile;
    private final JButton   btnWrite;
    private final JButton   btnAdmin;

    public HomePanel(MainFrame frame) {
        this.frame = frame;
        setBackground(UITheme.BG);
        setLayout(new BorderLayout());

        // ── Top header ────────────────────────────────────────────────────
        JPanel header = UITheme.header("K-Living Solution", "자취 생활 지식 공유 커뮤니티");
        add(header, BorderLayout.NORTH);

        // ── Centre card ───────────────────────────────────────────────────
        JPanel centre = new JPanel(new GridBagLayout());
        centre.setBackground(UITheme.BG);

        JPanel card = new JPanel();
        card.setBackground(UITheme.CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER_CLR),
            BorderFactory.createEmptyBorder(32, 48, 32, 48)));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(520, 430));

        lblWelcome = new JLabel();
        lblWelcome.setFont(UITheme.FONT_H2);
        lblWelcome.setForeground(UITheme.PRIMARY);
        lblWelcome.setAlignmentX(CENTER_ALIGNMENT);

        lblInfo = new JLabel();
        lblInfo.setFont(UITheme.FONT_BODY);
        lblInfo.setForeground(UITheme.TEXT_MUTED);
        lblInfo.setAlignmentX(CENTER_ALIGNMENT);

        JSeparator sep = UITheme.sep();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        // ── Navigation buttons ────────────────────────────────────────────
        JButton btnList    = UITheme.btnPrimary  ("📋  질문 목록 보기");
        btnWrite           = UITheme.btnSuccess  ("✏️   질문 등록하기");
        btnProfile         = UITheme.btnSecondary("👤  내 프로필");
        btnAdmin           = UITheme.btnWarning  ("🔧  관리자 페이지");
        JButton btnLogout  = UITheme.btnDanger   ("🚪  로그아웃");

        for (JButton b : new JButton[]{ btnList, btnWrite, btnProfile, btnAdmin, btnLogout }) {
            b.setAlignmentX(CENTER_ALIGNMENT);
            b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        }

        btnList   .addActionListener(e -> frame.showPostList());
        btnWrite  .addActionListener(e -> frame.showPostWrite());
        btnProfile.addActionListener(e -> frame.showProfile());
        btnAdmin  .addActionListener(e -> frame.showAdmin());
        btnLogout .addActionListener(e -> doLogout());

        card.add(lblWelcome);
        card.add(Box.createVerticalStrut(6));
        card.add(lblInfo);
        card.add(Box.createVerticalStrut(20));
        card.add(sep);
        card.add(Box.createVerticalStrut(20));
        card.add(btnList);
        card.add(Box.createVerticalStrut(10));
        card.add(btnWrite);
        card.add(Box.createVerticalStrut(10));
        card.add(btnProfile);
        card.add(Box.createVerticalStrut(10));
        card.add(btnAdmin);
        card.add(Box.createVerticalStrut(10));
        card.add(btnLogout);

        centre.add(card);
        add(centre, BorderLayout.CENTER);
    }

    /** Called every time this panel becomes visible to update user info. */
    public void refresh() {
        User u = SystemManager.getInstance().getLoginUser();
        if (u == null) return;

        lblWelcome.setText("안녕하세요, " + u.getNickname() + " 님!");
        if (u.isAdmin()) {
            lblInfo.setText("관리자 전용 화면");
        } else {
            lblInfo.setText(String.format(
                "[%s]  %s  |  현재 포인트: %d P  |  누적 포인트: %d P",
                u.getUserLabel(), u.getGrade(),
                u.getCurrentPoint(), u.getTotalPoint()));
        }

        // Admins do not directly participate in posting questions.
        btnWrite.setVisible(!u.isAdmin());
        btnProfile.setVisible(!u.isAdmin());

        // Show admin button only for admins
        btnAdmin.setVisible(u.isAdmin());
    }

    private void doLogout() {
        int choice = JOptionPane.showConfirmDialog(frame,
            "로그아웃 하시겠습니까?", "로그아웃", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            SystemManager.getInstance().logout();
            frame.showLogin();
        }
    }
}
