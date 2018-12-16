package habit.tracker.habittracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.ColorUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import habit.tracker.habittracker.api.VnHabitApiUtils;
import habit.tracker.habittracker.api.model.tracking.Tracking;
import habit.tracker.habittracker.api.model.tracking.TrackingList;
import habit.tracker.habittracker.api.model.user.User;
import habit.tracker.habittracker.api.service.VnHabitApiService;
import habit.tracker.habittracker.common.AppConstant;
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

import static habit.tracker.habittracker.common.AppConstant.TYPE_0;

public class ReportDetailsActivity extends BaseActivity {
    private static final String DEBUG_TAG = "vnhb_debug";
    @BindView(R.id.header)
    View vHeader;

    @BindView(R.id.tvHabitName)
    TextView tvHabitName;

    @BindView(R.id.pre)
    View imgPreDate;
    @BindView(R.id.next)
    View imgNextDate;

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
    @BindView(R.id.chartContainer)
    View chartContainer;
    @BindView(R.id.chart)
    BarChart chart;
    ChartHelper chartHelper;

    @BindView(R.id.tabEditHabit)
    View tabEditHabit;
    @BindView(R.id.tabAddJournal)
    View tabAddJournal;
    @BindView(R.id.tabChart)
    View tabChart;
    @BindView(R.id.tabCalendar)
    View tabCalendar;

    private HabitEntity habitEntity;
    private String firstCurrentDate;
    private String currentDate;
    private String habitStartDate;
    private String habitEndDate;

    private String chartStartReportDate;
    private String chartEndReportDate;
    private String curStartReportDate;
    private String curEndReportDate;

    private boolean[] trackingDaysInWeek = new boolean[7];

    private int timeLine = 0;
    private int mode = 0;

    int curTrackingCount = 0;
    int curSumCount = 0;

    float touchX = 0;
    float touchY = 0;
    float boundTop = 0;
    float boundBottom = 0;
    float touchThresh = 90;
    float touchTimeThresh = 100;
    long lastTouchTime = 0;

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
                if (habitEntity.getMonitorType().equals(TYPE_0)) {
                    finish();
                    return;
                }
                initDefaultUI(habitEntity);
                ArrayList<BarEntry> values = loadData(currentDate);
                chartHelper.setData(values, mode);
                updateUI();
            } else {
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_report_details);
        ButterKnife.bind(this);

        Database db = Database.getInstance(this);
        db.open();
        Bundle data = getIntent().getExtras();
        if (data != null) {
            String habitId = data.getString(AppConstant.HABIT_ID);

            if (!TextUtils.isEmpty(habitId)) {
                habitEntity = Database.getHabitDb().getHabit(habitId);

                initDefaultUI(habitEntity);

                // load chart nonEmptyNoteList (default is week)
                ArrayList<BarEntry> values = loadData(currentDate);
                chartStartReportDate = curStartReportDate;
                chartEndReportDate = curEndReportDate;

                chartHelper.setData(values, mode);

                updateUI();
            }
        }
        db.close();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Database db = Database.getInstance(this);
        db.open();
        habitEntity = Database.getHabitDb().getHabit(habitEntity.getHabitId());
        db.close();

        initDefaultUI(habitEntity);

        // load chart nonEmptyNoteList (default is week)
        ArrayList<BarEntry> values = loadData(currentDate);
        chartHelper.setData(values, mode);

        updateUI();
    }

    @SuppressLint("ResourceType")
    private void initDefaultUI(HabitEntity habitEntity) {
        mode = ChartHelper.MODE_WEEK;
        currentDate = AppGenerator.getCurrentDate(AppGenerator.YMD_SHORT);
        firstCurrentDate = currentDate;

        Database db = Database.getInstance(this);
        db.open();
        habitEntity = Database.getHabitDb().getHabit(habitEntity.getHabitId());
        TrackingEntity currentTrackingList = Database.getTrackingDb().getTracking(habitEntity.getHabitId(), currentDate);
        db.close();

        if (currentTrackingList != null) {
            curTrackingCount = Integer.parseInt(currentTrackingList.getCount());
        }

        trackingDaysInWeek[0] = habitEntity.getMon().equals(AppConstant.STATUS_OK);
        trackingDaysInWeek[1] = habitEntity.getTue().equals(AppConstant.STATUS_OK);
        trackingDaysInWeek[2] = habitEntity.getWed().equals(AppConstant.STATUS_OK);
        trackingDaysInWeek[3] = habitEntity.getThu().equals(AppConstant.STATUS_OK);
        trackingDaysInWeek[4] = habitEntity.getFri().equals(AppConstant.STATUS_OK);
        trackingDaysInWeek[5] = habitEntity.getSat().equals(AppConstant.STATUS_OK);
        trackingDaysInWeek[6] = habitEntity.getSun().equals(AppConstant.STATUS_OK);

        tvHabitName.setText(habitEntity.getHabitName());
        tvGoal.setText(habitEntity.getMonitorNumber() + " " + habitEntity.getMonitorUnit());

        String habitColor = habitEntity.getHabitColor();

        vHeader.setBackgroundColor(ColorUtils.setAlphaComponent(Color.parseColor(habitColor), 100));

        if (TextUtils.isEmpty(habitColor) || habitColor.equals(getString(R.color.color0))) {
            habitColor = getString(R.color.gray2);
        }
        int startColor = ColorUtils.setAlphaComponent(Color.parseColor(habitColor), 50);
        int endColor = ColorUtils.setAlphaComponent(Color.parseColor(habitColor), 225);

        // init tvDisplayTime tab
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
    public void onWindowFocusChanged(boolean hasFocus) {
        int[] outLocation = new int[2];
        chartContainer.getLocationOnScreen(outLocation);
        boundTop = outLocation[1];
        boundBottom = outLocation[1] + chartContainer.getHeight();
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        switch (action) {
            case (MotionEvent.ACTION_DOWN):
                touchX = ev.getX();
                touchY = ev.getY();
                Log.d(DEBUG_TAG, "DOWN");
                break;
            case (MotionEvent.ACTION_MOVE):
                if (ev.getY() > boundTop && ev.getY() < boundBottom) {
                    if (System.currentTimeMillis() - lastTouchTime > touchTimeThresh) {
                        if (ev.getX() - touchX > touchThresh && Math.abs(ev.getY() - touchY) < touchThresh) {
                            currentDate = AppGenerator.getDayPreWeek(currentDate);
                            timeLine -= 7;
                            loadChartByDate(currentDate);
                            Log.d(DEBUG_TAG, "Action was MOVE: right");
                        } else if (touchX - ev.getX() > touchThresh && Math.abs(ev.getY() - touchY) < touchThresh) {
                            currentDate = AppGenerator.getDayNextWeek(currentDate);
                            timeLine += 7;
                            loadChartByDate(currentDate);
                            Log.d(DEBUG_TAG, "Action was MOVE: left");
                        }
                        lastTouchTime = System.currentTimeMillis();
                    }
                }
                Log.d(DEBUG_TAG, "MOVE");
                break;
            case (MotionEvent.ACTION_UP):
                Log.d(DEBUG_TAG, "UP");
                break;
            case (MotionEvent.ACTION_CANCEL):
                Log.d(DEBUG_TAG, "CANCEL");
                break;
            case (MotionEvent.ACTION_OUTSIDE):
                Log.d(DEBUG_TAG, "OUTSIDE bounds of current screen element");
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @OnClick({R.id.pre, R.id.next})
    public void onDateChanged(View v) {
        switch (v.getId()) {
            case R.id.pre:
                timeLine--;
                currentDate = AppGenerator.getPreDate(currentDate, AppGenerator.YMD_SHORT);
                break;
            case R.id.next:
                timeLine++;
                currentDate = AppGenerator.getNextDate(currentDate, AppGenerator.YMD_SHORT);
                break;
        }

        loadChartByDate(currentDate);
        updateUI();
    }

    @OnClick({R.id.tabWeek, R.id.tabMonth, R.id.tabYear})
    public void loadChartByTabTime(View v) {
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

        ArrayList<BarEntry> values = loadData(currentDate);
        if (values != null && values.size() > 0) {
            chartHelper.setData(values, mode);
        }
        updateUI();
    }

    private void loadChartByDate(String currentDate) {
        Database db = Database.getInstance(this);
        db.open();
        TrackingEntity todayTracking = Database.getTrackingDb().getTracking(habitEntity.getHabitId(), currentDate);
        db.close();

        curTrackingCount = 0;
        if (todayTracking != null) {
            curTrackingCount = Integer.parseInt(todayTracking.getCount());
        }
        ArrayList<BarEntry> values = loadData(currentDate);
        if (currentDate.compareTo(chartStartReportDate) < 0 || currentDate.compareTo(chartEndReportDate) > 0) {
            chartHelper.setData(values, mode);
            chartStartReportDate = curStartReportDate;
            chartEndReportDate = curEndReportDate;
        }
    }

    @OnClick({R.id.minusCount, R.id.addCount})
    public void onTrackingCountChanged(View v) {
        if (timeLine > 0 || currentDate.compareTo(habitEntity.getStartDate()) < 0
                || !AppGenerator.isValidTrackingDay(currentDate, trackingDaysInWeek)) {
            return;
        }

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

        // save to mDb
        Database db = Database.getInstance(this);
        db.open();
        TrackingEntity trackingEntity = Database.getTrackingDb().getTracking(habitEntity.getHabitId(), currentDate);
        if (trackingEntity == null) {
            trackingEntity = new TrackingEntity();
            trackingEntity.setTrackingId(AppGenerator.getNewId());
            trackingEntity.setHabitId(habitEntity.getHabitId());
            trackingEntity.setCurrentDate(currentDate);
        }
        trackingEntity.setCount(String.valueOf(curTrackingCount));
        Database.getTrackingDb().saveUpdateTracking(trackingEntity);
        db.close();

        ArrayList<BarEntry> values = loadData(currentDate);
        chartHelper.setData(values, mode);

        TrackingList trackingData = new TrackingList();
        Tracking tracking = new Tracking();
        tracking.setTrackingId(trackingEntity.getTrackingId());
        tracking.setHabitId(trackingEntity.getHabitId());
        tracking.setCurrentDate(currentDate);
        tracking.setCount(String.valueOf(trackingEntity.getCount()));
        tracking.setDescription(trackingEntity.getDescription());
        trackingData.getTrackingList().add(tracking);

        VnHabitApiService service = VnHabitApiUtils.getApiService();
        service.saveUpdateTracking(trackingData).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });

        updateUI();
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
        curStartReportDate = daysInWeek[0];
        curEndReportDate = daysInWeek[6];

        Database db = Database.getInstance(this);
        db.open();
        HabitTracking habitTracking = Database.getTrackingDb().getHabitTrackingBetween(habitEntity.getHabitId(), curStartReportDate, curEndReportDate);
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
        curStartReportDate = daysInMonth[0];
        curEndReportDate = daysInMonth[daysInMonth.length - 1];

        Database db = Database.getInstance(this);
        db.open();
        HabitTracking habitTracking = Database.trackingImpl.getHabitTrackingBetween(habitEntity.getHabitId(), curStartReportDate, currentDate);
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
        curStartReportDate = year + "-" + "01-01";
        curEndReportDate = year + "-12-" + AppGenerator.getMaxDayInMonth(year, 12);

        Database db = new Database(this);
        db.open();

        int[] completedPerMonth = new int[12];

        HabitTracking habitTracking;
        HabitEntity hb = null;
        String startMonth, endMonth, date;
        for (int m = 0; m < 12; m++) {
            date = year + "-" + (m + 1) + "-01";
            startMonth = AppGenerator.format(date, AppGenerator.YMD_SHORT, AppGenerator.YMD_SHORT);
            date = year + "-" + (m + 1) + "-" + AppGenerator.getMaxDayInMonth(year, m);
            endMonth = AppGenerator.format(date, AppGenerator.YMD_SHORT, AppGenerator.YMD_SHORT);

            habitTracking = Database.trackingImpl.getHabitTrackingBetween(this.habitEntity.getHabitId(), startMonth, endMonth);

            if (habitTracking != null) {
                if (hb == null) {
                    hb = habitTracking.getHabit();
                }
                int count;
                // nonEmptyNoteList per day in month
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

        curSumCount = 0;
        if (habitTracking != null) {
            int count;
            HabitEntity habit = habitTracking.getHabit();
            for (TrackingEntity track : habitTracking.getTrackingList()) {
                count = Integer.parseInt(track.getCount());
                curSumCount += count;
                mapDayInMonth.put(track.getCurrentDate(),
                        mapDayInMonth.get(track.getCurrentDate()) + count);
            }
        }

        for (int i = 1; i <= days.length; i++) {
            values.add(new BarEntry(i, mapDayInMonth.get(days[i - 1])));
        }

        return values;
    }

    @SuppressLint("SetTextI18n")
    private void updateUI() {
        if (timeLine == 0) {
            tvCurrentTime.setText("Hôm nay");
        } else if (timeLine == -1) {
            tvCurrentTime.setText("Hôm qua");
        } else if (timeLine == 1) {
            tvCurrentTime.setText("Ngày mai");
        } else {
            tvCurrentTime.setText(
                    AppGenerator.format(currentDate, AppGenerator.YMD_SHORT, AppGenerator.DMY_SHORT));
        }

        if (timeLine > 0 || currentDate.compareTo(habitEntity.getStartDate()) < 0 || !AppGenerator.isValidTrackingDay(currentDate, trackingDaysInWeek)) {
            tvTrackCount.setText("--");
        } else {
            tvTrackCount.setText(String.valueOf(curTrackingCount) + " " + habitEntity.getMonitorUnit());
        }

        tvSumCount.setText(curSumCount + " " + habitEntity.getMonitorUnit());

        switch (mode) {
            case ChartHelper.MODE_WEEK:
                tvDescription.setText("Tuần này " + AppGenerator.format(chartStartReportDate, AppGenerator.YMD_SHORT, AppGenerator.DMY_SHORT)
                        + " - " + AppGenerator.format(chartEndReportDate, AppGenerator.YMD_SHORT, AppGenerator.DMY_SHORT));
                break;
            case ChartHelper.MODE_MONTH:
                tvDescription.setText("Tháng " + currentDate.split("-")[1] + "/ " + currentDate.split("-")[0]);
                break;
            case ChartHelper.MODE_YEAR:
                tvDescription.setText("Năm " + currentDate.split("-")[0]);
                break;
        }
    }

    @OnClick(R.id.tabEditHabit)
    public void editHabitDetails(View v) {
        super.editHabitDetails(habitEntity.getHabitId());
    }

    @OnClick(R.id.tabAddJournal)
    public void addJournal(View v) {
        super.showNoteScreen(habitEntity.getHabitId());
    }

    @OnClick(R.id.tabCalendar)
    public void showOnCalendar(View v) {
        super.showOnCalendar(habitEntity.getHabitId());
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
