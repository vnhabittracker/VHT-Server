package habit.tracker.habittracker.repository.feedback;

public interface FeedbackSchema {
    String FEEDBACK_TABLE = "feedback";
    String FEEDBACK_ID = "feedback_id";
    String USER_ID = "user_id";
    String STAR_NUM = "star_num";
    String FEEDBACK_DESCRIPTION = "feedback_description";
    String IS_UPDATE = "is_update";
    String CREATE_FEEDBACK_TABLE =
            "CREATE TABLE IF NOT EXISTS " + FEEDBACK_TABLE + " ("
                    + FEEDBACK_ID + " TEXT PRIMARY KEY NOT NULL, "
                    + USER_ID + " TEXT, "
                    + STAR_NUM + " TEXT, "
                    + FEEDBACK_DESCRIPTION + " TEXT, "
                    + IS_UPDATE + " TEXT"
                    + ")";
    String[] FEEDBACK_COLUMNS = {FEEDBACK_ID, USER_ID, STAR_NUM, FEEDBACK_DESCRIPTION, IS_UPDATE};
}
