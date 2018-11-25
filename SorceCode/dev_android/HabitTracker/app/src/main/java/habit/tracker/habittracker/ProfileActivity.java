package habit.tracker.habittracker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import habit.tracker.habittracker.adapter.RecyclerViewItemClickListener;
import habit.tracker.habittracker.adapter.suggestion.CustomLinearLayoutManager;
import habit.tracker.habittracker.adapter.suggestion.SuggestByGroupAdapter;
import habit.tracker.habittracker.api.VnHabitApiUtils;
import habit.tracker.habittracker.api.model.search.HabitSuggestion;
import habit.tracker.habittracker.api.model.suggestion.SuggestByLevelReponse;
import habit.tracker.habittracker.api.model.user.User;
import habit.tracker.habittracker.api.model.user.UserResponse;
import habit.tracker.habittracker.api.service.VnHabitApiService;
import habit.tracker.habittracker.common.AppConstant;
import habit.tracker.habittracker.common.util.AppGenerator;
import habit.tracker.habittracker.common.util.MySharedPreference;
import habit.tracker.habittracker.repository.Database;
import habit.tracker.habittracker.repository.habit.HabitEntity;
import habit.tracker.habittracker.repository.habit.HabitTracking;
import habit.tracker.habittracker.repository.tracking.TrackingEntity;
import habit.tracker.habittracker.repository.user.UserEntity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static habit.tracker.habittracker.common.AppConstant.TYPE_0;
import static habit.tracker.habittracker.common.AppConstant.TYPE_1;
import static habit.tracker.habittracker.common.AppConstant.TYPE_2;
import static habit.tracker.habittracker.common.AppConstant.TYPE_3;
import static habit.tracker.habittracker.common.util.AppGenerator.YMD_SHORT;
import static habit.tracker.habittracker.common.util.AppGenerator.getLevel;

public class ProfileActivity extends BaseActivity implements RecyclerViewItemClickListener {
    public static final int PICK_AVATAR = 0;
    public static String SUGGEST_NAME = "SuggestionByLevelActivity.pick_name";
    public static String SUGGEST_NAME_ID = "SuggestionByLevelActivity.suggest_name_id";

    @BindView(R.id.imgAvatar)
    ImageView imgAvatar;
    @BindView(R.id.tvStartedDate)
    TextView tvStartedDate;
    @BindView(R.id.tvUserDescription)
    TextView tvUserDescription;
    @BindView(R.id.tvLevel)
    TextView tvLevel;
    @BindView(R.id.tvUserScore)
    TextView tvUserScore;
    @BindView(R.id.tvBestContinue)
    TextView tvBestContinue;
    @BindView(R.id.tvCurrentContinue)
    TextView tvCurrentContinue;
    @BindView(R.id.tvContinueUsing)
    TextView tvContinueUsing;
    @BindView(R.id.tvTotalHabit)
    TextView tvTotalHabit;

    @BindView(R.id.rvSuggestion)
    RecyclerView rvSuggestion;

    UserEntity userEntity;
    List<HabitSuggestion> displaySuggestList = new ArrayList<>();
    SuggestByGroupAdapter suggestByGroupAdapter;
    VnHabitApiService mService = VnHabitApiUtils.getApiService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        suggestByGroupAdapter = new SuggestByGroupAdapter(this, displaySuggestList, this);
        CustomLinearLayoutManager customLinearLayoutManager = new CustomLinearLayoutManager(this);
        customLinearLayoutManager.setScrollEnabled(false);
        rvSuggestion.setLayoutManager(customLinearLayoutManager);
        rvSuggestion.setAdapter(suggestByGroupAdapter);

        String[] userInfo = MySharedPreference.getUser(this);
        mService.getUser(userInfo[1], userInfo[2]).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.body().getResult().equals(AppConstant.RES_OK)) {
                    User user = response.body().getData();
                    Database db = new Database(ProfileActivity.this);
                    db.open();
                    if (user != null) {
                        UserEntity userEntity = Database.getUserDb().getUser(user.getUserId());
                        userEntity.setUserId(user.getUserId());
                        userEntity.setUsername(user.getUsername());
                        userEntity.setEmail(user.getEmail());
                        userEntity.setPhone(user.getPhone());
                        userEntity.setGender(user.getGender());
                        userEntity.setDateOfBirth(user.getDateOfBirth());
                        userEntity.setPassword(user.getPassword());
                        userEntity.setUserIcon(user.getUserIcon());
//                        userEntity.setAvatar(user.getAvatar());
                        userEntity.setUserDescription(user.getUserDescription());
                        userEntity.setCreatedDate(user.getCreatedDate());
                        userEntity.setLastLoginTime(user.getLastLoginTime());
                        userEntity.setContinueUsingCount(user.getContinueUsingCount());
                        userEntity.setCurrentContinueUsingCount(user.getCurrentContinueUsingCount());
                        userEntity.setBestContinueUsingCount(user.getBestContinueUsingCount());
                        userEntity.setUserScore(user.getUserScore());
                        Database.getUserDb().saveUser(userEntity);
                    }
                    db.close();
                    initializeScreen();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
            }
        });
    }

    private void initializeScreen() {
        mService.getHabitSuggestByLevel().enqueue(new Callback<SuggestByLevelReponse>() {
            @Override
            public void onResponse(Call<SuggestByLevelReponse> call, Response<SuggestByLevelReponse> response) {
                if (response.body().getResult().equals("1")) {
                    Database db = Database.getInstance(ProfileActivity.this);
                    db.open();

                    // 0: low, 1: med, 2: hig
                    List<List<HabitSuggestion>> data = response.body().getData();

                    String currentDate = AppGenerator.getCurrentDate(YMD_SHORT);
                    String userId = MySharedPreference.getUserId(ProfileActivity.this);
                    userEntity = Database.getUserDb().getUser(userId);

                    int habitCount = Database.getHabitDb().countHabitByUser(userId);
                    int userLevel = AppGenerator.getLevel(Integer.parseInt(userEntity.getUserScore()));
                    String[] level = new String[]{"Thói quen dễ được nhiều người chọn", "Thói quen trung bình được nhiều người chọn", "Thói quen khó được nhiều người chọn"};
                    if (userLevel <= 3) {
                        level[0] = "Thói quen dễ được nhiều người chọn (khuyên chọn)";
                    } else if (userLevel <= 6) {
                        level[1] = "Thói quen trung bình được nhiều người chọn (khuyên chọn)";
                    } else {
                        level[2] = "Thói quen khó được nhiều người chọn (khuyên chọn)";
                    }

                    List<HabitSuggestion> currentLevel;
                    for (int i = 0; i < data.size(); i++) {
                        displaySuggestList.add(new HabitSuggestion(null, level[i], true));
                        currentLevel = data.get(i);
                        for (int j = 0; j < currentLevel.size(); j++) {
                            displaySuggestList.add(new HabitSuggestion(
                                    currentLevel.get(j).getHabitNameId(),
                                    currentLevel.get(j).getGroupId(),
                                    currentLevel.get(j).getHabitNameUni(),
                                    currentLevel.get(j).getHabitName(),
                                    currentLevel.get(j).getHabitNameCount(),
                                    false
                            ));
                        }
                    }
                    if (userEntity.getAvatar() != null) {
                        try {
                            Uri uri = Uri.parse(userEntity.getAvatar());
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(ProfileActivity.this.getContentResolver(), uri);
                            imgAvatar.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    tvUserDescription.setText(userEntity.getUserDescription());
                    tvStartedDate.setText(AppGenerator.format(userEntity.getCreatedDate(), AppGenerator.YMD_SHORT, AppGenerator.DMY_SHORT));
                    tvLevel.setText(String.valueOf(getLevel(Integer.parseInt(userEntity.getUserScore()))));
                    tvUserScore.setText(userEntity.getUserScore());
                    tvBestContinue.setText(userEntity.getBestContinueUsingCount() + " ngày");
                    tvCurrentContinue.setText(userEntity.getCurrentContinueUsingCount() + " ngày");
                    tvContinueUsing.setText(userEntity.getContinueUsingCount() + " ngày");
                    tvTotalHabit.setText(String.valueOf(habitCount));
                    suggestByGroupAdapter.notifyDataSetChanged();

//                    int totalTrack = 0;
//                    int successTrack = 0;
//                    HabitEntity habitEntity;
//                    List<TrackingEntity> trackingEntityList;
//                    List<HabitTracking> habitTrackingList = Database.getHabitDb().getHabitTracking(userId, userEntity.getCreatedDate(), currentDate);
//                    for (HabitTracking habitTracking : habitTrackingList) {
//                        habitEntity = habitTracking.getHabit();
//                        trackingEntityList = habitTracking.getTrackingList();
//                        switch (habitEntity.getHabitType()) {
//                            case TYPE_0:
//                                for (TrackingEntity trackingEntity: trackingEntityList) {
//                                }
//                                break;
//                            case TYPE_1:
//                                break;
//                            case TYPE_2:
//                                break;
//                            case TYPE_3:
//                                break;
//                        }
//                    }

                    db.close();
                }
            }

            @Override
            public void onFailure(Call<SuggestByLevelReponse> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.imgAvatar)
    public void setAvatar(ImageView img) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT );
        startActivityForResult(Intent.createChooser(intent, "Cài đặt avatar"), PICK_AVATAR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_AVATAR) {
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                Uri uri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    imgAvatar.setImageBitmap(bitmap);

                    Database db = Database.getInstance(ProfileActivity.this);
                    db.open();
                    UserEntity userEntity = Database.getUserDb().getUser(MySharedPreference.getUserId(ProfileActivity.this));
                    if (userEntity != null) {
                        userEntity.setAvatar(uri.toString());
                        Database.getUserDb().saveUser(userEntity);
                    }
                    db.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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