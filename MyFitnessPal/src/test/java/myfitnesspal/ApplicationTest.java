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
        String userInput = "7\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        Application app = new Application();
        app.run();

        String output = testOut.toString();

        Assertions.assertTrue(output.contains("1. Drink water"));
        Assertions.assertTrue(output.contains("7. Exit"));
        Assertions.assertTrue(output.contains("Program stopped."));
    }

    @Test
    void testRunInvalidCommandAndThenExit() {

        String userInput = "x\n7\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        Application app = new Application();
        app.run();

        String output = testOut.toString();

        Assertions.assertTrue(output.contains("Invalid input! Try again."));

        Assertions.assertTrue(output.contains("Program stopped."));
    }

    @Test
    void testRunDrinkWaterThenExit() {
        String userInput = String.join("\n",
                "1",
                "2025-03-19",
                "500",
                "7"
        ) + "\n";

        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        Application app = new Application();
        app.run();

        String output = testOut.toString();
        Assertions.assertTrue(output.contains(">How much?(ml)"));
        Assertions.assertTrue(output.contains(
                "Water intake recorded successfully!"));

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
                "7"
        ) + "\n";

        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        Application app = new Application();
        app.run();

        String output = testOut.toString();

        Assertions.assertTrue(output.contains(">Name:"));
        Assertions.assertTrue(output.contains(">Description(optional):"));
        Assertions.assertTrue(output.contains(">Serving Size (g):"));
        Assertions.assertTrue(output.contains(">Food created successfully!"));

        Assertions.assertTrue(output.contains("Program stopped."));
    }
}
