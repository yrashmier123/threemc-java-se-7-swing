package com.threemc.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import com.threemc.controller.ControllerForBookingDetails;
import com.threemc.controller.ControllerForPackageDetails;
import com.threemc.data.ServiceCategory;
import com.threemc.data.ServiceList;

public class ServicesLists extends Dialog {

	// TODO
	private JLabel title;
	private JLabel lblServiceName;
	private JLabel lblServiceCost;
	private JLabel lblServiceDesc;
	private JLabel lblServiceCat;

	private JTextField txtServiceName;
	private JTextField txtServiceCost;
	private JTextField txtServiceDesc;

	private JPanel panelPack;
	private JPanel panelButton;
	private JButton btnSave;
	private JButton btnClear;
	private JButton btnDel;
	private JButton btnRefresh;

	private JComboBox<String> cboServiceCat;

	private ControllerForPackageDetails controllerp;
	private ControllerForBookingDetails controllerb;
	private ServiceListsTableModel tableModel;
	private JTable table;

	private int id = 0;
	private Color colo = Color.decode("#ffd700");
	private String InfoMessage = "";
	private ArrayList<ServiceCategory> serviceCat;
	private ArrayList<ServiceList> serviceList;

	// TODO
	public ServicesLists(final Window parent, final Dialog.ModalityType modal) {
		super(parent, "List of services", modal);
		set(parent);
		initUI();
		layoutComponent();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				ServicesLists.this.dispose();
			}
		});

		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clearTextbox();
			}
		});

		btnDel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					int okcancel = JOptionPane.showConfirmDialog(
							ServicesLists.this,
							"Are you sure you want to Delete this Service?",
							"Service List", JOptionPane.YES_NO_OPTION,
							JOptionPane.INFORMATION_MESSAGE);

					if (okcancel == JOptionPane.YES_OPTION) {
						if(controllerp.connect().equals("ok")) {
							controllerp.deleteServiceList(id);
							controllerp.loadServiceList();
							setData(controllerp.getServiceList());
							refresh();
						} else {
							System.out.println(controllerp.connect());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					boolean b = true;
					for (int i = 0; i < serviceList.size(); i++) {
						ServiceList s = serviceList.get(i);
						if (s.getServiceName().equalsIgnoreCase(
								txtServiceName.getText().toString())) {
							b = false;
							break;
						}
					}
					if (id == 0) {
						if (b) {
							if(controllerp.connect().equals("ok")) {
								controllerp.addServiceList(getServiceInfo());
								controllerp.saveServiceList();
								controllerp.loadServiceList();
								setData(controllerp.getServiceList());
								refresh();
								clearTextbox();
								JOptionPane.showMessageDialog(ServicesLists.this,
										"Successfully Saved Services Information",
										"Service List",
										JOptionPane.INFORMATION_MESSAGE);
							} else {
								System.out.println(controllerp.connect());
							}
						} else {
							JOptionPane
									.showMessageDialog(
											ServicesLists.this,
											"Service name already exists, please type another name",
											"Service List",
											JOptionPane.ERROR_MESSAGE);
						}
					} else if (id > 0) {
						int okcancel = JOptionPane
								.showConfirmDialog(
										ServicesLists.this,
										"You are about to update an existing Service. \nDo you want to Proceed?",
										"Service List",
										JOptionPane.YES_NO_OPTION,
										JOptionPane.INFORMATION_MESSAGE);
						if (okcancel == JOptionPane.YES_OPTION) {
							if(controllerp.connect().equals("ok")) {
								controllerp.addServiceList(getServiceInfo());
								controllerp.saveServiceList();
								controllerp.loadServiceList();
								setData(controllerp.getServiceList());
								refresh();
								clearTextbox();
							} else {
								System.out.println(controllerp.connect());
							}
						}
					}
				} catch (NullPointerException e1) {
					JOptionPane.showMessageDialog(ServicesLists.this,
							"Please fill up the form properly.", "Error",
							JOptionPane.ERROR_MESSAGE);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		loadFirst();

		table.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
				tableToTextbox();
			}
		});

		table.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				super.keyReleased(e);
				tableToTextbox();
			}
		});

		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				final Component c = super.getTableCellRendererComponent(table,
						value, isSelected, hasFocus, row, column);
				if (isSelected) {
					c.setBackground(Color.BLACK);
					c.setForeground(colo);
				} else {
					c.setBackground(Color.WHITE);
					c.setForeground(Color.BLACK);
				}
				return c;
			}
		});

		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadFirst();
			}
		});
	}
	
	private void refreshServiceListTable() {
		try {
			if(controllerp.connect().equals("ok")) {
				controllerp.loadServiceList();
			} else {
				System.out.println(controllerp.connect());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayList<ServiceList> ss = new ArrayList<ServiceList>();
		for (int i = 0; i < controllerp.getServiceList().size(); i++) {
			ServiceList s = controllerp.getServiceList().get(i);
			if (cboServiceCat.getSelectedItem().toString()
					.equals(s.getServiceCategory())) {
				ss.add(s);
			}
		}
		setData(ss);
		refresh();
	}

	private void loadFirst() {
		try {
			if(controllerp.connect().equals("ok")) {
				controllerp.loadServiceList();
				serviceList = controllerp.getServiceList();
				setData(serviceList);
				refresh();
			} else {
				System.out.println(controllerp.connect());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// TODO
	public ServiceList getServiceInfo() {
		ServiceList serv = null;

		String serviceName = txtServiceName.getText();
		String serviceDesc = txtServiceDesc.getText();
		String serviceCost = txtServiceCost.getText();
		int serv_catId = serviceCat.get(cboServiceCat.getSelectedIndex())
				.getId();

		if (!serviceName.isEmpty()
				&& CustomPatternChecker
						.checkStringSomeCharsAllowed(serviceName)) {
			if (!serviceDesc.isEmpty()
					&& CustomPatternChecker
							.checkStringSomeCharsAllowed(serviceDesc)) {
				if (CustomPatternChecker.checkStringNumbersOnly(serviceCost)) {
					serv = new ServiceList(id, serviceName,
							Integer.parseInt(serviceCost), serviceDesc,
							serv_catId);
					return serv;
				}
			}
		}
		return null;
	}

	public void clearTextbox() {
		txtServiceName.setText("");
		txtServiceCost.setText("");
		txtServiceDesc.setText("");
		id = 0;
	}

	public void tableToTextbox() {
		int row = table.getSelectedRow();
		if(row != -1) {
			ServiceList serv = controllerp.getServiceList().get(row);
			id = serv.getId();
			txtServiceName.setText(tableModel.getValueAt(row, 0).toString());
			txtServiceCost.setText(tableModel.getValueAt(row, 2).toString());
			txtServiceDesc.setText(tableModel.getValueAt(row, 3).toString());
			cboServiceCat.setSelectedItem(tableModel.getValueAt(row, 1).toString());
		}
	}

	private void setData(List<ServiceList> db) {
		tableModel.setData(db);
	}

	private void refresh() {
		tableModel.fireTableDataChanged();
	}

	// TODO
	public void layoutComponent() {
		GridBagConstraints gc = new GridBagConstraints();

		Insets inset = new Insets(2, 2, 2, 2);

		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = inset;
		gc.fill = GridBagConstraints.BOTH;

		gc.gridy = 0;
		gc.gridx = 0;

		panelPack.add(lblServiceName, gc);

		gc.gridx = 1;
		gc.weightx = 1;

		panelPack.add(txtServiceName, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.weightx = 0;

		panelPack.add(lblServiceDesc, gc);

		gc.gridx = 1;
		gc.weightx = 1;

		panelPack.add(txtServiceDesc, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.weightx = 0;

		panelPack.add(lblServiceCost, gc);

		gc.gridx = 1;
		gc.weightx = 1;

		panelPack.add(txtServiceCost, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.weightx = 0;

		panelPack.add(lblServiceCat, gc);

		gc.gridx = 1;
		gc.weightx = 1;

		panelPack.add(cboServiceCat, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.weightx = 1;
		gc.gridwidth = 2;
		
		panelButton.add(btnSave);
		panelButton.add(btnClear);
		panelButton.add(btnDel);
		panelButton.add(btnRefresh);

		panelPack.add(panelButton, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.weighty = 1;

		panelPack.add(new JScrollPane(table,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), gc);

		add(title, BorderLayout.NORTH);
		add(panelPack, BorderLayout.CENTER);
	}

	// TODO
	public void initUI() {
		controllerp = new ControllerForPackageDetails();
		controllerb = new ControllerForBookingDetails();

		title = new JLabel("Manage List of Services", SwingConstants.LEFT);
		title.setFont(CustomFont.setFont("Tahoma", Font.PLAIN, 24));
		title.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

		Font f = CustomFont.setFont("Tahoma", Font.PLAIN, 15);
		Font f2 = CustomFont.setFont("Tahoma", Font.PLAIN, 13);

		lblServiceName = new JLabel("Service Name ");
		lblServiceCost = new JLabel("Service Cost ");
		lblServiceDesc = new JLabel("Service Description ");
		lblServiceCat = new JLabel("Service Category ");

		lblServiceName.setFont(f);
		lblServiceCost.setFont(f);
		lblServiceDesc.setFont(f);
		lblServiceCat.setFont(f);

		txtServiceName = new JTextField(20);
		txtServiceCost = new JTextField(20);
		txtServiceDesc = new JTextField(20);

		txtServiceName.setFont(f);
		txtServiceCost.setFont(f);
		txtServiceDesc.setFont(f);

		panelPack = new JPanel();
		panelPack.setLayout(new GridBagLayout());
		panelPack.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		panelPack.setBackground(CustomColor.bgColor());

		panelButton = new JPanel();
		panelButton.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelButton.setBackground(CustomColor.bgColor());

		tableModel = new ServiceListsTableModel();
		table = new JTable(tableModel);

		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(true);
		table.setRowHeight(20);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFont(f);
		table.getTableHeader().setFont(f);

		TableColumnModel tcm = table.getColumnModel();
		tcm.getColumn(0).setPreferredWidth(250);
		tcm.getColumn(2).setPreferredWidth(200);
		tcm.getColumn(3).setPreferredWidth(400);

		CustomIcon ci = new CustomIcon();
		
		btnSave = new JButton("Save", ci.createIcon("/res/save.png"));
		btnDel = new JButton("Delete", ci.createIcon("/res/delete.png"));
		btnClear = new JButton("Clear", ci.createIcon("/res/clear.png"));
		btnRefresh = new JButton("Refresh", ci.createIcon("/res/refresh.png"));
		
		btnSave.setFont(f2);
		btnDel.setFont(f2);
		btnClear.setFont(f2);
		btnRefresh.setFont(f2);

		cboServiceCat = new JComboBox<String>();
		cboServiceCat.setFont(f);

		try {
			if(controllerb.connect().equals("ok")) {
				controllerb.loadAllServiceCategory();
				serviceCat = controllerb.getServiceCategory();
			} else {
				System.out.println(controllerb.connect());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		DefaultComboBoxModel<String> ee = new DefaultComboBoxModel<String>();
		if(serviceCat != null) {
			for (ServiceCategory serviceCategory : serviceCat) {
				ee.addElement(serviceCategory.getServiceCategoryName());
			}
		}
		
		cboServiceCat.setModel(ee);
	}

	// TODO
	public void set(final Window parent) {
		setResizable(false);
		setSize(550, 650);
		setLayout(new BorderLayout());
		setLocationRelativeTo(parent);
		Image img = new ImageIcon(getClass().getResource("/res/mcicon.png"))
				.getImage();
		setIconImage(img);
		setBackground(CustomColor.bgColor());
	}
}
