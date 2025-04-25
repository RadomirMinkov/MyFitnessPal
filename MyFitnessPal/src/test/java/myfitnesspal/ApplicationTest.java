package myfitnesspal;

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

class ApplicationLoginWithUserDirTest {

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

    @Test
    void registerLoginAndExit() {
        String input = String.join("\n",
                "2", "ivan", "123",
                "1", "ivan", "123",
                "14"
        ) + "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        new Application().run();
        String out = testOut.toString();
        Assertions.assertTrue(out.contains("Registered"));
        Assertions.assertTrue(out.contains("1. Drink water"));
        Assertions.assertTrue(out.contains("14. Exit"));
        Assertions.assertTrue(out.contains("Program stopped."));
    }

    @Test
    void wrongCredentialsThenExit() {
        String input = String.join("\n",
                "1", "ghost", "wrong",
                "3"
        ) + "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        new Application().run();
        String out = testOut.toString();
        Assertions.assertTrue(out.contains("Wrong credentials"));
        Assertions.assertTrue(out.contains(">1. Login"));
    }

    @Test
    void drinkWaterAfterLogin() {
        String input = String.join("\n",
                "2", "ivan", "pass",
                "1", "ivan", "pass",
                "1", "2025-04-01", "400",
                "14"
        ) + "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        new Application().run();
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
                "13",
                "2", "maria", "2",
                "1", "maria", "2",
                "6", "2025-01-01",
                "13",
                "1", "ivan", "1",
                "6", "2025-01-01",
                "14"
        ) + "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        new Application().run();
        String out = testOut.toString();
        Assertions.assertTrue(out.contains("No water logged"));
    }
}
