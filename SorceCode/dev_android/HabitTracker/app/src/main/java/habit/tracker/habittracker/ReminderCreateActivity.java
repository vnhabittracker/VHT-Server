package habit.tracker.habittracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import habit.tracker.habittracker.common.util.AppGenerator;
import habit.tracker.habittracker.repository.Database;
import habit.tracker.habittracker.repository.reminder.ReminderEntity;

public class ReminderCreateActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener {
    public static final String IS_DELETE_REMINDER = "is_delete_reminder";
    public static final String REMINDER_ID = "reminder_id";
    public static final String POSITION_IN_LIST = "position_in_list";
    public static final String REMIND_TEXT = "remind_text";
    public static final String REMIND_HOUR = "remind_hour";
    public static final String REMIND_MINUTE = "remind_minute";
    public static final String REMIND_DATE = "remind_date";
    public static final String REMIND_TYPE = "remind_type";

    @BindView(R.id.edRemindText)
    EditText edRemindText;
    @BindView(R.id.picker_hour)
    NumberPicker pickerHour;
    @BindView(R.id.picker_minute)
    NumberPicker pickerMinute;
    @BindView(R.id.picker_date)
    NumberPicker pickerDate;

    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.btn_cancel)
    Button btnCancel;

    @BindView(R.id.btn_TypeAll)
    View btnTypeNoRepeat;
    @BindView(R.id.btn_TypeDaily)
    View btnTypeDaily;
    @BindView(R.id.btn_TypeWeekly)
    View btnTypeWeekly;
    @BindView(R.id.btn_TypeMonthly)
    View btnTypeMonthly;
    @BindView(R.id.btn_TypeYearly)
    View btnTypeYearly;
    View vType;

    boolean isUpdate = false;
    String[] hours;
    String[] minutes;
    String[] daysInYear;

    String reminderId;
    int positionInList;
    int hour;
    int minute;
    int type;
    String remindDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_reminder_create);
        ButterKnife.bind(this);

        hour = 0;
        minute = 0;
        type = -1;
        vType = btnTypeNoRepeat;

        String currentDate = AppGenerator.getCurrentDate(AppGenerator.YMD_SHORT);
        daysInYear = new String[1000];
        daysInYear[0] = currentDate;
        for (int i = 1; i < daysInYear.length; i++) {
            daysInYear[i] = AppGenerator.getNextDate(currentDate, AppGenerator.YMD_SHORT);
            currentDate = daysInYear[i];
        }

        Bundle data = getIntent().getExtras();
        if (data != null) {

            reminderId = data.getString(REMINDER_ID);

            if (!TextUtils.isEmpty(reminderId)) {
                Database db = Database.getInstance(this);
                db.open();

                ReminderEntity reminderEntity = Database.getReminderDb().getRemindersById(reminderId);
                if (reminderEntity != null) {

                    String t = reminderEntity.getReminderStartTime();
                    Date d = AppGenerator.getDate(t, AppGenerator.YMD2);
                    Calendar ca = Calendar.getInstance();
                    ca.setTime(d);

                    hour = ca.get(Calendar.HOUR_OF_DAY);
                    minute = ca.get(Calendar.MINUTE);
                    remindDate = AppGenerator.format(t, AppGenerator.YMD_SHORT, AppGenerator.YMD_SHORT);
                    type = Integer.parseInt(reminderEntity.getRepeatType());
                    setWhiteBg(btnTypeNoRepeat);
                    switch (type) {
                        case -1:
                            setWhiteBg(btnTypeNoRepeat);
                        case 0:
                            setGreenBg(btnTypeDaily);
                            break;
                        case 1:
                            setGreenBg(btnTypeWeekly);
                            break;
                        case 2:
                            setGreenBg(btnTypeMonthly);
                            break;
                        case 3:
                            setGreenBg(btnTypeYearly);
                            break;
                    }
                    edRemindText.setText(reminderEntity.getRemindText());
                    btnCancel.setText("Xóa");
                    btnSave.setText("Cập nhật");
                    positionInList = data.getInt(POSITION_IN_LIST, -1);
                    isUpdate = true;
                }
                db.close();
            }
        }

        String[] displayDates = getDisplayDate(daysInYear);
        pickerDate.setMinValue(0);
        pickerDate.setMaxValue(displayDates.length - 1);
        pickerDate.setWrapSelectorWheel(false);
        pickerDate.setDisplayedValues(displayDates);
        pickerDate.setOnValueChangedListener(this);
        remindDate = daysInYear[0];
        setPickerValue(displayDates, remindDate, pickerDate);

        hours = new String[24];
        for (int i = 0; i < hours.length; i++) {
            hours[i] = String.valueOf(i);
        }
        pickerHour.setMinValue(0);
        pickerHour.setMaxValue(hours.length - 1);
        pickerHour.setDisplayedValues(getStringArr(hours));
        pickerHour.setOnValueChangedListener(this);
        setPickerValue(hours, String.valueOf(hour), pickerHour);

        minutes = new String[60];
        for (int i = 0; i < minutes.length; i++) {
            minutes[i] = String.valueOf(i);
        }
        pickerMinute.setMinValue(0);
        pickerMinute.setMaxValue(minutes.length - 1);
        pickerMinute.setDisplayedValues(getStringArr(minutes));
        pickerMinute.setOnValueChangedListener(this);
        setPickerValue(minutes, String.valueOf(minute), pickerMinute);
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        switch (picker.getId()){
            case R.id.picker_hour:
                hour = Integer.parseInt(hours[newVal]);
                break;
            case R.id.picker_minute:
                minute = Integer.parseInt(minutes[newVal]);
                break;
            case R.id.picker_date:
                remindDate = daysInYear[newVal];
                break;
        }
    }

    @OnClick(R.id.btn_cancel)
    public void cancel(View v) {
        if (isUpdate) {
            Intent intent = getIntent();
            intent.putExtra(IS_DELETE_REMINDER, true);
            intent.putExtra(POSITION_IN_LIST, positionInList);
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    @OnClick(R.id.btn_save)
    public void save(View v) {
        if (TextUtils.isEmpty(edRemindText.getText().toString().trim())) {
            Toast.makeText(this, "Chưa thêm nội dung nhắc nhở.", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = getIntent();
        intent.putExtra(POSITION_IN_LIST, positionInList);
        intent.putExtra(REMINDER_ID, reminderId);
        intent.putExtra(REMIND_TEXT, edRemindText.getText().toString());
        intent.putExtra(REMIND_HOUR, hour);
        intent.putExtra(REMIND_MINUTE, minute);
        intent.putExtra(REMIND_DATE, remindDate);
        intent.putExtra(REMIND_TYPE, type);
        setResult(RESULT_OK, intent);
        finish();
    }

    @OnClick({R.id.btn_TypeAll, R.id.btn_TypeDaily, R.id.btn_TypeWeekly, R.id.btn_TypeMonthly, R.id.btn_TypeYearly})
    public void setHabitType(View view) {
        setWhiteBg(vType);
        setGreenBg(view);
        vType = view;
        type = Integer.parseInt(view.getTag().toString());
    }

    private void setPickerValue(String[] values, String defaultValue, NumberPicker picker) {
        for (int i = 0; i < values.length; i++) {
            if (values[i].equals(defaultValue)) {
                picker.setValue(i);
                break;
            }
        }
    }

    private String[] getDisplayDate(String[] dates) {
        String[] res = new String[dates.length];
        int year;
        int month;
        int day;
        String[] arr;
        String str;
        Calendar ca = Calendar.getInstance();
        for (int i=0; i < dates.length; i++) {
            arr = dates[i].split("-");
            year = Integer.parseInt(arr[0]);
            month = Integer.parseInt(arr[1]);
            day = Integer.parseInt(arr[2]);
            ca.set(year, month - 1, day);
            str = "";
            switch(ca.get(Calendar.DAY_OF_WEEK)){
                case Calendar.MONDAY:
                    str = "Th 2";
                    break;
                case Calendar.TUESDAY:
                    str = "Th 3";
                    break;
                case Calendar.WEDNESDAY:
                    str = "Th 4";
                    break;
                case Calendar.THURSDAY:
                    str = "Th 5";
                    break;
                case Calendar.FRIDAY:
                    str = "Th 6";
                    break;
                case Calendar.SATURDAY:
                    str = "Th 7";
                    break;
                case Calendar.SUNDAY:
                    str = "CN";
                    break;
            }
            str += " "+ day + " Th " + month + " " + year;
            res[i] = str;
        }
        return res;
    }

    private String[] getStringArr(String[] arr) {
        String[] res = new String[arr.length];
        for (int i = 0; i < arr.length; i++) {
            res[i] = String.valueOf(arr[i]);
        }
        return res;
    }

    public void setGreenBg(View v) {
        v.setBackground(ContextCompat.getDrawable(this, R.drawable.button_green));
    }

    public void setWhiteBg(View v) {
        v.setBackground(ContextCompat.getDrawable(this, android.R.color.transparent));
    }
}
