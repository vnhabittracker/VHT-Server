package habit.tracker.habittracker.repository.tracking;

import java.util.List;

public interface TrackingDao {
    boolean saveTracking(TrackingEntity entity);
    boolean updateTracking(TrackingEntity entity);
    List<TrackingEntity> getTrackingListByHabit(String habitId);
}
