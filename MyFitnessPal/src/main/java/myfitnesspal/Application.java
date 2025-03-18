package myfitnesspal;

import myfitnesspal.command.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Application {
    private static final String FILE_NAME = "all_data.txt";

    private final MyFitnessTracker tracker;
    private final Scanner scanner;
    private final Map<String, Command> commands;
    private boolean running;

    public Application() {
        this.tracker = new MyFitnessTracker();
        tracker.load(FILE_NAME);

        this.scanner = new Scanner(System.in);

        this.commands = new HashMap<>();

        commands.put("1", new DrinkWaterCommand(tracker, scanner, FILE_NAME));
        commands.put("2", new CheckWaterCommand(tracker, scanner));
        commands.put("3", new CreateFoodCommand(tracker, scanner, FILE_NAME));
        commands.put("4", new ViewAllFoodsCommand(tracker));

        commands.put("5", new LogFoodCommand(tracker, scanner, FILE_NAME));
        commands.put("6", new ViewLoggedFoodsCommand(tracker, scanner));

        commands.put("7", new ExitCommand(this::stop));

        this.running = true;
    }

    public void run() {
        while (running) {
            printMenu();
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

    private void printMenu() {
        System.out.println(">1. Drink water");
        System.out.println(">2. Check water");
        System.out.println(">3. Create Food");
        System.out.println(">4. View All Foods");
        System.out.println(">5. Log Food");
        System.out.println(">6. View Foods Logged");
        System.out.println(">7. Exit");
        System.out.print("-");
    }

    private void stop() {
        this.running = false;
    }
}
