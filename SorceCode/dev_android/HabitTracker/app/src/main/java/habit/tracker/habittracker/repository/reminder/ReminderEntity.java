package habit.tracker.habittracker.repository.reminder;

public class ReminderEntity {
    private String reminderId;
    private String habitId;
    private String remindText;
    private String reminderStartTime;
    private String reminderEndTime;
    private String repeatType;
    private String serverId;
    private String userId;

    public String getReminderId() {
        return reminderId;
    }

    public String getHabitId() {
        return habitId;
    }

    public String getRemindText() {
        return remindText;
    }

    public String getReminderStartTime() {
        return reminderStartTime;
    }

    public String getReminderEndTime() {
        return reminderEndTime;
    }

    public String getRepeatType() {
        return repeatType;
    }

    public String getServerId() {
        return serverId;
    }

    public String getUserId() {
        return userId;
    }

    public void setReminderId(String reminderId) {
        this.reminderId = reminderId;
    }

    public void setHabitId(String habitId) {
        this.habitId = habitId;
    }

    public void setRemindText(String remindText) {
        this.remindText = remindText;
    }

    public void setReminderStartTime(String reminderStartTime) {
        this.reminderStartTime = reminderStartTime;
    }

    public void setReminderEndTime(String reminderEndTime) {
        this.reminderEndTime = reminderEndTime;
    }

    public void setRepeatType(String repeatType) {
        this.repeatType = repeatType;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
