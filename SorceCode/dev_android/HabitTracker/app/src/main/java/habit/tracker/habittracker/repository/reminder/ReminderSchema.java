package habit.tracker.habittracker.repository.reminder;

public interface ReminderSchema {
    String REMINDER_TABLE = "reminder";
    String REMINDER_ID = "reminderId";
    String REMINDER_HOUR = "reminder_hour";
    String REMINDER_MINUTE = "reminder_minute";
    String REPEAT_TIME = "repeat_time";
    String REPEAT_REMAIN = "repeat_remain";
    String HABIT_ID = "habit_id";
    String CREATE_REMINDER_TABLE =
            "CREATE TABLE IF NOT EXISTS " + REMINDER_TABLE + " ("
                    + REMINDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + REMINDER_HOUR + " TEXT, "
                    + REMINDER_MINUTE + " TEXT, "
                    + REPEAT_TIME + " TEXT, "
                    + REPEAT_REMAIN + " TEXT, "
                    + HABIT_ID + " TEXT "
                    + ")";
    String[] REMINDER_COLUMNS = {REMINDER_ID, REMINDER_HOUR, REMINDER_MINUTE, REPEAT_TIME, REPEAT_REMAIN, HABIT_ID};
}
