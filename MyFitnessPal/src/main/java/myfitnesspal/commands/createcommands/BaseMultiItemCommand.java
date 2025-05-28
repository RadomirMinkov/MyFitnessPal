package myfitnesspal.commands.createcommands;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.commands.Command;
import myfitnesspal.items.Food;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;
import myfitnesspal.utility.PromptUtils;

import java.util.List;

public abstract class BaseMultiItemCommand implements Command {
    protected final MyFitnessTracker tracker;
    protected final InputProvider inputProvider;
    protected final OutputWriter outputWriter;

    protected BaseMultiItemCommand(MyFitnessTracker tracker,
                                   InputProvider inputProvider,
                                   OutputWriter outputWriter) {
        this.tracker = tracker;
        this.inputProvider = inputProvider;
        this.outputWriter = outputWriter;
    }

    protected Food selectFood() {
        List<Food> allFoods = tracker.getFoods();
        if (allFoods.isEmpty()) {
            throw new IllegalArgumentException(
                    "No foods in the system. Create a food first!");
        }

        outputWriter.write(">All foods list:");
        for (int i = 0; i < allFoods.size(); i++) {
            outputWriter.write((i + 1) + ". " + allFoods.get(i));
        }

        int idx = PromptUtils.promptInt(inputProvider,
                outputWriter, "-(Select food id):");
        if (idx < 1 || idx > allFoods.size()) {
            throw new IllegalArgumentException("Invalid food ID range");
        }
        return allFoods.get(idx - 1);
    }

    protected double promptSubServings() {
        double subServings = PromptUtils.promptDouble(inputProvider,
                outputWriter,
                ">Number of Servings:");
        if (subServings <= 0) {
            throw new IllegalArgumentException("Servings must be positive");
        }
        return subServings;
    }

    protected void accumulateTotals(Food chosen, double subServings,
                                    double[] totals) {
        double units = chosen.unitsPerServing() * subServings;
        double cals = chosen.calories() * subServings;
        double carbs = chosen.carbs() * subServings;
        double fat = chosen.fat() * subServings;
        double protein = chosen.protein() * subServings;

        totals[0] += units;
        totals[1] += cals;
        totals[2] += carbs;
        totals[3] += fat;
        totals[4] += protein;
    }
}
