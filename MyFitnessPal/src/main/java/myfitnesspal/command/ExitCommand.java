package myfitnesspal.command;

import myfitnesspal.MyFitnessTracker;

 public final class ExitCommand implements Command {

    private final Runnable onExit;
    private final MyFitnessTracker tracker;
    private final String fileName;

    public ExitCommand(Runnable onExit,
                       MyFitnessTracker tracker,
                       String fileName) {
        this.onExit = onExit;
        this.tracker = tracker;
        this.fileName = fileName;
    }

    @Override
    public void execute() {
        tracker.save(fileName);

        onExit.run();
    }
}
