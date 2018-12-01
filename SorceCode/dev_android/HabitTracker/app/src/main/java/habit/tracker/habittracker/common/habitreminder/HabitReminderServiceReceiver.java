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

import java.util.Date;

import habit.tracker.habittracker.R;
import habit.tracker.habittracker.common.util.AppGenerator;

public class HabitReminderServiceReceiver extends BroadcastReceiver {

    NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String remindId = extras.getString(HabitReminderManager.REMIND_ID);
            String remindText = extras.getString(HabitReminderManager.REMIND_TEXT);
            String remindTitle = extras.getString(HabitReminderManager.REMIND_TITLE);
            String endTime = extras.getString(HabitReminderManager.END_TIME);

            if (remindId == null || remindText == null || remindTitle == null) {
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

            if (notificationManager == null) {
                notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            }

            String defaultId = "habittracker";
            Notification notification = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                NotificationChannel mChannel = notificationManager.getNotificationChannel(defaultId);
                if (mChannel == null) {
                    mChannel = new NotificationChannel(defaultId, remindTitle, NotificationManager.IMPORTANCE_HIGH);
//                    mChannel.enableVibration(true);
//                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    notificationManager.createNotificationChannel(mChannel);
                }

                notification = new NotificationCompat.Builder(context, defaultId)
                                .setSmallIcon(R.drawable.app_launcher)
                                .setContentTitle("VN Habit Tracker: " + remindTitle)
                                .setContentText(remindText).build();
            } else {
                notification = new NotificationCompat.Builder(context, defaultId)
                        .setSmallIcon(R.drawable.app_launcher)
                        .setContentTitle("VN Habit Tracker: " + remindTitle)
                        .setContentText(remindText).build();
            }

            notificationManager.notify(0, notification);
        }
    }
}
