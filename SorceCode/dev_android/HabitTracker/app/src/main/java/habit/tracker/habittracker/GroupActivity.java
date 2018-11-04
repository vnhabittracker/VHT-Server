package habit.tracker.habittracker;

        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.text.TextUtils;
        import android.view.View;
        import android.view.WindowManager;
        import android.widget.EditText;
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
        import habit.tracker.habittracker.common.util.DateGenerator;
        import habit.tracker.habittracker.repository.Database;
        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;

public class GroupActivity extends AppCompatActivity implements GroupRecyclerViewAdapter.ItemClickListener {
    public static final String GROUP_NAME = "group_name";
    public static final String GROUP_ID = "group_id";

    RecyclerView rvGroupItem;
    GroupRecyclerViewAdapter groupViewAdapter;
    List<Group> data = new ArrayList<>();

    @BindView(R.id.btn_back)
    View btnBack;

    @BindView(R.id.groupName)
    EditText edGroupName;

    @BindView(R.id.imgAddGroup)
    View imgAddNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_group);
        ButterKnife.bind(this);


        VnHabitApiService mService = VnHabitApiUtils.getApiService();
        mService.getGroupItems().enqueue(new Callback<GroupResponse>() {
            @Override
            public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
                if (response.body().getResult().equals("1")) {
                    data.addAll(response.body().getGroupList());
                    Database db = new Database(GroupActivity.this);
                    db.open();
                    for (Group g : data) {
                        Database.sGroupDaoImpl.save(g);
                    }
                    db.close();
                    rvGroupItem = findViewById(R.id.rv_group);
                    rvGroupItem.setLayoutManager(new LinearLayoutManager(GroupActivity.this));
                    groupViewAdapter = new GroupRecyclerViewAdapter(GroupActivity.this, data);
                    groupViewAdapter.setClickListener(GroupActivity.this);
                    rvGroupItem.setAdapter(groupViewAdapter);
                }
            }

            @Override
            public void onFailure(Call<GroupResponse> call, Throwable t) {
                Toast.makeText(GroupActivity.this, "Not OK!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = getIntent();
        intent.putExtra(GROUP_ID, data.get(position).getGroupId());
        intent.putExtra(GROUP_NAME, data.get(position).getGroupName());
        setResult(RESULT_OK, intent);
        finish();
    }

    @OnClick(R.id.btn_back)
    public void back(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    @OnClick(R.id.imgAddGroup)
    public void addNewGroup(View v) {

        String groupName = edGroupName.getText().toString();
        if (TextUtils.isEmpty(groupName.trim())) {
            Toast.makeText(this, "Tên nhóm không được rỗng", Toast.LENGTH_SHORT).show();
            return;
        }

        final Group newGroup = new Group();
        newGroup.setGroupId(DateGenerator.getNewId());
        newGroup.setGroupName(groupName);

        VnHabitApiService mService = VnHabitApiUtils.getApiService();
        mService.addNewGroup(newGroup).enqueue(new Callback<GroupResponse>() {
            @Override
            public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
                if (response.body().getResult().equals("1")) {

                    Database db = new Database(GroupActivity.this);
                    db.open();
                    Database.sGroupDaoImpl.save(newGroup);
                    db.close();

                    edGroupName.setText(null);
                    data.add(0, newGroup);
                    groupViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<GroupResponse> call, Throwable t) {
                Toast.makeText(GroupActivity.this, "Tạo nhóm không thành công!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
