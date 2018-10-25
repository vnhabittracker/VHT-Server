package habit.tracker.habittracker.repository.reminder;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import habit.tracker.habittracker.repository.DatabaseHelper;
import habit.tracker.habittracker.repository.tracking.TrackingEntity;

public class ReminderDaoImpl extends DatabaseHelper implements ReminderDao, ReminderSchema {
    Cursor cursor;
    private ContentValues initialValues;
    private String lastId;

    public String getLastId() {
        return lastId;
    }

    public ReminderDaoImpl(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public int delete(String id) {
        return 0;
    }

    @Override
    protected ReminderEntity cursorToEntity(Cursor cursor) {
        return null;
    }

    @Override
    public boolean addReminder(ReminderEntity entity) {
        return false;
    }

    private void setContentValue(TrackingEntity entity) {

    }

    private ContentValues getContentValue() {
        return initialValues;
    }
}
