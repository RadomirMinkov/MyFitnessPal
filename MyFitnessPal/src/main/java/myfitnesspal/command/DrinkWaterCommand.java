package myfitnesspal.command;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.WaterIntake;
import myfitnesspal.utility.Parser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class DrinkWaterCommand implements Command {
    private final MyFitnessTracker tracker;
    private final Scanner scanner;
    private final String fileName;

    public DrinkWaterCommand(MyFitnessTracker tracker, Scanner scanner, String fileName) {
        this.tracker = tracker;
        this.scanner = scanner;
        this.fileName = fileName;
    }

    @Override
    public void execute() {
        System.out.print(">When? -");
        String rawDate = scanner.nextLine();

        LocalDate date = Parser.parseDate(rawDate);


        System.out.print(">How much?(ml) -");
        String amountStr = scanner.nextLine();
        int amount;
        try {
            amount = Integer.parseInt(amountStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number: " + amountStr);
        }

        WaterIntake waterIntake = new WaterIntake(date, amount);
        tracker.addItem(waterIntake);

        System.out.println(">Water intake recorded successfully!");
    }

}
