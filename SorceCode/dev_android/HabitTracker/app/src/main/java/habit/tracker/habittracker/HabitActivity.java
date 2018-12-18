package habit.tracker.habittracker;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
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
import android.view.MotionEvent;
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
import habit.tracker.habittracker.adapter.RecyclerViewItemClickListener;
import habit.tracker.habittracker.adapter.RemindRecyclerViewAdapter;
import habit.tracker.habittracker.adapter.search.HabitTextWatcher;
import habit.tracker.habittracker.adapter.search.SearchRecyclerViewAdapter;
import habit.tracker.habittracker.api.VnHabitApiUtils;
import habit.tracker.habittracker.api.model.habit.Habit;
import habit.tracker.habittracker.api.model.reminder.Reminder;
import habit.tracker.habittracker.api.model.search.HabitSuggestion;
import habit.tracker.habittracker.api.service.VnHabitApiService;
import habit.tracker.habittracker.common.AppConstant;
import habit.tracker.habittracker.common.dialog.AppDialogHelper;
import habit.tracker.habittracker.common.habitreminder.HabitReminderManager;
import habit.tracker.habittracker.common.util.AppGenerator;
import habit.tracker.habittracker.common.util.MySharedPreference;
import habit.tracker.habittracker.common.validator.Validator;
import habit.tracker.habittracker.common.validator.ValidatorType;
import habit.tracker.habittracker.repository.Database;
import habit.tracker.habittracker.repository.group.GroupEntity;
import habit.tracker.habittracker.repository.habit.HabitEntity;
import habit.tracker.habittracker.repository.reminder.ReminderEntity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HabitActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public static final int REQUEST_UPDATE = 0;
    public static final int SELECT_GROUP = 1;
    public static final int ADD_REMINDER = 2;
    public static final int GET_SUGGEST = 3;

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

    VnHabitApiService mApiService = VnHabitApiUtils.getApiService();

    @BindView(R.id.header)
    View header;
    @BindView(R.id.edit_habitName)
    EditText editHabitName;
    @BindView(R.id.rvHabitSuggestion)
    RecyclerView rvHabitSuggestion;

    SearchRecyclerViewAdapter habitSuggestionAdapter;
    List<HabitSuggestion> searchResultList = new ArrayList<>();

    String initHabitId;
    String suggestHabitNameId;
    String suggestHabitName;
    HabitTextWatcher habitTextWatcher = new HabitTextWatcher(this);

    @BindView(R.id.btn_suggestHabit)
    View btnSuggestHabit;

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

    @BindView(R.id.ll_GroupName)
    View selGroup;
    @BindView(R.id.tv_groupName)
    TextView tvGroupName;
    String selectedGroupId;

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
    boolean[] enableHabitRangeTime = new boolean[2];
    boolean onSetStartDate = false;

    static final int MODE_CREATE = 0;
    static final int MODE_UPDATE = 1;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    int screenMode = MODE_CREATE;

    @BindView(R.id.btn_addReminder)
    View btnAddReminder;

    @BindView(R.id.edit_habitDes)
    EditText editDescription;

    @BindView(R.id.rvRemind)
    RecyclerView rvRemind;
    List<Reminder> reminderDisplayList = new ArrayList<>();
    List<Reminder> reminderUpdateList = new ArrayList<>();
    RemindRecyclerViewAdapter reminderAdapter;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == SELECT_GROUP) {
            if (resultCode == RESULT_OK) {
                if (data != null && data.getExtras() != null) {
                    selectedGroupId = data.getStringExtra(GroupActivity.GROUP_ID);
                    Database db = Database.getInstance(HabitActivity.this);
                    db.open();
                    GroupEntity groupEntity = Database.getGroupDb().getGroup(selectedGroupId);
                    db.close();
                    if (groupEntity != null) {
                        tvGroupName.setText(data.getStringExtra(GroupActivity.GROUP_NAME));
                    } else {
                        selectedGroupId = null;
                        tvGroupName.setText("Chưa có");
                    }
                }
            } else {
                Database db = Database.getInstance(HabitActivity.this);
                db.open();
                GroupEntity entity = Database.getGroupDb().getGroup(selectedGroupId);
                if (entity == null || entity.isDelete()) {
                    selectedGroupId = null;
                }
                db.close();
            }
        } else if (requestCode == ADD_REMINDER) {
            if (resultCode == RESULT_OK) {
                if (data != null && data.getExtras() != null) {

                    boolean isDelete = data.getBooleanExtra(ReminderCreateActivity.IS_DELETE_REMINDER, false);
                    int pos = data.getIntExtra(ReminderCreateActivity.POSITION_IN_LIST, -1);

                    String reminderId = data.getStringExtra(ReminderCreateActivity.REMINDER_ID);
                    String remindType = String.valueOf(data.getIntExtra(ReminderCreateActivity.REMIND_TYPE, -1));
                    String remindText = data.getStringExtra(ReminderCreateActivity.REMIND_TEXT);
                    String hour = String.format(AppConstant.format2D, data.getIntExtra(ReminderCreateActivity.REMIND_HOUR, 0));
                    String minute = String.format(AppConstant.format2D, data.getIntExtra(ReminderCreateActivity.REMIND_MINUTE, 0));
                    String date = data.getStringExtra(ReminderCreateActivity.REMIND_DATE);

                    if (TextUtils.isEmpty(reminderId)) {
                        // add new
                        Reminder reminder = new Reminder();
                        reminder.setServerId(AppGenerator.getNewId());
                        reminder.setRemindText(remindText);
                        reminder.setRemindStartTime(date + " " + hour + ":" + minute);
                        reminder.setRepeatType(remindType);
                        reminderDisplayList.add(reminder);
                        reminderUpdateList.add(reminder);

                    } else {
                        // update
                        boolean f = false;
                        Reminder reminder = reminderDisplayList.get(pos);
                        reminder.setRemindText(remindText);
                        reminder.setRepeatType(remindType);
                        reminder.setRemindStartTime(date + " " + hour + ":" + minute);
                        for (Reminder item : reminderUpdateList) {
                            if (item.getReminderId().equals(reminderId)) {
                                item.setDelete(isDelete);
                                f = true;
                                break;
                            }
                        }
                        if (!f) {
                            reminderUpdateList.add(reminder);
                        }
                        if (isDelete) {
                            reminderDisplayList.remove(pos);
                        }
                    }
                    reminderAdapter.notifyDataSetChanged();
                }
            }

        } else if (requestCode == GET_SUGGEST) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    suggestHabitNameId = data.getStringExtra(SuggestionByGroupActivity.SUGGEST_HABIT_ID);
                    suggestHabitName = data.getStringExtra(SuggestionByGroupActivity.SUGGEST_HABIT_NAME_UNI);
                    habitTextWatcher.setAfterSelection(true);
                    editHabitName.setText(suggestHabitName);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_habit);
        ButterKnife.bind(this);

        final HabitTextWatcher habitNameTextWatcher = new HabitTextWatcher(this);
        habitSuggestionAdapter = new SearchRecyclerViewAdapter(this, searchResultList, new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                habitNameTextWatcher.setAfterSelection(true);
                suggestHabitNameId = searchResultList.get(position).getHabitNameId();
                suggestHabitName = searchResultList.get(position).getHabitNameUni();
                editHabitName.setText(suggestHabitName);
                searchResultList.clear();
                habitSuggestionAdapter.notifyDataSetChanged();
            }
        });
        rvHabitSuggestion.setHasFixedSize(true);
        rvHabitSuggestion.setLayoutManager(new LinearLayoutManager(this));
        rvHabitSuggestion.setAdapter(habitSuggestionAdapter);
        rvHabitSuggestion.setItemAnimator(null);

        // get habit name suggestion
        habitNameTextWatcher.setSearchResultList(searchResultList);
        habitNameTextWatcher.setAdapter(habitSuggestionAdapter);
        editHabitName.addTextChangedListener(habitNameTextWatcher);

        // init remind list
        reminderAdapter = new RemindRecyclerViewAdapter(this, reminderDisplayList);
        reminderAdapter.setListener(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                Intent intent = new Intent(HabitActivity.this, ReminderCreateActivity.class);
                intent.putExtra(ReminderCreateActivity.REMINDER_ID, reminderDisplayList.get(position).getReminderId());
                intent.putExtra(ReminderCreateActivity.POSITION_IN_LIST, position);
                startActivityForResult(intent, ADD_REMINDER);
            }
        });
        rvRemind.setLayoutManager(new LinearLayoutManager(this));
        rvRemind.setAdapter(reminderAdapter);

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

        // start and end remindDate
        startHabitDate = AppGenerator.getCurrentDate(AppGenerator.YMD_SHORT);
        endHabitDate = getEndDateByMonitorType();

        // load habit from local itemList
        Bundle data = getIntent().getExtras();

        if (data != null) {
            initHabitId = data.getString(AppConstant.HABIT_ID, null);
            suggestHabitNameId = data.getString(ProfileActivity.SUGGEST_NAME_ID, null);
            suggestHabitName = data.getString(ProfileActivity.SUGGEST_NAME, null);
            editHabitName.setText(suggestHabitName);
            habitNameTextWatcher.setAfterSelection(true);
        }

        if (!TextUtils.isEmpty(initHabitId)) {
            if (initHabitId != null) {
                // mode UPDATE
                screenMode = MODE_UPDATE;
                initializeBySavedHabit(initHabitId);
            }

        } else {
            // init monitor remindDate
            setMonitorDate(btnMon);
            setMonitorDate(btnTue);
            setMonitorDate(btnWed);
            setMonitorDate(btnThu);
            setMonitorDate(btnFri);
            setMonitorDate(btnSat);
            setMonitorDate(btnSun);

            // set plan remindDate
            tvStartDate.setText(AppGenerator.format(startHabitDate, AppGenerator.YMD_SHORT, AppGenerator.DMY_SHORT));
            tvEndDate.setText(AppGenerator.format(endHabitDate, AppGenerator.YMD_SHORT, AppGenerator.DMY_SHORT));
        }
    }

    private void initializeBySavedHabit(String habitId) {
        if (habitId != null) {
            Database db = new Database(this);
            db.open();

            // change UI for update and delete
            btnSave.setText("Cập nhật");
            btnCancel.setText("Xóa");

            HabitEntity habitEntity = Database.getHabitDb().getHabit(habitId);
            if (habitEntity == null) {
                return;
            }

            // set group
            if (habitEntity.getGroupId() != null) {
                this.selectedGroupId = habitEntity.getGroupId();
                GroupEntity groupEntity = Database.groupDaoImpl.getGroup(habitEntity.getGroupId());
                if (groupEntity != null) {
                    tvGroupName.setText(groupEntity.getGroupName());
                }
            }

            // set monitor days in week
            if (habitEntity.getMonitorId() != null) {
                this.savedMonitorDateId = habitEntity.getMonitorId();
            }
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

            suggestHabitName = habitEntity.getHabitName();
            suggestHabitNameId = habitEntity.getHabitNameId();

            // set habit name
            editHabitName.setText(habitEntity.getHabitName());

            // set habit target
            setHabitTarget(habitEntity.getHabitTarget().equals(TYPE_0) ? btnHabitBuild : btnHabitQuit);

            // set start and end remindDate of habit
            if (habitEntity.getStartDate() != null) {
                startHabitDate = habitEntity.getStartDate();
                enableHabitDateRange(mStartDate);
                tvStartDate.setText(AppGenerator.format(startHabitDate, AppGenerator.YMD_SHORT, AppGenerator.DMY_SHORT));
            }
            if (habitEntity.getEndDate() != null) {
                endHabitDate = habitEntity.getEndDate();
                enableHabitDateRange(mEndDate);
                tvEndDate.setText(AppGenerator.format(endHabitDate, AppGenerator.YMD_SHORT, AppGenerator.DMY_SHORT));
            } else {
                tvEndDate.setText(AppGenerator.format(endHabitDate, AppGenerator.YMD_SHORT, AppGenerator.DMY_SHORT));
            }

            // set habit type
            switch (habitEntity.getHabitType()) {
                case TYPE_0:
                    selectHabitType(btnDaily);
                    break;
                case TYPE_1:
                    selectHabitType(btnWeekly);
                    break;
                case TYPE_2:
                    selectHabitType(btnMonthly);
                    break;
                case TYPE_3:
                    selectHabitType(btnYearly);
                    break;
            }

            // set habit monitor info
            enableMonitorType(habitEntity.getMonitorType().equals(TYPE_0) ? chkMonitorCheck : chkMonitorCount);
            editCheckNumber.setText(habitEntity.getMonitorNumber());
            editMonitorUnit.setText(!TextUtils.isEmpty(habitEntity.getMonitorUnit()) ? habitEntity.getMonitorUnit() : "lần");

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
            List<ReminderEntity> reminderEntityList = Database.getReminderDb().getRemindersByHabit(habitId);
            Reminder reminder;
            for (ReminderEntity entity : reminderEntityList) {
                reminder = new Reminder();
                reminder.setReminderId(entity.getReminderId());
                reminder.setHabitId(habitId);
                reminder.setRemindText(entity.getRemindText());
                reminder.setRemindStartTime(entity.getReminderStartTime());
                reminder.setRepeatType(entity.getRepeatType());
                reminder.setServerId(entity.getServerId());
                reminderDisplayList.add(reminder);
            }
            reminderUpdateList.addAll(reminderDisplayList);
            reminderAdapter.notifyDataSetChanged();

            // habit description
            editDescription.setText(habitEntity.getDescription());

            db.close();
        }
    }

    @OnClick(R.id.btn_save)
    public void saveHabit(View v) {
        Database db = new Database(HabitActivity.this);
        db.open();

        // validate user input
        Validator validator = new Validator();
        validator.setErrorMsgListener(new Validator.ErrorMsg() {
            @Override
            public void showError(ValidatorType type, String key, String cond) {
                switch (type) {
                    case EMPTY:
                        Toast.makeText(HabitActivity.this, key + " không được trống", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        String savedUserId = MySharedPreference.getUserId(this);
        String habitName = editHabitName.getText().toString();
        String monitorNumber;
        if (monitorType == 1) {
            monitorNumber = this.editCheckNumber.getText().toString();
        } else {
            monitorNumber = TYPE_1;
        }

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
        final Habit habit = new Habit();
        if (screenMode == MODE_CREATE) {
            habit.setHabitId(AppGenerator.getNewId());
            habit.setMonitorId(AppGenerator.getNewId());
        } else if (screenMode == MODE_UPDATE) {
            habit.setHabitId(initHabitId);
            habit.setMonitorId(savedMonitorDateId);
        }
        habit.setUserId(savedUserId);
        habit.setGroupId(selectedGroupId);

        habit.setHabitName(habitName);
        habit.setHabitTarget(String.valueOf(habitTarget));
        habit.setHabitType(String.valueOf(habitType));
        habit.setMonitorType(String.valueOf(monitorType));
        habit.setMonitorUnit(monitorUnit);
        habit.setMonitorNumber(monitorNumber);

        habit.setStartDate(enableHabitRangeTime[0]? startHabitDate: AppGenerator.getCurrentDate(AppGenerator.YMD_SHORT));
        habit.setEndDate(enableHabitRangeTime[1]? endHabitDate: null);
        habit.setCreatedDate(AppGenerator.getCurrentDate(AppGenerator.YMD_SHORT));

        habit.setHabitColor(habitColorCode);
        habit.setDescription(editDescription.getText().toString());

        // monitor_date
        habit.setMon(String.valueOf(monitorDate[0] ? 1 : 0));
        habit.setTue(String.valueOf(monitorDate[1] ? 1 : 0));
        habit.setWed(String.valueOf(monitorDate[2] ? 1 : 0));
        habit.setThu(String.valueOf(monitorDate[3] ? 1 : 0));
        habit.setFri(String.valueOf(monitorDate[4] ? 1 : 0));
        habit.setSat(String.valueOf(monitorDate[5] ? 1 : 0));
        habit.setSun(String.valueOf(monitorDate[6] ? 1 : 0));

        // suggestions
        habit.setHabitNameAscii(AppGenerator.getSearchKey(habit.getHabitName()));
        // user create new Habit Name
        if (TextUtils.isEmpty(suggestHabitName) || !suggestHabitName.equals(habit.getHabitName())) {
            habit.setHabitNameId(AppGenerator.getNewId());
            habit.setGroupId(habit.getGroupId());
        } else {
            // user chose a habit name in the suggestion list
            habit.setHabitNameId(suggestHabitNameId);
        }

        // set reminder list
        for (Reminder reminder : reminderUpdateList) {
            reminder.setHabitId(habit.getHabitId());
            reminder.setHabitName(habit.getHabitName());
            reminder.setRemindEndTime(habit.getEndDate());
        }
        habit.setReminderList(reminderUpdateList);

        // save or update habit
        if (screenMode == MODE_UPDATE) {
            habit.setUpdate(true);
        }
        if (Database.getHabitDb().saveUpdateHabit(habit.toEntity())) {
            String reminderId;
            for (Reminder reminder : habit.getReminderList()) {

                if (reminder.isDelete()) {
                    Database.getReminderDb().delete(reminder.getReminderId());
                } else {
                    reminderId = Database.getReminderDb().saveReminder(reminder.toEntity());
                    reminder.setReminderId(reminderId);
                }
            }
        }

        db.close();

        // start remind server to put notification
        HabitReminderManager habitReminderManager = new HabitReminderManager(this, habit.getReminderList());
        habitReminderManager.start();

        setResult(RESULT_OK);
        finish();

        // call api
        if (screenMode == MODE_CREATE) {
            mApiService.addHabit(habit).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Toast.makeText(HabitActivity.this, "Tạo thói quen thành công", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(HabitActivity.this, "Đã xãy ra lỗi", Toast.LENGTH_LONG).show();
                }
            });

        } else if (screenMode == MODE_UPDATE) {
            mApiService.updateHabit(habit).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Toast.makeText(HabitActivity.this, "Cập nhật thành công", Toast.LENGTH_LONG).show();

                    Database db = Database.getInstance(HabitActivity.this);
                    db.open();

                    Database.getHabitDb().setUpdate(habit.getHabitId(), false);
                    db.close();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(HabitActivity.this, "Đã xãy ra lỗi khi kết nối server ", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @OnClick(R.id.btn_suggestHabit)
    public void showSuggestHabitByGroup(View v) {
        Intent intent = new Intent(this, SuggestionByGroupActivity.class);
        startActivityForResult(intent, GET_SUGGEST);
    }

    @OnClick({R.id.btn_back})
    public void back(View v) {
        finish();
    }

    @OnClick({R.id.btn_cancel})
    public void cancel(View v) {
        if (screenMode == MODE_CREATE) {
            finish();
        } else if (screenMode == MODE_UPDATE) {

            AppDialogHelper appDialogHelper = new AppDialogHelper();
            appDialogHelper.setPositiveListener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Database db = new Database(HabitActivity.this);
                    db.open();

                    HabitEntity item = Database.getHabitDb().getHabit(initHabitId);
                    item.setDelete(true);
                    Database.getHabitDb().saveUpdateHabit(item);
                    db.close();

                    Intent intent = new Intent();
                    intent.putExtra("delete", true);
                    setResult(RESULT_OK, intent);
                    finish();

                    mApiService.deleteHabit(initHabitId).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Toast.makeText(HabitActivity.this, "Đã xóa thói quen", Toast.LENGTH_SHORT).show();

                            Database db = new Database(HabitActivity.this);
                            db.open();

                            Database.getHabitDb().delete(initHabitId);
                            db.close();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(HabitActivity.this, "Đã xãy ra lỗi", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
            appDialogHelper.getDialog(this, "Bạn có chắc muốn xóa thói quen này?", "Có", "Không").show();
        }
    }
    

    @OnClick(R.id.btn_addReminder)
    public void addReminder(View v) {
        Intent intent = new Intent(this, ReminderCreateActivity.class);
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
    public void selectHabitType(View view) {
        setWhiteBg(btnHabitType);
        setGreenBg(view);
        btnHabitType = view;
        habitType = Integer.parseInt(view.getTag().toString());
        switch (view.getId()) {
            case R.id.btn_TypeDaily:
                endHabitDate = AppGenerator.getNextDate(startHabitDate, AppGenerator.YMD_SHORT);
                tvCountUnit.setText("một ngày");
                break;
            case R.id.btn_TypeWeekly:

                endHabitDate = AppGenerator.getFirstDateNextWeek(startHabitDate, AppGenerator.YMD_SHORT, AppGenerator.YMD_SHORT);
                tvCountUnit.setText("một tuần");
                break;
            case R.id.btn_TypeMonthly:
                endHabitDate = AppGenerator.getFirstDateNextMonth(startHabitDate, AppGenerator.YMD_SHORT, AppGenerator.YMD_SHORT);
                tvCountUnit.setText("một tháng");
                break;
            case R.id.btn_TypeYearly:
                endHabitDate = AppGenerator.getFirstDateNextYear(startHabitDate, AppGenerator.YMD_SHORT, AppGenerator.YMD_SHORT);
                tvCountUnit.setText("một năm");
                break;
        }
        tvEndDate.setText(AppGenerator.format(endHabitDate, AppGenerator.YMD_SHORT, AppGenerator.DMY_SHORT));
    }

    @OnClick({R.id.ll_checkDone, R.id.ll_checkCount})
    public void enableMonitorType(View view) {
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
    public void showDatePickerDialog(View v) {
        Calendar ca = Calendar.getInstance();
        DatePickerDialog dialog;
        onSetStartDate = v.getId() == R.id.edit_startDate;

        if (v.getId() == R.id.edit_startDate) {
            ca.setTime(AppGenerator.getDate(startHabitDate, AppGenerator.YMD_SHORT));
            dialog = new DatePickerDialog(this, this, ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DATE));
            dialog.getDatePicker().setMinDate(System.currentTimeMillis());
            dialog.show();

        } else {
            String minDate = getEndDateByMonitorType();
            ca.setTime(AppGenerator.getDate(endHabitDate, AppGenerator.YMD_SHORT));

            dialog = new DatePickerDialog(this, this, ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DATE));

            ca.setTime(AppGenerator.getDate(minDate, AppGenerator.YMD_SHORT));
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
    public void enableHabitDateRange(View v) {
        if (v.getId() == R.id.ll_start_date) {
            if (enableHabitRangeTime[0]) {
                uncheckBox(chkStartDate);
            } else {
                checkBox(chkStartDate);
            }
            enableHabitRangeTime[0] = !enableHabitRangeTime[0];
            tvStartDate.setEnabled(enableHabitRangeTime[0]);

            if (!enableHabitRangeTime[0]) {
                startHabitDate = AppGenerator.getCurrentDate(AppGenerator.YMD_SHORT);
                tvStartDate.setText(AppGenerator.format(startHabitDate, AppGenerator.YMD_SHORT, AppGenerator.DMY_SHORT));
            }

        } else if (v.getId() == R.id.ll_end_date) {
            if (enableHabitRangeTime[1]) {
                uncheckBox(chkEndDate);
            } else {
                checkBox(chkEndDate);
            }

            enableHabitRangeTime[1] = !enableHabitRangeTime[1];
            tvEndDate.setEnabled(enableHabitRangeTime[1]);

            if (!enableHabitRangeTime[1]) {
                endHabitDate = getEndDateByMonitorType();
                tvEndDate.setText(AppGenerator.format(endHabitDate, AppGenerator.YMD_SHORT, AppGenerator.DMY_SHORT));
            }
        }
    }

    // date picker set date method
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        if (onSetStartDate) {
            startHabitDate = AppGenerator.getDate(year, month + 1, day, AppGenerator.YMD_SHORT);
            tvStartDate.setText(AppGenerator.format(startHabitDate, AppGenerator.YMD_SHORT, AppGenerator.DMY_SHORT));

            String minEndDate = getEndDateByMonitorType();
            if (endHabitDate.compareTo(minEndDate) < 0) {
                endHabitDate = minEndDate;
                tvEndDate.setText(AppGenerator.format(endHabitDate, AppGenerator.YMD_SHORT, AppGenerator.DMY_SHORT));
            }

        } else {
            endHabitDate = AppGenerator.getDate(year, month + 1, day, AppGenerator.YMD_SHORT);
            tvEndDate.setText(AppGenerator.format(endHabitDate, AppGenerator.YMD_SHORT, AppGenerator.DMY_SHORT));
        }
    }

    private String getEndDateByMonitorType () {
        switch (habitType) {
            case 0:
                return AppGenerator.getNextDate(startHabitDate, AppGenerator.YMD_SHORT);
            case 1:

                return AppGenerator.getFirstDateNextWeek(startHabitDate, AppGenerator.YMD_SHORT, AppGenerator.YMD_SHORT);
            case 2:
                return AppGenerator.getFirstDateNextMonth(startHabitDate, AppGenerator.YMD_SHORT, AppGenerator.YMD_SHORT);
            case 3:
                return AppGenerator.getFirstDateNextYear(startHabitDate, AppGenerator.YMD_SHORT, AppGenerator.YMD_SHORT);
        }
        return endHabitDate;
    }

    @OnClick(R.id.ll_GroupName)
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
        int idx;
        if (habitColor != null) {
            idx = Integer.parseInt(habitColor.getTag().toString());
            unPickColor(habitColor, colorsList.get(idx));
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
        img.setImageResource(R.drawable.rd_checked);
    }

    public void uncheck(ImageView img) {
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

    public void unPickColor(View v, String colorCode) {
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = Math.round(ev.getX());
        int y = Math.round(ev.getY());

        if (x < rvHabitSuggestion.getLeft() || x > rvHabitSuggestion.getRight()
                || y < rvHabitSuggestion.getTop() || y > (rvHabitSuggestion.getBottom() + header.getBottom() )) {
            searchResultList.clear();
            habitSuggestionAdapter.notifyDataSetChanged();
        }

        return super.dispatchTouchEvent(ev);
    }
}
