package myfitnesspal.command;

import org.junit.jupiter.api.*;
import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class ExitCommandTest {

    private boolean exited = false;

    @BeforeEach
    void setUp() {
        exited = false;
    }

    @Test
    @DisplayName("Test ExitCommand triggers onExit callback")
    void testExecute() {
        Runnable onExit = () -> exited = true;


        assertTrue(exited, "ExitCommand should trigger the onExit callback to set 'exited' to true");
    }
}
