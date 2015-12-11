package com.threemc.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.threemc.data.Log;

public class LogTableModel extends AbstractTableModel {

	private String[] colNames = { "User", "Title", "Description",
			"Date" };
	private ArrayList<Log> db;

	public void setData(ArrayList<Log> db) {
		this.db = db;
	}

	public int getColumnCount() {
		return 4;
	}

	public String getColumnName(int column) {
		return colNames[column];
	}

	public int getRowCount() {
		if(db == null) {
			return 0;
		}
		return db.size();
	}

	public Object getValueAt(int row, int col) {
		Log log = db.get(row);
		switch (col) {
		case 0:
			return log.getUser_id();
		case 1:
			return log.getLogTitle();
		case 2:
			return log.getLogDesc();
		case 3:
			return log.getLogDate();
		}
		return null;
	}
}
