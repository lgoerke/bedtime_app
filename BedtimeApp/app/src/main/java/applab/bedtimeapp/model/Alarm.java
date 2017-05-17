package applab.bedtimeapp.model;

/**
 * Created by berberakif on 16/05/17.
 */

public class Alarm {

    private long alarmId;
    private long userId;
    private String date;
    private int sleepRate;
    private String morningAlarm;
    private String eveningAlarm;
    private int numberOfSnoozes;

    public long getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(long alarmId) {
        this.alarmId = alarmId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    @Override
    public String toString() {
        return "Alarm{" +
                "alarmId=" + alarmId +
                ", userId=" + userId +
                ", date='" + date + '\'' +
                ", sleepRate=" + sleepRate +
                ", morningAlarm='" + morningAlarm + '\'' +
                ", eveningAlarm='" + eveningAlarm + '\'' +
                ", numberOfSnoozes=" + numberOfSnoozes +
                '}';
    }

    public Alarm(long alarmId, long userId, String date, int sleepRate, String morningAlarm, String eveningAlarm, int numberOfSnoozes) {
        this.alarmId = alarmId;
        this.userId = userId;
        this.date = date;
        this.sleepRate = sleepRate;
        this.morningAlarm = morningAlarm;
        this.eveningAlarm = eveningAlarm;
        this.numberOfSnoozes = numberOfSnoozes;
    }
    public Alarm(){}
}
