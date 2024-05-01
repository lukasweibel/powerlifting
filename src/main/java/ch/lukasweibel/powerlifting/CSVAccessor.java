package ch.lukasweibel.powerlifting;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CSVAccessor {

    public ArrayList<Powerlifter> readEntities() {
        ArrayList<Powerlifter> powerlifters = new ArrayList<>();
        String csvFile = "data/powerlifting.csv";
        String line = "";
        String csvSplitBy = ",";
        Integer readLimit = 1000;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                try {
                    Powerlifter powerlifter = new Powerlifter();
                    String[] columns = line.split(csvSplitBy);

                    powerlifter.setName(columns[0]);
                    powerlifter.setSex(columns[1]);
                    powerlifter.setEquipment(columns[2]);
                    powerlifter.setAge(Double.parseDouble(columns[3]));
                    powerlifter.setBodyweightKg(Double.parseDouble(columns[4]));
                    powerlifter.setBest3SquatKg(Double.parseDouble(columns[5]));

                    powerlifters.add(powerlifter);
                    if (powerlifters.size() == readLimit) {
                        break;
                    }
                } catch (Exception e) {
                    continue;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Number of entities: " + powerlifters.size());
        return powerlifters;
    }

}
