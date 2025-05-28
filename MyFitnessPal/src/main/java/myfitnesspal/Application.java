package myfitnesspal;

import myfitnesspal.commands.usercommands.ChangeUserCommand;
import myfitnesspal.commands.Command;
import myfitnesspal.commands.CommandType;
import myfitnesspal.commands.ExitCommand;
import myfitnesspal.users.AuthManager;
import myfitnesspal.users.UserDatabase;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;

import java.util.HashMap;
import java.util.Map;

public final class Application {
    private static final String USER_DIR = "users";
    private MyFitnessTracker tracker;
    private final InputProvider in;
    private final OutputWriter out;
    private final UserDatabase users;
    private final AuthManager auth;
    private Map<String, Command> commands;
    private String currentUser;
    private boolean running;

    public Application(InputProvider in, OutputWriter out, UserDatabase users) {
        this.in = in;
        this.out = out;
        this.users = users;
        this.auth = new AuthManager(in, out, users);
    }

    public void run() {
        loginLoop();
        if (currentUser == null) {
            out.write("Exiting...");
            return;
        }
        startSession();
        out.write("Program stopped.");
    }

    private void loginLoop() {
        while (currentUser == null) {
            out.write(">1. Login" + System.lineSeparator()
                            + ">2. Register"
                    + System.lineSeparator()
                    + ">3. Exit" + System.lineSeparator() + "-");
            String choice = in.readLine().trim();

            if (choice.equals("3")) {
                return;
            }

            String user = switch (choice) {
                case "1" -> auth.login();
                case "2" -> auth.register();
                default -> {
                    out.writeln("Invalid!");
                    yield null;
                }
            };

            if (user != null) {
                currentUser = user;
            }
        }
    }



    private void startSession() {
        tracker = new MyFitnessTracker();
        tracker.load(dataFile());
        initCommands();
        running = true;
        while (running) {
            try {
                printMenu();
                String c = in.readLine();
                Command cmd = commands.get(c);
                if (cmd != null) {
                    cmd.execute();
                } else {
                    out.write("Invalid input! Try again.");
                }
            } catch (Exception e) {
                out.write(e.getMessage());
            }
        }
    }

    private void initCommands() {
        commands = new HashMap<>();
        for (CommandType ct : CommandType.values()) {
            Command cmd = ct.getCommand(tracker, in, out);
            commands.put(String.valueOf(ct.ordinal() + 1), cmd);
        }

        commands.put("15", new ChangeUserCommand(in, out, this::switchUser));
        commands.put("16", new ExitCommand(this::stop,
                tracker, out, dataFile()));
    }

    private void switchUser() {
        tracker.save(dataFile());
        currentUser = null;
        loginLoop();
        if (currentUser != null) {
            startSession();
        }
    }

    private String dataFile() {
        return USER_DIR + "/" + currentUser + "_data.txt";
    }

    private void printMenu() {
        int i = 1;
        for (CommandType ct : CommandType.values()) {
            out.writeln(">" + i + ". " + ct.getDescription());
            i++;
        }
        out.writeln(">15. Change User");
        out.writeln(">16. Exit");
        out.write("-");
    }


    private void stop() {
        running = false;
    }
}
