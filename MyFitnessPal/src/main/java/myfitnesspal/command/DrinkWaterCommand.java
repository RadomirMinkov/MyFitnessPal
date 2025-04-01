package myfitnesspal.command;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.WaterIntake;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;
import myfitnesspal.utility.Parser;

import java.time.LocalDate;

public final class DrinkWaterCommand implements Command {
    private final MyFitnessTracker tracker;
    private final InputProvider inputProvider;
    private final OutputWriter outputWriter;
    private final String fileName;

    public DrinkWaterCommand(MyFitnessTracker tracker,
                             InputProvider inputProvider,
                             OutputWriter outputWriter,
                             String fileName) {
        this.tracker = tracker;
        this.inputProvider = inputProvider;
        this.outputWriter = outputWriter;
        this.fileName = fileName;
    }

    @Override
    public void execute() {
        outputWriter.write(">When? -");
        String rawDate = inputProvider.readLine();
        LocalDate date = Parser.parseDate(rawDate);

        outputWriter.write("\n>How much?(ml) -");
        String amountStr = inputProvider.readLine();

        int amount;
        try {
            amount = Integer.parseInt(amountStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Invalid number: " + amountStr, e);
        }

        WaterIntake waterIntake = new WaterIntake(date, amount);
        tracker.addItem(waterIntake);

        outputWriter.write("\n>Water intake recorded successfully!");
    }
}
