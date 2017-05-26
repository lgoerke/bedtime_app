package applab.bedtimeapp.model;

/**
 * Created by berberakif on 14/05/17.
 */

public class Reason {

    private long reasonId;
    private long userId;
    private String reason;
    private int duration;

    public Reason(){

    }

    public Reason(long reasonId, long userId, String reason) {
        this.reasonId = reasonId;
        this.userId = userId;
        this.reason = reason;
    }

    public long getReasonId() {
        return reasonId;
    }

    public void setReasonId(long reasonId) {
        this.reasonId = reasonId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }


    @Override
    public String toString() {
        return "Reason{" +
                "reasonId=" + reasonId +
                ", userId=" + userId +
                ", reason='" + reason + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }
}
