package com.threemc.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.sf.dynamicreports.report.exception.DRException;

import com.threemc.controller.ControllerForBookingDetails;
import com.threemc.controller.ControllerForPaymentDetails;
import com.threemc.data.Booking;
import com.threemc.data.Client;
import com.threemc.data.PaymentHistory;
import com.threemc.data.ServicesWanted;

public class Reports extends Dialog {

	private JPanel panelCenter;
	private JPanel panelClient;
	private JPanel panelEvent;
	private JPanel panelButton;
	private JPanel panelSearch;

	private JButton btnClientReport;
	private JButton btnBookDetailsReport;
	private JButton btnShowDetails;
	private JButton btnClientDetails;

	private JLabel lblTitle;
	private JLabel lblSearch;

	private JTextField txtSearch;

	private ClientTableModel modelClient;
	private CustomTableClient tableClient;

	private BookingEventInfoTableModel modelEvent;
	private CustomTableEvent tableEvent;

	private ControllerForBookingDetails controllerb;
	private ControllerForPaymentDetails controllerp;

	private ArrayList<Client> clientList;
	private ArrayList<Booking> bookingList;
	private ArrayList<ServicesWanted> dbServicesWanted;
	private ArrayList<PaymentHistory> dbPaymentHist;

	private JComboBox<String> cboType;

	private String category = "client_firstName";

	private ProgressbarDialog prog;
	
	private ReportCompleteBookingDetails bdr;
	private ReportClientDetails rcd;

	public Reports(final Window parent, final Dialog.ModalityType modal) throws FileNotFoundException , DRException {
		super(parent, "Reports", modal);
		set(parent);
		initUI();
		layoutComponents();
		
		prog = new ProgressbarDialog(Reports.this, ModalityType.APPLICATION_MODAL);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Reports.this.dispose();
				super.windowClosing(e);
			}
		});

		btnClientReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ClientReports cr = new ClientReports(parent);
					cr.createReport();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		btnBookDetailsReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int clientRow = tableClient.getSelectedRow();
				int eventRow =  tableEvent.getSelectedRow();
				if(clientRow != -1) {
					if(eventRow != -1) {
						final Client client = clientList.get(clientRow);
						final Booking book = bookingList.get(eventRow);
						try {
							if(controllerb.connect().equals("ok")) {
								controllerb.loadServicesRecord(client.getId(), book.getId());
								dbServicesWanted = controllerb.getServicesWanted();
								if(controllerp.connect().equals("ok")) {
									controllerp.loadPaymentHistoryByClientEvent(client.getId(), book.getId());
									dbPaymentHist = controllerp.getPaymentHistory();
								} else {
									System.out.println(controllerp.connect());
								}
							} else {
								System.out.println(controllerb.connect());
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
						
						int yesno = JOptionPane.showConfirmDialog(Reports.this, message, "Client - Event Record", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
						if(yesno == JOptionPane.YES_OPTION) {
							prog.setIndeterminate(true);
							prog.setVisible(true);
							
							SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
								protected Void doInBackground() throws Exception {
									bdr = new ReportCompleteBookingDetails();
									return null;
								}
								
								protected void done() {
									try {
										prog.setIndeterminate(false);
										prog.setVisible(false);
										bdr.setClientEvent_id(client.getId(), book.getId());
										bdr.createReport();
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							};
							
							worker.execute();
						}
					}
				}
			}
		});
		
		btnShowDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int row = tableEvent.getSelectedRow();
				if (row != -1) {
					Booking book = bookingList.get(row);
					if (book.isHasServices()) {
						if(book.getEventPaymentStatus().equals("Partial") || 
								book.getEventPaymentStatus().equals("Fully Paid")) {
							if (book.getEventStatus().equals("Open")) {
								BookingsShowList bssl = new BookingsShowList(parent, modal);
								int cid = book.getClient_id();
								int eid = book.getId();
								bssl.setClientAndEventID(cid, eid);
								bssl.showData();
								bssl.setClientDetails();
								bssl.setPaymentDetails();
								bssl.setBookingDetails();
								bssl.setVisible(true);
							} else {
								JOptionPane.showMessageDialog(Reports.this,
										"Event is closed", "Reports",
										JOptionPane.ERROR_MESSAGE);
							}
						} else {
							JOptionPane.showMessageDialog(Reports.this,
									"No Paymet set yet", "Reports",
									JOptionPane.ERROR_MESSAGE);
						}
					} else { 
						JOptionPane.showMessageDialog(Reports.this,
							"Event does not have any services.", "Reports",
							JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		
		btnClientDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					int row = tableClient.getSelectedRow();
					if(row != -1) {
						final Client c = clientList.get(row);
						
						prog.setIndeterminate(true);
						prog.setVisible(true);
						
						SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
							protected Void doInBackground() throws Exception {
								rcd = new ReportClientDetails();
								return null;
							}
							
							@Override
							protected void done() {
								try {
									prog.setIndeterminate(false);
									prog.setVisible(false);
									rcd.setClient_id(c.getId());
									rcd.createReport();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						};
						
						worker.execute();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		cboType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(cboType.getSelectedItem().equals("First name")) {
					category = "client_firstName";
				} else if(cboType.getSelectedItem().equals("Middle name")) {
					category = "client_middleName";
				} else if(cboType.getSelectedItem().equals("Last name")) {
					category = "client_lastName";
				} else if(cboType.getSelectedItem().equals("Address")) {
					category = "client_address";
				} else if(cboType.getSelectedItem().equals("Contact no.")) {
					category = "client_contactNo";
				}
			}
		});

		tableClient.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				setBookingPerClient();
			}
		});

		tableClient.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				setBookingPerClient();
			}
		});
		
		txtSearch.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				search();
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				search();
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				search();
			}
		});
	}
	
	public void search() {
		try {
			if(controllerb.connect().equals("ok")) {
				controllerb.searchClientWithEventAndPayment(category, txtSearch.getText());
				clientList = controllerb.getClient();
				tableClient.setClientList(clientList);
			} else {
				System.out.println(controllerb.connect());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setBookingPerClient() {
		int row = tableClient.getSelectedRow();
		if(clientList != null) {
			final int id = clientList.get(row).getId();
			SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
				protected Void doInBackground() throws Exception {
					try {
						if(controllerb.connect().equals("ok")) {
							controllerb.loadBookingRecordsByClientIdForPrintReports(id);
							bookingList = controllerb.getBooking();
						} else {
							System.out.println(controllerb.connect());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
				
				protected void done() {
					tableEvent.setBookingList(bookingList);
				}
			};
			worker.execute();
		}
	}

	private void initUI() {
		controllerb = new ControllerForBookingDetails();
		controllerp = new ControllerForPaymentDetails();

		Font f = CustomFont.setFontTahomaPlain();

		lblTitle = new JLabel("Generate Reports", SwingConstants.CENTER);
		lblTitle.setFont(CustomFont.setFont("Tahoma", Font.PLAIN, 24));

		lblSearch = new JLabel("Search Client: ");
		lblSearch.setFont(f);

		txtSearch = new JTextField(20);
		txtSearch.setFont(f);

		btnClientReport = new JButton("Client List");
		btnBookDetailsReport = new JButton("Complete Booking Details");
		btnShowDetails = new JButton("Show Details");
		btnClientDetails = new JButton("Client Details");

		panelCenter = new JPanel();
		panelCenter.setLayout(new GridBagLayout());
		panelCenter.setBackground(CustomColor.bgColor());

		panelClient = new JPanel();
		panelClient.setLayout(new BorderLayout());
		panelClient.setBackground(CustomColor.bgColor());
		panelClient.setBorder(BorderFactory.createEtchedBorder());

		panelEvent = new JPanel();
		panelEvent.setLayout(new BorderLayout());
		panelEvent.setBackground(CustomColor.bgColor());
		panelEvent.setBorder(BorderFactory.createEtchedBorder());

		panelButton = new JPanel();
		panelButton.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelButton.setBackground(CustomColor.bgColor());

		panelSearch = new JPanel();
		panelSearch.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelSearch.setBorder(BorderFactory.createEtchedBorder());
		panelSearch.setBackground(CustomColor.bgColor());

		modelClient = new ClientTableModel();
		tableClient = new CustomTableClient(modelClient);

		modelEvent = new BookingEventInfoTableModel();
		tableEvent = new CustomTableEvent(modelEvent);

		try {
			if(controllerb.connect().equals("ok")) {
				controllerb.loadClientWithEventAndPayment();
				clientList = controllerb.getClient();
				tableClient.setClientList(clientList);
			} else {
				System.out.println(controllerb.connect());
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		cboType = new JComboBox<String>();
		cboType.setFont(new Font("Tahoma", Font.PLAIN, 14));
		cboType.setBackground(CustomColor.bgColor());

		DefaultComboBoxModel<String> typemod = new DefaultComboBoxModel<String>();
		typemod.addElement("First name");
		typemod.addElement("Last name");
		typemod.addElement("Middle name");
		typemod.addElement("Address");
		typemod.addElement("Contact no.");

		cboType.setModel(typemod);
	}

	private void layoutComponents() {
		GridBagConstraints gc = new GridBagConstraints();

		Insets inset = new Insets(5,5,5,5);

		gc.weightx = 1;
		gc.insets = inset;

		gc.gridy = 0;
		gc.gridx = 0;
		gc.weighty = 0;
		gc.fill = GridBagConstraints.HORIZONTAL;

		panelSearch.add(lblSearch);
		panelSearch.add(txtSearch);
		panelSearch.add(cboType);

		panelCenter.add(panelSearch, gc);

		gc.weighty = 1;
		gc.fill = GridBagConstraints.BOTH;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;

		gc.gridy++;
		gc.gridx = 0;

		panelClient.add(new JScrollPane(tableClient, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));

		panelCenter.add(panelClient, gc);

		gc.gridy++;
		gc.gridx = 0;

		panelEvent.add(new JScrollPane(tableEvent, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));

		panelCenter.add(panelEvent, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.weighty = 0;
		gc.fill = GridBagConstraints.HORIZONTAL;

		panelButton.add(btnShowDetails);
		panelButton.add(btnClientReport);
		panelButton.add(btnBookDetailsReport);
		panelButton.add(btnClientDetails);

		panelCenter.add(panelButton, gc);

		add(lblTitle, BorderLayout.NORTH);
		add(panelCenter, BorderLayout.CENTER);
	}

	private void set(final Window parent) {
		setSize(800, 700);
		setLayout(new BorderLayout());
		setLocationRelativeTo(parent);
		setResizable(false);
		setBackground(CustomColor.bgColor());
		Image img = new ImageIcon(getClass().getResource("/res/mcicon.png")).getImage();
		setIconImage(img);
	}
}
