package com.threemc.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.threemc.data.Employee;
import com.threemc.data.Output;
import com.threemc.data.OutputsUpdate;
import com.threemc.view.CategoryGender;

public class DatabaseOutputModule {

	private ArrayList<Output> dbOutput;
	private ArrayList<OutputsUpdate> dbOutputsUpdates;
	private Connection con;

	public DatabaseOutputModule() {
		dbOutput = new ArrayList<Output>();
		dbOutputsUpdates = new ArrayList<OutputsUpdate>();
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

	public Connection getConnection() {
		return con;
	}

	//Add

	public void addOutput(Output out) {
		dbOutput.clear();
		dbOutput.add(out);
	}

	// Get 

	public ArrayList<Output> getOutputs() {
		return dbOutput;
	}

	public ArrayList<OutputsUpdate> getOutputUpdates() {
		return dbOutputsUpdates;
	}

	// Save

	// Save Outputs
	public void saveOutputs() throws SQLException {

		String checkSql = "SELECT COUNT(*) AS count FROM outputs WHERE output_id = ?";
		PreparedStatement checkStmt = con.prepareStatement(checkSql);

		String insertSql = "INSERT INTO " + "outputs(employee_id , event_id, "
				+ "client_id, output_name , output_desc, output_status) "
				+ "VALUES(?, ?, ?, ?, ?, ?)";
		PreparedStatement insertStmt = con.prepareStatement(insertSql);

		String updateSql = "UPDATE outputs SET employee_id = ? , "
				+ "event_id = ? , client_id = ? , "
				+ "output_name = ? , output_desc = ? , output_status = ? "
				+ "WHERE output_id  = ? ";
		PreparedStatement updateStmt = con.prepareStatement(updateSql);

		for (Output out: dbOutput) {
			int id = out.getId();
			int empid = out.getEmployee_id();
			int cid = out.getClient_id();
			int eid = out.getEvent_id();
			String oname = out.getOutputName();
			String odesc = out.getOutputDesc();
			String ostat = out.getOutputStat();

			checkStmt.setInt(1, id);
			ResultSet checkResult = checkStmt.executeQuery();
			checkResult.next();
			int count = checkResult.getInt(1);
			
			if (count == 0) {
				int col = 1;
				insertStmt.setInt(col++, empid);
				insertStmt.setInt(col++, eid);
				insertStmt.setInt(col++, cid);
				insertStmt.setString(col++, oname);
				insertStmt.setString(col++, odesc);
				insertStmt.setString(col++, ostat);
				insertStmt.executeUpdate();

//					AddLog.addLog(DatabaseLogs.TITLE_SAVE_CLIENT,
//							"Successfully saved " + fname + " Data");
			} else {
				int col = 1;
				updateStmt.setInt(col++, empid);
				updateStmt.setInt(col++, eid);
				updateStmt.setInt(col++, cid);
				updateStmt.setString(col++, oname);
				updateStmt.setString(col++, odesc);
				updateStmt.setString(col++, ostat);
				updateStmt.setInt(col++, id);
				updateStmt.executeUpdate();

//					AddLog.addLog(DatabaseLogs.TITLE_UPDATE_CLIENT,
//							"Updated Client's Information with Client ID: " + id
//									+ " Data");
			}
		}
		insertStmt.close();
		updateStmt.close();
		checkStmt.close();
	}

	// Load

	// Load All outputs

	public void loadAllOutputs() throws SQLException {
		dbOutput.clear();
		String sql = "SELECT * FROM outputs";
		Statement loadStatement = null;
		ResultSet res = null;
		try {
			loadStatement = con.createStatement();
			res = loadStatement.executeQuery(sql);
			while (res.next()) {
				int id = res.getInt("output_id");
				int empid = res.getInt("employee_id");
				int eid = res.getInt("event_id");
				int cid = res.getInt("client_id");
				String oname = res.getString("output_name");
				String odesc = res.getString("output_desc");
				String ostat = res.getString("output_status");
				String eventDate = loadEventDateByEvent(eid);

				Output out = new Output(id, empid, eid, cid, oname, odesc, ostat);
				out.setEventDate(eventDate);
				dbOutput.add(out);
			}
		} catch (SQLException sqle) {
			throw new SQLException();
		} finally {
			loadStatement.close();
			res.close();
		}
	}

	public void loadAllOutputsById(String category, int id) throws SQLException {
		dbOutput.clear();
		String sql = "";
		if(category.equals("output")) {
			sql = "SELECT * FROM outputs WHERE output_id = " + id;
		} else if(category.equals("employee")) {
			sql = "SELECT * FROM outputs WHERE employee_id = " + id;
		} else if(category.equals("event")) {
			sql = "SELECT * FROM outputs WHERE event_id = " + id;
		} else if(category.equals("client")) {
			sql = "SELECT * FROM outputs WHERE client_id = " + id;
		}
		Statement loadStatement = null;
		ResultSet res = null;
		try {
			loadStatement = con.createStatement();
			res = loadStatement.executeQuery(sql);
			while (res.next()) {
				int oid = res.getInt("output_id");
				int empid = res.getInt("employee_id");
				int eid = res.getInt("event_id");
				int cid = res.getInt("client_id");
				String oname = res.getString("output_name");
				String odesc = res.getString("output_desc");
				String ostat = res.getString("output_status");
				String eventDate = loadEventDateByEvent(eid);

				Output out = new Output(oid, empid, eid, cid, oname, odesc, ostat);
				out.setEventDate(eventDate);
				dbOutput.add(out);
			}
		} catch (SQLException sqle) {
			throw new SQLException();
		} finally {
			loadStatement.close();
			res.close();
		}
	}

	public Output loadOutputById(int id) throws SQLException {
		String sql = "SELECT * FROM outputs WHERE output_id = " + id + " LIMIT 1";
		Statement loadStatement = null;
		ResultSet res = null;
		Output out = null;
		try {
			loadStatement = con.createStatement();
			res = loadStatement.executeQuery(sql);
			while (res.next()) {
				int oid = res.getInt("output_id");
				int empid = res.getInt("employee_id");
				int eid = res.getInt("event_id");
				int cid = res.getInt("client_id");
				String oname = res.getString("output_name");
				String odesc = res.getString("output_desc");
				String ostat = res.getString("output_status");
				String eventDate = loadEventDateByEvent(eid);

				out = new Output(oid, empid, eid, cid, oname, odesc, ostat);
				out.setEventDate(eventDate);
				return out;
			}
		} catch (SQLException sqle) {
			throw new SQLException();
		} finally {
			loadStatement.close();
			res.close();
		}
		return out;
	}

	public Employee loadEmployeesByEmpId(int empid) throws SQLException {
		Employee emp = null;
		String sql = "SELECT e.`employee_id`, e.`position_id`, "
				+ "e.`employee_firstName`, e.`employee_middleName`, "
				+ "e.`employee_lastName`, e.`employee_gender`, "
				+ "e.`employee_address`, e.`employee_dateOfBirth`, "
				+ "e.`employee_dateOfEmployment`, e.`employee_contactno`, "
				+ "e.`employee_status`, (SELECT p.position_name FROM "
				+ "positions p WHERE e.position_id = p.position_id) "
				+ "AS position_name FROM `employees`  e "
				+ "WHERE e.`employee_id` = " + empid + " "
				+ "ORDER BY e.`employee_lastName` LIMIT 1";
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

				emp = new Employee(id, fname, mname, lname, dob, doe, pos, address, cont, CategoryGender.valueOf(gender));
				emp.setEmpPosId(posid);
				emp.setEmpStatus(stat);
			}
			return emp;
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			loadStatement.close();
			res.close();
		}
		return emp;
	}

	// loads all event
	public String loadEventDateByEvent(int event_id) throws SQLException {
		String sql = "SELECT `event_date` FROM `events` WHERE `event_id` = " + event_id;
		String eventDate = "";
		Statement loadStatement = null;
		ResultSet res = null;
		try {
			loadStatement = con.createStatement();
			res = loadStatement.executeQuery(sql);
			while (res.next()) {
				eventDate = res.getString("event_date");
			}
			return eventDate;
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			loadStatement.close();
			res.close();
		}
		return eventDate;
	}

	public void loadAllOutputsUpdate() throws SQLException {
		dbOutputsUpdates.clear();
		String sql = "SELECT * FROM outputs_updates ORDER BY ou_id DESC LIMIT 20 ";
		Statement loadStatement= null;
		ResultSet res = null;
		try {
			loadStatement = con.createStatement();
			res = loadStatement.executeQuery(sql);
			while (res.next()) {
				int id = res.getInt("ou_id");
				int oid = res.getInt("output_id");
				int empid = res.getInt("employee_id");
				String odate = res.getString("ou_date");
				String odesc = res.getString("ou_desc");
				Output outs = loadOutputById(oid);
				Employee emp = loadEmployeesByEmpId(empid);

				OutputsUpdate out = new OutputsUpdate(id, oid, empid, odesc, odate);
				out.setOutput(outs);
				out.setEmployee(emp);
				dbOutputsUpdates.add(out);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			loadStatement.close();
			res.close();
		}
	}

	public void loadAllOutputsUpdateById(int output_id) throws SQLException {
		dbOutputsUpdates.clear();
		String sql = "SELECT * FROM outputs_updates WHERE output_id = " + output_id;
		Statement loadStatement= null;
		ResultSet res = null;
		try {
			loadStatement = con.createStatement();
			res = loadStatement.executeQuery(sql);
			while (res.next()) {
				int id = res.getInt("ou_id");
				int oid = res.getInt("output_id");
				int empid = res.getInt("employee_id");
				String odate = res.getString("ou_date");
				String odesc = res.getString("ou_desc");

				OutputsUpdate out = new OutputsUpdate(id, oid, empid, odesc, odate);
				dbOutputsUpdates.add(out);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			loadStatement.close();
			res.close();
		}
	}

	public int getOutputUpdatesNewCount() {
		try {
			String checkSql = "SELECT COUNT(ou_status) as counts FROM outputs_updates WHERE `ou_status` = \"new\"";
			PreparedStatement checkStmt = con.prepareStatement(checkSql);
			ResultSet res = checkStmt.executeQuery();
			res.next();
			int ss = res.getInt("counts");
			return ss;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void updateOutputUpdatesIfNew() throws SQLException {
		String updateSql = "UPDATE `outputs_updates` SET `ou_status` = ? WHERE `outputs_updates`.`ou_status` =  \"new\";";
		PreparedStatement updateStmt = con.prepareStatement(updateSql);
		int col = 1;
		updateStmt.setString(col++, "old");
		updateStmt.executeUpdate();
		updateStmt.close();
	}
}