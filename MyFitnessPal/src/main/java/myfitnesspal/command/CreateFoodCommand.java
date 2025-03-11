package myfitnesspal.command;

import myfitnesspal.Food;
import myfitnesspal.MyFitnessTracker;

import java.util.Scanner;

public class CreateFoodCommand implements Command {
    private final MyFitnessTracker tracker;
    private final Scanner scanner;
    private final String fileName;

    public CreateFoodCommand(MyFitnessTracker tracker, Scanner scanner, String fileName) {
        this.tracker = tracker;
        this.scanner = scanner;
        this.fileName = fileName;
    }

    @Override
    public void execute() {
        System.out.println(">3. Create Food");

        System.out.print(">Name:\n-");
        String name = scanner.nextLine();

        System.out.print(">Description(optional):\n-");
        String description = scanner.nextLine();

        System.out.print(">Serving Size (g):\n-");
        double servingSize = readDouble();

        System.out.print(">Servings per container:\n-");
        int servings = readInt();

        System.out.print(">Amount per serving:");
        System.out.print("\n>Calories (kcal):\n-");
        double calories = readDouble();

        System.out.print(">Carbs (g):\n-");
        double carbs = readDouble();

        System.out.print(">Fat (g):\n-");
        double fat = readDouble();

        System.out.print(">Protein (g):\n-");
        double protein = readDouble();

        Food newFood = new Food(name, description, servingSize, servings,
                calories, carbs, fat, protein);

        tracker.addItem(newFood);
        tracker.save(fileName);

        System.out.println(">Food created successfully!");
    }

    private double readDouble() {
        while (true) {
            String input = scanner.nextLine();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.print("Invalid number, try again:\n-");
            }
        }
    }

    private int readInt() {
        while (true) {
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Invalid integer, try again:\n-");
            }
        }
    }
}
