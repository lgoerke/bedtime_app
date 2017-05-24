package applab.bedtimeapp.model;

import applab.bedtimeapp.utils.ActionStatus;
import applab.bedtimeapp.utils.ActionType;

/**
 * Created by berberakif on 16/05/17.
 */

public class ActionLog {

    private long actionLogId;
    private long userId;
    private String creationDateTime;
    private String lastUpdateDateTime;
    private String actionDateTime;
    private ActionType actionType;
    private ActionStatus actionStatus;


    public ActionLog() {
    }

    public ActionLog(long actionLogId, long userId, String creationDateTime, String lastUpdateDateTime, String actionDateTime, ActionType actionType, ActionStatus actionStatus) {
        this.actionLogId = actionLogId;
        this.userId = userId;
        this.creationDateTime = creationDateTime;
        this.lastUpdateDateTime = lastUpdateDateTime;
        this.actionDateTime = actionDateTime;
        this.actionType = actionType;
        this.actionStatus = actionStatus;
    }

    public ActionStatus getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(ActionStatus actionStatus) {
        this.actionStatus = actionStatus;
    }

    public long getActionLogId() {
        return actionLogId;
    }

    public void setActionLogId(long actionLogId) {
        this.actionLogId = actionLogId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(String creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public String getLastUpdateDateTime() {
        return lastUpdateDateTime;
    }

    public void setLastUpdateDateTime(String lastUpdateDateTime) {
        this.lastUpdateDateTime = lastUpdateDateTime;
    }

    public String getActionDateTime() {
        return actionDateTime;
    }

    public void setActionDateTime(String actionDateTime) {
        this.actionDateTime = actionDateTime;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    @Override
    public String toString() {
        return "ActionLog{" +
                "actionLogId=" + actionLogId +
                ", userId=" + userId +
                ", creationDateTime='" + creationDateTime + '\'' +
                ", lastUpdateDateTime='" + lastUpdateDateTime + '\'' +
                ", actionDateTime='" + actionDateTime + '\'' +
                ", actionType=" + actionType +
                ", actionStatus=" + actionStatus +
                '}';
    }
}
