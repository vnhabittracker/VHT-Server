package habit.tracker.habittracker.common.util;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class AppGenerator {
    public static final long MILLISECOND_IN_DAY = 86400000;
    public static final long MILLISECOND_IN_WEEK = 86400000 * 7;

    // formats
    public static final String formatYMD = "yyyy-MM-dd HH:mm:ss";
    public static final String formatYMD2 = "yyyy-MM-dd";
    public static final String formatDMY = "dd-MM-yyyy HH:mm:ss";
    public static final String formatDMY2 = "dd/MM/yyyy";

    public static Date getDate(String date, String format) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCurrentDate(String format) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat fm = new SimpleDateFormat(format, Locale.getDefault());
        return fm.format(date);
    }

    public static String getNewId() {
        String id = UUID.randomUUID().toString();
        if (id.length() > 11) {
            return id.substring(0, 11);
        }
        return id;
    }

    public static String getDayPreWeek(String currentDate) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = dateFormat.parse(currentDate);
            Date oneDayBefore = new Date(date.getTime() - AppGenerator.MILLISECOND_IN_WEEK);
            return dateFormat.format(oneDayBefore);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDayNextWeek(String currentDate) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = dateFormat.parse(currentDate);
            Date oneDayBefore = new Date(date.getTime() + AppGenerator.MILLISECOND_IN_WEEK);
            return dateFormat.format(oneDayBefore);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getPreDate(String currentDate, String format) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
            Date date = dateFormat.parse(currentDate);
            Date oneDayBefore = new Date(date.getTime() - AppGenerator.MILLISECOND_IN_DAY);
            return dateFormat.format(oneDayBefore);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getNextDate(String currentDate, String format) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
            Date date = dateFormat.parse(currentDate);
            Date oneDayBefore = new Date(date.getTime() + AppGenerator.MILLISECOND_IN_DAY);
            return dateFormat.format(oneDayBefore);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getPreMonth(String currentDate) {
        String[] arrDate = currentDate.split("-");
        int month = Integer.parseInt(arrDate[1]) - 1;
        if (month < 1 || month > 12) {
            return currentDate;
        }
        return format(arrDate[0] + "-" + month + "-" + arrDate[2], formatYMD2, formatYMD2);
    }

    public static String getNextMonth(String currentDate) {
        String[] arrDate = currentDate.split("-");
        int month = Integer.parseInt(arrDate[1]) + 1;
        if (month < 1 || month > 12) {
            return currentDate;
        }
        return format(arrDate[0] + "-" + month + "-" + arrDate[2], formatYMD2, formatYMD2);
    }

    public static String getPreYear(String currentDate) {
        String[] arrDate = currentDate.split("-");
        int year = Integer.parseInt(arrDate[0]) - 1;
        if (year < 1970) {
            return currentDate;
        }
        return format(year + "-" + arrDate[1] + "-" + arrDate[2], formatYMD2, formatYMD2);
    }

    public static String getNextYear(String currentDate) {
        String[] arrDate = currentDate.split("-");
        int year = Integer.parseInt(arrDate[0]) + 1;
        if (year > 2020) {
            return currentDate;
        }
        return format(year + "-" + arrDate[1] + "-" + arrDate[2], formatYMD2, formatYMD2);
    }

    public static String getPreDate(String currentDate, boolean[] availDaysInWeek) {
        String preDate = null;
        while (preDate == null || !isValidTrackingDay(preDate, availDaysInWeek)) {
            preDate = getPreDate(currentDate, formatYMD2);
            currentDate = preDate;
        }
        return preDate;
    }

    public static boolean isValidTrackingDay(String day, boolean[] week) {
        if (TextUtils.isEmpty(day)) {
            return false;
        }

        Calendar calendar = Calendar.getInstance();
        try {
            SimpleDateFormat fm = new SimpleDateFormat(formatYMD2, Locale.getDefault());
            Date d = fm.parse(day);
            calendar.setTime(d);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        int diw = calendar.get(Calendar.DAY_OF_WEEK);
        switch (diw) {
            case Calendar.MONDAY:
                return week[0];
            case Calendar.TUESDAY:
                return week[1];
            case Calendar.WEDNESDAY:
                return week[2];
            case Calendar.THURSDAY:
                return week[3];
            case Calendar.FRIDAY:
                return week[4];
            case Calendar.SATURDAY:
                return week[5];
            case Calendar.SUNDAY:
                return week[6];
        }
        return false;
    }

    /**
     * @param month start from 0
     */
    public static int getMaxDayInMonth(int year, int month){
        Calendar ca = Calendar.getInstance();
        ca.set(year, month, 1);
        return ca.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static String[] getDatesInWeek(String currentDate) {
        String[] arrDate = currentDate.split("-");
        int year = Integer.parseInt(arrDate[0]);
        int month = Integer.parseInt(arrDate[1]);
        int date = Integer.parseInt(arrDate[2]);

        Calendar ca = Calendar.getInstance();
        ca.set(year, month - 1, date);
        date = ca.get(Calendar.DAY_OF_WEEK);

        int index = 0;
        String[] week = new String[7];
        switch (date) {
            case Calendar.MONDAY:
                index = 0;
                break;
            case Calendar.TUESDAY:
                index = 1;
                break;
            case Calendar.WEDNESDAY:
                index = 2;
                break;
            case Calendar.THURSDAY:
                index = 3;
                break;
            case Calendar.FRIDAY:
                index = 4;
                break;
            case Calendar.SATURDAY:
                index = 5;
                break;
            case Calendar.SUNDAY:
                index = 6;
                break;
            default:
                break;
        }
        week[index] = currentDate;
        for (int i = index - 1; i >= 0; i--) {
            week[i] = getPreDate(currentDate, AppGenerator.formatYMD2);
            currentDate = week[i];
        }
        currentDate = week[index];
        for (int i = index + 1; i < 7; i++) {
            week[i] = getNextDate(currentDate, AppGenerator.formatYMD2);
            currentDate = week[i];
        }
        return week;
    }

    public static String[] getDatesInMonth(String currentDate, boolean limit) {
        String[] arr = currentDate.split("-");
        int year = Integer.parseInt(arr[0]);
        int month = Integer.parseInt(arr[1]);
        int date = Integer.parseInt(arr[2]);
        Calendar ca = Calendar.getInstance();
        ca.set(year, month - 1, date);

        int numberOfDays;
        String[] daysInMonth;

        if (!limit) {
            numberOfDays = ca.getActualMaximum(Calendar.DAY_OF_MONTH);
            daysInMonth = new String[numberOfDays];
            daysInMonth[date - 1] = currentDate;
            for (int i = date - 2; i >= 0; i--) {
                daysInMonth[i] = getPreDate(currentDate, AppGenerator.formatYMD2);
                currentDate = daysInMonth[i];
            }
            currentDate = daysInMonth[date - 1];
        } else {
            numberOfDays = ca.getActualMaximum(Calendar.DAY_OF_MONTH) - date;
            daysInMonth = new String[numberOfDays];
            daysInMonth[0] = currentDate;
            date = 1;
        }

        for (int i = date; i < numberOfDays; i++) {
            daysInMonth[i] = getNextDate(currentDate, AppGenerator.formatYMD2);
            currentDate = daysInMonth[i];
        }
        return daysInMonth;
    }

    public static String format(String dateTime, String fm1, String fm2) {
        try {
            SimpleDateFormat fm = new SimpleDateFormat(fm1, Locale.getDefault());
            Date d = fm.parse(dateTime);
            fm = new SimpleDateFormat(fm2, Locale.getDefault());
            return fm.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int countDayBetween(String d1, String d2) {
        Date date1 = getDate(d1, formatYMD2);
        Date date2 = getDate(d2, formatYMD2);
        long off = date2.getTime() - date1.getTime();
        return (int) (off / MILLISECOND_IN_DAY);
    }
}
