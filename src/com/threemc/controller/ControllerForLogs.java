package com.threemc.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import com.threemc.data.Log;
import com.threemc.model.DatabaseLogs;

public class ControllerForLogs {

	private DatabaseLogs db;
	
	public ControllerForLogs() {
		db = new DatabaseLogs();
	}

	public String connect() throws Exception {
		return db.connect();
	}

	public Connection getConnection() {
		return db.getConnection();
	}

	public void disconnect() throws Exception {
		db.disconnect();
	}

	public void loadLogsLimitTen() throws SQLException {
		db.loadLogsLimitTen();
	}

	public void loadLogs() throws SQLException {
		db.loadLogs();
	}

	public ArrayList<Log> getLog() throws SQLException {
		return db.getLog();
	}

	public void searchLogs(String text) throws SQLException {
		db.searchLogs(text);
	}

	public int getNewLogsCount() {
		return db.getNewLogsCount();
	}

	public void updateLogsNewToOld() throws SQLException {
		db.updateLogsNewToOld();
	}
}
