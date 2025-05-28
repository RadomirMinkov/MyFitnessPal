package myfitnesspal.commands.usercommands;

import myfitnesspal.commands.Command;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;

public final class ChangeUserCommand implements Command {
    private final InputProvider in;
    private final OutputWriter out;
    private final Runnable switchUser;

    public ChangeUserCommand(InputProvider in,
                             OutputWriter out,
                             Runnable switchUser) {
        this.in = in;
        this.out = out;
        this.switchUser = switchUser;
    }

    @Override
    public void execute() {
        switchUser.run();
    }
}
