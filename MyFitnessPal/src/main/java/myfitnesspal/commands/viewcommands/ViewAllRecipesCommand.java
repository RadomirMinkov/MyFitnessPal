package myfitnesspal.commands.viewcommands;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.commands.Command;
import myfitnesspal.items.Recipe;
import myfitnesspal.utility.OutputWriter;

import java.util.List;

public final class ViewAllRecipesCommand implements Command {
    private final MyFitnessTracker tracker;
    private final OutputWriter outputWriter;

    public ViewAllRecipesCommand(MyFitnessTracker tracker,
                                 OutputWriter outputWriter) {
        this.tracker = tracker;
        this.outputWriter = outputWriter;
    }

    @Override
    public void execute() {
        outputWriter.writeln(">11. View All Recipes");

        List<Recipe> recipes = tracker.getItems(Recipe.class);
        if (recipes.isEmpty()) {
            outputWriter.writeln("No recipes found.");
            return;
        }

        int index = 1;
        for (Recipe recipe : recipes) {
            String summary = String.format(
                    "%d. %s (%d servings, "
                            + "%.0f units, %.0f kcal; %.2fg, %.2fg, %.2fg)",
                    index++,
                    recipe.name(),
                    recipe.servings(),
                    recipe.totalGrams(),
                    recipe.totalCalories(),
                    recipe.totalCarbs(),
                    recipe.totalFat(),
                    recipe.totalProtein()
            );
            outputWriter.write(summary);
        }
    }
}
