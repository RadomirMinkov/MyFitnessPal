package myfitnesspal;

import myfitnesspal.items.FoodLog;
import myfitnesspal.items.Trackable;
import myfitnesspal.utility.FileManager;
import myfitnesspal.parser.Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class MyFitnessTracker {
    private final List<Trackable> items = new ArrayList<>();
    private final Parser parser = new Parser();

    public void load(String fileName) {
        List<String> lines = FileManager.loadRawLines(fileName);
        items.clear();

        for (String line : lines) {
            Trackable t = parser.parseLine(line);
            if (t != null) {
                items.add(t);
            }
        }
    }

    public void save(String fileName) {
        List<String> lines = new ArrayList<>();
        for (Trackable item : items) {
            lines.add(item.toFileString());
        }
        FileManager.saveRawLines(fileName, lines);
    }

    public void addItem(Trackable item) {
        items.add(item);
    }
    @SuppressWarnings("unchecked")
    public <T extends Trackable> List<T> getItems(Class<T> type) {
        return items.stream()
                .filter(type::isInstance)
                .map(t -> (T) t)
                .collect(Collectors.toList());
    }
    public List<FoodLog> getFoodLogsForDate(java.time.LocalDate date) {
        return getItems(FoodLog.class).stream()
                .filter(log -> log.date().equals(date))
                .collect(Collectors.toList());
    }
}
