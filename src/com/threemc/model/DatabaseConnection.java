package com.threemc.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

	private static Connection con;
	
	private static String ip = "";
	private static String dbName = "";
	private static String dbUserName = "";
	private static String dbPassword = "";
	private static String dbPort = "";

	public DatabaseConnection() {

	}
	
	public static void setDefaults(String ip, String dbName, String username, String password, int port) {
		DatabaseConnection.ip = ip;
		DatabaseConnection.dbName = dbName;
		DatabaseConnection.dbUserName = username;
		DatabaseConnection.dbPassword = password;
		DatabaseConnection.dbPort = ""+port;
	}

	public static Connection connect() throws Exception {
		if (con != null) {
			return con;
		}
			
		try {
//			String url = "jdbc:mysql://192.168.254.127:3306/" + dbName;
			Class.forName("com.mysql.jdbc.Driver");
			String connectionString = "jdbc:mysql://"+ ip + ":" + dbPort + "/" + dbName + "?user=" + dbUserName + "&password=" + dbPassword + "&useUnicode=true&characterEncoding=UTF-8";
			con = DriverManager.getConnection(connectionString);
		} catch (ClassNotFoundException e) {

		} catch (Exception e) {
			
		}
		return con;
	}

	public Connection disconnect() {
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