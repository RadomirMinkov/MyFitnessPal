package myfitnesspal.command;

import myfitnesspal.Food;
import myfitnesspal.MyFitnessTracker;
import myfitnesspal.utility.InputReader;

import java.util.Scanner;

public class CreateFoodCommand implements Command {
    private final MyFitnessTracker tracker;
    private final Scanner scanner;
    private final String fileName;
    private final InputReader inputReader;

    public CreateFoodCommand(MyFitnessTracker tracker, Scanner scanner, String fileName) {
        this.tracker = tracker;
        this.scanner = scanner;
        this.fileName = fileName;
        this.inputReader = new InputReader(scanner);
    }

    @Override
    public void execute() {
        System.out.println(">3. Create Food");

        System.out.print(">Name:\n-");
        String name = scanner.nextLine();

        System.out.print(">Description(optional):\n-");
        String description = scanner.nextLine();

        System.out.print(">Serving Size (g):\n-");
        double servingSize = inputReader.readDouble();

        System.out.print(">Servings per container:\n-");
        int servings = inputReader.readInt();

        System.out.print(">Amount per serving:");
        System.out.print("\n>Calories (kcal):\n-");
        double calories = inputReader.readDouble();

        System.out.print(">Carbs (g):\n-");
        double carbs = inputReader.readDouble();

        System.out.print(">Fat (g):\n-");
        double fat = inputReader.readDouble();

        System.out.print(">Protein (g):\n-");
        double protein = inputReader.readDouble();

        Food newFood = new Food(name, description, servingSize, servings,
                calories, carbs, fat, protein);

        tracker.addItem(newFood);

        System.out.println(">Food created successfully!");
    }

}
