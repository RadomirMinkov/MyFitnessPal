package myfitnesspal.users;

import myfitnesspal.utility.PasswordUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public final class UserDatabase {
    private static final String ALL_USERS_FILE = "users";
    private static final String FILE = ALL_USERS_FILE + "/users.dat";
    private final Map<String, User> table = new HashMap<>();

    public UserDatabase() {
        new File(ALL_USERS_FILE).mkdirs();
        load();
    }

    public boolean userExists(String u) {
        return table.containsKey(u);
    }

    public boolean register(String u, String p) {
        if (userExists(u)) {
            return false;
        }
        String salt = PasswordUtil.newSalt();
        String hash = PasswordUtil.hash(salt, p);
        table.put(u, new User(u, salt, hash));
        save();
        return true;
    }

    public boolean login(String u, String p) {
        User usr = table.get(u);
        return usr != null
                && usr.hash().equals(PasswordUtil.hash(usr.salt(), p));
    }

    private void load() {
        File f = new File(FILE);
        if (!f.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    table.put(parts[0], new User(parts[0], parts[1], parts[2]));
                }
            }
        } catch (IOException ignored) {
            throw new IllegalArgumentException(
                    "File couldn't be opened. Invalid user.",
                    ignored.fillInStackTrace());
        }
    }

    private void save() {
        try (PrintWriter printWriter = new PrintWriter(new FileWriter(FILE))) {
            table.values().forEach(u ->
                    printWriter.println(u.name() + ";"
                            + u.salt() + ";" + u.hash()));
        } catch (IOException ignored) {
        }
    }
}
