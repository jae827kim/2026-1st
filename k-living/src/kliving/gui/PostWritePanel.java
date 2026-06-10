package kliving.gui;

import kliving.manager.SystemManager;
import kliving.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/** UC-04 : Write a new question post and escrow the reward points. */
public class PostWritePanel extends JPanel {

    private static final String[] CATEGORIES = {
        "계약/관리", "청소/위생", "요리/식재료", "생활용품", "안전/응급", "기타"
    };

    private final MainFrame    frame;
    private final JTextField   tfTitle;
    private final JComboBox<String> cbCategory;
    private final JTextArea    taContent;
    private final JTextField   tfReward;
    private final JLabel       lblPointInfo;

    public PostWritePanel(MainFrame frame) {
        this.frame = frame;
        setBackground(UITheme.BG);
        setLayout(new BorderLayout());

        // ── Header ────────────────────────────────────────────────────────
        JPanel header = UITheme.header("질문 등록", "K-Living Solution");

        JButton btnCancel = UITheme.btnSecondary("취소");
        btnCancel.addActionListener(e -> frame.showPostList());
        JPanel tb = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        tb.setBackground(UITheme.BG);
        tb.add(btnCancel);

        JPanel top = new JPanel(new BorderLayout());
        top.add(header, BorderLayout.NORTH);
        top.add(tb,     BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);

        // ── Form card ─────────────────────────────────────────────────────
        JPanel card = new JPanel();
        card.setBackground(UITheme.CARD_BG);
        card.setBorder(UITheme.cardBorder());
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        // Title
        tfTitle = UITheme.textField();
        addFormRow(card, "제목 *", tfTitle);
        card.add(Box.createVerticalStrut(12));

        // Category
        cbCategory = new JComboBox<>(CATEGORIES);
        cbCategory.setFont(UITheme.FONT_BODY);
        addFormRow(card, "카테고리", cbCategory);
        card.add(Box.createVerticalStrut(12));

        // Content
        JLabel lblContent = new JLabel("내용 *");
        lblContent.setFont(UITheme.FONT_H3);
        lblContent.setForeground(UITheme.TEXT_DARK);
        lblContent.setAlignmentX(LEFT_ALIGNMENT);
        card.add(lblContent);
        card.add(Box.createVerticalStrut(4));

        taContent = UITheme.textArea(10, 60);
        JScrollPane contentScroll = new JScrollPane(taContent);
        contentScroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER_CLR));
        contentScroll.setAlignmentX(LEFT_ALIGNMENT);
        contentScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
        card.add(contentScroll);
        card.add(Box.createVerticalStrut(14));

        // Reward points
        tfReward = UITheme.textField();
        tfReward.setPreferredSize(new Dimension(120, 32));
        addFormRow(card, "보상 포인트 *", tfReward);
        card.add(Box.createVerticalStrut(4));

        lblPointInfo = new JLabel();
        lblPointInfo.setFont(UITheme.FONT_SMALL);
        lblPointInfo.setForeground(UITheme.TEXT_MUTED);
        lblPointInfo.setAlignmentX(LEFT_ALIGNMENT);
        card.add(lblPointInfo);
        card.add(Box.createVerticalStrut(20));

        // Note
        JLabel note = new JLabel("* 질문 등록 시 보상 포인트가 즉시 차감됩니다. 답변 채택 시 선택한 답변자에게 지급됩니다.");
        note.setFont(UITheme.FONT_SMALL);
        note.setForeground(UITheme.WARNING);
        note.setAlignmentX(LEFT_ALIGNMENT);
        card.add(note);
        card.add(Box.createVerticalStrut(14));

        // Submit button
        JButton btnSubmit = UITheme.btnSuccess("질문 등록하기");
        btnSubmit.setAlignmentX(LEFT_ALIGNMENT);
        btnSubmit.addActionListener(this::doSubmit);
        card.add(btnSubmit);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(UITheme.BG);
        wrapper.setBorder(BorderFactory.createEmptyBorder(12, 16, 16, 16));
        wrapper.add(card, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(wrapper);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);
    }

    private void addFormRow(JPanel parent, String label, JComponent field) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        row.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(label);
        lbl.setFont(UITheme.FONT_H3);
        lbl.setForeground(UITheme.TEXT_DARK);
        lbl.setPreferredSize(new Dimension(110, 30));

        row.add(lbl,   BorderLayout.WEST);
        row.add(field, BorderLayout.CENTER);
        parent.add(row);
    }

    /** Called when this panel is shown — refreshes the point display. */
    public void reset() {
        tfTitle.setText("");
        taContent.setText("");
        tfReward.setText("");
        cbCategory.setSelectedIndex(0);

        User u = SystemManager.getInstance().getLoginUser();
        if (u != null)
            lblPointInfo.setText("현재 보유 포인트: " + u.getCurrentPoint() + " P");
    }

    private void doSubmit(ActionEvent e) {
        String title    = tfTitle.getText().trim();
        String content  = taContent.getText().trim();
        String category = cbCategory.getSelectedItem().toString();
        String reward   = tfReward.getText().trim();

        String result = SystemManager.getInstance().writePost(title, content, category, reward);
        if (result.startsWith("ERROR:")) {
            UITheme.showError(frame, result.substring(6));
        } else {
            String postId = result.replace("SUCCESS:", "");
            UITheme.showInfo(frame,
                "질문이 등록되었습니다!\n게시글 번호: " + postId + "\n보상 포인트: " + reward + " P 예치됨",
                "질문 등록 완료");
            frame.showPostList();
        }
    }
}
