package com.threemc.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.threemc.data.Booking;
import com.threemc.data.Client;
import com.threemc.data.HomeData;
import com.threemc.data.Log;
import com.threemc.data.Notice;
import com.threemc.data.Package;
import com.threemc.data.Payment;
import com.threemc.data.Service;
import com.threemc.data.ServiceCategory;
import com.threemc.data.ServiceList;
import com.threemc.data.ServicesWanted;
import com.threemc.view.AddLog;
import com.threemc.view.CategoryGender;
import com.threemc.view.CategoryPaymentType;

/**
 * @author Rashmier Ynawat
 * @since July 3, 2015
 * @version 1.0
 */
public class DatabaseBookingDetails {

	private ArrayList<Client> dbClientOnly;
	private ArrayList<Booking> dbBookingOnly;
	private ArrayList<Package> dbPackagesOnly;
	private ArrayList<Service> dbServicesOnly;
	private ArrayList<ServicesWanted> dbServicesWanted;
	private ArrayList<Payment> dbPaymentOnly;
	private ArrayList<HomeData> dbHomeData;
	private ArrayList<ServiceList> dbServiceList;
	private ArrayList<ServiceCategory> dbServiceCat;
	private ArrayList<Notice> dbNotice;
	private Connection con;
	
	private DatabaseConnection dbCon = new DatabaseConnection();

	public DatabaseBookingDetails() {
		dbClientOnly = new ArrayList<Client>();
		dbPackagesOnly = new ArrayList<Package>();
		dbServicesOnly = new ArrayList<Service>();
		dbBookingOnly = new ArrayList<Booking>();
		dbServicesWanted = new ArrayList<ServicesWanted>();
		dbPaymentOnly = new ArrayList<Payment>();
		dbHomeData = new ArrayList<HomeData>();
		dbServiceList = new ArrayList<ServiceList>();
		dbServiceCat = new ArrayList<ServiceCategory>();
		dbNotice = new ArrayList<Notice>();
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

	// disconnect database
	public void disconnect() throws Exception {
		con = dbCon.disconnect();
	}

	//ADD
	// add a single client to client's arraylist
	public void addClient(Client client) {
		dbClientOnly.clear();
		dbClientOnly.add(client);
	}

	// add a single booking to booking's arraylist
	public void addBooking(Booking booking) {
		dbBookingOnly.clear();
		dbBookingOnly.add(booking);
	}

	// add a single Package to Package arraylist
	public void addPackages(Package pack) {
		dbPackagesOnly.clear();
		dbPackagesOnly.add(pack);
	}

	// set services wanted of a client by event to the arraylist
	public void addServicesWanted(ArrayList<ServicesWanted> servw) {
		dbServicesWanted = servw;
	}

	// add a single Payment to Payment arraylist
	public void addPaymentRecord(Payment pay) {
		dbPaymentOnly.clear();
		dbPaymentOnly.add(pay);
	}

	//SAVE

	// save client to the database
	public void saveClient() throws SQLException {
		PreparedStatement checkStmt = null;
		PreparedStatement insertStmt = null;
		PreparedStatement updateStmt = null;
		try {
			String checkSql = "SELECT COUNT(*) AS count FROM clients WHERE client_id = ?";
			checkStmt = con.prepareStatement(checkSql);
			String insertSql = "INSERT INTO " + "clients(client_firstName , "
					+ "client_middleName, " + "client_lastName, "
					+ "client_address, " + "client_gender, " + "client_contactNo) "
					+ "VALUES(?, ?, ?, ?, ?, ?)";
			insertStmt = con.prepareStatement(insertSql);
			String updateSql = "UPDATE clients SET " + "client_firstName = ? , "
					+ "client_middleName = ? , " + "client_lastName = ? , "
					+ "client_address = ? , " + "client_gender = ? , "
					+ "client_contactNo = ? WHERE " + "client_id  = ? ";
			updateStmt = con.prepareStatement(updateSql);
			for (Client client : dbClientOnly) {
				int id = client.getId();
				String fname = client.getClientFirstName();
				String mname = client.getClientMiddleName();
				String lname = client.getClientLastName();
				String address = client.getClientAddress();
				String con = client.getClientContactNumber();
				CategoryGender gen = client.getClientGender();
				checkStmt.setInt(1, id);
				ResultSet checkResult = checkStmt.executeQuery();
				checkResult.next();
				int count = checkResult.getInt(1);
				if (count == 0) {
					int col = 1;
					insertStmt.setString(col++, fname);
					insertStmt.setString(col++, mname);
					insertStmt.setString(col++, lname);
					insertStmt.setString(col++, address);
					insertStmt.setString(col++, gen.name());
					insertStmt.setString(col++, con);
					insertStmt.executeUpdate();
					AddLog.addLog(Log.TITLE_CLIENT, Log.SAVE_CLIENT + " : " + lname + " , " + fname + " " + mname);
				} else {
					int col = 1;
					updateStmt.setString(col++, fname);
					updateStmt.setString(col++, mname);
					updateStmt.setString(col++, lname);
					updateStmt.setString(col++, address);
					updateStmt.setString(col++, gen.name());
					updateStmt.setString(col++, con);
					updateStmt.setInt(col++, id);
					updateStmt.executeUpdate();
					AddLog.addLog(Log.TITLE_CLIENT, Log.UPDATE_CLIENT + " : " + lname + " , " + fname + " " + mname);
				}
			}
		} catch (Exception e) {
			AddLog.addLog(Log.TITLE_CLIENT, Log.SAVE_CLIENT_FAILED + " : " + e.getMessage());
			e.printStackTrace();
		} finally {
			insertStmt.close();
			updateStmt.close();
			checkStmt.close();
		}
	}

	// save booking details to the database
	public void saveBooking() throws SQLException {
		PreparedStatement checkStmt = null;
		PreparedStatement insertStmt = null;
		PreparedStatement updateStmt = null;
		try {
			String checkSql = "SELECT COUNT(*) AS count FROM events WHERE event_id = ?";
			checkStmt = con.prepareStatement(checkSql);
			String insertSql = "INSERT INTO " + "events(client_id , "
					+ "event_title, " + "event_venue, " + "event_details, "
					+ "event_type, " + "event_date, " + "event_time, "
					+ "event_guest) " + "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
			insertStmt = con.prepareStatement(insertSql);
			String updateSql = "UPDATE events SET client_id = ? , "
					+ "event_title = ? , event_venue = ? , "
					+ "event_details = ? , event_type = ? , "
					+ "event_date = ? , event_time = ? , "
					+ "event_guest = ? , isEdited = ?  WHERE " + "event_id  = ? ";
			updateStmt = con.prepareStatement(updateSql);
			for (Booking book : dbBookingOnly) {
				int id = book.getId();
				int client_id = book.getClient_id();
				String eventName = book.getEventName();
				String eventVenue = book.getEventVenue();
				String eventDetails = book.getEventDetails();
				int eventGuest = book.getEventGuestNumber();
				String eventDate = book.getEventDate();
				String eventTime = book.getEventTime();
				String eventType = book.getEventType();
				checkStmt.setInt(1, id);
				ResultSet checkResult = checkStmt.executeQuery();
				checkResult.next();
				int count = checkResult.getInt(1);
				if (count == 0) {
					int col = 1;
					insertStmt.setInt(col++, client_id);
					insertStmt.setString(col++, eventName);
					insertStmt.setString(col++, eventVenue);
					insertStmt.setString(col++, eventDetails);
					insertStmt.setString(col++, eventType);
					insertStmt.setString(col++, eventDate);
					insertStmt.setString(col++, eventTime);
					insertStmt.setInt(col++, eventGuest);
					insertStmt.executeUpdate();
					AddLog.addLog(Log.TITLE_BOOKING, Log.SAVE_BOOKING + " : " + eventName);
				} else {
					int col = 1;
					updateStmt.setInt(col++, client_id);
					updateStmt.setString(col++, eventName);
					updateStmt.setString(col++, eventVenue);
					updateStmt.setString(col++, eventDetails);
					updateStmt.setString(col++, eventType);
					updateStmt.setString(col++, eventDate);
					updateStmt.setString(col++, eventTime);
					updateStmt.setInt(col++, eventGuest);
					updateStmt.setString(col++, "true");
					updateStmt.setInt(col++, id);
					updateStmt.executeUpdate();
					AddLog.addLog(Log.TITLE_BOOKING, Log.UPDATE_BOOKING + " : " + eventName);
				}
			}
		} catch (Exception e) {
			AddLog.addLog(Log.TITLE_BOOKING, Log.UPDATE_BOOKING_FAILED + " : " + e.getMessage());
			e.printStackTrace();
		} finally {
			insertStmt.close();
			updateStmt.close();
			checkStmt.close();
		}
	}

	// save event services to the database
	public void saveServicesWanted() throws SQLException {
		PreparedStatement checkStmt = null;
		PreparedStatement insertStmt =null; 
		PreparedStatement updateStmt = null;
		try {
			String checkSql = "SELECT COUNT(*) AS count FROM services_wanted WHERE service_name = ? AND event_id = ?";
			checkStmt = con.prepareStatement(checkSql);
			String insertSql = "INSERT INTO services_wanted (event_id , "
					+ "client_id, service_category_id, package_name, service_name, "
					+ "service_desc, " + "service_cost) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
			insertStmt = con.prepareStatement(insertSql);
			String updateSql = "UPDATE services_wanted SET " + "event_id = ? , "
					+ "client_id = ? , service_category_id = ? , " + "package_name = ? , "
					+ "service_name = ? , " + "service_desc = ? , "
					+ "service_cost = ? " + "WHERE "
					+ "service_name  = ? AND event_id = ? ";
			updateStmt = con.prepareStatement(updateSql);
			int c_id = 0;
			for (ServicesWanted serv : dbServicesWanted) {
				c_id = serv.getClient_id();
				int client_id = serv.getClient_id();
				int event_id = serv.getEvent_id();
				int scid = serv.getScId();
				String pname = serv.getPackageName();
				String sname = serv.getServiceName();
				String sdesc = serv.getServiceDesc();
				int scost = serv.getServiceCost();
				checkStmt.setString(1, sname);
				checkStmt.setInt(2, event_id);
				ResultSet checkResult = checkStmt.executeQuery();
				checkResult.next();
				int count = checkResult.getInt(1);
				if (count >= 1) {
					int col = 1;
					updateStmt.setInt(col++, event_id);
					updateStmt.setInt(col++, client_id);
					updateStmt.setInt(col++, scid);
					updateStmt.setString(col++, pname);
					updateStmt.setString(col++, sname);
					updateStmt.setString(col++, sdesc);
					updateStmt.setInt(col++, scost);
					updateStmt.setString(col++, sname);
					updateStmt.setInt(col++, event_id);
					updateStmt.executeUpdate(); 
				} else {
					int col = 1;
					insertStmt.setInt(col++, event_id);
					insertStmt.setInt(col++, client_id);
					insertStmt.setInt(col++, scid);
					insertStmt.setString(col++, pname);
					insertStmt.setString(col++, sname);
					insertStmt.setString(col++, sdesc);
					insertStmt.setInt(col++, scost);
					insertStmt.executeUpdate();
				}
			}
			AddLog.addLog(Log.TITLE_PACKAGE, Log.SAVE_BOOKING_PACKAGE + " For Client with ID: " + c_id);
			AddLog.addLog(Log.TITLE_SERVICES, Log.SAVE_BOOKING_SERVICES + " For Client with ID: " + c_id);
		} catch (Exception e) {
			AddLog.addLog(Log.TITLE_PACKAGE, Log.SAVE_BOOKING_PACKAGE_FAILED + " : " + e.getMessage());
			AddLog.addLog(Log.TITLE_SERVICES, Log.SAVE_BOOKING_SERVICES_FAILED + " : " + e.getMessage());
			e.printStackTrace();
		} finally {
			insertStmt.close();
			updateStmt.close();
			checkStmt.close();
		}
	}

	// save an events payment to the database
	public void savePayments() throws SQLException {
		PreparedStatement checkStmt = null;
		PreparedStatement insertStmt =  null;
		PreparedStatement updateStmt =  null;
		try {
			String checkSql = "SELECT COUNT(*) AS payments FROM payments WHERE event_id = ? AND client_id = ?";
			checkStmt = con.prepareStatement(checkSql);
			String insertSql = "INSERT INTO " + "payments (client_id, "
					+ "event_id , " + "payment_total, " + "payment_type, "
					+ "payment_paid, " + "payment_balance , "
					+ "payment_datetime ) " + "VALUES (?, ?, ?, ?, ?, ?, ?)";
			insertStmt = con.prepareStatement(insertSql);
			String updateSql = "UPDATE payments SET " + "client_id = ? , "
					+ "event_id = ? , " + "payment_total = ? , "
					+ "payment_type = ? , " + "payment_paid = ? , "
					+ "payment_balance = ? " + ", payment_datetime = ? " + "WHERE "
					+ "payment_id  = ? ";
			updateStmt = con.prepareStatement(updateSql);
			for (Payment pay : dbPaymentOnly) {
				int id = pay.getId();
				int client_id = pay.getClient_id();
				int event_id = pay.getEvent_id();
				int ptotal = pay.getPaymentTotal();
				CategoryPaymentType ptype = pay.getPaymentType();
				int ppaid = pay.getPaymentPaid();
				int pbal = pay.getPaymentBalance();
				String pdate = pay.getPaymentDate();
				checkStmt.setInt(1, event_id);
				checkStmt.setInt(2, client_id);
				ResultSet checkResult = checkStmt.executeQuery();
				checkResult.next();
				int count = checkResult.getInt(1);
				if (count == 0) {
					int col = 1;
					insertStmt.setInt(col++, client_id);
					insertStmt.setInt(col++, event_id);
					insertStmt.setInt(col++, ptotal);
					insertStmt.setString(col++, ptype.name());
					insertStmt.setInt(col++, ppaid);
					insertStmt.setInt(col++, pbal);
					insertStmt.setString(col++, pdate);
					insertStmt.executeUpdate();
				} else {
					int col = 1;
					updateStmt.setInt(col++, client_id);
					updateStmt.setInt(col++, event_id);
					updateStmt.setInt(col++, ptotal);
					updateStmt.setString(col++, ptype.name());
					updateStmt.setInt(col++, ppaid);
					updateStmt.setInt(col++, pbal);
					updateStmt.setString(col++, pdate);
					updateStmt.setInt(col++, id);
					updateStmt.executeUpdate();
				}
			}
			AddLog.addLog(Log.TITLE_PAYMENT, Log.SAVE_INITIAL_PAYMENT);
		} catch (Exception e) {
			AddLog.addLog(Log.TITLE_PAYMENT, Log.SAVE_INITIAL_PAYMENT_FAILED + " : " + e.getMessage());
			e.printStackTrace();
		} finally {
			insertStmt.close();
			updateStmt.close();
			checkStmt.close();
		}
	}

	// save Category details to the database
	public void saveCategory(ServiceCategory sc) throws SQLException {
		String insertSql = "INSERT INTO " + "service_category(service_category_name) " + "VALUES(?)";
		PreparedStatement insertStmt = con.prepareStatement(insertSql);
		String updateSql = "UPDATE service_category SET " + "service_category_name = ? ,service_category_status = ? WHERE " + " service_category_id = ? ";
		PreparedStatement updateStmt = con.prepareStatement(updateSql);
		int id = sc.getId();
		String scname = sc.getServiceCategoryName();
		String stat = sc.getServiceCategoryStatus();
		if (id == 0) {
			int col = 1;
			insertStmt.setString(col++, scname);
			insertStmt.executeUpdate();
		} else {
			int col = 1;
			updateStmt.setString(col++, scname);
			updateStmt.setString(col++, stat);
			updateStmt.setInt(col++, id);
			updateStmt.executeUpdate();
		}
		insertStmt.close();
		updateStmt.close();
	}

	// save notices
	public void saveNotice(Notice not) throws SQLException {
		PreparedStatement checkStmt = null;
		PreparedStatement insertStmt =  null;
		PreparedStatement updateStmt =  null;
		try {
			String checkSql = "SELECT COUNT(*) AS counts FROM notices WHERE notice_id = ?";
			checkStmt = con.prepareStatement(checkSql);
			String insertSql = "INSERT INTO " + "notices (notice_date, "
					+ "notice_desc ) " + "VALUES (?, ?)";
			insertStmt = con.prepareStatement(insertSql);
			String updateSql = "UPDATE payments SET " + "notice_date = ? , "
					+ "notice_desc = ? WHERE "
					+ "notice_id  = ? ";
			updateStmt = con.prepareStatement(updateSql);

			int id = not.getId();
			String date = not.getDate();
			String desc = not.getDesc();

			checkStmt.setInt(1, id);
			ResultSet checkResult = checkStmt.executeQuery();
			checkResult.next();

			int count = checkResult.getInt(1);
			if (count == 0) {
				int col = 1;
				insertStmt.setString(col++, date);
				insertStmt.setString(col++, desc);
				insertStmt.executeUpdate();
			} else {
				int col = 1;
				updateStmt.setString(col++, date);
				updateStmt.setString(col++, desc);
				updateStmt.setInt(col++, id);
				updateStmt.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			insertStmt.close();
			updateStmt.close();
			checkStmt.close();
		}
	}

	//LOAD

	// loads all client records and search if a client has an event or not
	public void loadClientRecords(String cat) throws SQLException {
		dbClientOnly.clear();
		String sql = "SELECT DISTINCT c.client_id, "
				+ "c.client_firstName , c.client_middleName , "
				+ "c.client_lastName, c.client_address , "
				+ "c.client_gender , c.client_contactNo, "
				+ "CASE WHEN (SELECT DISTINCT e.client_id AS eventss "
				+ "FROM events AS e WHERE c.client_id = e.client_id "
				+ "AND e.event_status = 'Open' AND e.event_status2 = 'Ongoing') "
				+ "IS NULL THEN 'false' ELSE 'true' END AS HasEvent "
				+ "FROM clients AS c , events AS e ORDER BY "+ cat + " ASC";
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
				dbClientOnly.add(client);
			}
		} catch (SQLException slqe) {
			throw new SQLException();
		} finally {
			loadStatement.close();
			res.close();
		}
	}
	
	// loads all client records and search if a client has an event or not
	public void loadClientRecordsForBooking(String cat) throws SQLException {
		dbClientOnly.clear();
		String sql = "SELECT DISTINCT c.client_id, "
				+ "c.client_firstName , c.client_middleName , "
				+ "c.client_lastName, c.client_address , "
				+ "c.client_gender , c.client_contactNo, "
				+ "CASE WHEN (SELECT DISTINCT e.client_id AS eventss "
				+ "FROM events AS e WHERE c.client_id = e.client_id "
				+ "AND e.event_status2 = 'Ongoing') "
				+ "IS NULL THEN 'false' ELSE 'true' END AS HasEvent "
				+ "FROM clients AS c , events AS e ORDER BY "+ cat + " ASC";
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
				dbClientOnly.add(client);
			}
		} catch (SQLException slqe) {
			throw new SQLException();
		} finally {
			loadStatement.close();
			res.close();
		}
	}

	// used for reports to fetch all client with event and payment
	public void loadClientWithEventAndPayment() throws SQLException {
		dbClientOnly.clear();
		String sql = "SELECT DISTINCT c.client_id, "
				+ "c.client_firstName, c.client_middleName, "
				+ "c.client_lastName, c.client_address, "
				+ "c.client_gender, c.client_contactNo "
				+ "FROM clients c, payments p , events e "
				+ "WHERE c.client_id = e.client_id "
				+ "AND c.client_id = p.client_id";
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

				Client client = new Client(id, fname, mname, lname, address,
						cont, CategoryGender.valueOf(gender), true);
				dbClientOnly.add(client);
			}
		} catch (SQLException slqe) {
			throw new SQLException();
		} finally {
			loadStatement.close();
			res.close();
		}
	}

	// fetch a package with its services
	public ArrayList<Service> loadServicesRerordsByPackage(int pack_id)
			throws SQLException {
		ArrayList<Service> pservice = new ArrayList<Service>();
		String sql = "SELECT s.service_id, s.package_id, s.service_category_id, "
				+ "s.service_name, ( SELECT c.service_category_name "
				+ "FROM service_category c WHERE s.service_category_id = c.service_category_id) AS service_cat, ( SELECT c.service_category_status "
				+ "FROM service_category c WHERE s.service_category_id = c.service_category_id) AS service_stat "
				+ ", s.service_desc, s.service_cost "
				+ "FROM services s WHERE s.package_id = " + pack_id;
		Statement loadStatement = null;
		ResultSet res = null;
		try {
			loadStatement = con.createStatement();
			res = loadStatement.executeQuery(sql);
			while (res.next()) {
				int id = res.getInt("service_id");
				int pkId = res.getInt("package_id");
				int scid = res.getInt("service_category_id");
				String sname = res.getString("service_name");
				String sdesc = res.getString("service_desc");
				int scost = res.getInt("service_cost");
				String scat = res.getString("service_cat");
				String stat = res.getString("service_stat");

				Service serv = new Service(id, pkId, sname, scost, sdesc, scat);
				serv.setServiceCatStat(stat);
				serv.setScId(scid);
				pservice.add(serv);
			}
			return pservice;
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			return pservice;
		} finally {
			loadStatement.close();
			res.close();
		}
	}

	public Booking loadBookingRecordsByEventId(int event_id) throws SQLException {
		String sql = "SELECT * FROM `events` WHERE event_id = " + event_id
				+ " LIMIT 1";
		Statement loadStatement = null;
		ResultSet res = null;
		Booking book = null;
		try {
			loadStatement = con.createStatement();
			res = loadStatement.executeQuery(sql);
			while (res.next()) {
				int id = res.getInt("event_id");
				int client_id = res.getInt("client_id");
				String eventName = res.getString("event_title");
				String eventVenue = res.getString("event_venue");
				String eventDetails = res.getString("event_details");
				String eventType = res.getString("event_type");
				String eventDate = res.getString("event_date");
				String eventTime = res.getString("event_time");
				int eventGuest = res.getInt("event_guest");
				String eventStat = res.getString("event_status");
				String eventStat2 = res.getString("event_status2");
				String eventPaymentStatus = res.getString("event_paymentStatus");
				book = new Booking(id, client_id, eventName,
						eventVenue, eventDetails, eventGuest, eventDate,
						eventTime, eventType);
				book.setEventStatus(eventStat);
				book.setHasServices(true);
				book.setEventStatus2(eventStat2);
				book.setEventPaymentStatus(eventPaymentStatus);
				return book;
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			loadStatement.close();
			res.close();
		}
		return book;
	}

	public ArrayList<Booking> loadBookingRecordsByEventDate(String currentDate) throws SQLException {
		ArrayList<Booking> bookingList = new ArrayList<Booking>();
		String sql = "SELECT * FROM `events` WHERE event_date = \"" + currentDate + "\" "
				+ "AND `event_status` = \"Open\" "
				+ "AND `event_status2` = \"Ongoing\" "
				+ "AND `event_paymentStatus` <> \"No Payment\"";
		Statement loadStatement = null;
		ResultSet res = null;
		Booking book = null;
		try {
			loadStatement = con.createStatement();
			res = loadStatement.executeQuery(sql);
			while (res.next()) {
				int id = res.getInt("event_id");
				int client_id = res.getInt("client_id");
				String eventName = res.getString("event_title");
				String eventVenue = res.getString("event_venue");
				String eventDetails = res.getString("event_details");
				String eventType = res.getString("event_type");
				String eventDate = res.getString("event_date");
				String eventTime = res.getString("event_time");
				int eventGuest = res.getInt("event_guest");
				String eventStat = res.getString("event_status");
				String eventStat2 = res.getString("event_status2");
				String eventPaymentStatus = res.getString("event_paymentStatus");
				book = new Booking(id, client_id, eventName,
						eventVenue, eventDetails, eventGuest, eventDate,
						eventTime, eventType);
				book.setEventStatus(eventStat);
				book.setHasServices(true);
				book.setEventStatus2(eventStat2);
				book.setEventPaymentStatus(eventPaymentStatus);
				bookingList.add(book);
			}
			return bookingList;
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			loadStatement.close();
			res.close();
		}
		return bookingList;
	}

	// load all event by client without services
	public void loadAllBookingRecordsByClientWithouServices(int client_ids)
			throws SQLException {
		dbBookingOnly.clear();
		String sql = "SELECT * FROM `events` e WHERE e.client_id = " + client_ids
				+ " AND NOT EXISTS(SELECT * FROM `services_wanted` s WHERE e.event_id = s.event_id)";
		Statement loadStatement = null;
		ResultSet res = null;
		try {
			loadStatement = con.createStatement();
			res = loadStatement.executeQuery(sql);
			while (res.next()) {
				int id = res.getInt("event_id");
				int client_id = res.getInt("client_id");
				String eventName = res.getString("event_title");
				String eventVenue = res.getString("event_venue");
				String eventDetails = res.getString("event_details");
				String eventType = res.getString("event_type");
				String eventDate = res.getString("event_date");
				String eventTime = res.getString("event_time");
				int eventGuest = res.getInt("event_guest");
				String eventStatus = res.getString("event_status");
				String eventStatus2 = res.getString("event_status2");
				String eventPaymentStatus = res.getString("event_paymentStatus");

				Booking book = new Booking(id, client_id, eventName,
						eventVenue, eventDetails, eventGuest, eventDate,
						eventTime, eventType);
				book.setEventStatus(eventStatus);
				book.setEventStatus2(eventStatus2);
				book.setEventPaymentStatus(eventPaymentStatus);
				dbBookingOnly.add(book);
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			loadStatement.close();
			res.close();
		}
	}

	// load all services from the services list with category
	public void loadAllServicesFromServiceList() throws SQLException {
		dbServiceList.clear();
		String sql = "SELECT s.sl_id , s.service_category_id, (SELECT c.service_category_name "
				+ "FROM service_category c WHERE s.service_category_id = c.service_category_id) AS category_name, ( SELECT c.service_category_status "
				+ "FROM service_category c WHERE s.service_category_id = c.service_category_id) AS service_stat "
				+ ", s.service_name , s.service_cost, "
				+ "s.service_desc FROM services_list s ORDER BY `s`.`service_name` ASC";
		Statement loadStatement = null;
		ResultSet res = null;
		try {
			loadStatement = con.createStatement();
			res = loadStatement.executeQuery(sql);
			while (res.next()) {
				int id = res.getInt("sl_id");
				int scid = res.getInt("service_category_id");
				String sname = res.getString("service_name");
				String sdesc = res.getString("service_desc");
				int scost = res.getInt("service_cost");
				String scat = res.getString("category_name");
				String stat = res.getString("service_stat");

				ServiceList serv = new ServiceList(id, sname, scost, sdesc, scat);
				serv.setServiceCatStat(stat);
				serv.setService_catId(scid);
				dbServiceList.add(serv);
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			loadStatement.close();
			res.close();
		}
	}

	// loads all event by client
	//stat = Open , Close
	//Statu = Ongoing , Done, Cancel
	//payStat = No Payment, Partial, Fully Paid
	public void loadAllBookingRecordsByClientID(int client_ids)
			throws SQLException {
		dbBookingOnly.clear();
		String sql = "SELECT DISTINCT e.event_id , "
				+ "e.client_id, e.event_title , "
				+ "e.event_venue, e.event_details, "
				+ "e.event_status, e.event_status2, "
				+ "e.event_type, e.event_date, "
				+ "e.event_time, e.event_guest, e.event_paymentStatus ,"
				+ "CASE WHEN (SELECT DISTINCT " + "s.event_id as eventss "
				+ "FROM services_wanted AS s "
				+ "WHERE e.event_id = s.event_id) " + "IS NULL THEN 'false' "
				+ "ELSE 'true' END " + "AS HasServices " + "FROM events AS e "
				+ "WHERE e.client_id = " + client_ids +" ";
		Statement loadStatement = null;
		ResultSet res = null;
		try {
			loadStatement = con.createStatement();
			res = loadStatement.executeQuery(sql);
			while (res.next()) {
				int id = res.getInt("event_id");
				int client_id = res.getInt("client_id");
				String eventName = res.getString("event_title");
				String eventVenue = res.getString("event_venue");
				String eventDetails = res.getString("event_details");
				String eventType = res.getString("event_type");
				String eventDate = res.getString("event_date");
				String eventTime = res.getString("event_time");
				int eventGuest = res.getInt("event_guest");
				boolean hsv = res.getBoolean("HasServices");
				String eventStatus = res.getString("event_status");
				String eventStatus2 = res.getString("event_status2");
				String eventPaymentStatus = res.getString("event_paymentStatus");
				Booking book = new Booking(id, client_id, eventName,
						eventVenue, eventDetails, eventGuest, eventDate,
						eventTime, eventType);
				book.setHasServices(hsv);
				book.setEventStatus(eventStatus);
				book.setEventStatus2(eventStatus2);
				book.setEventPaymentStatus(eventPaymentStatus);
				dbBookingOnly.add(book);
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			loadStatement.close();
			res.close();
		}
	}

	// loads all event by client
	//stat = Open , Close
	//Statu = Ongoing , Done, Cancel
	//payStat = No Payment, Partial, Fully Paid
	public void loadBookingRecordsByClientIdForPrintReports(int client_ids)
			throws SQLException {
		dbBookingOnly.clear();
		String sql = "SELECT DISTINCT e.event_id , "
				+ "e.client_id, e.event_title , "
				+ "e.event_venue, e.event_details, "
				+ "e.event_status, e.event_status2, "
				+ "e.event_type, e.event_date, "
				+ "e.event_time, e.event_guest, e.event_paymentStatus ,"
				+ "CASE WHEN (SELECT DISTINCT " + "s.event_id as eventss "
				+ "FROM services_wanted AS s "
				+ "WHERE e.event_id = s.event_id) " + "IS NULL THEN 'false' "
				+ "ELSE 'true' END " + "AS HasServices " + "FROM events AS e "
				+ "WHERE e.client_id = " + client_ids +" AND e.event_status <> 'Close' "
				+ "AND e.event_status2 <> 'Cancel' AND e.event_paymentStatus <> 'No Payment'" ;
		Statement loadStatement = null;
		ResultSet res = null;
		try {
			loadStatement = con.createStatement();
			res = loadStatement.executeQuery(sql);
			while (res.next()) {
				int id = res.getInt("event_id");
				int client_id = res.getInt("client_id");
				String eventName = res.getString("event_title");
				String eventVenue = res.getString("event_venue");
				String eventDetails = res.getString("event_details");
				String eventType = res.getString("event_type");
				String eventDate = res.getString("event_date");
				String eventTime = res.getString("event_time");
				int eventGuest = res.getInt("event_guest");
				boolean hsv = res.getBoolean("HasServices");
				String eventStatus = res.getString("event_status");
				String eventStatus2 = res.getString("event_status2");
				String eventPaymentStatus = res.getString("event_paymentStatus");
				Booking book = new Booking(id, client_id, eventName,
						eventVenue, eventDetails, eventGuest, eventDate,
						eventTime, eventType);
				book.setHasServices(hsv);
				book.setEventStatus(eventStatus);
				book.setEventStatus2(eventStatus2);
				book.setEventPaymentStatus(eventPaymentStatus);
				dbBookingOnly.add(book);
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			loadStatement.close();
			res.close();
		}
	}

	// loads all event by client
	//stat = Open , Close
	//Statu = Ongoing , Done, Cancel
	//payStat = No Payment, Partial, Fully Paid
	public void loadBookingRecordsByClientIDTypeStatus(int client_ids, int type, String status)
			throws SQLException {
		dbBookingOnly.clear();
		String sql = "";
		if(type == 1) {
			sql = "SELECT DISTINCT e.event_id , "
					+ "e.client_id, e.event_title , "
					+ "e.event_venue, e.event_details, "
					+ "e.event_status, e.event_status2, "
					+ "e.event_type, e.event_date, "
					+ "e.event_time, e.event_guest, e.event_paymentStatus ,"
					+ "CASE WHEN (SELECT DISTINCT " + "s.event_id as eventss "
					+ "FROM services_wanted AS s "
					+ "WHERE e.event_id = s.event_id) " + "IS NULL THEN 'false' "
					+ "ELSE 'true' END " + "AS HasServices " + "FROM events AS e "
					+ "WHERE e.client_id = " + client_ids +" AND e.event_status = '" + status +"'" ;
		} else if(type == 2) {
			sql = "SELECT DISTINCT e.event_id , "
					+ "e.client_id, e.event_title , "
					+ "e.event_venue, e.event_details, "
					+ "e.event_status, e.event_status2, "
					+ "e.event_type, e.event_date, "
					+ "e.event_time, e.event_guest, e.event_paymentStatus ,"
					+ "CASE WHEN (SELECT DISTINCT " + "s.event_id as eventss "
					+ "FROM services_wanted AS s "
					+ "WHERE e.event_id = s.event_id) " + "IS NULL THEN 'false' "
					+ "ELSE 'true' END " + "AS HasServices " + "FROM events AS e "
					+ "WHERE e.client_id = " + client_ids +" AND e.event_status2 = '"+ status + "'";
		} else if(type == 3) {
			sql = "SELECT DISTINCT e.event_id , "
					+ "e.client_id, e.event_title , "
					+ "e.event_venue, e.event_details, "
					+ "e.event_status, e.event_status2, "
					+ "e.event_type, e.event_date, "
					+ "e.event_time, e.event_guest, e.event_paymentStatus ,"
					+ "CASE WHEN (SELECT DISTINCT " + "s.event_id as eventss "
					+ "FROM services_wanted AS s "
					+ "WHERE e.event_id = s.event_id) " + "IS NULL THEN 'false' "
					+ "ELSE 'true' END " + "AS HasServices " + "FROM events AS e "
					+ "WHERE e.client_id = " + client_ids +" AND e.event_paymentStatus = '" + status +"' ";
		}
		Statement loadStatement = null;
		ResultSet res = null;
		try {
			loadStatement = con.createStatement();
			res = loadStatement.executeQuery(sql);
			while (res.next()) {
				int id = res.getInt("event_id");
				int client_id = res.getInt("client_id");
				String eventName = res.getString("event_title");
				String eventVenue = res.getString("event_venue");
				String eventDetails = res.getString("event_details");
				String eventType = res.getString("event_type");
				String eventDate = res.getString("event_date");
				String eventTime = res.getString("event_time");
				int eventGuest = res.getInt("event_guest");
				boolean hsv = res.getBoolean("HasServices");
				String eventStatus = res.getString("event_status");
				String eventStatus2 = res.getString("event_status2");
				String eventPaymentStatus = res.getString("event_paymentStatus");
				Booking book = new Booking(id, client_id, eventName,
						eventVenue, eventDetails, eventGuest, eventDate,
						eventTime, eventType);
				book.setHasServices(hsv);
				book.setEventStatus(eventStatus);
				book.setEventStatus2(eventStatus2);
				book.setEventPaymentStatus(eventPaymentStatus);
				dbBookingOnly.add(book);
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			loadStatement.close();
			res.close();
		}
	}

	// loads all event
	public void loadAllBookingRecords(String status, String statusu, String payment) throws SQLException {
		dbBookingOnly.clear();
		String sql = "";
		if(status.isEmpty() && statusu.isEmpty() && payment.isEmpty()) {
			sql = "SELECT DISTINCT e.event_id , "
					+ "e.client_id, e.event_title , "
					+ "e.event_venue, e.event_details, "
					+ "e.event_status, e.event_status2, "
					+ "e.event_type, e.event_date, "
					+ "e.event_time, e.event_guest,  e.event_paymentStatus ,"
					+ "CASE WHEN (SELECT DISTINCT " + "s.event_id as eventss "
					+ "FROM services_wanted AS s "
					+ "WHERE e.event_id = s.event_id) " + "IS NULL THEN 'false' "
					+ "ELSE 'true' END " + "AS HasServices FROM events AS e ";
		} else if(statusu.isEmpty() && payment.isEmpty()) {
			sql = "SELECT DISTINCT e.event_id , "
					+ "e.client_id, e.event_title , "
					+ "e.event_venue, e.event_details, "
					+ "e.event_status, e.event_status2, "
					+ "e.event_type, e.event_date, "
					+ "e.event_time, e.event_guest,  e.event_paymentStatus ,"
					+ "CASE WHEN (SELECT DISTINCT " + "s.event_id as eventss "
					+ "FROM services_wanted AS s "
					+ "WHERE e.event_id = s.event_id) " + "IS NULL THEN 'false' "
					+ "ELSE 'true' END " + "AS HasServices FROM events AS e "
					+ "WHERE e.event_status = '"+ status +"'";
		} else if(payment.isEmpty()) {
			sql = "SELECT DISTINCT e.event_id , "
					+ "e.client_id, e.event_title , "
					+ "e.event_venue, e.event_details, "
					+ "e.event_status, e.event_status2, "
					+ "e.event_type, e.event_date, "
					+ "e.event_time, e.event_guest,  e.event_paymentStatus ,"
					+ "CASE WHEN (SELECT DISTINCT " + "s.event_id as eventss "
					+ "FROM services_wanted AS s "
					+ "WHERE e.event_id = s.event_id) " + "IS NULL THEN 'false' "
					+ "ELSE 'true' END " + "AS HasServices FROM events AS e "
					+ "WHERE e.event_status = '"+ status +"' "
					+ "AND e.event_status2 = '"+ statusu +"'";
		} else {
			sql = "SELECT DISTINCT e.event_id , "
					+ "e.client_id, e.event_title , "
					+ "e.event_venue, e.event_details, "
					+ "e.event_status, e.event_status2, "
					+ "e.event_type, e.event_date, "
					+ "e.event_time, e.event_guest,  e.event_paymentStatus ,"
					+ "CASE WHEN (SELECT DISTINCT " + "s.event_id as eventss "
					+ "FROM services_wanted AS s "
					+ "WHERE e.event_id = s.event_id) " + "IS NULL THEN 'false' "
					+ "ELSE 'true' END " + "AS HasServices FROM events AS e "
					+ "WHERE e.event_status = '"+ status +"' AND "
					+ "e.event_status2 = '"+ statusu +"' AND "
					+ "e.event_paymentStatus = '"+ payment +"'";
		}

		Statement loadStatement = null;
		ResultSet res = null;
		try {
			loadStatement = con.createStatement();
			res = loadStatement.executeQuery(sql);
			while (res.next()) {
				int id = res.getInt("event_id");
				int client_id = res.getInt("client_id");
				String eventName = res.getString("event_title");
				String eventVenue = res.getString("event_venue");
				String eventDetails = res.getString("event_details");
				String eventType = res.getString("event_type");
				String eventDate = res.getString("event_date");
				String eventTime = res.getString("event_time");
				int eventGuest = res.getInt("event_guest");
				String eventStatus = res.getString("event_status");
				String eventStatus2 = res.getString("event_status2");
				boolean hsv = res.getBoolean("HasServices");
				String eventPaymentStatus = res.getString("event_paymentStatus");
				Booking book = new Booking(id, client_id, eventName,
					eventVenue, eventDetails, eventGuest, eventDate,
					eventTime, eventType);
				book.setHasServices(hsv);
				book.setEventStatus(eventStatus);
				book.setEventStatus2(eventStatus2);
				book.setEventPaymentStatus(eventPaymentStatus);
			dbBookingOnly.add(book);
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			loadStatement.close();
			res.close();
		}
	}

	// loads all packages
	public void loadPackageRecords() throws SQLException {
		dbPackagesOnly.clear();
		String sql = "SELECT * FROM packages ORDER BY `package_name` ASC";
		Statement loadStatement = null;
		ResultSet res = null;
		try {
			loadStatement = con.createStatement();
			res = loadStatement.executeQuery(sql);
			while (res.next()) {
				int id = res.getInt("package_id");
				String pname = res.getString("package_name");
				String pdesc = res.getString("package_desc");
				int pcost = res.getInt("package_cost");
				ArrayList<Service> pservice = loadServicesRerordsByPackage(id);

				if(pservice.size() == 0) {
					pcost = 0;
				}
				Package pack = new Package(id, pname, pdesc, pcost, pservice);
				dbPackagesOnly.add(pack);
			}
		} catch (SQLException sqle) {
			throw new SQLException();
		} finally {
			loadStatement.close();
			res.close();
		}
	}

	// loads services record by client and event
	public void loadServicesRecords(int client_ids, int event_ids)
			throws SQLException {
		dbServicesWanted.clear();
		String sql = "SELECT s.`sw_id`, `event_id`, s.`client_id`, "
				+ "s.`package_name`, s.`service_name`, s.service_category_id ,"
				+ "(SELECT c.`service_category_name` "
				+ "FROM service_category c "
				+ "WHERE c.service_category_id = s.service_category_id) "
				+ "AS service_cat, ( SELECT c.service_category_status "
				+ "FROM service_category c WHERE s.service_category_id = c.service_category_id) AS service_stat, s.`service_desc`, s.`service_cost` "
				+ "FROM `services_wanted` s WHERE `client_id` = "
				+ client_ids + " AND " + "`event_id` = " + event_ids;
		Statement loadStatement = null;
		ResultSet res = null;
		try {
			loadStatement = con.createStatement();
			res = loadStatement.executeQuery(sql);
			while (res.next()) {
				int id = res.getInt("sw_id");
				int client_id = res.getInt("client_id");
				int event_id = res.getInt("event_id");
				String packageName = res.getString("package_name");
				String serviceName = res.getString("service_name");
				String serviceCat = res.getString("service_cat");
				String serviceDesc = res.getString("service_desc");
				int serviceCost = res.getInt("service_cost");
				int scId = res.getInt("service_category_id");
				String stat = res.getString("service_stat");
				ServicesWanted serv = new ServicesWanted(id, event_id,
						client_id, packageName, serviceName,serviceCat, serviceDesc,
						serviceCost);
				serv.setScId(scId);
				serv.setServiceCatStat(stat);
				dbServicesWanted.add(serv);
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			loadStatement.close();
			res.close();
		}
	}

	// loads a payment record by client and event
	public Payment loadPaymentRecord(int client_ids, int event_ids)
			throws SQLException {
		Payment pay = null;
		String sql = "SELECT * FROM `payments` WHERE client_id = " + client_ids
				+ " AND event_id = " + event_ids + " LIMIT 1";
		Statement loadStatement = null;
		ResultSet res = null;
		try {
			loadStatement = con.createStatement();
			res = loadStatement.executeQuery(sql);
			while (res.next()) {
				int id = res.getInt("payment_id");
				int client_id = res.getInt("client_id");
				int event_id = res.getInt("event_id");
				int paymentTotal = res.getInt("payment_total");
				int paymentPaid = res.getInt("payment_paid");
				int paymentBalance = res.getInt("payment_balance");
				String paymentType = res.getString("payment_type");
				String paymentDate = res.getString("payment_datetime");
				pay = new Payment(id, client_id, event_id,
						paymentTotal, CategoryPaymentType.valueOf(paymentType),
						paymentPaid, paymentBalance, paymentDate);
			}
			return pay;
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			loadStatement.close();
			res.close();
		}
		return pay;
	}

	// loads an event and client record that have payments
	public void loadEventsWithServicesAndPayments() throws SQLException {
		dbHomeData.clear();
		String sql = "SELECT DISTINCT "
				+ "c.client_id, e.event_id, p.payment_id, "
				+ "c.client_firstName , c.client_middleName , "
				+ "c.client_lastName , c.client_contactNo , "
				+ "c.client_address , e.event_title , "
				+ "e.event_type , e.event_date , "
				+ "e.event_time , e.event_venue , "
				+ "e.event_guest , p.payment_type , " + "p.payment_paid "
				+ "FROM clients c , events e, payments p "
				+ "WHERE c.client_id = e.client_id "
				+ "AND e.event_id = p.event_id "
				+ "AND p.client_id = c.client_id "
				+ "AND e.event_status2 = 'Ongoing' ORDER BY e.event_title ASC";
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
				String eventName = res.getString("event_title");
				String eventType = res.getString("event_type");
				String eventDate = res.getString("event_date");
				String eventTime = res.getString("event_time");
				String eventVenue = res.getString("event_venue");
				int eventGuest = res.getInt("event_guest");
				String paymentType = res.getString("payment_type");

				HomeData hmd = new HomeData(client_id, event_id, payment_id,
						clientFirstName, clientMiddleName, clientLastName,
						eventName, eventType,
						eventDate, eventTime, eventVenue, eventGuest,
						CategoryPaymentType.valueOf(paymentType));
				dbHomeData.add(hmd);
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			loadStatement.close();
			res.close();
		}
	}

	//Load all Categories for services
	public void loadAllServiceCategory() throws SQLException {
		dbServiceCat.clear();
		String sql = "SELECT * FROM service_category WHERE service_category_status = 'Normal'";
		Statement loadStatement = null;
		ResultSet res = null;
		try {
			loadStatement = con.createStatement();
			res = loadStatement.executeQuery(sql);
			while (res.next()) {
				int id = res.getInt("service_category_id");
				String scname = res.getString("service_category_name");
				String scstat = res.getString("service_category_status");
				ServiceCategory scat = new ServiceCategory(id, scname,scstat);
				dbServiceCat.add(scat);
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			loadStatement.close();
			res.close();
		}
	}

	//Load Notices
	public void loadAllNotices() throws SQLException {
		dbNotice.clear();
		String sql = "SELECT * FROM notices";
		Statement loadStatement = null;
		ResultSet res = null;
		try {
			loadStatement = con.createStatement();
			res = loadStatement.executeQuery(sql);
			while (res.next()) {
				int id = res.getInt("notice_id");
				String date = res.getString("notice_date");
				String desc = res.getString("notice_desc");

				Notice not = new Notice(id,date, desc);
				dbNotice.add(not);
				
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			loadStatement.close();
			res.close();
		}
	}

	//Load Notices
	public Notice loadLastNotice() throws SQLException {
		String sql = "SELECT * FROM notices ORDER BY notice_id DESC LIMIT 1";
		Statement loadStatement = null;
		ResultSet res = null;
		Notice not = null;
		try {
			loadStatement = con.createStatement();
			res = loadStatement.executeQuery(sql);
			while (res.next()) {
				int id = res.getInt("notice_id");
				String date = res.getString("notice_date");
				String desc = res.getString("notice_desc");
				not = new Notice(id,date, desc);
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			loadStatement.close();
			res.close();
		}
		return not;
	}

	//GET

	public ArrayList<Client> getClient() {
		return dbClientOnly;
	}

	public ArrayList<Booking> getBooking() {
		return dbBookingOnly;
	}

	public ArrayList<Service> getService() {
		return dbServicesOnly;
	}

	public ArrayList<Package> getPackage() {
		return dbPackagesOnly;
	}

	public ArrayList<ServiceList> getServiceList() {
		return dbServiceList;
	}

	public ArrayList<HomeData> getHomeData() {
		return dbHomeData;
	}

	public ArrayList<ServicesWanted> getServicesWanted() {
		return dbServicesWanted;
	}

	public ArrayList<Payment> getPaymentRecord() {
		return dbPaymentOnly;
	}

	public ArrayList<ServiceCategory> getServiceCatRecord() {
		return dbServiceCat;
	}

	public ArrayList<Notice> getNotices() {
		return dbNotice;
	}

	//update event status - Open or Close
	public void updateBookingStatus(int event_id, String status) throws SQLException {
		String updateSql = "UPDATE `events` SET `event_status` = ? , `isEdited` = ? WHERE `events`.`event_id` = ?;";
		PreparedStatement updateStmt = con.prepareStatement(updateSql);
		int col = 1;
		updateStmt.setString(col++, status);
		updateStmt.setString(col++, "true");
		updateStmt.setInt(col++, event_id);
		updateStmt.executeUpdate();
		updateStmt.close();
	}

	public void updateBookingStatus2(int event_id, String status) throws SQLException {
		String updateSql = "UPDATE `events` SET `event_status2` = ? , `isEdited` = ? WHERE `events`.`event_id` = ?;";
		PreparedStatement updateStmt = con.prepareStatement(updateSql);
		int col = 1;
		updateStmt.setString(col++, status);
		updateStmt.setString(col++, "true");
		updateStmt.setInt(col++, event_id);
		updateStmt.executeUpdate();
		updateStmt.close();
	}

	public void updateBookingStatusPayment(int event_id, String status) throws SQLException {
		String updateSql = "UPDATE `events` SET `event_paymentStatus` = ? , `isEdited` = ? WHERE `events`.`event_id` = ?;";
		PreparedStatement updateStmt = con.prepareStatement(updateSql);
		int col = 1;
		updateStmt.setString(col++, status);
		updateStmt.setString(col++, "true");
		updateStmt.setInt(col++, event_id);
		updateStmt.executeUpdate();
		updateStmt.close();
	}

	public void updateBookingisEdited() throws SQLException {
		String updateSql = "UPDATE `events` SET `isEdited` = ? WHERE `events`.`isEdited` =  \"true\"";
		PreparedStatement updateStmt = con.prepareStatement(updateSql);
		int col = 1;
		updateStmt.setString(col++, "false");
		updateStmt.executeUpdate();
		updateStmt.close();
	}

	// Search From all Event
	public void searchAllBookingRecords(String val, String cat, String status, String statusu, String payment) throws SQLException {
		String value = val.replace("'", "");
		dbBookingOnly.clear();
		String sql = "";
		
		if(status.isEmpty() && statusu.isEmpty() && payment.isEmpty()) {
			sql = "SELECT DISTINCT e.event_id , "
					+ "e.client_id, e.event_title , "
					+ "e.event_venue, e.event_details, "
					+ "e.event_status, e.event_status2, "
					+ "e.event_type, e.event_date, "
					+ "e.event_time, e.event_guest,  e.event_paymentStatus ,"
					+ "CASE WHEN (SELECT DISTINCT " + "s.event_id as eventss "
					+ "FROM services_wanted AS s "
					+ "WHERE e.event_id = s.event_id) " + "IS NULL THEN 'false' "
					+ "ELSE 'true' END " + "AS HasServices FROM events AS e "
					+ "WHERE " + cat + " LIKE '%" + value + "%'";
		} else if(statusu.isEmpty() && payment.isEmpty()) {
			sql = "SELECT DISTINCT e.event_id , "
					+ "e.client_id, e.event_title , "
					+ "e.event_venue, e.event_details, "
					+ "e.event_status, e.event_status2, "
					+ "e.event_type, e.event_date, "
					+ "e.event_time, e.event_guest,  e.event_paymentStatus ,"
					+ "CASE WHEN (SELECT DISTINCT " + "s.event_id as eventss "
					+ "FROM services_wanted AS s "
					+ "WHERE e.event_id = s.event_id) " + "IS NULL THEN 'false' "
					+ "ELSE 'true' END " + "AS HasServices FROM events AS e "
					+ "WHERE " + cat + " LIKE '%" + value + "%' AND "
					+ "e.event_status = '"+ status +"'";
		} else if(payment.isEmpty()) {
			sql = "SELECT DISTINCT e.event_id , "
					+ "e.client_id, e.event_title , "
					+ "e.event_venue, e.event_details, "
					+ "e.event_status, e.event_status2, "
					+ "e.event_type, e.event_date, "
					+ "e.event_time, e.event_guest,  e.event_paymentStatus ,"
					+ "CASE WHEN (SELECT DISTINCT " + "s.event_id as eventss "
					+ "FROM services_wanted AS s "
					+ "WHERE e.event_id = s.event_id) " + "IS NULL THEN 'false' "
					+ "ELSE 'true' END " + "AS HasServices FROM events AS e "
					+ "WHERE " + cat + " LIKE '%" + value + "%' AND "
					+ "e.event_status = '"+ status +"' "
					+ "AND e.event_status2 = '"+ statusu +"'";
		} else {
			sql = "SELECT DISTINCT e.event_id , "
					+ "e.client_id, e.event_title , "
					+ "e.event_venue, e.event_details, "
					+ "e.event_status, e.event_status2, "
					+ "e.event_type, e.event_date, "
					+ "e.event_time, e.event_guest,  e.event_paymentStatus ,"
					+ "CASE WHEN (SELECT DISTINCT " + "s.event_id as eventss "
					+ "FROM services_wanted AS s "
					+ "WHERE e.event_id = s.event_id) " + "IS NULL THEN 'false' "
					+ "ELSE 'true' END " + "AS HasServices FROM events AS e "
					+ "WHERE " + cat + " LIKE '%" + value + "%' AND "
					+ "e.event_status = '"+ status +"' AND "
					+ "e.event_status2 = '"+ statusu +"' AND "
					+ "e.event_paymentStatus = '"+ payment +"'";
		}
		Statement loadStatement = null;
		ResultSet res = null;
		try {
			loadStatement = con.createStatement();
			res = loadStatement.executeQuery(sql);
			while (res.next()) {
				int id = res.getInt("event_id");
				int client_id = res.getInt("client_id");
				String eventName = res.getString("event_title");
				String eventVenue = res.getString("event_venue");
				String eventDetails = res.getString("event_details");
				String eventType = res.getString("event_type");
				String eventDate = res.getString("event_date");
				String eventTime = res.getString("event_time");
				int eventGuest = res.getInt("event_guest");
				boolean hsv = res.getBoolean("HasServices");
				String eventStat = res.getString("event_status");
				String eventStat2 = res.getString("event_status2");
				String eventPaymentStatus = res.getString("e.event_paymentStatus");
				Booking book = new Booking(id, client_id, eventName,
						eventVenue, eventDetails, eventGuest, eventDate,
						eventTime, eventType);
				book.setHasServices(hsv);
				book.setEventStatus(eventStat);
				book.setEventStatus2(eventStat2);
				book.setEventPaymentStatus(eventPaymentStatus);
				dbBookingOnly.add(book);
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			loadStatement.close();
			res.close();
		}
	}

	// search client by name - first, middle and last
	public void searchClient(String value) throws SQLException {
		String val = value.replace("'", "");
		dbClientOnly.clear();
		String sql = "SELECT DISTINCT "
				+ "c.client_id, "
				+ "c.client_firstName , "
				+ "c.client_middleName , "
				+ "c.client_lastName, "
				+ "c.client_address , "
				+ "c.client_gender , "
				+ "c.client_contactNo, "
				+ "CASE WHEN (SELECT DISTINCT "
				+ "e.client_id AS eventss "
				+ "FROM events AS e "
				+ "WHERE c.client_id = e.client_id ) "
				+ "IS NULL THEN 'false' ELSE 'true' END AS HasEvent "
				+ "FROM clients AS c , events AS e WHERE c.client_firstName LIKE '%"
				+ val + "%' " + "OR c.client_middleName LIKE '%" + val + "%' "
				+ "OR c.client_lastName LIKE '%" + val + "%'";
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
				dbClientOnly.add(client);
			}
		} catch (SQLException slqe) {
			throw new SQLException();
		} finally {
			loadStatement.close();
			res.close();
		}
	}

	// used for reports to fetch all client with event and payment
	public void searchClientWithEventAndPayment(String category, String value) throws SQLException {
		String val = value.replace("'", "");
		dbClientOnly.clear();
		String sql = "SELECT DISTINCT c.client_id, "
				+ "c.client_firstName, c.client_middleName, "
				+ "c.client_lastName, c.client_address, "
				+ "c.client_gender, c.client_contactNo "
				+ "FROM clients c, payments p , events e "
				+ "WHERE c.client_id = e.client_id "
				+ "AND c.client_id = p.client_id "
				+ "AND c."+ category+" LIKE '%"+val+"%'";
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

				Client client = new Client(id, fname, mname, lname, address,
						cont, CategoryGender.valueOf(gender), true);
				dbClientOnly.add(client);
			}
		} catch (SQLException slqe) {
			throw new SQLException();
		} finally {
			loadStatement.close();
			res.close();
		}
	}

	// serach client by client id
	public void searchClientById(int cid) throws SQLException {

		dbClientOnly.clear();
		String sql = "SELECT * FROM clients WHERE client_id = " + cid
				+ " LIMIT 1";
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

				Client client = new Client(id, fname, mname, lname, address,
						cont, CategoryGender.valueOf(gender), true);
				dbClientOnly.add(client);
			}
		} catch (SQLException slqe) {
			throw new SQLException();
		} finally {
			loadStatement.close();
			res.close();
		}
	}

	public void searchEventsWithServicesAndPayments(String category,
			String value) throws SQLException {
		String val = value.replace("'", "");
		dbHomeData.clear();
		String sql = "SELECT DISTINCT " + "c.client_id, " + "e.event_id, "
				+ "p.payment_id, " + "c.client_firstName , "
				+ "c.client_middleName , " + "c.client_lastName , "
				+ "c.client_contactNo , " + "c.client_address , "
				+ "e.event_title , " + "e.event_type , " + "e.event_date , "
				+ "e.event_time , " + "e.event_venue , " + "e.event_guest , "
				+ "p.payment_type , " + "p.payment_paid "
				+ "FROM clients c , events e, payments p "
				+ "WHERE c.client_id = e.client_id "
				+ "AND e.event_id = p.event_id "
				+ "AND p.client_id = c.client_id "
				+ "AND e.event_status2 = 'Ongoing' " 
				+ "AND " + category
				+ " LIKE '%" + val + "%'";
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
				String eventName = res.getString("event_title");
				String eventType = res.getString("event_type");
				String eventDate = res.getString("event_date");
				String eventTime = res.getString("event_time");
				String eventVenue = res.getString("event_venue");
				int eventGuest = res.getInt("event_guest");
				String paymentType = res.getString("payment_type");

				HomeData hmd = new HomeData(client_id, event_id, payment_id,
						clientFirstName, clientMiddleName, clientLastName,
						eventName, eventType,
						eventDate, eventTime, eventVenue, eventGuest,
						CategoryPaymentType.valueOf(paymentType));
				dbHomeData.add(hmd);
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			loadStatement.close();
			res.close();
		}
	}

	// delete client by id from the database
	public void deleteClientInfo(int client_id) throws SQLException {
		String delSql = "DELETE FROM `clients` WHERE `client_id` = ?";
		PreparedStatement deleteStatement = con.prepareStatement(delSql);
		deleteStatement.setInt(1, client_id);
		deleteStatement.executeUpdate();
		deleteStatement.close();
	}

	// deletes services from a certain event
	public void deleteEventServices(int event_id) throws SQLException {
		String delStudSql = "DELETE FROM services_wanted WHERE event_id = ?";
		PreparedStatement deleteStatement = con.prepareStatement(delStudSql);
		deleteStatement.setInt(1, event_id);
		deleteStatement.executeUpdate();
		deleteStatement.close();
	}

	// check the event in the database if it has any services
	public boolean checkifEventHaveServices(int event_id) throws SQLException {
		String checkSql = "SELECT COUNT(*) AS count FROM services_wanted WHERE event_id = ?";
		PreparedStatement checkStmt = con.prepareStatement(checkSql);
		checkStmt.setInt(1, event_id);
		ResultSet checkResult = checkStmt.executeQuery();
		checkResult.next();
		int count = checkResult.getInt(1);
		if (count >= 1) {
			return true;
		} else {
			return false;
		}
	}

	public ArrayList<Booking> checkifDateHasEvent(String date, String status, String statusu) throws SQLException {
		ArrayList<Booking> list = new ArrayList<Booking>();
		String sql = "SELECT DISTINCT e.event_id , "
				+ "e.client_id, e.event_title , "
				+ "e.event_venue, e.event_details, "
				+ "e.event_status, e.event_status2, "
				+ "e.event_type, e.event_date, "
				+ "e.event_time, e.event_guest, e.event_paymentStatus ,"
				+ "CASE WHEN (SELECT DISTINCT " + "s.event_id as eventss "
				+ "FROM services_wanted AS s "
				+ "WHERE e.event_id = s.event_id) " + "IS NULL THEN 'false' "
				+ "ELSE 'true' END " + "AS HasServices " + "FROM events AS e "
				+ "WHERE e.event_date = \"" + date +"\" AND e.event_status = '"+status+"' "
				+ "AND e.event_status2 = '"+statusu+"'";
		
		Statement checkStmt = null;
		ResultSet res = null;
		try {
			checkStmt = con.createStatement();
			res = checkStmt.executeQuery(sql);
			while(res.next()) {
				int id = res.getInt("event_id");
				int client_id = res.getInt("client_id");
				String eventName = res.getString("event_title");
				String eventVenue = res.getString("event_venue");
				String eventDetails = res.getString("event_details");
				String eventType = res.getString("event_type");
				String eventDate = res.getString("event_date");
				String eventTime = res.getString("event_time");
				int eventGuest = res.getInt("event_guest");
				boolean hsv = res.getBoolean("HasServices");
				String eventStatus = res.getString("event_status");
				String eventStatus2 = res.getString("event_status2");
				String eventPaymentStatus = res.getString("event_paymentStatus");
				Booking book = new Booking(id, client_id, eventName,
						eventVenue, eventDetails, eventGuest, eventDate,
						eventTime, eventType);
				book.setHasServices(hsv);
				book.setEventStatus(eventStatus);
				book.setEventStatus2(eventStatus2);
				book.setEventPaymentStatus(eventPaymentStatus);
				list.add(book);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			checkStmt.close();
			res.close();
		}
		return list;
	}

	public ArrayList<Booking> checkifDateHasEventForBookingList(String date) throws SQLException {
		ArrayList<Booking> list = new ArrayList<Booking>();
		String sql = "SELECT DISTINCT e.event_id , "
				+ "e.client_id, e.event_title , "
				+ "e.event_venue, e.event_details, "
				+ "e.event_status, e.event_status2, "
				+ "e.event_type, e.event_date, "
				+ "e.event_time, e.event_guest, e.event_paymentStatus ,"
				+ "CASE WHEN (SELECT DISTINCT " + "s.event_id as eventss "
				+ "FROM services_wanted AS s "
				+ "WHERE e.event_id = s.event_id) " + "IS NULL THEN 'false' "
				+ "ELSE 'true' END " + "AS HasServices " + "FROM events AS e "
				+ "WHERE e.event_date = \"" + date +"\"";
		
		Statement checkStmt = null;
		ResultSet res = null;
		try {
			checkStmt = con.createStatement();
			res = checkStmt.executeQuery(sql);
			while(res.next()) {
				int id = res.getInt("event_id");
				int client_id = res.getInt("client_id");
				String eventName = res.getString("event_title");
				String eventVenue = res.getString("event_venue");
				String eventDetails = res.getString("event_details");
				String eventType = res.getString("event_type");
				String eventDate = res.getString("event_date");
				String eventTime = res.getString("event_time");
				int eventGuest = res.getInt("event_guest");
				boolean hsv = res.getBoolean("HasServices");
				String eventStatus = res.getString("event_status");
				String eventStatus2 = res.getString("event_status2");
				String eventPaymentStatus = res.getString("event_paymentStatus");
				Booking book = new Booking(id, client_id, eventName,
						eventVenue, eventDetails, eventGuest, eventDate,
						eventTime, eventType);
				book.setHasServices(hsv);
				book.setEventStatus(eventStatus);
				book.setEventStatus2(eventStatus2);
				book.setEventPaymentStatus(eventPaymentStatus);
				list.add(book);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			checkStmt.close();
			res.close();
		}
		return list;
	}

	public int getClientCount() throws SQLException {
		String checkSql = "SELECT COUNT(*)  AS counts FROM clients;";
		PreparedStatement checkStmt = con.prepareStatement(checkSql);
		ResultSet checkResult = checkStmt.executeQuery();
		checkResult.next();
		int ss = checkResult.getInt("counts");
		return ss;
	}

	public int getEventsWithServicesAndPaymentsCount() {
		try {
			String checkSql = "SELECT DISTINCT COUNT(*) as counts FROM clients c , events e, payments p "
					+ "WHERE c.client_id = e.client_id AND e.event_id = p.event_id "
					+ "AND p.client_id = c.client_id AND e.event_status2 = 'Ongoing';";
			PreparedStatement checkStmt = con.prepareStatement(checkSql);
			ResultSet checkResult = checkStmt.executeQuery();
			checkResult.next();
			int ss = checkResult.getInt("counts");
			return ss;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int getEventsThatisEditedCount() throws SQLException {
		String checkSql = "SELECT DISTINCT COUNT(*) as counts FROM events e WHERE isEdited = \"true\"";
		PreparedStatement checkStmt = con.prepareStatement(checkSql);
		ResultSet checkResult = checkStmt.executeQuery();
		checkResult.next();
		int ss = checkResult.getInt("counts");
		return ss;
	}

	// check if an event has payment or not
	public boolean checkIfServicesHasPayment(int client_ids, int event_ids)
			throws SQLException {
		String checkSql = "SELECT CASE WHEN (SELECT p.payment_paid AS payment "
				+ "FROM payments AS p "
				+ "WHERE e.event_id = p.event_id ) "
				+ "IS NULL THEN 'false' ELSE 'true' "
				+ "END AS HasPayment FROM clients AS c , "
				+ "events AS e WHERE c.client_id = ? AND e.event_id = ? LIMIT 1";
		PreparedStatement checkStmt = con.prepareStatement(checkSql);
		checkStmt.setInt(1, client_ids);
		checkStmt.setInt(2, event_ids);
		ResultSet checkResult = checkStmt.executeQuery();
		checkResult.next();
		boolean ss = checkResult.getBoolean("HasPayment");
		return ss;
	}
}
