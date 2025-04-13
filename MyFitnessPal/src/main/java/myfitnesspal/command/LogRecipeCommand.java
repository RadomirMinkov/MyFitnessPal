package myfitnesspal.command;

import myfitnesspal.FoodLog;
import myfitnesspal.MyFitnessTracker;
import myfitnesspal.Recipe;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;
import myfitnesspal.utility.Parser;

import java.time.LocalDate;
import java.util.List;

public final class LogRecipeCommand implements Command {
    private final MyFitnessTracker tracker;
    private final InputProvider inputProvider;
    private final OutputWriter outputWriter;
    private final String fileName;

    public LogRecipeCommand(MyFitnessTracker tracker,
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
        outputWriter.write(">12. Log Recipe\n");
        LocalDate date = promptDate();
        String mealType = promptMealType();
        Recipe chosenRecipe = chooseRecipe();
        double howMany = promptServings();

        FoodLog logEntry =
                createFoodLog(date, mealType, chosenRecipe, howMany);
        tracker.addItem(logEntry);

        outputWriter.write("Logged recipe successfully: "
                + chosenRecipe.name() + " x " + howMany + " serving(s).");
    }

    private LocalDate promptDate() {
        outputWriter.write(">When (date):\n-");
        String rawDate = inputProvider.readLine().trim();
        return Parser.parseDate(rawDate);
    }

    private String promptMealType() {
        outputWriter.write(">When (meal):\n-");
        String mealType = inputProvider.readLine().trim();
        if (mealType.isEmpty()) {
            throw new IllegalArgumentException("Invalid meal type!");
        }
        return mealType;
    }

    private Recipe chooseRecipe() {
        List<Recipe> recipes = tracker.getRecipes();
        if (recipes.isEmpty()) {
            throw new IllegalArgumentException(
                    "No recipes in the system. Please create a recipe first.");
        }
        for (int i = 0; i < recipes.size(); i++) {
            outputWriter.write((i + 1) + ". " + recipes.get(i));
        }
        outputWriter.write(">Which recipe (recipe id):\n-");
        String line = inputProvider.readLine().trim();
        int chosenId = parseRecipeId(line, recipes.size());
        Recipe chosenRecipe = recipes.get(chosenId - 1);
        outputWriter.write(">" + chosenId + ". " + chosenRecipe);
        return chosenRecipe;
    }

    private int parseRecipeId(String line, int count) {
        int chosenId;
        try {
            chosenId = Integer.parseInt(line);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid recipe ID: " + line);
        }
        if (chosenId < 1 || chosenId > count) {
            throw new IllegalArgumentException("Invalid recipe ID.");
        }
        return chosenId;
    }

    private double promptServings() {
        outputWriter.write(">Number of serving(s):\n-");
        String servStr = inputProvider.readLine().trim();
        double howMany;
        try {
            howMany = Double.parseDouble(servStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid servings: " + servStr);
        }
        if (howMany <= 0) {
            throw new IllegalArgumentException("Servings must be > 0");
        }
        return howMany;
    }

    private FoodLog createFoodLog(LocalDate date, String mealType,
                                  Recipe chosenRecipe, double howMany) {
        double gramsPer = chosenRecipe.totalGrams() / chosenRecipe.servings();
        double calsPer  = chosenRecipe.totalCalories()
                / chosenRecipe.servings();
        double carbsPer = chosenRecipe.totalCarbs() / chosenRecipe.servings();
        double fatPer   = chosenRecipe.totalFat() / chosenRecipe.servings();
        double protPer  = chosenRecipe.totalProtein() / chosenRecipe.servings();

        double totalGrams   = gramsPer  * howMany;
        double totalCals    = calsPer   * howMany;
        double totalCarbs   = carbsPer  * howMany;
        double totalFat     = fatPer    * howMany;
        double totalProtein = protPer   * howMany;

        return new FoodLog(
                date,
                mealType,
                chosenRecipe.name(),
                totalGrams,
                totalCals,
                totalCarbs,
                totalFat,
                totalProtein
        );
    }
}
