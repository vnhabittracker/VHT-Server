package habit.tracker.habittracker;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import android.support.v7.app.AppCompatActivity;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import habit.tracker.habittracker.common.DayAxisValueFormatter;
import habit.tracker.habittracker.common.MyAxisValueFormatter;


public class ReportActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    @BindView(R.id.chart)
    BarChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);

//        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
//        anyChartView.setProgressBar(findViewById(R.id.progress_bar));
//
//        Cartesian cartesian = AnyChart.column();
//
//        cartesian.startSelectMarquee(true);
//        cartesian.selectMarqueeFill(new String[]{"#FFB6C1", "#F08080"});
//
//        List<DataEntry> data = new ArrayList<>();
//        data.add(new ValueDataEntry("T2", 1));
//        data.add(new ValueDataEntry("T3", 2));
//        data.add(new ValueDataEntry("T4", 1));
//        data.add(new ValueDataEntry("T5", 3));
//        data.add(new ValueDataEntry("T6", 2));
//        data.add(new ValueDataEntry("T7", 4));
//        data.add(new ValueDataEntry("CN", 1));
//
//        Column column = cartesian.column(data);
//        column.tooltip()
//                .titleFormat("")
//                .position(Position.CENTER_BOTTOM)
//                .anchor(Anchor.CENTER_BOTTOM)
//                .offsetX(0d)
//                .offsetY(5d)
//                .format("{%Value}");
//
//        cartesian.animation(true);
//        cartesian.title("");
//        cartesian.yScale().minimum(0d);
//        cartesian.yAxis(0).labels().format("{%Value}");
//        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
//        cartesian.interactivity().hoverMode(HoverMode.BY_X);
//        cartesian.xAxis(0).title("");
//        cartesian.yAxis(0).title("");
//        anyChartView.setChart(cartesian);

        initChart();
    }

    private void initChart() {
        chart.setOnChartValueSelectedListener(this);
        chart.setOnChartValueSelectedListener(this);
        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(false);
        chart.getDescription().setEnabled(false);
        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);
        // chart.setDrawYLabels(false);

        // if more than 7 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(7);

        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter();
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setTypeface(tfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(8);
        xAxis.setValueFormatter(xAxisFormatter);

        IAxisValueFormatter custom = new MyAxisValueFormatter();

        YAxis leftAxis = chart.getAxisLeft();
//        leftAxis.setTypeface(tfLight);
        leftAxis.setLabelCount(7, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

        Legend l = chart.getLegend();
        l.setEnabled(false);

        XYMarkerView mv = new XYMarkerView(this, xAxisFormatter);
        mv.setChartView(chart); // For bounds control
        chart.setMarker(mv); // Set the marker to the chart

        setData();
    }

    private void setData() {
        ArrayList<BarEntry> values = new ArrayList<>();

        values.add(new BarEntry(0, 10));
        values.add(new BarEntry(1, 15));
        values.add(new BarEntry(2, 12));
        values.add(new BarEntry(3, 9));
        values.add(new BarEntry(4, 5));
        values.add(new BarEntry(5, 12));
        values.add(new BarEntry(6, 13));

        BarDataSet set1;

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();

        } else {
            set1 = new BarDataSet(values, "The year 2017");
            set1.setDrawIcons(false);
            int startColor1 = ContextCompat.getColor(this, R.color.red1);
            int endColor1 = ContextCompat.getColor(this, R.color.red2);

            List<GradientColor> gradientColors2 = new ArrayList<>();
            gradientColors2.add(new com.github.mikephil.charting.model.GradientColor(startColor1, endColor1));

            set1.setGradientColors(gradientColors2);
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
//            data.setValueTypeface(tfLight);
            data.setBarWidth(0.6f);

            chart.setData(data);
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
