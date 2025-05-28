package myfitnesspal.commands.logcommands;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.commands.Command;
import myfitnesspal.items.Food;
import myfitnesspal.items.FoodLog;
import myfitnesspal.items.Meal;
import myfitnesspal.items.MealItem;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;
import myfitnesspal.parser.Parser;
import myfitnesspal.utility.PromptUtils;

import java.time.LocalDate;
import java.util.List;

public final class LogMealCommand implements Command {
    private final MyFitnessTracker tracker;
    private final InputProvider inputProvider;
    private final OutputWriter outputWriter;

    public LogMealCommand(MyFitnessTracker tracker,
                          InputProvider inputProvider,
                          OutputWriter outputWriter) {
        this.tracker = tracker;
        this.inputProvider = inputProvider;
        this.outputWriter = outputWriter;
    }

    @Override
    public void execute() {
        outputWriter.writeln(">9. Log Meal");

        LocalDate date = Parser.parseDate(PromptUtils.promptLine(inputProvider,
                outputWriter,
                ">When (date):"));
        String mealType = PromptUtils.promptLine(inputProvider, outputWriter,
                ">When (meal) [Breakfast/Lunch/Snacks/Dinner]:");

        Meal chosenMeal = pickMeal();
        double mealServings = PromptUtils.promptDouble(
                inputProvider, outputWriter,
                ">Number of serving(s) for this meal:");

        logMealItems(date, mealType, chosenMeal, mealServings);
        outputWriter.write("Logged meal successfully: "
                + chosenMeal.name() + " x "
                + mealServings + " serving(s).");
    }

    private Meal pickMeal() {
        List<Meal> allMeals = tracker.getMeals();
        if (allMeals.isEmpty()) {
            throw new IllegalArgumentException(
                    "No meals in the system. Please create a meal first.");
        }

        for (int i = 0; i < allMeals.size(); i++) {
            outputWriter.write((i + 1) + ". " + allMeals.get(i));
        }

        int chosenId = PromptUtils.promptInt(inputProvider, outputWriter,
                ">Which meal (meal id):");
        if (chosenId < 1 || chosenId > allMeals.size()) {
            throw new IllegalArgumentException("Invalid meal ID.");
        }

        Meal chosenMeal = allMeals.get(chosenId - 1);
        outputWriter.write(">" + chosenId + ". " + chosenMeal);
        return chosenMeal;
    }

    private void logMealItems(LocalDate date, String mealType,
                              Meal chosenMeal, double mealServings) {
        for (MealItem mi : chosenMeal.items()) {
            double finalServings = mi.servings() * mealServings;

            Food matchedFood = tracker.getFoods().stream()
                    .filter(f -> f.name().equals(mi.foodName()))
                    .findFirst()
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Meal references unknown food: "
                                            + mi.foodName()));

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
