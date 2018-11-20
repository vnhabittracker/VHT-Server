package habit.tracker.habittracker.repository.reminder;

import java.util.List;

public interface ReminderDao {
    String saveReminder(ReminderEntity entity);

    ReminderEntity getRemindersById(String id);

    List<ReminderEntity> getRemindersByHabit(String habitId);
}
