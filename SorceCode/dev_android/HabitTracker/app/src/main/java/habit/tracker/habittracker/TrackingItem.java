package habit.tracker.habittracker;

public class TrackingItem {
    private String trackId;
    private String habitId;
    private String name;
    private String description;
    private String target;
    private int habitType;
    private String habitTypeName;
    private int monitorType;
    private String group;
    private String number;
    private int count;
    private String unit;
    private String color;
    private float comp = 0f;

    public TrackingItem() {}

    public TrackingItem(String trackId, String habitId, String name, String description, String habitType, int monitorType, String number, int count, String unit, String color) {
        this.trackId = trackId;
        this.habitId = habitId;
        this.name = name;
        this.description = description;
        this.habitType = Integer.parseInt(habitType);
        switch (this.habitType) {
            case 0:
                this.habitTypeName = "hôm nay";
                break;
            case 1:
                this.habitTypeName = "tuần này";
                break;
            case 2:
                this.habitTypeName = "tháng này";
                break;
            case 3:
                this.habitTypeName = "năm nay";
                break;
        }
        this.monitorType = monitorType;
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

    public String getHabitTypeName() {
        return habitTypeName;
    }

    public int getMonitorType() {
        return monitorType;
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

    public String getTarget() {
        return target;
    }

    public int getHabitType() {
        return habitType;
    }

    public String getGroup() {
        return group;
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

    public void setHabitTypeName(String habitTypeName) {
        this.habitTypeName = habitTypeName;
    }

    public void setMonitorType(int monitorType) {
        this.monitorType = monitorType;
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

    public void setRatio(float comp) {
        this.comp = comp;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setHabitType(int habitType) {
        this.habitType = habitType;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
