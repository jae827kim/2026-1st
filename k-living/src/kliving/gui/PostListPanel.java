package kliving.gui;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import kliving.manager.SystemManager;
import kliving.model.Post;
import kliving.model.User;

/** Shows all active posts in a sortable table. (UC-04, UC-12 entry point) */
public class PostListPanel extends JPanel {

    private static final String[] COLS = { "번호", "카테고리", "제목", "작성자", "보상 (P)", "상태" };

    private final MainFrame        frame;
    private final DefaultTableModel tableModel;
    private final JTable           table;
    private final JLabel           lblCount;
    private final JButton          btnWrite;
    private final java.util.List<String> visiblePostIds;

    public PostListPanel(MainFrame frame) {
        this.frame = frame;
        this.visiblePostIds = new java.util.ArrayList<>();
        setBackground(UITheme.BG);
        setLayout(new BorderLayout());

        // ── Header ────────────────────────────────────────────────────────
        JPanel header = UITheme.header("질문 목록", "K-Living Solution");
        add(header, BorderLayout.NORTH);

        // ── Toolbar ───────────────────────────────────────────────────────
        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setBackground(UITheme.BG);
        toolbar.setBorder(BorderFactory.createEmptyBorder(10, 16, 6, 16));

        lblCount = new JLabel();
        lblCount.setFont(UITheme.FONT_BODY);
        lblCount.setForeground(UITheme.TEXT_MUTED);

        btnWrite         = UITheme.btnSuccess("✏️  질문 등록");
        JButton btnHome  = UITheme.btnSecondary("← 홈");
        JButton btnView  = UITheme.btnPrimary("📄  선택 게시글 보기");

        btnWrite.addActionListener(e -> frame.showPostWrite());
        btnHome .addActionListener(e -> frame.showHome());
        btnView .addActionListener(e -> viewSelected());

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        right.setOpaque(false);
        right.add(btnHome);
        right.add(btnView);
        right.add(btnWrite);

        toolbar.add(lblCount, BorderLayout.WEST);
        toolbar.add(right,    BorderLayout.EAST);
        add(toolbar, BorderLayout.NORTH);  // overrides header — re-add below

        // fix layout: header at top, toolbar below it
        remove(toolbar);
        JPanel top = new JPanel(new BorderLayout());
        top.add(header,  BorderLayout.NORTH);
        top.add(toolbar, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);

        // ── Table ─────────────────────────────────────────────────────────
        tableModel = new DefaultTableModel(COLS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel) {
            @Override
            protected JTableHeader createDefaultTableHeader() {
                JTableHeader header = new JTableHeader(columnModel);
                header.setReorderingAllowed(false);
                header.setDefaultRenderer(createHeaderRenderer());
                return header;
            }
        };
        styleTable();
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) viewSelected();
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 16, 16, 16));
        scroll.getViewport().setBackground(Color.WHITE);
        add(scroll, BorderLayout.CENTER);

        // ── Status info panel ─────────────────────────────────────────────
        JPanel legend = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 4));
        legend.setBackground(UITheme.BG);
        legend.setBorder(BorderFactory.createEmptyBorder(0, 16, 8, 16));
        legend.add(UITheme.badge("진행중", UITheme.ACCENT));
        JLabel lbl1 = new JLabel("= 답변 모집 중");
        lbl1.setFont(UITheme.FONT_SMALL.deriveFont(Font.PLAIN));
        legend.add(lbl1);
        legend.add(UITheme.badge("채택완료", UITheme.SUCCESS));
        JLabel lbl2 = new JLabel("= 답변 채택됨");
        lbl2.setFont(UITheme.FONT_SMALL.deriveFont(Font.PLAIN));
        legend.add(lbl2);
        add(legend, BorderLayout.SOUTH);
    }

    private void styleTable() {
        table.setFont(UITheme.FONT_BODY);
        table.setForeground(Color.BLACK);
        table.setBackground(Color.WHITE);
        table.setSelectionForeground(Color.BLACK);
        table.setRowHeight(30);
        table.getTableHeader().setFont(UITheme.FONT_H3);
        table.getTableHeader().setBackground(Color.WHITE);
        table.getTableHeader().setForeground(Color.BLACK);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setDefaultRenderer(createHeaderRenderer());

        // Force default cell text color to black on white rows.
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel,
                                                           boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, focus, row, col);
                if (!sel) {
                    setForeground(Color.BLACK);
                    setBackground(Color.WHITE);
                }
                return this;
            }
        });

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setGridColor(UITheme.BORDER_CLR);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));

        // Column widths
        int[] widths = { 60, 100, 280, 100, 80, 80 };
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
            table.getColumnModel().getColumn(i).setHeaderRenderer(createHeaderRenderer());
        }

        // Centre-align numeric / status columns
        DefaultTableCellRenderer centre = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel,
                                                           boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, focus, row, col);
                setHorizontalAlignment(SwingConstants.CENTER);
                if (!sel) {
                    setForeground(Color.BLACK);
                    setBackground(Color.WHITE);
                }
                return this;
            }
        };
        for (int i : new int[]{ 0, 1, 4, 5 })
            table.getColumnModel().getColumn(i).setCellRenderer(centre);

        // Colour the 상태 column
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel,
                                                           boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, focus, row, col);
                setHorizontalAlignment(SwingConstants.CENTER);
                String val = v == null ? "" : v.toString();
                if (val.equals("채택완료")) {
                    setForeground(UITheme.SUCCESS);
                    setFont(getFont().deriveFont(Font.BOLD));
                } else {
                    setForeground(UITheme.ACCENT);
                    setFont(getFont().deriveFont(Font.PLAIN));
                }
                return this;
            }
        });
    }

    private TableCellRenderer createHeaderRenderer() {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel,
                                                           boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, focus, row, col);
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
        tableModel.setRowCount(0);
        visiblePostIds.clear();
        SystemManager sm = SystemManager.getInstance();
        User loginUser = sm.getLoginUser();
        btnWrite.setVisible(loginUser != null && !loginUser.isAdmin());
        List<Post> posts = sm.getPostList();
        int shown = 0;
        for (int i = posts.size() - 1; i >= 0; i--) {
            Post p = posts.get(i);
            if (p.isDeleted()) continue;
            User writer = sm.findUser(p.getWriterId());
            String writerName = writer != null
                ? writer.getNickname() + " [" + writer.getUserLabel() + "]"
                : p.getWriterId();
            String status = p.isAdopted() ? "채택완료" : "진행중";
            visiblePostIds.add(p.getPostId());
            tableModel.addRow(new Object[]{
                parseDisplayPostNumber(p.getPostId()), p.getCategory(), p.getTitle(),
                writerName, p.getRewardPoint() + " P", status
            });
            shown++;
        }
        lblCount.setText("전체 " + shown + "개의 질문");
    }

    private void viewSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            UITheme.showError(frame, "게시글을 선택해 주세요.");
            return;
        }
        String postId = visiblePostIds.get(row);
        frame.showPostDetail(postId);
    }

    private String parseDisplayPostNumber(String postId) {
        if (postId != null && postId.matches("P\\d+")) {
            return postId.substring(1);
        }
        return postId;
    }
}
