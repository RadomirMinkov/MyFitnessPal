package myfitnesspal.items;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class WaterIntakeTest {

    @Test
    void testRecordFields() {
        WaterIntake wi = new WaterIntake(LocalDate.of(2025, 3, 19), 1000);
        Assertions.assertEquals(LocalDate.of(2025, 3, 19), wi.date());
        Assertions.assertEquals(1000, wi.amount());
    }

    @Test
    void testToFileString() {
        WaterIntake wi = new WaterIntake(LocalDate.of(2025, 3, 19), 500);
        String expected = "WATER;2025-03-19;500";
        Assertions.assertEquals(expected, wi.toFileString());
    }

    @Test
    void testToString() {
        WaterIntake wi = new WaterIntake(LocalDate.of(2025, 3, 19), 500);
        String s = wi.toString();
        Assertions.assertTrue(s.contains("2025-03-19"));
        Assertions.assertTrue(s.contains("500 ml"));
    }
}
