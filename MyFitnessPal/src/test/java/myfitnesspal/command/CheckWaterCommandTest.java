package myfitnesspal.command;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.WaterIntake;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.LocalDate;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class CheckWaterCommandTest {

    private MyFitnessTracker tracker;

    @BeforeEach
    void setUp() {
        tracker = new MyFitnessTracker();
        tracker.addItem(new WaterIntake(LocalDate.of(2025, 3, 12), 250));
        tracker.addItem(new WaterIntake(LocalDate.of(2025, 3, 12), 500));
        tracker.addItem(new WaterIntake(LocalDate.of(2025, 3, 13), 300));
    }

    @Test
    @DisplayName("Test checking water for a known date")
    void testExecuteFound() {
        String input = "2025-03-12\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PrintStream ps = new PrintStream(out);
        Scanner scanner = new Scanner(in);

        CheckWaterCommand cmd = new CheckWaterCommand(tracker, scanner);

        PrintStream oldOut = System.out;
        System.setOut(ps);

        cmd.execute();

        System.setOut(oldOut);

        String consoleOutput = out.toString();
        assertTrue(consoleOutput.contains("2025-03-12:"),
                "Should print the date header");
        assertTrue(consoleOutput.contains("-> 250 ml"),
                "Should list the 250 ml entry");
        assertTrue(consoleOutput.contains("-> 500 ml"),
                "Should list the 500 ml entry");
    }

    @Test
    @DisplayName("Test checking water for a date with no records")
    void testExecuteNotFound() {
        String input = "2025-03-14\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PrintStream ps = new PrintStream(out);
        Scanner scanner = new Scanner(in);

        CheckWaterCommand cmd = new CheckWaterCommand(tracker, scanner);

        PrintStream oldOut = System.out;
        System.setOut(ps);

        cmd.execute();

        System.setOut(oldOut);

        String consoleOutput = out.toString();
        assertTrue(consoleOutput.contains("No water intake recorded."),
                "Should print message indicating no records for that date");
    }
}
