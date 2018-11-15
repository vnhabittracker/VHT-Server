package habit.tracker.habittracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import habit.tracker.habittracker.api.VnHabitApiUtils;
import habit.tracker.habittracker.api.model.tracking.Tracking;
import habit.tracker.habittracker.api.model.tracking.TrackingList;
import habit.tracker.habittracker.api.service.VnHabitApiService;
import habit.tracker.habittracker.common.chart.ChartHelper;
import habit.tracker.habittracker.common.util.AppGenerator;
import habit.tracker.habittracker.repository.Database;
import habit.tracker.habittracker.repository.habit.HabitEntity;
import habit.tracker.habittracker.repository.habit.HabitTracking;
import habit.tracker.habittracker.repository.tracking.TrackingEntity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportDetailsActivity extends AppCompatActivity {
    @BindView(R.id.header)
    View vHeader;

    @BindView(R.id.tvHabitName)
    TextView tvHabitName;

    @BindView(R.id.pre)
    View btnPreDate;
    @BindView(R.id.next)
    View btnNextDate;

    @BindView(R.id.minusCount)
    View imgMinusCount;
    @BindView(R.id.addCount)
    View imgAddCount;

    @BindView(R.id.tvTrackCount)
    TextView tvTrackCount;
    @BindView(R.id.tvGoal)
    TextView tvGoal;
    @BindView(R.id.tvSumCount)
    TextView tvSumCount;
    @BindView(R.id.tvChartDescription)
    TextView tvDescription;

    @BindView(R.id.tabWeekHL)
    View tabWeekHL;
    @BindView(R.id.tabMonthHL)
    View tabMonthHL;
    @BindView(R.id.tabYearHL)
    View tabYearHL;
    View selectedTabHL;
    @BindView(R.id.tabWeek)
    View tabWeek;
    @BindView(R.id.tabMonth)
    View tabMonth;
    @BindView(R.id.tabYear)
    View tabYear;
    View selectedTab;

    @BindView(R.id.tvCurrentTime)
    TextView tvCurrentTime;
    @BindView(R.id.chart)
    BarChart chart;
    ChartHelper chartHelper;

    @BindView(R.id.tabEditHabit)
    View tabEditHabit;
    @BindView(R.id.tabCalendar)
    View tabCalendar;

    @BindView(R.id.tabAddJournal)
    View tabAddDiary;

    private HabitEntity habitEntity;
    private String firstCurrentDate;
    private String currentTrackingDate;
    private String habitStartDate;
    private String habitEndDate;
    private String startReportDate;
    private String endReportDate;
    private boolean[] availDaysInWeek = new boolean[7];

    private int timeLine = 0;
    private int mode = 0;

    int curTrackingCount = 0;
    int curSumCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_report_details);
        ButterKnife.bind(this);

        Bundle data = getIntent().getExtras();
        if (data != null) {
            String habitId = data.getString(MainActivity.HABIT_ID);

            if (!TextUtils.isEmpty(habitId)) {
                Database db = Database.getInstance(this);
                db.open();
                habitEntity = Database.getHabitDb().getHabit(habitId);
                db.close();

                initDefaultUI(habitEntity);

                // load chart noteItems (default is week)
                ArrayList<BarEntry> values = loadData(currentTrackingDate);
                chartHelper.setData(values, mode);

                updateUI();
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Database db = Database.getInstance(this);
        db.open();
        habitEntity = Database.getHabitDb().getHabit(habitEntity.getHabitId());
        db.close();

        initDefaultUI(habitEntity);

        // load chart noteItems (default is week)
        ArrayList<BarEntry> values = loadData(currentTrackingDate);
        chartHelper.setData(values, mode);

        updateUI();
    }

    @SuppressLint("ResourceType")
    private void initDefaultUI(HabitEntity habitEntity) {
        mode = ChartHelper.MODE_WEEK;
        currentTrackingDate = AppGenerator.getCurrentDate(AppGenerator.YMD_SHORT);
        firstCurrentDate = currentTrackingDate;

        Database db = Database.getInstance(this);
        db.open();
        habitEntity = Database.getHabitDb().getHabit(habitEntity.getHabitId());
        TrackingEntity currentTrackingList = Database.getTrackingDb().getTracking(habitEntity.getHabitId(), currentTrackingDate);
        curSumCount = Database.getTrackingDb().sumCountByHabit(habitEntity.getHabitId());
        db.close();

        if (currentTrackingList != null) {
            curTrackingCount = Integer.parseInt(currentTrackingList.getCount());
        }

        availDaysInWeek[0] = habitEntity.getMon().equals("1");
        availDaysInWeek[1] = habitEntity.getTue().equals("1");
        availDaysInWeek[2] = habitEntity.getWed().equals("1");
        availDaysInWeek[3] = habitEntity.getThu().equals("1");
        availDaysInWeek[4] = habitEntity.getFri().equals("1");
        availDaysInWeek[5] = habitEntity.getSat().equals("1");
        availDaysInWeek[6] = habitEntity.getSun().equals("1");

        tvHabitName.setText(habitEntity.getHabitName());
        tvSumCount.setText(curSumCount + " " + habitEntity.getMonitorUnit());

        String habitColor = habitEntity.getHabitColor();

        vHeader.setBackgroundColor(ColorUtils.setAlphaComponent(Color.parseColor(habitColor), 100));

        if (TextUtils.isEmpty(habitColor) || habitColor.equals(getString(R.color.color0))) {
            habitColor = getString(R.color.gray2);
        }
        int startColor = ColorUtils.setAlphaComponent(Color.parseColor(habitColor), 50);
        int endColor = ColorUtils.setAlphaComponent(Color.parseColor(habitColor), 225);

        // init time tab
        selectedTab = tabWeek;
        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{startColor, endColor});
        gd.setCornerRadius(0f);
        tabWeekHL.setBackground(gd);
        tabMonthHL.setBackground(gd);
        tabYearHL.setBackground(gd);
        select(tabWeekHL);
        selectedTabHL = tabWeekHL;

        // init chart
        chartHelper = new ChartHelper(this, chart);
        chartHelper.initChart();
        chartHelper.setChartColor(startColor, endColor);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HabitActivity.REQUEST_UPDATE) {
            boolean delete = false;
            if (data != null) {
                delete = data.getBooleanExtra("delete", false);
            }

            if (!delete) {
                Database db = Database.getInstance(this);
                db.open();
                habitEntity = Database.getHabitDb().getHabit(habitEntity.getHabitId());
                db.close();

                initDefaultUI(habitEntity);

                ArrayList<BarEntry> values = loadData(currentTrackingDate);
                chartHelper.setData(values, mode);
                updateUI();
            } else {
                finish();
            }
        }
    }

    @OnClick({R.id.tabWeek, R.id.tabMonth, R.id.tabYear})
    public void loadReportByMode(View v) {
        unSelect(selectedTabHL);
        switch (v.getId()) {
            case R.id.tabWeek:
                mode = ChartHelper.MODE_WEEK;
                select(tabWeekHL);
                selectedTabHL = tabWeekHL;
                break;
            case R.id.tabMonth:
                mode = ChartHelper.MODE_MONTH;
                select(tabMonthHL);
                selectedTabHL = tabMonthHL;
                break;
            case R.id.tabYear:
                mode = ChartHelper.MODE_YEAR;
                select(tabYearHL);
                selectedTabHL = tabYearHL;
                break;
        }

        ArrayList<BarEntry> values = loadData(currentTrackingDate);
        if (values != null && values.size() > 0) {
            chartHelper.setData(values, mode);
        }
        updateUI();
    }

    @OnClick({R.id.pre, R.id.next})
    public void onDateChanged(View v) {
        switch (v.getId()) {
            case R.id.pre:
                timeLine--;
                currentTrackingDate = AppGenerator.getPreDate(currentTrackingDate, AppGenerator.YMD_SHORT);
                break;
            case R.id.next:
                timeLine++;
                currentTrackingDate = AppGenerator.getNextDate(currentTrackingDate, AppGenerator.YMD_SHORT);
                break;
        }

        Database db = Database.getInstance(this);
        db.open();
        TrackingEntity todayTracking = Database.getTrackingDb()
                .getTracking(habitEntity.getHabitId(), currentTrackingDate);
        db.close();

        curTrackingCount = 0;
        if (todayTracking != null) {
            curTrackingCount = Integer.parseInt(todayTracking.getCount());
        }

        ArrayList<BarEntry> values = loadData(currentTrackingDate);
        if ((currentTrackingDate.compareTo(startReportDate) < 0 || currentTrackingDate.compareTo(endReportDate) > 0)
                && currentTrackingDate.compareTo(habitStartDate) >= 0 && currentTrackingDate.compareTo(habitEndDate) <= 0) {
            chartHelper.setData(values, mode);
        }

        updateUI();
    }

    @OnClick({R.id.minusCount, R.id.addCount})
    public void onCountChanged(View v) {
        if (timeLine > 0 || currentTrackingDate.compareTo(habitEntity.getStartDate()) < 0
                || !AppGenerator.isValidTrackingDay(currentTrackingDate, availDaysInWeek)) {
            return;
        }

        int goalNumber = Integer.parseInt(habitEntity.getMonitorNumber());
        boolean above = curTrackingCount >= goalNumber;

        switch (v.getId()) {
            case R.id.minusCount:
                curTrackingCount = curTrackingCount - 1 < 0 ? 0 : curTrackingCount - 1;
                curSumCount = curTrackingCount <= 0 ? curSumCount : curSumCount - 1;
                break;
            case R.id.addCount:
                curTrackingCount++;
                curSumCount++;
                break;
        }

        // save to appDatabase
        Database db = Database.getInstance(this);
        db.open();
        TrackingEntity record =
                Database.trackingImpl.getTracking(this.habitEntity.getHabitId(), this.currentTrackingDate);
        if (record == null) {
            record = new TrackingEntity();
            record.setTrackingId(AppGenerator.getNewId());
            record.setHabitId(this.habitEntity.getHabitId());
            record.setCurrentDate(this.currentTrackingDate);
        }
        record.setCount(String.valueOf(curTrackingCount));
        Database.trackingImpl.saveTracking(record);
        db.close();

        ArrayList<BarEntry> values = loadData(currentTrackingDate);
//        if ((!above && curTrackingCount == goalNumber)
//                || (above && goalNumber - curTrackingCount == 1)) {
//            chartHelper.setData(values, mode);
//        }
        chartHelper.setData(values, mode);

        updateUI();

        TrackingList trackingData = new TrackingList();
        Tracking tracking = new Tracking();
        tracking.setTrackingId(record.getTrackingId());
        tracking.setHabitId(record.getHabitId());
        tracking.setCurrentDate(currentTrackingDate);
        tracking.setCount(String.valueOf(record.getCount()));
        trackingData.getTrackingList().add(tracking);
        VnHabitApiService service = VnHabitApiUtils.getApiService();
        service.updateTracking(trackingData).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    private ArrayList<BarEntry> loadData(String currentTime) {
        ArrayList<BarEntry> values = null;

        switch (mode) {
            case ChartHelper.MODE_WEEK:
                values = loadWeekData(currentTime);
                break;
            case ChartHelper.MODE_MONTH:
                values = loadMonthData(currentTime);
                break;
            case ChartHelper.MODE_YEAR:
                values = loadYearData(currentTime);
                break;
            default:
                break;
        }
        return values;
    }

    public ArrayList<BarEntry> loadWeekData(String currentDate) {
        String[] daysInWeek = AppGenerator.getDatesInWeek(currentDate);
        startReportDate = daysInWeek[0];
        endReportDate = daysInWeek[6];

        Database db = Database.getInstance(this);
        db.open();
        HabitTracking habitTracking = Database.getTrackingDb().getHabitTrackingBetween(habitEntity.getHabitId(), startReportDate, currentDate);
        db.close();

        if (habitTracking != null && habitTracking.getHabit() != null) {
            habitEntity = habitTracking.getHabit();
            habitStartDate = habitEntity.getStartDate();
            habitEndDate = habitEntity.getEndDate();
        }

        return prepareData(habitTracking, daysInWeek);
    }

    public ArrayList<BarEntry> loadMonthData(String currentDate) {
        String[] daysInMonth = AppGenerator.getDatesInMonth(currentDate, false);
        startReportDate = daysInMonth[0];
        endReportDate = daysInMonth[daysInMonth.length - 1];

        Database db = Database.getInstance(this);
        db.open();
        HabitTracking habitTracking = Database.trackingImpl.getHabitTrackingBetween(habitEntity.getHabitId(), startReportDate, currentDate);
        db.close();

        if (habitTracking != null && habitTracking.getHabit() != null) {
            habitEntity = habitTracking.getHabit();
            habitStartDate = habitEntity.getStartDate();
            habitEndDate = habitEntity.getEndDate();
        }
        return prepareData(habitTracking, daysInMonth);
    }

    private ArrayList<BarEntry> loadYearData(String currentDate) {
        ArrayList<BarEntry> values = new ArrayList<>();
        String[] arrDate = currentDate.split("-");

        int year = Integer.parseInt(arrDate[0]);
        startReportDate = year + "-" + "01-01";
        endReportDate = year + "-01-" + AppGenerator.getMaxDayInMonth(year, 12);

        Database db = new Database(this);
        db.open();

        int[] completedPerMonth = new int[12];

        HabitTracking habitTracking;
        HabitEntity hb = null;
        String start;

        for (int m = 0; m < 12; m++) {
            start = year + "-" + (m + 1) + "-" + 1;
            start = AppGenerator.format(start, AppGenerator.YMD_SHORT, AppGenerator.YMD_SHORT);

            // noteItems per month
            habitTracking = Database.trackingImpl.getHabitTrackingBetween(this.habitEntity.getHabitId(), start, currentDate);

            if (habitTracking != null) {
                if (hb == null) {
                    hb = habitTracking.getHabit();
                }
                int count;
                // noteItems per day in month
                for (TrackingEntity track : habitTracking.getTrackingList()) {
                    count = Integer.parseInt(track.getCount());
                    completedPerMonth[m] += count;
                }
                if (habitTracking.getHabit() != null) {
                    habitEntity = habitTracking.getHabit();
                    habitStartDate = habitEntity.getStartDate();
                    habitEndDate = habitEntity.getEndDate();
                }
            }
        }
        db.close();

        for (int i = 1; i <= completedPerMonth.length; i++) {
            values.add(new BarEntry(i, completedPerMonth[i - 1]));
        }

        return values;
    }

    private ArrayList<BarEntry> prepareData(HabitTracking habitTracking, String[] days) {
        ArrayList<BarEntry> values = new ArrayList<>();

        Map<String, Integer> mapDayInMonth = new HashMap<>(31);
        for (String d : days) {
            mapDayInMonth.put(d, 0);
        }

        if (habitTracking != null) {
            int count;
            HabitEntity habit = habitTracking.getHabit();
            List<TrackingEntity> trackList = habitTracking.getTrackingList();
            for (TrackingEntity track : trackList) {
                count = Integer.parseInt(track.getCount());
//                if (count >= Integer.parseInt(habit.getMonitorNumber())) {
                mapDayInMonth.put(track.getCurrentDate(),
                        mapDayInMonth.get(track.getCurrentDate()) + count);
//                }
            }
        }

        for (int i = 1; i <= days.length; i++) {
            values.add(new BarEntry(i, mapDayInMonth.get(days[i - 1])));
        }

        return values;
    }

    private void updateUI() {
        if (timeLine == 0) {
            tvCurrentTime.setText("Hôm nay");
        } else if (timeLine == -1) {
            tvCurrentTime.setText("Hôm qua");
        } else {
            tvCurrentTime.setText(
                    AppGenerator.format(currentTrackingDate, AppGenerator.YMD_SHORT, AppGenerator.DMY_SHORT));
        }

//        if (timeLine == 0) {
//            btnNextDate.setVisibility(View.INVISIBLE);
//        } else {
//            btnNextDate.setVisibility(View.VISIBLE);
//        }
//
//        if (currentTrackingDate.compareTo(habitStartDate) <= 0) {
//            btnPreDate.setVisibility(View.INVISIBLE);
//        } else {
//            btnPreDate.setVisibility(View.VISIBLE);
//        }

        tvSumCount.setText(curSumCount + " " + habitEntity.getMonitorUnit());

        if (timeLine > 0 || currentTrackingDate.compareTo(habitEntity.getStartDate()) < 0
                || !AppGenerator.isValidTrackingDay(currentTrackingDate, availDaysInWeek)) {
            tvTrackCount.setText("--");
        } else {
            tvTrackCount.setText(String.valueOf(curTrackingCount) + " " + habitEntity.getMonitorUnit());
        }

        tvGoal.setText(habitEntity.getMonitorNumber() + " " + habitEntity.getMonitorUnit());

        String des = null;
        switch (mode) {
            case ChartHelper.MODE_WEEK:
                des = "Tuần này " + AppGenerator.format(startReportDate, AppGenerator.YMD_SHORT, AppGenerator.DMY_SHORT)
                        + " - " + AppGenerator.format(endReportDate, AppGenerator.YMD_SHORT, AppGenerator.DMY_SHORT);
                break;
            case ChartHelper.MODE_MONTH:
                des = "Tháng " + currentTrackingDate.split("-")[1] + "/ " + currentTrackingDate.split("-")[0];
                break;
            case ChartHelper.MODE_YEAR:
                des = "Năm " + currentTrackingDate.split("-")[0];
                break;
        }
        tvDescription.setText(des);
    }

    @OnClick(R.id.tabEditHabit)
    public void editHabitDetails(View v) {
        Intent intent = new Intent(this, HabitActivity.class);
        intent.putExtra(MainActivity.HABIT_ID, this.habitEntity.getHabitId());
        startActivityForResult(intent, HabitActivity.REQUEST_UPDATE);
    }

    @OnClick(R.id.tabCalendar)
    public void showOnCalendar(View v) {
        Intent intent = new Intent(this, ReportCalendarActivity.class);
        intent.putExtra(MainActivity.HABIT_ID, habitEntity.getHabitId());
        intent.putExtra(MainActivity.HABIT_COLOR, habitEntity.getHabitColor());
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.tabAddJournal)
    public void addJournal(View v) {
        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra(MainActivity.HABIT_ID, habitEntity.getHabitId());
        startActivity(intent);
        finish();
    }

    public void select(View v) {
        v.setVisibility(View.VISIBLE);
    }

    public void unSelect(View v) {
        v.setVisibility(View.INVISIBLE);
    }

    public void showEmpty(View view) {
        Intent intent = new Intent(this, EmptyActivity.class);
        startActivity(intent);
    }
}
