package habit.tracker.habittracker.repository.reminder;

import java.util.List;

public interface ReminderDao {
    int addReminder(ReminderEntity entity);

    ReminderEntity getRemindersById(String id);

    List<ReminderEntity> getRemindersByHabit(String habitId);
}
