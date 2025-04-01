package myfitnesspal.command;

import myfitnesspal.Food;
import myfitnesspal.MyFitnessTracker;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CreateFoodCommandTest {

    @Test
    void testExecuteValidInput() {
        MyFitnessTracker tracker = new MyFitnessTracker();

        InputProvider inputProvider = mock(InputProvider.class);
        OutputWriter outputWriter = mock(OutputWriter.class);

        when(inputProvider.readLine())
                .thenReturn("MyFood")
                .thenReturn("This is good")
                .thenReturn("100")
                .thenReturn("2")
                .thenReturn("300")
                .thenReturn("30")
                .thenReturn("10")
                .thenReturn("15");

        CreateFoodCommand command = new CreateFoodCommand(
                tracker, inputProvider, outputWriter, "testfile.txt"
        );

        command.execute();

        List<Food> foods = tracker.getFoods();
        Assertions.assertEquals(1, foods.size());

        Food createdFood = foods.get(0);
        Assertions.assertEquals("MyFood", createdFood.name());
        Assertions.assertEquals("This is good",
                createdFood.description());
        Assertions.assertEquals(100, createdFood.servingSize());
        Assertions.assertEquals(300, createdFood.calories());
        Assertions.assertEquals(30, createdFood.carbs());
        Assertions.assertEquals(10, createdFood.fat());
        Assertions.assertEquals(15, createdFood.protein());

        verify(outputWriter).write(">3. Create Food");
        verify(outputWriter).write(">Name:\n-");
        verify(outputWriter).write(">Food created successfully!");
    }
}
