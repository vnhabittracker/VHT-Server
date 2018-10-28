package habit.tracker.habittracker.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class Generator {
    public static final long MILLISECOND_IN_DAY = 86400000;

    public static String getNewId() {
        String id = UUID.randomUUID().toString();
        if (id.length() > 11) {
            return id.substring(0, 11);
        }
        return id;
    }

    public static String getPreDate(String currentDate) {
        try {
            Calendar ca = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = dateFormat.parse(currentDate);
            Date oneDayBefore = new Date(date.getTime() - Generator.MILLISECOND_IN_DAY);
            return dateFormat.format(oneDayBefore);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getNextDate(String currentDate) {
        try {
            Calendar ca = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = dateFormat.parse(currentDate);
            Date oneDayBefore = new Date(date.getTime() + Generator.MILLISECOND_IN_DAY);
            return dateFormat.format(oneDayBefore);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
