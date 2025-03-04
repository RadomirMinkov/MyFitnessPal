package myfitnesspal;

import myfitnesspal.command.CheckWaterCommand;
import myfitnesspal.command.Command;
import myfitnesspal.command.DrinkWaterCommand;
import myfitnesspal.command.ExitCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final String FILE_NAME = "water_data.txt";
    private static Scanner scanner;

    static {
        scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        WaterTracker tracker = new WaterTracker();
        tracker.loadDataFromFile(FILE_NAME);


        final boolean[] running = { true };

        Map<String, Command> commands = new HashMap<>();
        commands.put("1", new DrinkWaterCommand(tracker, scanner, FILE_NAME));
        commands.put("2", new CheckWaterCommand(tracker, scanner));
        commands.put("3", new ExitCommand(() -> running[0] = false));

        gameCycle(running, commands);
    }

    private static void gameCycle(boolean[] running, Map<String, Command> commands) {
        while (running[0]) {
            System.out.println(">1. Drink water");
            System.out.println(">2. Check water");
            System.out.println(">3. Exit");
            System.out.print("-");

            String choice = scanner.nextLine();
            Command command = commands.get(choice);
            if (command != null) {
                command.execute();
            } else {
                System.out.println("Invalid input! Try again.");
            }
        }

        scanner.close();
        System.out.println("Program stopped.");
    }
}
