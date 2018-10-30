package habit.tracker.habittracker.common.chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

public class MyAxisValueFormatter implements IAxisValueFormatter {

    public MyAxisValueFormatter() {

    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return (int) value + "";
    }
}
