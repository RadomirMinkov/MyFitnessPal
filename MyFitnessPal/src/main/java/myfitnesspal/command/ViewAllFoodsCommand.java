package myfitnesspal.command;

import myfitnesspal.Food;
import myfitnesspal.MyFitnessTracker;

import java.util.List;

public final class ViewAllFoodsCommand implements Command {
    private final MyFitnessTracker tracker;

    public ViewAllFoodsCommand(MyFitnessTracker tracker) {
        this.tracker = tracker;
    }

    @Override
    public void execute() {
        System.out.println(">4. View All Foods");
        List<Food> allFoods = tracker.getFoods();
        if (allFoods.isEmpty()) {
            System.out.println("No foods found.");
            return;
        }

        int index = 1;
        for (Food f : allFoods) {
            System.out.println(index + ". " + f);
            index++;
        }
    }
}
