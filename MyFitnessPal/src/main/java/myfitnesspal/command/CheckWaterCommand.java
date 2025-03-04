package myfitnesspal.command;

import myfitnesspal.WaterIntake;
import myfitnesspal.WaterTracker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class CheckWaterCommand implements Command {
    private final WaterTracker tracker;
    private final Scanner scanner;

    public CheckWaterCommand(WaterTracker tracker, Scanner scanner) {
        this.tracker = tracker;
        this.scanner = scanner;
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

        List<WaterIntake> intakesForDate = tracker.getIntakesForDate(date);
        if (intakesForDate.isEmpty()) {
            System.out.println(rawDate + ": No written data.");
        } else {
            System.out.println(rawDate + ":");
            for (WaterIntake waterIntake : intakesForDate) {
                System.out.println("->" + waterIntake.amount() + "ml");
            }
        }
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
