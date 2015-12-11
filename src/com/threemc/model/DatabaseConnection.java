package com.threemc.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

	private static Connection con;

	public DatabaseConnection() {

	}

	public static Connection connect() throws Exception {
		if (con != null)
			return con;
		try {
//			String url = "jdbc:mysql://192.168.254.127:3306/" + dbName;
			String dbName = "threemcqueens";
			String dbUserName = "root";
			String dbPassword = "";
			Class.forName("com.mysql.jdbc.Driver");
			String connectionString = "jdbc:mysql://localhost:3306/" + dbName + "?user=" + dbUserName + "&password=" + dbPassword + "&useUnicode=true&characterEncoding=UTF-8";
			con = DriverManager.getConnection(connectionString);
		} catch (ClassNotFoundException e) {
			
		}
		return con;
	}

	public static Connection disconnect() {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				System.out.println("can't close connection");
			}
		}
		return con;
	}
}