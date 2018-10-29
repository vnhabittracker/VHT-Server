package habit.tracker.habittracker.common.chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by philipp on 02/06/16.
 */
public class DayAxisValueFormatter implements IAxisValueFormatter {

    public DayAxisValueFormatter() {
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        String dayStr = "";
        int day = (int) value;
        switch (day) {
            case 0:
                dayStr = "Thứ 2";
                break;
            case 1:
                dayStr = "Thứ 3";
                break;
            case 2:
                dayStr = "Thứ 4";
                break;
            case 3:
                dayStr = "Thứ 5";
                break;
            case 4:
                dayStr = "Thứ 6";
                break;
            case 5:
                dayStr = "Thứ 7";
                break;
            case 6:
                dayStr = "CN";
                break;
        }
        return dayStr;
    }
}
