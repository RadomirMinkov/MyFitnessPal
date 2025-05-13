package myfitnesspal.command;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.items.Food;
import myfitnesspal.items.MeasurementType;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;
import myfitnesspal.utility.PromptUtils;

public final class CreateFoodCommand implements Command {
    private final MyFitnessTracker tracker;
    private final InputProvider input;
    private final OutputWriter out;

    public CreateFoodCommand(MyFitnessTracker tracker,
                             InputProvider input,
                             OutputWriter out) {
        this.tracker = tracker;
        this.input = input;
        this.out = out;
    }

    @Override
    public void execute() {
        out.write(">1. Create food");

        String name = promptName();
        String description = promptDescription();
        MeasurementType type = promptMeasurementType();
        double unitsPerServing = promptUnitsPerServing(type);
        int nutrientMode = promptNutrientMode(type, unitsPerServing);
        double[] nutrients = promptNutrientValues(type,
                nutrientMode,
                unitsPerServing);

        Food food = new Food(name, description, type, unitsPerServing, 1,
                nutrients[0], nutrients[1], nutrients[2], nutrients[3]);

        tracker.addItem(food);
        out.write("Food created successfully!");
    }

    private String promptName() {
        return PromptUtils.promptLine(input, out, ">name?");
    }

    private String promptDescription() {
        return PromptUtils.promptLine(input, out, ">description?");
    }

    private MeasurementType promptMeasurementType() {
        return PromptUtils.promptMeasurementType(input, out);
    }

    private double promptUnitsPerServing(MeasurementType type) {
        return switch (type) {
            case GRAM -> PromptUtils.promptDouble(input, out,
                    ">Enter grams per serving:");
            case MILLILITER -> PromptUtils.promptDouble(input, out,
                    ">Enter milliliters per serving:");
            case PIECE -> PromptUtils.promptDouble(input, out,
                    ">Enter pieces per serving:");
        };
    }

    private int promptNutrientMode(MeasurementType type,
                                   double unitsPerServing) {
        if (type == MeasurementType.GRAM
                || type == MeasurementType.MILLILITER) {
            out.write(">Choose nutrient info:\n>1. Per 100 " + type.label()
                    + "\n>2. Per serving (" + unitsPerServing + " "
                    + type.label() + ")");
        } else {
            out.write(">Choose nutrient info:\n>1. Per piece\n>2. Per serving ("
                    + unitsPerServing + " pcs)");
        }
        return PromptUtils.promptInt(input, out, "-");
    }

    private double[] promptNutrientValues(MeasurementType type,
                                          int mode,
                                          double unitsPerServing) {
        double cal = PromptUtils.promptDouble(input, out,
                ">Enter calories:");
        double carb = PromptUtils.promptDouble(input, out,
                ">Enter carbs:");
        double fat = PromptUtils.promptDouble(input, out,
                ">Enter fats:");
        double proteins = PromptUtils.promptDouble(input, out,
                ">Enter protein:");

        if ((type == MeasurementType.GRAM || type == MeasurementType.MILLILITER)
                && mode == 1) {
            return new double[]
                    {cal / 100.0, carb / 100.0, fat / 100.0, proteins / 100.0};
        } else if (type == MeasurementType.PIECE && mode == 2) {
            return new double[]{cal / unitsPerServing, carb / unitsPerServing,
                    fat / unitsPerServing, proteins / unitsPerServing};
        } else {
            return new double[]{cal, carb, fat, proteins};
        }
    }
}
