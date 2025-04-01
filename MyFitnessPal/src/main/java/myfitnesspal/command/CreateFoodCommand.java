package myfitnesspal.command;

import myfitnesspal.Food;
import myfitnesspal.MyFitnessTracker;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;

public final class CreateFoodCommand implements Command {
    private final MyFitnessTracker tracker;
    private final InputProvider inputProvider;
    private final OutputWriter outputWriter;
    private final String fileName;

    public CreateFoodCommand(MyFitnessTracker tracker,
                             InputProvider inputProvider,
                             OutputWriter outputWriter,
                             String fileName) {
        this.tracker = tracker;
        this.inputProvider = inputProvider;
        this.outputWriter = outputWriter;
        this.fileName = fileName;
    }

    @Override
    public void execute() {
        outputWriter.write(">3. Create Food");

        outputWriter.write(">Name:\n-");
        String name = inputProvider.readLine();

        outputWriter.write(">Description(optional):\n-");
        String description = inputProvider.readLine();

        outputWriter.write(">Serving Size (g):\n-");
        double servingSize = Double.parseDouble(inputProvider.readLine());

        outputWriter.write(">Servings per container:\n-");
        int servings = Integer.parseInt(inputProvider.readLine());

        outputWriter.write(">Amount per serving:");
        outputWriter.write("\n>Calories (kcal):\n-");
        double calories = Double.parseDouble(inputProvider.readLine());

        outputWriter.write(">Carbs (g):\n-");
        double carbs = Double.parseDouble(inputProvider.readLine());

        outputWriter.write(">Fat (g):\n-");
        double fat = Double.parseDouble(inputProvider.readLine());

        outputWriter.write(">Protein (g):\n-");
        double protein = Double.parseDouble(inputProvider.readLine());

        Food newFood = new Food(
                name,
                description,
                servingSize,
                servings,
                calories,
                carbs,
                fat,
                protein
        );
        tracker.addItem(newFood);

        outputWriter.write(">Food created successfully!");
    }
}
