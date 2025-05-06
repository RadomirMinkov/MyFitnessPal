package myfitnesspal.command;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.items.Food;
import myfitnesspal.items.Recipe;
import myfitnesspal.items.RecipeItem;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;
import myfitnesspal.utility.PromptUtils;

import java.util.ArrayList;
import java.util.List;

public final class CreateRecipeCommand extends BaseMultiItemCommand {

    public CreateRecipeCommand(MyFitnessTracker tracker,
                               InputProvider inputProvider,
                               OutputWriter outputWriter) {
        super(tracker, inputProvider, outputWriter);
    }

    @Override
    public void execute() {
        outputWriter.write(">10. Create Recipe\n");

        String name = PromptUtils.promptLine(inputProvider, outputWriter,
                ">Name:");
        String description = PromptUtils.promptLine(inputProvider, outputWriter,
                ">Description (optional):");
        int servings = PromptUtils.promptInt(inputProvider, outputWriter,
                ">How many servings will the recipe have?");

        double[] totals = new double[]{0, 0, 0, 0, 0};
        List<RecipeItem> recipeItems = new ArrayList<>();

        while (true) {
            Food chosen = selectFood();
            double servingsUsed = promptSubServings();

            recipeItems.add(new RecipeItem(chosen.name(), servingsUsed));
            accumulateTotals(chosen, servingsUsed, totals);

            String more = PromptUtils.promptLine(inputProvider, outputWriter,
                    ">More? (yes/no)").toLowerCase();
            if (!more.equals("yes")) {
                break;
            }
        }

        Recipe recipe = new Recipe(
                name, description, servings,
                totals[0], totals[1], totals[2], totals[3], totals[4],
                recipeItems
        );
        tracker.addItem(recipe);

        outputWriter.write("\n>Created Recipe: " + recipe.name());
        outputWriter.write("From:");
        for (RecipeItem ri : recipe.items()) {
            Food f = tracker.getFoods().stream()
                    .filter(ff -> ff.name().equals(ri.foodName()))
                    .findFirst()
                    .orElse(null);
            if (f != null) {
                double totalUnits = f.unitsPerServing() * ri.servings();
                outputWriter.write((int) ri.servings() + " x "
                        + f.name()
                        + " (" + totalUnits + " "
                        + f.measurementType().label() + ")");
            }
        }

        outputWriter.write("----------------------------------");
        outputWriter.write("Total: "
                + String.format("%.0f %s; %.0f kcal; %.2fg, %.2fg, %.2fg",
                recipe.totalGrams(),
                recipe.items().isEmpty() ? "units"
                        : tracker.getFoods().stream()
                                .filter(f -> f.name()
                                        .equals(recipe.items()
                                                .get(0).foodName()))
                                .map(f -> f.measurementType().label())
                                .findFirst()
                                .orElse("units"),
                recipe.totalCalories(),
                recipe.totalCarbs(),
                recipe.totalFat(),
                recipe.totalProtein()));
    }
}
