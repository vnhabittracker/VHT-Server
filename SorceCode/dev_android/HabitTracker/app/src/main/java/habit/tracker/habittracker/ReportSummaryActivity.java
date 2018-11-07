package habit.tracker.habittracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import habit.tracker.habittracker.adapter.CalendarNumber;
import habit.tracker.habittracker.adapter.TrackingCalendarAdapter;

public class ReportSummaryActivity extends AppCompatActivity {

    @BindView(R.id.calendar)
    RecyclerView calendar;

    TrackingCalendarAdapter calendarAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_report_summary);
        ButterKnife.bind(this);

        List<CalendarNumber> data = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            data.add(new CalendarNumber(String.valueOf(i+1), false));
        }
        data.get(0).setText("Hai");
        data.get(1).setText("Ba");
        data.get(2).setText("Tư");
        data.get(3).setText("Năm");
        data.get(4).setText("Sáu");
        data.get(5).setText("Bảy");
        data.get(6).setText("CN");

        calendarAdapter = new TrackingCalendarAdapter(this, data);
        calendar.setLayoutManager(new GridLayoutManager(this, 7));
        calendar.setAdapter(calendarAdapter);
    }
}
