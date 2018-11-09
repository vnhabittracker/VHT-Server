package habit.tracker.habittracker.repository.tracking;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import habit.tracker.habittracker.api.model.tracking.Tracking;
import habit.tracker.habittracker.repository.MyDatabaseHelper;
import habit.tracker.habittracker.repository.habit.HabitDaoImpl;
import habit.tracker.habittracker.repository.habit.HabitSchema;

public class TrackingDaoImpl extends MyDatabaseHelper implements TrackingDao, TrackingSchema {
    private Cursor cursor;
    private ContentValues initialValues;
    private String lastId;

    public String getLastId() {
        return lastId;
    }

    public TrackingDaoImpl(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public int delete(String id) {
        return 0;
    }

    @Override
    protected TrackingEntity cursorToEntity(Cursor cursor) {
        TrackingEntity entity = new TrackingEntity();
        if (cursor != null) {
            if (cursor.getColumnIndex(TRACKING_ID) != -1) {
                entity.setTrackingId(cursor.getString(cursor.getColumnIndexOrThrow(TRACKING_ID)));
            }
            if (cursor.getColumnIndex(HABIT_ID) != -1) {
                entity.setHabitId(cursor.getString(cursor.getColumnIndexOrThrow(HABIT_ID)));
            }
            if (cursor.getColumnIndex(CURRENT_DATE) != -1) {
                entity.setCurrentDate(cursor.getString(cursor.getColumnIndexOrThrow(CURRENT_DATE)));
            }
            if (cursor.getColumnIndex(COUNT) != -1) {
                entity.setCount(cursor.getString(cursor.getColumnIndexOrThrow(COUNT)));
            }
            if (cursor.getColumnIndex(TRACKING_DESCRIPTION) != -1) {
                entity.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(TRACKING_DESCRIPTION)));
            }
        }
        return entity;
    }

    public HabitTracking getHabitTrackingBetween(String habitId, String startDate, String endDate) {
        try {
            Cursor cursor = super.rawQuery(
                    "SELECT * FROM " + HabitSchema.HABIT_TABLE + " INNER JOIN " + TRACKING_TABLE
                            + " ON "
                            + TrackingSchema.TRACKING_TABLE + "." + TrackingSchema.HABIT_ID + " = " + HabitSchema.HABIT_TABLE + "." + HabitSchema.HABIT_ID
                            + " WHERE "
                            + TrackingSchema.TRACKING_TABLE + "." + TrackingSchema.CURRENT_DATE
                            + " BETWEEN "
                            + "'" + startDate + "' AND '" + endDate + "'"
                            + " AND "
                            + HabitSchema.HABIT_TABLE + "." + HabitSchema.HABIT_ID + " = '" + habitId + "'"
                    , null);

            if (cursor != null && cursor.getCount() > 0) {
                HabitTracking habitTracking = new HabitTracking();
                HabitDaoImpl habitDao = new HabitDaoImpl();

                cursor.moveToFirst();
                habitTracking.setHabitEntity(habitDao.cursorToEntity(cursor));

                while (!cursor.isAfterLast()) {
                    habitTracking.getTrackingEntityList().add(cursorToEntity(cursor));
                    cursor.moveToNext();
                }
                cursor.close();
                return habitTracking;
            }
        } catch (SQLiteConstraintException ex) {
        }
        return null;
    }

    public int countTrackByUser(String userId) {
        int count = 0;

        final String sql = "SELECT COUNT(*) as countTracking FROM " + TRACKING_TABLE + " INNER JOIN " + HabitSchema.HABIT_TABLE
                + " ON " + TRACKING_TABLE + "." + HABIT_ID + " = " + HabitSchema.HABIT_TABLE + "." + HabitSchema.HABIT_ID
                + " WHERE " + HabitSchema.HABIT_TABLE + "." + HabitSchema.USER_ID + " = '" + userId + "'";
        cursor = super.rawQuery(sql, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            if (cursor.getColumnIndex("countTracking") != -1) {
                count = cursor.getInt(cursor.getColumnIndexOrThrow("countTracking"));
            }
        }
        return count;
    }

    public int sumCountByHabit(String habitId) {
        int count = 0;

        final String sql = "SELECT SUM(" + COUNT + ") as sumTracking FROM " + TRACKING_TABLE
                + " WHERE " + HABIT_ID + " = '" + habitId + "'";
        cursor = super.rawQuery(sql, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            if (cursor.getColumnIndex("sumTracking") != -1) {
                count = cursor.getInt(cursor.getColumnIndexOrThrow("sumTracking"));
            }
        }
        return count;
    }

    @Override
    public List<TrackingEntity> getRecordByHabit(String habitId) {
        List<TrackingEntity> list = new ArrayList<>();
        final String selectionArgs[] = {String.valueOf(habitId)};
        final String selection = HABIT_ID + " = ?";
        cursor = super.query(TRACKING_TABLE, TRACKING_COLUMNS, selection, selectionArgs, CURRENT_DATE);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                list.add(cursorToEntity(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        if (list.size() > 0) {
            return list;
        }
        return null;
    }

    public TrackingEntity getTracking(String habitId, String currentDate) {
        final String selectionArgs[] = {habitId, currentDate};
        final String selection = HABIT_ID + " = ? AND " + CURRENT_DATE + " = ?";

        TrackingEntity entity;
        cursor = super.query(TRACKING_TABLE, TRACKING_COLUMNS, selection, selectionArgs, TRACKING_ID);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            entity = cursorToEntity(cursor);
            cursor.close();
            return entity;
        }
        return null;
    }

    @Override
    public boolean saveTracking(TrackingEntity entity) {
        setContentValue(entity);
        try {
            boolean res = super.replace(TRACKING_TABLE, getContentValue()) > 0;
            return res;
        } catch (SQLiteConstraintException ex) {
            return false;
        }
    }

    @Override
    public boolean updateTracking(TrackingEntity entity) {
        final String selectionArgs[] = {String.valueOf(entity.getTrackingId())};
        final String selection = TRACKING_ID + " = ?";
        setContentValue(entity);
        try {
            boolean res = super.update(TRACKING_TABLE, getContentValue(), selection, selectionArgs) > 0;
            return res;
        } catch (SQLiteConstraintException ex) {
            return false;
        }
    }

    private void setContentValue(TrackingEntity entity) {
        initialValues = new ContentValues();
        if (entity.getTrackingId() != null) {
            initialValues.put(TRACKING_ID, entity.getTrackingId());
        }
        if (entity.getHabitId() != null) {
            initialValues.put(HABIT_ID, entity.getHabitId());
        }
        if (entity.getCurrentDate() != null) {
            initialValues.put(CURRENT_DATE, entity.getCurrentDate());
        }
        if (entity.getCount() != null) {
            initialValues.put(COUNT, entity.getCount());
        }
        if (entity.getDescription() != null) {
            initialValues.put(TRACKING_DESCRIPTION, entity.getDescription());
        }
    }

    private ContentValues getContentValue() {
        return initialValues;
    }

    public TrackingEntity convert(Tracking track) {
        TrackingEntity entity = new TrackingEntity();
        entity.setTrackingId(track.getTrackingId());
        entity.setHabitId(track.getHabitId());
        entity.setCount(track.getCount());
        entity.setCurrentDate(track.getCurrentDate());
        return entity;
    }
}
