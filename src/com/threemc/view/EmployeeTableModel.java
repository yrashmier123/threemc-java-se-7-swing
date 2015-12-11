package com.threemc.view;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import com.threemc.data.Employee;

public class EmployeeTableModel extends AbstractTableModel {

	private ArrayList<Employee> db;
	private String[] colNames = {"Full name" , "Position", "Date of Employment", "Gender", "Date of Birth", "Address", "Contact Number"};

	public void setData(ArrayList<Employee> db) {
		this.db = db;
	}

	public int getColumnCount() {
		return 7;
	}

	public String getColumnName(int col) {
		return colNames[col];
	}

	public int getRowCount() {
		if (db == null) {
			return 0;
		}
		return db.size();
	}

	public Object getValueAt(int row, int col) {
		Employee emp = db.get(row);
		switch (col) {
		case 0:
			return emp.getEmpLastName() + ", " + emp.getEmpFirstName() + " " + emp.getEmpMiddleName();
		case 1:
			return emp.getEmpPosition();
		case 2:
			return emp.getEmpDateOfEmployment();
		case 3:
			return emp.getEmpGender();
		case 4:
			return emp.getEmpDateOfBirth();
		case 5:
			return emp.getEmpAddress();
		case 6:
			return emp.getEmpContactno();
		}
		return null;
	}
}
