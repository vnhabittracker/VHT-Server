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
    public List<GroupEntity> getAll() {
        List<GroupEntity> list = new ArrayList<>();

        cursor = super.query(GROUP_TABLE, GROUP_COLUMNS, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {

            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                list.add(cursorToEntity(cursor));
                cursor.moveToNext();
            }

            cursor.close();

            return list;
        }
        return null;
    }
    public List<GroupEntity> getGroupsByUser(String userId) {
        List<GroupEntity> list = new ArrayList<>();

        final String selectionArgs[] = {userId};
        final String selection = USER_ID + " = ?";

        cursor = super.query(GROUP_TABLE, GROUP_COLUMNS, selection, selectionArgs, null);

        if (cursor != null && cursor.getCount() > 0) {

            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                list.add(cursorToEntity(cursor));
                cursor.moveToNext();
            }

            cursor.close();

            return list;
        }
        return null;
    }


    @Override
    public GroupEntity getGroup(String groupId) {
        GroupEntity entity = null;
        final String selectionArgs[] = {groupId};
        final String selection = GROUP_ID + " = ?";
        cursor = super.query(GROUP_TABLE, GROUP_COLUMNS, selection, selectionArgs, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                entity = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return entity;
    }

    @Override
    public boolean save(Group group) {
        setContentValue(group);
        try {
            return super.replace(GROUP_TABLE, getContentValue()) > 0;
        } catch (SQLiteConstraintException ex) {
            return false;
        }
    }

    @Override
    public int delete(String id) {
        try {
            final String selectionArgs[] = {id};
            final String selection = GROUP_ID + " = ?";
            return super.mDb.delete(GROUP_TABLE, selection, selectionArgs);
        } catch (SQLiteConstraintException ignored) {
        }
        return 0;
    }

    @Override
    protected GroupEntity cursorToEntity(Cursor cursor) {
        GroupEntity entity = new GroupEntity();
        if (cursor.getColumnIndex(GROUP_ID) != -1) {
            entity.setGroupId(cursor.getString(cursor.getColumnIndexOrThrow(GROUP_ID)));
        }
        if (cursor.getColumnIndex(USER_ID) != -1) {
            entity.setUserId(cursor.getString(cursor.getColumnIndexOrThrow(USER_ID)));
        }
        if (cursor.getColumnIndex(GROUP_NAME) != -1) {
            entity.setGroupName(cursor.getString(cursor.getColumnIndexOrThrow(GROUP_NAME)));
        }
        if (cursor.getColumnIndex(GROUP_DESCRIPTION) != -1) {
            entity.setGroupDescription(cursor.getString(cursor.getColumnIndexOrThrow(GROUP_DESCRIPTION)));
        }
        if (cursor.getColumnIndex(IS_DELETE) != -1) {
            entity.setDelete(cursor.getString(cursor.getColumnIndexOrThrow(IS_DELETE)).equals("1"));
        }
        if (cursor.getColumnIndex(IS_DEFAULT) != -1) {
            entity.setDefault(cursor.getString(cursor.getColumnIndexOrThrow(IS_DEFAULT)).equals("1"));
        }
        return entity;
    }

    @Override
    public void setContentValue(Group group) {
        initialValues = new ContentValues();
        initialValues.put(GROUP_ID, group.getGroupId());
        initialValues.put(USER_ID, group.getUserId());
        initialValues.put(GROUP_NAME, group.getGroupName());
        initialValues.put(GROUP_DESCRIPTION, group.getGroupDescription());
        initialValues.put(IS_DELETE, group.isDelete() ? "1" : "0");
        initialValues.put(IS_DEFAULT, group.isDefault() ? "1" : "0");
    }

    @Override
    public ContentValues getContentValue() {
        return initialValues;
    }
}
