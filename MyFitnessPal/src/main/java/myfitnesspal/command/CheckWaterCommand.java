package myfitnesspal.command;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.WaterIntake;
import myfitnesspal.utility.Parser;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

 public final class CheckWaterCommand implements Command {
    private final MyFitnessTracker tracker;
    private final Scanner scanner;

    public CheckWaterCommand(MyFitnessTracker tracker, Scanner scanner) {
        this.tracker = tracker;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.print(">When? -");
        String rawDate = scanner.nextLine();

        LocalDate date = Parser.parseDate(rawDate);

        List<WaterIntake> allWater = tracker.getWaterIntakes();
        List<WaterIntake> sameDate = allWater.stream()
                .filter(w -> w.date().equals(date))
                .toList();

        if (sameDate.isEmpty()) {
            System.out.println(rawDate + ": No water intake recorded.");
        } else {
            System.out.println(rawDate + ":");
            for (WaterIntake wi : sameDate) {
                System.out.println("-> " + wi.amount() + " ml");
            }
        }
    }

}
