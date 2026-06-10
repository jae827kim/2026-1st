package kliving.gui;

import kliving.manager.SystemManager;
import kliving.model.Answer;
import kliving.model.Post;
import kliving.model.PointTransaction;
import kliving.model.User;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

/**
 * Shows the full detail of one post together with its answers.
 * Handles UC-05 (Answer Selection) and UC-12 (Answer Registration).
 */
public class PostDetailPanel extends JPanel {

    private final MainFrame frame;
    private String          currentPostId;

    // ── Post info area ────────────────────────────────────────────────────
    private final JLabel    lblTitle;
    private final JLabel    lblMeta;
    private final JTextArea taContent;

    // ── Answers area ──────────────────────────────────────────────────────
    private final JPanel    answersPanel;

    // ── Write-answer area ─────────────────────────────────────────────────
    private final JPanel    writePanel;
    private final JTextArea taAnswer;

    public PostDetailPanel(MainFrame frame) {
        this.frame = frame;
        setBackground(UITheme.BG);
        setLayout(new BorderLayout());

        // ── Top header ────────────────────────────────────────────────────
        JPanel header = UITheme.header("질문 상세보기", "K-Living Solution");
        add(header, BorderLayout.NORTH);

        // ── Toolbar ───────────────────────────────────────────────────────
        JButton btnBack = UITheme.btnSecondary("← 목록으로");
        btnBack.addActionListener(e -> frame.showPostList());
        JPanel tb = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        tb.setBackground(UITheme.BG);
        tb.add(btnBack);
        // Re-stack header + toolbar
        JPanel top = new JPanel(new BorderLayout());
        top.add(header, BorderLayout.NORTH);
        top.add(tb,     BorderLayout.SOUTH);
        remove(header);
        add(top, BorderLayout.NORTH);

        // ── Scrollable main body ──────────────────────────────────────────
        JPanel body = new JPanel();
        body.setBackground(UITheme.BG);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(BorderFactory.createEmptyBorder(8, 16, 16, 16));

        // -- Post card
        JPanel postCard = new JPanel(new BorderLayout(0, 8));
        postCard.setBackground(UITheme.CARD_BG);
        postCard.setBorder(UITheme.cardBorder());
        postCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        lblTitle = new JLabel();
        lblTitle.setFont(UITheme.FONT_H2);
        lblTitle.setForeground(UITheme.PRIMARY);

        lblMeta = new JLabel();
        lblMeta.setFont(UITheme.FONT_SMALL);
        lblMeta.setForeground(UITheme.TEXT_MUTED);

        taContent = UITheme.textArea(6, 60);
        taContent.setEditable(false);
        taContent.setBackground(new Color(248, 249, 252));
        JScrollPane contentScroll = new JScrollPane(taContent);
        contentScroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER_CLR));
        contentScroll.setPreferredSize(new Dimension(0, 120));

        JPanel postTop = new JPanel(new BorderLayout(8, 4));
        postTop.setOpaque(false);
        postTop.add(lblTitle, BorderLayout.CENTER);
        postTop.add(lblMeta,  BorderLayout.SOUTH);

        postCard.add(postTop,       BorderLayout.NORTH);
        postCard.add(contentScroll, BorderLayout.CENTER);

        // -- Answers label
        JLabel lblAnswers = new JLabel("💬  등록된 답변");
        lblAnswers.setFont(UITheme.FONT_H2);
        lblAnswers.setForeground(UITheme.PRIMARY);
        lblAnswers.setBorder(BorderFactory.createEmptyBorder(16, 0, 6, 0));
        lblAnswers.setAlignmentX(LEFT_ALIGNMENT);

        // -- Answers container
        answersPanel = new JPanel();
        answersPanel.setBackground(UITheme.BG);
        answersPanel.setLayout(new BoxLayout(answersPanel, BoxLayout.Y_AXIS));
        answersPanel.setAlignmentX(LEFT_ALIGNMENT);

        // -- Write-answer panel
        writePanel = new JPanel(new BorderLayout(0, 6));
        writePanel.setBackground(UITheme.CARD_BG);
        writePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(UITheme.ACCENT),
            "✏️  답변 작성",
            TitledBorder.LEFT, TitledBorder.TOP,
            UITheme.FONT_H3, UITheme.ACCENT));
        writePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
        writePanel.setAlignmentX(LEFT_ALIGNMENT);

        taAnswer = UITheme.textArea(4, 60);
        JScrollPane answerScroll = new JScrollPane(taAnswer);
        answerScroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER_CLR));

        JButton btnSubmitAnswer = UITheme.btnSuccess("답변 등록");
        btnSubmitAnswer.addActionListener(e -> submitAnswer());
        JPanel writeBtnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 4));
        writeBtnRow.setOpaque(false);
        writeBtnRow.add(btnSubmitAnswer);

        writePanel.add(answerScroll, BorderLayout.CENTER);
        writePanel.add(writeBtnRow,  BorderLayout.SOUTH);

        body.add(postCard);
        body.add(lblAnswers);
        body.add(answersPanel);
        body.add(Box.createVerticalStrut(12));
        body.add(writePanel);

        JScrollPane mainScroll = new JScrollPane(body);
        mainScroll.setBorder(null);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);
        add(mainScroll, BorderLayout.CENTER);
    }

    /** Sets the current post and re-renders all content. */
    public void setPost(String postId) {
        this.currentPostId = postId;
        refresh();
    }

    private void refresh() {
        SystemManager sm   = SystemManager.getInstance();
        Post          post = sm.findPost(currentPostId);
        if (post == null) { frame.showPostList(); return; }

        User   writer     = sm.findUser(post.getWriterId());
        String writerName = writer != null
            ? writer.getNickname() + " [" + writer.getUserLabel() + "]"
            : post.getWriterId();

        String status = post.isAdopted() ? "채택 완료" : "답변 모집 중";

        lblTitle.setText(post.getTitle());
        lblMeta.setText(String.format(
            "카테고리: %s  |  작성자: %s  |  보상: %d P  |  상태: %s  |  답변: %d개",
            post.getCategory(), writerName, post.getRewardPoint(),
            status, post.getActiveAnswerCount()));
        taContent.setText(post.getContent());
        taContent.setCaretPosition(0);

        // ── Rebuild answers list ──────────────────────────────────────────
        answersPanel.removeAll();

        User   loginUser = sm.getLoginUser();
        boolean isAuthor  = loginUser != null && loginUser.getUserId().equals(post.getWriterId());
        boolean canAdopt  = isAuthor && !post.isAdopted();

        List<Answer> answers = post.getAnswers();
        if (answers.isEmpty()) {
            JLabel empty = new JLabel("  아직 등록된 답변이 없습니다.");
            empty.setFont(UITheme.FONT_BODY);
            empty.setForeground(UITheme.TEXT_MUTED);
            answersPanel.add(empty);
        }

        for (Answer a : answers) {
            if (a.isDeleted()) continue;
            answersPanel.add(buildAnswerCard(a, post, canAdopt));
            answersPanel.add(Box.createVerticalStrut(8));
        }

        // ── Show / hide write panel ───────────────────────────────────────
        boolean canWrite = loginUser != null && !isAuthor && !post.isAdopted() && !post.isDeleted();
        writePanel.setVisible(canWrite);
        taAnswer.setText("");

        answersPanel.revalidate();
        answersPanel.repaint();
        revalidate();
        repaint();
    }

    private JPanel buildAnswerCard(Answer a, Post post, boolean canAdopt) {
        SystemManager sm         = SystemManager.getInstance();
        User          writer     = sm.findUser(a.getWriterId());
        String        writerName = writer != null
            ? writer.getNickname() + " [" + writer.getUserLabel() + "]  " + writer.getGrade()
            : a.getWriterId();

        JPanel card = new JPanel(new BorderLayout(0, 6));
        card.setBackground(a.isSelected() ? new Color(240, 255, 245) : UITheme.CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(a.isSelected() ? UITheme.SUCCESS : UITheme.BORDER_CLR, 1, true),
            BorderFactory.createEmptyBorder(10, 14, 10, 14)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        card.setAlignmentX(LEFT_ALIGNMENT);

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);

        JLabel lblWriter = new JLabel("👤  " + writerName);
        lblWriter.setFont(UITheme.FONT_H3);
        lblWriter.setForeground(UITheme.TEXT_DARK);

        topRow.add(lblWriter, BorderLayout.WEST);

        if (a.isSelected()) {
            JLabel adopted = UITheme.badge("✔ 채택된 답변", UITheme.SUCCESS);
            topRow.add(adopted, BorderLayout.EAST);
        } else if (canAdopt) {
            JButton btnAdopt = UITheme.btnSuccess("채택");
            btnAdopt.addActionListener(e -> adoptAnswer(post.getPostId(), a.getAnswerId()));
            topRow.add(btnAdopt, BorderLayout.EAST);
        }

        JTextArea taContent = UITheme.textArea(0, 0);
        taContent.setText(a.getContent());
        taContent.setEditable(false);
        taContent.setBackground(card.getBackground());
        taContent.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));

        card.add(topRow,    BorderLayout.NORTH);
        card.add(taContent, BorderLayout.CENTER);
        return card;
    }

    private void submitAnswer() {
        String content = taAnswer.getText().trim();
        String result  = SystemManager.getInstance().writeAnswer(currentPostId, content);
        if (result.startsWith("ERROR:")) {
            UITheme.showError(frame, result.substring(6));
        } else {
            UITheme.showInfo(frame, "답변이 등록되었습니다.", "답변 등록 완료");
            refresh();
        }
    }

    private void adoptAnswer(String postId, String answerId) {
        int choice = JOptionPane.showConfirmDialog(frame,
            "이 답변을 채택하시겠습니까?\n채택 후에는 변경할 수 없습니다.",
            "답변 채택", JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION) return;

        PointTransaction tx = SystemManager.getInstance().selectAnswer(postId, answerId);

        // UC-06 Transaction Result
        if (tx.isSuccess()) {
            UITheme.showInfo(frame, tx.getResultMessage(), "포인트 정산 완료");
        } else {
            UITheme.showError(frame, tx.getResultMessage());
        }
        refresh();
    }
}
