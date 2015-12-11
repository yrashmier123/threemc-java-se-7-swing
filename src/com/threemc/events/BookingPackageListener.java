package com.threemc.events;

public interface BookingPackageListener {
	public void saveEventActionOccured(int client_id, int event_id);
	public void backEventActionOccured();
}
