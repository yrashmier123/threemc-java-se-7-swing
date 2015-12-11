package com.threemc.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CheckAndCreateDatabase {

	// Database name and URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/";
	static final String DB_URL2 = "jdbc:mysql://localhost/threemcqueens";
	static final String JDBCDriver = "com.mysql.jdbc.Driver";

	// Database credentials
	static final String USER = "root";
	static final String PASS = "";

	private static Connection conn;
	private static Statement stmt;

	private static DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss MM/dd/yyyy");

	public CheckAndCreateDatabase() {

	}
	
	// TODO

	public static String checkDatabaseifExisting() {
		if (checkDBExists("threemcqueens")) {
			return "Already have a database";
		} else {
			createDatabase();
			creataTables();
			insertIntoAdmin();
			insertIntoServiceCategory();
			insertIntoPosition();
			return "Done creating a database";
		}
	}

	private static void creataTables() {
		createDatabaseTableForAdmin();
		createDatabaseTableForClient();
		createDatabaseTableForEvents();
		createDatabaseTableForPayments();
		createDatabaseTableForPackages();
		createDatabaseTableForServices();
		createDatabaseTableForUsers();
		createDatabaseTableForLogs();
		createDatabaseTableForServicesWanted();
		createDatabaseTableForPaymenHistory();
		createDatabaseTableForServicesList();
		createDatabaseTableForServicesCategory();
		createDatabaseTableForPositions();
		createDatabaseTableForEmployees();
		createDatabaseTableForOutputs();
		createDatabaseTableForOutputsUpdates();
		createDatabaseTableForNotices();
	}

	public static boolean checkDBExists(String dbName) {
		try {
			Class.forName(JDBCDriver); // Register JDBC Driver
			System.out.println("Creating a connection...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS); // Open a
																	// connection
			ResultSet resultSet = conn.getMetaData().getCatalogs();
			while (resultSet.next()) {
				String databaseName = resultSet.getString(1);
				if (databaseName.equals(dbName)) {
					return true;
				}
			}
			resultSet.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private static void createDatabase() {
		conn = null;
		stmt = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName(JDBCDriver);

			// STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// STEP 4: Execute a query
			System.out.println("Creating database...");
			stmt = conn.createStatement();

			String sql = "CREATE DATABASE threemcqueens";
			stmt.executeUpdate(sql);
			System.out.println("Database created successfully...");
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}// nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}// end finally try
		}// end try
		System.out.println("database threemcqueens successfully created!");
	}

	private static void createDatabaseTableForAdmin() {
		conn = null;
		stmt = null;
		try {
			Class.forName(JDBCDriver);
			conn = DriverManager.getConnection(DB_URL2, USER, PASS);
			System.out
					.println("Creating Clients Table table in given database...");
			stmt = conn.createStatement();

			String sql = "CREATE TABLE `admin` ( "
					+ "`admin_id` int(10) NOT NULL AUTO_INCREMENT, "
					+ "`admin_user` varchar(100) NOT NULL, "
					+ "`admin_pass` varchar(100) NOT NULL, "
					+ "`admin_lastLogIn` varchar(255) NOT NULL, "
					+ "`admin_status` varchar(255) NOT NULL, "
					+ "PRIMARY KEY (`admin_id`)) ENGINE=InnoDB AUTO_INCREMENT=1";
			stmt.executeUpdate(sql);
			System.out.println("Created Administrator table in database...");
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

	private static void createDatabaseTableForClient() {
		conn = null;
		stmt = null;
		try {
			Class.forName(JDBCDriver);

			conn = DriverManager.getConnection(DB_URL2, USER, PASS);
			System.out
					.println("Creating Clients Table table in given database...");
			stmt = conn.createStatement();

			String sql = "CREATE TABLE IF NOT EXISTS `clients` ( "
					+ " `client_id` int(20) NOT NULL AUTO_INCREMENT, "
					+ " `client_firstName` varchar(255) NOT NULL, "
					+ " `client_middleName` varchar(255) NOT NULL, "
					+ " `client_lastName` varchar(255) NOT NULL, "
					+ " `client_address` varchar(255) NOT NULL, "
					+ " `client_gender` enum('male','female') NOT NULL, "
					+ " `client_contactNo` varchar(255) NOT NULL, "
					+ " PRIMARY KEY (`client_id`) "
					+ ") ENGINE=InnoDB AUTO_INCREMENT=10001 ";

			stmt.executeUpdate(sql);
			System.out.println("Created Clients table in given database...");
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

	private static void createDatabaseTableForEvents() {
		conn = null;
		stmt = null;
		try {
			Class.forName(JDBCDriver);

			conn = DriverManager.getConnection(DB_URL2, USER, PASS);
			System.out.println("Creating Events table in given database...");
			stmt = conn.createStatement();

			String sql = "CREATE TABLE IF NOT EXISTS `events` ( "
					+ " `event_id` int(20) NOT NULL AUTO_INCREMENT, "
					+ " `client_id` int(20) NOT NULL, "
					+ " `event_title` varchar(255) NOT NULL, "
					+ " `event_venue` varchar(255) NOT NULL, "
					+ " `event_details` varchar(255) NOT NULL, "
					+ " `event_type` varchar(255) NOT NULL, "
					+ " `event_date` varchar(255) NOT NULL, "
					+ " `event_time` varchar(10) NOT NULL, "
					+ " `event_guest` int(20) NOT NULL, "
					+ " PRIMARY KEY (`event_id`), "
					+ " KEY `client_id` (`client_id`), "
					+ " CONSTRAINT `events_ibfk_1` FOREIGN KEY (`client_id`) "
					+ "REFERENCES `clients` (`client_id`) "
					+ "ON DELETE NO ACTION ON UPDATE CASCADE "
					+ ") ENGINE=InnoDB AUTO_INCREMENT=10001";
			stmt.executeUpdate(sql);
			System.out.println("Created Events table in given database...");
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

	private static void createDatabaseTableForPayments() {
		conn = null;
		stmt = null;
		try {
			Class.forName(JDBCDriver);

			conn = DriverManager.getConnection(DB_URL2, USER, PASS);
			System.out.println("Creating Payments table in given database...");
			stmt = conn.createStatement();

			String sql = "CREATE TABLE IF NOT EXISTS `payments` "
					+ "( `payment_id` int(20) NOT NULL AUTO_INCREMENT, "
					+ "`client_id` int(20) NOT NULL, `event_id` int(20) NOT NULL, "
					+ "`payment_total` int(30) NOT NULL, `payment_type` varchar(20) NOT NULL, "
					+ "`payment_paid` int(30) NOT NULL, `payment_balance` int(30) NOT NULL, "
					+ "`payment_datetime` varchar(255) NOT NULL, PRIMARY KEY (`payment_id`), "
					+ "KEY `client_id` (`client_id`), KEY `event_id` (`event_id`), "
					+ "CONSTRAINT `fkPaymentEid` FOREIGN KEY (`event_id`) "
					+ "REFERENCES `events` (`event_id`) ON DELETE NO ACTION ON UPDATE NO ACTION, "
					+ "CONSTRAINT `fkPaymentCid` FOREIGN KEY (`client_id`) "
					+ "REFERENCES `clients` (`client_id`) ON DELETE NO ACTION ON UPDATE NO ACTION) "
					+ "ENGINE=InnoDB AUTO_INCREMENT=10001";

			stmt.executeUpdate(sql);
			System.out.println("Created Payments table in given database...");
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

	private static void createDatabaseTableForPackages() {
		conn = null;
		stmt = null;
		try {
			Class.forName(JDBCDriver);

			conn = DriverManager.getConnection(DB_URL2, USER, PASS);
			System.out.println("Creating Packages table in given database...");
			stmt = conn.createStatement();

			String sql = "CREATE TABLE IF NOT EXISTS `packages` ( "
					+ " `package_id` int(20) NOT NULL AUTO_INCREMENT, "
					+ " `package_name` varchar(255) NOT NULL, "
					+ " `package_desc` varchar(255) NOT NULL, "
					+ " `package_cost` int(30) NOT NULL, "
					+ " PRIMARY KEY (`package_id`) "
					+ ") ENGINE=InnoDB AUTO_INCREMENT=10001";

			stmt.executeUpdate(sql);
			System.out.println("Created Packages table in given database...");
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

	private static void createDatabaseTableForServices() {
		conn = null;
		stmt = null;
		try {
			Class.forName(JDBCDriver);

			conn = DriverManager.getConnection(DB_URL2, USER, PASS);
			System.out.println("Creating Services table in given database...");
			stmt = conn.createStatement();

			String sql = "CREATE TABLE IF NOT EXISTS `services` ( `service_id` int(20) NOT NULL AUTO_INCREMENT, "
					+ "`package_id` int(20) NOT NULL, `service_category_id` int(10) NOT NULL, "
					+ "`service_name` varchar(255) NOT NULL, `service_desc` varchar(255) NOT NULL, "
					+ "`service_cost` int(30) NOT NULL, PRIMARY KEY (`service_id`), "
					+ "KEY `package_id` (`package_id`), KEY `service_category_id` (`service_category_id`),"
					+ "CONSTRAINT `servicesPK1` FOREIGN KEY (`package_id`) "
					+ "REFERENCES `packages` (`package_id`) ON DELETE NO ACTION ON UPDATE NO ACTION, "
					+ "CONSTRAINT `servicesSC2` FOREIGN KEY (`service_category_id`) "
					+ "REFERENCES `service_category` (`service_category_id`) ON DELETE NO ACTION ON UPDATE NO ACTION) "
					+ "ENGINE=InnoDB AUTO_INCREMENT=10001";

			stmt.executeUpdate(sql);
			System.out.println("Created Services table in given database...");
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

	private static void createDatabaseTableForUsers() {
		conn = null;
		stmt = null;
		try {
			Class.forName(JDBCDriver);

			conn = DriverManager.getConnection(DB_URL2, USER, PASS);
			System.out.println("Creating Users table in given database...");
			stmt = conn.createStatement();

			String sql = "CREATE TABLE IF NOT EXISTS `users` ( `user_id` int(20) NOT NULL AUTO_INCREMENT, "
					+ "`employee_id` int(10) NOT NULL, `user_name` varchar(255) NOT NULL, "
					+ "`user_password` varchar(255) NOT NULL, `user_lastLogIn` datetime NOT NULL, "
					+ "`user_type` varchar(255) NOT NULL,`user_logged` varchar(255) NOT NULL DEFAULT 'false',"
					+ " PRIMARY KEY (`user_id`), KEY `employee_id` (`employee_id`), CONSTRAINT `fkUserEmpid` "
					+ "FOREIGN KEY (`employee_id`) REFERENCES `employees` (`employee_id`) "
					+ "ON DELETE NO ACTION ON UPDATE NO ACTION) ENGINE=InnoDB AUTO_INCREMENT=10001";

			stmt.executeUpdate(sql);
			System.out.println("Created Users table in given database...");
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

	private static void createDatabaseTableForLogs() {
		conn = null;
		stmt = null;
		try {
			Class.forName(JDBCDriver);

			conn = DriverManager.getConnection(DB_URL2, USER, PASS);
			System.out.println("Creating Logs table in given database...");
			stmt = conn.createStatement();

			String sql = "CREATE TABLE IF NOT EXISTS `logs` ( "
					+ " `log_id` int(20) NOT NULL AUTO_INCREMENT, "
					+ " `user_id` int(20) NOT NULL, "
					+ " `log_title` varchar(255) NOT NULL, "
					+ " `log_desc` varchar(255) NOT NULL, "
					+ " `log_datetime` varchar(255) NOT NULL, "
					+ " PRIMARY KEY (`log_id`), "
					+ " KEY `user_id` (`user_id`) "
					+ ") ENGINE=InnoDB AUTO_INCREMENT=10001";

			stmt.executeUpdate(sql);
			System.out.println("Created Logs table in given database...");
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

	private static void createDatabaseTableForServicesWanted() {
		conn = null;
		stmt = null;
		try {
			Class.forName(JDBCDriver);

			conn = DriverManager.getConnection(DB_URL2, USER, PASS);
			System.out.println("Creating Logs table in given database...");
			stmt = conn.createStatement();

			String sql = "CREATE TABLE IF NOT EXISTS `services_wanted` ( "
					+ "`sw_id` int(20) NOT NULL AUTO_INCREMENT, `event_id` int(20) NOT NULL, "
					+ "`client_id` int(20) NOT NULL, `service_category_id` int(10) NOT NULL, "
					+ "`package_name` varchar(255) NOT NULL, `service_name` varchar(255) NOT NULL, "
					+ "`service_desc` varchar(255) NOT NULL, `service_cost` int(30) NOT NULL, "
					+ "PRIMARY KEY (`sw_id`), KEY `fkEvent_id` (`event_id`), KEY `fkClient_id` (`client_id`), "
					+ "KEY `service_category_id` (`service_category_id`), "
					+ "CONSTRAINT `fkClient_id` FOREIGN KEY (`client_id`) REFERENCES `clients` (`client_id`), "
					+ "CONSTRAINT `fkEvent_id` FOREIGN KEY (`event_id`) REFERENCES `events` (`event_id`), "
					+ "CONSTRAINT `services_wanted_ibfk_1` FOREIGN KEY (`event_id`) "
					+ "REFERENCES `events` (`event_id`) ON DELETE NO ACTION ON UPDATE NO ACTION, "
					+ "CONSTRAINT `services_wanted_ibfk_2` FOREIGN KEY (`client_id`) "
					+ "REFERENCES `clients` (`client_id`) ON DELETE NO ACTION ON UPDATE NO ACTION, "
					+ "CONSTRAINT `services_wanted_ibfk_3` FOREIGN KEY (`service_category_id`) "
					+ "REFERENCES `service_category` (`service_category_id`) ON DELETE NO ACTION ON UPDATE NO ACTION) "
					+ "ENGINE=InnoDB AUTO_INCREMENT=10001";

			stmt.executeUpdate(sql);
			System.out.println("Created Services Wanted table in given database...");
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

	private static void createDatabaseTableForPaymenHistory() {
		conn = null;
		stmt = null;
		try {
			Class.forName(JDBCDriver);

			conn = DriverManager.getConnection(DB_URL2, USER, PASS);
			System.out.println("Creating Logs table in given database...");
			stmt = conn.createStatement();

			String sql = "CREATE TABLE IF NOT EXISTS `payment_history` ( "
					+ " `ph_id` int(30) NOT NULL AUTO_INCREMENT, "
					+ " `client_id` int(30) NOT NULL, "
					+ " `event_id` int(30) NOT NULL, "
					+ " `payment_id` int(30) NOT NULL, "
					+ " `payment_type` varchar(255) NOT NULL, "
					+ " `payment_paidThisdate` int(255) NOT NULL, "
					+ " `payment_date` varchar(255) NOT NULL, "
					+ " PRIMARY KEY (`ph_id`), "
					+ " KEY `fkPayshist_id` (`payment_id`), "
					+ " KEY `client_id` (`client_id`), "
					+ " KEY `event_id` (`event_id`), "
					+ " CONSTRAINT `fkCliehist_id` FOREIGN KEY (`client_id`) REFERENCES `clients` (`client_id`) ON DELETE NO ACTION ON UPDATE NO ACTION, "
					+ " CONSTRAINT `fkEveshist_id` FOREIGN KEY (`event_id`) REFERENCES `events` (`event_id`) ON DELETE NO ACTION ON UPDATE NO ACTION, "
					+ " CONSTRAINT `fkPayshist_id` FOREIGN KEY (`payment_id`) REFERENCES `payments` (`payment_id`) ON DELETE NO ACTION ON UPDATE NO ACTION "
					+ ") ENGINE=InnoDB AUTO_INCREMENT=10001";

			stmt.executeUpdate(sql);
			System.out.println("Created Payment History table in given database...");
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

	private static void createDatabaseTableForServicesList() {
		conn = null;
		stmt = null;
		try {
			Class.forName(JDBCDriver);
			conn = DriverManager.getConnection(DB_URL2, USER, PASS);
			System.out.println("Creating Logs table in given database...");
			stmt = conn.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS `services_list` ( "
					+ "`sl_id` int(30) NOT NULL AUTO_INCREMENT, `service_category_id` int(10) NOT NULL, "
					+ "`service_name` varchar(255) NOT NULL, `service_desc` varchar(2552) NOT NULL, "
					+ "`service_cost` int(255) NOT NULL, PRIMARY KEY (`sl_id`), "
					+ "KEY `service_category_id` (`service_category_id`), "
					+ "CONSTRAINT `serviceSC1` FOREIGN KEY (`service_category_id`) "
					+ "REFERENCES `service_category` (`service_category_id`) ON DELETE NO ACTION ON UPDATE NO ACTION) "
					+ "ENGINE=InnoDB AUTO_INCREMENT=10001 DEFAULT CHARSET=latin1";
			stmt.executeUpdate(sql);
			System.out.println("Created Services List table in given database...");
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}
	
	private static void createDatabaseTableForServicesCategory() {
		conn = null;
		stmt = null;
		try {
			Class.forName(JDBCDriver);
			conn = DriverManager.getConnection(DB_URL2, USER, PASS);
			System.out.println("Creating Logs table in given database...");
			stmt = conn.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS `service_category` ( `service_category_id` int(10) NOT NULL AUTO_INCREMENT, "
					+ "`service_category_name` varchar(500) NOT NULL, "
					+ "`service_category_status` varchar(255) NOT NULL DEFAULT 'Normal', "
					+ "PRIMARY KEY (`service_category_id`)) ENGINE=InnoDB AUTO_INCREMENT=10001";
			stmt.executeUpdate(sql);
			System.out.println("Created Services Category table in given database...");
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}
	
	private static void createDatabaseTableForPositions() {
		conn = null;
		stmt = null;
		try {
			Class.forName(JDBCDriver);
			conn = DriverManager.getConnection(DB_URL2, USER, PASS);
			System.out.println("Creating Logs table in given database...");
			stmt = conn.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS `positions` ( `position_id` int(10) NOT NULL AUTO_INCREMENT, "
					+ "`position_name` varchar(255) NOT NULL, `position_desc` varchar(255) NOT NULL, "
					+ "PRIMARY KEY (`position_id`)) ENGINE=InnoDB AUTO_INCREMENT=10001";
			stmt.executeUpdate(sql);
			System.out.println("Created Positions table in given database...");
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}
	
	private static void createDatabaseTableForEmployees() {
		conn = null;
		stmt = null;
		try {
			Class.forName(JDBCDriver);
			conn = DriverManager.getConnection(DB_URL2, USER, PASS);
			System.out.println("Creating Logs table in given database...");
			stmt = conn.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS `employees` ( `employee_id` int(10) NOT NULL AUTO_INCREMENT, "
					+ "`position_id` int(10) NOT NULL, `employee_firstName` varchar(255) NOT NULL, "
					+ "`employee_middleName` varchar(255) NOT NULL, `employee_lastName` varchar(255) NOT NULL, "
					+ "`employee_gender` enum('male','female') NOT NULL, `employee_address` varchar(255) NOT NULL, "
					+ "`employee_dateOfBirth` varchar(255) NOT NULL, `employee_dateOfEmployment` varchar(255) NOT NULL, "
					+ "`employee_contactno` varchar(255) NOT NULL, `employee_status` varchar(255) NOT NULL DEFAULT 'Active', "
					+ "PRIMARY KEY (`employee_id`), KEY `position_id` (`position_id`), "
					+ "CONSTRAINT `postRef` FOREIGN KEY (`position_id`) "
					+ "REFERENCES `positions` (`position_id`) ON DELETE NO ACTION ON UPDATE NO ACTION) "
					+ "ENGINE=InnoDB AUTO_INCREMENT=10001";
			stmt.executeUpdate(sql);
			System.out.println("Created Employees table in given database...");
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}
	
	private static void createDatabaseTableForOutputs() {
		conn = null;
		stmt = null;
		try {
			Class.forName(JDBCDriver);
			conn = DriverManager.getConnection(DB_URL2, USER, PASS);
			System.out.println("Creating Logs table in given database...");
			stmt = conn.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS `outputs` ( `output_id` int(10) NOT NULL AUTO_INCREMENT, "
					+ "`client_id` int(10) NOT NULL, `event_id` int(10) NOT NULL, "
					+ "`employee_id` int(10) NOT NULL, `output_name` varchar(255) NOT NULL, "
					+ "`output_desc` varchar(255) NOT NULL, `output_status` varchar(255) NOT NULL DEFAULT 'Pending', "
					+ "PRIMARY KEY (`output_id`), KEY `event_id` (`event_id`), KEY `employee_id` (`employee_id`), KEY `client_id` (`client_id`), "
					+ "CONSTRAINT `opfkcid` FOREIGN KEY (`client_id`) REFERENCES `clients` (`client_id`) "
					+ "ON DELETE NO ACTION ON UPDATE NO ACTION, CONSTRAINT `opfkeid` FOREIGN KEY (`event_id`) "
					+ "REFERENCES `events` (`event_id`) ON DELETE NO ACTION ON UPDATE NO ACTION, "
					+ "CONSTRAINT `opfkempid` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`employee_id`) "
					+ "ON DELETE NO ACTION ON UPDATE NO ACTION) ENGINE=InnoDB AUTO_INCREMENT=10001";
			stmt.executeUpdate(sql);
			System.out.println("Created Outputs table in given database...");
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}
	
	private static void createDatabaseTableForOutputsUpdates() {
		conn = null;
		stmt = null;
		try {
			Class.forName(JDBCDriver);
			conn = DriverManager.getConnection(DB_URL2, USER, PASS);
			System.out.println("Creating Logs table in given database...");
			stmt = conn.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS `outputs_updates` ( `ou_id` int(10) NOT NULL AUTO_INCREMENT, "
					+ "`output_id` int(10) NOT NULL, `employee_id` int(10) NOT NULL, "
					+ "`ou_desc` varchar(255) NOT NULL, `ou_date` varchar(255) NOT NULL, "
					+ "`ou_status` varchar(255) NOT NULL  DEFAULT 'new', PRIMARY KEY (`ou_id`), "
					+ "KEY `output_id` (`output_id`), KEY `employee_id` (`employee_id`), "
					+ "CONSTRAINT `fkOuputid` FOREIGN KEY (`output_id`) "
					+ "REFERENCES `outputs` (`output_id`) ON DELETE NO ACTION ON UPDATE NO ACTION, "
					+ "CONSTRAINT `fkOutputempid` FOREIGN KEY (`employee_id`) "
					+ "REFERENCES `employees` (`employee_id`) ON DELETE NO ACTION ON UPDATE NO ACTION) "
					+ "ENGINE=InnoDB AUTO_INCREMENT=10001";
			stmt.executeUpdate(sql);
			System.out.println("Created Output updates table in given database...");
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}
	
	private static void createDatabaseTableForNotices() {
		conn = null;
		stmt = null;
		try {
			String date = dateFormat.format(new Date(System.currentTimeMillis()));
			Class.forName(JDBCDriver);
			conn = DriverManager.getConnection(DB_URL2, USER, PASS);
			System.out.println("Creating Logs table in given database...");
			stmt = conn.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS `notices` "
					+ "( `notice_id` int(10) NOT NULL AUTO_INCREMENT, "
					+ "`notice_date` varchar(255) NOT NULL, "
					+ "`notice_desc` varchar(3000) NOT NULL, "
					+ "PRIMARY KEY (`notice_id`)) "
					+ "ENGINE=InnoDB ;";
			stmt.executeUpdate(sql);
			System.out.println("Created Notices table in given database...");
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}
	
	private static void insertIntoAdmin() {
		conn = null;
		stmt = null;
		try {
			Class.forName(JDBCDriver);
			conn = DriverManager.getConnection(DB_URL2, USER, PASS);
			System.out.println("Inserting default Account to Admin Table");
			stmt = conn.createStatement();

			String sql = "INSERT INTO `admin`(`admin_user`, `admin_pass`) "
					+ "VALUES (\"admin\",123456)";
			stmt.executeUpdate(sql);
			System.out.println("Sucessfully Inserted.");
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}
	
	private static void insertIntoServiceCategory() {
		conn = null;
		stmt = null;
		try {
			Class.forName(JDBCDriver);
			conn = DriverManager.getConnection(DB_URL2, USER, PASS);
			System.out.println("Inserting default Account to Admin Table");
			stmt = conn.createStatement();

			String sql = "INSERT INTO `service_category`"
					+ "(`service_category_id`, `service_category_name`) "
					+ "VALUES (10000,\"Uncategorized\")";
			stmt.executeUpdate(sql);
			System.out.println("Sucessfully Inserted.");
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}
	
	private static void insertIntoPosition() {
		conn = null;
		stmt = null;
		try {
			Class.forName(JDBCDriver);
			conn = DriverManager.getConnection(DB_URL2, USER, PASS);
			System.out.println("Inserting default Account to position Table");
			stmt = conn.createStatement();

			String sql = "INSERT INTO `positions`"
					+ "(`position_id`, `position_name`, `position_desc`) "
					+ "VALUES (10000,\"No Position\",\"No Position\")";
			stmt.executeUpdate(sql);
			System.out.println("Sucessfully Inserted.");
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}
	
//
//	private static void createAutoIncrementIDForTables(String tableName) {
//		conn = null;
//		stmt = null;
//		try {
//			Class.forName(JDBCDriver);
//
//			conn = DriverManager.getConnection(DB_URL2, USER, PASS);
//			System.out.println("Creating Auto Increment id in " + tableName
//					+ " table in given database...");
//			stmt = conn.createStatement();
//
//			String sql = "ALTER TABLE `" + tableName
//					+ "` AUTO_INCREMENT = 10001";
//
//			stmt.executeUpdate(sql);
//			System.out.println("Created Auto Increment id in " + tableName
//					+ " table in given database...");
//		} catch (SQLException se) {
//			se.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (stmt != null)
//					conn.close();
//			} catch (SQLException se) {
//			}
//			try {
//				if (conn != null)
//					conn.close();
//			} catch (SQLException se) {
//				se.printStackTrace();
//			}
//		}
//	}

//	private static void createRelations(String tableName, String constraint,
//			String key, String refTableName) {
//		conn = null;
//		stmt = null;
//		try {
//			Class.forName(JDBCDriver);
//
//			conn = DriverManager.getConnection(DB_URL2, USER, PASS);
//			System.out.println("Creating Logs table in given database...");
//			stmt = conn.createStatement();
//
//			String sql = "ALTER TABLE `" + tableName + "` "
//					+ "ADD CONSTRAINT `" + constraint + "` "
//					+ "FOREIGN KEY ( `" + key + "` ) "
//					+ "REFERENCES `threemcqueens`.`" + refTableName + "`(`"
//					+ key + "`) " + "ON DELETE NO ACTION ON UPDATE CASCADE;";
//
//			stmt.executeUpdate(sql);
//			System.out.println("Created Logs table in given database...");
//		} catch (SQLException se) {
//			se.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (stmt != null)
//					conn.close();
//			} catch (SQLException se) {
//			}
//			try {
//				if (conn != null)
//					conn.close();
//			} catch (SQLException se) {
//				se.printStackTrace();
//			}
//		}
//	}
}

// check database before i change the methods sql with the show create table

/*
 * package com.threemc.model;
 * 
 * import java.sql.Connection; import java.sql.DriverManager; import
 * java.sql.ResultSet; import java.sql.SQLException; import java.sql.Statement;
 * 
 * public class CheckAndCreateDatabase {
 * 
 * // Database name and URL static final String JDBC_DRIVER =
 * "com.mysql.jdbc.Driver"; static final String DB_URL =
 * "jdbc:mysql://localhost/"; static final String DB_URL2 =
 * "jdbc:mysql://localhost/threemcqueens"; static final String JDBCDriver =
 * "com.mysql.jdbc.Driver";
 * 
 * // Database credentials static final String USER = "root"; static final
 * String PASS = "";
 * 
 * private static Connection conn; private static Statement stmt;
 * 
 * public CheckAndCreateDatabase() {
 * 
 * }
 * 
 * public static String checkDatabaseifExisting() { if
 * (checkDBExists("threemcqueens")) { return "Already have a database"; } else {
 * createDatabase(); createDatabaseTableForClient();
 * createDatabaseTableForEvents(); createDatabaseTableForPayments();
 * createDatabaseTableForPackages(); createDatabaseTableForServices();
 * createDatabaseTableForUsers(); createDatabaseTableForLogs();
 * createDatabaseTableForServicesWanted();
 * createDatabaseTableForPaymenHistory(); createDatabaseTableForServicesList();
 * 
 * createAutoIncrementIDForTables("clients");
 * createAutoIncrementIDForTables("events");
 * createAutoIncrementIDForTables("payments");
 * createAutoIncrementIDForTables("packages");
 * createAutoIncrementIDForTables("services");
 * createAutoIncrementIDForTables("users");
 * createAutoIncrementIDForTables("services_wanted");
 * createAutoIncrementIDForTables("logs");
 * 
 * createRelations("events", "eventsFK1", "client_id", "clients");
 * createRelations("payments", "paymentFK1", "client_id", "clients");
 * createRelations("payments", "paymentFK2", "event_id", "events");
 * createRelations("services", "servicesFK1", "package_id", "packages");
 * createRelations("services_wanted", "eventFKSW1", "event_id", "events");
 * createRelations("services_wanted", "eventFKSW2", "client_id", "clients"); }
 * return "Already have a database"; }
 * 
 * public static boolean checkDBExists(String dbName) { try {
 * Class.forName(JDBCDriver); // Register JDBC Driver
 * System.out.println("Creating a connection..."); conn =
 * DriverManager.getConnection(DB_URL, USER, PASS); // Open a // connection
 * ResultSet resultSet = conn.getMetaData().getCatalogs(); while
 * (resultSet.next()) { String databaseName = resultSet.getString(1); if
 * (databaseName.equals(dbName)) { return true; } } resultSet.close(); } catch
 * (Exception e) { e.printStackTrace(); } return false; }
 * 
 * private static void createDatabase() { conn = null; stmt = null; try { //
 * STEP 2: Register JDBC driver Class.forName(JDBCDriver);
 * 
 * // STEP 3: Open a connection System.out.println("Connecting to database...");
 * conn = DriverManager.getConnection(DB_URL, USER, PASS);
 * 
 * // STEP 4: Execute a query System.out.println("Creating database..."); stmt =
 * conn.createStatement();
 * 
 * String sql = "CREATE DATABASE threemcqueens"; stmt.executeUpdate(sql);
 * System.out.println("Database created successfully..."); } catch (SQLException
 * se) { // Handle errors for JDBC se.printStackTrace(); } catch (Exception e) {
 * // Handle errors for Class.forName e.printStackTrace(); } finally { //
 * finally block used to close resources try { if (stmt != null) stmt.close(); }
 * catch (SQLException se2) { }// nothing we can do try { if (conn != null)
 * conn.close(); } catch (SQLException se) { se.printStackTrace(); }// end
 * finally try }// end try
 * System.out.println("database threemcqueens successfully created!"); }
 * 
 * private static void createDatabaseTableForClient() { conn = null; stmt =
 * null; try { Class.forName(JDBCDriver);
 * 
 * conn = DriverManager.getConnection(DB_URL2, USER, PASS); System.out
 * .println("Creating Clients Table table in given database..."); stmt =
 * conn.createStatement();
 * 
 * String sql = "CREATE TABLE IF NOT EXISTS `clients` ( " +
 * " `client_id` int(20) NOT NULL AUTO_INCREMENT, " +
 * " `client_firstName` varchar(255) NOT NULL, " +
 * " `client_middleName` varchar(255) NOT NULL, " +
 * " `client_lastName` varchar(255) NOT NULL, " +
 * " `client_address` varchar(255) NOT NULL, " +
 * " `client_gender` enum('male','female') NOT NULL, " +
 * " `client_contactNo` varchar(255) NOT NULL, " + " PRIMARY KEY (`client_id`) "
 * + ") ENGINE=InnoDB AUTO_INCREMENT=10001 ";
 * 
 * stmt.executeUpdate(sql);
 * System.out.println("Created Clients table in given database..."); } catch
 * (SQLException se) { se.printStackTrace(); } catch (Exception e) {
 * e.printStackTrace(); } finally { try { if (stmt != null) conn.close(); }
 * catch (SQLException se) { } try { if (conn != null) conn.close(); } catch
 * (SQLException se) { se.printStackTrace(); } } }
 * 
 * private static void createDatabaseTableForEvents() { conn = null; stmt =
 * null; try { Class.forName(JDBCDriver);
 * 
 * conn = DriverManager.getConnection(DB_URL2, USER, PASS);
 * System.out.println("Creating Events table in given database..."); stmt =
 * conn.createStatement();
 * 
 * String sql = "CREATE TABLE IF NOT EXISTS `events` " +
 * "( `event_id` int(20) NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
 * "`client_id` int(20) NOT NULL, " + "`event_title` varchar(255) NOT NULL, " +
 * "`event_venue` varchar(255) NOT NULL, " +
 * "`event_details` varchar(255) NOT NULL, " +
 * "`event_type` enum('Birthday','Wedding','Baptismal','Reunion','SchoolAffairs','Pageant','Anniversary' , 'Photoshoot') NOT NULL, "
 * + "`event_date` date NOT NULL, " + "`event_time` varchar(10) NOT NULL, " +
 * "`event_guest` int(20) NOT NULL, " + "INDEX (`client_id`) ) ENGINE=InnoDB;";
 * 
 * stmt.executeUpdate(sql);
 * System.out.println("Created Events table in given database..."); } catch
 * (SQLException se) { se.printStackTrace(); } catch (Exception e) {
 * e.printStackTrace(); } finally { try { if (stmt != null) conn.close(); }
 * catch (SQLException se) { } try { if (conn != null) conn.close(); } catch
 * (SQLException se) { se.printStackTrace(); } } }
 * 
 * private static void createDatabaseTableForPayments() { conn = null; stmt =
 * null; try { Class.forName(JDBCDriver);
 * 
 * conn = DriverManager.getConnection(DB_URL2, USER, PASS);
 * System.out.println("Creating Payments table in given database..."); stmt =
 * conn.createStatement();
 * 
 * String sql = "CREATE TABLE IF NOT EXISTS `payments` " +
 * "(`payment_id` INT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY , " +
 * "`client_id` INT(20) NOT NULL , `event_id` INT(20) NOT NULL, " +
 * "`payment_total` INT(30) NOT NULL , " +
 * "`payment_type` VARCHAR(20) NOT NULL , " +
 * "`payment_paid` INT(30) NOT NULL , " +
 * "`payment_balance` INT(30) NOT NULL , " +
 * "`payment_datetime` VARCHAR(255) NOT NULL , " +
 * "CONSTRAINT fkPaymentEvent_id FOREIGN KEY (event_id) REFERENCES events(event_id) , "
 * +
 * "CONSTRAINT fkPaymentClient_id FOREIGN KEY (client_id) REFERENCES clients(client_id)) "
 * + "ENGINE = InnoDB;";
 * 
 * stmt.executeUpdate(sql);
 * System.out.println("Created Payments table in given database..."); } catch
 * (SQLException se) { se.printStackTrace(); } catch (Exception e) {
 * e.printStackTrace(); } finally { try { if (stmt != null) conn.close(); }
 * catch (SQLException se) { } try { if (conn != null) conn.close(); } catch
 * (SQLException se) { se.printStackTrace(); } } }
 * 
 * private static void createDatabaseTableForPackages() { conn = null; stmt =
 * null; try { Class.forName(JDBCDriver);
 * 
 * conn = DriverManager.getConnection(DB_URL2, USER, PASS);
 * System.out.println("Creating Packages table in given database..."); stmt =
 * conn.createStatement();
 * 
 * String sql = "CREATE TABLE IF NOT EXISTS `threemcqueens`.`packages` " +
 * "( `package_id` INT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY , " +
 * "`package_name` VARCHAR(255) NOT NULL , " +
 * "`package_desc` VARCHAR(255) NOT NULL , " +
 * "`package_cost` INT(30) NOT NULL ) ENGINE = InnoDB;";
 * 
 * stmt.executeUpdate(sql);
 * System.out.println("Created Packages table in given database..."); } catch
 * (SQLException se) { se.printStackTrace(); } catch (Exception e) {
 * e.printStackTrace(); } finally { try { if (stmt != null) conn.close(); }
 * catch (SQLException se) { } try { if (conn != null) conn.close(); } catch
 * (SQLException se) { se.printStackTrace(); } } }
 * 
 * private static void createDatabaseTableForServices() { conn = null; stmt =
 * null; try { Class.forName(JDBCDriver);
 * 
 * conn = DriverManager.getConnection(DB_URL2, USER, PASS);
 * System.out.println("Creating Services table in given database..."); stmt =
 * conn.createStatement();
 * 
 * String sql = "CREATE TABLE IF NOT EXISTS `threemcqueens`.`services` " +
 * "( `service_id` INT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY , " +
 * "`package_id` INT(20) NOT NULL, " + "`service_name` VARCHAR(255) NOT NULL , "
 * + "`service_desc` VARCHAR(255) NOT NULL , " +
 * "`service_cost` INT(30) NOT NULL," +
 * " INDEX  (`package_id`) ) ENGINE = InnoDB;";
 * 
 * stmt.executeUpdate(sql);
 * System.out.println("Created Services table in given database..."); } catch
 * (SQLException se) { se.printStackTrace(); } catch (Exception e) {
 * e.printStackTrace(); } finally { try { if (stmt != null) conn.close(); }
 * catch (SQLException se) { } try { if (conn != null) conn.close(); } catch
 * (SQLException se) { se.printStackTrace(); } } }
 * 
 * private static void createDatabaseTableForUsers() { conn = null; stmt = null;
 * try { Class.forName(JDBCDriver);
 * 
 * conn = DriverManager.getConnection(DB_URL2, USER, PASS);
 * System.out.println("Creating Users table in given database..."); stmt =
 * conn.createStatement();
 * 
 * String sql = "CREATE TABLE IF NOT EXISTS `threemcqueens`.`users` " +
 * "( `user_id` INT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY , " +
 * "`user_name` VARCHAR(255) NOT NULL , " +
 * "`user_password` VARCHAR(255) NOT NULL , " +
 * "`user_fname` VARCHAR(255) NOT NULL , " +
 * "`user_mname` VARCHAR(255) NOT NULL , " +
 * "`user_lname` VARCHAR(255) NOT NULL , " +
 * "`user_lastLogIn` DATETIME NOT NULL ) ENGINE = InnoDB;";
 * 
 * stmt.executeUpdate(sql);
 * System.out.println("Created Users table in given database..."); } catch
 * (SQLException se) { se.printStackTrace(); } catch (Exception e) {
 * e.printStackTrace(); } finally { try { if (stmt != null) conn.close(); }
 * catch (SQLException se) { } try { if (conn != null) conn.close(); } catch
 * (SQLException se) { se.printStackTrace(); } } }
 * 
 * private static void createDatabaseTableForLogs() { conn = null; stmt = null;
 * try { Class.forName(JDBCDriver);
 * 
 * conn = DriverManager.getConnection(DB_URL2, USER, PASS);
 * System.out.println("Creating Logs table in given database..."); stmt =
 * conn.createStatement();
 * 
 * String sql = "CREATE TABLE IF NOT EXISTS `threemcqueens`.`logs` " +
 * "( `log_id` INT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY , " +
 * "`user_id` INT(20) NOT NULL, " + "`log_title` INT(30) NOT NULL , " +
 * "`log_desc` VARCHAR(20) NOT NULL , " + "`log_type` INT(30) NOT NULL , " +
 * "`log_datetime` DATETIME NOT NULL , " +
 * " INDEX  (`user_id`) ) ENGINE = InnoDB;";
 * 
 * stmt.executeUpdate(sql);
 * System.out.println("Created Logs table in given database..."); } catch
 * (SQLException se) { se.printStackTrace(); } catch (Exception e) {
 * e.printStackTrace(); } finally { try { if (stmt != null) conn.close(); }
 * catch (SQLException se) { } try { if (conn != null) conn.close(); } catch
 * (SQLException se) { se.printStackTrace(); } } }
 * 
 * private static void createDatabaseTableForServicesWanted() { conn = null;
 * stmt = null; try { Class.forName(JDBCDriver);
 * 
 * conn = DriverManager.getConnection(DB_URL2, USER, PASS);
 * System.out.println("Creating Logs table in given database..."); stmt =
 * conn.createStatement();
 * 
 * String sql = "CREATE TABLE `services_wanted`( " +
 * "`sw_id` INT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY ," +
 * "`event_id` INT(20) NOT NULL ," + "`client_id` INT(20) NOT NULL ," +
 * "`package_name` VARCHAR(255) NOT NULL ," +
 * "`service_name` VARCHAR(255) NOT NULL ," +
 * "`service_desc` VARCHAR(255) NOT NULL ," +
 * "`service_cost` INT(30) NOT NULL , " +
 * "CONSTRAINT fkEvent_id FOREIGN KEY (event_id) REFERENCES events(event_id) , "
 * +
 * "CONSTRAINT fkClient_id FOREIGN KEY (client_id) REFERENCES clients(client_id) ) "
 * + "ENGINE = InnoDB;";
 * 
 * stmt.executeUpdate(sql);
 * System.out.println("Created Logs table in given database..."); } catch
 * (SQLException se) { se.printStackTrace(); } catch (Exception e) {
 * e.printStackTrace(); } finally { try { if (stmt != null) conn.close(); }
 * catch (SQLException se) { } try { if (conn != null) conn.close(); } catch
 * (SQLException se) { se.printStackTrace(); } } }
 * 
 * private static void createDatabaseTableForPaymenHistory() { conn = null; stmt
 * = null; try { Class.forName(JDBCDriver);
 * 
 * conn = DriverManager.getConnection(DB_URL2, USER, PASS);
 * System.out.println("Creating Logs table in given database..."); stmt =
 * conn.createStatement();
 * 
 * String sql = "CREATE TABLE `threemcqueens`.`payment_history` " +
 * "( `ph_id` INT(30) NOT NULL AUTO_INCREMENT , " +
 * "`payment_id` INT(30) NOT NULL , " + "`payment_total` INT(255) NOT NULL , " +
 * "`payment_balance` INT(255) NOT NULL , " +
 * "`payment_type` VARCHAR(255) NOT NULL , " +
 * "`payment_paid` INT(255) NOT NULL , " +
 * "`payment_paidThisdate` INT(255) NOT NULL , " +
 * "`payment_date` VARCHAR(255) NOT NULL , " +
 * "CONSTRAINT fkPayshist_id FOREIGN KEY (payment_id) REFERENCES payments(payment_id) ,"
 * + "PRIMARY KEY  (`ph_id`) ) ENGINE = InnoDB AUTO_INCREMENT=10001";
 * 
 * stmt.executeUpdate(sql);
 * System.out.println("Created Logs table in given database..."); } catch
 * (SQLException se) { se.printStackTrace(); } catch (Exception e) {
 * e.printStackTrace(); } finally { try { if (stmt != null) conn.close(); }
 * catch (SQLException se) { } try { if (conn != null) conn.close(); } catch
 * (SQLException se) { se.printStackTrace(); } } }
 * 
 * private static void createDatabaseTableForServicesList() { conn = null; stmt
 * = null; try { Class.forName(JDBCDriver); conn =
 * DriverManager.getConnection(DB_URL2, USER, PASS);
 * System.out.println("Creating Logs table in given database..."); stmt =
 * conn.createStatement(); String sql = "CREATE TABLE `services_list` " +
 * "( `sl_id` int(30) NOT NULL AUTO_INCREMENT, " +
 * "`service_name` varchar(255) NOT NULL, " +
 * "`service_desc` varchar(2552) NOT NULL, " +
 * "`service_cost` int(255) NOT NULL, " + "PRIMARY KEY (`sl_id`)) " +
 * "ENGINE=InnoDB AUTO_INCREMENT=10001"; stmt.executeUpdate(sql);
 * System.out.println("Created Logs table in given database..."); } catch
 * (SQLException se) { se.printStackTrace(); } catch (Exception e) {
 * e.printStackTrace(); } finally { try { if (stmt != null) conn.close(); }
 * catch (SQLException se) { } try { if (conn != null) conn.close(); } catch
 * (SQLException se) { se.printStackTrace(); } } }
 * 
 * private static void createAutoIncrementIDForTables(String tableName) { conn =
 * null; stmt = null; try { Class.forName(JDBCDriver);
 * 
 * conn = DriverManager.getConnection(DB_URL2, USER, PASS);
 * System.out.println("Creating Auto Increment id in " + tableName +
 * " table in given database..."); stmt = conn.createStatement();
 * 
 * String sql = "ALTER TABLE `" + tableName + "` AUTO_INCREMENT = 10001";
 * 
 * stmt.executeUpdate(sql); System.out.println("Created Auto Increment id in " +
 * tableName + " table in given database..."); } catch (SQLException se) {
 * se.printStackTrace(); } catch (Exception e) { e.printStackTrace(); } finally
 * { try { if (stmt != null) conn.close(); } catch (SQLException se) { } try {
 * if (conn != null) conn.close(); } catch (SQLException se) {
 * se.printStackTrace(); } } }
 * 
 * private static void createRelations(String tableName, String constraint,
 * String key, String refTableName) { conn = null; stmt = null; try {
 * Class.forName(JDBCDriver);
 * 
 * conn = DriverManager.getConnection(DB_URL2, USER, PASS);
 * System.out.println("Creating Logs table in given database..."); stmt =
 * conn.createStatement();
 * 
 * String sql = "ALTER TABLE `" + tableName + "` " + "ADD CONSTRAINT `" +
 * constraint + "` " + "FOREIGN KEY ( `" + key + "` ) " +
 * "REFERENCES `threemcqueens`.`" + refTableName + "`(`" + key + "`) " +
 * "ON DELETE NO ACTION ON UPDATE CASCADE;";
 * 
 * stmt.executeUpdate(sql);
 * System.out.println("Created Logs table in given database..."); } catch
 * (SQLException se) { se.printStackTrace(); } catch (Exception e) {
 * e.printStackTrace(); } finally { try { if (stmt != null) conn.close(); }
 * catch (SQLException se) { } try { if (conn != null) conn.close(); } catch
 * (SQLException se) { se.printStackTrace(); } } } }
 */