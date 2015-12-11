package com.threemc.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.threemc.data.Log;

public class DatabaseLogs {

	private static Connection con;
	private ArrayList<Log> dbLog = new ArrayList<Log>();

	public DatabaseLogs() {

	}

	public static String connect() throws Exception {
		String msg = "";
		try {
			con = DatabaseConnection.connect();
			msg = "ok";
		} catch (Exception e) {
			msg = e.getMessage();
		}
		return msg;
	}

	public void disconnect() throws Exception {
		con = DatabaseConnection.disconnect();
	}

	public Connection getConnection() {
		return con;
	}

	public static void saveLog(Log log) throws SQLException {
		String insertSql = "INSERT INTO logs"
				+ "(user_id, log_title , "
				+ "log_desc , log_datetime) "
				+ "VALUES (?,?,?,?)";
		PreparedStatement insertStmt = con.prepareStatement(insertSql);
		int user_id = log.getUser_id();
		String logTitle = log.getLogTitle();
		String logDesc = log.getLogDesc();
		String logDate = log.getLogDate();
		int col = 1;
		insertStmt.setInt(col++, user_id);
		insertStmt.setString(col++, logTitle);
		insertStmt.setString(col++, logDesc);
		insertStmt.setString(col++, logDate);
		insertStmt.executeUpdate();
		insertStmt.close();
	}

	public void loadLogs() throws SQLException {
		dbLog.clear();
		String sql = "SELECT * FROM `logs` ORDER BY log_id DESC";
		Statement loadStatement = null;
		ResultSet res = null;
		try {
			loadStatement = con.createStatement();
			res = loadStatement.executeQuery(sql);
			while (res.next()) {
				int id = res.getInt("log_id");
				int user_id = res.getInt("user_id");
				String logTitle = res.getString("log_title");
				String logDesc = res.getString("log_desc");
				String logDate = res.getString("log_datetime");

				Log log = new Log(id, user_id, logTitle, logDesc, logDate);
				dbLog.add(log);
			}
		} catch (SQLException slqe) {
			throw new SQLException();
		} finally {
			loadStatement.close();
			res.close();
		}
	}

	public void loadLogsLimitTen() throws SQLException {
		dbLog.clear();
		String sql = "SELECT * FROM `logs` ORDER BY log_id DESC LIMIT 15";
		Statement loadStatement = null;
		ResultSet res = null;
		try {
			loadStatement = con.createStatement();
			res = loadStatement.executeQuery(sql);
			while (res.next()) {
				int id = res.getInt("log_id");
				int user_id = res.getInt("user_id");
				String logTitle = res.getString("log_title");
				String logDesc = res.getString("log_desc");
				String logDate = res.getString("log_datetime");

				Log log = new Log(id, user_id, logTitle, logDesc, logDate);
				dbLog.add(log);
			}
		} catch (SQLException slqe) {
			throw new SQLException();
		} finally {
			loadStatement.close();
			res.close();
		}
	}

	public void searchLogs(String value) throws SQLException {
		dbLog.clear();
		String val = value.replace("'", "");
		String sql = "SELECT * FROM `logs` WHERE "
				+ "user_id LIKE '%" + val+ "%' OR "
				+ "log_title LIKE '%" + val+ "%' OR "
				+ "log_desc LIKE '%" + val+ "%'";
		Statement loadStatement = null;
		ResultSet res = null;
		try {
			loadStatement = con.createStatement();
			res = loadStatement.executeQuery(sql);
			while (res.next()) {
				int id = res.getInt("log_id");
				int user_id = res.getInt("user_id");
				String logTitle = res.getString("log_title");
				String logDesc = res.getString("log_desc");
				String logDate = res.getString("log_datetime");

				Log log = new Log(id, user_id, logTitle, logDesc, logDate);
				dbLog.add(log);
			}
		} catch (SQLException slqe) {
			throw new SQLException();
		} finally {
			loadStatement.close();
			res.close();
		}
	}

	public ArrayList<Log> getLog() {
		return dbLog;
	}

	public int getNewLogsCount() {
		try {
			String checkSql = "SELECT COUNT(*) AS counts FROM `logs` WHERE `log_status` = 'new'";
			PreparedStatement checkStmt = con.prepareStatement(checkSql);
			ResultSet checkResult = checkStmt.executeQuery();
			checkResult.next();
			int ss = checkResult.getInt("counts");
			return ss;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void updateLogsNewToOld() throws SQLException {
		String updateSql = "UPDATE `logs` SET `log_status` = ? WHERE `logs`.`log_status` =  \"new\";";
		PreparedStatement updateStmt = con.prepareStatement(updateSql);
		int col = 1;
		updateStmt.setString(col++, "old");
		updateStmt.executeUpdate();
		updateStmt.close();
	}
}
