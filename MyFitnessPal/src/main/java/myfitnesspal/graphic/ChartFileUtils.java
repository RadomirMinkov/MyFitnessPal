package myfitnesspal.graphic;

import myfitnesspal.items.BodyMeasurement;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartUtils;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.File;
import java.io.IOException;
import java.util.List;

public final class ChartFileUtils {
    public ChartFileUtils() {

    }

    public File generateLineChart(List<BodyMeasurement> data,
                                         String title,
                                         String unit) {
        XYSeries s = new XYSeries(title, false);
        int idx = 0;
        for (BodyMeasurement bodyMeasurement : data) {
            double currentValue =
                    bodyMeasurement.values().values().iterator().next();
            s.add(idx++, currentValue);
        }
        XYSeriesCollection set = new XYSeriesCollection(s);
        JFreeChart chart = ChartFactory.createXYLineChart(
                title, "index", unit, set);
        File file = new File(title.replace(' ', '_')
                + "_" + System.currentTimeMillis() + ".png");
        try {
            ChartUtils.saveChartAsPNG(file, chart, 640, 480);
        }   catch (IOException ignored) { }
        return file;
    }
}
