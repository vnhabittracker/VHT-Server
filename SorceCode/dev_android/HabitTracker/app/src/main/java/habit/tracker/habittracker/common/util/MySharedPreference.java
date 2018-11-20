package habit.tracker.habittracker.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import static android.content.Context.MODE_PRIVATE;

public class MySharedPreference {
    public final static  String MY_PREFS = "vnhabit_pref";
    public static final String USER_ID = "user_id";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    public static void saveUser(Context context, String userId, String username, String password){
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS, MODE_PRIVATE).edit();
        editor.putString(USER_ID, userId);
        editor.putString(USERNAME, username);
        editor.putString(PASSWORD, password);
        editor.apply();
    }

    public static String getUserId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        String userId = prefs.getString(USER_ID, null);
        if (!TextUtils.isEmpty(userId)) {
            return userId;
        }
        return null;
    }

    public static String[] getUser(Context context) {
        String[] user = new String[3];
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        user[0] = prefs.getString(USER_ID, null);
        user[1] = prefs.getString(USERNAME, null);
        user[2] = prefs.getString(PASSWORD, null);
        return user;
    }

    public static void save(Context context, String key, String tail, String val) {
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS, MODE_PRIVATE).edit();
        editor.putString(key + tail, val);
        editor.apply();
    }

    public static String get(Context context, String key, String tail) {
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        String val = prefs.getString(key + tail, null);
        if (!TextUtils.isEmpty(val)) {
            return val;
        }
        return null;
    }
}
