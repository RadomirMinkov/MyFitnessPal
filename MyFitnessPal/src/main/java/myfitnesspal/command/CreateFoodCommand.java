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

        String name = PromptUtils.promptLine(input, out, ">name?");
        String description = PromptUtils.promptLine(input, out,
                ">description?");

        MeasurementType type = PromptUtils.promptMeasurementType(input, out);
        double unitsPerServing;

        unitsPerServing = switch (type) {
            case GRAM -> PromptUtils.promptDouble(input, out,
                    ">Enter grams per serving:");
            case MILLILITER -> PromptUtils.promptDouble(input, out,
                    ">Enter milliliters per serving:");
            case PIECE -> PromptUtils.promptDouble(input, out,
                    ">Enter pieces per serving:");
            default -> throw new
                    IllegalArgumentException("Unknown measurement type");
        };

        int nutrientMode;
        if (type == MeasurementType.GRAM
                || type == MeasurementType.MILLILITER) {
            out.write(">Choose nutrient info:\n>1. Per 100 " + type.label()
                    + "\n>2. Per serving (" + unitsPerServing + " "
                    + type.label() + ")");
        } else {
            out.write(">Choose nutrient info:\n>1. Per piece\n>2. Per serving ("
                    + unitsPerServing + " pcs)");
        }

        nutrientMode = PromptUtils.promptInt(input, out, "-");

        double calories = PromptUtils.promptDouble(
                input, out, ">Enter calories:");
        double carbs = PromptUtils.promptDouble(
                input, out, ">Enter carbs:");
        double fat = PromptUtils.promptDouble(
                input, out, ">Enter fats:");
        double protein = PromptUtils.promptDouble(
                input, out, ">Enter protein:");

        if ((type == MeasurementType.GRAM || type == MeasurementType.MILLILITER)
                && nutrientMode == 1) {
            calories /= 100.0;
            carbs /= 100.0;
            fat /= 100.0;
            protein /= 100.0;
        } else if (type == MeasurementType.PIECE && nutrientMode == 2) {
            calories /= unitsPerServing;
            carbs /= unitsPerServing;
            fat /= unitsPerServing;
            protein /= unitsPerServing;
        }

        Food food = new Food(name, description, type, unitsPerServing, 1,
                calories, carbs, fat, protein);

        tracker.addItem(food);
        out.write("Food created successfully!");
    }
}
