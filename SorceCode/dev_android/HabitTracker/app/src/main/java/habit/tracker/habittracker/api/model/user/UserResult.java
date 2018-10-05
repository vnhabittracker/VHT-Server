package habit.tracker.habittracker.api.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserResult {

    @SerializedName("result")
    @Expose
    private String result;

    @SerializedName("message")
    @Expose
    private String message;

    public String getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
