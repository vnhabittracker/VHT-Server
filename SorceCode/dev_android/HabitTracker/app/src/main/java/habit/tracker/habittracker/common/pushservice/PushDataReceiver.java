package habit.tracker.habittracker.common.pushservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import java.util.Calendar;
import java.util.List;

import habit.tracker.habittracker.api.VnHabitApiUtils;
import habit.tracker.habittracker.api.model.suggestion.HabitSuggestion;
import habit.tracker.habittracker.api.model.user.UpdateScoreRequest;
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
                int userScore = 0;
                int habitGoalNumber;
                String lastSynDate;

                for (HabitEntity habit : habitList) {
                    habitGoalNumber = Integer.parseInt(habit.getMonitorNumber());
                    if (!TextUtils.isEmpty(habit.getLastDateSyn())) {
                        lastSynDate = habit.getLastDateSyn();
                    } else {
                        lastSynDate = habit.getStartDate();
                    }

                    // number of days between last syn date and current date
                    distance = AppGenerator.countDayBetween(lastSynDate, currentDate);

                    switch (habit.getHabitType()) {
                        case TYPE_DAILY:
                            TrackingEntity trackingEntity;
                            for (int i = 0; i < distance; i++) {
                                trackingEntity = Database.getTrackingDb().getTracking(habit.getHabitId(), lastSynDate);
                                if (Integer.parseInt(trackingEntity.getCount()) >= Integer.parseInt(habit.getMonitorNumber())) {
                                    successCount++;
                                    userScore += 1;
                                }
                                lastSynDate = AppGenerator.getNextDate(lastSynDate, AppGenerator.YMD_SHORT);
                            }
                            totalCount = distance;
                            break;

                        case TYPE_WEEKLY:
                            Calendar calendar = Calendar.getInstance();
                            String datePreWeek = lastSynDate;
                            String[] diw;
                            int sumInWeek = 0;
                            HabitTracking weekData;
                            if (distance >= 7) {
                                for (int i = 0; i < distance; i++) {
                                    lastSynDate = AppGenerator.getNextDate(lastSynDate, AppGenerator.YMD_SHORT);
                                    calendar.setTime(AppGenerator.getDate(lastSynDate, AppGenerator.YMD_SHORT));
                                    // on monday we will check pre week data
                                    if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                                        // one track per week
                                        totalCount++;
                                        diw = AppGenerator.getDatesInWeek(datePreWeek);
                                        // get data from pre week
                                        weekData = Database.getTrackingDb().getHabitTrackingBetween(habit.getHabitId(), diw[0], diw[6]);
                                        for (TrackingEntity entity : weekData.getTrackingList()) {
                                            sumInWeek += entity.getIntCount();
                                        }
                                        if (sumInWeek >= habitGoalNumber) {
                                            successCount++;
                                            userScore += 3;
                                        }
                                    }
                                    sumInWeek = 0;
                                    datePreWeek = lastSynDate;
                                }
                            }
                            break;

                        case TYPE_MONTHLY:
                            HabitTracking monthData;
                            String lastDateInMonth = AppGenerator.getLastDateInMonth(lastSynDate, AppGenerator.YMD_SHORT, AppGenerator.YMD_SHORT);
                            int sumInMonth = 0;
                            while (lastDateInMonth.compareTo(currentDate) < 0) {
                                monthData = Database.getTrackingDb().getHabitTrackingBetween(habit.getHabitId(), lastSynDate, lastDateInMonth);
                                for (TrackingEntity entity : monthData.getTrackingList()) {
                                    sumInMonth += entity.getIntCount();
                                }
                                if (sumInMonth >= habitGoalNumber) {
                                    successCount++;
                                    userScore += 12;
                                }
                                totalCount++;
                                lastSynDate = AppGenerator.getFirstDateNextMonth(lastSynDate, AppGenerator.YMD_SHORT, AppGenerator.YMD_SHORT);
                                lastDateInMonth = AppGenerator.getLastDateInMonth(lastSynDate, AppGenerator.YMD_SHORT, AppGenerator.YMD_SHORT);
                            }
                            break;

                        case TYPE_YEARLY:
                            HabitTracking yearData;
                            String lastDateInYear = AppGenerator.getLastDateInYear(lastSynDate, AppGenerator.YMD_SHORT, AppGenerator.YMD_SHORT);
                            int sumInYear = 0;
                            if (lastDateInYear.compareTo(currentDate) < 0) {
                                yearData = Database.getTrackingDb().getHabitTrackingBetween(habit.getHabitId(), lastSynDate, lastDateInYear);
                                for (TrackingEntity entity: yearData.getTrackingList()) {
                                    sumInYear += entity.getIntCount();
                                }
                                if (sumInYear >= habitGoalNumber) {
                                    successCount++;
                                    userScore += 150;
                                }
                                totalCount++;
                                lastSynDate = AppGenerator.getFirstDateNextYear(lastSynDate, AppGenerator.YMD_SHORT, AppGenerator.YMD_SHORT);
                                lastDateInYear = AppGenerator.getLastDateInYear(lastSynDate, AppGenerator.YMD_SHORT, AppGenerator.YMD_SHORT);
                            }
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
                // update user score
                if (userScore > 0) {
                    UpdateScoreRequest updateScore = new UpdateScoreRequest();
                    updateScore.setUserId(userId);
                    updateScore.setUserScore(String.valueOf(userScore));
                    mService.updateUserScore(updateScore).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }
            }
            db.close();
        }
    }
}
