package habit.tracker.habittracker.repository.reminder;

public interface ReminderSchema {
    String REMINDER_TABLE = "reminder";
    String REMINDER_ID = "reminderId";
    String HABIT_ID = "habit_id";
    String USER_ID = "user_id";
    String REMIND_TEXT = "remind_text";
    String REMINDER_START_TIME = "reminder_start_time";
    String REMINDER_END_TIME = "reminder_end_time";
    String REPEAT_TYPE = "repeat_type";
    String SERVICE_ID = "service_id";
    String CREATE_REMINDER_TABLE =
            "CREATE TABLE IF NOT EXISTS " + REMINDER_TABLE + " ("
                    + REMINDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + REMIND_TEXT + " TEXT, "
                    + USER_ID + " TEXT, "
                    + REMINDER_START_TIME + " TEXT, "
                    + REMINDER_END_TIME + " TEXT, "
                    + REPEAT_TYPE + " TEXT, "
                    + HABIT_ID + " TEXT, "
                    + SERVICE_ID + " TEXT"
                    + ")";
    String[] REMINDER_COLUMNS = {REMINDER_ID, HABIT_ID, USER_ID, REMIND_TEXT, REMINDER_START_TIME, REMINDER_END_TIME, REPEAT_TYPE, SERVICE_ID};
}
