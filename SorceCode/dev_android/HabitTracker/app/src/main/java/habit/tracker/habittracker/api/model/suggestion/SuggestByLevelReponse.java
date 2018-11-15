package habit.tracker.habittracker.api.model.suggestion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import habit.tracker.habittracker.api.model.search.HabitSuggestion;

public class SuggestByLevelReponse {
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("data")
    @Expose
    private List<List<HabitSuggestion>> data;

    public String getResult() {
        return result;
    }

    public List<List<HabitSuggestion>> getData() {
        return data;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setData(List<List<HabitSuggestion>> data) {
        this.data = data;
    }

    public class DataResponse {
        List<HabitSuggestion> data;

        public List<HabitSuggestion> getData() {
            return data;
        }

        public void setData(List<HabitSuggestion> data) {
            this.data = data;
        }
    }
}
