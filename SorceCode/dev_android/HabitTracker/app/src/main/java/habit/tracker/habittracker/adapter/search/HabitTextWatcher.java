package habit.tracker.habittracker.adapter.search;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    private boolean afterSelection = false;

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
        if (afterSelection) {
            afterSelection = false;
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

                    if (searchResult.size() > 0) {

                        List<HabitSuggestion> easyHabitList = new ArrayList<>();
                        List<HabitSuggestion> mediumHabitList = new ArrayList<>();
                        List<HabitSuggestion> hardHabitList = new ArrayList<>();
                        List<HabitSuggestion> sortedHabitList = new ArrayList<>();

                        int hbLevel;
                        for (HabitSuggestion sg : searchResult) {
                            hbLevel = (int) (((float) sg.getSuccessTrack() / (float) sg.getTotalTrack()) * 100);
                            if (hbLevel >= 80) {
                                easyHabitList.add(sg);
                            } else if (hbLevel >= 50) {
                                mediumHabitList.add(sg);
                            } else {
                                hardHabitList.add(sg);
                            }
                        }

                        Comparator<HabitSuggestion> comparator = new Comparator<HabitSuggestion>() {
                            @Override
                            public int compare(HabitSuggestion sg1, HabitSuggestion sg2) {
                                if (Integer.parseInt(sg2.getHabitNameCount()) > Integer.parseInt(sg1.getHabitNameCount())) {
                                    return -1;
                                }
                                if (Integer.parseInt(sg2.getHabitNameCount()) < Integer.parseInt(sg1.getHabitNameCount())) {
                                    return 1;
                                }
                                return 0;
                            }
                        };

                        Collections.sort(easyHabitList, comparator);
                        Collections.sort(mediumHabitList, comparator);
                        Collections.sort(hardHabitList, comparator);

                        int userLevel = AppGenerator.getLevel(Integer.parseInt(userEntity.getUserScore()));
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

    public boolean isAfterSelection() {
        return afterSelection;
    }

    public SearchRecyclerViewAdapter getHabitSuggestionAdapter() {
        return habitSuggestionAdapter;
    }

    public List<HabitSuggestion> getSearchResultList() {
        return searchResultList;
    }

    public void setAfterSelection(boolean justSelectedSuggestion) {
        this.afterSelection = justSelectedSuggestion;
    }

    public void setAdapter(SearchRecyclerViewAdapter habitSuggestionAdapter) {
        this.habitSuggestionAdapter = habitSuggestionAdapter;
    }

    public void setSearchResultList(List<HabitSuggestion> searchResultList) {
        this.searchResultList = searchResultList;
    }
}
