package habit.tracker.habittracker.repository.feedback;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import habit.tracker.habittracker.repository.MyDatabaseHelper;

public class FeedbackDaoImpl extends MyDatabaseHelper implements FeedbackDao, FeedbackSchema {
    private Cursor cursor;
    private ContentValues initialValues;

    public FeedbackDaoImpl(SQLiteDatabase db) {
        super(db);
    }

    @Override
    protected FeedbackEntity cursorToEntity(Cursor cursor) {
        FeedbackEntity entity = new FeedbackEntity();
        if (cursor.getColumnIndex(FEEDBACK_ID) != -1) {
            entity.setFeedbackId(cursor.getString(cursor.getColumnIndexOrThrow(FEEDBACK_ID)));
        }
        if (cursor.getColumnIndex(USER_ID) != -1) {
            entity.setUserId(cursor.getString(cursor.getColumnIndexOrThrow(USER_ID)));
        }
        if (cursor.getColumnIndex(STAR_NUM) != -1) {
            if (cursor.getString(cursor.getColumnIndexOrThrow(STAR_NUM)) != null) {
                entity.setStarNum(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(STAR_NUM))));
            }
        }
        if (cursor.getColumnIndex(FEEDBACK_DESCRIPTION) != -1) {
            entity.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(FEEDBACK_DESCRIPTION)));
        }
        if (cursor.getColumnIndex(IS_UPDATE) != -1) {
            entity.setUpdate(cursor.getString(cursor.getColumnIndexOrThrow(IS_UPDATE)).equals("1"));
        }
        return entity;
    }

    private ContentValues getContentValue() {
        return initialValues;
    }

    private void setContentValue(FeedbackEntity entity) {
        initialValues = new ContentValues();
        initialValues.put(FEEDBACK_ID, entity.getFeedbackId());
        initialValues.put(USER_ID, entity.getUserId());
        initialValues.put(STAR_NUM, entity.getStarNum());
        initialValues.put(FEEDBACK_DESCRIPTION, entity.getDescription());
        initialValues.put(IS_UPDATE, entity.isUpdate()?"1":"0");
    }

    @Override
    public int delete(String id) {
        try {
            final String selectionArgs[] = {id};
            final String selection = FEEDBACK_ID + " = ?";
            return super.mDb.delete(FEEDBACK_TABLE, selection, selectionArgs);
        } catch (SQLiteConstraintException ignored) {
        }
        return 0;
    }

    @Override
    public FeedbackEntity getFeedbackByUser(String feedbackId) {
        FeedbackEntity entity = null;
        final String selectionArgs[] = {feedbackId};
        final String selection = FEEDBACK_ID + " = ?";
        cursor = super.query(FEEDBACK_TABLE, FEEDBACK_COLUMNS, selection, selectionArgs, null);
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
    public boolean saveFeedback(FeedbackEntity entity) {
        setContentValue(entity);
        try {
            return super.replace(FEEDBACK_TABLE, getContentValue()) > 0;
        } catch (SQLiteConstraintException ignored) {
        }
        return false;
    }
}
