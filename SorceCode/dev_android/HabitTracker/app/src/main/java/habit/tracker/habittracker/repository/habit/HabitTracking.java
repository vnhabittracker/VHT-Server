package habit.tracker.habittracker.repository.habit;

import java.util.ArrayList;
import java.util.List;

import habit.tracker.habittracker.repository.tracking.TrackingEntity;

/**
 * Created by DatTVT1 on 2018/10/26
 */
public class HabitTracking {
    private HabitEntity habitEntity;
    private List<TrackingEntity> trackingEntityList = new ArrayList<>();

    public HabitEntity getHabit() {
        return habitEntity;
    }

    public List<TrackingEntity> getTrackingList() {
        return trackingEntityList;
    }

    public void setHabit(HabitEntity habitEntity) {
        this.habitEntity = habitEntity;
    }

    public void setTrackingList(List<TrackingEntity> trackingEntityList) {
        this.trackingEntityList = trackingEntityList;
    }
}
