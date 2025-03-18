package myfitnesspal.command;

import myfitnesspal.Food;
import myfitnesspal.FoodLog;
import myfitnesspal.MyFitnessTracker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Scanner;

class LogFoodCommandTest {

    @Test
    void testExecute_ValidByServings() {
        MyFitnessTracker tracker = new MyFitnessTracker();
        tracker.addItem(new Food("Pizza", "Cheesy slice",
                100, 2, 300, 30, 10, 15));

        String inputData = String.join("\n",
                "1",
                "2",
                "2025-03-19",
                "Lunch"
        ) + "\n";

        Scanner scanner = new Scanner(new ByteArrayInputStream(inputData.getBytes()));
        LogFoodCommand cmd = new LogFoodCommand(tracker, scanner, "testfile.txt");
        cmd.execute();

        List<FoodLog> logs = tracker.getFoodLogs();
        Assertions.assertEquals(1, logs.size());
        FoodLog fl = logs.get(0);

        Assertions.assertEquals("Pizza", fl.foodName());
        Assertions.assertEquals(200, fl.totalGrams());
        Assertions.assertEquals(600, fl.totalCalories());
        Assertions.assertEquals(30 * 2, fl.totalCarbs());
        Assertions.assertEquals(10 * 2, fl.totalFat());
        Assertions.assertEquals(15 * 2, fl.totalProtein());
    }

    @Test
    void testExecute_ValidByGrams() {

        MyFitnessTracker tracker = new MyFitnessTracker();
        tracker.addItem(new Food("Pizza", "Cheesy slice",
                100, 2, 300, 30, 10, 15));

        String inputData = String.join("\n",
                "1",
                "",
                "150",
                "2025-03-19",
                "Lunch"
        ) + "\n";

        Scanner scanner = new Scanner(new ByteArrayInputStream(inputData.getBytes()));
        LogFoodCommand cmd = new LogFoodCommand(tracker, scanner, "testfile.txt");
        cmd.execute();

        List<FoodLog> logs = tracker.getFoodLogs();
        Assertions.assertEquals(1, logs.size());
        FoodLog fl = logs.get(0);

        Assertions.assertEquals(150, fl.totalGrams());
        Assertions.assertEquals(300 * 1.5, fl.totalCalories());
        Assertions.assertEquals(30 * 1.5, fl.totalCarbs());
        Assertions.assertEquals(10 * 1.5, fl.totalFat());
        Assertions.assertEquals(15 * 1.5, fl.totalProtein());
    }

    @Test
    void testExecute_NoFoodsInSystem() {
        MyFitnessTracker tracker = new MyFitnessTracker();
        String inputData = "1\n2025-03-19\nLunch\n2\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(inputData.getBytes()));
        LogFoodCommand cmd = new LogFoodCommand(tracker, scanner, "testfile.txt");

        Assertions.assertThrows(IllegalArgumentException.class, cmd::execute);
    }
}
