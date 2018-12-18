package habit.tracker.habittracker.common.util;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.text.TextUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

public class AppGenerator {
    public static final long MILLISECOND_IN_MINUTE = 1000 * 60;
    public static final long MILLISECOND_IN_DAY = 86400000;
    public static final long MILLISECOND_IN_WEEK = 86400000 * 7;

    // formats
    public static final String YMD = "yyyy-MM-dd HH:mm:ss";
    public static final String YMD2 = "yyyy-MM-dd HH:mm";
    public static final String YMD_SHORT = "yyyy-MM-dd";
    public static final String DMY = "dd-MM-yyyy HH:mm:ss";
    public static final String DMY2 = "dd-MM-yyyy HH:mm";
    public static final String DMY_SHORT = "dd/MM/yyyy";

    public static Date getDate(String date, String format) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param year
     * @param month: 1-12
     * @param date
     * @param fm
     * @return
     */
    public static String getDate(int year, int month, int date, String fm) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, date);
        Date d = new Date(calendar.getTimeInMillis());
        DateFormat dateFormat = new SimpleDateFormat(fm, Locale.getDefault());
        return dateFormat.format(d);
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

    public static String getNewPassword() {
        String id = UUID.randomUUID().toString();
        if (id.length() > 6) {
            return id.substring(0, 6);
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
        int year = Integer.parseInt(arrDate[0]);
        int month = Integer.parseInt(arrDate[1]) - 1;
        if (month < 1) {
            year--;
            return format(year + "-12-01", YMD_SHORT, YMD_SHORT);
        }
        return format(year + "-" + month + "-01", YMD_SHORT, YMD_SHORT);
    }

    public static String getNextMonth(String currentDate) {
        String[] arrDate = currentDate.split("-");
        int year = Integer.parseInt(arrDate[0]);
        int month = Integer.parseInt(arrDate[1]) + 1;
        if (month > 12) {
            year++;
            return format(year + "-01-01", YMD_SHORT, YMD_SHORT);
        }
        return format(arrDate[0] + "-" + month + "-01", YMD_SHORT, YMD_SHORT);
    }

    public static String getPreYear(String currentDate) {
        String[] arrDate = currentDate.split("-");
        int year = Integer.parseInt(arrDate[0]) - 1;
        if (year < 1970) {
            return currentDate;
        }
        return format(year + "-01-01", YMD_SHORT, YMD_SHORT);
    }

    public static String getNextYear(String currentDate) {
        String[] arrDate = currentDate.split("-");
        int year = Integer.parseInt(arrDate[0]) + 1;
        if (year > 9999) {
            return currentDate;
        }
        return format(year + "-01-01", YMD_SHORT, YMD_SHORT);
    }

    public static String getPreDate(String currentDate, boolean[] availDaysInWeek) {
        String preDate = null;
        while (preDate == null || !isValidTrackingDay(preDate, availDaysInWeek)) {
            preDate = getPreDate(currentDate, YMD_SHORT);
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
            SimpleDateFormat fm = new SimpleDateFormat(YMD_SHORT, Locale.getDefault());
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
    public static int getMaxDayInMonth(int year, int month) {
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
            week[i] = getPreDate(currentDate, AppGenerator.YMD_SHORT);
            currentDate = week[i];
        }
        currentDate = week[index];
        for (int i = index + 1; i < 7; i++) {
            week[i] = getNextDate(currentDate, AppGenerator.YMD_SHORT);
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
                daysInMonth[i] = getPreDate(currentDate, AppGenerator.YMD_SHORT);
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
            daysInMonth[i] = getNextDate(currentDate, AppGenerator.YMD_SHORT);
            currentDate = daysInMonth[i];
        }
        return daysInMonth;
    }

    public static String format(String dateTime, String fmIn, String fmOut) {
        try {
            SimpleDateFormat fm = new SimpleDateFormat(fmIn, Locale.getDefault());
            Date d = fm.parse(dateTime);
            fm = new SimpleDateFormat(fmOut, Locale.getDefault());
            return fm.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int countDayBetween(String d1, String d2) {
        Date date1 = getDate(d1, YMD_SHORT);
        Date date2 = getDate(d2, YMD_SHORT);
        long offset = date2.getTime() - date1.getTime();
        return (int) (offset / MILLISECOND_IN_DAY);
    }

    public static String getSearchKey(String str) {
        if (TextUtils.isEmpty(str))
            return null;
        String temp = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replace('đ','d').replace('Đ','D').toLowerCase();
    }

    public static String getFirstDateNextWeek(String currentDate, String fmIn, String fmOut) {
        Calendar ca = Calendar.getInstance();
        String next;
        for (int i = 0; i < 7; i++) {
            next = getNextDate(currentDate, fmIn);
            ca.setTime(getDate(next, fmIn));
            if (ca.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                return next;
            }
            currentDate = next;
        }
        return null;
    }

    public static String getFirstDateNextMonth(String currentDate, String fmIn, String fmOut) {
        Date date = getDate(currentDate, fmIn);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        return getDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), fmOut);
    }

    public static String getFirstDateInMonth(String currentDate, String fmIn, String fmOut) {
        Date date = getDate(currentDate, fmIn);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return getDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), fmOut);
    }

    public static String getFirstDatePreMonth(String currentDate, String fmIn, String fmOut) {
        Date date = getDate(currentDate, fmIn);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
        return getDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), fmOut);
    }

    public static String getLastDateInMonth(String currentDate, String fmIn, String fmOut) {
        Date date = getDate(currentDate, fmIn);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.getActualMaximum(Calendar.DAY_OF_WEEK), fmOut);
    }

    public static String getLastDateInYear(String currentDate, String fmIn, String fmOut) {
        Date date = getDate(currentDate, fmIn);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, 12);
        return getDate(calendar.get(Calendar.YEAR), 12, calendar.getActualMaximum(Calendar.DAY_OF_WEEK), fmOut);
    }

    public static String getFirstDateNextYear(String currentDate, String fmIn, String fmOut) {
        Date date = getDate(currentDate, fmIn);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getDate(calendar.get(Calendar.YEAR) + 1, 1, 1, fmOut);
    }

    public static int getLevel(int score) {
        if (score < 10) {
            return 1;
        }
        if (score < 20) {
            return 2;
        }
        if (score < 50){
            return 3;
        }
        if (score < 120){
            return 4;
        }
        if (score < 290){
            return 5;
        }
        if (score < 700){
            return 6;
        }
        if (score < 1690){
            return 7;
        }
        if (score < 4080){
            return 8;
        }
        if (score <9850){
            return 9;
        }
        if (score < 23780){
            return 10;
        }
        return 11;
    }
}
