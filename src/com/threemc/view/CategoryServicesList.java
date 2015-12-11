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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import com.threemc.controller.ControllerForBookingDetails;
import com.threemc.controller.ControllerForPackageDetails;
import com.threemc.data.ServiceCategory;
import com.threemc.data.ServiceList;

public class CategoryServicesList extends Dialog {

	private JLabel title;
	private JTextField txtName;
	private JLabel lblCatname;
	private JButton btnSave;
	private JButton btnClear;
	private JButton btnDelete;
	private JButton btnQue;

	private ArrayList<ServiceCategory> catserviceList;
	private CategoryServicesListTableModel model;
	private JTable table;

	private JPanel panelTable;
	private JPanel panelButton;
	private JPanel panelPack;

	private ControllerForBookingDetails controller;
	private ControllerForPackageDetails controllerp;
	private ArrayList<ServiceList> serviceList;

	private int id = 0;

	private final String message = "Deleting a Category will not affect the previous services that are saved in the database.\n"
			+ "Package Services and Services wanted for every event will still contain  the same category.\n"
			+ "These changes will affect future transaction for new events or package modification. You can\n"
			+ "update the Service List and Package service afterwards to avoid \"Uncategorized\" Services.";

	// TODO
	public CategoryServicesList(final Window parent, Dialog.ModalityType modal) {
		super(parent, "Services List - Category", modal);
		set(parent);
		initUI();
		layoutComponents();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				CategoryServicesList.this.dispose();
			}
		});

		loadFirstServicesList();

		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String catname = txtName.getText().toString();
				if (!catname.isEmpty()) {
					if (CustomPatternChecker
							.checkStringSomeCharsAllowed(catname)) {
						if (id > 0) {
							int ak = JOptionPane
									.showConfirmDialog(
											CategoryServicesList.this,
											"You are about to Update an Existin record, Do you want to proceed?",
											"Service List - Category",
											JOptionPane.YES_NO_OPTION,
											JOptionPane.INFORMATION_MESSAGE);
							if (ak == JOptionPane.YES_OPTION) {
								try {
									if (controller.connect().equals("ok")) {
										controller.saveCategory(getSc());
										loadFirst();
										JOptionPane
												.showMessageDialog(
														CategoryServicesList.this,
														"Saved ",
														"Service List - Category",
														JOptionPane.INFORMATION_MESSAGE);
									} else {
										System.out.println(controller.connect());
									}
								} catch (Exception e1) {
									e1.printStackTrace();
								}
							}
						} else {
							boolean b = true;
							for (int i = 0; i < catserviceList.size(); i++) {
								ServiceCategory sc = catserviceList.get(i);
								if (sc.getServiceCategoryName()
										.equalsIgnoreCase(catname)) {
									b = false;
									break;
								}
							}
							if (b) {
								try {
									if (controller.connect().equals("ok")) {
										controller.saveCategory(getSc());
										loadFirst();
										JOptionPane
												.showMessageDialog(
														CategoryServicesList.this,
														"Saved ",
														"Service List - Category",
														JOptionPane.INFORMATION_MESSAGE);
									} else {
										System.out.println(controller.connect());
									}
								} catch (Exception e1) {
									e1.printStackTrace();
								}
							} else {
								JOptionPane
										.showMessageDialog(
												CategoryServicesList.this,
												"Service name already exists, please type another name",
												"Service List - Category",
												JOptionPane.ERROR_MESSAGE);
							}
						}
					} else {
						JOptionPane.showMessageDialog(
								CategoryServicesList.this, "Invalid chars",
								"Service List - Category",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(CategoryServicesList.this,
							"Category name is empty",
							"Service List - Category",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				id = 0;
				txtName.setText("");
			}
		});

		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				int aok = JOptionPane.showConfirmDialog(
						CategoryServicesList.this, message
								+ "\n\n\nDo you want to Proceed?",
						"Service List - Category", JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE);

				if (aok == JOptionPane.YES_OPTION) {
					int id = catserviceList.get(table.getSelectedRow()).getId();
					ArrayList<ServiceList> sList = new ArrayList<ServiceList>();
					for (ServiceList s : serviceList) {
						if (s.getService_catId() == id) {
							s.setService_catId(10000);
							sList.add(s);
						}
					}
					String scname = catserviceList.get(table.getSelectedRow())
							.getServiceCategoryName();
					ServiceCategory sc = new ServiceCategory(id, scname,
							"Hidden");

					try {
						if (controller.connect().equals("ok")) {
							controller.saveCategory(sc);
						} else {
							System.out.println(controller.connect());
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}

					try {
						if(controllerp.connect().equals("ok")) {
							controllerp.addServiceLists(sList);
							controllerp.updateServiceList();
						} else {
							System.out.println(controllerp.connect());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					loadFirst();
				}
			}
		});

		btnQue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(CategoryServicesList.this,
						message, "Service List - Category",
						JOptionPane.WARNING_MESSAGE);
			}
		});

		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				id = catserviceList.get(table.getSelectedRow()).getId();
				txtName.setText(catserviceList.get(table.getSelectedRow())
						.getServiceCategoryName());
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
					c.setForeground(CustomColor.goldColor());
				} else {
					c.setBackground(Color.WHITE);
					c.setForeground(Color.BLACK);
				}
				return c;
			}
		});
	}

	private ServiceCategory getSc() {
		String catName = txtName.getText().toString();
		ServiceCategory sc = new ServiceCategory(id, catName, "Normal");
		return sc;
	}

	private void setData(ArrayList<ServiceCategory> db) {
		model.setData(db);
	}

	private void refresh() {
		model.fireTableDataChanged();
	}

	private void loadFirstServicesList() {
		try {
			if(controllerp.connect().equals("ok")) {
				controllerp.loadServiceList();
				serviceList = controllerp.getServiceList();
			} else {
				System.out.println(controllerp.connect());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// TODO
	private void layoutComponents() {

		panelButton.add(btnSave);
		panelButton.add(btnClear);
		panelButton.add(btnDelete);
		panelButton.add(btnQue);

		panelTable.add(new JScrollPane(table,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),
				BorderLayout.CENTER);

		GridBagConstraints gc = new GridBagConstraints();

		Insets inset = new Insets(2, 2, 2, 2);
		Insets inseta = new Insets(0, 0, 0, 0);

		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = inset;
		gc.fill = GridBagConstraints.HORIZONTAL;

		gc.gridy = 0;
		gc.gridx = 0;
		gc.gridwidth = 1;
		panelPack.add(lblCatname, gc);

		gc.gridx = 1;
		gc.weightx = 1;
		panelPack.add(txtName, gc);

		gc.gridy++;
		gc.gridx = 1;
		gc.insets = inseta;
		panelPack.add(panelButton, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.weighty = 1;
		gc.gridwidth = 2;
		gc.insets = inset;
		gc.fill = GridBagConstraints.BOTH;
		panelPack.add(panelTable, gc);

		add(title, BorderLayout.NORTH);
		add(panelPack, BorderLayout.CENTER);
	}

	// TODO
	private void initUI() {
		controller = new ControllerForBookingDetails();
		controllerp = new ControllerForPackageDetails();

		Font f = CustomFont.setFont("Tahoma", Font.PLAIN, 15);
		Font f2 = CustomFont.setFont("Tahoma", Font.PLAIN, 13);

		panelButton = new JPanel();
		panelButton.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelButton.setBackground(CustomColor.bgColor());

		panelTable = new JPanel();
		panelTable.setLayout(new BorderLayout());

		panelPack = new JPanel();
		panelPack.setLayout(new GridBagLayout());
		panelPack.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		panelPack.setBackground(CustomColor.bgColor());

		txtName = new JTextField(20);
		txtName.setFont(f);

		lblCatname = new JLabel("Category Name:");
		lblCatname.setFont(f);

		title = new JLabel("Manage Category for Services", SwingConstants.LEFT);
		title.setFont(CustomFont.setFont("Tahoma", Font.PLAIN, 24));
		title.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

		CustomIcon ci = new CustomIcon();

		btnSave = new JButton("Save", ci.createIcon("/res/save.png"));
		btnClear = new JButton("Clear", ci.createIcon("/res/clear.png"));
		btnDelete = new JButton("Delete", ci.createIcon("/res/delete.png"));
		btnQue = new JButton("", ci.createIcon("/res/question.png"));

		btnSave.setFont(f2);
		btnClear.setFont(f2);

		model = new CategoryServicesListTableModel();
		table = new JTable(model);

		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(true);
		table.setRowHeight(20);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFont(f);
		table.getTableHeader().setFont(f);

		TableColumnModel tcm = table.getColumnModel();
		tcm.getColumn(0).setPreferredWidth(250);

		loadFirst();
	}

	private void loadFirst() {
		try {
			if (controller.connect().equals("ok")) {
				controller.loadAllServiceCategory();
				catserviceList = controller.getServiceCategory();
				setData(catserviceList);
				refresh();
			} else {
				System.out.println(controller.connect());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// TODO
	private void set(final Window parent) {
		setLayout(new BorderLayout());
		setSize(550, 650);
		setResizable(false);
		setLocationRelativeTo(parent);
		Image img = new ImageIcon(getClass().getResource("/res/mcicon.png"))
				.getImage();
		setIconImage(img);
		setBackground(CustomColor.bgColor());
	}
}
