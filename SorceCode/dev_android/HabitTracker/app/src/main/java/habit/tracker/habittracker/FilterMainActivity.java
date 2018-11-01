package habit.tracker.habittracker;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import habit.tracker.habittracker.adapter.GroupRecyclerViewAdapter;
import habit.tracker.habittracker.api.VnHabitApiUtils;
import habit.tracker.habittracker.api.model.group.Group;
import habit.tracker.habittracker.api.model.group.GroupResponse;
import habit.tracker.habittracker.api.service.VnHabitApiService;
import habit.tracker.habittracker.repository.Database;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterMainActivity extends AppCompatActivity implements GroupRecyclerViewAdapter.ItemClickListener {

    @BindView(R.id.rv_group)
    RecyclerView rvGroupItem;
    GroupRecyclerViewAdapter recyclerViewAdapter;
    List<Group> data = new ArrayList<>();
    private int pos = -1;

    String type;
    String target;
    String group;

    @BindView(R.id.imgCancel)
    View imgCancel;
    @BindView(R.id.btn_TypeAll)
    View btnTypeAll;
    @BindView(R.id.btn_TypeDaily)
    View btnTypeDaily;
    @BindView(R.id.btn_TypeWeekly)
    View btnTypeWeek;
    @BindView(R.id.btn_TypeMonthly)
    View btnTypeMonth;
    @BindView(R.id.btn_TypeYearly)
    View btnTypeYear;

    @BindView(R.id.btn_targetAll)
    View btnTargetAll;
    @BindView(R.id.btn_targetBuild)
    View btnTargetBuild;
    @BindView(R.id.btn_targetQuit)
    View btnTargetQuit;

    View vType;
    View vTarget;

    @BindView(R.id.btnApply)
    Button btnApply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_filter_main);
        ButterKnife.bind(this);

        type = "-1";
        target = "-1";
        group = "-1";

        vType = btnTypeAll;
        vTarget = btnTargetAll;

        VnHabitApiService mService = VnHabitApiUtils.getApiService();
        mService.getGroupItems().enqueue(new Callback<GroupResponse>() {
            @Override
            public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
                if (response.body().getResult().equals("1")) {
                    data.addAll(response.body().getGroupList());
                    Database db = new Database(FilterMainActivity.this);
                    db.open();
                    for (Group g : data) {
                        Database.sGroupDaoImpl.save(g);
                    }
                    db.close();
                    rvGroupItem = findViewById(R.id.rv_group);
                    rvGroupItem.setLayoutManager(new LinearLayoutManager(FilterMainActivity.this));
                    recyclerViewAdapter = new GroupRecyclerViewAdapter(FilterMainActivity.this, data);
                    recyclerViewAdapter.setClickListener(FilterMainActivity.this);
                    rvGroupItem.setAdapter(recyclerViewAdapter);
                }
            }

            @Override
            public void onFailure(Call<GroupResponse> call, Throwable t) {
                Toast.makeText(FilterMainActivity.this, "Đã xảy ra lỗi.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(View view, int newPos) {
        if (pos != -1) {
            data.get(pos).setSelected(false);
        }
        data.get(newPos).setSelected(true);
        group = data.get(newPos).getGroupId();
        recyclerViewAdapter.notifyItemChanged(pos);
        recyclerViewAdapter.notifyItemChanged(newPos);
        pos = newPos;
    }

    @OnClick({R.id.btn_targetAll, R.id.btn_targetBuild, R.id.btn_targetQuit})
    public void setHabitTarget(View v) {
        setWhiteBg(vTarget);
        setGreenBg(v);
        vTarget = v;
        target = v.getTag().toString();
    }

    @OnClick({R.id.btn_TypeAll, R.id.btn_TypeDaily, R.id.btn_TypeWeekly, R.id.btn_TypeMonthly, R.id.btn_TypeYearly})
    public void setHabitType(View view) {
        setWhiteBg(vType);
        setGreenBg(view);
        vType = view;
        type = view.getTag().toString();
    }

    @OnClick(R.id.btnApply)
    public void apply(View v) {
        Intent intent = getIntent();
        intent.putExtra("type", type);
        intent.putExtra("target", target);
        intent.putExtra("group", group);
        setResult(RESULT_OK, intent);
        finish();
    }

    @OnClick({R.id.imgCancel})
    public void cancel(View v) {
        finish();
    }

    public void setGreenBg(View v) {
        v.setBackground(ContextCompat.getDrawable(this, R.drawable.button_green));
    }

    public void setWhiteBg(View v) {
        v.setBackground(ContextCompat.getDrawable(this, android.R.color.transparent));
    }
}
