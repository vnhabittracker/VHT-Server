package habit.tracker.habittracker.repository.group;

public class GroupEntity {
    private String groupId;
    private String userId;
    private String groupName;
    private String groupDescription;
    private boolean isDelete = false;
    private boolean isDefault = false;

    public String getGroupId() {
        return groupId;
    }

    public String getUserId() {
        return userId;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getDescription() {
        return groupDescription;
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

    public void setDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
}
