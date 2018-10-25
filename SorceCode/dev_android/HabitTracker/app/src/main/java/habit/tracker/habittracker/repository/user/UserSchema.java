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

    String CREATE_USER_TABLE =
            "CREATE TABLE IF NOT EXISTS " + USER_TABLE + " ("
                    + USER_ID + " INTEGER PRIMARY KEY, "
                    + USERNAME + " TEXT, "
                    + EMAIL + " TEXT, "
                    + PHONE + " TEXT, "
                    + GENDER + " TEXT, "
                    + DATE_OF_BIRTH + " TEXT, "
                    + PASSWORD + " TEXT, "
                    + USER_ICON + " TEXT, "
                    + AVATAR + " TEXT, "
                    + USER_DESCRIPTION + " TEXT"
                    + ")";
    String[] USER_COLUMNS = new String[]{USER_ID, USERNAME, EMAIL, PHONE, GENDER, DATE_OF_BIRTH, PASSWORD, USER_ICON, AVATAR, USER_DESCRIPTION};
}
