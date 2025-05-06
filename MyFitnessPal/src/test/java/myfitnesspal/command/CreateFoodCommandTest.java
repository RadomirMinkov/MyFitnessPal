package myfitnesspal.command;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.items.Food;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CreateFoodCommandTest {

    @Test
    void testCreateFoodWithGramsPer100() {
        MyFitnessTracker tracker = new MyFitnessTracker();
        InputProvider input = mock(InputProvider.class);
        OutputWriter out = mock(OutputWriter.class);

        when(input.readLine())
                .thenReturn("Rice",
                        "White grain", "1", "125",
                        "1", "360", "80", "2", "6");

        CreateFoodCommand cmd = new CreateFoodCommand(tracker, input, out);
        cmd.execute();

        Food food = tracker.getFoods().get(0);
        assertEquals("Rice", food.name());
        assertEquals(125.0, food.unitsPerServing(), 0.01);
        assertEquals(3.6, food.calories(), 0.01);
        assertEquals(0.8, food.carbs(), 0.01);
        assertEquals(0.02, food.fat(), 0.01);
        assertEquals(0.06, food.protein(), 0.01);
    }

    @Test
    void testCreateFoodWithMlPerServing() {
        MyFitnessTracker tracker = new MyFitnessTracker();
        InputProvider input = mock(InputProvider.class);
        OutputWriter out = mock(OutputWriter.class);

        when(input.readLine())
                .thenReturn("Milk", "Dairy", "2",
                        "250", "2", "130", "10", "5", "7");

        CreateFoodCommand cmd = new CreateFoodCommand(tracker, input, out);
        cmd.execute();

        Food food = tracker.getFoods().get(0);
        assertEquals("Milk", food.name());
        assertEquals(250.0, food.unitsPerServing(), 0.01);
        assertEquals(130.0, food.calories(), 0.01);
    }

    @Test
    void testCreateFoodWithPiecesPerPiece() {
        MyFitnessTracker tracker = new MyFitnessTracker();
        InputProvider input = mock(InputProvider.class);
        OutputWriter out = mock(OutputWriter.class);

        when(input.readLine())
                .thenReturn("Cookie", "Chocolate chip",
                        "3", "2", "1", "90", "15", "4", "1");

        CreateFoodCommand cmd = new CreateFoodCommand(tracker, input, out);
        cmd.execute();

        Food food = tracker.getFoods().get(0);
        assertEquals("Cookie", food.name());
        assertEquals(2.0, food.unitsPerServing(), 0.01);
        assertEquals(90.0, food.calories(), 0.01);
    }

    @Test
    void testCreateFoodWithPiecesPerServing() {
        MyFitnessTracker tracker = new MyFitnessTracker();
        InputProvider input = mock(InputProvider.class);
        OutputWriter out = mock(OutputWriter.class);

        when(input.readLine())
                .thenReturn("Cookie", "Chocolate chip",
                        "3", "2", "2", "180", "30", "8", "2");

        CreateFoodCommand cmd = new CreateFoodCommand(tracker, input, out);
        cmd.execute();

        Food food = tracker.getFoods().get(0);
        assertEquals("Cookie", food.name());
        assertEquals(2.0, food.unitsPerServing(), 0.01);
        assertEquals(90.0, food.calories(), 0.01);
    }
}
