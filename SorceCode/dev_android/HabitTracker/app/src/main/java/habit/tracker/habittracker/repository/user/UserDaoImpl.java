package habit.tracker.habittracker.repository.user;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import habit.tracker.habittracker.repository.MyDatabaseHelper;

public class UserDaoImpl extends MyDatabaseHelper implements UserDao, UserSchema {

    private Cursor cursor;
    private ContentValues initialValues;

    public UserDaoImpl(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public int delete(String id) {
        return mDb.delete(USER_TABLE, null, null);
    }

    @Override
    protected UserEntity cursorToEntity(Cursor cursor) {
        UserEntity userEntity = new UserEntity();
        if (cursor != null) {
            if (cursor.getColumnIndex(USER_ID) != -1) {
                userEntity.setUserId(cursor.getString(cursor.getColumnIndexOrThrow(USER_ID)));
            }
            if (cursor.getColumnIndex(USERNAME) != -1) {
                userEntity.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(USERNAME)));
            }
            if (cursor.getColumnIndex(PASSWORD) != -1) {
                userEntity.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(PASSWORD)));
            }
            if (cursor.getColumnIndex(EMAIL) != -1) {

            }
            if (cursor.getColumnIndex(PHONE) != -1) {

            }
            if (cursor.getColumnIndex(GENDER) != -1) {

            }
            if (cursor.getColumnIndex(DATE_OF_BIRTH) != -1) {

            }
            if (cursor.getColumnIndex(USER_ICON) != -1) {

            }
            if (cursor.getColumnIndex(AVATAR) != -1) {

            }
            if (cursor.getColumnIndex(USER_DESCRIPTION) != -1) {

            }
        }
        return userEntity;
    }

    @Override
    public List<UserEntity> fetchUser() {
        List<UserEntity> list = new ArrayList<>();
        Cursor cursor = super.query(USER_TABLE, USER_COLUMNS, null,
                null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                list.add(cursorToEntity(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return list;
    }

    @Override
    public UserEntity getUser(int userId) {
        final String selectionArgs[] = {String.valueOf(userId)};
        final String selection = USER_ID + " = ?";
        UserEntity userEntity = new UserEntity();
        cursor = super.query(USER_TABLE, USER_COLUMNS, selection, selectionArgs, USER_ID);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                userEntity = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return userEntity;
    }

    @Override
    public UserEntity getUser(String username, String password) {
        final String selectionArgs[] = {username, password};
        final String selection = USERNAME + " =? AND " + PASSWORD + " =? ";
        UserEntity userEntity = new UserEntity();
        cursor = super.query(USER_TABLE, USER_COLUMNS, selection, selectionArgs, USER_ID);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                userEntity = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return userEntity;
    }

    @Override
    public boolean saveUser(UserEntity userEntity) {
        setContentValue(userEntity);
        try {
            return super.replace(USER_TABLE, getContentValue()) > 0;
        } catch (SQLiteConstraintException ex) {
            return false;
        }
    }

    @Override
    public boolean update(UserEntity userEntity) {
        final String selectionArgs[] = {String.valueOf(userEntity.getUserId())};
        final String selection = USER_ID + " = ?";
        setContentValue(userEntity);
        try {
            return super.update(USER_TABLE, getContentValue(), selection, selectionArgs) > 0;
        } catch (SQLiteConstraintException ex) {
            return false;
        }
    }

    @Override
    public boolean deleteUser(String userId) {
        return false;
    }

    private void setContentValue(UserEntity userEntity) {
        initialValues = new ContentValues();
        initialValues.put(USERNAME, userEntity.getUsername());
        initialValues.put(PASSWORD, userEntity.getPassword());
//        initialValues.put(EMAIL, userEntity.getEmail());
//        initialValues.put(PHONE, userEntity.getPhone());
//        initialValues.put(GENDER, userEntity.getGender());
//        initialValues.put(DATE_OF_BIRTH, userEntity.getDateOfBirth());
//        initialValues.put(USER_ICON, userEntity.getUserIcon());
//        initialValues.put(AVATAR, userEntity.getAvatar());
//        initialValues.put(USER_DESCRIPTION, userEntity.getUserDescription());
    }

    private ContentValues getContentValue() {
        return initialValues;
    }
}
