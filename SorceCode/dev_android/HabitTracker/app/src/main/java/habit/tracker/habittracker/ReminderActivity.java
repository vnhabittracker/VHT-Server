package habit.tracker.habittracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.NumberPicker;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReminderActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener {

    @BindView(R.id.picker_hour)
    NumberPicker pickerHour;
    @BindView(R.id.picker_minute)
    NumberPicker pickerMinute;
    @BindView(R.id.picker_date)
    NumberPicker pickerDate;

    String[] hours;
    String[] minutes;
    String[] dates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_reminder);
        ButterKnife.bind(this);


        dates = new String[]{"Th 5 1 Thg 11", "Th 5 1 Thg 11", "Th 6 1 Thg 10", "Th 2 1 Thg 12"};
        pickerDate.setMinValue(0);
        pickerDate.setMaxValue(dates.length-1);
        pickerDate.setDisplayedValues(dates);
        pickerDate.setOnValueChangedListener(this);

        hours = new String[24];
        for (int i=0; i < hours.length; i++) {
            hours[i] = String.valueOf(i);
        }
        pickerHour.setMinValue(0);
        pickerHour.setMaxValue(hours.length-1);
        pickerHour.setDisplayedValues(hours);
        pickerHour.setOnValueChangedListener(this);

        minutes = new String[59];
        for (int i=0; i < minutes.length; i++) {
            minutes[i] = String.valueOf(i);
        }
        pickerMinute.setMinValue(0);
        pickerMinute.setMaxValue(minutes.length-1);
        pickerMinute.setDisplayedValues(minutes);
        pickerMinute.setOnValueChangedListener(this);
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        switch (picker.getId()){
            case R.id.picker_hour:
                break;
            case R.id.picker_minute:
                break;
            case R.id.picker_date:
                break;
        }
    }


}
