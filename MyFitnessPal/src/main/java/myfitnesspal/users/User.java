package myfitnesspal.users;

import myfitnesspal.items.BodyMeasurement;
import myfitnesspal.items.BodyMeasurementMetric;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    private final String name;
    private final String salt;
    private final String hash;
    private final Map<BodyMeasurementMetric,
            List<BodyMeasurement>> measurementLog;

    public User(String name, String salt, String hash) {
        this.name = name;
        this.salt = salt;
        this.hash = hash;
        this.measurementLog = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public String getSalt() {
        return salt;
    }

    public String getHash() {
        return hash;
    }

    public void logBodyMeasurement(BodyMeasurement entry) {
        List<BodyMeasurement> list = measurementLog
                .computeIfAbsent(entry.metric(),
                        k -> new ArrayList<>());
        list.removeIf(e -> e.date().isEqual(entry.date()));
        list.add(entry);
    }

    public List<BodyMeasurement> getMeasurements(BodyMeasurementMetric type,
                                                 LocalDate from,
                                                 LocalDate to) {
        List<BodyMeasurement> all =
                measurementLog.getOrDefault(type, new ArrayList<>());
        List<BodyMeasurement> result = new ArrayList<>();
        for (BodyMeasurement entry : all) {
            if ((entry.date().isEqual(from) || entry.date().isAfter(from))
                    && (entry.date().isEqual(to)
                    || entry.date().isBefore(to))) {
                result.add(entry);
            }
        }
        return result;
    }
}
