package myfitnesspal.parser;

import myfitnesspal.items.BodyMeasurement;
import myfitnesspal.items.BodyMeasurementMetric;
import myfitnesspal.items.Trackable;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

final class BodyMeasurementLineParser {
    private BodyMeasurementLineParser() { }

    static Trackable parse(String data) {
        String[] p = data.split(";");
        if (p.length < 4) {
            throw new IllegalArgumentException("Invalid BM format");
        }

        LocalDate date = Parser.parseDate(p[0]);
        BodyMeasurementMetric metric = BodyMeasurementMetric.valueOf(p[1]);
        String unit = p[2];

        Map<String, Double> map = new HashMap<>();
        for (int i = 3; i < p.length; i++) {
            String[] kv = p[i].split("=");
            if (kv.length != 2) {
                throw new IllegalArgumentException("Invalid key=value");
            }
            map.put(kv[0], Double.parseDouble(kv[1]));
        }
        return new BodyMeasurement(date, metric, unit, map);
    }
}
