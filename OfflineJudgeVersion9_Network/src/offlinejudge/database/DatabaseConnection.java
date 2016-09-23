package offlinejudge.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
	
	static final String JDBCDRIVER = "com.mysql.jdbc.Driver";
	static final String DATABASEURL = "jdbc:mysql://localhost:3306/offline_judge";
	static final String USER = "root";
	static final String PASSWORD = "";
	
	public Connection getConnection() {
		try {
			Class.forName(JDBCDRIVER);
			return DriverManager.getConnection(DATABASEURL, USER, PASSWORD);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
