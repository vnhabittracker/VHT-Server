package habit.tracker.habittracker;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import habit.tracker.habittracker.adapter.RecyclerViewItemClickListener;
import habit.tracker.habittracker.adapter.note.NoteItem;
import habit.tracker.habittracker.adapter.note.NoteRecyclerViewAdapter;
import habit.tracker.habittracker.api.VnHabitApiUtils;
import habit.tracker.habittracker.api.model.tracking.TrackingList;
import habit.tracker.habittracker.api.model.user.User;
import habit.tracker.habittracker.api.service.VnHabitApiService;
import habit.tracker.habittracker.common.swipe.SwipeToDeleteCallback;
import habit.tracker.habittracker.common.util.AppGenerator;
import habit.tracker.habittracker.repository.Database;
import habit.tracker.habittracker.repository.habit.HabitEntity;
import habit.tracker.habittracker.repository.tracking.TrackingEntity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static habit.tracker.habittracker.common.AppConstant.TYPE_1;

public class NoteActivity extends BaseActivity implements RecyclerViewItemClickListener {
    @BindView(R.id.llHeader)
    View llHeader;
    @BindView(R.id.rvNote)
    RecyclerView recyclerViewNote;
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

    @BindView(R.id.tabEditHabit)
    View tabEditHabit;
    @BindView(R.id.tabAddJournal)
    View tabAddJournal;
    @BindView(R.id.tabChart)
    View tabChart;
    @BindView(R.id.tabCalendar)
    View tabCalendar;

    String habitId;
    HabitEntity habitEntity;
    boolean isCountHabitType = false;
    String currentDate;
    NoteRecyclerViewAdapter noteRecyclerViewAdapter;
    List<NoteItem> nonEmptyNoteList = new ArrayList<>();
    List<TrackingEntity> trackingRecordList;

    boolean isEdit = true;
    int curTrackingCount = 0;
    private int timeLine = 0;
    private boolean[] availDaysInWeek = new boolean[7];

    Database mDb = Database.getInstance(this);
    VnHabitApiService mApiService = VnHabitApiUtils.getApiService();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == HabitActivity.REQUEST_UPDATE) {
            boolean delete = false;
            if (data != null) {
                delete = data.getBooleanExtra("delete", false);
            }
            if (!delete) {
                initializeScreen(habitId);
                updateUI();

            } else {
                finish();
            }
        }
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_note);
        ButterKnife.bind(this);

        currentDate = AppGenerator.getCurrentDate(AppGenerator.YMD_SHORT);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            habitId = bundle.getString(MainActivity.HABIT_ID);
        }

        initializeScreen(habitId);

        updateUI();

//        enableSwipeToDeleteAndUndo();
    }

    @SuppressLint("ResourceType")
    private void initializeScreen(String habitId) {
        mDb.open();

        habitEntity = Database.getHabitDb().getHabit(habitId);
        isCountHabitType = habitEntity.getMonitorType().equals(TYPE_1);

        // contain all record
        trackingRecordList = Database.getTrackingDb().getTrackingRecordsByHabit(habitId);

        TrackingEntity todayTracking = Database.getTrackingDb().getTracking(habitEntity.getHabitId(), currentDate);

        availDaysInWeek[0] = habitEntity.getMon().equals("1");
        availDaysInWeek[1] = habitEntity.getTue().equals("1");
        availDaysInWeek[2] = habitEntity.getWed().equals("1");
        availDaysInWeek[3] = habitEntity.getThu().equals("1");
        availDaysInWeek[4] = habitEntity.getFri().equals("1");
        availDaysInWeek[5] = habitEntity.getSat().equals("1");
        availDaysInWeek[6] = habitEntity.getSun().equals("1");

        curTrackingCount = 0;
        if (todayTracking != null) {
            curTrackingCount = Integer.parseInt(todayTracking.getCount());
        }

        // list only contain not empty note record
        nonEmptyNoteList.clear();
        if (trackingRecordList != null) {
            for (TrackingEntity entity : trackingRecordList) {
                if (!TextUtils.isEmpty(entity.getDescription())) {
                    nonEmptyNoteList.add(new NoteItem(entity.getTrackingId(),
                            entity.getCurrentDate(),
                            AppGenerator.format(entity.getCurrentDate(), AppGenerator.YMD_SHORT, AppGenerator.DMY_SHORT),
                            entity.getDescription()));
                }
            }
        } else {
            trackingRecordList = new ArrayList<>();
        }

        noteRecyclerViewAdapter = new NoteRecyclerViewAdapter(this, nonEmptyNoteList, this);
        recyclerViewNote.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNote.setAdapter(noteRecyclerViewAdapter);

        if (TextUtils.isEmpty(habitEntity.getHabitColor()) || habitEntity.getHabitColor().equals(getString(R.color.color0))) {
            llHeader.setBackgroundColor(Color.parseColor(getString(R.color.gray2)));
        } else {
            llHeader.setBackgroundColor(Color.parseColor(habitEntity.getHabitColor()));
        }

        if (!isCountHabitType) {
            if (imgMinusCount.getVisibility() == View.VISIBLE) {
                imgMinusCount.setVisibility(View.GONE);
            }
            if (imgAddCount.getVisibility() == View.VISIBLE) {
                imgAddCount.setVisibility(View.GONE);
            }
            if (tabChart.getVisibility() == View.VISIBLE) {
                tabChart.setVisibility(View.GONE);
            }
        } else {
            if (imgMinusCount.getVisibility() == View.GONE) {
                imgMinusCount.setVisibility(View.VISIBLE);
            }
            if (imgAddCount.getVisibility() == View.GONE) {
                imgAddCount.setVisibility(View.VISIBLE);
            }
            if (tabChart.getVisibility() == View.GONE) {
                tabChart.setVisibility(View.VISIBLE);
            }
        }
    }

    @OnClick(R.id.imgAddNote)
    public void onAddNoteClick(View v) {
        isEdit = false;
        TrackingEntity trackingEntity = getTodayTracking(habitId, currentDate, 0);
        int index = 0;
        for (NoteItem item : nonEmptyNoteList) {
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
            final NoteItem noteItem = nonEmptyNoteList.get(position);
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
        title.setPadding(25, 20, 0, 10);
        title.setTextSize(14);
        builder.setCustomTitle(title);

        builder.setView(inflatedView)
                .setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String newNote = editNote.getText().toString().trim();
                        if (!TextUtils.isEmpty(newNote.trim())) {
                            if (isEdit) {
                                update(newNote, adapterPosition);
                            } else {
                                addNew(newNote);
                            }
                        }
                        dialog.cancel();
                    }
                })
                .setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (isEdit) {
                            delete(adapterPosition);
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

    private void addNew(String newNote) {
        mDb.open();

        NoteItem item;
        for (TrackingEntity entity : trackingRecordList) {
            if (entity.getCurrentDate().equals(currentDate)) {
                item = new NoteItem(entity.getTrackingId(), entity.getCurrentDate(),
                        AppGenerator.format(entity.getCurrentDate(), AppGenerator.YMD_SHORT, AppGenerator.DMY_SHORT), newNote);
                addToDisplayList(item);
                saveToLocalAndApi(newNote, entity.getTrackingId());
                return;
            }
        }

        TrackingEntity entity = new TrackingEntity();
        entity.setTrackingId(AppGenerator.getNewId());
        entity.setHabitId(habitId);
        entity.setCount("0");
        entity.setCurrentDate(currentDate);
        Database.getTrackingDb().saveUpdateTracking(entity);

        item = new NoteItem(entity.getTrackingId(), entity.getCurrentDate(),
                AppGenerator.format(entity.getCurrentDate(), AppGenerator.YMD_SHORT, AppGenerator.DMY_SHORT), newNote);
        addToDisplayList(item);

        saveToLocalAndApi(newNote, entity.getTrackingId());
    }

    private void addToDisplayList(NoteItem item) {
        nonEmptyNoteList.add(item);
        Collections.sort(nonEmptyNoteList, new Comparator<NoteItem>() {
            @Override
            public int compare(NoteItem n1, NoteItem n2) {
                if (n2.getDefDate().compareTo(n1.getDefDate()) > 0) {
                    return 1;
                }
                if (n2.getDefDate().compareTo(n1.getDefDate()) < 0) {
                    return -1;
                }
                return 0;
            }
        });
        noteRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void delete(int position) {
        NoteItem item = nonEmptyNoteList.get(position);
        saveToLocalAndApi(null, item.getTrackingId());
        nonEmptyNoteList.remove(position);
        noteRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void update(String newNote, int position) {
        NoteItem item = nonEmptyNoteList.get(position);
        if (TextUtils.isEmpty(newNote)) {
            nonEmptyNoteList.remove(position);
        } else {
            item.setNote(newNote);
        }
        noteRecyclerViewAdapter.notifyDataSetChanged();
        saveToLocalAndApi(newNote, item.getTrackingId());
    }

    private void saveToLocalAndApi(String newNote, final String trackId) {
        mDb.open();

        TrackingEntity entity = Database.getTrackingDb().getTracking(trackId);
        if (entity != null) {
            // save to local
            entity.setDescription(newNote);
            entity.setUpdate(true);
            Database.getTrackingDb().saveUpdateTracking(entity);

            // call api
            TrackingList trackingData = new TrackingList();
            trackingData.getTrackingList().add(entity.toModel());
            callUpdateTrackingApi(trackingData, entity.getTrackingId());
        }
    }

    @OnClick({R.id.minusCount, R.id.addCount})
    public void onTrackingCountChanged(View v) {
        mDb.open();

        if (timeLine > 0 || currentDate.compareTo(habitEntity.getStartDate()) < 0 || !AppGenerator.isValidTrackingDay(currentDate, availDaysInWeek)) {
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

        // save to mDb
        TrackingEntity entity = Database.getTrackingDb().getTracking(habitEntity.getHabitId(), currentDate);
        if (entity == null) {
            entity = new TrackingEntity();
            entity.setTrackingId(AppGenerator.getNewId());
            entity.setHabitId(habitEntity.getHabitId());
            entity.setCurrentDate(currentDate);
        }
        entity.setCount(String.valueOf(curTrackingCount));
        entity.setUpdate(true);
        Database.getTrackingDb().saveUpdateTracking(entity);

        updateUI();

        TrackingList trackingData = new TrackingList();
        trackingData.getTrackingList().add(entity.toModel());
        callUpdateTrackingApi(trackingData, entity.getTrackingId());
    }

    private void callUpdateTrackingApi(final TrackingList data, final String trackId) {
        VnHabitApiService service = VnHabitApiUtils.getApiService();
        service.saveUpdateTracking(data).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                mDb.open();
                Database.getTrackingDb().setUpdate(trackId, false);
                mDb.close();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    @OnClick({R.id.pre, R.id.next})
    public void onDateChanged(View v) {
        mDb.open();

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

        TrackingEntity todayTracking = Database.getTrackingDb().getTracking(habitEntity.getHabitId(), currentDate);

        curTrackingCount = 0;
        if (todayTracking != null) {
            curTrackingCount = Integer.parseInt(todayTracking.getCount());
        }

        updateUI();
    }


    @OnClick(R.id.tabEditHabit)
    public void editHabitDetails(View v) {
        super.editHabitDetails(habitEntity.getHabitId());
    }

    @OnClick(R.id.tabChart)
    public void showDetailsChart(View v) {
        super.showDetailsChart(habitEntity.getHabitId());
    }

    @OnClick(R.id.tabCalendar)
    public void showOnCalendar(View v) {
        super.showOnCalendar(habitEntity.getHabitId());
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
            if (imgAddNote.getVisibility() != View.VISIBLE) {
                imgAddNote.setVisibility(View.INVISIBLE);
            }
        } else {
            if (isCountHabitType) {
                tvTrackCount.setText(String.valueOf(curTrackingCount) + " " + habitEntity.getMonitorUnit());
            } else if (curTrackingCount > 0) {
                tvTrackCount.setText("Hoàn thành");
            } else if (curTrackingCount == 0) {
                tvTrackCount.setText("Chưa hoàn thành");
            }
            if (imgAddNote.getVisibility() != View.INVISIBLE) {
                imgAddNote.setVisibility(View.VISIBLE);
            }
        }
    }

    public TrackingEntity getTodayTracking(String habitId, String currentDate, int defaultVal) {
        mDb.open();

        TrackingEntity todayTracking = Database.getTrackingDb().getTracking(habitId, currentDate);
        if (todayTracking == null) {
            todayTracking = new TrackingEntity();
            todayTracking.setTrackingId(AppGenerator.getNewId());
            todayTracking.setHabitId(habitId);
            todayTracking.setCount(String.valueOf(defaultVal));
            todayTracking.setCurrentDate(currentDate);
            todayTracking.setDescription(null);
        }
        return todayTracking;
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                final NoteItem item = nonEmptyNoteList.get(position);

                nonEmptyNoteList.remove(position);
                noteRecyclerViewAdapter.notifyDataSetChanged();

//                Snackbar snackbar = Snackbar
//                        .make(coordinatorLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
//                snackbar.setAction("UNDO", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        mAdapter.restoreItem(item, position);
//                        recyclerViewNote.scrollToPosition(position);
//                    }
//                });
//
//                snackbar.setActionTextColor(Color.YELLOW);
//                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerViewNote);
    }

    @Override
    protected void onStop() {
        mDb.close();
        super.onStop();
    }
}
