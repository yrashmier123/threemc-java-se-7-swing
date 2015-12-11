package com.threemc.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.threemc.data.Booking;

public class BookingEventInfoTableModel extends AbstractTableModel {

	private List<Booking> db;
	private String [] colName = { "Event Title" , "Venue" , "Type" , "Date" , "Time" , "Guest number" , "Details" , "Has Services"};

	public void setData(List<Booking> db) {
		this.db = db;
	}

	public int getColumnCount() {
		return 8;
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
		Booking book = db.get(row);
		switch(col) {
		case 0:
			return book.getEventName();
		case 1:
			return book.getEventVenue();
		case 2:
			return book.getEventType();
		case 3:
			return book.getEventDate();
		case 4: 
			return book.getEventTime();
		case 5:
			return book.getEventGuestNumber();
		case 6:
			return book.getEventDetails();
		case 7:
			String s;
			if(book.isHasServices()) {
				s = "True";
			} else {
				s = "False";
			}
			return s;
		}
		
		return null;
	}
}
