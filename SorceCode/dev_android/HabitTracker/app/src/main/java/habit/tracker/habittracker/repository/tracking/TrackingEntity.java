package habit.tracker.habittracker.repository.tracking;

import android.text.TextUtils;

import habit.tracker.habittracker.api.model.tracking.Tracking;

public class TrackingEntity {
    private String trackingId;
    private String habitId;
    private String currentDate;
    private String count;
    private String description;
    private boolean isUpdate = false;

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

    public int getIntCount() {
        if (TextUtils.isEmpty(count)) {
            return 0;
        }
        return Integer.parseInt(count);
    }

    public String getDescription() {
        return description;
    }

    public boolean isUpdate() {
        return isUpdate;
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

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public Tracking toModel() {
        Tracking tracking = new Tracking();
        tracking.setTrackingId(trackingId);
        tracking.setHabitId(habitId);
        tracking.setCurrentDate(currentDate);
        tracking.setCount(count);
        tracking.setDescription(description);
        tracking.setUpdate(isUpdate);
        return tracking;
    }
}
