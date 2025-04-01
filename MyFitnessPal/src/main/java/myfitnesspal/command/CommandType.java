package myfitnesspal.command;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;

public enum CommandType {

    DRINK_WATER("Drink water") {
        @Override
        public Command getCommand(MyFitnessTracker tracker,
                                  InputProvider inputProvider,
                                  OutputWriter outputWriter,
                                  String fileName) {
            return new DrinkWaterCommand(tracker,
                    inputProvider, outputWriter, fileName);
        }
    },

    CHECK_WATER("Check water") {
        @Override
        public Command getCommand(MyFitnessTracker tracker,
                                  InputProvider inputProvider,
                                  OutputWriter outputWriter,
                                  String fileName) {
            return new CheckWaterCommand(tracker, inputProvider, outputWriter);
        }
    },

    CREATE_FOOD("Create Food") {
        @Override
        public Command getCommand(MyFitnessTracker tracker,
                                  InputProvider inputProvider,
                                  OutputWriter outputWriter,
                                  String fileName) {
            return new CreateFoodCommand(tracker,
                    inputProvider, outputWriter, fileName);
        }
    },

    VIEW_ALL_FOODS("View All Foods") {
        @Override
        public Command getCommand(MyFitnessTracker tracker,
                                  InputProvider inputProvider,
                                  OutputWriter outputWriter,
                                  String fileName) {
            return new ViewAllFoodsCommand(tracker, outputWriter);
        }
    },

    LOG_FOOD("Log Food") {
        @Override
        public Command getCommand(MyFitnessTracker tracker,
                                  InputProvider inputProvider,
                                  OutputWriter outputWriter,
                                  String fileName) {
            return new LogFoodCommand(tracker,
                    inputProvider, outputWriter, fileName);
        }
    },

    VIEW_LOGGED_FOODS("View Foods Logged") {
        @Override
        public Command getCommand(MyFitnessTracker tracker,
                                  InputProvider inputProvider,
                                  OutputWriter outputWriter,
                                  String fileName) {
            return new ViewLoggedFoodsCommand(tracker,
                    inputProvider, outputWriter);
        }
    },

    EXIT("Exit") {
        @Override
        public Command getCommand(MyFitnessTracker tracker,
                                  InputProvider inputProvider,
                                  OutputWriter outputWriter,
                                  String fileName) {
            return new ExitCommand(() -> { }, tracker, outputWriter, fileName);
        }
    };

    private final String description;

    CommandType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public abstract Command getCommand(MyFitnessTracker tracker,
                                       InputProvider inputProvider,
                                       OutputWriter outputWriter,
                                       String fileName);
}
