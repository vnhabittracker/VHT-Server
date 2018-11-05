package habit.tracker.habittracker.repository.habit;

import habit.tracker.habittracker.repository.reminder.ReminderEntity;

public class HabitNotification {
    HabitEntity habitEntity;
    ReminderEntity reminderEntity;

    public HabitEntity getHabitEntity() {
        return habitEntity;
    }

    public ReminderEntity getReminderEntity() {
        return reminderEntity;
    }

    public void setHabitEntity(HabitEntity habitEntity) {
        this.habitEntity = habitEntity;
    }

    public void setReminderEntity(ReminderEntity reminderEntity) {
        this.reminderEntity = reminderEntity;
    }
}
