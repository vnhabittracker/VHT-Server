package habit.tracker.habittracker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
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
import habit.tracker.habittracker.api.VnHabitApiUtils;
import habit.tracker.habittracker.api.model.feedback.Feedback;
import habit.tracker.habittracker.api.model.reminder.Reminder;
import habit.tracker.habittracker.api.service.VnHabitApiService;
import habit.tracker.habittracker.common.AppConstant;
import habit.tracker.habittracker.common.dialog.AppDialogHelper;
import habit.tracker.habittracker.common.habitreminder.HabitReminderManager;
import habit.tracker.habittracker.common.util.AppGenerator;
import habit.tracker.habittracker.common.util.MySharedPreference;
import habit.tracker.habittracker.repository.Database;
import habit.tracker.habittracker.repository.feedback.FeedbackEntity;
import habit.tracker.habittracker.repository.reminder.ReminderEntity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("ResourceType")
public class SettingActivity extends BaseActivity {
    private static final int ADD_USER_REMINDER = 0;
    private static final int SELECT_REMINDER_RINHTONE = 1;

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
    @BindView(R.id.lbSound)
    TextView lbSound;

    int starNumber = 5;

    VnHabitApiService mService = VnHabitApiUtils.getApiService();
    Database mDb = Database.getInstance(this);
    List<Reminder> reminderDisplayList = new ArrayList<>();
    RemindRecyclerViewAdapter reminderAdapter;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_USER_REMINDER && resultCode == RESULT_OK && data != null) {
            mDb.open();

            boolean isDelete = data.getBooleanExtra(ReminderCreateActivity.IS_DELETE_REMINDER, false);
            int pos = data.getIntExtra(ReminderCreateActivity.POSITION_IN_LIST, -1);

            String reminderId = data.getStringExtra(ReminderCreateActivity.REMINDER_ID);
            String remindType = String.valueOf(data.getIntExtra(ReminderCreateActivity.REMIND_TYPE, -1));
            String remindText = data.getStringExtra(ReminderCreateActivity.REMIND_TEXT);
            String hour = String.format(AppConstant.format2D, data.getIntExtra(ReminderCreateActivity.REMIND_HOUR, 0));
            String minute = String.format(AppConstant.format2D, data.getIntExtra(ReminderCreateActivity.REMIND_MINUTE, 0));
            String date = data.getStringExtra(ReminderCreateActivity.REMIND_DATE);
            String time = date + " " + hour + ":" + minute;

            List<Reminder> updateList = new ArrayList<>();

            Reminder reminder;
            // add new reminder
            if (TextUtils.isEmpty(reminderId)) {
                reminder = new Reminder();
                reminder.setUserId(MySharedPreference.getUserId(this));
                reminder.setServerId(AppGenerator.getNewId());
                reminder.setRepeatType(remindType);
                reminder.setRemindText(remindText);
                reminder.setRemindStartTime(time);
                reminder.setReminderId(Database.getReminderDb().saveReminder(reminder.toEntity()));
                reminderDisplayList.add(reminder);

            } else {
                // update ot delete
                reminder = reminderDisplayList.get(pos);
                reminder.setRemindText(remindText);
                reminder.setRepeatType(remindType);
                reminder.setRemindStartTime(date + " " + hour + ":" + minute);
                if (isDelete) {
                    reminderDisplayList.remove(pos);
                    reminder.setDelete(true);

                } else {
                    reminder.setUpdate(true);
                }
            }

            reminderAdapter.notifyDataSetChanged();

            updateList.add(reminder);
            HabitReminderManager habitReminderManager = new HabitReminderManager(SettingActivity.this, updateList);
            habitReminderManager.start();

            callUpdateReminderApi(reminder);

        } else if (resultCode == RESULT_OK && requestCode == SELECT_REMINDER_RINHTONE) {
            Uri uri;
            if (data != null) {
                uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                if (uri != null) {
                    String userId = MySharedPreference.getUserId(this);
                    MySharedPreference.save(this, userId + "_sound", uri.toString());
                }
            }
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
        mDb.open();
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
    }

    @OnClick(R.id.btnBack)
    public void back(View v) {
        finish();
    }

    @OnClick(R.id.tvLogout)
    public void logout(View v) {
        AppDialogHelper appDialogHelper = new AppDialogHelper();
        appDialogHelper.setPositiveListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                signOutSocialLogin();
                MySharedPreference.saveUser(SettingActivity.this, null, null, null);
                Intent intent = getIntent();
                intent.putExtra("logoutSocialLogin", true);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        appDialogHelper.getDialog(this, "Bạn có chắc muốn thoát?", "Có", "Không").show();
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
        if (isStoragePermissionGranted()) {
            exportDb2Ex();
        }
    }

    private void exportDb2Ex() {
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/Backup/";

        File file = new File(directory_path);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                return;
            }
        }

        // Export SQLite DB as EXCEL FILE
        SQLiteToExcel sqliteToExcel = new SQLiteToExcel(getApplicationContext(), Database.DATABASE_NAME, directory_path);
        sqliteToExcel.exportSingleTable("my_user", "users.csv", new SQLiteToExcel.ExportListener() {
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

    @OnClick(R.id.lbFeedback)
    public void sendFeedback(View v) {
        mDb.open();

        final FeedbackEntity feedbackEntity = Database.getFeedbackDb().getFeedbackByUser(MySharedPreference.getUserId(this));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View inflatedView = inflater.inflate(R.layout.dialog_edit_feedback, null);

        final EditText edFeedback = inflatedView.findViewById(R.id.editFeedback);

        ImageView imgStart1 = inflatedView.findViewById(R.id.star1);
        ImageView imgStart2 = inflatedView.findViewById(R.id.star2);
        ImageView imgStart3 = inflatedView.findViewById(R.id.star3);
        ImageView imgStart4 = inflatedView.findViewById(R.id.star4);
        ImageView imgStart5 = inflatedView.findViewById(R.id.star5);
        final ImageView[] starArray = {imgStart1, imgStart2, imgStart3, imgStart4, imgStart5};


        View.OnClickListener startClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag() != null) {
                    starNumber = Integer.parseInt(v.getTag().toString());
                    updateRatingUI(starArray, starNumber);
                }
            }
        };

        imgStart1.setOnClickListener(startClick);
        imgStart2.setOnClickListener(startClick);
        imgStart3.setOnClickListener(startClick);
        imgStart4.setOnClickListener(startClick);
        imgStart5.setOnClickListener(startClick);

        TextView title = new TextView(this);
        title.setText("Đánh giá ứng dụng");
        title.setGravity(Gravity.CENTER);
        title.setPadding(25, 20, 0, 10);
        title.setTextSize(14);
        builder.setCustomTitle(title);

        builder.setView(inflatedView)
                .setPositiveButton("Gửi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mDb.open();

                        final Feedback fb = new Feedback();
                        fb.setFeedbackId(MySharedPreference.getUserId(SettingActivity.this));
                        fb.setUserId(MySharedPreference.getUserId(SettingActivity.this));
                        fb.setStarNum(starNumber);
                        fb.setDescription(edFeedback.getText().toString().trim());

                        Database.getFeedbackDb().saveFeedback(fb.toEntity(true));

                        mService.sendFeedback(fb).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                mDb.open();
                                Database.getFeedbackDb().saveFeedback(fb.toEntity(false));
                                Toast.makeText(SettingActivity.this, "Gửi phản hồi thành công", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(SettingActivity.this, "Gửi phản hồi không thành công", Toast.LENGTH_SHORT).show();
                            }
                        });

                        dialog.cancel();
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                if (feedbackEntity != null) {
                    edFeedback.setText(feedbackEntity.getDescription());
                    starNumber = feedbackEntity.getStarNum();
                    updateRatingUI(starArray, starNumber);
                }

                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor(SettingActivity.this.getString(R.color.colorAccent)));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor(SettingActivity.this.getString(R.color.colorAccent)));
            }
        });
        alertDialog.show();
    }

    @OnClick(R.id.lbSound)
    public void selectNotificationSound(View v) {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Chọn âm báo");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
        startActivityForResult(intent, SELECT_REMINDER_RINHTONE);
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            exportDb2Ex();
        }
    }

    private void updateRatingUI(ImageView[] starArray, int num) {
        for (int i = 0; i < num; i++) {
            setGoldStar(starArray[i]);
        }
        for (int i = num; i < 5; i++) {
            unsetGoldStar(starArray[i]);
        }
    }

    private void callUpdateReminderApi(final Reminder reminder) {
        mService.addUpdateReminder(reminder).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (reminder.isDelete()) {
                    Database.getReminderDb().delete(reminder.getReminderId());
                } else if (reminder.isUpdate()) {
                    reminder.setUpdate(false);
                    Database.getReminderDb().saveReminder(reminder.toEntity());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    private void setGoldStar(ImageView img) {
        img.setImageResource(R.drawable.ic_star_yellow);
        img.setAlpha(1f);
    }

    private void unsetGoldStar(ImageView img) {
        img.setImageResource(R.drawable.ic_star_grey);
        img.setAlpha(0.2f);
    }

    @Override
    protected void onStop() {
        mDb.close();
        super.onStop();
    }
}
