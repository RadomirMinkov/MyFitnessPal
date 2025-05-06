package myfitnesspal.users;

import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;

public final class AuthManager {
    private final InputProvider in;
    private final OutputWriter out;
    private final UserDatabase users;

    public AuthManager(InputProvider in, OutputWriter out, UserDatabase users) {
        this.in = in;
        this.out = out;
        this.users = users;
    }

    public String login() {
        out.write("User:\n-");
        String username = in.readLine().trim();
        out.write("Pass:\n-");
        String password = in.readLine().trim();
        if (users.login(username, password)) {
            return username;
        } else {
            out.writeln("Wrong credentials");
            return null;
        }
    }

    public String register() {
        out.write("New user:\n-");
        String username = in.readLine().trim();
        if (users.userExists(username)) {
            out.writeln("Exists");
            return null;
        }
        out.write("Pass:\n-");
        String password = in.readLine().trim();
        if (users.register(username, password)) {
            out.writeln("Registered");
            return username;
        }
        return null;
    }
}
