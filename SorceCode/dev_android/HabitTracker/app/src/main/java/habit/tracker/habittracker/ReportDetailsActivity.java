package habit.tracker.habittracker;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import habit.tracker.habittracker.common.chart.ChartHelper;
import habit.tracker.habittracker.common.util.AppGenerator;
import habit.tracker.habittracker.repository.Database;
import habit.tracker.habittracker.repository.habit.HabitEntity;
import habit.tracker.habittracker.repository.tracking.HabitTracking;
import habit.tracker.habittracker.repository.tracking.TrackingEntity;

public class ReportDetailsActivity extends AppCompatActivity {

    @BindView(R.id.pre)
    View pre;
    @BindView(R.id.next)
    View next;

    @BindView(R.id.minusCount)
    View imgMinusCount;
    @BindView(R.id.addCount)
    View imgAddCount;

    @BindView(R.id.tvCount)
    TextView tvCount;
    @BindView(R.id.tvGoal)
    TextView tvGoal;
    @BindView(R.id.tvSumCount)
    TextView tvSumCount;
    @BindView(R.id.tvDescription)
    TextView tvDescription;

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

    private HabitEntity habitEntity;
    private String habitId;
    private String firstCurrentDate;
    private String currentDate;
    private String startReportDate;
    private String endReportDate;

    private int timeLine = 0;
    private int mode = 0;

    int curDayCount = 0;
    int curSumCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_report_details);
        ButterKnife.bind(this);

        mode = ChartHelper.MODE_WEEK;
        currentDate = AppGenerator.getCurrentDate(AppGenerator.formatYMD2);
        firstCurrentDate = currentDate;

        tvCurrentTime.setText("Hôm nay");
        selectedTab = tabWeek;

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            habitId = bundle.getString(MainActivity.HABIT_ID);
            if (!TextUtils.isEmpty(habitId)) {

                Database db = Database.getInstance(this);
                db.open();
                habitEntity = Database.sHabitDaoImpl.getHabit(habitId);
                TrackingEntity trackingEntity = Database.sTrackingImpl.getTracking(habitId, currentDate);
                db.close();

                if (trackingEntity != null) {
                    tvCount.setText(trackingEntity.getCount() + " " + habitEntity.getMonitorUnit());
                    tvGoal.setText(habitEntity.getMonitorNumber() + " " + habitEntity.getMonitorUnit());
                }

                chartHelper = new ChartHelper(this, chart);
                chartHelper.initChart();
                ArrayList<BarEntry> values = loadData(currentDate);
                chartHelper.setData(values, mode);
            }
        }
    }

    @OnClick({R.id.tabWeek, R.id.tabMonth, R.id.tabYear})
    public void loadReportByMode(View v) {
        unSelect(selectedTab);
        switch (v.getId()) {
            case R.id.tabWeek:
                mode = ChartHelper.MODE_WEEK;
                select(tabWeek);
                selectedTab = tabWeek;
                break;
            case R.id.tabMonth:
                mode = ChartHelper.MODE_MONTH;
                select(tabMonth);
                selectedTab = tabMonth;
                break;
            case R.id.tabYear:
                mode = ChartHelper.MODE_YEAR;
                select(tabYear);
                selectedTab = tabYear;
                break;
        }

        ArrayList<BarEntry> values = loadData(currentDate);
        if (values != null && values.size() > 0) {
            chartHelper.setData(values, mode);
//            chart.invalidate();
        }
    }

    @OnClick(R.id.pre)
    public void pre(View v) {
        timeLine--;
        currentDate = AppGenerator.getPreDate(currentDate, AppGenerator.formatYMD2);

        // current date is before the start date of report
        if (currentDate.compareTo(startReportDate) < 0) {
            ArrayList<BarEntry> values = loadData(currentDate);
            chartHelper.setData(values, mode);
//            chart.invalidate();
        }
    }

    @OnClick(R.id.next)
    public void next(View v) {
        timeLine++;
        currentDate = AppGenerator.getNextDate(currentDate, AppGenerator.formatYMD2);

        if (timeLine > 0) {
            timeLine = 0;
            return;
        }

        // current date is after the start date of report
        if (currentDate.compareTo(endReportDate) > 0) {
            ArrayList<BarEntry> values = loadData(currentDate);
            chartHelper.setData(values, mode);
//            chart.invalidate();
        }
    }

    @OnClick({R.id.minusCount, R.id.addCount})
    public void onChangeCount(View v) {
        int value = Integer.parseInt(tvCount.getText().toString());
        switch (v.getId()) {
            case R.id.minusCount:
                value = value - 1 < 0 ? 0 : value - 1;
                break;
            case R.id.addCount:
                value++;
                break;
        }
        tvCount.setText(String.valueOf(value));

        Database db = Database.getInstance(this);
        db.open();
        TrackingEntity newRecord =
                Database.sTrackingImpl.getTracking(this.habitId, this.currentDate);

        if (newRecord == null) {
            newRecord = new TrackingEntity();
            newRecord.setTrackingId(AppGenerator.getNewId());
            newRecord.setHabitId(this.habitId);
            newRecord.setCurrentDate(this.currentDate);
            newRecord.setCount(String.valueOf(value));
        }
        Database.sTrackingImpl.saveTracking(newRecord);
        db.close();

        ArrayList<BarEntry> values = loadData(currentDate);
        chartHelper.setData(values, mode);
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

        updateUI();
        return values;
    }

    public ArrayList<BarEntry> loadWeekData(String currentDate) {
        String[] daysInWeek = AppGenerator.getDatesInWeek(currentDate);

        startReportDate = daysInWeek[0];
        endReportDate = daysInWeek[6];

        Database db = Database.getInstance(this);
        db.open();
        HabitTracking habitTracking = Database.sTrackingImpl
                .getHabitTrackingBetween(habitId, startReportDate, endReportDate);
        db.close();

        return prepareData(habitTracking, daysInWeek);
    }

    public ArrayList<BarEntry> loadMonthData(String currentDate) {
        String[] daysInMonth = AppGenerator.getDatesInMonth(currentDate, false);

        startReportDate = daysInMonth[0];
        endReportDate = daysInMonth[daysInMonth.length - 1];

        Database db = Database.getInstance(this);
        db.open();
        HabitTracking habitTracking = Database.sTrackingImpl
                .getHabitTrackingBetween(habitId, startReportDate, endReportDate);
        db.close();

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
        String end;

        curDayCount = 0;
        curSumCount = 0;
        for (int m = 0; m < 12; m++) {
            start = year + "-" + (m + 1) + "-" + 1;
            end = year + "-" + (m + 1) + "-" + AppGenerator.getMaxDayInMonth(year, m);
            start = AppGenerator.format(start, AppGenerator.formatYMD2, AppGenerator.formatYMD2);
            end = AppGenerator.format(end, AppGenerator.formatYMD2, AppGenerator.formatYMD2);

            // data per month
            habitTracking = Database.sTrackingImpl
                    .getHabitTrackingBetween(habitId, start, end);

            if (habitTracking != null) {
                if (hb == null) {
                    hb = habitTracking.getHabitEntity();
                }
                // data per day in month
                for (TrackingEntity track : habitTracking.getTrackingEntityList()) {
                    if (track.getCount().compareTo(hb.getMonitorNumber()) >= 0) {
                        ++completedPerMonth[m];
                    }
                    curSumCount += Integer.parseInt(track.getCount());

                    if (track.getCurrentDate().equals(currentDate)) {
                        curDayCount = Integer.parseInt(track.getCount());
                    }
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

        curDayCount = 0;
        curSumCount = 0;
        if (habitTracking != null) {
            HabitEntity habit = habitTracking.getHabitEntity();
            List<TrackingEntity> trackList = habitTracking.getTrackingEntityList();

            for (TrackingEntity track : trackList) {

                if (track.getCount().compareTo(habit.getMonitorNumber()) >= 0) {
                    mapDayInMonth.put(track.getCurrentDate(),
                            mapDayInMonth.get(track.getCurrentDate()) + 1);
                    curSumCount += Integer.parseInt(track.getCount());
                }

                if (track.getCurrentDate().equals(currentDate)) {
                    curDayCount = Integer.parseInt(track.getCount());
                }
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
                    AppGenerator.format(currentDate, AppGenerator.formatYMD2, AppGenerator.formatDMY2));
        }

        tvCount.setText(String.valueOf(curDayCount) + " " + habitEntity.getMonitorUnit());

        tvSumCount.setText(String.valueOf(curSumCount) + " " + habitEntity.getMonitorUnit());

        String pre = "";
        if (firstCurrentDate.compareTo(startReportDate) > -1
                && firstCurrentDate.compareTo(endReportDate) < 1) {
            switch (mode) {
                case ChartHelper.MODE_WEEK:
                    pre = "Tuần này ";
                    break;
                case ChartHelper.MODE_MONTH:
                    pre = "Tháng này ";
                    break;
                case ChartHelper.MODE_YEAR:
                    pre = "Năm này ";
                    break;
            }
        }
        String des = pre +
                startReportDate.replace("-", ".") + " - " + endReportDate.replace("-", ".");
        tvDescription.setText(des);
    }

    public void select(View v) {
        switch (mode) {
            case ChartHelper.MODE_WEEK:
                v.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_tab_red));
                break;
            case ChartHelper.MODE_MONTH:
                v.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_tab_purple));
                break;
            case ChartHelper.MODE_YEAR:
                v.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_tab_blue));
                break;
            default:
                break;
        }
    }

    public void unSelect(View v) {
        v.setBackground(ContextCompat.getDrawable(this, android.R.color.transparent));
    }
}
