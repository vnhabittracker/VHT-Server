package habit.tracker.habittracker.repository.habit;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import habit.tracker.habittracker.api.model.habit.Habit;
import habit.tracker.habittracker.repository.MyDatabaseHelper;
import habit.tracker.habittracker.repository.tracking.TrackingDaoImpl;
import habit.tracker.habittracker.repository.tracking.TrackingEntity;
import habit.tracker.habittracker.repository.tracking.TrackingSchema;

/**
 * Created on 10/16/2018
 */
public class HabitDaoImpl extends MyDatabaseHelper implements HabitDao, HabitSchema, TrackingSchema {
    private Cursor cursor;
    private ContentValues initialValues;

    public HabitDaoImpl(SQLiteDatabase db) {
        super(db);
    }

    public HabitDaoImpl() {
    }

    @Override
    public List<HabitTracking> getHabitTracking(String userId, String startDate, String endDate) {
        List<HabitTracking> list = new ArrayList<>();
        try {
            final String sql = "SELECT " + getParams(HABIT_COLUMNS, "h", false) + getParams(TRACKING_COLUMNS, "t", true)
                    + " FROM " + HABIT_TABLE + " h INNER JOIN " + TRACKING_TABLE + " t "
                    + " ON " + "h." + HabitSchema.HABIT_ID + " = t." + TrackingSchema.HABIT_ID
                    + " WHERE h." + USER_ID + " = '" + userId + "'"
                    + " AND t." + TrackingSchema.CURRENT_DATE + " BETWEEN '" + startDate + "' AND '" + endDate + "'" +
                    " ORDER BY h." + HabitSchema.HABIT_ID + " ASC";

            cursor = super.rawQuery(sql, null);

            if (cursor != null && cursor.getCount() > 0) {
                TrackingDaoImpl trackingDao = new TrackingDaoImpl();

                cursor.moveToFirst();

                // add first item
                HabitTracking habitTracking = new HabitTracking();
                HabitEntity habit = cursorToEntity(cursor);
                TrackingEntity track = trackingDao.cursorToEntity(cursor);
                habitTracking.setHabit(habit);
                habitTracking.getTrackingList().add(track);
                list.add(habitTracking);

                cursor.moveToNext();
                while (!cursor.isAfterLast()) {

                    habit = cursorToEntity(cursor);
                    track = trackingDao.cursorToEntity(cursor);

                    if (list.get(list.size() - 1).getHabit().getHabitId().equals(habit.getHabitId())) {
                        list.get(list.size() - 1).getTrackingList().add(track);
                    } else {
                        habitTracking = new HabitTracking();
                        habitTracking.setHabit(habit);
                        habitTracking.getTrackingList().add(track);
                        list.add(habitTracking);
                    }
                    cursor.moveToNext();
                }

                cursor.close();
                return list;
            }

        } catch (SQLiteConstraintException ex) {
        }
        return list;
    }

    @Override
    public List<HabitEntity> getHabitByUser(String userId) {
        List<HabitEntity> list = new ArrayList<>();
        final String selectionArgs[] = {userId};
        final String selection = HabitSchema.USER_ID + " = ?";

        cursor = super.query(HABIT_TABLE, HABIT_COLUMNS, selection, selectionArgs, null);
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

    public List<HabitEntity> getActiveHabitByUser(String userId, String currentDate) {
        List<HabitEntity> list = new ArrayList<>();
        final String sql = "SELECT * FROM " + HABIT_TABLE
                + " WHERE " + USER_ID + " = '" + userId + "'"
                + " AND (" + END_DATE + " IS NULL"
                + " OR " + currentDate + " <= " + END_DATE;

        cursor = super.rawQuery(sql, null);
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

    public int countHabitByUser(String userId) {
        int count = 0;
        final String sql = "SELECT count(*) AS habit_count FROM " + HABIT_TABLE + " WHERE " + USER_ID + " = '" + userId + "'";
        cursor = super.rawQuery(sql, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            if (cursor.getColumnIndex("habit_count") != -1) {
                count = cursor.getInt(cursor.getColumnIndexOrThrow("habit_count"));
            }
        }
        return count;
    }

    public List<HabitEntity> getTodayHabit(TrackingDateInWeek date, String currentDate, String userId) {
        List<HabitEntity> list = new ArrayList<>();

        final String sql = "SELECT * FROM " + HabitSchema.HABIT_TABLE
                + " WHERE " + USER_ID + " = '" + userId + "'"
                + " AND ( '" + currentDate + "' >= " + HabitSchema.START_DATE + ")"
                + " AND ( " + HabitSchema.END_DATE + " IS NULL OR '" + currentDate + "' <= " + HabitSchema.END_DATE + ")"
                + " AND " + getTodayCond(date);

        cursor = super.rawQuery(sql, null);

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

    private String getTodayCond(TrackingDateInWeek date) {
        String str = "";
        if (date.getMon().equals("1")) {
            str = HabitSchema.MON;
        } else if (date.getTue().equals("1")) {
            str = HabitSchema.TUE;
        } else if (date.getWed().equals("1")) {
            str = HabitSchema.WED;
        } else if (date.getThu().equals("1")) {
            str = HabitSchema.THU;
        } else if (date.getFri().equals("1")) {
            str = HabitSchema.FRI;
        } else if (date.getSat().equals("1")) {
            str = HabitSchema.SAT;
        } else if (date.getSun().equals("1")) {
            str = HabitSchema.SUN;
        }
        return str + " = 1";
    }

    @Override
    public HabitEntity getHabit(String habitId) {
        final String selectionArgs[] = {habitId};
        final String selection = HabitSchema.HABIT_ID + " = ?";

        HabitEntity habitEntity = new HabitEntity();

        cursor = super.query(HABIT_TABLE, HABIT_COLUMNS, selection, selectionArgs, HabitSchema.HABIT_ID);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                habitEntity = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
            return habitEntity;
        }
        return null;
    }

    @Override
    public boolean saveUpdateHabit(HabitEntity habitEntity) {
        setContentValue(habitEntity);
        try {
            return super.replace(HABIT_TABLE, getContentValue()) > 0;
        } catch (SQLiteConstraintException ex) {
            return false;
        }
    }

    @Override
    public int delete(String habitId) {
        try {
            final String selectionArgs[] = {habitId};
            final String selection = HabitSchema.HABIT_ID + " = ?";
            return super.mDb.delete(HABIT_TABLE, selection, selectionArgs)
                    + super.mDb.delete(TRACKING_TABLE, selection, selectionArgs);
        } catch (SQLiteConstraintException ex) {
        }
        return 0;
    }

    public boolean setUpdate(String habitId, boolean isUpdate) {
        final String sql = "UPDATE " + HABIT_TABLE + " SET " + HabitSchema.IS_UPDATED + " = " + (isUpdate ? "1" : "0")
                + " WHERE " + HabitSchema.HABIT_ID + " = '" + habitId + "'";

        cursor = super.rawQuery(sql, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        return false;
    }

    @Override
    public HabitEntity cursorToEntity(Cursor cursor) {
        HabitEntity habitEntity = new HabitEntity();
        if (cursor != null) {
            if (cursor.getColumnIndex(HabitSchema.HABIT_ID) != -1) {
                habitEntity.setHabitId(cursor.getString(cursor.getColumnIndexOrThrow(HabitSchema.HABIT_ID)));
            }
            if (cursor.getColumnIndex(USER_ID) != -1) {
                habitEntity.setUserId(cursor.getString(cursor.getColumnIndexOrThrow(USER_ID)));
            }
            if (cursor.getColumnIndex(GROUP_ID) != -1) {
                habitEntity.setGroupId(cursor.getString(cursor.getColumnIndexOrThrow(GROUP_ID)));
            }
            if (cursor.getColumnIndex(MONITOR_ID) != -1) {
                habitEntity.setMonitorId(cursor.getString(cursor.getColumnIndexOrThrow(MONITOR_ID)));
            }
            if (cursor.getColumnIndex(HABIT_NAME) != -1) {
                habitEntity.setHabitName(cursor.getString(cursor.getColumnIndexOrThrow(HABIT_NAME)));
            }
            if (cursor.getColumnIndex(HABIT_TARGET) != -1) {
                habitEntity.setHabitTarget(cursor.getString(cursor.getColumnIndexOrThrow(HABIT_TARGET)));
            }
            if (cursor.getColumnIndex(HABIT_TYPE) != -1) {
                habitEntity.setHabitType(cursor.getString(cursor.getColumnIndexOrThrow(HABIT_TYPE)));
            }
            if (cursor.getColumnIndex(MONITOR_TYPE) != -1) {
                habitEntity.setMonitorType(cursor.getString(cursor.getColumnIndexOrThrow(MONITOR_TYPE)));
            }
            if (cursor.getColumnIndex(MONITOR_UNIT) != -1) {
                habitEntity.setMonitorUnit(cursor.getString(cursor.getColumnIndexOrThrow(MONITOR_UNIT)));
            }
            if (cursor.getColumnIndex(MONITOR_NUMBER) != -1) {
                habitEntity.setMonitorNumber(cursor.getString(cursor.getColumnIndexOrThrow(MONITOR_NUMBER)));
            }
            if (cursor.getColumnIndex(START_DATE) != -1) {
                habitEntity.setStartDate(cursor.getString(cursor.getColumnIndexOrThrow(START_DATE)));
            }
            if (cursor.getColumnIndex(END_DATE) != -1) {
                habitEntity.setEndDate(cursor.getString(cursor.getColumnIndexOrThrow(END_DATE)));
            }
            if (cursor.getColumnIndex(CREATED_DATE) != -1) {
                habitEntity.setCreatedDate(cursor.getString(cursor.getColumnIndexOrThrow(CREATED_DATE)));
            }
            if (cursor.getColumnIndex(HABIT_COLOR) != -1) {
                habitEntity.setHabitColor(cursor.getString(cursor.getColumnIndexOrThrow(HABIT_COLOR)));
            }
            if (cursor.getColumnIndex(HABIT_DESCRIPTION) != -1) {
                habitEntity.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(HABIT_DESCRIPTION)));
            }
            if (cursor.getColumnIndex(MON) != 1) {
                habitEntity.setMon(cursor.getString(cursor.getColumnIndexOrThrow(MON)));
            }
            if (cursor.getColumnIndex(TUE) != 1) {
                habitEntity.setTue(cursor.getString(cursor.getColumnIndexOrThrow(TUE)));
            }
            if (cursor.getColumnIndex(WED) != 1) {
                habitEntity.setWed(cursor.getString(cursor.getColumnIndexOrThrow(WED)));
            }
            if (cursor.getColumnIndex(THU) != 1) {
                habitEntity.setThu(cursor.getString(cursor.getColumnIndexOrThrow(THU)));
            }
            if (cursor.getColumnIndex(FRI) != 1) {
                habitEntity.setFri(cursor.getString(cursor.getColumnIndexOrThrow(FRI)));
            }
            if (cursor.getColumnIndex(SAT) != 1) {
                habitEntity.setSat(cursor.getString(cursor.getColumnIndexOrThrow(SAT)));
            }
            if (cursor.getColumnIndex(SUN) != 1) {
                habitEntity.setSun(cursor.getString(cursor.getColumnIndexOrThrow(SUN)));
            }
            if (cursor.getColumnIndex(HABIT_NAME_ID) != 1) {
                habitEntity.setHabitNameId(cursor.getString(cursor.getColumnIndexOrThrow(HABIT_NAME_ID)));
            }
            if (cursor.getColumnIndex(HABIT_NAME_ASCII) != 1) {
                habitEntity.setHabitNameAscii(cursor.getString(cursor.getColumnIndexOrThrow(HABIT_NAME_ASCII)));
            }
            if (cursor.getColumnIndex(LAST_DATE_SYN) != 1) {
                habitEntity.setLastDateSyn(cursor.getString(cursor.getColumnIndexOrThrow(LAST_DATE_SYN)));
            }
            if (cursor.getColumnIndex(IS_DELETE) != 1) {
                habitEntity.setDelete(cursor.getString(cursor.getColumnIndexOrThrow(IS_DELETE)).equals("1"));
            }
            if (cursor.getColumnIndex(HabitSchema.IS_UPDATED) != 1) {
                habitEntity.setUpdate(cursor.getString(cursor.getColumnIndexOrThrow(HabitSchema.IS_UPDATED)).equals("1"));
            }
        }
        return habitEntity;
    }

    @Override
    public void setContentValue(HabitEntity habitEntity) {
        initialValues = new ContentValues();
        initialValues.put(HabitSchema.HABIT_ID, habitEntity.getHabitId());
        initialValues.put(USER_ID, habitEntity.getUserId());
        initialValues.put(GROUP_ID, habitEntity.getGroupId());
        initialValues.put(MONITOR_ID, habitEntity.getMonitorId());
        initialValues.put(HABIT_NAME, habitEntity.getHabitName());
        initialValues.put(HABIT_TARGET, habitEntity.getHabitTarget());
        initialValues.put(HABIT_TYPE, habitEntity.getHabitType());
        initialValues.put(MONITOR_TYPE, habitEntity.getMonitorType());
        initialValues.put(MONITOR_UNIT, habitEntity.getMonitorUnit());
        initialValues.put(MONITOR_NUMBER, habitEntity.getMonitorNumber());
        initialValues.put(START_DATE, habitEntity.getStartDate());
        initialValues.put(END_DATE, habitEntity.getEndDate());
        initialValues.put(CREATED_DATE, habitEntity.getCreatedDate());
        initialValues.put(HABIT_COLOR, habitEntity.getHabitColor());
        initialValues.put(HABIT_DESCRIPTION, habitEntity.getDescription());
        initialValues.put(MON, habitEntity.getMon());
        initialValues.put(TUE, habitEntity.getTue());
        initialValues.put(WED, habitEntity.getWed());
        initialValues.put(THU, habitEntity.getThu());
        initialValues.put(FRI, habitEntity.getFri());
        initialValues.put(SAT, habitEntity.getSat());
        initialValues.put(SUN, habitEntity.getSun());
        initialValues.put(HABIT_NAME_ID, habitEntity.getHabitNameId());
        initialValues.put(HABIT_NAME_ASCII, habitEntity.getHabitNameAscii());
        initialValues.put(LAST_DATE_SYN, habitEntity.getLastDateSyn());
        initialValues.put(IS_DELETE, habitEntity.isDelete() ? "1" : "0");
        initialValues.put(HabitSchema.IS_UPDATED, habitEntity.isUpdate() ? "1" : "0");
    }

    @Override
    public ContentValues getContentValue() {
        return initialValues;
    }
}
