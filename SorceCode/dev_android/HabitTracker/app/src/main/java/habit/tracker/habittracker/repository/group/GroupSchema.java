package habit.tracker.habittracker.repository.group;

public interface GroupSchema {
    String GROUP_TABLE = "group_tb";
    String GROUP_ID = "group_id";
    String GROUP_NAME = "group_name";
    String PARENT_ID = "parent_id";
    String GROUP_ICON = "group_icon";
    String GROUP_DESCRIPTION = "group_description";
    String CREATE_GROUP_TABLE =
            "CREATE TABLE IF NOT EXISTS " + GROUP_TABLE + " ("
                    + GROUP_ID + " TEXT PRIMARY KEY NOT NULL, "
                    + GROUP_NAME + " TEXT, "
                    + PARENT_ID + " TEXT, "
                    + GROUP_ICON + " TEXT, "
                    + GROUP_DESCRIPTION + " TEXT "
                    + ")";
    String[] GROUP_COLUMNS = {GROUP_ID, GROUP_NAME, PARENT_ID, GROUP_ICON, GROUP_DESCRIPTION};
}
