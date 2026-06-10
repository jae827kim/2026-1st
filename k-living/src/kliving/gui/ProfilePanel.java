package kliving.gui;

import kliving.manager.SystemManager;
import kliving.model.Post;
import kliving.model.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/** UC-03 : Show the logged-in user's profile with label and grade. */
public class ProfilePanel extends JPanel {

    private final MainFrame frame;
    private final JLabel lblNickname;
    private final JLabel lblLabel;
    private final JLabel lblGrade;
    private final JLabel lblExp;
    private final JLabel lblCurrentPt;
    private final JLabel lblTotalPt;
    private final JLabel lblPostCount;
    private final JLabel lblAnswerCount;

    public ProfilePanel(MainFrame frame) {
        this.frame = frame;
        setBackground(UITheme.BG);
        setLayout(new BorderLayout());

        // ── Header ────────────────────────────────────────────────────────
        JPanel header = UITheme.header("내 프로필", "K-Living Solution");
        JButton btnBack = UITheme.btnSecondary("← 홈");
        btnBack.addActionListener(e -> frame.showHome());
        JPanel tb = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        tb.setBackground(UITheme.BG);
        tb.add(btnBack);
        JPanel top = new JPanel(new BorderLayout());
        top.add(header, BorderLayout.NORTH);
        top.add(tb,     BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);

        // ── Centre card ───────────────────────────────────────────────────
        JPanel centrePanel = new JPanel(new GridBagLayout());
        centrePanel.setBackground(UITheme.BG);

        JPanel card = new JPanel();
        card.setBackground(UITheme.CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER_CLR),
            BorderFactory.createEmptyBorder(32, 48, 32, 48)));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(500, 440));

        // Avatar placeholder
        JLabel avatar = new JLabel("👤");
        avatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 54));
        avatar.setAlignmentX(CENTER_ALIGNMENT);

        lblNickname = bigLabel("", UITheme.FONT_TITLE, UITheme.PRIMARY);
        lblLabel    = bigLabel("", UITheme.FONT_H3,    UITheme.ACCENT);
        lblGrade    = bigLabel("", UITheme.FONT_H3,    UITheme.WARNING);

        card.add(avatar);
        card.add(Box.createVerticalStrut(10));
        card.add(lblNickname);
        card.add(Box.createVerticalStrut(4));
        card.add(lblLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(lblGrade);
        card.add(Box.createVerticalStrut(20));
        card.add(UITheme.sep());
        card.add(Box.createVerticalStrut(16));

        // ── Stats grid ────────────────────────────────────────────────────
        lblExp         = infoLabel("");
        lblCurrentPt   = infoLabel("");
        lblTotalPt     = infoLabel("");
        lblPostCount   = infoLabel("");
        lblAnswerCount = infoLabel("");

        card.add(statRow("자취 경력",     lblExp));
        card.add(Box.createVerticalStrut(10));
        card.add(statRow("현재 포인트",   lblCurrentPt));
        card.add(Box.createVerticalStrut(10));
        card.add(statRow("누적 포인트",   lblTotalPt));
        card.add(Box.createVerticalStrut(10));
        card.add(statRow("등록한 질문",   lblPostCount));
        card.add(Box.createVerticalStrut(10));
        card.add(statRow("등록한 답변",   lblAnswerCount));

        centrePanel.add(card);
        add(centrePanel, BorderLayout.CENTER);
    }

    private JLabel bigLabel(String text, Font font, Color color) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font);
        lbl.setForeground(color);
        lbl.setAlignmentX(CENTER_ALIGNMENT);
        return lbl;
    }

    private JLabel infoLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(UITheme.FONT_H3);
        lbl.setForeground(UITheme.PRIMARY);
        return lbl;
    }

    private JPanel statRow(String caption, JLabel valueLabel) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        JLabel cap = new JLabel(caption);
        cap.setFont(UITheme.FONT_BODY);
        cap.setForeground(UITheme.TEXT_MUTED);
        cap.setPreferredSize(new Dimension(120, 24));
        row.add(cap,        BorderLayout.WEST);
        row.add(valueLabel, BorderLayout.CENTER);
        return row;
    }

    public void refresh() {
        SystemManager sm = SystemManager.getInstance();
        User u = sm.getLoginUser();
        if (u == null) return;

        lblNickname.setText(u.getNickname());
        lblLabel   .setText("[" + u.getUserLabel() + "]");
        lblGrade   .setText(u.getGrade());
        lblExp     .setText(u.getExperienceYear() + " 년");
        lblCurrentPt.setText(u.getCurrentPoint() + " P");
        lblTotalPt .setText(u.getTotalPoint()   + " P");

        // Count this user's posts and answers
        List<Post> posts = sm.getPostList();
        int myPosts = 0, myAnswers = 0;
        for (Post p : posts) {
            if (p.isDeleted()) continue;
            if (p.getWriterId().equals(u.getUserId())) myPosts++;
            myAnswers += p.getAnswers().stream()
                .filter(a -> !a.isDeleted() && a.getWriterId().equals(u.getUserId()))
                .count();
        }
        lblPostCount  .setText(myPosts   + " 개");
        lblAnswerCount.setText(myAnswers + " 개");
    }
}
