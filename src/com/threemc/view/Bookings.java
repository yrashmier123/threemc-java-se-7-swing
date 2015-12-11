package com.threemc.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.threemc.data.Client;
import com.threemc.events.BookingEventClientListener;
import com.threemc.events.BookingEventListener;
import com.threemc.events.BookingPackageListener;
import com.threemc.events.BookingPaymentListener;

public class Bookings extends Dialog {

	// TODO
	private JLabel lblTitle;

	private BookingClientInfo panelClient;
	private BookingEventInfo panelBooking;
	private BookingPackages panelPackage;
	private BookingPayment panelPayment;

	private JPanel panelTitle;
	private JPanel panelCenter;
	private JPanel panelBookingContainer;
	private JPanel panelBot;

	private JTabbedPane tabPane;

	private CustomIcon ci;

	private Window parent;

	// TODO
	public Bookings(final Window parent, final Dialog.ModalityType modal) {
		super(parent, "Bookings || Three McQueens Eventi Automated System",
				modal);
		set(parent);
		try {
			initUI();
		} catch (Exception e) {
			e.printStackTrace();
		}
		layoutComponents();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Bookings.this.dispose();
				super.windowClosing(e);
			}
		});

		panelClientEventListener();
		panelBookingEventListener();
		panelPackagesEventListener();
		panelPaymentEventListener();
	}

	private void panelPaymentEventListener() {
		panelPayment.setBookingPaymentListener(new BookingPaymentListener() {
			public void saveEventActionOccured() {
				Bookings.this.dispose();
			}

			public void backEventActionOccured() {
				tabPane.setEnabledAt(0, false);
				tabPane.setEnabledAt(1, false);
				tabPane.setEnabledAt(2, true);
				tabPane.setEnabledAt(3, false);
				tabPane.setSelectedIndex(2);
			}
		});
	}

	private void panelPackagesEventListener() {
		panelPackage.setBookingPackageListener(new BookingPackageListener() {

			public void backEventActionOccured() {
				tabPane.setEnabledAt(0, false);
				tabPane.setEnabledAt(1, true);
				tabPane.setEnabledAt(2, false);
				tabPane.setEnabledAt(3, false);
				tabPane.setSelectedIndex(1);
			}

			public void saveEventActionOccured(int client_id, int event_id) {
				panelPayment.setId(client_id, event_id);
				panelPayment.setTableData();
				panelPayment.setClientPaymentRecord();
				tabPane.setEnabledAt(0, false);
				tabPane.setEnabledAt(1, false);
				tabPane.setEnabledAt(2, false);
				tabPane.setEnabledAt(3, true);
				tabPane.setSelectedIndex(3);

			}
		});
	}

	private void panelBookingEventListener() {
		panelBooking.setBookingEventListener(new BookingEventListener() {
			public void saveEventActionOccured(BookingEventForm ev) {
				try {
					String title = ev.getEventName();
					String venue = ev.getEventVenue();
					int guest = ev.getEventGuestNumber();
					String details = ev.getEventDetails();

					panelPackage.set_id(panelBooking.getEvent_id(),
							panelBooking.getClient_id());
					panelPackage.checkIfHasPayment();

					tabPane.setEnabledAt(0, false);
					tabPane.setEnabledAt(1, false);
					tabPane.setEnabledAt(3, false);
					tabPane.setEnabledAt(2, true);
					tabPane.setSelectedIndex(2);
				} catch (NullPointerException e) {
					JOptionPane
							.showMessageDialog(
									Bookings.this,
									"Please select an Entry in the Booking list before proceeding",
									"No booking selected",
									JOptionPane.ERROR_MESSAGE);
				}
			}

			public void backEventActionOccured() {
				panelClient.loadFirstData("c.client_lastName");
				tabPane.setEnabledAt(0, true);
				tabPane.setEnabledAt(1, false);
				tabPane.setEnabledAt(2, false);
				tabPane.setEnabledAt(3, false);
				tabPane.setSelectedIndex(0);
			}
		});
	}

	private void panelClientEventListener() {
		panelClient
				.setBookingEventClientListener(new BookingEventClientListener() {
					public void saveEventActionOccured(BookingClientEventForm ev) {
						try {
							int id = ev.getId();
							String fname = ev.getClientFirstName();
							String mname = ev.getClientMiddleName();
							String lname = ev.getClientLastName();
							String cont = ev.getClientContactNumber();
							String address = ev.getClientAddress();

							panelBooking.setClientText(id + " - " + lname + ", " + fname + " " + mname);
							panelBooking.setClient_id(id);
							panelBooking.setTableData();

							tabPane.setEnabledAt(0, false);
							tabPane.setEnabledAt(1, true);
							tabPane.setEnabledAt(2, false);
							tabPane.setEnabledAt(3, false);
							tabPane.setSelectedIndex(1);
						} catch (NullPointerException e) {
							JOptionPane
									.showMessageDialog(
											Bookings.this,
											"Please select from the list of CLIENTS below before proceeding.",
											"No Client Selected",
											JOptionPane.ERROR_MESSAGE);
						}
					}
				});
	}

	public void setInClient(Client client) {
		panelClient.setTextBoxesforClient(client);
	}

	public void setInBooking(Client client) {
		int id = client.getId();
		String fname = client.getClientFirstName();
		String mname = client.getClientMiddleName();
		String lname = client.getClientLastName();

		panelBooking.setClientText(id + " - " + lname + ", " + fname + " " + mname);
		panelBooking.setClient_id(id);
		panelBooking.setTableData();

		tabPane.setEnabledAt(0, false);
		tabPane.setEnabledAt(1, true);
		tabPane.setEnabledAt(2, false);
		tabPane.setEnabledAt(3, false);
		tabPane.setSelectedIndex(1);
	}

	private void layoutComponents() {

		GridBagConstraints gc = new GridBagConstraints();

		gc.weightx = 1;
		gc.weighty = 0;
		gc.fill = GridBagConstraints.BOTH;

		gc.gridx = 0;
		gc.gridy = 0;
		gc.weighty = 1;

		panelBookingContainer.add(panelBooking, gc);

		panelTitle.add(lblTitle);
		add(panelTitle, BorderLayout.NORTH);

		panelCenter.add(tabPane, BorderLayout.CENTER);
		add(panelCenter, BorderLayout.CENTER);
	}

	// TODO
	private void initUI() {
		panelTitle = new JPanel();
		panelTitle.setLayout(new GridBagLayout());
		panelTitle.setBackground(Color.BLACK);
		panelTitle.setPreferredSize(CustomDimension.setDimensionHeight(100));

		panelClient = new BookingClientInfo(parent);
		panelBooking = new BookingEventInfo();
		panelPackage = new BookingPackages();
		panelPayment = new BookingPayment();

		panelCenter = new JPanel();
		panelCenter.setLayout(new BorderLayout());
		panelCenter.setPreferredSize(getMaximumSize());
		panelCenter.setBorder(BorderFactory.createEtchedBorder());

		panelBookingContainer = new JPanel();
		panelBookingContainer.setLayout(new GridBagLayout());
		panelBookingContainer.setPreferredSize(getMaximumSize());

		tabPane = new JTabbedPane();
		tabPane.add("Client Information", panelClient);
		tabPane.add("Booking Information", panelBookingContainer);
		tabPane.add("Packages and Services Information", panelPackage);
		tabPane.add("Payments", panelPayment);
		tabPane.setFont(new Font(tabPane.getFont().toString(), Font.BOLD, 16));

		tabPane.setEnabledAt(1, false);
		tabPane.setEnabledAt(2, false);
		tabPane.setEnabledAt(3, false);

		panelBot = new JPanel();
		panelBot.setLayout(new FlowLayout(FlowLayout.RIGHT));
		panelBot.setPreferredSize(CustomDimension.setDimensionHeight(40));

		ci = new CustomIcon();

		lblTitle = new JLabel("BOOKING");
		lblTitle.setIcon(ci.createIcon("/res/bookingtop.png"));
	}

	// TODO
	private void set(final Window parent) {
		this.parent = parent;
		setLayout(new BorderLayout());
		setSize(800, 700);
		setLocationRelativeTo(parent);
		setResizable(false);
		Image img = new ImageIcon(getClass().getResource("/res/mcicon.png")).getImage();
		setIconImage(img);
	}
}
