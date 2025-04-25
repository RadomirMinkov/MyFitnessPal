package myfitnesspal.command;

import myfitnesspal.items.Food;
import myfitnesspal.MyFitnessTracker;
import myfitnesspal.items.Recipe;
import myfitnesspal.items.RecipeItem;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;

import java.util.ArrayList;
import java.util.List;

public final class CreateRecipeCommand extends BaseMultiItemCommand {
    public CreateRecipeCommand(MyFitnessTracker tracker,
                               InputProvider inputProvider,
                               OutputWriter outputWriter,
                               String fileName) {
        super(tracker, inputProvider, outputWriter, fileName);
    }

    @Override
    public void execute() {
        outputWriter.write(">10. Create Recipe\n");
        String name = promptName();
        String description = promptDescription();
        int servings = promptServings();

        double[] totals = new double[]{0, 0, 0, 0, 0};
        List<RecipeItem> items = gatherRecipeItems(totals);

        Recipe recipe = buildRecipe(name, description,
                servings, totals, items);
        tracker.addItem(recipe);

        showCreationResult(recipe, items, servings, totals);
    }

    private String promptName() {
        outputWriter.write(">Name:\n-");
        return inputProvider.readLine().trim();
    }

    private String promptDescription() {
        outputWriter.write(">Description(optional):\n-");
        return inputProvider.readLine().trim();
    }

    private int promptServings() {
        outputWriter.write(">Servings:\n-");
        String servingsStr = inputProvider.readLine().trim();
        int servings;
        try {
            servings = Integer.parseInt(servingsStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Invalid servings: " + servingsStr);
        }
        if (servings <= 0) {
            throw new IllegalArgumentException(
                    "Servings must be a positive integer");
        }
        return servings;
    }

    private List<RecipeItem> gatherRecipeItems(double[] totals) {
        List<RecipeItem> items = new ArrayList<>();
        while (true) {
            Food chosen = selectFood();
            double subServings = promptSubServings();

            items.add(new RecipeItem(chosen.name(), subServings));
            accumulateTotals(chosen, subServings, totals);

            outputWriter.write(">More? (yes/no)\n-");
            String more = inputProvider.readLine()
                    .trim().toLowerCase();
            if (!more.equals("yes")) {
                break;
            }
        }
        return items;
    }

    private Recipe buildRecipe(String name, String description,
                               int servings, double[] totals,
                               List<RecipeItem> items) {
        return new Recipe(
                name,
                description,
                servings,
                totals[0],
                totals[1],
                totals[2],
                totals[3],
                totals[4],
                items
        );
    }

    private void showCreationResult(Recipe recipe,
                                    List<RecipeItem> items,
                                    int servings,
                                    double[] totals) {
        outputWriter.write("\n>Created Recipe: " + recipe.name());
        outputWriter.write("From:");
        for (int i = 0; i < items.size(); i++) {
            RecipeItem ri = items.get(i);
            Food found = tracker.getFoods().stream()
                    .filter(ff -> ff.name().equals(ri.foodName()))
                    .findFirst()
                    .orElse(null);
            if (found != null) {
                outputWriter.write(ri.servings()
                        + " x " + (i + 1) + ". " + found);
            }
        }
        outputWriter.write("----------------------------------");
        double gramsPerServing = totals[0] / servings;
        double calsPerServing  = totals[1] / servings;
        double carbsPerServing = totals[2] / servings;
        double fatPerServing   = totals[3] / servings;
        double protPerServing  = totals[4] / servings;
        outputWriter.write("Serving "
                + String.format(
                        "(%.2fg; %.2f kcal; %.2fg, %.2fg, %.2fg)",
                gramsPerServing, calsPerServing,
                carbsPerServing, fatPerServing, protPerServing));
    }
}
