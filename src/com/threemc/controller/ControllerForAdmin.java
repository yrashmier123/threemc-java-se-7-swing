package com.threemc.controller;

import java.sql.SQLException;
import java.util.ArrayList;

import com.threemc.data.Admin;
import com.threemc.data.User;
import com.threemc.model.DatabaseAdmin;

public class ControllerForAdmin {

	private DatabaseAdmin db;

	public ControllerForAdmin() {
		db = new DatabaseAdmin();
	}

	public String connect() throws Exception {
		return db.connect();
	}

	public void disconnect() throws Exception {
		db.disconnect();
	}

	public int checkAdminUserAndPass(String username, String password) throws SQLException {
		return db.checkAdminUserAndPass(username, password);
	}

	public String checkUserStatus(int user_id) throws SQLException {
		return db.checkUserStatus(user_id);
	}

	public ArrayList<Admin> getUser() {
		return db.getAdmin();
	}

	public void updateLastLogIn(int user_id, String date) throws SQLException {
		db.updateLastLogin(user_id, date);
	}

	public void updateUserStatus(int user_id , String status) throws SQLException {
		db.updateUserStatus(user_id, status);
	}

	public void updateAdmin(Admin admin) throws SQLException {
		db.updateAdmin(admin);
	}

//	public void updateUserLogged(int user_id, String status) throws SQLException {
//		db.updateUserLogged(user_id, status);
//	}
}
