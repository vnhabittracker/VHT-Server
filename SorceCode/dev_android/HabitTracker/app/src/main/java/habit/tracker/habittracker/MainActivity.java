package habit.tracker.habittracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import habit.tracker.habittracker.api.VnHabitApiUtils;
import habit.tracker.habittracker.api.model.habit.Habit;
import habit.tracker.habittracker.api.model.habit.HabitResponse;
import habit.tracker.habittracker.api.model.tracking.Tracking;
import habit.tracker.habittracker.api.model.tracking.TrackingList;
import habit.tracker.habittracker.api.service.VnHabitApiService;
import habit.tracker.habittracker.common.Generator;
import habit.tracker.habittracker.common.Schedule;
import habit.tracker.habittracker.common.TrackingDate;
import habit.tracker.habittracker.repository.Database;
import habit.tracker.habittracker.repository.habit.HabitEntity;
import habit.tracker.habittracker.repository.tracking.TrackingEntity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static habit.tracker.habittracker.HabitRecyclerViewAdapter.TYPE_ADD;

public class MainActivity extends AppCompatActivity implements HabitRecyclerViewAdapter.ItemClickListener {
    public static final int CREATE_NEW_HABIT = 0;
    public static final int UPDATE_HABIT = 1;
    public static final String HABIT_ID = "HABIT_ID";
    List<TrackingItem> trackingItemList = new ArrayList<>();
    HabitRecyclerViewAdapter trackingAdapter;
    String currentDate;
    @BindView(R.id.imgNext)
    ImageView imgNext;
    @BindView(R.id.imgBack)
    ImageView imgBack;
    @BindView(R.id.report)
    View btnReport;

    public void showEmpty(View v) {
        Intent intent = new Intent(this, EmptyActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Calendar ca = Calendar.getInstance();
        currentDate = ca.get(Calendar.YEAR) + "-" + (ca.get(Calendar.MONTH) + 1) + "-" + ca.get(Calendar.DATE);
        initScreen();
    }

    @Override
    public void onSetCount(View view, int type, int position, int count) {
        TrackingItem item = trackingItemList.get(position);
        item.setCount(count);
        trackingAdapter.notifyItemChanged(position);

        Database db = new Database(MainActivity.this);
        db.open();
        VnHabitApiService service = VnHabitApiUtils.getApiService();
        TrackingList trackingData = new TrackingList();
        Tracking tracking = new Tracking();
        tracking.setTrackingId(item.getTrackId());
        tracking.setHabitId(item.getHabitId());
        tracking.setCurrentDate(currentDate);
        tracking.setCount(String.valueOf(item.getCount()));
        trackingData.getTrackingList().add(tracking);

        if (!Database.sTrackingImpl.updateTrackCount(item.getTrackId(),
                String.valueOf(String.valueOf(item.getCount()))
        )) {
            return;
        }

        service.replace(trackingData).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
        db.close();
    }

    @Override
    public void onItemClick(View view, int type, int position) {
        if (TYPE_ADD == type) {
            Intent intent = new Intent(this, HabitActivity.class);
            startActivityForResult(intent, CREATE_NEW_HABIT);
        } else {
            Intent intent = new Intent(this, HabitActivity.class);
            intent.putExtra(HABIT_ID, trackingItemList.get(position).getHabitId());
            startActivityForResult(intent, UPDATE_HABIT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CREATE_NEW_HABIT || requestCode == UPDATE_HABIT) {
            if (resultCode == RESULT_OK) {
                initScreen();
            }
        }
    }

    private void initScreen() {
        RecyclerView recyclerView = findViewById(R.id.rvMenu);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        trackingItemList.clear();
        trackingAdapter = new HabitRecyclerViewAdapter(MainActivity.this, trackingItemList);
        trackingAdapter.setClickListener(MainActivity.this);
        recyclerView.setAdapter(trackingAdapter);
        String userId = MySharedPreference.getUserId(this);
        VnHabitApiService mService = VnHabitApiUtils.getApiService();
        mService.getHabit(userId).enqueue(new Callback<HabitResponse>() {
            @Override
            public void onResponse(Call<HabitResponse> call, Response<HabitResponse> response) {
                if (response.body().getResult().equals("1")) {
                    Database db = new Database(MainActivity.this);
                    db.open();
                    List<Habit> habitList = response.body().getHabit();
                    List<HabitEntity> habitEntities = new ArrayList<>();
                    for (Habit habit : habitList) {
                        habitEntities.add(Database.sHabitDaoImpl.convert(habit));
                    }
                    trackingItemList.clear();
                    for (Habit habit : habitList) {
                        Calendar ca = Calendar.getInstance();
                        ca.setTimeInMillis(System.currentTimeMillis());
                        int year = ca.get(Calendar.YEAR);
                        int month = ca.get(Calendar.MONTH) + 1;
                        int date = ca.get(Calendar.DATE);
                        String currentDate = year + "-" + month + "-" + date;
                        if (isTodayHabit(year, month - 1, date, habit)) {
                            // update tracking data from server
                            for (Tracking track : habit.getTracksList()) {
                                Database.sTrackingImpl.saveTracking(Database.sTrackingImpl.convert(track));
                            }
                            // get today tracking record
                            TrackingEntity todayTracking = getTrackRecord(habit.getHabitId(), currentDate, 0);
                            TrackingItem item = new TrackingItem(
                                    todayTracking.getTrackingId(),
                                    habit.getHabitId(),
                                    habit.getHabitName(),
                                    habit.getHabitDescription(),
                                    habit.getHabitType(),
                                    Integer.parseInt(habit.getMonitorType()),
                                    habit.getMonitorNumber(),
                                    Integer.parseInt(todayTracking.getCount()),
                                    habit.getMonitorUnit(),
                                    habit.getHabitColor());
                            trackingItemList.add(item);
                        }
                    }
                    trackingAdapter.notifyDataSetChanged();
                    // update db
                    for (HabitEntity entity : habitEntities) {
                        Database.sHabitDaoImpl.saveHabit(entity);
                    }
                    db.close();
                }
            }

            @Override
            public void onFailure(Call<HabitResponse> call, Throwable t) {
            }
        });
    }

    public void updateData(List<TrackingItem> trackingItemList, HabitRecyclerViewAdapter trackingAdapter, String currentDate) {
        String[] arr = currentDate.split("-");
        int year = Integer.parseInt(arr[0]);
        int month = Integer.parseInt(arr[1]);
        int date = Integer.parseInt(arr[2]);
        Schedule schedule = new Schedule(year, month, date);
        List<HabitEntity> habitEntities = Database.sHabitDaoImpl.fetchTodayHabit(schedule);
        boolean isDataSetChanged = false;
        for (HabitEntity habit : habitEntities) {
            TrackingEntity record = Database.sTrackingImpl.getTracking(habit.getHabitId(), currentDate);
            if (record.getTrackingId() == null) {
                record = getTrackRecord(habit.getHabitId(), currentDate, 0);
                Database.sTrackingImpl.saveTracking(record);
            }
            TrackingItem item = new TrackingItem(
                    record.getTrackingId(),
                    habit.getHabitId(),
                    habit.getHabitName(),
                    habit.getHabitDescription(),
                    habit.getHabitType(),
                    Integer.parseInt(habit.getMonitorType()),
                    habit.getMonitorNumber(),
                    Integer.parseInt(record.getCount()),
                    habit.getMonitorUnit(),
                    habit.getHabitColor());
            trackingItemList.add(item);
            if (!isDataSetChanged) {
                isDataSetChanged = true;
            }
        }
        if (isDataSetChanged) {
            trackingAdapter.notifyDataSetChanged();
        }
    }

    public TrackingEntity getTrackRecord(String habitId, String currentDate, int defaultVal) {
        TrackingEntity todayTracking = Database.sTrackingImpl.getTracking(habitId, currentDate);
        if (todayTracking.getTrackingId() == null) {
            todayTracking.setTrackingId(Generator.getNewId());
            todayTracking.setHabitId(habitId);
            todayTracking.setCount(String.valueOf(defaultVal));
            todayTracking.setCurrentDate(currentDate);
            todayTracking.setDescription(currentDate);
            Database.sTrackingImpl.saveTracking(todayTracking);
        }
        return todayTracking;
    }

    @OnClick(R.id.imgNext)
    public void next(ImageView img) {
        String nextDate = Generator.getNextDate(currentDate);
        Database db = new Database(this);
        db.open();
        if (nextDate != null) {
            Toast.makeText(this, nextDate, Toast.LENGTH_SHORT).show();
            currentDate = nextDate;
            trackingItemList.clear();
            updateData(trackingItemList, trackingAdapter, currentDate);
        }
        db.close();
    }

    @OnClick(R.id.imgBack)
    public void back(ImageView img) {
        String preDate = Generator.getPreDate(currentDate);
        Database db = new Database(this);
        db.open();
        if (preDate != null) {
            Toast.makeText(this, preDate, Toast.LENGTH_SHORT).show();
            currentDate = preDate;
            trackingItemList.clear();
            updateData(trackingItemList, trackingAdapter, currentDate);
        }
        db.close();
    }

    @OnClick(R.id.report)
    public void report(View v) {
        Intent intent = new Intent(this, ReportActivity.class);
        startActivity(intent);
    }

    public boolean isTodayHabit(int year, int month, int date, TrackingDate habit) {
        Calendar ca = Calendar.getInstance();
        ca.set(year, month, date);
        int toDay = ca.get(Calendar.DAY_OF_WEEK);
        return toDay == Calendar.MONDAY && habit.getMon() != null && habit.getMon().equals("1")
                || toDay == Calendar.TUESDAY && habit.getTue() != null && habit.getTue().equals("1")
                || toDay == Calendar.WEDNESDAY && habit.getWed() != null && habit.getWed().equals("1")
                || toDay == Calendar.THURSDAY && habit.getThu() != null && habit.getThu().equals("1")
                || toDay == Calendar.FRIDAY && habit.getFri() != null && habit.getFri().equals("1")
                || toDay == Calendar.SATURDAY && habit.getSat() != null && habit.getSat().equals("1")
                || toDay == Calendar.SUNDAY && habit.getSun() != null && habit.getSun().equals("1");
    }
}
