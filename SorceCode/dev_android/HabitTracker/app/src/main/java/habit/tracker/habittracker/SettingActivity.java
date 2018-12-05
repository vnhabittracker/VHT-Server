package habit.tracker.habittracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ajts.androidmads.library.SQLiteToExcel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import habit.tracker.habittracker.adapter.RecyclerViewItemClickListener;
import habit.tracker.habittracker.adapter.RemindRecyclerViewAdapter;
import habit.tracker.habittracker.api.model.reminder.Reminder;
import habit.tracker.habittracker.common.habitreminder.HabitReminderManager;
import habit.tracker.habittracker.common.util.AppGenerator;
import habit.tracker.habittracker.common.util.MySharedPreference;
import habit.tracker.habittracker.repository.Database;
import habit.tracker.habittracker.repository.reminder.ReminderEntity;

public class SettingActivity extends AppCompatActivity {
    private static final int ADD_USER_REMINDER = 0;

    @BindView(R.id.lbPersonal)
    TextView lbPersonal;
    @BindView(R.id.tvLogout)
    TextView tvLogout;

    @BindView(R.id.tvReminder)
    TextView tvReminder;
    @BindView(R.id.rvRemind)
    RecyclerView rvRemind;

    @BindView(R.id.lbExport)
    TextView lbExport;

    List<Reminder> reminderDisplayList = new ArrayList<>();
    RemindRecyclerViewAdapter reminderAdapter;

    @Override
    @SuppressLint("DefaultLocale")
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_USER_REMINDER && resultCode == RESULT_OK && data != null) {
            Database db = Database.getInstance(this);
            db.open();

            String format = "%02d";

            boolean isDelete = data.getBooleanExtra(ReminderCreateActivity.IS_DELETE_REMINDER, false);
            int pos = data.getIntExtra(ReminderCreateActivity.POSITION_IN_LIST, -1);
            String reminderId = data.getStringExtra(ReminderCreateActivity.REMINDER_ID);
            String remindType = String.valueOf(data.getIntExtra(ReminderCreateActivity.REMIND_TYPE, -1));
            String remindText = data.getStringExtra(ReminderCreateActivity.REMIND_TEXT);
            String hour = String.format(format, data.getIntExtra(ReminderCreateActivity.REMIND_HOUR, 0));
            String minute = String.format(format, data.getIntExtra(ReminderCreateActivity.REMIND_MINUTE, 0));
            String date = data.getStringExtra(ReminderCreateActivity.REMIND_DATE);
            String time = date + " " + hour + ":" + minute;

            List<Reminder> updateList = new ArrayList<>();
            Reminder reminder;
            if (TextUtils.isEmpty(reminderId)) {
                reminder = new Reminder();
                reminder.setUserId(MySharedPreference.getUserId(this));
                reminder.setServerId(AppGenerator.getNewId());
                reminder.setRepeatType(remindType);
                reminder.setRemindText(remindText);
                reminder.setRemindStartTime(time);
                reminder.setReminderId(Database.getReminderDb().saveReminder(Database.getReminderDb().convert(reminder)));
                reminderDisplayList.add(reminder);

            } else {
                // update
                reminder = reminderDisplayList.get(pos);
                reminder.setRemindText(remindText);
                reminder.setRepeatType(remindType);
                reminder.setRemindStartTime(date + " " + hour + ":" + minute);
                if (isDelete) {
                    reminder.setDelete(true);

                    reminderDisplayList.remove(pos);
                    Database.getReminderDb().delete(reminderId);
                } else {
                    Database.getReminderDb().saveReminder(Database.getReminderDb().convert(reminder));
                }
            }
            reminderAdapter.notifyDataSetChanged();
            updateList.add(reminder);
            HabitReminderManager habitReminderManager = new HabitReminderManager(SettingActivity.this, updateList);
            habitReminderManager.start();

            db.close();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        populateRecyclerView();
    }

    private void populateRecyclerView() {
        Database db = Database.getInstance(this);
        db.open();

        List<ReminderEntity> entityList = Database.getReminderDb().getReminderByUser(MySharedPreference.getUserId(this));
        Reminder reminder;
        for (ReminderEntity entity : entityList) {
            reminder = new Reminder();
            reminder.setReminderId(entity.getReminderId());
            reminder.setUserId(entity.getUserId());
            reminder.setRemindStartTime(entity.getReminderStartTime());
            reminder.setRemindText(entity.getRemindText());
            reminderDisplayList.add(reminder);
        }

        reminderAdapter = new RemindRecyclerViewAdapter(this, reminderDisplayList);
        reminderAdapter.setListener(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(SettingActivity.this, ReminderCreateActivity.class);
                intent.putExtra(ReminderCreateActivity.REMINDER_ID, reminderDisplayList.get(position).getReminderId());
                intent.putExtra(ReminderCreateActivity.POSITION_IN_LIST, position);
                startActivityForResult(intent, ADD_USER_REMINDER);
            }
        });
        rvRemind.setLayoutManager(new LinearLayoutManager(this));
        rvRemind.setAdapter(reminderAdapter);

        db.close();
    }

    @OnClick(R.id.btnBack)
    public void back(View v) {
        finish();
    }

    @OnClick(R.id.tvLogout)
    public void logout(View v) {
        MySharedPreference.saveUser(this, null, null, null);
        Intent intent = getIntent();
        intent.putExtra("logout", true);
        setResult(RESULT_OK, intent);
        finish();
    }

    @OnClick(R.id.lbPersonal)
    public void editPersonalInfo(View v) {
        Intent intent = new Intent(this, PersonalActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tvReminder)
    public void addReminder(View v) {
        Intent intent = new Intent(this, ReminderCreateActivity.class);
        startActivityForResult(intent, ADD_USER_REMINDER);
    }

    @OnClick(R.id.lbExport)
    public void exportData(View v) {
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/Backup/";

        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }

        // Export SQLite DB as EXCEL FILE
        SQLiteToExcel sqliteToExcel = new SQLiteToExcel(getApplicationContext(), Database.DATABASE_NAME, directory_path);
        sqliteToExcel.exportSingleTable("my_user","users.csv", new SQLiteToExcel.ExportListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onCompleted(String filePath) {
                Toast.makeText(SettingActivity.this, "Đã export ra " + filePath, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(SettingActivity.this, "Export không thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }
}