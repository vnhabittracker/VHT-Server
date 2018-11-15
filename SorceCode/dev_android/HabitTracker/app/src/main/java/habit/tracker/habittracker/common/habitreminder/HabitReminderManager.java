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
    Calendar caStart = Calendar.getInstance();

    public HabitReminderManager(Context context) {
        this.context = context;
    }

    public HabitReminderManager(Context context, List<Reminder> remindAddNew) {
        this.context = context;
        this.remindersList = remindAddNew;
    }

    public void start() {
        String time;
        int year;
        int month;
        int day;
        int hour;
        int minute;
        RepeatType repeatType = RepeatType.DAILY;

        SimpleDateFormat fm = new SimpleDateFormat(AppGenerator.YMD, Locale.getDefault());
        Date date;
        Calendar ca = Calendar.getInstance();
        Reminder reminder;

        for (int i = 0; i < remindersList.size(); i++) {
            try {
                reminder = remindersList.get(i);
                time = reminder.getReminderTime();
                date = fm.parse(time);
                ca.setTimeInMillis(date.getTime());

                year = ca.get(Calendar.YEAR);
                month = ca.get(Calendar.MONTH);
                day = ca.get(Calendar.DAY_OF_MONTH);
                hour = ca.get(Calendar.HOUR_OF_DAY);
                minute = ca.get(Calendar.MINUTE);

                switch (reminder.getRepeatType()) {
                    case "0":
                        repeatType = RepeatType.DAILY;
                        break;
                    case "1":
                        repeatType = RepeatType.WEEKLY;
                        break;
                    case "2":
                        repeatType = RepeatType.MONTHLY;
                        break;
                    case "3":
                        repeatType = RepeatType.YEARLY;
                        break;
                }

                remind(reminder, repeatType, year, month, day, hour, minute);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void remind(Reminder reminder, RepeatType repeatType, int year, int month, int day, int hour, int minute) {
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, HabitReminderServiceReceiver.class);
        intent.putExtra(REMIND_ID, reminder.getHabitId());
        intent.putExtra(REMIND_TEXT, reminder.getRemindText());
        intent.putExtra(HABIT_NAME, reminder.getHabitName());
        intent.putExtra(END_TIME, reminder.getEndDate());

        alarmIntent = PendingIntent.getBroadcast(context, Integer.parseInt(reminder.getReminderId()), intent, 0);

        caStart.set(Calendar.YEAR, year);
        caStart.set(Calendar.MONTH, month);
        caStart.set(Calendar.DAY_OF_MONTH, day);
        caStart.set(Calendar.HOUR_OF_DAY, hour);
        caStart.set(Calendar.MINUTE, minute);
        long startTime = caStart.getTimeInMillis();

        long interval = 0;

        switch (repeatType) {
            case DAILY:
                interval = AppGenerator.MILLISECOND_IN_DAY;
                break;
            case WEEKLY:
                interval = AppGenerator.MILLISECOND_IN_DAY * 7;
                break;
            case MONTHLY:
                if (month < 11) {
                    caStart.set(Calendar.MONTH, month + 1);
                } else {
                    caStart.set(Calendar.YEAR + 1, 1);
                }
                interval = caStart.getTimeInMillis() - System.currentTimeMillis();
                break;
            case YEARLY:
                caStart.set(Calendar.YEAR, year + 1);
                interval = caStart.getTimeInMillis() - System.currentTimeMillis();
                break;
        }

        if (interval > 0) {
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, startTime,
                    interval, alarmIntent);
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
