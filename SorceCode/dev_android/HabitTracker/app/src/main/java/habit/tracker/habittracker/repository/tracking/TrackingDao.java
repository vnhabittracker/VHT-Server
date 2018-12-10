package habit.tracker.habittracker.repository.tracking;

import java.util.List;

public interface TrackingDao {
    boolean saveUpdateTracking(TrackingEntity entity);
    List<TrackingEntity> getTrackingRecordsByHabit(String habitId);
}
