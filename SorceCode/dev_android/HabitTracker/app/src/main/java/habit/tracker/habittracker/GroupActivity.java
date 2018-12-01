package habit.tracker.habittracker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import habit.tracker.habittracker.adapter.GroupRecyclerViewAdapter;
import habit.tracker.habittracker.adapter.SwipeToDeleteCallback;
import habit.tracker.habittracker.api.VnHabitApiUtils;
import habit.tracker.habittracker.api.model.group.Group;
import habit.tracker.habittracker.api.model.group.GroupResponse;
import habit.tracker.habittracker.api.service.VnHabitApiService;
import habit.tracker.habittracker.common.util.AppGenerator;
import habit.tracker.habittracker.common.util.MySharedPreference;
import habit.tracker.habittracker.repository.Database;
import habit.tracker.habittracker.repository.group.GroupEntity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupActivity extends AppCompatActivity implements GroupRecyclerViewAdapter.ItemClickListener {
    public static final String GROUP_NAME = "group_name";
    public static final String GROUP_ID = "group_id";

    @BindView(R.id.mainLayout)
    LinearLayout linearLayout;

    RecyclerView recyclerViewGroup;
    GroupRecyclerViewAdapter groupViewAdapter;
    List<Group> groupList = new ArrayList<>();

    @BindView(R.id.btn_back)
    View btnBack;

    @BindView(R.id.groupName)
    EditText edGroupName;

    @BindView(R.id.imgAddGroup)
    View imgAddNew;

    VnHabitApiService mService = VnHabitApiUtils.getApiService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_group);
        ButterKnife.bind(this);

        final String userId = MySharedPreference.getUserId(this);

        mService.getGroups(userId).enqueue(new Callback<GroupResponse>() {
            @Override
            public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
                if (response.body().getResult().equals("1")) {
                    Database db = new Database(GroupActivity.this);
                    db.open();

                    List<Group> resultFromServer = response.body().getGroupList();
                    Map<String, String> mapGroupFromServer = new HashMap<>();
                    GroupEntity entity;

                    for (Group group : resultFromServer) {
                        entity = Database.getGroupDb().getGroup(group.getGroupId());
                        if (entity != null && entity.isDelete()) {

                            callDeleteGroupApi(entity.getGroupId());

                        } else {

                            group.setDefault(TextUtils.isEmpty(group.getUserId()));

                            Database.getGroupDb().save(group);

                            mapGroupFromServer.put(group.getGroupId(), group.getGroupName());
                        }
                    }

                    List<GroupEntity> groupEntities = Database.getGroupDb().getGroupsByUser(userId);
                    if (groupEntities == null || groupEntities.size() > 0) {
                        // get only default group
                        groupEntities = Database.getGroupDb().getAll();
                    }
                    if (groupEntities != null && groupEntities.size() > 0) {
                        Group group;
                        for (GroupEntity item : groupEntities) {
                            if (!item.isDelete()) {
                                group = new Group(item.getGroupId(), item.getUserId(), item.getGroupName(), item.getDescription(), item.isDefault());
                                groupList.add(group);

                                if (!mapGroupFromServer.containsKey(item.getGroupId())) {
                                    callAddGroupApi(group);
                                }
                            }
                        }
                    }

                    db.close();

                    populateRecyclerView();
                    enableSwipeToDeleteAndUndo();
                }
            }

            @Override
            public void onFailure(Call<GroupResponse> call, Throwable t) {
                Database db = new Database(GroupActivity.this);
                db.open();

                List<GroupEntity> list = Database.getGroupDb().getAll();
                for (GroupEntity entity : list) {
                    if (!entity.isDelete()) {
                        groupList.add(new Group(entity.getGroupId(), entity.getUserId(), entity.getGroupName(), entity.getDescription(), entity.isDefault()));
                    }
                }
                db.close();

                populateRecyclerView();
                enableSwipeToDeleteAndUndo();
            }
        });
    }

    private void populateRecyclerView() {
        recyclerViewGroup = findViewById(R.id.rv_group);
        recyclerViewGroup.setLayoutManager(new LinearLayoutManager(GroupActivity.this));
        groupViewAdapter = new GroupRecyclerViewAdapter(GroupActivity.this, groupList);
        groupViewAdapter.setClickListener(GroupActivity.this);
        recyclerViewGroup.setAdapter(groupViewAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = getIntent();
        intent.putExtra(GROUP_ID, groupList.get(position).getGroupId());
        intent.putExtra(GROUP_NAME, groupList.get(position).getGroupName());
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

        Database db = new Database(GroupActivity.this);
        db.open();

        final Group newGroup = new Group();
        newGroup.setGroupId(AppGenerator.getNewId());
        newGroup.setUserId(MySharedPreference.getUserId(this));
        newGroup.setGroupName(groupName);
        newGroup.setDefault(true);
        Database.getGroupDb().save(newGroup);

        edGroupName.setText(null);
        groupList.add(0, newGroup);
        groupViewAdapter.notifyDataSetChanged();

        callAddGroupApi(newGroup);

        db.close();
    }

    private void callAddGroupApi(Group group) {
        mService.addNewGroup(group).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    private void callDeleteGroupApi(final String groupId) {
        mService.deleteGroup(groupId).enqueue(new Callback<GroupResponse>() {
            @Override
            public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
                if (response.body().getResult().equals("1")) {
                    Database db = Database.getInstance(GroupActivity.this);
                    db.open();

                    Database.getGroupDb().delete(groupId);
                    db.close();
                }
            }

            @Override
            public void onFailure(Call<GroupResponse> call, Throwable t) {
            }
        });
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                Database db = Database.getInstance(GroupActivity.this);
                db.open();

                final int position = viewHolder.getAdapterPosition();
                final Group item = groupList.get(position);

                groupList.remove(position);
                groupViewAdapter.notifyDataSetChanged();

                item.setDelete(true);
                if (item.isDefault()){
                    Database.getGroupDb().save(item);
                } else {
                    callDeleteGroupApi(item.getGroupId());
                }

                Snackbar snackbar = Snackbar.make(linearLayout, "Nhóm " + item.getGroupName() + " đã bị xóa", Snackbar.LENGTH_LONG);
                snackbar.setAction("Khôi phục", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        groupList.add(position, item);
                        recyclerViewGroup.scrollToPosition(position);
                        groupViewAdapter.notifyDataSetChanged();

                        Database db = Database.getInstance(GroupActivity.this);
                        db.open();

                        item.setDelete(false);
                        Database.getGroupDb().save(item);
                        callAddGroupApi(item);

                        db.close();
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

                db.close();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerViewGroup);
    }
}
