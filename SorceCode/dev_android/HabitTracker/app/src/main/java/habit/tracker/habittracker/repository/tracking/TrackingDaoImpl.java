package habit.tracker.habittracker.repository.tracking;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import habit.tracker.habittracker.api.model.tracking.Tracking;
import habit.tracker.habittracker.common.Generator;
import habit.tracker.habittracker.repository.MyDatabaseHelper;

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

    @Override
    public TrackingEntity getTracking(String trackId) {
        final String selectionArgs[] = {String.valueOf(trackId)};
        final String selection = TRACKING_ID + " = ?";
        TrackingEntity entity = new TrackingEntity();
        cursor = super.query(TRACKING_TABLE, TRACKING_COLUMNS, selection, selectionArgs, TRACKING_ID);
        if (cursor != null) {
            cursor.moveToFirst();
            entity = cursorToEntity(cursor);
            if (entity.getHabitId() == null) {
                entity.setHabitId(cursor.getString(cursor.getColumnIndexOrThrow(HABIT_ID)));
            }
            cursor.close();
        }
        return entity;
    }

    public TrackingEntity getTracking(String habitId, String currentDate) {
        TrackingEntity entity = new TrackingEntity();
        final String selectionArgs[] = {habitId, currentDate};
        final String selection = HABIT_ID + " = ? AND " + CURRENT_DATE + " = ?";
        cursor = super.query(TRACKING_TABLE, TRACKING_COLUMNS, selection, selectionArgs, TRACKING_ID);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                entity = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return entity;
    }

    public List<TrackingEntity> getTrackingByHabitId(String habitId) {
        List<TrackingEntity> list = new ArrayList<>();
        final String selectionArgs[] = {habitId};
        final String selection = HABIT_ID + " = ?";
        cursor = super.query(TRACKING_TABLE, TRACKING_COLUMNS, selection, selectionArgs, TRACKING_ID);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                list.add(cursorToEntity(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return list;
    }


    public List<TrackingEntity> getTrackingByDate(String currentDate) {
        List<TrackingEntity> list = new ArrayList<>();
        final String selectionArgs[] = {currentDate};
        final String selection = CURRENT_DATE + " = ?";
        cursor = super.query(TRACKING_TABLE, TRACKING_COLUMNS, selection, selectionArgs, CURRENT_DATE);
        if (cursor != null) {
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
    public boolean saveTracking(TrackingEntity entity) {
        setContentValue(entity);
        try {
            boolean res = super.replace(TRACKING_TABLE, getContentValue()) > 0;
//            Cursor cursor = super.rawQuery("SELECT * FROM " + TRACKING_TABLE + " ORDER BY " + TRACKING_ID + " DESC LIMIT 1", null);
//            if (cursor != null && cursor.moveToFirst()) {
//                entity = cursorToEntity(cursor);
//                this.lastId = entity.getTrackingId();
//            }
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
            return super.update(TRACKING_TABLE, getContentValue(), selection, selectionArgs) > 0;
        } catch (SQLiteConstraintException ex) {
            return false;
        }
    }

    public boolean updateTrackCount(String trackId, String count) {
        final String selectionArgs[] = {String.valueOf(trackId)};
        final String selection = TRACKING_ID + " = ?";
        initialValues = new ContentValues();
        initialValues.put(COUNT, count);
        try {
            boolean res = super.update(TRACKING_TABLE, initialValues, selection, selectionArgs) > 0;
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
