package applab.bedtimeapp.model;

/**
 * Created by berberakif on 17/04/17.
 */

public class Feedback {

    private long fbId;
    private long userId;
    private String date;
    private int question1;
    private int question2;
    private int question3;
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

    public int getQuestion1() {
        return question1;
    }

    public void setQuestion1(int question1) {
        this.question1 = question1;
    }

    public int getQuestion2() {
        return question2;
    }

    public void setQuestion2(int question2) {
        this.question2 = question2;
    }

    public int getQuestion3() {
        return question3;
    }

    public void setQuestion3(int question3) {
        this.question3 = question3;
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

    public Feedback(long fbId, long userId, String date, int question1, int question2, int question3, String morningAlarm, String eveningAlarm, int numberOfSnoozes, String landingPage, String refusalReason) {
        this.fbId = fbId;
        this.userId = userId;
        this.date = date;
        this.question1 = question1;
        this.question2 = question2;
        this.question3 = question3;
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
                ", date=" + date +
                ", question1=" + question1 +
                ", question2=" + question2 +
                ", question3=" + question3 +
                ", morningAlarm=" + morningAlarm +
                ", eveningAlarm=" + eveningAlarm +
                ", numberOfSnoozes=" + numberOfSnoozes +
                ", landingPage='" + landingPage + '\'' +
                ", refusalReason='" + refusalReason + '\'' +
                '}';
    }
}
