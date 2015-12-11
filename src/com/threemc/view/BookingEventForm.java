package com.threemc.view;

import java.util.Date;
import java.util.EventObject;

public class BookingEventForm extends EventObject {

	private int id;
	private int client_id;
	private String eventName;
	private String eventVenue;
	private String eventDetails;
	private int eventGuestNumber;
	private String  eventDate;
	private String eventTime;
	private String eventType;

	public BookingEventForm(Object source) {
		super(source);
	}

	public BookingEventForm(Object source, int client_id , String eventName, String eventVenue, String eventDetails, int eventGuestNumber,
			String  eventDate, String eventTime, String eventType) {
		super(source);

		this.client_id = client_id;
		this.eventName = eventName;
		this.eventVenue = eventVenue;
		this.eventDetails = eventDetails;
		this.eventGuestNumber = eventGuestNumber;
		this.eventDate = eventDate;
		this.eventTime = eventTime;
		this.eventType = eventType;
	}
	
	public BookingEventForm(Object source, int id, int client_id , String eventName, String eventVenue, String eventDetails, int eventGuestNumber,
			String  eventDate, String eventTime, String eventType) { 
		
		this(source, client_id ,eventName,eventVenue,eventDetails,eventGuestNumber,eventDate,eventTime,eventType);
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

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getEventVenue() {
		return eventVenue;
	}

	public void setEventVenue(String eventVenue) {
		this.eventVenue = eventVenue;
	}

	public String getEventDetails() {
		return eventDetails;
	}

	public void setEventDetails(String eventDetails) {
		this.eventDetails = eventDetails;
	}

	public int getEventGuestNumber() {
		return eventGuestNumber;
	}

	public void setEventGuestNumber(int eventGuestNumber) {
		this.eventGuestNumber = eventGuestNumber;
	}

	public String  getEventDate() {
		return eventDate;
	}

	public void setEventDate(String  eventDate) {
		this.eventDate = eventDate;
	}

	public String getEventTime() {
		return eventTime;
	}

	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
}
