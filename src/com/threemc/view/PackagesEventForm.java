package com.threemc.view;

import java.util.ArrayList;
import java.util.EventObject;

import com.threemc.data.Service;

public class PackagesEventForm extends EventObject {
	
	private int id;
	private String packageName;
	private String packageDesc;
	private int packageCost;
	private ArrayList<Service> packageService;
	
	public PackagesEventForm(Object source) {
		super(source);
	}
	
	public PackagesEventForm(Object source, String packageName, String packageDesc, int packageCost, ArrayList<Service> packageService) {
		super(source);
		
		this.packageName = packageName;
		this.packageDesc = packageDesc;
		this.packageCost = packageCost;
		this.packageService = packageService;
	}
	
	public PackagesEventForm(Object source, int id, String packageName, String packageDesc, int packageCost, ArrayList<Service> packageService) {
		this(source, packageName,packageDesc, packageCost, packageService);
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getPackageDesc() {
		return packageDesc;
	}

	public void setPackageDesc(String packageDesc) {
		this.packageDesc = packageDesc;
	}

	public int getPackageCost() {
		return packageCost;
	}

	public void setPackageCost(int packageCost) {
		this.packageCost = packageCost;
	}

	public ArrayList<Service> getPackageService() {
		return packageService;
	}

	public void setPackageService(ArrayList<Service> packageService) {
		this.packageService = packageService;
	}

}
