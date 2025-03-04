package myfitnesspal.command;

import myfitnesspal.WaterTracker;
import myfitnesspal.WaterIntake;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DrinkWaterCommandTest {

    @Test
    void testValidInput(@TempDir File tempDir) {
        String input = "2025/03/04\n250\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        WaterTracker tracker = new WaterTracker();
        File file = new File(tempDir, "test_data.txt");
        DrinkWaterCommand command = new DrinkWaterCommand(tracker, new java.util.Scanner(in), file.getAbsolutePath());
        command.execute();
        assertEquals(1, tracker.getIntakes().size());
    }

    @Test
    void testInvalidDate(@TempDir File tempDir) {
        String input = "invalid-date\n250\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        WaterTracker tracker = new WaterTracker();
        File file = new File(tempDir, "test_data.txt");
        DrinkWaterCommand command = new DrinkWaterCommand(tracker, new java.util.Scanner(in), file.getAbsolutePath());
        command.execute();
        assertEquals(0, tracker.getIntakes().size());
    }

    @Test
    void testInvalidNumber(@TempDir File tempDir) {
        String input = "2025/03/04\nabc\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        WaterTracker tracker = new WaterTracker();
        File file = new File(tempDir, "test_data.txt");
        DrinkWaterCommand command = new DrinkWaterCommand(tracker, new java.util.Scanner(in), file.getAbsolutePath());
        command.execute();
        assertEquals(0, tracker.getIntakes().size());
    }
}
