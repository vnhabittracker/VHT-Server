package habit.tracker.habittracker.repository.habit;

import habit.tracker.habittracker.repository.tracking.TrackingEntity;

/**
 * Created by DatTVT1 on 2018/10/26
 */
public class DateTracking {
    HabitEntity habitEntity;
    TrackingEntity trackingEntity;

    public HabitEntity getHabitEntity() {
        return habitEntity;
    }

    public TrackingEntity getTrackingEntity() {
        return trackingEntity;
    }

    public void setHabitEntity(HabitEntity habitEntity) {
        this.habitEntity = habitEntity;
    }

    public void setTrackingEntity(TrackingEntity trackingEntity) {
        this.trackingEntity = trackingEntity;
    }
}
