package habit.tracker.habittracker.repository.feedback;

public interface FeedbackDao {
    FeedbackEntity getFeedbackByUser(String userId);
    boolean saveFeedback(FeedbackEntity entity);
}
