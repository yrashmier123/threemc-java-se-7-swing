package com.threemc.events;

import com.threemc.data.HomeData;

public interface MainFrameCenterPanelEventListener {
	public void invokeEventListener(String msg);
	public void invokeListClickListener();
	public void openBookingDetailsAction(HomeData hd);
}
