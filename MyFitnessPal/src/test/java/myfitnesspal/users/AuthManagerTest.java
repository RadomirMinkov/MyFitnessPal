package myfitnesspal.users;

import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthManagerTest {

    private AuthManager auth;
    private TestInput input;
    private ByteArrayOutputStream outStream;
    private TestOutput output;
    private UserDatabase db;

    @BeforeEach
    void setUp() {
        input = new TestInput();
        outStream = new ByteArrayOutputStream();
        output = new TestOutput(outStream);
        db = new UserDatabase();
        auth = new AuthManager(input, output, db);
    }

    @Test
    void successfulRegistrationReturnsUsername() {
        input.queue("alice", "pass123");
        String result = auth.register();
        assertEquals("alice", result);
        assertTrue(outStream.toString().contains("Registered"));
    }

    @Test
    void registrationFailsForExistingUser() {
        input.queue("bob", "123");
        auth.register();
        input.queue("bob", "1234");
        String result = auth.register();
        assertNull(result);
        assertTrue(outStream.toString().contains("Exists"));
    }

    @Test
    void successfulLoginReturnsUsername() {
        input.queue("user1", "pw");
        auth.register();
        input.queue("user1", "pw");
        String result = auth.login();
        assertEquals("user1", result);
    }

    @Test
    void loginFailsWithWrongPassword() {
        input.queue("user2", "pw");
        auth.register();
        input.queue("user2", "wrongpw");
        String result = auth.login();
        assertNull(result);
        assertTrue(outStream.toString().contains("Wrong credentials"));
    }

    private static class TestInput implements InputProvider {
        private final Queue<String> lines = new LinkedList<>();

        void queue(String... values) {
            for (String val : values) {
                lines.add(val);
            }
        }

        @Override
        public String readLine() {
            return lines.poll();
        }
    }

    private static class TestOutput implements OutputWriter {
        private final PrintStream out;

        TestOutput(ByteArrayOutputStream stream) {
            this.out = new PrintStream(stream);
        }

        @Override
        public void writeln(String message) {
            out.println(message);
        }

        @Override
        public void write(String message) {
            out.print(message);
        }
    }
}
