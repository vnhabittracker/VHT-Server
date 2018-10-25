package habit.tracker.habittracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import habit.tracker.habittracker.repository.Database;
import habit.tracker.habittracker.repository.reminder.ReminderEntity;

public class ReminderActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener, TimePicker.OnTimeChangedListener {
    public static final String TIME_HOUR = "hour";
    public static final String TIME_MINUTE = "minute";
    public static final String REPEAT_TIME = "repeat_time";

    @BindView(R.id.reminderTime_picker)
    TimePicker timePicker;

    @BindView(R.id.spinner_repeat)
    NumberPicker numberPicker;

    @BindView(R.id.btnCancel)
    Button btnCancel;

    @BindView(R.id.btnOk)
    Button btnOk;

    AlarmManager alarmMgr;
    Intent receiverIntent;

    boolean[] dayOfWeek;
    int hour;
    int minute;
    int repeatTime;
    final int INTERVAL = 1000 * 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            dayOfWeek = extras.getBooleanArray(HabitActivity.DAY_OF_WEEK);
        }

        timePicker.setOnTimeChangedListener(this);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10);
        numberPicker.setOnValueChangedListener(this);

        alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
        receiverIntent = new Intent(this, ReminderReceiver.class);
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        this.repeatTime = newVal;
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minute = minute;
    }

    @OnClick(R.id.btnOk)
    public void addReminder(Button ok) {

//        Database db = new Database(this);
//        db.open();
//        ReminderEntity entity = new ReminderEntity();
//        entity.setReminderHour(String.valueOf(hour));
//        entity.setReminderMinute(String.valueOf(minute));
//        entity.setRepeatTime(String.valueOf(repeatTime));
//        entity.setRepeatRemain(String.valueOf(repeatTime));
//        Database.sReminderImpl.addReminder(entity);
//        db.close();

        Intent intent = getIntent();
        intent.putExtra(TIME_HOUR, String.valueOf(hour));
        intent.putExtra(TIME_MINUTE, String.valueOf(minute));
        intent.putExtra(REPEAT_TIME, repeatTime);
        setResult(RESULT_OK, intent);
        finish();

//        if (dayOfWeek[0]) {
//            remindOnDay(Calendar.MONDAY);
//        }
//        if (dayOfWeek[1]) {
//            remindOnDay(Calendar.TUESDAY);
//        }
//        if (dayOfWeek[2]) {
//            remindOnDay(Calendar.WEDNESDAY);
//        }
//        if (dayOfWeek[3]) {
//            remindOnDay(Calendar.THURSDAY);
//        }
//        if (dayOfWeek[4]) {
//            remindOnDay(Calendar.FRIDAY);
//        }
//        if (dayOfWeek[5]) {
//            remindOnDay(Calendar.SATURDAY);
//        }
//        if (dayOfWeek[6]) {
//            remindOnDay(Calendar.SUNDAY);
//        }
    }

//    public void remindOnDay(int day) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.DAY_OF_WEEK, day);
//        calendar.set(Calendar.HOUR_OF_DAY, this.hour);
//        calendar.set(Calendar.MINUTE, this.minute);
//        calendar.setTimeInMillis(System.currentTimeMillis());
//
//        int requestCode = 0;
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, receiverIntent, 0);
//        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
//                calendar.getTimeInMillis() + 1000,
//                INTERVAL, pendingIntent);
//        Toast.makeText(this, "start remind", Toast.LENGTH_SHORT).show();
//    }

    @OnClick(R.id.btnCancel)
    public void cancel(Button cancel) {

    }
}
