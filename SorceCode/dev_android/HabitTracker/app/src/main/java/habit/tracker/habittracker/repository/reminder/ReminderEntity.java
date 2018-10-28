package habit.tracker.habittracker.repository.reminder;

public class ReminderEntity {
    private String reminderId;
    private String habitId;
    private String reminderTime;
    private String repeatTime;
    private String repeatRemain;
    private int serverId;

    public String getReminderId() {
        return reminderId;
    }

    public String getReminderTime() {
        return reminderTime;
    }

    public String getRepeatTime() {
        return repeatTime;
    }

    public String getRepeatRemain() {
        return repeatRemain;
    }

    public String getHabitId() {
        return habitId;
    }

    public int getServerId() {
        return serverId;
    }

    public void setReminderId(String reminderId) {
        this.reminderId = reminderId;
    }

    public void setReminderTime(String reminderTime) {
        this.reminderTime = reminderTime;
    }

    public void setRepeatTime(String repeatTime) {
        this.repeatTime = repeatTime;
    }

    public void setRepeatRemain(String repeatRemain) {
        this.repeatRemain = repeatRemain;
    }

    public void setHabitId(String habitId) {
        this.habitId = habitId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }
}
