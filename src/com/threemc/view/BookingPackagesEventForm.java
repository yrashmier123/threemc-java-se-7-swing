package com.threemc.view;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import com.threemc.data.Service;

public class BookingPackagesEventForm extends EventObject {

	private int id;
	private String packageName;
	private ArrayList<Service> packageServices;

	public BookingPackagesEventForm(Object source) {
		super(source);
	}

	public BookingPackagesEventForm(Object source, String packageName, List<Service> packageServices) {
		super(source);
		
		this.packageName = packageName;
		this.packageServices = (ArrayList<Service>) packageServices;
	}

	public BookingPackagesEventForm(Object source, int id, String packageName, List<Service> packageServices) {
		this(source, packageName, packageServices);
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

	public ArrayList<Service> getPackageServices() {
		return packageServices;
	}

	public void setPackageServices(ArrayList<Service> packageServices) {
		this.packageServices = packageServices;
	}
}
