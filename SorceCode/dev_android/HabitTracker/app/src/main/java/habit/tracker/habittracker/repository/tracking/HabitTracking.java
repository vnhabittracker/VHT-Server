package habit.tracker.habittracker.repository.tracking;

import java.util.ArrayList;
import java.util.List;

import habit.tracker.habittracker.repository.habit.HabitEntity;

public class HabitTracking {
    private HabitEntity habitEntity;
    private List<TrackingEntity> trackingEntityList = new ArrayList<>();

    public HabitEntity getHabitEntity() {
        return habitEntity;
    }

    public List<TrackingEntity> getTrackingEntityList() {
        return trackingEntityList;
    }

    public void setHabitEntity(HabitEntity habitEntity) {
        this.habitEntity = habitEntity;
    }

    public void setTrackingEntityList(List<TrackingEntity> trackingEntityList) {
        this.trackingEntityList = trackingEntityList;
    }
}
