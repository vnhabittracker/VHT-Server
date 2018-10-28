package habit.tracker.habittracker.api.model.tracking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TrackingList {
    @SerializedName("data")
    @Expose
    List<Tracking> trackingList = new ArrayList<>();

    public List<Tracking> getTrackingList() {
        return trackingList;
    }

    public void setTrackingList(List<Tracking> trackingList) {
        this.trackingList = trackingList;
    }
}
