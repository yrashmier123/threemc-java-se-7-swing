package com.threemc.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.threemc.data.Client;
import com.threemc.data.PaymentHistory;

public class PaymentHistoryTableModel extends AbstractTableModel {
	
	private List<PaymentHistory> db;
	private String[] colNames = {"Date" , "Paid" , "Description"};

	public int getColumnCount() {
		return 3;
	}

	public int getRowCount() {
		if(db == null) {
			return 0;
		}
		return db.size();
	}
	
	public void setData(List<PaymentHistory> db) {
		this.db = db;
	}
	
	@Override
	public String getColumnName(int column) {
		return colNames[column];
	}

	public Object getValueAt(int row, int col) {
		PaymentHistory ph = db.get(row);
		switch (col) {
		case 0:
			return ph.getPaymentDate();
		case 1:
			return ph.getPaymentPaidThisDate();
		case 2:
			return ph.getPaymentDesc();
		}
		return null;
	}
}
