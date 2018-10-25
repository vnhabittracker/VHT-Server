package habit.tracker.habittracker.repository.tracking;

public interface TrackingDao {
    TrackingEntity getTracking(String trackID);
    boolean saveTracking(TrackingEntity entity);
    boolean updateTracking(TrackingEntity entity);
}
