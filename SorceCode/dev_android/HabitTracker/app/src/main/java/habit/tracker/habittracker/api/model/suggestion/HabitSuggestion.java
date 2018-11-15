package habit.tracker.habittracker.api.model.suggestion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HabitSuggestion {
    @SerializedName("habit_name_id")
    @Expose
    private String habitSearchNameId;

    @SerializedName("group_id")
    @Expose
    private String groupId;

    @SerializedName("habit_name_uni")
    @Expose
    private String habitNameUni;

    @SerializedName("habit_name")
    @Expose
    private String habit_name;

    @SerializedName("habit_name_count")
    @Expose
    private String habitNameCount;

    @SerializedName("total_track")
    @Expose
    private int totalTrack;

    @SerializedName("success_track")
    @Expose
    private int successTrack;

    public String getHabitSearchNameId() {
        return habitSearchNameId;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getHabitNameUni() {
        return habitNameUni;
    }

    public String getHabit_name() {
        return habit_name;
    }

    public String getHabitNameCount() {
        return habitNameCount;
    }

    public int getTotalTrack() {
        return totalTrack;
    }

    public int getSuccessTrack() {
        return successTrack;
    }

    public void setHabitSearchNameId(String habitSearchNameId) {
        this.habitSearchNameId = habitSearchNameId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setHabitNameUni(String habitNameUni) {
        this.habitNameUni = habitNameUni;
    }

    public void setHabit_name(String habit_name) {
        this.habit_name = habit_name;
    }

    public void setHabitNameCount(String habitNameCount) {
        this.habitNameCount = habitNameCount;
    }

    public void setTotalTrack(int totalTrack) {
        this.totalTrack = totalTrack;
    }

    public void setSuccessTrack(int successTrack) {
        this.successTrack = successTrack;
    }
}
