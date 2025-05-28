package myfitnesspal.commands;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.utility.OutputWriter;

public final class ExitCommand implements Command {
    private Runnable onExit;
    private final MyFitnessTracker tracker;
    private final String fileName;
    private final OutputWriter outputWriter;

    public ExitCommand(Runnable onExit,
                       MyFitnessTracker tracker,
                       OutputWriter outputWriter,
                       String fileName) {
        this.onExit = onExit;
        this.tracker = tracker;
        this.outputWriter = outputWriter;
        this.fileName = fileName;
    }

    public void setOnExit(Runnable onExit) {
        this.onExit = onExit;
    }

    @Override
    public void execute() {
        tracker.save(fileName);
        outputWriter.write("Exiting the application...");
        if (onExit != null) {
            onExit.run();
        }
    }
}
