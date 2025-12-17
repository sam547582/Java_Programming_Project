package util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StatsManager {

	private static final String FILE_PATH = "resources/data/stats.txt";

	private static String name = "";
	private static String elective = "";
	private static int targetGrade = 0;
	private static int totalPlayed = 0;
	private static int correct = 0;
	private static int wrong = 0;
	
	private static List<Integer> score = new ArrayList<>();

	public static void load() {

		File file = new File(FILE_PATH);

		try {
			if (!file.exists()) {
				createDefaultFile();
				return;
			}

			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;

			score = new ArrayList<>();
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(" ");

				if (parts.length >= 2) {
					String key = parts[0].trim();
					String val = parts[1].trim();
					
					switch (key) {
					case "Name":
						name = val;
						for (int i = 2; i < parts.length; i++) {
							name += " ";
							name += (parts[i].trim());
						}
						break;
					case "Elective":
						elective = val;
						break;
					case "TargetGrade":
						targetGrade = Integer.parseInt(val);
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
					case "score":
						for (int i = 1; i < parts.length; i++) {
							score.add(Integer.parseInt(parts[i].trim()));
						}
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
		fw.write("Name 0\n");
		fw.write("Elective 0\n");
		fw.write("TargetGrade 0\n");
		fw.write("totalPlayed 0\n");
		fw.write("correct 0\n");
		fw.write("wrong 0\n");
		fw.write("score 0\n");
		fw.close();
	}

	public static void save() {

		try {
			FileWriter fw = new FileWriter(FILE_PATH);
			fw.write("Name " + name + "\n");
			fw.write("Elective " + elective + "\n");
			fw.write("TargetGrade " + targetGrade + "\n");
			fw.write("totalPlayed " + totalPlayed + "\n");
			fw.write("correct " + correct + "\n");
			fw.write("wrong " + wrong + "\n");
			fw.write("score");
			for (int k : score) {
				fw.write(" " + k);
			}
			fw.write("\n");
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

	public static void updateName(String s) {
		name = s;
		save();
	}

	public static void updateElective(String s) {
		elective = s;
		save();
	}

	public static void updateTargetGrade(int s) {
		targetGrade = s;
		save();
	}

	public static void updateScore(int s) {
		if (score.size() < 5) {
			score.add(s);
		} else {
			score.removeFirst();
			score.add(s);
		}

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

	public static String getElective() {
		return elective;
	}

	public static int getTargetGrade() {
		return targetGrade;
	}

	public static double getAccuracy() {
		if (totalPlayed == 0)
			return 0.0;
		return (double) correct / totalPlayed * 100;
	}

	public static List<Integer> getScore() {
		return score;
	}
}
