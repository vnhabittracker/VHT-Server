package habit.tracker.habittracker.common.pushservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import habit.tracker.habittracker.api.VnHabitApiUtils;
import habit.tracker.habittracker.api.model.suggestion.HabitSuggestion;
import habit.tracker.habittracker.api.service.VnHabitApiService;
import habit.tracker.habittracker.common.util.AppGenerator;
import habit.tracker.habittracker.repository.Database;
import habit.tracker.habittracker.repository.habit.HabitEntity;
import habit.tracker.habittracker.repository.habit.HabitTracking;
import habit.tracker.habittracker.repository.tracking.TrackingEntity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class PushDataReceiver extends BroadcastReceiver {
    VnHabitApiService mService = VnHabitApiUtils.getApiService();
    public static final String TYPE_DAILY = "0";
    public static final String TYPE_WEEKLY = "1";
    public static final String TYPE_MONTHLY = "2";
    public static final String TYPE_YEARLY = "3";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() != null) {
            String userId = intent.getStringExtra(PushDataService.USER_ID);
            String currentDate = AppGenerator.getCurrentDate(AppGenerator.YMD_SHORT);

            Database db = Database.getInstance(context);
            db.open();
            List<HabitEntity> habitList = Database.getHabitDb().getActiveHabitByUser(userId, currentDate);

            if (habitList != null && habitList.size() > 0) {
                int distance = 0;
                int totalCount = 0;
                int successCount = 0;
                int goalNumber = 0;
                String lastSynDate;
                for (HabitEntity habit : habitList) {
                    goalNumber = Integer.parseInt(habit.getMonitorNumber());
                    if (!TextUtils.isEmpty(habit.getLastDateSyn())) {
                        lastSynDate = habit.getLastDateSyn();
                    } else {
                        lastSynDate = habit.getStartDate();
                    }

                    // number of days between last syn date and current date
                    distance = AppGenerator.countDayBetween(lastSynDate, currentDate);

                    switch (habit.getHabitType()) {
                        case TYPE_DAILY:
                            TrackingEntity track;
                            for (int i = 0; i < distance; i++) {
                                track = Database.getTrackingDb().getTracking(habit.getHabitId(), lastSynDate);
                                if (Integer.parseInt(track.getCount()) >= Integer.parseInt(habit.getMonitorNumber())) {
                                    successCount++;
                                }
                                lastSynDate = AppGenerator.getNextDate(lastSynDate, AppGenerator.YMD_SHORT);
                            }
                            totalCount = distance;
                            break;

                        case TYPE_WEEKLY:
                            Calendar ca = Calendar.getInstance();
                            Date d;
                            String pre = lastSynDate;
                            String[] diw;
                            int sumPerWeek = 0;
                            HabitTracking weekData;
                            for (int i = 0; i < distance; i++) {
                                lastSynDate = AppGenerator.getNextDate(lastSynDate, AppGenerator.YMD_SHORT);
                                d = AppGenerator.getDate(lastSynDate, AppGenerator.YMD_SHORT);
                                ca.setTime(d);

                                // on monday we will check moveToPre week data
                                if (ca.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                                    // one track per week
                                    totalCount++;
                                    diw = AppGenerator.getDatesInWeek(pre);
                                    // get data from moveToPre week
                                    weekData = Database.getTrackingDb().getHabitTrackingBetween(habit.getHabitId(), diw[0], diw[6]);
                                    for (TrackingEntity entity : weekData.getTrackingList()) {
                                        sumPerWeek += entity.getIntCount();
                                    }
                                    if (sumPerWeek >= goalNumber) {
                                        successCount++;
                                    }
                                }
                                sumPerWeek = 0;
                                pre = lastSynDate;
                            }
                            break;

                        case TYPE_MONTHLY:
                            for (int i = 0; i < distance; i++) {

                            }
                            break;

                        case TYPE_YEARLY:
                            break;
                    }
                    habit.setLastDateSyn(lastSynDate);
                    Database.getHabitDb().saveUpdateHabit(habit);
                    HabitSuggestion habitSuggestion = new HabitSuggestion();
                    habitSuggestion.setHabitSearchNameId(habit.getHabitId());
                    habitSuggestion.setTotalTrack(totalCount);
                    habitSuggestion.setSuccessTrack(successCount);
                    mService.updateTrackNameStatus(habitSuggestion).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                        }
                    });

                    totalCount = 0;
                    successCount = 0;
                }
            }
            db.close();
        }
    }
}
