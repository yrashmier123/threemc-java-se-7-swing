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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.threemc.controller.ControllerForBookingDetails;
import com.threemc.controller.ControllerForPackageDetails;
import com.threemc.controller.ControllerForPaymentDetails;
import com.threemc.data.Booking;
import com.threemc.data.Package;
import com.threemc.data.Payment;
import com.threemc.data.Service;
import com.threemc.data.ServiceCategory;
import com.threemc.data.ServiceList;
import com.threemc.data.ServicesWanted;

public class BookingsUpdateServices extends Dialog {
	
	// TODO
	private JLabel lblPackage;
	private JLabel lblServCat;
	private JLabel lblServName;
	private JLabel lblServCost;
	private JLabel lblServRem;
	private JLabel lblList;
	private JLabel lblWanted;
	private JLabel lblStatus;
	private JLabel lblPayment;
	
	private JComboBox<String> cboPackage;
	private JComboBox<String> cboCategory;
	private JComboBox<String> cboService;
	
	private JTextField txtServcost;
	
	private JTextArea txtArea;
	
	private JPanel panelCenter;
	private JPanel panelTableSl;
	private JPanel panelTableSw;
	private JPanel panelStatus;
	private JPanel panelPayment;
	private JPanel panelList;
	private JPanel panelWant;
	
	private CustomTableServices tableList;
	private CustomTableServices tableWanted;
	
	private BookingPackagesForServicesTableModel modelList;
	private BookingPackagesForServicesTableModel modelWanted;
	
	private ArrayList<Service> servicesListForTable;
	private ArrayList<ServiceList> serviceList;
	private ArrayList<Package> packageList;
	private ArrayList<ServicesWanted> sWantedList;
	private ArrayList<ServiceCategory> serviceCategory;
	
	private DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss MM/dd/yyyy");
	
	private Booking book;
	private Payment pay;
	private ControllerForBookingDetails controllerb;
	private ControllerForPackageDetails controllerp;
	private ControllerForPaymentDetails controllerpa;
	
	private JButton btnAdd;
	private JButton btnRemove;
	private JButton btnSave;
	private JButton btnReprice;
	private JButton btnShow;
	private JButton btnGetServices;
	
	private int client_id = 0;
	private int event_id = 0;
	private int id = 0;
	private int totalAmount = 0;
	private int totalPaid = 0;
	private int payFlag = 0;
	
	//TODO
	public BookingsUpdateServices(final Window parent, Dialog.ModalityType modal) {
		super(parent, "Booking - Services" , modal);
		set(parent);
		initUI();
		layoutComponents();
		
		add(panelCenter, BorderLayout.CENTER);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				BookingsUpdateServices.this.dispose();
			}
		});
		
		cboCategory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshServiceList();
			}
		});

		cboPackage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				refreshServicesList();
			}
		});
		
		cboService.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String sname = cboService.getSelectedItem().toString();
				StringBuffer ssname = new StringBuffer();
				int scost = 0;
				StringBuffer sdesc = new StringBuffer();
				for (int i = 0; i < serviceList.size(); i++) {
					ServiceList list = serviceList.get(i);
					if (list.getServiceName().equals(sname)) {
						ssname.append(list.getServiceName());
						scost = scost + list.getServiceCost();
						sdesc.append(list.getServiceRemarks());
					}
				}
				txtServcost.setText("" + scost);
				txtArea.setText(sdesc.toString());
				
			}
		});
		
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					boolean b = true;
					for (int i = 0; i < sWantedList.size(); i++) {
						ServicesWanted sss = sWantedList.get(i);
						if (sss.getServiceName().equalsIgnoreCase((String) cboService.getSelectedItem())) {
							b = false;
							break;
						}
					}
					if (b) {
						String pname = cboPackage.getSelectedItem().toString();
						String scat = cboCategory.getSelectedItem().toString();
						String sname = cboService.getSelectedItem().toString();
						int scId = serviceCategory.get(cboCategory.getSelectedIndex()).getId();
						int scost = Integer.parseInt(txtServcost.getText().toString());
						String sdesc = txtArea.getText();
						String scstat = serviceCategory.get(cboCategory.getSelectedIndex()).getServiceCategoryStatus();
						ServicesWanted serv = new ServicesWanted(0,event_id, client_id, pname, scId, sname, sdesc, scost);
						serv.setScId(serviceCategory.get(cboCategory.getSelectedIndex()).getId());
						serv.setServiceCat(scat);
						serv.setServiceCatStat(scstat);

						sWantedList.add(serv);
						refreshServicesWanted();

						setMessage("Service added to Package", 1);
						refreshPayment();
					} else {
						setMessage("You already have the Service in your Package", 0);
					}
				} catch (Exception nfe) {
					nfe.printStackTrace();
					JOptionPane.showMessageDialog(BookingsUpdateServices.this,
							"Please fill up the form properly.",
							"Invalid Inputs ", JOptionPane.ERROR_MESSAGE);
					setMessage("Please fill up the form properly", 0);
					
				}
			}
		});

		btnReprice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int row = tableWanted.getSelectedRow();
					if(row != -1) {
					String pricee = JOptionPane.showInputDialog(BookingsUpdateServices.this,
							"Enter Amount:", "Reprice Service",
							JOptionPane.INFORMATION_MESSAGE);
						if (pricee != null) {
							if(CustomPatternChecker.checkStringNumbersOnly(pricee)) {
								int price = Integer.parseInt(pricee);
								ServicesWanted ss = sWantedList.get(row);
								sWantedList.remove(row);
								String pname = ss.getPackageName();
								String scat = ss.getServiceCat();
								String sname = ss.getServiceName();
								int scId = ss.getScId();
								String scstat = ss.getServiceCatStat();
								String sdesc = ss.getServiceDesc();
								ServicesWanted serv = new ServicesWanted(0,event_id, client_id, pname, scId, sname, sdesc, price);
								serv.setServiceCat(scat);
								serv.setScId(serviceCategory.get(cboCategory.getSelectedIndex()).getId());
								serv.setServiceCatStat(scstat);
								sWantedList.add(serv);
								refreshServicesWanted();
								JOptionPane.showMessageDialog(BookingsUpdateServices.this,
										"Service repriced!", "Services",
										JOptionPane.INFORMATION_MESSAGE);
								setMessage("Service repriced!", 1);
								refreshPayment();
							} else {
								JOptionPane.showMessageDialog(BookingsUpdateServices.this,
										"Invalid input", "Services",
										JOptionPane.ERROR_MESSAGE);
								setMessage("Invalid input", 0);
							}
						}
					} else {
						JOptionPane.showMessageDialog(BookingsUpdateServices.this,
								"Please select a record in Services Wanted", "Services",
								JOptionPane.ERROR_MESSAGE);
						setMessage("Please select a record in Services Wanted", 0);
					}
				} catch (NumberFormatException ee) {
					JOptionPane.showMessageDialog(BookingsUpdateServices.this,
							"Service cost value error!", "Invalid Input ",
							JOptionPane.ERROR_MESSAGE);
					setMessage("Service cost value error!", 0);
				} catch (ArrayIndexOutOfBoundsException aie) {
					JOptionPane
							.showMessageDialog(
									BookingsUpdateServices.this,
									"Please select a service that you want to reprice.",
									"None Selected", JOptionPane.ERROR_MESSAGE);
					setMessage("Please select a service that you want to reprice.", 0);
				} catch (Exception eee) {
					JOptionPane
							.showMessageDialog(
									BookingsUpdateServices.this,
									"Please select a Package first then the service that you want to reprice or remove \nfrom the list of services.",
									"None Selected", JOptionPane.ERROR_MESSAGE);
					setMessage("Please select a Package first.", 0);
				}
			}
		});
		
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = tableWanted.getSelectedRow();
				if(row != -1) {
					sWantedList.remove(row);
					refreshServicesWanted();
					setMessage("Service removed from Event Package", 1);
					refreshPayment();
				}
			}
		});
		
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(totalAmount >= totalPaid) {
						int yesno = JOptionPane.showConfirmDialog(BookingsUpdateServices.this, "Are you sure you want to save these Services?", "Bookings", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
						if(yesno == JOptionPane.YES_OPTION) {
							if(controllerb.connect().equals("ok")) {
								refreshServicesWanted();
								refreshPayment();
								if (controllerb.checkifEventHasServices(event_id)) {
									controllerb.deleteEventServices(event_id);
									controllerb.addServicesWanted(sWantedList);
									controllerb.saveServicesWanted();
									if(payFlag == 1) {
										if(controllerpa.connect().equals("ok")) {
											controllerpa.updatePayment(pay);
										} else {
											System.out.println(controllerpa.connect());
										}
									}
									JOptionPane
											.showMessageDialog(
													BookingsUpdateServices.this,
													"Successfully Updated event's services Information!",
													"Success",
													JOptionPane.INFORMATION_MESSAGE);
									setMessage("Successfully Updated event's services Information!", 1);
								} else {
									controllerb.addServicesWanted(sWantedList);
									controllerb.saveServicesWanted();
									JOptionPane
											.showMessageDialog(
													BookingsUpdateServices.this,
													"Successfully saved event's services Information!",
													"Success",
													JOptionPane.INFORMATION_MESSAGE);
									setMessage("Successfully saved event's services Information!", 1);
								}
							} else {
								System.out.println(controllerb.connect());
							}
						}
					} else {
						String msg = "The total Cost/Payment for these Services is lower compared to the initial payment set."
								+ "\nPlease Modify this Event's Services Cost/Payment so that it can amount to or greater \nthan the Initial Payment set."
								+ "\n\nInitial Payment Set: " + totalPaid + "\nTotal Package Cost: " + totalAmount;
						JOptionPane
						.showMessageDialog(
								BookingsUpdateServices.this,
								msg,"Payment Error",
								JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception ee) {
					ee.printStackTrace();
					setMessage("Saving Failed", 0);
				}
			}
		});
		
		btnShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(controllerb.connect().equals("ok")) {
						controllerb.loadServicesRecord(client_id, event_id);
						sWantedList = controllerb.getServicesWanted();
						refreshServicesWanted();
						setMessage("Retrieve complete", 1);
						refreshPayment();
					} else {
						System.out.println(controllerb.connect());
					}
				} catch (Exception e1) {
					e1.printStackTrace();
					setMessage("Retrieve Failed", 0);
				}
				
			}
		});
		
		btnGetServices.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				getServiceWantedFromList();
				String pkName = cboPackage.getSelectedItem().toString();
				setMessage("Services from " + pkName + "  Added to Event Package", 1);
				refreshPayment();
			}
		});
		
	}

	// TODO
	
	public void checkIfHasPayment() {
		if (event_id != 0 && client_id != 0) {
			try {
				if(controllerb.connect().equals("ok")) {
					if (controllerb.checkIfServicesHasPayment(client_id, event_id)) {
						totalPaid = 0;
						pay = controllerb.loadPaymentRecord(client_id, event_id);
						totalPaid = pay.getPaymentPaid();
						setMessage("These Services already have its payment.", 1);
						lblPayment.setText("Payment : " + totalPaid);
						payFlag = 1;
					} else {
						setMessage("No payment set yet.", 0);
					}
				} else {
					System.out.println(controllerb.connect());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setBooking(Booking book) {
		this.book = book;
		client_id = book.getClient_id();
		event_id = book.getId();
	}

	private void refreshPayment() {
		totalAmount = 0;
		for (ServicesWanted s : sWantedList) {
			totalAmount = totalAmount + s.getServiceCost();
		}
		int balance = totalAmount - totalPaid;
		if(pay != null) {
			pay.setPaymentBalance(balance);
			pay.setPaymentTotal(totalAmount);
		}
		lblPayment.setText("Payment : " + totalAmount);
	}

	private void refreshServiceList() {
		try {
			if(controllerb.connect().equals("ok")) {
				controllerb.loadServices();
				serviceList = controllerb.getServiceList();
			} else {
				System.out.println(controllerb.connect());
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		DefaultComboBoxModel<String> e = new DefaultComboBoxModel<String>();
		if (serviceList.size() != 0) {
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
	
	private void refreshServicesList() {
		try {
			int a = cboPackage.getSelectedIndex();
			Package packa = packageList.get(a);
			servicesListForTable = packa.getPackageServices();
			tableList.setServiceList(servicesListForTable);
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}
	
	private void getServiceWantedFromList() {
		ArrayList<ServicesWanted> sWtemp = new ArrayList<ServicesWanted>();

		for (int i = 0; i < sWantedList.size(); i++) {
			ServicesWanted sw = sWantedList.get(i);
			String pname = sw.getPackageName();
			String sname = sw.getServiceName();
			String sdesc = sw.getServiceDesc();
			String scat = sw.getServiceCat();
			String scstat = sw.getServiceCatStat();
			int scId = sw.getScId();
			int price = sw.getServiceCost();
			ServicesWanted serv = new ServicesWanted(0,event_id, client_id, pname, scId, sname, sdesc, price);
			serv.setServiceCatStat(scstat);
			serv.setServiceCat(scat);
			serv.setScId(scId);
			sWtemp.add(serv);
		}

		sWantedList.clear();

		for (int i = 0; i < servicesListForTable.size(); i++) {
			Service s = servicesListForTable.get(i);
			boolean b = true;
			for (int j = 0; j < sWtemp.size(); j++) {
				ServicesWanted sw = sWtemp.get(j);
				if(s.getServiceName().equals(sw.getServiceName())) {
					b = false;
				}
			}
			if(b) {
				String pname = cboPackage.getSelectedItem().toString();
				String sname = s.getServiceName();
				String sdesc = s.getServiceRemarks();
				String scat = s.getServiceCatname();
				String scstat = s.getServiceCatStat();
				int scId = s.getScId();
				int price = s.getServiceCost();
				ServicesWanted serv = new ServicesWanted(0,event_id, client_id, pname, scId, sname, sdesc, price);
				serv.setServiceCatStat(scstat);
				serv.setServiceCat(scat);
				serv.setScId(scId);
				sWtemp.add(serv);
			}
		}
		
		for (int i = 0; i < sWtemp.size(); i++) {
			ServicesWanted sw = sWtemp.get(i);
			String pname = sw.getPackageName();
			String sname = sw.getServiceName();
			String sdesc = sw.getServiceDesc();
			String scat = sw.getServiceCat();
			String scstat = sw.getServiceCatStat();
			int scId = sw.getScId();
			int price = sw.getServiceCost();
			ServicesWanted serv = new ServicesWanted(0,event_id, client_id, pname, scId, sname, sdesc, price);
			serv.setServiceCatStat(scstat);
			serv.setServiceCat(scat);
			serv.setScId(scId);
			sWantedList.add(serv);
		}

		refreshServicesWanted();
	}
	
	private void refreshServicesWanted() {
		try {
			ArrayList<Service> servs = new ArrayList<Service>();
			for (ServicesWanted sw : sWantedList) {
				String serviceName = sw.getServiceName();
				int serviceCost = sw.getServiceCost();
				String serviceRemarks = sw.getServiceDesc();
				String serviceCatName = sw.getServiceCat();
				String scstat = sw.getServiceCatStat();
				Service serv = new Service(serviceName, serviceCost, serviceRemarks, serviceCatName);
				serv.setScId(sw.getScId());
				serv.setServiceCatStat(scstat);
				servs.add(serv);
			}
			tableWanted.setServiceList(servs);
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}
	
	private void setMessage(String msg, int msgtype) {
		lblStatus.setText(msg);
		if (msgtype == 1) {
			panelStatus.setBackground(CustomColor.okColorBackGround());
		} else if (msgtype == 0) {
			panelStatus.setBackground(CustomColor.notOkColorBackGround());
		} else {
			panelStatus.setBackground(CustomColor.bgColor());
		}
	}

	// TODO
	private void layoutComponents() {
		GridBagConstraints gc = new GridBagConstraints();

		Insets inset = new Insets(5,5,5,5);

		gc.weighty = 0;
		gc.insets = inset;

		gc.gridy = 0;
		gc.gridx = 0;
		gc.weightx = 0;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.LINE_END;
		panelCenter.add(lblPackage, gc);

		gc.gridx = 1;
		gc.weightx = 1;
		gc.fill = GridBagConstraints.BOTH;
		gc.anchor = GridBagConstraints.LINE_START;
		panelCenter.add(cboPackage, gc);
		
		gc.gridx = 2;
		gc.weightx = 0;
		panelCenter.add(btnSave, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.LINE_END;
		panelCenter.add(lblServCat, gc);

		gc.gridx = 1;
		gc.weightx = 1;
		gc.fill = GridBagConstraints.BOTH;
		gc.anchor = GridBagConstraints.LINE_START;
		panelCenter.add(cboCategory, gc);
		
		gc.gridx = 2;
		gc.weightx = 0;
		panelCenter.add(btnAdd, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.LINE_END;
		panelCenter.add(lblServName, gc);

		gc.gridx = 1;
		gc.weightx = 1;
		gc.fill = GridBagConstraints.BOTH;
		gc.anchor = GridBagConstraints.LINE_START;
		panelCenter.add(cboService, gc);
		
		gc.gridx = 2;
		gc.weightx = 0;
		panelCenter.add(btnReprice, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.LINE_END;
		panelCenter.add(lblServCost, gc);

		gc.gridx = 1;
		gc.weightx = 1;
		gc.fill = GridBagConstraints.BOTH;
		gc.anchor = GridBagConstraints.LINE_START;
		panelCenter.add(txtServcost, gc);
		
		gc.gridx = 2;
		gc.weightx = 0;
		panelCenter.add(btnRemove, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.weightx = 0;
		gc.gridheight = 2;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		panelCenter.add(lblServRem, gc);

		gc.gridx = 1;
		gc.weightx = 1;
		gc.weighty = 0;
		gc.gridwidth = 1;
		gc.fill = GridBagConstraints.BOTH;
		gc.anchor = GridBagConstraints.LINE_START;
		panelCenter.add(new JScrollPane(txtArea,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), gc);
		
		gc.gridx = 2;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.gridwidth = 1;
		gc.gridheight = 1;
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		panelCenter.add(btnShow, gc);
		
		gc.gridy++;
		gc.gridx = 2;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.gridwidth = 1;
		gc.gridheight = 1;
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		panelCenter.add(btnGetServices, gc);
		
		panelList.add(lblList);
		
		gc.gridy++;
		gc.gridx = 0;
		gc.gridwidth = 3;
		gc.weighty = 0;
		gc.fill = GridBagConstraints.BOTH;
		panelCenter.add(panelList, gc);
		
		gc.gridy++;
		gc.gridx = 0;
		gc.weighty = 1;
		panelTableSl.add(new JScrollPane(tableList,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),
				BorderLayout.CENTER);

		panelCenter.add(panelTableSl, gc);
		
		panelWant.add(lblWanted);
		
		gc.gridy++;
		gc.gridx = 0;
		gc.weighty = 0;
		panelCenter.add(panelWant, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.weighty = 1;
		panelTableSw.add(new JScrollPane(tableWanted,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),
				BorderLayout.CENTER);

		panelCenter.add(panelTableSw, gc);
		
		panelStatus.add(lblStatus);
		
		panelPayment.add(lblPayment);
		
		gc.gridy++;
		gc.gridx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(2,5,2,5);
		panelCenter.add(panelPayment, gc);
		
		gc.gridy++;
		gc.gridx = 0;
		panelCenter.add(panelStatus, gc);
		

//		gc.gridy++;
//		gc.gridx = 0;
//		gc.gridwidth = 2;
//		gc.weighty = 1;
//		gc.fill = GridBagConstraints.BOTH;
//		panelCenter.add(panelTable, gc);
	}

	//TODO
	private void initUI() {
		Font f = CustomFont.setFont("Tahoma", Font.PLAIN, 15);
		Font f2 = CustomFont.setFont("Tahom", Font.BOLD, 15);

		controllerb = new ControllerForBookingDetails();
		controllerp = new ControllerForPackageDetails();

		lblPackage = new JLabel("Package Name: ");
		lblServCat = new JLabel("Category: ");
		lblServCost = new JLabel("Service Cost: ");
		lblServName = new JLabel("Service Name: ");
		lblServRem = new JLabel("Service Remarks: ");
		lblList = new JLabel("Service List of Package Selected");
		lblWanted = new JLabel("Services wanted");
		lblStatus = new JLabel("Status : ");
		lblPayment = new JLabel("Package Cost : 0");

		lblPackage.setFont(f);
		lblServCat.setFont(f);
		lblServCost.setFont(f);
		lblServName.setFont(f);
		lblServRem.setFont(f);
		lblList.setFont(f);
		lblWanted.setFont(f);
		lblPayment.setFont(f);

		txtServcost = new JTextField(20);
		txtArea = new JTextArea();

		txtServcost.setFont(f);
		txtArea.setFont(f);

		panelCenter = new JPanel();
		panelCenter.setLayout(new GridBagLayout());
		panelCenter.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panelCenter.setBackground(CustomColor.bgColor());

		panelTableSl = new JPanel();
		panelTableSl.setLayout(new BorderLayout());

		panelTableSw = new JPanel();
		panelTableSw.setLayout(new BorderLayout());

		panelStatus = new JPanel();
		panelStatus.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelStatus.setBackground(Color.WHITE);
		panelStatus.setBorder(BorderFactory.createEtchedBorder());

		panelPayment = new JPanel();
		panelPayment.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelPayment.setBackground(Color.WHITE);
		panelPayment.setBorder(BorderFactory.createEtchedBorder());

		panelList = new JPanel();
		panelList.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelList.setBackground(Color.WHITE);
		panelList.setBorder(BorderFactory.createEtchedBorder());

		panelWant = new JPanel();
		panelWant.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelWant.setBackground(Color.WHITE);
		panelWant.setBorder(BorderFactory.createEtchedBorder());

		btnSave = new JButton("Save services");
		btnAdd = new JButton("Add to package");
		btnRemove = new JButton("Remove");
		btnReprice = new JButton("Reprice");
		btnShow = new JButton("Retrieve services");
		btnGetServices = new JButton("Get services");

		modelList = new BookingPackagesForServicesTableModel();
		modelWanted = new BookingPackagesForServicesTableModel();

		tableList = new CustomTableServices(modelList);
		tableWanted = new CustomTableServices(modelWanted);

		cboCategory = new JComboBox<String>();
		cboPackage = new JComboBox<String>();
		cboService = new JComboBox<String>();

		cboCategory.setFont(f);
		cboPackage.setFont(f);
		cboService.setFont(f);

		try {
			if(controllerb.connect().equals("ok")) {
				controllerb.loadPackages();
				packageList = controllerb.getPackages();
			} else {
				System.out.println(controllerb.connect());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (packageList.size() != 0) {
			Package	packa = packageList.get(0);
			servicesListForTable = packa.getPackageServices();
			tableList.setServiceList(servicesListForTable);
		}

		DefaultComboBoxModel<String> ee = new DefaultComboBoxModel<String>();
		if (packageList.size() != 0) {
			for (int i = 0; i < packageList.size(); i++) {
				Package pack = packageList.get(i);
				ee.addElement(pack.getPackageName());
			}
		}

		cboPackage.setModel(ee);
		cboPackage.setSelectedIndex(1);
		
		try {
			if(controllerb.connect().equals("ok")) {
				controllerb.loadAllServiceCategory();
				serviceCategory = controllerb.getServiceCategory();
			} else {
				System.out.println(controllerb.connect());
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		DefaultComboBoxModel<String> es = new DefaultComboBoxModel<String>();
		if (serviceCategory.size() != 0) {
			for (int i = 0; i < serviceCategory.size(); i++) {
				ServiceCategory scat = serviceCategory.get(i);
				es.addElement(scat.getServiceCategoryName());
			}
		}

		cboCategory.setModel(es);
		cboCategory.setSelectedIndex(1);
		refreshServiceList();
		sWantedList = new ArrayList<ServicesWanted>();
	}

	//TODO
	private void set(final Window parent) {
		setLayout(new BorderLayout());
		setSize(600, 700);
		setLocationRelativeTo(parent);
		setResizable(false);
		Image img = new ImageIcon(getClass().getResource("/res/mcicon.png")).getImage();
		setIconImage(img);
	}
}
