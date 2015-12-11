package com.threemc.view;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import com.threemc.data.PaymentModuleData;

public class PaymentTableModel extends AbstractTableModel {

	private ArrayList<PaymentModuleData> db;
	private String[] colNames = { "Client's name", "Contact #", "Event name",
			"Date of first payment", "Payment type", "Bill", "Amount paid",
			"Balance" };

	public void setData(ArrayList<PaymentModuleData> db) {
		this.db = db;
	}

	@Override
	public int getColumnCount() {
		return 8;
	}

	@Override
	public String getColumnName(int column) {
		return colNames[column];
	}

	@Override
	public int getRowCount() {
		if(db == null) {
			return 0;
		}
		return db.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		PaymentModuleData pmd = db.get(row);
		switch(col) {
		case 0:
			return pmd.getClientLastName() + " , " + pmd.getClientFirstName() + " " + pmd.getClientMiddleName();
		case 1:
			return pmd.getClientContactNo();
		case 2:
			return pmd.getEventName();
		case 3:
			return pmd.getPaymentDate();
		case 4: 
			return pmd.getPaymentType();
		case 5:
			return pmd.getPaymentTotal();
		case 6:
			return pmd.getPaymentPaid();
		case 7:
			return pmd.getPaymentBalance();
		}
		return null;
	}
}
