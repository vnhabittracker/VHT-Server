package habit.tracker.habittracker.common.habitreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import habit.tracker.habittracker.api.model.reminder.Reminder;
import habit.tracker.habittracker.common.util.AppGenerator;

public class HabitReminderManager {
    public static final String REMIND_ID = "remind_id";
    public static final String REMIND_TEXT = "remind_text";
    public static final String HABIT_NAME = "habit_name";
    public static final String END_TIME = "end_time";

    private Context context;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private List<Reminder> remindersList;

    public HabitReminderManager(Context context) {
        this.context = context;
    }

    public HabitReminderManager(Context context, List<Reminder> remindAddNew) {
        this.context = context;
        this.remindersList = remindAddNew;
    }

    public void start() {
        String remindTime;
        int year;
        int month;
        int day;
        int hour;
        int minute;
        Date date;
        Calendar calendar = Calendar.getInstance();
        Reminder reminder;
        for (int i = 0; i < remindersList.size(); i++) {
            reminder = remindersList.get(i);
            remindTime = reminder.getReminderTime();
            date = AppGenerator.getDate(remindTime, AppGenerator.YMD2);
            calendar.setTime(date);

            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
            remind(reminder, year, month, day, hour, minute);
        }
    }

    private void remind(Reminder reminder, int year, int month, int day, int hour, int minute) {
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, HabitReminderServiceReceiver.class);
        intent.putExtra(REMIND_ID, reminder.getHabitId());
        intent.putExtra(REMIND_TEXT, reminder.getRemindText());
        intent.putExtra(HABIT_NAME, reminder.getHabitName());
        intent.putExtra(END_TIME, reminder.getEndDate());
        alarmIntent = PendingIntent.getBroadcast(context, Integer.parseInt(reminder.getReminderId()), intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        long startTime = calendar.getTimeInMillis();
        long interval = 0;

        switch (reminder.getRepeatType()) {
            case "0":
                interval = AppGenerator.MILLISECOND_IN_DAY;
                break;
            case "1":
                interval = AppGenerator.MILLISECOND_IN_DAY * 7;
                break;
            case "2":
                if (month < 11) {
                    calendar.set(Calendar.MONTH, month + 1);
                } else {
                    calendar.set(Calendar.YEAR + 1, 1);
                }
                interval = calendar.getTimeInMillis() - startTime;
                break;
            case "3":
                calendar.set(Calendar.YEAR, year + 1);
                interval = calendar.getTimeInMillis() - startTime;
                break;
        }

        if (interval > 0) {
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, startTime, interval, alarmIntent);
        } else {
            alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, startTime, alarmIntent);
        }
    }

    public void cancelReminder(int requestCode) {
        Intent i = new Intent(context, HabitReminderServiceReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, requestCode, i, 0);
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmMgr != null) {
            alarmMgr.cancel(alarmIntent);
        }
    }
}
