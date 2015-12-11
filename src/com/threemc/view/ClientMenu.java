package com.threemc.view;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.threemc.data.Client;

public class ClientMenu extends Dialog {

	private JPanel panelCont;

	private JButton btnOpenInClient;
	private JButton btnOpenInBooking;

	private Client client;

	public ClientMenu(final Window parent, Dialog.ModalityType modal) {
		super(parent, "", modal);
		set();
		initUI();
		layoutComponents();
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				ClientMenu.this.dispose();
				super.windowClosing(e);
			}
		});

		btnOpenInClient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Bookings booking = new Bookings(ClientMenu.this.getOwner(), getModalityType());
				booking.setInClient(client);
				booking.setVisible(true);
			}
		});

		btnOpenInBooking.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Bookings booking = new Bookings(ClientMenu.this.getOwner(), getModalityType());
				booking.setInBooking(client);
				booking.setVisible(true);
			}
		});
	}

	public void setClient(Client client) {
		this.client = client;
	}

	private void layoutComponents() {
		GridBagConstraints gc = new GridBagConstraints();

		gc.weightx = 1;
		gc.weighty = 1;
		gc.fill = GridBagConstraints.BOTH;

		gc.gridy = 0;
		gc.gridx = 0;

		panelCont.add(btnOpenInClient, gc);

		gc.gridy++;
		gc.gridx = 0;

		panelCont.add(btnOpenInBooking, gc);

		add(panelCont, BorderLayout.CENTER);
	}

	private void initUI() {
		panelCont = new JPanel();
		panelCont.setLayout(new GridBagLayout());
		panelCont.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));

		btnOpenInClient = new JButton("Client Information");
		btnOpenInBooking = new JButton("Show Bookings");
	}

	private void set() {
		setLayout(new BorderLayout());
		setSize(200, 200);
		setResizable(false);
		setLocationRelativeTo(null);
	}
}
