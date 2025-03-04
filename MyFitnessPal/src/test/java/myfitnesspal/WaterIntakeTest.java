package myfitnesspal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class WaterIntakeTest {

    @Test
    void testFromString_validIsoFormat() {
        String input = "2025-03-04;250";
        WaterIntake waterIntake = WaterIntake.fromString(input);
        Assertions.assertNotNull(waterIntake, "Shouldn't be null when we have a valid data");

        Assertions.assertEquals(LocalDate.of(2025, 3, 4), waterIntake.date(),
                "Invalid data");
        Assertions.assertEquals(250, waterIntake.amount(),
                "Invalid mls");
    }

    @Test
    void testFromString_validYearSlashMonthSlashDay() {

        String input = "2025/03/04;300";
        WaterIntake waterIntake = WaterIntake.fromString(input);
        Assertions.assertNotNull(waterIntake);

        Assertions.assertEquals(LocalDate.of(2025, 3, 4), waterIntake.date());
        Assertions.assertEquals(300, waterIntake.amount());
    }

    @Test
    void testFromString_validDayDotMonthDotYear() {

        String input = "05.05.2024;500";
        WaterIntake waterIntake = WaterIntake.fromString(input);
        Assertions.assertNotNull(waterIntake);

        Assertions.assertEquals(LocalDate.of(2024, 5, 5), waterIntake.date());
        Assertions.assertEquals(500, waterIntake.amount());
    }

    @Test
    void testFromString_invalidDate() {

        String input = "2025-13-40;250";
        WaterIntake waterIntake = WaterIntake.fromString(input);
        Assertions.assertNull(waterIntake,
                "For invalid data null is expected");
    }

    @Test
    void testFromString_incorrectFormat() {

        String input = "2025-03-04 250";
        WaterIntake waterIntake = WaterIntake.fromString(input);
        Assertions.assertNull(waterIntake,
                "Null is expected if wrong CSV format is given");
    }

    @Test
    void testFromString_invalidAmount() {
        String input = "2025/03/04;ABC";

        Assertions.assertThrows(NumberFormatException.class, () -> {
            WaterIntake.fromString(input);
        }, "NumberFormatException is expected");
    }

    @Test
    void testToString() {
        WaterIntake waterIntake = new WaterIntake(LocalDate.of(2025, 3, 4), 250);

        String expected = "2025-03-04;250";
        String actual = waterIntake.toString();
        Assertions.assertEquals(expected, actual,
                "Format of method toString is different from expected");
    }

    @Test
    void testRecordFields() {
        LocalDate date = LocalDate.of(2025, 1, 1);
        int amount = 100;
        WaterIntake waterIntake = new WaterIntake(date, amount);

        Assertions.assertEquals(date, waterIntake.date());
        Assertions.assertEquals(amount, waterIntake.amount());
    }
}
