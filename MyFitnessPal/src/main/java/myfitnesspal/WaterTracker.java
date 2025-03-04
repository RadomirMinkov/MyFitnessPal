package myfitnesspal;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WaterTracker {
    private List<WaterIntake> intakes = new ArrayList<>();

    public  List<WaterIntake> getIntakes() {
        return intakes;
    }
    public void addIntake(LocalDate date, int amount) {
        intakes.add(new WaterIntake(date, amount));
    }

    public List<WaterIntake> getIntakesForDate(LocalDate date) {
        List<WaterIntake> result = new ArrayList<>();
        for (WaterIntake waterIntake : intakes) {
            if (waterIntake.date().equals(date)) {
                result.add(waterIntake);
            }
        }
        return result;
    }


    public void loadDataFromFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                WaterIntake wi = WaterIntake.fromString(line);
                if (wi != null) {
                    intakes.add(wi);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }

    public void saveDataToFile(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (WaterIntake wi : intakes) {
                writer.write(wi.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}
