package myfitnesspal.command;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.WaterIntake;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class DrinkWaterCommandTest {

    private MyFitnessTracker tracker;

    @BeforeEach
    void setUp() {
        tracker = new MyFitnessTracker();
    }

    @Test
    @DisplayName("Test valid water input (date + amount)")
    void testExecuteValidInput() {
        String userInput = "2025-03-12\n500\n";
        InputStream in = new ByteArrayInputStream(userInput.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PrintStream ps = new PrintStream(out);
        Scanner scanner = new Scanner(in);

        DrinkWaterCommand command = new DrinkWaterCommand(tracker, scanner, "test_file.txt");

        PrintStream oldOut = System.out;
        System.setOut(ps);

        command.execute();

        System.setOut(oldOut);

        String consoleOutput = out.toString();
        assertTrue(consoleOutput.contains("Water intake recorded successfully"),
                "Should indicate water intake was recorded successfully");

        List<WaterIntake> waterList = tracker.getWaterIntakes();
        assertEquals(1, waterList.size(), "Should have 1 water record after the command");
        WaterIntake wi = waterList.get(0);
        assertEquals(LocalDate.of(2025, 3, 12), wi.date());
        assertEquals(500, wi.amount());
    }

    @Test
    @DisplayName("Test invalid date input")
    void testExecuteInvalidDate() {
        String userInput = "not-a-valid-date\n500\n";
        InputStream in = new ByteArrayInputStream(userInput.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PrintStream ps = new PrintStream(out);
        Scanner scanner = new Scanner(in);

        DrinkWaterCommand command = new DrinkWaterCommand(tracker, scanner, "test_file.txt");

        PrintStream oldOut = System.out;
        System.setOut(ps);

        command.execute();

        System.setOut(oldOut);

        String consoleOutput = out.toString();
        assertTrue(consoleOutput.contains("Invalid date: not-a-valid-date"),
                "Should print invalid date message");
        assertTrue(tracker.getWaterIntakes().isEmpty());
    }

    @Test
    @DisplayName("Test invalid amount input")
    void testExecuteInvalidAmount() {
        String userInput = "2025-03-12\nnotanumber\n";
        InputStream in = new ByteArrayInputStream(userInput.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PrintStream ps = new PrintStream(out);
        Scanner scanner = new Scanner(in);

        DrinkWaterCommand command = new DrinkWaterCommand(tracker, scanner, "test_file.txt");

        PrintStream oldOut = System.out;
        System.setOut(ps);

        command.execute();

        System.setOut(oldOut);

        String consoleOutput = out.toString();
        assertTrue(consoleOutput.contains("Invalid number: notanumber"),
                "Should print invalid number message");
        assertTrue(tracker.getWaterIntakes().isEmpty(),
                "Tracker should remain empty after invalid amount");
    }
}
