package myfitnesspal.parser;

import myfitnesspal.items.BodyMeasurement;
import myfitnesspal.items.BodyMeasurementMetric;
import myfitnesspal.items.Trackable;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static myfitnesspal.utility.Constants.BODY_MEASUREMENT_PARAMS;

final class BodyMeasurementLineParser {

    Trackable parse(String data) {
        String[] p = data.split(";");
        if (p.length < BODY_MEASUREMENT_PARAMS) {
            throw new IllegalArgumentException("Invalid BM format");
        }

        LocalDate date = Parser.parseDate(p[0]);
        BodyMeasurementMetric metric = BodyMeasurementMetric.valueOf(p[1]);
        String unit = p[2];

        Map<String, Double> map = new HashMap<>();
        for (int i = BODY_MEASUREMENT_PARAMS - 1; i < p.length; i++) {
            String[] kv = p[i].split("=");
            if (kv.length != 2) {
                throw new IllegalArgumentException("Invalid key=value");
            }
            map.put(kv[0], Double.parseDouble(kv[1]));
        }
        return new BodyMeasurement(date, metric, unit, map);
    }
}
