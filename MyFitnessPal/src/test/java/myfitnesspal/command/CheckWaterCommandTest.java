package myfitnesspal.command;

import myfitnesspal.WaterIntake;
import myfitnesspal.WaterTracker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CheckWaterCommandTest {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream testOut;

    @BeforeEach
    void setUp() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testCheckWaterInvalidDate() {
        String input = "abc\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        WaterTracker tracker = new WaterTracker();
        CheckWaterCommand command = new CheckWaterCommand(tracker, new java.util.Scanner(in));
        command.execute();
        String consoleOutput = testOut.toString();
        assertTrue(consoleOutput.contains("Invalid data: abc"));
    }

    @Test
    void testCheckWaterNoData() {
        String input = "2025-03-04\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        WaterTracker tracker = new WaterTracker();
        CheckWaterCommand command = new CheckWaterCommand(tracker, new java.util.Scanner(in));
        command.execute();
        String consoleOutput = testOut.toString();
        assertTrue(consoleOutput.contains("No written data."));
    }

    @Test
    void testCheckWaterWithData() {
        String input = "2025-03-04\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        WaterTracker tracker = new WaterTracker();
        tracker.addIntake(LocalDate.of(2025, 3, 4), 250);
        CheckWaterCommand command = new CheckWaterCommand(tracker, new java.util.Scanner(in));
        command.execute();
        String consoleOutput = testOut.toString();
        assertTrue(consoleOutput.contains("->250ml"));
    }
}
