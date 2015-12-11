package com.threemc.data;

import com.threemc.view.CategoryPaymentType;

public class PaymentModuleData {

	private int id;
	private int payment_id;
	private int client_id;
	private int event_id;
	private String clientFirstName;
	private String clientMiddleName;
	private String clientLastName;
	private String clientContactNo;
	private String eventName;
	private String paymentDate;
	private CategoryPaymentType paymentType;
	private int paymentTotal;
	private int paymentPaid;
	private int paymentBalance;

	public PaymentModuleData(int payment_id, int client_id, int event_id, String clientFirstName,
			String clientMiddleName, String clientLastName, String clientContactNo, 
			String eventName, String paymentDate, CategoryPaymentType paymentType, 
			int paymentTotal, int paymentPaid, int paymentBalance) {

		this.payment_id = payment_id;
		this.client_id = client_id;
		this.event_id = event_id;
		this.clientFirstName = clientFirstName;
		this.clientMiddleName = clientMiddleName;
		this.clientLastName = clientLastName;
		this.clientContactNo = clientContactNo;
		this.eventName = eventName;
		this.paymentDate = paymentDate;
		this.paymentType = paymentType;
		this.paymentTotal = paymentTotal;
		this.paymentPaid = paymentPaid;
		this.paymentBalance = paymentBalance;
	}
	
	public PaymentModuleData(int id, int payment_id, int client_id, int event_id, String clientFirstName,
			String clientMiddleName, String clientLastName, String clientContactNo, 
			String eventName, String paymentDate, CategoryPaymentType paymentType, 
			int paymentTotal, int paymentPaid, int paymentBalance) {
		this(payment_id,client_id,event_id,clientFirstName, clientMiddleName, clientLastName, clientContactNo, eventName,paymentDate,paymentType, paymentTotal, paymentPaid, paymentBalance);
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPayment_id() {
		return payment_id;
	}

	public void setPayment_id(int payment_id) {
		this.payment_id = payment_id;
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

	public String getClientFirstName() {
		return clientFirstName;
	}

	public void setClientFirstName(String clientFirstName) {
		this.clientFirstName = clientFirstName;
	}

	public String getClientMiddleName() {
		return clientMiddleName;
	}

	public void setClientMiddleName(String clientMiddleName) {
		this.clientMiddleName = clientMiddleName;
	}

	public String getClientLastName() {
		return clientLastName;
	}

	public void setClientLastName(String clientLastName) {
		this.clientLastName = clientLastName;
	}

	public String getClientContactNo() {
		return clientContactNo;
	}

	public void setClientContactNo(String clientContactNo) {
		this.clientContactNo = clientContactNo;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	public CategoryPaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(CategoryPaymentType paymentType) {
		this.paymentType = paymentType;
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

	public int getPaymentBalance() {
		return paymentBalance;
	}

	public void setPaymentBalance(int paymentBalance) {
		this.paymentBalance = paymentBalance;
	}
}
