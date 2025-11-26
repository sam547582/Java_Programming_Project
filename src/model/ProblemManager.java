package model;

import java.io.File;
import java.net.URL;
import java.util.*;

public class ProblemManager {

    public static List<Problem> loadProblems(String folderPath) {
    	URL folderURL = ProblemManager.class.getClassLoader().getResource(folderPath);
    	if (folderURL == null) {
    	    throw new RuntimeException("폴더를 찾을 수 없습니다: " + folderPath);
    	}

    	File folder = new File(folderURL.getFile());
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
    
    private static Problem[] pickRandom(List<Problem> all,int size) {
        Collections.shuffle(all);
        
        Problem[] selected = new Problem[size];
        for (int i = 0; i < size; i++) {
            selected[i] = all.get(i);
        }
        return selected;
    }
    
    public static Problem[] getProblem(String difficulty) {
    	
    	Problem[] problems = null;
    	
		if(difficulty.equals("easy")) {
			List<Problem> all = ProblemManager.loadProblems("img/easy");
			
			int size = Math.min(all.size(), 20);
			
			problems = ProblemManager.pickRandom(all,size);
		}
		else if(difficulty.equals("normal")) {
			List<Problem> all = ProblemManager.loadProblems("img/normal");
			
			int size = Math.min(all.size(), 20);
			
			problems = ProblemManager.pickRandom(all,size);
		}
		else if(difficulty.equals("hard")) {
			List<Problem> all = ProblemManager.loadProblems("img/hard");
			
			int size = Math.min(all.size(), 10);
			
			problems = ProblemManager.pickRandom(all,size);
		}
		else if(difficulty.equals("extreme")) {
			List<Problem> all = ProblemManager.loadProblems("img/extreme");
			
			int size = Math.min(all.size(), 5);
			
			problems = ProblemManager.pickRandom(all,size);
		}
		
		return problems;
	}
}
