package habit.tracker.habittracker;

import android.annotation.SuppressLint;
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
    @BindView(R.id.tvGroup)
    TextView tvGroup;
    @BindView(R.id.rvNote)
    RecyclerView rvNote;
    @BindView(R.id.imgAddNote)
    ImageView imgAddNote;

    String habitId;
    HabitEntity habitEntity;
    String currentDate;
    NoteRecyclerViewAdapter noteRecyclerViewAdapter;
    List<NoteItem> noteItems = new ArrayList<>();
    List<TrackingEntity> trackingEntities;

    boolean isEdit = true;

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

        Database db = Database.getInstance(this);
        db.open();
        habitEntity = Database.getHabitDb().getHabit(habitId);
        trackingEntities = Database.getTrackingDb().getRecordByHabit(habitId);
        db.close();

        llHeader.setBackgroundColor(Color.parseColor(habitEntity.getHabitColor()));

        for (TrackingEntity entity : trackingEntities) {
            if (!TextUtils.isEmpty(entity.getDescription())) {
                noteItems.add(new NoteItem(entity.getTrackingId(), entity.getCurrentDate(),
                        AppGenerator.format(entity.getCurrentDate(), AppGenerator.YMD_SHORT, AppGenerator.DMY_SHORT), entity.getDescription()));
            }
            if (imgAddNote.getVisibility() != View.INVISIBLE
                    && currentDate.equals(entity.getCurrentDate())
                    && !TextUtils.isEmpty(entity.getDescription())) {
                imgAddNote.setVisibility(View.INVISIBLE);
            }
        }

        noteRecyclerViewAdapter = new NoteRecyclerViewAdapter(this, noteItems, this);
        rvNote.setLayoutManager(new LinearLayoutManager(this));
        rvNote.setAdapter(noteRecyclerViewAdapter);
    }

    @OnClick(R.id.imgAddNote)
    public void addNote(View v) {
        isEdit = false;
        TrackingEntity tracking = getTodayTracking(habitId, currentDate, 0);
        int index = 0;
        for (TrackingEntity entity: trackingEntities) {
            if (entity.getTrackingId().equals(tracking.getTrackingId())) {
                break;
            }
            index++;
        }
        showEditDialog("Lưu", "Hủy",
                AppGenerator.format(tracking.getCurrentDate(), AppGenerator.YMD_SHORT, AppGenerator.DMY_SHORT), tracking.getDescription(), index);
    }

    @Override
    public void onItemClick(View view, final int position) {
        if (view.getId() == R.id.itemNote) {
            isEdit = true;
            final NoteItem noteItem = noteItems.get(position);
            showEditDialog("Lưu", "Xóa", noteItem.getDate(), noteItem.getNote(), position);
        }
    }

    private void showEditDialog(String btn1, String btn2, String head, final String note, final int adapterPosition) {
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
                .setPositiveButton(btn1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String newNote = editNote.getText().toString().trim();
                        if (isEdit) {
                            updateNote(newNote, adapterPosition);
                        } else {

                            addTodayNote(newNote, trackingEntities.get(adapterPosition));
                        }
                        dialog.cancel();
                    }
                })
                .setNegativeButton(btn2, new DialogInterface.OnClickListener() {
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
        NoteItem noteItem = noteItems.get(position);
        if (TextUtils.isEmpty(newNote)) {
            noteItems.remove(position);
            if (noteItem.getDate().equals(currentDate)) {
                imgAddNote.setVisibility(View.VISIBLE);
            }
        } else {
            noteItems.get(position).setNote(newNote);
        }
        noteRecyclerViewAdapter.notifyDataSetChanged();
        updateData(newNote, noteItem.getTrackId());
    }

    private void deleteNote(int position) {
        NoteItem noteItem = noteItems.get(position);
        updateData(null, noteItem.getTrackId());
        if (noteItem.getDefDate().equals(currentDate)) {
            imgAddNote.setVisibility(View.VISIBLE);
        }
        noteItems.remove(position);
        noteRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addTodayNote(String newNote, TrackingEntity trackingEntity) {
        noteItems.add(new NoteItem(trackingEntity.getTrackingId(), trackingEntity.getCurrentDate(),
                AppGenerator.format(trackingEntity.getCurrentDate(), AppGenerator.YMD_SHORT, AppGenerator.DMY_SHORT), newNote));
        noteRecyclerViewAdapter.notifyDataSetChanged();
        imgAddNote.setVisibility(View.INVISIBLE);
        updateData(newNote, trackingEntity.getTrackingId());
    }

    private void updateData(String newNote, String trackId) {
        TrackingEntity trackingEntity = null;
        for (TrackingEntity entity : trackingEntities) {
            if (entity.getTrackingId().equals(trackId)) {
                trackingEntity = entity;
                break;
            }
        }
        if (trackingEntity == null) {
            return;
        }
        trackingEntity.setDescription(newNote);

        Database db = Database.getInstance(NoteActivity.this);
        db.open();
        Database.getTrackingDb().saveTracking(trackingEntity);
        db.close();

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
            todayTracking.setDescription(null);
            Database.getTrackingDb().saveTracking(todayTracking);
        }
        db.close();
        return todayTracking;
    }
}
