package habit.tracker.habittracker.repository.tracking;

public class TrackingEntity {
    private String trackingId;
    private String habitId;
    private String currentDate;
    private String count;
    private String description;

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
        return description;
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

    public void setDescription(String description) {
        this.description = description;
    }
}
