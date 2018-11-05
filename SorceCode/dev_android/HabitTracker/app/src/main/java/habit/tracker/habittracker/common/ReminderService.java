package habit.tracker.habittracker.common;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import habit.tracker.habittracker.common.receiver.ReminderReceiver;

public class ReminderService {
    Context context;
    Intent intent;
    AlarmManager alarmMgr;
    PendingIntent alarmIntent;

    public ReminderService(Context context) {
        this.context = context;
        intent = new Intent(context, ReminderReceiver.class);
    }

    public void remind(int requestCode, int dayOfWeek, int hour, int minute) {
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                7 * 24 * 60 * 60 * 1000, alarmIntent);
    }
}
