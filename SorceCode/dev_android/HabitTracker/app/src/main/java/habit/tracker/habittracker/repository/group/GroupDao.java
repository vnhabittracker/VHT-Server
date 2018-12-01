package habit.tracker.habittracker.repository.group;

import android.content.ContentValues;

import java.util.List;

import habit.tracker.habittracker.api.model.group.Group;

public interface GroupDao {
    List<GroupEntity> getAll();
    GroupEntity getGroup(String id);
    boolean save(Group groupEntity);
    void setContentValue(Group entity);
    ContentValues getContentValue();
}
