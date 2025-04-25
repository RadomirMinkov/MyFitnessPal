package myfitnesspal.command;

import myfitnesspal.items.FoodLog;
import myfitnesspal.items.Meal;
import myfitnesspal.items.MealItem;
import myfitnesspal.MyFitnessTracker;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;
import myfitnesspal.utility.Parser;

import java.time.LocalDate;
import java.util.List;

public final class LogMealCommand implements Command {
    private final MyFitnessTracker tracker;
    private final InputProvider inputProvider;
    private final OutputWriter outputWriter;
    private final String fileName;

    public LogMealCommand(MyFitnessTracker tracker,
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
        outputWriter.write(">9. Log Meal");
        LocalDate date = promptDate();
        String mealType = promptMealType();
        Meal chosenMeal = pickMeal();
        double mealServings = promptMealServings();
        logMealItems(date, mealType, chosenMeal, mealServings);
        outputWriter.write("Logged meal successfully: "
                + chosenMeal.name() + " x " + mealServings + " serving(s).");
    }

    private LocalDate promptDate() {
        outputWriter.write(">When (date):\n-");
        String rawDate = inputProvider.readLine().trim();
        return Parser.parseDate(rawDate);
    }

    private String promptMealType() {
        outputWriter.write(">When (meal) [Breakfast/Lunch/Snacks/Dinner]:\n-");
        String mealType = inputProvider.readLine().trim();
        if (mealType.isEmpty()) {
            throw new IllegalArgumentException("Invalid meal type!");
        }
        return mealType;
    }

    private Meal pickMeal() {
        List<Meal> allMeals = tracker.getMeals();
        if (allMeals.isEmpty()) {
            throw new IllegalArgumentException(
                    "No meals in the system. Please create a meal first.");
        }
        for (int i = 0; i < allMeals.size(); i++) {
            outputWriter.write((i + 1) + ". " + allMeals.get(i));
        }
        outputWriter.write(">Which meal (meal id):\n-");
        String line = inputProvider.readLine().trim();
        int chosenId = parseMealId(line, allMeals.size());
        Meal chosenMeal = allMeals.get(chosenId - 1);
        outputWriter.write(">" + chosenId + ". " + chosenMeal);
        return chosenMeal;
    }

    private int parseMealId(String line, int max) {
        int chosenId;
        try {
            chosenId = Integer.parseInt(line);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid meal ID: " + line, e);
        }
        if (chosenId < 1 || chosenId > max) {
            throw new IllegalArgumentException("Invalid meal ID.");
        }
        return chosenId;
    }

    private double promptMealServings() {
        outputWriter.write(">Number of serving(s) for this meal:\n-");
        String servingsStr = inputProvider.readLine().trim();
        double mealServings;
        try {
            mealServings = Double.parseDouble(servingsStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Invalid servings: " + servingsStr, e);
        }
        if (mealServings <= 0) {
            throw new IllegalArgumentException("Servings must be > 0");
        }
        return mealServings;
    }

    private void logMealItems(LocalDate date,
                              String mealType,
                              Meal chosenMeal,
                              double mealServings) {
        for (MealItem mi : chosenMeal.items()) {
            double finalServings = mi.servings() * mealServings;
            var matchedFood = tracker.getFoods().stream()
                    .filter(f -> f.name().equals(mi.foodName()))
                    .findFirst()
                    .orElse(null);

            if (matchedFood == null) {
                throw new IllegalArgumentException(
                        "Meal references unknown food: " + mi.foodName());
            }

            double totalGrams   = matchedFood.servingSize() * finalServings;
            double totalCals    = matchedFood.calories()    * finalServings;
            double totalCarbs   = matchedFood.carbs()       * finalServings;
            double totalFat     = matchedFood.fat()         * finalServings;
            double totalProtein = matchedFood.protein()     * finalServings;

            FoodLog foodLog = new FoodLog(
                    date,
                    mealType,
                    matchedFood.name(),
                    totalGrams,
                    totalCals,
                    totalCarbs,
                    totalFat,
                    totalProtein
            );
            tracker.addItem(foodLog);
        }
    }
}
