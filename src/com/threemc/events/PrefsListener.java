package com.threemc.events;

public interface PrefsListener {
	public void preferenceSet(String ip, String dbName, String username, String password, int port);
}
