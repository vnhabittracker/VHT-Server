package habit.tracker.habittracker;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import habit.tracker.habittracker.adapter.RemindRecyclerViewAdapter;
import habit.tracker.habittracker.api.VnHabitApiUtils;
import habit.tracker.habittracker.api.model.habit.Habit;
import habit.tracker.habittracker.api.model.reminder.Reminder;
import habit.tracker.habittracker.api.service.VnHabitApiService;
import habit.tracker.habittracker.common.Validator;
import habit.tracker.habittracker.common.ValidatorType;
import habit.tracker.habittracker.common.util.AppGenerator;
import habit.tracker.habittracker.common.util.MySharedPreference;
import habit.tracker.habittracker.common.util.ReminderManager;
import habit.tracker.habittracker.repository.Database;
import habit.tracker.habittracker.repository.group.GroupEntity;
import habit.tracker.habittracker.repository.habit.HabitEntity;
import habit.tracker.habittracker.repository.reminder.ReminderEntity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HabitActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public static final int SELECT_GROUP = 1;
    public static final int ADD_REMINDER = 2;
    public static final int REQUEST_UPDATE = 0;

    public static final String TYPE_0 = "0";
    public static final String TYPE_1 = "1";
    public static final String TYPE_2 = "2";
    public static final String TYPE_3 = "3";
    public static final String TYPE_4 = "4";
    public static final String TYPE_5 = "5";
    public static final String TYPE_6 = "6";
    public static final String TYPE_7 = "7";
    public static final String TYPE_8 = "8";
    public static final String TYPE_9 = "9";
    public static final String DAY_OF_WEEK = "day_of_week";

    @BindView(R.id.edit_habitName)
    EditText editHabitName;
    String initHabitId;

    @BindView(R.id.btn_TargetBuild)
    Button btnHabitBuild;
    @BindView(R.id.btn_TargetQuit)
    Button btnHabitQuit;
    int habitTarget = 0;

    View btnHabitType;
    @BindView(R.id.btn_TypeDaily)
    Button btnDaily;
    @BindView(R.id.btn_TypeWeekly)
    Button btnWeekly;
    @BindView(R.id.btn_TypeMonthly)
    Button btnMonthly;
    @BindView(R.id.btn_TypeYearly)
    Button btnYearly;
    int habitType = 0;

    @BindView(R.id.tv_count_unit)
    TextView tvCountUnit;
    @BindView(R.id.ll_checkDone)
    View chkMonitorCheck;
    @BindView(R.id.ll_checkCount)
    View chkMonitorCount;
    @BindView(R.id.img_checkDone)
    ImageView imgTypeCheck;
    @BindView(R.id.img_checkCount)
    ImageView imgTypeCount;
    @BindView(R.id.edit_checkNumber)
    EditText editCheckNumber;
    @BindView(R.id.edit_monitorUnit)
    EditText editMonitorUnit;
    int monitorType = 0;

    @BindView(R.id.ll_group)
    View selGroup;
    @BindView(R.id.tv_groupName)
    TextView tvGroupName;
    String savedGroupId;

    @BindView(R.id.btnMon)
    TextView btnMon;
    @BindView(R.id.btnTue)
    TextView btnTue;
    @BindView(R.id.btnWed)
    TextView btnWed;
    @BindView(R.id.btnThu)
    TextView btnThu;
    @BindView(R.id.btnFri)
    TextView btnFri;
    @BindView(R.id.btnSat)
    TextView btnSat;
    @BindView(R.id.btnSun)
    TextView btnSun;
    boolean[] monitorDate = new boolean[7];
    String savedMonitorDateId;

    @BindView(R.id.color1)
    View color1;
    @BindView(R.id.color2)
    View color2;
    @BindView(R.id.color3)
    View color3;
    @BindView(R.id.color4)
    View color4;
    @BindView(R.id.color5)
    View color5;
    @BindView(R.id.color6)
    View color6;
    @BindView(R.id.color7)
    View color7;
    @BindView(R.id.color8)
    View color8;
    @BindView(R.id.color9)
    View color9;
    @BindView(R.id.color10)
    View color10;
    View habitColor;
    String habitColorCode;
    int[] colors = new int[]{
            R.color.color0,
            R.color.color1,
            R.color.color2,
            R.color.color3,
            R.color.color4,
            R.color.color5,
            R.color.color6,
            R.color.color7,
            R.color.color8,
            R.color.color9
    };
    List<String> colorsList;

    @BindView(R.id.ll_start_date)
    View mStartDate;
    @BindView(R.id.ll_end_date)
    View mEndDate;
    @BindView(R.id.check_startDate)
    ImageView chkStartDate;
    @BindView(R.id.check_endDate)
    ImageView chkEndDate;
    @BindView(R.id.edit_startDate)
    TextView tvStartDate;
    @BindView(R.id.edit_endDate)
    TextView tvEndDate;

    String startHabitDate;
    String endHabitDate;
    boolean[] enableHabitLimitTime = new boolean[2];
    boolean onSetStartDate = false;

    static final int MODE_CREATE = 0;
    static final int MODE_UPDATE = 1;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    int createMode = MODE_CREATE;

    @BindView(R.id.btn_addReminder)
    View btnAddReminder;

    @BindView(R.id.spinner_repeat)
    EditText editDescription;

    @BindView(R.id.rvRemind)
    RecyclerView rvRemind;
    List<Reminder> remindDispList = new ArrayList<>();
    List<Reminder> remindAddNew = new ArrayList<>();
    RemindRecyclerViewAdapter remindAdapter;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_GROUP) {
                if (data != null && data.getExtras() != null) {
                    savedGroupId = data.getStringExtra(GroupActivity.GROUP_ID);
                    tvGroupName.setText(data.getStringExtra(GroupActivity.GROUP_NAME));
                }
            } else if (requestCode == ADD_REMINDER) {
                if (data != null && data.getExtras() != null) {
                    String remindType = String.valueOf(
                            data.getIntExtra(ReminderCreateActivity.REMIND_TYPE, -1));
                    String remindText = data.getStringExtra(ReminderCreateActivity.REMIND_TEXT);
                    String date = data.getStringExtra(ReminderCreateActivity.REMIND_DATE);
                    String format = "%02d";
                    String hour = String.format(format, data.getIntExtra(ReminderCreateActivity.REMIND_HOUR, 0));
                    String minute = String.format(format, data.getIntExtra(ReminderCreateActivity.REMIND_MINUTE, 0));

                    Reminder reminder = new Reminder();
                    reminder.setServerId(AppGenerator.getNewId());
                    reminder.setRemindText(remindText);
                    reminder.setReminderTime(date + " " + hour + ":" + minute);
                    reminder.setRepeatType(remindType);
                    remindDispList.add(reminder);
                    remindAddNew.add(reminder);
                    remindAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_habit);
        ButterKnife.bind(this);

        // init habit type: daily
        btnHabitType = btnDaily;

        // init habit color
        colorsList = new ArrayList<>();
        for (int colorId : colors) {
            colorsList.add(getResources().getString(colorId));
        }
        color1.setBackground(getCircleBackground(colorsList.get(0)));
        color2.setBackground(getCircleBackground(colorsList.get(1)));
        color3.setBackground(getCircleBackground(colorsList.get(2)));
        color4.setBackground(getCircleBackground(colorsList.get(3)));
        color5.setBackground(getCircleBackground(colorsList.get(4)));
        color6.setBackground(getCircleBackground(colorsList.get(5)));
        color7.setBackground(getCircleBackground(colorsList.get(6)));
        color8.setBackground(getCircleBackground(colorsList.get(7)));
        color9.setBackground(getCircleBackground(colorsList.get(8)));
        color10.setBackground(getCircleBackground(colorsList.get(9)));
        setHabitColor(color1);

        // start and end date
        startHabitDate = AppGenerator.getCurrentDate(AppGenerator.YMD_SHORT);
        endHabitDate = AppGenerator.getNextDate(startHabitDate, AppGenerator.YMD_SHORT);

        // init remind list
        remindAdapter = new RemindRecyclerViewAdapter(this, remindDispList);
        rvRemind.setLayoutManager(new LinearLayoutManager(this));
        rvRemind.setOnClickListener(null);
        rvRemind.setAdapter(remindAdapter);

        // load habit from local trackingItemList
        Bundle data = getIntent().getExtras();
        if (data != null) {

            initHabitId = data.getString(MainActivity.HABIT_ID, null);

            if (initHabitId != null) {

                // mode CREATE
                createMode = 1;
                initFromSavedHabit(initHabitId);
            }

        } else {

            // init monitor date
            setMonitorDate(btnMon);
            setMonitorDate(btnTue);
            setMonitorDate(btnWed);
            setMonitorDate(btnThu);
            setMonitorDate(btnFri);
            setMonitorDate(btnSat);
            setMonitorDate(btnSun);

            // set plan date
            tvStartDate.setText(startHabitDate);
            tvEndDate.setText(endHabitDate);
        }
    }

    private void initFromSavedHabit(String habitId) {
        if (habitId != null) {
            // change UI for update and delete
            btnSave.setText("Cập nhật");
            btnCancel.setText("Xóa");

            Database db = new Database(this);
            db.open();
            HabitEntity habitEntity = Database.habitDaoImpl.getHabit(habitId);

            if (habitEntity != null) {

                if (habitEntity.getGroupId() != null) {
                    this.savedGroupId = habitEntity.getGroupId();
                    GroupEntity groupEntity = Database.groupDaoImpl.getGroup(habitEntity.getGroupId());
                    tvGroupName.setText(groupEntity.getGroupName());
                }
                if (habitEntity.getMonitorId() != null) {
                    this.savedMonitorDateId = habitEntity.getMonitorId();
                }

                // habit name
                editHabitName.setText(habitEntity.getHabitName());

                // habit stat and end date
                startHabitDate = habitEntity.getStartDate();
                endHabitDate = habitEntity.getEndDate();

                // habit target
                switch (habitEntity.getHabitTarget()) {
                    case TYPE_0:
                        setHabitTarget(btnHabitBuild);
                        break;
                    case TYPE_1:
                        setHabitTarget(btnHabitQuit);
                        break;
                }

                // monitor date
                if (habitEntity.getMon() != null && habitEntity.getMon().equals(TYPE_1)) {
                    setMonitorDate(btnMon);
                }
                if (habitEntity.getTue() != null && habitEntity.getTue().equals(TYPE_1)) {
                    setMonitorDate(btnTue);
                }
                if (habitEntity.getWed() != null && habitEntity.getWed().equals(TYPE_1)) {
                    setMonitorDate(btnWed);
                }
                if (habitEntity.getThu() != null && habitEntity.getThu().equals(TYPE_1)) {
                    setMonitorDate(btnThu);
                }
                if (habitEntity.getFri() != null && habitEntity.getFri().equals(TYPE_1)) {
                    setMonitorDate(btnFri);
                }
                if (habitEntity.getSat() != null && habitEntity.getSat().equals(TYPE_1)) {
                    setMonitorDate(btnSat);
                }
                if (habitEntity.getSat() != null && habitEntity.getSun().equals(TYPE_1)) {
                    setMonitorDate(btnSun);
                }

                // start and end date of habit
                if (habitEntity.getStartDate() != null) {
                    setHabitDate(mStartDate);
                    tvStartDate.setText(habitEntity.getStartDate());
                }
                if (habitEntity.getEndDate() != null) {
                    setHabitDate(mEndDate);
                    tvEndDate.setText(habitEntity.getEndDate());
                }

                // habit type
                switch (habitEntity.getHabitType()) {
                    case TYPE_0:
                        setHabitType(btnDaily);
                        break;
                    case TYPE_1:
                        setHabitType(btnWeekly);
                        break;
                    case TYPE_2:
                        setHabitType(btnMonthly);
                        break;
                    case TYPE_3:
                        setHabitType(btnYearly);
                        break;
                }
                switch (habitEntity.getMonitorType()) {
                    case TYPE_0:
                        selectMonitorType(chkMonitorCheck);
                        break;
                    case TYPE_1:
                        selectMonitorType(chkMonitorCount);
                        break;
                }

                // habit monitor type
                switch (habitEntity.getMonitorType()) {
                    case TYPE_0:
                        selectMonitorType(chkMonitorCheck);
                        break;
                    case TYPE_1:
                        selectMonitorType(chkMonitorCount);
                        break;
                }
                editCheckNumber.setText(habitEntity.getMonitorNumber());
                editMonitorUnit.setText(habitEntity.getMonitorUnit());

                // habit color
                if (habitEntity.getHabitColor() != null) {
                    for (int i = 0; i < colorsList.size(); i++) {
                        String code = colorsList.get(i);
                        if (habitEntity.getHabitColor().equals(code)) {
                            switch (String.valueOf(i)) {
                                case TYPE_0:
                                    setHabitColor(color1);
                                    break;
                                case TYPE_1:
                                    setHabitColor(color2);
                                    break;
                                case TYPE_2:
                                    setHabitColor(color3);
                                    break;
                                case TYPE_3:
                                    setHabitColor(color4);
                                    break;
                                case TYPE_4:
                                    setHabitColor(color5);
                                    break;
                                case TYPE_5:
                                    setHabitColor(color6);
                                    break;
                                case TYPE_6:
                                    setHabitColor(color7);
                                    break;
                                case TYPE_7:
                                    setHabitColor(color8);
                                    break;
                                case TYPE_8:
                                    setHabitColor(color9);
                                    break;
                                case TYPE_9:
                                    setHabitColor(color10);
                                    break;
                            }
                        }
                    }
                }

                // habit reminders
                List<ReminderEntity> reminders = Database.reminderImpl.getRemindersByHabit(habitId);
                Reminder reminder;
                for (ReminderEntity entity : reminders) {
                    reminder = new Reminder();
                    reminder.setReminderId(entity.getReminderId());
                    reminder.setHabitId(habitId);
                    reminder.setRemindText(entity.getRemindText());
                    reminder.setReminderTime(entity.getReminderTime());
                    reminder.setRepeatType(entity.getRepeatType());
                    reminder.setServerId(entity.getServerId());
                    remindDispList.add(reminder);
                }
                remindAdapter.notifyDataSetChanged();

                // habit description
                editDescription.setText(habitEntity.getHabitDescription());
            }
            db.close();
        }
    }

    @SuppressLint("ResourceType")
    @OnClick(R.id.btn_save)
    public void saveHabit(View v) {
        // validate user input
        Validator validator = new Validator();
        validator.setErrorMsgListener(new Validator.ErrorMsg() {
            @Override
            public void showError(ValidatorType type, String key) {
                switch (type) {
                    case EMPTY:
                        Toast.makeText(HabitActivity.this, key + " không được trống", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        String savedUserId = MySharedPreference.getUserId(this);
        String habitName = editHabitName.getText().toString();
        String monitorNumber = this.editCheckNumber.getText().toString();
        if (!validator.checkEmpty("Tên thói quen", habitName)) {
            return;
        }
        String monitorUnit = null;
        if (this.monitorType == 1) {
            monitorUnit = this.editMonitorUnit.getText().toString();
            if (!validator.checkEmpty("Đơn vị", habitName)) {
                return;
            }
        }
        if (monitorType == 1 && !validator.checkNumber(monitorNumber, 1)) {
            Toast.makeText(HabitActivity.this, "Số lần phải lớn hon 0", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean isMonitor = false;
        for (boolean day : monitorDate) {
            if (day) {
                isMonitor = true;
                break;
            }
        }
        if (!isMonitor) {
            Toast.makeText(HabitActivity.this, "Phải chọn ít nhất một ngày trong tuần", Toast.LENGTH_LONG).show();
            return;
        }

        // collect user input
        Calendar ca = Calendar.getInstance();
        final Habit habit = new Habit();
        if (createMode == MODE_CREATE) {
            habit.setHabitId(AppGenerator.getNewId());
            habit.setMonitorId(AppGenerator.getNewId());
        } else if (createMode == MODE_UPDATE) {
            habit.setHabitId(this.initHabitId);
            habit.setMonitorId(this.savedMonitorDateId);
        }
        habit.setUserId(savedUserId);
        habit.setGroupId(this.savedGroupId);

        habit.setHabitName(habitName);
        habit.setHabitTarget(String.valueOf(this.habitTarget));
        habit.setHabitType(String.valueOf(this.habitType));
        habit.setMonitorType(String.valueOf(this.monitorType));
        habit.setMonitorUnit(monitorUnit);
        habit.setMonitorNumber(monitorNumber);

        habit.setStartDate(enableHabitLimitTime[0]? startHabitDate: null);
        habit.setEndDate(enableHabitLimitTime[1]? endHabitDate: null);
        habit.setCreatedDate(AppGenerator.getCurrentDate(AppGenerator.YMD_SHORT));

        habit.setHabitColor(habitColorCode);
        habit.setHabitDescription(this.editDescription.getText().toString());

        habit.setMon(String.valueOf(this.monitorDate[0] ? 1 : 0));
        habit.setTue(String.valueOf(this.monitorDate[1] ? 1 : 0));
        habit.setWed(String.valueOf(this.monitorDate[2] ? 1 : 0));
        habit.setThu(String.valueOf(this.monitorDate[3] ? 1 : 0));
        habit.setFri(String.valueOf(this.monitorDate[4] ? 1 : 0));
        habit.setSat(String.valueOf(this.monitorDate[5] ? 1 : 0));
        habit.setSun(String.valueOf(this.monitorDate[6] ? 1 : 0));

        for (Reminder reminder : remindDispList) {
            reminder.setHabitId(habit.getHabitId());
            reminder.setHabitName(habit.getHabitName());
            if (TextUtils.isEmpty(habit.getEndDate())) {
                reminder.setEndDate(habit.getEndDate());
            }
        }
        habit.setReminderList(remindDispList);

        // save habit
        Database db = new Database(HabitActivity.this);
        db.open();
        if (Database.getHabitDb().saveUpdateHabit(
                Database.getHabitDb().convert(habit))) {
            // save reminder list
            for (Reminder reminder : remindAddNew) {
                reminder.setHabitId(habit.getHabitId());
                int remindId = Database.reminderImpl.addReminder(
                        Database.reminderImpl.convert(reminder));
                reminder.setReminderId(String.valueOf(remindId));
            }
        }
        db.close();

        // call API to syn data
        VnHabitApiService mService = VnHabitApiUtils.getApiService();
        // create new habit
        if (createMode == MODE_CREATE) {
            mService.addHabit(habit).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Toast.makeText(HabitActivity.this, "Tạo thói quen thành công", Toast.LENGTH_LONG).show();
                    HabitActivity.this.setResult(HabitActivity.RESULT_OK);
                    ReminderManager reminderManager = new ReminderManager(HabitActivity.this, remindDispList);
                    reminderManager.start();
                    finish();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(HabitActivity.this, "Đã xãy ra lỗi", Toast.LENGTH_LONG).show();
                }
            });
        }
        // update habit
        else if (createMode == MODE_UPDATE) {
            mService.updateHabit(habit).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    HabitActivity.this.setResult(HabitActivity.RESULT_OK);
                    ReminderManager reminderManager = new ReminderManager(HabitActivity.this, remindDispList);
                    reminderManager.start();
                    finish();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(HabitActivity.this, "Đã xãy ra lỗi", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @OnClick({R.id.btn_back})
    public void back(View v) {
        finish();
    }

    @OnClick({R.id.btn_cancel})
    public void cancel(View v) {
        if (createMode == MODE_CREATE) {
            finish();

        } else if (createMode == MODE_UPDATE) {

            // delete habit
            Database db = new Database(this);
            db.open();
            Database.getHabitDb().deleteHabit(initHabitId);
            db.close();

            VnHabitApiService service = VnHabitApiUtils.getApiService();
            service.deleteHabit(this.initHabitId).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Toast.makeText(HabitActivity.this, "Đã xóa thói quen", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("delete", true);
                    HabitActivity.this.setResult(HabitActivity.RESULT_OK, intent);
                    finish();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(HabitActivity.this, "Đã xãy ra lỗi", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @OnClick(R.id.btn_addReminder)
    public void addReminder(View v) {
        Intent intent = new Intent(this, ReminderCreateActivity.class);
        intent.putExtra(DAY_OF_WEEK, monitorDate);
        startActivityForResult(intent, ADD_REMINDER);
    }

    @OnClick({R.id.btn_TargetBuild, R.id.btn_TargetQuit})
    public void setHabitTarget(View v) {
        setGreenBg(v);
        switch (v.getId()) {
            case R.id.btn_TargetBuild:
                setWhiteBg(btnHabitQuit);
                habitTarget = 0;
                break;
            case R.id.btn_TargetQuit:
                setWhiteBg(btnHabitBuild);
                habitTarget = 1;
                break;
        }
    }

    @OnClick({R.id.btn_TypeDaily, R.id.btn_TypeWeekly, R.id.btn_TypeMonthly, R.id.btn_TypeYearly})
    public void setHabitType(View view) {
        setWhiteBg(btnHabitType);
        setGreenBg(view);
        btnHabitType = view;
        habitType = Integer.parseInt(view.getTag().toString());
        switch (view.getId()) {
            case R.id.btn_TypeDaily:
                tvCountUnit.setText("một ngày");
                break;
            case R.id.btn_TypeWeekly:
                tvCountUnit.setText("một tuần");
                break;
            case R.id.btn_TypeMonthly:
                tvCountUnit.setText("một tháng");
                break;
            case R.id.btn_TypeYearly:
                tvCountUnit.setText("một năm");
                break;
        }
    }

    @OnClick({R.id.ll_checkDone, R.id.ll_checkCount})
    public void selectMonitorType(View view) {
        if (view.getId() == R.id.ll_checkDone) {
            uncheck(imgTypeCount);
            check(imgTypeCheck);
            editCheckNumber.setEnabled(false);
            editMonitorUnit.setEnabled(false);
            monitorType = 0;
        } else if (view.getId() == R.id.ll_checkCount) {
            uncheck(imgTypeCheck);
            check(imgTypeCount);
            editCheckNumber.setEnabled(true);
            editMonitorUnit.setEnabled(true);
            monitorType = 1;
        }
    }

    @OnClick({R.id.edit_startDate, R.id.edit_endDate})
    public void showDatePicker(View v) {
        Calendar ca = Calendar.getInstance();
        DatePickerDialog dialog;
        onSetStartDate = v.getId() == R.id.edit_startDate;

        if (v.getId() == R.id.edit_startDate) {
            ca.setTime(AppGenerator.getDate(startHabitDate, AppGenerator.YMD_SHORT));
            dialog = new DatePickerDialog(this, this,
                    ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DATE));
            dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            dialog.show();

        } else {
            String end = endHabitDate;
            if (TextUtils.isEmpty(end)) {
                end = startHabitDate;
            }
            ca.setTime(AppGenerator.getDate(end, AppGenerator.YMD_SHORT));
            dialog = new DatePickerDialog(this, this,
                    ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DATE));

            ca.setTime(AppGenerator.getDate(startHabitDate, AppGenerator.YMD_SHORT));
            dialog.getDatePicker().setMinDate(ca.getTimeInMillis());
            dialog.show();
        }

        Button pos = dialog.getButton(DatePickerDialog.BUTTON_POSITIVE);
        pos.setAllCaps(false);
        pos.setText("Chọn");
        pos.setTextColor(getResources().getColor(R.color.colorAccent));
        Button nav = dialog.getButton(DatePickerDialog.BUTTON_NEGATIVE);
        nav.setAllCaps(false);
        nav.setText("Hủy");
        nav.setTextColor(getResources().getColor(R.color.colorAccent));
    }

    @OnClick({R.id.ll_start_date, R.id.ll_end_date})
    public void setHabitDate(View v) {
        if (v.getId() == R.id.ll_start_date) {
            if (enableHabitLimitTime[0]) {
                uncheckBox(chkStartDate);
            } else {
                checkBox(chkStartDate);
            }
            enableHabitLimitTime[0] = !enableHabitLimitTime[0];
            tvStartDate.setEnabled(enableHabitLimitTime[0]);
        } else if (v.getId() == R.id.ll_end_date) {
            if (enableHabitLimitTime[1]) {
                uncheckBox(chkEndDate);
            } else {
                checkBox(chkEndDate);
            }
            enableHabitLimitTime[1] = !enableHabitLimitTime[1];
            tvEndDate.setEnabled(enableHabitLimitTime[1]);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        if (onSetStartDate) {
            startHabitDate = AppGenerator.getDate(year, month + 1, day, AppGenerator.YMD_SHORT);
            tvStartDate.setText(AppGenerator.format(startHabitDate, AppGenerator.YMD_SHORT, AppGenerator.DMY_SHORT));

        } else {
            endHabitDate = AppGenerator.getDate(year, month + 1, day, AppGenerator.YMD_SHORT);
            tvEndDate.setText(AppGenerator.format(endHabitDate, AppGenerator.YMD_SHORT, AppGenerator.DMY_SHORT));
        }
    }

    @OnClick(R.id.ll_group)
    public void selectGroup(View view) {
        Intent intent = new Intent(this, GroupActivity.class);
        startActivityForResult(intent, SELECT_GROUP);
    }

    @OnClick({R.id.btnMon, R.id.btnTue, R.id.btnWed, R.id.btnThu, R.id.btnFri, R.id.btnSat, R.id.btnSun})
    public void setMonitorDate(View v) {
        int tag = Integer.parseInt(v.getTag().toString());
        if (monitorDate[tag]) {
            uncheckDate(v);
        } else {
            checkDate(v);
        }
        monitorDate[tag] = !monitorDate[tag];
    }

    @OnClick({R.id.color1,
            R.id.color2,
            R.id.color3,
            R.id.color4,
            R.id.color5,
            R.id.color6,
            R.id.color7,
            R.id.color8,
            R.id.color9,
            R.id.color10})
    public void setHabitColor(View v) {
        int idx = 0;
        if (habitColor != null) {
            idx = Integer.parseInt(habitColor.getTag().toString());
            unpickColor(habitColor, colorsList.get(idx));
        }
        idx = Integer.parseInt(v.getTag().toString());
        pickColor(v, colors[idx]);
        habitColor = v;
        habitColorCode = getResources().getString(colors[idx]);
    }

    public void setGreenBg(View v) {
        v.setBackground(ContextCompat.getDrawable(this, R.drawable.button_green));
    }

    public void setWhiteBg(View v) {
        v.setBackground(ContextCompat.getDrawable(this, android.R.color.transparent));
    }

    public void check(ImageView img) {
//        img.setImageResource(R.drawable.ck_checked);
        img.setImageResource(R.drawable.rd_checked);
    }

    public void uncheck(ImageView img) {
//        img.setImageResource(R.drawable.ck_unchecked);
        img.setImageResource(R.drawable.rd_unchecked);
    }

    public void checkBox(ImageView img) {
        img.setImageResource(R.drawable.ck_checked);
    }

    public void uncheckBox(ImageView img) {
        img.setImageResource(R.drawable.ck_unchecked);
    }

    public void checkDate(View v) {
        v.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_circle));
    }

    public void uncheckDate(View v) {
        v.setBackground(ContextCompat.getDrawable(this, android.R.color.transparent));
    }

    public void pickColor(View v, int color) {
        v.setBackground(getCircleCheckBackground(color));
    }

    public void unpickColor(View v, String colorCode) {
        v.setBackground(getCircleBackground(colorCode));
    }

    public void showEmpty(View v) {
        Intent intent = new Intent(HabitActivity.this, EmptyActivity.class);
        HabitActivity.this.startActivity(intent);
    }

    private Drawable getCircleBackground(String colorCode) {
        Drawable mDrawable = ContextCompat.getDrawable(this, R.drawable.bg_circle);
        if (mDrawable != null) {
            mDrawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor(colorCode), PorterDuff.Mode.MULTIPLY));
        }
        return mDrawable;
    }

    private Drawable getCircleCheckBackground(int color) {
        Drawable mDrawable = ContextCompat.getDrawable(this, R.drawable.bg_circle_check);
        if (mDrawable != null) {
            mDrawable.setColorFilter(new PorterDuffColorFilter(this.getResources().getColor(color), PorterDuff.Mode.MULTIPLY));
        }
        return mDrawable;
    }
}
