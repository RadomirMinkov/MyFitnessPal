package myfitnesspal.users;

import myfitnesspal.items.BodyMeasurement;
import myfitnesspal.items.BodyMeasurementMetric;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

class UserTest {

    @Test
    void testLogBodyMeasurementAddsEntry() {
        User user = new User("u", "s", "h");
        LocalDate date = LocalDate.of(2025, 5, 27);
        BodyMeasurement bm = new BodyMeasurement(
                date,
                BodyMeasurementMetric.WEIGHT, "kg",
                Map.of("value", 80.0));

        user.logBodyMeasurement(bm);

        List<BodyMeasurement> r =
                user.getMeasurements(
                        BodyMeasurementMetric.WEIGHT, date, date);
        Assertions.assertEquals(1, r.size());
        Assertions.assertEquals(80.0,
                r.get(0).values().get("value"), 0.001);
    }

    @Test
    void testOverwriteSameDay() {
        User user = new User("u", "s", "h");
        LocalDate date = LocalDate.of(2025, 5, 27);

        user.logBodyMeasurement(new BodyMeasurement(
                date, BodyMeasurementMetric.WEIGHT, "kg",
                Map.of("value", 80.0)));

        user.logBodyMeasurement(new BodyMeasurement(
                date, BodyMeasurementMetric.WEIGHT, "kg",
                Map.of("value", 82.0)));

        List<BodyMeasurement> r =
                user.getMeasurements(
                        BodyMeasurementMetric.WEIGHT, date, date);
        Assertions.assertEquals(1, r.size());
        Assertions.assertEquals(82.0,
                r.get(0).values().get("value"), 0.001);
    }

    @Test
    void testRangeFiltering() {
        User user = new User("u", "s", "h");
        LocalDate d1 = LocalDate.of(2025, 5, 1);
        LocalDate d2 = LocalDate.of(2025, 5, 10);
        LocalDate d3 = LocalDate.of(2025, 5, 20);

        user.logBodyMeasurement(new BodyMeasurement(d1,
                BodyMeasurementMetric.WEIGHT, "kg", Map.of("value", 80.0)));
        user.logBodyMeasurement(new BodyMeasurement(d2,
                BodyMeasurementMetric.WEIGHT, "kg", Map.of("value", 81.0)));
        user.logBodyMeasurement(new BodyMeasurement(d3,
                BodyMeasurementMetric.WEIGHT, "kg", Map.of("value", 82.0)));

        List<BodyMeasurement> r = user.getMeasurements(
                BodyMeasurementMetric.WEIGHT,
                LocalDate.of(2025, 5, 5),
                LocalDate.of(2025, 5, 15));

        Assertions.assertEquals(1, r.size());
        Assertions.assertEquals(d2, r.get(0).date());
    }

    @Test
    void testGetMeasurementsEmpty() {
        User user = new User("u", "s", "h");
        List<BodyMeasurement> r = user.getMeasurements(
                BodyMeasurementMetric.WEIGHT,
                LocalDate.now(), LocalDate.now());
        Assertions.assertTrue(r.isEmpty());
    }
}
