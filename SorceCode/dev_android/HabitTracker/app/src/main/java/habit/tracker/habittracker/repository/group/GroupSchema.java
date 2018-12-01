package habit.tracker.habittracker.repository.group;

public interface GroupSchema {
    String GROUP_TABLE = "group_tb";
    String GROUP_ID = "group_id";
    String USER_ID = "user_id";
    String GROUP_NAME = "group_name";
    String GROUP_DESCRIPTION = "group_description";
    String IS_DELETE = "is_delete";
    String IS_DEFAULT = "is_local";
    String CREATE_GROUP_TABLE =
            "CREATE TABLE IF NOT EXISTS " + GROUP_TABLE + " ("
                    + GROUP_ID + " TEXT PRIMARY KEY NOT NULL, "
                    + USER_ID + " TEXT, "
                    + GROUP_NAME + " TEXT, "
                    + GROUP_DESCRIPTION + " TEXT, "
                    + IS_DELETE + " TEXT, "
                    + IS_DEFAULT + " TEXT"
                    + ")";
    String[] GROUP_COLUMNS = {GROUP_ID, USER_ID, GROUP_NAME, GROUP_DESCRIPTION, IS_DELETE, IS_DEFAULT};
}
