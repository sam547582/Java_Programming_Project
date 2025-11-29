package util;

import java.io.*;

public class StatsManager {

    private static final String FILE_PATH = "resources/data/stats.txt";
    
    private static String name = "";
    private static int totalPlayed = 0;
    private static int correct = 0;
    private static int wrong = 0;

    public static void load() {

        File file = new File(FILE_PATH);

        try {
            if (!file.exists()) {
                createDefaultFile();
                return;
            }

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split("=");

                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String val = parts[1].trim();

                    switch (key) {
                    	case "Name":
                    		name = val;
                    		break;
                        case "totalPlayed":
                            totalPlayed = Integer.parseInt(val);
                            break;
                        case "correct":
                            correct = Integer.parseInt(val);
                            break;
                        case "wrong":
                            wrong = Integer.parseInt(val);
                            break;
                    }
                }
            }

            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createDefaultFile() throws IOException {
        FileWriter fw = new FileWriter(FILE_PATH);
        fw.write("Name=0\n");
        fw.write("totalPlayed=0\n");
        fw.write("correct=0\n");
        fw.write("wrong=0\n");
        fw.close();
    }

    public static void save() {

        try {
            FileWriter fw = new FileWriter(FILE_PATH);
            fw.write("Name=" + name + "\n");
            fw.write("totalPlayed=" + totalPlayed + "\n");
            fw.write("correct=" + correct + "\n");
            fw.write("wrong=" + wrong + "\n");
            fw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateStats(int correctCount, int wrongCount) {
    	totalPlayed += correctCount + wrongCount;
        correct += correctCount;
        wrong += wrongCount;
        save();
    }
    
    public static void updateStats(String name_) {
        name = name_;
        save();
    }

    public static int getTotalPlayed() {
        return totalPlayed;
    }

    public static int getCorrect() {
        return correct;
    }

    public static int getWrong() {
        return wrong;
    }
    
    public static String getName() {
        return name;
    }

    public static double getAccuracy() {
        if (totalPlayed == 0) return 0.0;
        return (double) correct / totalPlayed * 100;
    }
}
