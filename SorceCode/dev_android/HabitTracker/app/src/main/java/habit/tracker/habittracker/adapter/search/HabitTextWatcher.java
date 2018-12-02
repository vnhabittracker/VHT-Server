package habit.tracker.habittracker.adapter.search;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import habit.tracker.habittracker.api.VnHabitApiUtils;
import habit.tracker.habittracker.api.model.search.HabitSuggestion;
import habit.tracker.habittracker.api.model.search.SearchResponse;
import habit.tracker.habittracker.api.service.VnHabitApiService;
import habit.tracker.habittracker.common.util.AppGenerator;
import habit.tracker.habittracker.common.util.MySharedPreference;
import habit.tracker.habittracker.repository.Database;
import habit.tracker.habittracker.repository.user.UserEntity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static habit.tracker.habittracker.common.AppConstant.STATUS_OK;

public class HabitTextWatcher implements TextWatcher {
    private Context context;
    private SearchRecyclerViewAdapter habitSuggestionAdapter;
    private List<HabitSuggestion> searchResultList = new ArrayList<>();
    private VnHabitApiService mService;

    private boolean afterSelectedSuggestion = false;

    public HabitTextWatcher(Context context) {
        this.context = context;
        mService = VnHabitApiUtils.getApiService();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(final CharSequence searchKey, int start, int before, int count) {
        if (TextUtils.isEmpty(searchKey.toString())) {
            searchResultList.clear();
            habitSuggestionAdapter.notifyDataSetChanged();
            return;
        }
        if (afterSelectedSuggestion) {
            afterSelectedSuggestion = false;
            return;
        }

        mService.searchHabitName(AppGenerator.getSearchKey(searchKey.toString().trim())).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                searchResultList.clear();
                if (response.body().getResult().equals(STATUS_OK)) {
                    Database db = Database.getInstance(context);
                    db.open();

                    List<HabitSuggestion> searchResult = response.body().getSearchResult();
                    UserEntity userEntity = Database.getUserDb().getUser(MySharedPreference.getUserId(context));
                    int userLevel = AppGenerator.getLevel(Integer.parseInt(userEntity.getUserScore()));

                    if (searchResult.size() > 0) {
                        List<HabitSuggestion> easyHabitList = new ArrayList<>();
                        List<HabitSuggestion> mediumHabitList = new ArrayList<>();
                        List<HabitSuggestion> hardHabitList = new ArrayList<>();
                        List<HabitSuggestion> sortedHabitList = new ArrayList<>();
                        int topLow = -1;
                        int topMed = -1;
                        int topHig = -1;
                        int hbLevel;
                        int numOfUser;
                        for (HabitSuggestion sg : searchResult) {
                            hbLevel = (int) (((float) sg.getSuccessTrack() / (float) sg.getTotalTrack()) * 100);
                            numOfUser = Integer.parseInt(sg.getHabitNameCount());
                            if (hbLevel >= 80) {
                                if (numOfUser > topLow) {
                                    easyHabitList.add(0, sg);
                                    topLow = numOfUser;
                                } else {
                                    easyHabitList.add(sg);
                                }
                            } else if (hbLevel >= 50) {
                                if (numOfUser > topMed) {
                                    mediumHabitList.add(0, sg);
                                    topMed = numOfUser;
                                } else {
                                    mediumHabitList.add(sg);
                                }
                            } else {
                                if (numOfUser > topHig) {
                                    hardHabitList.add(0, sg);
                                    topMed = numOfUser;
                                } else {
                                    hardHabitList.add(sg);
                                }
                            }
                        }


                        if (userLevel <= 3) {
                            sortedHabitList.addAll(easyHabitList);
                            sortedHabitList.addAll(mediumHabitList);
                            sortedHabitList.addAll(hardHabitList);
                        } else if (userLevel < 6) {
                            sortedHabitList.addAll(mediumHabitList);
                            sortedHabitList.addAll(easyHabitList);
                            sortedHabitList.addAll(hardHabitList);
                        } else {
                            sortedHabitList.addAll(hardHabitList);
                            sortedHabitList.addAll(mediumHabitList);
                            sortedHabitList.addAll(easyHabitList);
                        }
                        searchResultList.addAll(sortedHabitList.size() >= 5 ? sortedHabitList.subList(0, 5) : sortedHabitList);
                    }
                    db.close();
                }
                // notify adapter to show new result
                habitSuggestionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
            }
        });
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public boolean isAfterSelectedSuggestion() {
        return afterSelectedSuggestion;
    }

    public SearchRecyclerViewAdapter getHabitSuggestionAdapter() {
        return habitSuggestionAdapter;
    }

    public List<HabitSuggestion> getSearchResultList() {
        return searchResultList;
    }

    public void setAfterSelectedSuggestion(boolean justSelectedSuggestion) {
        this.afterSelectedSuggestion = justSelectedSuggestion;
    }

    public void setAdapter(SearchRecyclerViewAdapter habitSuggestionAdapter) {
        this.habitSuggestionAdapter = habitSuggestionAdapter;
    }

    public void setSearchResultList(List<HabitSuggestion> searchResultList) {
        this.searchResultList = searchResultList;
    }
}
