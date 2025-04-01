package myfitnesspal.command;

import myfitnesspal.Food;
import myfitnesspal.MyFitnessTracker;
import myfitnesspal.utility.OutputWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class ViewAllFoodsCommandTest {

    private MyFitnessTracker tracker;
    private OutputWriter outputWriter;

    @BeforeEach
    void setUp() {
        tracker = Mockito.mock(MyFitnessTracker.class);
        outputWriter = mock(OutputWriter.class);
    }

    @Test
    void testExecuteNoFoods() {
        when(tracker.getFoods()).thenReturn(new ArrayList<>());

        ViewAllFoodsCommand command = new
                ViewAllFoodsCommand(tracker, outputWriter);
        command.execute();

        verify(outputWriter).write(">4. View All Foods");
        verify(outputWriter).write("No foods found.");
        verifyNoMoreInteractions(outputWriter);
    }

    @Test
    void testExecuteWithFoods() {
        List<Food> foods = new ArrayList<>();
        foods.add(new Food("Apple", "desc",
                100, 1, 52,
                14, 0.2, 0.3));
        foods.add(new Food("Bread", "desc",
                50,
                4, 120, 25,
                2, 4));

        when(tracker.getFoods()).thenReturn(foods);

        ViewAllFoodsCommand command =
                new ViewAllFoodsCommand(tracker, outputWriter);
        command.execute();

        verify(outputWriter).write(">4. View All Foods");
        verify(outputWriter).write("1. " + foods.get(0));
        verify(outputWriter).write("2. " + foods.get(1));
        verifyNoMoreInteractions(outputWriter);
    }
}
