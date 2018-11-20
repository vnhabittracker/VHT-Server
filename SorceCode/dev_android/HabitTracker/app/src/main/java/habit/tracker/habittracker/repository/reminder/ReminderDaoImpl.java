package habit.tracker.habittracker.repository.reminder;

import android.app.Notification;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import habit.tracker.habittracker.api.model.reminder.Reminder;
import habit.tracker.habittracker.repository.MyDatabaseHelper;
import habit.tracker.habittracker.repository.habit.HabitDaoImpl;
import habit.tracker.habittracker.repository.habit.HabitEntity;
import habit.tracker.habittracker.repository.habit.HabitNotification;
import habit.tracker.habittracker.repository.habit.HabitSchema;

public class ReminderDaoImpl extends MyDatabaseHelper implements ReminderDao, ReminderSchema {

    Cursor cursor;

    private ContentValues initialValues;

    public ReminderDaoImpl(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public int delete(String id) {
        return 0;
    }

    @Override
    protected ReminderEntity cursorToEntity(Cursor cursor) {
        ReminderEntity entity = new ReminderEntity();
        if (cursor.getColumnIndex(REMINDER_ID) != -1) {
            entity.setReminderId(cursor.getString(cursor.getColumnIndexOrThrow(REMINDER_ID)));
        }
        if (cursor.getColumnIndex(ReminderSchema.HABIT_ID) != -1) {
            entity.setHabitId(cursor.getString(cursor.getColumnIndexOrThrow(ReminderSchema.HABIT_ID)));
        }
        if (cursor.getColumnIndex(REMIND_TEXT) != -1) {
            entity.setRemindText(cursor.getString(cursor.getColumnIndexOrThrow(REMIND_TEXT)));
        }
        if (cursor.getColumnIndex(REMINDER_TIME) != -1) {
            entity.setReminderTime(cursor.getString(cursor.getColumnIndexOrThrow(REMINDER_TIME)));
        }
        if (cursor.getColumnIndex(REPEAT_TYPE) != -1) {
            entity.setRepeatType(cursor.getString(cursor.getColumnIndexOrThrow(REPEAT_TYPE)));
        }
        if (cursor.getColumnIndex(SERVICE_ID) != -1) {
            entity.setServerId(cursor.getString(cursor.getColumnIndexOrThrow(SERVICE_ID)));
        }
        return entity;
    }

    @Override
    public String saveReminder(ReminderEntity reminderEntity) {
        setContentValue(reminderEntity);
        try {
            boolean isInserted = super.replace(REMINDER_TABLE, getContentValue()) > 0;
            if (isInserted) {
                cursor = super.rawQuery("SELECT * FROM " + ReminderSchema.REMINDER_TABLE
                        + " WHERE "
                        + SERVICE_ID + " = '" + reminderEntity.getServerId() + "'", null);
                cursor.moveToFirst();
                ReminderEntity entity = cursorToEntity(cursor);
                return entity.getReminderId();
            }
        } catch (SQLiteConstraintException ex) {
        }
        return null;
    }

    @Override
    public List<ReminderEntity> getRemindersByHabit(String habitId) {
        List<ReminderEntity> res = new ArrayList<>();
        final String selectionArgs[] = {habitId};
        final String selection = ReminderSchema.HABIT_ID + " = ?";
        cursor = super.query(REMINDER_TABLE, REMINDER_COLUMNS, selection, selectionArgs, REMINDER_TIME);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                res.add(cursorToEntity(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return res;
    }

    @Override
    public ReminderEntity getRemindersById(String id) {
        ReminderEntity res = null;
        final String selectionArgs[] = {id};
        final String selection = REMINDER_ID + " = ?";
        cursor = super.query(REMINDER_TABLE, REMINDER_COLUMNS, selection, selectionArgs, REMINDER_TIME);
        if (cursor != null) {
            cursor.moveToFirst();
            res = cursorToEntity(cursor);
            cursor.close();
        }
        return res;
    }

    public HabitNotification getHabitNotifi(String remindId) {
        try {
            HabitDaoImpl habitDao = new HabitDaoImpl(null);
            cursor = super.rawQuery("SELECT * FROM " + REMINDER_TABLE
                            + " INNER JOIN " + HabitSchema.HABIT_TABLE
                            + " ON " + REMINDER_TABLE + "." + HABIT_ID + " = " + HabitSchema.HABIT_TABLE + "." + HABIT_ID
                            + " WHERE " + REMINDER_ID + " = " + remindId
                    , null);
            if (cursor != null) {
                cursor.moveToFirst();
                HabitNotification n = new HabitNotification();
                ReminderEntity rRntity = cursorToEntity(cursor);
                HabitEntity hEntity = habitDao.cursorToEntity(cursor);
                n.setHabitEntity(hEntity);
                n.setReminderEntity(rRntity);
                return n;
            }
        } catch (SQLiteConstraintException ex) {
        }
        return null;
    }

    private void setContentValue(ReminderEntity entity) {
        initialValues = new ContentValues();
        initialValues.put(REMINDER_ID, entity.getReminderId());
        initialValues.put(HABIT_ID, entity.getHabitId());
        initialValues.put(REMIND_TEXT, entity.getRemindText());
        initialValues.put(REMINDER_TIME, entity.getReminderTime());
        initialValues.put(REPEAT_TYPE, entity.getRepeatType());
        initialValues.put(SERVICE_ID, entity.getServerId());
    }

    private ContentValues getContentValue() {
        return initialValues;
    }

    public ReminderEntity convert(Reminder reminder) {
        ReminderEntity entity = new ReminderEntity();
        entity.setHabitId(reminder.getHabitId());
        entity.setRemindText(reminder.getRemindText());
        entity.setReminderTime(reminder.getReminderTime());
        entity.setRepeatType(reminder.getRepeatType());
        entity.setServerId(reminder.getServerId());
        return entity;
    }
}
