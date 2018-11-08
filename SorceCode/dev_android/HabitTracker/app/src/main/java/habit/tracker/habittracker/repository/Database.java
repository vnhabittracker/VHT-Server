package habit.tracker.habittracker.repository;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import habit.tracker.habittracker.repository.group.GroupDaoImpl;
import habit.tracker.habittracker.repository.group.GroupSchema;
import habit.tracker.habittracker.repository.habit.HabitDaoImpl;
import habit.tracker.habittracker.repository.habit.HabitSchema;
import habit.tracker.habittracker.repository.reminder.ReminderDaoImpl;
import habit.tracker.habittracker.repository.reminder.ReminderSchema;
import habit.tracker.habittracker.repository.tracking.TrackingDaoImpl;
import habit.tracker.habittracker.repository.tracking.TrackingSchema;
import habit.tracker.habittracker.repository.user.UserDaoImpl;
import habit.tracker.habittracker.repository.user.UserSchema;

/**
 * Created by DatTVT1 on 10/12/2018
 */
public class Database {
    private static final String DATABASE_NAME = "vnhabit";
    private static final int DATABASE_VERSION = 1;

    private DatabaseHelper dbHelper;
    private final Context mContext;

    public static UserDaoImpl userDaoImpl;
    public static HabitDaoImpl habitDaoImpl;
    public static GroupDaoImpl groupDaoImpl;
    public static TrackingDaoImpl trackingImpl;
    public static ReminderDaoImpl reminderImpl;

    public static UserDaoImpl getUser() {
        return userDaoImpl;
    }

    public static HabitDaoImpl getHabitDb() {
        return habitDaoImpl;
    }

    public static GroupDaoImpl getGroupDb() {
        return groupDaoImpl;
    }

    public static TrackingDaoImpl getTrackingDb() {
        return trackingImpl;
    }

    public static ReminderDaoImpl getReminderDb() {
        return reminderImpl;
    }

    public Database(Context context) {
        this.mContext = context;
    }
    private static Database sInstance;

    public static synchronized Database getInstance(Context context) {
        if (sInstance == null) {
            return sInstance = new Database(context);
        }
        return sInstance;
    }

    public Database open() throws SQLException {
        dbHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        userDaoImpl = new UserDaoImpl(db);
        habitDaoImpl = new HabitDaoImpl(db);
        groupDaoImpl = new GroupDaoImpl(db);
        trackingImpl = new TrackingDaoImpl(db);
        reminderImpl = new ReminderDaoImpl(db);
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public static class DatabaseHelper extends SQLiteOpenHelper implements UserSchema, HabitSchema, GroupSchema, TrackingSchema, ReminderSchema {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_USER_TABLE);
            db.execSQL(CREATE_HABIT_TABLE);
            db.execSQL(CREATE_GROUP_TABLE);
            db.execSQL(CREATE_TRACKING_TABLE);
            db.execSQL(CREATE_REMINDER_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + HABIT_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + GROUP_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + TRACKING_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + REMINDER_TABLE);
            onCreate(db);
        }
    }
}
