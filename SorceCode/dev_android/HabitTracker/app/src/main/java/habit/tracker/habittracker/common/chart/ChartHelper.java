package habit.tracker.habittracker.common.chart;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.model.GradientColor;

import java.util.ArrayList;
import java.util.List;

import habit.tracker.habittracker.R;

public class ChartHelper implements OnChartValueSelectedListener {

    Context context;
    BarChart chart;

    private int mode = 0;
    public static final int MODE_WEEK = 0;
    public static final int MODE_MONTH = 1;
    public static final int MODE_YEAR = 2;

    public ChartHelper(Context context, BarChart chart) {
        this.context = context;
        this.chart = chart;
    }

    public void initChart() {
        chart.setOnChartValueSelectedListener(this);

        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);

        chart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setDrawGridBackground(false);
        // chart.setDrawYLabels(false);

        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter();

        XAxis xAxis = chart.getXAxis();
        xAxis.setLabelCount(10);
        xAxis.setValueFormatter(xAxisFormatter);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day

        IAxisValueFormatter custom = new MyAxisValueFormatter();

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setLabelCount(5, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

        Legend l = chart.getLegend();
        l.setEnabled(false);

        XYMarkerView mv = new XYMarkerView(context, xAxisFormatter);
        mv.setChartView(chart); // For bounds control
        chart.setMarker(mv); // Set the marker to the chart
    }

    public void setData(ArrayList<BarEntry> values) {
        BarDataSet set1;
        int startColor1 = ContextCompat.getColor(context, R.color.red1);
        int endColor1 = ContextCompat.getColor(context, R.color.red2);
        switch (mode) {
            case MODE_WEEK:
                startColor1 = ContextCompat.getColor(context, R.color.red1);
                endColor1 = ContextCompat.getColor(context, R.color.red2);
                break;
            case MODE_MONTH:
                startColor1 = ContextCompat.getColor(context, R.color.purple1);
                endColor1 = ContextCompat.getColor(context, R.color.purple2);
                break;
            case MODE_YEAR:
                startColor1 = ContextCompat.getColor(context, R.color.blue1);
                endColor1 = ContextCompat.getColor(context, R.color.blue2);
                break;
            default:
                break;
        }

        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mode);
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(xAxisFormatter);

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);

            List<GradientColor> gradientColors = new ArrayList<>();
            gradientColors.add(new com.github.mikephil.charting.model.GradientColor(startColor1, endColor1));

            set1.setGradientColors(gradientColors);

            set1.setValues(values);
            chart.getData().setDrawValues(false);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
            chart.animateY(500);

        } else {
            set1 = new BarDataSet(values, "");
            set1.setDrawIcons(false);

            List<GradientColor> gradientColors = new ArrayList<>();
            gradientColors.add(new com.github.mikephil.charting.model.GradientColor(startColor1, endColor1));

            set1.setGradientColors(gradientColors);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setDrawValues(false);
            data.setValueTextSize(7f);
            data.setBarWidth(0.5f);

            chart.setData(data);
            chart.animateY(500);
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
