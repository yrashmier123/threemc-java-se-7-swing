package com.threemc.data;

import java.util.ArrayList;

/**
 * @author Rashmier Ynawat
 * @since July 4, 2015
 * @version 1.0
 */
public class Package {

	private int id;
	private String packageName;
	private String packageDesc;
	private int packageCost;
	private String hasServices;
	private ArrayList<Service> packageServices;
	private boolean edited = false;

	public Package(String pname, String pdesc, int pcost , ArrayList<Service> pservice) {
		this.packageName = pname;
		this.packageDesc = pdesc;
		this.packageCost = pcost;
		this.packageServices = pservice;
	}

	public Package(int id , String pname, String pdesc, int pcost , ArrayList<Service> pservice) {
		this(pname, pdesc , pcost, pservice);
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

	public ArrayList<Service> getPackageServices() {
		return packageServices;
	}

	public void setPackageServices(ArrayList<Service> packageServices) {
		this.packageServices = packageServices;
	}

	public boolean isEdited() {
		return edited;
	}

	public void setEdited(boolean edited) {
		this.edited = edited;
	}

	public String getHasServices() {
		return hasServices;
	}

	public void setHasEvent(String hasServices) {
		this.hasServices = hasServices;
	}
}
