package habit.tracker.habittracker.repository.reminder;

public class ReminderEntity {
    private String reminderId;
    private String habitId;
    private String remindText;
    private String reminderTime;
    private String repeatType;
    private String serverId;

    public String getReminderId() {
        return reminderId;
    }

    public String getHabitId() {
        return habitId;
    }

    public String getRemindText() {
        return remindText;
    }

    public String getReminderTime() {
        return reminderTime;
    }

    public String getRepeatType() {
        return repeatType;
    }

    public String getServerId() {
        return serverId;
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

    public void setReminderTime(String reminderTime) {
        this.reminderTime = reminderTime;
    }

    public void setRepeatType(String repeatType) {
        this.repeatType = repeatType;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }
}
