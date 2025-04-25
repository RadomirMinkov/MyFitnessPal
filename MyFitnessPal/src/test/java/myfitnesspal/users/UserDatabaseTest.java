package myfitnesspal.users;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class UserDatabaseTest {

    private static final Path USER_DIR = Path.of("users");
    private static final Path USER_FILE = USER_DIR.resolve("users.dat");

    @BeforeEach
    void setup() throws IOException {
        Files.createDirectories(USER_DIR);
        Files.deleteIfExists(USER_FILE);
    }

    @AfterEach
    void teardown() throws IOException {
        Files.deleteIfExists(USER_FILE);
    }

    @Test
    void testRegisterAndLoginSuccess() {
        UserDatabase db = new UserDatabase();
        Assertions.assertTrue(db.register("alice", "password123"));
        Assertions.assertTrue(db.userExists("alice"));
        Assertions.assertTrue(db.login("alice", "password123"));
    }

    @Test
    void testRegisterDuplicateFails() {
        UserDatabase db = new UserDatabase();
        Assertions.assertTrue(db.register("bob", "pass"));
        Assertions.assertFalse(db.register("bob", "otherpass"));
    }

    @Test
    void testLoginWithWrongPasswordFails() {
        UserDatabase db = new UserDatabase();
        db.register("carol", "secret");
        Assertions.assertFalse(db.login("carol", "wrong"));
    }

    @Test
    void testPersistenceAfterReload() {
        UserDatabase db = new UserDatabase();
        db.register("dave", "1234");
        Assertions.assertTrue(db.login("dave", "1234"));

        db = new UserDatabase();
        Assertions.assertTrue(db.userExists("dave"));
        Assertions.assertTrue(db.login("dave", "1234"));
    }

    @Test
    void testNonExistentUserLoginFails() {
        UserDatabase db = new UserDatabase();
        Assertions.assertFalse(db.login("ghost", "none"));
    }
}
