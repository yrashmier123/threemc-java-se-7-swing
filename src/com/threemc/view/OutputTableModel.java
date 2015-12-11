package com.threemc.view;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import com.threemc.data.Output;

public class OutputTableModel extends AbstractTableModel {

	private ArrayList<Output> db;
	private String[] colname = {"Name" , "Event Date" , "Description" , "Status"};
	
	public void setData(ArrayList<Output> db) {
		this.db = db;
	}
	
	public int getColumnCount() {
		return 2;
	}

	public int getRowCount() {
		if(db != null) {
			return db.size();
		}
		return 0;
	}
	
	public String getColumnName(int column) {
		return colname[column];
	}

	@Override
	public Object getValueAt(int row, int col) {
		Output out = db.get(row);
		switch(col) {
		case 0:
			return out.getOutputName();
		case 1:
			return out.getEventDate();
		case 2:
			return out.getOutputDesc();
		case 3:
			return out.getOutputStat();
		}
		return null;
	}

}
