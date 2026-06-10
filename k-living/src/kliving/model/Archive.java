package kliving.model;

/** Stores an adopted Q&A pair so other users can search and reuse it later. */
public class Archive {
    private String archiveId;
    private String postId;
    private String answerId;
    private String category;
    private String keyword;

    public Archive(String archiveId, String postId, String answerId,
                   String category, String keyword) {
        this.archiveId = archiveId;
        this.postId    = postId;
        this.answerId  = answerId;
        this.category  = category;
        this.keyword   = keyword;
    }

    public String getArchiveId() { return archiveId; }
    public String getPostId()    { return postId; }
    public String getAnswerId()  { return answerId; }
    public String getCategory()  { return category; }
    public String getKeyword()   { return keyword; }
}
