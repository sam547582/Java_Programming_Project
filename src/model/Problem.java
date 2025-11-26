package model;

public class Problem {
	
	private String path;
	private String answer;
	private String playerAnswer;
	private int id;
	
	public Problem(int id, String path, String answer) {
		this.id = id;
		this.path = path;
		this.answer = answer;
		playerAnswer = "";
	}
	
	public void setPlayerAnswer(String answer) {
		playerAnswer = answer;
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
}
