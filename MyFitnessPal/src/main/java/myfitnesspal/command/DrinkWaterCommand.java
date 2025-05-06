package myfitnesspal.command;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.items.MeasurementType;
import myfitnesspal.items.WaterIntake;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;
import myfitnesspal.utility.Parser;
import myfitnesspal.utility.PromptUtils;

import java.time.LocalDate;

public final class DrinkWaterCommand implements Command {
    private final MyFitnessTracker tracker;
    private final InputProvider input;
    private final OutputWriter out;

    public DrinkWaterCommand(MyFitnessTracker tracker,
                             InputProvider input,
                             OutputWriter out) {
        this.tracker = tracker;
        this.input = input;
        this.out = out;
    }

    @Override
    public void execute() {
        LocalDate date = Parser.parseDate(
                PromptUtils.promptLine(input, out,
                        ">Drink Water\n>When?")
        );

        MeasurementType type = PromptUtils.promptMeasurementType(input, out);

        double mlPerUnit = 1;
        if (type == MeasurementType.PIECE) {
            mlPerUnit = PromptUtils.promptDouble(input, out,
                    ">How many ml per piece?");
        } else if (type == MeasurementType.GRAM) {
            out.write("Note: Water is measured in milliliters.\n"
                    + "If using grams, 1g ≈ 1ml (density = 1.0 assumed).");
        }

        String label = switch (type) {
            case PIECE -> "pieces";
            case GRAM -> "grams";
            case MILLILITER -> "milliliters";
        };

        double inputAmount = PromptUtils.promptDouble(input, out,
                ">How much did you drink? (" + label + ")");

        double mlTotal = switch (type) {
            case PIECE -> inputAmount * mlPerUnit;
            case GRAM, MILLILITER -> inputAmount;
        };

        tracker.addItem(new WaterIntake(date,
                MeasurementType.MILLILITER, mlTotal));
        out.write("Water intake recorded successfully!");
    }
}
