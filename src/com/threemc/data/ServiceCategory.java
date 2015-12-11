package com.threemc.data;

public class ServiceCategory {
	private int id;
	private String serviceCategoryName;
	private String serviceCategoryStatus;

	public ServiceCategory(String serviceCategoryName, String serviceCategoryStatus) {
		this.serviceCategoryName = serviceCategoryName;
		this.serviceCategoryStatus = serviceCategoryStatus;
	}

	public ServiceCategory(int id, String serviceCategoryName, String serviceCategoryStatus) {
		this(serviceCategoryName,serviceCategoryStatus);
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getServiceCategoryName() {
		return serviceCategoryName;
	}

	public void setServiceCategoryName(String serviceCategoryName) {
		this.serviceCategoryName = serviceCategoryName;
	}

	public String getServiceCategoryStatus() {
		return serviceCategoryStatus;
	}

	public void setServiceCategoryStatus(String serviceCategoryStatus) {
		this.serviceCategoryStatus = serviceCategoryStatus;
	}
}
