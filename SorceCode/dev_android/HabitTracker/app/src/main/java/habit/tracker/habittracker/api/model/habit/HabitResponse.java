package habit.tracker.habittracker.api.model.habit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HabitResponse {
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("data")
    @Expose
    private List<Habit> habit;

    public HabitResponse(String result, List<Habit> habit) {
        this.result = result;
        this.habit = habit;
    }

    public String getResult() {
        return result;
    }

    public List<Habit> getHabit() {
        return habit;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setHabit(List<Habit> habit) {
        this.habit = habit;
    }
}
