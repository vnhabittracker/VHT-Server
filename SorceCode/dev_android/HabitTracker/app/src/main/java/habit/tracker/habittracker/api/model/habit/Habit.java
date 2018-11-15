package habit.tracker.habittracker.api.model.habit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.Normalizer;
import java.util.List;
import java.util.regex.Pattern;

import habit.tracker.habittracker.api.model.reminder.Reminder;
import habit.tracker.habittracker.api.model.tracking.Tracking;
import habit.tracker.habittracker.repository.habit.TrackingDateInWeek;

public class Habit implements TrackingDateInWeek {

    @SerializedName("habit_id")
    @Expose
    private String habitId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("group_id")
    @Expose
    private String groupId;
    @SerializedName("monitor_id")
    @Expose
    private String monitorId;
    @SerializedName("habit_name")
    @Expose
    private String habitName;
    @SerializedName("habit_target")
    @Expose
    private String habitTarget;
    @SerializedName("habit_type")
    @Expose
    private String habitType;
    @SerializedName("monitor_type")
    @Expose
    private String monitorType;
    @SerializedName("monitor_unit")
    @Expose
    private String monitorUnit;
    @SerializedName("monitor_number")
    @Expose
    private String monitorNumber;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("habit_color")
    @Expose
    private String habitColor;
    @SerializedName("habit_description")
    @Expose
    private String habitDescription;
    @SerializedName("mon")
    @Expose
    private String mon;
    @SerializedName("tue")
    @Expose
    private String tue;
    @SerializedName("wed")
    @Expose
    private String wed;
    @SerializedName("thu")
    @Expose
    private String thu;
    @SerializedName("fri")
    @Expose
    private String fri;
    @SerializedName("sat")
    @Expose
    private String sat;
    @SerializedName("sun")
    @Expose
    private String sun;
    @SerializedName("reminders")
    @Expose
    private List<Reminder> reminderList;
    @SerializedName("tracks")
    @Expose
    private List<Tracking> tracksList;
    @SerializedName("habit_name_id")
    @Expose
    private String habitSearchNameId;
    @SerializedName("habit_name_ascii")
    @Expose
    private String habitNameAscii;
    @SerializedName("habit_name_count")
    @Expose
    private int habitNameCount;

    public String getHabitId() {
        return habitId;
    }

    public String getUserId() {
        return userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getMonitorId() {
        return monitorId;
    }

    public String getHabitName() {
        return habitName;
    }

    public String getHabitTarget() {
        return habitTarget;
    }

    public String getHabitType() {
        return habitType;
    }

    public String getMonitorType() {
        return monitorType;
    }

    public String getMonitorUnit() {
        return monitorUnit;
    }

    public String getMonitorNumber() {
        return monitorNumber;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getHabitColor() {
        return habitColor;
    }

    public String getHabitDescription() {
        return habitDescription;
    }

    public int getHabitNameCount() {
        return habitNameCount;
    }

    public String getMon() {
        return mon;
    }

    public String getTue() {
        return tue;
    }

    public String getWed() {
        return wed;
    }

    public String getThu() {
        return thu;
    }

    public String getFri() {
        return fri;
    }

    public String getSat() {
        return sat;
    }

    public String getSun() {
        return sun;
    }

    public List<Reminder> getReminderList() {
        return reminderList;
    }

    public List<Tracking> getTracksList() {
        return tracksList;
    }

    public String getHabitSearchNameId() {
        return habitSearchNameId;
    }

    public String getHabitNameAscii() {
        return habitNameAscii;
    }

    public void setHabitId(String habitId) {
        this.habitId = habitId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setMonitorId(String monitorId) {
        this.monitorId = monitorId;
    }

    public void setHabitName(String habitName) {
        this.habitName = habitName;
    }

    public void setHabitTarget(String habitTarget) {
        this.habitTarget = habitTarget;
    }

    public void setHabitType(String habitType) {
        this.habitType = habitType;
    }

    public void setMonitorType(String monitorType) {
        this.monitorType = monitorType;
    }

    public void setMonitorUnit(String monitorUnit) {
        this.monitorUnit = monitorUnit;
    }

    public void setMonitorNumber(String monitorNumber) {
        this.monitorNumber = monitorNumber;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public void setHabitColor(String habitColor) {
        this.habitColor = habitColor;
    }

    public void setHabitDescription(String habitDescription) {
        this.habitDescription = habitDescription;
    }

    public void setMon(String mon) {
        this.mon = mon;
    }

    public void setTue(String tue) {
        this.tue = tue;
    }

    public void setWed(String wed) {
        this.wed = wed;
    }

    public void setThu(String thu) {
        this.thu = thu;
    }

    public void setFri(String fri) {
        this.fri = fri;
    }

    public void setSat(String sat) {
        this.sat = sat;
    }

    public void setSun(String sun) {
        this.sun = sun;
    }

    public void setReminderList(List<Reminder> reminderList) {
        this.reminderList = reminderList;
    }

    public void setTracksList(List<Tracking> tracksList) {
        this.tracksList = tracksList;
    }

    public void setHabitSearchNameId(String habitNameId) {
        this.habitSearchNameId = habitNameId;
    }

    public void setHabitNameAscii(String habitName) {
        habitNameAscii = habitName;
    }

    public void setHabitNameCount(int habitNameCount) {
        this.habitNameCount = habitNameCount;
    }
}
