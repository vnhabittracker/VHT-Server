package habit.tracker.habittracker.api.model.feedback;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Feedback {
    @SerializedName("feedback_id")
    @Expose
    private String feedbackId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("star_num")
    @Expose
    private String starNum;
    @SerializedName("feedback_description")
    @Expose
    private String description;

    public String getFeedbackId() {
        return feedbackId;
    }

    public String getUserId() {
        return userId;
    }

    public String getStarNum() {
        return starNum;
    }

    public String getDescription() {
        return description;
    }

    public void setFeedbackId(String feedbackId) {
        this.feedbackId = feedbackId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setStarNum(String starNum) {
        this.starNum = starNum;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
