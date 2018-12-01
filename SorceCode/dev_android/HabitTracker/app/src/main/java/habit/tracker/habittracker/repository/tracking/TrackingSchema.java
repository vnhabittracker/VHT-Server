package habit.tracker.habittracker.repository.tracking;

public interface TrackingSchema {
    String TRACKING_TABLE = "tracking";
    String TRACKING_ID = "tracking_id";
    String HABIT_ID = "habit_id";
    String CURRENT_DATE = "tracking_current_date";
    String COUNT = "count";
    String TRACKING_DESCRIPTION = "tracking_description";
    String IS_UPDATED = "is_updated";
    String CREATE_TRACKING_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TRACKING_TABLE + " ("
                    + TRACKING_ID + " TEXT PRIMARY KEY NOT NULL, "
                    + HABIT_ID + " TEXT, "
                    + CURRENT_DATE + " TEXT, "
                    + COUNT + " TEXT, "
                    + TRACKING_DESCRIPTION + " TEXT, "
                    + IS_UPDATED + " TEXT DEFAULT 0"
                    + ")";
    String[] TRACKING_COLUMNS = new String[]{TRACKING_ID, HABIT_ID, CURRENT_DATE, COUNT, TRACKING_DESCRIPTION, IS_UPDATED};
}
