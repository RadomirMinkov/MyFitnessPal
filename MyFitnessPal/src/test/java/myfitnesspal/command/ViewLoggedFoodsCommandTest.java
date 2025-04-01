package myfitnesspal.command;

import myfitnesspal.FoodLog;
import myfitnesspal.MyFitnessTracker;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class ViewLoggedFoodsCommandTest {

    private MyFitnessTracker tracker;
    private InputProvider inputProvider;
    private OutputWriter outputWriter;

    @BeforeEach
    void setUp() {
        tracker = mock(MyFitnessTracker.class);
        inputProvider = mock(InputProvider.class);
        outputWriter = mock(OutputWriter.class);
    }

    @Test
    void testExecuteWithLogs() {
        when(inputProvider.readLine()).thenReturn("2025-04-01");

        List<FoodLog> logs = new ArrayList<>();
        logs.add(new FoodLog(
                LocalDate.of(2025, 4,
                        1), "Lunch", "Bread",
                100, 200,
                40, 2, 6
        ));
        logs.add(new FoodLog(
                LocalDate.of(2025, 4,
                        1), "Dinner", "Apple",
                150, 78,
                20, 0.5, 1
        ));

        when(tracker.getFoodLogsForDate(LocalDate.of(
                2025, 4, 1)))
                .thenReturn(logs);

        ViewLoggedFoodsCommand command = new ViewLoggedFoodsCommand(
                tracker, inputProvider, outputWriter
        );
        command.execute();

        verify(outputWriter).write(">6. View Foods Logged");
        verify(outputWriter).write(">When (date):\n-");
        verify(outputWriter).write("Foods logged on 2025-04-01:");
        verify(outputWriter).write("- " + logs.get(0));
        verify(outputWriter).write("- " + logs.get(1));
        verifyNoMoreInteractions(outputWriter);
    }

    @Test
    void testExecuteNoLogs() {
        when(inputProvider.readLine()).thenReturn("2025-04-01");

        when(tracker.getFoodLogsForDate(LocalDate.of(
                2025, 4, 1)))
                .thenReturn(new ArrayList<>());

        ViewLoggedFoodsCommand command = new ViewLoggedFoodsCommand(
                tracker, inputProvider, outputWriter
        );

        Assertions.assertThrows(
                IllegalArgumentException.class,
                command::execute,
                "No foods logged for 2025-04-01"
        );

        verify(outputWriter).write(">6. View Foods Logged");
        verify(outputWriter).write(">When (date):\n-");
        verifyNoMoreInteractions(outputWriter);
    }
}
