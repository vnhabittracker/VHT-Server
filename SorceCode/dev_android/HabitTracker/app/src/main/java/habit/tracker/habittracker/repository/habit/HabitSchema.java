package habit.tracker.habittracker.repository.habit;

/**
 * Created on 10/16/2018
 */
public interface HabitSchema {
    String HABIT_TABLE = "habit";
    String HABIT_ID = "habit_id";
    String USER_ID = "user_id";
    String GROUP_ID = "group_id";
    String MONITOR_ID = "monitor_id";
    String HABIT_NAME = "habit_name";
    String HABIT_TARGET = "habit_target";
    String HABIT_TYPE = "habit_type";
    String MONITOR_TYPE = "monitor_type";
    String MONITOR_UNIT = "monitor_unit";
    String MONITOR_NUMBER = "monitor_number";
    String START_DATE = "start_date";
    String END_DATE = "end_date";
    String CREATED_DATE = "created_date";
    String HABIT_COLOR = "habit_color";
    String HABIT_DESCRIPTION = "habit_description";
    String MON = "mon";
    String TUE = "tue";
    String WED = "wed";
    String THU = "thu";
    String FRI = "fri";
    String SAT = "sat";
    String SUN = "sun";
    String LAST_DATE_SYN = "last_date_syn";
    String IS_DELETE = "is_delete";

    String CREATE_HABIT_TABLE =
            "CREATE TABLE IF NOT EXISTS " + HABIT_TABLE + " ("
                    + HABIT_ID + " TEXT PRIMARY KEY NOT NULL, "
                    + USER_ID + " TEXT, "
                    + GROUP_ID + " TEXT, "
                    + MONITOR_ID + " TEXT, "
                    + HABIT_NAME + " TEXT, "
                    + HABIT_TARGET + " TEXT, "
                    + HABIT_TYPE + " TEXT, "
                    + MONITOR_TYPE + " TEXT, "
                    + MONITOR_UNIT + " TEXT, "
                    + MONITOR_NUMBER + " TEXT, "
                    + START_DATE + " TEXT, "
                    + END_DATE + " TEXT, "
                    + CREATED_DATE + " TEXT, "
                    + HABIT_COLOR + "  TEXT, "
                    + HABIT_DESCRIPTION + " TEXT, "
                    + MON + " TEXT, "
                    + TUE + " TEXT, "
                    + WED + " TEXT, "
                    + THU + " TEXT, "
                    + FRI + " TEXT, "
                    + SAT + " TEXT, "
                    + SUN + " TEXT, "
                    + LAST_DATE_SYN + " TEXT, "
                    + IS_DELETE + " TEXT "
                    + ")";

    String[] HABIT_COLUMNS = {HABIT_ID, USER_ID, GROUP_ID, MONITOR_ID,
            HABIT_NAME, HABIT_TARGET, HABIT_TYPE, MONITOR_TYPE,
            MONITOR_UNIT, MONITOR_NUMBER,
            START_DATE, END_DATE, CREATED_DATE,
            HABIT_COLOR, HABIT_DESCRIPTION, MON, TUE, WED, THU, FRI, SAT, SUN,
            LAST_DATE_SYN, IS_DELETE};
}
