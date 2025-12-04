package model;

import java.io.*;
import java.util.*;

/**
 * ProblemStatsManager - resources/img/problem/ 내부 모든 문제 이미지 스캔 -
 * problem_stats.txt 로드 및 동기화 - 새 문제 자동 추가 - 삭제된 문제 자동 제거 (옵션) - 문제 경로에서 subject
 * / level 자동 파싱
 * 
 * 파일 저장 형식 예: math1/easy/e001.png solve=3 wrong=1
 */
public class TestStatsManager {

	private static final String TEST_ROOT = "resources/img/test";
	private static final String STATS_FILE = "resources/data/test_stats.txt";

	private static Map<String, int[]> map = new LinkedHashMap<>();

	public static Map<String, int[]> loadTestStats() {

		File f = new File(STATS_FILE);

		if (!f.exists()) {
			return map; // 최초 실행이면 빈 map 반환
		}

		try (BufferedReader br = new BufferedReader(new FileReader(f))) {

			String line;
			while ((line = br.readLine()) != null) {

				// 예: math1/easy/e001.png solve=3 wrong=1
				String[] parts = line.split(" ");
				String filePath = parts[0];

				int cnt = Integer.parseInt(parts[1].split("=")[1]);
				int score = Integer.parseInt(parts[2].split("=")[1]);

				map.put(filePath, new int[] { cnt, score });
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}

	public static void syncStats() {

		Map<String, int[]> stats = loadTestStats();

		File root = new File(TEST_ROOT);
		File[] folders = root.listFiles(File::isDirectory);

		boolean updated = false;

		// 새로운 문제 자동 추가
		for (File file : folders) {
			
			String name = file.getName();
			
			if (!stats.containsKey(name)) {
				stats.put(name, new int[] { 0, 0 });
				updated = true;
				System.out.println("[새 TEST 추가됨] " + name);
			}
		}

		if (updated) {
			saveTestStats(stats);
		}
	}

	public static void saveTestStats(Map<String, int[]> stats) {

		File file = new File(STATS_FILE);

		try (PrintWriter pw = new PrintWriter(file)) {

			for (String key : stats.keySet()) {

				int[] val = stats.get(key);
				pw.println(key + " cnt=" + val[0] + " score=" + val[1]);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Map<String, int[]> getAllStats() {
		return map;
	}

	public static int getCount(String folderName) {
		return map.getOrDefault(folderName, new int[] { 0, 0 })[0];
	}

	public static int getScore(String folderName) {
		return map.getOrDefault(folderName, new int[] { 0, 0 })[1];
	}

	// 테스트 완료 시 카운트 증가
	public static void increment(String folderName, int score) {
		map.put(folderName, new int[] { getCount(folderName) + 1, score });
		saveTestStats(map);
	}

}
