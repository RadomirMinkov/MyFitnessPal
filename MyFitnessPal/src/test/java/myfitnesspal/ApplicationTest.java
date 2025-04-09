package myfitnesspal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class ApplicationTest {

    private final PrintStream originalOut = System.out;
    private final java.io.InputStream originalIn = System.in;
    private ByteArrayOutputStream testOut;

    @BeforeEach
    void setup() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @AfterEach
    void teardown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    void testRunExitImmediately() {
        String userInput = "10\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        Application app = new Application();
        app.run();
        String output = testOut.toString();
        Assertions.assertTrue(output.contains("1. Drink water"));
        Assertions.assertTrue(output.contains("2. Check water"));
        Assertions.assertTrue(output.contains("3. Create Food"));
        Assertions.assertTrue(output.contains("4. View All Foods"));
        Assertions.assertTrue(output.contains("5. Log Food"));
        Assertions.assertTrue(output.contains("6. View All Logged"));
        Assertions.assertTrue(output.contains("7. Create Meal"));
        Assertions.assertTrue(output.contains("8. View All Meals"));
        Assertions.assertTrue(output.contains("9. Log Meal"));
        Assertions.assertTrue(output.contains("10. Exit"));
        Assertions.assertTrue(output.contains("Program stopped."));
    }

    @Test
    void testRunInvalidCommandAndThenExit() {
        String userInput = "x\n10\n";
        System.setIn(
                new ByteArrayInputStream(userInput.getBytes()));
        Application app = new Application();
        app.run();
        String output = testOut.toString();
        Assertions.assertTrue(output
                .contains("Invalid input! Try again."));
        Assertions.assertTrue(output.contains("Program stopped."));
    }

    @Test
    void testRunDrinkWaterThenExit() {
        String userInput = String.join("\n",
                "1",
                "2025-03-19",
                "500",
                "10"
        ) + "\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        Application app = new Application();
        app.run();
        String output = testOut.toString();
        Assertions.assertTrue(output.contains(">How much?(ml)"));
        Assertions.assertTrue(output
                .contains("Water intake recorded successfully!"));
        Assertions.assertTrue(output.contains("Program stopped."));
    }

    @Test
    void testRunCreateFoodThenExit() {
        String userInput = String.join("\n",
                "3",
                "MyFood",
                "Description",
                "100",
                "2",
                "300",
                "30",
                "10",
                "15",
                "10"
        ) + "\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        Application app = new Application();
        app.run();
        String output = testOut.toString();
        Assertions.assertTrue(output.contains(">Name:"));
        Assertions.assertTrue(output.contains(">Description(optional):"));
        Assertions.assertTrue(output
                .contains(">Serving Size (g):"));
        Assertions.assertTrue(output
                .contains(">Food created successfully!"));
        Assertions.assertTrue(output.contains("Program stopped."));
    }

    @Test
    void testViewAllLoggedNoEntriesThenExit() {
        String userInput = String.join("\n",
                "6",
                "2025-01-01",
                "10"
        ) + "\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        Application app = new Application();
        app.run();
        String output = testOut.toString();
        Assertions.assertTrue(output
                .contains("No foods logged for 2025-01-01")
                || output.contains("No foods logged for 2025/01/01"));
        Assertions.assertTrue(output.contains("No water logged"));
        Assertions.assertTrue(output.contains("Program stopped."));
    }
}
