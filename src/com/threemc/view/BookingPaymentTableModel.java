package com.threemc.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.threemc.data.ServicesWanted;

public class BookingPaymentTableModel extends AbstractTableModel {

	private List<ServicesWanted> db = new ArrayList<ServicesWanted>();
	private String[] colNames = {"Service name" , "Service Cost" , "Service desc" };
	
	public void setData(List<ServicesWanted> db) {
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
		ServicesWanted serv = db.get(row);
		switch(col) {
		case 0:
			return serv.getServiceName();
		case 1:
			return serv.getServiceCost();
		case 2:
			return serv.getServiceDesc();
		}
		return null;
	}
}
