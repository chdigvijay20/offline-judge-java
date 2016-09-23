package offlinejudge.core;

public class Score {
	private Double maximumScore;
	private Double achievedScore;
	private String runStatus;
	
	public Score(Double maximumScore) {
		this.maximumScore = maximumScore;
	}
	
	public Double getMaximumScore() {
		return maximumScore;
	}
	
	public Double getAchievedScore() {
		return achievedScore;
	}
	
	public String getRunStatus() {
		return runStatus;
	}
	
	public void setMaximumScore(Double maximumScore) {
		this.maximumScore = maximumScore;
	}
	
	public void setAchievedScore(Double achievedScore) {
		this.achievedScore = achievedScore;
	}
	
	public void setRunStatus(String runStatus) {
		this.runStatus = runStatus;
	}
}