package myfitnesspal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class WaterIntakeTest {

    @Test
    @DisplayName("Test WaterIntake creation and toFileString")
    void testWaterIntakeCreation() {
        LocalDate date = LocalDate.of(2025, 3, 12);
        WaterIntake waterIntake = new WaterIntake(date, 500);

        assertEquals(date, waterIntake.date());
        assertEquals(500, waterIntake.amount());

        String fileStr = waterIntake.toFileString();
        assertTrue(fileStr.startsWith("WATER;"), "File string must start with WATER;");
        String[] parts = fileStr.split(";");
        assertEquals(3, parts.length, "WATER;date;amount => 3 parts total");
    }
}
