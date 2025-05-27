package myfitnesspal.items;

public enum BodyMeasurementMetric {
    WEIGHT,
    HIP,
    WAIST,
    THIGH,
    BICEPS;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

    public static BodyMeasurementMetric fromString(String value) {
        return value == null ? null
                : BodyMeasurementMetric.valueOf(value.trim().toUpperCase());
    }
}
