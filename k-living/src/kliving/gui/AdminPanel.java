package kliving.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import kliving.manager.SystemManager;
import kliving.model.Post;
import kliving.model.User;

/** UC-10 (System Statistics) + UC-11 (Data Control) for administrators. */
public class AdminPanel extends JPanel {

    private final MainFrame frame;

    // ── Statistics tab ────────────────────────────────────────────────────
    private final JTextArea taStats;

    // ── User control tab ──────────────────────────────────────────────────
    private static final String[] USER_COLS = { "아이디", "닉네임", "라벨", "등급", "현재 P", "누적 P", "상태" };
    private final DefaultTableModel userTableModel;
    private final JTable            userTable;

    // ── Post control tab ──────────────────────────────────────────────────
    private static final String[] POST_COLS = { "번호", "카테고리", "제목", "작성자", "보상 P", "상태" };
    private final DefaultTableModel postTableModel;
    private final JTable            postTable;

    public AdminPanel(MainFrame frame) {
        this.frame = frame;
        setBackground(UITheme.BG);
        setLayout(new BorderLayout());

        // ── Header ────────────────────────────────────────────────────────
        JPanel header = UITheme.header("관리자 페이지", "K-Living Solution");
        JButton btnHome = UITheme.btnSecondary("← 홈");
        btnHome.addActionListener(e -> frame.showHome());
        JPanel tb = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        tb.setBackground(UITheme.BG);
        tb.add(btnHome);
        JPanel top = new JPanel(new BorderLayout());
        top.add(header, BorderLayout.NORTH);
        top.add(tb,     BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);

        // ── Tabs ──────────────────────────────────────────────────────────
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(UITheme.FONT_H3);
        tabs.setBackground(UITheme.BG);

        // ────── Tab 1 : Statistics ─────────────────────────────────────────
        taStats = UITheme.textArea(20, 60);
        taStats.setEditable(false);
        taStats.setFont(UITheme.FONT_MONO);
        taStats.setBackground(new Color(248, 249, 252));
        JPanel statsPanel = new JPanel(new BorderLayout(0, 8));
        statsPanel.setBackground(UITheme.BG);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JButton btnRefreshStats = UITheme.btnPrimary("새로고침");
        btnRefreshStats.addActionListener(e -> loadStats());
        JPanel statsBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        statsBar.setOpaque(false);
        statsBar.add(btnRefreshStats);

        statsPanel.add(statsBar,           BorderLayout.NORTH);
        statsPanel.add(new JScrollPane(taStats), BorderLayout.CENTER);
        tabs.addTab("📊  시스템 통계", statsPanel);

        // ────── Tab 2 : User Management ────────────────────────────────────
        userTableModel = new DefaultTableModel(USER_COLS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        userTable = styledTable(userTableModel);

        JButton btnBlock   = UITheme.btnDanger  ("차단");
        JButton btnUnblock = UITheme.btnSuccess ("차단 해제");
        btnBlock  .addActionListener(e -> blockSelected());
        btnUnblock.addActionListener(e -> unblockSelected());

        JPanel userPanel = buildControlTab(userTable,
            new JButton[]{ btnBlock, btnUnblock },
            "사용자 선택 후 차단/해제 버튼을 누르세요.");
        tabs.addTab("👥  사용자 제어", userPanel);

        // ────── Tab 3 : Post Management ────────────────────────────────────
        postTableModel = new DefaultTableModel(POST_COLS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        postTable = styledTable(postTableModel);

        JButton btnDelPost = UITheme.btnDanger("게시글 삭제");
        btnDelPost.addActionListener(e -> deleteSelectedPost());

        JPanel postPanel = buildControlTab(postTable,
            new JButton[]{ btnDelPost },
            "게시글 선택 후 삭제 버튼을 누르세요.");
        tabs.addTab("📋  게시글 제어", postPanel);

        add(tabs, BorderLayout.CENTER);
    }

    private JPanel buildControlTab(JTable table, JButton[] buttons, String hint) {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(UITheme.BG);
        p.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(Color.WHITE);
        p.add(scroll, BorderLayout.CENTER);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        south.setOpaque(false);
        JLabel hintLabel = new JLabel(hint);
        hintLabel.setFont(UITheme.FONT_SMALL);
        hintLabel.setForeground(UITheme.TEXT_MUTED);
        south.add(hintLabel);
        for (JButton b : buttons) south.add(b);
        p.add(south, BorderLayout.SOUTH);
        return p;
    }

    private JTable styledTable(DefaultTableModel model) {
        JTable t = new JTable(model) {
            @Override
            protected JTableHeader createDefaultTableHeader() {
                JTableHeader header = new JTableHeader(columnModel);
                header.setReorderingAllowed(false);
                header.setDefaultRenderer(createHeaderRenderer());
                return header;
            }
        };
        t.setFont(UITheme.FONT_BODY);
        t.setForeground(Color.BLACK);
        t.setBackground(Color.WHITE);
        t.setSelectionForeground(Color.BLACK);
        t.setRowHeight(28);
        t.getTableHeader().setFont(UITheme.FONT_H3);
        t.getTableHeader().setBackground(Color.WHITE);
        t.getTableHeader().setForeground(Color.BLACK);
        t.getTableHeader().setReorderingAllowed(false);
        t.getTableHeader().setDefaultRenderer(createHeaderRenderer());

        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    setForeground(Color.BLACK);
                    setBackground(Color.WHITE);
                }
                return this;
            }
        });

        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.setGridColor(UITheme.BORDER_CLR);
        t.setShowGrid(true);
        t.setIntercellSpacing(new Dimension(1, 1));
        return t;
    }

    private TableCellRenderer createHeaderRenderer() {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);
                setBackground(Color.WHITE);
                setForeground(Color.BLACK);
                setFont(UITheme.FONT_H3);
                setBorder(UIManager.getBorder("TableHeader.cellBorder"));
                setOpaque(true);
                return this;
            }
        };
    }

    public void refresh() {
        loadStats();
        loadUsers();
        loadPosts();
    }

    private void loadStats() {
        taStats.setText(SystemManager.getInstance().getStatistics());
    }

    private void loadUsers() {
        userTableModel.setRowCount(0);
        for (User u : SystemManager.getInstance().getUserList()) {
            userTableModel.addRow(new Object[]{
                u.getUserId(), u.getNickname(), u.getUserLabel(),
                u.getGrade(), u.getCurrentPoint(), u.getTotalPoint(),
                u.isBlocked() ? "차단됨" : (u.isAdmin() ? "관리자" : "정상")
            });
        }
    }

    private void loadPosts() {
        postTableModel.setRowCount(0);
        SystemManager sm = SystemManager.getInstance();
        for (Post p : sm.getPostList()) {
            String status = p.isDeleted() ? "삭제됨"
                          : p.isAdopted() ? "채택완료" : "진행중";
            postTableModel.addRow(new Object[]{
                p.getPostId(), p.getCategory(), p.getTitle(),
                p.getWriterId(), p.getRewardPoint() + " P", status
            });
        }
    }

    private void blockSelected() {
        int row = userTable.getSelectedRow();
        if (row < 0) { UITheme.showError(frame, "사용자를 선택해 주세요."); return; }
        String userId = userTableModel.getValueAt(row, 0).toString();
        String result = SystemManager.getInstance().blockUser(userId);
        if (result.startsWith("ERROR:")) UITheme.showError(frame, result.substring(6));
        else { UITheme.showInfo(frame, "[" + userId + "] 사용자를 차단했습니다.", "차단 완료"); loadUsers(); }
    }

    private void unblockSelected() {
        int row = userTable.getSelectedRow();
        if (row < 0) { UITheme.showError(frame, "사용자를 선택해 주세요."); return; }
        String userId = userTableModel.getValueAt(row, 0).toString();
        String result = SystemManager.getInstance().unblockUser(userId);
        if (result.startsWith("ERROR:")) UITheme.showError(frame, result.substring(6));
        else { UITheme.showInfo(frame, "[" + userId + "] 차단을 해제했습니다.", "차단 해제 완료"); loadUsers(); }
    }

    private void deleteSelectedPost() {
        int row = postTable.getSelectedRow();
        if (row < 0) { UITheme.showError(frame, "게시글을 선택해 주세요."); return; }
        String postId = postTableModel.getValueAt(row, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(frame,
            "게시글 [" + postId + "] 을(를) 삭제하시겠습니까?", "게시글 삭제", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        String result = SystemManager.getInstance().deletePost(postId);
        if (result.startsWith("ERROR:")) UITheme.showError(frame, result.substring(6));
        else { UITheme.showInfo(frame, "게시글이 삭제되었습니다.", "삭제 완료"); loadPosts(); loadStats(); }
    }
}
