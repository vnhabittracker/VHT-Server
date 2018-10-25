package habit.tracker.habittracker.api.model.group;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GroupResponse {
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("data")
    @Expose
    private List<Group> groupList;

    public String getResult() {
        return result;
    }

    public List<Group> getGroupList() {
        return groupList;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
    }
}
