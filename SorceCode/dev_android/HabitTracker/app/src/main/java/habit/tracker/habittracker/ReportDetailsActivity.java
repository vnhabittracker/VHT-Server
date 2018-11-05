package habit.tracker.habittracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.WindowManager;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import habit.tracker.habittracker.common.chart.ChartHelper;
import habit.tracker.habittracker.common.util.AppGenerator;
import habit.tracker.habittracker.repository.Database;
import habit.tracker.habittracker.repository.habit.HabitEntity;
import habit.tracker.habittracker.repository.tracking.HabitTracking;
import habit.tracker.habittracker.repository.tracking.TrackingEntity;

public class ReportDetailsActivity extends AppCompatActivity {

    @BindView(R.id.chart)
    BarChart chart;

    private String habitId;
    private String currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_report_details);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            habitId = bundle.getString(MainActivity.HABIT_ID);
            if (!TextUtils.isEmpty(habitId)) {

                currentDate = AppGenerator.getCurrentDate();
                ArrayList<BarEntry> values = loadWeekData(currentDate);

                ChartHelper chartHelper = new ChartHelper(this, chart);
                chartHelper.initChart();
                chartHelper.setData(values);
            }
        }
    }

    private ArrayList<BarEntry> loadWeekData(String currentDate) {
        ArrayList<BarEntry> values = new ArrayList<>();
        String[] daysInWeek = AppGenerator.getDatesInWeek(currentDate);

        Database db = Database.getInstance(this);
        db.open();
        HabitTracking habitTracking = Database.sTrackingImpl
                .getHabitTrackingBetween(habitId, daysInWeek[0], daysInWeek[6]);
        db.close();

        HabitEntity habit = habitTracking.getHabitEntity();
        List<TrackingEntity> trackList = habitTracking.getTrackingEntityList();
        List<TrackingEntity> doneList = new ArrayList<>();

        for (TrackingEntity track : trackList) {

            if (track.getCount() != null
                    && track.getCount().compareTo(habit.getMonitorNumber()) >= 0) {
                doneList.add(track);
            }
        }

        int[] count = new int[7];
        for (int i = 0; i < doneList.size(); i++) {
            String diw = doneList.get(i).getCurrentDate();

            if (diw.equals(daysInWeek[0])) {
                ++count[0];
            } else if (diw.equals(daysInWeek[1])) {
                ++count[1];
            } else if (diw.equals(daysInWeek[2])) {
                ++count[2];
            } else if (diw.equals(daysInWeek[3])) {
                ++count[3];
            } else if (diw.equals(daysInWeek[4])) {
                ++count[4];
            } else if (diw.equals(daysInWeek[5])) {
                ++count[5];
            } else if (diw.equals(daysInWeek[6])) {
                ++count[6];
            }
        }

        for (int i = 1; i <= 7; i++) {
            values.add(new BarEntry(i, count[i - 1]));
        }

        return values;
    }
}
