package offlinejudge.core;

import java.util.ArrayList;

public class Problem {
	
	private ProblemStatement problemStatement;
	private TestCase testCase;
	private ArrayList<Score> scores;
	private int timeLimit;
	
	public Problem(ProblemStatement problemStatement, TestCase testCase, int timeLimit) {
		this.problemStatement = problemStatement;
		this.testCase = testCase;
		this.timeLimit = timeLimit;
	}
	
	public Problem(ProblemStatement problemStatement, TestCase testCase, ArrayList<Score> scores, int timeLimit) {
		this.problemStatement = problemStatement;
		this.testCase = testCase;
		this.scores = scores;
		this.timeLimit = timeLimit;
	}

	public void setScore(ArrayList<Double> maximumScore) {
		scores = new ArrayList<>();
		for (int i = 0; i < maximumScore.size(); i++) {
			scores.add(new Score(maximumScore.get(i)));
		}
	}
	
	public TestCase getTestCase() {
		return testCase;
	}
	
	public ProblemStatement getProblemStatement() {
		return problemStatement;
	}
	
	public ArrayList<Score> getScore() {
		return scores;
	}
	
	public int getTimeLimit() {
		return timeLimit;
	}
}
