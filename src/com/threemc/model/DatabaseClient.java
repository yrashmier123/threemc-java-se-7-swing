package com.threemc.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.threemc.data.Client;
import com.threemc.view.CategoryGender;

public class DatabaseClient {

	private DatabaseConnection dbCon = new DatabaseConnection();
	public ArrayList<Client> dbClientonly;
	private Connection con;

	public DatabaseClient() {
		dbClientonly = new ArrayList<Client>();
	}

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

	// loads all client records and search if a client has an event or not
	public Client loadClientById(int ids) throws SQLException {
		Client client = null;
		String sql = "SELECT DISTINCT c.client_id, "
				+ "c.client_firstName , c.client_middleName , "
				+ "c.client_lastName, c.client_address , "
				+ "c.client_gender , c.client_contactNo, "
				+ "CASE WHEN (SELECT DISTINCT e.client_id AS eventss "
				+ "FROM events AS e WHERE c.client_id = e.client_id "
				+ "AND e.event_status = 'Open'  AND e.event_status2 = 'Ongoing' ) "
				+ "IS NULL THEN 'false' ELSE 'true' END AS HasEvent "
				+ "FROM clients  c , events  e WHERE c.client_id = " + ids + " "
				+ "ORDER BY c.client_id ASC LIMIT 1";
		Statement loadStatement = null;
		ResultSet res = null;
		try {
			loadStatement = con.createStatement();
			res = loadStatement.executeQuery(sql);
			while (res.next()) {
				int id = res.getInt("client_id");
				String fname = res.getString("client_firstName");
				String mname = res.getString("client_middleName");
				String lname = res.getString("client_lastName");
				String address = res.getString("client_address");
				String gender = res.getString("client_gender");
				String cont = res.getString("client_contactNo");
				boolean hevn = res.getBoolean("HasEvent");

				client = new Client(id, fname, mname, lname, address,
						cont, CategoryGender.valueOf(gender), hevn);
			}
			return client;
		} catch (SQLException slqe) {
			System.out.println(slqe.getMessage());
		} finally {
			loadStatement.close();
			res.close();
		}
		return client;
	}

	public void searchClient(String category, String value) throws SQLException {
		String val = value.replace("'", "");
		dbClientonly.clear();
		String sql = "SELECT DISTINCT " + "c.client_id, "
				+ "c.client_firstName , " + "c.client_middleName , "
				+ "c.client_lastName, " + "c.client_address , "
				+ "c.client_gender , " + "c.client_contactNo, "
				+ "CASE WHEN (SELECT DISTINCT " + "e.client_id AS eventss "
				+ "FROM events AS e " + "WHERE c.client_id = e.client_id ) "
				+ "IS NULL THEN 'false' ELSE 'true' END AS HasEvent "
				+ "FROM clients AS c , events AS e WHERE c."+ category+" LIKE '%"+val+"%'";
		Statement loadStatement = null;
		ResultSet res = null;
		try {
			loadStatement = con.createStatement();
			res = loadStatement.executeQuery(sql);
			while (res.next()) {
				int id = res.getInt("client_id");
				String fname = res.getString("client_firstName");
				String mname = res.getString("client_middleName");
				String lname = res.getString("client_lastName");
				String address = res.getString("client_address");
				String gender = res.getString("client_gender");
				String cont = res.getString("client_contactNo");
				boolean hevn = res.getBoolean("HasEvent");

				Client client = new Client(id, fname, mname, lname, address,
						cont, CategoryGender.valueOf(gender), hevn);
				dbClientonly.add(client);
			}
		} catch (SQLException slqe) {
			System.out.println("asdasd");
			slqe.printStackTrace();
		} finally {
			loadStatement.close();
			res.close();
		}
	}

	public ArrayList<Client> getClient() {
		return dbClientonly;
	}
}
