package myfitnesspal.command;

import myfitnesspal.items.Meal;
import myfitnesspal.MyFitnessTracker;
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
            outputWriter.write(index + ". " + meal);
            index++;
        }
    }
}
