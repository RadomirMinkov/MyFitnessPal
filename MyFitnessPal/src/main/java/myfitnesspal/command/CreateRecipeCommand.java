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

        String name = promptRecipeName();
        String description = promptRecipeDescription();
        int servings = promptServingCount();

        double[] totals = new double[]{0, 0, 0, 0, 0};
        List<RecipeItem> items = gatherRecipeItems(totals);

        Recipe recipe = buildRecipe(name, description, servings,
                totals, items);
        tracker.addItem(recipe);

        showRecipeSummary(recipe);
    }

    private String promptRecipeName() {
        return PromptUtils.promptLine(inputProvider, outputWriter,
                ">Name:");
    }

    private String promptRecipeDescription() {
        return PromptUtils.promptLine(inputProvider, outputWriter,
                ">Description (optional):");
    }

    private int promptServingCount() {
        return PromptUtils.promptInt(inputProvider, outputWriter,
                ">How many servings will the recipe have?");
    }

    private List<RecipeItem> gatherRecipeItems(double[] totals) {
        List<RecipeItem> items = new ArrayList<>();
        while (true) {
            Food food = selectFood();
            double servingsUsed = promptSubServings();

            items.add(new RecipeItem(food.name(), servingsUsed));
            accumulateTotals(food, servingsUsed, totals);

            String more = PromptUtils.promptLine(inputProvider, outputWriter,
                    ">More? (yes/no)").toLowerCase();
            if (!more.equals("yes")) {
                break;
            }
        }
        return items;
    }

    private Recipe buildRecipe(String name, String description, int servings,
                               double[] totals, List<RecipeItem> items) {
        return new Recipe(name, description, servings,
                totals[0], totals[1], totals[2], totals[3], totals[4], items);
    }

    private void showRecipeSummary(Recipe recipe) {
        outputWriter.write("\n>Created Recipe: " + recipe.name());
        outputWriter.write("From:");

        for (RecipeItem item : recipe.items()) {
            Food food = tracker.getFoods().stream()
                    .filter(f -> f.name().equals(item.foodName()))
                    .findFirst().orElse(null);

            if (food != null) {
                double totalUnits = food.unitsPerServing() * item.servings();
                outputWriter.write((int) item.servings() + " x "
                        + food.name() + " (" + totalUnits + " "
                        + food.measurementType().label() + ")");
            }
        }

        outputWriter.write("----------------------------------");
        outputWriter.write("Total: " + formatRecipeSummary(recipe));
    }

    private String formatRecipeSummary(Recipe recipe) {
        String unitLabel = resolveUnitLabel(recipe);
        return String.format("%.0f %s; %.0f kcal; %.2fg, %.2fg, %.2fg",
                recipe.totalGrams(),
                unitLabel,
                recipe.totalCalories(),
                recipe.totalCarbs(),
                recipe.totalFat(),
                recipe.totalProtein());
    }

    private String resolveUnitLabel(Recipe recipe) {
        if (recipe.items().isEmpty()) {
            return "units";
        }

        String firstFoodName = recipe.items().get(0).foodName();

        return tracker.getFoods().stream()
                .filter(f -> f.name().equals(firstFoodName))
                .map(f -> f.measurementType().label())
                .findFirst()
                .orElse("units");
    }

}
