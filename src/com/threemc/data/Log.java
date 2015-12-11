package com.threemc.data;
/**
 * @author Rashmier Ynawat
 * @since July 3, 2015
 * @version 1.0
 */
public class Log {

	public static final String SAVE_CLIENT = "Successfully saved new Client";
	public static final String SAVE_BOOKING = "Successfully saved new Booking";
	public static final String SAVE_BOOKING_PACKAGE = "Successfully saved Booking's Package";
	public static final String SAVE_BOOKING_SERVICES = "Successfully saved Booking's Services";
	public static final String SAVE_INITIAL_PAYMENT = "Successfully saved Client's Initial Payment";
	public static final String SAVE_EMPLOYEE = "Successfully saved Employee records";
	public static final String SAVE_USER = "Successfully saved User records";
	public static final String SAVE_POSITION = "Successfully saved Position information";

	public static final String UPDATE_CLIENT = "Successfully Updated Client Information";
	public static final String UPDATE_BOOKING = "Successfully Updated Booking Information";
	public static final String UPDATE_BOOKING_PACKAGE = "Successfully Updated Booking's package";
	public static final String UPDATE_BOOKING_SERVICES = "Successfully Updated Booking's Services";
	public static final String UPDATE_CLIENT_PAYMENT = "Successfully Updated Client's Payment";
	public static final String UPDATE_EMPLOYEE = "Successfully Updated Employee records";
	public static final String UPDATE_USER = "Successfully Updated User records";
	public static final String UPDATE_POSITION = "Successfully Updated Position information";

	public static final String SAVE_CLIENT_FAILED = "Unable to save client information";
	public static final String SAVE_BOOKING_FAILED = "Unable to save booking information";
	public static final String SAVE_BOOKING_PACKAGE_FAILED = "Unable to save package information";
	public static final String SAVE_BOOKING_SERVICES_FAILED = "Unable to save services information";
	public static final String SAVE_INITIAL_PAYMENT_FAILED = "Unable to save initial payment information";
	public static final String SAVE_EMPLOYEE_FAILED = "Unable to save Employee record";
	public static final String SAVE_USER_FAILED = "Unable to save User record";
	public static final String SAVE_POSITION_FAILED = "Unable to save Position information";

	public static final String UPDATE_CLIENT_FAILED = "Unable to update client information";
	public static final String UPDATE_BOOKING_FAILED = "Unable to update booking information";
	public static final String UPDATE_BOOKING_PACKAGE_FAILED = "Unable to update package information";
	public static final String UPDATE_BOOKING_SERVICES_FAILED = "Unable to update services information";
	public static final String UPDATE_CLIENT_PAYMENT_FAILED = "Unable to update initial payment information";

	public static final String TITLE_CLIENT = "Client";
	public static final String TITLE_BOOKING = "Booking";
	public static final String TITLE_PACKAGE = "Package";
	public static final String TITLE_SERVICES = "Services";
	public static final String TITLE_PAYMENT = "Payment";
	public static final String TITLE_EMPLOYEE = "Employee";
	public static final String TITLE_USER = "User";
	public static final String TITLE_POSITION = "Position";

	private int id;
	private int user_id;
	private String logTitle;
	private String logDesc;
	private String logDate;

	public Log(int user_id, String logTitle, String logDesc, String logDate) {
		this.user_id = user_id;
		this.logTitle = logTitle;
		this.logDesc = logDesc;
		this.logDate = logDate;
	}

	public Log(int id, int user_id, String logTitle, String logDesc, String logDate) {
		this(user_id, logTitle, logDesc, logDate);
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getLogTitle() {
		return logTitle;
	}

	public void setLogTitle(String logTitle) {
		this.logTitle = logTitle;
	}

	public String getLogDesc() {
		return logDesc;
	}

	public void setLogDesc(String logDesc) {
		this.logDesc = logDesc;
	}

	public String getLogDate() {
		return logDate;
	}

	public void setLogDate(String logDate) {
		this.logDate = logDate;
	}
}
