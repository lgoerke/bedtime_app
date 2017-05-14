package applab.bedtimeapp.model;

/**
 * Created by berberakif on 10/05/17.
 */

public class SelfEfficacy {

    public SelfEfficacy() {
    }

    public SelfEfficacy(long seId, long userId, String date, int q1, int q2, int q3, int q4, int q5, int q6, int q7, int q8, int q9, int q10) {
        this.seId = seId;
        this.userId = userId;
        this.date = date;
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

    private long seId;
    private long userId;
    private String date;

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

    public long getSeId() {
        return seId;
    }

    public void setSeId(long seId) {
        this.seId = seId;
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
        return "SelfEfficacy{" +
                "seId=" + seId +
                ", userId=" + userId +
                ", date='" + date + '\'' +
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
