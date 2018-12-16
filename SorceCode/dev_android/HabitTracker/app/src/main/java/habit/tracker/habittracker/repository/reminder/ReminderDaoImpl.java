package habit.tracker.habittracker.repository.reminder;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import habit.tracker.habittracker.api.model.reminder.Reminder;
import habit.tracker.habittracker.repository.MyDatabaseHelper;

public class ReminderDaoImpl extends MyDatabaseHelper implements ReminderDao, ReminderSchema {

    private Cursor cursor;

    private ContentValues initialValues;

    public ReminderDaoImpl(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public int delete(String reminderId) {
        try {
            final String selectionArgs[] = {reminderId};
            final String selection = REMINDER_ID + " = ?";

            return super.mDb.delete(REMINDER_TABLE, selection, selectionArgs);

        } catch (SQLiteConstraintException ignored) {
        }
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
        if (cursor.getColumnIndex(ReminderSchema.USER_ID) != -1) {
            entity.setUserId(cursor.getString(cursor.getColumnIndexOrThrow(ReminderSchema.USER_ID)));
        }
        if (cursor.getColumnIndex(REMIND_TEXT) != -1) {
            entity.setRemindText(cursor.getString(cursor.getColumnIndexOrThrow(REMIND_TEXT)));
        }
        if (cursor.getColumnIndex(REMINDER_START_TIME) != -1) {
            entity.setReminderStartTime(cursor.getString(cursor.getColumnIndexOrThrow(REMINDER_START_TIME)));
        }
        if (cursor.getColumnIndex(REMINDER_END_TIME) != -1) {
            entity.setReminderEndTime(cursor.getString(cursor.getColumnIndexOrThrow(REMINDER_END_TIME)));
        }
        if (cursor.getColumnIndex(REPEAT_TYPE) != -1) {
            entity.setRepeatType(cursor.getString(cursor.getColumnIndexOrThrow(REPEAT_TYPE)));
        }
        if (cursor.getColumnIndex(SERVICE_ID) != -1) {
            entity.setServerId(cursor.getString(cursor.getColumnIndexOrThrow(SERVICE_ID)));
        }
        if (cursor.getColumnIndex(IS_DELETE) != -1) {
            entity.setDelete(cursor.getString(cursor.getColumnIndexOrThrow(IS_DELETE)).equals("1"));
        }
        if (cursor.getColumnIndex(IS_UPDATE) != -1) {
            entity.setUpdate(cursor.getString(cursor.getColumnIndexOrThrow(IS_UPDATE)).equals("1"));
        }
        return entity;
    }

    private void setContentValue(ReminderEntity entity) {
        initialValues = new ContentValues();
        initialValues.put(REMINDER_ID, entity.getReminderId());
        initialValues.put(HABIT_ID, entity.getHabitId());
        initialValues.put(USER_ID, entity.getUserId());
        initialValues.put(REMIND_TEXT, entity.getRemindText());
        initialValues.put(REMINDER_START_TIME, entity.getReminderStartTime());
        initialValues.put(REMINDER_END_TIME, entity.getReminderEndTime());
        initialValues.put(REPEAT_TYPE, entity.getRepeatType());
        initialValues.put(SERVICE_ID, entity.getServerId());
        initialValues.put(IS_DELETE, entity.isDelete() ? "1" : "0");
        initialValues.put(IS_UPDATE, entity.isUpdate() ? "1" : "0");
    }

    private ContentValues getContentValue() {
        return initialValues;
    }

    @Override
    public String saveReminder(ReminderEntity reminderEntity) {
        setContentValue(reminderEntity);
        try {
            boolean isInserted = super.replace(REMINDER_TABLE, getContentValue()) > 0;
            if (isInserted) {
                cursor = super.rawQuery("SELECT * FROM " + ReminderSchema.REMINDER_TABLE
                        + " WHERE " + SERVICE_ID + " = '" + reminderEntity.getServerId() + "'", null);

                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    ReminderEntity entity = cursorToEntity(cursor);
                    return entity.getReminderId();
                }
            }
        } catch (SQLiteConstraintException ex) {
            Log.e("error", ex.toString());
        }
        return null;
    }

    public String saveReminder(ReminderEntity reminderEntity, String serverId) {
        try {
            final String selectionArgs[] = {serverId};
            final String selection = ReminderSchema.SERVICE_ID + " = ?";
            cursor = super.query(REMINDER_TABLE, REMINDER_COLUMNS, selection, selectionArgs, null);
            reminderEntity.setReminderId(null);
            reminderEntity.setServerId(serverId);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                ReminderEntity entity = cursorToEntity(cursor);
                reminderEntity.setReminderId(entity.getReminderId());
            }
            cursor = null;
            setContentValue(reminderEntity);
            boolean isInserted = super.replace(REMINDER_TABLE, getContentValue()) > 0;
            if (isInserted) {
                cursor = super.rawQuery("SELECT * FROM " + ReminderSchema.REMINDER_TABLE
                        + " WHERE " + SERVICE_ID + " = '" + reminderEntity.getServerId() + "'", null);
                cursor.moveToFirst();
                ReminderEntity entity = cursorToEntity(cursor);
                cursor.close();
                return entity.getReminderId();
            }
        } catch (SQLiteConstraintException ex) {
        }
        return null;
    }

    @Override
    public List<ReminderEntity> getRemindersByHabit(String habitId) {
        List<ReminderEntity> list = new ArrayList<>();
        final String selectionArgs[] = {habitId};
        final String selection = ReminderSchema.HABIT_ID + " = ?";
        cursor = super.query(REMINDER_TABLE, REMINDER_COLUMNS, selection, selectionArgs, REMINDER_START_TIME);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                list.add(cursorToEntity(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return list;
    }

    @Override
    public ReminderEntity getRemindersById(String id) {
        ReminderEntity res = null;
        final String selectionArgs[] = {id};
        final String selection = REMINDER_ID + " = ?";
        cursor = super.query(REMINDER_TABLE, REMINDER_COLUMNS, selection, selectionArgs, REMINDER_START_TIME);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            res = cursorToEntity(cursor);
            cursor.close();
        }
        return res;
    }

    public List<ReminderEntity> getReminderByUser(String userId) {
        List<ReminderEntity> list = new ArrayList<>();

        final String selectionArgs[] = {userId};
        final String selection = ReminderSchema.USER_ID + " = ?";

        cursor = super.query(REMINDER_TABLE, REMINDER_COLUMNS, selection, selectionArgs, REMINDER_START_TIME);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                list.add(cursorToEntity(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return list;
    }
}
