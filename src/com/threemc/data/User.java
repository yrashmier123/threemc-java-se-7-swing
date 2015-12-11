package com.threemc.data;

public class User {

	private int id;
	private int employee_id;
	private String userName;
	private String userPassword;
	private String userLastLogin;
	private String userType;
	private String userStatus;

	public User(int employee_id, String userName, String userPassword,
			String userLastLogin, String userType, String userStatus) {
		this.employee_id = employee_id;
		this.userName = userName;
		this.userPassword = userPassword;
		this.userLastLogin = userLastLogin;
		this.userType = userType;
		this.userStatus = userStatus;
	}

	public User(int id, int employee_id, String userName, String userPassword,
			String userLastLogin, String userType, String userStatus) {
		this(employee_id, userName, userPassword, userLastLogin, userType,userStatus);
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getEmployee_id() {
		return employee_id;
	}

	public void setEmployee_id(int employee_id) {
		this.employee_id = employee_id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserLastLogin() {
		return userLastLogin;
	}

	public void setUserLastLogin(String userLastLogin) {
		this.userLastLogin = userLastLogin;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}
}
