package habit.tracker.habittracker.adapter.calendar;

public class TrackingCalendarItem {
    private String text;
    private String date;
    private boolean isFilled;
    private boolean outOfRange;
    private boolean isHeader = false;

    public TrackingCalendarItem(String text, String date, boolean isFilled, boolean outOfRange) {
        this.text = text;
        this.date = date;
        this.isFilled = isFilled;
        this.outOfRange = outOfRange;
    }

    public TrackingCalendarItem(String text, String date, boolean isFilled, boolean outOfRange, boolean isHeader) {
        this.text = text;
        this.date = date;
        this.isFilled = isFilled;
        this.outOfRange = outOfRange;
        this.isHeader = isHeader;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }

    public boolean isOutOfRange() {
        return outOfRange;
    }

    public boolean isFilled() {
        return isFilled;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setFilled(boolean filled) {
        isFilled = filled;
    }

    public void setOutOfRange(boolean outOfRange) {
        this.outOfRange = outOfRange;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }
}
