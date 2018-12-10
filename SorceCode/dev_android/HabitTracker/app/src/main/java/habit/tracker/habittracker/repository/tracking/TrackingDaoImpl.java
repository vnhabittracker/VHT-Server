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
import habit.tracker.habittracker.repository.habit.HabitTracking;

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

    public TrackingDaoImpl() {}

    @Override
    public int delete(String id) {
        return 0;
    }

    @Override
    public TrackingEntity cursorToEntity(Cursor cursor) {
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
            if (cursor.getColumnIndex(IS_UPDATED) != -1) {
                entity.setUpdate(cursor.getString(cursor.getColumnIndexOrThrow(IS_UPDATED)).equals("1"));
            }
        }
        return entity;
    }

    private ContentValues getContentValue() {
        return initialValues;
    }

    private void setContentValue(TrackingEntity entity) {
        initialValues = new ContentValues();
        initialValues.put(TRACKING_ID, entity.getTrackingId());
        initialValues.put(HABIT_ID, entity.getHabitId());
        initialValues.put(CURRENT_DATE, entity.getCurrentDate());
        initialValues.put(COUNT, entity.getCount());
        initialValues.put(TRACKING_DESCRIPTION, entity.getDescription());
        initialValues.put(IS_UPDATED, entity.isUpdate() ? "1" : "0");
    }

    public TrackingEntity convert(Tracking tracking) {
        TrackingEntity entity = new TrackingEntity();
        entity.setTrackingId(tracking.getTrackingId());
        entity.setHabitId(tracking.getHabitId());
        entity.setCount(tracking.getCount());
        entity.setCurrentDate(tracking.getCurrentDate());
        entity.setDescription(tracking.getDescription());
        entity.setUpdate(tracking.isUpdate());
        return entity;
    }

    public HabitTracking getHabitTrackingBetween(String habitId, String startDate, String endDate) {
        HabitTracking habitTracking = null;
        try {
            final String sql = "SELECT " + getParams(HabitSchema.HABIT_COLUMNS, "h", false) + getParams(TRACKING_COLUMNS, "t", true)
                    + " FROM " + HabitSchema.HABIT_TABLE + " h INNER JOIN " + TRACKING_TABLE + " t "
                    + " ON "
                    + "h." + HabitSchema.HABIT_ID + " = t." + TrackingSchema.HABIT_ID
                    + " WHERE "
                    + "t." + HABIT_ID + " = '" + habitId + "'"
                    + " AND t." + TrackingSchema.CURRENT_DATE + " BETWEEN '" + startDate + "' AND '" + endDate + "'  ORDER BY t." + CURRENT_DATE + " DESC";

            Cursor cursor = super.rawQuery(sql, null);

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                HabitDaoImpl habitDao = new HabitDaoImpl();
                habitTracking = new HabitTracking();
                habitTracking.setHabit(habitDao.cursorToEntity(cursor));
                habitTracking.getTrackingList().add(cursorToEntity(cursor));
                cursor.moveToNext();
                while (!cursor.isAfterLast()) {
                    habitTracking.getTrackingList().add(cursorToEntity(cursor));
                    cursor.moveToNext();
                }
                cursor.close();
                return habitTracking;
            }
        } catch (SQLiteConstraintException ex) {
        }
        return habitTracking;
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

    public int sumCountByHabit(String habitId, String start, String end) {
        int count = 0;

        final String sql = "SELECT SUM(" + COUNT + ") AS sumTracking FROM " + TRACKING_TABLE
                + " WHERE " + HABIT_ID + " = '" + habitId + "'"
                + " AND " + CURRENT_DATE + " >= '" + start + "'"
                + " AND " + CURRENT_DATE + " <= '" + end + "'";

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
    public List<TrackingEntity> getTrackingRecordsByHabit(String habitId) {
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
        return list;
    }

    public TrackingEntity getTracking(String trackId) {
        final String selectionArgs[] = {trackId};
        final String selection = TRACKING_ID + " = ?";

        TrackingEntity entity;
        cursor = super.query(TRACKING_TABLE, TRACKING_COLUMNS, selection, selectionArgs, CURRENT_DATE);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            entity = cursorToEntity(cursor);
            cursor.close();
            return entity;
        }
        return null;
    }

    public TrackingEntity getTracking(String habitId, String currentDate) {
        final String selectionArgs[] = {habitId, currentDate};
        final String selection = HABIT_ID + " = ? AND " + CURRENT_DATE + " = ?";

        TrackingEntity entity;
        cursor = super.query(TRACKING_TABLE, TRACKING_COLUMNS, selection, selectionArgs, CURRENT_DATE);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            entity = cursorToEntity(cursor);
            cursor.close();
            return entity;
        }
        return null;
    }

    @Override
    public boolean saveUpdateTracking(TrackingEntity entity) {
        setContentValue(entity);
        try {
            boolean res = super.replace(TRACKING_TABLE, getContentValue()) > 0;
            return res;
        } catch (SQLiteConstraintException ex) {
            return false;
        }
    }

    public boolean setUpdate(String trackId, boolean isUpdate) {
        final String sql = "UPDATE " + TRACKING_TABLE + " SET " + IS_UPDATED + " = " + (isUpdate ? "1" : "0")
                + " WHERE " + TRACKING_ID + " = '" + trackId + "'";

        cursor = super.rawQuery(sql, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        return false;
    }

    public String getParams(String[] columns, String alias, boolean removeEnd) {
        String str = "";
        for (int i = 0; i < columns.length; i++) {
            str = str + alias + "." + columns[i] + ", ";
            if (removeEnd && i == columns.length - 1) {
                return str.substring(0, str.length() - 2);
            }
        }
        return str;
    }
}
