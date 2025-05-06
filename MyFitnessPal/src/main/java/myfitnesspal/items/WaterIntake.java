package myfitnesspal.items;


import java.time.LocalDate;

public record WaterIntake(LocalDate date, int amount) implements Trackable {

    @Override
    public String toFileString() {
        return "WATER;" + date + ";" + amount;
    }

    @Override
    public String toString() {
        return "Water on " + date + ": " + amount + " ml";
    }
}
