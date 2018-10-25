package habit.tracker.habittracker.repository.habit;

import android.content.ContentValues;

/**
 * Created by DatTVT1 on 10/16/2018
 */
public interface HabitDao {
    HabitEntity getHabit(String habitId);
    boolean saveHabit(HabitEntity habitEntity);
    boolean updateHabit(HabitEntity habitEntity);
    boolean deleteHabit(String habitId);
    void setContentValue(HabitEntity habitEntity);
    ContentValues getContentValue();
}
