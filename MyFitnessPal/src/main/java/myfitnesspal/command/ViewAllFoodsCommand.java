package myfitnesspal.command;

import myfitnesspal.Food;
import myfitnesspal.MyFitnessTracker;
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
            outputWriter.write(index + ". " + food);
            index++;
        }
    }
}
