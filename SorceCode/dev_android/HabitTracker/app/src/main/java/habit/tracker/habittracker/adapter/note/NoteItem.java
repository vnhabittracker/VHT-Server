package habit.tracker.habittracker.adapter.note;

public class NoteItem {
    private String trackId;
    private String date;
    private String note;
    private String defDate;

    public NoteItem(String trackId, String defDate, String date, String note) {
        this.trackId = trackId;
        this.defDate = defDate;
        this.date = date;
        this.note = note;
    }

    public String getTrackId() {
        return trackId;
    }

    public String getDefDate() {
        return defDate;
    }

    public String getDate() {
        return date;
    }

    public String getNote() {
        return note;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public void setDefDate(String defDate) {
        this.defDate = defDate;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
