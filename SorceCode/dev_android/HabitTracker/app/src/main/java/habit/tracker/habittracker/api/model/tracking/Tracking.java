package habit.tracker.habittracker.api.model.tracking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tracking {
    @SerializedName("tracking_id")
    @Expose
    private String trackingId;
    @SerializedName("habit_id")
    @Expose
    private String habitId;
    @SerializedName("current_date")
    @Expose
    private String currentDate;
    @SerializedName("count")
    @Expose
    private String count;
    @SerializedName("tracking_description")
    @Expose
    private String trackingDescription;

    public String getTrackingId() {
        return trackingId;
    }

    public String getHabitId() {
        return habitId;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public String getCount() {
        return count;
    }

    public String getDescription() {
        return trackingDescription;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public void setHabitId(String habitId) {
        this.habitId = habitId;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public void setDescription(String trackingDescription) {
        this.trackingDescription = trackingDescription;
    }
}
