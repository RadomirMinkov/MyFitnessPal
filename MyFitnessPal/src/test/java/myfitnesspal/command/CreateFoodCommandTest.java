package myfitnesspal.command;

import myfitnesspal.Food;
import myfitnesspal.MyFitnessTracker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Scanner;

class CreateFoodCommandTest {

    @Test
    void testExecuteValidInput() {
        String inputData = String.join("\n",
                "MyFood",
                "This is good",
                "100",
                "2",
                "300",
                "30",
                "10",
                "15"
        ) + "\n";
        Scanner scanner = new Scanner(
                new ByteArrayInputStream(inputData.getBytes()));
        MyFitnessTracker tracker = new MyFitnessTracker();

        CreateFoodCommand cmd = new
                CreateFoodCommand(tracker, scanner,
                "testfile.txt");
        cmd.execute();

        List<Food> foods = tracker.getFoods();
        Assertions.assertEquals(1, foods.size());
        Food createdFood = foods.get(0);
        Assertions.assertEquals("MyFood", createdFood.name());
        Assertions.assertEquals("This is good",
                createdFood.description());
        Assertions.assertEquals(100, createdFood.servingSize());
        Assertions.assertEquals(300, createdFood.calories());
    }
}
