package habit.tracker.habittracker.api.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserResponse {

    @SerializedName("result")
    @Expose
    private String result;

    @SerializedName("data")
    @Expose
    private User data;

    public UserResponse(String result, User user) {
        this.result = result;
        this.data = user;
    }

    public String getResult() {
        return result;
    }

    public User getData() {
        return data;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setData(User data) {
        this.data = data;
    }
}
