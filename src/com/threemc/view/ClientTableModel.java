package com.threemc.view;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.threemc.data.Client;
import com.threemc.data.Service;

public class ClientTableModel extends AbstractTableModel {

	private List<Client> db;
	private String[] colNames = { "Client ID", "First name", "Middle name",
			"Last name", "Contact #", "Address", "Gender", "Has Active Events" };

	public void setData(ArrayList<Client> db2) {
		this.db = db2;
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
		Client client = db.get(row);
		switch (col) {
		case 0:
			return client.getId();
		case 1:
			return client.getClientFirstName();
		case 2:
			return client.getClientMiddleName();
		case 3:
			return client.getClientLastName();
		case 4:
			return client.getClientContactNumber();
		case 5:
			return client.getClientAddress();
		case 6:
			return client.getClientGender();
		case 7:
			String s;
			if (client.isHasEvent()) {
				s = "True";
			} else {
				s = "False";
			}
			return s;
		}
		return null;
	}
}
