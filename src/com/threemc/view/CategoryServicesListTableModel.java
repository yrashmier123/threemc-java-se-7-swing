package com.threemc.view;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import com.threemc.data.ServiceCategory;

public class CategoryServicesListTableModel extends AbstractTableModel {

	private String colName[] = {"Category Name"};
	private ArrayList<ServiceCategory> db;
	
	public void setData(ArrayList<ServiceCategory> db) {
		this.db = db;
	}
	
	public int getColumnCount() {
		return 1;
	}

	public String getColumnName(int column) {
		return colName[column];
	}

	public int getRowCount() {
		if(db == null) {
			return 0;
		}
		return db.size();
	}

	public Object getValueAt(int row, int col) {
		ServiceCategory cat = db.get(row);
		switch(col) {
		case 0:
			return cat.getServiceCategoryName();
		}
		
		return null;
	}


}
