package myfitnesspal.command;

import myfitnesspal.Food;
import myfitnesspal.Meal;
import myfitnesspal.MyFitnessTracker;
import myfitnesspal.utility.ConsoleOutputWriter;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.ScannerInputProvider;
import myfitnesspal.utility.OutputWriter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

class CreateMealCommandTest {

    private MyFitnessTracker tracker;
    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;
    private InputProvider inputProvider;
    private OutputWriter outputWriter;

    @BeforeEach
    void setUp() {
        tracker = new MyFitnessTracker();
        outContent = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        outputWriter = new ConsoleOutputWriter();
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testExecuteNoFoods() {
        String input = String.join("\n",
                "MyMeal",
                "OptionalDesc"
        ) + "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        inputProvider = new ScannerInputProvider(
                new java.util.Scanner(System.in));
        CreateMealCommand cmd = new CreateMealCommand(
                tracker, inputProvider, outputWriter, "testFile.txt");
        Assertions.assertThrows(IllegalArgumentException.class, cmd::execute);
    }

    @Test
    void testExecuteSingleFood() {
        Food f = new Food("TestFood",
                "desc", 50, 1,
                100, 10, 5, 3);
        tracker.addItem(f);
        String input = String.join("\n",
                "MyMeal",
                "OptionalDesc",
                "1",
                "2",
                "no"
        ) + "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        inputProvider = new ScannerInputProvider(
                new java.util.Scanner(System.in));
        CreateMealCommand cmd = new CreateMealCommand(
                tracker, inputProvider, outputWriter,
                "testFile.txt");
        cmd.execute();
        String output = outContent.toString();
        Assertions.assertTrue(output.contains(">Created Meal: MyMeal"));
        List<Meal> meals = tracker.getMeals();
        Assertions.assertEquals(1, meals.size());
        Meal created = meals.get(0);
        Assertions.assertEquals("MyMeal", created.name());
        Assertions.assertEquals(100.0,
                created.totalGrams(), 0.001);
        Assertions.assertEquals(200.0,
                created.totalCalories(), 0.001);
    }

    @Test
    void testExecuteMultipleFoods() {
        Food f1 = new Food("FoodA", "",
                100, 1,
                200, 20, 10, 5);
        Food f2 = new Food("FoodB", "",
                50, 1,
                100, 10, 2,
                4);
        tracker.addItem(f1);
        tracker.addItem(f2);
        String input = String.join("\n",
                "MegaMeal",
                "BigDesc",
                "1",
                "1",
                "yes",
                "2",
                "2",
                "no"
        ) + "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        inputProvider = new ScannerInputProvider(
                new java.util.Scanner(System.in));
        CreateMealCommand cmd = new CreateMealCommand(
                tracker, inputProvider,
                outputWriter, "testFile.txt");
        cmd.execute();
        String output = outContent.toString();
        Assertions.assertTrue(output.contains(
                ">Created Meal: MegaMeal"));
        List<Meal> meals = tracker.getMeals();
        Assertions.assertEquals(1, meals.size());
        Meal created = meals.get(0);
        Assertions.assertEquals("MegaMeal",
                created.name());
        Assertions.assertTrue(output.contains("1 x FoodA"));
        Assertions.assertTrue(output.contains("2 x FoodB"));
        Assertions.assertEquals(200.0,
                created.totalGrams(), 0.001);
        Assertions.assertEquals(400.0,
                created.totalCalories(), 0.001);
    }

    @Test
    void testExecuteInvalidFoodId() {
        Food f = new Food("TestFood", "",
                50, 1,
                100, 10, 5, 3);
        tracker.addItem(f);
        String input = String.join("\n",
                "TestMeal",
                "",
                "99"
        ) + "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        inputProvider = new ScannerInputProvider(
                new java.util.Scanner(System.in));
        CreateMealCommand cmd = new CreateMealCommand(
                tracker, inputProvider, outputWriter,
                "testFile.txt");
        Assertions.assertThrows(IllegalArgumentException.class, cmd::execute);
    }

    @Test
    void testExecuteInvalidServings() {
        Food f = new Food("TestFood",
                "", 50,
                1, 100,
                10, 5, 3);
        tracker.addItem(f);
        String input = String.join("\n",
                "TestMeal",
                "",
                "1",
                "-2"
        ) + "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        inputProvider = new ScannerInputProvider(
                new java.util.Scanner(System.in));
        CreateMealCommand cmd = new CreateMealCommand(tracker,
                inputProvider, outputWriter, "testFile.txt");
        Assertions.assertThrows(IllegalArgumentException.class, cmd::execute);
    }
}
