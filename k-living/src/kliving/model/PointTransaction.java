package kliving.model;

/** Represents the result of a point transfer between a questioner and an answerer. */
public class PointTransaction {
    private String transactionId;
    private String postId;
    private String questionerId;
    private String answererId;
    private int    pointAmount;
    private String status;        // "SUCCESS" | "FAILED" | "PENDING"
    private String resultMessage;

    public PointTransaction(String transactionId, String postId,
                            String questionerId, String answererId, int pointAmount) {
        this.transactionId = transactionId;
        this.postId        = postId;
        this.questionerId  = questionerId;
        this.answererId    = answererId;
        this.pointAmount   = pointAmount;
        this.status        = "PENDING";
        this.resultMessage = "";
    }

    public String getTransactionId() { return transactionId; }
    public String getPostId()        { return postId; }
    public String getQuestionerId()  { return questionerId; }
    public String getAnswererId()    { return answererId; }
    public int    getPointAmount()   { return pointAmount; }
    public String getStatus()        { return status; }
    public String getResultMessage() { return resultMessage; }

    public void setStatus(String status)               { this.status        = status; }
    public void setResultMessage(String resultMessage) { this.resultMessage = resultMessage; }

    public boolean isSuccess() { return "SUCCESS".equals(status); }
}
