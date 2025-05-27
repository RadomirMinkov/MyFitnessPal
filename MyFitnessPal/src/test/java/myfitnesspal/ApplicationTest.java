package myfitnesspal;

import myfitnesspal.users.UserDatabase;
import myfitnesspal.utility.ScannerInputProvider;
import myfitnesspal.utility.ConsoleOutputWriter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

class ApplicationTest {

    private final PrintStream originalOut = System.out;
    private final InputStream originalIn  = System.in;

    private ByteArrayOutputStream testOut;

    @BeforeEach
    void setUp() throws IOException {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
        deleteIfExists("users/users.dat");
        deleteIfExists("users/ivan_data.txt");
        deleteIfExists("users/maria_data.txt");
        Files.createDirectories(Path.of("users"));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    void deleteIfExists(String path) throws IOException {
        Files.deleteIfExists(Path.of(path));
    }

    private void runAppWithInput(String input) {
        System.setIn(new ByteArrayInputStream((input + "\n").getBytes()));
        InputStream in = System.in;
        Application app = new Application(
                new ScannerInputProvider(new Scanner(in)),
                new ConsoleOutputWriter(),
                new UserDatabase()
        );
        app.run();
    }

    @Test
    void registerLoginAndExit() {
        String input = String.join("\n",
                "2", "ivan", "123",
                "1", "ivan", "123",
                "16"
        );
        runAppWithInput(input);
        String out = testOut.toString();
        Assertions.assertTrue(out.contains("Registered"));
        Assertions.assertTrue(out.contains("1. Drink water"));
        Assertions.assertTrue(out.contains("16. Exit"));
        Assertions.assertTrue(out.contains("Program stopped."));
    }

    @Test
    void wrongCredentialsThenExit() {
        String input = String.join("\n",
                "1", "ghost", "wrong",
                "3"
        );
        runAppWithInput(input);
        String out = testOut.toString();
        Assertions.assertTrue(out.contains("Wrong credentials"));
        Assertions.assertTrue(out.contains(">1. Login"));
    }

    @Test
    void drinkWaterAfterLogin() {
        String input = String.join("\n",
                "2", "ivan", "pass",
                "1", "2025-04-01", "2", "400",
                "16"
        );
        runAppWithInput(input);
        String out = testOut.toString();
        Assertions.assertTrue(out.contains(
                "Water intake recorded successfully!"));
        Assertions.assertTrue(out.contains("Program stopped."));
    }

    @Test
    void changeUserKeepsDataSeparate() throws IOException {
        String input = String.join("\n",
                "2", "ivan", "1",
                "1", "ivan", "1",
                "1", "2025-01-01", "250",
                "15",
                "2", "maria", "2",
                "1", "maria", "2",
                "6", "2025-01-01",
                "15",
                "1", "ivan", "1",
                "6", "2025-01-01",
                "16"
        );
        runAppWithInput(input);
        String out = testOut.toString();
        Assertions.assertTrue(out.contains("No water logged"));
    }
}
