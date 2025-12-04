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
public class ProblemStatsManager {

	private static final String PROBLEM_ROOT = "resources/img/problem";
	private static final String STATS_FILE = "resources/data/problem_stats.txt";

	// ====================================================================================
	// 1. 문제 파일 전체 스캔
	// ====================================================================================

	public static List<String> scanProblemFiles() {
		List<String> list = new ArrayList<>();
		File root = new File(PROBLEM_ROOT);
		scanDirectory(root, list);
		return list;
	}

	private static void scanDirectory(File dir, List<String> list) {
		File[] files = dir.listFiles();

		if (files == null)
			return;

		for (File f : files) {

			if (f.isDirectory()) {
				scanDirectory(f, list); // 재귀 탐색
			} else {
				if (isImageFile(f.getName())) {

					// 전체 경로 → 상대경로만 추출
					String relative = f.getPath().replace(PROBLEM_ROOT + File.separator, "").replace("\\", "/"); // 윈도우
																													// 호환

					list.add(relative);
				}
			}
		}
	}

	private static boolean isImageFile(String name) {
		name = name.toLowerCase();
		return name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg");
	}

	// ====================================================================================
	// 2. problem_stats.txt 로드
	// ====================================================================================

	public static Map<String, int[]> loadProblemStats() {

		Map<String, int[]> map = new LinkedHashMap<>();
		File f = new File(STATS_FILE);

		if (!f.exists()) {
			return map; // 최초 실행이면 빈 map 반환
		}

		try (BufferedReader br = new BufferedReader(new FileReader(f))) {

			String line;
			while ((line = br.readLine()) != null) {

				// 예: math1/easy/e001.png solve=3 wrong=1
				String[] sp = line.split(" ");
				String filePath = sp[0];

				int solve = Integer.parseInt(sp[1].split("=")[1]);
				int wrong = Integer.parseInt(sp[2].split("=")[1]);

				map.put(filePath, new int[] { solve, wrong });
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}

	// ====================================================================================
	// 3. 문제 파일 목록과 stats 동기화
	// ====================================================================================

	public static void syncStats() {

		List<String> problemFiles = scanProblemFiles();
		Map<String, int[]> stats = loadProblemStats();

		boolean updated = false;

		// 새로운 문제 자동 추가
		for (String file : problemFiles) {
			if (!stats.containsKey(file)) {
				stats.put(file, new int[] { 0, 0 });
				updated = true;
				System.out.println("[새 문제 추가됨] " + file);
			}
		}

		// 삭제된 문제 제거 (원하면 사용)
		/*
		 * Iterator<String> it = stats.keySet().iterator(); while (it.hasNext()) {
		 * String key = it.next(); if (!problemFiles.contains(key)) { it.remove();
		 * updated = true; System.out.println("[삭제된 문제 제거됨] " + key); } }
		 */

		if (updated) {
			saveProblemStats(stats);
		}
	}

	// ====================================================================================
	// 4. stats 파일 저장
	// ====================================================================================

	public static void saveProblemStats(Map<String, int[]> stats) {

		File file = new File(STATS_FILE);

		// data 폴더 없으면 생성
		new File("data").mkdirs();

		try (PrintWriter pw = new PrintWriter(file)) {

			for (String key : stats.keySet()) {

				int[] val = stats.get(key);
				pw.println(key + " solve=" + val[0] + " wrong=" + val[1]);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ====================================================================================
	// 5. 문제 경로에서 subject / level 자동 파싱 (원하면 사용할 수 있음)
	// ====================================================================================

	public static String getSubject(String relativePath) {
		// math1/easy/xxx.png → math1
		return relativePath.split("/")[0];
	}

	public static String getLevel(String relativePath) {
		// math1/easy/xxx.png → easy
		return relativePath.split("/")[1];
	}

	public static String getFileName(String relativePath) {
		// math1/easy/xxx.png → xxx.png
		String[] sp = relativePath.split("/");
		return sp[sp.length - 1];
	}

}
