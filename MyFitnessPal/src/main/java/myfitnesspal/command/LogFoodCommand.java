package myfitnesspal.command;

import myfitnesspal.Food;
import myfitnesspal.FoodLog;
import myfitnesspal.MyFitnessTracker;
import myfitnesspal.utility.InputReader;
import myfitnesspal.utility.Parser;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class LogFoodCommand implements Command {
    private final MyFitnessTracker tracker;
    private final Scanner scanner;
    private final String fileName;
    private final InputReader inputReader;

    public LogFoodCommand(MyFitnessTracker tracker, Scanner scanner, String fileName) {
        this.tracker = tracker;
        this.scanner = scanner;
        this.fileName = fileName;
        this.inputReader = new InputReader(scanner);
    }

    @Override
    public void execute() {
        System.out.println(">5. Log Food");

        Food chosenFood = chooseFood();

        double[] totals = promptTotals(chosenFood);

        FoodLog foodLog = new FoodLog(
                promptDate(),
                promptMeal(),
                chosenFood.name(),
                totals[0],
                totals[1],
                totals[2],
                totals[3],
                totals[4]
        );

        tracker.addItem(foodLog);
        System.out.println("Logged successfully:\n" + foodLog);
    }

    private LocalDate promptDate() {
        System.out.print(">When (date):\n-");
        String rawDate = scanner.nextLine();
        return  Parser.parseDate(rawDate);

    }

    private String promptMeal() {
        System.out.print(">When (meal) [Breakfast/Lunch/Snacks/Dinner]:\n-");
        String meal = scanner.nextLine().trim();
        if (meal.isEmpty()) {
            throw new IllegalArgumentException("Invalid meal type!");
        }
        return meal;
    }

    private Food chooseFood() {
        List<Food> allFoods = tracker.getFoods();
        if (allFoods.isEmpty()) {
            throw new IllegalArgumentException("No foods in the system. Please create a food first.");
        }

        for (int i = 0; i < allFoods.size(); i++) {
            System.out.println((i + 1) + ". " + allFoods.get(i));
        }

        System.out.print(">Which food (food id):\n-");
        int chosenId = inputReader.readInt();
        if (chosenId < 1 || chosenId > allFoods.size()) {
            throw new IllegalArgumentException("Invalid food ID.");
        }

        Food chosenFood = allFoods.get(chosenId - 1);
        System.out.println(">" + chosenId + ". " + chosenFood);
        return chosenFood;
    }
    private double[] promptTotals(Food chosenFood) {
        System.out.print("(Either)\n>Number of serving(s):\n-");
        String line = scanner.nextLine().trim();

        double totalGrams;
        double totalCalories;
        double totalCarbs;
        double totalFat;
        double totalProtein;

        if (!line.isEmpty()) {
            double numServings;
            try {
                numServings = Double.parseDouble(line);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid number of servings: " + line);
            }
            totalGrams    = chosenFood.servingSize() * numServings;
            totalCalories = chosenFood.calories()    * numServings;
            totalCarbs    = chosenFood.carbs()       * numServings;
            totalFat      = chosenFood.fat()         * numServings;
            totalProtein  = chosenFood.protein()     * numServings;

        } else {
            System.out.print("(Or)\n>Serving size (g):\n-");
            double grams = inputReader.readDouble();
            if (grams <= 0) {
                throw new IllegalArgumentException("Invalid gram amount!");
            }

            double factor = grams / chosenFood.servingSize();
            totalGrams    = grams;
            totalCalories = chosenFood.calories() * factor;
            totalCarbs    = chosenFood.carbs()    * factor;
            totalFat      = chosenFood.fat()      * factor;
            totalProtein  = chosenFood.protein()  * factor;
        }

        return new double[] {
                totalGrams,
                totalCalories,
                totalCarbs,
                totalFat,
                totalProtein
        };
    }
}
