package com.threemc.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.threemc.data.HomeData;
import com.threemc.events.CenterPanelEastEventListener;
import com.threemc.events.CenterPanelTableEventListener;
import com.threemc.events.MainFrameCenterPanelEventListener;

public class MainFrameCenterPanel extends JPanel {
	//Date Started: 06/26/2015
	private CenterPanelTable panelTable;
	private CenterPanelEast panelFeats;
	private MainFrameCenterPanelEventListener listener;

	public MainFrameCenterPanel() {
		setLayout(new BorderLayout());

		panelTable = new CenterPanelTable();
		panelFeats = new CenterPanelEast();

		add(panelFeats, BorderLayout.EAST);
		add(panelTable, BorderLayout.CENTER);

		panelFeats.setCenterPanelEastEventListener(new CenterPanelEastEventListener() {
			public void invokeCenterPanelEastNewOutputEventListener(String msg) {
				if(listener != null) {
					listener.invokeEventListener(msg);
				}
			}

			public void invokeCenterPanelEastListClickEventListener() {
				if(listener != null) {
					listener.invokeListClickListener();
				}
				
			}
		});

		panelTable.setCenterPanelTableEventListener(new CenterPanelTableEventListener() {
			public void openBookingDetailsAction(HomeData hd) {
				if(listener != null) {
					listener.openBookingDetailsAction(hd);
				}
			}
		});
	}
	
	public void setParent(JFrame parent) {
		panelFeats.setParent(parent);
		panelTable.setParent(parent);
	}

	public void showIfEventisNow() {
		panelTable.showIfEventisNow();
	}

	public void setMainFrameCenterPanelEventListener(MainFrameCenterPanelEventListener listener) {
		this.listener = listener;
	}
}
