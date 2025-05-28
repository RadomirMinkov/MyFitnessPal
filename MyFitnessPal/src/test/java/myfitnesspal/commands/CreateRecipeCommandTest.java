package myfitnesspal.commands;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.commands.createcommands.CreateRecipeCommand;
import myfitnesspal.items.Food;
import myfitnesspal.items.MeasurementType;
import myfitnesspal.items.Recipe;
import myfitnesspal.items.RecipeItem;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CreateRecipeCommandTest {

    private MyFitnessTracker tracker;
    private InputProvider inputProvider;
    private OutputWriter outputWriter;
    private CreateRecipeCommand command;

    @BeforeEach
    void setUp() {
        tracker = mock(MyFitnessTracker.class);
        inputProvider = mock(InputProvider.class);
        outputWriter = mock(OutputWriter.class);
        command = new CreateRecipeCommand(tracker, inputProvider, outputWriter);
    }

    @Test
    void testExecuteNoFoodsInSystem() {
        when(tracker.getFoods()).thenReturn(new ArrayList<>());
        when(inputProvider.readLine())
                .thenReturn("Banitsa")
                .thenReturn("Traditional")
                .thenReturn("6");

        assertThrows(IllegalArgumentException.class, command::execute);
        verify(outputWriter, atLeastOnce()).write(anyString());
        verify(tracker, never()).addItem(any());
    }

    @Test
    void testExecuteInvalidServings() {
        when(tracker.getFoods()).thenReturn(List.of());
        when(inputProvider.readLine())
                .thenReturn("SomeRecipe")
                .thenReturn("Desc")
                .thenReturn("-2");

        assertThrows(IllegalArgumentException.class, command::execute);
        verify(outputWriter, atLeastOnce()).write(anyString());
        verify(tracker, never()).addItem(any());
    }

    @Test
    void testExecuteSingleFood() {
        Food f = new Food("Tomato", "Fresh", MeasurementType.GRAM,
                100, 1, 20,
                4, 0, 1);
        when(tracker.getFoods()).thenReturn(List.of(f));
        when(inputProvider.readLine())
                .thenReturn("TomatoSoup")
                .thenReturn("Just water + tomato")
                .thenReturn("2")
                .thenReturn("1")
                .thenReturn("2")
                .thenReturn("no");

        command.execute();

        ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);
        verify(tracker).addItem(captor.capture());
        Recipe created = captor.getValue();
        assertEquals("TomatoSoup", created.name());
        assertEquals("Just water + tomato", created.description());
        assertEquals(2, created.servings());
        assertEquals(200.0, created.totalGrams(), 0.001);
        assertEquals(40.0, created.totalCalories(), 0.001);
        assertEquals(8.0, created.totalCarbs(), 0.001);
        assertEquals(0.0, created.totalFat(), 0.001);
        assertEquals(2.0, created.totalProtein(), 0.001);
        List<RecipeItem> items = created.items();
        assertEquals(1, items.size());
        assertEquals("Tomato", items.get(0).foodName());
        assertEquals(2.0, items.get(0).servings(), 0.001);
    }

    @Test
    void testExecuteMultipleFoods() {
        Food f1 = new Food("Egg", "", MeasurementType.PIECE,
                50, 1, 70, 0, 5, 6);
        Food f2 = new Food("Milk", "", MeasurementType.MILLILITER,
                100, 1, 60, 5, 3, 4);
        List<Food> foodList = List.of(f1, f2);

        when(tracker.getFoods()).thenReturn(foodList);
        when(inputProvider.readLine())
                .thenReturn("Omelet")
                .thenReturn("Egg + Milk")
                .thenReturn("4")
                .thenReturn("1")
                .thenReturn("3")
                .thenReturn("yes")
                .thenReturn("2")
                .thenReturn("1.5")
                .thenReturn("no");

        command.execute();

        ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);
        verify(tracker).addItem(captor.capture());
        Recipe created = captor.getValue();
        assertEquals("Omelet", created.name());
        assertEquals("Egg + Milk", created.description());
        assertEquals(4, created.servings());

        List<RecipeItem> items = created.items();
        assertEquals(2, items.size());
        assertEquals("Egg", items.get(0).foodName());
        assertEquals(3.0, items.get(0).servings(), 0.001);
        assertEquals("Milk", items.get(1).foodName());
        assertEquals(1.5, items.get(1).servings(), 0.001);

        double totalGrams = f1.unitsPerServing() * 3
                + f2.unitsPerServing() * 1.5;
        double totalCals = f1.calories() * 3 + f2.calories() * 1.5;
        double totalFat = f1.fat() * 3 + f2.fat() * 1.5;
        double totalProt = f1.protein() * 3 + f2.protein() * 1.5;

        assertEquals(totalGrams, created.totalGrams(), 0.001);
        assertEquals(totalCals, created.totalCalories(), 0.001);
        assertEquals(totalFat, created.totalFat(), 0.001);
        assertEquals(totalProt, created.totalProtein(), 0.001);
    }

    @Test
    void testExecuteInvalidFoodId() {
        Food f = new Food("Bread", "", MeasurementType.GRAM,
                100, 1, 250, 50, 2, 8);
        when(tracker.getFoods()).thenReturn(List.of(f));
        when(inputProvider.readLine())
                .thenReturn("Sandwich")
                .thenReturn("")
                .thenReturn("2")
                .thenReturn("99");

        assertThrows(IllegalArgumentException.class, command::execute);
        verify(tracker, never()).addItem(any());
    }

    @Test
    void testExecuteInvalidSubServings() {
        Food f = new Food("Cheese", "", MeasurementType.GRAM,
                50, 1, 200, 1, 15, 10);
        when(tracker.getFoods()).thenReturn(List.of(f));
        when(inputProvider.readLine())
                .thenReturn("CheeseDish")
                .thenReturn("desc")
                .thenReturn("4")
                .thenReturn("1")
                .thenReturn("-2");

        assertThrows(IllegalArgumentException.class, command::execute);
        verify(tracker, never()).addItem(any());
    }

    @Test
    void testStopGatheringItemsOnNo() {
        Food f = new Food("Potato", "", MeasurementType.GRAM,
                100, 1,
                75, 17, 0.1, 2);
        when(tracker.getFoods()).thenReturn(List.of(f));
        when(inputProvider.readLine())
                .thenReturn("SimplePotatoes")
                .thenReturn("desc")
                .thenReturn("3")
                .thenReturn("1")
                .thenReturn("2")
                .thenReturn("no");

        command.execute();

        ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);
        verify(tracker).addItem(captor.capture());
        Recipe r = captor.getValue();
        assertEquals("SimplePotatoes", r.name());
        assertEquals(1, r.items().size());
        assertEquals("Potato", r.items().get(0).foodName());
        assertEquals(2.0, r.items().get(0).servings(), 0.001);
    }
}
