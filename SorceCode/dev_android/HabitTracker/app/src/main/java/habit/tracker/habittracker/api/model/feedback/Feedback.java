package habit.tracker.habittracker.api.model.feedback;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import habit.tracker.habittracker.repository.feedback.FeedbackEntity;

public class Feedback {
    @SerializedName("feedback_id")
    @Expose
    private String feedbackId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("star_num")
    @Expose
    private int starNum;
    @SerializedName("feedback_description")
    @Expose
    private String description;

    public String getFeedbackId() {
        return feedbackId;
    }

    public String getUserId() {
        return userId;
    }

    public int getStarNum() {
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

    public void setStarNum(int starNum) {
        this.starNum = starNum;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FeedbackEntity toEntity(boolean isUpdate) {
        FeedbackEntity entity = new FeedbackEntity();
        entity.setFeedbackId(feedbackId);
        entity.setUpdate(isUpdate);
        entity.setStarNum(starNum);
        entity.setDescription(description);
        return entity;
    }
}
