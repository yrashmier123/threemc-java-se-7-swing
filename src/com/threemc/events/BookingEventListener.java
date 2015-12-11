package com.threemc.events;

import com.threemc.view.BookingEventForm;

public interface BookingEventListener {
	public void saveEventActionOccured(BookingEventForm ev);
	public void backEventActionOccured();
}
