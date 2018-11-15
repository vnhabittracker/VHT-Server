package habit.tracker.habittracker.api.model.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HabitSuggestion {
    @SerializedName("habit_name_id")
    @Expose
    private String habitNameId;
    @SerializedName("group_id")
    private String groupId;
    @SerializedName("habit_name_uni")
    @Expose
    private String habitNameUni;
    @SerializedName("habit_name")
    @Expose
    private String habitName;
    @SerializedName("habit_name_count")
    @Expose
    private String habitNameCount;
    @SerializedName("total_track")
    @Expose
    private int totalTrack;

    @SerializedName("success_track")
    @Expose
    private int successTrack;

    private String group;
    private boolean isHeader;

    public HabitSuggestion(String groupId, String group, boolean isHeader) {
        this.groupId = groupId;
        this.group = group;
        this.isHeader = isHeader;
    }

    public HabitSuggestion(String habitNameId, String groupId, String habitNameUni, String habitName, String habitNameCount, boolean isHeader) {
        this.habitNameId = habitNameId;
        this.groupId = groupId;
        this.habitNameUni = habitNameUni;
        this.habitName = habitName;
        this.habitNameCount = habitNameCount;
        this.isHeader = isHeader;
    }

    public String getHabitNameId() {
        return habitNameId;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getHabitNameUni() {
        return habitNameUni;
    }

    public String getHabitName() {
        return habitName;
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

    public String getGroup() {
        return group;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHabitNameId(String habitNameId) {
        this.habitNameId = habitNameId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setHabitNameUni(String habit_name_uni) {
        this.habitNameUni = habit_name_uni;
    }

    public void setHabitName(String habitName) {
        this.habitName = habitName;
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

    public void setGroup(String group) {
        this.group = group;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }
}
