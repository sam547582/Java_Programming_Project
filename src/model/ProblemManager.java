package model;

import java.io.File;
import java.util.*;

public class ProblemManager {

    public static List<Problem> loadProblems(String folderPath) {
        File folder = new File(folderPath);

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".png"));

        if (files == null || files.length == 0) {
            throw new RuntimeException("문제 이미지가 없습니다!");
        }

        List<Problem> list = new ArrayList<>();

        for (File f : files) {
            String fileName = f.getName(); 

            String[] parts = fileName.split("_");

            if (parts.length < 2) continue;

            String answerPart = parts[1].replace(".png", "");

            Problem p = new Problem(Integer.parseInt(parts[0]),folderPath + "/" + fileName,answerPart);

            list.add(p);
        }

        return list;
    }

    public static Problem[] pickRandomFive(List<Problem> all) {
        Collections.shuffle(all);

        Problem[] selected = new Problem[5];
        for (int i = 0; i < 5; i++) {
            selected[i] = all.get(i);
        }
        return selected;
    }
}
