package com.threemc.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import com.threemc.data.Payment;
import com.threemc.data.PaymentHistory;
import com.threemc.data.PaymentModuleData;
import com.threemc.model.DatabasePaymentModule;

public class ControllerForPaymentDetails {

	private DatabasePaymentModule db;

	public ControllerForPaymentDetails() {
		db = new DatabasePaymentModule();
	}

	public String connect() throws Exception {
		return db.connect();
	}

	public void disconnect() throws Exception {
		db.disconnect();
	}

	public Connection getConnection() {
		return db.getConnection();
	}

	public void loadPaymentDataWithBalance() throws SQLException {
		db.loadPaymentDataWithBalance();
	}

	public void loadPaidPaymentData() throws SQLException {
		db.loadPaidPaymentData();
	}

	public void loadPaymentHistoryByClientEvent(int c_id, int e_id) throws SQLException {
		db.loadPaymentHistoryByClientEvent(c_id, e_id);
	}

	public int loadPaymentDataTotalCostById(int pid) throws SQLException {
		return db.loadPaymentDataTotalCostById(pid);
	}

	public void searchPaymentWithBalance(String category, String value) throws SQLException {
		db.searchPaymentWithBalance(category, value);
	}

	public void searchPaidPaymentData(String category, String value) throws SQLException {
		db.searchPaidPaymentData(category, value);
	}

	public void updatePayment(Payment payment) throws SQLException {
		db.updatePayment(payment);
	}

	public void saveInPaymentHistory(PaymentHistory pay) throws SQLException {
		db.saveInPaymentHistory(pay);
	}

	public ArrayList<PaymentModuleData> getPaymentDataWithBalance() {
		return db.getPaymentDataWithBalance();
	}

	public ArrayList<PaymentModuleData> getPaidPaymentData() {
		return db.getPaidPaymentData();
	}

	public ArrayList<PaymentHistory> getPaymentHistory() {
		return db.dbPaymentHistory;
	}
}
