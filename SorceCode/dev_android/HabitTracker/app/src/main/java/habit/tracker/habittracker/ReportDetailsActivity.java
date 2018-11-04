package habit.tracker.habittracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import habit.tracker.habittracker.common.chart.ChartHelper;
import habit.tracker.habittracker.common.util.DateGenerator;
import habit.tracker.habittracker.repository.Database;
import habit.tracker.habittracker.repository.tracking.HabitTracking;

public class ReportDetailsActivity extends AppCompatActivity {

    @BindView(R.id.chart)
    BarChart chart;

    private String habitId;
    private HabitTracking habitTracking;
    private String startDate;
    private String endDate;

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
//                Calendar ca = Calendar.getInstance();
//                int year = ca.get(Calendar.YEAR);
//                int month = ca.get(Calendar.MONTH) + 1;
//                int date = ca.get(Calendar.DAY_OF_MONTH);
//                String currentDate = year + "-" + month + "-" + date;
//                String[] weekDates = DateGenerator.getDatesInWeek(currentDate);
//                Database db = new Database(this);
//                habitTracking = Database.sTrackingImpl.getHabitTrackingBetween(habitId, weekDates[0], weekDates[6]);
//                db.close();

                ArrayList<BarEntry> values = new ArrayList<>();
                for (int i = 1; i <= 7; i++) {
                    values.add(new BarEntry(i, i));
                }

                ChartHelper chartHelper = new ChartHelper(this, chart);
                chartHelper.initChart();
                chartHelper.setData(values);
            }
        }
    }
}
