package habit.tracker.habittracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import habit.tracker.habittracker.api.model.search.HabitSuggestion;
import habit.tracker.habittracker.api.model.suggestion.SuggestByLevelReponse;
import habit.tracker.habittracker.api.service.VnHabitApiService;
import habit.tracker.habittracker.common.util.AppGenerator;
import habit.tracker.habittracker.common.util.MySharedPreference;
import habit.tracker.habittracker.repository.Database;
import habit.tracker.habittracker.repository.user.UserEntity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuggestionByLevelActivity extends AppCompatActivity implements RecyclerViewItemClickListener {

    public static String SUGGEST_NAME = "SuggestionByLevelActivity.pick_name";
    public static String SUGGEST_NAME_ID = "SuggestionByLevelActivity.suggest_name_id";

    @BindView(R.id.rvSuggestion)
    RecyclerView rvSuggestion;

    List<HabitSuggestion> displaySuggestList = new ArrayList<>();
    SuggestByGroupAdapter suggestByGroupAdapter;
    VnHabitApiService mService = VnHabitApiUtils.getApiService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_suggestion_by_level);
        ButterKnife.bind(this);

        mService.getHabitSuggestByLevel().enqueue(new Callback<SuggestByLevelReponse>() {
            @Override
            public void onResponse(Call<SuggestByLevelReponse> call, Response<SuggestByLevelReponse> response) {
                if (response.body().getResult().equals("1")) {
                    // 0: low, 1: med, 2: hig
                    List<List<HabitSuggestion>> data = response.body().getData();
                    List<HabitSuggestion> curLevl;
                    String[] level = new String[]{"Thói quen dễ được nhiều người chọn", "Thói quen trung bình được nhiều người chọn", "Thói quen khó được nhiều người chọn"};

                    Database db = Database.getInstance(SuggestionByLevelActivity.this);
                    db.open();
                    UserEntity userEntity = Database.getUserDb().getUser(MySharedPreference.getUserId(SuggestionByLevelActivity.this));
                    db.close();
                    int pendDate = AppGenerator.countDayBetween(userEntity.getCreatedDate(), AppGenerator.getCurrentDate(AppGenerator.YMD_SHORT));
                    if (pendDate >= 30) {
                        level[2] = "Thói quen khó được nhiều người chọn (khuyên chọn)";
                    } else {
                        level[0] = "Thói quen dễ được nhiều người chọn (khuyên chọn)";
                    }

                    for (int i = 0; i < data.size(); i++) {
                        displaySuggestList.add(new HabitSuggestion(null, level[i], true));
                        curLevl = data.get(i);
                        for (int j = 0; j < curLevl.size(); j++) {
                            displaySuggestList.add(new HabitSuggestion(
                                    curLevl.get(j).getHabitNameId(),
                                    curLevl.get(j).getGroupId(),
                                    curLevl.get(j).getHabitNameUni(),
                                    curLevl.get(j).getHabitName(),
                                    curLevl.get(j).getHabitNameCount(),
                                    false
                            ));
                        }
                    }
                    suggestByGroupAdapter = new SuggestByGroupAdapter(SuggestionByLevelActivity.this, displaySuggestList, SuggestionByLevelActivity.this);
                    rvSuggestion.setLayoutManager(new LinearLayoutManager(SuggestionByLevelActivity.this));
                    rvSuggestion.setAdapter(suggestByGroupAdapter);
                }
            }

            @Override
            public void onFailure(Call<SuggestByLevelReponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        HabitSuggestion item = displaySuggestList.get(position);
        Intent intent = new Intent(this, HabitActivity.class);
        intent.putExtra(SUGGEST_NAME_ID, item.getHabitNameId());
        intent.putExtra(SUGGEST_NAME, item.getHabitNameUni());
        startActivity(intent);
        finish();
    }
}
