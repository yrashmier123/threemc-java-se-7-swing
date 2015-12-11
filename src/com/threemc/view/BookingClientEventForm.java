package com.threemc.view;

import java.util.EventObject;

public class BookingClientEventForm extends EventObject {

	private int id;
	private String clientFirstName;
	private String clientMiddleName;
	private String clientLastName;
	private String clientAddress;
	private String clientContactNumber;
	private String clientGender;

	public BookingClientEventForm(Object source) {
		super(source);
	}

	public BookingClientEventForm(Object source, String clientFirstName, String clientMiddleName,
			String clientLastName, String clientAddress,
			String clientContactNumber, String clientGender) {
		super(source);

		this.clientFirstName = clientFirstName;
		this.clientMiddleName = clientMiddleName;
		this.clientLastName = clientLastName;
		this.clientAddress = clientAddress;
		this.clientContactNumber = clientContactNumber;
		this.clientGender = clientGender;
	}

	public BookingClientEventForm(Object source, int id, String clientFirstName, String clientMiddleName,
			String clientLastName, String clientAddress,
			String clientContactNumber, String clientGender) {

		this(source, clientFirstName, clientMiddleName, clientLastName, clientAddress,clientContactNumber,clientGender);
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getClientAddress() {
		return clientAddress;
	}

	public void setClientAddress(String clientAddress) {
		this.clientAddress = clientAddress;
	}

	public String getClientContactNumber() {
		return clientContactNumber;
	}

	public void setClientContactNumber(String clientContactNumber) {
		this.clientContactNumber = clientContactNumber;
	}

	public String getClientGender() {
		return clientGender;
	}

	public void setClientGender(String clientGender) {
		this.clientGender = clientGender;
	}
}
