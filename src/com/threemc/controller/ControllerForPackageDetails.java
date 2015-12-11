package com.threemc.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import com.threemc.data.Package;
import com.threemc.data.Service;
import com.threemc.data.ServiceList;
import com.threemc.model.DatabasePackageModule;
import com.threemc.view.PackagesEventForm;

public class ControllerForPackageDetails {

	private DatabasePackageModule db;

	public ControllerForPackageDetails() {
		db = new DatabasePackageModule();
	}

	public String connect() throws Exception {
		return db.connect();
	}

	public void disconnect() throws Exception {
		db.disconnect();
	}

	public Connection getConnection() {
		return db.getConnection();
	}

	public void addPackage(PackagesEventForm ev) {
		int id = 0;
		if(ev.getId() >= 1) {
			id = ev.getId();
		}
		String packageName = ev.getPackageName();
		String packageDesc = ev.getPackageDesc();
		int packageCost = ev.getPackageCost();
		ArrayList<Service> packageService = ev.getPackageService();

		Package pack;
		if(id >= 1) {
			pack = new Package(id, packageName, packageDesc, packageCost, packageService);
		} else {
			pack = new Package(packageName, packageDesc, packageCost, packageService);
		}
		db.addPackage(pack);
	}

	public void addServiceList(ServiceList serv) {
		db.addServiceList(serv);
	}

	public void addServiceLists(ArrayList<ServiceList> serv) {
		db.addServiceLists(serv);
	}

	public void savePackage() throws SQLException {
		db.savePackages();
	}

	public Package getLastPackage() throws SQLException {
		return db.getLastPackage();
	}

	public void saveServiceList() throws SQLException {
		db.saveServiceList();
	}

	public void updateServiceList() throws SQLException {
		db.updateServiceList();
	}

	public void saveServices(ArrayList<Service> servs, Package pack) throws SQLException {
		db.saveServices(servs, pack);
	}

	public void updateService(Service serv) throws SQLException {
		db.updateService(serv);
	}

	public void loadPackages() throws SQLException {
		db.loadPackages();
	}

	public void loadAllPackageRecordsWithServices() throws SQLException {
		db.loadAllPackageRecordsWithServices();
	}

	public ArrayList<Package> getPackagesWithServices() {
		return db.dbPackageOnly;
	}

	public void searchPackages(String value) throws SQLException {
		db.searchPackages(value);
	}

	public ArrayList<Package> getPackages() {
		return db.getPackages();
	}

	public void loadServiceList() throws SQLException {
		db.loadServicesList();
	}

	public ArrayList<ServiceList> getServiceList() {
		return db.getServicesList();
	}

	public void deletePackages(int package_id) throws SQLException {
		db.deletePackages(package_id);
	}

	public void deleteServiceList(int sl_id) throws SQLException {
		db.deleteServiceList(sl_id);
	}
}
