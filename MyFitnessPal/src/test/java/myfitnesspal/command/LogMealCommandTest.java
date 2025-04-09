package myfitnesspal.command;

import myfitnesspal.Food;
import myfitnesspal.FoodLog;
import myfitnesspal.Meal;
import myfitnesspal.MealItem;
import myfitnesspal.MyFitnessTracker;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.startsWith;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LogMealCommandTest {

    @Test
    void testExecuteSuccess() {
        MyFitnessTracker tracker = mock(MyFitnessTracker.class);
        InputProvider inputProvider = mock(InputProvider.class);
        OutputWriter outputWriter = mock(OutputWriter.class);

        Food foodA = new Food("FoodA", "desc",
                100, 1,
                200, 20, 10, 5);
        Food foodB = new Food("FoodB", "desc",
                50, 1, 100,
                10, 2, 3);

        List<Food> foods = new ArrayList<>();
        foods.add(foodA);
        foods.add(foodB);
        when(tracker.getFoods()).thenReturn(foods);

        List<MealItem> items = new ArrayList<>();
        items.add(new MealItem("FoodA", 1.0));
        items.add(new MealItem("FoodB", 0.5));
        Meal meal = new Meal("MixedMeal", "desc",
                150, 300, 30,
                15, 8, items);

        List<Meal> meals = new ArrayList<>();
        meals.add(meal);
        when(tracker.getMeals()).thenReturn(meals);

        when(inputProvider.readLine())
                .thenReturn("2025-05-01")
                .thenReturn("Lunch")
                .thenReturn("1")
                .thenReturn("2");

        LogMealCommand command = new LogMealCommand(tracker,
                inputProvider, outputWriter, "testfile.txt");
        command.execute();

        verify(outputWriter).write(">9. Log Meal");
        verify(outputWriter).write(startsWith(">When (date):"));
        verify(outputWriter).write(startsWith(">When (meal)"));
        verify(outputWriter).write("1. " + meal);
        verify(outputWriter).write(">Which meal (meal id):\n-");
        verify(outputWriter).write(">Number of serving(s) for this meal:\n-");
        verify(outputWriter).write(
                "Logged meal successfully: MixedMeal x 2.0 serving(s).");

        verify(tracker).addItem(argThat(t -> {
            if (!(t instanceof FoodLog fl)) {
                return false;
            }
            return fl.foodName().equals("FoodA")
                    && fl.meal().equals("Lunch")
                    && fl.date().equals(LocalDate.of(2025, 5, 1))
                    && fl.totalGrams() == 200.0
                    && fl.totalCalories() == 400.0;
        }));

        verify(tracker).addItem(argThat(t -> {
            if (!(t instanceof FoodLog fl)) {
                return false;
            }
            return fl.foodName().equals("FoodB")
                    && fl.meal().equals("Lunch")
                    && fl.date().equals(LocalDate.of(2025, 5, 1))
                    && fl.totalGrams() == 50.0
                    && fl.totalCalories() == 100.0;
        }));
    }

    @Test
    void testExecuteNoMeals() {
        MyFitnessTracker tracker = mock(MyFitnessTracker.class);
        InputProvider inputProvider = mock(InputProvider.class);
        OutputWriter outputWriter = mock(OutputWriter.class);

        when(tracker.getMeals()).thenReturn(new ArrayList<>());
        when(inputProvider.readLine())
                .thenReturn("2025-04-01")
                .thenReturn("Lunch");

        LogMealCommand command = new LogMealCommand(tracker,
                inputProvider, outputWriter, "testfile.txt");
        Assertions.assertThrows(IllegalArgumentException.class,
                command::execute);

        verify(outputWriter).write(">9. Log Meal");
    }

    @Test
    void testExecuteInvalidMealId() {
        MyFitnessTracker tracker = mock(MyFitnessTracker.class);
        InputProvider inputProvider = mock(InputProvider.class);
        OutputWriter outputWriter = mock(OutputWriter.class);

        Meal meal = new Meal("TestMeal", "",
                100, 200, 20,
                10, 5, new ArrayList<>());
        List<Meal> mealList = new ArrayList<>();
        mealList.add(meal);

        when(tracker.getMeals()).thenReturn(mealList);
        when(inputProvider.readLine())
                .thenReturn("2025-04-01")
                .thenReturn("Dinner")
                .thenReturn("5")
                .thenReturn("1");

        LogMealCommand command = new LogMealCommand(tracker,
                inputProvider, outputWriter, "file.txt");
        Assertions.assertThrows(IllegalArgumentException.class,
                command::execute);

        verify(outputWriter).write(">9. Log Meal");
        verify(outputWriter).write("1. " + meal);
        verify(outputWriter).write(">Which meal (meal id):\n-");
    }

    @Test
    void testExecuteInvalidMealType() {
        MyFitnessTracker tracker = mock(MyFitnessTracker.class);
        InputProvider inputProvider = mock(InputProvider.class);
        OutputWriter outputWriter = mock(OutputWriter.class);

        when(tracker.getMeals()).thenReturn(new ArrayList<>());
        when(inputProvider.readLine())
                .thenReturn("2025-04-01")
                .thenReturn("");

        LogMealCommand command = new LogMealCommand(tracker,
                inputProvider, outputWriter, "test.txt");
        Assertions.assertThrows(IllegalArgumentException.class,
                command::execute);

        verify(outputWriter).write(">9. Log Meal");
    }

    @Test
    void testExecuteInvalidServings() {
        MyFitnessTracker tracker = mock(MyFitnessTracker.class);
        InputProvider inputProvider = mock(InputProvider.class);
        OutputWriter outputWriter = mock(OutputWriter.class);

        Meal meal = new Meal("MealX", "",
                100, 200,
                20, 10, 5,
                List.of(new MealItem("FoodX", 1)));
        when(tracker.getMeals()).thenReturn(List.of(meal));
        when(tracker.getFoods()).thenReturn(List.of(
                new Food("FoodX", "",
                        100, 1,
                        200, 20, 10, 5)));

        when(inputProvider.readLine())
                .thenReturn("2025-04-01")
                .thenReturn("Breakfast")
                .thenReturn("1")
                .thenReturn("0");

        LogMealCommand command =
                new LogMealCommand(tracker,
                inputProvider, outputWriter,
                        "test.txt");
        Assertions.assertThrows(
                IllegalArgumentException.class, command::execute);

        verify(outputWriter).write(">9. Log Meal");
    }

    @Test
    void testExecuteUnknownFoodInMeal() {
        MyFitnessTracker tracker = mock(MyFitnessTracker.class);
        InputProvider inputProvider = mock(InputProvider.class);
        OutputWriter outputWriter = mock(OutputWriter.class);

        Meal meal = new Meal("MealRefBroken", "",
                100, 200, 20,
                10, 5,
                List.of(new MealItem("NonExistingFood", 1)));
        when(tracker.getMeals()).thenReturn(List.of(meal));
        when(tracker.getFoods()).thenReturn(new ArrayList<>());

        when(inputProvider.readLine())
                .thenReturn("2025-05-12")
                .thenReturn("Lunch")
                .thenReturn("1")
                .thenReturn("1");

        LogMealCommand command = new LogMealCommand(
                tracker, inputProvider, outputWriter,
                "file.txt");
        Assertions.assertThrows(IllegalArgumentException.class,
                command::execute);

        verify(outputWriter).write(">9. Log Meal");
    }
}
