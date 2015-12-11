package com.threemc.data;

import com.threemc.view.CategoryPaymentType;

public class Payment {

	private int id;
	private int client_id;
	private int event_id;
	private int paymentTotal;
	private CategoryPaymentType paymentType;
	private int paymentPaid;
	private int paymentBalance;
	private String paymentDate;

	public Payment(int client_id, int event_id, int paymentTotal,
			CategoryPaymentType paymentType, int paymentPaid,
			int paymentBalance, String paymentDate) {
		this.client_id = client_id;
		this.event_id = event_id;
		this.paymentTotal = paymentTotal;
		this.paymentType = paymentType;
		this.paymentPaid = paymentPaid;
		this.paymentBalance = paymentBalance;
		this.paymentDate = paymentDate;
	}

	public Payment(int id, int client_id, int event_id, int paymentTotal,
			CategoryPaymentType paymentType, int paymentPaid,
			int paymentBalance, String paymentDate) {
		
		this(client_id, event_id, paymentTotal, paymentType, paymentPaid, paymentBalance, paymentDate);
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getClient_id() {
		return client_id;
	}

	public void setClient_id(int client_id) {
		this.client_id = client_id;
	}

	public int getEvent_id() {
		return event_id;
	}

	public void setEvent_id(int event_id) {
		this.event_id = event_id;
	}

	public int getPaymentTotal() {
		return paymentTotal;
	}

	public void setPaymentTotal(int paymentTotal) {
		this.paymentTotal = paymentTotal;
	}

	public CategoryPaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(CategoryPaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public int getPaymentPaid() {
		return paymentPaid;
	}

	public void setPaymentPaid(int paymentPaid) {
		this.paymentPaid = paymentPaid;
	}

	public int getPaymentBalance() {
		return paymentBalance;
	}

	public void setPaymentBalance(int paymentBalance) {
		this.paymentBalance = paymentBalance;
	}

	public String getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}
}
