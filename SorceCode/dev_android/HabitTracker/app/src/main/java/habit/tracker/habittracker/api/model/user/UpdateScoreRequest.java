package habit.tracker.habittracker.api.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateScoreRequest {
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("user_score")
    @Expose
    private String userScore;

    public String getUserId() {
        return userId;
    }

    public String getUserScore() {
        return userScore;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserScore(String score) {
        this.userScore = score;
    }
}
