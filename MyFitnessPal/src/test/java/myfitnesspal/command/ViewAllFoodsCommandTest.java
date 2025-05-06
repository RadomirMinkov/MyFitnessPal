package myfitnesspal.command;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.items.Food;
import myfitnesspal.items.MeasurementType;
import myfitnesspal.utility.OutputWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        tracker = mock(MyFitnessTracker.class);
        outputWriter = mock(OutputWriter.class);
    }

    @Test
    void testExecuteNoFoods() {
        when(tracker.getFoods()).thenReturn(new ArrayList<>());

        ViewAllFoodsCommand command =
                new ViewAllFoodsCommand(tracker, outputWriter);
        command.execute();

        verify(outputWriter).write(">4. View All Foods");
        verify(outputWriter).write("No foods found.");
        verifyNoMoreInteractions(outputWriter);
    }

    @Test
    void testExecuteWithFoods() {
        List<Food> foods = new ArrayList<>();
        foods.add(new Food("Apple", "desc",
                MeasurementType.GRAM, 100,
                1, 52, 14, 0.2, 0.3));
        foods.add(new Food("Bread", "desc",
                MeasurementType.PIECE, 50,
                4, 120, 25, 2, 4));

        when(tracker.getFoods()).thenReturn(foods);

        ViewAllFoodsCommand command =
                new ViewAllFoodsCommand(tracker, outputWriter);
        command.execute();

        verify(outputWriter).write(">4. View All Foods");
        verify(outputWriter)
                .write("1. Apple (1 serving = "
                        + "100.00 g, 52 kcal, 14.00g, 0.20g, 0.30g)");
        verify(outputWriter)
                .write("2. Bread (1 serving = "
                        + "50.00 pcs, 120 kcal, 25.00g, 2.00g, 4.00g)");
        verifyNoMoreInteractions(outputWriter);
    }
}
