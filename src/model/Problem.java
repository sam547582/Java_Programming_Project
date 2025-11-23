package model;

public class Problem {
	
	private String path;
	private String answer;
	private int id;
	
	public Problem(int id, String path, String answer) {
		this.id = id;
		this.path = path;
		this.answer = answer;
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
}
