package myfitnesspal.command;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.WaterIntake;

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

        LocalDate date = parseDate(rawDate);
        if (date == null) {
            System.out.println("Invalid date: " + rawDate);
            return;
        }

        System.out.print(">How much?(ml) -");
        String amountStr = scanner.nextLine();
        int amount;
        try {
            amount = Integer.parseInt(amountStr);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number: " + amountStr);
            return;
        }

        WaterIntake waterIntake = new WaterIntake(date, amount);
        tracker.addItem(waterIntake);

        tracker.save(fileName);

        System.out.println(">Water intake recorded successfully!");
    }

    private LocalDate parseDate(String dateStr) {
        DateTimeFormatter[] formats = {
                DateTimeFormatter.ofPattern("yyyy/MM/dd"),
                DateTimeFormatter.ofPattern("dd.MM.yyyy"),
                DateTimeFormatter.ISO_LOCAL_DATE
        };

        for (DateTimeFormatter fmt : formats) {
            try {
                return LocalDate.parse(dateStr, fmt);
            } catch (DateTimeParseException ignored) {}
        }
        return null;
    }
}
