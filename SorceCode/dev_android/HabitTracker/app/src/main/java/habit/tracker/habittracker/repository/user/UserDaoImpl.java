package habit.tracker.habittracker.repository.user;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import habit.tracker.habittracker.api.model.user.User;
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
                userEntity.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(EMAIL)));
            }
            if (cursor.getColumnIndex(GENDER) != -1) {
                userEntity.setGender(cursor.getString(cursor.getColumnIndexOrThrow(GENDER)));
            }
            if (cursor.getColumnIndex(DATE_OF_BIRTH) != -1) {
                userEntity.setDateOfBirth(cursor.getString(cursor.getColumnIndexOrThrow(DATE_OF_BIRTH)));
            }
            if (cursor.getColumnIndex(REAL_NAME) != -1) {
                userEntity.setRealName(cursor.getString(cursor.getColumnIndexOrThrow(REAL_NAME)));
            }
            if (cursor.getColumnIndex(AVATAR) != -1) {
                userEntity.setAvatar(cursor.getString(cursor.getColumnIndexOrThrow(AVATAR)));
            }
            if (cursor.getColumnIndex(USER_DESCRIPTION) != -1) {
                userEntity.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(USER_DESCRIPTION)));
            }
            if (cursor.getColumnIndex(USER_CREATED_DATE) != -1) {
                userEntity.setCreatedDate(cursor.getString(cursor.getColumnIndexOrThrow(USER_CREATED_DATE)));
            }
            if (cursor.getColumnIndex(LAST_LOGIN_TIME) != -1) {
                userEntity.setLastLoginTime(cursor.getString(cursor.getColumnIndexOrThrow(LAST_LOGIN_TIME)));
            }
            if (cursor.getColumnIndex(CONTINUE_USING_COUNT) != -1) {
                userEntity.setContinueUsingCount(cursor.getString(cursor.getColumnIndexOrThrow(CONTINUE_USING_COUNT)));
            }
            if (cursor.getColumnIndex(CURRENT_CONTINUE_USING_COUNT) != -1) {
                userEntity.setCurrentContinueUsingCount(cursor.getString(cursor.getColumnIndexOrThrow(CURRENT_CONTINUE_USING_COUNT)));
            }
            if (cursor.getColumnIndex(BEST_CONTINUE_USING_COUNT) != -1) {
                userEntity.setBestContinueUsingCount(cursor.getString(cursor.getColumnIndexOrThrow(BEST_CONTINUE_USING_COUNT)));
            }
            if (cursor.getColumnIndex(USER_SCORE) != -1) {
                userEntity.setUserScore(cursor.getString(cursor.getColumnIndexOrThrow(USER_SCORE)));
            }
        }
        return userEntity;
    }

    private void setContentValue(UserEntity userEntity) {
        initialValues = new ContentValues();
        initialValues.put(USER_ID, userEntity.getUserId());
        initialValues.put(USERNAME, userEntity.getUsername());
        initialValues.put(PASSWORD, userEntity.getPassword());
        initialValues.put(EMAIL, userEntity.getEmail());
        initialValues.put(GENDER, userEntity.getGender());
        initialValues.put(DATE_OF_BIRTH, userEntity.getDateOfBirth());
        initialValues.put(REAL_NAME, userEntity.getRealName());
        initialValues.put(AVATAR, userEntity.getAvatar());
        initialValues.put(USER_DESCRIPTION, userEntity.getDescription());
        initialValues.put(USER_CREATED_DATE, userEntity.getCreatedDate());
        initialValues.put(LAST_LOGIN_TIME, userEntity.getLastLoginTime());
        initialValues.put(CONTINUE_USING_COUNT, userEntity.getContinueUsingCount());
        initialValues.put(CURRENT_CONTINUE_USING_COUNT, userEntity.getCurrentContinueUsingCount());
        initialValues.put(BEST_CONTINUE_USING_COUNT, userEntity.getBestContinueUsingCount());
        initialValues.put(USER_SCORE, userEntity.getUserScore());
    }

    private ContentValues getContentValue() {
        return initialValues;
    }

    public UserEntity convert(User user) {
        if (user != null) {
            UserEntity entity = new UserEntity();
            entity.setUserId(user.getUserId());
            entity.setUsername(user.getUsername());
            entity.setPassword(user.getPassword());
            entity.setGender(user.getGender());
            entity.setEmail(user.getEmail());
            entity.setDateOfBirth(user.getDateOfBirth());
            entity.setAvatar(user.getAvatar());
            entity.setRealName(user.getRealName());
            entity.setDescription(user.getDescription());
            entity.setCreatedDate(user.getCreatedDate());
            entity.setLastLoginTime(user.getLastLoginTime());
            entity.setContinueUsingCount(user.getContinueUsingCount());
            entity.setCurrentContinueUsingCount(user.getCurrentContinueUsingCount());
            entity.setBestContinueUsingCount(user.getBestContinueUsingCount());
            entity.setUserScore(user.getUserScore());
            return entity;
        }
        return null;
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
    public UserEntity getUser(String userId) {
        final String selectionArgs[] = {String.valueOf(userId)};
        final String selection = USER_ID + " = ?";
        UserEntity userEntity = new UserEntity();
        cursor = super.query(USER_TABLE, USER_COLUMNS, selection, selectionArgs, USER_ID);
        if (cursor != null && cursor.getCount() > 0) {
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
}
