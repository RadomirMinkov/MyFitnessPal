package myfitnesspal.command;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.items.Food;
import myfitnesspal.items.Meal;
import myfitnesspal.items.MealItem;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;
import myfitnesspal.utility.PromptUtils;

import java.util.ArrayList;
import java.util.List;

public final class CreateMealCommand extends BaseMultiItemCommand {

    public CreateMealCommand(MyFitnessTracker tracker,
                             InputProvider inputProvider,
                             OutputWriter outputWriter) {
        super(tracker, inputProvider, outputWriter);
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

        showMealCreationSummary(meal);
    }

    private String promptMealName() {
        return PromptUtils.promptLine(inputProvider, outputWriter,
                ">Name:");
    }

    private String promptMealDescription() {
        return PromptUtils.promptLine(inputProvider, outputWriter,
                ">Description (optional):");
    }

    private List<MealItem> gatherMealItems(double[] totals) {
        List<MealItem> mealItems = new ArrayList<>();
        while (true) {
            Food food = selectFood();
            double servings = promptSubServings();

            mealItems.add(new MealItem(food.name(), servings));
            accumulateTotals(food, servings, totals);

            String more = PromptUtils.promptLine(inputProvider, outputWriter,
                    ">More? (yes/no)").toLowerCase();
            if (!more.equals("yes")) {
                break;
            }
        }
        return mealItems;
    }

    private Meal buildMeal(String name, String description,
                           double[] totals,
                           List<MealItem> items) {
        return new Meal(name, description,
                totals[0], totals[1], totals[2], totals[3], totals[4], items);
    }

    private void showMealCreationSummary(Meal meal) {
        outputWriter.write("\n>Created Meal: " + meal.name());
        outputWriter.write("From:");

        for (MealItem item : meal.items()) {
            Food food = tracker.getFoods().stream()
                    .filter(f -> f.name().equals(item.foodName()))
                    .findFirst().orElse(null);

            if (food != null) {
                double quantity = food.unitsPerServing() * item.servings();
                outputWriter.write((int) item.servings() + " x "
                        + food.name() + " (" + quantity + " "
                        + food.measurementType().label() + ")");
            }
        }

        outputWriter.write("----------------------------------");
        outputWriter.write("Serving " + formatMealSummary(meal));
    }

    private String formatMealSummary(Meal meal) {
        String unitLabel = meal.items().isEmpty() ? "units"
                : tracker.getFoods().stream()
                .filter(f -> f.name().equals(meal.items().get(0).foodName()))
                .map(f -> f.measurementType().label())
                .findFirst().orElse("units");

        return String.format("(%.0f %s; %.0f kcal; %.2fg, %.2fg, %.2fg)",
                meal.totalGrams(),
                unitLabel,
                meal.totalCalories(),
                meal.totalCarbs(),
                meal.totalFat(),
                meal.totalProtein());
    }
}
