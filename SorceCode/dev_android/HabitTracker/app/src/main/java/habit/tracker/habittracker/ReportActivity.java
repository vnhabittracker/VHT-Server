package habit.tracker.habittracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.model.GradientColor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import habit.tracker.habittracker.common.Generator;
import habit.tracker.habittracker.common.chart.DayAxisValueFormatter;
import habit.tracker.habittracker.common.chart.MyAxisValueFormatter;
import habit.tracker.habittracker.repository.Database;
import habit.tracker.habittracker.repository.habit.DateTracking;


public class ReportActivity extends AppCompatActivity implements OnChartValueSelectedListener {
    @BindView(R.id.chart)
    BarChart chart;
    @BindView(R.id.pre)
    View pre;
    @BindView(R.id.next)
    View next;
    @BindView(R.id.displayTime)
    TextView time;
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

    View selectedTab;

    private int mode = 0;
    public static final int MODE_WEEK = 0;
    public static final int MODE_MONTH = 1;
    public static final int MODE_YEAR = 2;
    String currentTime;

    public int getMode() {
        return mode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);
        selectedTab = tabWeek;
        initChart();
    }

    @OnClick({R.id.tabWeek, R.id.tabMonth, R.id.tabYear})
    public void loadReportByMode(View v) {
        unselect(selectedTab);
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
                break;
            case R.id.tabYear:
                mode = MODE_YEAR;
                select(tabYear);
                selectedTab = tabYear;
                break;
        }

        ArrayList<BarEntry> values = loadData(currentTime);
        setData(values);
        chart.invalidate();
    }

    @OnClick(R.id.pre)
    public void pre(View v) {
        switch (mode) {
            case MODE_WEEK:
                currentTime = Generator.getDayPreWeek(currentTime);
                break;
            case MODE_MONTH:
                currentTime = Generator.getPreMonth(currentTime);
                break;
            case MODE_YEAR:
                break;
        }
        ArrayList<BarEntry> values = loadData(currentTime);
        setData(values);
        chart.invalidate();
    }

    @OnClick(R.id.next)
    public void next(View v) {
        switch (mode) {
            case MODE_WEEK:
                currentTime = Generator.getDayNextWeek(currentTime);
                break;
            case MODE_MONTH:
                currentTime = Generator.getNextMonth(currentTime);
                break;
            case MODE_YEAR:
                break;
        }
        ArrayList<BarEntry> values = loadData(currentTime);
        setData(values);
        chart.invalidate();
    }

    private void initChart() {
        chart.setOnChartValueSelectedListener(this);

        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);

        chart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setDrawGridBackground(false);
        // chart.setDrawYLabels(false);

        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter();

        XAxis xAxis = chart.getXAxis();
        xAxis.setLabelCount(10);
        xAxis.setValueFormatter(xAxisFormatter);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day

        IAxisValueFormatter custom = new MyAxisValueFormatter();

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setLabelCount(5, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

        Legend l = chart.getLegend();
        l.setEnabled(false);

        XYMarkerView mv = new XYMarkerView(this, xAxisFormatter);
        mv.setChartView(chart); // For bounds control
        chart.setMarker(mv); // Set the marker to the chart

        // chart.setDrawLegend(false);

        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);
        int month = ca.get(Calendar.MONTH) + 1;
        int date = ca.get(Calendar.DATE);
        currentTime = year + "-" + month + "-" + date;

        ArrayList<BarEntry> values = loadWeekData(currentTime);
        setData(values);
    }

    private void setData(ArrayList<BarEntry> values) {
        BarDataSet set1;
        int startColor1 = ContextCompat.getColor(this, R.color.red1);
        int endColor1 = ContextCompat.getColor(this, R.color.red2);
        switch (mode) {
            case MODE_WEEK:
                startColor1 = ContextCompat.getColor(this, R.color.red1);
                endColor1 = ContextCompat.getColor(this, R.color.red2);
                break;
            case MODE_MONTH:
                startColor1 = ContextCompat.getColor(this, R.color.purple1);
                endColor1 = ContextCompat.getColor(this, R.color.purple2);
                break;
            case MODE_YEAR:
                startColor1 = ContextCompat.getColor(this, R.color.blue1);
                endColor1 = ContextCompat.getColor(this, R.color.blue2);
                break;
            default:
                break;
        }

        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mode);
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(xAxisFormatter);

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);

            List<GradientColor> gradientColors = new ArrayList<>();
            gradientColors.add(new com.github.mikephil.charting.model.GradientColor(startColor1, endColor1));

            set1.setGradientColors(gradientColors);

            set1.setValues(values);
            chart.getData().setDrawValues(false);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
            chart.animateY(500);

        } else {
            set1 = new BarDataSet(values, "");
            set1.setDrawIcons(false);

            List<GradientColor> gradientColors = new ArrayList<>();
            gradientColors.add(new com.github.mikephil.charting.model.GradientColor(startColor1, endColor1));

            set1.setGradientColors(gradientColors);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setDrawValues(false);
            data.setValueTextSize(7f);
            data.setBarWidth(0.5f);

            chart.setData(data);
            chart.animateY(500);
        }
    }

    private ArrayList<BarEntry> loadData(String currentTime) {
        ArrayList<BarEntry> values = null;
        switch (mode) {
            case MODE_WEEK:
                values = loadWeekData(currentTime);
                break;
            case MODE_MONTH:
                values = loadMonthData(currentTime);
                break;
            case MODE_YEAR:
                values = loadYearData(currentTime);
                break;
            default:
                break;
        }
        return values;
    }

    private ArrayList<BarEntry> loadWeekData(String currentTime) {
        ArrayList<BarEntry> values = new ArrayList<>();
        String[] strs = currentTime.split("-");
        int year = Integer.parseInt(strs[0]);
        int month = Integer.parseInt(strs[1]);
        int date = Integer.parseInt(strs[2]);
        String[] daysInWeek = Generator.getDatesInWeek(year, month, date);

        String startDate = Generator.convert(daysInWeek[0], "-", "/");
        String endDate = Generator.convert(daysInWeek[6], "-", "/");
        time.setText(startDate + " - " + endDate);

        Database db = new Database(this);
        db.open();
        // get all completed habits in one week
        List<DateTracking> total = Database.sHabitDaoImpl.getHabitsBetween(daysInWeek[0], daysInWeek[6]);
        List<DateTracking> completedList = new ArrayList<>();
        for (DateTracking item : total) {
            if (item.getHabitEntity().getMonitorNumber() != null
                    && item.getTrackingEntity().getCount() != null
                    && item.getHabitEntity().getMonitorNumber().equals(item.getTrackingEntity().getCount())) {
                completedList.add(item);
            }
        }
        int[] count = countInWeek(daysInWeek, completedList);
        for (int i = 1; i <= 7; i++) {
            values.add(new BarEntry(i, count[i - 1]));
        }
        db.close();

        tvTotal.setText(String.valueOf(total.size()));
        tvTotalDone.setText(String.valueOf(completedList.size()));

        return values;
    }

    private ArrayList<BarEntry> loadMonthData(String currentTime) {
        ArrayList<BarEntry> values = new ArrayList<>();
        String[] strs = currentTime.split("-");
        int year = Integer.parseInt(strs[0]);
        int month = Integer.parseInt(strs[1]);
        int date = Integer.parseInt(strs[2]);
        String[] daysInMonth = Generator.getDatesInMonth(year, month, date);

        String startDate = Generator.convert(daysInMonth[0], "-", "/");
        String endDate = Generator.convert(daysInMonth[daysInMonth.length - 1], "-", "/");
        time.setText(startDate + " - " + endDate);

        Database db = new Database(this);
        db.open();
        List<DateTracking> total = Database.sHabitDaoImpl.getHabitsBetween(daysInMonth[0], daysInMonth[daysInMonth.length - 1]);
        List<DateTracking> completedList = new ArrayList<>();
        for (DateTracking item : total) {
            if (item.getHabitEntity().getMonitorNumber() != null
                    && item.getTrackingEntity().getCount() != null
                    && item.getHabitEntity().getMonitorNumber().equals(item.getTrackingEntity().getCount())) {
                completedList.add(item);
            }
        }

        int[] count = new int[daysInMonth.length];
        for (DateTracking item : completedList) {
            for (int i = 0; i < daysInMonth.length; i++) {
                if (item.getTrackingEntity().getCurrentDate().equals(daysInMonth[i])) {
                    ++count[i];
                }
            }
        }

        for (int i = 1; i <= count.length; i++) {
            values.add(new BarEntry(i, count[i-1]));
        }
        db.close();

        tvTotal.setText(String.valueOf(total.size()));
        tvTotalDone.setText(String.valueOf(completedList.size()));

        return values;
    }

    private ArrayList<BarEntry> loadYearData(String currentTime) {
        ArrayList<BarEntry> values = new ArrayList<>();
        String[] strs = currentTime.split("-");
        int year = Integer.parseInt(strs[0]);
        int month = Integer.parseInt(strs[1]);
        int date = Integer.parseInt(strs[2]);
        time.setText("Tháng 01" + "/" + year + " - " + "tháng 12" + "/" + year);

        Database db = new Database(this);
        db.open();
        List<List<DateTracking>> yearData = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            yearData.add(
                    Database.sHabitDaoImpl.getHabitsBetween(
                            year + "-" + (i + 1) + "-" + 1, year + "-" + (i + 1) + "-" + Generator.getMaxDayInMonth(year, i))
            );
        }

        int sum = 0;
        int done = 0;
        int[] count = new int[12];
        List<List<DateTracking>> completedList = new ArrayList<>();
        for (int m = 0; m < yearData.size(); m++) {
            List<DateTracking> monthly = new ArrayList<>();
            for (DateTracking item : yearData.get(m)) {
                if (item.getHabitEntity().getMonitorNumber() != null
                        && item.getTrackingEntity().getCount() != null
                        && item.getHabitEntity().getMonitorNumber().equals(item.getTrackingEntity().getCount())) {
                    monthly.add(item);
                    ++count[m];
                }
            }
            completedList.add(monthly);
            sum += yearData.get(m).size();
            done += monthly.size();
        }
        for (int i = 1; i <= count.length; i++) {
            values.add(new BarEntry(i, count[i-1]));
        }
        db.close();

        tvTotal.setText(String.valueOf(sum));
        tvTotalDone.setText(String.valueOf(done));

        return values;
    }

    private int[] countInWeek(String[] week, List<DateTracking> list) {
        int[] count = new int[7];
        for (int i = 0; i < list.size(); i++) {
            String date = list.get(i).getTrackingEntity().getCurrentDate();
            if (date.equals(week[0])) {
                ++count[0];
            } else if (date.equals(week[1])) {
                ++count[1];
            } else if (date.equals(week[2])) {
                ++count[2];
            } else if (date.equals(week[3])) {
                ++count[3];
            } else if (date.equals(week[4])) {
                ++count[4];
            } else if (date.equals(week[5])) {
                ++count[5];
            } else if (date.equals(week[6])) {
                ++count[6];
            }
        }
        return count;
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

    public void unselect(View v) {
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

    public void finishThis(View view) {
        finish();
    }
}
