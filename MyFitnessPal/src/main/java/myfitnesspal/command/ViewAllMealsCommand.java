package myfitnesspal.command;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.items.Meal;
import myfitnesspal.utility.OutputWriter;

import java.util.List;

public final class ViewAllMealsCommand implements Command {
    private final MyFitnessTracker tracker;
    private final OutputWriter outputWriter;

    public ViewAllMealsCommand(MyFitnessTracker tracker,
                               OutputWriter outputWriter) {
        this.tracker = tracker;
        this.outputWriter = outputWriter;
    }

    @Override
    public void execute() {
        outputWriter.write(">8. View All Meals");

        List<Meal> allMeals = tracker.getMeals();
        if (allMeals.isEmpty()) {
            outputWriter.write("No meals found.");
            return;
        }

        int index = 1;
        for (Meal meal : allMeals) {
            String summary = String.format(
                    "%d. %s (%.0f units, "
                            + "%.0f kcal; %.2fg, %.2fg, %.2fg)",
                    index++,
                    meal.name(),
                    meal.totalGrams(),
                    meal.totalCalories(),
                    meal.totalCarbs(),
                    meal.totalFat(),
                    meal.totalProtein()
            );
            outputWriter.write(summary);
        }
    }
}
