package habit.tracker.habittracker.api.model.feedback;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeedbackResponse {
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("data")
    @Expose
    private Feedback feedback;

    public String getResult() {
        return result;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }
}
