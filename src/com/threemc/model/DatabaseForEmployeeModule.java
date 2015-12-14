package com.threemc.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.threemc.data.Employee;
import com.threemc.data.Log;
import com.threemc.data.Position;
import com.threemc.data.User;
import com.threemc.view.AddLog;
import com.threemc.view.CategoryGender;


public class DatabaseForEmployeeModule {
	private DatabaseConnection dbCon = new DatabaseConnection();
	private ArrayList<Employee> dbEmp;
	private ArrayList<Employee> dbEmpWacc;
	private ArrayList<Position> dbPosition;
	private ArrayList<User>		dbUser;
	private Connection con;

	public DatabaseForEmployeeModule() {
		dbEmp = new ArrayList<Employee>();
		dbEmpWacc = new ArrayList<Employee>();
		dbPosition = new ArrayList<Position>();
		dbUser = new ArrayList<User>();
	}

	// connect database
	public String connect() throws Exception {
		String msg = "";
		try {
			con = dbCon.connect();
			msg = "ok";
		} catch (Exception e) {
			msg = e.getMessage();
		}
		return msg;
	}

	public void disconnect() throws Exception {
		con = dbCon.disconnect();
	}

	public Connection getConnection() {
		return con;
	}

	//add
	public void addEmployee(Employee emp) {
		dbEmp.clear();
		dbEmp.add(emp);
	}

	public void addPosition(Position pos) {
		dbPosition.clear();
		dbPosition.add(pos);
	}

	//get
	public ArrayList<Employee> getEmp() {
		return dbEmp;
	}

	public ArrayList<Position> getPos() {
		return dbPosition;
	}

	public ArrayList<Employee> getEmpcc() {
		return dbEmpWacc;
	}

	public ArrayList<User> getUsers() {
		return dbUser;
	}

	// Save

	// Save Employee to the database
	public void saveEmployee() throws SQLException {
		PreparedStatement checkStmt = null;
		PreparedStatement insertStmt =  null;
		PreparedStatement updateStmt =  null;
		try {
			String checkSql = "SELECT COUNT(*) AS count FROM employees WHERE employee_id = ?";
			checkStmt = con.prepareStatement(checkSql);
			String insertSql = "INSERT INTO " + "employees(position_id , employee_firstName, "
					+ "employee_middleName, employee_lastName, employee_gender, employee_address, "
					+ "employee_dateOfBirth, employee_dateOfEmployment, employee_contactno) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
			insertStmt = con.prepareStatement(insertSql);

			String updateSql = "UPDATE employees SET position_id = ? , "
					+ "employee_firstName = ? , employee_middleName = ? , "
					+ "employee_lastName = ? , employee_gender = ? , employee_address = ?, "
					+ "employee_dateOfBirth = ? , employee_dateOfEmployment = ?, employee_contactno = ? "
					+ "WHERE employee_id  = ? ";
			updateStmt = con.prepareStatement(updateSql);

			for (Employee emp: dbEmp) {
				int id = emp.getId();
				int posid = emp.getEmpPosId();
				String fname = emp.getEmpFirstName();
				String mname = emp.getEmpMiddleName();
				String lname = emp.getEmpLastName();
				String address = emp.getEmpAddress();
				String con = emp.getEmpContactno();
				CategoryGender gen = emp.getEmpGender();
				String dob = emp.getEmpDateOfBirth();
				String doe = emp.getEmpDateOfEmployment();

				checkStmt.setInt(1, id);
				ResultSet checkResult = checkStmt.executeQuery();
				checkResult.next();
				int count = checkResult.getInt(1);

				if (count == 0) {
					int col = 1;
					insertStmt.setInt(col++, posid);
					insertStmt.setString(col++, fname);
					insertStmt.setString(col++, mname);
					insertStmt.setString(col++, lname);
					insertStmt.setString(col++, gen.name());
					insertStmt.setString(col++, address);
					insertStmt.setString(col++, dob);
					insertStmt.setString(col++, doe);
					insertStmt.setString(col++, con);
					insertStmt.executeUpdate();
					AddLog.addLog(Log.TITLE_EMPLOYEE, Log.SAVE_EMPLOYEE + " : " + lname + ", " + fname + " " + mname);
				} else {
					int col = 1;
					updateStmt.setInt(col++, posid);
					updateStmt.setString(col++, fname);
					updateStmt.setString(col++, mname);
					updateStmt.setString(col++, lname);
					updateStmt.setString(col++, gen.name());
					updateStmt.setString(col++, address);
					updateStmt.setString(col++, dob);
					updateStmt.setString(col++, doe);
					updateStmt.setString(col++, con);
					updateStmt.setInt(col++, id);
					updateStmt.executeUpdate();
					AddLog.addLog(Log.TITLE_EMPLOYEE, Log.UPDATE_EMPLOYEE + " : " + lname + ", " + fname + " " + mname);
				}
			}
		} catch (Exception e) {
			AddLog.addLog(Log.TITLE_EMPLOYEE, Log.SAVE_EMPLOYEE_FAILED);
			e.printStackTrace();
		} finally {
			insertStmt.close();
			updateStmt.close();
			checkStmt.close();
		}
	}

	// Save Employee to the database
	public void saveUser(User user ) throws SQLException {
		PreparedStatement checkStmt = null;
		PreparedStatement insertStmt = null; 
		PreparedStatement updateStmt = null;
		try {
			String checkSql = "SELECT COUNT(*) AS count FROM users WHERE user_id = ?";
			checkStmt = con.prepareStatement(checkSql);
			String insertSql = "INSERT INTO " + "users(employee_id , user_name, user_password, "
					+ " user_lastLogIn, user_type, user_status) VALUES(?, ?, ?, ?, ?, ?)";
			insertStmt = con.prepareStatement(insertSql);
			String updateSql = "UPDATE users SET  "
					+ "employee_id = ? , user_name = ? , "
					+ "user_password = ? , user_lastLogIn = ? , user_type = ? , user_status = ? "
					+ "WHERE user_id  = ? ";
			updateStmt = con.prepareStatement(updateSql);
			int id = user.getId();
			int empid = user.getEmployee_id();
			String uname = user.getUserName();
			String upass = user.getUserPassword();
			String llog = user.getUserLastLogin();
			String type = user.getUserType();
			String status = user.getUserStatus();
			checkStmt.setInt(1, id);
			ResultSet checkResult = checkStmt.executeQuery();
			checkResult.next();
			int count = checkResult.getInt(1);
			if (count == 0) {
				int col = 1;
				insertStmt.setInt(col++, empid);
				insertStmt.setString(col++, uname);
				insertStmt.setString(col++, upass);
				insertStmt.setString(col++, llog);
				insertStmt.setString(col++, type);
				insertStmt.setString(col++, status);
				insertStmt.executeUpdate();
				AddLog.addLog(Log.TITLE_USER, Log.SAVE_USER + ", Username: " + uname + " Password: " + upass);
			} else {
				int col = 1;
				updateStmt.setInt(col++, empid);
				updateStmt.setString(col++, uname);
				updateStmt.setString(col++, upass);
				updateStmt.setString(col++, llog);
				updateStmt.setString(col++, type);
				updateStmt.setString(col++, status);
				updateStmt.setInt(col++, id);
				updateStmt.executeUpdate();
				AddLog.addLog(Log.TITLE_USER, Log.UPDATE_USER + ", Username: " + uname + " Password: " + upass);
			}
		} catch (Exception e) {
			AddLog.addLog(Log.TITLE_USER, Log.SAVE_USER_FAILED);
			e.printStackTrace();
		} finally {
			insertStmt.close();
			updateStmt.close();
			checkStmt.close();
		}
	}

	// Save Position to the database
	public void savePosition(Position pos) throws SQLException {
		PreparedStatement checkStmt = null;
		PreparedStatement insertStmt = null;
		PreparedStatement updateStmt = null;
		try {
			String checkSql = "SELECT COUNT(*) AS count FROM positions WHERE position_id = ?";
			checkStmt = con.prepareStatement(checkSql);
			String insertSql = "INSERT INTO positions(position_name, position_desc "
					+ " ) VALUES(?, ?)";
			insertStmt = con.prepareStatement(insertSql);
			String updateSql = "UPDATE positions SET  "
					+ "position_name = ? , position_desc = ? "
					+ "WHERE position_id  = ? ";
			updateStmt = con.prepareStatement(updateSql);
			int id = pos.getId();
			String posname = pos.getPositionName();
			String posdesc = pos.getPositionDesc();
			checkStmt.setInt(1, id);
			ResultSet checkResult = checkStmt.executeQuery();
			checkResult.next();
			int count = checkResult.getInt(1);
			if (count == 0) {
				int col = 1;
				insertStmt.setString(col++, posname);
				insertStmt.setString(col++, posdesc);
				insertStmt.executeUpdate();
				AddLog.addLog(Log.TITLE_POSITION, Log.SAVE_POSITION + " : " + posname);
			} else {
				int col = 1;
				updateStmt.setString(col++, posname);
				updateStmt.setString(col++, posdesc);
				updateStmt.setInt(col++, id);
				updateStmt.executeUpdate();
				AddLog.addLog(Log.TITLE_POSITION, Log.UPDATE_POSITION + " : " + posname);
			}
		} catch (Exception e) {
			AddLog.addLog(Log.TITLE_POSITION, Log.SAVE_POSITION_FAILED);
			e.printStackTrace();
		} finally {
			insertStmt.close();
			updateStmt.close();
			checkStmt.close();
		}
	}

	//load
	public void loadAllPosition() throws SQLException {
		dbPosition.clear();
		String sql = "SELECT * FROM positions";
		Statement loadStatement = null;
		ResultSet res = null;
		try {
			loadStatement = con.createStatement();
			res = loadStatement.executeQuery(sql);
			while (res.next()) {
				int id = res.getInt("position_id");
				String pname = res.getString("position_name");
				String pdesc = res.getString("position_desc");
				
				Position pos = new Position(id ,pname, pdesc);
				dbPosition.add(pos);
			}
		} catch (SQLException sqle) {
			throw new SQLException();
		} finally {
			loadStatement.close();
			res.close();
		}
	}

	public void loadAllEmployees() throws SQLException {
		dbEmp.clear();
		String sql = "SELECT e.`employee_id`, e.`position_id`, "
				+ "e.`employee_firstName`, e.`employee_middleName`, "
				+ "e.`employee_lastName`, e.`employee_gender`, "
				+ "e.`employee_address`, e.`employee_dateOfBirth`, "
				+ "e.`employee_dateOfEmployment`, e.`employee_contactno`, "
				+ "e.`employee_status`, (SELECT p.position_name FROM "
				+ "positions p WHERE e.position_id = p.position_id) "
				+ "AS position_name FROM `employees` e ORDER BY e.`employee_lastName`";
		Statement loadStatement = null;
		ResultSet res = null;
		try {
			loadStatement = con.createStatement();
			res = loadStatement.executeQuery(sql);
			while (res.next()) {
				int id = res.getInt("employee_id");
				int posid = res.getInt("position_id");
				String fname = res.getString("employee_firstName");
				String mname = res.getString("employee_middleName");
				String lname = res.getString("employee_lastName");
				String gender = res.getString("employee_gender");
				String address = res.getString("employee_address");
				String dob = res.getString("employee_dateOfBirth");
				String doe = res.getString("employee_dateOfEmployment");
				String cont = res.getString("employee_contactno");
				String stat = res.getString("employee_status");
				String pos = res.getString("position_name");

				Employee emp = new Employee(id, fname, mname, lname, dob, doe, pos, address, cont, CategoryGender.valueOf(gender));
				emp.setEmpPosId(posid);
				emp.setEmpStatus(stat);
				dbEmp.add(emp);
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			loadStatement.close();
			res.close();
		}
	}

	public void loadAllEmployeesByEmpId(int empid) throws SQLException {
		dbEmp.clear();
		String sql = "SELECT e.`employee_id`, e.`position_id`, "
				+ "e.`employee_firstName`, e.`employee_middleName`, "
				+ "e.`employee_lastName`, e.`employee_gender`, "
				+ "e.`employee_address`, e.`employee_dateOfBirth`, "
				+ "e.`employee_dateOfEmployment`, e.`employee_contactno`, "
				+ "e.`employee_status`, (SELECT p.position_name FROM "
				+ "positions p WHERE e.position_id = p.position_id) "
				+ "AS position_name FROM `employees` e ORDER BY e.`employee_lastName` "
				+ "WHERE e.`employee_id` = " + empid + " LIMIT 1";
		Statement loadStatement = null;
		ResultSet res = null;
		try {
			loadStatement = con.createStatement();
			res = loadStatement.executeQuery(sql);
			while (res.next()) {
				int id = res.getInt("employee_id");
				int posid = res.getInt("position_id");
				String fname = res.getString("employee_firstName");
				String mname = res.getString("employee_middleName");
				String lname = res.getString("employee_lastName");
				String gender = res.getString("employee_gender");
				String address = res.getString("employee_address");
				String dob = res.getString("employee_dateOfBirth");
				String doe = res.getString("employee_dateOfEmployment");
				String cont = res.getString("employee_contactno");
				String stat = res.getString("employee_status");
				String pos = res.getString("position_name");

				Employee emp = new Employee(id, fname, mname, lname, dob, doe, pos, address, cont, CategoryGender.valueOf(gender));
				emp.setEmpPosId(posid);
				emp.setEmpStatus(stat);
				dbEmp.add(emp);
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			loadStatement.close();
			res.close();
		}
	}

	public void loadAllEmployeesWithoutAccount() throws SQLException {
		dbEmpWacc.clear();
		String sql = "SELECT e.`employee_id`, e.`position_id`, "
				+ "e.`employee_firstName`, e.`employee_middleName`, "
				+ "e.`employee_lastName`, e.`employee_gender`, "
				+ "e.`employee_address`, e.`employee_dateOfBirth`, "
				+ "e.`employee_dateOfEmployment`, e.`employee_contactno`, "
				+ "e.`employee_status`, (SELECT p.position_name FROM "
				+ "positions p WHERE e.position_id = p.position_id) "
				+ "AS position_name FROM `employees` e WHERE NOT EXISTS"
				+ "(SELECT * FROM users u WHERE e.employee_id = u.employee_id)"
				+ " ORDER BY e.`employee_lastName`";

		Statement loadStatement= null;
		ResultSet res = null;
		try {
			loadStatement = con.createStatement();
			res = loadStatement.executeQuery(sql);
			while(res.next()) {
				int id = res.getInt("employee_id");
				int posid = res.getInt("position_id");
				String fname = res.getString("employee_firstName");
				String mname = res.getString("employee_middleName");
				String lname = res.getString("employee_lastName");
				String gender = res.getString("employee_gender");
				String address = res.getString("employee_address");
				String dob = res.getString("employee_dateOfBirth");
				String doe = res.getString("employee_dateOfEmployment");
				String cont = res.getString("employee_contactno");
				String stat = res.getString("employee_status");
				String pos = res.getString("position_name");

				Employee emp = new Employee(id, fname, mname, lname, dob, doe, pos, address, cont, CategoryGender.valueOf(gender));
				emp.setEmpPosId(posid);
				emp.setEmpStatus(stat);
				dbEmpWacc.add(emp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			loadStatement.close();
			res.close();
		}
	}

	public void loadAllUsers() throws SQLException {
		dbUser.clear();
		String sql = "SELECT * FROM `users`";
		Statement loadStatement = null;
		ResultSet res = null;
		try {
			loadStatement = con.createStatement();
			res = loadStatement.executeQuery(sql);
			while (res.next()) {
				int id = res.getInt("user_id");
				int empid = res.getInt("employee_id");
				String userName = res.getString("user_name");
				String userPass = res.getString("user_password");
				String llogin = res.getString("user_lastLogIn");
				String type = res.getString("user_type");
				String status = res.getString("user_status");

				User user = new User(id, empid, userName, userPass, llogin, type, status);
				dbUser.add(user);
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			loadStatement.close();
			res.close();
		}
	}

	public void updateUserStatus(int user_id, String status) throws SQLException {
		String updateSql = "UPDATE `users` SET `user_status` = ? WHERE `users`.`user_id` = ?;";
		PreparedStatement updateStmt = con.prepareStatement(updateSql);
		int col = 1;
		updateStmt.setString(col++, status);
		updateStmt.setInt(col++, user_id);
		updateStmt.executeUpdate();
		updateStmt.close();
	}

	public int getIfUserIsLogged() throws SQLException {
		String checkSql = "SELECT DISTINCT COUNT(*) as counts FROM `users` WHERE `user_logged` = \"true\"";
		PreparedStatement checkStmt = con.prepareStatement(checkSql);
		ResultSet checkResult = checkStmt.executeQuery();
		checkResult.next();
		int ss = checkResult.getInt("counts");
		return ss;
	}

	public void updateIfUserIsLogged() throws SQLException {
		String updateSql = "UPDATE `users` SET `user_logged` = ? WHERE `users`.`user_logged` =  \"true\";";
		PreparedStatement updateStmt = con.prepareStatement(updateSql);
		int col = 1;
		updateStmt.setString(col++, "false");
		updateStmt.executeUpdate();
		updateStmt.close();
	}

	// delete client by id from the database
	public void deletePositionInfo(int position_id) throws SQLException {
		String delSql = "DELETE FROM `positions` WHERE `position_id` = ?";
		PreparedStatement deleteStatement = con.prepareStatement(delSql);
		deleteStatement.setInt(1, position_id);
		deleteStatement.executeUpdate();
		deleteStatement.close();
	}

	public void updateBeforeDelete(int position_id) throws SQLException {
		String updateSql = "UPDATE `employees` SET `position_id` = ? WHERE `employees`.`position_id` = " + position_id;
		PreparedStatement updateStmt = con.prepareStatement(updateSql);
		int col = 1;
		updateStmt.setInt(col++, 10000);
		updateStmt.executeUpdate();
		updateStmt.close();
	}
}