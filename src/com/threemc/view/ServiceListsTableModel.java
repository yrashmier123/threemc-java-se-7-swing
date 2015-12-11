package com.threemc.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.threemc.data.ServiceList;

public class ServiceListsTableModel extends AbstractTableModel {

	private String colName[] = {"Service name" , "Category" , "Service cost" , "Service desc"};
	private List<ServiceList> db;
	
	public void setData(List<ServiceList> db) {
		this.db = db;
	}
	
	public int getColumnCount() {
		return 4;
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
		ServiceList serv = db.get(row);
		switch (col) {
		case 0:
			return serv.getServiceName();
		case 1:
			return serv.getServiceCategory();
		case 2:
			return serv.getServiceCost();
		case 3:
			return serv.getServiceRemarks();
		}
		return null;
	}

}
