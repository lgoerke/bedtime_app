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
    private String morningAlarm;
    private String eveningAlarm;
    private int numberOfSnoozes;
    private String landingPage;
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

    public String getEveningAlarm() {
        return eveningAlarm;
    }

    public void setEveningAlarm(String eveningAlarm) {
        this.eveningAlarm = eveningAlarm;
    }

    public String getMorningAlarm() {
        return morningAlarm;
    }

    public void setMorningAlarm(String morningAlarm) {
        this.morningAlarm = morningAlarm;
    }

    public int getNumberOfSnoozes() {
        return numberOfSnoozes;
    }

    public void setNumberOfSnoozes(int numberOfSnoozes) {
        this.numberOfSnoozes = numberOfSnoozes;
    }

    public String getLandingPage() {
        return landingPage;
    }

    public void setLandingPage(String landingPage) {
        this.landingPage = landingPage;
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


    public Feedback(long fbId, long userId, String date, int questionRested, int questionBusy, int questionMood, int questionConcentration, String morningAlarm, String eveningAlarm, int numberOfSnoozes, String landingPage, String refusalReason) {
        this.fbId = fbId;
        this.userId = userId;
        this.date = date;
        this.questionRested = questionRested;
        this.questionBusy = questionBusy;
        this.questionMood = questionMood;
        this.questionConcentration = questionConcentration;
        this.morningAlarm = morningAlarm;
        this.eveningAlarm = eveningAlarm;
        this.numberOfSnoozes = numberOfSnoozes;
        this.landingPage = landingPage;
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
                ", morningAlarm='" + morningAlarm + '\'' +
                ", eveningAlarm='" + eveningAlarm + '\'' +
                ", numberOfSnoozes=" + numberOfSnoozes +
                ", landingPage='" + landingPage + '\'' +
                ", refusalReason='" + refusalReason + '\'' +
                '}';
    }
}
