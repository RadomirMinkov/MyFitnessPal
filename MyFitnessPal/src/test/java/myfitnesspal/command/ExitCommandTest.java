package myfitnesspal.command;

import myfitnesspal.MyFitnessTracker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExitCommandTest {

    @Test
    void testExecuteSavesAndCallsOnExit() {
        MyFitnessTracker tracker = new MyFitnessTracker();

        final boolean[] exitCalled = {false};
        Runnable onExit = () -> exitCalled[0] = true;

        ExitCommand cmd = new ExitCommand(onExit, tracker, "testfile.txt");
        cmd.execute();

        Assertions.assertTrue(exitCalled[0]);
    }
}
