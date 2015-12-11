package com.threemc.data;

public class ServiceList {
	private int id;
	private String serviceCategory;
	private int service_catId;
	private String serviceName;
	private int serviceCost;
	private String serviceRemarks;
	private String serviceCatStat;

	public ServiceList( String serviceName, int serviceCost, String serviceRemarks, String serviceCategory) {
		this.serviceCategory = serviceCategory;
		this.serviceName = serviceName;
		this.serviceCost = serviceCost;
		this.serviceRemarks = serviceRemarks;
	}

	public ServiceList(int id , String serviceName, int serviceCost, String serviceRemarks, String serviceCategory) {
		this(serviceName, serviceCost, serviceRemarks , serviceCategory);
		this.id = id;
	}

	public ServiceList( String serviceName, int serviceCost, String serviceRemarks, int service_catId) {
		this.service_catId = service_catId;
		this.serviceName = serviceName;
		this.serviceCost = serviceCost;
		this.serviceRemarks = serviceRemarks;
	}

	public ServiceList(int id , String serviceName, int serviceCost, String serviceRemarks, int service_catId) {
		this(serviceName, serviceCost, serviceRemarks , service_catId);
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getServiceCategory() {
		return serviceCategory;
	}

	public void setServiceCategory(String serviceCategory) {
		this.serviceCategory = serviceCategory;
	}

	public int getService_catId() {
		return service_catId;
	}

	public void setService_catId(int service_catId) {
		this.service_catId = service_catId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public int getServiceCost() {
		return serviceCost;
	}

	public void setServiceCost(int serviceCost) {
		this.serviceCost = serviceCost;
	}

	public String getServiceRemarks() {
		return serviceRemarks;
	}

	public void setServiceRemarks(String serviceRemarks) {
		this.serviceRemarks = serviceRemarks;
	}

	public String getServiceCatStat() {
		return serviceCatStat;
	}

	public void setServiceCatStat(String serviceCatStat) {
		this.serviceCatStat = serviceCatStat;
	}
}
