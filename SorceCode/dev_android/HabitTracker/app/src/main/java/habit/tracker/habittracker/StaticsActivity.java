package habit.tracker.habittracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import habit.tracker.habittracker.common.chart.ChartHelper;
import habit.tracker.habittracker.common.util.AppGenerator;
import habit.tracker.habittracker.common.util.MySharedPreference;
import habit.tracker.habittracker.repository.Database;
import habit.tracker.habittracker.repository.habit.HabitEntity;
import habit.tracker.habittracker.repository.habit.HabitTracking;
import habit.tracker.habittracker.repository.tracking.TrackingEntity;

import static habit.tracker.habittracker.common.AppConstant.TYPE_0;


public class StaticsActivity extends BaseActivity implements OnChartValueSelectedListener {
    private static final String DEBUG_TAG = "vnhb_debug";
    @BindView(R.id.pre)
    View imgPreDate;
    @BindView(R.id.next)
    View imgNextDate;
    @BindView(R.id.displayTime)
    TextView tvDisplayTime;
    @BindView(R.id.total)
    TextView tvTotal;
    @BindView(R.id.totalDone)
    TextView tvTotalDone;
    @BindView(R.id.tabWeek)
    View tabWeek;
    @BindView(R.id.tabMonth)
    View tabMonth;
    @BindView(R.id.tabYear)
    View tabYear;
    View selectedTab = tabWeek;

    @BindView(R.id.chart)
    BarChart chart;
    ChartHelper chartHelper;

    @BindView(R.id.chartContainer)
    View chartContainer;

    public static final int MODE_WEEK = 0;
    public static final int MODE_MONTH = 1;
    public static final int MODE_YEAR = 2;
    private int mode = MODE_WEEK;
    String currentDate;
    String firstCurrentDate;

    float touchX = 0;
    float touchY = 0;
    float boundTop = 0;
    float boundBottom = 0;
    float touchThresh = 90;
    float touchTimeThresh = 100;
    long lastTouchTime = 0;

    Database appDatabase = Database.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_statics);
        ButterKnife.bind(this);
        appDatabase.open();
        selectedTab = tabWeek;
        int startColor = ContextCompat.getColor(this, R.color.red1);
        int endColor = ContextCompat.getColor(this, R.color.red2);
        switch (mode) {
            case MODE_WEEK:
                startColor = ContextCompat.getColor(this, R.color.red1);
                endColor = ContextCompat.getColor(this, R.color.red2);
                break;
            case MODE_MONTH:
                startColor = ContextCompat.getColor(this, R.color.purple1);
                endColor = ContextCompat.getColor(this, R.color.purple2);
                break;
            case MODE_YEAR:
                startColor = ContextCompat.getColor(this, R.color.blue1);
                endColor = ContextCompat.getColor(this, R.color.blue2);
                break;
            default:
                break;
        }
        chartHelper = new ChartHelper(this, chart);
        chartHelper.initChart();
        chartHelper.setChartColor(startColor, endColor);
        initializeScreen();
    }

    private void initializeScreen() {
        currentDate = AppGenerator.getCurrentDate(AppGenerator.YMD_SHORT);
        firstCurrentDate = currentDate;
        ArrayList<BarEntry> values = loadWeekData(currentDate);
        chartHelper.setData(values, mode);
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
                            moveToPre(imgPreDate);
                            Log.d(DEBUG_TAG, "Action was MOVE: right");
                        } else if (touchX - ev.getX() > touchThresh && Math.abs(ev.getY() - touchY) < touchThresh) {
                            moveToNext(imgNextDate);
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

    @SuppressLint("ResourceType")
    @OnClick({R.id.tabWeek, R.id.tabMonth, R.id.tabYear})
    public void loadReportByMode(View v) {
        unSelect(selectedTab);
        int startColor = Color.parseColor(getString(R.color.red1));
        int endColor = Color.parseColor(getString(R.color.red2));
        switch (v.getId()) {
            case R.id.tabWeek:
                mode = MODE_WEEK;
                select(tabWeek);
                selectedTab = tabWeek;
                break;

            case R.id.tabMonth:
                mode = MODE_MONTH;
                select(tabMonth);
                selectedTab = tabMonth;

                startColor = Color.parseColor(getString(R.color.purple1));
                endColor = Color.parseColor(getString(R.color.purple2));
                break;

            case R.id.tabYear:
                mode = MODE_YEAR;
                select(tabYear);
                selectedTab = tabYear;

                startColor = Color.parseColor(getString(R.color.blue1));
                endColor = Color.parseColor(getString(R.color.blue2));
                break;
        }
        currentDate = firstCurrentDate;
        ArrayList<BarEntry> values = loadData(currentDate);
        if (values != null && values.size() > 0) {

            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{startColor, endColor});
            gd.setCornerRadius(0f);
            chartHelper.setChartColor(startColor, endColor);

            chartHelper.setData(values, mode);
        }
    }

    @OnClick(R.id.pre)
    public void moveToPre(View v) {
        switch (mode) {
            case MODE_WEEK:
                currentDate = AppGenerator.getDayPreWeek(currentDate);
                break;
            case MODE_MONTH:
                currentDate = AppGenerator.getPreMonth(currentDate);
                break;
            case MODE_YEAR:
                currentDate = AppGenerator.getPreYear(currentDate);
                break;
        }
        ArrayList<BarEntry> values = loadData(currentDate);
        chartHelper.setData(values, mode);
    }

    @OnClick(R.id.next)
    public void moveToNext(View v) {
        switch (mode) {
            case MODE_WEEK:
                currentDate = AppGenerator.getDayNextWeek(currentDate);
                break;
            case MODE_MONTH:
                currentDate = AppGenerator.getNextMonth(currentDate);
                break;
            case MODE_YEAR:
                currentDate = AppGenerator.getNextYear(currentDate);
                break;
        }
        ArrayList<BarEntry> values = loadData(currentDate);
        chartHelper.setData(values, mode);
    }

    private ArrayList<BarEntry> loadData(String currentDate) {
        ArrayList<BarEntry> values = null;
        switch (mode) {
            case MODE_WEEK:
                values = loadWeekData(currentDate);
                break;
            case MODE_MONTH:
                values = loadMonthData(currentDate);
                break;
            case MODE_YEAR:
                values = loadYearData(currentDate);
                break;
            default:
                break;
        }
        return values;
    }

    private ArrayList<BarEntry> loadWeekData(String currentDate) {
        appDatabase.open();

        ArrayList<BarEntry> values = new ArrayList<>();
        String[] daysInWeek = AppGenerator.getDatesInWeek(currentDate);
        String startDate = AppGenerator.format(daysInWeek[0], AppGenerator.YMD_SHORT, AppGenerator.DMY_SHORT);
        String endDate = AppGenerator.format(daysInWeek[6], AppGenerator.YMD_SHORT, AppGenerator.DMY_SHORT);
        tvDisplayTime.setText(startDate + " - " + endDate);

        // get groupList in week
        List<HabitTracking> weekData = Database.getHabitDb().getHabitTracking(MySharedPreference.getUserId(this), daysInWeek[0], daysInWeek[6]);

        List<TrackingEntity> meetGoalTrackingList = getMeetGoalDateList(weekData, daysInWeek[0], daysInWeek[6]);

        int[] count = new int[7];
        for (int i = 0; i < meetGoalTrackingList.size(); i++) {
            String date = meetGoalTrackingList.get(i).getCurrentDate();
            if (date.equals(daysInWeek[0])) {
                ++count[0];
            } else if (date.equals(daysInWeek[1])) {
                ++count[1];
            } else if (date.equals(daysInWeek[2])) {
                ++count[2];
            } else if (date.equals(daysInWeek[3])) {
                ++count[3];
            } else if (date.equals(daysInWeek[4])) {
                ++count[4];
            } else if (date.equals(daysInWeek[5])) {
                ++count[5];
            } else if (date.equals(daysInWeek[6])) {
                ++count[6];
            }
        }
        for (int i = 1; i <= 7; i++) {
            values.add(new BarEntry(i, count[i - 1]));
        }

        tvTotal.setText(String.valueOf(weekData.size()));
        tvTotalDone.setText(String.valueOf(meetGoalTrackingList.size()));

        return values;
    }

    private ArrayList<BarEntry> loadMonthData(String currentDate) {
        appDatabase.open();

        ArrayList<BarEntry> values = new ArrayList<>();
        String[] daysInMonth = AppGenerator.getDatesInMonth(currentDate, false);

        String startDate = AppGenerator.format(daysInMonth[0], AppGenerator.YMD_SHORT, AppGenerator.DMY_SHORT);
        String endDate = AppGenerator.format(daysInMonth[daysInMonth.length - 1], AppGenerator.YMD_SHORT, AppGenerator.DMY_SHORT);
        tvDisplayTime.setText(startDate + " - " + endDate);

        List<HabitTracking> monthData = Database.getHabitDb().getHabitTracking(MySharedPreference.getUserId(this), daysInMonth[0], daysInMonth[daysInMonth.length - 1]);

        List<TrackingEntity> meetGoalTrackingList = getMeetGoalDateList(monthData, daysInMonth[0], daysInMonth[daysInMonth.length - 1]);

        int[] count = new int[daysInMonth.length];
        for (TrackingEntity item : meetGoalTrackingList) {
            for (int i = 0; i < daysInMonth.length; i++) {
                if (item.getCurrentDate().equals(daysInMonth[i])) {
                    ++count[i];
                }
            }
        }
        for (int i = 1; i <= count.length; i++) {
            values.add(new BarEntry(i, count[i - 1]));
        }

        tvTotal.setText(String.valueOf(monthData.size()));
        tvTotalDone.setText(String.valueOf(meetGoalTrackingList.size()));

        return values;
    }

    private ArrayList<BarEntry> loadYearData(String currentDate) {
        appDatabase.open();

        ArrayList<BarEntry> values = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(AppGenerator.getDate(currentDate.split("-")[0] + "-12-01", AppGenerator.YMD_SHORT));
        tvDisplayTime.setText("Năm " + calendar.get(Calendar.YEAR));

        String startYearDate = calendar.get(Calendar.YEAR) + "-01-01";
        String endYearDate = calendar.get(Calendar.YEAR) + "-12-" + calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        List<HabitTracking> yearData = Database.getHabitDb().getHabitTracking(MySharedPreference.getUserId(this), startYearDate, endYearDate);

        List<TrackingEntity> meetGoalTrackingList = getMeetGoalDateList(yearData, startYearDate, endYearDate);

        String[] months = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        int[] count = new int[12];
        for (TrackingEntity item : meetGoalTrackingList) {
            for (int i = 0; i < months.length; i++) {
                if (item.getCurrentDate().split("-")[1].equals(months[i])) {
                    count[i]++;
                }
            }
        }
        for (int i = 1; i <= count.length; i++) {
            values.add(new BarEntry(i, count[i - 1]));
        }

        tvTotal.setText(String.valueOf(yearData.size()));
        tvTotalDone.setText(String.valueOf(meetGoalTrackingList.size()));

        return values;
    }

    private List<TrackingEntity> getMeetGoalDateList(List<HabitTracking> trackingData, String start, String end) {
        List<TrackingEntity> meetGoalTrackingList = new ArrayList<>();
        HabitEntity habitEntity;
        int total = 0;
        int goal;
        boolean isDaily = false;
        for (HabitTracking habitTracking : trackingData) {
            habitEntity = habitTracking.getHabit();
            isDaily = habitEntity.getMonitorType().equals(TYPE_0);
            goal = Integer.parseInt(habitEntity.getMonitorNumber());
            for (TrackingEntity trackingEntity : habitTracking.getTrackingList()) {
                if (trackingEntity.getCurrentDate().compareTo(start) >= 0 && trackingEntity.getCurrentDate().compareTo(end) <= 0) {
                    total += Integer.parseInt(trackingEntity.getCount());
                    if (total >= goal) {
                        meetGoalTrackingList.add(trackingEntity);
                        if (isDaily) {
                            total = 0;
                        } else {
                            break;
                        }
                    }
                }
            }
            total = 0;
        }
        return meetGoalTrackingList;
    }

    public void select(View v) {
        switch (mode) {
            case MODE_WEEK:
                v.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_tab_red));
                break;
            case MODE_MONTH:
                v.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_tab_purple));
                break;
            case MODE_YEAR:
                v.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_tab_blue));
                break;
            default:
                break;
        }
    }

    public void unSelect(View v) {
        v.setBackground(ContextCompat.getDrawable(this, android.R.color.transparent));
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void showEmpty(View view) {
        Intent intent = new Intent(this, EmptyActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        appDatabase.close();
        super.onStop();
    }
}
