package persistence.factory;

import java.sql.*;

public class ConnectionFactory {
	
	public Connection getConnection(String databaseName) {
	     try {
	         return DriverManager.getConnection(
	        		 "jdbc:postgresql://localhost:5432/"+ databaseName, "postgres", "mra041328");
	     } catch (SQLException e) {
	         throw new RuntimeException(e);
	     }
	 }

}
