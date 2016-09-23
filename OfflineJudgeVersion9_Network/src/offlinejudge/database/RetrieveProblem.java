package offlinejudge.database;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import offlinejudge.core.Problem;
import offlinejudge.core.ProblemStatement;
import offlinejudge.core.Score;
import offlinejudge.core.TestCase;

public class RetrieveProblem {
	
	public Problem retrieveProblem(String problemName) {
		ProblemStatement problemStatement = getProblemStatement(problemName);
		TestCase testCase = getTestCase(problemName);
		ArrayList<Score> scores = getScore(problemName);
		int timeLimit = getTimeLimit(problemName);
		
		return new Problem(problemStatement, testCase, scores, timeLimit);
	}
	
	public ProblemStatement getProblemStatement(String problemName) {
		String problemStatementQuery = new String("select *from problem_statement where problem_name like ?");
		String problemCode = null, problemStatementFile = null;
		
		try {
			Connection connection = (new DatabaseConnection()).getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(problemStatementQuery);
			preparedStatement.setString(1, problemName);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				problemCode = resultSet.getString(2);
				problemStatementFile = resultSet.getString(3);
			}
						
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return createProblemObject(problemName, problemCode, problemStatementFile);
	}
	
	public ProblemStatement createProblemObject(String problemName, String problemCode, String problemStatementFile) {
		if(problemCode == null && problemStatementFile == null) {
			return new ProblemStatement(problemName);
		} else if(problemCode == null) {
			return new ProblemStatement(problemName, new File(problemStatementFile));			
		} else if(problemStatementFile == null) {
			return new ProblemStatement(problemName, problemCode);
		} else {
			return new ProblemStatement(problemName, problemCode, new File(problemStatementFile));
		}
	}
	
	public int getTimeLimit(String problemName) {
		String timeLimitQuery = new String("select time_limit from time_limit where problem_name like ?");
		int timeLimit = 0;

		try {
			Connection connection = (new DatabaseConnection()).getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(timeLimitQuery);
			preparedStatement.setString(1, problemName);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				timeLimit = resultSet.getInt(1);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return timeLimit;
	}
	
	public ArrayList<Score> getScore(String problemName) {
		String scoreQuery = new String("select max_score from score where problem_name like ?");
		ArrayList<Score> scores = new ArrayList<>();
		
		try {
			Connection connection = (new DatabaseConnection()).getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(scoreQuery);
			preparedStatement.setString(1, problemName);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				scores.add(new Score(resultSet.getDouble(1)));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return scores;
	}
	
	public TestCase getTestCase(String problemName) {
		String testcaseQuery = new String("select testcase_dir from test_case where problem_name like ?");
		String testCaseFile = null;
		
		try {
			Connection connection = (new DatabaseConnection()).getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(testcaseQuery);
			preparedStatement.setString(1, problemName);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				testCaseFile = resultSet.getString(1);
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return new TestCase(new File(testCaseFile), "RETRIEVE");
	}
	
	public List<ProblemStatement> getProblemStatementList() {
		List<ProblemStatement> problemStatementList = new ArrayList<>();
		
		try {
			String query = new String("select *from problem_statement");
			Connection connection = (new DatabaseConnection()).getConnection();
			
			PreparedStatement preparedStatement;
			preparedStatement = connection.prepareStatement(query);
			ResultSet problemStatementResultSet = preparedStatement.executeQuery();
			
			String problemName, problemCode, problemFile;
			while(problemStatementResultSet.next()) {
				problemName = problemStatementResultSet.getString(1);
				problemCode = problemStatementResultSet.getString(2);
				problemFile = problemStatementResultSet.getString(3);
				
				problemStatementList.add(createProblemObject(problemName, problemCode, problemFile));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return problemStatementList;
	}
	
	public Boolean isEntryDuplicate(String problemName) {
		String problemStatementQuery = new String("select *from problem_statement where problem_name like ?");
		
		try {
			Connection connection = (new DatabaseConnection()).getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(problemStatementQuery);
			preparedStatement.setString(1, problemName);
			ResultSet resultSet = preparedStatement.executeQuery();
					
			return resultSet.next();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public int getNumberOfProblemStatements() {
		int numberOfProblems = 0;
		String query = new String("select count(*) from problem_statement");
		
		try {
			Connection connection = (new DatabaseConnection()).getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			
			ResultSet resultSet;
			resultSet = preparedStatement.executeQuery();

			while(resultSet.next()) {
				numberOfProblems = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return numberOfProblems;
	}
}
