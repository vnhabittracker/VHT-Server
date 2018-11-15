package habit.tracker.habittracker.common.habitreminder;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import habit.tracker.habittracker.R;
import habit.tracker.habittracker.common.util.AppGenerator;

public class HabitReminderServiceReceiver extends BroadcastReceiver {

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

            if (endTime != null) {
                try {
                    SimpleDateFormat fm = new SimpleDateFormat(AppGenerator.YMD, Locale.getDefault());
                    Date endDate = fm.parse(endTime);
                    if (endDate.getTime() < System.currentTimeMillis()) {
                        Intent i = new Intent(context, HabitReminderServiceReceiver.class);
                        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, Integer.parseInt(remindId), i, 0);
                        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        if (alarmMgr != null) {
                            alarmMgr.cancel(alarmIntent);
                        }

                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_launcher_foreground)
                            .setContentTitle("VN Habit Tracker: " + habitName)
                            .setContentText(remindText);
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, builder.build());
        }
    }
}
