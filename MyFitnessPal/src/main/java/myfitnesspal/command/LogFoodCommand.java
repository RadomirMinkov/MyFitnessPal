package myfitnesspal.command;

import myfitnesspal.items.Food;
import myfitnesspal.items.FoodLog;
import myfitnesspal.MyFitnessTracker;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;
import myfitnesspal.utility.Parser;

import java.time.LocalDate;
import java.util.List;

public final class LogFoodCommand implements Command {
    private final MyFitnessTracker tracker;
    private final InputProvider inputProvider;
    private final OutputWriter outputWriter;
    private final String fileName;

    public LogFoodCommand(MyFitnessTracker tracker,
                          InputProvider inputProvider,
                          OutputWriter outputWriter,
                          String fileName) {
        this.tracker = tracker;
        this.inputProvider = inputProvider;
        this.outputWriter = outputWriter;
        this.fileName = fileName;
    }

    @Override
    public void execute() {
        outputWriter.write(">5. Log Food\n");

        Food chosenFood = chooseFood();
        double[] totals = promptTotals(chosenFood);

        FoodLog foodLog = new FoodLog(
                promptDate(),
                promptMeal(),
                chosenFood.name(),
                totals[0],
                totals[1],
                totals[2],
                totals[3],
                totals[4]
        );

        tracker.addItem(foodLog);
        outputWriter.write("Logged successfully:\n" + foodLog);
    }

    private LocalDate promptDate() {
        outputWriter.write(">When (date):\n-");
        String rawDate = inputProvider.readLine().trim();
        return Parser.parseDate(rawDate);
    }

    private String promptMeal() {
        outputWriter.write(">When (meal)"
                + " [Breakfast/Lunch/Snacks/Dinner]:\n-");
        String meal = inputProvider.readLine().trim();
        if (meal.isEmpty()) {
            throw new IllegalArgumentException("Invalid meal type!");
        }
        return meal;
    }

    private Food chooseFood() {
        List<Food> allFoods = tracker.getFoods();
        if (allFoods.isEmpty()) {
            throw new IllegalArgumentException(
                    "No foods in the system. Please create a food first.");
        }
        for (int i = 0; i < allFoods.size(); i++) {
            outputWriter.write((i + 1) + ". " + allFoods.get(i));
        }
        outputWriter.write(">Which food (food id):\n-");
        String line = inputProvider.readLine().trim();
        int chosenId;
        try {
            chosenId = Integer.parseInt(line);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Invalid food ID: " + line, e);
        }
        if (chosenId < 1 || chosenId > allFoods.size()) {
            throw new IllegalArgumentException("Invalid food ID.");
        }
        Food chosenFood = allFoods.get(chosenId - 1);
        outputWriter.write(">" + chosenId + ". " + chosenFood);
        return chosenFood;
    }

    private double[] promptTotals(Food chosenFood) {
        outputWriter.write("(Either)\n>Number of serving(s):\n-");
        String line = inputProvider.readLine().trim();

        double totalGrams;
        double totalCalories;
        double totalCarbs;
        double totalFat;
        double totalProtein;

        if (!line.isEmpty()) {
            double numServings;
            try {
                numServings = Double.parseDouble(line);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(
                        "Invalid number of servings: " + line, e);
            }
            totalGrams    = chosenFood.servingSize() * numServings;
            totalCalories = chosenFood.calories()    * numServings;
            totalCarbs    = chosenFood.carbs()       * numServings;
            totalFat      = chosenFood.fat()         * numServings;
            totalProtein  = chosenFood.protein()     * numServings;
        } else {
            outputWriter.write(">Serving size (g):\n-");
            String gramsLine = inputProvider.readLine().trim();
            double grams;
            try {
                grams = Double.parseDouble(gramsLine);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(
                        "Invalid gram amount: " + gramsLine, e);
            }
            if (grams <= 0) {
                throw new IllegalArgumentException("Invalid gram amount!");
            }
            double factor  = grams / chosenFood.servingSize();
            totalGrams     = grams;
            totalCalories  = chosenFood.calories() * factor;
            totalCarbs     = chosenFood.carbs()    * factor;
            totalFat       = chosenFood.fat()      * factor;
            totalProtein   = chosenFood.protein()  * factor;
        }

        return new double[] {
                totalGrams,
                totalCalories,
                totalCarbs,
                totalFat,
                totalProtein
        };
    }

}
