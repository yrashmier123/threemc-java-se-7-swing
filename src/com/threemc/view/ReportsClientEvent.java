package com.threemc.view;

import java.awt.Dialog;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import com.threemc.controller.ControllerForBookingDetails;
import com.threemc.controller.ControllerForPaymentDetails;
import com.threemc.data.Booking;
import com.threemc.data.Client;
import com.threemc.data.PaymentHistory;
import com.threemc.data.ServicesWanted;

public class ReportsClientEvent extends Dialog {

	private JLabel lblClient;
	private JLabel lblEvent;
	private JLabel lblSearch;

	private JTextField txtSearch;

	private JComboBox<String> cboClient;
	private JComboBox<String> cboEvent;

	private JButton btnClear;
	private JButton btnGenerate;
	private CustomIcon ci = new CustomIcon();

	private ArrayList<Client> dbClient;
	private ArrayList<Booking> dbEvent;
	private ArrayList<ServicesWanted> dbServicesWanted;
	private ArrayList<PaymentHistory> dbPaymentHist;
	
	private ControllerForBookingDetails controller;
	private ControllerForPaymentDetails controllerp;
	
	private int id = 0;

	public ReportsClientEvent(final Window parent, Dialog.ModalityType modal) {
		super(parent, "", modal);
		set(parent);
		initUI();
		layoutComponents();
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				ReportsClientEvent.this.dispose();
				super.windowClosing(e);
			}
		});
		cboClient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				yeah();
			}
		});

		btnGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Client client = dbClient.get(cboClient.getSelectedIndex());
				Booking book = dbEvent.get(cboEvent.getSelectedIndex());
				
				try {
					if(controller.connect().equals("ok")) {
						controller.loadServicesRecord(client.getId(), book.getId());
						dbServicesWanted = controller.getServicesWanted();
						if(controllerp.connect().equals("ok")) {
							controllerp.loadPaymentHistoryByClientEvent(client.getId(), book.getId());
							dbPaymentHist = controllerp.getPaymentHistory();
						} else {
							System.out.println(controllerp.connect());
						}
					} else {
						System.out.println(controller.connect());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				StringBuffer smg = new StringBuffer();
				for (ServicesWanted sw : dbServicesWanted) {
					smg.append("Service name: "+sw.getServiceName() + "     price: " + sw.getServiceCost() + "     detail: " + sw.getServiceDesc()+ "\n");
				}
				
				StringBuffer phmg = new StringBuffer();
				for(PaymentHistory ph : dbPaymentHist) {
					phmg.append("Date : " + ph.getPaymentDate() + " ; Paid: " + ph.getPaymentPaidThisDate()+ "\n");
				}
				
				String message = "Client name: " + client.getClientFirstName() + " "
						+ client.getClientMiddleName() + " "
						+ client.getClientLastName() + "\n\n"
						+ "Event name: " + book.getEventName() + "\n"
						+ "Venue : " + book.getEventVenue() + "\n"
						+ "Date : " + book.getEventDate() + "\n"
						+ "Time : " + book.getEventTime() + "\n"
						+ "Type : " + book.getEventType() + "\n"
						+ "No. Of Guests : " + book.getEventGuestNumber() + "\n"
						+ "Details : " + book.getEventDetails() + "\n\n"
						+ "Package and Services Wanted\n\n"
						+ smg.toString()+ "\n\n"
						+ "Payment History \n\n"
						+ phmg.toString()+ "\n"
						+ "Are you sure you want to proceed? ";
				
				int yesno = JOptionPane.showConfirmDialog(ReportsClientEvent.this, message, "Client - Event Record", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if(yesno == JOptionPane.YES_OPTION) {
					try {
						ReportCompleteBookingDetails bdr = new ReportCompleteBookingDetails();
						bdr.setClientEvent_id(client.getId(), book.getId());
						bdr.createReport();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	private void yeah() {
		int row = cboClient.getSelectedIndex();
		if(dbClient != null) {
			id = dbClient.get(row).getId();
			SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
				protected Void doInBackground() throws Exception {
					try {
						if(controller.connect().equals("ok")) {
							//TODO arrange parameter to get the exact data wanted
							controller.loadBookingRecordsByClientIdForPrintReports(id);
							dbEvent = controller.getBooking();
						} else {
							System.out.println(controller.connect());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
				
				protected void done() {
					DefaultComboBoxModel<String> co = new DefaultComboBoxModel<String>();
					for (Booking book : dbEvent) {
						co.addElement(book.getEventName());
					}
					cboEvent.setModel(co);
				}
			};
			worker.execute();
		}
	}

	private void layoutComponents() {
		GridBagConstraints gc = new GridBagConstraints();

		Insets inset = new Insets(5, 5, 5, 5);

		gc.weightx = 1;
		gc.weighty = 0;
		gc.insets = inset;
		gc.fill = GridBagConstraints.HORIZONTAL;

		gc.gridy = 0;
		gc.gridx = 0;

		add(lblSearch, gc);

		gc.gridx = 1;

		add(txtSearch, gc);

		gc.gridy++;
		gc.gridx = 0;

		add(lblClient, gc);

		gc.gridx = 1;

		add(cboClient, gc);

		gc.gridy++;
		gc.gridx = 0;

		add(lblEvent, gc);

		gc.gridx = 1;

		add(cboEvent, gc);

		gc.gridy++;
		gc.gridx = 0;

		add(btnClear, gc);

		gc.gridx = 1;
		gc.weighty = 1;

		add(btnGenerate, gc);
	}

	private void initUI() {
		controllerp = new ControllerForPaymentDetails();
		controller = new ControllerForBookingDetails();
		Font f = CustomFont.setFontRockwellPlain();

		lblClient = new JLabel("Client name ");
		lblEvent = new JLabel("Event name ");
		lblSearch = new JLabel("Search");

		lblClient.setFont(f);
		lblEvent.setFont(f);
		lblSearch.setFont(f);

		txtSearch = new JTextField(20);
		txtSearch.setFont(f);

		cboClient = new JComboBox<String>();
		cboClient.setFont(f);
		cboEvent = new JComboBox<String>();
		cboEvent.setFont(f);

		btnClear = new JButton("Clear", ci.createIcon("/res/clear.png"));
		btnGenerate = new JButton("Generate");

		try {
			if(controller.connect().equals("ok")) {
				controller.loadClientWithEventAndPayment();
				dbClient = controller.getClient();
			} else {
				System.out.println(controller.connect());
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		DefaultComboBoxModel<String> modc = new DefaultComboBoxModel<String>();
		if(dbClient != null) {
			for (Client client : dbClient) {
				modc.addElement(client.getId() + " - "
						+ client.getClientFirstName() + " "
						+ client.getClientMiddleName() + " "
						+ client.getClientLastName());
			}
		}

		cboClient.setModel(modc);
		
		yeah();
	}

	private void set(final Window parent) {
		setLayout(new GridBagLayout());
		setSize(550, 200);
		setLocationRelativeTo(parent);
		setResizable(false);
	}
}
