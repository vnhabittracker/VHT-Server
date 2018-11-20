package habit.tracker.habittracker.repository.user;

public interface UserSchema {
    String USER_TABLE = "user";
    String USER_ID = "user_id";
    String USERNAME = "username";
    String EMAIL = "email";
    String PHONE = "phone";
    String GENDER = "gender";
    String DATE_OF_BIRTH = "date_of_birth";
    String PASSWORD = "password";
    String USER_ICON = "user_icon";
    String AVATAR = "avatar";
    String USER_DESCRIPTION = "user_description";
    String USER_CREATED_DATE = "user_created_date";
    String LAST_LOGIN_TIME = "last_login_time";
    String CONTINUE_USING_COUNT = "continue_using_count";
    String CURRENT_CONTINUE_USING_COUNT = "current_continue_using_count";
    String BEST_CONTINUE_USING_COUNT = "best_continue_using_count";
    String USER_SCORE = "user_score";

    String CREATE_USER_TABLE =
            "CREATE TABLE IF NOT EXISTS " + USER_TABLE + " ("
                    + USER_ID + " TEXT PRIMARY KEY NOT NULL, "
                    + USERNAME + " TEXT, "
                    + EMAIL + " TEXT, "
                    + PHONE + " TEXT, "
                    + GENDER + " TEXT, "
                    + DATE_OF_BIRTH + " TEXT, "
                    + PASSWORD + " TEXT, "
                    + USER_ICON + " TEXT, "
                    + AVATAR + " TEXT, "
                    + USER_DESCRIPTION + " TEXT, "
                    + USER_CREATED_DATE + " TEXT, "
                    + LAST_LOGIN_TIME + " TEXT, "
                    + CONTINUE_USING_COUNT + " TEXT, "
                    + CURRENT_CONTINUE_USING_COUNT + " TEXT, "
                    + BEST_CONTINUE_USING_COUNT + " TEXT, "
                    + USER_SCORE + " TEXT"
                    + ")";
    String[] USER_COLUMNS = new String[]{USER_ID, USERNAME, EMAIL, PHONE, GENDER, DATE_OF_BIRTH, PASSWORD, USER_ICON, AVATAR, USER_DESCRIPTION, USER_CREATED_DATE,
            LAST_LOGIN_TIME, CONTINUE_USING_COUNT, CURRENT_CONTINUE_USING_COUNT, BEST_CONTINUE_USING_COUNT, USER_SCORE};
}
