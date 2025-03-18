package myfitnesspal.command;

import myfitnesspal.FoodLog;
import myfitnesspal.MyFitnessTracker;
import myfitnesspal.utility.Parser;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ViewLoggedFoodsCommand implements Command {
    private final MyFitnessTracker tracker;
    private final Scanner scanner;

    public ViewLoggedFoodsCommand(MyFitnessTracker tracker, Scanner scanner) {
        this.tracker = tracker;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.println(">6. View Foods Logged");

        System.out.print(">When (date):\n-");
        String rawDate = scanner.nextLine();

        LocalDate date = Parser.parseDate(rawDate);

        List<FoodLog> logs = tracker.getFoodLogsForDate(date);
        if (logs.isEmpty()) {
            throw new IllegalArgumentException("No foods logged for " + rawDate);
        }

        System.out.println("Foods logged on " + rawDate + ":");
        for (FoodLog log : logs) {
            System.out.println("- " + log);
        }
    }
}
