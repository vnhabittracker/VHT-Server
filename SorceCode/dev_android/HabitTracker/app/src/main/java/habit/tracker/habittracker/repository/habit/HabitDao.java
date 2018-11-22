package habit.tracker.habittracker.repository.habit;

import android.content.ContentValues;

import java.util.List;

/**
 * Created by DatTVT1 on 10/16/2018
 */
public interface HabitDao {
    List<HabitTracking> getHabitTracking(String userId, String startDate, String endDate);
    List<HabitEntity> getHabitByUser(String userId);
    HabitEntity getHabit(String habitId);
    boolean saveUpdateHabit(HabitEntity habitEntity);
    boolean deleteHabit(String habitId);
    void setContentValue(HabitEntity habitEntity);
    ContentValues getContentValue();
}
