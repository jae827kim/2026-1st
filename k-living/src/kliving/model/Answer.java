package kliving.model;

import kliving.util.DataUtil;

public class Answer {
    private String  answerId;
    private String  targetPostId;
    private String  writerId;
    private String  content;
    private boolean selected;
    private boolean deleted;

    public Answer(String answerId, String targetPostId, String writerId,
                  String content, boolean selected, boolean deleted) {
        this.answerId     = answerId;
        this.targetPostId = targetPostId;
        this.writerId     = writerId;
        this.content      = content;
        this.selected     = selected;
        this.deleted      = deleted;
    }

    // ── Getters ────────────────────────────────────────────────────────────
    public String  getAnswerId()     { return answerId; }
    public String  getTargetPostId() { return targetPostId; }
    public String  getWriterId()     { return writerId; }
    public String  getContent()      { return content; }
    public boolean isSelected()      { return selected; }
    public boolean isDeleted()       { return deleted; }

    // ── Setters ────────────────────────────────────────────────────────────
    public void setSelected(boolean selected) { this.selected = selected; }
    public void setDeleted(boolean deleted)   { this.deleted  = deleted; }

    /**
     * Serialises this answer to a single pipe-delimited line.
     * The "ANSWER" prefix distinguishes it from POST lines in Post.txt.
     */
    public String toFileString() {
        return "ANSWER"
             + "|" + DataUtil.encode(answerId)
             + "|" + DataUtil.encode(targetPostId)
             + "|" + DataUtil.encode(writerId)
             + "|" + DataUtil.encode(content)
             + "|" + selected
             + "|" + deleted;
    }
}
