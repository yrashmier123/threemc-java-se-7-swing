package com.threemc.data;

public class ServicesWanted {

	private int id;
	private int event_id;
	private int client_id;
	private int scId;
	private String packageName;
	private String serviceName;
	private String serviceCat;
	private String serviceDesc;
	private int serviceCost;
	private String serviceCatStat;

	public ServicesWanted(int event_id, int client_id, String packageName,
			String serviceName, String serviceCat, String serviceDesc, int serviceCost) {
		this.event_id = event_id;
		this.client_id = client_id;
		this.packageName = packageName;
		this.serviceName = serviceName;
		this.serviceCat = serviceCat;
		this.serviceDesc = serviceDesc;
		this.serviceCost = serviceCost;
	}

	public ServicesWanted(int id , int event_id, int client_id, String packageName,
			String serviceName,String serviceCat, String serviceDesc, int serviceCost ) {
		this(event_id,client_id,packageName,serviceName, serviceCat, serviceDesc,serviceCost);
		this.id = id;
	}

	public ServicesWanted(int event_id, int client_id, String packageName,
			int scId, String serviceName,  String serviceDesc, int serviceCost) {

		this.event_id = event_id;
		this.client_id = client_id;
		this.packageName = packageName;
		this.scId = scId;
		this.serviceName = serviceName;
		this.serviceDesc = serviceDesc;
		this.serviceCost = serviceCost;
	}

	public ServicesWanted(int id , int event_id, int client_id, String packageName,
			int scId, String serviceName, String serviceDesc, int serviceCost ) {
		this(event_id,client_id,packageName,scId, serviceName, serviceDesc,serviceCost);
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getScId() {
		return scId;
	}

	public void setScId(int scId) {
		this.scId = scId;
	}

	public String getServiceCat() {
		return serviceCat;
	}

	public void setServiceCat(String serviceCat) {
		this.serviceCat = serviceCat;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceDesc() {
		return serviceDesc;
	}

	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}

	public int getServiceCost() {
		return serviceCost;
	}

	public void setServiceCost(int serviceCost) {
		this.serviceCost = serviceCost;
	}

	public String getServiceCatStat() {
		return serviceCatStat;
	}

	public void setServiceCatStat(String serviceCatStat) {
		this.serviceCatStat = serviceCatStat;
	}
}
