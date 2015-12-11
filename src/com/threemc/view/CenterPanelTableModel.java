package com.threemc.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.threemc.data.HomeData;

public class CenterPanelTableModel extends AbstractTableModel {
	// Date Started: 06/26/2015

	private List<HomeData> db;
	private String[] colNames = { "Name of Event", "Type of Event",
			"Name of Client", "Date of Event", "Time", "Venue",
			"Total Guests", "Payment type" };

	public void setData(List<HomeData> db) {
		this.db = db;
	}

	public int getColumnCount() {
		return 8;
	}

	public String getColumnName(int column) {
		return colNames[column];
	}

	public int getRowCount() {
		if (db == null) {
			return 0;
		}
		return db.size();
	}

	public Object getValueAt(int row, int col) {
		HomeData hm = db.get(row);
		switch (col) {
		case 0:
			return hm.getEventName();
		case 1:
			return hm.getEventType();
		case 2:
			return hm.getClientLastName() + " , " + hm.getClientFirstName()
					+ " " + hm.getClientMiddleName();
		case 3:
			return hm.getEventDate();
		case 4:
			return hm.getEventTime();
		case 5:
			return hm.getEventVenue();
		case 6:
			return hm.getEventGuest();
		case 7:
			return hm.getPaymentType();
		}
		return null;
	}
}
