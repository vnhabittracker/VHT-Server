package habit.tracker.habittracker.repository.reminder;

public class ReminderEntity {
    String reminderId;
    String reminderHour;
    String reminderMinute;
    String repeatTime;
    String repeatRemain;
    String habitId;

    public String getReminderId() {
        return reminderId;
    }
    public String getReminderHour() {
        return reminderHour;
    }

    public String getReminderMinute() {
        return reminderMinute;
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

    public void setReminderId(String reminderId) {
        this.reminderId = reminderId;
    }

    public void setReminderHour(String reminderHour) {
        this.reminderHour = reminderHour;
    }

    public void setReminderMinute(String reminderMinute) {
        this.reminderMinute = reminderMinute;
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
}
