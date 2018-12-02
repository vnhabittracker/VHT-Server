package habit.tracker.habittracker.repository.habit;

import habit.tracker.habittracker.api.model.habit.Habit;

/**
 * Created on 10/16/2018
 */
public class HabitEntity implements TrackingDateInWeek {
    private String habitId;
    private String userId;
    private String groupId;
    private String monitorId;
    private String habitName;
    private String habitTarget;
    private String habitType;
    private String monitorType;
    private String monitorUnit;
    private String monitorNumber;
    private String startDate;
    private String endDate;
    private String createdDate;
    private String habitColor;
    private String description;
    private String mon;
    private String tue;
    private String wed;
    private String thu;
    private String fri;
    private String sat;
    private String sun;
    private String habitNameId;
    private String habitNameAscii;
    private String lastDateSyn;
    private boolean isDelete = false;
    private boolean isUpdate = false;

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

    public String getDescription() {
        return description;
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

    public String getHabitNameId() {
        return habitNameId;
    }

    public String getHabitNameAscii() {
        return habitNameAscii;
    }

    public String getLastDateSyn() {
        return lastDateSyn;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public boolean isUpdate() {
        return isUpdate;
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

    public void setDescription(String description) {
        this.description = description;
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

    public void setHabitNameId(String habitNameId) {
        this.habitNameId = habitNameId;
    }

    public void setHabitNameAscii(String habitNameAscii) {
        this.habitNameAscii = habitNameAscii;
    }

    public void setLastDateSyn(String lastDateSyn) {
        this.lastDateSyn = lastDateSyn;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public Habit toModel() {
        Habit habit = new Habit();
        habit.setHabitId(habitId);
        habit.setUserId(userId);
        habit.setGroupId(groupId);
        habit.setMonitorId(monitorId);
        habit.setHabitName(habitName);
        habit.setHabitTarget(habitTarget);
        habit.setHabitType(habitType);
        habit.setMonitorType(monitorType);
        habit.setMonitorUnit(monitorUnit);
        habit.setMonitorNumber(monitorNumber);
        habit.setCreatedDate(createdDate);
        habit.setStartDate(startDate);
        habit.setEndDate(endDate);
        habit.setHabitColor(habitColor);
        habit.setDescription(description);
        habit.setMon(mon);
        habit.setTue(tue);
        habit.setWed(wed);
        habit.setThu(thu);
        habit.setFri(fri);
        habit.setSat(sat);
        habit.setSun(sun);
        habit.setHabitNameId(habitNameId);
        habit.setHabitNameAscii(habitNameAscii);
        habit.setDelete(isDelete);
        habit.setUpdate(isUpdate);
        return habit;
    }
}
