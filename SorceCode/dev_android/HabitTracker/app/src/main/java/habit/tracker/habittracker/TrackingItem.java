package habit.tracker.habittracker;

public class TrackingItem {
    private String trackId;
    private String habitId;
    private String name;
    private String description;
    private String habitType;
    private int type;
    private String number;
    private int count;
    private String unit;
    private String color;
    private float comp = 0f;

    public TrackingItem() {}

    public TrackingItem(String trackId, String habitId, String name, String description, String habitType, int type, String number, int count, String unit, String color) {
        this.trackId = trackId;
        this.habitId = habitId;
        this.name = name;
        this.description = description;
        switch (Integer.parseInt(habitType)) {
            case 0:
                this.habitType = "hôm nay";
                break;
            case 1:
                this.habitType = "tuần này";
                break;
            case 2:
                this.habitType = "tháng này";
                break;
            case 3:
                this.habitType = "năm nay";
                break;
        }
        this.type = type;
        this.number = number;
        this.count = count;
        this.unit = unit;
        this.color = color;
    }

    public String getTrackId() {
        return trackId;
    }

    public String getHabitId() {
        return habitId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getHabitType() {
        return habitType;
    }

    public int getType() {
        return type;
    }

    public String getNumber() {
        return number;
    }

    public int getCount() {
        return count;
    }

    public String getUnit() {
        return unit;
    }

    public String getColor() {
        return color;
    }

    public float getComp() {
        return comp;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public void setHabitId(String habitId) {
        this.habitId = habitId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHabitType(String habitType) {
        this.habitType = habitType;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setComp(float comp) {
        this.comp = comp;
    }
}
