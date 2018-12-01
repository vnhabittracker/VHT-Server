package habit.tracker.habittracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import habit.tracker.habittracker.adapter.RecyclerViewItemClickListener;
import habit.tracker.habittracker.adapter.suggestion.SuggestByGroupAdapter;
import habit.tracker.habittracker.api.VnHabitApiUtils;
import habit.tracker.habittracker.api.model.group.Group;
import habit.tracker.habittracker.api.model.group.GroupResponse;
import habit.tracker.habittracker.api.model.search.HabitSuggestion;
import habit.tracker.habittracker.api.model.search.SearchResponse;
import habit.tracker.habittracker.api.service.VnHabitApiService;
import habit.tracker.habittracker.common.util.MySharedPreference;
import habit.tracker.habittracker.repository.Database;
import habit.tracker.habittracker.repository.group.GroupEntity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuggestionByGroupActivity extends AppCompatActivity implements RecyclerViewItemClickListener {
    public static final String SUGGEST_HABIT_ID = "suggest_habit_id";
    public static final String SUGGEST_HABIT_NAME_UNI = "suggest_habit_name_uni";

    @BindView(R.id.rvSuggestion)
    RecyclerView rvSuggestion;

    List<Group> groupsList;
    List<HabitSuggestion> suggestionsList;
    SuggestByGroupAdapter suggestByGroupAdapter;

    VnHabitApiService mService = VnHabitApiUtils.getApiService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_suggestion_by_group);
        ButterKnife.bind(this);

        groupsList = new ArrayList<>();
        suggestionsList = new ArrayList<>();

        initializeGroupList();

        suggestByGroupAdapter = new SuggestByGroupAdapter(this, suggestionsList, this);
        rvSuggestion.setLayoutManager(new LinearLayoutManager(this));
        rvSuggestion.setAdapter(suggestByGroupAdapter);
    }

    private void initializeHabitNameList() {
        mService.getAllHabitSuggestion().enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (response.body().getResult().equals("1")) {
                    List<HabitSuggestion> sgList = response.body().getSearchResult();
                    List<HabitSuggestion> buff = new ArrayList<>();
                    List<HabitSuggestion> sortList = new ArrayList<>();
                    int maxCount = 0;
                    for (Group g : groupsList) {
                        for (HabitSuggestion sg : sgList) {
                            if (g.getGroupId().equals(sg.getGroupId())) {
                                buff.add(
                                        new HabitSuggestion(
                                                sg.getHabitNameId(),
                                                sg.getGroupId(),
                                                sg.getHabitNameUni(),
                                                sg.getHabitName(),
                                                sg.getHabitNameCount(),
                                                false
                                        )
                                );
                            }
                        }
                        if (buff.size() > 0) {
                            for (HabitSuggestion sg : buff) {
                                if (Integer.parseInt(sg.getHabitNameCount()) > maxCount) {
                                    sortList.add(0, sg);
                                    maxCount = Integer.parseInt(sg.getHabitNameCount());
                                }
                            }
                            int nextStart = suggestionsList.size();
                            for (int i = 0; i < 5 && i < sortList.size(); i++) {
                                suggestionsList.add(
                                        new HabitSuggestion(
                                                sortList.get(i).getHabitNameId(),
                                                sortList.get(i).getGroupId(),
                                                sortList.get(i).getHabitNameUni(),
                                                sortList.get(i).getHabitName(),
                                                sortList.get(i).getHabitNameCount(),
                                                false
                                        )
                                );
                            }

                            suggestionsList.add(nextStart, new HabitSuggestion(g.getGroupId(), g.getGroupName(), true));
                        }
                        maxCount = 0;
                        buff.clear();
                        sortList.clear();
                    }
                    suggestByGroupAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {

            }
        });
    }

    private void initializeGroupList() {
        String userId = MySharedPreference.getUserId(this);
        mService.getGroups(userId).enqueue(new Callback<GroupResponse>() {
            @Override
            public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
                if (response.body().getResult().equals("1")) {
                    groupsList.addAll(response.body().getGroupList());
                    Database db = new Database(SuggestionByGroupActivity.this);
                    db.open();
                    for (Group group : groupsList) {
                        Database.getGroupDb().save(group);
                    }
                    db.close();

                    initializeHabitNameList();
                }
            }

            @Override
            public void onFailure(Call<GroupResponse> call, Throwable t) {
                Database db = new Database(SuggestionByGroupActivity.this);
                db.open();

                List<GroupEntity> entities = Database.getGroupDb().getAll();

                for (GroupEntity entity: entities) {
                    groupsList.add(new Group(
                            entity.getGroupId(),
                            entity.getUserId(),
                            entity.getGroupName(),
                            entity.getDescription(),
                            entity.isDefault()
                    ));
                }
                db.close();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent();
        intent.putExtra(SUGGEST_HABIT_ID, suggestionsList.get(position).getHabitNameId());
        intent.putExtra(SUGGEST_HABIT_NAME_UNI, suggestionsList.get(position).getHabitNameUni());
        setResult(RESULT_OK, intent);
        finish();
    }
}
