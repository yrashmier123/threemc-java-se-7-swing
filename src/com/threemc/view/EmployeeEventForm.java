package com.threemc.view;

import java.util.EventObject;

public class EmployeeEventForm extends EventObject {

	private int id;
	private String empFirstName;
	private String empMiddleName;
	private String empLastName;
	private String empDateOfBirth;
	private String empDateOfEmployment;
	private String empPosition;
	private String empAddress;
	private String empContactno;
	private String empStatus;
	private String empGender;
	private int empPosId;

	public EmployeeEventForm(Object source) {
		super(source);
	}

	public EmployeeEventForm(Object source, String empFirstName, String empMiddleName,
			String empLastName, String empDateOfBirth,
			String empDateOfEmployment, String empPosition, String empAddress,
			String empContactno, String empGender) {
		super(source);
		this.empFirstName = empFirstName;
		this.empMiddleName = empMiddleName;
		this.empLastName = empLastName;
		this.empDateOfBirth = empDateOfBirth;
		this.empDateOfEmployment = empDateOfEmployment;
		this.empPosition = empPosition;
		this.empAddress = empAddress;
		this.empContactno = empContactno;
		this.empGender = empGender;
	}

	public EmployeeEventForm(Object source, int id, String empFirstName, String empMiddleName,
			String empLastName, String empDateOfBirth,
			String empDateOfEmployment, String empPosition, String empAddress,
			String empContactno, String empGender) {
		this(source, empFirstName, empMiddleName, empLastName, empDateOfBirth, empDateOfEmployment, 
				empPosition, empAddress, empContactno, empGender);
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmpFirstName() {
		return empFirstName;
	}

	public void setEmpFirstName(String empFirstName) {
		this.empFirstName = empFirstName;
	}

	public String getEmpMiddleName() {
		return empMiddleName;
	}

	public void setEmpMiddleName(String empMiddleName) {
		this.empMiddleName = empMiddleName;
	}

	public String getEmpLastName() {
		return empLastName;
	}

	public void setEmpLastName(String empLastName) {
		this.empLastName = empLastName;
	}

	public String getEmpDateOfBirth() {
		return empDateOfBirth;
	}

	public void setEmpDateOfBirth(String empDateOfBirth) {
		this.empDateOfBirth = empDateOfBirth;
	}

	public String getEmpDateOfEmployment() {
		return empDateOfEmployment;
	}

	public void setEmpDateOfEmployment(String empDateOfEmployment) {
		this.empDateOfEmployment = empDateOfEmployment;
	}

	public String getEmpPosition() {
		return empPosition;
	}

	public void setEmpPosition(String empPosition) {
		this.empPosition = empPosition;
	}

	public String getEmpAddress() {
		return empAddress;
	}

	public void setEmpAddress(String empAddress) {
		this.empAddress = empAddress;
	}

	public String getEmpContactno() {
		return empContactno;
	}

	public void setEmpContactno(String empContactno) {
		this.empContactno = empContactno;
	}

	public String getEmpStatus() {
		return empStatus;
	}

	public void setEmpStatus(String empStatus) {
		this.empStatus = empStatus;
	}

	public int getEmpPosId() {
		return empPosId;
	}

	public void setEmpPosId(int empPosId) {
		this.empPosId = empPosId;
	}

	public String getEmpGender() {
		return empGender;
	}

	public void setEmpGender(String empGender) {
		this.empGender = empGender;
	}
}
