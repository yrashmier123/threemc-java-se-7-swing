package com.threemc.view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.threemc.controller.ControllerForBookingDetails;
import com.threemc.controller.ControllerForPaymentDetails;
import com.threemc.data.Payment;
import com.threemc.data.PaymentHistory;
import com.threemc.data.Service;
import com.threemc.data.ServicesWanted;
import com.threemc.events.BookingPaymentListener;

public class BookingPayment extends JPanel {

	private BookingPaymentListener listener;

	private JPanel panelPay;
	private JPanel panelButton;

	private JButton btnBack;
	private JButton btnSave;

	private JLabel lblPaymentBy;
	private JLabel lblPayment;
	private JLabel lblTotalbill;
	private JLabel lblBal;
	private JLabel lblTotalPaid;

	private JTextField txtPayment;
	private JTextField txtTotalPaid;
	private JTextField txtBal;
	protected JTextField txtTotalBill;
	private JComboBox<String> cboPaymentType;

	private int totalPaid = 0;
	private int totalPaidto = 0;
	private int balance = 0;
	private int totalBill = 0;
	private int client_id;
	private int event_id;
	private int id = 0;

	private ControllerForBookingDetails controller;
	private ControllerForPaymentDetails controllerp;

	private BookingPackagesForServicesTableModel tableModel;
	private ArrayList<ServicesWanted> sWantedList;
	private ArrayList<Service> serviceList;
	private Payment pay;
	private CustomTableServices table;
	private DateFormat format;

	// TODO
	public BookingPayment() {
		set();
		initUI();
		layoutComponent();

		txtPayment.getDocument().addDocumentListener(new DocumentListener() {
			public void removeUpdate(DocumentEvent arg0) {
				if (txtBal.getText().equals("")) {
					txtBal.setText("");
				} else {
					try {
						totalPaidto = Integer.parseInt(txtPayment.getText())- totalPaid;
						txtTotalPaid.setText("" + totalPaidto);
						balance = (totalBill - totalPaid)- Integer.parseInt(txtPayment.getText());
						txtBal.setText("" + balance);
						if(balance == 0) {
							txtBal.setForeground(CustomColor.okColorBackGround());
						} else { 
							txtBal.setForeground(CustomColor.notOkColorBackGround());
						}
					} catch (NumberFormatException npe) {
						txtTotalPaid.setText("" + totalPaid);
						balance = totalBill - totalPaid;
						txtBal.setText("" + balance);
						if(balance == 0) {
							txtBal.setForeground(CustomColor.okColorBackGround());
						} else { 
							txtBal.setForeground(CustomColor.notOkColorBackGround());
						}
					}
				}
			}

			public void insertUpdate(DocumentEvent arg0) {
				try {
					totalPaidto = totalPaid + Integer.parseInt(txtPayment.getText());
					txtTotalPaid.setText("" + totalPaidto);
					balance = (totalBill - totalPaid)- Integer.parseInt(txtPayment.getText());
					txtBal.setText("" + balance);
					if(balance == 0) {
						txtBal.setForeground(CustomColor.okColorBackGround());
					} else { 
						txtBal.setForeground(CustomColor.notOkColorBackGround());
					}
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(BookingPayment.this,
							"Invalid input!", "Error",
							JOptionPane.ERROR_MESSAGE);
					txtTotalPaid.setText("" + totalPaid);
					balance = totalBill - totalPaid;
					txtBal.setText("" + balance);
					if(balance == 0) {
						txtBal.setForeground(CustomColor.okColorBackGround());
					} else { 
						txtBal.setForeground(CustomColor.notOkColorBackGround());
					}
				}
			}

			public void changedUpdate(DocumentEvent arg0) {
				try {
					totalPaidto = totalPaid + Integer.parseInt(txtPayment.getText());
					txtTotalPaid.setText("" + totalPaidto);
					balance = (totalBill - totalPaid) - Integer.parseInt(txtPayment.getText());
					txtBal.setText("" + balance);
					if(balance == 0) {
						txtBal.setForeground(CustomColor.okColorBackGround());
					} else { 
						txtBal.setForeground(CustomColor.notOkColorBackGround());
					}
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(BookingPayment.this,
							"Invalid input!", "Error",
							JOptionPane.ERROR_MESSAGE);
					txtTotalPaid.setText("" + totalPaid);
					balance = totalBill - totalPaid;
					txtBal.setText("" + balance);
					if(balance == 0) {
						txtBal.setForeground(CustomColor.okColorBackGround());
					} else { 
						txtBal.setForeground(CustomColor.notOkColorBackGround());
					}
				}
			}
		});

		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				totalPaid = 0;
				totalPaidto = 0;
				totalBill = 0;
				balance = 0;
				client_id = 0;
				event_id = 0;
				txtPayment.setText("");
				btnSave.setEnabled(true);
				txtPayment.setEnabled(true);
				sWantedList = null;
				if (listener != null) {
					listener.backEventActionOccured();
				}
			}
		});

		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if(Integer.parseInt(txtTotalBill.getText()) > 0) {
						if(controller.connect().equals("ok")) {
							controller.addPaymentRecord(getPaymentInformation());
							controller.savePaymentRecord();
							if(Integer.parseInt(txtBal.getText()) == 0) {
								controller.updateBookingStatusPayment(event_id, "Fully Paid");
							} else {
								controller.updateBookingStatusPayment(event_id, "Partial");
							}
							pay = controller.loadPaymentRecord(client_id, event_id);
							if(controllerp.connect().equals("ok")) {
								controllerp.saveInPaymentHistory(getDetailsForPaymentHistory(pay));
//								JOptionPane.showMessageDialog(BookingPayment.this, pay.getClient_id() + " " + pay.getEvent_id() + " " + pay.getPaymentType(), "Success", JOptionPane.INFORMATION_MESSAGE);
								JOptionPane.showMessageDialog(BookingPayment.this, "Successfully saved Client's Payments", "Success", JOptionPane.INFORMATION_MESSAGE);
								if(listener != null) {
									listener.saveEventActionOccured();
								}
							} else {
								System.out.println(controllerp.connect());
							}
						} else {
							System.out.println(controller.connect());
						}
					} else {
						JOptionPane.showMessageDialog(BookingPayment.this, "Please Specify the services that you want for the event. \nYou don't have any services to save.", "Fail", JOptionPane.ERROR_MESSAGE);
					}
				} catch (NumberFormatException e) {
					txtPayment.setText("");
					JOptionPane.showMessageDialog(BookingPayment.this, "Amount Paid Empty", "Fail", JOptionPane.ERROR_MESSAGE);
				} catch (Exception e) {
					txtPayment.setText("");
					e.printStackTrace();
				}
			}
		});
	}

	// TODO
	private PaymentHistory getDetailsForPaymentHistory(Payment pay) {
		PaymentHistory ev = null;
		int payment_id = pay.getId();
		int client_id = pay.getClient_id();
		int event_id = pay.getEvent_id();
		CategoryPaymentType paymentType = pay.getPaymentType();
		int paymentPaidThisDate = Integer.parseInt(txtPayment.getText());
		String paymentDate = (String) format.format(System
				.currentTimeMillis());

		ev = new PaymentHistory(client_id, event_id, payment_id, paymentType,
				paymentPaidThisDate, paymentDate);
		ev.setPaymentDesc("Paid with the Amount as recorded");
		return ev;
	}

	public void setTableData() {
		try {
			if(controller.connect().equals("ok")) {
				controller.loadServicesRecord(client_id, event_id);
				serviceList.clear();
				for(ServicesWanted sw : controller.getServicesWanted()) {
					int scid = sw.getScId();
					String catname = sw.getServiceCat();
					String sname = sw.getServiceName();
					String sdecs = sw.getServiceDesc();
					int scost = sw.getServiceCost();
					String scstat = sw.getServiceCatStat();
					Service s = new Service(sname, scost, sdecs, catname);
					s.setScId(scid);
					s.setServiceCatStat(scstat);
					serviceList.add(s);
				}
				table.setServiceList(serviceList);
				sWantedList = controller.getServicesWanted();
				for (int i = 0; i < sWantedList.size(); i++) {
					ServicesWanted serv = sWantedList.get(i);
					totalBill = totalBill + serv.getServiceCost();
				}
				txtTotalBill.setText("" + totalBill);
				txtBal.setText(""+balance);
				txtTotalPaid.setText(""+totalPaid);
			} else {
				System.out.println(controller.connect());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setClientPaymentRecord() {
		try {
			if(controller.connect().equals("ok")) {
				if (controller.checkIfServicesHasPayment(client_id, event_id)) {
					pay = controller.loadPaymentRecord(client_id, event_id);
					if(pay != null) {
						totalPaid = pay.getPaymentPaid();
						totalPaidto = pay.getPaymentPaid();
						txtTotalPaid.setText(""+totalPaid);
						balance = pay.getPaymentBalance();
						txtBal.setText(""+balance);
						id = pay.getId();
						if(totalBill == totalPaid || balance >= 0) {
							btnSave.setEnabled(false);
							txtPayment.setEnabled(false);
						}
					}
				}
			} else {
				System.out.println(controller.connect());
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(BookingPayment.this, "No payment set yet.", "Payment", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	public void setId(int client_id, int event_id) {
		this.client_id = client_id;
		this.event_id = event_id;
	}

	private BookingPaymentEventForm getPaymentInformation() {
		BookingPaymentEventForm ev = null;
		int total = Integer.parseInt(txtTotalBill.getText());
		int paid = Integer.parseInt(txtPayment.getText());
		String type = (String) cboPaymentType.getSelectedItem();
		int balance = Integer.parseInt(txtBal.getText());
		String date = format.format(System.currentTimeMillis());

		CategoryPaymentType categ = null;
		if (type.equalsIgnoreCase("Reservation fee")) {
			categ = CategoryPaymentType.reservation;
		} else if (type.equalsIgnoreCase("Booking fee")) {
			categ = CategoryPaymentType.booking;
		} else if (type.equalsIgnoreCase("Pre-preparation fee")) {
			categ = CategoryPaymentType.prepreparation;
		} else if (type.equalsIgnoreCase("final payment")) {
			categ = CategoryPaymentType.finalpayment;
		}

		ev = new BookingPaymentEventForm(this, id, client_id, event_id, total,
				paid, categ, balance, date);

		return ev;
	}

	public void setBookingPaymentListener(BookingPaymentListener listener) {
		this.listener = listener;
	}

	// TODO
	private void layoutComponent() {
		DefaultComboBoxModel<String> modpayType = new DefaultComboBoxModel<String>();
		modpayType.addElement("Reservation fee");
		modpayType.addElement("Booking fee");
		modpayType.addElement("Pre-preparation fee");
		modpayType.addElement("final payment");

		cboPaymentType.setModel(modpayType);

		GridBagConstraints gc = new GridBagConstraints();

		Insets inset = new Insets(5, 5, 5, 5);

		gc.gridx = 0;
		gc.gridy = 0;
		gc.gridwidth = 2;
		gc.weighty = 1;
		gc.weightx = 1;
		gc.fill = GridBagConstraints.BOTH;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = inset;

		panelPay.add(new JScrollPane(table,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), gc);

		gc.gridx = 0;
		gc.gridy++;
		gc.gridwidth = 1;
		gc.weighty = 0;
		gc.weightx = 0;

		panelPay.add(lblTotalbill, gc);

		gc.gridx = 1;

		panelPay.add(txtTotalBill, gc);

		gc.gridx = 0;
		gc.gridy++;

		panelPay.add(lblTotalPaid, gc);

		gc.gridx = 1;
		panelPay.add(txtTotalPaid, gc);

		gc.gridx = 0;
		gc.gridy++;

		panelPay.add(lblBal, gc);

		gc.gridx = 1;
		panelPay.add(txtBal, gc);

		gc.gridx = 0;
		gc.gridy++;

		panelPay.add(lblPayment, gc);

		gc.gridx = 1;
		panelPay.add(txtPayment, gc);

		gc.gridx = 0;
		gc.gridy++;

		panelPay.add(lblPaymentBy, gc);

		gc.gridx = 1;

		panelPay.add(cboPaymentType, gc);

		gc.gridx = 1;
		gc.gridy++;
		gc.insets = new Insets(0, 0, 0, 0);

		panelButton.add(btnBack);
		panelButton.add(btnSave);

		panelPay.add(panelButton, gc);

		gc.gridx = 0;
		gc.gridy = 0;
		gc.weighty = 1;

		add(panelPay, gc);
	}

	// TODO
	private void initUI() {
		Font df = CustomFont.setFont("Rockwell", Font.PLAIN, 20);

		serviceList = new ArrayList<Service>();

		controllerp = new ControllerForPaymentDetails();
		controller = new ControllerForBookingDetails();

		panelPay = new JPanel();
		panelPay.setLayout(new GridBagLayout());
		panelPay.setPreferredSize(CustomDimension.setDimensionWidth(400));
		panelPay.setBackground(CustomColor.bgColor());

		panelButton = new JPanel();
		panelButton.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelButton.setBackground(CustomColor.bgColor());

		lblPaymentBy = new JLabel("Payment by ");
		lblPayment = new JLabel("Amount Paid ");
		lblTotalbill = new JLabel("Total Bill ");
		lblBal = new JLabel("Balance");
		lblTotalPaid = new JLabel("Total Paid");

		Font fbold = CustomFont.setFont("Tahoma", Font.BOLD, 15);

		lblPaymentBy.setFont(df);
		lblPayment.setFont(df);
		lblBal.setFont(df);
		lblTotalPaid.setFont(df);
		lblTotalbill.setFont(df);

		txtPayment = new JTextField(20);
		txtPayment.setFont(df);
		txtPayment.setHorizontalAlignment(JTextField.RIGHT);
		txtPayment.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				super.keyPressed(arg0);
				if (txtPayment.getText().length() >= 10) {
					txtPayment.setText(txtPayment.getText().substring(0, 9));
				}
			}
		});

		txtTotalBill = new JTextField(20);
		txtTotalBill.setFont(df);
		txtTotalBill.setHorizontalAlignment(JTextField.RIGHT);
		txtTotalBill.setEditable(false);
		txtTotalBill.setForeground(CustomColor.okColorBackGround());
		txtTotalBill.setBackground(Color.BLACK);

		txtTotalPaid = new JTextField(20);
		txtTotalPaid.setFont(df);
		txtTotalPaid.setHorizontalAlignment(JTextField.RIGHT);
		txtTotalPaid.setEditable(false);
		txtTotalPaid.setForeground(CustomColor.okColorBackGround()); 
		txtTotalPaid.setBackground(Color.BLACK);

		txtBal = new JTextField(20);
		txtBal.setFont(df);
		txtBal.setHorizontalAlignment(JTextField.RIGHT);
		txtBal.setEditable(false);
		txtBal.setForeground(CustomColor.notOkColorBackGround());
		txtBal.setBackground(Color.BLACK);

		cboPaymentType = new JComboBox<String>();
		cboPaymentType.setFont(fbold);

		tableModel = new BookingPackagesForServicesTableModel();
		table = new CustomTableServices(tableModel);
		
		CustomIcon ci = new CustomIcon();

		btnBack = new JButton("Back", ci.createIcon("/res/back.png"));
		btnSave = new JButton("Save", ci.createIcon("/res/save.png"));
		btnBack.setBackground(CustomColor.bgColor());
		btnSave.setBackground(CustomColor.bgColor());

		format = new SimpleDateFormat("MMMMM/dd/yyyy hh:ss");
	}

	private void set() {
		setLayout(new GridBagLayout());
		setPreferredSize(getMaximumSize());
		setFocusable(true);
		setBackground(CustomColor.bgColor());
	}
}
