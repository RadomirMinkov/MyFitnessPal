package myfitnesspal;

import myfitnesspal.users.UserDatabase;
import myfitnesspal.utility.ConsoleOutputWriter;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;
import myfitnesspal.utility.ScannerInputProvider;

import java.util.Scanner;

public final class Main {
    private Main() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        InputProvider in = new ScannerInputProvider(scanner);
        OutputWriter out = new ConsoleOutputWriter();
        UserDatabase users = new UserDatabase();
        Application app = new Application(in, out, users);
        app.run();
    }
}
