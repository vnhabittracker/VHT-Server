package habit.tracker.habittracker.common.pushservice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class PushDataService {
    private static final int START_TIME = 0;
    private static final int REQUEST_CODE = 0;
    public static final String USER_ID = "user_id";
    private Context context;
    private String userId;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    public PushDataService(Context context, String userId) {
        this.context = context;
        this.userId = userId;
    }

    public void start() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, START_TIME);
        calendar.set(Calendar.MINUTE, 1);
        Intent intent = new Intent(context, PushDataReceiver.class);
        intent.putExtra(USER_ID, userId);
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
    }
}
