package model;

import java.io.File;
import java.net.URL;
import java.util.*;

public class ProblemManager {

	public static List<Problem> loadProblems(String folderPath) {

		File folder = new File(folderPath);
		File[] files = folder.listFiles((dir, name) -> name.endsWith(".png"));

		if (files == null || files.length == 0) {
			throw new RuntimeException("문제 이미지가 없습니다!");
		}

		Map<String, int[]> stats = ProblemStatsManager.loadProblemStats();

		List<Problem> list = new ArrayList<>();

		for (File f : files) {
			String fileName = f.getName();

			String[] parts = fileName.split("_");

			if (parts.length < 2)
				continue;

			String relativePath = folderPath + "/" + fileName;
			String answerPart = parts[1].replace(".png", "");

			Problem p = new Problem(Integer.parseInt(parts[0]), relativePath, answerPart);

			if (stats.containsKey(relativePath)) {
				int[] sw = stats.get(relativePath);
				p.setSolveCount(sw[0]);
				p.setWrongCount(sw[1]);
			} else {
				// 새 문제라면 기본값
				p.setSolveCount(0);
				p.setWrongCount(0);
			}

			list.add(p);
		}

		return list;
	}

	private static Problem[] pickWeightedRandom(List<Problem> all, int size) {

		List<Problem> copy = new ArrayList<>(all);
		Problem[] selected = new Problem[size];

		for (int i = 0; i < size; i++) {
			Problem p = pickOneWeighted(copy);
			selected[i] = p;
			copy.remove(p); // 중복 제거
		}

		return selected;
	}

	// algorithm 룰렛 휠 알고리즘
	private static Problem pickOneWeighted(List<Problem> list) {

		double sum = 0;
		for (Problem p : list) {
			sum += p.getWeight();
		}

		double r = Math.random() * sum;

		for (Problem p : list) {
			r -= p.getWeight();
			if (r <= 0)
				return p;
		}

		return list.get(list.size() - 1);
	}

	public static Problem[] getProblem(String difficulty, String subject) {

		Problem[] problems = null;
		int size = 0;
		String path = "resources/img/problem" + "/" + subject + "/" + difficulty;

		List<Problem> all = ProblemManager.loadProblems(path);

		if (difficulty.equals("easy")) {
			size = Math.min(all.size(), 10);
		} else if (difficulty.equals("normal")) {
			size = Math.min(all.size(), 10);
		} else if (difficulty.equals("hard")) {
			size = Math.min(all.size(), 7);
		} else if (difficulty.equals("extreme")) {
			size = Math.min(all.size(), 5);
		}

		problems = ProblemManager.pickWeightedRandom(all, size);

		List<Problem> shuffled = new ArrayList<>(Arrays.asList(problems));
		Collections.shuffle(shuffled);
		problems = shuffled.toArray(new Problem[0]);

		return problems;
	}

}
