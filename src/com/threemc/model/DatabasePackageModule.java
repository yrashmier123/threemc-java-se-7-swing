package com.threemc.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.threemc.data.Package;
import com.threemc.data.Service;
import com.threemc.data.ServiceList;

public class DatabasePackageModule {

	public ArrayList<Package> dbPackageOnly;
	public ArrayList<ServiceList> dbServiceList;
	public ArrayList<Service> dbServices;
	private DatabaseBookingDetails dbBooking;
	private Connection con;

	public DatabasePackageModule() {
		dbPackageOnly = new ArrayList<Package>();
		dbServiceList = new ArrayList<ServiceList>();
		dbServices = new ArrayList<Service>();
		dbBooking = new DatabaseBookingDetails();
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

	//Add
	public void addPackage(Package pack) {
		dbPackageOnly.clear();
		dbPackageOnly.add(pack);
	}

	public void addServiceList(ServiceList serv) {
		dbServiceList.clear();
		dbServiceList.add(serv);
	}

	public void addServiceLists(ArrayList<ServiceList> serv) {
		dbServiceList = serv;
	}

	//Save
	public void savePackages() throws SQLException {
		String checkSql = null;
		String insertSql = null;
		String updateSql = null;
		PreparedStatement checkStmt = null;
		PreparedStatement insertStmt = null;
		PreparedStatement updateStmt = null;

		try {
			checkSql = "SELECT COUNT(*) AS count FROM packages WHERE package_id = ?";
			checkStmt = con.prepareStatement(checkSql);

			insertSql = "INSERT INTO packages (package_name , package_desc, package_cost) VALUES (?, ?, ?)";
			insertStmt = con.prepareStatement(insertSql);

			updateSql = "UPDATE packages SET " + "package_name = ? , "
					+ "package_desc = ? , " + "package_cost = ? WHERE "
					+ "package_id  = ? ";
			updateStmt = con.prepareStatement(updateSql);

			for (Package pack : dbPackageOnly) {
				int id = pack.getId();
				String pname = pack.getPackageName();
				String pdesc = pack.getPackageDesc();
				int pcost = pack.getPackageCost();
				ArrayList<Service> pserv = pack.getPackageServices();

				checkStmt.setInt(1, id);
				ResultSet checkResult = checkStmt.executeQuery();
				checkResult.next();

				int count = checkResult.getInt(1);

				if (count == 1) {

					deleteService(id);
					saveServices(pserv, pack);

					int col = 1;
					updateStmt.setString(col++, pname);
					updateStmt.setString(col++, pdesc);
					updateStmt.setInt(col++, pcost);
					updateStmt.setInt(col++, id);

					updateStmt.executeUpdate();

				} else {

					int col = 1;
					insertStmt.setString(col++, pname);
					insertStmt.setString(col++, pdesc);
					insertStmt.setInt(col++, pcost);

					insertStmt.executeUpdate();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			insertStmt.close();
			updateStmt.close();
			checkStmt.close();
		}
	}

	public void saveServiceList() throws SQLException {
		String checkSql = null;
		String insertSql = null;
		String updateSql = null;
		PreparedStatement checkStmt = null;
		PreparedStatement insertStmt = null;
		PreparedStatement updateStmt = null;

		try {
			checkSql = "SELECT COUNT(*) AS count FROM services_list WHERE sl_id = ?";
			checkStmt = con.prepareStatement(checkSql);

			insertSql = "INSERT INTO services_list (service_category_id ,service_name , "
					+ "service_desc, service_cost) VALUES (?, ?, ?, ?)";
			insertStmt = con.prepareStatement(insertSql);

			updateSql = "UPDATE services_list SET service_category_id = ? ,  service_name = ? , "
					+ "service_desc = ? , " + "service_cost = ? WHERE "
					+ "sl_id  = ? ";
			updateStmt = con.prepareStatement(updateSql);

			for (ServiceList serv : dbServiceList) {

				int id = serv.getId();
				int scatid = serv.getService_catId();
				String sname = serv.getServiceName();
				String sdesc = serv.getServiceRemarks();
				int scost = serv.getServiceCost();

				checkStmt.setInt(1, id);

				ResultSet checkResult = checkStmt.executeQuery();
				checkResult.next();

				int count = checkResult.getInt(1);

				if (count == 1) {
					int col = 1;
					updateStmt.setInt(col++, scatid);
					updateStmt.setString(col++, sname);
					updateStmt.setString(col++, sdesc);
					updateStmt.setInt(col++, scost);
					updateStmt.setInt(col++, id);

					updateStmt.executeUpdate();

				} else {
					int col = 1;
					insertStmt.setInt(col++, scatid);
					insertStmt.setString(col++, sname);
					insertStmt.setString(col++, sdesc);
					insertStmt.setInt(col++, scost);

					insertStmt.executeUpdate();
				}
			}
		} catch (SQLException e) {
			throw new SQLException();
		} finally {
			insertStmt.close();
			updateStmt.close();
			checkStmt.close();
		}
	}

	public void saveServices(ArrayList<Service> servs, Package pack)
			throws SQLException {
		String checkSql = "SELECT COUNT(*) AS count FROM services WHERE service_id = ?";
		PreparedStatement checkStmt = con.prepareStatement(checkSql);

		String insertSql = "INSERT INTO "
				+ "services (package_id, service_category_id ,service_name , "
				+ "service_desc, service_cost) VALUES (?, ?, ?, ?, ?)";
		PreparedStatement insertStmt = con.prepareStatement(insertSql);

		String updateSql = "UPDATE services SET "
				+ "package_id = ? ,service_category_id = ? , service_name = ? , "
				+ "service_desc = ? , " + "service_cost = ? WHERE "
				+ "service_id  = ? ";
		PreparedStatement updateStmt = con.prepareStatement(updateSql);

		for (Service serv : servs) {
			int id = serv.getId();
			int package_id = pack.getId();
			String sname = serv.getServiceName();
			String sdesc = serv.getServiceRemarks();
			int scost = serv.getServiceCost();
			int scid = serv.getScId();

			checkStmt.setInt(1, id);

			ResultSet checkResult = checkStmt.executeQuery();
			checkResult.next();

			int count = checkResult.getInt(1);

			if (count >= 1) {
				int col = 1;
				updateStmt.setInt(col++, package_id);
				updateStmt.setInt(col++, scid);
				updateStmt.setString(col++, sname);
				updateStmt.setString(col++, sdesc);
				updateStmt.setInt(col++, scost);
				updateStmt.setInt(col++, id);

				updateStmt.executeUpdate();

			} else {
				int col = 1;
				insertStmt.setInt(col++, package_id);
				insertStmt.setInt(col++, scid);
				insertStmt.setString(col++, sname);
				insertStmt.setString(col++, sdesc);
				insertStmt.setInt(col++, scost);

				insertStmt.executeUpdate();
			}
		}
		insertStmt.close();
		updateStmt.close();
		checkStmt.close();
	}

	public void updateService(Service serv) throws SQLException {
		String updateSql = "UPDATE services SET "
				+ "package_id = ? ,service_category_id = ? , service_name = ? , "
				+ "service_desc = ? , " + "service_cost = ? WHERE "
				+ "service_id  = ? ";
		PreparedStatement updateStmt = con.prepareStatement(updateSql);
		int id = serv.getId();
		int package_id = serv.getPkId();
		String sname = serv.getServiceName();
		String sdesc = serv.getServiceRemarks();
		int scost = serv.getServiceCost();
		int scid = serv.getScId();
		int col = 1;
		updateStmt.setInt(col++, package_id);
		updateStmt.setInt(col++, scid);
		updateStmt.setString(col++, sname);
		updateStmt.setString(col++, sdesc);
		updateStmt.setInt(col++, scost);
		updateStmt.setInt(col++, id);
		updateStmt.executeUpdate();
		updateStmt.close();
	}

	public void updateServiceList() throws SQLException {
		String updateSql = null;
		PreparedStatement updateStmt = null;

		try {
			updateSql = "UPDATE services_list SET service_category_id = ? ,  service_name = ? , "
					+ "service_desc = ? , service_cost = ? WHERE sl_id  = ? ";
			updateStmt = con.prepareStatement(updateSql);

			for (ServiceList serv : dbServiceList) {
				int id = serv.getId();
				int scatid = serv.getService_catId();
				String sname = serv.getServiceName();
				String sdesc = serv.getServiceRemarks();
				int scost = serv.getServiceCost();

				int col = 1;
				updateStmt.setInt(col++, scatid);
				updateStmt.setString(col++, sname);
				updateStmt.setString(col++, sdesc);
				updateStmt.setInt(col++, scost);
				updateStmt.setInt(col++, id);
				updateStmt.executeUpdate();
			}
		} catch (SQLException e) {
			throw new SQLException();
		} finally {
			updateStmt.close();
		}
	}

	public void loadPackages() throws SQLException {
		try {
			dbBooking.connect();
			dbBooking.loadPackageRecords();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadAllPackageRecordsWithServices() throws SQLException {
		dbPackageOnly.clear();
		String sql = "SELECT DISTINCT p.package_id, p.package_name, p.package_desc, p.package_cost, CASE WHEN (SELECT DISTINCT s.package_id FROM services s WHERE s.package_id = p.package_id) IS NULL THEN 'False' ELSE 'True' END as HasServices FROM packages p ORDER BY p.package_name ASC";
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
				String pserv = res.getString("HasServices");
				ArrayList<Service> pservice = loadServicesRerordsByPackage(id);

				if(pserv.equals("True")) {
					Package pack = new Package(id, pname, pdesc, pcost, pservice);
					pack.setHasEvent(pserv);
					dbPackageOnly.add(pack);
				}
			}
		} catch (SQLException sqle) {
			throw new SQLException();
		} finally {
			loadStatement.close();
			res.close();
		}
	}

	public ArrayList<Package> getPackagesWithServices() {
		return dbPackageOnly;
	}

	public void searchPackages(String value) throws SQLException {
		dbPackageOnly.clear();
		String val = value.replace("'", "");
		String sql = "SELECT * FROM packages WHERE package_name LIKE '%" + val + "%'";
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

				Package pack = new Package(id, pname, pdesc, pcost, pservice);
				dbPackageOnly.add(pack);
			}
		} catch (SQLException sqle) {
			throw new SQLException();
		} finally {
			loadStatement.close();
			res.close();
		}
	}

	public Package getLastPackage() throws SQLException {
		dbPackageOnly.clear();
		String sql = "SELECT * FROM packages ORDER BY package_id DESC LIMIT 1";
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

				Package pack = new Package(id, pname, pdesc, pcost, pservice);
				dbPackageOnly.add(pack);
			}
			return dbPackageOnly.get(0);
		} catch (SQLException sqle) {
			throw new SQLException();
		} finally {
			loadStatement.close();
			res.close();
		}
	}

	public ArrayList<Service> loadServicesRerordsByPackage(int pack_id)
			throws SQLException {
		ArrayList<Service> pservice = new ArrayList<Service>();
		String sql = "SELECT s.service_id, s.package_id, s.service_category_id, "
				+ "s.service_name, ( SELECT c.service_category_name "
				+ "FROM service_category c WHERE s.service_category_id = c.service_category_id) "
				+ "AS service_cat, s.service_desc, s.service_cost "
				+ "FROM services s WHERE s.package_id = " + pack_id;
		Statement loadStatement = null;
		ResultSet res = null;
		try {
			loadStatement = con.createStatement();
			res = loadStatement.executeQuery(sql);
			while (res.next()) {
				int id = res.getInt("service_id");
				int pkId = res.getInt("package_id");
				String sname = res.getString("service_name");
				String sdesc = res.getString("service_desc");
				int scost = res.getInt("service_cost");
				int scid = res.getInt("service_category_id");
				String scat = res.getString("service_cat");

				Service serv = new Service(id, pkId, sname, scost, sdesc, scat);
				serv.setScId(scid);
				pservice.add(serv);
			}
			return pservice;
		} catch (SQLException sqle) {
			throw new SQLException();
		} finally {
			loadStatement.close();
			res.close();
		}
	}

	public ArrayList<Package> getPackages() {
		dbPackageOnly = dbBooking.getPackage();
		return dbPackageOnly;
	}

	public void loadServicesList() throws SQLException {
		try {
			dbBooking.connect();
			dbBooking.loadAllServicesFromServiceList();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<ServiceList> getServicesList() {
		dbServiceList = dbBooking.getServiceList();
		return dbServiceList;
	}

	public void deletePackages(int package_id) throws SQLException {
		deleteService(package_id);
		String delStudSql = "DELETE FROM packages WHERE package_id = ?";
		PreparedStatement deleteStatement = con.prepareStatement(delStudSql);
		deleteStatement.setInt(1, package_id);
		deleteStatement.executeUpdate();
		deleteStatement.close();
	}

	public void deleteService(int package_id) throws SQLException {
		String delServSql = "DELETE FROM services WHERE package_id = ?";
		PreparedStatement deleteStatement = con.prepareStatement(delServSql);
		deleteStatement.setInt(1, package_id);
		deleteStatement.executeUpdate();
		deleteStatement.close();
	}

	public void deleteServiceList(int sl_id) throws SQLException {
		String delServListSql = "DELETE FROM services_list WHERE sl_id = ?";
		PreparedStatement deleteStatement = con
				.prepareStatement(delServListSql);
		deleteStatement.setInt(1, sl_id);
		deleteStatement.executeUpdate();
		deleteStatement.close();
	}
}
