package com.threemc.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import com.threemc.controller.ControllerForBookingDetails;
import com.threemc.controller.ControllerForPackageDetails;
import com.threemc.data.Package;
import com.threemc.data.Service;
import com.threemc.data.ServiceCategory;
import com.threemc.data.ServiceList;

public class Packages extends Dialog {

	// TODO
	private JPanel panelTitle;

	private JLabel lblTitle;
	private JLabel lblPackageName;
	private JLabel lblPackageCost;
	private JLabel lblPackageDesc;
	private JLabel lblServiceName;
	private JLabel lblServiceCost;
	private JLabel lblServiceDesc;
	private JLabel lblServiceCat;

	private JTextField txtPackageName;
	private JTextField txtPackageCost;
	private JTextField txtPackageDesc;
	private JTextField txtServiceName;
	private JTextField txtServiceCost;
	private JTextField txtServiceDesc;
	private JTextField txtSearch;

	private JButton btnAddPackage;
	private JButton btnDelPackage;
	private JButton btnAddService;
	private JButton btnDelService;
	private JButton btnServiceList;
	private JButton btnCategoryList;
	private JButton btnSaveEdit;

	private JComboBox<String> cboServiceList;
	private JComboBox<String> cboServiceCat;

	private BookingPackagesForServicesTableModel serviceTableModel;
	private PackageTableModel packageTableModel;

	private ArrayList<Package> packageList;
	private ArrayList<Service> serviceListForTable;
	private ArrayList<ServiceList> serviceList;
	private ArrayList<ServiceCategory> serviceCat;

	private JPanel panelTop;
	private JPanel panelCenter;
	private JPanel panelPackageTable;
	private JPanel panelServiceTable;
	private JPanel panelButton;
	private JPanel panelButtona;

	private JTable tablePackage;
	private CustomTableServices tableService;

	private ControllerForPackageDetails controller;
	private ControllerForBookingDetails controllerb;

	private int package_id = 0;
	private int check = 0;
	private int checkSave = 0;
	private int totalPackageCost = 0;
	private Color col = Color.decode("#fd9c9c");
	private Color colo = Color.decode("#ffd700");

	private JPopupMenu popService;
	private JPopupMenu popPackage;
	private JMenuItem mniDel;
	private JMenuItem mniReprice;
	private JMenuItem mniDuplicate;

	private Timer timer;
	private Timer timer2;

	// TODO
	public Packages(final Window parent, final Dialog.ModalityType modal) {
		super(parent, "Packages || Three McQueens Eventi Automated System",
				modal);
		set(parent);
		initUI();
		layoutComponents();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				if (check == 1) {
					int okcancel = JOptionPane
							.showConfirmDialog(
									Packages.this,
									"Are you sure you want to exit? Some data may be lost.\nPlease save your data before leaving",
									"Packages", JOptionPane.YES_NO_OPTION,
									JOptionPane.INFORMATION_MESSAGE);

					if (okcancel == JOptionPane.YES_OPTION) {
						Packages.this.dispose();
					}
				} else {
					Packages.this.dispose();
				}
			}
		});

		btnServiceList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timer.start();
				ServicesLists serv = new ServicesLists(parent, modal);
				serv.setVisible(true);
				timer.stop();
			}
		});

		btnAddService.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					boolean test = true;
					if (serviceListForTable.size() != 0) {
						for (int i = 0; i < serviceListForTable.size(); i++) {
							Service sss = serviceListForTable.get(i);
							if (sss.getServiceName().equalsIgnoreCase(
									txtServiceName.getText())) {
								test = false;
							}
						}
					}
					if (test) {
						String serviceName = txtServiceName.getText();
						String serviceDesc = txtServiceDesc.getText();
						int serviceCost = Integer.parseInt(txtServiceCost
								.getText());
						String serviceCat = cboServiceCat.getSelectedItem()
								.toString();
						Service serv = new Service(serviceName, serviceCost,
								serviceDesc, serviceCat);
						serv.setScId(Packages.this.serviceCat.get(
								cboServiceCat.getSelectedIndex()).getId());
						serviceListForTable.add(serv);
						int tots = 0;
						if (serviceListForTable.size() != 0) {
							for (Service serve : serviceListForTable) {
								tots = tots + serve.getServiceCost();
							}
						}
						totalPackageCost = tots;
						txtPackageCost.setText("" + totalPackageCost);
						btnSaveEdit.setEnabled(true);
						check = 1;
						savePackage();
					} else {
						JOptionPane
								.showMessageDialog(
										Packages.this,
										"You already have the Service in your Package!",
										"Same service record",
										JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception nfe) {
					JOptionPane
							.showMessageDialog(
									Packages.this,
									"Please select a package first then select from the List of Services",
									"Invalid Inputs ",
									JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		btnDelService.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					int row = tableService.getSelectedRow();
					Service serv = serviceListForTable.get(row);
					int serviceCost = serv.getServiceCost();
					totalPackageCost = totalPackageCost - serviceCost;
					txtPackageCost.setText("" + totalPackageCost);
					serviceListForTable.remove(row);
					refreshService();
					check = 1;
				} catch (ArrayIndexOutOfBoundsException aie) {
					JOptionPane.showMessageDialog(Packages.this,
							"Please select a service that you want to remove",
							"None Selected", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		btnDelPackage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int okcancel = JOptionPane.showConfirmDialog(Packages.this,
						"Are you sure you want to delete this Package?",
						"Delete package", JOptionPane.YES_NO_OPTION,
						JOptionPane.INFORMATION_MESSAGE);
				if (okcancel == JOptionPane.YES_OPTION) {
					try {
						if(controller.connect().equals("ok")) {
							controller.deletePackages(package_id);
							controller.loadPackages();
							refreshPackage();
							refreshService();
						} else {
							System.out.println(controller.connect());
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		btnAddPackage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					boolean b = true;
					for (int i = 0; i < packageList.size(); i++) {
						if (txtPackageName.getText().toString().equalsIgnoreCase(packageList.get(i).getPackageName())) {
							b = false;
						}
					}
					if (b) {
						int yesno = JOptionPane.showConfirmDialog(Packages.this,
										"You are about to save these details as a new Package.\nDo you want to proceed?",
										"Packages", JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE);
						if (yesno == JOptionPane.YES_OPTION) {
							checkSave = 1;
							savePackage();
							check = 1;
							checkSave = 0;
							JOptionPane.showMessageDialog(Packages.this,
									"Successfully Saved Package Information",
									"Success", JOptionPane.INFORMATION_MESSAGE);
						}
					} else {
						String pkName = JOptionPane.showInputDialog(Packages.this,"Package name already exists, Please type another one","Packages",JOptionPane.INFORMATION_MESSAGE);
						if (!pkName.isEmpty() && CustomPatternChecker.checkStringSomeCharsAllowed(pkName)) {
							if(!pkName.equalsIgnoreCase(txtPackageName.getText().toString())) {
								txtPackageName.setText(pkName);
								checkSave = 1;
								savePackage();
								check = 1;
								checkSave = 0;
								JOptionPane.showMessageDialog(Packages.this,"Successfully Saved Package Information","Success", JOptionPane.INFORMATION_MESSAGE);
							} else {
								JOptionPane.showMessageDialog(Packages.this,"Package name already exists, Please type another one", "Packages",JOptionPane.ERROR_MESSAGE);
							}
						} else {
							JOptionPane.showMessageDialog(Packages.this,"Invalid Package name", "Packages",JOptionPane.ERROR_MESSAGE);
						}
					}
				} catch (NullPointerException e) {
					JOptionPane
							.showMessageDialog(
									Packages.this,
									"Don't leave empty fields and The only allowed special characters are as follows:\n"
											+ "\n\n ( ) - : @ & ' ! .  !",
									"Error", JOptionPane.ERROR_MESSAGE);
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(Packages.this,
							"Package cost value error!", "Error",
							JOptionPane.ERROR_MESSAGE);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(Packages.this,
							e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		btnSaveEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int yesno = JOptionPane
							.showConfirmDialog(
									Packages.this,
									"You are about to update an existing Package.\nDo you want to proceed?",
									"Packages", JOptionPane.YES_NO_OPTION,
									JOptionPane.INFORMATION_MESSAGE);
					if (yesno == JOptionPane.YES_OPTION) {
						savePackage();
						JOptionPane.showMessageDialog(Packages.this,
								"Successfully Updated Package Information",
								"Success", JOptionPane.INFORMATION_MESSAGE);
						check = 0;
					}
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(Packages.this,
							"Please fill up the form properly.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		btnCategoryList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CategoryServicesList cat = new CategoryServicesList(parent,
						modal);
				cat.setVisible(true);
			}
		});

		refreshPackageList();

		tablePackage.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
				packageTabletoTextboxes();
			};

			public void mousePressed(MouseEvent e) {
				int row = tablePackage.rowAtPoint(e.getPoint());
				tablePackage.getSelectionModel().setSelectionInterval(row, row);
				if (e.getButton() == MouseEvent.BUTTON3) {
					popPackage.show(tablePackage, e.getX(), e.getY());
				}
			}
		});

		tablePackage.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				super.keyReleased(e);
				packageTabletoTextboxes();
			}
		});

		tablePackage.setDefaultRenderer(Object.class,
				new DefaultTableCellRenderer() {
					@Override
					public Component getTableCellRendererComponent(
							JTable table, Object value, boolean isSelected,
							boolean hasFocus, int row, int column) {
						final Component c = super
								.getTableCellRendererComponent(table, value,
										isSelected, hasFocus, row, column);
						Package pack = packageList.get(row);
						if (pack.isEdited()) {
							if (isSelected) {
								c.setBackground(Color.BLACK);
								c.setForeground(colo);
							} else {
								c.setBackground(col);
								c.setForeground(Color.BLACK);
							}
						} else if (!pack.isEdited()) {
							if (isSelected) {
								c.setBackground(Color.BLACK);
								c.setForeground(colo);
							} else {
								c.setBackground(Color.WHITE);
								c.setForeground(Color.BLACK);
							}
						}
						return c;
					}
				});

		tableService.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
				serViceTabletoTextboxes();
			}

			public void mousePressed(MouseEvent e) {
				int row = tableService.rowAtPoint(e.getPoint());
				tableService.getSelectionModel().setSelectionInterval(row, row);
				if (e.getButton() == MouseEvent.BUTTON3) {
					popService.show(tableService, e.getX(), e.getY());
				}
			}
		});

		mniDel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int yesno = JOptionPane
						.showConfirmDialog(
								Packages.this,
								"Are you sure you want to delete this service\nIn your Package?",
								"Package and Services Information",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.INFORMATION_MESSAGE);
				if (yesno == JOptionPane.YES_OPTION) {
					deleteSelectedRows(tableService);
					int tots = 0;
					for (Service service : serviceListForTable) {
						tots = tots + service.getServiceCost();
					}
					txtPackageCost.setText("" + tots);
					try {
						savePackage();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		mniReprice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String pricee = JOptionPane.showInputDialog(Packages.this,
						"Enter Amount:", "Reprice Service",
						JOptionPane.INFORMATION_MESSAGE);
				int price = Integer.parseInt(pricee);

				if (pricee != null && CustomPatternChecker.checkStringNumbersOnly(pricee)) {
					int row = tableService.getSelectedRow();
					Service serv = serviceListForTable.get(row);
					serv.setServiceCost(price);
					serv.setScId(serviceCat.get(
							cboServiceCat.getSelectedIndex()).getId());
					try {
						if(controller.connect().equals("ok")) {
							controller.updateService(serv);
							refreshService();
						} else {
							System.out.println(controller.connect());
						}
					} catch (Exception e1) {

					}
				}
			}
		});

		mniDuplicate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tableService.getRowCount() == 0) {
					timer2.start();
					PackagesCopy pcop = new PackagesCopy(Packages.this,
							ModalityType.APPLICATION_MODAL);
					pcop.setPackage(packageList.get(tablePackage
							.getSelectedRow()));
					pcop.setVisible(true);
					timer2.stop();
				} else {
					JOptionPane.showMessageDialog(Packages.this,
							"Package is not empty", "Package",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		tableService.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				super.keyReleased(e);
				serViceTabletoTextboxes();
			}
		});

		cboServiceList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (cboServiceList.getItemCount() == 0) {

				} else {
					String sname = cboServiceList.getSelectedItem().toString();
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

					txtServiceName.setText(ssname.toString());
					txtServiceCost.setText("" + scost);
					txtServiceDesc.setText(sdesc.toString());
				}
			}
		});

		cboServiceCat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				refreshCboServiceList();
			}
		});

		txtSearch.getDocument().addDocumentListener(new DocumentListener() {
			public void removeUpdate(DocumentEvent arg0) {
				search();
			}

			public void insertUpdate(DocumentEvent arg0) {
				search();
			}

			public void changedUpdate(DocumentEvent arg0) {
				search();
			}
		});
	}

	private void refreshPackageList() {
		try {
			if(controller.connect().equals("ok")) {
				controller.loadPackages();
				setPackageData(controller.getPackages());
				refreshPackage();
				packageList = controller.getPackages();
			} else {
				System.out.println(controller.connect());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void refreshCboServiceList() {
		refreshServiceData();
		if(serviceList != null) {
			System.out.println(serviceList.size());
			DefaultComboBoxModel<String> ee = new DefaultComboBoxModel<String>();
			for (int i = 0; i < serviceList.size(); i++) {
				ServiceList list = serviceList.get(i);
				if (cboServiceCat.getSelectedItem().toString().equalsIgnoreCase(
						list.getServiceCategory())) {
					ee.addElement(list.getServiceName());
				}
			}
			cboServiceList.setModel(ee);
			if (cboServiceList.getItemCount() != 0) {
				cboServiceList.setSelectedIndex(1);
			}
		}
	}

	private void search() {
		try {
			if(controller.connect().equals("ok")) {
				controller.searchPackages(txtSearch.getText().toString());
				setPackageData(controller.getPackages());
				refreshPackage();
			} else {
				System.out.println(controller.connect());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deleteSelectedRows(JTable entryTable) {
		if (entryTable.getRowCount() > 0) {
			if (entryTable.getSelectedRowCount() > 0) {
				int selectedRow[] = entryTable.getSelectedRows();
				for (int i : selectedRow) {
					try {
						serviceListForTable.remove(i);
					} catch (Exception e) {
					}
				}
			}
		}
	}

	public PackagesEventForm getPackagesEventForm() {
		PackagesEventForm ev = null;
		String packageName = txtPackageName.getText();
		String packageDesc = txtPackageDesc.getText();

		int tots = 0;
		for (Service service : serviceListForTable) {
			tots = tots + service.getServiceCost();
		}

		if (checkSave == 1) {
			package_id = 0;
			tots = 0;
		}

		String packageCost = "" + tots;

		if (!packageName.isEmpty()
				&& CustomPatternChecker
						.checkStringSomeCharsAllowed(packageName)) {
			if (!packageDesc.isEmpty()
					&& CustomPatternChecker
							.checkStringSomeCharsAllowed(packageDesc)) {
				if (CustomPatternChecker.checkStringNumbersOnly(packageCost)) {
					ev = new PackagesEventForm(this, package_id, packageName,
							packageDesc, Integer.parseInt(packageCost),
							serviceListForTable);
				}
			}
		}
		return ev;
	}

	public void savePackage() {
		try {
			if(controller.connect().equals("ok")) {
				controller.addPackage(getPackagesEventForm());
				controller.savePackage();
				controller.loadPackages();
				setPackageData(controller.getPackages());
				refreshPackage();
				refreshService();
			} else {
				System.out.println(controller.connect());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void serViceTabletoTextboxes() {
		int row = tableService.getSelectedRow();
		if (row != -1) {
			txtServiceName.setText(tableService.getValueAt(row, 0).toString());
			txtServiceCost.setText(tableService.getValueAt(row, 2).toString());
			txtServiceDesc.setText(tableService.getValueAt(row, 3).toString());
			cboServiceCat.setSelectedItem(tableService.getValueAt(row, 1)
					.toString());
			System.out.println(serviceListForTable.get(row).getScId());
		} else {

		}
	}

	public void packageTabletoTextboxes() {
		int row = tablePackage.getSelectedRow();
		Package pack = packageList.get(row);
		serviceListForTable = pack.getPackageServices();
		if (serviceListForTable == null || serviceListForTable.size() == 0) {
			btnSaveEdit.setEnabled(false);
		} else {
			btnSaveEdit.setEnabled(true);
		}
		
		tableService.setServiceList(serviceListForTable);

		package_id = pack.getId();
		txtServiceName.setText("");
		txtServiceCost.setText("");
		txtServiceDesc.setText("");

		int tots = 0;
		if (serviceListForTable.size() != 0) {
			for (Service serv : serviceListForTable) {
				tots = tots + serv.getServiceCost();
			}
		}

		txtPackageName.setText(pack.getPackageName());
		txtPackageCost.setText("" + tots);
		txtPackageDesc.setText(pack.getPackageDesc());

		totalPackageCost = tots;
	}

	private void refreshServiceData() {
		try {
			if(controller.connect().equals("ok")) {
				controller.loadServiceList();
				serviceList = controller.getServiceList();
			} else {
				System.out.println(controller.connect());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void refreshPackage() {
		packageTableModel.fireTableDataChanged();
	}

	private void setPackageData(ArrayList<Package> db) {
		packageTableModel.setData(db);
	}

	private void refreshService() {
		serviceTableModel.fireTableDataChanged();
	}

	// TODO
	public void layoutComponents() {
		GridBagConstraints gc = new GridBagConstraints();
		GridBagConstraints gc2 = new GridBagConstraints();

		Insets inset = new Insets(5, 5, 5, 5);

		gc.gridy = 0;
		gc.gridx = 0;
		gc.insets = inset;
		gc.fill = GridBagConstraints.BOTH;
		gc.weightx = 1;

		panelTop.add(lblPackageName, gc);

		gc.gridx = 1;

		panelTop.add(txtPackageName, gc);

		gc.gridx = 2;

		panelTop.add(lblServiceName, gc);

		gc.gridx = 3;

		panelTop.add(txtServiceName, gc);

		gc.gridy++;
		gc.gridx = 0;

		panelTop.add(lblPackageCost, gc);

		gc.gridx = 1;

		panelTop.add(txtPackageCost, gc);

		gc.gridx = 2;

		panelTop.add(lblServiceCost, gc);

		gc.gridx = 3;

		panelTop.add(txtServiceCost, gc);

		gc.gridy++;
		gc.gridx = 0;

		panelTop.add(lblPackageDesc, gc);

		gc.gridx = 1;

		panelTop.add(txtPackageDesc, gc);

		gc.gridx = 2;

		panelTop.add(lblServiceDesc, gc);

		gc.gridx = 3;

		panelTop.add(txtServiceDesc, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.gridwidth = 2;

		panelButton.add(btnAddPackage);
		panelButton.add(btnSaveEdit);
		panelButton.add(btnDelPackage);
		// panelButton.add(btnDelService);

		panelTop.add(panelButton, gc);

		gc.gridx = 2;
		gc.gridwidth = 1;

		panelTop.add(lblServiceCat, gc);

		gc.gridx = 3;

		panelTop.add(cboServiceCat, gc);

		gc.gridy++;
		gc.gridx = 2;
		gc.gridwidth = 1;

		panelTop.add(btnAddService, gc);

		gc.gridx = 3;

		panelTop.add(cboServiceList, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.gridwidth = 2;

		panelTop.add(txtSearch, gc);

		gc.gridx = 3;
		gc.weightx = 1;
		// gc.fill = GridBagConstraints.NONE;
		// gc.anchor = GridBagConstraints.LINE_START;

		panelButtona.add(btnServiceList);
		panelButtona.add(btnCategoryList);
		
		panelTop.add(panelButtona, gc);

		gc2.gridy = 0;
		gc2.gridx = 0;
		gc2.insets = inset;
		gc2.fill = GridBagConstraints.BOTH;
		gc2.weightx = 1;
		gc2.weighty = 1;

		panelPackageTable.add(new JScrollPane(tablePackage,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),
				BorderLayout.CENTER);
		panelCenter.add(panelPackageTable, gc2);

		gc2.gridx = 1;

		panelServiceTable.add(new JScrollPane(tableService,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),
				BorderLayout.CENTER);
		panelCenter.add(panelServiceTable, gc2);

		gc.gridy++;
		gc.gridx = 0;
		gc.gridwidth = 4;
		gc.weighty = 1;
		panelTop.add(panelCenter, gc);

		panelTitle.add(lblTitle, BorderLayout.CENTER);
		add(panelTop, BorderLayout.CENTER);
		add(panelTitle, BorderLayout.NORTH);
	}

	// TODO
	public void initUI() {
		controller = new ControllerForPackageDetails();
		controllerb = new ControllerForBookingDetails();

		serviceListForTable = new ArrayList<Service>();

		packageTableModel = new PackageTableModel();
		tablePackage = new JTable(packageTableModel);

		Font f = CustomFont.setFont(tablePackage.getFont().toString(),
				Font.PLAIN, 15);

		tablePackage.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tablePackage.getTableHeader().setReorderingAllowed(false);
		tablePackage.getTableHeader().setResizingAllowed(true);
		tablePackage.setRowHeight(20);
		tablePackage.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablePackage.getTableHeader().setFont(f);
		tablePackage.setFont(f);

		TableColumnModel tcm = tablePackage.getColumnModel();
		tcm.getColumn(0).setPreferredWidth(250);
		tcm.getColumn(1).setPreferredWidth(150);
		tcm.getColumn(2).setPreferredWidth(400);

		serviceTableModel = new BookingPackagesForServicesTableModel();
		tableService = new CustomTableServices(serviceTableModel);

		panelTitle = new JPanel();
		panelTitle.setLayout(new BorderLayout());
		panelTitle.setPreferredSize(new Dimension(0, 100));
		panelTitle.setBackground(Color.BLACK);

		panelPackageTable = new JPanel();
		panelPackageTable.setLayout(new BorderLayout());
		panelPackageTable.setPreferredSize(getMaximumSize());

		panelServiceTable = new JPanel();
		panelServiceTable.setLayout(new BorderLayout());
		panelServiceTable.setPreferredSize(getMaximumSize());

		panelTop = new JPanel();
		panelTop.setLayout(new GridBagLayout());
		panelTop.setPreferredSize(getMaximumSize());
		panelTop.setBorder(BorderFactory.createEtchedBorder());
		panelTop.setBackground(CustomColor.bgColor());
		
		panelCenter = new JPanel();
		panelCenter.setLayout(new GridBagLayout());
		panelCenter.setPreferredSize(getMaximumSize());
		panelCenter.setBorder(BorderFactory.createEtchedBorder());

		panelButton = new JPanel();
		panelButton.setLayout(new FlowLayout());
		panelButton.setBackground(CustomColor.bgColor());
		
		panelButtona = new JPanel();
		panelButtona.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelButtona.setBackground(CustomColor.bgColor());

		lblTitle = new JLabel("Package and Services");
		CustomIcon ci = new CustomIcon();
		lblTitle.setIcon(ci.createIcon("/res/packagetop.png"));

		lblPackageName = new JLabel("Package name ");
		lblPackageCost = new JLabel("Package cost ");
		lblPackageDesc = new JLabel("Package info ");
		lblServiceName = new JLabel("Service name ");
		lblServiceCost = new JLabel("Service cost ");
		lblServiceDesc = new JLabel("Service info ");
		lblServiceCat = new JLabel("Category ");

		lblPackageName.setFont(f);
		lblPackageCost.setFont(f);
		lblPackageDesc.setFont(f);
		lblServiceName.setFont(f);
		lblServiceCost.setFont(f);
		lblServiceDesc.setFont(f);
		lblServiceCat.setFont(f);

		txtPackageName = new JTextField(20);
		txtPackageCost = new JTextField(20);
		txtPackageDesc = new JTextField(20);
		txtServiceName = new JTextField(20);
		txtServiceCost = new JTextField(20);
		txtServiceDesc = new JTextField(20);
		txtSearch = new JTextField(20);

		txtPackageCost.setText("" + totalPackageCost);

		txtPackageName.setFont(f);
		txtPackageCost.setFont(f);
		txtPackageDesc.setFont(f);
		txtServiceName.setFont(f);
		txtServiceCost.setFont(f);
		txtServiceDesc.setFont(f);
		txtSearch.setFont(f);

		btnAddPackage = new JButton("Create New",
				ci.createIcon("/res/save_add.png"));
		btnDelPackage = new JButton("Delete Package",
				ci.createIcon("/res/delete.png"));
		btnAddService = new JButton("Add Service",
				ci.createIcon("/res/add_to_package.png"));
		btnDelService = new JButton("Remove Service",
				ci.createIcon("/res/remove_from_package.png"));
		btnServiceList = new JButton("List of Services",
				ci.createIcon("/res/list.png"));
		btnCategoryList = new JButton("List of Category",
				ci.createIcon("/res/list.png"));
		btnSaveEdit = new JButton("Save Edited",
				ci.createIcon("/res/save_edit.png"));

		btnAddPackage.setBackground(CustomColor.bgColor());
		btnDelPackage.setBackground(CustomColor.bgColor());
		btnAddService.setBackground(CustomColor.bgColor());
		btnDelService.setBackground(CustomColor.bgColor());
		btnSaveEdit.setBackground(CustomColor.bgColor());
		btnServiceList.setBackground(CustomColor.bgColor());
		btnCategoryList.setBackground(CustomColor.bgColor());
		
		cboServiceList = new JComboBox<String>();
		cboServiceList.setFont(f);
		cboServiceList.setBackground(CustomColor.bgColor());

		cboServiceCat = new JComboBox<String>();
		cboServiceCat.setFont(f);
		cboServiceCat.setBackground(CustomColor.bgColor());

		popService = new JPopupMenu();
		mniDel = new JMenuItem("Remove");
		mniReprice = new JMenuItem("Reprice");
		popService.add(mniReprice);
		popService.add(mniDel);

		popPackage = new JPopupMenu();
		mniDuplicate = new JMenuItem("Copy services from Existing Package");
		popPackage.add(mniDuplicate);

		try {
			if(controllerb.connect().equals("ok")) {
				controllerb.loadAllServiceCategory();
				serviceCat = controllerb.getServiceCategory();
			} else {
				System.out.println(controllerb.connect());
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}

		timer = new Timer(1500, new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				refreshCboServiceList();
			}
		});

		timer2 = new Timer(1500, new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				refreshPackageList();
			}
		});

		DefaultComboBoxModel<String> es = new DefaultComboBoxModel<String>();
		if(serviceCat != null) {
			for (int i = 0; i < serviceCat.size(); i++) {
				ServiceCategory list = serviceCat.get(i);
				es.addElement(list.getServiceCategoryName());
			}
			
			cboServiceCat.setModel(es);
			cboServiceCat.setSelectedIndex(1);
		}

		refreshCboServiceList();

	}

	private void set(final Window parent) {
		setResizable(false);
		setLayout(new BorderLayout());
		setSize(800, 700);
		setLocationRelativeTo(parent);
		Image img = new ImageIcon(getClass().getResource("/res/mcicon.png")).getImage();
		setIconImage(img);
		setBackground(CustomColor.bgColor());
	}
}
