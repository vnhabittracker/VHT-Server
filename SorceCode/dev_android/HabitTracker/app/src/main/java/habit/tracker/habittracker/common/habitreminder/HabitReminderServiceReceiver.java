package habit.tracker.habittracker.common.habitreminder;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import habit.tracker.habittracker.R;
import habit.tracker.habittracker.common.util.AppGenerator;

public class HabitReminderServiceReceiver extends BroadcastReceiver {

    NotificationManager notifManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();

        if (extras != null) {
            String remindId = extras.getString(HabitReminderManager.REMIND_ID);
            String remindText = extras.getString(HabitReminderManager.REMIND_TEXT);
            String habitName = extras.getString(HabitReminderManager.HABIT_NAME);
            String endTime = extras.getString(HabitReminderManager.END_TIME);

            if (remindId == null || remindText == null || habitName == null) {
                return;
            }

            if (!TextUtils.isEmpty(endTime)) {
                Date endDate = AppGenerator.getDate(endTime, AppGenerator.YMD);
                if (endDate != null && endDate.compareTo(new Date()) < 0) {
                    Intent i = new Intent(context, HabitReminderServiceReceiver.class);
                    PendingIntent alarmIntent = PendingIntent.getBroadcast(context, Integer.parseInt(remindId), i, 0);
                    AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    if (alarmMgr != null) {
                        alarmMgr.cancel(alarmIntent);
                    }

                    return;
                }
            }

            if (notifManager == null) {
                notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            }

            String defaultId = "habittracker";
            Notification notification = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                NotificationChannel mChannel = notifManager.getNotificationChannel(defaultId);
                if (mChannel == null) {
                    mChannel = new NotificationChannel(defaultId, habitName, NotificationManager.IMPORTANCE_HIGH);
//                    mChannel.enableVibration(true);
//                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    notifManager.createNotificationChannel(mChannel);
                }

                notification = new NotificationCompat.Builder(context, defaultId)
                                .setSmallIcon(R.drawable.ic_launcher_foreground)
                                .setContentTitle("VN Habit Tracker: " + habitName)
                                .setContentText(remindText).build();
            } else {
                notification = new NotificationCompat.Builder(context, defaultId)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("VN Habit Tracker: " + habitName)
                        .setContentText(remindText).build();
            }

            notifManager.notify(0, notification);
        }
    }
}
