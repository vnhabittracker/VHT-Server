package habit.tracker.habittracker;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import habit.tracker.habittracker.adapter.RecyclerViewItemClickListener;
import habit.tracker.habittracker.adapter.note.NoteItem;
import habit.tracker.habittracker.adapter.note.NoteRecyclerViewAdapter;
import habit.tracker.habittracker.api.VnHabitApiUtils;
import habit.tracker.habittracker.api.model.tracking.Tracking;
import habit.tracker.habittracker.api.model.tracking.TrackingList;
import habit.tracker.habittracker.api.service.VnHabitApiService;
import habit.tracker.habittracker.common.util.AppGenerator;
import habit.tracker.habittracker.repository.Database;
import habit.tracker.habittracker.repository.habit.HabitEntity;
import habit.tracker.habittracker.repository.tracking.TrackingEntity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoteActivity extends AppCompatActivity implements RecyclerViewItemClickListener {

    @BindView(R.id.llHeader)
    View llHeader;
    @BindView(R.id.rvNote)
    RecyclerView rvNote;
    @BindView(R.id.imgAddNote)
    ImageView imgAddNote;

    @BindView(R.id.tvCurrentTime)
    TextView tvCurrentTime;
    @BindView(R.id.tvTrackCount)
    TextView tvTrackCount;
    @BindView(R.id.pre)
    View btnPreDate;
    @BindView(R.id.next)
    View btnNextDate;
    @BindView(R.id.minusCount)
    View imgMinusCount;
    @BindView(R.id.addCount)
    View imgAddCount;

    String habitId;
    HabitEntity habitEntity;
    String currentDate;
    NoteRecyclerViewAdapter noteRecyclerViewAdapter;
    List<NoteItem> displayItemList = new ArrayList<>();
    List<TrackingEntity> trackingEntityList;

    boolean isEdit = true;
    int curTrackingCount = 0;
    private int timeLine = 0;
    private boolean[] availDaysInWeek = new boolean[7];

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_note);
        ButterKnife.bind(this);

        Database db = Database.getInstance(this);
        db.open();

        currentDate = AppGenerator.getCurrentDate(AppGenerator.YMD_SHORT);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            habitId = bundle.getString(MainActivity.HABIT_ID);
        }

        habitEntity = Database.getHabitDb().getHabit(habitId);
        trackingEntityList = Database.getTrackingDb().getTrackingListByHabit(habitId);
        TrackingEntity todayTracking = Database.getTrackingDb().getTracking(habitEntity.getHabitId(), currentDate);

        availDaysInWeek[0] = habitEntity.getMon().equals("1");
        availDaysInWeek[1] = habitEntity.getTue().equals("1");
        availDaysInWeek[2] = habitEntity.getWed().equals("1");
        availDaysInWeek[3] = habitEntity.getThu().equals("1");
        availDaysInWeek[4] = habitEntity.getFri().equals("1");
        availDaysInWeek[5] = habitEntity.getSat().equals("1");
        availDaysInWeek[6] = habitEntity.getSun().equals("1");

        curTrackingCount = Integer.parseInt(todayTracking.getCount());

        for (TrackingEntity entity : trackingEntityList) {
            if (!TextUtils.isEmpty(entity.getDescription())) {
                displayItemList.add(new NoteItem(entity.getTrackingId(),
                        entity.getCurrentDate(),
                        AppGenerator.format(entity.getCurrentDate(), AppGenerator.YMD_SHORT, AppGenerator.DMY_SHORT),
                        entity.getDescription()));
            }
        }

        noteRecyclerViewAdapter = new NoteRecyclerViewAdapter(this, displayItemList, this);
        rvNote.setLayoutManager(new LinearLayoutManager(this));
        rvNote.setAdapter(noteRecyclerViewAdapter);

        llHeader.setBackgroundColor(Color.parseColor(habitEntity.getHabitColor()));

        updateUI();

        db.close();
    }

    @OnClick(R.id.imgAddNote)
    public void onAddNoteClick(View v) {
        isEdit = false;
        TrackingEntity trackingEntity = getTodayTracking(habitId, currentDate, 0);
        int index = 0;
        for (NoteItem item : displayItemList) {
            if (item.getTrackingId().equals(trackingEntity.getTrackingId())) {
                isEdit = true;
                break;
            }
            index++;
        }
        String btnNegative;
        if (isEdit) {
            btnNegative = "Xóa";
        } else {
            btnNegative = "Hủy";
        }
        showEditDialog("Lưu", btnNegative,
                AppGenerator.format(trackingEntity.getCurrentDate(), AppGenerator.YMD_SHORT, AppGenerator.DMY_SHORT), trackingEntity.getDescription(), index);
    }

    @Override
    public void onItemClick(View view, final int position) {
        if (view.getId() == R.id.itemNote) {
            isEdit = true;
            final NoteItem noteItem = displayItemList.get(position);
            showEditDialog("Lưu", "Xóa", noteItem.getDate(), noteItem.getNote(), position);
        }
    }

    private void showEditDialog(String positiveButton, String negativeButton, String head, final String note, final int adapterPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View inflatedView = inflater.inflate(R.layout.dialog_edit_note, null);
        final EditText editNote = inflatedView.findViewById(R.id.editNote);

        TextView title = new TextView(this);
        title.setText(head);
        title.setGravity(Gravity.CENTER);
        title.setPadding(25,20, 0, 10);
        title.setTextSize(14);
        builder.setCustomTitle(title);

        builder.setView(inflatedView)
                .setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String newNote = editNote.getText().toString().trim();
                        if (isEdit) {
                            updateNote(newNote, adapterPosition);
                        } else {
                            addNewNote(newNote);
                        }
                        dialog.cancel();
                    }
                })
                .setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (isEdit) {
                            deleteNote(adapterPosition);
                        }
                        dialog.cancel();
                    }
                });
        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onShow(DialogInterface dialog) {
                editNote.setText(note);
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor(NoteActivity.this.getString(R.color.colorAccent)));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor(NoteActivity.this.getString(R.color.colorAccent)));
            }
        });
        alertDialog.show();
    }

    private void updateNote(String newNote, int position) {
        NoteItem noteItem = displayItemList.get(position);
        if (TextUtils.isEmpty(newNote)) {
            displayItemList.remove(position);
//            if (noteItem.getDate().equals(currentDate)) {
//                imgAddNote.setVisibility(View.VISIBLE);
//            }
        } else {
            displayItemList.get(position).setNote(newNote);
        }
        noteRecyclerViewAdapter.notifyDataSetChanged();
        updateData(newNote, noteItem.getTrackingId());
    }

    private void deleteNote(int position) {
        NoteItem noteItem = displayItemList.get(position);
        updateData(null, noteItem.getTrackingId());
//        if (noteItem.getDefDate().equals(currentDate)) {
//            imgAddNote.setVisibility(View.VISIBLE);
//        }
        displayItemList.remove(position);
        noteRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addNewNote(String newNote) {
////        imgAddNote.setVisibility(View.INVISIBLE);
        updateData(newNote, AppGenerator.getNewId());
    }

    private void updateData(String newNote, String trackId) {
        Database db = Database.getInstance(this);
        db.open();
        TrackingEntity trackingEntity = null;
        for (TrackingEntity entity : trackingEntityList) {
            if (entity.getTrackingId().equals(trackId)) {
                trackingEntity = entity;
                trackingEntity.setDescription(newNote);
                break;
            }
        }
        if (trackingEntity == null) {
            trackingEntity = new TrackingEntity();
            trackingEntity.setTrackingId(trackId);
            trackingEntity.setHabitId(habitId);
            trackingEntity.setCount("0");
            trackingEntity.setCurrentDate(currentDate);
            trackingEntity.setDescription(newNote);
            trackingEntityList.add(trackingEntity);
            displayItemList.add(
                    new NoteItem(trackingEntity.getTrackingId(),
                            trackingEntity.getCurrentDate(),
                            AppGenerator.format(trackingEntity.getCurrentDate(), AppGenerator.YMD_SHORT, AppGenerator.DMY_SHORT),
                            newNote));
            noteRecyclerViewAdapter.notifyDataSetChanged();
        }
        Database.getTrackingDb().saveTracking(trackingEntity);

        TrackingList trackingData = new TrackingList();
        Tracking tracking = new Tracking();
        tracking.setTrackingId(trackingEntity.getTrackingId());
        tracking.setHabitId(trackingEntity.getHabitId());
        tracking.setCount(String.valueOf(trackingEntity.getCount()));
        tracking.setCurrentDate(trackingEntity.getCurrentDate());
        tracking.setDescription(trackingEntity.getDescription());
        trackingData.getTrackingList().add(tracking);

        VnHabitApiService service = VnHabitApiUtils.getApiService();
        service.updateTracking(trackingData).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
        db.close();
    }

    @OnClick({R.id.minusCount, R.id.addCount})
    public void onTrackingCountChanged(View v) {
        if (timeLine > 0 || currentDate.compareTo(habitEntity.getStartDate()) < 0
                || !AppGenerator.isValidTrackingDay(currentDate, availDaysInWeek)) {
            return;
        }

        switch (v.getId()) {
            case R.id.minusCount:
                curTrackingCount = curTrackingCount - 1 < 0 ? 0 : curTrackingCount - 1;
                break;
            case R.id.addCount:
                curTrackingCount++;
                break;
        }

        // save to appDatabase
        Database db = Database.getInstance(this);
        db.open();
        TrackingEntity trackingEntity = Database.getTrackingDb().getTracking(habitEntity.getHabitId(), currentDate);
        if (trackingEntity == null) {
            trackingEntity = new TrackingEntity();
            trackingEntity.setTrackingId(AppGenerator.getNewId());
            trackingEntity.setHabitId(habitEntity.getHabitId());
            trackingEntity.setCurrentDate(currentDate);
        }
        trackingEntity.setCount(String.valueOf(curTrackingCount));
        Database.trackingImpl.saveTracking(trackingEntity);
        db.close();

        updateUI();

        TrackingList trackingData = new TrackingList();
        Tracking tracking = new Tracking();
        tracking.setTrackingId(trackingEntity.getTrackingId());
        tracking.setHabitId(trackingEntity.getHabitId());
        tracking.setCurrentDate(currentDate);
        tracking.setCount(String.valueOf(trackingEntity.getCount()));
        tracking.setDescription(trackingEntity.getDescription());
        trackingData.getTrackingList().add(tracking);

        VnHabitApiService service = VnHabitApiUtils.getApiService();
        service.updateTracking(trackingData).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    @OnClick({R.id.pre, R.id.next})
    public void onDateChanged(View v) {
        switch (v.getId()) {
            case R.id.pre:
                timeLine--;
                currentDate = AppGenerator.getPreDate(currentDate, AppGenerator.YMD_SHORT);
                break;
            case R.id.next:
                timeLine++;
                currentDate = AppGenerator.getNextDate(currentDate, AppGenerator.YMD_SHORT);
                break;
        }

        Database db = Database.getInstance(this);
        db.open();
        TrackingEntity todayTracking = Database.getTrackingDb().getTracking(habitEntity.getHabitId(), currentDate);
        db.close();

        curTrackingCount = 0;
        if (todayTracking != null) {
            curTrackingCount = Integer.parseInt(todayTracking.getCount());
        }

        updateUI();
    }

    private void updateUI() {
        if (timeLine == 0) {
            tvCurrentTime.setText("Hôm nay");
        } else if (timeLine == -1) {
            tvCurrentTime.setText("Hôm qua");
        } else if (timeLine == 1) {
            tvCurrentTime.setText("Ngày mai");
        } else {
            tvCurrentTime.setText(
                    AppGenerator.format(currentDate, AppGenerator.YMD_SHORT, AppGenerator.DMY_SHORT));
        }

        if (timeLine > 0 || currentDate.compareTo(habitEntity.getStartDate()) < 0
                || !AppGenerator.isValidTrackingDay(currentDate, availDaysInWeek)) {
            tvTrackCount.setText("--");
            if (imgAddNote.getVisibility() != View.VISIBLE ) {
                imgAddNote.setVisibility(View.INVISIBLE);
            }
        } else {
            tvTrackCount.setText(String.valueOf(curTrackingCount) + " " + habitEntity.getMonitorUnit());
            if (imgAddNote.getVisibility() != View.INVISIBLE ) {
                imgAddNote.setVisibility(View.VISIBLE);
            }
        }
    }

    public TrackingEntity getTodayTracking(String habitId, String currentDate, int defaultVal) {
        Database db = Database.getInstance(this);
        db.open();
        TrackingEntity todayTracking = Database.getTrackingDb().getTracking(habitId, currentDate);
        if (todayTracking == null) {
            todayTracking = new TrackingEntity();
            todayTracking.setTrackingId(AppGenerator.getNewId());
            todayTracking.setHabitId(habitId);
            todayTracking.setCount(String.valueOf(defaultVal));
            todayTracking.setCurrentDate(currentDate);
//            todayTracking.setHabitDescription(null);
//            Database.getTrackingDb().saveTracking(todayTracking);
        }
        db.close();
        return todayTracking;
    }
}
