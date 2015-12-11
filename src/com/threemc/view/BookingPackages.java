package com.threemc.view;

import java.awt.BorderLayout;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.threemc.controller.ControllerForBookingDetails;
import com.threemc.controller.ControllerForLogs;
import com.threemc.controller.ControllerForPaymentDetails;
import com.threemc.data.Log;
import com.threemc.data.Package;
import com.threemc.data.Payment;
import com.threemc.data.Service;
import com.threemc.data.ServiceCategory;
import com.threemc.data.ServiceList;
import com.threemc.data.ServicesWanted;
import com.threemc.events.BookingPackageListener;
import com.threemc.model.DatabaseLogs;

public class BookingPackages extends JPanel {

	private ControllerForBookingDetails controller;
	private ControllerForPaymentDetails controllerp;
	private ControllerForLogs controllerl;

	private BookingPackageListener listener;
	private JComboBox<String> cboPackage;
	private JComboBox<String> cboService;
	private JComboBox<String> cboCategory;

	private JLabel lblPackage;
	private JLabel lblService;
	private JLabel lblPrice;
	private JLabel lblCategory;
	private JLabel lblRemarks;
	private JLabel lblMessage;
	private JLabel lblPayment;

	private JTextArea txtRemarks;
	private JTextField txtPrice;

	private JButton btnAdd;
	private JButton btnRemove;
	private JButton btnReprice;
	private JButton btnRemarks;
	private JButton btnNext;
	private JButton btnBack;
	private JButton btnSave;
//	private JButton btnShow;

	private JPanel panelTable;
	private JPanel panelPack;
	private JPanel panelPay;
	private JPanel panelBut;
	private JPanel panelPayment;

	private BookingPackagesForServicesTableModel model;
	private CustomTableServices table;

	private ArrayList<Service> servicesListForTable;
	private ArrayList<ServiceList> serviceList;
	private ArrayList<Package> packageList;
	private ArrayList<ServicesWanted> sWantedList;
	private ArrayList<ServiceCategory> serviceCategory;
	private DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss MM/dd/yyyy");

	private int totalAmount = 0;
	private int totalPaid = 0;
	private int id = 0;
	private int client_id = 0;
	private int event_id = 0;
	private int check = 0;
	private int payFlag = 0;

	private Color colNotOk = CustomColor.notOkColorBackGround();
	private Color colOk = CustomColor.okColorBackGround();
	private Color col = CustomColor.goldColor();

	private Package packa;
	private Payment pay;

	// TODO
	public BookingPackages() {
		set();
		initUI();
		layoutComponent();
		refreshServicesWantedList();

		servicesListForTable = new ArrayList<Service>();

		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					check = 1;
					boolean b = true;
					refreshServicesWantedList();
					for (int i = 0; i < servicesListForTable.size(); i++) {
						Service sss = servicesListForTable.get(i);
						if (sss.getServiceName().equalsIgnoreCase((String) cboService.getSelectedItem())) {
							b = false;
						}
					}
					if (b) {
						refreshServicesWantedList();
						String scat = cboCategory.getSelectedItem().toString();
						String sname = cboService.getSelectedItem().toString();
						int scost = Integer.parseInt(txtPrice.getText());
						String sdesc = txtRemarks.getText();
						String scstat = serviceCategory.get(cboCategory.getSelectedIndex()).getServiceCategoryStatus();
						int scid = serviceCategory.get(cboCategory.getSelectedIndex()).getId();
						int pkid = packageList.get(cboPackage.getSelectedIndex()).getId();
						Service serv = new Service(sname, scost, sdesc, scat);
						serv.setServiceCatStat(scstat);
						serv.setPkId(pkid);
						serv.setScId(scid);
						
						servicesListForTable.add(serv);
						table.setServiceList(servicesListForTable);
						
						refreshServicesWantedList();
						refresh();
						updateTotalAmount();
						JOptionPane.showMessageDialog(BookingPackages.this,
								"Service added to Package", "Services",
								JOptionPane.INFORMATION_MESSAGE);
						setMessage("Service added to Package", 1);
					} else {
						JOptionPane
								.showMessageDialog(
										BookingPackages.this,
										"You already have the Service in your Package!",
										"Same service record",
										JOptionPane.ERROR_MESSAGE);
						setMessage("Select another service from the List", 0);
					}
				} catch (Exception nfe) {
					nfe.printStackTrace();
					JOptionPane.showMessageDialog(BookingPackages.this,
							"Please fill up the form properly.",
							"Invalid Inputs ", JOptionPane.ERROR_MESSAGE);
					setMessage("Invalid Inputs", 0);
				}
			}
		});

		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					check = 1;
					int row = table.getSelectedRow();
					if(row != -1) {
						refreshServicesWantedList();
						servicesListForTable.remove(row);
						sWantedList.remove(row);
						refresh();
						setMessage("Service removed from package", 1);
						refreshServicesWantedList();
						updateTotalAmount();
					}
				} catch (ArrayIndexOutOfBoundsException e1) {
					JOptionPane.showMessageDialog(BookingPackages.this,
							"Please select a service that you want to remove",
							"None Selected", JOptionPane.ERROR_MESSAGE);
					setMessage("Select a record before removing", 0);
				}
			}
		});

		cboPackage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				check = 1;
				refreshServicesWantedList();
				updateTotalAmount();
			}
		});

		table.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
				tableToTexboxes();
			}
		});

		table.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				super.keyReleased(e);
				tableToTexboxes();
			}
		});

		btnReprice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					String pricee = JOptionPane.showInputDialog(BookingPackages.this, "Amount", "Bookings", JOptionPane.PLAIN_MESSAGE);
					if(!pricee.isEmpty()) {
						if(CustomPatternChecker.checkStringNumbersOnly(pricee)) {
							int row = table.getSelectedRow();
							if(row != -1) {
								refreshServicesWantedList();
								check = 1;
								Service s = servicesListForTable.get(row);
								servicesListForTable.remove(row);
								String scat = s.getServiceCatname();
								String sname = s.getServiceName();
								int scost = Integer.parseInt(pricee);
								String sdesc = s.getServiceRemarks();
								String scstat = s.getServiceCatStat();
								int scid = s.getScId();
								int pkid = s.getPkId();
								Service serv = new Service(sname, scost, sdesc, scat);
								serv.setServiceCatStat(scstat);
								serv.setScId(scid);
								serv.setPkId(pkid);
								servicesListForTable.add(serv);
								refresh();
								sWantedList.remove(row);
								JOptionPane.showMessageDialog(BookingPackages.this,
										"Service repriced", "Services",
										JOptionPane.INFORMATION_MESSAGE);
								setMessage("Service repriced", 1);
								refreshServicesWantedList();
								updateTotalAmount();
							} else {
								JOptionPane
								.showMessageDialog(
										BookingPackages.this,
										"Please select a service that you want to reprice.",
										"None Selected", JOptionPane.ERROR_MESSAGE);
								setMessage("None selected!", 0);
							}
						}
					} else {
//						JOptionPane
//						.showMessageDialog(
//								BookingPackages.this,
//								"Please select a Package first then the service that you want to reprice or remove \nfrom the list of services.",
//								"None Selected", JOptionPane.ERROR_MESSAGE);
//						setMessage("Select a package first", 0);
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(BookingPackages.this,
							"Service cost value error!", "Invalid Input ",
							JOptionPane.ERROR_MESSAGE);
					setMessage("Service cost value error!", 0);
				} catch (ArrayIndexOutOfBoundsException aie) {
					aie.printStackTrace();
				} catch (NullPointerException e) {
//					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		btnRemarks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					String desc = JOptionPane.showInputDialog(BookingPackages.this, "New service details", "Bookings", JOptionPane.PLAIN_MESSAGE);
					if(!desc.isEmpty()) {
						if(CustomPatternChecker.checkStringSomeCharsAllowed(desc)) {
							int row = table.getSelectedRow();
							if(row != -1) {
								refreshServicesWantedList();
								check = 1;
								Service s = servicesListForTable.get(row);
								servicesListForTable.remove(row);
								String scat = s.getServiceCatname();
								String sname = s.getServiceName();
								int scost = s.getServiceCost();
								String scstat = s.getServiceCatStat();
								int scid = s.getScId();
								int pkid = s.getPkId();
								Service serv = new Service(sname, scost, desc, scat);
								serv.setServiceCatStat(scstat);
								serv.setScId(scid);
								serv.setPkId(pkid);
								servicesListForTable.add(serv);
								refresh();
								sWantedList.remove(row);
								JOptionPane.showMessageDialog(BookingPackages.this,
										"Service repriced", "Services",
										JOptionPane.INFORMATION_MESSAGE);
								setMessage("Service repriced", 1);
								refreshServicesWantedList();
								updateTotalAmount();
							} else {
								JOptionPane
								.showMessageDialog(
										BookingPackages.this,
										"Please select a service that you want to reprice.",
										"None Selected", JOptionPane.ERROR_MESSAGE);
								setMessage("None selected!", 0);
							}
						}
					} else {
//						JOptionPane
//						.showMessageDialog(
//								BookingPackages.this,
//								"Please select a Package first then the service that you want to reprice or remove \nfrom the list of services.",
//								"None Selected", JOptionPane.ERROR_MESSAGE);
//						setMessage("Select a package first", 0);
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(BookingPackages.this,
							"Service cost value error!", "Invalid Input ",
							JOptionPane.ERROR_MESSAGE);
					setMessage("Service cost value error!", 0);
				} catch (ArrayIndexOutOfBoundsException aie) {
					aie.printStackTrace();
				} catch (NullPointerException e) {
//					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int yesno = JOptionPane
						.showConfirmDialog(
								BookingPackages.this,
								"Please be sure you have saved your services\nbefore proceeding, Do you want to continue?",
								"Services", JOptionPane.YES_NO_OPTION,
								JOptionPane.INFORMATION_MESSAGE);
				if (yesno == JOptionPane.YES_OPTION) {
					if (listener != null) {
						int c_id = client_id;
						int e_id = event_id;
						listener.saveEventActionOccured(c_id, e_id);
					}
				}
			}
		});

		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				client_id = 0;
				event_id = 0;
				totalAmount = 0;
				totalPaid = 0;
				lblMessage.setText("");
				if (listener != null) {
					listener.backEventActionOccured();
				}
			}
		});

		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					refreshServicesWantedList();
					updateTotalAmount();
					if(totalAmount >= totalPaid) {
						int yesno = JOptionPane.showConfirmDialog(BookingPackages.this, "Are you sure you want to save these Services?", "Bookings", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
						if(yesno == JOptionPane.YES_OPTION) {
							if (check == 1) {
								if(controller.connect().equals("ok")) {
									if (controller.checkifEventHasServices(event_id)) {
										controller.deleteEventServices(event_id);
										controller.addServicesWanted(sWantedList);
										controller.saveServicesWanted();
										JOptionPane
												.showMessageDialog(
														BookingPackages.this,
														"Successfully Updated event's services Information!",
														"Success",
														JOptionPane.INFORMATION_MESSAGE);
										setMessage("Successfully Updated event's services Information!", 1);
										if(payFlag == 1) {
											if(controllerp.connect().equals("ok")) {
												controllerp.updatePayment(pay);
											} else {
												System.out.println(controllerp.connect());
											}
										}
									} else {
										controller.addServicesWanted(sWantedList);
										controller.saveServicesWanted();
										JOptionPane
												.showMessageDialog(
														BookingPackages.this,
														"Successfully saved event's services Information!",
														"Success",
														JOptionPane.INFORMATION_MESSAGE);
										setMessage("Successfully saved event's services Information!", 1);
									}
								} else {
									System.out.println(controller.connect());
								}
							} else {
								JOptionPane
										.showMessageDialog(
												BookingPackages.this,
												"Please select and choose you Package first before Saving",
												"No Package Selected",
												JOptionPane.ERROR_MESSAGE);
							}
						}
					} else {
						String msg = "The total Cost/Payment for these Services is lower compared to the initial payment set."
								+ "\nPlease Modify this Event's Services Cost/Payment so that it can amount to or greater \nthan the Initial Payment set."
								+ "\n\nInitial Payment Set: " + totalPaid + "\nTotal Package Cost: " + totalAmount;
						JOptionPane
						.showMessageDialog(
								BookingPackages.this,
								msg,"Payment Error",
								JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		cboService.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sname = cboService.getSelectedItem().toString();
				StringBuffer ssname = new StringBuffer();
				int scost = 0;
				StringBuffer sdesc = new StringBuffer();
				for (int i = 0; i < serviceList.size(); i++) {
					ServiceList list = serviceList.get(i);
					if (list.getServiceName().equalsIgnoreCase(sname)) {
						ssname.append(list.getServiceName());
						scost = scost + list.getServiceCost();
						sdesc.append(list.getServiceRemarks());
					}
				}
				txtPrice.setText(""+scost);
				txtRemarks.setText(sdesc.toString());
			}
		});

		cboCategory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshServiceList();
			}
		});
	}
	
	private void updateTotalAmount() {
		totalAmount = 0;
		for (ServicesWanted servicesWanted : sWantedList) {
			totalAmount = totalAmount + servicesWanted.getServiceCost();
		}
		int balance = totalAmount - totalPaid;
		if(pay != null) {
			pay.setPaymentBalance(balance);
			pay.setPaymentTotal(totalAmount);
		}
		
		lblPayment.setText("Total: " + totalAmount);
	}

	private void setMessage(String msg, int msgtype) {
		lblMessage.setText(msg);
		if (msgtype == 1) {
			panelPay.setBackground(colOk);
		} else {
			panelPay.setBackground(colNotOk);
		}
	}

	private void tableToTexboxes() {
		for (int i = 0; i < servicesListForTable.size(); i++) {
			Service s = servicesListForTable.get(i);
			cboCategory.setSelectedItem(s.getServiceCatname());
			cboService.setSelectedItem(s.getServiceName());
			txtPrice.setText(""+s.getServiceCost());
			txtRemarks.setText(s.getServiceRemarks());
		}
		check = 1;
	}

	private void refreshServicesWantedList() {
		try {
			int row = cboPackage.getSelectedIndex();
			if(packageList != null) {
				Package packa = packageList.get(row);
				servicesListForTable = packa.getPackageServices();
				table.setServiceList(servicesListForTable);
				sWantedList.clear();
				for (int i = 0; i < servicesListForTable.size(); i++) {
					Service serv = servicesListForTable.get(i);
					ServicesWanted swtd = new ServicesWanted(id, event_id,
							client_id, packa.getPackageName(), serv.getScId() , 
							serv.getServiceName(), serv.getServiceRemarks(),
							serv.getServiceCost());
					swtd.setServiceCat(serv.getServiceCatname());
					swtd.setServiceCatStat(serv.getServiceCatStat());
					sWantedList.add(swtd);
				}
			}
		} catch (Exception e) {
			 e.printStackTrace();
		}
	}

	public void checkIfHasPayment() {
		if (event_id != 0 && client_id != 0) {
			try {
				if(controller.connect().equals("ok")) {
					if (controller.checkIfServicesHasPayment(client_id, event_id)) {
						totalPaid = 0;
						pay = controller.loadPaymentRecord(client_id, event_id);
						totalPaid = pay.getPaymentPaid();
						setMessage("These Services already has a payment.", 1);
						payFlag = 1;
					} else {
						setMessage("No payment set yet.", 0);
					}
				} else {
					System.out.println(controller.connect());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public int getTotalAmount() {
		return totalAmount;
	}

	public void set_id(int e_id, int c_id) {
		this.client_id = c_id;
		this.event_id = e_id;
	}

//	private BookingPackagesEventForm getPackagesInformation() {
//		String name = (String) cboPackage.getSelectedItem();
//		BookingPackagesEventForm ev = new BookingPackagesEventForm(this, name,
//				servicesListForTable);
//		return ev;
//	}

	public void setBookingPackageListener(BookingPackageListener listener) {
		this.listener = listener;
	}

	private void refresh() {
		model.fireTableDataChanged();
	}

	private void refreshServiceList() {
		try {
			if(controller.connect().equals("ok")) {
				controller.loadServices();
				serviceList = controller.getServiceList();
			} else {
				System.out.println(controller.connect());
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		DefaultComboBoxModel<String> e = new DefaultComboBoxModel<String>();
		if (serviceList != null) {
			for (int i = 0; i < serviceList.size(); i++) {
				ServiceList slist = serviceList.get(i);
				if(cboCategory.getSelectedItem().equals(slist.getServiceCategory())) {
					e.addElement(slist.getServiceName());
				}
			}
		}
		cboService.setModel(e);
		if(cboService.getItemCount() != 0) {
			cboService.setSelectedIndex(0);
		}
	}

	//TODO
	private void layoutComponent() {
		GridBagConstraints gc = new GridBagConstraints();

		Insets inset = new Insets(5, 5, 5, 5);

		gc.gridy = 0;
		gc.gridx = 0;

		gc.weightx = 0;
		gc.insets = inset;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.fill = GridBagConstraints.NONE;
		panelPack.add(lblPackage, gc);

		gc.gridx = 1;
		gc.gridwidth = 2;
//		gc.weightx = 1;
//		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.anchor = GridBagConstraints.LINE_START;
		panelPack.add(cboPackage, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.gridwidth = 1;
		gc.weightx = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.fill = GridBagConstraints.NONE;
		panelPack.add(lblCategory, gc);

		gc.gridx = 1;
		gc.gridwidth = 2;
//		gc.weightx = 1;
//		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.anchor = GridBagConstraints.LINE_START;
		panelPack.add(cboCategory, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.gridwidth = 1;
		gc.weightx = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.fill = GridBagConstraints.NONE;
		panelPack.add(lblService, gc);

		gc.gridx = 1;
		gc.gridwidth = 2;
//		gc.weightx = 1;
//		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.anchor = GridBagConstraints.LINE_START;
		panelPack.add(cboService, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.gridwidth = 1;
		gc.weightx = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.fill = GridBagConstraints.NONE;
		panelPack.add(lblPrice, gc);

		gc.gridx = 1;
		gc.gridwidth = 2;
//		gc.weightx = 1;
//		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.anchor = GridBagConstraints.LINE_START;
		panelPack.add(txtPrice, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.gridwidth = 1;
		gc.weightx = 0;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		gc.fill = GridBagConstraints.NONE;
		panelPack.add(lblRemarks, gc);

		gc.gridx = 1;
		gc.gridwidth = 2;
		gc.weightx = 1;
		gc.fill = GridBagConstraints.BOTH;
		gc.anchor = GridBagConstraints.LINE_START;
		panelPack.add(new JScrollPane(txtRemarks,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.gridwidth = 1;
		gc.gridwidth = 3;

		panelBut.add(btnAdd);
		panelBut.add(btnRemove);
		panelBut.add(btnReprice);
		panelBut.add(btnRemarks);
		//		panelBut.add(btnShow);

		panelPack.add(panelBut, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.gridwidth = 3;
		gc.weighty = 1;
		panelPack.add(panelTable, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.gridwidth = 3;
		gc.weighty = 0;

		panelPayment.add(lblPayment);

		panelPack.add(panelPayment, gc);

		gc.gridy++;
		gc.gridx = 0;

		panelPay.add(btnBack);
		panelPay.add(btnSave);
		panelPay.add(btnNext);
		panelPay.add(lblMessage);

		panelPack.add(panelPay, gc);

		gc.gridy = 0;
		gc.gridx = 0;
		gc.weighty = 1;
		gc.weightx = 0;

		add(panelPack, gc);
	}

	// TODO
	private void initUI() {
		controller = new ControllerForBookingDetails();
		controllerp = new ControllerForPaymentDetails();
		controllerl = new ControllerForLogs();

		panelTable = new JPanel();
		panelTable.setPreferredSize(CustomDimension.setDimensionWidth(600));
		panelTable.setLayout(new BorderLayout());

		panelPay = new JPanel();
		panelPay.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelPay.setBackground(CustomColor.bgColor());
		panelPay.setBorder(BorderFactory.createEtchedBorder());

		panelBut = new JPanel();
		panelBut.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelBut.setBackground(CustomColor.bgColor());
		
		panelPayment = new JPanel();
		panelPayment.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelPayment.setBackground(Color.WHITE);
		panelPayment.setBorder(BorderFactory.createEtchedBorder());

		panelPack = new JPanel();
		panelPack.setLayout(new GridBagLayout());
		panelPack.setBackground(CustomColor.bgColor());

		model = new BookingPackagesForServicesTableModel();
		table = new CustomTableServices(model);

		Font f = CustomFont.setFont(table.getFont().toString(), Font.PLAIN, 15);
		Font df = CustomFont.setFont(table.getFont().toString(), Font.BOLD, 15);

		panelTable.add(new JScrollPane(table,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),
				BorderLayout.CENTER);

		cboPackage = new JComboBox<String>();
		cboPackage.setFont(df);

		cboService = new JComboBox<String>();
		cboService.setFont(df);

		cboCategory = new JComboBox<String>();
		cboCategory.setFont(df);

		try {
			if(controller.connect().equals("ok")) {
				controller.loadPackages();
				packageList = controller.getPackages();
			} else {
				System.out.println(controller.connect());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (packageList !=null) {
			packa = packageList.get(0);
			servicesListForTable = packa.getPackageServices();
			table.setServiceList(servicesListForTable);
		}

		sWantedList = new ArrayList<ServicesWanted>();

		if (servicesListForTable != null && servicesListForTable.size() != 0) {
			for (int i = 0; i < servicesListForTable.size(); i++) {
				int e_id = event_id;
				int c_id = client_id;
				Service serv = servicesListForTable.get(i);
				ServicesWanted swtd = new ServicesWanted(id, e_id, c_id,
						packa.getPackageName(), serv.getServiceName(), serv.getServiceCatname(),
						serv.getServiceRemarks(), serv.getServiceCost());
				String scstat = serv.getServiceCatStat();
				int scid = serv.getScId();
				swtd.setServiceCatStat(scstat);
				swtd.setScId(scid);
				sWantedList.add(swtd);
			}
		}

		DefaultComboBoxModel<String> ee = new DefaultComboBoxModel<String>();
		if (packageList !=null) {
			for (int i = 0; i < packageList.size(); i++) {
				Package pack = packageList.get(i);
				ee.addElement(pack.getPackageName());
			}
		}

		cboPackage.setModel(ee);

		try {
			if(controller.connect().equals("ok")) {
				controller.loadAllServiceCategory();
				serviceCategory = controller.getServiceCategory();
			} else {
				System.out.println(controller.connect());
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		DefaultComboBoxModel<String> es = new DefaultComboBoxModel<String>();
		if (serviceCategory != null) {
			for (int i = 0; i < serviceCategory.size(); i++) {
				ServiceCategory scat = serviceCategory.get(i);
				es.addElement(scat.getServiceCategoryName());
			}
		}

		cboCategory.setModel(es);
		if(cboCategory.getItemCount() != 0) {
			cboCategory.setSelectedIndex(0);
		}

		refreshServiceList();

		lblPackage = new JLabel("Packages name: ");
		lblService = new JLabel("Services List: ");
		lblPrice = new JLabel("Service Cost: ");
		lblRemarks = new JLabel("Service Remarks: ");
		lblCategory = new JLabel("Service Category: ");
		lblMessage = new JLabel("");
		lblPayment = new JLabel("Total: 0");

		lblRemarks.setFont(f);
		lblService.setFont(f);
		lblPackage.setFont(f);
		lblPrice.setFont(f);
		lblCategory.setFont(f);
		lblPayment.setFont(f);

		txtRemarks = new JTextArea(3, 15);
		txtRemarks.setLineWrap(true);
		txtPrice = new JTextField(20);

		txtRemarks.setFont(f);
		txtPrice.setFont(f);

		CustomIcon ci = new CustomIcon();

		btnAdd = new JButton("Add to Package",
				ci.createIcon("/res/add_to_package.png"));
		btnRemove = new JButton("Remove",
				ci.createIcon("/res/remove_from_package.png"));
		btnReprice = new JButton("Reprice", ci.createIcon("/res/reprice.png"));
		btnBack = new JButton("Back", ci.createIcon("/res/back.png"));
		btnNext = new JButton("Proceed", ci.createIcon("/res/next.png"));
		btnSave = new JButton("Save", ci.createIcon("/res/save.png"));
		btnRemarks = new JButton("Edit Remarks", ci.createIcon("/res/reprice.png"));

		btnAdd.setBackground(CustomColor.bgColor());
		btnRemove.setBackground(CustomColor.bgColor());
		btnReprice.setBackground(CustomColor.bgColor());
		btnBack.setBackground(CustomColor.bgColor());
		btnNext.setBackground(CustomColor.bgColor());
		btnSave.setBackground(CustomColor.bgColor());
		btnRemarks.setBackground(CustomColor.bgColor());

//		btnShow = new JButton("Retrieve Event's Services",
//				ci.createIcon("/res/retrieve.png"));

		btnNext.setVerticalTextPosition(AbstractButton.CENTER);
		btnNext.setHorizontalTextPosition(AbstractButton.LEADING);
	}

	// TODO
	private void set() {
		setLayout(new GridBagLayout());
		setPreferredSize(getMaximumSize());
		setBackground(CustomColor.bgColor());
	}
}
