package habit.tracker.habittracker.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created on 10/15/2018
 */
public abstract class MyDatabaseHelper {
    public SQLiteDatabase mDb;

    public MyDatabaseHelper(){}

    public MyDatabaseHelper(SQLiteDatabase db) {
        this.mDb = db;
    }

    public long insert(String tableName, ContentValues values) {
        return mDb.insert(tableName, null, values);
    }

    public long replace(String tableName, ContentValues values) {
        return mDb.replace(tableName, null, values);
    }

    public abstract int delete(String id);

    protected abstract <T> T cursorToEntity(Cursor cursor);

    public Cursor query(String tableName, String[] columns, String selection, String[] selectionArgs, String sortOrder) {
        final Cursor cursor = mDb.query(tableName, columns, selection, selectionArgs, null, null, sortOrder);
        return cursor;
    }

    public int update(String tableName, ContentValues values, String selection, String[] selectionArgs) {
        return mDb.update(tableName, values, selection, selectionArgs);
    }

    public Cursor rawQuery(String sql, String[] selectionArgs) {
        return mDb.rawQuery(sql, selectionArgs);
    }

    public String getParams(String[] columns, String alias, boolean removeEnd) {
        String str = "";
        for (int i = 0; i < columns.length; i++) {
            str = str + alias + "." + columns[i] + ", ";
            if (removeEnd && i == columns.length - 1) {
                return str.substring(0, str.length() - 2);
            }
        }
        return str;
    }
}
