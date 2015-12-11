package com.threemc.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.threemc.data.Log;
import com.threemc.data.Payment;
import com.threemc.data.PaymentHistory;
import com.threemc.data.PaymentModuleData;
import com.threemc.view.CategoryPaymentType;

public class DatabasePaymentModule {

	public ArrayList<PaymentModuleData> dbPaymentDataWithBalance;
	public ArrayList<PaymentModuleData> dbPaidPaymentData;
	public ArrayList<PaymentHistory> dbPaymentHistory;
	private DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss MM/dd/yyyy");

	private Connection con;

	public DatabasePaymentModule() {
		dbPaymentDataWithBalance = new ArrayList<PaymentModuleData>();
		dbPaidPaymentData = new ArrayList<PaymentModuleData>();
		dbPaymentHistory = new ArrayList<PaymentHistory>();
	}

	// connect database
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

	public ArrayList<PaymentModuleData> getPaymentDataWithBalance() {
		return dbPaymentDataWithBalance;
	}

	public ArrayList<PaymentModuleData> getPaidPaymentData() {
		return dbPaidPaymentData;
	}

	public void loadPaymentDataWithBalance() throws SQLException {
		dbPaymentDataWithBalance.clear();
		String sql = "SELECT DISTINCT p.payment_id, "
				+ "c.client_id, e.event_id, "
				+ "c.client_firstName, c.client_middleName, "
				+ "c.client_lastName, c.client_contactNo, "
				+ "e.event_title, p.payment_datetime, "
				+ "p.payment_type, p.payment_total, "
				+ "p.payment_paid, p.payment_balance "
				+ "FROM clients c, events e, payments p "
				+ "WHERE p.event_id = e.event_id "
				+ "AND c.client_id = p.client_id "
				+ "AND p.payment_balance != 0";
		Statement loadStatement = null;
		ResultSet res = null;
		try {
			loadStatement = con.createStatement();
			res = loadStatement.executeQuery(sql);
			while (res.next()) {
				int client_id = res.getInt("client_id");
				int event_id = res.getInt("event_id");
				int payment_id = res.getInt("payment_id");
				String clientFirstName = res.getString("client_firstName");
				String clientMiddleName = res.getString("client_middleName");
				String clientLastName = res.getString("client_lastName");
				String clientContactNo = res.getString("client_contactNo");
				String eventName = res.getString("event_title");
				String paymentDate = res.getString("payment_datetime");
				String paymentType = res.getString("payment_type");
				int paymentTotal = res.getInt("payment_total");
				int paymentPaid = res.getInt("payment_paid");
				int paymentBalance = res.getInt("payment_balance");

				PaymentModuleData pmd = new PaymentModuleData(payment_id,
						client_id, event_id, clientFirstName, clientMiddleName,
						clientLastName, clientContactNo, eventName,
						paymentDate, CategoryPaymentType.valueOf(paymentType),
						paymentTotal, paymentPaid, paymentBalance);
				dbPaymentDataWithBalance.add(pmd);
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			loadStatement.close();
			res.close();
		}
	}

	public void loadPaidPaymentData() throws SQLException {
		dbPaidPaymentData.clear();
		String sql = "SELECT DISTINCT p.payment_id, "
				+ "c.client_id, e.event_id, "
				+ "c.client_firstName, c.client_middleName, "
				+ "c.client_lastName, c.client_contactNo, "
				+ "e.event_title, p.payment_datetime, "
				+ "p.payment_type, p.payment_total, "
				+ "p.payment_paid, p.payment_balance "
				+ "FROM clients c, events e, payments p "
				+ "WHERE p.event_id = e.event_id "
				+ "AND c.client_id = p.client_id "
				+ "AND p.payment_balance = 0";
		Statement loadStatement = null;
		ResultSet res = null;
		try {
			loadStatement = con.createStatement();
			res = loadStatement.executeQuery(sql);
			while (res.next()) {
				int client_id = res.getInt("client_id");
				int event_id = res.getInt("event_id");
				int payment_id = res.getInt("payment_id");
				String clientFirstName = res.getString("client_firstName");
				String clientMiddleName = res.getString("client_middleName");
				String clientLastName = res.getString("client_lastName");
				String clientContactNo = res.getString("client_contactNo");
				String eventName = res.getString("event_title");
				String paymentDate = res.getString("payment_datetime");
				String paymentType = res.getString("payment_type");
				int paymentTotal = res.getInt("payment_total");
				int paymentPaid = res.getInt("payment_paid");
				int paymentBalance = res.getInt("payment_balance");

				PaymentModuleData pmd = new PaymentModuleData(payment_id,
						client_id, event_id, clientFirstName, clientMiddleName,
						clientLastName, clientContactNo, eventName,
						paymentDate, CategoryPaymentType.valueOf(paymentType),
						paymentTotal, paymentPaid, paymentBalance);
				dbPaidPaymentData.add(pmd);
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			loadStatement.close();
			res.close();
		}
	}

	public int loadPaymentDataTotalCostById(int pid) throws SQLException {
		String sql = "SELECT `payment_total` FROM `payments` WHERE `payment_id` = " + pid;
		Statement loadStatement = null;
		ResultSet res = null;
		int paymentTotal = 0;
		try {
			loadStatement = con.createStatement();
			res = loadStatement.executeQuery(sql);
			while (res.next()) {
				paymentTotal = res.getInt("payment_total");
			}
			return paymentTotal;
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			loadStatement.close();
			res.close();
		}
		return paymentTotal;
	}

	public void searchPaymentWithBalance(String category, String value) throws SQLException {
		dbPaymentDataWithBalance.clear();
		String sql = "SELECT DISTINCT p.payment_id, "
					+ "c.client_id, e.event_id, "
					+ "c.client_firstName, c.client_middleName, "
					+ "c.client_lastName, c.client_contactNo, "
					+ "e.event_title,p.payment_datetime, "
					+ "p.payment_type, p.payment_total, "
					+ "p.payment_paid, p.payment_balance "
					+ "FROM clients c, events e, payments p "
					+ "WHERE p.event_id = e.event_id "
					+ "AND c.client_id = p.client_id AND p.payment_balance > 0 AND " + category
					+ " LIKE " + "'%" + value + "%' ORDER BY c.client_lastName ASC";
		Statement loadStatement = null;
		ResultSet res = null;
		try {
			loadStatement = con.createStatement();
			res = loadStatement.executeQuery(sql);
			while (res.next()) {
				int client_id = res.getInt("client_id");
				int event_id = res.getInt("event_id");
				int payment_id = res.getInt("payment_id");
				String clientFirstName = res.getString("client_firstName");
				String clientMiddleName = res.getString("client_middleName");
				String clientLastName = res.getString("client_lastName");
				String clientContactNo = res.getString("client_contactNo");
				String eventName = res.getString("event_title");
				String paymentDate = res.getString("payment_datetime");
				String paymentType = res.getString("payment_type");
				int paymentTotal = res.getInt("payment_total");
				int paymentPaid = res.getInt("payment_paid");
				int paymentBalance = res.getInt("payment_balance");

				PaymentModuleData pmd = new PaymentModuleData(payment_id,
						client_id, event_id, clientFirstName, clientMiddleName,
						clientLastName, clientContactNo, eventName,
						paymentDate, CategoryPaymentType.valueOf(paymentType),
						paymentTotal, paymentPaid, paymentBalance);
				dbPaymentDataWithBalance.add(pmd);
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			loadStatement.close();
			res.close();
		}
	}

	public void searchPaidPaymentData(String category, String value) throws SQLException {
		dbPaidPaymentData.clear();
		String sql = "SELECT DISTINCT p.payment_id, "
					+ "c.client_id, e.event_id, "
					+ "c.client_firstName, c.client_middleName, "
					+ "c.client_lastName, c.client_contactNo, "
					+ "e.event_title,p.payment_datetime, "
					+ "p.payment_type, p.payment_total, "
					+ "p.payment_paid, p.payment_balance "
					+ "FROM clients c, events e, payments p "
					+ "WHERE p.event_id = e.event_id "
					+ "AND c.client_id = p.client_id AND p.payment_balance = 0 AND " + category
					+ " LIKE " + "'%" + value + "%' ORDER BY c.client_lastName ASC";
		Statement loadStatement = null;
		ResultSet res = null;
		try {
			loadStatement = con.createStatement();
			res = loadStatement.executeQuery(sql);
			while (res.next()) {
				int client_id = res.getInt("client_id");
				int event_id = res.getInt("event_id");
				int payment_id = res.getInt("payment_id");
				String clientFirstName = res.getString("client_firstName");
				String clientMiddleName = res.getString("client_middleName");
				String clientLastName = res.getString("client_lastName");
				String clientContactNo = res.getString("client_contactNo");
				String eventName = res.getString("event_title");
				String paymentDate = res.getString("payment_datetime");
				String paymentType = res.getString("payment_type");
				int paymentTotal = res.getInt("payment_total");
				int paymentPaid = res.getInt("payment_paid");
				int paymentBalance = res.getInt("payment_balance");

				PaymentModuleData pmd = new PaymentModuleData(payment_id,
						client_id, event_id, clientFirstName, clientMiddleName,
						clientLastName, clientContactNo, eventName,
						paymentDate, CategoryPaymentType.valueOf(paymentType),
						paymentTotal, paymentPaid, paymentBalance);
				dbPaidPaymentData.add(pmd);
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			loadStatement.close();
			res.close();
		}
	}

	public void updatePayment(Payment pay) throws SQLException {
		String updateSql = "UPDATE payments SET payment_type = ? , "
				+ "payment_paid = ? , payment_balance = ? ,"
				+ "payment_total = ? WHERE payment_id  = ? ";
		PreparedStatement updateStmt = con.prepareStatement(updateSql);

		int id = pay.getId();
		CategoryPaymentType ptype = pay.getPaymentType();
		int ppaid = pay.getPaymentPaid();
		int pbal = pay.getPaymentBalance();
		int ptot = pay.getPaymentTotal();

		int col = 1;
		updateStmt.setString(col++, ptype.name());
		updateStmt.setInt(col++, ppaid);
		updateStmt.setInt(col++, pbal);
		updateStmt.setInt(col++, ptot);
		updateStmt.setInt(col++, id);
		updateStmt.executeUpdate();

		updateStmt.close();
	}

	public void saveInPaymentHistory(PaymentHistory pay) throws SQLException {
		String insertSql = "INSERT INTO payment_history"
				+ "(client_id, event_id , " + "payment_id , payment_type, "
				+ "payment_paidThisDate, payment_date , payment_desc) VALUES (?,?,?,?,?,?,?)";
		PreparedStatement insertStmt = con.prepareStatement(insertSql);

		int pid = pay.getPayment_id();
		int cid = pay.getClient_id();
		int eid = pay.getEvent_id();
		CategoryPaymentType ptype = pay.getPaymentType();
		int pptdate = pay.getPaymentPaidThisDate();
		String pdate = pay.getPaymentDate();
		String desc = pay.getPaymentDesc();

		int col = 1;
		insertStmt.setInt(col++, cid);
		insertStmt.setInt(col++, eid);
		insertStmt.setInt(col++, pid);
		insertStmt.setString(col++, ptype.name());
		insertStmt.setInt(col++, pptdate);
		insertStmt.setString(col++, pdate);
		insertStmt.setString(col++, desc);
		insertStmt.executeUpdate();

		insertStmt.close();
	}

	public void loadPaymentHistoryByClientEvent(int c_id, int e_id) throws SQLException {
		dbPaymentHistory.clear();
		String sql = "SELECT * FROM payment_history WHERE client_id = " + c_id + " AND event_id = " + e_id;
		Statement loadStatement = null;
		ResultSet res = null;
		try {
			loadStatement = con.createStatement();
			res = loadStatement.executeQuery(sql);
			while (res.next()) {
				int id = res.getInt("ph_id");
				int client_id = res.getInt("client_id");
				int event_id = res.getInt("event_id");
				int payment_id = res.getInt("payment_id");
				int paymentPaidThisDate = res.getInt("payment_paidThisdate");
				String paymentDate = res.getString("payment_date");
				String paymentType = res.getString("payment_type");
				String paymentDesc = res.getString("payment_desc");

				PaymentHistory ph = new PaymentHistory(id, client_id, event_id, payment_id, CategoryPaymentType.valueOf(paymentType), paymentPaidThisDate, paymentDate);
				ph.setPaymentDesc(paymentDesc);
				dbPaymentHistory.add(ph);
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			loadStatement.close();
			res.close();
		}
	}
}