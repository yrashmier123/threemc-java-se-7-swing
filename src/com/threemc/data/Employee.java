package com.threemc.data;

import com.threemc.view.CategoryGender;

public class Employee {

	private int id;
	private int empPosId;
	private String empFirstName;
	private String empMiddleName;
	private String empLastName;
	private String empDateOfBirth;
	private String empDateOfEmployment;
	private String empPosition;
	private String empAddress;
	private String empContactno;
	private String empStatus;
	private CategoryGender empGender;

	public Employee(String empFirstName, String empMiddleName,
			String empLastName, String empDateOfBirth,
			String empDateOfEmployment, String empPosition, String empAddress,
			String empContactno, CategoryGender empGender) {
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

	public Employee(int id, String empFirstName, String empMiddleName,
			String empLastName, String empDateOfBirth,
			String empDateOfEmployment, String empPosition, String empAddress,
			String empContactno, CategoryGender empGender) {
		this(empFirstName, empMiddleName, empLastName, empDateOfBirth, empDateOfEmployment, 
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

	public CategoryGender getEmpGender() {
		return empGender;
	}

	public void setEmpGender(CategoryGender empGender) {
		this.empGender = empGender;
	}
}
