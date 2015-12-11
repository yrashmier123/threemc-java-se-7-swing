package com.threemc.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.threemc.data.Service;
import com.threemc.data.ServicesWanted;

/**
 * @author Rashmier Ynawat
 * @since July 7, 2015
 * @version 1.0
 */
public class BookingPackagesForServicesTableModel extends AbstractTableModel {

	private List<Service> db;
	private String[] colNames = { "Service name","Category", "Service cost", "Remarks" };

	public void setData(List<Service> db) {
		this.db = db;
	}

	public int getColumnCount() {
		return 4;
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
		Service service = db.get(row);
		switch (col) {
		case 0:
			return service.getServiceName();
		case 1:
			return service.getServiceCatname();
		case 2:
			return service.getServiceCost();
		case 3:
			return service.getServiceRemarks();
		}

		return null;
	}
}
