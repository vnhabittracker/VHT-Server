package habit.tracker.habittracker.api.model.tracking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrackingResult {
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("id")
    @Expose
    private String id;

    public String getResult() {
        return result;
    }

    public String getId() {
        return id;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setId(String id) {
        this.id = id;
    }
}
