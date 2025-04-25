package myfitnesspal;

import myfitnesspal.command.ChangeUserCommand;
import myfitnesspal.command.Command;
import myfitnesspal.command.CommandType;
import myfitnesspal.users.UserDatabase;
import myfitnesspal.utility.ConsoleOutputWriter;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;
import myfitnesspal.utility.ScannerInputProvider;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public final class Application {
        private static final String USER_DIR = "users";
        private MyFitnessTracker tracker;
        private final Scanner scanner = new Scanner(System.in);
        private final InputProvider in = new ScannerInputProvider(scanner);
        private final OutputWriter out = new ConsoleOutputWriter();
        private final UserDatabase users = new UserDatabase();
        private Map<String, Command> commands;
        private String currentUser;
        private boolean running;

        public Application() {
            new File(USER_DIR).mkdirs();
            loginLoop();
        }

        private void loginLoop() {
            while (currentUser == null) {
                out.write(">1. Login\n>2. Register\n>3. Exit\n-");
                switch (in.readLine().trim()) {
                    case "1" -> login();
                    case "2" -> register();
                    case "3" -> {
                        return;
                    }
                    default -> out.writeln("Invalid!");
                }
            }
            startSession();
        }

        private void login() {
            out.write("User:\n-");
            String u = in.readLine().trim();
            out.write("Pass:\n-");
            String p = in.readLine().trim();
            if (users.login(u, p)) {
                currentUser = u;
            } else {
                out.writeln("Wrong credentials");
            }
        }

        private void register() {
            out.write("New user:\n-");
            String u = in.readLine().trim();
            if (users.userExists(u)) {
                out.writeln("Exists");
                return;
            }
            out.write("Pass:\n-");
            String p = in.readLine().trim();
            if (users.register(u, p)) {
                out.writeln("Registered");
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
            Command cmd;
            if (ct == CommandType.CHANGE_USER) {
                cmd = new ChangeUserCommand(in, out, this::switchUser);
            } else {
                cmd = ct.getCommand(tracker, in, out, dataFile());
                if (cmd instanceof myfitnesspal.command.ExitCommand e) {
                    e.setOnExit(this::stop);
                }
            }
            commands.put(String.valueOf(ct.ordinal() + 1), cmd);
        }
    }

        private void switchUser() {
            tracker.save(dataFile());
            currentUser = null;
            loginLoop();
        }

        public void run() {
            while (running) {
                try {
                    printMenu();
                    String choice = in.readLine();
                    Command command = commands.get(choice);

                    if (command != null) {
                        command.execute();
                    } else {
                        out.write("Invalid input! Try again.");
                    }
                } catch (Exception exception) {
                    out.write(exception.getMessage());
                }
            }
            scanner.close();
            out.write("Program stopped.");
        }

        private String dataFile() {
            return USER_DIR + "/" + currentUser + "_data.txt";
        }

        private void printMenu() {
            for (CommandType ct : CommandType.values()) {
                out.writeln(">"
                        + (ct.ordinal() + 1) + ". " + ct.getDescription());
            }
            out.write("-");
        }

        private void stop() {
            running = false;
        }
    }
