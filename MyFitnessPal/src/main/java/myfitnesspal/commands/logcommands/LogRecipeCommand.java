package myfitnesspal.commands.logcommands;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.commands.Command;
import myfitnesspal.items.Food;
import myfitnesspal.items.FoodLog;
import myfitnesspal.items.Recipe;
import myfitnesspal.items.RecipeItem;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;
import myfitnesspal.parser.Parser;
import myfitnesspal.utility.PromptUtils;

import java.time.LocalDate;
import java.util.List;

public final class LogRecipeCommand implements Command {
    private final MyFitnessTracker tracker;
    private final InputProvider input;
    private final OutputWriter out;

    public LogRecipeCommand(MyFitnessTracker tracker,
                            InputProvider input,
                            OutputWriter out) {
        this.tracker = tracker;
        this.input = input;
        this.out = out;
    }

    @Override
    public void execute() {
        out.writeln(">12. Log Recipe");

        LocalDate date = Parser.parseDate(PromptUtils.promptLine(input, out,
                ">When (date):"));
        String mealType = PromptUtils.promptLine(input, out,
                ">When (meal) [Breakfast/Lunch/Snacks/Dinner]:");

        Recipe recipe = chooseRecipe();
        double servings = PromptUtils.promptDouble(input, out,
                ">Number of servings consumed:");

        logRecipeItems(date, mealType, recipe, servings);
        out.write("Logged recipe successfully: " + recipe.name()
                + " x " + servings
                + " serving(s).");
    }

    private Recipe chooseRecipe() {
        List<Recipe> allRecipes = tracker.getRecipes();
        if (allRecipes.isEmpty()) {
            throw new IllegalArgumentException(
                    "No recipes available. Please create a recipe first.");
        }

        for (int i = 0; i < allRecipes.size(); i++) {
            out.write((i + 1) + ". " + allRecipes.get(i));
        }

        int choice = PromptUtils.promptInt(input, out,
                ">Which recipe (recipe id):");
        if (choice < 1 || choice > allRecipes.size()) {
            throw new IllegalArgumentException("Invalid recipe ID.");
        }

        return allRecipes.get(choice - 1);
    }

    private void logRecipeItems(LocalDate date, String mealType,
                                Recipe recipe, double servings) {
        for (RecipeItem ri : recipe.items()) {
            double finalServings = ri.servings() * servings;

            Food matchedFood = tracker.getFoods().stream()
                    .filter(f -> f.name().equals(ri.foodName()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Recipe references unknown food: "
                                    + ri.foodName()));

            double totalUnits   = matchedFood.unitsPerServing() * finalServings;
            double totalCals    = matchedFood.calories() * finalServings;
            double totalCarbs   = matchedFood.carbs() * finalServings;
            double totalFat     = matchedFood.fat() * finalServings;
            double totalProtein = matchedFood.protein() * finalServings;

            FoodLog foodLog = new FoodLog(
                    date,
                    mealType,
                    matchedFood.name(),
                    totalUnits,
                    totalCals,
                    totalCarbs,
                    totalFat,
                    totalProtein
            );
            tracker.addItem(foodLog);
        }
    }
}
