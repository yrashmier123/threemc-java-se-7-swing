package com.threemc.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.threemc.data.Admin;
import com.threemc.data.User;

public class DatabaseAdmin {

	private ArrayList<Admin> dbAdmin;
	private Connection con;

	public DatabaseAdmin() {
		dbAdmin = new ArrayList<Admin>();
	}

	public String connect() throws Exception {
		String msg = "";
		try {
			con = DatabaseConnection.connect();
			msg = "ok";
		} catch (Exception e) {
			msg = e.getMessage();
		}
		return msg;
	}

	public void disconnect() throws Exception {
		con = DatabaseConnection.disconnect();
	}

	public int checkAdminUserAndPass(String username, String password) throws SQLException {
		dbAdmin.clear();
		int ress = 0;
		String myQuery = "SELECT * FROM `admin` WHERE `admin_user` = \"" + username + "\" AND `admin_pass` = \"" + password + "\" LIMIT 1";
		Statement stmt = con.createStatement();
		ResultSet res = stmt.executeQuery(myQuery);
		try {
			if (res.next()) {
				int id = res.getInt("admin_id");
				String usernames = res.getString("admin_user");
				String pass = res.getString("admin_pass");
				String llgin = res.getString("admin_lastLogIn");
				String status = res.getString("admin_status");
				Admin ad = new Admin(id, usernames, pass, llgin, status);
				dbAdmin.add(ad);
				ress = 1;
			} else {
				ress = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
			res.close();
		}
		return ress;
	}

	public String checkUserStatus(int admin_id) throws SQLException {
		String status = "";
		String myQuery = "SELECT `admin_status` FROM `admin` WHERE `admin_id` = " + admin_id + " LIMIT 1";
		Statement stmt = con.createStatement();
		ResultSet res = stmt.executeQuery(myQuery);
		try {
			if (res.next()) {
				status = res.getString("admin_status");
			} else {
				status = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
			res.close();
		}
		return status;
	}

	public ArrayList<Admin> getAdmin() {
		return dbAdmin;
	}

	public void updateLastLogin(int admin_id, String date) throws SQLException {
		String updateSql = "UPDATE `admin` SET `admin_lastLogIn` = ? WHERE `admin`.`admin_id` = ?;";
		PreparedStatement updateStmt = con.prepareStatement(updateSql);
		int col = 1;
		updateStmt.setString(col++, date);
		updateStmt.setInt(col++, admin_id);
		updateStmt.executeUpdate();
		updateStmt.close();
	}

	public void updateUserStatus(int admin_id, String status) throws SQLException {
		String updateSql = "UPDATE `admin` SET `admin_status` = ? WHERE `admin`.`admin_id` = ?;";
		PreparedStatement updateStmt = con.prepareStatement(updateSql);
		int col = 1;
		updateStmt.setString(col++, status);
		updateStmt.setInt(col++, admin_id);
		updateStmt.executeUpdate();
		updateStmt.close();
	}

	public void updateAdmin(Admin admin) throws SQLException {
		String updateSql = "UPDATE `admin` SET `admin_user` = ? , `admin_pass` = ? WHERE `admin`.`admin_id` = ?;";
		PreparedStatement updateStmt = con.prepareStatement(updateSql);
		int col = 1;
		updateStmt.setString(col++, admin.getAdminUsername());
		updateStmt.setString(col++, admin.getAdminPassword());
		updateStmt.setInt(col++, admin.getId());
		updateStmt.executeUpdate();
		updateStmt.close();
	}
}
