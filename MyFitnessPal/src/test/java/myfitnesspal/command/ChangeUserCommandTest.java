package myfitnesspal.command;

import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

class ChangeUserCommandTest {

    @Test
    void testExecuteCallsSwitchUser() {
        InputProvider input = mock(InputProvider.class);
        OutputWriter output = mock(OutputWriter.class);
        Runnable switchUser = mock(Runnable.class);

        ChangeUserCommand command =
                new ChangeUserCommand(input, output, switchUser);
        command.execute();

        verify(switchUser).run();
        verifyNoInteractions(input, output);
    }
}
