package habit.tracker.habittracker.api.model.habit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Habit {

    @SerializedName("habit_id")
    @Expose
    private String habitId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("catagory_id")
    @Expose
    private String catagoryId;
    @SerializedName("schedule_id")
    @Expose
    private String scheduleId;
    @SerializedName("goal_id")
    @Expose
    private String goalId;
    @SerializedName("habit_name")
    @Expose
    private String habitName;
    @SerializedName("habit_type")
    @Expose
    private String habitType;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("count_type")
    @Expose
    private String countType;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("habit_icon")
    @Expose
    private String habitIcon;
    @SerializedName("habit_description")
    @Expose
    private String habitDescription;

    public String getHabitId() {
        return habitId;
    }

    public void setHabitId(String habitId) {
        this.habitId = habitId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCatagoryId() {
        return catagoryId;
    }

    public void setCatagoryId(String catagoryId) {
        this.catagoryId = catagoryId;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getGoalId() {
        return goalId;
    }

    public void setGoalId(String goalId) {
        this.goalId = goalId;
    }

    public String getHabitName() {
        return habitName;
    }

    public void setHabitName(String habitName) {
        this.habitName = habitName;
    }

    public String getHabitType() {
        return habitType;
    }

    public void setHabitType(String habitType) {
        this.habitType = habitType;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCountType() {
        return countType;
    }

    public void setCountType(String countType) {
        this.countType = countType;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getHabitIcon() {
        return habitIcon;
    }

    public void setHabitIcon(String habitIcon) {
        this.habitIcon = habitIcon;
    }

    public String getHabitDescription() {
        return habitDescription;
    }

    public void setHabitDescription(String habitDescription) {
        this.habitDescription = habitDescription;
    }

}
