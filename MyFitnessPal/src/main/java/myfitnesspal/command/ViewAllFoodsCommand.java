package myfitnesspal.command;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.items.Food;
import myfitnesspal.utility.OutputWriter;

import java.util.List;

public final class ViewAllFoodsCommand implements Command {
    private final MyFitnessTracker tracker;
    private final OutputWriter outputWriter;

    public ViewAllFoodsCommand(MyFitnessTracker tracker,
                               OutputWriter outputWriter) {
        this.tracker = tracker;
        this.outputWriter = outputWriter;
    }

    @Override
    public void execute() {
        outputWriter.write(">4. View All Foods");

        List<Food> allFoods = tracker.getFoods();
        if (allFoods.isEmpty()) {
            outputWriter.write("No foods found.");
            return;
        }

        int index = 1;
        for (Food food : allFoods) {
            String line = String.format(
                    "%d. %s (1 serving = "
                            + "%.2f %s, %.0f kcal, %.2fg, %.2fg, %.2fg)",
                    index++,
                    food.name(),
                    food.unitsPerServing(),
                    food.measurementType().label(),
                    food.calories(),
                    food.carbs(),
                    food.fat(),
                    food.protein());
            outputWriter.write(line);
        }
    }
}
