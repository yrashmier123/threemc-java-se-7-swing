package com.threemc.view;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import com.threemc.data.Package;

public class PackageTableModel extends AbstractTableModel {

	private ArrayList<Package> db;
	private String[] colNames = {"Package name" , "Package total", "Package Description"};
	
	public void setData(ArrayList<Package> db) {
		this.db = db;
	}
	
	public int getColumnCount() {
		return 3;
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
		Package pack = db.get(row);
		switch(col) {
		case 0:
			return pack.getPackageName();
		case 1:
			return pack.getPackageCost();
		case 2:
			return pack.getPackageDesc();
		}
		return null;
	}

}
