package habit.tracker.habittracker.api.model.reminder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Reminder {
    @SerializedName("reminder_id")
    @Expose
    private String reminderId;
    @SerializedName("reminder_time")
    @Expose
    private String reminderTime;
    @SerializedName("repeat_time")
    @Expose
    private String repeatTime;
    @SerializedName("habit_id")
    @Expose
    private String habitId;

    public String getReminderId() {
        return reminderId;
    }

    public String getReminderTime() {
        return reminderTime;
    }

    public String getRepeatTime() {
        return repeatTime;
    }

    public String getHabitId() {
        return habitId;
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

    public void setHabitId(String habitId) {
        this.habitId = habitId;
    }
}
