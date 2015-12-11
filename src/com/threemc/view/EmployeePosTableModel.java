package com.threemc.view;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import com.threemc.data.Position;

public class EmployeePosTableModel extends AbstractTableModel {

	private ArrayList<Position> db;
	private String[] colNames = {"Position name", "Position description"};

	public void setData(ArrayList<Position> db) {
		this.db = db;
	}

	public int getColumnCount() {
		return 2;
	}

	public int getRowCount() {
		if(db == null) {
			return 0;
		}
		return db.size();
	}

	public String getColumnName(int col) {
		return colNames[col];
	}

	public Object getValueAt(int row, int col) {
		Position pos = db.get(row);
		switch(col) {
		case 0:
			return pos.getPositionName();
		case 1:
			return pos.getPositionDesc();
		}
		return null;
	}
}
