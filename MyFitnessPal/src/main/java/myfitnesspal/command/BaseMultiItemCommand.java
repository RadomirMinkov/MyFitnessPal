package myfitnesspal.command;

import myfitnesspal.items.Food;
import myfitnesspal.MyFitnessTracker;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;

import java.util.List;

public abstract class BaseMultiItemCommand implements Command {
    protected final MyFitnessTracker tracker;
    protected final InputProvider inputProvider;
    protected final OutputWriter outputWriter;
    protected final String fileName;

    protected BaseMultiItemCommand(MyFitnessTracker tracker,
                                   InputProvider inputProvider,
                                   OutputWriter outputWriter,
                                   String fileName) {
        this.tracker = tracker;
        this.inputProvider = inputProvider;
        this.outputWriter = outputWriter;
        this.fileName = fileName;
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
        outputWriter.write("-(Select food id):");
        String idxStr = inputProvider.readLine().trim();
        int idx = parseFoodIndex(idxStr, allFoods.size());
        return allFoods.get(idx - 1);
    }

    protected int parseFoodIndex(String idxStr, int size) {
        int idx;
        try {
            idx = Integer.parseInt(idxStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid food ID: " + idxStr);
        }
        if (idx < 1 || idx > size) {
            throw new IllegalArgumentException("Invalid food ID range");
        }
        return idx;
    }

    protected double promptSubServings() {
        outputWriter.write(">Number of Servings:\n-");
        String servingStr2 = inputProvider.readLine().trim();
        double subServings;
        try {
            subServings = Double.parseDouble(servingStr2);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Invalid servings: " + servingStr2);
        }
        if (subServings <= 0) {
            throw new IllegalArgumentException("Servings must be positive");
        }
        return subServings;
    }

    protected void accumulateTotals(Food chosen,
                                    double subServings, double[] totals) {
        double grams   = chosen.servingSize() * subServings;
        double cals    = chosen.calories()    * subServings;
        double carbs   = chosen.carbs()       * subServings;
        double fat     = chosen.fat()         * subServings;
        double protein = chosen.protein()     * subServings;

        totals[0] += grams;
        totals[1] += cals;
        totals[2] += carbs;
        totals[3] += fat;
        totals[4] += protein;
    }
}
