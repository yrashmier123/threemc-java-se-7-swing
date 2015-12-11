package com.threemc.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import com.threemc.data.Client;
import com.threemc.model.DatabaseClient;

public class ControllerForClients {

	DatabaseClient db;

	public ControllerForClients() {
		db = new DatabaseClient();
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

	public Client loadClientById(int ids) throws SQLException {
		return	db.loadClientById(ids);
	}

	public void searchClient(String category, String value) throws SQLException {
		db.searchClient(category, value);
	}

	public ArrayList<Client> getClient() {
		return db.getClient();
	}
}
