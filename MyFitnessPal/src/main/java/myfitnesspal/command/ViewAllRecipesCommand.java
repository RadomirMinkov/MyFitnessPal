package myfitnesspal.command;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.Recipe;
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
        List<Recipe> recipes = tracker.getRecipes();
        if (recipes.isEmpty()) {
            outputWriter.writeln("No recipes found.");
            return;
        }
        int index = 1;
        for (Recipe r : recipes) {
            outputWriter.write(index + ". " + r);
            index++;
        }
    }
}
