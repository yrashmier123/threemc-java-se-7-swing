package com.threemc.data;

public class Output {

	private int id;
	private int employee_id;
	private int event_id;
	private int client_id;
	private String outputName;
	private String outputDesc;
	private String outputStat;
	private String eventDate;

	public Output(int employee_id, int event_id, int client_id,
			String outputName, String outputDesc, String outputStat) {
		this.employee_id = employee_id;
		this.event_id = event_id;
		this.client_id = client_id;
		this.outputName = outputName;
		this.outputDesc = outputDesc;
		this.outputStat = outputStat;
	}

	public Output(int id, int employee_id, int event_id, int client_id,
			String outputName, String outputDesc, String outputStat) {
		this(employee_id,event_id,client_id,outputName,outputDesc,outputStat);
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getEmployee_id() {
		return employee_id;
	}

	public void setEmployee_id(int employee_id) {
		this.employee_id = employee_id;
	}

	public int getEvent_id() {
		return event_id;
	}

	public void setEvent_id(int event_id) {
		this.event_id = event_id;
	}

	public int getClient_id() {
		return client_id;
	}

	public void setClient_id(int client_id) {
		this.client_id = client_id;
	}

	public String getOutputName() {
		return outputName;
	}

	public void setOutputName(String outputName) {
		this.outputName = outputName;
	}

	public String getOutputDesc() {
		return outputDesc;
	}

	public void setOutputDesc(String outputDesc) {
		this.outputDesc = outputDesc;
	}

	public String getOutputStat() {
		return outputStat;
	}

	public void setOutputStat(String outputStat) {
		this.outputStat = outputStat;
	}

	public String getEventDate() {
		return eventDate;
	}

	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}
}
