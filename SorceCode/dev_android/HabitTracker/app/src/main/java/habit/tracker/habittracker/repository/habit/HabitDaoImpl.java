package habit.tracker.habittracker.repository.habit;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import habit.tracker.habittracker.api.model.habit.Habit;
import habit.tracker.habittracker.repository.DatabaseHelper;

/**
 * Created by DatTVT1 on 10/16/2018
 */
public class HabitDaoImpl extends DatabaseHelper implements HabitDao, HabitSchema {
    private Cursor cursor;
    private ContentValues initialValues;

    public HabitDaoImpl(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public HabitEntity getHabit(String habitId) {
        final String selectionArgs[] = {habitId};
        final String selection = HABIT_ID + " = ?";
        HabitEntity habitEntity = new HabitEntity();
        cursor = super.query(HABIT_TABLE, HABIT_COLUMNS, selection, selectionArgs, HABIT_ID);
        if (cursor != null) {
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
    public boolean saveHabit(HabitEntity habitEntity) {
        setContentValue(habitEntity);
        try {
            return super.replace(HABIT_TABLE, getContentValue()) > 0;
        } catch (SQLiteConstraintException ex) {
            return false;
        }
    }

    @Override
    public boolean updateHabit(HabitEntity habitEntity) {
        return false;
    }

    @Override
    public boolean deleteHabit(String habitId) {
        return false;
    }

    @Override
    public int delete(String id) {
        return 0;
    }

    @Override
    protected HabitEntity cursorToEntity(Cursor cursor) {
        HabitEntity habitEntity = new HabitEntity();
        if (cursor != null) {
            if (cursor.getColumnIndex(HABIT_ID) != -1) {
                habitEntity.setHabitId(cursor.getString(cursor.getColumnIndexOrThrow(HABIT_ID)));
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
                habitEntity.setHabitDescription(cursor.getString(cursor.getColumnIndexOrThrow(HABIT_DESCRIPTION)));
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
        }
        return habitEntity;
    }

    public HabitEntity convert(Habit habit) {
        if (habit != null) {
            HabitEntity entity = new HabitEntity();
            entity.setHabitId(habit.getHabitId());
            entity.setUserId(habit.getUserId());
            entity.setGroupId(habit.getGroupId());
            entity.setMonitorId(habit.getMonitorId());
            entity.setHabitName(habit.getHabitName());
            entity.setHabitTarget(habit.getHabitTarget());
            entity.setHabitType(habit.getHabitType());
            entity.setMonitorType(habit.getMonitorType());
            entity.setMonitorUnit(habit.getMonitorUnit());
            entity.setMonitorNumber(habit.getMonitorNumber());
            entity.setStartDate(habit.getStartDate());
            entity.setEndDate(habit.getEndDate());
            entity.setHabitColor(habit.getHabitColor());
            entity.setHabitDescription(habit.getHabitDescription());
            entity.setMon(habit.getMon());
            entity.setTue(habit.getTue());
            entity.setWed(habit.getWed());
            entity.setThu(habit.getThu());
            entity.setFri(habit.getFri());
            entity.setSat(habit.getSat());
            entity.setSun(habit.getSun());
            return entity;
        }
        return null;
    }

    @Override
    public void setContentValue(HabitEntity habitEntity) {
        initialValues = new ContentValues();
        initialValues.put(HABIT_ID, habitEntity.getHabitId());
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
        initialValues.put(HABIT_DESCRIPTION, habitEntity.getHabitDescription());
        initialValues.put(MON, habitEntity.getMon());
        initialValues.put(TUE, habitEntity.getTue());
        initialValues.put(WED, habitEntity.getWed());
        initialValues.put(THU, habitEntity.getThu());
        initialValues.put(FRI, habitEntity.getFri());
        initialValues.put(SAT, habitEntity.getSat());
        initialValues.put(SUN, habitEntity.getSun());
    }

    @Override
    public ContentValues getContentValue() {
        return initialValues;
    }
}
