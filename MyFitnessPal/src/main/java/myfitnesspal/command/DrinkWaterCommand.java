package myfitnesspal.command;

import myfitnesspal.WaterTracker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class DrinkWaterCommand implements Command {
    private final WaterTracker tracker;
    private final Scanner scanner;
    private final String fileName;

    public DrinkWaterCommand(WaterTracker tracker, Scanner scanner, String fileName) {
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
            System.out.println("Invalid data: " + rawDate);
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

        tracker.addIntake(date, amount);
        tracker.saveDataToFile(fileName);
        System.out.println(">ok");
    }

    private LocalDate parseDate(String dateStr) {
        DateTimeFormatter[] possibleFormats = new DateTimeFormatter[] {
                DateTimeFormatter.ofPattern("yyyy/MM/dd"),
                DateTimeFormatter.ofPattern("dd.MM.yyyy"),
                DateTimeFormatter.ISO_LOCAL_DATE
        };

        for (DateTimeFormatter formatter : possibleFormats) {
            try {
                return LocalDate.parse(dateStr, formatter);
            } catch (DateTimeParseException e) {

            }
        }
        return null;
    }
}
