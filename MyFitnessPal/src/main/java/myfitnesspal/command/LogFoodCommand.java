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

        System.out.print(">When (date):\n-");
        String rawDate = scanner.nextLine();
        LocalDate date = Parser.parseDate(rawDate);
        if (date == null) {
            System.out.println("Invalid date: " + rawDate);
            return;
        }

        System.out.print(">When (meal) [Breakfast/Lunch/Snacks/Dinner]:\n-");
        String meal = scanner.nextLine().trim();
        if (meal.isEmpty()) {
            System.out.println("Invalid meal type!");
            return;
        }

        List<Food> allFoods = tracker.getFoods();
        if (allFoods.isEmpty()) {
            System.out.println("No foods in the system. Please create a food first.");
            return;
        }

        int index = 1;
        for (Food f : allFoods) {
            System.out.println(index + ". " + f);
            index++;
        }

        System.out.print(">Which food (food id):\n-");
        int chosenId = inputReader.readInt();
        if (chosenId < 1 || chosenId > allFoods.size()) {
            System.out.println("Invalid food ID.");
            return;
        }
        Food chosenFood = allFoods.get(chosenId - 1);
        System.out.println(">" + chosenId + ". " + chosenFood);

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
                System.out.println("Invalid number of servings: " + line);
                return;
            }
            totalGrams    = chosenFood.servingSize() * numServings;
            totalCalories = chosenFood.calories() * numServings;
            totalCarbs    = chosenFood.carbs()    * numServings;
            totalFat      = chosenFood.fat()      * numServings;
            totalProtein  = chosenFood.protein()  * numServings;
        } else {

            System.out.print("(Or)\n>Serving size (g):\n-");
            double grams = inputReader.readDouble();
            if (grams <= 0) {
                System.out.println("Invalid gram amount!");
                return;
            }

            double factor = grams / chosenFood.servingSize();

            totalGrams    = grams;
            totalCalories = chosenFood.calories() * factor;
            totalCarbs    = chosenFood.carbs()    * factor;
            totalFat      = chosenFood.fat()      * factor;
            totalProtein  = chosenFood.protein()  * factor;
        }

        FoodLog foodLog = new FoodLog(
                date,
                meal,
                chosenFood.name(),
                totalGrams,
                totalCalories,
                totalCarbs,
                totalFat,
                totalProtein
        );

        tracker.addItem(foodLog);
        tracker.save(fileName);

        System.out.println("Logged successfully:\n" + foodLog);
    }
}
