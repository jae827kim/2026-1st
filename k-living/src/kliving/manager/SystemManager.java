package kliving.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import kliving.model.*;

/**
 * Central controller for K-Living Solution.
 * Implements all business-logic use cases (UC-01 through UC-12).
 * Singleton – access via SystemManager.getInstance().
 */
public class SystemManager {

    private static SystemManager instance;

    private List<User>    userList;
    private List<Post>    postList;
    private List<Archive> archiveList;
    private User          loginUser;

    private final FileManager   fileManager;
    private final AtomicInteger postIdSeq;
    private final AtomicInteger answerIdSeq;
    private final AtomicInteger txIdSeq;

    private SystemManager() {
        fileManager = new FileManager();
        archiveList = new ArrayList<>();
        loadData();

        // Seed auto-increment counters from existing data
        int maxPost = 0, maxAnswer = 0;
        for (Post p : postList) {
            maxPost = Math.max(maxPost, parseSeq(p.getPostId(), "P"));
            for (Answer a : p.getAnswers())
                maxAnswer = Math.max(maxAnswer, parseSeq(a.getAnswerId(), "A"));
        }
        postIdSeq   = new AtomicInteger(maxPost);
        answerIdSeq = new AtomicInteger(maxAnswer);
        txIdSeq     = new AtomicInteger(0);
    }

    public static SystemManager getInstance() {
        if (instance == null) instance = new SystemManager();
        return instance;
    }

    // ══════════════════════════════════════════════════════════════════════
    // UC-07  Data Load
    // ══════════════════════════════════════════════════════════════════════

    public void loadData() {
        userList = fileManager.loadUsers();
        postList = fileManager.loadPosts();

        boolean normalized = false;
        for (User user : userList) {
            if (user.isAdmin()) {
                if (user.getCurrentPoint() != 0 || user.getTotalPoint() != 0) {
                    user.setCurrentPoint(0);
                    user.setTotalPoint(0);
                    LabelManager.updateLabel(user);
                    normalized = true;
                }
                continue;
            }
            if (user.getTotalPoint() < user.getCurrentPoint()) {
                user.setTotalPoint(user.getCurrentPoint());
                LabelManager.updateLabel(user);
                normalized = true;
            }
        }

        if (normalized) fileManager.saveUsers(userList);
    }

    // ══════════════════════════════════════════════════════════════════════
    // UC-01  Registration Info
    // ══════════════════════════════════════════════════════════════════════

    /** Returns "SUCCESS" or "ERROR:<message>". */
    public String register(String userId, String password,
                           String nickname, String expYearStr) {
        if (userId.isBlank() || password.isBlank() || nickname.isBlank() || expYearStr.isBlank())
            return "ERROR:필수 정보를 모두 입력해 주세요.";
        if (password.length() < 6)
            return "ERROR:비밀번호는 6자 이상이어야 합니다.";
        if (containsPipe(userId, password, nickname))
            return "ERROR:특수문자 '|' 는 사용할 수 없습니다.";

        int expYear;
        try {
            expYear = Integer.parseInt(expYearStr.trim());
            if (expYear < 0) return "ERROR:자취 경력은 0 이상의 숫자여야 합니다.";
        } catch (NumberFormatException e) {
            return "ERROR:자취 경력은 숫자로 입력해 주세요.";
        }

        for (User u : userList) {
            if (u.getUserId().equals(userId))
                return "ERROR:이미 사용 중인 아이디입니다.";
            if (u.getNickname().equals(nickname))
                return "ERROR:이미 사용 중인 닉네임입니다.";
        }

        User newUser = new User(userId, password, nickname, expYear, 100, 100, false, false);
        LabelManager.updateLabel(newUser);
        userList.add(newUser);

        if (!fileManager.saveUsers(userList)) {
            userList.remove(newUser);
            return "ERROR:회원가입 처리 중 오류가 발생했습니다.";
        }
        return "SUCCESS";
    }

    // ══════════════════════════════════════════════════════════════════════
    // UC-02  Login
    // ══════════════════════════════════════════════════════════════════════

    public String login(String userId, String password) {
        if (userId.isBlank() || password.isBlank())
            return "ERROR:아이디와 비밀번호를 입력해 주세요.";

        for (User u : userList) {
            if (u.getUserId().equals(userId)) {
                if (u.isBlocked())
                    return "ERROR:차단된 계정입니다. 관리자에게 문의하세요.";
                if (u.getPassword().equals(password)) {
                    loginUser = u;
                    LabelManager.updateLabel(loginUser);
                    return "SUCCESS";
                }
                return "ERROR:비밀번호가 일치하지 않습니다.";
            }
        }
        return "ERROR:등록되지 않은 아이디입니다.";
    }

    public void logout() { loginUser = null; }

    public User    getLoginUser() { return loginUser; }
    public boolean isLoggedIn()   { return loginUser != null; }
    public boolean isAdmin()      { return loginUser != null && loginUser.isAdmin(); }

    // ══════════════════════════════════════════════════════════════════════
    // UC-04  Post & Reward
    // ══════════════════════════════════════════════════════════════════════

    public String writePost(String title, String content,
                            String category, String rewardStr) {
        if (loginUser == null)          return "ERROR:로그인이 필요합니다.";
        if (title.isBlank())            return "ERROR:제목을 입력해 주세요.";
        if (content.isBlank())          return "ERROR:내용을 입력해 주세요.";
        if (containsPipe(title, category)) return "ERROR:특수문자 '|' 는 사용할 수 없습니다.";

        int reward;
        try {
            reward = Integer.parseInt(rewardStr.trim());
            if (reward <= 0) return "ERROR:보상 포인트는 1 이상이어야 합니다.";
        } catch (NumberFormatException e) {
            return "ERROR:보상 포인트를 올바르게 입력해 주세요.";
        }

        if (loginUser.getCurrentPoint() < reward)
            return "ERROR:포인트가 부족합니다. (현재: " + loginUser.getCurrentPoint() + " P)";

        int prevPoint = loginUser.getCurrentPoint();
        loginUser.setCurrentPoint(prevPoint - reward);

        String postId = "P" + postIdSeq.incrementAndGet();
        Post post = new Post(postId, title, content, category, reward,
                             loginUser.getUserId(), false, null, false);
        postList.add(post);

        if (!fileManager.savePosts(postList)) {
            loginUser.setCurrentPoint(prevPoint);
            postList.remove(post);
            postIdSeq.decrementAndGet();
            return "ERROR:질문 등록에 실패했습니다. 포인트를 복구합니다.";
        }
        if (!fileManager.saveUsers(userList)) {
            loginUser.setCurrentPoint(prevPoint);
            postList.remove(post);
            postIdSeq.decrementAndGet();
            fileManager.savePosts(postList);
            return "ERROR:포인트 갱신에 실패했습니다. 이전 상태로 복구합니다.";
        }
        return "SUCCESS:" + postId;
    }

    // ══════════════════════════════════════════════════════════════════════
    // UC-12  Answer Registration
    // ══════════════════════════════════════════════════════════════════════

    public String writeAnswer(String postId, String content) {
        if (loginUser == null)    return "ERROR:로그인이 필요합니다.";
        if (content.isBlank())    return "ERROR:답변 내용을 입력해 주세요.";
        if (content.length() > 2000) return "ERROR:답변은 2000자 이내로 작성해 주세요.";

        Post post = findPost(postId);
        if (post == null)         return "ERROR:게시글을 찾을 수 없습니다.";
        if (post.isAdopted())     return "ERROR:이미 답변이 채택된 질문입니다.";
        if (post.isDeleted())     return "ERROR:삭제된 게시글입니다.";
        if (post.getWriterId().equals(loginUser.getUserId()))
                                  return "ERROR:본인이 작성한 질문에는 답변할 수 없습니다.";

        String answerId = "A" + answerIdSeq.incrementAndGet();
        Answer answer = new Answer(answerId, postId, loginUser.getUserId(), content, false, false);
        post.addAnswer(answer);

        if (!fileManager.savePosts(postList)) {
            post.getAnswers().remove(answer);
            answerIdSeq.decrementAndGet();
            return "ERROR:답변 등록에 실패했습니다.";
        }
        return "SUCCESS";
    }

    // ══════════════════════════════════════════════════════════════════════
    // UC-05  Answer Selection  +  UC-06  Transaction Result
    // ══════════════════════════════════════════════════════════════════════

    public PointTransaction selectAnswer(String postId, String answerId) {
        String txId = "TX" + txIdSeq.incrementAndGet();
        PointTransaction tx = new PointTransaction(txId, postId,
                loginUser != null ? loginUser.getUserId() : "", "", 0);

        if (loginUser == null) { return fail(tx, "로그인이 필요합니다."); }

        Post post = findPost(postId);
        if (post == null)   { return fail(tx, "게시글을 찾을 수 없습니다."); }
        if (!post.getWriterId().equals(loginUser.getUserId()))
                            { return fail(tx, "답변 채택 권한이 없습니다."); }
        if (post.isAdopted()){ return fail(tx, "이미 답변이 채택된 게시글입니다."); }

        Answer target = null;
        for (Answer a : post.getAnswers())
            if (a.getAnswerId().equals(answerId) && !a.isDeleted()) { target = a; break; }

        if (target == null) { return fail(tx, "답변을 찾을 수 없습니다."); }

        User answerer = findUser(target.getWriterId());
        if (answerer == null) { return fail(tx, "답변자 정보를 찾을 수 없습니다."); }
        if (answerer.getUserId().equals(loginUser.getUserId())) {
            return fail(tx, "자신이 작성한 답변은 채택할 수 없습니다.");
        }

        // ── Apply transfer ────────────────────────────────────────────────
        int reward    = post.getRewardPoint();
        int prevCur   = answerer.getCurrentPoint();
        int prevTotal = answerer.getTotalPoint();

        answerer.setCurrentPoint(prevCur   + reward);
        answerer.setTotalPoint(prevTotal + reward);
        LabelManager.updateLabel(answerer);
        target.setSelected(true);
        post.setAdopted(true);
        post.setSelectedAnswerId(answerId);

        archiveList.add(new Archive(
            "AR_" + postId + "_" + answerId,
            postId, answerId, post.getCategory(), post.getTitle()
        ));

        if (!fileManager.savePosts(postList)) {
            rollbackSelection(answerer, prevCur, prevTotal, target, post);
            return fail(tx, "답변 채택 저장에 실패했습니다.");
        }
        if (!fileManager.saveUsers(userList)) {
            rollbackSelection(answerer, prevCur, prevTotal, target, post);
            fileManager.savePosts(postList);
            return fail(tx, "포인트 갱신에 실패했습니다.");
        }

        PointTransaction success = new PointTransaction(
            txId, postId, loginUser.getUserId(), answerer.getUserId(), reward);
        success.setStatus("SUCCESS");
        success.setResultMessage(String.format(
            "채택 완료!\n\n답변자: %s (%s)\n지급 포인트: +%d P\n답변자 현재 포인트: %d P\n답변자 누적 포인트: %d P\n답변자 등급: %s",
            answerer.getNickname(), answerer.getUserLabel(),
            reward, answerer.getCurrentPoint(),
            answerer.getTotalPoint(), answerer.getGrade()));
        return success;
    }

    private void rollbackSelection(User answerer, int prevCur, int prevTotal,
                                   Answer target, Post post) {
        answerer.setCurrentPoint(prevCur);
        answerer.setTotalPoint(prevTotal);
        LabelManager.updateLabel(answerer);
        target.setSelected(false);
        post.setAdopted(false);
        post.setSelectedAnswerId(null);
        if (!archiveList.isEmpty()) archiveList.remove(archiveList.size() - 1);
    }

    private PointTransaction fail(PointTransaction tx, String msg) {
        tx.setStatus("FAILED");
        tx.setResultMessage(msg);
        return tx;
    }

    // ══════════════════════════════════════════════════════════════════════
    // UC-10  System Statistics
    // ══════════════════════════════════════════════════════════════════════

    public String getStatistics() {
        int totalUsers = userList.size();
        int blockedUsers = 0;
        int totalCurrent = 0;
        int totalAccumulated = 0;
        for (User u : userList) {
            if (u.isBlocked()) blockedUsers++;
            totalCurrent     += u.getCurrentPoint();
            totalAccumulated += u.getTotalPoint();
        }

        int totalPosts = 0, totalAnswers = 0, adoptedPosts = 0, circulation = 0;
        for (Post p : postList) {
            if (p.isDeleted()) continue;
            totalPosts++;
            if (p.isAdopted()) { adoptedPosts++; circulation += p.getRewardPoint(); }
            for (Answer a : p.getAnswers())
                if (!a.isDeleted()) totalAnswers++;
        }

        return "=== K-Living Solution 시스템 통계 ===\n\n"
             + String.format("전체 사용자 수       : %d 명%n", totalUsers)
             + String.format("차단된 사용자 수     : %d 명%n", blockedUsers)
             + String.format("전체 게시글 수       : %d 개%n", totalPosts)
             + String.format("전체 답변 수         : %d 개%n", totalAnswers)
             + String.format("채택 완료 질문 수    : %d 개%n", adoptedPosts)
             + String.format("미해결 질문 수       : %d 개%n", totalPosts - adoptedPosts)
             + String.format("총 포인트 유통량     : %d P%n", circulation)
             + String.format("시스템 현재 포인트 합: %d P%n", totalCurrent);
    }

    // ══════════════════════════════════════════════════════════════════════
    // UC-11  Data Control
    // ══════════════════════════════════════════════════════════════════════

    public String blockUser(String userId) {
        if (!isAdmin()) return "ERROR:관리자 권한이 필요합니다.";
        User t = findUser(userId);
        if (t == null)      return "ERROR:사용자를 찾을 수 없습니다.";
        if (t.isAdmin())    return "ERROR:관리자 계정은 차단할 수 없습니다.";
        if (t.isBlocked())  return "ERROR:이미 차단된 사용자입니다.";
        t.setBlocked(true);
        if (!fileManager.saveUsers(userList)) { t.setBlocked(false); return "ERROR:차단 처리에 실패했습니다."; }
        return "SUCCESS";
    }

    public String unblockUser(String userId) {
        if (!isAdmin()) return "ERROR:관리자 권한이 필요합니다.";
        User t = findUser(userId);
        if (t == null)       return "ERROR:사용자를 찾을 수 없습니다.";
        if (!t.isBlocked())  return "ERROR:차단되지 않은 사용자입니다.";
        t.setBlocked(false);
        if (!fileManager.saveUsers(userList)) { t.setBlocked(true); return "ERROR:차단 해제에 실패했습니다."; }
        return "SUCCESS";
    }

    public String deletePost(String postId) {
        if (!isAdmin()) return "ERROR:관리자 권한이 필요합니다.";
        Post p = findPost(postId);
        if (p == null)    return "ERROR:게시글을 찾을 수 없습니다.";
        if (p.isDeleted())return "ERROR:이미 삭제된 게시글입니다.";
        p.setDeleted(true);
        if (!fileManager.savePosts(postList)) { p.setDeleted(false); return "ERROR:게시글 삭제에 실패했습니다."; }
        return "SUCCESS";
    }

    public String deleteAnswer(String postId, String answerId) {
        if (!isAdmin()) return "ERROR:관리자 권한이 필요합니다.";
        Post post = findPost(postId);
        if (post == null) return "ERROR:게시글을 찾을 수 없습니다.";
        for (Answer a : post.getAnswers()) {
            if (a.getAnswerId().equals(answerId)) {
                if (a.isDeleted()) return "ERROR:이미 삭제된 답변입니다.";
                a.setDeleted(true);
                if (!fileManager.savePosts(postList)) { a.setDeleted(false); return "ERROR:답변 삭제에 실패했습니다."; }
                return "SUCCESS";
            }
        }
        return "ERROR:답변을 찾을 수 없습니다.";
    }

    // ══════════════════════════════════════════════════════════════════════
    // Helpers
    // ══════════════════════════════════════════════════════════════════════

    public User findUser(String userId) {
        for (User u : userList) if (u.getUserId().equals(userId)) return u;
        return null;
    }

    public Post findPost(String postId) {
        for (Post p : postList) if (p.getPostId().equals(postId)) return p;
        return null;
    }

    public List<User>    getUserList()    { return userList; }
    public List<Post>    getPostList()    { return postList; }
    public List<Archive> getArchiveList() { return archiveList; }

    private int parseSeq(String id, String prefix) {
        try { return Integer.parseInt(id.replace(prefix, "")); }
        catch (Exception e) { return 0; }
    }

    private boolean containsPipe(String... values) {
        for (String v : values) if (v != null && v.contains("|")) return true;
        return false;
    }
}
