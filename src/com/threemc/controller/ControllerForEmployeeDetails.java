package com.threemc.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import com.threemc.data.Employee;
import com.threemc.data.Position;
import com.threemc.data.User;
import com.threemc.model.DatabaseForEmployeeModule;
import com.threemc.view.CategoryGender;
import com.threemc.view.EmployeeEventForm;

public class ControllerForEmployeeDetails {

	private DatabaseForEmployeeModule db;

	public ControllerForEmployeeDetails() {
		db = new DatabaseForEmployeeModule();
	}

	public String connect() throws Exception {
		return db.connect();
	}

	public void disconnect() throws Exception {
		db.disconnect();
	}

	public Connection getConnection() {
		return db.getConnection();
	}

	//Add
	public void addPosition(Position pos) {
		db.addPosition(pos);
	}

	public void addEmployee(EmployeeEventForm emp) {
		int id = emp.getId();
		int posid = emp.getEmpPosId();
		String fname = emp.getEmpFirstName();
		String mname = emp.getEmpMiddleName();
		String lname = emp.getEmpLastName();
		String dob = emp.getEmpDateOfBirth();
		String doe = emp.getEmpDateOfEmployment();
		String pos = emp.getEmpPosition();
		String add = emp.getEmpAddress();
		String con = emp.getEmpContactno();
		String stat = emp.getEmpStatus();
		String gender = emp.getEmpGender();

		CategoryGender genderCat = null;
		if(gender.equalsIgnoreCase("Male")) {
			genderCat = CategoryGender.male;
		} else if (gender.equalsIgnoreCase("Female")) {
			genderCat = CategoryGender.female;
		}

		Employee emps;
		if(id >=1) {
			emps = new Employee(id, fname, mname, lname, dob, doe, pos, add, con, genderCat);
			emps.setEmpPosId(posid);
			emps.setEmpStatus(stat);
		} else {
			emps = new Employee(fname, mname, lname, dob, doe, pos, add, con, genderCat);
			emps.setEmpPosId(posid);
			emps.setEmpStatus(stat);
		}
		db.addEmployee(emps);
	}

	//Get

	public ArrayList<Employee> getEmployee() {
		return db.getEmp();
	}

	public ArrayList<Position> getPosition() {
		return db.getPos();
	}

	public ArrayList<Employee> getEmpcc() {
		return db.getEmpcc();
	}

	public ArrayList<User> getUsers() {
		return db.getUsers();
	}

	//Save 

	public void saveEmployee() throws SQLException {
		db.saveEmployee();
	}

	public void saveUser(User user) throws SQLException	{
		db.saveUser(user);
	}

	public void savePosition(Position pos) throws SQLException {
		db.savePosition(pos);
	}

	//Load

	public void loadPosition() throws SQLException {
		db.loadAllPosition();
	}

	public void loadAllEmployees() throws SQLException {
		db.loadAllEmployees();
	}

	public void loaddAllEmployeesWithoutAccount() throws SQLException {
		db.loadAllEmployeesWithoutAccount();
	}

	public void loadAllUsers() throws SQLException 	{
		db.loadAllUsers();
	}

	public void updateUserStatus(int user_id, String status) throws SQLException	{
		db.updateUserStatus(user_id, status);
	}

	public int getIfUsersLogged() throws SQLException	{
		return db.getIfUserIsLogged();
	}

	public void updateIfUserIsLogged() throws SQLException	{
		db.updateIfUserIsLogged();
	}

	public void deletePositionInfo(int position_id) throws SQLException {
		db.deletePositionInfo(position_id);
	}

	public void updateBeforeDelete(int position_id) throws SQLException {
		db.updateBeforeDelete(position_id);
	}
}
