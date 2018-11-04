package habit.tracker.habittracker.api.model.reminder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Reminder {
    @SerializedName("reminder_id")
    @Expose
    private String reminderId;
    @SerializedName("remind_text")
    @Expose
    private String remindText;
    @SerializedName("reminder_time")
    @Expose
    private String reminderTime;
    @SerializedName("repeat_type")
    @Expose
    private String repeatType;
    @SerializedName("habit_id")
    @Expose
    private String habitId;
    @SerializedName("server_id")
    @Expose
    private String serverId;

    private String habitName;

    private String endDate;

    public String getReminderId() {
        return reminderId;
    }

    public String getRemindText() {
        return remindText;
    }

    // yyyy-MM-dd HH:mm:ss
    public String getReminderTime() {
        return reminderTime;
    }

    public String getRepeatType() {
        return repeatType;
    }

    public String getHabitId() {
        return habitId;
    }

    public String getServerId() {
        return serverId;
    }

    public String getHabitName() {
        return habitName;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setReminderId(String reminderId) {
        this.reminderId = reminderId;
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

    public void setHabitId(String habitId) {
        this.habitId = habitId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public void setHabitName(String habitName) {
        this.habitName = habitName;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
