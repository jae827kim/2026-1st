package kliving.model;

import kliving.util.DataUtil;

import java.util.ArrayList;
import java.util.List;

public class Post {
    private String       postId;
    private String       title;
    private String       content;
    private String       category;
    private int          rewardPoint;
    private String       writerId;
    private boolean      adopted;
    private String       selectedAnswerId;   // null when not yet adopted
    private boolean      deleted;
    private List<Answer> answers;

    public Post(String postId, String title, String content, String category,
                int rewardPoint, String writerId,
                boolean adopted, String selectedAnswerId, boolean deleted) {
        this.postId           = postId;
        this.title            = title;
        this.content          = content;
        this.category         = category;
        this.rewardPoint      = rewardPoint;
        this.writerId         = writerId;
        this.adopted          = adopted;
        this.selectedAnswerId = selectedAnswerId;
        this.deleted          = deleted;
        this.answers          = new ArrayList<>();
    }

    // ── Getters ────────────────────────────────────────────────────────────
    public String       getPostId()           { return postId; }
    public String       getTitle()            { return title; }
    public String       getContent()          { return content; }
    public String       getCategory()         { return category; }
    public int          getRewardPoint()      { return rewardPoint; }
    public String       getWriterId()         { return writerId; }
    public boolean      isAdopted()           { return adopted; }
    public String       getSelectedAnswerId() { return selectedAnswerId; }
    public boolean      isDeleted()           { return deleted; }
    public List<Answer> getAnswers()          { return answers; }

    // ── Setters ────────────────────────────────────────────────────────────
    public void setAdopted(boolean adopted)                   { this.adopted           = adopted; }
    public void setSelectedAnswerId(String selectedAnswerId)  { this.selectedAnswerId  = selectedAnswerId; }
    public void setDeleted(boolean deleted)                   { this.deleted           = deleted; }

    public void addAnswer(Answer answer) { answers.add(answer); }

    /** Returns the number of non-deleted answers for display purposes. */
    public int getActiveAnswerCount() {
        int cnt = 0;
        for (Answer a : answers) if (!a.isDeleted()) cnt++;
        return cnt;
    }

    /**
     * Serialises the POST header line only.
     * Answer lines are written separately by FileManager.
     */
    public String toFileString() {
        return "POST"
             + "|" + DataUtil.encode(postId)
             + "|" + DataUtil.encode(title)
             + "|" + DataUtil.encode(content)
             + "|" + DataUtil.encode(category)
             + "|" + rewardPoint
             + "|" + DataUtil.encode(writerId)
             + "|" + adopted
             + "|" + (selectedAnswerId != null ? DataUtil.encode(selectedAnswerId) : "")
             + "|" + deleted;
    }
}
