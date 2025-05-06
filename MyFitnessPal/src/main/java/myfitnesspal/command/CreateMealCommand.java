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
        String name = PromptUtils.promptLine(inputProvider, outputWriter,
                ">Name:");
        String description = PromptUtils.promptLine(inputProvider, outputWriter,
                ">Description (optional):");

        double[] totals = new double[]{0, 0, 0, 0, 0};
        List<MealItem> mealItems = new ArrayList<>();

        while (true) {
            Food chosen = selectFood();
            double servings = promptSubServings();

            mealItems.add(new MealItem(chosen.name(), servings));
            accumulateTotals(chosen, servings, totals);

            String more = PromptUtils.promptLine(inputProvider, outputWriter,
                    ">More? (yes/no)").toLowerCase();
            if (!more.equals("yes")) {
                break;
            }
        }

        Meal meal = new Meal(name, description,
                totals[0], totals[1], totals[2], totals[3],
                totals[4], mealItems);
        tracker.addItem(meal);

        outputWriter.write("\n>Created Meal: " + meal.name());
        outputWriter.write("From:");
        for (MealItem mi : meal.items()) {
            Food f = tracker.getFoods().stream()
                    .filter(ff -> ff.name().equals(mi.foodName()))
                    .findFirst()
                    .orElse(null);
            if (f != null) {
                double quantity = f.unitsPerServing() * mi.servings();
                outputWriter.write((int) mi.servings() + " x "
                        + f.name()
                        + " (" + quantity + " "
                        + f.measurementType().label() + ")");
            }
        }
        outputWriter.write("----------------------------------");
        outputWriter.write("Serving "
                + String.format("(%.0f %s; %.0f kcal; %.2fg, %.2fg, %.2fg)",
                meal.totalGrams(),
                meal.items().isEmpty() ? ""
                        : tracker.getFoods().stream()
                                .filter(f -> f.name()
                                        .equals(meal.items()
                                                .get(0).foodName()))
                                .map(f -> f.measurementType().label())
                                .findFirst()
                                .orElse("units"),
                meal.totalCalories(),
                meal.totalCarbs(),
                meal.totalFat(),
                meal.totalProtein()));
    }
}
