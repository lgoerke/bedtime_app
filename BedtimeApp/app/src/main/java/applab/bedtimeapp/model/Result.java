package applab.bedtimeapp.model;

/**
 * Created by berberakif on 24/05/17.
 */

public class Result {

    private char updateType; // A alarm, F feedback, S self efficacy
    private long resultId;
    private long userId;
    private long dayId; // 1-14
    private String creationDate;

    // alarm
    private String alarmDate; // can be used as a status flag
    private int sleepRate;
    private String morningAlarm;
    private String eveningAlarm;
    private int numberOfSnoozes;

    // feedback
    private String feedbackDate; // can be used as a status flag
    private int questionRested;
    private int questionBusy;
    private int questionMood;
    private int questionConcentration;
    // reason
    private String refusalReason;
    private int procrastinationDuration; // todo name

    // self_efficacy
    private String selfEfficacyDate; // can be used as a status flag
    int q1,
            q2,
            q3,
            q4,
            q5,
            q6,
            q7,
            q8,
            q9,
            q10;

    public Result(long resultId, long userId, long dayId, String creationDate, String alarmDate, int sleepRate, String morningAlarm, String eveningAlarm, int numberOfSnoozes, String feedbackDate, int questionRested, int questionBusy, int questionMood, int questionConcentration, String refusalReason, int procrastinationDuration, String selfEfficacyDate, int q1, int q2, int q3, int q4, int q5, int q6, int q7, int q8, int q9, int q10) {
        this.resultId = resultId;
        this.userId = userId;
        this.dayId = dayId;
        this.creationDate = creationDate;
        this.alarmDate = alarmDate;
        this.sleepRate = sleepRate;
        this.morningAlarm = morningAlarm;
        this.eveningAlarm = eveningAlarm;
        this.numberOfSnoozes = numberOfSnoozes;
        this.feedbackDate = feedbackDate;
        this.questionRested = questionRested;
        this.questionBusy = questionBusy;
        this.questionMood = questionMood;
        this.questionConcentration = questionConcentration;
        this.refusalReason = refusalReason;
        this.procrastinationDuration = procrastinationDuration;
        this.selfEfficacyDate = selfEfficacyDate;
        this.q1 = q1;
        this.q2 = q2;
        this.q3 = q3;
        this.q4 = q4;
        this.q5 = q5;
        this.q6 = q6;
        this.q7 = q7;
        this.q8 = q8;
        this.q9 = q9;
        this.q10 = q10;
    }

    public Result() {
    }

    public char getUpdateType() {
        return updateType;
    }

    public void setUpdateType(char updateType) {
        this.updateType = updateType;
    }

    public long getResultId() {
        return resultId;
    }

    public void setResultId(long resultId) {
        this.resultId = resultId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getDayId() {
        return dayId;
    }

    public void setDayId(long dayId) {
        this.dayId = dayId;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getAlarmDate() {
        return alarmDate;
    }

    public void setAlarmDate(String alarmDate) {
        this.alarmDate = alarmDate;
    }

    public int getSleepRate() {
        return sleepRate;
    }

    public void setSleepRate(int sleepRate) {
        this.sleepRate = sleepRate;
    }

    public String getMorningAlarm() {
        return morningAlarm;
    }

    public void setMorningAlarm(String morningAlarm) {
        this.morningAlarm = morningAlarm;
    }

    public String getEveningAlarm() {
        return eveningAlarm;
    }

    public void setEveningAlarm(String eveningAlarm) {
        this.eveningAlarm = eveningAlarm;
    }

    public int getNumberOfSnoozes() {
        return numberOfSnoozes;
    }

    public void setNumberOfSnoozes(int numberOfSnoozes) {
        this.numberOfSnoozes = numberOfSnoozes;
    }

    public String getFeedbackDate() {
        return feedbackDate;
    }

    public void setFeedbackDate(String feedbackDate) {
        this.feedbackDate = feedbackDate;
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

    public int getProcrastinationDuration() {
        return procrastinationDuration;
    }

    public void setProcrastinationDuration(int procrastinationDuration) {
        this.procrastinationDuration = procrastinationDuration;
    }

    public String getSelfEfficacyDate() {
        return selfEfficacyDate;
    }

    public void setSelfEfficacyDate(String selfEfficacyDate) {
        this.selfEfficacyDate = selfEfficacyDate;
    }

    public int getQ1() {
        return q1;
    }

    public void setQ1(int q1) {
        this.q1 = q1;
    }

    public int getQ2() {
        return q2;
    }

    public void setQ2(int q2) {
        this.q2 = q2;
    }

    public int getQ3() {
        return q3;
    }

    public void setQ3(int q3) {
        this.q3 = q3;
    }

    public int getQ4() {
        return q4;
    }

    public void setQ4(int q4) {
        this.q4 = q4;
    }

    public int getQ5() {
        return q5;
    }

    public void setQ5(int q5) {
        this.q5 = q5;
    }

    public int getQ6() {
        return q6;
    }

    public void setQ6(int q6) {
        this.q6 = q6;
    }

    public int getQ7() {
        return q7;
    }

    public void setQ7(int q7) {
        this.q7 = q7;
    }

    public int getQ8() {
        return q8;
    }

    public void setQ8(int q8) {
        this.q8 = q8;
    }

    public int getQ9() {
        return q9;
    }

    public void setQ9(int q9) {
        this.q9 = q9;
    }

    public int getQ10() {
        return q10;
    }

    public void setQ10(int q10) {
        this.q10 = q10;
    }

    @Override
    public String toString() {
        return "Result{" +
                "resultId=" + resultId +
                ", userId=" + userId +
                ", dayId=" + dayId +
                ", creationDate='" + creationDate + '\'' +
                ", alarmDate='" + alarmDate + '\'' +
                ", sleepRate=" + sleepRate +
                ", morningAlarm='" + morningAlarm + '\'' +
                ", eveningAlarm='" + eveningAlarm + '\'' +
                ", numberOfSnoozes=" + numberOfSnoozes +
                ", feedbackDate='" + feedbackDate + '\'' +
                ", questionRested=" + questionRested +
                ", questionBusy=" + questionBusy +
                ", questionMood=" + questionMood +
                ", questionConcentration=" + questionConcentration +
                ", refusalReason='" + refusalReason + '\'' +
                ", procrastinationDuration='" + procrastinationDuration + '\'' +
                ", selfEfficacyDate='" + selfEfficacyDate + '\'' +
                ", q1=" + q1 +
                ", q2=" + q2 +
                ", q3=" + q3 +
                ", q4=" + q4 +
                ", q5=" + q5 +
                ", q6=" + q6 +
                ", q7=" + q7 +
                ", q8=" + q8 +
                ", q9=" + q9 +
                ", q10=" + q10 +
                '}';
    }
}
