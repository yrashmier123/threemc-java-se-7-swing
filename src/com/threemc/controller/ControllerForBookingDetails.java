package com.threemc.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.threemc.data.Booking;
import com.threemc.data.Client;
import com.threemc.data.HomeData;
import com.threemc.data.Notice;
import com.threemc.data.Package;
import com.threemc.data.Payment;
import com.threemc.data.Service;
import com.threemc.data.ServiceCategory;
import com.threemc.data.ServiceList;
import com.threemc.data.ServicesWanted;
import com.threemc.model.DatabaseBookingDetails;
import com.threemc.view.BookingClientEventForm;
import com.threemc.view.BookingEventForm;
import com.threemc.view.BookingPaymentEventForm;
import com.threemc.view.CategoryEventType;
import com.threemc.view.CategoryGender;
import com.threemc.view.CategoryPaymentType;

public class ControllerForBookingDetails {

	private DatabaseBookingDetails db;

	public ControllerForBookingDetails() {
		db = new DatabaseBookingDetails();
	}

	public String connect() throws Exception {
		return db.connect();
	}

	public void disconnect() throws Exception {
		db.disconnect();
	}

	public void addClient(BookingClientEventForm ev) {
		int id = ev.getId();
		String fname = ev.getClientFirstName();
		String mname = ev.getClientMiddleName();
		String lname = ev.getClientLastName();
		String addres = ev.getClientAddress();
		String gender = ev.getClientGender();
		String cont = ev.getClientContactNumber();

		CategoryGender genderCat = null;
		if(gender.equalsIgnoreCase("Male")) {
			genderCat = CategoryGender.male;
		} else if (gender.equalsIgnoreCase("Female")) {
			genderCat = CategoryGender.female;
		}

		Client client;
		if(id >= 1) {
			client = new Client(id, fname, mname, lname, addres, cont, genderCat);
		} else {
			client = new Client(fname, mname, lname, addres, cont, genderCat);
		}

		db.addClient(client);
	}
	
	public void addBooking(BookingEventForm ev) {
		int id = ev.getId();
		int client_id = ev.getClient_id();
		String title = ev.getEventName();
		String venue = ev.getEventVenue();
		String date = ev.getEventDate();
		String time = ev.getEventTime();
		String type = ev.getEventType();
		int gustno = ev.getEventGuestNumber();
		String detail = ev.getEventDetails();

		CategoryEventType eventType = null;
		if(type.equalsIgnoreCase("Birthday")) {
			eventType = CategoryEventType.Birthday;
		} else if(type.equalsIgnoreCase("Wedding")) {
			eventType = CategoryEventType.Wedding;
		} else if(type.equalsIgnoreCase("Baptismal")) {
			eventType = CategoryEventType.Baptismal;
		} else if(type.equalsIgnoreCase("Reunion")) {
			eventType = CategoryEventType.Reunion;
		} else if(type.equalsIgnoreCase("School Affairs")) {
			eventType = CategoryEventType.SchoolAffairs;
		} else if(type.equalsIgnoreCase("Pageant")) {
			eventType = CategoryEventType.Pageant;
		} else if(type.equalsIgnoreCase("Anniversary")) {
			eventType = CategoryEventType.Anniversary;
		} else if(type.equalsIgnoreCase("Photoshoot")) {
			eventType = CategoryEventType.Photoshoot;
		} else if(type.equalsIgnoreCase("Party")) {
			eventType = CategoryEventType.Party;
		} else if(type.equalsIgnoreCase("Seminar")) {
			eventType = CategoryEventType.Seminar;
		} else if(type.equalsIgnoreCase("Forum")) {
			eventType = CategoryEventType.Forum;
		} else if(type.equalsIgnoreCase("Sponsorsip")) {
			eventType = CategoryEventType.Sponsorhip;
		} else {
			eventType = CategoryEventType.Others;
		}

		Booking booking;

		if(eventType.name().equalsIgnoreCase("Others")) {
			if(id >= 1) {
				booking = new Booking(id, client_id, title, venue, detail, gustno, date, time , type);
			} else {
				booking = new Booking(client_id, title, venue, detail, gustno, date, time , type);
			}
			db.addBooking(booking);
		} else {
			if(id >= 1) {
				booking = new Booking(id, client_id, title, venue, detail, gustno, date, time , eventType.name());
			} else {
				booking = new Booking(client_id, title, venue, detail, gustno, date, time , eventType.name());
			}
			db.addBooking(booking);
		}
	}

	public void addServicesWanted(ArrayList<ServicesWanted> sw) {
		db.addServicesWanted(sw);
	}

	public void addPaymentRecord(BookingPaymentEventForm ev) {

		int id = ev.getId();
		int client_id = ev.getClient_id();
		int event_id = ev.getEvent_id();
		int ptotal = ev.getPaymentTotal();
		int pbal = ev.getPaymentBalance();
		int ppaid = ev.getPaymentPaid();
		CategoryPaymentType type = ev.getPaymentType();
		String date = ev.getPaymentDate();

		Payment pay;
		if(id >= 1) {
			pay = new Payment(id, client_id, event_id, ptotal, type, ppaid, pbal, date);
		} else {
			pay = new Payment(client_id, event_id, ptotal, type, ppaid, pbal, date);
		}
		db.addPaymentRecord(pay);
	}

	public void saveClient() throws SQLException {
		db.saveClient();
	}

	public void saveBooking() throws SQLException {
		db.saveBooking();
	}

	public void saveServicesWanted() throws SQLException {
		db.saveServicesWanted();
	}

	public void savePaymentRecord() throws SQLException {
		db.savePayments();
	}

	public void saveCategory(ServiceCategory sc) throws SQLException {
		db.saveCategory(sc);
	}

	public void saveNotice(Notice not) throws SQLException {
		db.saveNotice(not);
	}

	public boolean checkifEventHasServices(int event_id) throws SQLException {
		return db.checkifEventHaveServices(event_id);
	}

	public ArrayList<Booking> checkifDateHasEvent(String date, String status, String status2) throws SQLException {
		return db.checkifDateHasEvent(date,status,status2);
	}

	public ArrayList<Booking> checkifDateHasEventForBookingList(String date) throws SQLException {
		return db.checkifDateHasEventForBookingList(date);
	}

	public void deleteEventServices(int event_id) throws SQLException {
		db.deleteEventServices(event_id);
	}

	public void deleteClientInfo(int client_id) throws SQLException {
		db.deleteEventServices(client_id);
	}

	public boolean checkIfServicesHasPayment(int client_ids, int event_ids) throws SQLException {
		return db.checkIfServicesHasPayment(client_ids, event_ids);
	}

	public void loadClient(String cat) throws SQLException {
		db.loadClientRecords(cat);
	}

	public void loadClientRecordsForBooking(String cat) throws SQLException {
		db.loadClientRecordsForBooking(cat);
	}

	public void loadClientWithEventAndPayment() throws SQLException {
		db.loadClientWithEventAndPayment();
	}

	public Booking loadBookingRecordsByEventId(int event_id) throws SQLException {
		return db.loadBookingRecordsByEventId(event_id);
	}

	public ArrayList<Booking> loadBookingRecordsByEventDate(String currentDate) throws SQLException {
		return db.loadBookingRecordsByEventDate(currentDate);
	}

	public void loadAllBookingRecord(String status, String statusu, String payment) throws SQLException {
		db.loadAllBookingRecords(status,statusu,payment);
	}

	public void loadServicesRecord(int client_id, int event_id) throws SQLException {
		db.loadServicesRecords(client_id, event_id);
	}

	public void loadAllBookingRecordsByClientID(int client_ids) throws SQLException {
		db.loadAllBookingRecordsByClientID(client_ids);
	}

	public void loadBookingRecordsByClientIdForPrintReports(int c_id) throws SQLException {
		db.loadBookingRecordsByClientIdForPrintReports(c_id);
	}

	public void loadBookingRecordsByClientIDTypeStatus(int client_ids, int type, String status) throws SQLException {
		db.loadBookingRecordsByClientIDTypeStatus(client_ids, type, status);
	}

	public void loadAllBookingRecordsByClientWithoutServices(int c_id) throws SQLException {
		db.loadAllBookingRecordsByClientWithouServices(c_id);
	}

	public void loadEventsWithServicesAndPayments() throws SQLException {
		db.loadEventsWithServicesAndPayments();
	}

	public void loadPackages() throws SQLException {
		db.loadPackageRecords();
	}

	public void loadServices() throws SQLException {
		db.loadAllServicesFromServiceList();
	}

	public Payment loadPaymentRecord(int client_id, int event_id) throws SQLException {
		return db.loadPaymentRecord(client_id, event_id);
	}

	public void loadAllServiceCategory() throws SQLException {
		db.loadAllServiceCategory();
	}

	public void loadAllNotices() throws SQLException {
		db.loadAllNotices();
	}

	public Notice loadLastNotice() throws SQLException {
		return db.loadLastNotice();
	}

	public ArrayList<Booking> getBooking() {
		return db.getBooking();
	}

	public ArrayList<ServiceCategory> getServiceCategory() {
		return db.getServiceCatRecord();
	}

	public ArrayList<Package> getPackages() {
		return db.getPackage();
	}

	public List<Service> getServices() {
		return db.getService();
	}

	public ArrayList<ServiceList> getServiceList() {
		return db.getServiceList();
	}

	public ArrayList<ServicesWanted> getServicesWanted() {
		return db.getServicesWanted();
	}

	public ArrayList<HomeData> getHomeData() {
		return db.getHomeData();
	}

	public ArrayList<Payment> getPayment() {
		return db.getPaymentRecord();
	}

	public ArrayList<ServiceCategory> getServiceCatRecord() {
		return db.getServiceCatRecord();
	}

	public ArrayList<Notice> getNotices() {
		return db.getNotices();
	}

	public int getClientCount() throws SQLException {
		return db.getClientCount();
	}

	public int getEventsWithServicesAndPaymentsCount() throws SQLException {
		return db.getEventsWithServicesAndPaymentsCount();
	}

	public int getEventsThatisEditedCount() throws SQLException {
		return db.getEventsThatisEditedCount();
	}

	public void searchClient(String value) throws SQLException {
		db.searchClient(value);
	}

	public void searchClientWithEventAndPayment(String category, String value) throws SQLException {
		db.searchClientWithEventAndPayment(category, value);
	}

	public void searchClientById(int value) throws SQLException {
		db.searchClientById(value);
	}

	public void searchEventsWithServicesAndPayments(String category, String value) throws SQLException {
		db.searchEventsWithServicesAndPayments(category, value);
	}

	public void searchAllBookingRecord(String val, String cat , String status, String statusu, String payment) throws SQLException {
		db.searchAllBookingRecords(val, cat, status, statusu, payment);
	}

	public void updateBookingStatus(int event_id, String status) throws SQLException {
		db.updateBookingStatus(event_id, status);
	}

	public void updateBookingStatus2(int event_id, String status) throws SQLException {
		db.updateBookingStatus2(event_id, status);
	}

	public void updateBookingStatusPayment(int event_id, String status) throws SQLException {
		db.updateBookingStatusPayment(event_id, status);
	}

	public void updateBookingisEdited() throws SQLException {
		db.updateBookingisEdited();
	}

	public ArrayList<Client> getClient() {
		return db.getClient();
	}
}
