package com.threemc.model;

import java.awt.Dialog.ModalityType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.prefs.Preferences;

import com.threemc.events.PrefsListener;

import com.threemc.view.DatabaseSettings;

public class DatabaseConnection {

	private Connection con;
	
	private String ip = "localhost";
	private String dbName = "threemcqueens";
	private String dbUserName = "root";
	private String dbPassword = "";
	private String dbPort = "3306";
	
	private Preferences prefs;
	
	private DatabaseSettings ds;

	public DatabaseConnection() {
//		prefs = Preferences.userRoot().node("db");
//		
//		ip = prefs.get("ip", "localhost");
//		dbName = prefs.get("dbname", "threemcqueens");
//		dbUserName = prefs.get("username", "root");
//		dbPassword = prefs.get("password", "");
//		dbPort = prefs.get("port", ""+3306);
//
//		if(con == null) {
//
//			ds = new DatabaseSettings(null, ModalityType.APPLICATION_MODAL);
//			ds.setPrefsListener(new PrefsListener() {
//				public void preferenceSet(String ip, String dbName, String username, String password, int port) {
//					prefs.put("dbname", dbName);
//					prefs.put("ip", ip);
//					prefs.put("username", username);
//					prefs.put("password", password);
//					prefs.putInt("port", 3306);
//					ds.dispose();
//				}
//			});
//			
//			setDefaults(prefs);
//
//			ip = prefs.get("ip", "localhost");
//			dbName = prefs.get("dbname", "threemcqueens");
//			dbUserName = prefs.get("username", "root");
//			dbPassword = prefs.get("password", "");
//			dbPort = prefs.get("port", ""+3306);
//
//			ds.setDefaults(ip, dbName, dbUserName, dbPassword, Integer.parseInt(dbPort));
//			ds.setVisible(true);
//		}
	}

	public Connection connect() throws Exception {
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
	
	public void setDefaults(Preferences prefs) {
		this.prefs = prefs;
	}
}