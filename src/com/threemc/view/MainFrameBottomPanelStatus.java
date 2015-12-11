package com.threemc.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import mondrian.test.loader.DBLoader.Table.Controller;

import com.threemc.controller.ControllerForClients;
import com.threemc.events.MainFrameBottomPanelStatusEventListener;

public class MainFrameBottomPanelStatus extends JPanel {
	//Date Started: 06/26/2015

	private JPanel panelBot;
	private JPanel panelUpdates;

	private JLabel lblUser;
	private JLabel lblUpdates;
	private JLabel lblDatabase;

	private Timer timer;

	private MainFrameBottomPanelStatusEventListener listener;

	private ControllerForClients controllerc;

	public MainFrameBottomPanelStatus() {
		setLayout(new BorderLayout());
		setBackground(CustomColor.bgColor());
		initUI();
		layoutComponents();
	}

	public void setMainFrameBottomPanelStatusEventListener(MainFrameBottomPanelStatusEventListener listener) {
		this.listener = listener;
	}

	private void initUI() {
		controllerc = new ControllerForClients();
		Font f = CustomFont.setFont("Tahom", Font.BOLD, 13);

		Dimension dim = new Dimension();
		dim.height = 30;
		setPreferredSize(dim);

		panelBot = new JPanel();
		panelBot.setLayout(new GridBagLayout());
		panelBot.setBackground(CustomColor.bgColor());

		panelUpdates = new JPanel();
		panelUpdates.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelUpdates.setBackground(CustomColor.bgColor());

		lblUpdates = new JLabel("No new updates for Outputs.");
		lblUser = new JLabel("User");
		lblDatabase = new JLabel("");

		lblUpdates.setFont(f);
		lblUser.setFont(f);
		lblDatabase.setFont(f);

		timer = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if(controllerc.connect().equals("ok")) {
						if(controllerc.getConnection() != null) {
							lblDatabase.setText("Database Status: Connected");
						}
					} else {
						lblDatabase.setText(controllerc.connect());
					}
				} catch (Exception e) {
					lblDatabase.setText(e.getMessage());
				}
			}
		});
		timer.start();
	}

	private void layoutComponents() {
		GridBagConstraints gc = new GridBagConstraints();
		Insets inset = new Insets(5,5,5,5);

		gc.weightx = 1;
		gc.weighty = 0;
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.insets = inset;

		gc.gridx = 0;
		gc.gridy = 0;

		panelBot.add(lblUser, gc);

		gc.gridx = 1;

		panelUpdates.add(lblUpdates);

		panelBot.add(panelUpdates, gc);

		gc.gridx = 2;

		panelBot.add(lblDatabase, gc);

		add(panelBot, BorderLayout.CENTER);
	}

	public void setUserLoggedIn(String username) {
		lblUser.setText("Username: " + username);
	}

	public void setUserStatus(String userStatus) {
		
	}

	public void setUpdates(String msg) {
		lblUpdates.setText(msg);
		panelUpdates.setBackground(CustomColor.okColorBackGround());
		panelUpdates.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}

	public void setEastPanelClickedSeeAll() {
		lblUpdates.setText("No new updates for Outputs.");
		panelUpdates.setBackground(CustomColor.bgColor());
		panelUpdates.setBorder(BorderFactory.createEmptyBorder());
	}
}
