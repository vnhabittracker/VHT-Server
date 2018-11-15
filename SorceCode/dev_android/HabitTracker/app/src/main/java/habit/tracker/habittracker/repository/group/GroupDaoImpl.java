package habit.tracker.habittracker.repository.group;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import habit.tracker.habittracker.api.model.group.Group;
import habit.tracker.habittracker.repository.MyDatabaseHelper;

public class GroupDaoImpl extends MyDatabaseHelper implements GroupDao, GroupSchema {
    private Cursor cursor;
    private ContentValues initialValues;

    public GroupDaoImpl(SQLiteDatabase db){
        super(db);
    }

    @Override
    public List<GroupEntity> fetchGroup() {
        List<GroupEntity> groupEntities = new ArrayList<>();
        cursor = super.query(GROUP_TABLE, GROUP_COLUMNS, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                groupEntities.add(cursorToEntity(cursor));
                cursor.moveToNext();
            }
            cursor.close();
            return groupEntities;
        }
        return null;
    }

    @Override
    public GroupEntity getGroup(String groupId) {
        final String selectionArgs[] = {groupId};
        final String selection = GROUP_ID + " = ?";
        GroupEntity entity = new GroupEntity();
        cursor = super.query(GROUP_TABLE, GROUP_COLUMNS, selection, selectionArgs, GROUP_ID);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                entity = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
            return entity;
        }
        return null;
    }

    @Override
    public boolean save(Group groupEntity) {
        setContentValue(groupEntity);
        try {
            return super.replace(GROUP_TABLE, getContentValue()) > 0;
        } catch (SQLiteConstraintException ex) {
            return false;
        }
    }

    @Override
    public int delete(String id) {
        return 0;
    }

    @Override
    protected GroupEntity cursorToEntity(Cursor cursor) {
        GroupEntity entity = new GroupEntity();
        if (cursor.getColumnIndex(GROUP_ID) != -1) {
            entity.setGroupId(cursor.getString(cursor.getColumnIndexOrThrow(GROUP_ID)));
        }
        if (cursor.getColumnIndex(GROUP_NAME) != -1) {
            entity.setGroupName(cursor.getString(cursor.getColumnIndexOrThrow(GROUP_NAME)));
        }
        if (cursor.getColumnIndex(PARENT_ID) != -1) {
            entity.setParentId(cursor.getString(cursor.getColumnIndexOrThrow(PARENT_ID)));
        }
        if (cursor.getColumnIndex(GROUP_ICON) != -1) {
            entity.setGroupIcon(cursor.getString(cursor.getColumnIndexOrThrow(GROUP_ICON)));
        }
        if (cursor.getColumnIndex(GROUP_DESCRIPTION) != -1) {
            entity.setGroupDescription(cursor.getString(cursor.getColumnIndexOrThrow(GROUP_DESCRIPTION)));
        }
        return entity;
    }

    @Override
    public void setContentValue(Group entity) {
        initialValues = new ContentValues();
        initialValues.put(GROUP_ID, entity.getGroupId());
        initialValues.put(GROUP_NAME, entity.getGroupName());
        initialValues.put(PARENT_ID, entity.getParentId());
        initialValues.put(GROUP_ICON, entity.getGroupIcon());
        initialValues.put(GROUP_DESCRIPTION, entity.getGroupDescription());
    }

    @Override
    public ContentValues getContentValue() {
        return initialValues;
    }
}
