package myfitnesspal.command;

import myfitnesspal.Food;
import myfitnesspal.MyFitnessTracker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class ViewAllFoodsCommandTest {

    @Test
    void testExecuteNoFoods() {
        MyFitnessTracker tracker = new MyFitnessTracker();
        ViewAllFoodsCommand cmd = new ViewAllFoodsCommand(tracker);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        cmd.execute();

        System.setOut(System.out);

        String output = outputStream.toString();
        Assertions.assertTrue(output.contains("No foods found."));
    }

    @Test
    void testExecuteWithFoods() {
        MyFitnessTracker tracker = new MyFitnessTracker();
        tracker.addItem(new Food("Pizza", "Desc",
                100, 2, 300, 30, 10, 15));
        tracker.addItem(new Food("Burger", "DescB",
                150, 1, 400, 40, 20, 25));

        ViewAllFoodsCommand cmd = new ViewAllFoodsCommand(tracker);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        cmd.execute();

        System.setOut(System.out);

        String output = outputStream.toString();
        Assertions.assertTrue(output.contains("1. Pizza"));
        Assertions.assertTrue(output.contains("2. Burger"));
    }
}
