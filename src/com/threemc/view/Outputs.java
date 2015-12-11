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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableColumnModel;

import com.threemc.controller.ControllerForBookingDetails;
import com.threemc.controller.ControllerForEmployeeDetails;
import com.threemc.controller.ControllerForOutputDetails;
import com.threemc.data.Booking;
import com.threemc.data.Client;
import com.threemc.data.Employee;
import com.threemc.data.Output;

public class Outputs extends Dialog {

	// TODO
	private JPanel panelTitle;
	private JPanel panelCenter;
	private JPanel panelButton;
	private JPanel panelTableEvents;
	private JPanel panelTableOuts;
	private JPanel panellblEvs;
	private JPanel panellblOus;
	private JPanel panelCbo;

	private JLabel lblTitle;
	private JLabel lblOutstat;
	private JLabel lblOutputs;
	private JLabel lblSearch;
	private JLabel lblBystatus;
	private JLabel lblByEmp;
	
	private JComboBox<String> cboSearchType;
	private JComboBox<String> cboStatus;
	private JComboBox<String> cboStatusu;
	private JComboBox<String> cboPayment;
	private JComboBox<String> cboEmployee;
	
	private JTextField txtSearch;

	private JTextField txtOutputstat;

	private BookingEventInfoTableModel eventModel;
	private OutputTableModel outModel;

	private CustomTableEvent tableEvents;
	private JTable tableOuts;

	private JButton btnShow;
	private JButton btnClear;

	private CustomIcon ci = new CustomIcon();

	private ControllerForBookingDetails controllerb;
	private ControllerForEmployeeDetails controllere;
	private ControllerForOutputDetails controllero;

	private ArrayList<Booking> bookingList;
	private ArrayList<Employee> employeeList;
	private ArrayList<Output> outputList;

	private int client_id = 0;
	private int id = 0;

	public Outputs(final Window parent, final Dialog.ModalityType modal) {
		super(parent, "Outputs", modal);
		set(parent);
		initUI();
		layoutComponents();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				Outputs.this.dispose();
			}
		});

		tableEvents.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				refreshOutPutData();
			}
		});

		tableEvents.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent arg0) {
				refreshOutPutData();
			}
		});
		
		tableOuts.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int row = tableOuts.getSelectedRow();
				if(row != -1) {
					id = outputList.get(row).getId();
				}
			}
		});
		
		txtSearch.getDocument().addDocumentListener(new DocumentListener() {
			public void removeUpdate(DocumentEvent e) {
				search();
			}

			public void insertUpdate(DocumentEvent e) {
				search();
			}
			
			public void changedUpdate(DocumentEvent arg0) {
				search();
			}
		});
		
		btnShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = tableOuts.getSelectedRow();
				if(row != - 1) {
					OutputUpdates ou = new OutputUpdates(parent, modal);
					ou.setOutputID(outputList.get(row).getId());
					ou.setOutputName(outputList.get(row).getOutputName());
					ou.setOutputUpdate();
					ou.setVisible(true);
				}
			}
		});
		
		cboStatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cboStatusu.setSelectedIndex(0);
				cboPayment.setSelectedIndex(0);
				refreshTable();
			}
		});
		
		cboStatusu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cboPayment.setSelectedIndex(0);
				refreshTable();
			}
		});
		
		cboPayment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshTable();
			}
		});
		
		cboEmployee.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int row = cboEmployee.getSelectedIndex();
					if(row != -1) {
						int emp_id = employeeList.get(row).getId();
						if(controllero.connect().equals("ok")) {
							controllero.loadAllOutputById("employee", emp_id);
							outputList = controllero.getOutputs();
							setDataOutput(outputList);
							refreshOutputTable();
						} else {
							System.out.println(controllero.connect());
						}
					}
				} catch (Exception es) {
					es.printStackTrace();
				}
			}
		});
	}
	
	private void refreshTable() {
		try {
			if(controllerb.connect().equals("ok")) {
				String status = cboStatus.getSelectedItem().toString();
				String statusu = cboStatusu.getSelectedItem().toString();
				String payment = cboPayment.getSelectedItem().toString();
				if(status.equals("All")) {
					status = "";
				}
				if(statusu.equals("All")) {
					statusu = "";
				}
				if(payment.equals("All")) {
					payment = "";
				}
				controllerb.loadAllBookingRecord(status, statusu, payment);
				bookingList = controllerb.getBooking();
				tableEvents.setBookingList(bookingList);
			} else {
				System.out.println(controllerb.connect());
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private void refreshOutPutData() {
		try {
			int row = tableEvents.getSelectedRow();
			if(row != -1) {
				int event_id = bookingList.get(row).getId();
				if(controllero.connect().equals("ok")) {
					controllero.loadAllOutputById("event", event_id);
					outputList = controllero.getOutputs();
					setDataOutput(outputList);
					refreshOutputTable();
				} else {
					System.out.println(controllero.connect());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void refreshEventData() {
		try {
			if (controllerb.connect().equals("ok")) {
				//TODO arrange parameter to get the exact data wanted
				controllerb.loadAllBookingRecord("", "", "");
				bookingList = controllerb.getBooking();
				tableEvents.setBookingList(bookingList);
			} else {
				System.out.println(controllerb.connect());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void search() {
		String val = txtSearch.getText().toString();
		String cat = "";
		if (cboSearchType.getSelectedIndex() == 0) {
			cat = "e.event_title";
		} else if (cboSearchType.getSelectedIndex() == 1) {
			cat = "e.event_venue";
		} else if (cboSearchType.getSelectedIndex() == 2) {
			cat = "e.event_date";
		} else if (cboSearchType.getSelectedIndex() == 3) {
			cat = "e.event_type";
		}
		
		String status = cboStatus.getSelectedItem().toString();
		String statusu = cboStatusu.getSelectedItem().toString();
		String payment = cboPayment.getSelectedItem().toString();
		if(status.equals("All")) {
			status = "";
		}
		if(statusu.equals("All")) {
			statusu = "";
		}
		if(payment.equals("All")) {
			payment = "";
		}
		
		try {
			if(controllerb.connect().equals("ok")) {
				controllerb.searchAllBookingRecord(val, cat, status, statusu, payment);
				bookingList = controllerb.getBooking();
				tableEvents.setBookingList(bookingList);
			} else {
				System.out.println(controllerb.connect());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setDataOutput(ArrayList<Output> db) {
		outModel.setData(db);
	}

	private void refreshOutputTable() {
		outModel.fireTableDataChanged();
	}

	// TODO
	private void layoutComponents() {
		GridBagConstraints gc = new GridBagConstraints();

		Insets inset = new Insets(5, 5, 5, 5);

		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = inset;

		gc.gridy = 0;
		gc.gridx = 0;
		gc.gridwidth = 1;
		gc.fill = GridBagConstraints.HORIZONTAL;
		
		panellblEvs.add(lblSearch);
		panellblEvs.add(txtSearch);
		panellblEvs.add(cboSearchType);

		panelCenter.add(panellblEvs, gc);

		panelTableEvents.add(new JScrollPane(tableEvents,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));

		gc.gridy++;
		gc.gridx = 0;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.fill = GridBagConstraints.BOTH;
		panelCenter.add(panelTableEvents, gc);

		panellblOus.add(lblOutputs);

		gc.gridy++;
		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		panelCenter.add(panellblOus, gc);

		panelTableOuts.add(new JScrollPane(tableOuts,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));

		gc.gridy++;
		gc.gridx = 0;
		gc.weightx = 1;
		gc.weighty = 1;
		panelCenter.add(panelTableOuts, gc);
		
		panelCbo.add(lblBystatus);
		panelCbo.add(cboStatus);
		panelCbo.add(cboStatusu);
		panelCbo.add(cboPayment);
		panelCbo.add(lblByEmp);
		panelCbo.add(cboEmployee);
		
		gc.gridy++;
		gc.gridx = 0;
		gc.weighty = 0;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.insets = new Insets(0, 0, 0, 0);
		panelCenter.add(panelCbo, gc);

		panelButton.add(btnShow);
		panelButton.add(btnClear);

		gc.gridy++;
		gc.gridx = 0;
		panelCenter.add(panelButton, gc);

		panelTitle.add(lblTitle);
		add(panelTitle, BorderLayout.NORTH);
		add(new JScrollPane(panelCenter,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),
				BorderLayout.CENTER);
	}

	// TODO
	private void initUI() {
		controllerb = new ControllerForBookingDetails();
		controllere = new ControllerForEmployeeDetails();
		controllero = new ControllerForOutputDetails();

		Font f = CustomFont.setFont("Tahom", Font.PLAIN, 15);

		lblTitle = new JLabel("sad");
		lblTitle.setIcon(ci.createIcon("/res/outputstop.png"));
		
		lblSearch = new JLabel("Search: ");
		lblSearch.setFont(f);
		
		txtSearch = new JTextField(35);
		txtSearch.setFont(f);

		panelTitle = new JPanel();
		panelTitle.setLayout(new GridBagLayout());
		panelTitle.setPreferredSize(CustomDimension.setDimensionHeight(100));

		panelCenter = new JPanel();
		panelCenter.setLayout(new GridBagLayout());
		panelCenter.setBorder(BorderFactory.createEtchedBorder());
		panelCenter.setBackground(CustomColor.bgColor());

		panelTableEvents = new JPanel();
		panelTableEvents.setLayout(new BorderLayout());
		panelTableEvents.setPreferredSize(CustomDimension
				.setDimensionWidth(400));

		panelTableOuts = new JPanel();
		panelTableOuts.setLayout(new BorderLayout());
		panelTableOuts.setPreferredSize(CustomDimension.setDimensionWidth(400));

		panelButton = new JPanel();
		panelButton.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelButton.setBackground(CustomColor.bgColor());

		panellblEvs = new JPanel();
		panellblEvs.setLayout(new FlowLayout(FlowLayout.LEFT));
		panellblEvs.setBackground(CustomColor.bgColor());
		panellblEvs.setBorder(BorderFactory.createEtchedBorder());

		panellblOus = new JPanel();
		panellblOus.setLayout(new FlowLayout(FlowLayout.LEFT));
		panellblOus.setBackground(Color.WHITE);
		panellblOus.setBorder(BorderFactory.createEtchedBorder());
		
		panelCbo = new JPanel();
		panelCbo.setLayout(new FlowLayout(FlowLayout.LEFT));
//		panelCbo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
//		panelCbo.setBorder(BorderFactory.createTitledBorder(BorderFactory
//				.createCompoundBorder(
//						BorderFactory.createEmptyBorder(2, 2, 2, 2),
//						BorderFactory.createLineBorder(Color.BLACK)),
//				"Show by status ", TitledBorder.LEFT,
//				TitledBorder.DEFAULT_POSITION, f, Color.BLACK));
		panelCbo.setBackground(CustomColor.bgColor());

		lblOutstat = new JLabel("Status");
		lblOutputs = new JLabel("Output List For this Event");
		lblBystatus = new JLabel("Show by Status: ");
		lblByEmp = new JLabel("By Employee");

		lblOutstat.setFont(f);
		lblOutputs.setFont(f);
		lblBystatus.setFont(f);
		lblByEmp.setFont(f);

		txtOutputstat = new JTextField(20);

		txtOutputstat.setFont(f);

		btnShow = new JButton("Show updates");
		btnClear = new JButton("Clear");

		eventModel = new BookingEventInfoTableModel();
		tableEvents = new CustomTableEvent(eventModel);

		outModel = new OutputTableModel();
		tableOuts = new JTable(outModel);

		tableOuts.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableOuts.getTableHeader().setReorderingAllowed(false);
		tableOuts.getTableHeader().setResizingAllowed(true);
		tableOuts.getTableHeader().setFont(f);
		tableOuts.setFont(f);
		tableOuts.setRowHeight(20);
		tableOuts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		TableColumnModel tcms = tableOuts.getColumnModel();
		tcms.getColumn(0).setPreferredWidth(400);
		tcms.getColumn(1).setPreferredWidth(400);

		refreshEventData();

		cboSearchType = new JComboBox<String>();
		cboStatus = new JComboBox<String>();
		cboStatusu = new JComboBox<String>();
		cboPayment = new JComboBox<String>();
		cboEmployee = new JComboBox<String>();
		
		cboSearchType.setFont(f);
		cboStatus.setFont(f);
		cboStatusu.setFont(f);
		cboPayment.setFont(f);
		cboEmployee.setFont(f);
		
		DefaultComboBoxModel<String> ee = new DefaultComboBoxModel<String>();
		ee.addElement("Event Title");
		ee.addElement("Venue");
		ee.addElement("Date");
		ee.addElement("Type");
		
		cboSearchType.setModel(ee);
		cboSearchType.setSelectedIndex(0);
		
		DefaultComboBoxModel<String> es = new DefaultComboBoxModel<String>();
		es.addElement("All");
		es.addElement("Open");
		es.addElement("Close");
		
		cboStatus.setModel(es);
		
		DefaultComboBoxModel<String> ed = new DefaultComboBoxModel<String>();
		ed.addElement("All");
		ed.addElement("Ongoing");
		ed.addElement("Done");
		ed.addElement("Cancel");
		
		cboStatusu.setModel(ed);
		
		DefaultComboBoxModel<String> ef = new DefaultComboBoxModel<String>();
		ef.addElement("All");
		ef.addElement("Fully Paid");
		ef.addElement("Partial");
		ef.addElement("No Payment");
		
		cboPayment.setModel(ef);
		
		try {
			if(controllere.connect().equals("ok")) {
				controllere.loadAllEmployees();
				employeeList = controllere.getEmployee();
				DefaultComboBoxModel<String> eg = new DefaultComboBoxModel<String>();
				for (int i = 0; i < employeeList.size(); i++) {
					Employee emp = employeeList.get(i);
					eg.addElement(emp.getEmpLastName() + ", " + emp.getEmpFirstName() + " " + emp.getEmpFirstName());
				}
				cboEmployee.setModel(eg);
			} else {
				System.out.println(controllere.connect());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// TODO
	private void set(final Window parent) {
		setLayout(new BorderLayout());
		setSize(800, 700);
		setLocationRelativeTo(parent);
		setResizable(false);
		Image img = new ImageIcon(getClass().getResource("/res/mcicon.png"))
				.getImage();
		setIconImage(img);
		setBackground(CustomColor.bgColor());
	}
}
