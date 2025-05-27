package myfitnesspal.items;

import java.time.LocalDate;
import java.util.Map;
import java.util.StringJoiner;

public final class BodyMeasurement implements Trackable {
    private final LocalDate date;
    private final BodyMeasurementMetric metric;
    private final String unit;
    private final Map<String, Double> values;

    public BodyMeasurement(LocalDate date,
                           BodyMeasurementMetric metric,
                           String unit,
                           Map<String, Double> values) {
        this.date = date;
        this.metric = metric;
        this.unit = unit;
        this.values = Map.copyOf(values);
    }

    public LocalDate date() {
        return date;
    }
    public BodyMeasurementMetric metric() {
        return metric;
    }
    public String unit() {
        return unit;
    }
    public Map<String, Double> values() {
        return values;
    }

    @Override
    public String toFileString() {
        StringJoiner stringJoiner = new StringJoiner(";");
        for (Map.Entry<String, Double> entry : values.entrySet()) {
            stringJoiner.add(entry.getKey() + "=" + entry.getValue());
        }
        return "BM;" + date + ";" + metric + ";" + unit + ";" + stringJoiner;
    }

    @Override
    public String toString() {
        return metric + " " + values + " " + date;
    }
}
