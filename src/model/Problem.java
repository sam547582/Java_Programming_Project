package model;

public class Problem {

	private String path;
	private String answer;
	private String playerAnswer;
	private int id;
	
	private int score = 0;
	private int solveCount = 0;
	private int wrongCount = 0;

	public Problem(int id, String path, String answer, int score) {
		this.id = id;
		this.path = path;
		this.answer = answer;
		this.score = score;
		playerAnswer = "";
	}
	
	public Problem(int id, String path, String answer) {
		this.id = id;
		this.path = path;
		this.answer = answer;
		playerAnswer = "";
	}
	
	public void setPlayerAnswer(String answer) {
		playerAnswer = answer;
	}

	public void setSolveCount(int solveCount) {
		this.solveCount = solveCount;
	}

	public void setWrongCount(int wrongCount) {
		this.wrongCount = wrongCount;
	}
	
	public void setScore(int score) {
		this.score = score;
	}

	public int getId() {
		return id;
	}

	public String getPath() {
		return path;
	}

	public String getAnswer() {
		return answer;
	}

	public String getPlayerAnswer() {
		return playerAnswer;
	}
	
	public int getScore() {
		return score;
	}
	
	public int getSolveCount() {
		return solveCount;
	}

	public int getWrongCount() {
		return wrongCount;
	}

	public double getWeight() {
		double w = 1.0 + wrongCount * 3 - solveCount * 0.5;
		if (w < 0.1) w = 0.1;
		return w;
	}

}
