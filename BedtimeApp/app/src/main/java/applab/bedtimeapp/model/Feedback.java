package applab.bedtimeapp.model;

/**
 * Created by berberakif on 17/04/17.
 */

public class Feedback {

    private long fbId;
    private long userId;
    private String date;
    private int questionRested;
    private int questionBusy;
    private int questionMood;
    private int questionConcentration;
    private String refusalReason;


    public long getFbId() {
        return fbId;
    }

    public void setFbId(long fbId) {
        this.fbId = fbId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }


    public int getQuestionRested() {
        return questionRested;
    }

    public void setQuestionRested(int questionRested) {
        this.questionRested = questionRested;
    }

    public int getQuestionBusy() {
        return questionBusy;
    }

    public void setQuestionBusy(int questionBusy) {
        this.questionBusy = questionBusy;
    }

    public int getQuestionMood() {
        return questionMood;
    }

    public void setQuestionMood(int questionMood) {
        this.questionMood = questionMood;
    }

    public int getQuestionConcentration() {
        return questionConcentration;
    }

    public void setQuestionConcentration(int questionConcentration) {
        this.questionConcentration = questionConcentration;
    }


    public String getRefusalReason() {
        return refusalReason;
    }

    public void setRefusalReason(String refusalReason) {
        this.refusalReason = refusalReason;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public Feedback(long fbId, long userId, String date, int questionRested, int questionBusy, int questionMood, int questionConcentration, String refusalReason) {
        this.fbId = fbId;
        this.userId = userId;
        this.date = date;
        this.questionRested = questionRested;
        this.questionBusy = questionBusy;
        this.questionMood = questionMood;
        this.questionConcentration = questionConcentration;
        this.refusalReason = refusalReason;
    }

    public Feedback() {
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "fbId=" + fbId +
                ", userId=" + userId +
                ", date='" + date + '\'' +
                ", questionRested=" + questionRested +
                ", questionBusy=" + questionBusy +
                ", questionMood=" + questionMood +
                ", questionConcentration=" + questionConcentration +
                ", refusalReason='" + refusalReason + '\'' +
                '}';
    }
}
