package com.threemc.data;

import com.threemc.view.CategoryPaymentType;

public class PaymentHistory {

	private int id;
	private int client_id;
	private int event_id;
	private int payment_id;
	private CategoryPaymentType paymentType;
	private int paymentPaidThisDate;
	private String paymentDate;
	private String paymentDesc;

	public PaymentHistory(int client_id, int event_id, int payment_id,
			CategoryPaymentType paymentType, int paymentPaidThisDate,
			String paymentDate) {

		this.client_id = client_id;
		this.event_id = event_id;
		this.payment_id = payment_id;
		this.paymentType = paymentType;
		this.paymentPaidThisDate = paymentPaidThisDate;
		this.paymentDate = paymentDate;
	}

	public PaymentHistory(int id, int client_id, int event_id, int payment_id,
			CategoryPaymentType paymentType, int paymentPaidThisDate,
			String paymentDate) {

		this(client_id, event_id, payment_id, paymentType, paymentPaidThisDate,
				paymentDate);
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

	public int getPayment_id() {
		return payment_id;
	}

	public void setPayment_id(int payment_id) {
		this.payment_id = payment_id;
	}

	public CategoryPaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(CategoryPaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public int getPaymentPaidThisDate() {
		return paymentPaidThisDate;
	}

	public void setPaymentPaidThisDate(int paymentPaidThisDate) {
		this.paymentPaidThisDate = paymentPaidThisDate;
	}

	public String getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getPaymentDesc() {
		return paymentDesc;
	}

	public void setPaymentDesc(String paymentDesc) {
		this.paymentDesc = paymentDesc;
	}
}
