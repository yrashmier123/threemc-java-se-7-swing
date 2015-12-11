package com.threemc.data;

import com.threemc.view.CategoryPaymentType;

public class HomeData {

	private int id;
	private int client_id;
	private int event_id;
	private int payment_id;
	private String clientFirstName;
	private String clientMiddleName;
	private String clientLastName;
	private String eventName;
	private String eventType;
	private String eventDate;
	private String eventTime;
	private String eventVenue;
	private int eventGuest;
	private CategoryPaymentType paymentType;

	public HomeData(int client_id, int event_id, int payment_id,
			String clientFirstName, String clientMiddleName,
			String clientLastName, 
			String eventName, String eventType, String eventDate,
			String eventTime, String eventVenue, int eventGuest,
			CategoryPaymentType paymentType) {

		this.client_id = client_id;
		this.event_id = event_id;
		this.payment_id = payment_id;
		this.clientFirstName = clientFirstName;
		this.clientMiddleName = clientMiddleName;
		this.clientLastName = clientLastName;
		this.eventName = eventName;
		this.eventType = eventType;
		this.eventDate = eventDate;
		this.eventTime = eventTime;
		this.eventVenue = eventVenue;
		this.eventGuest = eventGuest;
		this.paymentType = paymentType;
	}

	public HomeData(int id, int client_id, int event_id, int payment_id,
			String clientFirstName, String clientMiddleName,
			String clientLastName,
			String eventName, String eventType, String eventDate,
			String eventTime, String eventVenue, int eventGuest,
			CategoryPaymentType paymentType) {

		this(client_id, event_id, payment_id, clientFirstName,
				clientMiddleName, clientLastName,
				eventName, eventType, eventDate, eventTime, eventVenue,
				eventGuest, paymentType);
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

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getEventDate() {
		return eventDate;
	}

	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}

	public String getEventTime() {
		return eventTime;
	}

	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}

	public String getEventVenue() {
		return eventVenue;
	}

	public void setEventVenue(String eventVenue) {
		this.eventVenue = eventVenue;
	}

	public int getEventGuest() {
		return eventGuest;
	}

	public void setEventGuest(int eventGuest) {
		this.eventGuest = eventGuest;
	}

	public CategoryPaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(CategoryPaymentType paymentType) {
		this.paymentType = paymentType;
	}
}
