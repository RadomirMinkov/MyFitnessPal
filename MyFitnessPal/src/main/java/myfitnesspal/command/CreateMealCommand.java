package myfitnesspal.command;

import myfitnesspal.Food;
import myfitnesspal.Meal;
import myfitnesspal.MealItem;
import myfitnesspal.MyFitnessTracker;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;

import java.util.ArrayList;
import java.util.List;

public final class CreateMealCommand implements Command {
    private final MyFitnessTracker tracker;
    private final InputProvider inputProvider;
    private final OutputWriter outputWriter;
    private final String fileName;

    public CreateMealCommand(MyFitnessTracker tracker,
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
        outputWriter.write(">7. Create Meal\n");
        String name = promptMealName();
        String description = promptMealDescription();

        double[] totals = new double[]{0, 0, 0, 0, 0};
        List<MealItem> mealItems = gatherMealItems(totals);

        Meal meal = buildMeal(name, description, totals, mealItems);
        tracker.addItem(meal);

        showMealCreationResult(meal);
    }

    private String promptMealName() {
        outputWriter.write(">Name:\n-");
        return inputProvider.readLine().trim();
    }

    private String promptMealDescription() {
        outputWriter.write(">Description(optional):\n-");
        return inputProvider.readLine().trim();
    }

    private List<MealItem> gatherMealItems(double[] totals) {
        List<MealItem> mealItems = new ArrayList<>();
        while (true) {
            Food chosen = selectFood();
            double servings = promptServings();

            mealItems.add(new MealItem(chosen.name(), servings));
            accumulateTotals(chosen, servings, totals);

            outputWriter.write(">More? (yes/no)\n-");
            String more = inputProvider.readLine().trim().toLowerCase();
            if (!more.equals("yes")) {
                break;
            }
        }
        return mealItems;
    }

    private Food selectFood() {
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

    private int parseFoodIndex(String idxStr, int size) {
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

    private double promptServings() {
        outputWriter.write(">Number of Servings:\n-");
        String servingStr = inputProvider.readLine().trim();
        double servings;
        try {
            servings = Double.parseDouble(servingStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Invalid servings: " + servingStr);
        }
        if (servings <= 0) {
            throw new IllegalArgumentException("Servings must be positive");
        }
        return servings;
    }

    private void accumulateTotals(Food chosen,
                                  double servings, double[] totals) {
        double grams   = chosen.servingSize() * servings;
        double cals    = chosen.calories()    * servings;
        double carbs   = chosen.carbs()       * servings;
        double fat     = chosen.fat()         * servings;
        double protein = chosen.protein()     * servings;

        totals[0] += grams;
        totals[1] += cals;
        totals[2] += carbs;
        totals[3] += fat;
        totals[4] += protein;
    }

    private Meal buildMeal(String name, String description,
                           double[] totals,
                           List<MealItem> mealItems) {
        return new Meal(
                name,
                description,
                totals[0],
                totals[1],
                totals[2],
                totals[3],
                totals[4],
                mealItems
        );
    }

    private void showMealCreationResult(Meal meal) {
        outputWriter.write("\n>Created Meal: " + meal.name());
        outputWriter.write("From:");
        for (MealItem mi : meal.items()) {
            Food f = tracker.getFoods()
                    .stream()
                    .filter(ff -> ff.name().equals(mi.foodName()))
                    .findFirst()
                    .orElse(null);
            if (f != null) {
                double grams = f.servingSize() * mi.servings();
                outputWriter.write((int) mi.servings() + " x " + f);
            }
        }
        outputWriter.write("----------------------------------");
        outputWriter.write(
                "Serving " + String.format("(%.0fg;"
                                + " %.0f kcal; %.2fg, %.2fg, %.2fg)",
                        meal.totalGrams(),
                        meal.totalCalories(),
                        meal.totalCarbs(),
                        meal.totalFat(),
                        meal.totalProtein())
        );
    }
}
