package model;

import java.io.File;
import java.util.*;

public class TestManager {

	private static final String TEST_ROOT = "resources/img/test";
	private static String selectedName;
	
	public static List<Problem> loadTest(String folderPath) {

		File folder = new File(folderPath);
		File[] files = folder.listFiles((dir, name) -> name.endsWith(".png"));

		if (files == null || files.length == 0) {
			throw new RuntimeException("문제 이미지가 없습니다!");
		}
		
		List<Problem> list = new ArrayList<>();

		for (File f : files) {
			String fileName = f.getName();

			String[] parts = fileName.split("_");

			if (parts.length < 2)
				continue;

			String relativePath = folderPath + "/" + fileName;
			String answerPart = parts[1];
			String score = parts[2].replace(".png", "");
			
			Problem p = new Problem(Integer.parseInt(parts[0]), relativePath, answerPart, Integer.parseInt(score));

			list.add(p);
		}

		return list;
	}
	
	public static Problem[] getTest(String elective) {

		File root = new File(TEST_ROOT);

		File[] folders = root.listFiles(File::isDirectory);

		if (folders == null || folders.length == 0) {
			System.err.println("No test folders found.");
			return null;
		}

		Map<String, int[]> stats = TestStatsManager.getAllStats();
		
		int minCount = Integer.MAX_VALUE;
		
		for (File folder : folders) {
            String name = folder.getName();
            int count = stats.get(name)[0];
            minCount = Math.min(minCount, count);
        }
		
		List<File> candidates = new ArrayList<>();
		
		for (File folder : folders) {
            String name = folder.getName();
            int count = stats.get(name)[0];

            if (count == minCount) {
                candidates.add(folder);
            }
        }
		
		File selected = candidates.get(new Random().nextInt(candidates.size()));
        selectedName = selected.getName();
        
        String path = "resources/img/test/" + selectedName;
        
        Problem[] problems = new Problem[30];
        List<Problem> all = new ArrayList<>();
        
        all = loadTest(path);
        
        for (int i = 0; i < 30; i++) {
        	problems[i] = all.get(i);
		}
        
        System.out.println("Selected Test Folder = " + selectedName);

        return problems;
	}
	
	public static String getSelectedName() {
		return selectedName;
	}

}
