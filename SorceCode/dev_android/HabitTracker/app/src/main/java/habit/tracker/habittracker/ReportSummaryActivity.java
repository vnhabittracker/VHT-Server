package habit.tracker.habittracker;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;

import com.github.mikephil.charting.data.BarEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import habit.tracker.habittracker.adapter.CalendarNumber;
import habit.tracker.habittracker.adapter.TrackingCalendarAdapter;
import habit.tracker.habittracker.common.util.AppGenerator;
import habit.tracker.habittracker.repository.Database;
import habit.tracker.habittracker.repository.habit.HabitEntity;
import habit.tracker.habittracker.repository.tracking.HabitTracking;
import habit.tracker.habittracker.repository.tracking.TrackingEntity;

public class ReportSummaryActivity extends AppCompatActivity {

    @BindView(R.id.header)
    View vHeader;

    @BindView(R.id.calendar)
    RecyclerView recyclerViewCalendar;

    private HabitEntity habitEntity;
    TrackingCalendarAdapter calendarAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_report_summary);
        ButterKnife.bind(this);

        // UI
        Bundle data = getIntent().getExtras();
        if (data != null) {
            String habitId = data.getString(MainActivity.HABIT_ID);
            String habitColor = data.getString(MainActivity.HABIT_COLOR);

            Database db = Database.getInstance(this);
            db.open();
            habitEntity = Database.sHabitDaoImpl.getHabit(habitId);
            db.close();

            vHeader.setBackgroundColor(ColorUtils.setAlphaComponent(Color.parseColor(habitColor), 100));
        }

        // data
        List<CalendarNumber> values = new ArrayList<>();
        List<CalendarNumber> head = new ArrayList<>();
        List<CalendarNumber> tail = new ArrayList<>();

        String currentDate = AppGenerator.getCurrentDate(AppGenerator.formatYMD2);
        String[] datesInMonth = AppGenerator.getDatesInMonth(currentDate, false);
        Calendar calendar = Calendar.getInstance();

        try {
            SimpleDateFormat format = new SimpleDateFormat(AppGenerator.formatYMD2, Locale.getDefault());
            Date d = format.parse(datesInMonth[0]);
            calendar.setTimeInMillis(d.getTime());

            int dayInW = calendar.get(Calendar.DAY_OF_WEEK);
            int padding = getPadding(dayInW);
            for (int i = 0; i < padding; i++) {
                head.add(new CalendarNumber(null, false));
            }

            d = format.parse(datesInMonth[datesInMonth.length - 1]);
            calendar.setTimeInMillis(d.getTime());
            dayInW = calendar.get(Calendar.DAY_OF_WEEK);
            padding = getPadding(dayInW);

            for (int i = 0; i < padding; i++) {
                tail.add(new CalendarNumber(null, false));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        values.add(new CalendarNumber("Hai", false));
        values.add(new CalendarNumber("Ba", false));
        values.add(new CalendarNumber("Tư", false));
        values.add(new CalendarNumber("Năm", false));
        values.add(new CalendarNumber("Sáu", false));
        values.add(new CalendarNumber("Bảy", false));
        values.add(new CalendarNumber("CN", false));

        // add pre month item
        values.addAll(head);

        Map<String, TrackingEntity> mapValues = loadMonthData(currentDate);

        // add days in month
        for (int i = 0; i < datesInMonth.length; i++) {
            if (mapValues.containsKey(datesInMonth[i])) {
                values.add(new CalendarNumber(String.valueOf(i + 1), true));
            } else {
                values.add(new CalendarNumber(String.valueOf(i + 1), false));
            }
        }

        // add next month item
        values.addAll(tail);

        calendarAdapter = new TrackingCalendarAdapter(this, values);
        recyclerViewCalendar.setLayoutManager(new GridLayoutManager(this, 7));
        recyclerViewCalendar.setAdapter(calendarAdapter);
    }

    public Map<String, TrackingEntity> loadMonthData(String currentDate) {
        String[] daysInMonth = AppGenerator.getDatesInMonth(currentDate, false);
        String startReportDate = daysInMonth[0];
        String endReportDate = daysInMonth[daysInMonth.length - 1];

        Map<String, TrackingEntity> mapDayInMonth = new HashMap<>(31);

        Database db = Database.getInstance(this);
        db.open();
        HabitTracking habitTracking = Database.sTrackingImpl
                .getHabitTrackingBetween(this.habitEntity.getHabitId(), startReportDate, endReportDate);
        db.close();

        for (TrackingEntity entity: habitTracking.getTrackingEntityList()) {
            mapDayInMonth.put(entity.getCurrentDate(), entity);
        }

        if (habitTracking.getHabitEntity() != null) {
            this.habitEntity = habitTracking.getHabitEntity();
        }

        if (mapDayInMonth.size() == 0) {
            return null;
        }
        return mapDayInMonth;
    }

    private int getPadding(int dayInW) {
        int padding = 0;
        switch (dayInW) {
            case Calendar.MONDAY:
                break;
            case Calendar.TUESDAY:
                padding = 1;
                break;
            case Calendar.WEDNESDAY:
                padding = 2;
                break;
            case Calendar.THURSDAY:
                padding = 3;
                break;
            case Calendar.FRIDAY:
                padding = 4;
                break;
            case Calendar.SATURDAY:
                padding = 5;
                break;
            case Calendar.SUNDAY:
                padding = 6;
                break;
        }
        return padding;
    }

    public void showEmpty(View view) {
        Intent intent = new Intent(this, EmptyActivity.class);
        startActivity(intent);
    }
}
