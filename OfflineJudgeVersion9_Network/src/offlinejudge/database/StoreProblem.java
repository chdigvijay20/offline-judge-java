package offlinejudge.database;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import offlinejudge.core.Problem;
import offlinejudge.core.ProblemStatement;

public class StoreProblem {
	
	public void storeProblem(Problem problem) {
		storeProblemStatement(problem);
		storeTestCase(problem);
		storeScore(problem);
		storeTimeLimit(problem);
	}
	
	public void storeProblemStatement(Problem problem) {
		try {
			PreparedStatement preparedStatement = createPreaparedStatement(problem.getProblemStatement());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public PreparedStatement createPreaparedStatement(ProblemStatement problemStatement) {
		String query;
		
		String problemCode = problemStatement.getProblemCode();
		File problemFile = problemStatement.getProblemStatementFile();

		Connection connection = (new DatabaseConnection()).getConnection();
		PreparedStatement preparedStatement = null;
		
		try {
			if(problemCode != null && problemFile != null) {
	 			query = new String("insert into problem_statement values(?, ?, ?)");
	 			preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(2, problemStatement.getProblemCode());
				preparedStatement.setString(3, problemStatement.getProblemStatementFile().getAbsolutePath());
			} else if(problemCode != null){
	 			query = new String("insert into problem_statement(problem_name, problem_code) values(?, ?)");
	 			preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(2, problemStatement.getProblemCode());
			} else if(problemFile != null) {
	 			query = new String("insert into problem_statement(problem_name, problem_file) values(?, ?)");
	 			preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(2, problemStatement.getProblemStatementFile().getAbsolutePath());
			} else {
	 			query = new String("insert into problem_statement(problem_name) values(?)");
	 			preparedStatement = connection.prepareStatement(query);
			}
			preparedStatement.setString(1, problemStatement.getProblemName());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return preparedStatement;
	}
	
	public void storeTestCase(Problem problem) {
		try {
			String query = new String("insert into test_case values(?, ?)");
			Connection connection = (new DatabaseConnection()).getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			
			preparedStatement.setString(1, problem.getProblemStatement().getProblemName());
			preparedStatement.setString(2, problem.getTestCase().getTestCaseDirectory().getAbsolutePath());
			
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void storeScore(Problem problem) {
		String query = new String("insert into score values (?, ?, ?)");
		
		try {
			Connection connection = (new DatabaseConnection()).getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, problem.getProblemStatement().getProblemName());

			for (int i = 0; i < problem.getScore().size(); i++) {
				preparedStatement.setString(2, problem.getTestCase().getInputFiles()[i].getName());
				preparedStatement.setDouble(3, problem.getScore().get(i).getMaximumScore());
				preparedStatement.executeUpdate();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void storeTimeLimit(Problem problem) {
		String query = new String("insert into time_limit values (?, ?)");
		
		try {
			Connection connection = (new DatabaseConnection()).getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, problem.getProblemStatement().getProblemName());
			preparedStatement.setInt(2, problem.getTimeLimit());
			preparedStatement.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
