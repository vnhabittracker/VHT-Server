package habit.tracker.habittracker.repository.group;

public class GroupEntity {
    private String groupId;
    private String groupName;
    private String parentId;
    private String groupIcon;
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
