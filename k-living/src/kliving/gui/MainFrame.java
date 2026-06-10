package kliving.gui;

import kliving.manager.SystemManager;

import javax.swing.*;
import java.awt.*;

/**
 * Root JFrame with a CardLayout that hosts every screen panel.
 * All navigation flows through the showXXX() methods here.
 */
public class MainFrame extends JFrame {

    // ── Card names ────────────────────────────────────────────────────────
    static final String CARD_LOGIN       = "LOGIN";
    static final String CARD_REGISTER    = "REGISTER";
    static final String CARD_HOME        = "HOME";
    static final String CARD_POST_LIST   = "POST_LIST";
    static final String CARD_POST_DETAIL = "POST_DETAIL";
    static final String CARD_POST_WRITE  = "POST_WRITE";
    static final String CARD_PROFILE     = "PROFILE";
    static final String CARD_ADMIN       = "ADMIN";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel     cardPanel  = new JPanel(cardLayout);

    // ── Panels (lazy-init avoids forward-reference issues) ────────────────
    private final LoginPanel      loginPanel;
    private final RegisterPanel   registerPanel;
    private final HomePanel       homePanel;
    private final PostListPanel   postListPanel;
    private final PostDetailPanel postDetailPanel;
    private final PostWritePanel  postWritePanel;
    private final ProfilePanel    profilePanel;
    private final AdminPanel      adminPanel;

    public MainFrame() {
        setTitle("K-Living Solution  —  Reward-based Knowledge Sharing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(960, 680);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);

        // Initialise panels
        loginPanel      = new LoginPanel(this);
        registerPanel   = new RegisterPanel(this);
        homePanel       = new HomePanel(this);
        postListPanel   = new PostListPanel(this);
        postDetailPanel = new PostDetailPanel(this);
        postWritePanel  = new PostWritePanel(this);
        profilePanel    = new ProfilePanel(this);
        adminPanel      = new AdminPanel(this);

        cardPanel.add(loginPanel,      CARD_LOGIN);
        cardPanel.add(registerPanel,   CARD_REGISTER);
        cardPanel.add(homePanel,       CARD_HOME);
        cardPanel.add(postListPanel,   CARD_POST_LIST);
        cardPanel.add(postDetailPanel, CARD_POST_DETAIL);
        cardPanel.add(postWritePanel,  CARD_POST_WRITE);
        cardPanel.add(profilePanel,    CARD_PROFILE);
        cardPanel.add(adminPanel,      CARD_ADMIN);

        add(cardPanel);
        setVisible(true);
    }

    // ── Navigation helpers ────────────────────────────────────────────────

    public void showLogin() {
        cardLayout.show(cardPanel, CARD_LOGIN);
    }

    public void showRegister() {
        cardLayout.show(cardPanel, CARD_REGISTER);
    }

    public void showHome() {
        homePanel.refresh();
        cardLayout.show(cardPanel, CARD_HOME);
    }

    public void showPostList() {
        postListPanel.refresh();
        cardLayout.show(cardPanel, CARD_POST_LIST);
    }

    public void showPostDetail(String postId) {
        postDetailPanel.setPost(postId);
        cardLayout.show(cardPanel, CARD_POST_DETAIL);
    }

    public void showPostWrite() {
        postWritePanel.reset();
        cardLayout.show(cardPanel, CARD_POST_WRITE);
    }

    public void showProfile() {
        profilePanel.refresh();
        cardLayout.show(cardPanel, CARD_PROFILE);
    }

    public void showAdmin() {
        if (!SystemManager.getInstance().isAdmin()) {
            UITheme.showError(this, "관리자 권한이 필요합니다.");
            return;
        }
        adminPanel.refresh();
        cardLayout.show(cardPanel, CARD_ADMIN);
    }
}
