package habit.tracker.habittracker.api.model.group;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Group {
    @SerializedName("group_id")
    @Expose
    private String groupId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("group_name")
    @Expose
    private String groupName;
    @SerializedName("group_description")
    @Expose
    private String groupDescription;

    private boolean isSelected = false;
    private boolean isDelete = false;
    private boolean isDefault = false;

    public Group() {
    }

    public Group(String groupId, String userId, String groupName, String groupDescription, boolean isDefault) {
        this.groupId = groupId;
        this.userId = userId;
        this.groupName = groupName;
        this.groupDescription = groupDescription;
        this.isDefault = isDefault;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getUserId() {
        return userId;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public void setDefault(boolean local) {
        isDefault = local;
    }
}
