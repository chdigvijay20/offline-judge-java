package offlinejudge.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteProblem {
	
	public void deleteProblem(String problemName) {
		deleteTestCase(problemName);
		deleteScore(problemName);
		deleteTimeLimit(problemName);
		deleteProblemStatement(problemName);
	}
	
	public void deleteProblemStatement(String problemName) {
		
		String query = new String("delete from problem_statement where problem_name like ?");
		try {
			Connection connection = (new DatabaseConnection()).getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, problemName);
			preparedStatement.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteTestCase(String problemName) {
		
		String query = new String("delete from test_case where problem_name like ?");
		try {
			Connection connection = (new DatabaseConnection()).getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, problemName);
			preparedStatement.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteScore(String problemName) {
		
		String query = new String("delete from score where problem_name like ?");
		try {
			Connection connection = (new DatabaseConnection()).getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, problemName);
			preparedStatement.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteTimeLimit(String problemName) {

		String query = new String("delete from time_limit where problem_name like ?");
		try {
			Connection connection = (new DatabaseConnection()).getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, problemName);
			preparedStatement.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
