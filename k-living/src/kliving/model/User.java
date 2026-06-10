package kliving.model;

import kliving.util.DataUtil;

public class User {
    private String userId;
    private String password;
    private String nickname;
    private int experienceYear;
    private int currentPoint;
    private int totalPoint;
    private String userLabel;
    private boolean blocked;
    private boolean admin;

    public User(String userId, String password, String nickname,
                int experienceYear, int currentPoint, int totalPoint,
                boolean blocked, boolean admin) {
        this.userId       = userId;
        this.password     = password;
        this.nickname     = nickname;
        this.experienceYear = experienceYear;
        this.currentPoint = currentPoint;
        this.totalPoint   = totalPoint;
        this.blocked      = blocked;
        this.admin        = admin;
        this.userLabel    = "";
    }

    // ── Getters ────────────────────────────────────────────────────────────
    public String getUserId()       { return userId; }
    public String getPassword()     { return password; }
    public String getNickname()     { return nickname; }
    public int    getExperienceYear(){ return experienceYear; }
    public int    getCurrentPoint() { return currentPoint; }
    public int    getTotalPoint()   { return totalPoint; }
    public String getUserLabel()    { return userLabel; }
    public boolean isBlocked()      { return blocked; }
    public boolean isAdmin()        { return admin; }

    // ── Setters ────────────────────────────────────────────────────────────
    public void setPassword(String password)        { this.password = password; }
    public void setCurrentPoint(int currentPoint)   { this.currentPoint = currentPoint; }
    public void setTotalPoint(int totalPoint)       { this.totalPoint = totalPoint; }
    public void setUserLabel(String userLabel)      { this.userLabel = userLabel; }
    public void setBlocked(boolean blocked)         { this.blocked = blocked; }

    /** Calculates a grade tier based on accumulated points. */
    public String getGrade() {
        if (totalPoint >= 1000) return "플래티넘";
        if (totalPoint >= 500)  return "골드";
        if (totalPoint >= 100)  return "실버";
        return "브론즈";
    }

    /**
     * Serialises this user to a single pipe-delimited line suitable for
     * storage in User.txt.  Special characters are encoded by DataUtil.
     */
    public String toFileString() {
        return DataUtil.encode(userId)    + "|"
             + DataUtil.encode(password)  + "|"
             + DataUtil.encode(nickname)  + "|"
             + experienceYear             + "|"
             + currentPoint               + "|"
             + totalPoint                 + "|"
             + blocked                    + "|"
             + admin;
    }

    @Override
    public String toString() {
        return nickname + " [" + userLabel + "]";
    }
}
