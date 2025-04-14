package myfitnesspal.command;

import myfitnesspal.Food;
import myfitnesspal.Meal;
import myfitnesspal.MealItem;
import myfitnesspal.MyFitnessTracker;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;

import java.util.ArrayList;
import java.util.List;

public final class CreateMealCommand extends BaseMultiItemCommand {
    public CreateMealCommand(MyFitnessTracker tracker,
                             InputProvider inputProvider,
                             OutputWriter outputWriter,
                             String fileName) {
        super(tracker, inputProvider, outputWriter, fileName);
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
            double servings = promptSubServings();

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

    private Meal buildMeal(String name, String description,
                           double[] totals, List<MealItem> mealItems) {
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
            Food f = tracker.getFoods().stream()
                    .filter(ff -> ff.name().equals(mi.foodName()))
                    .findFirst()
                    .orElse(null);
            if (f != null) {
                double grams = f.servingSize() * mi.servings();
                outputWriter.write((int) mi.servings() + " x " + f);
            }
        }
        outputWriter.write("----------------------------------");
        outputWriter.write("Serving "
                + String.format("(%.0fg; %.0f kcal; %.2fg, %.2fg, %.2fg)",
                meal.totalGrams(),
                meal.totalCalories(),
                meal.totalCarbs(),
                meal.totalFat(),
                meal.totalProtein()));
    }
}
