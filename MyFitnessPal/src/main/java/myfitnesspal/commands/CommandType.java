package myfitnesspal.commands;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.commands.createcommands.CreateFoodCommand;
import myfitnesspal.commands.createcommands.CreateMealCommand;
import myfitnesspal.commands.createcommands.CreateRecipeCommand;
import myfitnesspal.commands.createcommands.DrinkWaterCommand;
import myfitnesspal.commands.logcommands.LogBodyMeasurementCommand;
import myfitnesspal.commands.logcommands.LogFoodCommand;
import myfitnesspal.commands.logcommands.LogMealCommand;
import myfitnesspal.commands.logcommands.LogRecipeCommand;
import myfitnesspal.commands.viewcommands.CheckWaterCommand;
import myfitnesspal.commands.viewcommands.MeasurementReportCommand;
import myfitnesspal.commands.viewcommands.ViewAllFoodsCommand;
import myfitnesspal.commands.viewcommands.ViewAllLoggedCommand;
import myfitnesspal.commands.viewcommands.ViewAllMealsCommand;
import myfitnesspal.commands.viewcommands.ViewAllRecipesCommand;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;

public enum CommandType {

    DRINK_WATER("Drink water") {
        @Override
        public Command getCommand(MyFitnessTracker tracker,
                                  InputProvider inputProvider,
                                  OutputWriter outputWriter) {
            return new DrinkWaterCommand(tracker,
                    inputProvider, outputWriter);
        }
    },

    CHECK_WATER("Check water") {
        @Override
        public Command getCommand(MyFitnessTracker tracker,
                                  InputProvider inputProvider,
                                  OutputWriter outputWriter) {
            return new CheckWaterCommand(tracker, inputProvider, outputWriter);
        }
    },

    CREATE_FOOD("Create Food") {
        @Override
        public Command getCommand(MyFitnessTracker tracker,
                                  InputProvider inputProvider,
                                  OutputWriter outputWriter) {
            return new CreateFoodCommand(tracker,
                    inputProvider, outputWriter);
        }
    },

    VIEW_ALL_FOODS("View All Foods") {
        @Override
        public Command getCommand(MyFitnessTracker tracker,
                                  InputProvider inputProvider,
                                  OutputWriter outputWriter) {
            return new ViewAllFoodsCommand(tracker, outputWriter);
        }
    },

    LOG_FOOD("Log Food") {
        @Override
        public Command getCommand(MyFitnessTracker tracker,
                                  InputProvider inputProvider,
                                  OutputWriter outputWriter) {
            return new LogFoodCommand(tracker,
                    inputProvider, outputWriter);
        }
    },

    VIEW_ALL_LOGGED("View All Logged") {
        @Override
        public Command getCommand(MyFitnessTracker tracker,
                                  InputProvider inputProvider,
                                  OutputWriter outputWriter) {
            return new ViewAllLoggedCommand(tracker,
                    inputProvider, outputWriter);
        }
    },

    CREATE_MEAL("Create Meal") {
        @Override
        public Command getCommand(MyFitnessTracker tracker,
                                  InputProvider inputProvider,
                                  OutputWriter outputWriter) {
            return new CreateMealCommand(tracker,
                    inputProvider, outputWriter);
        }
    },

    VIEW_ALL_MEALS("View All Meals") {
        @Override
        public Command getCommand(MyFitnessTracker tracker,
                                  InputProvider inputProvider,
                                  OutputWriter outputWriter) {
            return new ViewAllMealsCommand(tracker, outputWriter);
        }
    },

    LOG_MEAL("Log Meal") {
        @Override
        public Command getCommand(MyFitnessTracker tracker,
                                  InputProvider inputProvider,
                                  OutputWriter outputWriter) {
            return new LogMealCommand(tracker, inputProvider,
                    outputWriter);
        }
    },
    CREATE_RECIPE("Create Recipe") {
        @Override
        public Command getCommand(MyFitnessTracker tracker,
                                  InputProvider inputProvider,
                                  OutputWriter outputWriter) {
            return new CreateRecipeCommand(tracker,
                    inputProvider, outputWriter);
        }
    },
    VIEW_ALL_RECIPES("View All Recipes") {
        @Override
        public Command getCommand(MyFitnessTracker tracker,
                                  InputProvider inputProvider,
                                  OutputWriter outputWriter) {
            return new ViewAllRecipesCommand(tracker, outputWriter);
        }
    },
    LOG_RECIPE("Log Recipe") {
        @Override
        public Command getCommand(MyFitnessTracker tracker,
                                  InputProvider inputProvider,
                                  OutputWriter outputWriter) {
            return new LogRecipeCommand(tracker,
                    inputProvider, outputWriter);
        }
    },
    LOG_BODY_MEASUREMENT("Log Body Measurement") {
        @Override
        public Command getCommand(MyFitnessTracker tracker,
                                  InputProvider inputProvider,
                                  OutputWriter outputWriter) {
            return new LogBodyMeasurementCommand(tracker,
                    inputProvider, outputWriter);
        }
    },
    MEASUREMENT_REPORT("Measurement report") {
        public Command getCommand(MyFitnessTracker tracker,
                                  InputProvider inputProvider,
                                  OutputWriter outputWriter) {
            return new MeasurementReportCommand(tracker,
                    inputProvider, outputWriter);
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
                                       OutputWriter outputWriter);
}
