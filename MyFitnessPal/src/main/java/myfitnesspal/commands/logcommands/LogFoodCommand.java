package myfitnesspal.commands.logcommands;

import myfitnesspal.commands.Command;
import myfitnesspal.items.Food;
import myfitnesspal.items.FoodLog;
import myfitnesspal.MyFitnessTracker;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;
import myfitnesspal.parser.Parser;
import myfitnesspal.utility.PromptUtils;

import java.time.LocalDate;
import java.util.List;

public final class LogFoodCommand implements Command {
    private final MyFitnessTracker tracker;
    private final InputProvider input;
    private final OutputWriter out;

    public LogFoodCommand(MyFitnessTracker tracker,
                          InputProvider input,
                          OutputWriter out) {
        this.tracker = tracker;
        this.input = input;
        this.out = out;
    }

    @Override
    public void execute() {
        out.writeln(">5. Log Food");

        Food chosenFood = chooseFood();
        LocalDate date = Parser.parseDate(PromptUtils.promptLine(
                input, out, ">When (date):"));
        String meal = PromptUtils.promptLine(input, out,
                ">When (meal) [Breakfast/Lunch/Snacks/Dinner]:");

        double factor = promptAmount(chosenFood);
        double grams = chosenFood.unitsPerServing() * factor;
        double calories = chosenFood.calories() * factor;
        double carbs = chosenFood.carbs() * factor;
        double fat = chosenFood.fat() * factor;
        double protein = chosenFood.protein() * factor;

        FoodLog log = new FoodLog(date, meal, chosenFood.name(),
                grams, calories, carbs, fat, protein);
        tracker.addItem(log);
        out.write("Logged successfully:" + System.lineSeparator() + log);
    }

    private Food chooseFood() {
        List<Food> allFoods = tracker.getFoods();
        if (allFoods.isEmpty()) {
            throw new IllegalArgumentException(
                    "No foods in the system. Please create a food first.");
        }

        for (int i = 0; i < allFoods.size(); i++) {
            out.write((i + 1) + ". " + allFoods.get(i));
        }

        int id = PromptUtils.promptInt(input, out,
                ">Which food (food id):");
        if (id < 1 || id > allFoods.size()) {
            throw new IllegalArgumentException("Invalid food ID.");
        }
        return allFoods.get(id - 1);
    }

    private double promptAmount(Food food) {
        int mode = PromptUtils.promptFoodLogMode(input, out,
                food.measurementType(), food.unitsPerServing());

        if (mode == 1) {
            double units = PromptUtils.promptDouble(input, out,
                    ">How many " + food.measurementType().label() + "?");
            return units / food.unitsPerServing();
        } else if (mode == 2) {
            return PromptUtils.promptDouble(input, out,
                    ">How many servings?");
        } else {
            throw new IllegalArgumentException("Invalid choice.");
        }
    }
}
