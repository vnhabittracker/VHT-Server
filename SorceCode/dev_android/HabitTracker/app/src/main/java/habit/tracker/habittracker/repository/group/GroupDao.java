package habit.tracker.habittracker.repository.group;

import android.content.ContentValues;

import habit.tracker.habittracker.api.model.group.Group;

public interface GroupDao {
    GroupEntity getGroup(String id);
    boolean save(Group groupEntity);
    void setContentValue(Group entity);
    ContentValues getContentValue();
}
