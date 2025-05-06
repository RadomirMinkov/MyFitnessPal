package myfitnesspal.items;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class WaterIntakeTest {

    @Test
    void testRecordFields() {
        WaterIntake wi = new WaterIntake(LocalDate.of(2025, 3, 19),
                MeasurementType.MILLILITER, 1000);
        Assertions.assertEquals(LocalDate.of(2025, 3, 19), wi.date());
        Assertions.assertEquals(MeasurementType.MILLILITER,
                wi.measurementType());
        Assertions.assertEquals(1000, wi.amount());
    }

    @Test
    void testToFileString() {
        WaterIntake wi = new WaterIntake(LocalDate.of(2025, 3, 19),
                MeasurementType.PIECE, 2);
        String expected = "WATER;2025-03-19;PIECE;2.0";
        Assertions.assertEquals(expected, wi.toFileString());
    }

    @Test
    void testToString() {
        WaterIntake wi = new WaterIntake(LocalDate.of(2025, 3, 19),
                MeasurementType.MILLILITER, 500);
        String s = wi.toString();
        Assertions.assertTrue(s.contains("2025-03-19"));
        Assertions.assertTrue(s.contains("500.0 ml"));
    }
}
