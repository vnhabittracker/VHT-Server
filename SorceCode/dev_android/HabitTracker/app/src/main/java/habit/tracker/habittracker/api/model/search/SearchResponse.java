package habit.tracker.habittracker.api.model.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResponse {
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("data")
    @Expose
    private List<HabitSuggestion> searchResult;

    public String getResult() {
        return result;
    }

    public List<HabitSuggestion> getSearchResult() {
        return searchResult;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setSearchResult(List<HabitSuggestion> searchResult) {
        this.searchResult = searchResult;
    }
}
