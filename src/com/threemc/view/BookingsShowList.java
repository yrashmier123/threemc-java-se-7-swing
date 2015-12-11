package com.threemc.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.table.TableColumnModel;

import com.threemc.controller.ControllerForBookingDetails;
import com.threemc.controller.ControllerForPaymentDetails;
import com.threemc.data.Booking;
import com.threemc.data.Client;
import com.threemc.data.Payment;
import com.threemc.data.Service;
import com.threemc.data.ServicesWanted;

public class BookingsShowList extends Dialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BookingPackagesForServicesTableModel tableModel;
	private CustomTableServices table;

	private PaymentHistoryTableModel tablepModel;
	private JTable tablep;

	private ControllerForBookingDetails controller;
	private ControllerForPaymentDetails controllerp;

	private ArrayList<ServicesWanted> sWantedList;
	private ArrayList<Service> servicesList;

	private int client_id = 0;
	private int event_id = 0;

	private SwingWorker<Void, Void> worker;

	private Client client;
	private Payment pay;
	private Booking book;

	private JTabbedPane tabPane;

	// client
	private JPanel panelClient;

	private JLabel lblid;
	private JLabel lblFirst;
	private JLabel lblMiddle;
	private JLabel lblLast;
	private JLabel lblAddress;
	private JLabel lblGender;
	private JLabel lblContactno;

	//event
	private JPanel panelBook;
	private JPanel panelBookc;

	private JLabel lblEventname;
	private JLabel lblEventVenue;
	private JLabel lblEventDate;
	private JLabel lblEventTime;
	private JLabel lblEventType;
	private JLabel lblEventno;
	private JLabel lblEventDetails;

	//payment
	private JPanel panelPayment;
	private JPanel paneltablep;
	private JPanel panelCost;

	private JLabel lblPackageCost;
	private JLabel lblTotal;
	private JLabel lblBalance;
	private JLabel lblPaid;

	//package
	private JPanel panelPackage;
	private JPanel paneltable;

	private JPanel panelCenter;

	//TODO
	public BookingsShowList(final Window parent , final Dialog.ModalityType modal) {
		super(parent ,"Booking - List" , modal);
		set(parent);
		initUI();
		layoutComponents();
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				BookingsShowList.this.dispose();
			}
		});

	}

	// TODO
	public void setClientAndEventID(int client_id , int event_id) {
		this.client_id = client_id;
		this.event_id = event_id;
	}

	public void setClientDetails() {
		try {
			if(controller.connect().equals("ok")) {
				controller.searchClientById(client_id);
				client = controller.getClient().get(0);
				lblid.setText("Client ID: " + client.getId());
				lblFirst.setText("First name: " + client.getClientFirstName().toUpperCase());
				lblMiddle.setText("Middle name: " + client.getClientMiddleName().toUpperCase());
				lblLast.setText("Last name: " + client.getClientLastName().toUpperCase());
				lblAddress.setText("Address: " + client.getClientAddress().toUpperCase());
				lblGender.setText("Gender: " + client.getClientGender().name().toUpperCase());
				lblContactno.setText("Contact: " + client.getClientContactNumber());
			} else {
				System.out.println(controller.connect());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setBookingDetails() {
		try {
			if(controller.connect().equals("ok")) {
				book = controller.loadBookingRecordsByEventId(event_id);
				lblEventname.setText("Title: "+book.getEventName());
				lblEventVenue.setText("Venue: "+book.getEventVenue());
				lblEventDate.setText("Date: "+book.getEventDate());
				lblEventTime.setText("Time: "+book.getEventTime());
				lblEventType.setText("Type: "+book.getEventType());
				lblEventno.setText("Total Guest: "+book.getEventGuestNumber());
				lblEventDetails.setText("Details: "+book.getEventDetails());
			} else {
				System.out.println(controller.connect());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setPaymentDetails() {
		try {
			if(controller.connect().equals("ok")) {
				if(controller.checkIfServicesHasPayment(client_id, event_id)) {
					pay = controller.loadPaymentRecord(client_id, event_id);
					lblTotal.setText("Total Bill: "+pay.getPaymentTotal());
					lblPaid.setText("Total Paid: "+pay.getPaymentPaid());
					lblBalance.setText("Balance: "+pay.getPaymentBalance());
				}
			} else {
				System.out.println(controller.connect());
			}
			if(controllerp.connect().equals("ok")) {
				controllerp.loadPaymentHistoryByClientEvent(client_id, event_id);
				tablepModel.setData(controllerp.getPaymentHistory());
				tablepModel.fireTableDataChanged();
			} else {
				System.out.println(controllerp.connect());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showData() {
		worker = new SwingWorker<Void, Void>() {
			protected Void doInBackground() throws Exception {
				try {
					if(controller.connect().equals("ok")) {
						controller.loadServicesRecord(client_id, event_id);
					} else {
						System.out.println(controller.connect());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				sWantedList = controller.getServicesWanted();
				servicesList.clear();
				for (int i = 0; i < sWantedList.size(); i++) {
					ServicesWanted sw = sWantedList.get(i);
					Service serv = new Service(sw.getServiceName(),
							sw.getServiceCost(), sw.getServiceDesc(),
							sw.getServiceCat());
					serv.setScId(sw.getScId());
					serv.setServiceCatStat(sw.getServiceCatStat());
					servicesList.add(serv);
				}
				return null;
			}
			protected void done() {
				table.setServiceList(servicesList);
				setPackageCost();
			}
		};
		worker.execute();
	}
	
	public void setPackageCost() {
		int tot = 0;
		if(sWantedList != null) {
			for (int i = 0; i < sWantedList.size(); i++) {
				ServicesWanted sw = sWantedList.get(i);
				tot = tot + sw.getServiceCost();
			}
			lblPackageCost.setText("Package Cost : " + tot);
		}
	}
	
	// TODO
	private void layoutComponents() {
		GridBagConstraints gc = new GridBagConstraints();
		Insets inset = new Insets(5,5,5,5);

		gc.weightx = 1;
		gc.weighty = 0;
		gc.insets = inset;
		gc.fill = GridBagConstraints.HORIZONTAL;

		gc.gridy = 0;
		gc.gridx = 0;

		panelClient.add(lblid, gc);

		gc.gridy++;
		gc.gridx = 0;

		panelClient.add(lblFirst, gc);

		gc.gridy++;
		gc.gridx = 0;

		panelClient.add(lblMiddle, gc);

		gc.gridy++;
		gc.gridx = 0;

		panelClient.add(lblLast, gc);

		gc.gridy++;
		gc.gridx = 0;

		panelClient.add(lblAddress, gc);

		gc.gridy++;
		gc.gridx = 0;

		panelClient.add(lblGender, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.weighty = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.fill = GridBagConstraints.NONE;

		panelClient.add(lblContactno, gc);

		// ----------------

		GridBagConstraints gz = new GridBagConstraints();

		gz.weightx = 1;
		gz.weighty = 0;
		gz.insets = inset;
		gz.fill = GridBagConstraints.HORIZONTAL;

		gz.gridy = 0;
		gz.gridx = 0;

		panelBookc.add(lblEventname, gz);

		gz.gridy++;
		gz.gridx = 0;

		panelBookc.add(lblEventVenue, gz);

		gz.gridy++;
		gz.gridx = 0;

		panelBookc.add(lblEventDate, gz);

		gz.gridy++;
		gz.gridx = 0;

		panelBookc.add(lblEventType, gz);

		gz.gridy++;
		gz.gridx = 0;

		panelBookc.add(lblEventTime, gz);

		gz.gridy++;
		gz.gridx = 0;

		panelBookc.add(lblEventno, gz);

		gz.gridy++;
		gz.gridx = 0;
		gz.weighty = 1;
		gz.anchor = GridBagConstraints.FIRST_LINE_START;
		gz.fill = GridBagConstraints.NONE;
	
		panelBookc.add(lblEventDetails, gz);
		
		panelBook.add(new JScrollPane(panelBookc,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);

		// ----------------

		GridBagConstraints gv = new GridBagConstraints();

		gv.weightx = 1;
		gv.weighty = 0;
		gv.insets = inset;
		gv.fill = GridBagConstraints.HORIZONTAL;

		gv.gridy = 0;
		gv.gridx = 0;

		panelPayment.add(lblTotal, gv);

		gv.gridy++;
		gv.gridx = 0;

		panelPayment.add(lblPaid, gv);

		gv.gridy++;
		gv.gridx = 0;

		panelPayment.add(lblBalance, gv);

		gv.gridy++;
		gv.gridx = 0;
		gv.weighty = 1;
		gv.fill = GridBagConstraints.BOTH;

		paneltablep.add(new JScrollPane(tablep,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);

		panelPayment.add(paneltablep, gv);

		// ----------------

		GridBagConstraints ga = new GridBagConstraints();

		ga.insets = inset;

		ga.gridy = 0;
		ga.gridx = 0;
		ga.weightx = 1;
		ga.weighty = 1;
		ga.fill = GridBagConstraints.BOTH;

		paneltable.add(new JScrollPane(table,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);

		panelPackage.add(paneltable, ga);

		ga.gridy++;
		ga.gridx = 0;
		ga.weighty = 0;
		ga.fill = GridBagConstraints.HORIZONTAL;
		ga.anchor = GridBagConstraints.FIRST_LINE_START;

		panelCost.add(lblPackageCost, BorderLayout.CENTER);

		panelPackage.add(panelCost, ga);

		// ----------------

		panelCenter.add(panelClient, BorderLayout.CENTER);
		
		// ----------------

		add(tabPane, BorderLayout.CENTER);
	}

	// TODO
	private void initUI() {
		controller = new ControllerForBookingDetails();
		controllerp = new ControllerForPaymentDetails();

		servicesList = new ArrayList<Service>();

		Font f = CustomFont.setFont("Tahoma", Font.PLAIN, 15);
		Font fb = CustomFont.setFont("Tahoma", Font.BOLD, 15);

		tableModel = new BookingPackagesForServicesTableModel();
		table = new CustomTableServices(tableModel);

		tablepModel = new PaymentHistoryTableModel();
		tablep = new JTable(tablepModel);

		tablep.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tablep.getTableHeader().setReorderingAllowed(false);
		tablep.getTableHeader().setResizingAllowed(true);
		tablep.getTableHeader().setFont(fb);
		tablep.setFont(f);
		tablep.setRowHeight(20);
		tablep.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		TableColumnModel tcm = tablep.getColumnModel();
		tcm.getColumn(0).setPreferredWidth(200);
		tcm.getColumn(1).setPreferredWidth(150);
		tcm.getColumn(2).setPreferredWidth(450);

		lblid = new JLabel("Client ID: ");
		lblFirst = new JLabel("First name: ");
		lblMiddle = new JLabel("Middle name: ");
		lblLast = new JLabel("Last name: ");
		lblAddress = new JLabel("Address: ");
		lblGender = new JLabel("Gender: ");
		lblContactno = new JLabel("Contact: ");

		lblPackageCost = new JLabel("Package Cost : 0");
		lblTotal = new JLabel("Total Bill : 0");
		lblPaid = new JLabel("Total Paid : 0");
		lblBalance = new JLabel("Balance : 0");

		lblEventname = new JLabel("Event Title: ");
		lblEventVenue = new JLabel("Event Venue: ");
		lblEventDate = new JLabel("Event Date: ");
		lblEventTime = new JLabel("Event Time: ");
		lblEventType = new JLabel("Event Type: ");
		lblEventno = new JLabel("Total Guest:  ");
		lblEventDetails = new JLabel("Details: ");

		lblid.setFont(f);
		lblFirst.setFont(f);
		lblMiddle.setFont(f);
		lblLast.setFont(f);
		lblAddress.setFont(f);
		lblGender.setFont(f);
		lblContactno.setFont(f);

		lblPackageCost.setFont(f);
		lblTotal.setFont(f);
		lblPaid.setFont(f);
		lblBalance.setFont(f);

		lblEventname.setFont(f);
		lblEventVenue.setFont(f);
		lblEventDate.setFont(f);
		lblEventTime.setFont(f);
		lblEventType.setFont(f);
		lblEventno.setFont(f);
		lblEventDetails.setFont(f);

		panelClient = new JPanel();
		panelClient.setBackground(Color.WHITE);
		panelClient.setLayout(new GridBagLayout());
		panelClient.setBorder(BorderFactory.createEtchedBorder());

		panelPayment = new JPanel();
		panelPayment.setBackground(CustomColor.bgColor());
		panelPayment.setLayout(new GridBagLayout());
		panelPayment.setBorder(BorderFactory.createEtchedBorder());

		panelBook = new JPanel();
		panelBook.setLayout(new BorderLayout());
		panelBook.setBackground(CustomColor.bgColor());
		panelBook.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		panelBookc = new JPanel();
		panelBookc.setBackground(Color.WHITE);
		panelBookc.setLayout(new GridBagLayout());
		panelBookc.setBorder(BorderFactory.createEtchedBorder());

		panelPackage = new JPanel();
		panelPackage.setBackground(CustomColor.bgColor());
		panelPackage.setLayout(new GridBagLayout());
		panelPackage.setBorder(BorderFactory.createEtchedBorder());

		panelCenter = new JPanel();
		panelCenter.setLayout(new BorderLayout());
		panelCenter.setBackground(CustomColor.bgColor());
		panelCenter.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		panelCost = new JPanel();
		panelCost.setBackground(Color.WHITE);
		panelCost.setLayout(new BorderLayout());
		panelCost.setBorder(BorderFactory.createEtchedBorder());

		paneltable = new JPanel();
		paneltable.setLayout(new BorderLayout());

		paneltablep = new JPanel();
		paneltablep.setLayout(new BorderLayout());
		paneltablep.setBorder(BorderFactory.createEtchedBorder());

		tabPane = new JTabbedPane();
		tabPane.setFont(fb);
		tabPane.setBackground(CustomColor.bgColor());
		tabPane.add("Client Details", panelCenter);
		tabPane.add("Booking Details", panelBook);
		tabPane.add("Package Details", panelPackage);
		tabPane.add("Payment Details", panelPayment);
		lblPackageCost.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	}

	//TODO
	private void set(final Window parent) {
		setLayout(new BorderLayout());
		setBackground(CustomColor.bgColor());
		setResizable(false);
		setSize(800, 400);
		setLocationRelativeTo(parent);
		Image img = new ImageIcon(getClass().getResource("/res/mcicon.png")).getImage();
		setIconImage(img);
	}
}
