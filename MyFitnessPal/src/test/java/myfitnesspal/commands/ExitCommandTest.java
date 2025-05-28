package myfitnesspal.commands;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.utility.OutputWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class ExitCommandTest {

    @Test
    void testExecuteWithoutOnExit() {
        MyFitnessTracker tracker = Mockito.mock(MyFitnessTracker.class);
        OutputWriter outputWriter = Mockito.mock(OutputWriter.class);

        ExitCommand command = new ExitCommand(
                null,
                tracker,
                outputWriter,
                "testfile.txt"
        );

        command.execute();

        verify(tracker).save("testfile.txt");

        verify(outputWriter).write("Exiting the application...");

        verifyNoMoreInteractions(tracker, outputWriter);
    }

    @Test
    void testExecuteWithOnExit() {
        MyFitnessTracker tracker = mock(MyFitnessTracker.class);
        OutputWriter outputWriter = mock(OutputWriter.class);

        final boolean[] onExitCalled = {false};
        Runnable onExit = () -> onExitCalled[0] = true;

        ExitCommand command = new ExitCommand(
                onExit,
                tracker,
                outputWriter,
                "testfile.txt"
        );

        command.execute();

        verify(tracker).save("testfile.txt");
        verify(outputWriter).write("Exiting the application...");

        Assertions.assertTrue(onExitCalled[0],
                "onExit callback should be invoked.");

        verifyNoMoreInteractions(tracker, outputWriter);
    }

    @Test
    void testSetOnExit() {
        MyFitnessTracker tracker = mock(MyFitnessTracker.class);
        OutputWriter outputWriter = mock(OutputWriter.class);

        Runnable originalOnExit = () -> { };
        ExitCommand command = new ExitCommand(
                originalOnExit,
                tracker,
                outputWriter,
                "testfile.txt"
        );

        final boolean[] newOnExitCalled = {false};
        Runnable newOnExit = () -> newOnExitCalled[0] = true;
        command.setOnExit(newOnExit);

        command.execute();

        verify(tracker).save("testfile.txt");
        verify(outputWriter).write("Exiting the application...");

        Assertions.assertTrue(newOnExitCalled[0],
                "The new onExit callback should be invoked.");

        verifyNoMoreInteractions(tracker, outputWriter);
    }
}
