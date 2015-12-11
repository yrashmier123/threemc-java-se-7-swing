package com.threemc.data;

public class Admin {
	private int id;
	private String adminUsername;
	private String adminPassword;
	private String adminUserLastLogin;
	private String adminUserStatus;

	public Admin(String userName, String userPassword,
			String userLastLogin, String userStatus) {
		this.adminUsername = userName;
		this.adminPassword = userPassword;
		this.adminUserLastLogin = userLastLogin;
		this.adminUserStatus = userStatus;
	}

	public Admin(int id, String userName, String userPassword,
			String userLastLogin, String userStatus) {
		this(userName, userPassword, userLastLogin, userStatus);
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAdminUsername() {
		return adminUsername;
	}

	public void setAdminUsername(String adminUsername) {
		this.adminUsername = adminUsername;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	public String getAdminUserLastLogin() {
		return adminUserLastLogin;
	}

	public void setAdminUserLastLogin(String adminUserLastLogin) {
		this.adminUserLastLogin = adminUserLastLogin;
	}

	public String getAdminUserStatus() {
		return adminUserStatus;
	}

	public void setAdminUserStatus(String adminUserStatus) {
		this.adminUserStatus = adminUserStatus;
	}
}
