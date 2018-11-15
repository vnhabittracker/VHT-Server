package habit.tracker.habittracker.repository.habit;

import java.util.Calendar;

public class Schedule implements TrackingDateInWeek {
    private String mon;
    private String tue;
    private String wed;
    private String thu;
    private String fri;
    private String sat;
    private String sun;

    public Schedule(int year, int month, int date) {
        Calendar ca = Calendar.getInstance();
        ca.set(year, month - 1, date);
        int dayOfWeek = ca.get(Calendar.DAY_OF_WEEK);
        setMon(dayOfWeek == Calendar.MONDAY ? "1" : "0");
        setTue(dayOfWeek == Calendar.TUESDAY ? "1" : "0");
        setWed(dayOfWeek == Calendar.WEDNESDAY ? "1" : "0");
        setThu(dayOfWeek == Calendar.THURSDAY ? "1" : "0");
        setFri(dayOfWeek == Calendar.FRIDAY ? "1" : "0");
        setSat(dayOfWeek == Calendar.SATURDAY ? "1" : "0");
        setSun(dayOfWeek == Calendar.SUNDAY ? "1" : "0");
    }

    public void setMon(String mon) {
        this.mon = mon;
    }

    public void setTue(String tue) {
        this.tue = tue;
    }

    public void setWed(String wed) {
        this.wed = wed;
    }

    public void setThu(String thu) {
        this.thu = thu;
    }

    public void setFri(String fri) {
        this.fri = fri;
    }

    public void setSat(String sat) {
        this.sat = sat;
    }

    public void setSun(String sun) {
        this.sun = sun;
    }

    @Override
    public String getMon() {
        return mon;
    }

    @Override
    public String getTue() {
        return tue;
    }

    @Override
    public String getWed() {
        return wed;
    }

    @Override
    public String getThu() {
        return thu;
    }

    @Override
    public String getFri() {
        return fri;
    }

    @Override
    public String getSat() {
        return sat;
    }

    @Override
    public String getSun() {
        return sun;
    }
}
