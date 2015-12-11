package com.threemc.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import com.threemc.data.Output;
import com.threemc.data.OutputsUpdate;
import com.threemc.model.DatabaseOutputModule;

public class ControllerForOutputDetails {

	private DatabaseOutputModule db;

	public ControllerForOutputDetails() {
		db = new DatabaseOutputModule();
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

	// Add

	// Add outputs
	public void addOutput(Output out) {
		db.addOutput(out);
	}

	// Get

	public ArrayList<Output> getOutputs() {
		return db.getOutputs();
	}

	public ArrayList<OutputsUpdate> getOutputsUpdates() {
		return db.getOutputUpdates();
	}

	// Save

	public void saveOutputs() throws SQLException {
		db.saveOutputs();
	}

	// Load
	public void loadAllOutputs() throws SQLException {
		db.loadAllOutputs();
	}

	public void loadAllOutputById(String cat, int id) throws SQLException {
		db.loadAllOutputsById(cat, id);
	}

	public void loadAllOutputsUpdate() throws SQLException {
		db.loadAllOutputsUpdate();
	}

	public void loadAllOutputsUpdateById(int output_id) throws SQLException {
		db.loadAllOutputsUpdateById(output_id);
	}

	public int getOutputUpdatesNewCount() {
		return db.getOutputUpdatesNewCount();
	}

	public void updateOutputUpdatesIfNew() throws SQLException {
		db.updateOutputUpdatesIfNew();
	}
}
