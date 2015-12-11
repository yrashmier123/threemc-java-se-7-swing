package com.threemc.view;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import com.threemc.data.User;

public class UserTableModel extends AbstractTableModel {

	private ArrayList<User> db;
	private String[] colNames = {"User ID" , "User Name" , "Password","Status"};
	
	public void setData(ArrayList<User> db) {
		this.db = db;
	}
	
	public int getColumnCount() {
		return 4;
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
		User user = db.get(row);
		switch (col) {
		case 0:
			return user.getId();
		case 1:
			return user.getUserName();
		case 2:
			return user.getUserPassword();
		case 3:
			return user.getUserStatus();
		}
		return null;
	}

}
