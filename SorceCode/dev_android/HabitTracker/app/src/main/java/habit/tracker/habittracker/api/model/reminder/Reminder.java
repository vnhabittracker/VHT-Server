package habit.tracker.habittracker.api.model.reminder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import habit.tracker.habittracker.repository.reminder.ReminderEntity;

public class Reminder {
    @SerializedName("reminder_id")
    @Expose
    private String reminderId;
    @SerializedName("reminder_description")
    @Expose
    private String remindText;
    @SerializedName("remind_start_time")
    @Expose
    private String remindStartTime;
    @SerializedName("remind_end_time")
    @Expose
    private String remindEndTime;
    @SerializedName("repeat_type")
    @Expose
    private String repeatType;
    @SerializedName("habit_id")
    @Expose
    private String habitId;
    @SerializedName("server_id")
    @Expose
    private String serverId;
    @SerializedName("is_delete")
    @Expose
    private boolean isDelete = false;

    private String habitName;

    private String userId;

    public String getReminderId() {
        return reminderId;
    }

    public String getRemindText() {
        return remindText;
    }

    // yyyy-MM-dd HH:mm:ss
    public String getRemindStartTime() {
        return remindStartTime;
    }

    public String getRemindEndTime() {
        return remindEndTime;
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

    public boolean isDelete() {
        return isDelete;
    }

    public String getUserId() {
        return userId;
    }

    public void setReminderId(String reminderId) {
        this.reminderId = reminderId;
    }

    public void setRemindText(String remindText) {
        this.remindText = remindText;
    }

    public void setRemindStartTime(String remindStartTime) {
        this.remindStartTime = remindStartTime;
    }

    public void setRemindEndTime(String remindEndTime) {
        this.remindEndTime = remindEndTime;
    }

    public void setRepeatType(String repeatType) {
        this.repeatType = repeatType;
    }

    public void setHabitId(String habitId) {
        this.habitId = habitId;
    }

    public void setServerId(String reminderIdApp) {
        this.serverId = reminderIdApp;
    }

    public void setHabitName(String habitName) {
        this.habitName = habitName;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ReminderEntity toEnity() {
        ReminderEntity entity = new ReminderEntity();
        entity.setReminderId(reminderId);
        entity.setHabitId(habitId);
        entity.setUserId(userId);
        entity.setRemindText(remindText);
        entity.setReminderStartTime(remindStartTime);
        entity.setReminderEndTime(remindEndTime);
        entity.setRepeatType(repeatType);
        entity.setServerId(serverId);
        return entity;
    }
}
