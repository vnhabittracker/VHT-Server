package habit.tracker.habittracker.api.model.group;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Group {
    @SerializedName("group_id")
    @Expose
    private String groupId;
    @SerializedName("group_name")
    @Expose
    private String groupName;
    @SerializedName("parrent_id")
    @Expose
    private String parentId;
    @SerializedName("group_icon")
    @Expose
    private String groupIcon;
    @SerializedName("group_description")
    @Expose
    private String groupDescription;

    public String getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getParentId() {
        return parentId;
    }

    public String getGroupIcon() {
        return groupIcon;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void setGroupIcon(String groupIcon) {
        this.groupIcon = groupIcon;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }
}
