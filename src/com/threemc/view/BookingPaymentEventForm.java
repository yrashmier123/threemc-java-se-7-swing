package com.threemc.view;

import java.util.EventObject;

public class BookingPaymentEventForm extends EventObject {

	private int id;
	private int client_id;
	private int event_id;
	private int paymentTotal;
	private int paymentPaid;
	private CategoryPaymentType paymentType;
	private int paymentBalance;
	private String paymentDate;

	public BookingPaymentEventForm(Object source) {
		super(source);
	}

	public BookingPaymentEventForm(Object source, int client_id, int event_id,  int paymentTotal, int paymentPaid, CategoryPaymentType paymentType, int paymentBalance, String paymentDate) {
		super(source);
		this.client_id = client_id;
		this.event_id = event_id;
		this.paymentTotal = paymentTotal;
		this.paymentPaid = paymentPaid;
		this.paymentType = paymentType;
		this.paymentBalance = paymentBalance;
		this.paymentDate = paymentDate;
	}

	public BookingPaymentEventForm(Object source,int id, int client_id, int event_id, int paymentTotal, int paymentPaid, CategoryPaymentType paymentType, int paymentBalance, String paymentDate) {
		this( source, client_id, event_id, paymentTotal,  paymentPaid,  paymentType, paymentBalance, paymentDate);
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPaymentTotal() {
		return paymentTotal;
	}

	public void setPaymentTotal(int paymentTotal) {
		this.paymentTotal = paymentTotal;
	}

	public int getPaymentPaid() {
		return paymentPaid;
	}

	public void setPaymentPaid(int paymentPaid) {
		this.paymentPaid = paymentPaid;
	}

	public CategoryPaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(CategoryPaymentType paymentType) {
		this.paymentType = paymentType;
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
