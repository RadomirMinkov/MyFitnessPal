package myfitnesspal;

import myfitnesspal.command.Command;
import myfitnesspal.command.CommandType;
import myfitnesspal.command.ExitCommand;
import myfitnesspal.utility.ConsoleOutputWriter;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;
import myfitnesspal.utility.ScannerInputProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public final class Application {
    private static final String FILE_NAME = "all_data.txt";

    private final MyFitnessTracker tracker;
    private final Scanner scanner;
    private final InputProvider inputProvider;
    private final OutputWriter outputWriter;
    private final Map<String, Command> commands;
    private boolean running;

    public Application() {
        this.tracker = new MyFitnessTracker();
        tracker.load(FILE_NAME);

        this.scanner = new Scanner(System.in);
        this.inputProvider = new ScannerInputProvider(scanner);
        this.outputWriter = new ConsoleOutputWriter();

        this.commands = new HashMap<>();
        initCommands();

        this.running = true;
    }

    private void initCommands() {
        CommandType[] commandValues = CommandType.values();
        for (int i = 0; i < commandValues.length; i++) {
            CommandType commandType = commandValues[i];
            commands.put(
                    String.valueOf(i + 1),
                    commandType.getCommand(tracker,
                            inputProvider, outputWriter, FILE_NAME)
            );
        }
        Command exitCommand = commands.get(String.valueOf(
                CommandType.EXIT.ordinal() + 1));
        if (exitCommand instanceof ExitCommand) {
            ExitCommand e = (ExitCommand) exitCommand;
            e.setOnExit(this::stop);
        }
    }

    public void run() {
        while (running) {
            try {
                printMenu();
                String choice = inputProvider.readLine();
                Command command = commands.get(choice);

                if (command != null) {
                    command.execute();
                } else {
                    outputWriter.write("Invalid input! Try again.");
                }
            } catch (Exception exception) {
                outputWriter.write(exception.getMessage());
            }
        }
        scanner.close();
        outputWriter.write("Program stopped.");
    }

    private void printMenu() {
        CommandType[] commandValues = CommandType.values();
        for (int i = 0; i < commandValues.length; i++) {
            outputWriter.writeln(">" + (i + 1) + ". "
                    + commandValues[i].getDescription());
        }
        outputWriter.write("-");
    }

    private void stop() {
        this.running = false;
    }
}
